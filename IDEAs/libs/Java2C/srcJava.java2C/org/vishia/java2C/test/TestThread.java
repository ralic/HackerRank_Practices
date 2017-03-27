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

/**This class demonstrates and tests the usage of threads with the synchronized access to data (mutex). 
 * It implements the run()-Method of the Thread defined in the interface Runnable. 
 * <br><br>
 * The threads private data which should be able to access only by the current thread are stored
 * in an extra class, here defined as static inner class.
 *
 */
public class TestThread implements Runnable
{
  
  /**This is a central thread-control variable. If it is set to true, if the thread is started. */
  private boolean threadRunning;
  
  /**This is a central thread-control variable. If it is set to false, if the thread is finished. */
  private boolean threadFinished;
  
  /**Two test counter, they will be count always simultaneously. Therefore a <code>synchronized</code>
   * is used. The data of an instance of this class, especially this counters are visible from outside the thread.
   * Because there are private, an access to it is only possible in this class. But the method which is accessing
   * may be called from another thread. Therefore a mutex-access is necessary, 
   * see {@link #testSynchronized(org.vishia.java2C.test.TestThread.TestThreadLocalData)}
   * and {@link #otherThreadRoutine()}.
   * 
   */
  private int testCt1 = 0, testCt2 = 0;
  
  /**This counter is used to test whether the interrupting of the thread works. */
  int testCtInterrupted = 0;
  
  /**A Thread-instance is a composite part of this class. The thread-instance organizes the thread only.
   * Another possibility is deriving this class from Thread instead implementing Runnable. It is realized
   * in {@link TestWaitNotify}. 
   */
  final Thread theThread = new Thread(this, "TestThread");
  
  /**This class is defined only to use in the threads context. No other thread should have access to it.
   * Therefore the instance can be defined in a thread-local data range. For Java2C it is possible
   * to create a stack-instance. It is done in the {@link TestThread#run()}. routine.
   * <br><br>
   * 
   */
  private final static class TestThreadLocalData
  {
    float x,y;
    
    /**Example for a large data area. The buffer is allocated in C in the class as embedded instance,
     * because the class' instances is allocated in stack, the buffer is located in the stack.
     * <br><br>
     * If the Java program takes the reference of the buffer and stores it in another instance,
     * which is accessed from another thread, it is possible in Java because in Standard-Java
     * it's a normal instance in heap. But the implementation in C in a protected memory system
     * will force a memory protection error, because the data area is located in the stack of another thread.
     * The stacks of several threads should not accessible one to another. That is ones of distinctions
     * between a standard Java environment to test and the implementation in a target system.
     * Such errors should recognized in the test of target. 
     */
    final StringBuffer threadownBuffer = new StringBuffer(3000);
  }
  
  /**Aggregation to data to check wait/notify. */
  private final TestWaitNotify.WaitNotifyData theNotifyingData;
  
  /**Constructor. 
   * @param theNotifyingData The aggregation to data, which is used to test wait-notify.
   */
  TestThread(TestWaitNotify.WaitNotifyData theNotifyingData)
  {
    this.theNotifyingData = theNotifyingData;
  }
  
  /**This is the thread main-routine complying the Java rules. The routine is started 
   * if the java.lang.Thread-instance, which associates the class ({@link #theThread}, 
   * is called with ,,start(),,-method. 
   * implements {@link java.lang.Runnable#run()}
   * <br><br>
   * The thread-main-routine creates instances, which are only accessed by this thread.
   * In Java the instances are referenced, but the reference is only known in stack context,
   * provided to called routines via parameter. In C the instances are allocated in the stack
   * because a <code>@ java2c=stackInstance.</code> is written thereby. Large-size instances need
   * an adequate stack size. The Java-code for this code-snippet is:
   * <pre class="Java">
     ...* @java2c=stackInstance. * /
    TestThreadLocalData threadLocalData = new TestThreadLocalData();
   * </pre>
   * The generated C-code is:
   * <pre class="CCode">
    TestThread_Test__TestThreadLocalData_s threadLocalData;  
    ...
    init_ObjectJc(&(threadLocalData.base.object), sizeof(threadLocalData), 0); 
    ctorO_TestThread_Test__TestThreadLocalData(&(threadLocalData.base.object), _thCxt);
   * </pre>
   * The thread contains a <code>for</code>-loop to force a determined call  of <code>testSynchronized</code>,
   * and a call of <code>notify(...)</code> to test the wait/notify-concept with a third thread.
   * Than a sleep is called to delay the execution of the thread with deterministic milliseconds-time. 
   * The Java-form is:
   * <pre class="Java">
    for(int liveCt = 0; liveCt <200; liveCt++){
      testSynchronized(threadLocalData);
      theNotifyingData.notify(testCt1);
      try{ Thread.sleep(10); } 
      catch(InterruptedException exc){
        testCtInterrupted +=1;
      }
      
    }//for
   * </pre>
   * The catch clause is processed if the thread was woken up abnormal. The coding of this catch is prescribed in Java.
   * In C it is mapped too, but it isn't used yet. 
   * <br><br>
   * The C-code of this snippet is:
   * <pre class="CCode">
      for(liveCt = 0; liveCt < 200; liveCt++)
        { testSynchronized_TestThread_Test(ythis, & (threadLocalData), _thCxt);
          theNotifyingDataMtbl.mtbl->notify( (theNotifyingDataMtbl.ref), ythis->testCt1, _thCxt);
          TRY
          { sleep_ThreadJc(10, _thCxt);
          }_TRY
          CATCH(InterruptedException, exc)
            { ythis->testCtInterrupted += 1;
            }
          END_TRY
        }
   * </pre>
   * The call of <code>theNotifyingDataMtbl.mtbl->notify(...)</code> is executed dynamically,
   * because the destination instance can be an derived class of the reference type
   * in an enhancement of the example. To prevent effort to get the method table reference,
   * the Java-code contains a stack-local reference at top of the routine, 
   * see {@link org.vishia.java2C.Docu.D_SuperClassesAndInterfaces#D6_callingOverrideableMethods()}.
   * <pre class="Java">
    /**Use local variable to enforce only one preparation of the method table for dynamic call:
     * @java2c=dynamic-call.
     * /
    final TestWaitNotify.WaitNotifyData theNotifyingDataMtbl = theNotifyingData;
   * </pre> 
   */
  public void run(){
    threadRunning = true;
    /**Use local variable to enforce only one preparation of the method table for dynamic call:
     * @java2c=dynamic-call.
     */
    final TestWaitNotify.WaitNotifyData theNotifyingDataMtbl = theNotifyingData;
    /**This instance is only visible in the threads context. It is allocated in the stack.
     * @java2c=stackInstance. */
    TestThreadLocalData threadLocalData = new TestThreadLocalData();
    for(int liveCt = 0; liveCt <200; liveCt++){
      testSynchronized(threadLocalData);
      theNotifyingDataMtbl.notify(testCt1);
      try{ Thread.sleep(10); } 
      catch(InterruptedException exc){
        testCtInterrupted +=1;
      }
      
    }//for
    System.out.println("test-thread stopped at " + threadLocalData.x);
    threadFinished = true;
  }


  
  /**This routine shows and support test of a mutex-access. 
   * The {@link #testCt1} and {@link #testCt2} of this class are decremented together. 
   * The rule is: both counter should contain the same number. There are incremented 
   * in the routine {@link #otherThreadRoutine()} from another thread and decremented here
   * independently, but similar. The equality of both counter values are tested on entry 
   * using an assert statement. The Java-Code is
   * <pre class="Java">
    synchronized(this)
    { assert(testCt1 == testCt2);
      testCt1 -=2;
      try{ Thread.sleep(5);} 
      catch (InterruptedException e){}   
      testCt2 -=2;
      threadLocalData.x = testCt1;
    }  
   * </pre> 
   * The C-Code is:
   * <pre class="CCode">
    synchronized_ObjectJc(& ((* (ythis)).base.RunnableJc.base.object)); {
      { ASSERT(ythis->testCt1 == ythis->testCt2);
        ythis->testCt1 -= 2;
        TRY
        { sleep_ThreadJc(5, _thCxt);
        }_TRY
        CATCH(InterruptedException, e){} END_TRY
        ythis->testCt2 -= 2;
        threadLocalData->x = ((float)(ythis->testCt1));
      }
    } endSynchronized_ObjectJc(& ((* (ythis)).base.RunnableJc.base.object));
   * </pre>
   * The <code>sleep</code>-call is disposed here only to provoke a thread switch 
   * during this mutex-saved operation. Without <code>synchronized</code> the interruption of this
   * statement block between the two counter-increments from the other thread accessing the counters
   * is likely, the <code>synchronized</code> is effective well able to test.
   * <br><br>
   * The Java construction 
   * <pre class="Java">
   * synchronized {
   *   ...guarded block
   * }  
   * <pre>
   * is well bounded with <code>{ .... }</code>, the programmer can't forget the exit from the guard.
   * It is the syntax of Java. Hand written in C, the exit of the guard block (critical section)
   * may be missed because the programmer has forgotten it. The C-translation of Java is safety respectively this problem,
   * though an extra routine <code>endSynchronized_ObjectJc</code> is called.
   * @param threadLocalData
   */
  final void testSynchronized(TestThreadLocalData threadLocalData)
  { synchronized(this)
    { assert(testCt1 == testCt2);
      testCt1 -=2;
      
      /**Let the thread sleeping to provoke a switch to the other thread. 
       * It isn't a pattern for a well programming, because it is in a synchronized block.
       * It is only prober to force errors.
       */
      try{ Thread.sleep(5);} 
      catch (InterruptedException e){}   
      testCt2 -=2;
      threadLocalData.x = (float)testCt1;
    }
  }
  
  
  
  
  /**Facade routine to start the Thread.
   * The Java-lines are:
   * <pre class="Java">
    / **@java2c=stackSize(TestThreadLocalData+2000). * /
    theThread.start();
   * </pre>
   * The stackSize is necessary for the C-implementation. Because an instance of <code>TestThreadLocalData</code>
   * is created in the {@link #run()}-Method, its size should be regarded. 
   * The rest, 2000 Bytes, is a proper value for typical stack usages.
   * <br><br>
   * The translated C-code is
   * <pre class="CCode">
    start_ThreadJc(& (ythis->theThread)
                  , sizeof(TestThread_Test__TestThreadLocalData_s)+2000, _thCxt);
   * </pre> 
   * The stackSize-annotation is translated to the stackSize-parameter-value for the <code>start_ThreadJc</code>-routine.
   * The really size should be tested at C-level-debugging.
   */
  public void start()
  { /**@java2c=stackSize(TestThreadLocalData+2000). */
    theThread.start();
  }
  
  /**This routine is called from outside in another thread. It demonstrates a concurrent access 
   * which is guard with a <code>synchronized</code> adequate to {@link #run()}.
   * This routine works with 500 loops in a time of 10 ms. At its end {@link #shouldRun} is set to <code>false</code>,
   * which causes an aborting of the {@link #run()}-routine and therefore the finishing of the thread.
   * 
   */
  public void otherThreadRoutine()
  {
    for(int i = 0; i < 200; i++){
      synchronized(this)
      //if(testCt1 ==5){}
      { assert(testCt1 == testCt2);
        testCt1 +=3;
        testCt2 +=3;
      }
      try{ Thread.sleep(10);} 
      catch (InterruptedException e){}
    }//for
    
    /**Wait unil the thread of the instance is finished: */
    int ctWaitFinished = 0;
    while(!isThreadFinished()){
      ctWaitFinished ++;
      /**This is a polling loop. It should be contain a reasonable wait statement to release the CPU-ressource while polling: */
      try{ Thread.sleep(5);} 
      catch (InterruptedException e){}
    }
    System.out.println("main-thread stopped at " + testCt1 + ", ctWaitFinished=" + ctWaitFinished);

  }

  /**Returns true if the class private variable {@link #threadFinished} is set. That occurs at last action
   * of the {@link #run()}-routine of the thread.
   * @return
   */
  public final boolean isThreadFinished()
  {
    return threadFinished;
  }
}
