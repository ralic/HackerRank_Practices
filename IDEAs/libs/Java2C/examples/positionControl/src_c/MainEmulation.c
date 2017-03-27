#include "MainEmulation.h"
#include "Jc/ObjectJc.h"
#include "BlockHeap/BlockHeapJc.h"

#include "PosCtrl/MainController.h"
#include "simPc/SimPc.h"
#include "J1c/MsgDispatcher_MSG.h"
#include "simPc/iRequireMainController.h"
#include <stdio.h>
#include <string.h>
//#include "testSomeDynamicLinkedMethodConcepts.h"
#include "Fwc/fw_LogMessage.h"
#include "Fwc/fw_Exception.h"

#include "Main.h"
#include <stdio.h>
#include <stdlib.h>
#include <OSAL/inc/os_thread.h>

//not available up to now: #include "Inspc/ModulAcq_Inspc.h"
//not available up to now: #include "Inspc/FactoryDevice_Inspc.h"

#include "Jc/ThreadJc.h"

/*@DATA @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/

/**static data of BlockHeap concept. */


/**The data for the controller. This class is translated Java2C. 
 * They are staticly instantiated, at example. They have to be initialized with the staticly known data of ObjectJc, the rest with 0.
 */ 
// MainController_s mainController;

/**The data for the simulation environment in C. 
 * They are staticly instantiated, at example. They have to be initialized with 0.
 */ 
MainEmulation_s mainEmulation;

/**The data for the PC-simulation environment (Java2c-translated). 
 * They are staticly instantiated, at example. They have to be initialized with 0.
 */ 
SimPc_s simPc;


/*@PROTOTYPES @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/

/**Subroutine for setting new targets to the controller. */
void setTargets_Main();

/**Subroutine to write the state to the logMessage-system. */
void logMsg_Main(int nStep);

/*@ROUTINES @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/



/*not available up to now: 
void initInspector(ObjectJc* appl)
{ 
  struct ModulAcq_Inspc_t* inspector;
  inspector = create_ii_Device_modulifcInspc_FactoryDevice_Inspc(appl, 0, 60078);
  completeConstruction_ModulAcq_Inspc(inspector);
  startSystem_ModulAcq_Inspc(inspector);
}
*/







/**In this phase of example the main routine simulates the time-behaviour of a real controlling system and the environment.
 * It is all done in one thread.
 * In main a while-loop is executed. Each pass of the while-loop is adequate to one sampling time of the controller.  
 */
int main()
{ os_initLib();
  { STACKTRC_ENTRY("main");
    /* Initializing the block heap for garbage collection: */
    initBlockHeap();
    setCurrent_BlockHeapJc(pBlockHeapModul1, _thCxt);
  

    /*test something at first ... not necessary apart from the example*/
    //testWaitNotify();
    //testSomeDynamicLinkedMethodConcepts();
  
    { int nStep = 0;
    
      /* Call the constructor to init the instances. 
         The ObjectJc-base of the instances have to be (should) initialized before, the rest should set to 0 before.
         Use the Object based ctorO_.... It is the common concept of BlockHeap: 
         An instances is given first as ObjectJc-Reference, 
         possible with a base initialiazation of the ObjectJc-part with known length information 
         in the ObjectJc.objectIdentSize field, but also possible with 0-initialization. 
       */
      //init_ObjectJc(&mainController.base.object, sizeof(mainController), 0);
      init_ObjectJc(&simPc.base.object, sizeof(SimPc_s), 0);
      test_StacktraceJc(&stacktrace);
      
      //ctorO_MainController(&mainController.base.object, null, null, null, null, _thCxt);
      ctorO_SimPc(&simPc.base.object, _thCxt);
      /* The initialization of non-ObjectJc-based instances should be done with the ctorM-concept. */
      ctorM_MainEmulation(build_MemC(&mainEmulation, sizeof(mainEmulation)));       
      //
      test_StacktraceJc(&stacktrace);

      //not available up to now: initInspector(&mainController.base.object);

      //start of the run mode:
      setRunModeBlockHeap(REFJc(simPc.mainController->msg));

      execute_SimPc(&simPc, _thCxt);
      

      TRY
      { int countGcTime = 1;
        int countMsgDispTime = 1;
        //prepare_MainController(&mainController, _thCxt);
        while(true)
        { test_StacktraceJc(&stacktrace);
          if(nStep == 210)
          { /* changing the amplification of actuator causes an exception,
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
            mainEmulation.nAmplificationActuator = 0.45F;
          }
          //step_MainController(&mainController, _thCxt); //step sample times of the controller.
          //step_MainEmulation(&mainEmulation);                   //step sample times of the enviroment simulation.         
          setTargets_Main();                                    //step thread to set target points.
          nStep +=1;

          logMsg_Main(nStep);

          if(nStep == 3000) break;
          if(--countGcTime <= 0)
          { countGcTime =100;
            garbageCollection();
          }
          if(--countMsgDispTime <=0)
          { countMsgDispTime = 100;         
            dispatchQueuedMsg_MsgDispatcher_MSG(&simPc.mainController->msgDispatcher, _thCxt);
          }

        }
      }_TRY
      CATCH(RuntimeException, exc)
      { printStackTraceFile_ExceptionJc(exc, stdout);
      }
      END_TRY
    }
    STACKTRC_LEAVE;
    while(true)
    { sleep_ThreadJc(100, _thCxt);
    }
  }
  return 0;
}



/**Subroutine for setting new targets to the controller. */
void setTargets_Main()
{ static short targets[] = { 15000, 17000, 11000, 15000, 17000, 11000, 15000, 17000, 12000 };
  static int targets_length = sizeof(targets) / sizeof(targets[0]);
  static int idxTarget = 0;
  STACKTRC_ENTRY("setTargets_Main");
  //it may be also in another thread: set target values:
  if(idxTarget < targets_length)
  { if(setTarget_MainController(simPc.mainController, targets[idxTarget], _thCxt))
    { idxTarget +=1; //increment it only if it was successfull.
    }
  }
  STACKTRC_LEAVE;
}



void logMsg_Main(int nStep)
{ char buffer[200];
  STACKTRC_ENTRY("logMsg_Main");
  sprintf(buffer, "%5.5i x=%1.4f w=%1.4f m, dw=%2.3f mm, intg=0x%8.8X = %i"
         , nStep
         , mainEmulation.hardware.way1/1000000.0F
         , simPc.mainController->wWay/10000.0F
         , simPc.mainController->dWay/1000.0F
         , simPc.mainController->pidCtrl1.intgVal
         , simPc.mainController->pidCtrl1.intgVal
         );
  //sendMsg_s0_LogMessageJc(null, 1, buffer, _thCxt);
  STACKTRC_LEAVE;
}





MainEmulation_s* ctorM_MainEmulation(MemC mem)
{ MainEmulation_s* ythis = PTR_MemC(mem, MainEmulation_s);
  STACKTRC_ENTRY("ctor_MainEmulation");
  if(size_MemC(mem) < sizeof(MainEmulation_s)) THROW_s0(RuntimeException,"fault size", size_MemC(mem));
  init0_MemC(mem);  
  /*For emulation, set the addresses of the hwPorts to the existing emulation variables.*/
  //ythis->waySensor1.hwPort = &ythis->hardware.way1;  
  //ythis->waySensor2.hwPort = &ythis->hardware.way2;  
  //ythis->wayActuator1.hwPort = &ythis->hardware.motor1;  
  //ythis->wayActuator2.hwPort = &ythis->hardware.motor2;  
  //ythis->hardware.way1 = 1000000L;
  //ythis->hardware.way2 = 2000000L;
  ythis->nAmplificationActuator = 1.7F;
  STACKTRC_LEAVE;
  return ythis;
}



void step_MainEmulation(MainEmulation_s* ythis)
{
  ythis->hardware.way1 += (int32)(ythis->nAmplificationActuator* ythis->hardware.motor1);  //integrate the way because voltage!
  ythis->hardware.way2 += 2* ythis->hardware.motor2;  //integrate the way because voltage!
}

