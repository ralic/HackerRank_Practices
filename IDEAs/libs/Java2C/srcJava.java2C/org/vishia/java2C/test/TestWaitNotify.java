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

/**This class demonstrates and tests the usage of threads with wait and notify. 
 * It implements the run()-Method of the Thread defined in the interface Runnable, which is inherited from the super class Thread.
 * The class based on Thread, therefore no other instance is necessary to organize the thread. 
 * <br><br>
 * The threads private data which should be able to access only by the current thread are stored
 * in an extra class, here defined as static inner class.
 *
 */
public class TestWaitNotify extends Thread
{

  /**This is a central thread-control variable. If it is set to false, the thread should finish. */
  boolean shouldRun = true;
  
  //TODO if this class is placed after TestThreadLocalData, a translation error occurs. Test!
  /**This class is visible from outside, it is used from the notifying thread and from this thread, 
   * which waits for data.
   * 
   */
  public final static class WaitNotifyData
  {
    /**A value which is supplied with notify. */
    int x;
    
    /**Sequence counter to detect a notify. It is incremented on any notify(). */
    int ctNewData;
    
    /**Notify routine, it may be called from outside. New data are stored than. The <code>notify()</code>-call
     * have to be placed in a <code>synchronized</code>- (mutex-)-block. Elsewhere in Java an exception is thrown.
     * It is necessary to do so. That <code>synchronized</code>-block should be used to set the data,
     * which are supplied with the notify action. Than the data and the notify-call are mutual exclusive handled.
     * The Java-code is: 
     * <pre class="Java">
      synchronized(this){
        x = value;
        ctNewData +=1;
        notify();
      }  
     * </pre>
     * The value is stored. The sequence counter is incremented to advertise the notify. Than notify is called.
     * All this operations are done under mutex in the <code>synchronized</code>-block.
     * The translated C-code is:
     * <pre class="CCode">
    synchronized_ObjectJc(& ((* (ythis)).base.object)); {
      ythis->x = value;
      ythis->ctNewData += 1;
      notify_ObjectJc(& ((* (ythis)).base.object), _thCxt);
    } endSynchronized_ObjectJc(& ((* (ythis)).base.object));
     * </pre>
     * @param value The value supplied with notify.
     */
    public void notify(int value)
    { synchronized(this){
        x = value;
        ctNewData +=1;
        notify();
      }  
    }
  }
  
  
  
  
  /**This class is defined only to use in the threads context. No other thread should have access to it.
   * Therefore the instance can be defined in a thread-local data range. For Java2C it is possible
   * to create a stack-instance.
   * <br><br>
   * 
   */
  private final static class TestThreadLocalData
  {
    int x,y;
    
    /**A sequence counter which holds the last value of {@link WaitNotifyData#ctNewData} to check
     * whether all data are got.
     * 
     */
    int seqCtLast = -1, seqCt;
    
    /**This counter is used to test whether the interrupting of the thread works. */
    int testCtInterrupted = 0;
    
    int testCtNothingReceived = 0;
    
    int testCtSuccessNotify = 0;
    
    int testCtMissNotify = 0;
    
    /**Association to the data which are notifying from outside.
     */
    private final WaitNotifyData theAwaitingData;

    TestThreadLocalData(WaitNotifyData theAwaitingDataP)
    { this.theAwaitingData = theAwaitingDataP;  ///
    }
    
    /**In this routine the thread is waiting for data. The thread runs only, if new data are available.
     * But such an thread may be blocked forever, if no notifying occurs. Therefore it may be recommended,
     * that the wait action is interrupted cyclically, to check some other conditions. In this case this
     * routine waits max 1 second. If a notify doesn't occur, it returns anyway 
     * because outside there may be some other things to work.
     * <br><br>
     * The core of the routine is a wait for new data in Java written as:
     * <pre class="Java">
        synchronized(theAwaitingData){
          if(seqCtLast == -1){
            seqCt = seqCtLast = theAwaitingData.ctNewData;
          }
          theAwaitingData.wait(1000);
          seqCt = theAwaitingData.ctNewData;  //same as seqCtLast if no notify is called.
          valueFromAwaitingData = theAwaitingData.x;  
        }  
     * </pre>
     * This is translated to C in form:
     * <pre class="CCode">
      synchronized_ObjectJc(& ((* (REFJc(ythis->theAwaitingData))).base.object)); {
        if(ythis->seqCtLast == -1) 
        { 
          ythis->seqCt = ythis->seqCtLast = REFJc(ythis->theAwaitingData)->ctNewData;
        }
        wait_ObjectJc(& ((*(REFJc(ythis->theAwaitingData))).base.object), 1000, _thCxt);
        ythis->seqCt = REFJc(ythis->theAwaitingData)->ctNewData;
        valueFromAwaitingData = REFJc(ythis->theAwaitingData)->x;
      } endSynchronized_ObjectJc(& ((* (REFJc(ythis->theAwaitingData))).base.object));
     * </pre>
     * The rest of code tests the seqCt. If new data are available, the seqCt is incremented for 1.
     * If a notify is missed, the counter is incremented for greater 1. If no notify is occured,
     * but the wait time is out, the seqCt isn't incremented. This three conditions are tested, 
     * and the appropriate counters are counted. The counters give an overview of occurrence 
     * while running the process. Last not least the value given by notify is processed. But it is
     * only an example.
     */
    private void awaitData()
    {
      int valueFromAwaitingData;
      try { 
        synchronized(theAwaitingData){
          if(seqCtLast == -1){
            /**initial:*/
            seqCt = seqCtLast = theAwaitingData.ctNewData;
          }
          /**Wait at maximum 1 second. */
          theAwaitingData.wait(1000);
          /**the thread is waken up either because notify or because time.*/
          seqCt = theAwaitingData.ctNewData;  //same as seqCtLast if no notify is called.
          /**Copy the value to a stack variable, because after synchronized-end the value may be changed already.*/
          valueFromAwaitingData = theAwaitingData.x;  
        }  
      } 
      catch(InterruptedException exc){
        testCtInterrupted +=1;
        valueFromAwaitingData = 0;
      }
      /**All data to process are stored in this instance, it is accessible only be the own thread. */
      int seqCtDiff = seqCt - seqCtLast;
      seqCtLast = seqCt;
      if(seqCtDiff == 1){
        /**The next data are received.*/
        x += valueFromAwaitingData;
        testCtSuccessNotify +=1;
      }
      else if(seqCtDiff == 0){
        /**A wake up because time cycle has occurred ; */
        testCtNothingReceived +=1;
      }
      else if(seqCtDiff > 0){
        testCtMissNotify +=1;
      }
      else {
        assert(false);
      }
    }
    
    
    
  }
  
  
  /**Aggregation to data to check wait/notify. */
  final WaitNotifyData theAwaitingData;
  
  
  /**Constructor. 
   * @param theAwaitingData for aggregation
   */
  TestWaitNotify(WaitNotifyData theAwaitingDataP)
  { ////
    this.theAwaitingData = theAwaitingDataP;
  }
  
  
  /**This routine overrides <code>Thread.start()</code>, it's a facade. It calls Thread.start() using 
   * <pre class=Java>
    /**@java2c=stackSize(TestThreadLocalData+500). * /
    super.start();
   * </pre>
   * It is a facade, containing the stacksize annotation regarded in Java2C-translation.
   * The produced C-Code is:
   * <pre clas=CCode>
    start_ThreadJc(ythis, sizeof(TestWaitNotify_Test__TestThreadLocalData_s)+500, _thCxt);
   * <pre>
   * @see {@link java.lang.Thread#start()}
   */
  @Override public void start()
  {
    /**@java2c=stackSize(TestThreadLocalData+500). */
    super.start();
  }
  
  
  
  /**This is the thread main-routine complying the Java rules. The routine is started 
   * if the ,,start(),,-method of this instance is called. 
   * <br><br>
   * This routine creates an instances {@link TestThreadLocalData}, which are accessed by this thread only.
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
   * The thread contains a <code>while</code>-loop with test of {@link #shouldRun} and a sleep in the Java-form:
   * <pre class="Java">
    while(shouldRun){
      threadLocalData.awaitData();
    }//while
   * </pre>
   * In C it is mapped too, but it isn't used yet. 
   * <br><br>
   * The C-code of this snippet is:
   * <pre class="CCode">
    while(ythis->shouldRun) {
      awaitData_TestWaitNotify_Test__TestThreadLocalData_F(& (threadLocalData), _thCxt);
    }
   * </pre>
   */
  public void run(){
    /**This instance is only visible in the threads context. It is allocated in the stack.
     * @java2c=stackInstance. */
    TestThreadLocalData threadLocalData = new TestThreadLocalData(theAwaitingData);
    while(shouldRun){
      threadLocalData.awaitData();
    }//while
    System.out.println("wait/notify-thread stopped at " + threadLocalData.x);
    System.out.println("wait/notify-thread: nothingRcv=" + threadLocalData.testCtNothingReceived
                      + ", successfull=" + threadLocalData.testCtSuccessNotify
                      + ", missNotify=" + threadLocalData.testCtMissNotify
                      );
  }


  
}
