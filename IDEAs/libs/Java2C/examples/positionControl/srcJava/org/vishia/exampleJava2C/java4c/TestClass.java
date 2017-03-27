package org.vishia.exampleJava2C.java4c;

/**This class is only used to test how the garbage collector concept is implemented.
 * It is not used actively in running code yet, because the GC concept is just under construction.
 * @author JcHartmut
 *
 */
public class TestClass
{
  protected int a;
  void set(int value){ a = value; }

  int get(){ return a; }
}
