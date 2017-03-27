package org.vishia.exampleJava2C.emulationEnv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


 class ProcessMessageImpl implements LogMessageOld
{

  
  final Writer fileOut;
      

  public ProcessMessageImpl()
  {
    Writer fileOut;
    try
    {
      fileOut = new FileWriter(new File("out/out.txt"));
    } 
    catch (IOException e)
    { fileOut = null;
      e.printStackTrace();
    }
    this.fileOut = fileOut;  //NOTE: set the final field after catch, it may be null.
  }


  public void xxxsendMsg(int identNumber, String text)
  {
    try
    {
      fileOut.write("" + identNumber + ": " + text);
      fileOut.write("\n");
      fileOut.flush();
    } catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    
  }


  public void xxxsendMsg_4i(int identNumber, String text, int val1, int val2, int val3, int val4)
  {
    try
    {
      fileOut.write("\n" + identNumber + ": " + text + "=" + val1 + ", " + val2 + ", " + val3 + ", " + val4);
      fileOut.flush();
    } catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }

}
