package xyj;

public class Room {
	private String _name;
	private String _area;
	
	public Room(String name, String area) {
		this._name = name;
		this._area = area;
	}
	
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}
	public String getArea() {
		return _area;
	}
	public void setArea(String area) {
		this._area = area;
	}

}
