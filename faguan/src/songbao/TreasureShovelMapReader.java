package songbao;

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

public class TreasureShovelMapReader {
	
	public static ArrayList<TreasureShovelMap> parseOriginMapFile(String fileName) {
		ArrayList<TreasureShovelMap> maps = new ArrayList<TreasureShovelMap>();

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

	    	String pictureStartLine = "Ö»¼ûÉÏÃæÍáÍáÅ¤Å¤µÄÐ´×Å£ºÄ³ÄêÄ³ÔÂÄ³ÈÕ£º²Ø±¦ÓÚ£º";
	    	TreasureShovelMap map = null;
	    	List<String> hanziPicture = new ArrayList<String>();
	    	int lineNum = 0;
	        while ((tempString = reader.readLine()) != null) {
	        	lineNum++;
	        	
	        	// read room
        		if (!tempString.equals(pictureStartLine)) {
        			continue;
        		}
        		
        		// read picture
        		hanziPicture.clear();
        		
        		while (tempString != null) {
	        		tempString = reader.readLine();
    				lineNum++;
	        		if (tempString == null || tempString.contains("ÏÂÃæµÄ×Ö¸üÐ¡£¬ÒþÒþÔ¼Ô¼ÊÇ")) {
	        			break;
	        		}
	        		if (tempString.length() == 0) {
	        			continue;
	        		}
	        		
	        		if(!tempString.endsWith("[2;37;0m")) {
	        			lineNum++;
	        			tempString = tempString + reader.readLine();
	        		}
	        		hanziPicture.add(tempString);
        		}
        		
        		if((tempString == null) || (hanziPicture.size() != 24) || !tempString.contains("ÏÂÃæµÄ×Ö¸üÐ¡£¬ÒþÒþÔ¼Ô¼ÊÇ")) {
        			System.out.println("Parse treasure shovel map hanzi error near by line:" + lineNum);
        			continue;
        		}
        		
        		String area = getArea(tempString);
        		
    			try {
    				map = parseOneTreasureShovelMap(area, hanziPicture);
    			} catch (Exception e) {
    				System.out.println("Parse treasure shovel map hanzi error near by line:" + lineNum);
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


	
	private static TreasureShovelMap parseOneTreasureShovelMap(String area, List<String> hanziPicture) throws Exception {

		if (hanziPicture.size() != 24) {// ²»ÊÇ24
			throw new Exception("invalid treasure shovel picture line size:" + hanziPicture.size());
		}
		
		//int hzCount = hanziPicture.get(0).split("[2;37;0m").length;
		int hzCount = hanziPicture.get(0).split("\\e\\[2;37;0m").length;
		long[][] lines = new long[hzCount][24];
		
		for(int i = 0; i < 24; i++) {
			long line[] = parseLine(hanziPicture.get(i));
			
			if (line.length != hzCount) {
				throw new Exception("invalid treasure shovel picture line hanzi size:" + line.length);
			}

			for(int j=0; j<hzCount; j++) {
				lines[j][i] = line[j];
			}
		}
		
		TreasureShovelMapHanzi[] roomName = new TreasureShovelMapHanzi[hzCount];
		for(int i = 0; i < hzCount; i++) {
			roomName[i] = new TreasureShovelMapHanzi(null, lines[i]);
		}
		
		return new TreasureShovelMap(area, roomName);
	}
	
	private static long[] parseLine(String line) {
		// "[2;37;0m"
		String[] lines = line.split("\\e\\[2;37;0m");
		long[] lineLongs = new long[lines.length];
		for(int i = 0; i < lines.length; i++) {
			long lineLong = 0;
			int bitPosition = 48;
			for(int j = 0; j < lines[i].length(); j++) {
				bitPosition--;
				if(lines[i].charAt(j) == '¡ö') {
					lineLong |= 1L << bitPosition;
				}
			}
			lineLongs[i] = lineLong;
		}
		
		return lineLongs;
	}

	// ÏÂÃæµÄ×Ö¸üÐ¡£¬ÒþÒþÔ¼Ô¼ÊÇ¡°×ÏÖì¹ú¡±
	private static String getArea(String tempString) {
		String prefix = "ÏÂÃæµÄ×Ö¸üÐ¡£¬ÒþÒþÔ¼Ô¼ÊÇ¡°";
		int index =  tempString.lastIndexOf('¡±');
		if (index != -1) {
			return tempString.substring(prefix.length(), index);
		}
		return null;
	}
	
	private static TreasureShovelMapHanzi parseOneHanzi(String[] hanziPicture) {
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
