package answer;

import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

public class AnswerTask
  extends TimerTask
{
  public static String dir = "E:/whn/mud/555/zMUD/";
  public static String dir2 = "";
  public static String esc = "\033";
  public static String line_start = esc + "[0m";
  public static boolean debug = false;
  
  public static String replaceStr(String source, String oldString, String newString)
  {
    if ((oldString == null) || (oldString.length() == 0)) {
      return source;
    }
    if (source == null) {
      return "";
    }
    if (newString == null) {
      newString = "";
    }
    StringBuffer output = new StringBuffer();
    int lengthOfSource = source.length();
    int lengthOfOld = oldString.length();
    int posStart = 0;
    int pos;
    while ((pos = source.indexOf(oldString, posStart)) >= 0)
    {
      output.append(source.substring(posStart, pos));
      output.append(newString);
      posStart = pos + lengthOfOld;
    }
    if (posStart < lengthOfSource) {
      output.append(source.substring(posStart));
    }
    return output.toString();
  }
  
  public void run()
  {
    String[] result = new String[2];
    long ctime = System.currentTimeMillis();
    
    String[] ss_file = new File(dir).list();
    dealfile(dir, ss_file);
    if (dir2.length() > 0)
    {
      ss_file = new File(dir2).list();
      dealfile(dir2, ss_file);
    }
  }
  
  public static void dealfile(String dir, String[] ss_file)
  {
    for (int k = 0; k < ss_file.length; k++)
    {
      String filename = ss_file[k];
      if ((ss_file[k].startsWith("kaoti_")) && (ss_file[k].endsWith(".txt"))) {
        try
        {
          System.out.println("开始处理" + filename);
          long l1 = System.currentTimeMillis();
          String s = FileUtil.readFile(dir + filename);
          StringTokenizer st = new StringTokenizer(s, "\r\n");
          StringBuffer sb = new StringBuffer();
          StringBuffer sb1 = new StringBuffer();
          StringBuffer sb2 = new StringBuffer();
          int rowcount = 0;
          while (st.hasMoreElements())
          {
            String line = (String)st.nextElement();
            if (line.getBytes().length > 95)
            {
              rowcount++;
              line = line.replace("■", "1");
              line = line.replaceAll("  ", " ");
              line = line.replaceAll(" ", "0");
              String line1 = line.substring(0, 24);
              String line2 = line.substring(24);
              sb1.append(line1 + "\r\n");
              sb2.append(line2 + "\r\n");
            }
          }
          if (rowcount != 24)
          {
            relook(dir, filename);
          }
          else
          {
            String answer2 = getanswer(sb1, sb2);
            System.out.println("write " + answer2 + "    处理" + filename + "完成，耗时" + (System.currentTimeMillis() - l1) + "毫秒");
            




            FileUtil.writeFile(dir + "write_" + filename, "write " + answer2 + "\r\n", false);
            

            deletefile(dir, filename, "");
          }
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
          deletefile(dir, filename, ".err");
        }
      } else if ((ss_file[k].startsWith("box_")) && (ss_file[k].endsWith(".txt"))) {
        try
        {
          System.out.println("开始处理" + filename);
          long l1 = System.currentTimeMillis();
          String s = FileUtil.readFile(dir + filename);
          if (s.lastIndexOf("这里没有 法官开始") != -1) {
            s = s.substring(s.lastIndexOf("这里没有 法官开始"));
          }
          int rowcount = new StringTokenizer(s, "\r\n").countTokens();
          StringTokenizer st = new StringTokenizer(s, "\r\n");
          StringBuffer sb = new StringBuffer();
          
          int row = 0;
          boolean start = false;boolean redisplay = false;
          
          StringBuffer sb1 = new StringBuffer();
          StringBuffer sb2 = new StringBuffer();
          while (st.hasMoreElements())
          {
            String line = (String)st.nextElement();
            if (line.getBytes().length > 95)
            {
              row++;
              

              line = replaceStr(line, "" + esc + "[1m", "");
              line = line.replaceAll("  ", " ");
              
              String line01 = "";
              char co = '0';
              for (char c : line.toCharArray())
              {
                if (Character.isDigit(c)) {
                  co = c;
                }
                if (c == ' ') {
                  line01 = line01 + co;
                }
              }
              String line1 = line01.substring(0, 24);
              String line2 = line01.substring(24);
              sb1.append(line1 + "\r\n");
              sb2.append(line2 + "\r\n");
            }
          }
          if (row < 24) {
            redisplay = true;
          }
          if (redisplay)
          {
            redisplay(dir, filename);
          }
          else
          {
            String answer2 = getanswer(sb1, sb2);
            System.out.println("answer " + answer2 + "    处理" + filename + "完成，耗时" + (System.currentTimeMillis() - l1) + "毫秒");
            




            FileUtil.writeFile(dir + "answer_" + filename, "answer " + answer2 + "\r\n", false);
            

            deletefile(dir, filename, "");
          }
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
          deletefile(dir, filename, ".err");
        }
      }
    }
  }
  
  private static String getanswer(StringBuffer sb1, StringBuffer sb2)
  {
    System.out.println("正确");
    
    int[][] ii = new int[2][24];
    int[][] ii2 = new int[2][24];
    String[] ss1 = new String[24];
    String[] ss2 = new String[24];
    ZKUtil.init(ss1);
    ZKUtil.init(ss2);
    


    sb1 = format01(sb1);
    sb2 = format01(sb2);
    
    String[] s_line1 = sb1.toString().split("\r\n");
    String[] s_line2 = sb2.toString().split("\r\n");
    for (int i = 0; i < 24; i++) {
      for (int j = 0; j < 24; j++)
      {
        //int tmp111_109 = j; String[] tmp111_107 = ss1;tmp111_107[tmp111_109] = (tmp111_107[tmp111_109] + s_line1[i].charAt(j));
    	  ss1[j] = ss1[j] + s_line1[i].charAt(j);
        //int tmp144_142 = j; String[] tmp144_140 = ss2;tmp144_140[tmp144_142] = (tmp144_140[tmp144_142] + s_line2[i].charAt(j));
    	  ss2[j] = ss2[j] + s_line2[i].charAt(j);
      }
    }
    String s1_result = sb1.toString();
    String s2_result = sb2.toString();
    if (ss1[0].replace(" ", "").length() > 12)
    {
      System.out.println("置换前背景s1");
      s1_result = s1_result.replaceAll(" ", "f");
      s1_result = s1_result.replaceAll("1", " ");
      s1_result = s1_result.replaceAll("f", "1");
    }
    if (ss2[0].replace(" ", "").length() > 12)
    {
      System.out.println("置换前背景s2");
      s2_result = s2_result.replaceAll(" ", "f");
      s2_result = s2_result.replaceAll("1", " ");
      s2_result = s2_result.replaceAll("f", "1");
    }
    s_line1 = s1_result.split("\r\n");
    s_line2 = s2_result.split("\r\n");
    





    qzcl(s_line1);
    qzcl(s_line2);
    
    System.out.println(format(getString(s_line1)));
    
    System.out.println(format(getString(s_line2)));
    ss1 = new String[24];
    ss2 = new String[24];
    ZKUtil.init(ss1);
    ZKUtil.init(ss2);
    for (int i = 0; i < 24; i++)
    {
      ii[0][i] = s_line1[i].replace(" ", "").length();
      ii2[0][i] = s_line2[i].replace(" ", "").length();
      for (int j = 0; j < 24; j++)
      {
        int tmp463_461 = j; String[] tmp463_459 = ss1;tmp463_459[tmp463_461] = (tmp463_459[tmp463_461] + s_line1[i].charAt(j)); int 
          tmp496_494 = j; String[] tmp496_492 = ss2;tmp496_492[tmp496_494] = (tmp496_492[tmp496_494] + s_line2[i].charAt(j));
      }
    }
    for (int i = 0; i < 24; i++)
    {
      ii[1][i] = ss1[i].replace(" ", "").length();
      ii2[1][i] = ss2[i].replace(" ", "").length();
    }
    int[] a1 = new int[48];
    int[] a2 = new int[48];
    System.arraycopy(ii[0], 0, a1, 0, 24);
    System.arraycopy(ii[1], 0, a1, 24, 24);
    System.arraycopy(ii2[0], 0, a2, 0, 24);
    System.arraycopy(ii2[1], 0, a2, 24, 24);
    





    String answer1 = Answer.getAnswer(a1);
    String answer2 = Answer.getAnswer(a2);
    return answer1 + answer2;
  }
  
  public static void deletefile(String filedir, String filename, String errflag)
  {
    if (debug)
    {
      SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
      
      String systime = sdf.format(new Date());
      String newfile = filedir + "faguan/" + filename + systime + errflag;
      if (new File(filedir + filename).renameTo(new File(newfile))) {
        System.out.println("转移成功!");
      } else {
        System.out.println("转移失败!");
      }
    }
    else if (new File(filedir + filename).delete())
    {
      System.out.println("删除成功!");
    }
    else
    {
      System.out.println("删除失败!");
    }
  }
  
  public static void redisplay(String dir, String filename)
  {
    System.out.println("redisplay");
    if (debug)
    {
      FileUtil.writeFile(dir + "answer_" + filename, "say redisplay", false);
      

      SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
      
      String systime = sdf.format(new Date());
      if (new File(dir + filename).renameTo(new File(dir + "faguan/" + filename + systime + ".res"))) {
        System.out.println("删除成功!");
      } else {
        System.out.println("删除失败!");
      }
    }
    else if (new File(dir + filename).delete())
    {
      System.out.println("删除成功!");
    }
    else
    {
      System.out.println("删除失败!");
    }
  }
  
  public static void relook(String dir, String filename)
  {
    System.out.println("relook");
    
    FileUtil.writeFile(dir + "write_" + filename, "follow 重新显示考题", false);
    

    SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
    
    String systime = sdf.format(new Date());
    if (new File(dir + filename).renameTo(new File(dir + "faguan/" + filename + systime + ".res"))) {
      System.out.println("删除成功!");
    } else {
      System.out.println("删除失败!");
    }
  }
  
  public static String format(String s)
  {
    s = s.replaceAll("1", "■");
    s = s.replaceAll(" ", "　");
    return s;
  }
  
  public static StringBuffer format01(StringBuffer sb)
  {
    String s = sb.toString().replaceAll("\r\n", "");
    String bg = s.substring(0, 1);
    String font = s.replaceAll(bg, "").substring(0, 1);
    if (s.replaceAll(bg, "").replaceAll(font, "").trim().length() != 0)
    {
      String other = s.replaceAll(bg, "").replaceAll(font, "").substring(0, 1);
      
      System.out.println("背景色" + bg + "前景色" + font + "第三颜色：" + other);
      s = sb.toString().replaceAll(other, " ");
    }
    else
    {
      s = sb.toString();
    }
    s = s.replaceAll(bg, " ");
    s = s.replaceAll(font, "1");
    
    return new StringBuffer(s);
  }
  
  public static void qzcl(String[] s)
  {
    char[][] cc = new char[24][24];
    for (int i = 0; i < s.length; i++) {
      cc[i] = s[i].toCharArray();
    }
    for (int i = 0; i < s.length; i++) {
      for (int j = 0; j < s[i].length(); j++) {
        if ('1' == cc[i][j])
        {
          boolean isZd = false;
          int countds = countZwds(cc, i, j);
          if (countds < 1)
          {
            isZd = true;
          }
          else if (countds == 1)
          {
            if (!isZd) {
              isZd = isZd(cc, i - 1, j - 1);
            }
            if (!isZd) {
              isZd = isZd(cc, i - 1, j);
            }
            if (!isZd) {
              isZd = isZd(cc, i - 1, j + 1);
            }
            if (!isZd) {
              isZd = isZd(cc, i, j - 1);
            }
            if (!isZd) {
              isZd = isZd(cc, i, j + 1);
            }
            if (!isZd) {
              isZd = isZd(cc, i + 1, j - 1);
            }
            if (!isZd) {
              isZd = isZd(cc, i + 1, j);
            }
            if (!isZd) {
              isZd = isZd(cc, i + 1, j + 1);
            }
          }
          if (isZd) {
            cc[i][j] = 32;
          }
        }
      }
    }
    for (int i = 0; i < cc.length; i++)
    {
      s[i] = "";
      for (int j = 0; j < cc[i].length; j++)
      {
        int tmp275_274 = i;s[tmp275_274] = (s[tmp275_274] + cc[i][j]);
      }
    }
  }
  
  public static int countZwds(char[][] cc, int i, int j)
  {
    return countDd(cc, i - 1, j - 1) + countDd(cc, i - 1, j) + countDd(cc, i - 1, j + 1) + countDd(cc, i, j - 1) + countDd(cc, i, j + 1) + countDd(cc, i + 1, j - 1) + countDd(cc, i + 1, j) + countDd(cc, i + 1, j + 1);
  }
  
  public static boolean isZd(char[][] cc, int i, int j)
  {
    boolean iszd = false;
    if ((countDd(cc, i, j) == 1) && 
      (countZwds(cc, i, j) == 1)) {
      iszd = true;
    }
    return iszd;
  }
  
  public static boolean getYx(int i, int j)
  {
    return ((i >= 0) && (i < 24)) && ((j >= 0) && (j < 24));
  }
  
  public static int countDd(char[][] cc, int i, int j)
  {
    if (getYx(i, j))
    {
      if ('1' == cc[i][j]) {
        return 1;
      }
      if (' ' == cc[i][j]) {
        return 0;
      }
      return 0;
    }
    return 0;
  }
  
  public static String getString(String[] s)
  {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < s.length; i++) {
      sb.append(s[i] + "\r\n");
    }
    return sb.toString();
  }
  
  public static void main(String[] args)
  {
    System.out.println("*********    开始启动法官机器         ****");
    System.out.println("*********    版权所有：薇薇(weee)     ****");
    System.out.println("*********    QQ:893705731           ****");
    System.out.println("*********    2008.08.01             ****");
    System.out.println("");
    
    dir = new File("").getAbsolutePath() + "/";
    if (args.length != 0) {
      debug = !"0".equals(args[0]);
    }
    if (args.length >= 2) {
      dir2 = args[1];
    }
    if (!new File(dir + "/faguan/").exists()) {
      new File(dir + "/faguan/").mkdir();
    }
    System.out.println("开始加载字库，请稍候...");
    long t = System.currentTimeMillis();
    













    Answer an = new Answer();
    Answer.initziku("ziku.txt");
    System.out.println("加载字库成功！" + Answer.ii_ziku.length);
    











    Timer timer = new Timer();
    
    AnswerTask task = new AnswerTask();
    timer.schedule(task, 0L, 2000L);
    System.out.println("法官robot已启动");
    try
    {
      for (;;)
      {
        Thread.currentThread();Thread.sleep(30000L);
      }
    }
    catch (Exception ex) {}
  }
}


/* Location:           C:\Users\qianshaohua\Desktop\answer.jar
 * Qualified Name:     answer.AnswerTask
 * JD-Core Version:    0.7.0.1
 */