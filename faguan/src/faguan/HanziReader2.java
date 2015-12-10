package faguan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import duishi.Program;

public class HanziReader2 {
	
	public Hanzi parseOneHanzi(String[] hanziPicture) {
		Long[] bitMap = new Long[24];
		
		String frontColor;
		String firstLine = hanziPicture[0];
		String backColor = firstLine.substring(0, firstLine.indexOf(' '));
		
		for(int i = 0; i < 24; i++) {
			long lineBit = parseLine(hanziPicture[i], backColor);
			bitMap[i] = lineBit;
		}
		
		
		return new Hanzi(null, bitMap, true);
	}
	
	public Hanzi[] parseTwoHanzi(String[] twoHanziPicture) {
	
		if(twoHanziPicture == null || twoHanziPicture.length < 24) {
			return null;
		}
		
		String[] hzFirstString = new String[24];
		String[] hzSecondString = new String[24];
		for(int i = 0; i < 24; i++) {
			//String line = twoHanziPicture[i].replace('[', 'a');
			String[] lineParts = twoHanziPicture[i].split("\033\\[2;37;0m");  // "[2;37;0m"
			hzFirstString[i] = lineParts[0];
			hzSecondString[i] = lineParts[1];
		}
		
		Hanzi hzFirst = parseOneHanzi(hzFirstString);
		Hanzi hzSecond = parseOneHanzi(hzSecondString);
		
		return new Hanzi[]{hzFirst, hzSecond};
	}

	private long parseLine(String line, String backColor) {
		//replace 2 blank to 1 blank
		line = line.replaceAll("  ", " "); 
		
		backColor = backColor.replace("[", "\\["); //for regex
		long lineLong = 0;

		String lineParts[] = line.split(backColor);
		int bitPosition = 24;
		for (int i = 0; i < lineParts.length; i++) {
			boolean front = false;
			String linePart = lineParts[i];
			for(int j = 0; j < linePart.length(); j++) {
				if (linePart.charAt(j) == ' ') {
					bitPosition--;
					if (front) {
						lineLong |= 1L << bitPosition; 
					}
				} else {
					front = true;
				}
			}
			
		}
		
		return lineLong;
	}
	
	public ArrayList<Hanzi> parseManualAnswerFile(String fileName) {
		ArrayList<Hanzi> hanziList = new ArrayList<Hanzi>();
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
	    String tempString = null;
	    
	    try {
	    	InputStreamReader read = new InputStreamReader(is, encoding);//¿¼ÂÇµ½±àÂë¸ñÊ½
	    	reader = new BufferedReader(read);

	    	String state="ReadMap";
	    	String regexMap = ".+\033\\[2;37;0m.+\033\\[2;37;0m";
	    	String[] twoHanziPicture = null;
	        while ((tempString = reader.readLine()) != null) {
	        	if(state.equals("ReadMap")) {
	        		if(tempString.matches(regexMap) && tempString.length()>100) { //·¨¹Ùºº×ÖÍ¼
	        			twoHanziPicture = new String[24];
	        			twoHanziPicture[0] = tempString;
	        			for(int i=1;i<24;i++){
	        				twoHanziPicture[i] = reader.readLine();
	        			}
	        			state = "ReadAnswer";  
	        			continue;
	        		}
	        	}
	        	
	        	if(tempString.contains("retry") || tempString.contains("display")){
	        		state = "ReadMap";
	        		continue;
	        	}
	        	
	        	if(state.equals("ReadAnswer")) {
	        		if(tempString.contains("Äã´ðµÀ") && reader.readLine().contains("·¨¹ÙËµµ½:´ð¶ÔÁË£¬¹§Ï²¹§Ï²¡£")){ //Äã´ðµÀ:°Ô´á//·¨¹ÙËµµ½:´ð¶ÔÁË£¬¹§Ï²¹§Ï²¡£
	        			String twoHanziAnswer = tempString.split(":")[1];
	        			Hanzi[] twoHanzi = parseTwoHanzi(twoHanziPicture);
	        			twoHanzi[0].setWenzi(twoHanziAnswer.substring(0, 1));
	        			twoHanzi[1].setWenzi(twoHanziAnswer.substring(1, 2));
	        			hanziList.add(twoHanzi[0]);
	        			hanziList.add(twoHanzi[1]);
	        			
		        		state = "ReadMap";
		        		continue;
		        	};
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
	    
		return hanziList;
	}

	public ArrayList<Hanzi> parseOriginFile(String fileName) { //no answer
		ArrayList<Hanzi> hanziList = new ArrayList<Hanzi>();
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
	    String tempString = null;
	    
	    try {
	    	InputStreamReader read = new InputStreamReader(is, encoding);//¿¼ÂÇµ½±àÂë¸ñÊ½
	    	reader = new BufferedReader(read);

	    	String regexMap = ".+\033\\[2;37;0m.+\033\\[2;37;0m";
	    	String[] twoHanziPicture = null;
	        while ((tempString = reader.readLine()) != null) {
        		if(tempString.matches(regexMap) && tempString.length()>100) { //·¨¹Ùºº×ÖÍ¼
        			twoHanziPicture = new String[24];
        			twoHanziPicture[0] = tempString;
        			for(int i=1;i<24;i++){
        				twoHanziPicture[i] = reader.readLine();
        			}
        			
        			Hanzi[] twoHanzi = parseTwoHanzi(twoHanziPicture);
        			hanziList.add(twoHanzi[0]);
        			hanziList.add(twoHanzi[1]);
        			continue;
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
	    
		return hanziList;
	}

}
