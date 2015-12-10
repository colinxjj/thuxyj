package wabao;


public class TreasureMapHanzi {
	private String _wenzi;
	public static final int RowSize = 24;
	public static final int ColSize = 48;
	private Long[] _bitMap = new Long[RowSize];
	private Long[] _formalBitMap;
	private static final char _pointChar = '●';
	
	public TreasureMapHanzi() {
	}
	
	public TreasureMapHanzi(String wenzi, long[] bitMap) {
		_wenzi = wenzi;
		for(int i = 0; i < bitMap.length; i++) {
			_bitMap[i] = bitMap[i];
		}
		for(int i = bitMap.length; i < _bitMap.length; i++) {
			_bitMap[i] = 0L;
		}
	}
	
	public TreasureMapHanzi(String wenzi, Long[] bitMap) {
		_wenzi = wenzi;
		_bitMap = bitMap;
	}
	
	public TreasureMapHanzi(String wenzi, String bitMapString) {
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

	public String getBitMapString() {
		Long[] bitMap = _bitMap;
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bitMap.length; i++) {
			sb.append(Long.toHexString(bitMap[i]) + ";");
		}
		return sb.toString();
	}
	
	
	public String getDisplayString() {
		StringBuilder displayMap = new StringBuilder();
		
		for (int i = 0; i < _bitMap.length; i++) {
			for (int j = ColSize - 1; j>=0; j--)
			{
				if((_bitMap[i] & (1L<<j)) != 0) {
					displayMap.append(_pointChar);
				} else {
					displayMap.append(" ");
				}
			}
			displayMap.append("\n");
		}
		return displayMap.toString();
	}
	
	
	public String getBitCountString() {
		int[] bitCounts = getBitCounts();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bitCounts.length; i++) {
			sb.append(bitCounts[i] + ",");
		}

		return sb.toString();
	}

	public int[] getBitCounts() {
		int[] bitCounts = new int[RowSize + ColSize];
		Long[] bitMap = getFormalBitMap();

		for (int i = 0; i < RowSize; i++) {
			bitCounts[i] = 0;
			Long row = bitMap[i];
			for(int j = 0; j < ColSize; j++) {
				if((row & (1L<<(ColSize-1-j))) != 0) {
					bitCounts[i]++;
					bitCounts[RowSize + j]++;
				}
			}
		}
		
		return bitCounts;
	}
	
	
	// Get 24*48 bitmap, remove single point
	public Long[] getFormalBitMap() {
		if(_formalBitMap == null) {
			_formalBitMap = buildFormalBitMap();
		}
		
		return _formalBitMap;
	}

	private Long[] buildFormalBitMap() {
		Long[] formalBitMap = new Long[24];

		for (int i = 0; i < RowSize; i++) {
			formalBitMap[i] = _bitMap[i];
		}
		
		// if need reverse 0/1, then reverse
		int firstColBitCount = 0;
		for(Long line : formalBitMap) {
			firstColBitCount += line >> (ColSize - 1);
		}
		
		if(firstColBitCount > (RowSize/2)) {
			long mask = 0xFFFFFFFFFFFFl; // 48个1
			for (int i = 0; i < RowSize; i++) {
				Long line = formalBitMap[i];
				formalBitMap[i] = (~line) & mask;
			}
		}
		
		// remove single point in bitMap
		clearSinglePoint(formalBitMap);
		
		return formalBitMap;
	}

	
	// 对应zhzb机器中的 answer.AnswerTask.qzcl(String[])
	private void clearSinglePoint(Long[] formalBitMap){
		for(int i = 0; i < formalBitMap.length; i++) {
			for(int j = 0; j < ColSize; j++) {
				if(isSinglePoint(formalBitMap, i, j)){
					//set bit to 0
					Long mask = ~(1L << (ColSize-1-j));
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
	
	private boolean isZd(Long[] cc, int i, int j) {
		return (countDd(cc, i, j) == 1) && (countAroundPoint(cc, i, j) == 1);
	}
	
	private int countAroundPoint(Long[] formalBitMap, int i, int j)
	{
	    return countDd(formalBitMap, i - 1, j - 1) + countDd(formalBitMap, i - 1, j) + countDd(formalBitMap, i - 1, j + 1) + countDd(formalBitMap, i, j - 1) + countDd(formalBitMap, i, j + 1) + countDd(formalBitMap, i + 1, j - 1) + countDd(formalBitMap, i + 1, j) + countDd(formalBitMap, i + 1, j + 1);
	}

	private boolean getYx(int i, int j) {
		return ((i >= 0) && (i < RowSize)) && ((j >= 0) && (j < ColSize));
	}

	private int countDd(Long[] formalBitMap, int i, int j) {
		if (getYx(i, j)) {
			Long mask = 1L << (ColSize - 1 - j);
			if ((formalBitMap[i] & mask) > 0) {
				return 1;
			}
		}
		
		return 0;
	}
	  
}
