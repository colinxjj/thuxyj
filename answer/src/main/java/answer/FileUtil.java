package answer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class FileUtil
{
  public static String readFile(String fileName)
  {
	StringBuffer content = new StringBuffer();
    
    BufferedReader filedata = null;
    try
    {
      filedata = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
      
      String fileline = filedata.readLine();
      content.append(fileline);
      while ((fileline = filedata.readLine()) != null)
      {
        content.append("\r\n");
        content.append(fileline);
      }
      return content.toString();
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
    
    return null;
  }
  
  public static boolean writeFile(String fileName, String content, boolean append)
  {
    boolean result = true;
    
    File tempFile = new File(fileName);
    
    File file = new File(tempFile.getParent(), tempFile.getName());
    
    BufferedOutputStream outfile = null;
    try
    {
      if (!file.exists()) {
        file.createNewFile();
      }
      outfile = new BufferedOutputStream(new FileOutputStream(fileName, append));
      outfile.write(content.getBytes());
      outfile.flush();
      

















    }
    catch (IOException ex1)
    {
      System.out.println("写文件:" + fileName + "时,出现输入输出异常!");
      ex1.printStackTrace();
      result = false;
    }
    finally
    {
      if (outfile != null) {
        try
        {
          outfile.close();
        }
        catch (IOException ex3)
        {
          System.out.println("关闭文件:" + fileName + "时,出现输入输出异常!");
          ex3.printStackTrace();
        }
      }
    }
    
    return result;
  }
  
  public static void main(String[] args)
  {
    String[] s = { "1", "2" };
    System.out.println(s.toString());
  }
}


/* Location:           C:\Users\qianshaohua\Desktop\answer.jar
 * Qualified Name:     answer.FileUtil
 * JD-Core Version:    0.7.0.1
 */