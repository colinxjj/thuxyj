package songbao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import wabao.TreasureMapZi;
import xyj.Room;
import xyj.Roomku;



public class TreasureShovelMapIdentifierImpl implements TreasureShovelMapIdentifier {

	private Collection<TreasureMapZi> _ziku;
	
	private Roomku _roomku;

	@Override
	public int initHanziku() {
		_roomku = Roomku.createFromFile("all_room.txt");
		
		LinkedHashMap<String, TreasureMapZi> ziku = TreasureShovelMapHanzikuBuilder.readZikuFile("songbao_ziku.txt");
		_ziku = ziku.values();
		return _ziku.size();
	}

	@Override
	public Room identify(TreasureShovelMap map) {
		boolean identified = false;
		
        //1. identify map by ziku
		identified = identifyByZiku(map, _ziku);
		
		if (!identified) {
			System.out.println("通过房间图模糊识别失败！记录到songbao_unknown.txt");
			dumpMapToUnknownFile(map);
		}
		
		return new Room(map.getRoomName(), map.getArea());
	}
	
	private void dumpMapToUnknownFile(TreasureShovelMap map) {
		FileWriter mapWriter = null;
		try {
			File f = new File("songbao_unknown.txt");
			mapWriter = new FileWriter(f, true);
			mapWriter.append(map.getRoomName() + "-" + map.getArea() + "\n");
			TreasureShovelMapHanzi[] hanziList = map.getRoom();
			for(int i = 0;i < hanziList.length;i++) {
				TreasureShovelMapHanzi hz = hanziList[i];
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


	private boolean identifyByZiku(TreasureShovelMap map, Collection<TreasureMapZi> ziku) {
		// > 50% return true;
		float matchMinLimit = 0.5f; 
		
		map.setArea(_roomku.normalizeArea(map.getArea()));
		
		TreasureShovelMapHanzi[] roomHanziArray = map.getRoom();
		for(int i = 0; i < roomHanziArray.length; i++) {
			TreasureShovelMapHanzi hz = roomHanziArray[i];
			TreasureShovelMapHanzi hz1 = new TreasureShovelMapHanzi(null, hz.getBitMap());
			identifyByZiku(hz.getBitCounts(), ziku, hz1);
			hz.setWenzi(hz1.getWenzi());
		}
		
		// Look for most possible room in the area
		int roomNameLength = roomHanziArray.length;
		HashSet<String> areaRooms = _roomku.getRoomsByArea(map.getArea());
		int maxMatchedCount = 0;
		String maxMatchedRoomName = map.getRoomName();
		for (String areaRoom : areaRooms) {
			int matchedCount = 0;
			if (((roomNameLength < 4) && (areaRoom.length() == roomNameLength)) || ((roomNameLength==4) && (areaRoom.length() >= roomNameLength))) {
				for(int i = 0; i < roomNameLength; i++) {
					if (areaRoom.substring(i, i+1).equals(map.getRoom()[i].getWenzi())) {
						matchedCount++;
					}
				}
			}
			
			if (matchedCount > maxMatchedCount) {
				maxMatchedCount = matchedCount;
				maxMatchedRoomName = areaRoom;
			}
		}
		map.setRoomName(maxMatchedRoomName.substring(0, roomNameLength));
			
		float matchPercent = maxMatchedCount/(float)maxMatchedRoomName.length();
		return (matchPercent > matchMinLimit);
	}
	
	private static int identifyByZiku(int[] bitCounts, Collection<TreasureMapZi> ziku, TreasureShovelMapHanzi hz1) {
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
