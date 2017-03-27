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

import org.vishia.bridgeC.AllocInBlock;
import org.vishia.msgDispatch.LogMessageFile;


/**This class contains some examples to demonstrate and test all features of Java2C.
 * The elements all explained respecting the Java2C-features which are tested there.
 * <br>
 * <br>
 * Most of the methods demonstrates a programming style, which is opportune also in C.
 * <br>
 * The class is represent in C wit a struct type definition:
 * <pre>
 * typedef struct TestAllConcepts_Test_t
 *  { 
 *    union { ObjectJc object; ImplIfc_Test_s super;} base; 
 *    int32 simpleInt; 
 *    ...etc.
 *  }
 *  </pre>
 *  The first element of struct is a union, which pools the super class, all interfaces and the ObjectJc-base.
 *  The most inner super class has ObjectJc as its first Element, the access to that base of all super classes
 *  can accessed immediately in this unit.  
 */
public class TestAllConcepts //extends ImplIfc
{
  /**A simple class variable. In C it's a member of the class struct:
   * <pre>
   * int32 simpleInt;
   * </pre>
   * The type <code>int32</code> is defined in os_types_def.h platform-depending, so that a 32-bit-integer
   * is used.
   * <br>
   * The initialization with <code>25</code> is done in the Constructor, see {@link #TestAllConcepts()}
   */
  private int simpleInt = 25;

  
  /**A total constant value, it is a #define kMsgBlockHeap 9000 in C. 
   * This is the base of message ident numbers for output Garbage collection activity. */
  static final int kMsgBlockHeap = 9000;
  
  static final int kIdxMsgOutputFile = 2;
  
  /**A final constant not-int-value, it is a const variable in C. */
  static final char kCharConst = (char)(3);
  
  /**A static, but not final variable. It should be intitalized outside the constructor,
   * but in C it is simple implemented like <code>int32 nrofInstances_TestAllConcepts = 5;</code>
   * It may be better, to write in a common <code>initializeStatics()</code>-Method, to regard more complex expressions.*/
  static int nrofInstances = 5;
  
  /**In Java it is a fix build instance at construction time. In C it is an embedded instance.  */
  final SimpleDataStruct embeddedData = new SimpleDataStruct();
  
  /**In Java it is a fix build instance at construction time. In C it is also an embedded instance. 
   * The expandable class isn't expand here because it is final. */
  final SimpleDataStruct embeddedDataNotEnpand = new SimpleDataStruct();
  
  /**In Java it is a fix build instance at construction time. In C it is also an embedded instance. 
   * The expandable class isn't expand here because it is final. */
  final ExpandedDataStruct embeddedDataExpand = new ExpandedDataStruct();

  final TestWaitNotify.WaitNotifyData testWaitNotifyData = new TestWaitNotify.WaitNotifyData(); 
  
  final TestThread testThread = new TestThread(testWaitNotifyData);
  
  final TestWaitNotify testWaitNotify = new TestWaitNotify(testWaitNotifyData);
  
  final StringBuffer stringBufferMain = new StringBuffer(20000);
  
  final TestString testString = new TestString(stringBufferMain);

  final TestStringFormatter testStringFormatter = new TestStringFormatter();
  
  /**A initialized array, it is embedded because it's final. */
  private final int[] intArray = new int[1000];
  
  /**A initialized array, but it is not embedded because it isn't final. */
  private int[] intArrayRef = new int[234];
  
  /**A simple reference to an array. @java2c=noGC. */
  private int[] intArrayRef2;
  
  /**A initialized array, it is embedded because it's final. @java2c=simpleArray.*/
  private final int[] intArraySimple = new int[1000];
  
  /**A initialized array, but it is not embedded because it isn't final.  @java2c=simpleArray.*/
  private int[] intArrayRefSimple = new int[234];
  
  static final int[] intArrayStaticConst = {10, 11, 12};
  
  /**@java2c = embeddedArrayElements. */
  SimpleDataStruct[] dataArrayRef;
  
  /**@java2c = embeddedArrayElements. */
  final SimpleDataStruct[] dataArrayEmbedded = new SimpleDataStruct[12];

  /**@java2c = embeddedArrayElements, simpleArray. */
  final SimpleDataStruct[] dataArraySimpleEmbedded = new SimpleDataStruct[12];
  
  /**@java2c = embeddedArrayElements. A array of embedded elements. */
  final SimpleDataStruct[] dataRefArrayEmbedded = new SimpleDataStruct[12];
  
  /**A array of references, but the head is embedded */
  final SimpleDataStruct[] dataRefArray = new SimpleDataStruct[12];
  
  /**A array of embedded elements, not final. */
  SimpleDataStruct[] dataAssociatedEmbeddedArray;
  
  /**@java2c = embeddedArrayElements. A array of references, not final. */
  SimpleDataStruct[] dataAssociatedRefArray;
  
  /**A initialized array, it is embedded because it's final. */
  //TODO private static final int[] intArrayStatic = new int[1000];
  
  /**A initialized array, but it is not embedded because it isn't final. */
  //TODO private static int[] intArrayRefStatic = new int[234];
  
  /**A initialized array, it is embedded because it's final. @java2c=simpleArray.*/
  //TODO private static final int[] simpleArrayStatic = new int[1000];
  
  /**A initialized array, but it is not embedded because it isn't final.  @java2c=simpleArray.*/
  //TODO private static int[] simpleArrayRefStatic = new int[234];
  
  /**An instance which implements a interface, using to test interface access. */
  private final ImplIfc implifc = new ImplIfc(100);
  
  private final Ifc2 ifc3 = new ImplIfc(123);
  
  /**A second instance which implements a interface, using to test interface access. */
  private ImplIfc implifc2;
  
  /**A class reference to an interface, set one time final. */
  private final Ifc ifc = implifc;
  
  /**A class reference to an interface, 
   * but it is set to only one instance: 
   * Therefore the call of methods at C-level uses the methods of the implemtation, it doesn't use
   * the dynamic call. 
   * @java2c=instanceType:"org.vishia.java2C.test.ImplIfc".
   * */
  private final Ifc ifcNonVirtual;
  
  
  
  /**A class reference to an interface, but set only if used. 
   * It may be referenced to one or another instance depend on fine algorithm. */
  private Ifc ifc2;
  
  private final ExtendsImpl extendsImpl= new ExtendsImpl();
  
  /**A initialized but not final reference. */
  private ImplIfc ifc4 = new ImplIfc(555);
  

  private final AnyClass anyRef;
  
  private final TestAnonymous testAnonymous = new TestAnonymous();
  
  /**The Constructor of this example. 
   * Before calling the constructor (ctor), the memory space is provided and initialized.
   * The initialization of the memory space contains:
   * <ul>
   * <li> Initialization of the ObjectJc-base. That base data are disposed at start of memory space always.
   *   It the class doesn't have a Object-base (special case of data classes possible, see {@link SimpleDataStruct}).
   *   than this initialization isn't happen.
   * <li>All other bytes of the whole data spaces are set to 0. Therefore all references are <code>null</code>-references,
   *   and all values are default set to 0.  
   * </ul>
   * This memory initialization is done calling a allocation in the Jc-Runtime environment. 
   * If the memory is a static one, the user should call <code>init_ObjectJc(object, sizeof(Instance), ident);</code>
   * manually at C-level. Static instances can be defined at C-level before starting initialization
   * in the C-typical wise.
   * <br>   
   * The constructor starts in C-file with:
   * <pre>
   * struct TestAllConcepts_Test_t* ctorO_TestAllConcepts_Test(ObjectJc* othis, ThCxt* _thCxt)
   * { TestAllConcepts_Test_s* ythis = (TestAllConcepts_Test_s*)othis;  //upcasting to the real class.
   *   STACKTRC_TENTRY("ctorO_TestAllConcepts_Test");
   *   checkConsistence_ObjectJc(othis, sizeof(TestAllConcepts_Test_s), null, _thCxt);  
   *   setReflection_ObjectJc(othis, &reflection_TestAllConcepts_Test_s, sizeof(TestAllConcepts_Test_s));  
   * </pre> 
   * <ul>
   * <li>The reference to the instance is given as a <code>ObjectJc*</code>-Reference. This is, 
   *   because the calling environment supplied only this base reference. This ObjectJc is initialized,
   *   especially the size of the memory area is set in <code>ObjectJc.objectIdentSize</code>.
   * <li>The return value is the same reference, but typed to the instance, to set the reference 
   *   to the initialized instance without casting.
   * <li><code>ThCxt* _thCxt</code> is the reference of the threadContext. It is necessary for exception handling.
   * <li>The first instruction is the casting to the real type. The casting is unchecked yet, but see next:
   * <li>The <code>STACKTRC_TENTRY(...)</code> registers the method stack level in thread context. 
   *   It is for exception handling with stack-trace dump. 
   * <li>The <code>checkConsistence_ObjectJc(...)</code> tests whether the memory area 
   *   referenced with <code>ythis</code> is initialized properly. Especially the size of the area is tested.
   *   This routine calls an Exception if the conditions are not true. The conditions should be met any time,
   *   elsewhere a user software error is happen. After this test the continuation of initialize is safety. 
   *   Without such test the software may be crash here causing of an external error.
   * <li><code>setReflection(...)</code> completes the ObjectJc-Data with the only one special information 
   *   regarding the implementation instance. The reflection include the method table too. It is a structured data
   *   defined at C-level in this compilation unit.    
   * </ul>
   * The next action is the call of constructor of the super class. That constructor calls first the constructor of its superclass
   *  and so on. The constructor of Object is not called, because the initialization of memory 
   *  before executing the constructor initializes the ObjectJc-base. There are contained some informations 
   *  of memory organization. 
   *  */
  public TestAllConcepts()
  { implifc2 = new ImplIfc(200);
    ifcNonVirtual = implifc2;          //use the interface reference.
    anyRef = new AnyClass(extendsImpl);
  }
  
  
  public int access(int x)
  {
    intArray[3] = simpleInt;
    intArrayRef[2] = kMsgBlockHeap;
    
    embeddedData.db = 3.14F;
    embeddedDataExpand.xb = (short)x;
    embeddedDataNotEnpand.db = 2.7;
    
    
    
    simpleInt = intArrayStaticConst[1];
    intArrayRef2 = intArray;        //cast from a special embedded array type to int_Y
    intArrayRef2 = intArrayRef;
    //TODO intArray[2] = intArrayStatic[4];
    
    { /**Test get new array with embedded array elements. */
      int maxQueue = 5;
      /**@java2c = embeddedArrayElements. */
      SimpleDataStruct[] entries = new SimpleDataStruct[maxQueue];
      for(int idxEntry = 0; idxEntry < entries.length; idxEntry++)
      { entries[idxEntry] = new SimpleDataStruct();
      }
      dataArrayRef = entries;
    }
    return simpleInt;
  }

  
  /**This method shows some calls of interface methods, see {@link org.vishia.java2C.Docu.SuperClassesAndInterfaces#callingOverrideableMethods()}.
   * @return
   */
  private int checkSomeDynamicCalls()
  { int a;
    Ifc ifc3 = this.implifc;
    ifc3.processIfcMethod(5);
    /**Because the instance implifc is embedded and their type is known hence, 
     * the method is performed non-dynamically, but direct. */
    a = implifc.processIfcMethod(456);
    /**A stack instance, data in stack. @java2c=stackInstance. */
    ImplIfc stackInstance = new ImplIfc(45);
    /**Because the stackInstance is embedded and their type is known hence, 
     * the method is performed non-dynamically, but direct. */
    a += stackInstance.processIfcMethod(678);
    
    /**call of own overrideable method. */
    //TODO, fails yet: processIfcMethod(4);
    /**call of a method with a class reference: */
    ifc.processIfcMethod(56);
    return a;
  }
  
  
  
  
  /**Example to show concatenated calls in a simple variant. Concatenated calls are a well used
   * construct in Java, whereby there are two suggestions to do so:
   * <ol>
   * <li>References are needed, there are got calling a method. 
   *   <br><br>
   *   In C such references are known too,
   *   but mostly they are got immediate writing <code>myInstance.ref->refOfRef->variable</code>.
   *   It is the adequate construct. The usage of methods instead the immediately getting of the reference
   *   is the concept of ObjectOrientation and Encapsulation: The access to the class data should be done
   *   always using get-methods. The advantage is: Any access check can be implemented in the get-method.
   *   The programming can be implemented more safety. But Lastly it's possible to present a get-method 
   *   with a primitive macro in C. Than no extra method call is produced, the machine code is the same
   *   like the immediately access.
   *   <br><br>
   *   Sum up: The usage of get-methods instead immediately access to references is recommended.
   *   <br><br>
   *   This variant of usage is shown in a statement line
   *   <pre class="Java">
   *   int a = 234 + implifc.returnAnyInstance().returnRef().returnAnyInstance().addValue(24) + 27;
   *   </pre>    
   * <li>calls to the same instance written in a nicely kind, which is associated with a concatenation of data.
   *   A typically example in Java is the here shown concatenation of append(...) for a StringBuffer,
   *   shown in a second statement line.
   *   <pre class="Java">
   *   sbufferFix.append("Value=").append(simpleInt).append(" miles");
   *   </pre>    
   *   In comparison with C++, the usage of an C++-expression <code>cout << "Value=" << simpleInt << " miles"</code>
   *   phrases the same. The shift operator should phrase a association to "shift in pipe",
   *   but at syntactical level it is an concatenation like shown above. 
   *   <br><br>
   * </ol>
   * Both application types of concatenation calls should be translated to C in a well readable form.
   * The translated result of both lines is:
   * <pre>
    AnyClass_Test_s* _tempRef1; ImplIfcTest_s* _tempRef2; AnyClass_Test_s* _tempRef3; 
    a = 234 + 
      ( _tempRef1= returnAnyInstance_ImplIfcTest(& (ythis->implifc), _thCxt)
      , _tempRef2= returnRef_AnyClass_Test(_tempRef1, _thCxt)
      , _tempRef3= returnAnyInstance_ImplIfcTest(_tempRef2, _thCxt)
      , addValue_AnyClass_Test(_tempRef3, 24, _thCxt)
      ) + 27;
    
      ( append_s_StringBufferJc(& (ythis->sbufferFix.sb), s0_StringJc("Value="), _thCxt)
      , append_i_StringBufferJc(& (ythis->sbufferFix.sb), ythis->simpleInt, _thCxt)
      , append_s_StringBufferJc(& (ythis->sbufferFix.sb), s0_StringJc(" miles"), _thCxt)
      );
   * </pre>
   * In the first concatenation example: The references from each call are parked in temporary variables.
   * That is because the result from the first call is the first parameter of the next call.
   * If temporary variables are not used, the first call may be placed as first parameter of the second call etc.
   * This construct may be lesser able to read, it is a nesting of calls. There is another problem thereby:
   * If method tables are necessary for dynamic calls, the reference is needed twice. Therefore a explicitly
   * temporary reference is proper.
   * <br><br>
   * In the second concatenation example: The references are the same in any call because the methods contain
   * <pre class="Java">
   *   return this;
   * </pre>
   * in its implementation. That is labeled at the methods. The translator knows that therefore and can optimize: 
   * All methods use the same reference. The concatenation in Java is translated to a simple order of routine call.
   * <br><br>
   * In both examples a comma-separated expression is used. It is a non-often used but possible construct in C.
   * The first examples shows the necessity of that construct: The concatenation is a part of an comprehensive expression,
   * and the calling of the concatenated routines should be done in the correct order. In the second example 
   * the comma-separation may be unnecessary, but it's possible and don't may be confusingly.
   * The one-line-expression in Java is broken in several lines at position of the concatenation-representing comma, 
   * otherwise the line in C would be to long. This form is better to read.
   *       
   * @return a value.
   */
  private final int checkConcatenationSimple()
  {
    int a = 234 + implifc.returnAnyInstance().returnRef().returnAnyInstance().addValue(24) + 27;
    //sbufferFix.append("Value=").append(simpleInt).append(" miles");
    
    { /**This line is a test whether a mix of return this and return association works. See Java- and C-code.*/
      a +=implifc.returnAnyInstance().returnThis(56).returnRef().testImplIfc();
    }
    return a;
  }
  
  
  /**Example to show concatenated calls of override-able methods. There are some kinds.
   * The first line in Java
   * <pre class="Java">
   * a = 234 + implifc2.returnAnyInstanceOverrideable().returnRefOverrideable().returnAnyInstanceOverrideable().addValueOverrideable(24) + 27;
   * </pre>
   * is related to the first line in {@link #checkConcatenationSimple()}, but the difference is: 
   * all methods are dynamically called. It is a simple non-prepared dynamic call, 
   * therefore the code in C may be look catastrophic for real time. 
   * The method table of the references have to be got. It is a call of <code>getMtbl_ObjectJc(...)</code>
   * with the given reference as first parameter. But the calculation time is not so expensive, 
   * it may be a few nanoseconds if the derivation is not deep, and some more nanoseconds if it is deeper,
   * but not more. Only for hard real-time it is non-opportune. The lines in C are:  
   * <pre>
    AnyClass_Test_s* _tempRef1; ImplIfcTest_s* _tempRef2; AnyClass_Test_s* _tempRef3; 
    a = 234 + 
      ( _tempRef1= ((Mtbl_ImplIfcTest const*)getMtbl_ObjectJc(&(REFJc(ythis->implifc2))->base.object, sign_Mtbl_ImplIfcTest) )->returnAnyInstanceOverrideable(REFJc(ythis->implifc2), _thCxt)
      , _tempRef2= ((Mtbl_AnyClass_Test const*)getMtbl_ObjectJc(&(_tempRef1)->base.object, sign_Mtbl_AnyClass_Test) )->returnRefOverrideable(_tempRef1, _thCxt)
      , _tempRef3= ((Mtbl_ImplIfcTest const*)getMtbl_ObjectJc(&(_tempRef2)->base.object, sign_Mtbl_ImplIfcTest) )->returnAnyInstanceOverrideable(_tempRef2, _thCxt)
      , ((Mtbl_AnyClass_Test const*)getMtbl_ObjectJc(&(_tempRef3)->base.object, sign_Mtbl_AnyClass_Test) )->addValueOverrideable(_tempRef3, 24, _thCxt)
      ) + 27;
   * </pre>
   * The second line in Java
   * <pre class="Java">
   * a += this.returnThisOverrideable_Test(34).returnThisOverrideable_Test(45).access(56);
   * </pre>
   * shows a concatenated call of overrideable methods, which returns this and uses this as first reference.
   * Because this is supported as method-table-reference if it is need, with one access to <code>getMtbl_ObjectJc(...)</code>
   * in the method, and the <code>mthis</code> is used for all calls because the <code>return this</code>-property
   * of the called method is known, this is a optimized access. In C it is:
   * <pre>
      Mtbl_TestAllConcepts_Test const* mtthis = (Mtbl_TestAllConcepts_Test const*)getMtbl_ObjectJc(&ythis->base.object, sign_Mtbl_TestAllConcepts_Test);
      ...
      a += 
      ( mtthis->returnThisOverrideable_Test(ythis, 34)
      , mtthis->returnThisOverrideable_Test(ythis, 45)
      , mtthis->access_i(ythis, 56, _thCxt)
      );
   * </pre>
   * whereby the first line is generated only one time in the method.
   * <br>
   * <br>
   * The next line in Java shows a call of dynamic methods of referenced data. The methods returns <code>this</code>,
   * why wet even the reference is the same for all three calls. The reference is copied into a stack variable,
   * which is labeled with <code>@ java2c=dynamic-call</code>. Therefore in C a so named "method-table-reference" is built.
   * This reference contains the pointer to the method table beside the pointer of data. The pointer to the method table
   * is got calling <code>getMtbl_ObjectJc(...)</code> only on setting the reference. 
   * It's a part of the macro <code>SETMTBJc(...)</code>. So the C-code is optimized. The Java-lines are:
   * <pre class="Java">
   * / **This reference is build in stack because it contains the method-table-reference too: @ java2c=dynamic-call. * /
   * AnyClass anyRef2 = anyRef;
   * anyRef2.returnThisOverrideable(45).returnThisOverrideable(234).addValueOverrideable(67);
   * </pre>
   * The translated C-code is:    
   * <pre>
    SETMTBJc(anyRef2, REFJc(ythis->anyRef), AnyClass_Test);
    a += 
      ( anyRef2.mtbl->returnThisOverrideable( (anyRef2.ref), 45, _thCxt)
      , anyRef2.mtbl->returnThisOverrideable( (anyRef2.ref), 234, _thCxt)
      , anyRef2.mtbl->addValueOverrideable( (anyRef2.ref), 67, _thCxt)
      );
   * </pre>
   * In opposite, if the special stack-local method-table-reference is not built, the C-code isn't optimize,
   * but able to run (translated from next Java line using <code>anyRef</code> as class reference):
   * <pre>
    a += 
      ( ((Mtbl_AnyClass_Test const*)getMtbl_ObjectJc(&(REFJc(ythis->anyRef))->base.object, sign_Mtbl_AnyClass_Test) )->returnThisOverrideable(REFJc(ythis->anyRef), 345, _thCxt)
      , ((Mtbl_AnyClass_Test const*)getMtbl_ObjectJc(&(REFJc(ythis->anyRef))->base.object, sign_Mtbl_AnyClass_Test) )->returnThisOverrideable(REFJc(ythis->anyRef), 3234, _thCxt)
      , ((Mtbl_AnyClass_Test const*)getMtbl_ObjectJc(&(REFJc(ythis->anyRef))->base.object, sign_Mtbl_AnyClass_Test) )->addValueOverrideable(REFJc(ythis->anyRef), 367, _thCxt)
      );
   * </pre> 
   * shows a call of a dynamic-linked method of a other instance, whereby the <code>return this</code>-property
   * is known too, and the reference is prepared as a locally method-table-reference:
   * <br><br>
   * The conclusion:
   * <ul>
   * <li>If dynamic call is necessary, a optimization is possible.
   * <li>Calling of methods of the own class, which return this, uses the one-time-prepared <code>mthis</code>.
   * <li>Calling of methods of a referenced instance: The reference should be copied in a stack-local variable
   *   which is labeled with <code>@ java2c=dynamic-call</code> or it is an interface-reference.
   *   If the methods returns this, and they are labeled with <code>@ java2c=return-this</code>,
   *   the access is optimized.
   * <li>If no such optimization is done, the method-table-pointer is got some more times, but the call is correct.
   * </ul>  
   */
  private int checkConcatenationDynamicCall()
  {
    /**Checks how return-this-methods of the own class can concatenated: */
    int a;
    a = 234 + implifc2.returnAnyInstanceOverrideable().returnRefOverrideable().returnAnyInstanceOverrideable().addValueOverrideable(24) + 27;
    a += this.returnThisOverrideable_Test(34).returnThisOverrideable_Test(45).access(56);
    
    /**This reference is build in stack because it contains the method-table-reference too: @java2c=dynamic-call. */
    AnyClass anyRef2 = anyRef;
    a += anyRef2.returnThisOverrideable(45).returnThisOverrideable(234).addValueOverrideable(67);
    
    /**Oposite: don't use the stacl-local reference, but the class variable, it isn't optimized in C, but able to run: */
    a += anyRef.returnThisOverrideable(345).returnThisOverrideable(3234).addValueOverrideable(367);
    
    return a;
  }
  
  
  
  
  /**Example to test dynamic and static calls to methods, which are methods of the base class.
   * The calling of base methods requires the appropriate types of pointers of this. 
   * @return
   */
  private int checkConcatenationDynamicCallToBaseMethods()
  { int a =5;
    a =implifc.returnThisOverrideable(34).returnThisOverrideable(56).processIfcMethod(44);
    
    return a;
  }
  
  /**Example to show concatenated calls with the special kind: The methods returns this itself.
   * @deprecated
   * In this case the calling instance is the same as the returned instance, which is the calling instance
   * for the next concatenated call.
   * In Java it is written:
   * <pre class="Java">
   * a =implifc.returnThisA(34).returnThisA(56).processIfcMethod(44);
   * </pre>
   * Because the reference <code>implifc</code> is a embedded instance and the type of it is fix, 
   * (the same is for stack instances or @ <code>java2c=instanceType:"Type"</code>-designated references), 
   * all method-calls are execute non-dynamic. 
   * Because a <code>@ java2c=return-this</code> is designated to the called method, the Java2C-translator
   * accept that the result instance is the same as calling instance, but because that is type-fix,
   * the concatenated call is execute non-dynamic too.  
   * <pre>  
   * a = 
   *   ( returnThisA_ImplIfcTest_F(& (ythis->implifc), 34, _thCxt)
   *   , returnThisA_ImplIfcTest_F(& (ythis->implifc), 56, _thCxt)
   *   , processIfcMethod_i_ImplIfcTest_F(&((& ((ythis->implifc).base.IfcToTest))->base.object), 44, _thCxt)
   *   );
   * </pre>
   * The instance to call the method is <code>implifc</code> always, it's the first parameter of any called method.
   * That is because the methods are labeled to <code>@ java2c=return-this</code>. The concatenation in Java is presented
   * as a comma-separated expression, see {@link #checkConcatenationSimple()}. 
   * But in this case no temporary references are needed, it is more simple. Lastly the concatenated methods in Java
   * are only a simplifying of writing of the method calls with the same reference.
   * <br><br>
   * Note: the expression <code>&((& ((ythis->implifc).base.IfcToTest))->base.object)</code> is needed 
   * because the <code>processIfcMethod_i_ImplIfcTest_F</code> is a method of the interface type.   
   */
  private int checkConcatCallReturnThisTypefixNonVirtual()
  { int a;
    ///**A stack instance, data in stack. @java2c=stackInstance. */
    //ImplIfc stackInstance = new ImplIfc(45);
    a =implifc.returnThisOverrideable(34).returnThisOverrideable(56).processIfcMethod(44);
    return a;
  }
  
  
  
  private int checkConcatCallReturnAnything()
  { int a=0;
    ///**A stack instance, data in stack. @java2c=stackInstance. */
    //ImplIfc stackInstance = new ImplIfc(45);
    //a =implifc.returnAnyInstanceOverrideable(34).returnAnyInstanceOverrideable(56).processIfcMethod(44);
    return a;
  }
  
  
  
  
  
 
  /**This method helps to test concatenations with return-this, but override-able.
   * The return value is the same as the calling instance. This is recognized by Java2C,
   * if <code>@ java2c=return-this</code> is written.
   * <br>
   * This is also a example for non-using of STACKTRC, it is not necessary here.
   * @java2c=return-this, stacktrace:no. 
   * @param value any value
   * @return this
   */
  public TestAllConcepts returnThisOverrideable_Test(int value)
  {
    simpleInt += value;
    return this;
  }

  
  
  /**Example for a non-dynamic call of an interface referenced method. 
   * Because the reference is marked with the instanceType, this information is used to produce 
   * a normal C-function call of the known instance method. The additional designation of the reference
   * helps to economize calculation time, if the dynamic call isn't necessary really.
   * <br>
   * This method is used as an example for using STACKTRC, but without external parameter.
   * @java2c=stacktrace:no-param.
   * 
   */
  void checkNonVirtual()
  {
    /**This method should not use a dynamic call in C, because the reference is marked with the instanceType. */
    ifcNonVirtual.processIfcMethod(23);
  }
  
  
  
  /**Check whether Object.equals() will be overridden.
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object cmp)
  { return true;
  }
  
  
  /**This method doesn't override Object.equals. */
  public boolean equals(String cmp)
  { return true;
  }
  
  
  /**A method with same name but other parameter types.
   * @param x
   */
  public void access(float x)
  {
  }
  
  
  int testAccessIfc()
  { int val = 0;
    Ifc ifc22 = implifc;         //stacklocal reference to type Ifc
    if(ifc22 !=null){            //test usage ifc22.ref
	    for(int ii=0; ii<1000; ii++)
	    { val += ifc22.processIfcMethod(5);   //use it for dynamic call, in C too.
	    }
    }  
    return val;
  }
  
  
  int testAccessIfcMtbl()
  { int val = ifc.processIfcMethod(5);
    return val;
  }
  
  
  int testAccessIfcMtbl2(boolean bTest)
  { int val;
    if(bTest){
      ifc2 = implifc2;
    } else {
      ifc2 = implifc;
    }
    Ifc ifcCall = ifc2;
    val = ifcCall.processIfcMethod(6);
    val += ifc2.processIfcMethod(5);
    return val;
  }
  
  
  /**Example to check how an dynamic call of own methods is implemented in C. 
   * The internal non-final method {@link #testAccessIfcMtbl()} is called twice:
   * 
   */
  final void testInternalDynCall()
  { boolean cond = true;
    testAccessIfcMtbl();
    if(cond)
    { testAccessIfcMtbl();
    }
  }
  
  
  /*
  int testNewDerived()
  {
  	/**Any instance-reference can be initialized with an derived class. * /
  	SimpleClass myClass = new SimpleClass(10)
  	{
  		@Override int addValue(int value)
  		{
  			x1 += 2*value;
  			return x1;
  		}
  	};
  	return myClass.addValue(3);
  }
  */
  
  /**Example for a special finalize method body. finalize is called by garbage collection 
   * if the object is deleted finally. For Java2C in C all enhanced references will be deleted than.
   * The finalize method is part of override-able methods of Object. The method will be placed 
   * in the method table.
   * <br>
   * The C-code of this method has the following form:
   * <pre>
   * TODO copy from C
   * </pre>
   */
  @Override public void finalize()
  {
    System.out.println("finalize");
  }
  
  
  /**
   * 
   */
  private void main()
  { int ret = 0;
    TestAllConcepts main = this;
    TestgarbageCollector testGc = new TestgarbageCollector();
    testAnonymous.test();
    testGc.test();
    main.testInternalDynCall();
    main.checkConcatenationSimple();
    main.checkConcatenationDynamicCall();
    //main.checkConcatCallReturnThisTypefixNonVirtual();
    //main.checkConcatCallReturnThisVirtual();
    //main.checkConcatCallThisVirtual();
    main.access(234);
    main.testString.testStringProcessing();
    main.testStringFormatter.test();
    /**Method from a super class which is only defined there. but called final. */
    extendsImpl.testImplIfc();
    /**Method from a super class which implements an interface method, but called final. */
    //TODO dynCall of baseclass method faulty. processIfcMethod(234);
    ret += main.checkSomeDynamicCalls();
    ret += main.testAccessIfc();
    ret += main.testAccessIfcMtbl();
    ret += main.testAccessIfcMtbl2(true);
 
    TestContainer testContainer = new TestContainer();
    testContainer.test();
    
    /**Start a two threads: */
    testWaitNotify.start();
    
    testThread.start();
    testThread.otherThreadRoutine();
    testWaitNotify.shouldRun = false;
    
    try{ Thread.sleep(2000);}            //wait for finishing notify-thread. 
    catch (InterruptedException e) {}
    System.out.println("main finished.");
  }
  
  
  public static void main(String[] args)
  { //MsgDispatcher msgDispatcher = new MsgDispatcher(10,100,100,8);
    LogMessageFile msgOutputFile = new LogMessageFile("log/gc$MMMdd-hhmmssS$.log", 0, 0, null,null,null); //msgDispatcher.getSharedFreeEntries());
    //msgDispatcher.setOutputRoutine(kIdxMsgOutputFile, "log", true, msgOutputFile);
    //msgDispatcher.setOutputRange(kMsgBlockHeap, kMsgBlockHeap+99, 1<<kIdxMsgOutputFile, MsgDispatcher.mSet, 4);
    TestAllConcepts main1 = new TestAllConcepts();
    AllocInBlock.setRunModeAll();
    AllocInBlock.setLogMessageOutput(msgOutputFile, kMsgBlockHeap);
    //main1.processIfcMethod(234);
    main1.main();
    System.gc();
    main1.finalize();     //it isn't need anymore, but the gc hasn't freed it because it is in use still.
    main1 = null;
    System.gc();
    msgOutputFile.close();
  }
  
  
}
