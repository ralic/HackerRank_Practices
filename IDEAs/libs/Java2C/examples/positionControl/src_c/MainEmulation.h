#ifndef __MainEmulation_h__
#define __MainEmulation_h__

#include "Jc/ObjectJc.h"
//#include "WaySensor.h"
//#include "WayActuator.h"


/**The emulated way values because in the example there are no real hardware.
 * Also if real hardware is exists, a simple simulation in C at PC in such way is recommended.
 * The way values are affected by output on actuator. See emulation implementation.
 */
typedef struct Hardware_t 
{ int32 way1, way2;
  int16 motor1, motor2;
}Hardware;



typedef struct MainEmulation_t
{ 
  ObjectJc object;

  /**The hardware access values from the real target implementation, */
  //WaySensor waySensor1, waySensor2;
  /**The hardware access values from the real target implementation, */
  //WayActuator wayActuator1, wayActuator2;
  
  /**The emulation of hardware access. */
  Hardware hardware;

  /**The actuator amplification. */
  float nAmplificationActuator;

} MainEmulation_s;


METHOD_C MainEmulation_s* ctorM_MainEmulation(MemC mem);


/**The routine to simulate the hardware behaviour. */
METHOD_C void step_MainEmulation(MainEmulation_s* ythis);


#endif //__MainEmulation_h__