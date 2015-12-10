package faguan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import duishi.Program;

public class HanziReader {
	
	public Hanzi parseOneHanzi(String[] hanziPicture) {
		Long[] bitMap = new Long[24];
		
		String frontColor;
		String firstLine = hanziPicture[0];
		String backColor = firstLine.substring(0, firstLine.indexOf(' '));
		
		for(int i = 0; i < 24; i++) {
			long lineBit = parseLine(hanziPicture[i], backColor);
			bitMap[i] = lineBit;
		}
		
		
		return new Hanzi(null, bitMap);
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
		//line = line.replace('[', 'a');
		
		backColor = backColor.replace("[", "\\["); //for regex
		
		long lineLong = 0;

		String lineParts[] = line.split(backColor);
		int bitPosition = 48;
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

	    	String[] twoHanziPicture = null;
	        while ((tempString = reader.readLine()) != null) {
        		if(isHanziBeginLine(tempString)) { //·¨¹Ùºº×ÖÍ¼
        			twoHanziPicture = new String[24];
        			twoHanziPicture[0] = tempString;
        			for(int i=1;i<24;i++){
        				twoHanziPicture[i] = reader.readLine();
        			}
        			
        			Hanzi[] twoHanzi = null;
        			try {
        				twoHanzi = parseTwoHanzi(twoHanziPicture);
        			} catch (Exception ex) {
        				continue;
        			}
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

	public ArrayList<Hanzi> parseZhzbAnswerOutputFile(String fileName) { //no answer
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

	    	Long[] hanziPicture1 = null;
	    	Long[] hanziPicture2 = null;
	    	int lineCount = 0;
	    	while ((tempString = reader.readLine()) != null) {
	    		lineCount++;
        		if(tempString.equals("ÕýÈ·")) { //·¨¹Ùºº×ÖÍ¼
        			String firstLine = reader.readLine();
        			if(firstLine.length() != 24){
        				continue;
        			}
        			hanziPicture1 = new Long[24];
        			hanziPicture1[0] = parse24BitLine(firstLine, '¡ö');
        			for(int i=1; i<24; i++){
        				hanziPicture1[i] = parse24BitLine(reader.readLine(), '¡ö');
        				lineCount++;
        			}
        			Hanzi hanzi1 = new Hanzi(null, hanziPicture1);
        			hanziList.add(hanzi1);
        			
        			reader.readLine();
        			lineCount++;

        			hanziPicture2 = new Long[24];
        			for(int i=0; i<24; i++){
        				hanziPicture2[i] = parse24BitLine(reader.readLine(), '¡ö');
        				lineCount++;
        			}
        			Hanzi hanzi2 = new Hanzi(null, hanziPicture2);
        			hanziList.add(hanzi2);
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


	public ArrayList<LinkedHashMap<String,Hanzi>> parseAutoAnsweredFile(String fileName, LinkedHashMap<String,Hanzi> hanziku) {
		LinkedHashMap<String,Hanzi> rightHanziList = new LinkedHashMap<String,Hanzi>();
		LinkedHashMap<String,Hanzi> wrongHanziList = new LinkedHashMap<String,Hanzi>();
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

	    	String[] twoHanziPicture = null;
	    	Hanzi[] twoHanzi = null;
	    	Hanzi[] previousTwoHanzi = null;
	    	int lineNum = 0;
	        while ((tempString = reader.readLine()) != null) {
	        	lineNum++;
	        	if(state.equals("ReadMap") || state.equals("ReadAnswer")) {
	        		if(isHanziBeginLine(tempString)) { //·¨¹Ùºº×ÖÍ¼
	        			twoHanziPicture = new String[24];
	        			twoHanziPicture[0] = tempString;
	        			for(int i=1; i<24; i++){
	        				twoHanziPicture[i] = reader.readLine();
	        				lineNum++;
	        			}
	        			try {
	        				twoHanzi = parseTwoHanzi(twoHanziPicture);
	        			} catch (Exception e) {
	        				System.out.println("Parse hanzi error near by line:" + lineNum);
	        				continue;
	        			}
	        			
	        			if(previousTwoHanzi != null 
	        					&& !previousTwoHanzi[0].getBitMapString().equals(twoHanzi[0].getBitMapString()) 
	        					&& !rightHanziList.containsKey(previousTwoHanzi[0].getBitMapString())) {
	        				wrongHanziList.put(previousTwoHanzi[0].getBitMapString(), previousTwoHanzi[0]);
	        				wrongHanziList.put(previousTwoHanzi[1].getBitMapString(), previousTwoHanzi[1]);
	        			}
	        		
	        			previousTwoHanzi = twoHanzi;
	        			state = "ReadAnswer";  
	        			continue;
	        		}
	        	}
	        	
	        	// for manual answer
	        	if((tempString.contains("retry") || tempString.contains("display")) && tempString.length() < 20){
	        		state = "ReadMap";
	        		continue;
	        	}
	        	
	        	if(state.equals("ReadAnswer")) {
	        		if(tempString.contains("Äã´ðµÀ")) {
	        			String faguanLine = reader.readLine();
	    	        	lineNum++;
	        			
	        			LinkedHashMap<String,Hanzi> hanziListToAdd = null;
	        			if(faguanLine != null && faguanLine.contains("·¨¹ÙËµµ½:´ð¶ÔÁË£¬¹§Ï²¹§Ï²¡£")){ //Äã´ðµÀ:°Ô´á//·¨¹ÙËµµ½:´ð¶ÔÁË£¬¹§Ï²¹§Ï²¡£
	        				hanziListToAdd = rightHanziList;
	        			} else { //faguanLine.contains("·¨¹ÙËµµ½:´ð´íÁË£¬¿ÉÏ§¿ÉÏ§£¬")){ //·¨¹ÙËµµ½:´ð´íÁË£¬¿ÉÏ§¿ÉÏ§£¬»¹ÓÐ4´Î»ú»á£¬¼ÓÓÍ°¡£¡
	        				hanziListToAdd = wrongHanziList;
	        			}
	        			String twoHanziAnswer = tempString.split(":")[1];

	        			twoHanzi[0].setWenzi(twoHanziAnswer.substring(0, 1));
	        			twoHanzi[1].setWenzi(twoHanziAnswer.substring(1, 2));
	        			hanziListToAdd.put(twoHanzi[0].getBitMapString(), twoHanzi[0]);
	        			hanziListToAdd.put(twoHanzi[1].getBitMapString(), twoHanzi[1]);
	        			
		        		state = "ReadMap";
		        		continue;
		        	}
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
	    
	    ArrayList<LinkedHashMap<String,Hanzi>> result = new ArrayList<LinkedHashMap<String,Hanzi>>();
	    result.add(rightHanziList);
	    result.add(wrongHanziList);
//	    LinkedHashMap<String,Hanzi>[] result = LinkedHashMap<String,Hanzi>[];
//	    Array.newInstance(LinkedHashMap.class, 2);
		return result;
	}

	private long parse24BitLine(String line, char frontChar) {
		long result = 0;
		for(int i = 0;i < 24;i++) {
			if(line.charAt(i) == frontChar) { // frontChar = '¡ö'
				result |= 1 << (23-i);
			}
		}
		
		return result;
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
