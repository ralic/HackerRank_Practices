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

public class TestAnonymous {

	int testValueOuter = 34;
	
	SimpleClass ref;
	
	/**An composite instance is able to access with the type {@link SimpleClass}, 
	 * but it is an enhanced instance of {@link ImplIfc}, which based in {@link SimpleClass}.   
	 * It is suitable to use 
	 * <ul>
	 * <li>if the overridden method bodies should access data of the outer class {@link TestAnonymous},
	 * <li>if the access to this methods are sufficient over the interface or base class.
	 * </ul> 
	 */
	final SimpleClass ref2 = new ImplIfc(23)
	{
		@Override int addValue(int value) {
		  x1 += testValueOuter * value;  ////access to an value of the outer class.
	    return x1;
		}
	};
	
	/**A static anonymous instantiated reference. 
	 * TODO: The static variant doesn't work yet (Version 0.92) because the instantiation isn't clarified.
	 */
	/*static*/ SimpleClass staticAnonymous = new SimpleClass(23)
	{
		float disturbing;
		float x1;   //covers the x1 of the base class.
		@Override int addValue(int value) {
		  disturbing = (float)Math.random() - 0.5F;
			x1 += value + disturbing;  //access to an value of the outer class.
	    super.x1 = (int)x1;
			return super.x1;
		}
	};
	
	void setRef(SimpleClass ref){
		this.ref = ref;
	}
	
	void test(){
		int testvalue1 = 212;
		//An anonymous class is instanciated here, but dynamically.
		SimpleClass refTempSimpleClass = new SimpleClass(12){
			int addValue(int value)   //with this changed method addValue().
		  { testValueOuter = value; 
		    x1 += value;            
		    return x1;
		  }
		};
		refTempSimpleClass.addValue(43);
		//test of a anonymous class definition inside the expression for a parameter of a method.
		///
		setRef(
			new SimpleClass(34){        //The class SimpleClass is overridden:
				int addValue(int value)   //with this changed method addValue().
			  { x1 -= value;            //other operation.
			    x1 += testValueOuter;   ////access to an value of the outer class.
			    //x1 += testvalue1;       //access to values of the method-body or statement-block is not possible.
			    return x1;
			  }
			}
		);
		for(int x=0; x<100; x++){
			ref.addValue(x);  //calls the method above....
			// */
			ref2.addValue(x);
			
			staticAnonymous.addValue(x);
		}
		System.out.println("TestAnonymous " + staticAnonymous.getValue());
	}
	
	
	final class TestInnerNonstatic
	{
		int xInnerNonstatic;
		
		final void testInnerNonstatic()
		{
			testValueOuter = xInnerNonstatic;  //
		}
	}
	
	
	final static class TestInnerStatic
	{
		int xInnerStatic;
		
		final void testInnerNonstatic(int input)
		{
			xInnerStatic = input;
		}
	}
	
	
}
