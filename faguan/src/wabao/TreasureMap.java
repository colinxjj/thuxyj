package wabao;

public class TreasureMap {
	private String _room;
	private TreasureMapHanzi[] _area;
	
	public TreasureMap(String room, TreasureMapHanzi[] area) {
		this.setRoom(room);
		this.setArea(area);
	}

	public String getRoom() {
		return _room;
	}

	public void setRoom(String _room) {
		this._room = _room;
	}

	public TreasureMapHanzi[] getArea() {
		return _area;
	}

	public void setArea(TreasureMapHanzi[] _area) {
		this._area = _area;
	}

	public String getAreaName() {
		if (this._area == null) {
			return null;
		}
		
		String areaName = "";
		for(int i = 0; i < _area.length; i++) {
			areaName += _area[i].getWenzi();
		}
		return areaName;
	}

	public void setAreaName(String areaName) {
		if (areaName == null || areaName.length() != _area.length) {
			return;
		}
		
		for(int i = 0; i < _area.length; i++) {
			_area[i].setWenzi(areaName.substring(i, i+1));
		}
		
	}
	
	public void clearAreaName() {
		for(int i = 0; i < _area.length; i++) {
			_area[i].setWenzi(null);
		}
	}

	public String getKey() {
		StringBuilder builder = new StringBuilder();
		builder.append(this._room);
		for(int i = 0; i < _area.length; i++) {
			builder.append(_area[i].getBitMapString());
		}
		
		return builder.toString();
	}
}
