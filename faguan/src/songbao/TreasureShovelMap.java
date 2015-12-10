package songbao;


public class TreasureShovelMap {
	private String _area;
	private TreasureShovelMapHanzi[] _room;
	
	public TreasureShovelMap(String area, TreasureShovelMapHanzi[] room) {
		this.setRoom(room);
		this.setArea(area);
	}

	public String getArea() {
		return _area;
	}

	public void setArea(String _area) {
		this._area = _area;
	}

	public TreasureShovelMapHanzi[] getRoom() {
		return _room;
	}

	public void setRoom(TreasureShovelMapHanzi[] _room) {
		this._room = _room;
	}

	public String getRoomName() {
		if (this._room == null) {
			return null;
		}
		
		String roomName = "";
		for(int i = 0; i < _room.length; i++) {
			roomName += _room[i].getWenzi();
		}
		return roomName;
	}

	public void setRoomName(String roomName) {
		if (roomName == null || roomName.length() != _room.length) {
			return;
		}
		
		for(int i = 0; i < _room.length; i++) {
			_room[i].setWenzi(roomName.substring(i, i+1));
		}
		
	}
	
	public void clearRoomName() {
		for(int i = 0; i < _room.length; i++) {
			_room[i].setWenzi(null);
		}
	}

	public String getKey() {
		StringBuilder builder = new StringBuilder();
		builder.append(this._area);
		for(int i = 0; i < _room.length; i++) {
			builder.append(_room[i].getBitMapString());
		}
		
		return builder.toString();
	}
}
