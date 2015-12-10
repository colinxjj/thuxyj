package duishi;

import java.util.Timer;


public class Program {

	//�����ַ� ��¥
	//    ����������ʫ��Ϸ���ɲ販ʿ��һ��ʫ�ʵ������� 
	//�ߵ�����д��ǽ�ϡ���׼ȷ���(answer)ԭ����Ϊʤ�� 
	//
	//���磺�販ʿ�����ǽ��д��������ԭ�ϲ�һ��һ���� 
	//��Ӧ�ûش�answer ����ԭ�ϲ�һ��һ���� 

	public static void main(String[] args) {
		
		if(args.length < 1) {
			usage();
			return;
		}
		
		String dir =  args[0]; //"c:\wintin++\";
	    Timer timer = new Timer();
	    
	    AnswerPoemTask task = new AnswerPoemTask(dir);
	    timer.schedule(task, 10L, 1000L);
	    System.out.println("�販ʿ��ʫrobot������");
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
