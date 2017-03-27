package org.vishia.exampleJava2C.testGarbageCollector;

public class TestClass
{
  int a;
    
  TestClass(int val){ a = val; }
  
  /**There is an aggregation to use it in Garbage Collection. */
  TestClass test1;
  
  /**There is a second aggregation to use it in Garbage Collection, a cluster of objects will be built. */
  TestClass test2;
  
}
