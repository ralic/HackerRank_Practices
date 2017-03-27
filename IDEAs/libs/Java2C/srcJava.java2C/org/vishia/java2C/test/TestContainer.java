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

import java.util.LinkedList;


/**This class demonstrates and tests the usage of some container concepts. 
 *
 */
public class TestContainer 
{

	private final LinkedList<Object> linkedList = new LinkedList<Object>();
	
	private final SimpleClass anObject = new SimpleClass();
	
	void addToList(Object src)
	{
		linkedList.add(src);
	}
	
	Object removeFirstfromLinkedList()
	{
		if(linkedList.size() >0){
			Object data = linkedList.remove();
		  return data;
		} else {  
		  return null;
		}
	}
	
	void addSomeData()
	{
		for(int ix=0; ix<5; ix++){
			SimpleClass data = new SimpleClass();  //alloc in heap
			linkedList.add(data);
		}
		
	}
	
	
	void getAndRemoveAllData()
	{ Object data;
		do{
			data = removeFirstfromLinkedList();
		}while(data != null);
	}
	
	
	
	void test()
	{ addSomeData();
    getAndRemoveAllData();
	  stressTest.start();   //runs the thread, but the method finished.  
	}

	
	
	/**It adds and removes permanently, But the limit of the heap should be regarded.
	 */
	private final Thread stressTest = new Thread("stressTestC"){
		@Override public void run(){
			boolean bRun = true;
			do{
				double whatodo = Math.random();
			  if(whatodo < 0.25){
			  	//add
			  	//SimpleClass data = new SimpleClass();  //alloc in heap
					linkedList.add(anObject);  //now only alloc node.
			  } else if(whatodo <0.5){
			  	//remove
					if(linkedList.size()>0){
			  		linkedList.remove();
			  	}
			  } else if(whatodo <0.625){
			  	//wait thread
			  	try{ wait(10);} catch(InterruptedException exc){ bRun = false;}
			  } else {
			  	//do nothing, try again.
			  }
			} while(bRun);  
		}
	};	
	
	
	
}
