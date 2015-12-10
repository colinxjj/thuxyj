package faguan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.TimerTask;

public class AnswerFaguanTask extends TimerTask {

	private String _dir;
	private HanziIdentifier _hanziIdentifier;
	private int _faguanFileMinLength = 5000;
	
	public AnswerFaguanTask(String dir, HanziIdentifier hanziIdentifier) {
		_dir = dir;
		_hanziIdentifier = hanziIdentifier;
	}


	@Override
	public void run() {
	    File[] files = new File(_dir).listFiles();
	    for(File fuguanFile : files) {
	    	String fileName = fuguanFile.getName();
	    	if(fileName.startsWith("box_") && fileName.endsWith("'s.txt")) {
	    		System.out.println("��ʼ����" + fileName);
	    		long start = System.currentTimeMillis();
	    		dealfile(fuguanFile);
	    		System.out.println("����" + fileName + "��ɣ���ʱ" + (System.currentTimeMillis() - start) + "����");
	    	}
	    }
	}


	private void dealfile(File fuguanFile) {
		String faguanAnswerFile = fuguanFile.getParent() + "/answer_" + fuguanFile.getName();

		HanziReader reader = new HanziReader();
		long len = fuguanFile.length();
		int retryTimes = 5;
		String errMsg = "";

		//�ȴ�ץ������ļ�����
		while(retryTimes > 0 && len < _faguanFileMinLength) {
			errMsg = "�ļ����Ȳ�����Ϊ" + len;
			System.out.println("����" + fuguanFile.getName() +"�쳣��" + errMsg + ",�ȴ�0.2�������");
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
			
			retryTimes--;
			len = fuguanFile.length();
		}

		ArrayList<Hanzi> hanziTwo = reader.parseOriginFile(fuguanFile.getAbsolutePath());
		
		//need to backup!
		try{
			File fgBakFile = new File(fuguanFile.getPath() + ".bak");
			fgBakFile.delete();
			fuguanFile.renameTo(fgBakFile);
		}catch(Exception e){
		}
			
		if(hanziTwo != null && hanziTwo.size() >= 2) {
			System.out.println("��ȷ");
			int count = hanziTwo.size();
			Hanzi hanzi1 = hanziTwo.get(count - 2);
			Hanzi hanzi2 = hanziTwo.get(count - 1);
			IdentifyResult result1 = _hanziIdentifier.identify(hanzi1);
			IdentifyResult result2 =_hanziIdentifier.identify(hanzi2);
			
			String retString = "answer " + result1.getHanzi().getWenzi() + result2.getHanzi().getWenzi();
			writeFaguanAnswer(faguanAnswerFile, retString);
			
			System.out.println(hanzi1.getDisplayString());
			System.out.println(hanzi2.getDisplayString());
			System.out.println("��ѡpos: " + result1.getPosition() + ", " + result1.getHanzi().getWenzi() +", delta: " + result1.getDelta());
			System.out.println("��ѡpos: " + result2.getPosition() + ", " + result2.getHanzi().getWenzi() +", delta: " + result2.getDelta());
			System.out.println(retString);
		} else {
			System.out.println("�쳣");
//			String retString = "follow judge"; // for redisplay
//			String retString = "retry";
			String retString = "answer ����";
			System.out.println(retString);
			writeFaguanAnswer(faguanAnswerFile, retString);
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

	private void writeFaguanAnswer(String answerFile, String content) {
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
