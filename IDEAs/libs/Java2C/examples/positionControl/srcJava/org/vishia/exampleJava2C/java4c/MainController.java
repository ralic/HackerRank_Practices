package org.vishia.exampleJava2C.java4c;

import org.vishia.exampleJava2C.emulationEnv.MainEmulation;
import org.vishia.exampleJava2C.simPc.iRequireMainController;
import org.vishia.msgDispatch.LogMessage;
import org.vishia.msgDispatch.LogMessageFile;
import org.vishia.msgDispatch.MsgDispatcher;

/**This class is the example main class to control a movement.
 * 
 * @author JcHartmut
 * @java2c=staticInstance. CC:Instances of this class will be created 
 *                     only in the construction path of the system.
 */ 
public class MainController
{
  /**Aggregation The message interface. It is public to enable static access via MainController.singleton. */
  public final LogMessage msg;
  
  /**Reference to the implementing class of the interface method waitCycle().
   * For PC-simulation this method should done some additional things.
   * In real target hardware it should wait in thread for notifying from interrupt or data receive.
   */
  private final WaitThreadOrganizer waitCycleOrganizer;
  
  /**Composition The message dispatcher. 
   * It is not embedded in C because the amount of memory depends from arguments (Queue size etc.
   */
  public final MsgDispatcher msgDispatcher = new MsgDispatcher(/*maxNrofDispatchEntries*/ 100, /*sizeQueue*/ 200, 28, 28);

  /**The instance for dispatching messages.
   * @java2c=noGC. */
  private final MsgDispatcher.DispatcherThread msgDispatcherThread;
  
  /**Embedded Composition a log message output to file. */
  public final LogMessageFile logMsgFileCtrlValues = new LogMessageFile("testLog/ctrl$MMdd_HHmmss$.log", 10, 1, null, null, msgDispatcher.getSharedFreeEntries());
  
  /**Index to identify the LogMsg output to file.*/
  public final static int kMsgOutFile = 4, kMsgOutFileGC = 6; 
  
  public final LogMessageFile logMsgFileGC = new LogMessageFile("testLog/GC.log", 0, 0, null, null, msgDispatcher.getSharedFreeEntries());
  
  /**Aggregation to a way sensor for input for control. */
  private final WaySensor way1Sensor, way2Sensor;
  
  /**Aggregation to a way actuator, servomotor or such, output for control. */
  private final WayActuator way1Actuator, way2Actuator;
  
  /** The set value for the way in 0.1mm-units from above -3 m to 3 m. */
  private short wWay = 10000;  //1 m as initial.
  
  /**controller offset (deviation). Only a deviation in range of -32..32 mm is regarded.*/
  private short dWay;
  
  private short[] dWay1 = new short[1];
  
  /**Simple set value generator as first example: add 0.5 mm per step in cycle time. */
  private short addWay = 5;
  
  /**The instance of set-value generator. 
   * The instance is referenced with an interface for a good software design,
   * but the usage in C is direct to @java2c=instanceType:"SetValueGenerator".
   * Second approach: @java2c=simpleRef. It should be a simple reference.*/
  private final SetValueGenerator_ifc mySetValueGenerator;
  
  private SetValueGenerator.Target.OnlyTest.InnerTest testEnvJ2c;
  
  /**The actuating variable for the movement. It is a voltage for a motor in range -10000 to 10000, unit milliVolt. */
  private short yOut;

  private short[] yOut2;
  
  /**The PID controller. 
   * CC: In C it is an embedded struct because the reference is final (given from construction, fix)
   *     and created here.
   */
  private final PID_controller pidCtrl1 = new PID_controller();
  
  /**The PID controller as universal control module. */
  private final CtrlBase pidCtrl2 = pidCtrl1;
  
  /**This class helps to read target positions and its velocity from a text file. */
  private final ReadTargetFromText targetReader = new ReadTargetFromText();
  
  /**A interface reference to test.
   * CC: This reference uses a reference struct with 2 values, the reference to the Object_Jc
   *     and a value which contains the index of jump table of the instance for interface method call.
   */
  private Testifc testifc;
  
  /**A reference to test the garbage collection.
   * CC: This reference uses a reference struct with 2 values, the reference to the instance
   *     and a value which contains beside the index of jump table of the instance for dynamic method call
   *     the index of back reference from the instance block in BlockHeap.
   */
  private TestClass testDynamicReference;
  
  
  
  /**Instance for the Oam-variables. */
  public final OamVariables oamVariables = new OamVariables();
  

  /**The MainController is a singelton, the constructor is private. @java2c=noGC.*/
  public static MainController singleton;
  
  
  /**The constructor of this Controller assumes, that the depended instances all built always. 
   * @param broker Interface contains all require methods to depended instances.
   */
  public MainController(iRequireMainController broker, LogMessage msg1
  		, WaitThreadOrganizer waitCycleOrganizer
  		)
  { singleton = this;
     msgDispatcherThread = msgDispatcher.new DispatcherThread(100);
  	this.waitCycleOrganizer = waitCycleOrganizer;
  	//int maxNrofDispatchEntries = 100; //max number of different ranges in identifier numbers of messages.
    //int sizeQueue = 200; //max nrof messages where are queued temporary to process in the dispatcher thread.
    //some tests, debug it.
    msgDispatcher.setOutputRoutine(kMsgOutFile, "FileCtrl", true, logMsgFileCtrlValues);
    msgDispatcher.setOutputRoutine(kMsgOutFileGC, "FileGC", false, logMsgFileGC);
    msgDispatcher.setOutputRange(0, 1000, 1<<kMsgOutFile, MsgDispatcher.mAdd, 4);
    //this.msg = LogMessage.convertFromMsgDispatcher(msgDispatcher);
    this.msg = msgDispatcher;
    /**Example of a input for dispatching control. */
    String msgCtrl = "1..999: 0x1; 5: 0x1; 0..1002:FileGC + FileCtrl; 2000..2010: FileGC; 2004: 0; 20..50:+CON;";
    /**If an error occurs, the buffer contains the error message plus input at fault position.
     * Here no allocated String should be used, because this routine should used in a system
     * without garbage-collection. 
     * @java2c=stackInstance, fixStringBuffer.
     * The StringBuffer is created in C in the stack!
     */
    final StringBuffer errorBuffer = new StringBuffer(100);
    String sError = msgDispatcher.setOutputFromString(msgCtrl, errorBuffer);
    if(sError != null)
    { msg.sendMsg(1, "Error parsing msg dispatcher control string: %s.", errorBuffer.toString());
    }
    /**Sends test messages, to test the message dispatcher.*/
    msg.sendMsg(1, "Test to console %s", "test-string");
    msg.sendMsg(1000, "Test to dispatched files. numbers= %d, %d", 1,2);
    
    //this.msg = msg;
    mySetValueGenerator = new SetValueGenerator(msg, oamVariables);
    way1Sensor = broker.requireWay1Sensor();
    way2Sensor = broker.requireWay2Sensor();
    way1Actuator = broker.requireWay1Actuator();
    way2Actuator = broker.requireWay2Actuator();

    
  }
  
 
  /**Gets the set value in units mm*/
  final public short getWaySetvalue(){ return wWay; }
  
  /**Gets the offset of the controller on input*/
  final public int getWayOffset(){ return dWay; }
  
  
  
  /**prepares the working of the controller, called on startup from outside.
   * 
   */
  final public void prepare()
  { wireFunction();
    targetReader.test();
  }
  
  
  final public void start()
  {
    wayCtrlThread.start();
  }
  
  final public void wireFunction()
  { pidCtrl1.connectInput(dWay1);
    yOut2 = pidCtrl1.provideOutput();
  }
  
  /**This is the cyclic called method to execute the controlling. It is called from outside. */
  final public void step()
  {
  }

  /**Get the internal value of the integrator of the first way controller. */
  public final float getWay11Intg(){ return pidCtrl1.getIntg(); }

  /**stores a next target point. This method may be called in a slower thread.
   * If there is no space for storing the demand, the value isn't be stored.
   * The method should be called later once more.
   * 
   * @param targetValue the target point value.
   * @return true if it is stored, false if there is no space to storing. 
   */ 
  public final boolean setTarget(short targetValue)
  { //it is a delagation pattern. The outside user don't know the SetValueGenerator.
    boolean isSet =  mySetValueGenerator.setTarget(targetValue);
    return isSet;
  }
  

  private class WayCtrlThread extends Thread
  {
  	public WayCtrlThread(String sName) {
			super(sName);
		}

		/**It is the main loop of the fast controller thread.
  	 * implements {@link java.lang.Thread#run()}
  	 */
  	@Override public final void run()
  	{
  		/**Internal variable to hold the reference to the interface,
  		 * @java2c=dynamic-call. In C the reference to the method-table will be gotten one time here in startup-phase,
  		 * to relieve the time-critical calc-time in the loop. */
  		final WaitThreadOrganizer waitCycleOrganizer1 = waitCycleOrganizer;
  		/**same, dynamic call: @java2c=dynamic-call. */
  		final WaySensor way1Sensor = MainController.this.way1Sensor, way2Sensor= MainController.this.way2Sensor;
  		/**same, dynamic call: @java2c=dynamic-call. */
  		final SetValueGenerator_ifc mySetValueGenerator = MainController.this.mySetValueGenerator;
  		/**same, dynamic call: @java2c=dynamic-call. */
  		final WayActuator way1Actuator = MainController.this.way1Actuator, way2Actuator = MainController.this.way2Actuator;
  		/**unterminated loop as long as the controller runs. */
  		while(true){
  			/**Wait until cycle, it is a wait/notify from interrupt
  			 */
  			waitCycleOrganizer1.waitCycle();
  			/** Runs the cycle: */
  			//step();
  	    /**reads the inputs from Hardware, it is in units 1 micrometer. */
  	    int xWay = way1Sensor.getWay();
  	    oamVariables.way = xWay;
  	    
  	    /*first example: way moves up and down between 1.000 m and 2.000 m in 5 mm per step (1m/s if sample time = 5 millisec)*/
  	    boolean bFirstExample = false;
  	    if(bFirstExample)
  	    { if(wWay >= 20000)
  	      { /*The way is greater the maximum. */
  	        addWay = -50; 
  	        pidCtrl1.setIntg((short)-yOut);
  	  
  	      }
  	      else if(wWay <= 10000)
  	      { addWay = 50; 
  	        pidCtrl1.setIntg((short)-yOut);
  	      }
  	      wWay += addWay; 
  	      if(wWay >=20000) wWay =1;  //test Java2C only!
  	    }
  	    else
  	    { /* The next set-value for the way is got from the valueGenerator. 
  	       * It interpolates over given fix points. The fix points are given
  	       * in another thread, see there.
  	      */
  	      wWay = mySetValueGenerator.step();
  	    }
  	    /**wWay in microMeter, the wWay is in 100um-step */
  	    int wWayu = 100 * (int)wWay;
  	    /* If the difference is outside a capturing range, the controller is off 
  	     * and the output value is set immediately. 
  	     */
  	    if(xWay > wWayu + 30000)
  	    { /*The way is out of range, move down.*/
  	      yOut = -20000;
  	      dWay = 30000;  //for OaM
  	      pidCtrl1.setIntg(yOut);
  	    }
  	    else if(xWay < wWayu - 30000)
  	    { /*The way is out of range, move up. */
  	      yOut = 20000;
  	      dWay = -30000;  //for OaM
  	      pidCtrl1.setIntg(yOut);
  	    }
  	    else
  	    { /**difference of way in units 1 Micrometer, range should be -32..32 mm. */
          dWay = (short)(wWayu - xWay);
          /*call of PID-control. */ 
  	    	yOut = pidCtrl1.calculate(dWay);
  	      //dWay1[0] = (short)(-dWay);  //Input for PID
  	      //yOut = pidCtrl1.step((short)(-dWay));
  	      //yOut = yOut2[0];
  	      /*The yOut have an output range from -16768 to 16767, limit the output value: */ 
  	      if(yOut > 19999)
  	      { yOut = 19999; 
  	        /* the next statement is commented, because it may be "forgotten",
  	         * therefore the integral value runs away and has an overflow than.
  	         * the overflow is recognized and an exception is thrown.
  	         * If the statement is not commented, the controller is stable.
  	         */
  	        //pidCtrl1.setIntg(yOut);
  	      }
  	      else if(yOut < -19999)
  	      { yOut = -19999; 
  	        //next statement commented, see above.
  	        //pidCtrl1.setIntg(yOut);
  	      }
  	    }  
  	    
  	    way1Actuator.setMotorVoltage((short)(yOut>>1));
  	    oamVariables.dway = dWay;
  	    oamVariables.output = yOut;
  	  }
  	}
  	
  }
  
  /**@java2c=noGC. */
  final WayCtrlThread wayCtrlThread = this.new WayCtrlThread("wayCtrl-1");
  
}

