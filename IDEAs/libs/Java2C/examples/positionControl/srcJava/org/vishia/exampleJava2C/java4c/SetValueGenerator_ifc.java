package org.vishia.exampleJava2C.java4c;

public interface SetValueGenerator_ifc {

	/**stores a next target point. This method may be called in a slower thread.
   * If there is no space for storing the demand, the value isn't be stored.
   * The method should be called later once more.
   * 
   * @param targetValue the target point value.
   * @return true if it is stored, false if there is no space to storing. 
   */ 
  boolean setTarget(short targetValue);
  
  /**generates the next reference value. 
   * This method is to be called in the cycle of controlling.
   * @return the next set value for controller
   */
  short step();
  
  
	
}
