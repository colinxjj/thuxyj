package wabao;

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
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;

import xyj.Room;
import xyj.Roomku;
import duishi.Program;
import faguan.Hanzi;
import faguan.ZhzbHanzi;

public class TreasureMapHanzikuBuilder {


	private static long[] _bitMaskArray = { 1L<<23,
											1L<<22,
											1L<<21,
											1L<<20,
											1L<<19,
											1L<<18,
											1L<<17,
											1L<<16,
											1L<<15,
											1L<<14,
											1L<<13,
											1L<<12,
											1L<<11,
											1L<<10,
											1L<<9,
											1L<<8,
											1L<<7,
											1L<<6,
											1L<<5,
											1L<<4,
											1L<<3,
											1L<<2,
											1L<<1,
											1L};
	
	private LinkedHashMap<String, TreasureMapHanzi> _hanziku = new LinkedHashMap<String, TreasureMapHanzi>();

	private LinkedHashMap<String, Hanzi> _unansweredHanziku;
	
	
	
	
	public void addMapku(String mapkuFile, LinkedHashMap<String, TreasureMap> rightMaps){
		InputStream is = null;
		File file = new File(mapkuFile);
		if(file.exists()) {
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
    		System.out.println("File doesn't exit! " + mapkuFile);
			return;
		}
		
	    String encoding = "GBK";
	    BufferedReader reader = null;
	    int totalCountFile = 0;
	    int addedCount = 0;
	    int conflictCount = 0;
	    int lineCount = 0;
	    
	    try {
	    	InputStreamReader read = new InputStreamReader(is, encoding);//考虑到编码格式
	    	reader = new BufferedReader(read);

	    	String line = null; //
	        while ((line = reader.readLine()) != null) {
	        	lineCount++;
	        	String[] parts = line.split("-");
	        	if(parts.length != 2) {
					System.out.println("Error near by line " + lineCount);
	        		continue;
	        	}
	        	
	        	String room = parts[0];
	        	String areaName = parts[1];
	        	TreasureMapHanzi[] hanziList = new TreasureMapHanzi[areaName.length()];
	        	
	        	for(int i = 0; i < hanziList.length; i++) {
	        		// ignore hanzi picture lines
	        		do {
	        			line = reader.readLine();
	        			lineCount++;
	        		} while (!isMapHanziLine(line));
	        		
		        	totalCountFile++;
		        	String wenzi = line.substring(0, 1);
		        	String bitMapString = line.substring(2);
		        	
		        	TreasureMapHanzi hz = new TreasureMapHanzi(wenzi, bitMapString);
		        	hanziList[i] = hz;
	        	}
	        	
	        	TreasureMap map = new TreasureMap(room, hanziList);
	        	map.setAreaName(areaName); // for identified file
	        	
	        	// check conflict
	        	for(int i = 0; i < hanziList.length; i++) {
	        		TreasureMapHanzi hz = hanziList[i];
	        		String bitMapString = hz.getBitMapString();
	        		TreasureMapHanzi oldHz = _hanziku.get(bitMapString);
		        	if(oldHz != null) {
		        		if(!oldHz.getWenzi().equals(hz.getWenzi())) {
			        		conflictCount++;
			        		System.out.println("Conflict!");
			        		System.out.println(oldHz.getDisplayString());
			        		System.out.println("Mapku: " + oldHz.getWenzi() + ", new: " + hz.getWenzi());
			        		System.out.println(bitMapString);
		        		}
		        	} else {
			        	addedCount++;
			        	_hanziku.put(bitMapString, hz);
		        	}
	        	}
	        	
	        	String key = map.getKey();
	        	if (rightMaps.get(key) == null) {
	        		rightMaps.put(key, map);
	        	}
	        }
	        reader.close();
//	        return ziku;
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

		System.out.println("totalCount: " + totalCountFile + ", added:" + addedCount + ", conflict: " + conflictCount);
		System.out.println("Now zikuCount:" + _hanziku.size());
//	    return null;
	}
	

	private boolean isMapHanziLine(String line) {
		if(line.length() > 50 && line.indexOf(';') > 2){
			return true;
		}
		return false;
	}


	public static ArrayList<TreasureMap> readUnknownFile(String mapkuFile){
		ArrayList<TreasureMap> unknownMaps = new ArrayList<TreasureMap>();
		
		InputStream is = null;
		File file = new File(mapkuFile);
		if(file.exists()) {
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
    		System.out.println("File doesn't exit! " + mapkuFile);
			return unknownMaps;
		}
		
	    String encoding = "GBK";
	    BufferedReader reader = null;
	    int totalCountFile = 0;
	    int addedCount = 0;
	    int conflictCount = 0;
	    int lineCount = 0;
	    
	    try {
	    	InputStreamReader read = new InputStreamReader(is, encoding);//考虑到编码格式
	    	reader = new BufferedReader(read);

	    	String line = null; //
	        while ((line = reader.readLine()) != null) {
	        	lineCount++;
	        	String[] parts = line.split("-");
	        	if(parts.length != 2) {
					System.out.println("Error near by line " + lineCount);
	        		continue;
	        	}
	        	
	        	String room = parts[0];
	        	String areaName = parts[1]; //such as: nullnull nullnullnull
	        	TreasureMapHanzi[] hanziList = new TreasureMapHanzi[areaName.length()/4];
	        	
	        	for(int i = 0; i < hanziList.length; i++) {
	        		line = reader.readLine();
	        		lineCount++;
	        		while (line != null && !line.startsWith("null ")) {
	        			line = reader.readLine();
	        			lineCount++;
	        		}
	        		
		        	totalCountFile++;
		        	String wenzi = null;//line.substring(0, 1);
		        	String bitMapString = line.substring(5);
		        	
		        	TreasureMapHanzi hz = new TreasureMapHanzi(wenzi, bitMapString);
		        	hanziList[i] = hz;
	        	}
	        	
	        	TreasureMap map = new TreasureMap(room, hanziList);
	        	unknownMaps.add(map);
	        }
	        reader.close();
//	        return ziku;
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

	    return unknownMaps;
	}

	public void dumpAnsweredZiku(String file, boolean withPiture) {
//		dumpHanziList(_hanziku.values(), file, withPiture);
	}
	
	public void dumpUnansweredZiku(String file, boolean withPiture) {
//		dumpHanziList(_unansweredHanziku.values(), file, withPiture);
	}

	public void dumpAnsweredZhzbZiku(String file) {
		dumpZhzbHanziList(_hanziku.values(), file);
	}
	
	public LinkedHashMap<String, TreasureMapHanzi> getHanziku() {
		return _hanziku;
	}

	public void setHanziku(LinkedHashMap<String, TreasureMapHanzi> hanziku) {
		this._hanziku = hanziku;
	}
	
	private static void dumpMapList(Collection<TreasureMap> mapList, String file, boolean withPiture) {
		try {
			FileOutputStream haizi = new FileOutputStream(file);
			OutputStreamWriter mapWriter = new OutputStreamWriter(haizi, "GBK");
			for(TreasureMap map : mapList){
				mapWriter.append(map.getRoom() + "-" + map.getAreaName() + "\n");
				TreasureMapHanzi[] hanziList = map.getArea();
				for(int i = 0;i < hanziList.length;i++) {
					TreasureMapHanzi hz = hanziList[i];
					if(withPiture){
						mapWriter.append(hz.getDisplayString() + hz.getWenzi() + "\n");
					}
					mapWriter.append(hz.getWenzi() + " " +  hz.getBitMapString() + "\n");
				}
			}
			mapWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private static void dumpZhzbHanziList(Collection<TreasureMapHanzi> hanziList, String file) {
		try {
			FileOutputStream haizi = new FileOutputStream(file);
			OutputStreamWriter mapWriter = new OutputStreamWriter(haizi, "GBK");
			for(TreasureMapHanzi hz : hanziList){
//				if(withPiture){
//					mapWriter.append(hz.getDisplayString() + hz.getWenzi() + "\n");
//				}
				mapWriter.append(hz.getWenzi() + " " +  hz.getBitCountString() + "\n");
			}
			mapWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	

	private static void dumpZhzbHanziList(Collection<TreasureMap> mapList, String file, boolean withPiture) {
		try {
			FileOutputStream haizi = new FileOutputStream(file);
			OutputStreamWriter mapWriter = new OutputStreamWriter(haizi, "GBK");
			for(TreasureMap map : mapList){
				mapWriter.append(map.getRoom() + " - " + map.getAreaName() + "\n");
				TreasureMapHanzi[] hanziList = map.getArea();
				for(int i = 0;i < hanziList.length;i++) {
					TreasureMapHanzi hz = hanziList[i];
					if(withPiture){
						mapWriter.append(hz.getDisplayString() + hz.getWenzi() + "\n");
					}
					mapWriter.append(hz.getWenzi() + " " +  hz.getBitCountString() + "\n");
				}
			}
			mapWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
	
		
//		buildHanzikuWithOriginMapFile("resource_wabao/map1.txt");
//		buildHanzikuWithOriginMapFile("resource_wabao/maps_20150227.txt");
//		buildHanzikuWithOriginMapFile("resource_wabao/maps_20150323.txt");
//		buildHanzikuWithOriginMapFile("resource_wabao/maps_20150323_2.txt");
		
//		identifyUnansweredFile("resource_wabao/maps_20150227.txt_unknown");
		
//		createAllRoomsFromTimeoutTin();
//		refineRoomku();
		
//		buildMapku("resource_wabao/maps_20150227.txt_unknown_identified");
		
//		buildHanzikuWithOriginMapFile("resource_wabao/maps_20150323.txt");
//		buildMapku("resource_wabao/maps_20150323.txt_unknown");
		
//		buildHanzikuWithOriginMapFile("resource_wabao/maps_20150323_2.txt");
//		buildMapku("resource_wabao/maps_20150323_2.txt_unknown");
		
//		buildHanzikuWithOriginMapFile("g:/wb001.txt");
//		buildHanzikuWithOriginMapFile("g:/wb002.txt");
//		buildMapku("g:/wb002.txt_unknown");
	}

	private static void refineRoomku() {
		Roomku roomku = Roomku.createFromFile("resource_wabao/all_room.txt");
		roomku.dumpFile("resource_wabao/all_room.txt");
		
	}


	public static void testComputeDelta() {
		TreasureMapHanzi z1 = new TreasureMapHanzi("长", "0;e000;3e000;f8001f80000;fc00e180000;fe018040000;fc300c0000;3e0060000;3e0060000;3f0c0000;ff81800;fff000;ffc00;3ff000;9ffc00000;fff800000;3e0780000;3807c0000;fe0000;ff00000;38780000;f0f00000;e0780000;c00e0000;");
		TreasureMapHanzi z2 = new TreasureMapHanzi("长", "e000000;e000000;f000000;3800000;3800000;fe1c0000000;f3ff0000000;e01fffc0000;38007ffe0000;e007ffe0000;381fcfc0000;e1ce700000;ce1860000;3c0e1c000;3c0e1e000;f00e07800;f000f03800;3c000e00000;3c000e00000;3c000000000;38000000000;38000000000;38000000000;0;");
		z1.getFormalBitMap();
		z2.getFormalBitMap();
		
		long start = System.currentTimeMillis();
		int delta1 = computeDeltaOld(z1, z2);
		long end = System.currentTimeMillis();
		System.out.println(delta1 + ",time " + (end - start));
		
		
		start = System.currentTimeMillis();
		int delta2 = computeDelta(z1, z2);
		end = System.currentTimeMillis();
		System.out.println(delta2 + ",time " + (end - start));
	}


	public static void buildHanzikuWithOriginMapFile(String fileName) {
		ArrayList<TreasureMap> result = TreasureMapReader.parseOriginMapFile(fileName);

		ArrayList<TreasureMap> unknownMaps = new ArrayList<TreasureMap>(); 
		ArrayList<TreasureMap> rightMaps = identify(result, unknownMaps);
		dumpMapList(rightMaps, fileName + "_right", false);
		dumpMapList(unknownMaps, fileName + "_unknown", true);
		
		buildMapku(fileName + "_right");
	}
	
	
	private static ArrayList<TreasureMap> identify(ArrayList<TreasureMap> originMaps, ArrayList<TreasureMap> unknownMaps) {
		ArrayList<TreasureMap> identifiedMaps = new ArrayList<TreasureMap>();
		
		//identify by all room
		Roomku roomku = Roomku.createFromFile("resource_wabao/all_room.txt");
		Hashtable<String, TreasureMapZi> ziku = readZiku("wabao_ziku.txt");
		
		for(TreasureMap map : originMaps) {
			boolean identified = false;
			
	        //1. identify map by unique
	        Room identifiedRoom = roomku.getUniqueRoom(map.getRoom(), map.getArea().length);
	        if (identifiedRoom != null) {
				identified = true;
	        }
	        
	        //2. identify map by ziku
	        if (!identified) {
		        identifiedRoom = identifyUnansweredByZiku(map, ziku.values());
				if(roomku.isValid(identifiedRoom)) {
					identified = true;
				}
	        }
			
			if (identified) {
				map.setRoom(identifiedRoom.getName());
				map.setAreaName(identifiedRoom.getArea());
				identifiedMaps.add(map);
			} else {
				unknownMaps.add(map);
			}
		}
		
		return identifiedMaps;
	}
	
	private static ArrayList<TreasureMap> identifyByUnique(ArrayList<TreasureMap> originMaps, ArrayList<TreasureMap> unknownMaps, boolean checkUnique) {
		ArrayList<TreasureMap> identifiedMaps = new ArrayList<TreasureMap>();
		
		//identify by unique room
//		Hashtable<String, Room> uniqueRooms = readUniqueRoomFile();
		Roomku roomku = Roomku.createFromFile("resource_wabao/all_room.txt");
//		Hashtable<String, Room> uniqueRooms = roomku.getUniqueRooms();
		
		Hashtable<String, TreasureMapZi> ziku = readZiku("wabao_ziku.txt");
		
		
		for(TreasureMap map : originMaps) {
			boolean identified = false;
			
			String room = map.getRoom();
	        Room identifiedRoom = roomku.getUniqueRoom(room, map.getArea().length);
	        
	        // identify map
	        Room zikuIdentifiedRoom = identifyUnansweredByZiku(map, ziku.values());

	        if(identifiedRoom != null) {
				String area = identifiedRoom.getArea();
				if((area != null) && (area.length() == map.getArea().length)) {
					if (checkUnique) {
						if (identifiedRoom.getArea().equals(zikuIdentifiedRoom.getArea())) {
							map.setRoom(identifiedRoom.getName());
							map.setAreaName(area);
							identifiedMaps.add(map);
							identified = true;
						}
					} else {
						map.setRoom(identifiedRoom.getName());
						map.setAreaName(area);
						identifiedMaps.add(map);
						identified = true;
					}
				} else {
					System.out.println("invalid unique room:" + identifiedRoom.getName() + "-" + area + ", origin: " + room);
				}
			}
			
			if (!identified) {
//				map.clearAreaName();
				unknownMaps.add(map);
			}
		}
		
		
		// TODO Auto-generated method stub
		return identifiedMaps;
	}

	private static Room identifyUnansweredByZiku(TreasureMap map, Collection<TreasureMapZi> ziku) {
		int delta = 9999;
		
		TreasureMapHanzi[] area = map.getArea();
		for(int i = 0; i < area.length; i++) {
			TreasureMapHanzi hz = area[i];
			TreasureMapHanzi hz1 = new TreasureMapHanzi(null, hz.getBitMap());
			int zhzbDelta = identifyByZiku(hz.getBitCounts(), ziku, hz1);
			
			if((zhzbDelta < delta)) {
				hz.setWenzi(hz1.getWenzi());
			} else {
				hz.setWenzi(null);
			}
		}
			
		return new Room(map.getRoom(), map.getAreaName());
	}


	private static Hashtable<String, Room> readUniqueRoomFile() {
		Hashtable<String, Room> rooms = new Hashtable<String, Room>(4000);
		InputStream is = null;
		File file = new File("resource_wabao/unique_room.txt");
		if(file.exists()) {
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			is = Program.class.getResourceAsStream("/resource_wabao/unique_room.txt");
		}
	    String encoding = "GBK";
	    BufferedReader reader = null;
	    String tempString = null;
	    
	    try {
	    	InputStreamReader read = new InputStreamReader(is, encoding);//考虑到编码格式
	    	reader = new BufferedReader(read);

	        while ((tempString = reader.readLine()) != null) {
	        	if(tempString.trim().length() < 5) {
	        		continue;
	        	}
	        	
	        	//add parts
	        	char[] tempChars;
	        	String[] parts = tempString.split("-");
	        	if(parts.length != 2) {
	        		continue;
	        	}
	        	
	        	Room room = new Room(parts[0], parts[1]);
	        	tempChars = room.getName().toCharArray();
	    	    Arrays.sort(tempChars);
	    	    Room oldRoom = rooms.put(new String(tempChars), room);
	    	    if(oldRoom != null) {
	    	    	System.out.println("Conflict room: " + room.getName());
	    	    	rooms.remove(new String(tempChars));
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
		return rooms;
	}

	
	public static void identifyUnansweredOriginFile(String fileIn, String fileOut) {
//		HanziReader reader = new HanziReader();
//		ArrayList<Hanzi> hanziList = reader.parseOriginFile(fileIn);
//		identifyUnansweredByZiku(hanziList);
//		dumpHanziList(hanziList, fileOut, true);
////		dumpHanziList(hanziList, fileOut+"_withmap", true);
////		dumpZhzbHanziList(hanziList, fileOut+"_ziku", false);
		
	}
	
	
	private static void identifyUnansweredFile(String unansweredFile) {
		Collection<TreasureMap> unansweredMaps = readUnknownFile(unansweredFile);
		Hashtable<String, TreasureMapZi> ziku = readZiku("wabao_ziku.txt");
		identifyUnansweredByZiku(unansweredMaps, ziku.values());
		
		dumpMapList(unansweredMaps, unansweredFile + "_identified", true);
		
	}
	
	private static void buildMapku(String identifiedFile) {
		TreasureMapHanzikuBuilder zikuBuilder = new TreasureMapHanzikuBuilder();
		LinkedHashMap<String, TreasureMap> rightMaps = new LinkedHashMap<String, TreasureMap>();
		zikuBuilder.addMapku("wabao_hanziku.txt", rightMaps);
		System.out.println();

		zikuBuilder.addMapku(identifiedFile, rightMaps);
		
		Collection<TreasureMap> mapCollection = rightMaps.values();
		correctRoomName(mapCollection);
		
		TreasureMapHanzikuBuilder.dumpMapList(mapCollection, "wabao_hanziku.txt", false);
		TreasureMapHanzikuBuilder.dumpMapList(mapCollection, "wabao_hanziku_withmap.txt", true);
		
		
		zikuBuilder.dumpAnsweredZhzbZiku("wabao_ziku.txt");
		
//		zikuBuilder.dumpUnansweredZiku("wabao_hanziku_unknown_hard.txt", true);
//		zikuBuilder.identifyUnanswered();
//		zikuBuilder.dumpUnansweredZiku("hanziku_unknown_identify.txt", true);

	}

	private static void correctRoomName(Collection<TreasureMap> maps) {
		List<TreasureMap> newMaps = new ArrayList<TreasureMap>();
		Roomku roomku = Roomku.createFromFile("resource_wabao/all_room.txt");
		for(TreasureMap map : maps) {
			boolean newRoom = true;
			List<Room> rooms = roomku.getRooms(map.getRoom());
			if (rooms != null) {
				for(Room existRoom : rooms) {
					if(existRoom.getArea().equals(map.getAreaName())) {
						map.setRoom(existRoom.getName());
						newRoom = false;
						break;
					}
				}
			}
						
			if(newRoom) {
				newMaps.add(map);
			}
		}
		
		if(newMaps.size() > 0) {
			System.out.println("Found new rooms: " + newMaps.size());
			dumpMapList(newMaps, "resource_wabao/new_room.txt", true);
			System.out.println("New rooms file: resource_wabao/new_room.txt");
		}
		
	}


	public int identify(TreasureMapHanzi hanzi) {
		TreasureMapHanzi result = new TreasureMapHanzi();
		int delta = identifyByHanziku(hanzi, _hanziku.values(), result);
		hanzi.setWenzi(result.getWenzi());
		return delta;
	}

	public static void identifyUnansweredByHanziku(Collection<TreasureMapHanzi> unansweredHanziku) {
		TreasureMapHanzikuBuilder zikuBuilder = new TreasureMapHanzikuBuilder();
		zikuBuilder.addMapku("resource/wabao_hanziku.txt", new LinkedHashMap<String, TreasureMap>());
		Collection<TreasureMapHanzi> hanziku = zikuBuilder.getHanziku().values();
		identifyUnansweredByHanziku(unansweredHanziku, hanziku, 9999);
	}

	public static void identifyUnansweredByHanziku(Collection<TreasureMapHanzi> unansweredHanziku, Collection<TreasureMapHanzi> hanziku, int deltaLimit) {
		System.out.println("Use wabao_hanziku to identify: " +  unansweredHanziku.size());
		int count = 0;
		int deltaMin = 9999;
		
		for(TreasureMapHanzi hz : unansweredHanziku) {
			TreasureMapHanzi result = new TreasureMapHanzi(null, hz.getBitMap());
			int delta = identifyByHanziku(hz, hanziku, result);
			
			if((delta < deltaMin) && (delta < deltaLimit)) {
				hz.setWenzi(result.getWenzi());
			} else {
				hz.setWenzi(null);
			}
			
			count++;
			if(count % 100 == 0) {
				System.out.println("identified " + count);
			}
		}
		System.out.println("Identification done!");
	}
	
	public static int identifyByHanziku(TreasureMapHanzi hz2, Collection<TreasureMapHanzi> hanziku, TreasureMapHanzi result) {
		//like match
		int deltaMin = 9999;
		for(TreasureMapHanzi hz : hanziku) {
			int delta = computeDelta(hz, hz2);
			if(delta < deltaMin) {
				deltaMin = delta;
				result.setWenzi(hz.getWenzi());
				result.setBitMap(hz.getFormalBitMap());
				if(deltaMin == 0) {
					break;
				}
			}
		}

		return deltaMin;
	}
	
	public static void identifyUnansweredByZiku(Collection<TreasureMap> unansweredMaps, Collection<TreasureMapZi> ziku) {
		identifyUnansweredByZiku(unansweredMaps, ziku, 9999);
	}
	
	public static void identifyUnansweredByZiku(Collection<TreasureMap> unansweredMaps, Collection<TreasureMapZi> ziku, int deltaLimit) {
		System.out.println("Use ziku to identify map: " +  unansweredMaps.size());
		int count = 0;
		int delta = 9999;
		
		for(TreasureMap map : unansweredMaps) {
			TreasureMapHanzi[] area = map.getArea();
			for(int i = 0; i < area.length; i++) {
				TreasureMapHanzi hz = area[i];
				TreasureMapHanzi hz1 = new TreasureMapHanzi(null, hz.getBitMap());
				int zhzbDelta = identifyByZiku(hz.getBitCounts(), ziku, hz1);
				
				if((zhzbDelta < delta) && (zhzbDelta < deltaLimit)) {
					hz.setWenzi(hz1.getWenzi());
				} else {
					hz.setWenzi(null);
				}
				
				count++;
				if(count % 100 == 0) {
					System.out.println("identified " + count);
				}
			}
		}
		System.out.println("Identification done!");
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
	

	/**
	 * Compute delta of two hanzi
	 * @deprecated too slow, please use computeDelta
	 * @param hanzi1
	 * @param hanzi2
	 * @return
	 */
	private static int computeDeltaOld(TreasureMapHanzi hanzi1, TreasureMapHanzi hanzi2){
		int delta = 0;
		Long[] bitMap1 = hanzi1.getFormalBitMap();
		Long[] bitMap2 = hanzi2.getFormalBitMap();
		
		for (int i = 0; i < 24; i++) {
			Long lineXor = bitMap1[i] ^ bitMap2[i];
			String xorResult = Long.toBinaryString(lineXor);
			System.out.println(xorResult);
			String[] parts = xorResult.split("0+");
			for(String part : parts) {
				delta += part.length() * part.length();
			}
			System.out.println(delta);
		}
		
		return delta;
	}
	
	/**
	 * Compute delta of two hanzi
	 * delta = Sum(line_delta);
	 * line_delta = xor(line of two hanzi),split with 0,sum(parts.len^2);
	 * such as xor=110110111011000000, line_delta=21
	 * @param hanzi1
	 * @param hanzi2
	 * @return
	 */
	private static int computeDelta(TreasureMapHanzi hanzi1, TreasureMapHanzi hanzi2){
		int delta = 0;
		Long[] bitMap1 = hanzi1.getFormalBitMap();
		Long[] bitMap2 = hanzi2.getFormalBitMap();
		
		for (int i = 0; i < 24; i++) {
			Long lineXor = bitMap1[i] ^ bitMap2[i];
//			System.out.println(Long.toBinaryString(lineXor));
			int previouseBitCount = 0;
			for(int j = 0; j < 24; j++) {
				if((lineXor & _bitMaskArray[j]) > 0) {
					previouseBitCount++;
				} else if(previouseBitCount > 0) {
					delta += previouseBitCount * previouseBitCount;
					previouseBitCount = 0;
				}
			}
			if(previouseBitCount > 0) {  //最后一位是1的情况
				delta += previouseBitCount * previouseBitCount;
			}
//			System.out.println(delta);
		}
		
		return delta;
	}
	
	
	
	public static Hashtable<String, TreasureMapZi> readZiku(String fileName) {
		Hashtable<String, TreasureMapZi> ziku = new Hashtable<String, TreasureMapZi>();
		InputStream is = null;
		File file = new File(fileName);
		if(file.exists()) {
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    String encoding = "GBK";
	    BufferedReader reader = null;
	    
	    
	    try {
	    	InputStreamReader read = new InputStreamReader(is, encoding);//考虑到编码格式
	    	reader = new BufferedReader(read);

	    	String line = null; //蟠 3,3,4,3,15,11,14,21,19,15,16,16,17,12,10,12,11,14,15,9,9,8,4,2,6,5,8,9,9,8,9,10,8,6,9,9,15,14,14,11,12,14,13,10,12,12,11,9,7,7,4,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
	        while ((line = reader.readLine()) != null) {
	        	if (line.length() < 50) {
	        		continue;
	        	}
	        	
	        	String bitCounts = line.substring(2);
	        	String newZi = line.substring(0, 1);
	        	
	        	TreasureMapZi hz = new TreasureMapZi(newZi, bitCounts);
	        	TreasureMapZi old = ziku.put(bitCounts, hz);
	        	if(old != null && !old.getWenzi().equals(hz.getWenzi())) {
	        		System.out.println("Conflict!");
	        		System.out.println(bitCounts);
	        		System.out.println(old.getWenzi() + "," + newZi);
	        	}
	        	
	        }
	        reader.close();
	        return ziku;
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
	    
	    return null;
		
	}
	
	public static void createAllRoomsFromTimeoutTin() {
		InputStream is = null;
		File file = new File("resource_wabao/timeout.tin");
		if(file.exists()) {
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    String encoding = "GBK";
	    BufferedReader reader = null;
	    OutputStreamWriter writer = null;
	    
	    try {
	    	InputStreamReader read = new InputStreamReader(is, encoding);
	    	reader = new BufferedReader(read);

			FileOutputStream allRoom = new FileOutputStream("resource_wabao/all_room.txt");
			writer = new OutputStreamWriter(allRoom, "GBK");
			
	    	String line = null; //
	        while ((line = reader.readLine()) != null) {
	        	int beginIndex = line.indexOf('{');
	        	int endIndex = line.indexOf('}');
	        	if (beginIndex != -1 && endIndex != -1 && endIndex > beginIndex) {
	        		writer.write(line.substring(beginIndex + 1, endIndex) + "\r\n");
	        	}
	        }
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
	        
	        if(writer != null){
	            try {
	            	writer.close();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	
	public static LinkedHashMap<String, TreasureMapZi> readZikuFile(String fileName) {
		return readZikuFile(fileName, null);
	}
	
	public static LinkedHashMap<String, TreasureMapZi> readZikuFile(String fileName, LinkedHashMap<String, TreasureMapZi> ziku) {
		if(ziku == null) {
			ziku = new LinkedHashMap<String, TreasureMapZi>();
		}
		
		InputStream is = null;
		File file = new File(fileName);
		if(file.exists()) {
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    String encoding = "GBK";
	    BufferedReader reader = null;
	    
	    
	    try {
	    	InputStreamReader read = new InputStreamReader(is, encoding);//考虑到编码格式
	    	reader = new BufferedReader(read);

	    	String line = null; //掖,3,5,4,10,14,13,11,9,10,14,17,14,15,11,9,8,10,10,9,10,8,7,8,3,4,6,5,8,9,9,12,15,20,18,9,6,7,8,13,14,15,14,15,10,8,4,2,1,
	    	int lineNum = 0;
	    	int duplicateCount = 0;
	    	int conflictCount = 0;
	        while ((line = reader.readLine()) != null) {
	        	lineNum++;
	        	if (line.length() < 50) {
	        		System.out.println("Invalid line[" + lineNum + "]:" + line);
	        		continue;
	        	}
	        	
	        	String bitCounts = line.substring(2);
	        	String newZi = line.substring(0, 1);
	        	
	        	String[] bitCountStrings = bitCounts.split(",");
	        	int bitCountLength = TreasureMapHanzi.RowSize + TreasureMapHanzi.ColSize;
	        	if(bitCountStrings.length != bitCountLength) {
	        		System.out.println("Ingore too long line[" + lineNum + "]:" + line);
	        		continue;
	        	}

//	        	//比对行点总数和列点总数
//	        	int rowBits = hz.getTotalBits();
//	        	int[] bitCount = hz.getBitCount();
//	        	int colBits = 0;
//	        	for(int i = TreasureMapHanzi.RowSize; i < TreasureMapHanzi.RowSize + TreasureMapHanzi.ColSize; i++) {
//	        		colBits += bitCount[i];
//	        	}
//	        	if(rowBits != colBits) {
//	        		System.out.println("Ingore row and col total bits mismatch line[" + lineNum + "]:" + line);
//	        		continue;
//	        	}
	        	
	        	//去除含单点的
	        	if(bitCounts.contains(",0,1,0") || bitCounts.startsWith("1,0,") || bitCounts.endsWith(",0,1,")) {
	        		System.out.println("Ingore contain single point line[" + lineNum + "]:" + line);
	        		continue;
	        	}
	        	
	        	TreasureMapZi hz = new TreasureMapZi(newZi, bitCounts);
	        	TreasureMapZi old = ziku.get(bitCounts);
	        	if(old != null) {
	        		if(old.getWenzi().equals(hz.getWenzi())) {
	        			duplicateCount++;
//	        			System.out.println("Ingore duplicated line[" + lineNum + "]:" + line);
	        		} else {
	        			conflictCount++;
	        			System.out.println("Conflict!");
		        		System.out.println(bitCounts);
		        		System.out.println(old.getWenzi() + "," + newZi);
	        		}
        			continue;	        			
	        	}
	        	ziku.put(bitCounts, hz);
	        }
	        reader.close();
	        
	        System.out.println("Valid: " + ziku.size() + ", Duplicated: " + duplicateCount + ", Conflict:" + conflictCount);
	        return ziku;
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
	    
	    return null;
	}
}
