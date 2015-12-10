package songbao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.TimerTask;

import xyj.Room;

public class ReadShovelMapTask extends TimerTask {

	private String _dir;
	private TreasureShovelMapIdentifier _mapIdentifier;
	private int _shovelMapFileMinLength = 2800;
	
	public ReadShovelMapTask(String dir, TreasureShovelMapIdentifier mapIdentifier) {
		_dir = dir;
		_mapIdentifier = mapIdentifier;
	}


	@Override
	public void run() {
	    File[] files = new File(_dir).listFiles();
	    for(File mapFile : files) {
	    	String fileName = mapFile.getName();
	    	if(fileName.startsWith("shovelmap_") && fileName.endsWith("'s.txt")) {
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
		while(retryTimes > 0 && len < _shovelMapFileMinLength) {
			errMsg = "�ļ����Ȳ�����Ϊ" + len;
			System.out.println("����" + mapFile.getName() +"�쳣��" + errMsg + ",�ȴ�0.2�������");
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
			
			retryTimes--;
			len = mapFile.length();
		}

		ArrayList<TreasureShovelMap> treasureMaps = TreasureShovelMapReader.parseOriginMapFile(mapFile.getAbsolutePath());
		
		//need to backup!
		try{
			File fgBakFile = new File(mapFile.getPath() + ".bak");
			fgBakFile.delete();
			mapFile.renameTo(fgBakFile);
		}catch(Exception e){
		}
	
		String retString = "#sh ���еص㣺����";
		if(treasureMaps != null && treasureMaps.size() >= 1) {
			System.out.println("��ȷ");
			int count = treasureMaps.size();
			TreasureShovelMap map = treasureMaps.get(count - 1);
			Room room = _mapIdentifier.identify(map);
			
			// #sh ���еص㣺��ɽ-������
			if (room != null) {
				retString = "#sh ���еص㣺 " + room.getName() + "-" + room.getArea() + "��";
			}
			writeMapAnswer(mapAnswerFile, retString);
		
			StringBuilder mapWriter = new StringBuilder();
			TreasureShovelMapHanzi[] hanziList = map.getRoom();
			for(int line = 0; line < TreasureShovelMapHanzi.RowSize; line++) {
				for(int i = 0;i < hanziList.length;i++) {
					Long hzLine = hanziList[i].getBitMap()[line];
					for (int j = TreasureShovelMapHanzi.ColSize - 1; j>=0; j--) {
						if((hzLine & (1L<<j)) != 0) {
							mapWriter.append("��");
						} else {
							mapWriter.append(" ");
						}
					}
				}
				mapWriter.append('\n');
			}
			System.out.println(mapWriter.toString());
			
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
