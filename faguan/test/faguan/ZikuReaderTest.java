package faguan;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import org.junit.Test;

public class ZikuReaderTest {

	@Test
	public void testReadZikuFileStringLinkedHashMapOfStringZhzbHanzi() {
		File a = new File("abc.txt");
		System.out.println("path: " + a.getPath());
		System.out.println("ab path: " + a.getAbsolutePath());
		System.out.println("name: " + a.getName());
		System.out.println("getParent: " + a.getParent());
		System.out.println();
		File b = new File(a.getAbsolutePath());
		System.out.println("path: " + b.getPath());
		System.out.println("ab path: " + b.getAbsolutePath());
		System.out.println("name: " + b.getName());
		System.out.println("getParent: " + b.getParent());
		fail("Not yet implemented");
	}

	@Test
	public void testFile() {
	    File[] files = new File("C:\\WinTin++").listFiles();
	    for(File fuguanFile : files) {
	    	printFileInfo(fuguanFile);
	    }
	}
	
	private void printFileInfo(File f) {
		System.out.println("path: " + f.getPath());
		System.out.println("ab path: " + f.getAbsolutePath());
		System.out.println("name: " + f.getName());
		System.out.println("getParent: " + f.getParent());
	}
	
	private static void testZhzbZiku() {
		//		HanziReader reader = new HanziReader();
		//		ArrayList<Hanzi> hanziList = reader.parseManualAnswerFile("resource/faguan_windyii.txt");
		//		ArrayList<Hanzi> hanziList2 = reader.parseManualAnswerFile("resource/faguan_windyme.txt");
		//		hanziList.addAll(hanziList2);
				
				HanziReader2 reader = new HanziReader2();
				ArrayList<Hanzi> hanziList = new ArrayList<Hanzi>(8000);
				hanziList.addAll(reader.parseOriginFile("resource/box_windyii's_20140722.txt"));
				hanziList.addAll(reader.parseOriginFile("resource/box_windyii's_20140729.txt"));
				hanziList.addAll(reader.parseOriginFile("resource/box_windyii's_20140731.txt"));
				hanziList.addAll(reader.parseOriginFile("resource/box_windyme's_20140722.txt"));
				hanziList.addAll(reader.parseOriginFile("resource/box_windyme's_20140729.txt"));
				hanziList.addAll(reader.parseOriginFile("resource/box_windyme's_20140731.txt"));
		
				ZikuReader zikuReader = new ZikuReader();
				Hashtable<String, ZhzbHanzi> ziku = zikuReader.readFile("resource/ziku.txt");
				String wenzi = null;
				int matchedZikuCount = 0;
				int matchedZikuNewCount = 0;
				Hanzi hzMatch = null;
				Hashtable<String,Hanzi> zikuNow = new Hashtable<String,Hanzi>(); 
				for(Hanzi hz : hanziList) {
					String colBitCountString = hz.getBitCountString();
//					if((wenzi = ziku.get(colBitCountString)) != null) {
//						matchedZikuCount++;
//						System.out.println("Match! ziku " + colBitCountString);
//						System.out.println("Match! " + hz.getWenzi() + "," + wenzi);
//						System.out.println(hz.getDisplayString() + hz.getWenzi());
//					}
					if((hzMatch = zikuNow.get(colBitCountString)) != null) {
//						matchedZikuNewCount++;
//						System.out.println("Match! zikuNow" + colBitCountString);
//						System.out.println("Match! " + hz.getWenzi() + "," + wenzi);
//						System.out.println(hz.getDisplayString() + hz.getWenzi());
//						System.out.println(hzMatch.getDisplayString() + hzMatch.getWenzi());
//						System.out.println(hz.getBitMapString());
//						System.out.println(hzMatch.getBitMapString());
					} else {
						zikuNow.put(colBitCountString, hz);
					}
				}
				
				System.out.println("Matched ziku " + matchedZikuCount+ "/" + hanziList.size());
				System.out.println("Matched zikuNow " + matchedZikuNewCount+ "/" + hanziList.size());
	}

	private static String reverseString(String colBitCountString){
		StringBuilder retBuilder = new StringBuilder();
		String[] bitCounts = colBitCountString.split(",");
		for(int i = bitCounts.length - 1 ; i >=0 ; i--) {
			if(bitCounts[i].length() > 0) {
				retBuilder.append(bitCounts[i] + ",");
			}
		}
		
		return retBuilder.toString();
	}
}
