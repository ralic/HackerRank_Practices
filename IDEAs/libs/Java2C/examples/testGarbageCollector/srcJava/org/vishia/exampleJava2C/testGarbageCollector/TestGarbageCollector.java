package org.vishia.exampleJava2C.testGarbageCollector;





public class TestGarbageCollector
{


  TestClass refA;
  TestClass refB;
    
  
  public final static void main(String[] args)
  { TestGarbageCollector i1 = new TestGarbageCollector(); 
    theInstance = i1;
    theInstance.run();
  }

  static TestGarbageCollector theInstance;
  
  /**The run method called in static main(). It uses some references. */
  void run()
  { //TestClass instance = new TestClass(1);
    refA = new TestClass(1);
    refA.test1  = new TestClass(2);    //this refs (1) and (1) refs (2)
    refA.test1 .test2 = refA;     //and (2) refs (1)
    /**Both class are references via refA. There are not to freed. */
  }
  
  
  /**This method is be called as test in C-level while the garbage collector runs,
   * to test the following scenario: 
   * 
   *  If in file CRuntimeJavalike/BlockHeapJc_References.c 
   *  the line with "theGarbageCollectorJc.bAbortTest = true" is commented, 
   *
   *  and the garbage Collector has already tests the TestClass(2),
   *  it is only referenced from the block TestClass(1), not from outside.
   *  Than this routine is called, after it the TestClass(2) is referenced from outside,
   *  but the GC don't know it. 
   *  But the GC detect now, that TestClass(1) isn't referenced
   *  from outside, both blocks are imagined that there are not referenced, 
   *  and there will be freed. But this is an thread timing mistake.
   *  
   *  The implementation is, to abort the GC test if a tested block is reused.
   *  That is implemented with setting "theGarbageCollectorJc.bAbortTest = true"
   *  and testing it in file CRuntimeJavalike/BlockHeapJc_GarbageCol.c.
   * 
   */
  void run1()
  { //The reference is changed to the other instance.
    /** To test:
     */
    refB = refA.test1;  //reference to the 3. block, 
    refA = null;        //remove the reference to the 2. block
  }

  

}