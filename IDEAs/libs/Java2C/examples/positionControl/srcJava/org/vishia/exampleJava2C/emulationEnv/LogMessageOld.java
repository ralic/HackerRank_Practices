package org.vishia.exampleJava2C.emulationEnv;

/**A message interface for state changes and important values from the controlling process.
 * 
 * @author JcHartmut
 *
 */
 interface LogMessageOld
{
  void xxxsendMsg(int identNumber, String text);
  void xxxsendMsg_4i(int identNumber, String text, int val1, int val2, int val3, int val4);
  
}
