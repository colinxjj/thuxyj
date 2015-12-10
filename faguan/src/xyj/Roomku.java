package xyj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

public class Roomku {
	
	private Hashtable<String, List<Room>> _rooms = new Hashtable<String, List<Room>>(4000);
	
	private HashSet<String> _normalizedAreas = new HashSet<String>();
	
	private Hashtable<String, HashSet<String>> _roomsByArea =  new Hashtable<String, HashSet<String>>(100);
	
	protected Roomku(Hashtable<String, List<Room>> rooms) {
		setRooms(rooms);
	}
	
	public Roomku() {
		// TODO Auto-generated constructor stub
	}
	
	public Hashtable<String, List<Room>> getRooms() {
		return _rooms;
	}

	public void setRooms(Hashtable<String, List<Room>> _rooms) {
		this._rooms = _rooms;
	}

	public static Roomku createFromFile(String roomFile) {
		Roomku roomku = new Roomku();
		InputStream is = null;
		File file = new File(roomFile);
//		System.out.println(file.getAbsolutePath());
		if(file.exists()) {
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			is = Roomku.class.getResourceAsStream("/resource/" + roomFile);
		}
	    String encoding = "GBK";
	    BufferedReader reader = null;
	    String tempString = null;
	    
	    try {
	    	InputStreamReader read = new InputStreamReader(is, encoding);//¿¼ÂÇµ½±àÂë¸ñÊ½
	    	reader = new BufferedReader(read);

	        while ((tempString = reader.readLine()) != null) {
	        	if(tempString.trim().length() < 5) {
	        		System.out.println("ignore line: " + tempString);
	        		continue;
	        	}
	        	
	        	//add parts
	        	String[] parts = tempString.split("-");
	        	if(parts.length != 2) {
	        		System.out.println("ignore line: " + tempString);
	        		continue;
	        	}
	        	
	        	Room room = new Room(parts[0], parts[1]);
	        	boolean add = roomku.add(room);
	        	if (!add) {
	        		System.out.println("duplicated line: " + tempString);
	        	}
	        }
	        reader.close();
	    } catch (FileNotFoundException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }finally{
	        if(reader != null){
	            try {
	                reader.close();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	    }
		return roomku;
	}

	public boolean add(Room room) {
		char[] tempChars = room.getName().toCharArray();
	    Arrays.sort(tempChars);
	    String key = new String(tempChars);
	    List<Room> existRooms = getRooms().get(key);
	    if(existRooms == null) {
	    	existRooms = new ArrayList<Room>();
	    	getRooms().put(key, existRooms);
	    	this._normalizedAreas.add(room.getArea());
	    }
	    
	    boolean add = true;
	    for (Room rm : existRooms) {
	    	if (rm.getArea().equals(room.getArea())){
	    		add = false;
	    	}
	    }
	    
	    if (add) {
	    	existRooms.add(room);
	    	
	    	HashSet<String> existRoomsByArea = _roomsByArea.get(room.getArea());
	    	if (existRoomsByArea == null) {
	    		existRoomsByArea = new HashSet<String>();
	    		_roomsByArea.put(room.getArea(), existRoomsByArea);
	    	}
	    	existRoomsByArea.add(room.getName());
	    }
	    
	    return add;
	}
	
	public List<Room> getRooms(String name) {
		if(name == null) {
			return new ArrayList<Room>();
		}
		
		char[] tempChars = name.toCharArray();
	    Arrays.sort(tempChars);
	    
	    List<Room> rooms = _rooms.get(new String(tempChars));
	    if (rooms == null) {
	    	rooms = new ArrayList<Room>();
	    }
	    
	    return rooms;
	}
	
	public List<Room> getRooms(String name, int areaLength) {
	    List<Room> roomList = getRooms(name);
	    
	    List<Room> result = new ArrayList<Room>();
	    for(Room room : roomList) {
			if(room.getArea().length() == areaLength) {
				result.add(room);
			}
		}
	    
	    return result;
	}
	
	public Hashtable<String, Room> getUniqueRooms() {
		Hashtable<String, Room> uniqueRooms = new Hashtable<String, Room>();
		for (Entry<String, List<Room>> entry : _rooms.entrySet()) {
			List<Room> roomList = entry.getValue();
			if (roomList.size() == 1) {
				uniqueRooms.put(entry.getKey(), roomList.get(0));
			}
		}
		
		return uniqueRooms;
	}
	
	public Room getUniqueRoom(String name, int areaLength) {
		Room result = null;
		List<Room> roomList = getRooms(name);
		if (roomList == null) {
			return null;
		}
		
		int matchCount = 0;
		for (Room room : roomList) {
			if(room.getArea().length() == areaLength) {
				result = room;
				matchCount++;
			}
		}
		
		if(matchCount == 1) {
			return result;
		}
		
		return null;
	}
	
	public boolean isValid(Room room) {
		List<Room> roomList = getRooms(room.getName());
		if (roomList == null) {
			return false;
		}
		
		for (Room rm : roomList) {
			if(rm.getArea().equals(room.getArea())) {
				return true;
			}
		}
		
		return false;
	}


	public boolean isValidByRoomNameLength(Room room, int lengthToCheck) {
		HashSet<String> existRoomsByArea = getRoomsByArea(room.getArea());
		if (existRoomsByArea == null) {
			return false;
		}
		
		String roomName = room.getName();
		if(roomName.length() < lengthToCheck) {
			return existRoomsByArea.contains(roomName);
		} else { // roomName.length() == lengthToCheck
			for (String rmName : existRoomsByArea) {
				if(rmName.startsWith(roomName)) {
					return true;
				}
			}
		}
		
		return false;
	}

	public HashSet<String> getRoomsByArea(String areaName) {
		HashSet<String> existRoomsByArea = this._roomsByArea.get(areaName);
	    
	    return existRoomsByArea;
	}

	
	public void dumpFile(String fileName) {
		List<Room> rooms = orderByArea();
		
		try {
			FileOutputStream haizi = new FileOutputStream(fileName);
			OutputStreamWriter writer = new OutputStreamWriter(haizi, "GBK");
			for(Room room : rooms){
				writer.append(room.getName() + "-" +  room.getArea() + "\r\n");
			}
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	private List<Room> orderByArea() {
		ArrayList<Room> orderedRoom = new ArrayList<Room>();
		for(List<Room> rms : _rooms.values()) {
			orderedRoom.addAll(rms);
		}
		
		Collections.sort(orderedRoom, new Comparator<Room>() {
			public int compare(Room room1, Room room2) {
				return room1.getArea().compareTo(room2.getArea());
			}
		});
		return orderedRoom;
	}

	public String normalizeArea(String area) {
		if(area == null) {
			return null;
		}
		
		char[] areaChars = area.toCharArray();
	    Arrays.sort(areaChars);
	    
//		for(List<Room> rms : _rooms.values()) {
//			for(Room room : rms) {
//				char[] areaCharsOfRoom = room.getArea().toCharArray();
//				Arrays.sort(areaCharsOfRoom);
//				if(areaCharsOfRoom.equals(areaChars)) {
//					return room.getArea();
//				}
//			}
//		}
		
		for (String normalizedArea : _normalizedAreas) {
			char[] areaCharsOfRoom = normalizedArea.toCharArray();
			Arrays.sort(areaCharsOfRoom);
			if(Arrays.equals(areaCharsOfRoom, areaChars)) {
				return normalizedArea;
			}		
		}
		
		return null;
	}

}
