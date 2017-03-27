package org.vishia.exampleJava2C.simPc;

import java.io.IOException;

import org.vishia.bridgeC.AllocInBlock;
import org.vishia.exampleJava2C.java4c.MainController;
import org.vishia.exampleJava2C.java4c.OamVariables;
import org.vishia.exampleJava2C.java4c.WaitThreadOrganizer;
import org.vishia.exampleJava2C.java4c.WayActuator;
import org.vishia.exampleJava2C.java4c.WaySensor;

/**
 * @author Hartmut
 *
 */
public class SimPc
{
	/**Reference to the mainController, independent of singleton or not.
	 * @java2c=noGC. */
	final MainController mainController;
	
	public boolean bRun = true;
	
  /**Way in microMeter */
  public double way1, way2;
  
  public double disturbance1 = 1.0;
  
  /**Voltage as output from controller to the both movement motors. */
  public short voltage1, voltage2;
  
  /**The continuously time. */
  public int time =0;
  
  /**The sampling interval in microsecond. */
  public int samplingInverval = 100;

  //private final WriteActValues oamWriter = new WriteActValues();
  
  private final SendActValues oamWriter = new SendActValues();
  
  public SimPc()
  { /**instantiation: */
    mainController = new MainController(broker2, null, waitThreadOrganizer);
    MainController.singleton = mainController;
    
  }

  /**Internal agent class to implement the waitCycle()-functionality.
   */
  class WaitThread implements WaitThreadOrganizer
  {
		@Override
		public void waitCycle() 
		{
	    step();  //the emulation of environment in main control thread.
	    //try{ writeValues.write(); }
	    //catch(IOException exc){ }
			/**Wait until cycle, it is a wait/notify from interrupt
			 * in an original hardware, but a timing wait in the simulation environment.
			 */
			try{ Thread.sleep(20); }
			catch(InterruptedException exc){ }
		 	
		}
  }
  
  
  
  
  /**Instance to implementing the agent class. */
  WaitThread waitThreadOrganizer = new WaitThread();

  /**This is the run method of the execution. */
  public final void execute()
  {
    
    way1 = 1000000;  //start at 1 m
    way2 = 1000000;
    
    
    int idxTarget = 0;
    /**Simple example: Some values are given fixed. */
    short[] targets = { 15000, 17000, 11000, 15000, 17000, 11000, 15000, 17000, 12000};
    
    oamWriter.start();  //opens the socket.
    
    /**Prepare the building of the controller. */
    MainController.singleton.prepare();
    
    /**Starts the controller thread. */
    MainController.singleton.start();
    
    int ii = 0; 
    /**This thread is an example for a superior thread over the controller,
     * which sets some setting values. It may be also read from an ethernet communication port
     * (socket) or adequate. In this example a fix array of target positions 
     * for the way controller is tried to set. The way controller accepts only
     * the next and the next-next target position. This thread polls whether a next position
     * is able to set. 
     */
    do
    { ii++;
      try {Thread.sleep(200);} catch (InterruptedException exc) {}
      //it may be also in another thread: set target values:
      if(idxTarget < targets.length)
      { /**try to set a new target position. It is able to set if there are space for it. 
         * It depends from the state of the way control. */
      	if(MainController.singleton.setTarget(targets[idxTarget]))
        { /**increment it, only if it was successful. */
          idxTarget +=1;
          if(idxTarget >= targets.length){
          	idxTarget = 0;
          }
        }
      } 
      /**Call of the garbage collector in the loop of this thread, 
       * It is sufficient time to do that here,
       * All blocks are tested, alternative: 
       * One call deals with 1 block-cluster only, to limit time. */
      { int catastropicalCount = 10000; //limit it.
        int success;
        do
        { /**One call handles only 1 block with its cluster. */
        	success = AllocInBlock.garbageCollection_BlockHeapJc(false);
        } while(success != AllocInBlock.checkGcFinished && --catastropicalCount >=0 );
      }  
    } while(bRun);
    System.out.println("finish");
  }
  

	

	
  /**The step routine to build the simulation environment values.
   * This routine will be called if the control-step-thread waits for the cycle notifying.
   * see {@link WaitThread#waitCycle()}.
   */
  final void step()
  { time +=1;
    if(time == 551)
      stop();
    /* the amplification of the actuator-motor is disturbed, it is between 0.4 and 1.4
     * it causes an exception 
     * because the software MainController.java has a difficult error to demonstrate this:
     * If the output from PID-Controller is greater than 20000, it is limited,
     * because the actuator has a range from -10000 to 10000. But if the limitation occurs,
     * the integral part of PID-ctrl should be fixed. This isn't implemented. So the integral
     * has an overflow and an exception is thrown. 
     * If the amplification is more less, the control deviation is reached a programmed limit,
     * so the PID-controller is switched off and no exception occurs.
     * If the amplification is greater, the actuator value is no limited and no exception occurs.
     * It is only a small range.
     */    
    disturbance1 = 0.9 + 0.5*Math.sin( ((double)(time % 1000))/1000 * 2* 3.1456); //todo Math.PI);
    //10 V moves 100 mm per second.
    way1 += 100.0 * ((double)(voltage1))/samplingInverval * disturbance1;
    oamWriter.write(mainController.oamVariables); //, mainController.d);
  }

  
  /**An instance of interface WaySensor implemented as anonymous class definition
   * to access the value way1 from MainEmulation.
   */
  final WaySensor waySensor1 = new WaySensor()
  { public int getWay()
    { return (int)(way1);
    }
  };
  
  
  /**A second instance of interface WaySensor implemented as anonymous class definition
   * to access the value way2 from MainEmulation.
   */
  final WaySensor waySensor2 =  new WaySensor()
  {
    public int getWay()
    { return (int)(way2);
      
    }
  };
  
  
  /**An instance of interface WayActuator implemented as anonymous class definition
   * to affect the value way1 from MainEmulation.
   */
  final WayActuator wayActuator1 = new WayActuator()
  {
    public void setMotorVoltage(short voltage)
    { voltage1 = voltage; 
    }
  };
  
  
  
  /**An instance of interface WayActuator implemented as anonymous class definition
   * to affect the value way1 from MainEmulation.
   */
  final WayActuator wayActuator2 = new WayActuator()
  {
    public void setMotorVoltage(short voltage)
    { voltage2 = voltage; 
    }
  };
  
  

  
   
  /**The anonymous class for broker implements the methods 
   * to get the different working interface-references. 
   * This interface-implementing instances are implemented as anonymous inner classes
   * of the outer class {@link SimPc} (agent classes), which access the data of SimPc.
   */
  final iRequireMainController broker2 =  new iRequireMainController()
  {
    public WayActuator requireWay1Actuator()
    { /**@java2c=dynamic-call. */
    	WayActuator ret = wayActuator1;
    	return ret;
    }

    public WaySensor requireWay1Sensor()
    { /**@java2c=dynamic-call. */
    	WaySensor ret = waySensor1;
    	return ret;
    }

    public WayActuator requireWay2Actuator()
    { /**@java2c=dynamic-call. */
    	WayActuator ret = wayActuator2;
    	return ret;
    }

    public WaySensor requireWay2Sensor()
    { /**@java2c=dynamic-call. */
    	WaySensor ret = waySensor2;
    	return ret;
    }
 };
  

  private void stop()
  { //debug
  	
  }
	
	
}
