package answer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ZKUtil
{
  public static String s_bg = " _-^`',.";
  
  public static void work(String fileName)
  {
    StringBuffer content = new StringBuffer();
    
    BufferedReader filedata = null;
    String dir = "ziku/";
    try
    {
      filedata = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
      

      String fileline = filedata.readLine();
      content.append(fileline);
      StringBuffer sb = new StringBuffer();
      String line = "";
      int count = 0;
      while ((fileline = filedata.readLine()) != null) {
        if (fileline.indexOf("法官说:") != -1)
        {
          count++;
          if (count % 1000 == 0) {
            System.out.println("正在处理" + count);
          }
          String hanzi = fileline.substring(fileline.indexOf("法官说:") + "法官说:".length());
          
          StringBuffer s1 = new StringBuffer();
          StringBuffer s2 = new StringBuffer();
          try
          {
            int[][] ii = new int[2][24];
            int[][] ii2 = new int[2][24];
            String[] ss1 = new String[24];
            String[] ss2 = new String[24];
            init(ss1);
            init(ss2);
            for (int i = 0; i < 24; i++)
            {
              line = filedata.readLine();
              line = line.replace("■", "1");
              line = line.replaceAll("  ", " ");
              
              s1.append(line.substring(0, 24) + "\r\n");
              s2.append(line.substring(24) + "\r\n");
              ii[0][i] = line.substring(0, 24).replace(" ", "").length();
              
              ii2[0][i] = line.substring(24).replace(" ", "").length();
              for (int j = 0; j < 24; j++)
              {
                int tmp370_368 = j; String[] tmp370_366 = ss1;tmp370_366[tmp370_368] = (tmp370_366[tmp370_368] + line.charAt(j)); int 
                  tmp400_398 = j; String[] tmp400_396 = ss2;tmp400_396[tmp400_398] = (tmp400_396[tmp400_398] + line.charAt(j + 24));
              }
            }
            for (int i = 0; i < 24; i++)
            {
              ii[1][i] = ss1[i].replace(" ", "").length();
              ii2[1][i] = ss2[i].replace(" ", "").length();
            }
            FileUtil.writeFile("ziku.txt", hanzi.substring(0, 1) + "," + getString(ii) + "\r\n", true);
            

            FileUtil.writeFile("ziku.txt", hanzi.substring(1) + "," + getString(ii2) + "\r\n", true);
          }
          catch (Exception ex)
          {
            System.out.println(hanzi);
            ex.printStackTrace();
          }
        }
      }
      return;
    }
    catch (FileNotFoundException ex1)
    {
      System.out.println("文件:" + fileName + "不存在!");
      ex1.printStackTrace();
    }
    catch (IOException ex2)
    {
      System.out.println("读取文件:" + fileName + "时,出现输入输出异常!");
      ex2.printStackTrace();
    }
    finally
    {
      if (filedata != null) {
        try
        {
          filedata.close();
        }
        catch (IOException ex3)
        {
          System.out.println("关闭文件:" + fileName + "时,出现输入输出异常!");
          ex3.printStackTrace();
        }
      }
    }
  }
  
  public static void workbz(String fileName)
  {
    StringBuffer content = new StringBuffer();
    
    BufferedReader filedata = null;
    String dir = "ziku/";
    try
    {
      filedata = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
      

      String fileline = filedata.readLine();
      content.append(fileline);
      StringBuffer sb = new StringBuffer();
      String line = "";
      int count = 0;
      while ((fileline = filedata.readLine()) != null) {
        if (fileline.indexOf("法官说:") != -1)
        {
          count++;
          if (count % 1000 == 0) {
            System.out.println("正在处理" + count);
          }
          String hanzi = fileline.substring(fileline.indexOf("法官说:") + "法官说:".length());
          try
          {
            int hanzicount = hanzi.length();
            
            int[][][] ii = new int[hanzicount][2][24];
            
            String[][] ss1 = new String[hanzicount][24];
            for (int i = 0; i < ss1.length; i++) {
              init(ss1[i]);
            }
            for (int i = 0; i < 24; i++)
            {
              line = filedata.readLine();
              for (int j = 0; j < hanzicount; j++)
              {
                int startpos = j * 24;
                


                String templine = line.substring(startpos, startpos + 24);
                for (int k = 0; k < 24; k++)
                {
                  int tmp263_261 = k; String[] tmp263_260 = ss1[j];tmp263_260[tmp263_261] = (tmp263_260[tmp263_261] + templine.charAt(k));
                }
                for (int k = 0; k < s_bg.length(); k++)
                {
                  String s = "" + s_bg.charAt(k);
                  templine = AnswerTask.replaceStr(templine, s, "");
                }
                ii[j][0][i] = templine.length();
              }
            }
            for (int k = 0; k < hanzicount; k++) {
              for (int i = 0; i < 24; i++)
              {
                String tempcloumn = ss1[k][i];
                for (int j = 0; j < s_bg.length(); j++) {
                  tempcloumn = AnswerTask.replaceStr(tempcloumn, "" + s_bg.charAt(j), "");
                }
                ii[k][1][i] = tempcloumn.length();
              }
            }
            for (int i = 0; i < hanzicount; i++) {
              FileUtil.writeFile("zikubz.txt", "" + hanzi.charAt(i) + "," + getString(ii[i]) + "\r\n", true);
            }
          }
          catch (Exception ex)
          {
            System.out.println(hanzi);
            ex.printStackTrace();
          }
        }
      }
      return;
    }
    catch (FileNotFoundException ex1)
    {
      System.out.println("文件:" + fileName + "不存在!");
      ex1.printStackTrace();
    }
    catch (IOException ex2)
    {
      System.out.println("读取文件:" + fileName + "时,出现输入输出异常!");
      ex2.printStackTrace();
    }
    finally
    {
      if (filedata != null) {
        try
        {
          filedata.close();
        }
        catch (IOException ex3)
        {
          System.out.println("关闭文件:" + fileName + "时,出现输入输出异常!");
          ex3.printStackTrace();
        }
      }
    }
  }
  
  public static String getFileName(String dir, String name, String ext)
  {
    int i = 0;
    String fileatt = "";
    while (new File(dir + name + fileatt + ext).exists())
    {
      i++;
      fileatt = "" + i;
    }
    return dir + name + fileatt + ext;
  }
  
  public static void main(String[] args)
  {
    workbz("hzbz.txt");
  }
  
  public static void init(String[] ss)
  {
    for (int i = 0; i < ss.length; i++) {
      ss[i] = "";
    }
  }
  
  public static String getString(int[][] ii)
  {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < ii.length; i++) {
      for (int j = 0; j < ii[i].length; j++) {
        sb.append(ii[i][j] + ",");
      }
    }
    return sb.toString();
  }
  
  public static String getString(int[] ii)
  {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < ii.length; i++) {
      sb.append(ii[i] + ",");
    }
    return sb.toString();
  }
}


/* Location:           C:\Users\qianshaohua\Desktop\answer.jar
 * Qualified Name:     answer.ZKUtil
 * JD-Core Version:    0.7.0.1
 */