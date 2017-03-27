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

public interface Ifc2
{
  /**Example for an interface method with the same name but other parameter types,
   * which is implemented together with {@link Ifc#processIfcMethod(int)} in {@link ImplIfc}.
   * In Java that are two different methods of course, and also in C.
   * In both interfaces, this methods are independent, this method is declared with an method type
   * in its headerFile, adequate to the other method in Ifc:
   * <pre class="CCode">
   * typedef float MT_processIfcMethod_Ifc2_Test(ObjectJc* ithis, float input, ThCxt* _thCxt);
   * </pre> 
   */
  float processIfcMethod(float input);

  /**Another method. */
  float testIfc2(float input);

  /**Example for an interface method, which has the same name and same paremeters like the method
   * {@link Ifc#anotherIfcmethod(float)}. Therefore only one implementation is necessary. 
   * In this interface the method type
   * <pre class="CCode">
   * METHOD_C float anotherIfcmethod_Ifc2_Test(ObjectJc* ithis, float input, ThCxt* _thCxt);
   * </pre>
   * is declared for it. In opposite the method type of the same method {@link Ifc#anotherIfcmethod(float)} is
   * <pre class="CCode">
   * METHOD_C float anotherIfcmethod_f_Ifc_Test(ObjectJc* ithis, float input, ThCxt* _thCxt);
   * </pre>
   * Both declarations are independent, because the interface definitions are so. But the implementation
   * for both methods are the same, and the implementation are compatible to both declarations of function type.
   * See {@link ImplIfc#anotherIfcmethod(float)}.
   */
  float anotherIfcmethod(float input);
  
  
}
