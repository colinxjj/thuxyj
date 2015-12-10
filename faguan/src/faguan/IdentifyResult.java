package faguan;

public class IdentifyResult {
	private Hanzi _hanzi;
	private int _delta;
	private int _pos;
	
	public IdentifyResult(Hanzi hanzi, int delta, int position) {
		this._hanzi = hanzi;
		this._delta = delta;
		this._pos = position;
	}
	
	public Hanzi getHanzi() {
		return _hanzi;
	}
	public void setHanzi(Hanzi hanzi) {
		this._hanzi = hanzi;
	}
	public int getDelta() {
		return _delta;
	}
	public void setDelta(int delta) {
		this._delta = delta;
	}

	public int getPosition() {
		return _pos;
	}

	public void setPosition(int position) {
		this._pos = _pos;
	}
}
