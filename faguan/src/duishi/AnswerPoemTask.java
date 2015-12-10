package duishi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.TimerTask;

public class AnswerPoemTask extends TimerTask {

	static Hashtable<String, String> _poems = readPoemLibFile();
	private String _dir;
	

	

	public AnswerPoemTask(String dir) {
		_dir = dir;
	}

	@Override
	public void run() {
	    String[] ss_file = new File(_dir).list();
	    for(String poemFile : ss_file) {
	    	// poemFile = "poem_windyme's.txt";
	    	if(poemFile.startsWith("poem_") && poemFile.endsWith("'s.txt")) {
	    		dealfile(poemFile);
	    	}
	    }
		
	}

	private void dealfile(String idPoemFileName) {
		File idPoemFile = new File(_dir + idPoemFileName);
		if(!idPoemFile.exists()) {
			return;
		}
		
		String id = idPoemFileName.substring(5, idPoemFileName.indexOf("'"));
		
		//String strToSearch = "我澹素已闲清川心如此";
		PoemInput poemInput = readIdFile(idPoemFile);
		
		idPoemFile.delete();
		
		String strToSearch = poemInput.poem;
		if(poemInput.isPet) {
			System.out.println(strToSearch + " pet");
		} else {
			System.out.println(strToSearch);
		}

		
	    if (strToSearch != null) {
			char[] inChars = strToSearch.toCharArray();
	    	Arrays.sort(inChars);
	    	String retString = _poems.get(new String(inChars)); //我心素已闲清川澹如此
		    System.out.println(retString);
		    
		    String poemAnswerFile = _dir + "answer_" + idPoemFileName;
			String answerString;
			if(poemInput.isPet) {
				answerString = "whisper " + id + "'s answer " + retString;
			} else {
				answerString = "answer " + retString;
			}
			writeAnswerFile(poemAnswerFile, answerString);
	    }
	}

	private static Hashtable<String, String> readPoemLibFile() {
		Hashtable<String, String> poems = new Hashtable<String, String>(4000);
		InputStream is = null;
		File file = new File("resource/Poem.txt");
		if(file.exists()) {
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			is = Program.class.getResourceAsStream("/resource/Poem.txt");
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
	        	String[] parts = tempString.split("  ");
	        	for(String part : parts) {
	        		if(part.length()>=5 && !part.contains(" ")){
	        			tempChars = part.toCharArray();
	    	        	Arrays.sort(tempChars);
	    	        	poems.put(new String(tempChars), part);
	        		}
	        	}
	        	
	        	//add full
	        	tempString = tempString.replace("  ", "");
	        	tempChars = tempString.toCharArray();
	        	Arrays.sort(tempChars);
	        	poems.put(new String(tempChars), tempString);
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
		return poems;
	}
	
	
	private static PoemInput readIdFile(File file) {
		PoemInput poemInput =  new PoemInput();

		BufferedReader reader = null;
		try {
	    	InputStreamReader read = new InputStreamReader(new FileInputStream(file), "GBK");//考虑到编码格式
	    	reader = new BufferedReader(read);

	    	String tempString = null;
	    	
	    	//return the last one line
	        while ((tempString = reader.readLine()) != null) {
	        	poemInput.poem = tempString;
	        	String[] strInLine = tempString.split(" ");
	        	if(strInLine.length == 2 && strInLine[1].equals("pet")){
	        		poemInput.isPet = true;
	        		poemInput.poem = strInLine[0];
	        	}
	        }
	        reader.close();
	        return poemInput;
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

	private static void writeAnswerFile(String answerFile, String content) {
		if (content != null) {
			try {
				FileOutputStream os = new FileOutputStream(answerFile);
				OutputStreamWriter osw = new OutputStreamWriter(os, "GBK");
				PrintWriter pw = new PrintWriter(osw, true);
				pw.write(content);
				pw.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

}
