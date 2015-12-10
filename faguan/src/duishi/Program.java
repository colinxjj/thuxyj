package duishi;

import java.util.Timer;


public class Program {

	//长安乐坊 三楼
	//    本社新增猜诗游戏，由茶博士将一句诗词的若干字 
	//颠倒次序，写在墙上。能准确答出(answer)原句者为胜。 
	//
	//例如：茶博士提笔在墙上写道：离离原上草一荣一枯岁 
	//你应该回答：answer 离离原上草一岁一枯荣 

	public static void main(String[] args) {
		
		if(args.length < 1) {
			usage();
			return;
		}
		
		String dir =  args[0]; //"c:\wintin++\";
	    Timer timer = new Timer();
	    
	    AnswerPoemTask task = new AnswerPoemTask(dir);
	    timer.schedule(task, 10L, 1000L);
	    System.out.println("茶博士对诗robot已启动");
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
		System.out.println("java -classpath xyj_windyii.jar duishi.Program <dir>");
		System.out.println("input file: poem_<id>'s.txt");
		System.out.println("output file: answer_poem_<id>'s.txt");
	}

}
