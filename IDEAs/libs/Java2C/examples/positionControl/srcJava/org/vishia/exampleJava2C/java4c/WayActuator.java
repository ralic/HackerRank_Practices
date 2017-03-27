package org.vishia.exampleJava2C.java4c;

/**This interface is the access to Hardware. 
 * In the target platform it is a routine directly programmed in C.
 * The interface reference helps to select the instance of Actuator. 
 * In C it may not be implemented with a java-like interface concept, but more simple.
 */  
public interface WayActuator
{
  /**Sets the output to the motor. The reference used with this call designates the hardware instance.
   * 
   * @param value Voltage in range -32000...32000. It may be Millivolt 
   *        or a directly value for an digital-analog-converter.
   */
  public void setMotorVoltage(short value);

}
