package wabao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.TimerTask;

import xyj.Room;

public class ReadMapTask extends TimerTask {

	private String _dir;
	private TreasureMapIdentifier _mapIdentifier;
	private int _treasureMapFileMinLength = 4000;
	
	public ReadMapTask(String dir, TreasureMapIdentifier mapIdentifier) {
		_dir = dir;
		_mapIdentifier = mapIdentifier;
	}


	@Override
	public void run() {
	    File[] files = new File(_dir).listFiles();
	    for(File mapFile : files) {
	    	String fileName = mapFile.getName();
	    	if(fileName.startsWith("map_") && fileName.endsWith("'s.txt")) {
	    		System.out.println("��ʼ����" + fileName);
	    		long start = System.currentTimeMillis();
	    		dealfile(mapFile);
	    		System.out.println("����" + fileName + "��ɣ���ʱ" + (System.currentTimeMillis() - start) + "����");
	    	}
	    }
	}


	private void dealfile(File mapFile) {
		String mapAnswerFile = mapFile.getParent() + "/answer_" + mapFile.getName();

		long len = mapFile.length();
		int retryTimes = 5;
		String errMsg = "";

		//�ȴ�ץ������ļ�����
		while(retryTimes > 0 && len < _treasureMapFileMinLength) {
			errMsg = "�ļ����Ȳ�����Ϊ" + len;
			System.out.println("����" + mapFile.getName() +"�쳣��" + errMsg + ",�ȴ�0.2�������");
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
			
			retryTimes--;
			len = mapFile.length();
		}

		ArrayList<TreasureMap> treasureMaps = TreasureMapReader.tryParseOriginMapFile(mapFile.getAbsolutePath());
		
		//need to backup!
		try{
			File fgBakFile = new File(mapFile.getPath() + ".bak");
			fgBakFile.delete();
			mapFile.renameTo(fgBakFile);
		}catch(Exception e){
		}
	
		String retString = "#sh �ر��ص㣺����";
		if(treasureMaps != null && treasureMaps.size() >= 1) {
			System.out.println("��ȷ");
			int count = treasureMaps.size();
			TreasureMap map = treasureMaps.get(count - 1);
			Room room = _mapIdentifier.identify(map);
			
			// #sh �ر��ص㣺��ɽ-������
			if (room != null) {
				retString = "#sh �ر��ص㣺 " + room.getName() + "-" + room.getArea() + "��";
			}
			writeMapAnswer(mapAnswerFile, retString);
			
			TreasureMapHanzi[] hanziList = map.getArea();
			for(int i = 0;i < hanziList.length;i++) {
				TreasureMapHanzi hz = hanziList[i];
				System.out.println(hz.getDisplayString());
			}
			System.out.println(retString);
		} else {
			System.out.println("�쳣");
			System.out.println(retString);
			writeMapAnswer(mapAnswerFile, retString);
		}
	}

	private static boolean backupFaguanBox(String fileName, String bakFile) {
        try {  
            FileInputStream in = new java.io.FileInputStream(fileName);  
            FileOutputStream out = new FileOutputStream(bakFile, true);  
            byte[] bt = new byte[1024];  
            int count;  
            while ((count = in.read(bt)) > 0) {  
                out.write(bt, 0, count);  
            }  
            in.close();  
            out.close();  
            return true;  
        } catch (IOException ex) {  
            return false;  
        }  
	}

	private void writeMapAnswer(String answerFile, String content) {
		try {
			FileOutputStream os = new FileOutputStream(answerFile);
			OutputStreamWriter osw = new OutputStreamWriter(os, "GBK");
			osw.write(content);
			osw.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}




}
