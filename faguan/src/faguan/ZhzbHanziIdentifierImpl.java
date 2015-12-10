package faguan;

import java.util.LinkedHashMap;


public class ZhzbHanziIdentifierImpl implements HanziIdentifier {

	private ZhzbHanzi[] _hanziku;
	private int _bitCountDelta = 35;
	
	@Override
	public int initHanziku() {
		ZikuReader builder = new ZikuReader();
		LinkedHashMap<String, ZhzbHanzi> ziku = builder.readZikuFile("ziku.txt");
		_hanziku = new ZhzbHanzi[ziku.size()];
		ziku.values().toArray(_hanziku);
		return _hanziku.length;
	}

	@Override
	public IdentifyResult identify(Hanzi hanzi) {
		Hanzi result = new Hanzi();
		int deltaMin = 9999;
		int pos = 0;
		int[] bitCounts = hanzi.getBitCounts();
		int totalBits = getTotalBits(bitCounts);
		
		// Use totalBits to binary search for proper start and end position in order to reduce search time.
		// _hanziku should be ordered.

		int startTotalBits = Math.max(totalBits - _bitCountDelta,  0); 
		int startPos = binary(_hanziku, startTotalBits);
		int endTotalBits = Math.min(totalBits + _bitCountDelta,  24*24); 
		int endPos = binary(_hanziku, endTotalBits);
		
		System.out.println("["+startPos+".."+endPos+"], size:" + (endPos-startPos+1) +", Searching...");
		for(int i = startPos; i <= endPos; i++) {
			int delta = computeDelta(bitCounts, _hanziku[i], deltaMin);
			if(delta < deltaMin) {
				pos = i;
				deltaMin = delta;
				result.setWenzi(_hanziku[i].getWenzi());
				if(deltaMin == 0) {
					break;
				}
			}
		}

		return new IdentifyResult(result, deltaMin, pos);
	}
	
	private int computeDelta(int[] bitCounts, ZhzbHanzi zhzbHz, int deltaMin) {
		int[] hzBitCount = zhzbHz.getBitCount();
		int delta = 0;
		for (int i = 0; i < 48; i++) {
			delta += (hzBitCount[i] - bitCounts[i]) * (hzBitCount[i] - bitCounts[i]);
			if(delta > deltaMin) {
				break;
			}
		}
		return delta;
	}
	
	public static int binary(ZhzbHanzi[] ziku, int value) {
		int low = 0;
		int high = ziku.length - 1;
		while (low <= high) {
			int middle = (low + high) / 2;
			int middleTotalBits = ziku[middle].getTotalBits();
			if (value == middleTotalBits) {
				return middle;
			}
			if (value > middleTotalBits) {
				low = middle + 1;
			}
			if (value < middleTotalBits) {
				high = middle - 1;
			}
		}
		return low;
	}
	
	public int getTotalBits(int bitCounts[]) {
		int totalBits = 0;
		for(int i = 0; i < 24; i++) {
			totalBits += bitCounts[i];
		}
		return totalBits;
	}
}
