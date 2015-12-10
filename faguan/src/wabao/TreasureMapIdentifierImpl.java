package wabao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import xyj.Room;
import xyj.Roomku;



public class TreasureMapIdentifierImpl implements TreasureMapIdentifier {

	private Collection<TreasureMapZi> _ziku;
	
	private Roomku _roomku;

	@Override
	public int initHanziku() {
		_roomku = Roomku.createFromFile("all_room.txt");
		
		LinkedHashMap<String, TreasureMapZi> ziku = TreasureMapHanzikuBuilder.readZikuFile("wabao_ziku.txt");
		_ziku = ziku.values();
		return _ziku.size();
	}

	@Override
	public Room identify(TreasureMap map) {
		boolean identified = false;
		
        //1. identify map by unique
        Room identifiedRoom = null;
        List<Room> possibleRooms = null;
        if (map.getArea().length == 0) {
        	possibleRooms = _roomku.getRooms(map.getRoom());
        } else {
        	possibleRooms = _roomku.getRooms(map.getRoom(), map.getArea().length);
        }
        if (possibleRooms.size() == 1) {
        	identifiedRoom = possibleRooms.get(0);
			identified = true;
			System.out.println("通过房间名识别成功！");
        }
        
        //2. identify map by ziku
        if (!identified && map.getArea().length > 0) {
	        identifiedRoom = identifyByZiku(map, _ziku);
	        
	        //3. select most possible room and correct room name
	        String zikuIdentifiedArea = identifiedRoom.getArea();
	        identified = selectMostPossibleRoom(identifiedRoom, possibleRooms);
	        if (identified) {
				if(identifiedRoom.getArea().equals(zikuIdentifiedArea)) {
					System.out.println("通过区域图识别成功！");
				} else {
					System.out.println("通过区域图模糊识别成功！");
				}
	        }
        }
		
        if (identifiedRoom != null) {
			map.setRoom(identifiedRoom.getName());
			if (map.getArea().length > 0) {
				map.setAreaName(identifiedRoom.getArea());
			}
        }
		
		if (!identified) {
			System.out.println("通过区域图模糊识别失败！记录到wabao_unknown.txt");
			dumpMapToUnknownFile(map);
		}
		
		return identifiedRoom;
	}
	
	private void dumpMapToUnknownFile(TreasureMap map) {
		FileWriter mapWriter = null;
		try {
			File f = new File("wabao_unknown.txt");
			mapWriter = new FileWriter(f, true);
			mapWriter.append(map.getRoom() + "-" + map.getAreaName() + "\n");
			TreasureMapHanzi[] hanziList = map.getArea();
			for(int i = 0;i < hanziList.length;i++) {
				TreasureMapHanzi hz = hanziList[i];
				mapWriter.append(hz.getDisplayString() + hz.getWenzi() + "\n");
				mapWriter.append(hz.getWenzi() + " " +  hz.getBitMapString() + "\n");
			}
			mapWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (mapWriter != null) {
				try {
					mapWriter.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private boolean selectMostPossibleRoom(Room identifiedRoom, List<Room> possibleRooms) {
		if (possibleRooms == null || possibleRooms.isEmpty()) {
			return false;
		}
		
		// > 50% return true;
		float matchMinLimit = 0.5f; 
		
		int[] matchCounts = new int[possibleRooms.size()];
		String area = identifiedRoom.getArea();
		for(int i = 0; i < matchCounts.length; i++) {
			for(int j = 0; j < area.length(); j++) {
				if (area.charAt(j) == possibleRooms.get(i).getArea().charAt(j)) {
					matchCounts[i]++;
				}
			}
		}
		
		int mostMatchIndex = -1;
		int mostMatchCount = -1;
		for(int i = 0; i < matchCounts.length; i++) {
			if(matchCounts[i] > mostMatchCount) {
				mostMatchCount = matchCounts[i];
				mostMatchIndex = i;
			}
		}
		
		Room mostMatchRoom = possibleRooms.get(mostMatchIndex);
		identifiedRoom.setName(mostMatchRoom.getName());
		identifiedRoom.setArea(mostMatchRoom.getArea());
		
		float matchPercent = mostMatchCount/(float)area.length();
		return (matchPercent > matchMinLimit);
	}

	private static Room identifyByZiku(TreasureMap map, Collection<TreasureMapZi> ziku) {
		TreasureMapHanzi[] area = map.getArea();
		for(int i = 0; i < area.length; i++) {
			TreasureMapHanzi hz = area[i];
			TreasureMapHanzi hz1 = new TreasureMapHanzi(null, hz.getBitMap());
			identifyByZiku(hz.getBitCounts(), ziku, hz1);
			
			hz.setWenzi(hz1.getWenzi());
		}
			
		return new Room(map.getRoom(), map.getAreaName());
	}
	
	private static int identifyByZiku(int[] bitCounts, Collection<TreasureMapZi> ziku, TreasureMapHanzi hz1) {
		int deltaMin = 9999;
		for(TreasureMapZi zhzbHz: ziku) {
			int[] hzBitCount = zhzbHz.getBitCount();
			int delta = 0;
			for (int i = 0; i < hzBitCount.length; i++) {
				delta += (hzBitCount[i] - bitCounts[i]) * (hzBitCount[i] - bitCounts[i]);
			}
			
			if (delta < deltaMin) {
				deltaMin = delta;
				hz1.setWenzi(zhzbHz.getWenzi());
				if (deltaMin == 0) {
					break;
				}
			}
		}
		
		return deltaMin;
	}

}
