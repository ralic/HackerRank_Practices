package org.vishia.Inspc;

import java.lang.reflect.Field;

public interface GetInspcData
{
  float getFloatInspcData(int addr);

  int getIntInspcData(int addr);

  
  /**Gets an internal number with given field. In C it is the memory address immediately.
   * In Java it is a number which is used to access the data of the given instance
   * with the {@link #getFloatInspcData(int)} etc. 
   * @param element
   * @return Java: The access number. C: The memory address of the element 
   */
  int getAddr(Field element);

}
