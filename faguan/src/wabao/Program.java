package wabao;

import java.util.Timer;

public class Program {

	
	public static void main(String[] args) {
	    System.out.println("*********    开始启动挖宝机器                                ****");
	    System.out.println("*********    版权所有：风儿(windyii)   ****");
	    System.out.println("*********    QQ:9915538           ****");
	    System.out.println("*********    v1.0.3, 2015.06.02   ****");
	    System.out.println("");
	    
		if(args.length < 1) {
			usage();
			return;
		}
		String dir =  args[0]; //"c:\wintin++\";
		
	    System.out.println("开始加载字库，请稍候...");
	    long start = System.currentTimeMillis();
	    
	    TreasureMapIdentifier identifier = new TreasureMapIdentifierImpl();
	    int zikuSize = identifier.initHanziku();
	    ReadMapTask task = new ReadMapTask(dir, identifier);
	    
	    System.out.println("加载挖宝字库成功！汉字数: " + zikuSize + "，耗时" + (System.currentTimeMillis()-start) + "毫秒");
	    
	    Timer timer = new Timer();
	    timer.schedule(task, 10L, 2000L);

	    //主程序循环，防止退出
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
