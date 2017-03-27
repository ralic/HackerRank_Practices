package org.vishia.exampleJava2C.java4c;

/**Commonly Interface to all classes for a automation control system.
 * 
 * @author Hartmut Schorrig
 *
 */
public interface CtrlBase
{
  /**Initialize the function. */
  void init();
  
  /**Reset to start values. */
  void reset();
  
  /**It is a tick in the sampling time. 
   * @param time the continues time for storing values, calculate differences etc.
   */
  void step(int time);
  
  /**calculates the parameters.*/
  void parametrize();
}
