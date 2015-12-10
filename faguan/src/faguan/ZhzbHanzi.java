package faguan;

public class ZhzbHanzi {
	private String _wenzi;
	private int[] _bitCount;
	private Integer _totalBits;
	
	public ZhzbHanzi(String wenzi, int[] bitCount) {
		_wenzi = wenzi;
		_bitCount = bitCount;
	}

	public ZhzbHanzi(String wenzi, String bitCounts) {
		_wenzi = wenzi;
		String[] bitCount = bitCounts.split(",");
		_bitCount = new int[48];
		for(int i = 0; i < 48; i++) {
			_bitCount[i] = Integer.parseInt(bitCount[i]);
		}
	}

	public String getWenzi() {
		return _wenzi;
	}

	public void setWenzi(String _wenzi) {
		this._wenzi = _wenzi;
	}

	public int[] getBitCount() {
		return _bitCount;
	}

	public void setBitCount(int[] _bitCount) {
		this._bitCount = _bitCount;
		_totalBits = null;
	}

	public int getTotalBits() {
		if(_totalBits == null) {
			_totalBits = new Integer(0);
			for(int i = 0; i < 24; i++) {
				_totalBits += _bitCount[i];
			}
		}
		return _totalBits;
	}

	public String getBitCountString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < _bitCount.length; i++) {
			sb.append(_bitCount[i] + ",");
		}

		return sb.toString();
	}
}
