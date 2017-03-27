package org.vishia.exampleJava2C.java4c;

/**This interface is the access to Hardware. 
 * In the target platform it is a routine directly programmed in C.
 * The interface reference helps to select the instance of Actuator. 
 * In C it may not be implemented with a java-like interface concept, but more simple.
 */  
public interface WaySensor
{

  /**gets a actual way
   * @return way in Micrometer from -2.000 to 2.000 meter or adequate unit. 
   *         The high solution of 32 bit is necessary because small way variations should be detected.
   *         The high solution may be implemented in hardware by a counter mechanism. 
   *            
   */
  public int getWay();
}
