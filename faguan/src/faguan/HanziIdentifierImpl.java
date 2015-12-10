package faguan;


public class HanziIdentifierImpl implements HanziIdentifier {

	private static long[] _bitMaskArray = { 1L<<23,
		1L<<22,
		1L<<21,
		1L<<20,
		1L<<19,
		1L<<18,
		1L<<17,
		1L<<16,
		1L<<15,
		1L<<14,
		1L<<13,
		1L<<12,
		1L<<11,
		1L<<10,
		1L<<9,
		1L<<8,
		1L<<7,
		1L<<6,
		1L<<5,
		1L<<4,
		1L<<3,
		1L<<2,
		1L<<1,
		1L};

	
	private Hanzi[] _hanziku;
	
	@Override
	public int initHanziku() {
		HanzikuBuilder builder = new HanzikuBuilder();
		builder.addZiku("hanziku.txt");
		_hanziku = new Hanzi[builder.getHanziku().size()];
		builder.getHanziku().values().toArray(_hanziku);
		return _hanziku.length;
	}

	@Override
	public IdentifyResult identify(Hanzi hanzi) {
		Hanzi result = new Hanzi();
		int deltaMin = 9999;
		int pos = 0;
		for(int i = 0; i < _hanziku.length; i++) {
			Hanzi hz = _hanziku[i];
			int delta = computeDelta(hz, hanzi, deltaMin);
			if(delta < deltaMin) {
				pos = i;
				deltaMin = delta;
				result.setWenzi(hz.getWenzi());
				result.setBitMap(hz.getFormalBitMap());
				if(deltaMin == 0) {
					break;
				}
			}
		}

		return new IdentifyResult(result, deltaMin, pos);
	}
	
	
	/**
	 * Compute delta of two hanzi
	 * delta = Sum(line_delta);
	 * line_delta = xor(line of two hanzi),split with 0,sum(parts.len^2);
	 * such as xor=110110111011000000, line_delta=21
	 * @param hanzi1
	 * @param hanzi2
	 * @return
	 */
	private static int computeDelta(Hanzi hanzi1, Hanzi hanzi2){
		int delta = 0;
		Long[] bitMap1 = hanzi1.getFormalBitMap();
		Long[] bitMap2 = hanzi2.getFormalBitMap();
		
		for (int i = 0; i < 24; i++) {
			Long lineXor = bitMap1[i] ^ bitMap2[i];
//			System.out.println(Long.toBinaryString(lineXor));
			int previouseBitCount = 0;
			for(int j = 0; j < 24; j++) {
				if((lineXor & _bitMaskArray[j]) > 0) {
					previouseBitCount++;
				} else if(previouseBitCount > 0) {
					delta += previouseBitCount * previouseBitCount;
					previouseBitCount = 0;
				}
			}
			if(previouseBitCount > 0) {  //最后一位是1的情况
				delta += previouseBitCount * previouseBitCount;
			}
//			System.out.println(delta);
		}
		
		return delta;
	}
	
	private static int computeDelta(Hanzi hanzi1, Hanzi hanzi2, int deltaLimit){
		int delta = 0;
		Long[] bitMap1 = hanzi1.getFormalBitMap();
		Long[] bitMap2 = hanzi2.getFormalBitMap();
		
		for (int i = 0; i < 24; i++) {
			Long lineXor = bitMap1[i] ^ bitMap2[i];
//			System.out.println(Long.toBinaryString(lineXor));
			int previouseBitCount = 0;
			for(int j = 0; j < 24; j++) {
				if((lineXor & _bitMaskArray[j]) > 0) {
					previouseBitCount++;
				} else if(previouseBitCount > 0) {
					delta += previouseBitCount * previouseBitCount;
					previouseBitCount = 0;
				}
			}
			if(previouseBitCount > 0) {  //最后一位是1的情况
				delta += previouseBitCount * previouseBitCount;
			}
			
			//for efficiency
			if(delta > deltaLimit) {
				break;
			}
//			System.out.println(delta);
		}
		
		return delta;
	}
}
