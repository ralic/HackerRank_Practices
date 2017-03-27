package org.vishia.exampleJava2C.emulationEnv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.vishia.byteData.ByteDataAccess;
import org.vishia.communication.InterProcessCommFactorySocket;
import org.vishia.communication.InterProcessComm_SocketImpl;
import org.vishia.exampleJava2C.java4c.MainController;
import org.vishia.exampleJava2C.java4c.ProcessMessageIdents;
import org.vishia.exampleJava2C.java4c.WaitThreadOrganizer;
import org.vishia.exampleJava2C.simPc.SimPc;
import org.vishia.exampleJava2C.simPc.iRequireMainController;
import org.vishia.mainCmd.Report;
import org.vishia.msgDispatch.MsgDispatcher;
import org.vishia.util.StringFormatter;


public class MainEmulation
{
  //final MainController theController;
  
  //final LogMessage msg;
  
  final MsgDispatcher msgDispatcher;

  final SimPc simPc;
  
  WaitThreadOrganizer waitCycleOrganizer = new WaitThreadOrganizer() 
  {
		
		@Override
		public void waitCycle()
    {
    	
    }
 };
  
  private MainEmulation() 
  throws IOException
  { writeValues = new WriteValues();
    //msg = new LogMessageConsole(); //ProcessMessageImpl();
    /*
    LogMessage logMsgConsole = LogMessageConsole.create();
    msgDispatcher = new MsgDispatcher(100, 1000);
    msgDispatcher.setOutputRoutine(0, false, logMsgConsole);    
    msgDispatcher.setOutputRoutine(1, true, logMsgConsole);    
    msgDispatcher.setOutputRoutine(2, false, logMsgConsole);    
    msgDispatcher.setOutputRoutine(3, false, logMsgConsole);    
    msgDispatcher.setOutputRoutine(4, false, logMsgConsole);    

    msgDispatcher.setOutput(12, 15, 0x1, Report.fineDebug);
    msgDispatcher.setOutput(5, 7, 0x2, Report.fineDebug);
    msgDispatcher.setOutput(20, 22, 0x4, Report.fineDebug);
    msgDispatcher.setOutput(13, 13, 0x8, Report.fineDebug);
    msgDispatcher.setOutput(6, 20, 0x10, Report.fineDebug);
    msgDispatcher.setOutput(0, 4, 0x1, Report.fineDebug);
    msg = msgDispatcher;
    */
    //msg = null;
    msgDispatcher = null;
    simPc = new SimPc();
  }
  
  

  public final static void main(String[] args)
  { //String ipcFactory = "org.vishia.communication.InterProcessComm_Socket";
  	//try{ ClassLoader.getSystemClassLoader().loadClass(ipcFactory, true);
  	//}catch(ClassNotFoundException exc){
  	//	System.out.println("class not found: " + "org.vishia.communication.InterProcessComm_Socket");
  	//}
  	new InterProcessCommFactorySocket();
  	try
    {
  		MainEmulation main = new MainEmulation();
      main.simPc.execute();
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  
  
  
  
  /**Support write values in graphical output. */
  private class WriteValues
  {
    final StringBuffer buffer = new StringBuffer(100);
    
    /**Graphical output. */
    final OutputStream grvOut;
    
    StringFormatter out = new StringFormatter(buffer);
    
    /**Helper class to write float values into a byte[]. */
    class GrvLineAccess extends ByteDataAccess
    { /**Constructor creates a byte buffer for 16 float values. */
      GrvLineAccess(){ assignEmpty(new byte[64]);}
      
      /**abstract method of baseclass: set empty data. */
      @Override protected void specifyEmptyDefaultData(){ for(int ii=0; ii < data.length; ii++){ data[ii]= 0;} }
      
      
      @Override protected int specifyLengthElement() throws IllegalArgumentException{ return 64;}
      @Override public int specifyLengthElementHead(){ return 64;}
      public void setValue(int idx, float val){ setFloat(0, idx, 16, val);}
    };
    
    final GrvLineAccess grvLineAccess;
    
    WriteValues() throws IOException
    { grvOut = new FileOutputStream(new File("out/out.grv"));

      grvLineAccess = new GrvLineAccess();
    }
    
    public void write() 
    throws IOException
    {
      out.reset()
         .addint(simPc.time, "22221")
         .add("   w=").addint(MainController.singleton.getWaySetvalue(),"1.1111 m ")
         .add(" x").addint((int)simPc.way1,"+1.111'111 m ")
         .add("  d=").addint(MainController.singleton.getWayOffset(),"+22221.111 mm ")
         .add(" V=").addint(simPc.voltage1,"-221.111 V ")
         .add("  intg=").addint((int)(MainController.singleton.getWay11Intg()*10),"-22221.1")
         ;
      //System.out.println(out.getContent());
      MainController.singleton.msg.sendMsg(ProcessMessageIdents.kProcessValueInStep, out.getContent());
      out.reset();
      
      
      
      grvLineAccess.setValue(0,MainController.singleton.getWaySetvalue());
      grvLineAccess.setValue(1,MainController.singleton.getWay11Intg());
      grvLineAccess.setValue(2,MainController.singleton.getWayOffset());
      grvLineAccess.setValue(3,(float)(simPc.way1));
      grvLineAccess.setValue(4,simPc.voltage1);
      
      grvOut.write(grvLineAccess.getData(),0,64);
      
    }
    
  }
  
  
  WriteValues writeValues;
   
  
  void testQuadrad()
  { float x = -10.0E30F;
    float y = x*x;
    float z = y + 3.0F;
    float a = z /0.0F;
    System.out.println(y);
  }
  
  
  void stop()
  { //debug stop
  }

}




