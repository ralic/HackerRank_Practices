package org.vishia.exampleJava2C.simPc;

import org.vishia.exampleJava2C.java4c.WayActuator;
import org.vishia.exampleJava2C.java4c.WaySensor;



/**This interface contains all requirenesses of the MainController
 * from the external environment. It should be implemented in the constructing class.
 * @author JcHartmut
 *
 */
public interface iRequireMainController
{ WaySensor requireWay1Sensor();
  WaySensor requireWay2Sensor();
  WayActuator requireWay1Actuator();
  WayActuator requireWay2Actuator();
}
