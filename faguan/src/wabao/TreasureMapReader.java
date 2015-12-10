package wabao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import faguan.Hanzi;

public class TreasureMapReader {
	
	public static ArrayList<TreasureMap> parseOriginMapFile(String fileName) {
		ArrayList<TreasureMap> maps = new ArrayList<TreasureMap>();

		InputStream is = null;
		File file = new File(fileName);
		if(file.exists()) {
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return maps;
			}
		}
		
	    String encoding = "GBK";
	    BufferedReader reader = null;
	    String tempString = null;
	    
	    try {
	    	InputStreamReader read = new InputStreamReader(is, encoding);//¿¼ÂÇµ½±àÂë¸ñÊ½
	    	reader = new BufferedReader(read);

	    	String state="ReadRoom";

	    	String room = null;
	    	TreasureMap map = null;
	    	List<String> hanziPicture = new ArrayList<String>();
	    	int lineNum = 0;
	        while ((tempString = reader.readLine()) != null) {
	        	lineNum++;
	        	
	        	// read room
        		room = getRoom(tempString);
        		if (room == null) {
        			continue;
        		}
        		
        		// read picture
        		hanziPicture.clear();
        		
        		// for wintin++ output, first picture line is embedded in room line.
        		if (tempString.endsWith("[2;37;0m")) {
        			tempString = tempString.substring(tempString.indexOf("ËÆºõÊÇ¡°") + 4, tempString.length() - 9);
        			hanziPicture.add(tempString);
        		}
        		
        		while (tempString != null) {
	        		tempString = reader.readLine();
    				lineNum++;
	        		if (tempString == null || tempString.contains("ÎÄ×ÖÒ»ÉÁ¡£¡£¡£")) {
	        			break;
	        		}
	        		
	        		if(tempString.endsWith("[2;37;0m")) {
	        			tempString = tempString.substring(0, tempString.length() - 9);
	        		}
	        		hanziPicture.add(tempString);
        		}
        		
    			try {
    				map = parseOneTreasureMap(room, hanziPicture);
    			} catch (Exception e) {
    				System.out.println("Parse treasure map hanzi error near by line:" + lineNum);
    				continue;
    			}
    			
    			maps.add(map);
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
	    
		return maps;
	}

	public static ArrayList<TreasureMap> tryParseOriginMapFile(String fileName) {
		ArrayList<TreasureMap> maps = new ArrayList<TreasureMap>();

		InputStream is = null;
		File file = new File(fileName);
		if(file.exists()) {
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return maps;
			}
		}
		
	    String encoding = "GBK";
	    BufferedReader reader = null;
	    String tempString = null;
	    
	    try {
	    	InputStreamReader read = new InputStreamReader(is, encoding);//¿¼ÂÇµ½±àÂë¸ñÊ½
	    	reader = new BufferedReader(read);

	    	String state="ReadRoom";

	    	String room = null;
	    	TreasureMap map = null;
	    	List<String> hanziPicture = new ArrayList<String>();
	    	int lineNum = 0;
	        while ((tempString = reader.readLine()) != null) {
	        	lineNum++;
	        	
	        	// read room
        		room = getRoom(tempString);
        		if (room == null) {
        			continue;
        		}
        		
        		// read picture
        		hanziPicture.clear();
        		
        		// for wintin++ output, first picture line is embedded in room line.
        		if (tempString.endsWith("[2;37;0m")) {
        			tempString = tempString.substring(tempString.indexOf("ËÆºõÊÇ¡°") + 4, tempString.length() - 9);
        			hanziPicture.add(tempString);
        		}
        		
        		while (tempString != null) {
	        		tempString = reader.readLine();
    				lineNum++;
	        		if (tempString == null || tempString.contains("ÎÄ×ÖÒ»ÉÁ¡£¡£¡£")) {
	        			break;
	        		}
	        		
	        		if(tempString.endsWith("[2;37;0m")) {
	        			tempString = tempString.substring(0, tempString.length() - 9);
	        		}
	        		hanziPicture.add(tempString);
        		}

    			try {
    				map = parseOneTreasureMap(room, hanziPicture);
    			} catch (Exception e) {
    				System.out.println("Parse treasure map hanzi error near by line:" + lineNum);
    				map = new TreasureMap(room, new TreasureMapHanzi[0]);
    			}
    			maps.add(map);
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
	    
		return maps;
	}
	
	private static TreasureMap parseOneTreasureMap(String room, List<String> hanziPicture) throws Exception {

		if (hanziPicture.size() % 24 != 0) {// ²»ÊÇ24µÄ±¶Êý
			throw new Exception("invalid picture line size:" + hanziPicture.size());
		}
		
		int hzCount = hanziPicture.size()/24;
		TreasureMapHanzi[] area = new TreasureMapHanzi[hzCount];
		Long[] lines = new Long[24];
		
		for(int i = 0;i < hanziPicture.size(); i++) {
			long line = parseLine(hanziPicture.get(i));
			lines[i%24] = line;
			if(i%24 == 23) {
				TreasureMapHanzi hz = new TreasureMapHanzi(null, lines);
				area[i/24] = hz;
				lines = new Long[24];
			}
		}
		
		return new TreasureMap(room, area);
	}
	
	private static long parseLine(String line) {
		long lineLong = 0;
		int bitPosition = 48;
		for(int i = 0; i < line.length(); i++) {
			bitPosition--;
			if(line.charAt(i) == '¡ñ') {
				lineLong |= 1L << bitPosition;
			}
		}
		
		return lineLong;
	}

	// Áú¶ÉÍåºóÃæµÄ×Ö¸ü¼ÓÄÑÒÔ±æÈÏ£¬ËÆºõÊÇ¡°
	private static String getRoom(String tempString) {
		String suffix = "ºóÃæµÄ×Ö¸ü¼ÓÄÑÒÔ±æÈÏ£¬ËÆºõÊÇ¡°";
		int index =  tempString.indexOf(suffix);
		if (index != -1) {
			return tempString.substring(0, index);
		}
		return null;
	}
	
	private static TreasureMapHanzi parseOneHanzi(String[] hanziPicture) {
		return null;
	}
	
	private boolean isHanziBeginLine(String line) {
    	String hanziBeginRegexMap = ".+\033\\[2;37;0m.+\033\\[2;37;0m";
    	
    	if(line.matches(hanziBeginRegexMap) && line.length()>100) { //·¨¹Ùºº×ÖÍ¼
    		Pattern pattern = Pattern.compile("  ");
    		Matcher matcher=pattern.matcher(line);
    		int blankCount=0;  
            while (matcher.find())  
            {  
            	blankCount++; 
            }
            if(blankCount == 48) {
            	return true;
            }
    	}
		
		return false;
	}

}
