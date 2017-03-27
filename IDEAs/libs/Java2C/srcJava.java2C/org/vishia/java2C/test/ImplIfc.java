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

/**This class is an example for implementation of some interface methods.
 * <br>
 * The superclass {@link SimpleDataStruct} is a very simple class, only contains data.
 * The interfaces are complex, it contains methods with same name but different parameters, 
 * also dispersed to both interfaces. So the implementation of methods using method tables 
 * can be demonstrated. See descriptions of the method.
 * <br>
 * Visit the translated ImplIfc_Test.c and ImplIfc_Test.h.
 * <br> 
 * The struct of the class is defined in the Headerfile:
 * <pre class="CCode">
 * typedef struct ImplIfc_Test_t
 * { 
 *   union { ObjectJc object; ExpandableDataStruct_Test_s super;
 *           Ifc_Test_s Ifc_Test;Ifc2_Test_s Ifc2_Test;
 *         } base; 
 *   int32 val;  
 * } ImplIfc_Test_s;
 * 
 * </pre>
 * The other struct is the method table, it is shown as explanation on method {@link #anotherIfcmethod(float)}.
 * The method table shows and represents the structure of dynamic call-able (virtual) methods 
 * including the inheritance- and interface structure of this class. The implementation of interface
 * of this class in Java is transformed in C in the first place there.
 */
public class ImplIfc extends SimpleClass implements Ifc, Ifc2
{

  /**Example for a simple class variable. Mark value to difference several instances. */
  int val;
  
  /**An enhanced reference.
   * 
   */
  Ifc ref;
  
  final AnyClass data = new AnyClass(this); 
  
  /**Example for simple Constructor. The Constructor is declared in C as
   * <pre class="CCode">
   * METHOD_C struct ImplIfc_Test_t* ctorO_ImplIfc_Test(ObjectJc* othis, int32 val, ThCxt* _thCxt);
   * </pre>
   * The implementation is the C-Routine
   * <pre class="CCode">
   * struct ImplIfc_Test_t* ctorO_ImplIfc_Test(ObjectJc* othis, int32 val, ThCxt* _thCxt)
   * { ImplIfc_Test_s* ythis = (ImplIfc_Test_s*)othis;  //upcasting to the real class.
   *   int sizeObj = getSizeInfo_ObjectJc(othis);
   *   extern ClassJc const reflection_ImplIfc_Test_s;
   *   STACKTRC_TENTRY("ctorO_ImplIfc_Test");
   *   checkConsistence_ObjectJc(othis, sizeof(ImplIfc_Test_s), &reflection_ImplIfc_Test_s, _thCxt);  
   *   setReflection_ObjectJc(othis, &reflection_ImplIfc_Test_s, sizeof(ImplIfc_Test_s));  
   *   //j2c: Initialize all class variables:
   *   {
   *   }
   *   { ythis->val = val;
   *   }
   *   STACKTRC_LEAVE;
   *   return ythis;
   * }
   * The constructor routine is called with a given Memory space which is pre-initialized as ObjectJc,
   * named <code>othis</code>. The <code>checkConsistence_ObjectJc(...)</code>-Routine checks whether
   * the memory space is sufficient. It should be guaranteed. The <code>setReflection_ObjectJc(...)</code>
   * completes the ObjectJc base with the appropriate reflections defined for this class. 
   * In this example no class variable should be intitialized The core statements follows.
   * See the {@link TestAllConcepts#TestAllConcepts()} Constructor for a more extensive example.
   */
  ImplIfc(int val)
  { /**In the constructor a overridden method of a interface is used, call it dynamically. */
    this.val = processIfcMethod(val); 
  }
  
  /**Example for a method which is override-able and implements an interface method.
   * This method implements {@link Ifc#processIfcMethod(int)}.
   * The method is declared in C as
   * <pre class="CCode">
   * METHOD_C int32 processIfcMethod_i_ImplIfc_Test_F(ObjectJc* ithis, int32 input, ThCxt* _thCxt);
   * </pre>
   * The middle part of name is <code>_i_</code> because there is also a variant of the method 
   * with another parameter types. The suffix <code>_F</code>  is because the implementation version
   * of the method is the final version, but the user may call the also declared method:
   * <pre class="CCode">
   * METHOD_C int32 processIfcMethod_i_ImplIfc_Test(ObjectJc* ithis, int32 input, ThCxt* _thCxt);
   * </pre>
   * This variant implements the dynamic call (virtual method) internally.
   * <br>
   * The implementation of the final method is
   * <pre class="CCode">
   * int32 processIfcMethod_i_ImplIfc_Test_F(ObjectJc* ithis, int32 input, ThCxt* _thCxt)
   * { ImplIfc_Test_s* ythis = (ImplIfc_Test_s*)ithis;
   *   STACKTRC_TENTRY("processIfcMethod_i_ImplIfc_Test_F");
   *   { { STACKTRC_LEAVE;
   *       return ythis->base.super.xb + input + ythis->val;
   *     }
   *   }
   *   STACKTRC_LEAVE;
   * }
   * </pre>
   * The this-pointer is given in Form of ObjectJc, because it is an implementation of an interface method.
   * The cast to the real type is done in the next line. It is a unchecked cast. 
   * It may be correct, because this method is only called
   * <ul>
   * <li>via a Method table reference, where the choice of the method table part 
   *   is depending on the correct type, In this case the type can only be correct because calling code.
   * <li>via a direct call if the user denotes the reference with @ java2c=instanceType:"Type".
   *   In this case the type should be correct because the user should named the type carefully.
   * </ul> 
   * Therefore a type test is not generated automatically. If a safety level is need, the programmer
   * should write a first statement <code>instanceof Type</code> in Java. Another possibility were,
   * to generate a type test depending on @ java2c=checkType - annotation. This is not implemented yet,
   * but it was a possible feature for methods, which overrides or implements base methods.
   * <br>
   * The second generated method implementation for support dynamic call is:
   * <pre class="CCode">
   * / *J2C: dynamic call variant of the override-able method: * /
   * int32 processIfcMethod_i_ImplIfc_Test(ObjectJc* ithis, int32 input, ThCxt* _thCxt)
   * { Mtbl_Ifc_Test const* mtbl = (Mtbl_Ifc_Test const*)getMtbl_ObjectJc(ithis, sign_Mtbl_Ifc_Test);
   *   return mtbl->processIfcMethod(ithis, input, _thCxt);
   * }
   * </pre>
   * If the user doesn't determine that an instance is type of a given instance, a reference may refer
   * an instance with derived type. The method may be overridden there. 
   * Therefore the dynamic call should be done. But a normal C user doesn't may know this mechanism,
   * it may be a call of the method from manual written C-code. Therefore the dynamic call is implemented here.
   * Another way is to organize the dynamic call at user level, in the adequate kind as generated here.
   */
  public int processIfcMethod(int input)
  {
    return x1 + input + val;
  }

  /**Example for a method which is final but implements an interface method (the same is if it
   * overrides methods from a super class).This method implements {@link Ifc#processIfcMethod(int)}.
   * The method is declared in C as
   * <pre class="CCode">
   * METHOD_C float processIfcMethod_f_ImplIfc_Test(ObjectJc* ithis, float input, ThCxt* _thCxt);
   * </pre>
   * The middle part of name <code>_f_</code>, because another method which the same name exists. 
   * For this method no variant with a suffix <code>_F</code> exists, because this method is final itself.
   * No extra final method is necessary.
   * <br>
   * The implementation of the method is adequate to |{@link #processIfcMethod(int)}, see there.
   */
  final public float processIfcMethod(float input)
  {
    return 3.56F * input;
  }

  /**This method is declared in the interface {@link Ifc#processIfcMethod(int)} .
   */
  final public int anotherIfcmethod(int input)
  {
    return 34 + input;
  }

  @Override public String toString()
  { return "ImplIfc";
  }
  
  /**Example for a simple method, no implementation, no override-able.
   * This method executes something to test.
   * @return
   */
  final int testImplIfc()
  { int a = processIfcMethod(5);
    a += (int)processIfcMethod(6.28F);
    a += (int)testIfc2(23.4F);
    a += testOverrideAble(3.14F);
    return a; 
  }

  /**This method implements {@link Ifc2#testIfc2(float)}. @java2c=override-able.
   */
  final public float testIfc2(float input)
  {
    return input / 3.14F;
  }
  
  
  /**This method should demonstrate override-able methods for Java2C outside of interface-concepts. 
   * @java2c=override-able. */
  int testOverrideAble(float value)
  { return (int)value;
  }

  
  /**This method should demonstrate overridden methods of base classes. The method is overridden
   * in {@link TestAllConcepts}. 
   */
  int testOverridden(float value)
  { return (int)value;
  }

  
  /**This method demonstrates, how a concatenation of method calls works, if the method returns this itself.
   * The return value is the same as the calling instance. This is recognized by Java2C,
   * if the @ return contains the text <code>this</code> or if <code>@ java2c=return-this</code> is written.
   * @java2c=return-this. 
   * @param value any value
   * @return this
   */
  final ImplIfc returnThis(int value)
  {
    val = value;
    return this;
  }
  
  
  /**This method demonstrates, how a concatenation of method calls works, if the method returns this itself.
   * The return value is the same as the calling instance. This is recognized by Java2C,
   * if the @ return contains the text <code>this</code> or if <code>@ java2c=return-this</code> is written.
   * The difference to {@link #returnThis(int)} is: This method is able to override, therefore a dynamic call is necessary.
   * @java2c=return-this. 
   * @param value any value
   * @return this
   */
  ImplIfc returnThisOverrideable(int value)
  {
    val = value;
    return this;
  }
  
  
  /**This method demonstrates, how a concatenation of method calls works, if the method returns any other reference.
   * Concretely the return value is this, but the translator doesn't know that. 
   * @param value any value
   * @return any unknown instance, only concretely this.
   */
  AnyClass returnAnyInstanceOverrideable()
  {
    return data;
  }
  
  
  /**This method demonstrates, how a concatenation of method calls works, if the method returns any other reference.
   * Concretely the return value is this, but the translator doesn't know that. 
   * @param value any value
   * @return any unknown instance, only concretely this.
   */
  final AnyClass returnAnyInstance()
  {
   return data;
  }
  
  
  /**This methods implements both methods declared in Interface {@link Ifc#anotherIfcmethod(float)}
   * and {@link Ifc2#anotherIfcmethod(float)}. Only one implementation is necessary in Java 
   * and in the translated C too. The Implementation is defined in the Headerfile:
   * <pre class="CCode">
   * METHOD_C int32 anotherIfcMethod_f_ImplIfc_Test_F(ImplIfc_Test_s* ythis, int32 input, ThCxt* _thCxt);
   * METHOD_C int32 anotherIfcMethod_f_ImplIfc_Test(ImplIfc_Test_s* ythis, int32 input, ThCxt* _thCxt);
   * </pre>
   * The first form is the implementation method. The second form is the method, which realizes
   * the dynamically call because the method is override-able.
   * <br>
   * The Method table of this class contains the struct of method tables for the interfaces. 
   * Its definition in the headerfile is:
   * <pre class="CCode">
   * typedef struct Mtbl_ImplIfc_Test_t
   * { MtblHeadJc head;
   *   MT_testOverrideAble_ImplIfc_Test* testOverrideAble;
   *   Mtbl_ExpandableDataStruct_Test ExpandableDataStruct_Test;
   *   //Method table of interfaces:
   *   Mtbl_Ifc_Test Ifc_Test;
   *   Mtbl_Ifc2_Test Ifc2_Test;
   * } Mtbl_ImplIfc_Test;
   * </pre>
   * Both interfaces are contained. The definition of the instance of methode table for this class
   * in the C-File <code>ImplIfc_Test.c</code> contains the reference to the implementation method
   * at twice positions:
   * <pre class="CCode">
   * const struct { Mtbl_ImplIfc_Test mtbl; MtblHeadJc end; } mtblImplIfc_Test = {
    { { sign_Mtbl_ImplIfc_Test//J2C: Head of methodtable.
      , (struct Size_Mtbl_t*)((2 +2) * sizeof(void*)) //size. NOTE: all elements are standard-pointer-types.
      }
    , <b>anotherIfcMethod_ImplIfc_Test_F</b> //anotherIfcMethod
    , testOverrideAble_ImplIfc_Test_F //testOverrideAble
    , { { sign_Mtbl_ExpandableDataStruct_Test//J2C: Head of methodtable.
        , (struct Size_Mtbl_t*)((0 +2) * sizeof(void*)) //size. NOTE: all elements are standard-pointer-types.
        }
      , { { sign_Mtbl_ObjectJc//J2C: Head of methodtable.
          , (struct Size_Mtbl_t*)((5 +2) * sizeof(void*)) //size. NOTE: all elements are standard-pointer-types.
          }
        , clone_ObjectJc_F //clone
        , equals_ObjectJc_F //equals
        , finalize_ObjectJc_F //finalize
        , hashCode_ObjectJc_F //hashCode
        , toString_ImplIfc_Test //toString
        }
      }
    , { { sign_Mtbl_Ifc_Test//J2C: Head of methodtable.
        , (struct Size_Mtbl_t*)((3 +2) * sizeof(void*)) //size. NOTE: all elements are standard-pointer-types.
        }
      , processIfcMethod_i_ImplIfc_Test_F //processIfcMethod
      , anotherIfcmethod_i_ImplIfc_Test //anotherIfcmethod_i
      , { { sign_Mtbl_ObjectJc//J2C: Head of methodtable.
          , (struct Size_Mtbl_t*)((5 +2) * sizeof(void*)) //size. NOTE: all elements are standard-pointer-types.
          }
        , clone_ObjectJc_F //clone
        , equals_ObjectJc_F //equals
        , finalize_ObjectJc_F //finalize
        , hashCode_ObjectJc_F //hashCode
        , toString_ImplIfc_Test //toString
        }
      }
    , { { sign_Mtbl_Ifc2_Test//J2C: Head of methodtable.
        , (struct Size_Mtbl_t*)((3 +2) * sizeof(void*)) //size. NOTE: all elements are standard-pointer-types.
        }
      , processIfcMethod_f_ImplIfc_Test_F //processIfcMethod
      , testIfc2_f_ImplIfc_Test_F //testIfc2
      , <b>anotherIfcmethod_f_ImplIfc_Test_F</b> //anotherIfcmethod
      , { { sign_Mtbl_ObjectJc//J2C: Head of methodtable.
          , (struct Size_Mtbl_t*)((5 +2) * sizeof(void*)) //size. NOTE: all elements are standard-pointer-types.
          }
        , clone_ObjectJc_F //clone
        , equals_ObjectJc_F //equals
        , finalize_ObjectJc_F //finalize
        , hashCode_ObjectJc_F //hashCode
        , toString_ImplIfc_Test //toString
        }
      }
    }, { signEnd_Mtbl_ObjectJc, null } }; //Mtbl
   * </pre>
   * The reference to <code>anotherIfcmethod_i_ImplIfc_Test</code> is accordingly to {@link #anotherIfcmethod(int)}.
   * @java2c=override-able.
   */
  public float anotherIfcmethod(float input)
  {
    return 3.5F*input;
  }
  
}
