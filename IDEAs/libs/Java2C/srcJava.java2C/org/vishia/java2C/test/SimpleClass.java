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


/**This is an example for a simple class without interface, but override-able methods.
 * It based on ObjectJc, because the overriding of methods needs this base class.
 * The <code>struct</code> in C is defined in the following form:
 * <pre class="CCode">
typedef struct SimpleClass_Test_t
{ 
  union { ObjectJc object; } base; 
  int32 x1;
} SimpleClass_Test_s;
 * </pre>
 * It contains the base data of <code>ObjectJc</code> wrapped with the <code>union</code>,
 * because the access to the <code>ObjectJc</code> is written anyway in form <code>ref.base.object</code>.
 * The <code>struct</code> contains the data. 
 * <br><br>
 * There are two additional defines for all classes in C:
 * <pre class="CCode">
typedef struct SimpleClass_Test_Y_t { ObjectArrayJc head; SimpleClass_Test_s data[50]; } SimpleClass_Test_Y;

extern struct ClassJc_t const reflection_SimpleClass_Test_s;
 * </pre>
 * <ul>
 * <li>The first line defines a array structure. It is ready to use always. The number of data is a placeholder.
 * The structure type is used as a reference anytime. Than the really number of elements in the array
 * depends from the memory allocation size and it is stored in the head data.  
 * <li>The second line declares the existence of reflection for the class in C.
 * </ul>
 * Methods are defined as prototypes in headerfile:
 * <pre class="CCode">
METHOD_C struct SimpleClass_Test_t* ctorO_SimpleClass_Test(ObjectJc* othis, ThCxt* _thCxt);

typedef int32 MT_addValue_SimpleClass_Test(SimpleClass_Test_s* ythis, int32 value, ThCxt* _thCxt);

METHOD_C int32 addValue_SimpleClass_Test_F(SimpleClass_Test_s* ythis, int32 value, ThCxt* _thCxt);

METHOD_C int32 addValue_SimpleClass_Test(SimpleClass_Test_s* ythis, int32 value, ThCxt* _thCxt);
 * </pre>
 * <ul>
 * <li>The first line is the constructor definition, auto generated.
 * <li>The second line is the method type definition as a so named <i>C-function pointer type</i>.
 *   It is used in the method table.
 * <li>The third line declares the final version of the method, 
 *   whereas the forth line declares that method definition, which executes a dynamic linked call.
 * </ul>
 * The header file contains at last the method table type definition:
 * <pre class="CCode">
extern const char sign_Mtbl_SimpleClass_Test[]; //marker for methodTable check
typedef struct Mtbl_SimpleClass_Test_t
{ MtblHeadJc head;
  MT_addValue_SimpleClass_Test* addValue;
  Mtbl_ObjectJc ObjectJc;
} Mtbl_SimpleClass_Test;
 * </pre>
 * See {@link org.vishia.java2C.Docu.D_SuperClassesAndInterfaces#D8_gen_MethodTable()}.  
 */
public class SimpleClass
{

	/**Any reference. It may be used in a derived class. 
	 * But because that, a finalize method is generated to clean the reference. */
	Object anyRef;
	
  /**A value stored in the class' instance. */
  int x1 = 5;
  
  /**Parametrized constructor to set an initial value. 
   * @param value Intial value.
   */
  SimpleClass(int value)
  {
  	x1 = value;
  }
  
  /**Empty constructor: The variable x1 keeps its default value. */
  SimpleClass(){}
  
  /**Add something. This method is able to override.
   * @param value Value to add.
   * @return The new value.
   */
  int addValue(int value)
  { x1 += value;
    return x1;
  }
  
  /**Returns the current value. */
  int getValue(){ return x1; }
}
