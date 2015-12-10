package faguan;

import java.math.BigInteger;

public class Hanzi {

	private String _wenzi;
	private Long[] _bitMap = new Long[24];
	private Long[] _formalBitMap;
	private boolean _simple = false;
	
	public Hanzi() {
		
	}
	
	public Hanzi(String wenzi, Long[] bitMap) {
		_wenzi = wenzi;
		_bitMap = bitMap;
	}
	
	public Hanzi(String wenzi, Long[] bitMap, boolean simple) {
		_wenzi = wenzi;
		_bitMap = bitMap;
		_simple = simple;
	}


	public Hanzi(String wenzi, String bitMapString) {
		_wenzi = wenzi;
		String[] bitMapStr = bitMapString.split(";");
		for(int i = 0; i < _bitMap.length; i++) {
			_bitMap[i] = Long.parseLong(bitMapStr[i], 16);
		}
	}



	public String getWenzi() {
		return _wenzi;
	}

	public void setWenzi(String wenzi) {
		this._wenzi = wenzi;
	}

	public Long[] getBitMap() {
		return _bitMap;
	}

	public void setBitMap(Long[] bitMap) {
		this._bitMap = bitMap;
	}

	public String getBitMapString(boolean formal) {
		Long[] bitMap = formal? getFormalBitMap() : _bitMap;
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bitMap.length; i++) {
			sb.append(Long.toHexString(bitMap[i]) + ";");
		}
		return sb.toString();
	}
	
	public String getBitMapString() {
		return getBitMapString(true);
	}
	
	public String getDisplayString() {
		StringBuilder displayMap = new StringBuilder();
		int colMax = isWide()? 47: 23;
		
		for (int i = 0; i < _bitMap.length; i++) {
			for (int j = colMax; j>=0; j--)
			{
				if((_bitMap[i] & (1L<<j)) != 0) {
					if(isWide()){
						displayMap.append("■");
					} else {
						displayMap.append("■■");
					}
				} else {
					if(isWide()){
						displayMap.append(" ");
					} else {
						displayMap.append("  ");
					}
				}
			}
			displayMap.append("\n");
		}
		return displayMap.toString();
	}
	
	public String getFormalDisplayString() {
		Long[] bitMap = getFormalBitMap();
		StringBuilder displayMap = new StringBuilder();
		
		for (int i = 0; i < bitMap.length; i++) {
			for (int j = 23; j>=0; j--)
			{
				if((bitMap[i] & (1L<<j)) != 0) {
					displayMap.append("■");
				} else {
					displayMap.append(" ");
				}
			}
			displayMap.append("\n");
		}
		return displayMap.toString();
	}
	
	public String getBitCountString() {
		//for zhzb robot
//		if(!_simple) {
//			return "";
//		}
		
		int[] bitCounts = getBitCounts();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bitCounts.length; i++) {
			sb.append(bitCounts[i] + ",");
		}

		return sb.toString();
	}

	public int[] getBitCounts() {
		int[] bitCounts = new int[48];
		Long[] bitMap = getFormalBitMap();

		for (int i = 0; i < 24; i++) {
			bitCounts[i] = 0;
			Long row = bitMap[i];
			for(int j = 0; j < 24; j++) {
				if((row & (1L<<(23-j))) != 0) {
					bitCounts[i]++;
					bitCounts[24 + j]++;
				}
			}
		}
		
		return bitCounts;
	}
	
	
	// Get 24*24 bitmap
	public Long[] getFormalBitMap() {
		if(_formalBitMap == null) {
			_formalBitMap = buildFormalBitMap();
		}
		
		return _formalBitMap;
	}

	private Long[] buildFormalBitMap() {
		Long[] formalBitMap = new Long[24];
		boolean isWide = isWide();

		for (int i = 0; i < 24; i++) {
			Long line = _bitMap[i];
			formalBitMap[i] = isWide? getBrief(line) : line;
		}
		
		// if need reverse 0/1, then reverse
		int firstColBitCount = 0;
		for(Long line : formalBitMap) {
			firstColBitCount += line >> 23;
		}
		if(firstColBitCount > 12) {
			Long mask = Long.parseLong("111111111111111111111111", 2);
			for (int i = 0; i < 24; i++) {
				Long line = formalBitMap[i];
				formalBitMap[i] = (~line) & mask;
			}
		}
		
		// remove single point in bitMap
		clearSinglePoint(formalBitMap);
		
		return formalBitMap;
	}

	private Long getBrief(Long line) {
		String lineStr = Long.toBinaryString(line);
		String briefLineStr = lineStr.replaceAll("00", "0").replaceAll("11", "1");
		return Long.parseLong(briefLineStr, 2);
	}

	private boolean isWide() {
		Long halfLong = 1L << 24;
		for (Long line : _bitMap) {
			if (line > halfLong) {
				return true;
			}
		}
		
		return false;
	}
	
	
	// 对应zhzb机器中的 answer.AnswerTask.qzcl(String[])
	private void clearSinglePoint(Long[] formalBitMap){
		for(int i = 0; i < formalBitMap.length; i++) {
			for(int j = 0; j < 24; j++) {
				if(isSinglePoint(formalBitMap, i, j)){
					//set bit to 0
					Long mask = ~(1L << (23-j));
					formalBitMap[i] = formalBitMap[i] & mask;
				}
			}
		}
	}

	// 单点或连续的两点被认为是单点
	private boolean isSinglePoint(Long[] formalBitMap, int i, int j) {
//		boolean single = false;
		if(countDd(formalBitMap, i, j) == 0) {
			return false;
		}
		
		int countAround = countAroundPoint(formalBitMap, i, j);
		if(countAround < 1){ //single point
			return true;
		}
        
		// check two point
		boolean twoPoint = false;
		if (countAround == 1)
        {
			if (!twoPoint) {
				twoPoint = isZd(formalBitMap, i - 1, j - 1);
			}
			if (!twoPoint) {
				twoPoint = isZd(formalBitMap, i - 1, j);
			}
			if (!twoPoint) {
				twoPoint = isZd(formalBitMap, i - 1, j + 1);
			}
			if (!twoPoint) {
				twoPoint = isZd(formalBitMap, i, j - 1);
			}
			if (!twoPoint) {
				twoPoint = isZd(formalBitMap, i, j + 1);
			}
			if (!twoPoint) {
				twoPoint = isZd(formalBitMap, i + 1, j - 1);
			}
			if (!twoPoint) {
				twoPoint = isZd(formalBitMap, i + 1, j);
			}
			if (!twoPoint) {
				twoPoint = isZd(formalBitMap, i + 1, j + 1);
			}
        }
		
		return twoPoint;
	}
	
	public static boolean isZd(Long[] cc, int i, int j) {
		return (countDd(cc, i, j) == 1) && (countAroundPoint(cc, i, j) == 1);
	}
	
	public static int countAroundPoint(Long[] formalBitMap, int i, int j)
	{
	    return countDd(formalBitMap, i - 1, j - 1) + countDd(formalBitMap, i - 1, j) + countDd(formalBitMap, i - 1, j + 1) + countDd(formalBitMap, i, j - 1) + countDd(formalBitMap, i, j + 1) + countDd(formalBitMap, i + 1, j - 1) + countDd(formalBitMap, i + 1, j) + countDd(formalBitMap, i + 1, j + 1);
	}

	public static boolean getYx(int i, int j) {
		return ((i >= 0) && (i < 24)) && ((j >= 0) && (j < 24));
	}

	public static int countDd(Long[] formalBitMap, int i, int j) {
		if (getYx(i, j)) {
			Long mask = 1L << (23 - j);
			if ((formalBitMap[i] & mask) > 0) {
				return 1;
			}
		}
		
		return 0;
	}
	  
}
