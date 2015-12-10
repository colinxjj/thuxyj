package songbao;

import java.util.Timer;

public class Program {

	
	public static void main(String[] args) {
	    System.out.println("*********    ��ʼ�����ͱ�����                                ****");
	    System.out.println("*********    ��Ȩ���У����(windyii)   ****");
	    System.out.println("*********    QQ:9915538           ****");
	    System.out.println("*********    v1.0.0, 2015.10.16   ****");
	    System.out.println("");
	    
		if(args.length < 1) {
			usage();
			return;
		}
		String dir =  args[0]; //"c:\wintin++\";
		
	    System.out.println("��ʼ�����ֿ⣬���Ժ�...");
	    long start = System.currentTimeMillis();
	    
	    TreasureShovelMapIdentifier identifier = new TreasureShovelMapIdentifierImpl();
	    int zikuSize = identifier.initHanziku();
	    ReadShovelMapTask task = new ReadShovelMapTask(dir, identifier);
	    
	    System.out.println("�����ͱ��ֿ�ɹ���������: " + zikuSize + "����ʱ" + (System.currentTimeMillis()-start) + "����");
	    
	    Timer timer = new Timer();
	    timer.schedule(task, 10L, 2000L);

	    //������ѭ������ֹ�˳�
	    try
	    {
	      for (;;)
	      {
	        Thread.sleep(30000L);
	      }
	    }
	    catch (Exception ex) {}
		
	}


	
	private static void usage() {
		System.out.println("java -classpath xyj_windyii_x.x.x.jar songbao.Program <dir>");
		System.out.println("input file: shovelmap_<id>'s.txt");
		System.out.println("output file: answer_shovelmap_<id>'s.txt");
	}


}
