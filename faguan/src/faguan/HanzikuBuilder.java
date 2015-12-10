package faguan;

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
import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedHashMap;

public class HanzikuBuilder {


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
	
	private LinkedHashMap<String,Hanzi> _hanziku = new LinkedHashMap<String,Hanzi>();
	private LinkedHashMap<String,Hanzi> _unansweredHanziku = new LinkedHashMap<String,Hanzi>();
	
	
	
	public void addZiku(String zikuFile){
//		Hashtable<String,Hanzi> ziku = new Hashtable<String,Hanzi>();
		InputStream is = null;
		File file = new File(zikuFile);
		if(file.exists()) {
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
    		System.out.println("File doesn't exit! " + zikuFile);
			return;//return ziku;
		}
		
	    String encoding = "GBK";
	    BufferedReader reader = null;
	    int totalCountFile = 0;
	    int addedCount = 0;
	    int conflictCount = 0;
	    
	    try {
	    	InputStreamReader read = new InputStreamReader(is, encoding);//考虑到编码格式
	    	reader = new BufferedReader(read);

	    	String line = null; //霸 ffcfff000;3ffcffc3000;ff3ff0ff000;ff03f0000;c3fcf3c0f000;fff03c3f0300;ffff3cf03f00;3fff3cc0ff00;f3ffffff0300;c0fc3ffc3300;3f0fc3f3fc0;ff0f0fc0c0;ffcfc0c30c0;fffc3cc3f30;3fffffc030;33ffff003c;ffffffc00c;f3ff0fc03c;3ffcffcf3f;3c3f0f00ff;fff03003f;3ffcf0f0000;f00f0f0000;fcf0000;
	        while ((line = reader.readLine()) != null) {
	        	totalCountFile++;
	        	String wenzi = line.substring(0, 1);
	        	String bitMapString = line.substring(2);
	        	
	        	Hanzi hz = new Hanzi(wenzi, bitMapString);
//	        	ziku.put(bitMapString, hz);
	        	bitMapString = hz.getBitMapString();
	        	if(_hanziku.containsKey(bitMapString)){
	        		Hanzi oldHz = _hanziku.get(bitMapString);
		        	if(!oldHz.getWenzi().equals(wenzi) ) {
		        		conflictCount++;
		        		System.out.println("Conflict!");
		        		System.out.println(oldHz.getDisplayString());
		        		System.out.println("Ziku: " + oldHz.getWenzi() + ", new: " + wenzi);
		        		System.out.println(bitMapString);
		        	}
	        		continue;
	        	}
	        	addedCount++;
	        	_hanziku.put(bitMapString, hz);
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
	
	public void readOriginAnswer(String answerFile){
		InputStream is = null;
		File file = new File(answerFile);
		if(file.exists()) {
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
    		System.out.println("File doesn't exit! " + answerFile);
			return;//return ziku;
		}
		
	    String encoding = "GBK";
	    BufferedReader reader = null;
	    int totalCountFile = 0;
	    int addedCount = 0;
	    int conflictCount = 0;
	    int unknownCount = 0;
	    
	    try {
	    	InputStreamReader read = new InputStreamReader(is, encoding);//考虑到编码格式
	    	reader = new BufferedReader(read);

	    	String line = null; //霸|null
	    	String bitMapLine = null;//3c000cc0f00;f0c0ffff00;ffcfff3c00;f0ff0f3c00;3f0f00f3c00;3c0f00f3c00;3ccffcf3c00;f0fffcc3ffc;3cfff00f03ff;fff3c0c00c0;3ffc3c0cfff0;3c3fffffc0;f03fcfc3c0;3c33c0f0f00;3ff3c0f0f00;ff03cf3fc00;f003fc03f00;ffc03f00;fff0033c0;ffff00c3f0;ff0cf03c0fc;f000f0f003f;f3c003f;ff00000; null
	    	String wenzi = null;
	        while ((line = reader.readLine()) != null) {
	        	if(line.length() == 1) {
	        		totalCountFile++;
	        		wenzi = line;
	        	} else if (line.equals("null")) {
	        		totalCountFile++;
	        		wenzi = null;
	        	} else {
	        		continue;
	        	}
	        	bitMapLine = reader.readLine();
	        	String bitMapString = bitMapLine.split(" ")[0];
	        	if(bitMapString.equals("null") || bitMapString.length() == 1) {
	        		bitMapString = bitMapLine.split(" ")[1];
	        	}
	        	
	        	Hanzi hz = new Hanzi(wenzi, bitMapString);
//	        	ziku.put(bitMapString, hz);
	        	bitMapString = hz.getBitMapString();
	        	if(_hanziku.containsKey(bitMapString)){
	        		Hanzi oldHz = _hanziku.get(bitMapString);
		        	if(wenzi!= null && !oldHz.getWenzi().equals(wenzi) ) {
		        		conflictCount++;
		        		System.out.println("Conflict!");
		        		System.out.println(oldHz.getDisplayString());
		        		System.out.println("Ziku: " + oldHz.getWenzi() + ", new: " + wenzi);
		        		System.out.println(bitMapString);
		        	}
	        		continue;
	        	} else if (_unansweredHanziku.containsKey(bitMapString)) {
	        		System.out.println("Unknown again: " + bitMapString);
	        	}
	        	
	        	if(wenzi != null) {
		        	addedCount++;
		        	_hanziku.put(bitMapString, hz);
	        	} else {
	        		unknownCount++;
	        		_unansweredHanziku.put(bitMapString, hz);
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
	    
		System.out.println("totalCount: " + totalCountFile + ", added:" + addedCount + ", conflict: " + conflictCount + ",unknown: " + unknownCount);
		System.out.println("Now zikuCount: " + _hanziku.size() + ", unknown: " + _unansweredHanziku.size());
	}

	public static LinkedHashMap<String,Hanzi> readUnknownFile(String unknownFile){
		LinkedHashMap<String,Hanzi> unknownHanzis = new LinkedHashMap<String,Hanzi>();
		InputStream is = null;
		File file = new File(unknownFile);
		if(file.exists()) {
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
    		System.out.println("File doesn't exit! " + unknownFile);
			return unknownHanzis;//return ziku;
		}
		
	    String encoding = "GBK";
	    BufferedReader reader = null;
	    int totalCountFile = 0;
	    int duplicatedCount = 0;
	    
	    try {
	    	InputStreamReader read = new InputStreamReader(is, encoding);//考虑到编码格式
	    	reader = new BufferedReader(read);

	    	String line = null; //霸|null
	    	String bitMapLine = null;//3c000cc0f00;f0c0ffff00;ffcfff3c00;f0ff0f3c00;3f0f00f3c00;3c0f00f3c00;3ccffcf3c00;f0fffcc3ffc;3cfff00f03ff;fff3c0c00c0;3ffc3c0cfff0;3c3fffffc0;f03fcfc3c0;3c33c0f0f00;3ff3c0f0f00;ff03cf3fc00;f003fc03f00;ffc03f00;fff0033c0;ffff00c3f0;ff0cf03c0fc;f000f0f003f;f3c003f;ff00000; null
	        while ((line = reader.readLine()) != null) {
	        	if(line.equals("null") || line.length() == 1) {
	        		totalCountFile++;
	        	} else {
	        		continue;
	        	}
	        	bitMapLine = reader.readLine();
	        	String bitMapString = bitMapLine.split(" ")[0];
	        	if(bitMapString.equals("null") || bitMapString.length() == 1) {
	        		bitMapString = bitMapLine.split(" ")[1];
	        	}
	        	
	        	Hanzi hz = new Hanzi(null, bitMapString);
	        	Hanzi hzOld = unknownHanzis.put(bitMapString, hz);
	        	if(hzOld != null){
	        		duplicatedCount++;
	        		System.out.println("Duplicated line, bitMapString: " + bitMapString);
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
	    
		System.out.println("totalCount: " + totalCountFile + ", unknown:" + unknownHanzis.size() + ", duplicated: " + duplicatedCount);
		return unknownHanzis;
	}

	public void dumpAnsweredZiku(String file, boolean withPiture) {
		dumpHanziList(_hanziku.values(), file, withPiture);
	}
	
	public void dumpUnansweredZiku(String file, boolean withPiture) {
		dumpHanziList(_unansweredHanziku.values(), file, withPiture);
	}

	public void dumpAnsweredZhzbZiku(String file, boolean withPiture) {
		dumpZhzbHanziList(_hanziku.values(), file, withPiture);
	}
	
	public LinkedHashMap<String,Hanzi> getHanziku() {
		return _hanziku;
	}

	public void setHanziku(LinkedHashMap<String,Hanzi> hanziku) {
		this._hanziku = hanziku;
	}
	
	private static void dumpHanziList(Collection<Hanzi> hanziList, String file, boolean withPiture) {
		try {
			FileOutputStream haizi = new FileOutputStream(file);
			OutputStreamWriter haiziWriter = new OutputStreamWriter(haizi, "GBK");
			for(Hanzi hz : hanziList){
				if(withPiture){
					haiziWriter.append(hz.getDisplayString() + hz.getWenzi() + "\n");
				}
				haiziWriter.append(hz.getWenzi() + " " +  hz.getBitMapString() + "\n");
			}
			haiziWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private static void dumpZhzbHanziList(Collection<Hanzi> hanziList, String file, boolean withPiture) {
		try {
			FileOutputStream haizi = new FileOutputStream(file);
			OutputStreamWriter haiziWriter = new OutputStreamWriter(haizi, "GBK");
			for(Hanzi hz : hanziList){
				if(withPiture){
					haiziWriter.append(hz.getDisplayString() + hz.getWenzi() + "\r\n");
				}
				haiziWriter.append(hz.getWenzi() + "," +  hz.getBitCountString() + "\r\n");
			}
			haiziWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
//		buildAnswered();
		
//		buildUnanswered();
		
//		HanziReader hzReader = new HanziReader();
//		ArrayList<Hanzi> hzList = hzReader.parseZhzbAnswerOutputFile("resource/answer_all_20140910.txt");
//		dumpHanziList(hzList, "answer_all_20140910.txt", true);
		
//		buildHanziku();
		
//		identifyUnansweredOriginFile("resource/fbox_windyii's.txt", "resource/fbox_windyii's_identify.txt");
//		identifyUnansweredFile("resource/hanzi_unmatched.txt");
//		buildHanziku("resource/hanzi_unmatched.txt_identify");
		
//		identifyZhzbAnswerOutputFile("resource/answer_all_20140911.txt", "resource/answer_all_20140911.txt_identify");
//		buildHanziku("resource_origin/answer_all_20140911.txt_identify");

//		identifyZhzbAnswerOutputFile("resource_origin/answer_all_20140917.txt", "resource_origin/answer_all_20140917.txt_identify");
//		buildHanziku("resource_origin/answer_all_20140917.txt_identify");

//		identifyZhzbAnswerOutputFile("resource_origin/answer_all_20140924.txt", "resource_origin/answer_all_20140924.txt_identify");
//		buildHanziku("resource_origin/answer_all_20140924.txt_identify");

//		identifyZhzbAnswerOutputFile("resource_origin/answer_all_20140929.txt", "resource_origin/answer_all_20140929.txt_identify");
//		buildHanziku("resource_origin/answer_all_20140929.txt_identify");

//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20140925.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20140925.txt_wrong");

//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20140925.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20140925.txt_wrong");

//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20140929.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20140929.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20140929.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20140929.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20140929.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20140929.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20141006.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20141006.txt_wrong1");
//		identifyUnansweredFile("resource_origin/faguan_origin_windyii's_20141006.txt_wrong");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20141006.txt_wrong_identify");
		
//		identifyUnansweredFile("hanziku_unknown_hard.txt");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20141006.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20141006.txt_wrong1");
//		identifyUnansweredFile("resource_origin/faguan_origin_windyme's_20141006.txt_wrong");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20141006.txt_wrong_identify");

//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20141006.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20141006.txt_wrong1");
//		testIdentify();
//		identifyUnansweredFile("resource_origin/faguan_origin_dwfz's_20141006.txt_wrong");
//		testComputeDelta();
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20141006.txt_wrong_identify");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20141011.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20141011.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20141011.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20141011.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20141011.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20141011.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20141016.txt");
//		identifyUnansweredFile("resource_origin/faguan_origin_windyii's_20141016.txt_wrong");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20141016.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20141016.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20141016.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20141016.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20141016.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20141021.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20141021.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20141021.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20141021.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20141021.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20141021.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20141025.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20141025.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20141025.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20141025.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20141025.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20141025.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20141030.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20141030.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20141030.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20141030.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20141030.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20141030.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_firewing's_20141030.txt");
//		identifyUnansweredFile("resource_origin/faguan_origin_firewing's_20141030.txt_right");
//		buildHanziku("resource_origin/faguan_origin_firewing's_20141030.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yjfh's_20141030.txt");
//		buildHanziku("resource_origin/faguan_origin_yjfh's_20141030.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yqfx's_20141030.txt");
//		buildHanziku("resource_origin/faguan_origin_yqfx's_20141030.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20141104.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20141104.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20141104.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20141104.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20141104.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20141104.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20141109.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20141109.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20141109.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20141109.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20141109.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20141109.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_firewing's_20141110.txt");
//		identifyUnansweredFile("resource_origin/faguan_origin_firewing's_20141110.txt_right");
//		buildHanziku("resource_origin/faguan_origin_firewing's_20141110.txt_wrong");

//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yjfh's_20141110.txt");
//		buildHanziku("resource_origin/faguan_origin_yjfh's_20141110.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yqfx's_20141110.txt");
//		buildHanziku("resource_origin/faguan_origin_yqfx's_20141110.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20141116.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20141116.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20141116.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20141116.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20141116.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20141116.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_firewing's_20141118.txt");
//		buildHanziku("resource_origin/faguan_origin_firewing's_20141118.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yjfh's_20141118.txt");
//		buildHanziku("resource_origin/faguan_origin_yjfh's_20141118.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yqfx's_20141118.txt");
//		buildHanziku("resource_origin/faguan_origin_yqfx's_20141118.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20141122.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20141122.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20141122.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20141122.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20141122.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20141122.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_firewing's_20141126.txt");
//		buildHanziku("resource_origin/faguan_origin_firewing's_20141126.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_meiying's_20141126.txt");
//		buildHanziku("resource_origin/faguan_origin_meiying's_20141126.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yjfh's_20141126.txt");
//		buildHanziku("resource_origin/faguan_origin_yjfh's_20141126.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yqfx's_20141126.txt");
//		buildHanziku("resource_origin/faguan_origin_yqfx's_20141126.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20141127.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20141127.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20141127.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20141127.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20141127.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20141127.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_firewing's_20141205.txt");
//		buildHanziku("resource_origin/faguan_origin_firewing's_20141205.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_meiying's_20141205.txt");
//		buildHanziku("resource_origin/faguan_origin_meiying's_20141205.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yjfh's_20141205.txt");
//		buildHanziku("resource_origin/faguan_origin_yjfh's_20141205.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yqfx's_20141205.txt");
//		buildHanziku("resource_origin/faguan_origin_yqfx's_20141205.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20141206.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20141206.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20141206.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20141206.txt_wrong");

//		checkAutoAnsweredFile("resource_origin/huancan.txt");

//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20141206.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20141206.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20141212.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20141212.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20141212.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20141212.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20141212.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20141212.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_firewing's_20141212.txt");
//		buildHanziku("resource_origin/faguan_origin_firewing's_20141212.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_meiying's_20141212.txt");
//		buildHanziku("resource_origin/faguan_origin_meiying's_20141212.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yjfh's_20141212.txt");
//		buildHanziku("resource_origin/faguan_origin_yjfh's_20141212.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yqfx's_20141212.txt");
//		buildHanziku("resource_origin/faguan_origin_yqfx's_20141212.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20141216.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20141216.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20141216.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20141216.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20141216.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20141216.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_firewing's_20141222.txt");
//		buildHanziku("resource_origin/faguan_origin_firewing's_20141222.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_meiying's_20141222.txt");
//		buildHanziku("resource_origin/faguan_origin_meiying's_20141222.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yjfh's_20141222.txt");
//		buildHanziku("resource_origin/faguan_origin_yjfh's_20141222.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yqfx's_20141222.txt");
//		buildHanziku("resource_origin/faguan_origin_yqfx's_20141222.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20141225.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20141225.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20141225.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20141225.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20141225.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20141225.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20141231.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20141231.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20141231.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20141231.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20141231.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20141231.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dabi's_20150102.txt");
//		buildHanziku("resource_origin/faguan_origin_dabi's_20150102.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_firewing's_20150102.txt");
//		buildHanziku("resource_origin/faguan_origin_firewing's_20150102.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_kudi's_20150102.txt");
//		buildHanziku("resource_origin/faguan_origin_kudi's_20150102.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_meiying's_20150102.txt");
//		buildHanziku("resource_origin/faguan_origin_meiying's_20150102.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yjfh's_20150102.txt");
//		buildHanziku("resource_origin/faguan_origin_yjfh's_20150102.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yqfx's_20150102.txt");
//		buildHanziku("resource_origin/faguan_origin_yqfx's_20150102.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20150111.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20150111.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20150111.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20150111.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20150111.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20150111.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dabi's_20150112.txt");
//		buildHanziku("resource_origin/faguan_origin_dabi's_20150112.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_firewing's_20150112.txt");
//		buildHanziku("resource_origin/faguan_origin_firewing's_20150112.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_kudi's_20150112.txt");
//		buildHanziku("resource_origin/faguan_origin_kudi's_20150112.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_meiying's_20150112.txt");
//		buildHanziku("resource_origin/faguan_origin_meiying's_20150112.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yjfh's_20150112.txt");
//		buildHanziku("resource_origin/faguan_origin_yjfh's_20150112.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yqfx's_20150112.txt");
//		buildHanziku("resource_origin/faguan_origin_yqfx's_20150112.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20150123.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20150123.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20150123.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20150123.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windymea's_20150123.txt");
//		buildHanziku("resource_origin/faguan_origin_windymea's_20150123.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20150123.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20150123.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dabi's_20150124.txt");
//		buildHanziku("resource_origin/faguan_origin_dabi's_20150124.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_firewing's_20150124.txt");
//		buildHanziku("resource_origin/faguan_origin_firewing's_20150124.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_kudi's_20150124.txt");
//		buildHanziku("resource_origin/faguan_origin_kudi's_20150124.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_meiying's_20150124.txt");
//		buildHanziku("resource_origin/faguan_origin_meiying's_20150124.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yjfh's_20150124.txt");
//		buildHanziku("resource_origin/faguan_origin_yjfh's_20150124.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yqfx's_20150124.txt");
//		buildHanziku("resource_origin/faguan_origin_yqfx's_20150124.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20150203.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20150203.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyme's_20150203.txt");
//		buildHanziku("resource_origin/faguan_origin_windyme's_20150203.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windymea's_20150203.txt");
//		buildHanziku("resource_origin/faguan_origin_windymea's_20150203.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dwfz's_20150203.txt");
//		buildHanziku("resource_origin/faguan_origin_dwfz's_20150203.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_dabi's_20150203.txt");
//		buildHanziku("resource_origin/faguan_origin_dabi's_20150203.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_firewing's_20150203.txt");
//		buildHanziku("resource_origin/faguan_origin_firewing's_20150203.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_kudi's_20150203.txt");
//		buildHanziku("resource_origin/faguan_origin_kudi's_20150203.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_meiying's_20150203.txt");
//		buildHanziku("resource_origin/faguan_origin_meiying's_20150203.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yjfh's_20150203.txt");
//		buildHanziku("resource_origin/faguan_origin_yjfh's_20150203.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_yqfx's_20150203.txt");
//		buildHanziku("resource_origin/faguan_origin_yqfx's_20150203.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_louisaa's_20150205.txt");
//		buildHanziku("resource_origin/faguan_origin_louisaa's_20150205.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_superd's_20150205.txt");
//		buildHanziku("resource_origin/faguan_origin_superd's_20150205.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_xiond's_20150205.txt");
//		buildHanziku("resource_origin/faguan_origin_xiond's_20150205.txt_wrong");
		
//		buildHanzikuWithAutoAnsweredFile("resource_origin/faguan_origin_windyii's_20150209.txt");
//		buildHanziku("resource_origin/faguan_origin_windyii's_20150209.txt_wrong");
	}

	public static void testComputeDelta() {
		Hanzi z1 = new Hanzi(null, "60c0;c0c0;c1c0;c180;1f8180;bf300;1fb00;79340;7dff0;1ff7f8;1ee6ff;7659ff;c619e1;8c19e3;c33e3;c76c2;18e6c4;198ccc;19cd9c;364df8;14719c;1c7100;3100;0;");
		Hanzi z2 = new Hanzi(null, "30e00;70c00;61e00;70d80;61d80;e18c0;c3860;183070;1c6038;3cffde;6dff1e;cd804c;8f0c60;e4470;c4660;c6660;c36c0;c36c0;c3080;c2180;c01fe;dfffc;6fe00;0;");
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

	public static void testIdentify() {
		Hanzi unknown = new Hanzi("俭", "30e00;70c00;61e00;70d80;61d80;e18c0;c3860;183070;1c6038;3cffde;6dff1e;cd804c;8f0c60;e4470;c4660;c6660;c36c0;c36c0;c3080;c2180;c01fe;dfffc;6fe00;0;");

		System.out.println("use hanziku to identify...");
		HanzikuBuilder zikuBuilder = new HanzikuBuilder();
		zikuBuilder.addZiku("hanziku.txt");
		Hanzi result1 = new Hanzi(null, unknown.getBitMapString());
		int delta1 = HanzikuBuilder.identifyByHanziku(unknown, zikuBuilder.getHanziku().values(), result1);
		System.out.println(result1.getDisplayString());
		System.out.println(result1.getWenzi() + " " + delta1);
		System.out.println(result1.getWenzi() + " " + result1.getBitMapString());
		System.out.println(result1.getBitCountString());
		
		System.out.println("use ziku to identify...");
		Hashtable<String, ZhzbHanzi> ziku = ZikuReader.getDefaultZiku();
		Hanzi result2 = new Hanzi(null, unknown.getBitMapString());
		int delta2 = HanzikuBuilder.identifyByZiku(unknown.getBitCounts(), ziku.values(), result2);
		System.out.println(result2.getDisplayString());
		System.out.println(result2.getWenzi() + " " + delta2);
		System.out.println(result2.getWenzi() + " " + result1.getBitMapString());
		System.out.println(result2.getBitCountString());
	}

	public static void buildHanzikuWithAutoAnsweredFile(String fileName) {
		HanziReader reader = new HanziReader();
		ArrayList<LinkedHashMap<String,Hanzi>> result = reader.parseAutoAnsweredFile(fileName, null);
		LinkedHashMap<String,Hanzi> rightHanziList = result.get(0);
		LinkedHashMap<String,Hanzi> wrongHanziList = result.get(1);
		dumpHanziList(rightHanziList.values(), fileName + "_right", true);
		dumpHanziList(wrongHanziList.values(), fileName + "_wrong", true);
		
		buildHanziku(fileName + "_right");
	}
	
	public static void checkAutoAnsweredFile(String fileName) {
		HanziReader reader = new HanziReader();
		ArrayList<LinkedHashMap<String,Hanzi>> result = reader.parseAutoAnsweredFile(fileName, null);
		LinkedHashMap<String,Hanzi> rightHanziList = result.get(0);
		LinkedHashMap<String,Hanzi> wrongHanziList = result.get(1);
		dumpHanziList(rightHanziList.values(), fileName + "_right", true);
		dumpHanziList(wrongHanziList.values(), fileName + "_wrong", true);
	}

	public static void identifyUnansweredOriginFile(String fileIn, String fileOut) {
		HanziReader reader = new HanziReader();
		ArrayList<Hanzi> hanziList = reader.parseOriginFile(fileIn);
		identifyUnansweredByZiku(hanziList);
		dumpHanziList(hanziList, fileOut, true);
//		dumpHanziList(hanziList, fileOut+"_withmap", true);
//		dumpZhzbHanziList(hanziList, fileOut+"_ziku", false);
		
	}
	
	public static void identifyZhzbAnswerOutputFile(String fileIn, String fileOut) {
		HanziReader reader = new HanziReader();
		ArrayList<Hanzi> hanziList = reader.parseZhzbAnswerOutputFile(fileIn);
		identifyUnansweredByZiku(hanziList);
		dumpHanziList(hanziList, fileOut, true);
		System.out.println("done! " + fileOut);
//		dumpHanziList(hanziList, fileOut+"_withmap", true);
//		dumpZhzbHanziList(hanziList, fileOut+"_ziku", false);
		
	}
	
	private static void identifyUnansweredFile(String unansweredFile) {
		LinkedHashMap<String, Hanzi> hanziListMap = readUnknownFile(unansweredFile);
		Collection<Hanzi> hanziList = hanziListMap.values();
		
		long start = System.currentTimeMillis();
		identifyUnansweredByZiku(hanziList);
		long end = System.currentTimeMillis();
		System.out.println("Ziku Time cost:" + (end - start));
		
		dumpHanziList(hanziList, unansweredFile + "_identify", true);
		
		start = System.currentTimeMillis();
		identifyUnansweredByHanziku(hanziList);
		end = System.currentTimeMillis();
		System.out.println("Hanziku Time cost:" + (end - start));
		
		dumpHanziList(hanziList, unansweredFile + "_identify_new", true);
		
//		dumpHanziList(hanziList, fileOut+"_withmap", true);
//		dumpZhzbHanziList(hanziList, fileOut+"_ziku", false);
		
	}
	
	private static void buildHanziku(String identifiedFile) {
		HanzikuBuilder zikuBuilder = new HanzikuBuilder();
		zikuBuilder.addZiku("hanziku.txt");
		System.out.println();
		zikuBuilder.addZiku("hanzi.txt");
		System.out.println();
		zikuBuilder.readOriginAnswer("hanziku_unknown_hard_identify.txt");
		System.out.println();
		zikuBuilder.readOriginAnswer("hanziku_unknown_hard.txt");
		System.out.println();
		zikuBuilder.readOriginAnswer(identifiedFile);
		
//		zikuBuilder.readOriginAnswer("hanziku_unknown_identify2.txt");
//		zikuBuilder.readOriginAnswer("hanziku_unknown_hard_identify.txt");
//		zikuBuilder.readOriginAnswer("resource/hanzi_origin_withmap_answer.txt");
//		System.out.println();
//		zikuBuilder.readOriginAnswer("resource/hanzi_origin_withmap_answer.txt");
		zikuBuilder.dumpAnsweredZiku("hanziku.txt", false);
//		zikuBuilder.dumpAnsweredZiku("hanziku_withmap.txt", true);
		zikuBuilder.dumpAnsweredZhzbZiku("ziku_windyii.txt", false);
		zikuBuilder.dumpUnansweredZiku("hanziku_unknown_hard.txt", true);
//		zikuBuilder.identifyUnanswered();
//		zikuBuilder.dumpUnansweredZiku("hanziku_unknown_identify.txt", true);
		
		ZikuReader zikuReader = new ZikuReader();
		LinkedHashMap<String, ZhzbHanzi> zikuWindyii = zikuReader.readZikuFile("ziku_windyii.txt");
		zikuReader.dumpHanziList(zikuWindyii, "ziku_windyii_clean.txt");

	}

	public int identify(Hanzi hanzi) {
		Hanzi result = new Hanzi();
		int delta = identifyByHanziku(hanzi, _hanziku.values(), result);
		hanzi.setWenzi(result.getWenzi());
		return delta;
	}

	public static void identifyUnansweredByHanziku(Collection<Hanzi> unansweredHanziku) {
		HanzikuBuilder zikuBuilder = new HanzikuBuilder();
		zikuBuilder.addZiku("resource/hanziku.txt");
		Collection<Hanzi> hanziku = zikuBuilder.getHanziku().values();
		identifyUnansweredByHanziku(unansweredHanziku, hanziku, 9999);
	}

	public static void identifyUnansweredByHanziku(Collection<Hanzi> unansweredHanziku, Collection<Hanzi> hanziku, int deltaLimit) {
		System.out.println("Use hanziku to identify: " +  unansweredHanziku.size());
		int count = 0;
		int deltaMin = 9999;
		
		for(Hanzi hz : unansweredHanziku) {
			Hanzi result = new Hanzi(null, hz.getBitMap());
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
	
	public static int identifyByHanziku(Hanzi hanzi, Collection<Hanzi> hanziku, Hanzi result) {
		//like match
		int deltaMin = 9999;
		for(Hanzi hz : hanziku) {
			int delta = computeDelta(hz, hanzi);
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
	
	public static void identifyUnansweredByZiku(Collection<Hanzi> unansweredHanziku) {
		Hashtable<String, ZhzbHanzi> ziku = ZikuReader.getDefaultZiku();
		identifyUnansweredByZiku(unansweredHanziku, ziku.values(), 999);
	}
	
	public static void identifyUnansweredByZiku(Collection<Hanzi> unansweredHanziku, Collection<ZhzbHanzi> ziku, int deltaLimit) {
		System.out.println("Use ziku to identify: " +  unansweredHanziku.size());
		int count = 0;
		int delta = 9999;
		
		for(Hanzi hz : unansweredHanziku) {
			//delta = identify(hz);
			
			Hanzi hz1 = new Hanzi(null, hz.getBitMap());
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
		System.out.println("Identification done!");
	}


	private static int identifyByZiku(int[] bitCounts, Collection<ZhzbHanzi> ziku, Hanzi hzResult) {
		int deltaMin = 9999;
		for(ZhzbHanzi zhzbHz: ziku) {
			int[] hzBitCount = zhzbHz.getBitCount();
			int delta = 0;
			for (int i = 0; i < 48; i++) {
				delta += (hzBitCount[i] - bitCounts[i]) * (hzBitCount[i] - bitCounts[i]);
			}
			
			if (delta < deltaMin) {
				deltaMin = delta;
				hzResult.setWenzi(zhzbHz.getWenzi());
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
	private static int computeDeltaOld(Hanzi hanzi1, Hanzi hanzi2){
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
	private static int computeDelta(Hanzi hanzi1, Hanzi hanzi2){
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
	
	private static void buildUnanswered() {
		HanziReader reader = new HanziReader();
		ArrayList<Hanzi> hanziList = new ArrayList<Hanzi>(8000);
		hanziList.addAll(reader.parseOriginFile("resource/box_windyii's_20140722.txt"));
		hanziList.addAll(reader.parseOriginFile("resource/box_windyii's_20140729.txt"));
		hanziList.addAll(reader.parseOriginFile("resource/box_windyii's_20140731.txt"));
		hanziList.addAll(reader.parseOriginFile("resource/box_windyme's_20140722.txt"));
		hanziList.addAll(reader.parseOriginFile("resource/box_windyme's_20140729.txt"));
		hanziList.addAll(reader.parseOriginFile("resource/box_windyme's_20140731.txt"));
		
		Hashtable<String, Hanzi> hanziHash = new Hashtable<String, Hanzi>(5000);
		int matchedCount = 0;
		for(Hanzi hz : hanziList) {
			if(hanziHash.get(hz.getBitMapString()) != null) {
				matchedCount++;
				System.out.println("Match! " + hz.getWenzi());
				System.out.println(hz.getDisplayString() + hz.getWenzi());
			} else {
				hanziHash.put(hz.getBitMapString(), hz);
			}
		}
		
		System.out.println("Matched " + matchedCount + "/" + hanziList.size());
		
//		dumpHanziList(hanziList, "hanzi_origin_withmap.txt", true);
//		dumpHanziList(hanziList, "hanzi_origin.txt", false);
	}
	
	private static void buildAnsweredHanzi(String file, boolean withPiture) {
		HanziReader reader = new HanziReader();
		ArrayList<Hanzi> answeredHanziList = reader.parseManualAnswerFile("resource/faguan_windyii.txt");
		ArrayList<Hanzi> answeredHanziList2 = reader.parseManualAnswerFile("resource/faguan_windyme.txt");
		answeredHanziList.addAll(answeredHanziList2);
		
		dumpHanziList(answeredHanziList, file, withPiture);
	}

	private static void buildAnswered(){
		buildAnsweredHanzi("hanzi_withmap.txt", true);
		buildAnsweredHanzi("hanzi.txt", false);
	}
}
