package answer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

public class Answer
  implements Serializable
{
  public static int[][] ii_ziku = (int[][])null;
  public static String[] hanzi = null;
  
  public static void initziku(String filename)
  {
    long l1 = System.currentTimeMillis();
    String s = FileUtil.readFile(filename);
    StringTokenizer st = new StringTokenizer(s, "\r\n");
    int rowcount = st.countTokens();
    ii_ziku = new int[rowcount][48];
    hanzi = new String[rowcount];
    int row = 0;
    while (st.hasMoreElements())
    {
      String line = st.nextToken();
      String[] ss = line.split(",");
      for (int i = 0; i < ss.length; i++) {
        if (i == 0) {
          hanzi[row] = ss[0];
        } else {
          for (int j = 0; j < 48; j++) {
            ii_ziku[row][j] = Integer.parseInt(ss[(j + 1)]);
          }
        }
      }
      row++;
      if (row % 5000 == 0) {
        System.out.println("正在处理" + row);
      }
    }
    long l2 = System.currentTimeMillis();
    System.out.println("加载字库成功!兄1�7" + ii_ziku.length + "条记录，耗时" + (l2 - l1) + "毫秒");
  }
  
  public static void getResult(String fileName)
  {
    StringBuffer content = new StringBuffer();
    
    BufferedReader filedata = null;
    String dir = "";
    try
    {
      filedata = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
      



      StringBuffer sb = new StringBuffer();
      String line = "";
      int count = 0;
      

      StringBuffer s1 = new StringBuffer();
      StringBuffer s2 = new StringBuffer();
      
      int[][] ii = new int[2][24];
      int[][] ii2 = new int[2][24];
      String[] ss1 = new String[24];
      String[] ss2 = new String[24];
      ZKUtil.init(ss1);
      ZKUtil.init(ss2);
      for (int i = 0; i < 24; i++)
      {
        line = filedata.readLine();
        

        System.out.println(line);
        s1.append(line.substring(0, 24) + "\r\n");
        s2.append(line.substring(24) + "\r\n");
        ii[0][i] = line.substring(0, 24).replace(" ", "").length();
        
        ii2[0][i] = line.substring(24).replace(" ", "").length();
        for (int j = 0; j < 24; j++)
        {
          int tmp270_268 = j; String[] tmp270_266 = ss1;tmp270_266[tmp270_268] = (tmp270_266[tmp270_268] + line.charAt(j)); int 
            tmp300_298 = j; String[] tmp300_296 = ss2;tmp300_296[tmp300_298] = (tmp300_296[tmp300_298] + line.charAt(j + 24));
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
      
      System.out.println(ZKUtil.getString(a1));
      System.out.println(ZKUtil.getString(a2));
      System.out.println(getAnswer(a1));
      








































//      return "";
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
  
  public static String getAnswer(int[] ii)
  {
    int minchazhi = 99999999;
    int minpos = 0;
    int minpos2 = 0;
    int chazhi2 = 0;
    for (int i = 0; i < ii_ziku.length; i++)
    {
      int chazhi = 0;
      for (int j = 0; j < 48; j++) {
        chazhi += (ii_ziku[i][j] - ii[j]) * (ii_ziku[i][j] - ii[j]);
      }
      if (chazhi < minchazhi)
      {
        chazhi2 = minchazhi;
        minpos2 = minpos;
        minchazhi = chazhi;
        minpos = i;
      }
    }
    System.out.println("正选pos" + minpos + "-----------------------------------备选：" + hanzi[minpos2] + chazhi2 + ":::" + minchazhi);
    
    return hanzi[minpos];
  }
  
  public static void fxziku()
  {
    HashMap map = new HashMap();
    for (int i = 0; i < ii_ziku.length; i++) {
      if (map.containsKey(hanzi[i])) {
        map.put(hanzi[i], "" + (Integer.parseInt((String)map.get(hanzi[i])) + 1));
      } else {
        map.put(hanzi[i], "1");
      }
    }
    System.out.println("共计汉字数" + map.size());
    int count = 0;
    for (int i = 0; i < hanzi.length; i++)
    {
      if (i % 100 == 0) {
        System.out.println("---" + i + ":" + count);
      }
      for (int j = i; j < hanzi.length; j++) {
        if (hanzi[j].equals(hanzi[i]))
        {
          int chazhi = 0;
          for (int k = 0; k < 48; k++) {
            chazhi += (ii_ziku[i][k] - ii_ziku[j][k]) * (ii_ziku[i][k] - ii_ziku[j][k]);
          }
          if (chazhi <= 4) {
            count++;
          }
        }
      }
    }
    System.out.println("相同字数" + count);
  }
  
  public static void main(String[] args)
  {
    String s = FileUtil.readFile("ziku.txt");
    StringTokenizer st = new StringTokenizer(s, "\r\n");
    HashMap map = new HashMap();
    System.out.println(st.countTokens());
    int count = 1;
    while (st.hasMoreElements())
    {
      map.put(st.nextToken(), "1");
      if (count % 1000 == 0) {
        System.out.println(count++);
      }
    }
    System.out.println(map.size());
    Object[] ss = map.keySet().toArray();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < ss.length; i++)
    {
      sb.append(ss[i] + "\r\n");
      if (i % 1000 == 0)
      {
        System.out.println("..." + i);
        FileUtil.writeFile("zikuaa.txt", sb.toString(), true);
        sb = new StringBuffer();
      }
    }
    System.out.println("aaa");
  }
}


/* Location:           C:\Users\qianshaohua\Desktop\answer.jar
 * Qualified Name:     answer.Answer
 * JD-Core Version:    0.7.0.1
 */