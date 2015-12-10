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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;

public class ZikuReader {

	
	private String name = "zhzb提供的字库";
	static Hashtable<String, ZhzbHanzi> s_ziku = null;
	
	
	public ZikuReader() {
		
	}

	public static Hashtable<String, ZhzbHanzi> getDefaultZiku(){
		if(s_ziku == null){
			s_ziku = readFile("resource/ziku.txt");
		}
		return s_ziku;
	}
	
	public static Hashtable<String, ZhzbHanzi> readFile(String fileName) {
		Hashtable<String, ZhzbHanzi> ziku = new Hashtable<String, ZhzbHanzi>();
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
	        while ((line = reader.readLine()) != null) {
	        	if (line.length() < 50) {
	        		continue;
	        	}
	        	
	        	String bitCounts = line.substring(2);
	        	String newZi = line.substring(0, 1);
	        	
	        	ZhzbHanzi hz = new ZhzbHanzi(newZi, bitCounts);
	        	ZhzbHanzi old = ziku.put(bitCounts, hz);
	        	if(old != null) {
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

	public LinkedHashMap<String, ZhzbHanzi> readZikuFile(String fileName) {
		return readZikuFile(fileName, null);
	}
	
	public LinkedHashMap<String, ZhzbHanzi> readZikuFile(String fileName, LinkedHashMap<String, ZhzbHanzi> ziku) {
		if(ziku == null) {
			ziku = new LinkedHashMap<String, ZhzbHanzi>();
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
	        	
	        	ZhzbHanzi hz = new ZhzbHanzi(newZi, bitCounts);
	        	String[] bitCountStrings = bitCounts.split(",");
	        	if(bitCountStrings.length > 50) {
	        		System.out.println("Ingore too long line[" + lineNum + "]:" + line);
	        		continue;
	        	}

	        	//比对行点总数和列点总数
	        	int rowBits = hz.getTotalBits();
	        	int[] bitCount = hz.getBitCount();
	        	int colBits = 0;
	        	for(int i = 24; i < 48; i++) {
	        		colBits += bitCount[i];
	        	}
	        	if(rowBits != colBits) {
	        		System.out.println("Ingore row and col total bits mismatch line[" + lineNum + "]:" + line);
	        		continue;
	        	}
	        	
	        	//去除含单点的
	        	if(bitCounts.contains(",0,1,0") || bitCounts.startsWith("1,0,") || bitCounts.endsWith(",0,1,")) {
	        		System.out.println("Ingore contain single point line[" + lineNum + "]:" + line);
	        		continue;
	        	}
	        	
	        	ZhzbHanzi old = ziku.get(bitCounts);
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
	
	public void dumpHanziList(LinkedHashMap<String, ZhzbHanzi> hanziList, String file) {
		try {
			FileOutputStream haizi = new FileOutputStream(file);
			OutputStreamWriter haiziWriter = new OutputStreamWriter(haizi, "GBK");

			for(Entry<String, ZhzbHanzi> hzEntry : hanziList.entrySet()){
				haiziWriter.append(hzEntry.getValue().getWenzi() + "," + hzEntry.getKey() + "\r\n");
			}
			haiziWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void dumpHanziList(ZhzbHanzi[] hanziList, String file) {
		try {
			FileOutputStream haizi = new FileOutputStream(file);
			OutputStreamWriter haiziWriter = new OutputStreamWriter(haizi, "GBK");

			for(ZhzbHanzi hz : hanziList){
				haiziWriter.append(hz.getWenzi() + "," + hz.getBitCountString() + "\r\n");
			}
			haiziWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ZikuReader zikuReader = new ZikuReader();

//		LinkedHashMap<String, ZhzbHanzi> zikuWindyii = zikuReader.readZikuFile("ziku_windyii.txt");
//		zikuReader.dumpHanziList(zikuWindyii, "ziku_windyii_clean.txt");
//		
//		LinkedHashMap<String, ZhzbHanzi> ziku1 = zikuReader.readZikuFile("resource/ziku1.txt");
//		zikuReader.dumpHanziList(ziku1, "resource/ziku1_clean.txt");
		
		
//		LinkedHashMap<String, ZhzbHanzi> ziku2 = zikuReader.readZikuFile("ziku2.txt");
//		zikuReader.dumpHanziList(ziku2, "ziku2_clean.txt");
		
//		LinkedHashMap<String, ZhzbHanzi> ziku1_clean_windyii = zikuReader.readZikuFile("resource_origin/ziku1_clean_windyii.txt");
//		zikuReader.dumpHanziList(ziku1_clean_windyii, "resource_origin/ziku1_clean_windyii.txt");
		
//		LinkedHashMap<String, ZhzbHanzi> ziku = zikuReader.readZikuFile("ziku.txt");
//		zikuReader.dumpHanziList(ziku, "ziku_clean.txt");
		
		combineZhzbAndWindyiiZiku();
		
//		orderZikuFile("resource_origin/ziku1_clean_windyii.txt", "resource_origin/ziku1_clean_windyii_ordered.txt");
		
	}

	public static void combineZhzbAndWindyiiZiku() {
		ZikuReader zikuReader = new ZikuReader();
		LinkedHashMap<String, ZhzbHanzi> ziku2 = zikuReader.readZikuFile("resource_origin/ziku1_clean_windyii.txt");
		zikuReader.readZikuFile("ziku_windyii_clean.txt", ziku2);
		zikuReader.dumpHanziList(ziku2, "ziku_all.txt");
//		zikuReader.dumpHanziList(ziku2, "resource_origin/ziku1_clean2_windyii.txt");
		
		ZhzbHanzi[] sortedZiku = new ZhzbHanzi[ziku2.size()];
		Arrays.sort(ziku2.values().toArray(sortedZiku), new TotalBitsComparator());
		zikuReader.dumpHanziList(sortedZiku, "ziku.txt");
	}
	
	public static void orderZikuFile(String fileName, String orderedFileName) {
		System.out.println("Order ziku file: " + fileName); 
		ZikuReader zikuReader = new ZikuReader();
		LinkedHashMap<String, ZhzbHanzi> ziku2 = zikuReader.readZikuFile(fileName);
		ZhzbHanzi[] sortedZiku = new ZhzbHanzi[ziku2.size()];
		Arrays.sort(ziku2.values().toArray(sortedZiku), new TotalBitsComparator());
		zikuReader.dumpHanziList(sortedZiku, orderedFileName);
		System.out.println("Ordered ziku file: " + orderedFileName); 
	}
	
	static class TotalBitsComparator implements Comparator<ZhzbHanzi> {
		public int compare(ZhzbHanzi o1, ZhzbHanzi o2){
			ZhzbHanzi s1 = (ZhzbHanzi)o1;
			ZhzbHanzi s2 = (ZhzbHanzi)o2;
			int result = s1.getTotalBits() > s2.getTotalBits() ? 1 : (s1.getTotalBits() == s2.getTotalBits() ? 0 : -1);
			return result;
		} 
	}
	

}
