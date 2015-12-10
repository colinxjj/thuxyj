package wabao;

import java.util.Timer;

public class Program {

	
	public static void main(String[] args) {
	    System.out.println("*********    ��ʼ�����ڱ�����                                ****");
	    System.out.println("*********    ��Ȩ���У����(windyii)   ****");
	    System.out.println("*********    QQ:9915538           ****");
	    System.out.println("*********    v1.0.3, 2015.06.02   ****");
	    System.out.println("");
	    
		if(args.length < 1) {
			usage();
			return;
		}
		String dir =  args[0]; //"c:\wintin++\";
		
	    System.out.println("��ʼ�����ֿ⣬���Ժ�...");
	    long start = System.currentTimeMillis();
	    
	    TreasureMapIdentifier identifier = new TreasureMapIdentifierImpl();
	    int zikuSize = identifier.initHanziku();
	    ReadMapTask task = new ReadMapTask(dir, identifier);
	    
	    System.out.println("�����ڱ��ֿ�ɹ���������: " + zikuSize + "����ʱ" + (System.currentTimeMillis()-start) + "����");
	    
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
		System.out.println("java -classpath xyj_windyii_x.x.x.jar wabao.Program <dir>");
		System.out.println("input file: map_<id>'s.txt");
		System.out.println("output file: answer_map_<id>'s.txt");
	}


}
