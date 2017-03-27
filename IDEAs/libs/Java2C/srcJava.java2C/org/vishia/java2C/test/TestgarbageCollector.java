/****************************************************************************
 * Copyright/Copyleft:
 *
 * For this source the LGPL Lesser General Public License,
 * published by the Free Software Foundation is valid.
 * It means:
 * 1) You can use this source without any restriction for any desired purpose.
 * 2) You can redistribute copies of this source to everybody.
 * 3) Every user of this source, also the user of redistribute copies
 *    with or without payment, must accept this license for further using.
 * 4) But the LPGL is not appropriate for a whole software product,
 *    if this source is only a part of them. It means, the user
 *    must publish this part of source,
 *    but don't need to publish the whole source of the own product.
 * 5) You can study and modify (improve) this source
 *    for own using or for redistribution, but you have to license the
 *    modified sources likewise under this LGPL Lesser General Public License.
 *    You mustn't delete this Copyright/Copyleft inscription in this source file.
 *
 * @author Hartmut Schorrig: hartmut.schorrig@vishia.de, www.vishia.org
 * @version 0.93 2011-01-05  (year-month-day)
 *******************************************************************************/ 
package org.vishia.java2C.test;

public class TestgarbageCollector
{

  /**This static reference references a new created instance, so it isn't delete from the garbage collector. */
  private static SimpleClass staticData; 
  
  
  final private void useCreatedInstanceInternal()
  {
  	/**Creates a new instance, works with it, but after them the instance isn't need furthermore. 
     * It will be deleted by garbage collector. */
    { SimpleClass dataTemp = new SimpleClass();
      dataTemp.x1 = 34;
      /**To concat a temporary variable will be used and freed from GC. */
      System.out.println("test:" + dataTemp.x1);
    }  
    
  }
  
  /**This class creates a new instance and returns the pointer to them. It should not be activated
   * for handling with garbage collection.
   * @return the instance reference.
   * @java2c=return-new.
   */
  private SimpleClass createAndReturnInstance()
  {
    SimpleClass obj = new SimpleClass();
    /**Because this obj is returned, it won't be activated for garbage collection. */
    return obj;
  }
  
  
  /**This method calls the method {@link #createAndReturnInstance()} to create an instance, which is not stored
   * in the class. The reference is known only in a stack variable. The reference is used. 
   * The garbage collector should not attack this instance. To force working of garbage collection,
   * a Thread.sleep() is built in this method. 
   */
  public void testNewInstance()
  {
    /**Because the data are returned from an method, it will be activated for garbage collection 
     * at end of this method, except if the reference is returned.
     */
    SimpleClass data = createAndReturnInstance();
    for(int ii =0; ii<5; ii++){
      try{ Thread.sleep(1000);} 
      catch (InterruptedException e){}
      data.x1 += 234;
      System.out.println("testNewInstance: " + data.x1);
    }  
  }
  
  
  
  
  
  void test()
  {
    /**Creates a new instance and referes it to a static reference: */
    { SimpleClass data = new SimpleClass();
      staticData = data;
    }
    System.gc();
    
    useCreatedInstanceInternal();
    
    System.gc();
    
  }
  
  
  
}
