package songbao;

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
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;

import duishi.Program;
import wabao.TreasureMapHanzi;
import wabao.TreasureMapZi;
import xyj.Room;
import xyj.Roomku;

public class TreasureShovelMapHanzikuBuilder {


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
	

	private static Roomku _roomku = Roomku.createFromFile("all_room.txt");
	

	public void addMapku(String mapkuFile, LinkedHashMap<String, TreasureShovelMap> rightMaps, LinkedHashMap<String, TreasureShovelMapHanzi> hanziku){
		InputStream is = null;
		File file = new File(mapkuFile);
		if(file.exists()) {
			System.out.println("Adding mapku file: " + file.getAbsolutePath());
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
	    	InputStreamReader read = new InputStreamReader(is, encoding);//
	    	reader = new BufferedReader(read);

	    	String line = null; //
	        while ((line = reader.readLine()) != null) {
	        	lineCount++;
	        	String[] parts = line.split("-");
	        	if(parts.length != 2 || parts[0].length() > 4) {
					System.out.println("Error near by line " + lineCount);
	        		continue;
	        	}
	        	
	        	String roomName = parts[0];
	        	String areaName = parts[1];
	        	Room room = new Room(roomName, areaName);
	        	if (!_roomku.isValidByRoomNameLength(room, 4)) {
					System.out.println("Error near by line " + lineCount);
	        		continue;
	        	}
	        	
	        	TreasureShovelMapHanzi[] hanziList = new TreasureShovelMapHanzi[roomName.length()];
	        	
	        	for(int i = 0; i < hanziList.length; i++) {
	        		// ignore hanzi picture lines
	        		do {
	        			line = reader.readLine();
	        			lineCount++;
	        		} while (!isMapHanziLine(line));
	        		
		        	totalCountFile++;
		        	String[] lineParts = line.split(" ");
		        	String wenzi = lineParts[0];
		        	String bitMapString = lineParts[1];
		        	
		        	TreasureShovelMapHanzi hz = new TreasureShovelMapHanzi(wenzi, bitMapString);
		        	hanziList[i] = hz;
	        	}
	        	
	        	TreasureShovelMap map = new TreasureShovelMap(areaName, hanziList);
	        	map.setRoomName(roomName); // for identified file
	        	
	        	// check conflict
	        	for(int i = 0; i < hanziList.length; i++) {
	        		TreasureShovelMapHanzi hz = hanziList[i];
	        		String bitMapString = hz.getBitMapString();
	        		TreasureMapHanzi oldHz = hanziku.get(bitMapString);
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
			        	hanziku.put(bitMapString, hz);
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
		System.out.println("Now zikuCount:" + hanziku.size());
//	    return null;
	}
	
	private boolean isMapHanziLine(String line) {
		if(line.length() > 50 && line.indexOf(';') > 2){
			return true;
		}
		return false;
	}


	
	private static void dumpMapList(Collection<TreasureShovelMap> mapList, String file, boolean withPiture) {
		try {
			FileOutputStream haizi = new FileOutputStream(file);
			OutputStreamWriter mapWriter = new OutputStreamWriter(haizi, "GBK");
			for(TreasureShovelMap map : mapList){
				mapWriter.append(map.getRoomName() + "-" + map.getArea() + "\n");
				TreasureShovelMapHanzi[] hanziList = map.getRoom();
				if(withPiture){
					for(int line = 0; line < TreasureShovelMapHanzi.RowSize; line++) {
						for(int i = 0;i < hanziList.length;i++) {
							String hzLineMap = "";
							Long hzLine = hanziList[i].getBitMap()[line];
							for (int j = TreasureShovelMapHanzi.ColSize - 1; j>=0; j--) {
								if((hzLine & (1L<<j)) != 0) {
									hzLineMap += "¡ñ";
								} else {
									hzLineMap += " ";
								}
							}
							mapWriter.append(hzLineMap);
						}
						mapWriter.append('\n');
					}
				}
					
				
				for(int i = 0;i < hanziList.length;i++) {
					TreasureShovelMapHanzi hz = hanziList[i];
					mapWriter.append(hz.getWenzi() + " " +  hz.getBitMapString() + "\n");
				}
			}
			mapWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private static void dumpZhzbHanziList(Collection<TreasureShovelMapHanzi> hanziList, String file) {
		try {
			FileOutputStream haizi = new FileOutputStream(file);
			OutputStreamWriter mapWriter = new OutputStreamWriter(haizi, "GBK");
			for(TreasureShovelMapHanzi hz : hanziList){
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
	

	private static void dumpZhzbHanziList(Collection<TreasureShovelMap> mapList, String file, boolean withPiture) {
		try {
			FileOutputStream haizi = new FileOutputStream(file);
			OutputStreamWriter mapWriter = new OutputStreamWriter(haizi, "GBK");
			for(TreasureShovelMap map : mapList){
				mapWriter.append(map.getRoomName() + " - " + map.getArea() + "\n");
				TreasureShovelMapHanzi[] hanziList = map.getRoom();
				for(int i = 0;i < hanziList.length;i++) {
					TreasureShovelMapHanzi hz = hanziList[i];
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
	
//		buildHanzikuWithOriginMapFile_Init("resource_songbao/songbao001.txt");
//		buildHanzikuWithOriginMapFile_Init("resource_songbao/songbao_origin_20150831.txt");
		
//		buildMapku("resource_songbao/songbao001.txt_right");
//		buildMapku("resource_songbao/songbao_origin_20150831_01.txt_right");
//		buildMapku("resource_songbao/songbao_origin_20150831_10.txt_right");
//		buildMapku("resource_songbao/songbao_origin_20150831_unknown_04_identified.txt");
		
//		identifyUnansweredFile("resource_songbao/songbao_origin_20150831_unknown_06.txt");
//		buildMapku("resource_songbao/songbao_origin_20150831_unknown_06.txt_identified");
//		identifyUnansweredFile("resource_songbao/songbao_origin_20150831_unknown_07.txt");
//		buildMapku("resource_songbao/songbao_origin_20150831_unknown_07.txt_identified");
//		identifyUnansweredFile("resource_songbao/songbao_origin_20150831_unknown_08.txt");
//		buildMapku("resource_songbao/songbao_origin_20150831_unknown_08.txt_identified");
//		identifyUnansweredFile("resource_songbao/songbao_origin_20150831_unknown_09.txt");
//		buildMapku("resource_songbao/songbao_origin_20150831_unknown_09.txt_identified");
//		identifyUnansweredFile("resource_songbao/songbao_origin_20150831_unknown_02.txt");
//		buildMapku("resource_songbao/songbao_origin_20150831_unknown_02.txt_identified");
//		identifyUnansweredFile("resource_songbao/songbao_origin_20150831_unknown_03.txt");
//		buildMapku("resource_songbao/songbao_origin_20150831_unknown_03.txt_identified");
//		identifyUnansweredFile("resource_songbao/songbao_origin_20150831_unknown_05.txt");
//		buildMapku("resource_songbao/songbao_origin_20150831_unknown_05.txt_identified");

//		buildHanzikuWithOriginMapFile("resource_songbao/songbao_origin_20150902.txt");
//		buildMapku("resource_songbao/songbao_origin_20150902_unknown_01.txt_identified");
		
//		identifyUnansweredFile("resource_songbao/songbao_origin_20150902_unknown_02.txt_identified");
//		buildMapku("resource_songbao/songbao_origin_20150902_unknown_02.txt_identified");
//		identifyUnansweredFile("resource_songbao/songbao_origin_20150902_unknown_03.txt");
//		buildMapku("resource_songbao/songbao_origin_20150902_unknown_03.txt_identified");
//		identifyUnansweredFile("resource_songbao/songbao_origin_20150902_unknown_04.txt");
//		buildMapku("resource_songbao/songbao_origin_20150902_unknown_04.txt_identified");
//		identifyUnansweredFile("resource_songbao/songbao_origin_20150902_unknown_05.txt");
		buildMapku("resource_songbao/songbao_origin_20150902_unknown_05.txt_identified");
		
		
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





	public static void buildHanzikuWithOriginMapFile_Init(String fileName) {
		ArrayList<TreasureShovelMap> result = TreasureShovelMapReader.parseOriginMapFile(fileName);

		ArrayList<TreasureShovelMap> unknownMaps = new ArrayList<TreasureShovelMap>();
		ArrayList<TreasureShovelMap> rightMaps = identifyArea(result, unknownMaps);
		dumpMapList(rightMaps, fileName + "_right", true);
		dumpMapList(unknownMaps, fileName + "_unknown", true);
		
//		ArrayList<TreasureShovelMap> unknownMaps = new ArrayList<TreasureShovelMap>();
//		ArrayList<TreasureShovelMap> rightMaps = identify(result, unknownMaps);
//		dumpMapList(rightMaps, fileName + "_right", false);
//		dumpMapList(unknownMaps, fileName + "_unknown", true);
		
//		buildMapku(fileName + "_right");
	}
	
	
	public static void buildHanzikuWithOriginMapFile(String fileName) {
		ArrayList<TreasureShovelMap> unansweredMaps = TreasureShovelMapReader.parseOriginMapFile(fileName);

		Hashtable<String, TreasureMapZi> ziku = readZiku("songbao_ziku.txt");
		identifyUnansweredByZiku(unansweredMaps, ziku.values());
		
		dumpMapList(unansweredMaps, fileName + "_identified", true);
	}
	
	private static ArrayList<TreasureShovelMap> identifyArea(ArrayList<TreasureShovelMap> originMaps, ArrayList<TreasureShovelMap> unknownMaps) {
		ArrayList<TreasureShovelMap> identifiedMaps = new ArrayList<TreasureShovelMap>();
		
		//identify by all room
		Roomku roomku = Roomku.createFromFile("resource_wabao/all_room.txt");
		
		for(TreasureShovelMap map : originMaps) {
			boolean identified = false;
			
	        //1. identify map by unique
	        String area = roomku.normalizeArea(map.getArea());
	        if (area != null) {
				identified = true;
	        }
	        
	
			if (identified) {
//				map.setRoom(identifiedRoom.getName());
				map.setArea(area);
				identifiedMaps.add(map);
			} else {
				unknownMaps.add(map);
			}
		}
		
		return identifiedMaps;
	}
	
	private static ArrayList<TreasureShovelMap> identify(ArrayList<TreasureShovelMap> originMaps, ArrayList<TreasureShovelMap> unknownMaps) {
		ArrayList<TreasureShovelMap> identifiedMaps = new ArrayList<TreasureShovelMap>();
		
		//identify by all room
		Roomku roomku = Roomku.createFromFile("resource_wabao/all_room.txt");
//		Hashtable<String, TreasureMapZi> ziku = readZiku("wabao_ziku.txt");
		
		for(TreasureShovelMap map : originMaps) {
			boolean identified = false;
			
//	        //1. identify map by unique
//	        String area = roomku.normalizeArea(map.getArea());
//	        if (area != null) {
//				identified = true;
//	        }
//	        
//	
//			if (identified) {
////				map.setRoom(identifiedRoom.getName());
//				map.setArea(identifiedRoom.getArea());
//				identifiedMaps.add(map);
//			} else {
//				unknownMaps.add(map);
//			}
		}
		
		return identifiedMaps;
	}
	


	private static Room identifyUnansweredByZiku(TreasureShovelMap map, Collection<TreasureMapZi> ziku) {
		int delta = 9999;
		
		TreasureShovelMapHanzi[] area = map.getRoom();
		for(int i = 0; i < area.length; i++) {
			TreasureShovelMapHanzi hz = area[i];
			TreasureShovelMapHanzi hz1 = new TreasureShovelMapHanzi(null, hz.getBitMap());
			int zhzbDelta = identifyByZiku(hz.getBitCounts(), ziku, hz1);
			
			if((zhzbDelta < delta)) {
				hz.setWenzi(hz1.getWenzi());
			} else {
				hz.setWenzi(null);
			}
		}
			
		return new Room(map.getRoomName(), map.getArea());
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
	    	InputStreamReader read = new InputStreamReader(is, encoding);//
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
		ArrayList<TreasureShovelMap> unansweredMaps = readUnknownFile(unansweredFile);
		Hashtable<String, TreasureMapZi> ziku = readZiku("songbao_ziku.txt");
		identifyUnansweredByZiku(unansweredMaps, ziku.values());
		
		dumpMapList(unansweredMaps, unansweredFile + "_identified", true);
		
	}
	
	public static ArrayList<TreasureShovelMap> readUnknownFile(String mapkuFile){
		ArrayList<TreasureShovelMap> unknownMaps = new ArrayList<TreasureShovelMap>();
		
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
//	    int totalCountFile = 0;
	    int lineCount = 0;
	    
	    try {
	    	InputStreamReader read = new InputStreamReader(is, encoding);//¿¼ÂÇµ½±àÂë¸ñÊ½
	    	reader = new BufferedReader(read);

	    	String line = null; //
	        while ((line = reader.readLine()) != null) {
	        	lineCount++;
	        	String[] parts = line.split("-");
	        	if(parts.length != 2) {
					System.out.println("Error near by line " + lineCount);
	        		continue;
	        	}
	        	
	        	String room = parts[0]; //such as: nullnull nullnullnull
	        	boolean roomRecognized = false;
	        	int hanziCount = room.length()/4;
	        	if(room.length()<=4) {
	        		roomRecognized = true;
	        		hanziCount = room.length();
	        	}
	        	String areaName = parts[1]; 
	        	TreasureShovelMapHanzi[] hanziList = new TreasureShovelMapHanzi[hanziCount];
	        	
	        	for(int i = 0; i < hanziList.length; i++) {
	        		line = reader.readLine();
	        		lineCount++;
	        		while (line != null && ((!roomRecognized && !line.startsWith("null ")) || (roomRecognized && !room.contains(line.substring(0, 1))))) {
	        			line = reader.readLine();
	        			lineCount++;
	        		}
	        		
//		        	totalCountFile++;
		        	String wenzi = null; //line.substring(0, 1);
		        	String bitMapString = roomRecognized? line.substring(2) : line.substring(5);
		        	
		        	TreasureShovelMapHanzi hz = new TreasureShovelMapHanzi(wenzi, bitMapString);
		        	hanziList[i] = hz;
	        	}
	        	
	        	TreasureShovelMap map = new TreasureShovelMap(areaName, hanziList);
	        	unknownMaps.add(map);
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

	    return unknownMaps;
	}
	
	private static void buildMapku(String identifiedFile) {
		TreasureShovelMapHanzikuBuilder zikuBuilder = new TreasureShovelMapHanzikuBuilder();
		LinkedHashMap<String, TreasureShovelMap> rightMaps = new LinkedHashMap<String, TreasureShovelMap>();
		LinkedHashMap<String, TreasureShovelMapHanzi> hanziku = new LinkedHashMap<String, TreasureShovelMapHanzi>();
		zikuBuilder.addMapku("songbao_hanziku.txt", rightMaps, hanziku);
		System.out.println();

		zikuBuilder.addMapku(identifiedFile, rightMaps, hanziku);
		
		Collection<TreasureShovelMap> mapCollection = rightMaps.values();
//		correctRoomName(mapCollection);
		
		TreasureShovelMapHanzikuBuilder.dumpMapList(mapCollection, "songbao_hanziku.txt", false);
		TreasureShovelMapHanzikuBuilder.dumpMapList(mapCollection, "songbao_hanziku_withmap.txt", true);

		TreasureShovelMapHanzikuBuilder.dumpZhzbHanziList(hanziku.values(), "songbao_ziku.txt");
		
//		zikuBuilder.dumpUnansweredZiku("wabao_hanziku_unknown_hard.txt", true);
//		zikuBuilder.identifyUnanswered();
//		zikuBuilder.dumpUnansweredZiku("hanziku_unknown_identify.txt", true);

	}

	private static void correctRoomName(Collection<TreasureShovelMap> maps) {
//		List<TreasureShovelMap> newMaps = new ArrayList<TreasureShovelMap>();
//		Roomku roomku = Roomku.createFromFile("resource_wabao/all_room.txt");
//		for(TreasureShovelMap map : maps) {
//			boolean newRoom = true;
//			List<Room> rooms = roomku.getRooms(map.getRoom());
//			if (rooms != null) {
//				for(Room existRoom : rooms) {
//					if(existRoom.getArea().equals(map.getAreaName())) {
//						map.setRoom(existRoom.getName());
//						newRoom = false;
//						break;
//					}
//				}
//			}
//						
//			if(newRoom) {
//				newMaps.add(map);
//			}
//		}
//		
//		if(newMaps.size() > 0) {
//			System.out.println("Found new rooms: " + newMaps.size());
//			dumpMapList(newMaps, "resource_wabao/new_room.txt", true);
//			System.out.println("New rooms file: resource_wabao/new_room.txt");
//		}
		
	}


	public int identify(TreasureShovelMapHanzi hanzi) {
//		TreasureShovelMapHanzi result = new TreasureShovelMapHanzi();
//		int delta = identifyByHanziku(hanzi, _hanziku.values(), result);
//		hanzi.setWenzi(result.getWenzi());
//		return delta;
		return 0;
	}

	public static void identifyUnansweredByHanziku(Collection<TreasureShovelMapHanzi> unansweredHanziku) {
//		TreasureMapHanzikuBuilder zikuBuilder = new TreasureMapHanzikuBuilder();
//		zikuBuilder.addMapku("resource/wabao_hanziku.txt", new LinkedHashMap<String, TreasureShovelMap>());
//		Collection<TreasureShovelMapHanzi> hanziku = zikuBuilder.getHanziku().values();
//		identifyUnansweredByHanziku(unansweredHanziku, hanziku, 9999);
	}

	public static void identifyUnansweredByHanziku(Collection<TreasureShovelMapHanzi> unansweredHanziku, Collection<TreasureShovelMapHanzi> hanziku, int deltaLimit) {
		System.out.println("Use wabao_hanziku to identify: " +  unansweredHanziku.size());
		int count = 0;
		int deltaMin = 9999;
		
		for(TreasureShovelMapHanzi hz : unansweredHanziku) {
			TreasureShovelMapHanzi result = new TreasureShovelMapHanzi(null, hz.getBitMap());
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
	
	public static int identifyByHanziku(TreasureShovelMapHanzi hz2, Collection<TreasureShovelMapHanzi> hanziku, TreasureShovelMapHanzi result) {
		//like match
		int deltaMin = 9999;
		for(TreasureShovelMapHanzi hz : hanziku) {
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
	
	public static void identifyUnansweredByZiku(Collection<TreasureShovelMap> unansweredMaps, Collection<TreasureMapZi> ziku) {
		identifyUnansweredByZiku(unansweredMaps, ziku, 9999);
	}
	
	public static void identifyUnansweredByZiku(Collection<TreasureShovelMap> unansweredMaps, Collection<TreasureMapZi> ziku, int deltaLimit) {
		System.out.println("Use ziku to identify map: " +  unansweredMaps.size());
		int count = 0;
		int delta = 9999;
		
		for(TreasureShovelMap map : unansweredMaps) {
			map.setArea(_roomku.normalizeArea(map.getArea()));
			
			TreasureShovelMapHanzi[] roomHanziArray = map.getRoom();
			for(int i = 0; i < roomHanziArray.length; i++) {
				TreasureShovelMapHanzi hz = roomHanziArray[i];
				TreasureShovelMapHanzi hz1 = new TreasureShovelMapHanzi(null, hz.getBitMap());
				int zhzbDelta = identifyByZiku(hz.getBitCounts(), ziku, hz1);
				
				if((zhzbDelta < delta) && (zhzbDelta < deltaLimit)) {
					hz.setWenzi(hz1.getWenzi());
				} else {
					hz.setWenzi(null);
				}
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
						if (areaRoom.substring(i, i+1).equals(roomHanziArray[i].getWenzi())) {
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
			
			count++;
			if(count % 100 == 0) {
				System.out.println("identified " + count);
			}
		}
		System.out.println("Identification done!");
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
	

	/**
	 * Compute delta of two hanzi
	 * @deprecated too slow, please use computeDelta
	 * @param hanzi1
	 * @param hanzi2
	 * @return
	 */
	private static int computeDeltaOld(TreasureShovelMapHanzi hanzi1, TreasureShovelMapHanzi hanzi2){
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
	private static int computeDelta(TreasureShovelMapHanzi hanzi1, TreasureShovelMapHanzi hanzi2){
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
			if(previouseBitCount > 0) {  
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
	    	InputStreamReader read = new InputStreamReader(is, encoding);//
	    	reader = new BufferedReader(read);

	    	String line = null; // 3,3,4,3,15,11,14,21,19,15,16,16,17,12,10,12,11,14,15,9,9,8,4,2,6,5,8,9,9,8,9,10,8,6,9,9,15,14,14,11,12,14,13,10,12,12,11,9,7,7,4,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
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
	    	InputStreamReader read = new InputStreamReader(is, encoding);//
	    	reader = new BufferedReader(read);

	    	String line = null; //Ò´,3,5,4,10,14,13,11,9,10,14,17,14,15,11,9,8,10,10,9,10,8,7,8,3,4,6,5,8,9,9,12,15,20,18,9,6,7,8,13,14,15,14,15,10,8,4,2,1,
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
	        	int bitCountLength = TreasureShovelMapHanzi.RowSize + TreasureShovelMapHanzi.ColSize;
	        	if(bitCountStrings.length != bitCountLength) {
	        		System.out.println("Ingore too long line[" + lineNum + "]:" + line);
	        		continue;
	        	}

//	        	//
//	        	int rowBits = hz.getTotalBits();
//	        	int[] bitCount = hz.getBitCount();
//	        	int colBits = 0;
//	        	for(int i = TreasureShovelMapHanzi.RowSize; i < TreasureShovelMapHanzi.RowSize + TreasureShovelMapHanzi.ColSize; i++) {
//	        		colBits += bitCount[i];
//	        	}
//	        	if(rowBits != colBits) {
//	        		System.out.println("Ingore row and col total bits mismatch line[" + lineNum + "]:" + line);
//	        		continue;
//	        	}
	        	
	        	//È¥ï¿½ï¿½ï¿½ï¿½
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
