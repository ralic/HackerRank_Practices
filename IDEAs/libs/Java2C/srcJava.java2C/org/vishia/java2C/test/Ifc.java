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

/**This interface should demonstrate the translation of Java-Interfaces to C. Visit the translated
 * <code>Ifc_Test.c</code> and <code>Ifc_Test.h</code>.
 * <br>
 * The C-File contains code for:
 * <ul>
 * <li>An implementation of all interface methods, which searches the appropriate method table
 *   of the interface inside the method table of the given instance, calling <code>getMtbl_ObjectJc(...)</code>
 *   and than calls the method dynamically. The user can call this method static linked, this method 
 *   organized the dynmically call. ( Elsewhere the user can optimize and call via method table itself,
 *   Java2C translates such calls.)
 * <li>Reflection for the interface, contains the reference to the method table.
 * <li>The definition of the <code>sign_Mtbl_...</code> of this interface.
 * <li>The definition of all final static variables which are not defined as <code>#define ...</code>
 * </ul>
 * The Headerfile contains declarations and definitions for:
 * <ul>
 * <li>The <code>typedef struct {...}</code> of the interface type. The struct contains only 
 *   <code>union { ObjectJc object; } base; </code>
 * <li>Typedef for the array type.
 * <li>extern declaration for reflection.
 * <li>extern declaration or <code>#define ...</code>for all static final values.
 * <li>Definition of <code>MT_</code> method type of the interface methods.
 * <li>Declaration of head of interface methods to implement the dynamic call.
 * <li>extern Declaration of the <code>sign_Mtbl_...</code> of teh interface.
 * <li>Definition of the struct of the method table.
 * </ul>
 * Compilation and linking the C-File: 
 * The kept C-File of any translated Java-interfaces should be compiled and provided as object-file 
 * maybe in a library outside of a usage of the interface in an implementation. 
 * It means, it should not be a part of a implementation library of the interface, 
 * but it should be a part of a library providing the interface stand-alone independent of its usage.
 * It is the same like a Java-package with interfaces only. It has its compiled byte code.
 * <br>
 *   
 * @author Hartmut Schorrig
 *
 */
public interface Ifc
{
  /**Example for a simple constant declared in java. Because it is fix and constant, 
   * it is translated in C as
   * <pre class="CCode">
   * #define constValue_Ifc_Test 24
   * </pre>
   * In this form the constant is used as an immediate value. A usage of this value is independent 
   * of the content of the interface' C-file. The same behavior is known for java in such cases:
   * A substitution of a class file which contains another value for the same constant doesn't update
   * the value in already-compiled class files. 
   */
  static final int constValue = 24;
  
  /**Example for a simple constant string literal. It is declared as 
   * <pre class="CCode">
   * extern const StringJc constString_Ifc_Test;
   * </pre>
   * and defined in the C-File. It means, its value is updated if the interface is compiled and supplied newly.
   * The string literal itself is stored only one time in the code. It is better as translating 
   * a <code>#define constString_Ifc_Test "IfcTest"</code>.
   */
  static final String constString = "IfcTest";
  
  /**Example for a calculated constant.At this time this constant is declared in Headerfile as
   * <pre class="CCode">
   * extern const int32 constValue2_Ifc_Test;
   * </pre>
   * and defined in C-file as 
   * <pre class="CCode">
   * const int32 constValue2_Ifc_Test = (constValue_Ifc_Test + 1) / 2 + 1;
   * </pre>
   * where the calculation is made at compile time. It is possible because the input values are constants
   * or constants defined per #define. 
   * <br>
   * This strategy of compiling is limited. A constant defined in this form isn't able to use in C
   * as constant input to built a adequate constant. The compiler may cause an error "initializer is not a constant"
   * though the value is defined before. (testet with visual-Studio-6 C-compilation).
   * Therefore it is better to build a 
   * <pre class="CCode">
   * #define constValue2_Ifc_Test (constValue_Ifc_Test + 1)/2 +1
   * </pre>
   * The behavior should be changed in the next releases of Java2C. 
   */
  static final int constValue2 = (constValue + 1)/2 +1;

  
  /**Example for a calculated constant using another calculated constant as input.
   * At this time the C-Compiler causes an error, see about. Therefore it is commented.
   */
  //TODO: static final int constValue3 = (constValue2 + constValue+ 1)/2 +1;
  
  
  /**Example for an interface method. This method is only abstract. 
   * In C it is a member of a so named <i>method table</i>. Therefore a method type definition
   * is written in the Java2C-translated Headerfile:
   * <pre class="CCode">
   * typedef int32 MT_processIfcMethod_Ifc_Test(ObjectJc* ithis, int32 input, ThCxt* _thCxt);
   * </pre>
   * The <i>method table</i> is defined one time for all interface methods, containing both methods 
   * of this example of Java-interface:
   * <pre class="CCode">
   * typedef struct Mtbl_Ifc_Test_t
   * { MtblHeadJc head;
   *   MT_processIfcMethod_Ifc_Test* processIfcMethod;
   *   MT_anotherIfcmethod_i_Ifc_Test* anotherIfcmethod_i;
   *   MT_anotherIfcmethod_f_Ifc_Test* anotherIfcmethod_f;
   *   Mtbl_ObjectJc ObjectJc;
   * } Mtbl_Ifc_Test;
   * </pre>
   * An implementation uses this struct definition inside its more complex method table struct definition,
   * defines a const method table instance and set the implementation of the method at the appropriate position,
   * see {@link ImplIfc#processIfcMethod(int)}.
   * @param input Example
   * @return Example
   */
  int processIfcMethod(int input);


  
  
  /**Example for a second interface method, not a recentness, but see next {@link #anotherIfcmethod(float)}.
   * @param input
   * @return
   */
  int anotherIfcmethod(int input);

  /**Example for an interface method with same name as another in the same class, but other parameter set.
   * The ambiguousness of the methods are detect and a unambiguous C-name is build. Therefore the two methods
   * builds several method type declarations in headerfile:
   * <pre class="CCode">
   * typedef int32 MT_anotherIfcmethod_i_Ifc_Test(ObjectJc* ithis, int32 input, ThCxt* _thCxt);
   * typedef float MT_anotherIfcmethod_f_Ifc_Test(ObjectJc* ithis, float input, ThCxt* _thCxt);
   * </pre>
   * and the adequate positions in the method table.
   * @param input
   * @return
   */
  float anotherIfcmethod(float input);

}
