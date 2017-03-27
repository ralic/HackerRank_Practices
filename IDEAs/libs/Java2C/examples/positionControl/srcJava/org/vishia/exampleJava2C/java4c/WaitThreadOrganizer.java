package org.vishia.exampleJava2C.java4c;


/**This interface contains only a method which should called in a run-loop
 * to wait for the cycle. In a simulation some additional things can be do there.
 * @author Hartmut Schorrig
 *
 */
public interface WaitThreadOrganizer 
{
  void waitCycle();
}
