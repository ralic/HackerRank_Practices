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
 * 4) But the LPGL ist not appropriate for a whole software product,
 *    if this source is only a part of them. It means, the user
 *    must publish this part of source,
 *    but don't need to publish the whole source of the own product.
 * 5) You can study and modify (improve) this source
 *    for own using or for redistribution, but you have to license the
 *    modified sources likewise under this LGPL Lesser General Public License.
 *    You mustn't delete this Copyright/Copyleft inscription in this source file.
 *
 * @author JcHartmut: hartmut.schorrig@vishia.de
 * @version 2008-04-06  (year-month-day)
 * list of changes:
 * 2008-04-06 JcHartmut: some correction
 * 2008-03-15 JcHartmut: creation
 *
 ****************************************************************************/
package org.vishia.java2C;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.vishia.java2C.JavaSources.ClassDataOrJavaSrcFile;
import org.vishia.mainCmd.Report;
import org.vishia.msgDispatch.LogMessage;
import org.vishia.zbnf.ZbnfParseResultItem;



/**This class represents the content of a java class while Translation.
 * The instance is created and filled while executing the first pass of translation,
 * that is the detecting of the class elements and generating the C-header. 
 * It is used for the second pass, the generation of C-file.
 * <br>
 * Chars are used to identify the kind of a element or type:
 * <table>
 * <tr><td>C</td><td>a class type identifier</td></tr>
 * <tr><td>I</td><td>an interface type identifier</td></tr>
 * <tr><td>B</td><td>a basic scalar type</td></tr>
 * </table>
 */
class ClassData implements JavaSources.ClassDataOrJavaSrcFile
{
  /**The full name, like produced in C. If it is an inner class, outer__inner. */
  public final String sClassNameC;
  
  /**The full name in java, dot to separate outer.inner class names. */
  public final String sClassNameJava;
  
  /**Short form of type-name for argument sensitive C-routine names. */
  public final String sArgSensitiveName;
  
  public final String sPackage;
  
  public final String sClassNameJavaFullqualified;
  
  /**Char which identifies a value in a variable argument list. The following chars are used:
   * <ul>
   * <li><code>Z</code>: boolean
   * <li><code>B</code>: byte, uint8
   * <li><code>C</code>: char
   * <li><code>L</code>: Object
   * <li><code>D</code>: double
   * <li><code>F</code>: float
   * <li><code>I</code>: int
   * <li><code>J</code>: long
   * <li><code>S</code>: short
   * <li><code>z</code>: zero terminated string literal
   * <li><code>s</code>: StringJc
   * <li><code>b</code>: StringBufferJc
   * <li><code>x</code>: ExceptionJc
   * <li><code>t</code>: OS_TimeStamp
   * <li><code>.</code>: unknown
   * </ul>
   * The upper-case-chars are the same, which are used in Java language 
   * to identify Array types returned by <code>java.lang.Class.getName()</code>.
   * 
   */
  public final char cVaArgIdent;
  
  /**The file were the class is defined in C. Without extension .h.
   * 
   */
  public final String sFileName;
  
  /**The context of generating the whole file, where the class is defined. */
  final GenerateFile fileContainsClass;
  
  
  /**The source, from where the ClassData are created. It is used as a hint 
   * in exception message, if any method, field or etc. is missed. */
  public final String sSourceOfClassData;
  
  
  /**Set if <code>java2c = staticInstance</code> is found in description of the class.
   * A class is a static instance if the class should not be created dynamically using garbage collection,
   * but only in the construction path of the application.
   * In such classes all final references to final classes (may not be staticInstance itself)
   * are simple pointers in the C Conversion. Otherwise enhanced references are to be used.
   */
  public final boolean isStaticInstanciated;
  
  /**Information String given in constructor.
   */
  private final String sInfo;

  /**Set if the class is final. */
  public final boolean isFinal;

  /**Set if the class is a non-static inner class (only inner classes can be static or non-static). */
  public final boolean isNonStaticInner;

  /**Set if the class or interface has only get-methods, so that it is able to declare as const* in C. */ 
  public final boolean isConstant;
  
  /**Mode of anynymous class:
   * <ul><li>C: inner class at class level
   * <li>F: class at file level
   * <li>P: primary class created in CRuntimeJavalike
   * <li>Y: anonymous class
   * <li>b: In a statement block.
   * <li>.: Non anonymous class
   * </ul>
   */
  public final char creationMode;
  
  private final boolean basedOnObject;
  
  /**Set if instances should only used embedded. 
   * It is a property able to set to classes with less elements. */
  public final boolean bEmbedded;
  
  /**Set if it is an interface. */
  private final boolean bInterface;
  
  /**Set if it is an abstract class. */
  public final boolean isAbstract; 
  
  /**Set true if any enhanced reference type is need. */
  private boolean bFinalizeNeed;
  
  /**For second pass, the parse result. NOTE: Not final because it should be removed after running second path. */
  private ZbnfParseResultItem zbnfClass;

  /**True if the class isn't known in Java2C context. It is a external used class.*/
	private final boolean bExternal;

	/**For finalize method body, null if no finalize method is defined for java. */
  private ZbnfParseResultItem zbnfFinalizeMethod;
  
  /**Some bits set if the array form of the type is need anywhere. The bits are coded in the set- and get-methods.
   * See {@link #need_Y()}, {@link #isneed_Y()} etc.
   */
  private short bitArrayNeed;
  
  /**All known identifiers of this class. Also fields of the outer classes and base classes.
   * To search any known field identifier in class context, only an access to this instance should be necessary.
   */
  public final LocalIdents classLevelIdents;
  
  /**All identifiers of only this class. Used for reflection generation and the stc-File.
   */
  public final List<FieldData> classLevelIdentsOwn;
  
  /**All inner classes of only this class. 
   * Used for completion of inner classes with outer identifiers. */
  private List<ClassData> innerClasses;
  
  
  /**true if it has fields. Used for reflection generation. */
  private boolean hasFields = false;
  
  /**All identifiers of only this class.
   */
  public final Map<String, ClassData> classLevelUsageTypes;
  
  /**Simple Data class to hold Strings and modes for generation casting.
   * The class contains a  pre- and a suffix String and the access modes. 
   * They will be disposed around the input expression to build a expression with cast.
   * The class is used to check and execute possible casts. Some casts where done both in Java
   * and in C implicitly, but the Java2C-translator should known it. Than pre- and suffixes are empty. 
   */ 
  public static class CastInfo
  { 
  	/**5 Levels of cast-ability. equal: same types. automatic: cast from compiler, forex short to int.*/
  	public final static int kCastEqual=4, kCastAutomatic=3, kCastAble=2, kCastCommon=1, kCastNo=0;
  	
  	
  	/**C-Code before and after the expression, which is to cast. 
     * At example <code>(int32(</code> and <code>)</code> to cast from float or double. */
    final String pre, post;
    
    /**The provided access mode of the result. */
    final char modeAccessDst;
    
    /**The expected access mode of the input. */
    final char modeAccessSrc;
    
    final ClassData castType;
    
    /**Constructs the data. */
    CastInfo(ClassData castType, String pre, String post, char modeAccessDst, char modeAccessSrc)
    { this.pre = pre; this.post = post; 
      this.modeAccessDst = modeAccessDst; 
      this.modeAccessSrc = modeAccessSrc; 
      this.castType = castType;
    }
  }
  
  /**Types from which a cast for initial values is possible, and its cast rule. 
   * This list is build using {@link #addCastInitializer(ClassData srcType, String cast)}
   * and used in {@link #testAndcast(ClassData srcType, String value, char intension)}.
   */
  private TreeMap<String, CastInfo> castableInitializerFromType;
  
  /**Types from which a cast is possible, and its cast rule. 
   * This list is build using {@link #addCastFromType(ClassData srcType, String cast, String modes)}
   * and at least used in {@link FieldData#testAndcast(CCodeData)}. 
   * This class provides the info via {@link #getCastInfoFromType(String)}
   * The key is the C-typename.
   */
  private TreeMap<String, CastInfo> castableFromType;
  
  /**Types to which a cast is possible, and its cast rule. 
   * This list is build using {@link #addCastToType(ClassData srcType, String cast, String modes)}
   * and at least used in {@link FieldData#testAndcast(CCodeData)}. 
   * This class provides the info via {@link #getCastInfoToType(String)}
   * The key is the C-typename.
   */
  private TreeMap<String, CastInfo> castableToType;
  
  
  /**Holds Informations about superclasses and interfaces.
   * <ul>
   * <li>Reference to the ClassData of the given level.
   * <li>Reference to InheritanceInfo of the superclass.
   * <li>List of References to InheritanceInfo of all Interfaces of this level.
   * <li>All Methods of this level, which are able to override (virtual methods).
   * </ul>
   */
  static class InheritanceInfo
  { 
    /**The ClassData associated to this node of inheritance information. The toplevel node of
     * {@link ClassData#inheritanceInfo} references the declaring class itself, but the 
     * {@link #superClass}-referenced instance references the {@link ClassData} of the superclass etc.
     * 
     */
    final ClassData classData;
    
    /**The Superclass of the current {@link #classData}. It isn't a instance of {@link ClassData}, 
     * but an instance of this classtype, because the names of the overridden methods are different
     * from the Methods named in the parallel list in {@link ClassData#inheritanceInfo} of the superclass.*/
    InheritanceInfo superInheritance;
    
    /**All Interfaces of the current {@link #classData}. */
    //private Map<String, InheritanceInfo> ifcInheritance;
    private InheritanceInfo[] ifcInheritance;
    
    /**Array of all methods of this inheritance level of ClassData with methods, 
     * which are able to override and which are overridden. Each element refers the method in its
     * originally representation (associated to the superclass or interface) and its overriding name
     * in this class. The overriding name is the name, which is set to the Mtbl_Type in C.
     */
    private MethodOverrideable[] methodTable;
    

    
    /**Creates a new instance for a new creating class. This constructor is called inside the Constructor of 
     * {@link ClassData#ClassData(GenerateFile, String, String, String, String, String, char, ClassData, ClassData, ClassData[], ZbnfParseResultItem, boolean, boolean, boolean, LocalIdents)}).
     * The constructor creates the tree of some {@link ClassData.InheritanceInfo} and their instances 
     * of {@link ClassData.MethodOverrideable}. The picture shows that:
     * <img src="../../../../Java2C/img/ctor_InheritanceInfo_omdJava2C.png" />
     * The tree of {@link ClassData.MethodOverrideable} are determined by the source tree of the superclass.
     * To create the inner InheritanceInfo-instances, the {@link ClassData.InheritanceInfo#InheritanceInfo(ClassData, ClassData, ClassData[])}-constructor
     * is used. This constructor uses the given source-InheritanceInfo to get the next deeper level. 
     * The InheritanceInfo- tree of the superClass contains the correct names of the implementation routines, 
     * attribute {@link ClassData.MethodOverrideable#sNameOverriding}. But the parallel existing instance 
     * referenced from any own class immediately, showing in green but red crossed out doesn't contain 
     * the correct implementation names. It contain the implementation name of this class-level. 
     * The class is a deeper superclass of the superclass, the methods may be overridden long ago.    
     * 
     * @param classDataP The creating class. A backward aggregation is set: {@link #classData}.
     * @param superClassP The superclass of the creating class. The superclass is referenced only here, 
     *   not in the {@link ClassData}. The {@link ClassData#inheritanceInfo} of the superclass is the source
     *   for all inheritance infos of this class. The picture show the referenced instance of this parameter 
     *   in yellow on top-right, and the source of all inheritance infos referenced from there in cyan color.
     * @param ifcClassDataP The interfaces of the creating class. 
     *   The interfaces are referenced only here, not in the {@link ClassData}.
     *   The all-inheritance-infos of interface tree are got from here, see picture in bottom area.
     */
    private InheritanceInfo(ClassData classDataP, ClassData superClassP, ClassData[] ifcClassDataP)
    { this.classData = classDataP; 
      if(classDataP.sClassNameJava.equals("ImplIfc"))
        stop();
      if(classDataP.sClassNameJava.equals("TestAllConcepts"))
        stop();
      if(superClassP != null)
      { InheritanceInfo srcSuperInheritance = superClassP.inheritanceInfo;
        this.superInheritance = new InheritanceInfo(superClassP, srcSuperInheritance);  //recursively call.
      }
      if(ifcClassDataP != null)
      { this.ifcInheritance = new InheritanceInfo[ifcClassDataP.length];
        //this.ifcInheritance = new TreeMap<String, InheritanceInfo>();
        for(int idxIfc = 0; idxIfc< ifcClassDataP.length; idxIfc++){
          ClassData interfaceClass = ifcClassDataP[idxIfc];
        //for(ClassData interfaceClass: ifcClassDataP){
          InheritanceInfo ifcInfo = new InheritanceInfo(interfaceClass, interfaceClass.inheritanceInfo); //recursively call.
          this.ifcInheritance[idxIfc] = ifcInfo; 
          //this.ifcInheritance.put(interfaceClass.getClassNameJava(), ifcInfo); 
        }//for
      }//if
    }
    

    
    /**The constructor for the inner InheritanceInfos, see picture on other constructor.
     * @param classDataP The associated class.
     * @param srcInheritance The InheritanceInfo, but from the tree of the primary super class,
     *   but not from classDataP immediately. See picture. It may be null, if the class hasn't any 
     *   override-able methods.
     */
    private InheritanceInfo(ClassData classDataP, InheritanceInfo srcInheritance)
    { this.classData = classDataP; 
      if(srcInheritance != null)
      { //final ClassData superSuperClass = superClassP.getSuperClassData();  //superclass of next level? 
        //final ClassData[] ifcSuperClass = superClassP.getInterfaces();
        if(srcInheritance.superInheritance != null){
          //MethodOverrideable[] srcSuperMethods = srcInheritance.methodTable;
          InheritanceInfo srcSuperSuperInheritance = srcInheritance.superInheritance;
          //ClassData superClass = srcInheritance.classData;
          this.superInheritance = new InheritanceInfo(srcSuperSuperInheritance.classData, srcSuperSuperInheritance);  //recursively call.
        }
        if(srcInheritance.ifcInheritance != null) {
          this.ifcInheritance = new InheritanceInfo[srcInheritance.ifcInheritance.length];
          //this.ifcInheritance = new TreeMap<String, InheritanceInfo>();
          //Set<Entry<String, InheritanceInfo>> entrySet = srcInheritance.ifcInheritance.entrySet();
          int idxIfc = 0;
          for(InheritanceInfo srcSuperifcInheritance: srcInheritance.ifcInheritance){
          //for(Entry<String, InheritanceInfo> ifcEntry: entrySet){
            //InheritanceInfo srcSuperifcInheritance = ifcEntry.getValue();
            ClassData ifcClass = srcSuperifcInheritance.classData;
            /**Super interfaces, an interface can extend another one, the last of this is ObjectJc. */
            InheritanceInfo ifcInheritance = new InheritanceInfo(ifcClass, srcSuperifcInheritance);
            this.ifcInheritance[idxIfc++] = ifcInheritance;
            //this.ifcInheritance.put(ifcClass.getClassNameJava(), ifcInheritance);
          }
          
        }
        MethodOverrideable[] srcMethodTable = srcInheritance.methodTable;
        if( srcMethodTable != null 
          //&& superClassP != Java2C_Main.CRuntimeJavalikeClassData.clazzObjectJc
          )
        { //methodTable of given superclass
          int zMethodTable = srcMethodTable.length;
          this.methodTable = new MethodOverrideable[zMethodTable];
          for(int ix=0; ix<zMethodTable; ix++){
            this.methodTable[ix] = srcMethodTable[ix].clone();
          }
        }  
      }
    }
    
    
    
    
    
    @Override public String toString()
    { return classData + " extends "+ superInheritance.classData + ", ifc:"+ ifcInheritance;
    }
    
    void stop(){}
  }
  
  /**Tree of all method tables of interfaces and the superclasses. */
  InheritanceInfo inheritanceInfo;  
  
  /**unused yet, need? */
  static class MethodArg
  { ClassData type;
    
  }
  
  class MethodWithZbnfItem
  { final Method method;
    final ZbnfParseResultItem zbnfMethod;
    final Method supermethod;
    private final char isConstructor;
    
    /**Constructs.
     * @param method The method
     * @param zbnfMethod The parse result item, used for the body.
     * @param supermethod If it is a ctor of an anonymous class, the found super ctor. It should be called.
     * @param isConstructor 'c' if it is an constrcutor
     */
    public MethodWithZbnfItem(Method method, ZbnfParseResultItem zbnfMethod
    , Method supermethod, char isConstructor
    )
    { this.method = method;
      this.zbnfMethod = zbnfMethod;
      this.supermethod = supermethod;
      this.isConstructor = isConstructor;
      if(isConstructor == 'c'){ nrofConstructor +=1; } 
    }

    public final boolean isConstructor()
    { return isConstructor == 'c';
    }
    
  }
  
  
  
  /**This class composed the reference of a method and the name of implementation of the method
   * in an inheriting class. The class is used in all {@link ClassData.InheritanceInfo#methodTable}
   * to allow definition of an implementing name to override the method.
   */
  private static class MethodOverrideable
  { 
    /**The method. */
    final Method method;
  
    /**The implementing name in the class, to which this instance is associated.  */
    private String sNameOverriding;
    
    /**Creates.
     * @param method The method which is able to override
     * @param sNamePrimary The primary name of the implementation. The instance will be create
     *        if the method is declared first.
     */
    MethodOverrideable(Method method, String sNamePrimary)
    { this.method = method; sNameOverriding =sNamePrimary; 
    }
    
    /**An inheriting class should have its own instance. clone it.
     * The method reference is the same, because its content isn't changed.
     * The {@link #sNameOverriding} can be changed. 
     */
    public MethodOverrideable clone()
    {
      return new MethodOverrideable(method, sNameOverriding);
    }
    
    public String toString(){ return sNameOverriding; }
    
    private void stop(){}
  }
  
  
  /**Instances of this class are created only temporary while running first pass of this class.
   * This class stores some data about methods from super classes and interfaces.
   * After finish first pass this informations are stored in the methods of the class. All methods, 
   * from supers and interfaces too, are stored finally in {@link ClassData#methods} 
   * and are provided by {@link ClassData#searchMethod(String, List)}.
   * to access it while running second passes (method call).
   */
  private static class MethodOverrideCheck
  { 
    /**The method. This element is null if the method is referenced in {@link #listOccurence},
     * because the method is referenced there.
     * It is only used for new methods of this class.*/
    final Method method;
    
    /**The path to the super method table for calling. */
    final String sPathToMtbl;
  
    /**If the method is declared in a base class or interface too, it is the path necessary to address
     * the base class or interface started from <code>ythis</code>
     */
    public final String sPathToBase;

    public static class MethodIndex
    { final MethodOverrideable[] ref;
      final int idx;
      
      /**Constructs.
       * @param ref reference in any {@link ClassData.InheritanceInfo#methodTable2}
       * @param idx index in the reference
       */
      public MethodIndex(MethodOverrideable[] ref, int idx)
      { this.idx = idx;
        this.ref = ref;
      }
    }
    
    /**Index of all occurences of this method in superclasses and interfaces. */
    private final List<MethodIndex> listOccurence = new LinkedList<MethodIndex>();
    
    /**Creates.
     * @param method The method which is able to override
     * @param sNamePrimary The primary name of the implementation. The instance will be create
     *        if the method is declared first.
     */
    MethodOverrideCheck(Method method, String sPathToMtbl, String sPathToBase)
    { this.method = method; 
      this.sPathToMtbl = sPathToMtbl;
      this.sPathToBase = sPathToBase;
    }
    
    /**Adds a occurence of the method in any method table of superclasses or interfaces.
     * It is called on construction of a {@link ClassData} and takes informations from base classes
     * and interfaces.
       * @param ref reference in any {@link ClassData.InheritanceInfo#methodTable2}
       * @param index index in the reference
     */
    void addOccurence(MethodOverrideable[] ref, int index)
    { listOccurence.add(new MethodIndex(ref, index));
      
    }
    
    public String toString(){ return "" + listOccurence.size(); }
  }
  
  
  /**Index of all methods with the same number of arguments. 
   * The key is the java method name, following "#nr", where nr is the number of arguments.
   * The value is either a {@link Method} or a List<Methode> if more as one method 
   * with the same number of argumentes exists.  
   */
  private TreeMap<String, Object> methods;
  
  /**Index of all methods with its C name.  
   */
  private TreeMap<String, Method> methodsCname;
  
  
  
  /**This index is only temporary used while the first pass is running. 
   * All override-able methods from base classes and interfaces are referenced here, 
   * via the {@link ClassData.InheritanceInfo#methodTable} reference and the index in the table.
   * Any occurrence of the same method in nested or parallel interfaces and super classes 
   * is registered only one time here. 
   * If a method is declared in an interface, and also declared in another parallel interface, 
   * than implemented in a super class, it is found here only one time with its unambiguous name 
   * (respecting same name but different parameter types). The unambiguous name is the same 
   * in all occurrences of the same method.
   * <br>
   * The key of this Map is the unambiguous name of the method.  
   */
  private TreeMap<String, Object> methodsCheckOverriding = new TreeMap<String, Object>();

 
  
  /**This index stores methods, which are designated as override-able, but they are defined in this class, 
   * the aren't found in any base class. 
   * The list is used only temporary while running the first pass. After them the content of this list
   * will be stored in {@link #inheritanceInfo} and there in {@link ClassData.InheritanceInfo#methodTable} 
   * of the primary level. A list but not an array is used first to add a unknown amount of methods.
   * <br>
   * The order of methods and hence the order of method in {@link ClassData.InheritanceInfo#methodTable}
   * and in the generated method table in C-code is adequate the order of methods in the java source code.
   * The order is important if the compiled C-unit is used as part of library, and using C-units 
   * should not be recompiled if inner functionality is changed in the library. The Headerfile contains
   * and shows this order. It is the same problem like virtual methods in C++.
   */
  private List<MethodOverrideable> methodsOverrideableNew = null;
    
  
  final Queue<MethodWithZbnfItem> methodsWithZbnf = new LinkedList<MethodWithZbnfItem>(); 
  
  int nrofConstructor = 0;
  
  /**If <code>this</code> or a unnamed instance of the class is used as a reference, 
   * this field supplied the necessary instance infos.
   * It represents the instance as simple forward reference, like <code>this</code>
   * respectively <code>ythis</code> in C.
   * <br>
   * An Unnamed instance is especially a returned instance reference or a instance per value (StringJc) 
   * of a method call.
   * <br>
   * In opposite: The {@link #typeCodeInfo} contains a quasi-FieldData for static access. 
   */
  public final FieldData classTypeInfo;
  
  /**This codeinfo is used if a classlevel ident is accessed. It is the CcodeData of this. */
  final CCodeData thisCodeInfo;
  
  /**This codeinfo is used if a super constructor is called. It is either a reference to ObjectJc or MemC. */
  final CCodeData ctorCodeInfo;
  
  /**This codeinfo contains an cCode == "" and the correct type. The modeAccess is 'C'. It is used
   * if a static access to a class member is translated. 
   * It is used in {@link SecondPass.StatementBlock#gen_reference(String[], ZbnfParseResultItem, LocalIdents, CCodeData, char)}.
   */
  final CCodeData typeCodeInfo;
  
  /**The superclass of this or null. */
  //final ClassData superClazz;
  
  
  /**The outer class of this or null. */
  final ClassData outerClazz;
  
  /**This class saves the position of zbnfInit in a parse result to the named variable.
   * It is used if the initialization is generated.
   */
  static class InitInfoVariable
  { /**The zbnf parse result element which contains the <code>value::=</code> 
     * or <code>newObject::=</code> or <code>newArray::=</code> or <code>constArray::=</code>. 
     */
    final ZbnfParseResultItem zbnfInit;
    
    final FieldData identInfos;
    
    public InitInfoVariable(FieldData identInfo, ZbnfParseResultItem zbnfValue)
    { this.zbnfInit = zbnfValue; this.identInfos = identInfo;
    }
    
  }

  /**List of parse result items of initial values for variables. 
   * Filled in first path, used in second pass.
   */
  private final List<InitInfoVariable> variablesToInit;

  public List<InitInfoVariable> getVariablesToInit(){ return variablesToInit; }
  
  /**List of all static variables either with initial values or not.
   * Filled in first path, used in second pass.
   */
  final List<InitInfoVariable> staticVariables;

  /**Initializes new translated classes.
   * @param fileContainsClass
   * @param sFileName
   * @param sPkgJava
   * @param sNameJava The Name of the class from Java, 
   * @param sNameCP The Name of the class used in C-code
   * @param sArgSensitiveName Short type-identifier for parameter-sensitive C-routine-name-part 
   * @param cVaArgIdent Should be 'L' always
   * @param outerClassData null if it isn't an inner class.
   * @param superClassData if the class doesn't extend any other class, it is ObjectJc. 
   *                       But if the class doesn't base on ObjectJc, it is null. 
   *                       It is controlled by <code>@ java2c=noObject</code>.
   * @param ifcClassData null or array of all interfaces.                      
   * @param zbnfClass The parse result of the class code. It is given to run the second pass
   *                  of translation. See {@link #zbnfClass} of the class.
   * @param nonStaticInner
   * @param bFinal
   * @param fileLevelIdents The localIdents from the file level containing all known classes in the file
   *                 including the imported classes
   */
  /**
   * @param sSourceOfClassData
   * @param fileContainsClass
   * @param sFileName
   * @param sPkgJava
   * @param sNameJava
   * @param sNameCP
   * @param sArgSensitiveName
   * @param cVaArgIdent
   * @param outerClassData
   * @param superClassData
   * @param ifcClassData
   * @param zbnfClass
   * @param infos
   * @param intension Intension of creation, P: primary (file level), C: inner class, 
   *                                         Y: anonymous inner class at class level,
   *                                         m, b etc: anonymous or not at statement block level 
   * @param fileLevelIdents
   */
  ClassData(String sSourceOfClassData
    , GenerateFile fileContainsClass, final String sFileName
    , String sPkgJava, String sNameJava, String sNameCP
    , String sArgSensitiveName, char cVaArgIdent 
    , ClassData outerClassData, ClassData superClassData, ClassData[] ifcClassData    
    , ZbnfParseResultItem zbnfClass
    , char access, String infos
    , char creationMode
    //, LocalIdents fileLevelIdents    
  )
  { assert(sPkgJava == null || sPkgJava.length()==0 || sPkgJava.endsWith("/"));
    Java2C_Main.singleton.console.reportln(Report.debug, "Java2C-ClassData.ctor:(" + sSourceOfClassData 
    	+ ", nameJava=" + sNameJava
    	+ ", nameC=" + sNameCP + ", " + sArgSensitiveName + ", ..., " + infos);
  	if(sNameCP.startsWith("InterProcessCommFactorySocket"))
  		stop();
    assert("PFCYb".indexOf(creationMode)>=0);
    this.creationMode = creationMode;
    this.sSourceOfClassData = sSourceOfClassData;
    this.sFileName = sFileName;
    this.fileContainsClass = fileContainsClass;
    this.sClassNameC = sNameCP; 
    
    this.sClassNameJava = sNameJava;
		this.outerClazz = outerClassData;
    
    this.sPackage = sPkgJava;
    if(sNameCP.equals("StringJc"))
      stop();
    { String sFullName = "";
      ClassData outerClazz1 = this;
      while( (outerClazz1 = outerClazz1.outerClazz) != null)
      { sFullName = outerClazz1.sClassNameJava + "." + sFullName;  //add left
      }
      sFullName = sPkgJava + sFullName;  //add left
      sFullName += sNameJava;
      sClassNameJavaFullqualified = sFullName;
    }
    this.sArgSensitiveName = sArgSensitiveName;
    this.cVaArgIdent = cVaArgIdent;
    if(sNameCP.equals("ReadTargetFromText__Target"))
      stop();
    if(sNameCP.contains("/"))
      stop();
    this.zbnfClass= zbnfClass;
    String sInfo = infos;
    this.sInfo = sInfo;
    this.isNonStaticInner = infos.contains("+nonStaticInner+");
    this.bExternal = infos.contains("+extern+");
    this.bEmbedded = infos.contains("+embedded+");
    this.isAbstract = infos.contains("+abstract+");
    this.isStaticInstanciated = infos.contains("+static+");
    this.isFinal = infos.contains("+final+");
    this.bInterface = infos.contains("+interface+");
    this.isConstant = infos.contains("+const+");
    if(this.isNonStaticInner){ sInfo += "nonStaticInner+"; }
    if(infos.contains("+ObjectJc+")){
			superClassData = CRuntimeJavalikeClassData.clazzObjectJc;
		}
    if(superClassData != null || ifcClassData != null){
      /**The inheritanceInfo is null if the class is a simple data struct. without virtual methods.*/
      inheritanceInfo = new InheritanceInfo(this, superClassData, ifcClassData);
      fillMethodsOverrideable(inheritanceInfo, "", "");
      Java2C_Main.singleton.console.report(Report.debug, " + super/ifc");
    }
    
    this.basedOnObject = (superClassData != null && (superClassData.basedOnObject || superClassData == CRuntimeJavalikeClassData.clazzObjectJc));
    if(infos.contains("+primitive+"))
    { this.classLevelIdents = null; //identsParent);
      this.classLevelIdentsOwn = null;
      this.classLevelUsageTypes = null;
			this.ctorCodeInfo = null;

    } else { 
      this.classLevelIdents = new LocalIdents(this);     //after fill inheritanceInfo because it needs superClass infos 
      this.classLevelIdentsOwn = new LinkedList<FieldData>();
      this.classLevelUsageTypes = new TreeMap<String, ClassData>();
      this.ctorCodeInfo = isBasedOnObject() 
                        ? new CCodeData("othis", CRuntimeJavalikeClassData.clazz_unknown.classTypeInfo, '*', 0)
                        : new CCodeData("mthis", CRuntimeJavalikeClassData.clazz_unknown.classTypeInfo, '*', 0);
    }
		this.variablesToInit = bExternal ? null : new LinkedList<InitInfoVariable>();
    this.staticVariables = bExternal ? null : new LinkedList<InitInfoVariable>();
    this.classTypeInfo = new FieldData(sNameCP, this, null, null, null, '.', access, 0, null, '.', this );
    this.thisCodeInfo = new CCodeData("ythis", classTypeInfo, access == '*' ? '~' : access, 0);
    this.typeCodeInfo = new CCodeData("", classTypeInfo, 'C', 0);
    //CCodeData for class member reference: use ythis as simple pointer.
    if(outerClazz != null){
    	addCastToType(outerClazz, "(?)->outer", "**");
	    if(outerClazz.outerClazz != null){
	    	addCastToType(outerClazz.outerClazz, "(?)->outer->outer", "**");
	    }
    }
    //if(superClassData !=null && superClassData.isFinalizeNeed()){
    	bFinalizeNeed = false;
    //}
    
  }

  
  

  ClassData(String sSourceOfClassData
  , String sPkgJava, String sNameJava, String sNameC, String sArgSensitiveName, char cVaArgIdent
  , String sFileName, char access, String infos
  ) //, LocalIdents identsParent)
  {
    this(sSourceOfClassData, null, sFileName, sPkgJava, sNameJava, sNameC, sArgSensitiveName, cVaArgIdent
    	, null, null, null, null, access, infos, 'P');
    
  }
  
  
  /**returns the instance because it is.
   * @see org.vishia.java2C.JavaSources.ClassDataOrJavaSrcFile#getClassData()
   */
  public ClassData getClassData()
  {
    return this;
  }


  /**returns null because it is not.
   * @see org.vishia.java2C.JavaSources.ClassDataOrJavaSrcFile#getJavaSrc()
   */
  public JavaSrcTreeFile getJavaSrc()
  {
    return null;
  }
  
  
  /**Sets the body for the finalize method.
   * @param zbnfFinalizeMethod
   */
  void setBodyForFinalize(ZbnfParseResultItem zbnfFinalizeMethod)
  { this.zbnfFinalizeMethod = zbnfFinalizeMethod;
    needFinalize();
  }
  
  /**Gets the body for the finalize method.
   * @param zbnfFinalizeMethod
   */
  ZbnfParseResultItem getBodyForFinalize()
  { return zbnfFinalizeMethod;
  }
  
  
  
  
  /**Returns true if the ClassData represents an Array type. */
  //public boolean isArray(){ return dimensionArrayOrFixSize >0; }
  
  void      need_Y() {         bitArrayNeed |= 0x01; }
  boolean isneed_Y() { return (bitArrayNeed  & 0x01) !=0; }
  void      need_YP(){         bitArrayNeed |= 0x02; }
  boolean isneed_YP(){ return (bitArrayNeed  & 0x02) !=0; }
  
  
  
  public void addField(String sIdent, FieldData identInfo)
  {
    //classLevelIdents.putClassElement(sIdent, identInfo);
    classLevelIdents.putElement(sIdent, identInfo);
    //if(classLevelIdentsOwn == null) { classLevelIdentsOwn = new LinkedList<FieldData>(); }
    if(identInfo.nClassLevel == 1 && identInfo.nOuterLevel == 1) {
    	classLevelIdentsOwn.add(identInfo);
    }
    if(identInfo.modeStatic != 'd')
    { classLevelUsageTypes.put(identInfo.getTypeName(), identInfo.typeClazz);
      hasFields = true;
    }  
    
  }
  
  /**Test whether the field is known and returns it data.
   * @param name The name of the field. The field can be local or in super scopes.
   * @return null if the field isn't existing.
   */
  public FieldData getFieldIdent(String name){ return classLevelIdents.getField(name); }
  
  public boolean hasFields(){ return hasFields; } 
  
  
  /**adds a casting possibility for initializer.
   * @param srcType The src type for which the cast is valid.
   * @param cast String in form "pre?post". To implement the cast the pre and post part will wrap the value.
   */
  public void addCastInitializerFromType(ClassData srcType, String cast, String modeAccessDstSrc)
  { int posSep = cast.indexOf('?');
    String sPre = cast.substring(0, posSep);
    String sPost = cast.substring(posSep+1);
    CastInfo castInfo = new CastInfo(srcType, sPre, sPost, modeAccessDstSrc.charAt(0), modeAccessDstSrc.charAt(1));
    if(castableInitializerFromType == null){ castableInitializerFromType = new TreeMap<String, CastInfo>(); }
    castableInitializerFromType.put(srcType.sClassNameC, castInfo);
    
  }
  
  
  
  /**adds a casting possibility from a given type to this. This is used for special cases. 
   * The cast from a base class to all its derived class is not registered here. It needs 
   * instead a cast at programming level in Java.
   * @param srcType Type from which the cast is possible
   * @param cast String in form "pre?post". To implement the cast the pre and post part will wrap the value.
   * @param modeAccessDstSrc first and second char is modeAccessDst and modeAccessSrc, 
   *        typically "**" if both are pointers or "%%" if both are primitives.
   *        Only in special cases the modeAccess is differently
   */
  public void addCastFromType(ClassData srcType, String cast, String modeAccessDstSrc)
  { int posSep = cast.indexOf('?');
    String sPre = cast.substring(0, posSep);
    if(sPre.equals("toString_StringBuilderJc("))
    	stop();
    String sPost = cast.substring(posSep+1);
    CastInfo castInfo = new CastInfo(srcType, sPre, sPost, modeAccessDstSrc.charAt(0), modeAccessDstSrc.charAt(1));
    if(castableFromType == null){ castableFromType = new TreeMap<String, CastInfo>(); }
    castableFromType.put(srcType.sClassNameC, castInfo);
    //castableFromType.put(srcType.sClassNameJavaFullqualified, castInfo);
    
  }
  
  
  /**adds a casting possibility to a given type from this. This is the typically cause for downcast
   * form a derived class to its super class.
   * @param dstType Type to which the cast is possible
   * @param cast String in form "pre?post". To implement the cast the pre and post part will wrap the value.
   * @param modeAccessDstSrc first and second char is modeAccessDst and modeAccessSrc, 
   *        typically "**" if both are pointers or "%%" if both are primitives.
   *        Only in special cases the modeAccess is differently
   */
  public void addCastToType(ClassData dstType, String cast, String modeAccessDstSrc)
  { int posSep = cast.indexOf('?');
    String sPre = cast.substring(0, posSep);
    String sPost = cast.substring(posSep+1);
    CastInfo castInfo = new CastInfo(dstType, sPre, sPost, modeAccessDstSrc.charAt(0), modeAccessDstSrc.charAt(1));
    if(castableToType == null){ castableToType = new TreeMap<String, CastInfo>(); }
    castableToType.put(dstType.sClassNameC, castInfo);
    //castableToType.put(dstType.sClassNameJavaFullqualified, castInfo);
    
  }
  
  
  /**tests wether a cast is necessary, if it, returns the casted value.
   * This is checked to srcType. If srcType is the same as this, no cast occurs, the value is returned direct.
   * If srcType is a derivateable type of this, the cast is set with {@link #addCast(ClassData, String)}.
   * That is used to produce the casted return value. 
   * @param srcType The type of the value
   * @param value The value string.
   * @param intension 'i' init-value may be in {...}
   * @return The value itself if no cast is necessary
   */
  public String xxxtestAndcast(ClassData srcType, String value, char intension)
  { String sRet;
    CastInfo castInfo;
    if(  srcType == this 
      || this == CRuntimeJavalikeClassData.clazz_void  //void as argument is a void*
      || this == CRuntimeJavalikeClassData.clazz_va_argRaw  //any type is valid
      ) 
    { //no cast necessary
      sRet = value; 
    }
    else if( value.equals("null"))
    { //a null-pointer should be compatible with all expect StringJc.
      if(sClassNameC.equals("StringJc")){ 
        sRet = "null_StringJc"; }
      else {
        sRet = value; } 
    }
    else
    { //normal value
      if(  intension == 'i' && castableInitializerFromType != null
        && (castInfo = castableInitializerFromType.get(srcType.sClassNameC)) != null
        )
      { sRet = castInfo.pre + value + castInfo.post;
      }
      else if( castableFromType != null
        && (castInfo = castableFromType.get(srcType.sClassNameC)) != null
        )
      { sRet = castInfo.pre + value + castInfo.post;
      }
      else
      { sRet = "((/*cast*/" + sClassNameC + (isPrimitiveType()? "" : "*") + ")(" + value + "))";
      }
    }
    return sRet;    
  }
  
  
  /**Registers a Method if the class is parsed or a standard class is initialized.
   * Inside {@link #addMethod(String, String, int, org.vishia.java2C.FieldData, org.vishia.java2C.FieldData[])}
   * is called with {@link ClassData#classTypeInfo}. 
   * @param sNameJava Name of the method in Java
   * @param sCName Name of the method to translate in C. If more than 1 method with the same sNameJava is given,
   *               the sCName should be unambiguous.
   * @param modifier Ones of {@link ClassData.Method#modeStatic}, {@link ClassData.Method#modeNoThCxt}               
   * @param returnType The type of return. It isn't a Classdata, but a IdentInfo, 
   *                   because some additional properties like return by value or reference should be present.
   * @param paramsType Array of all argument types. They are given in form of pure types, 
   *                   because they are taken with standard conventions: simple pointers or simple primitive types.
   */
  public void addMethod(String sNameJava, String sNameUnambiguous, int modifier, FieldData returnType, ClassData[] paramsType)
  { FieldData[] paramsIdent = new FieldData[paramsType.length];
    if(sNameUnambiguous.equals("ctorO_Target"))
      stop();
    for(int ii=0; ii<paramsType.length; ii++)
    { paramsIdent[ii] = paramsType[ii].classTypeInfo;
    }
    addMethod(sNameJava, sNameUnambiguous, modifier, returnType,paramsIdent);
  }

  /**Adds. @deprecated, use {@link #addMethod(String, String, int, FieldData, ClassData[])}
   * 
   * @param sNameJava
   * @param sCName
   * @param returnType
   * @param paramsType
   */
  public void addMethod(String sNameJava, String sNameUnambiguous, FieldData returnType, ClassData[] paramsType)
  { final int modifier = 0;
    addMethod(sNameJava, sNameUnambiguous, modifier, returnType, paramsType);
  }

  /**Registers a argumentless method if the class is parsed or a standard class is initialized.
   * @param sNameJava
   * @param sCName
   * @param modifier
   * @param returnType
   */
  public void addMethod(String sNameJava, String sNameUnambiguous, int modifier, FieldData returnType)
  { FieldData[] paramsIdent = null;
    addMethod(sNameJava, sNameUnambiguous, modifier, returnType, paramsIdent);
  }

  
  final Method addMethod
  ( String sMethodNameJava
  		, String sMethodNameUnambiguous
  	  , int modifier
  , FieldData retType
  , FieldData[] argTypes
  )
  {
  	return addMethod(sMethodNameJava, sMethodNameUnambiguous, null, modifier, retType, argTypes);
  }
  
  /**Creates a method and adds it to the lists. 
   * Test whether this method is already defined in a superclass or interface and overrides it.
   * The name of the override-able method in {@link ClassData.InheritanceInfo#methodTable} 
   * (aggregation {@link #inheritanceInfo}) is changed to the name of this method for all overridden methods.
   * It is possible that more as one overridden method is found, if the same method is defined 
   * in an interface, in more as one parallel inherited interfaces or in more as one nested interfaces or super-classes.
   * 
   * @param sMethodNameJava given method-name in Java or <code>ctorO</code> or <code>ctorM</code>
   * @param sMethodNameUnambiguous The java name plus argument type designations if the method is ambiguous.
   * @param argTypes Array of all argument types with there type modifier.
   * @param modifier Contains bits {@link ClassData.Method#modeStatic}, {@link ClassData.Method#modeNoThCxt}
   *                 or {@link ClassData.Method#modeOverrideable}. <br>
   *                 If the method is able to override, its unambiguous name is saved in {@link #methodsOverrideableNew} of this ClassData.
   *                 The keyname is ... TODO. The method name is also saved in {@link ClassData.InheritanceInfo#methodTable}.
   *                 This information is used primary to write out the method table. The names are copied and changed in an inherited class.
   * @param retType The type of return. It isn't a Classdata, but a IdentInfo, 
   *                   because some additional properties like return by value or reference should be present.
   * @return The created and registered instance of method. 
   */
  final Method addMethod
  ( String sMethodNameJava
  		, String sMethodNameUnambiguous
  		, String sImplementationName
  	  , int modifier
  , FieldData retType
  , FieldData[] argTypes
  )
  { /**The declaring class of the method may be a super-class or interface. */
    final ClassData declaringClassOfMethod;
    
    if(sMethodNameJava.equals("processIfcMethod") && sClassNameJava.equals("ImplIfc"))
      stop();
    
    final String sPathToMtbl;
    final String sPathToBase;
    final Method primaryMethod;
    if(sMethodNameUnambiguous.startsWith("processIfcMethod"))
      stop();
    
    /**Check whether the method is already declared in a base class or interface: */
    MethodOverrideCheck method1Found = searchOverrideableMethod(sMethodNameJava, argTypes);
    //MethodOverrideCheck method1Found = methodsCheckOverriding.get(sMethodNameUnambiguous);
    if(method1Found != null){
      /**The method is defined in a superclass or interface and will be overridden here: */
      //sMethodNameUnambiguous = method1Found.method.sNameUnambiguous; //this name is valid.
      final String sNameCFinal;  //use the same algorithm like in Method.ctor!!!
      if(sMethodNameUnambiguous.startsWith("!")){
        /**Special case, name not ending with class, only for base methods. */
        sNameCFinal = sMethodNameUnambiguous.substring(1);
      } else {
        sNameCFinal = sMethodNameUnambiguous + "_" + getClassIdentName() 
                         + ( (modifier & Method.modeOverrideable) != 0 ? "_F" : "");
      }
      for(MethodOverrideCheck.MethodIndex methodIndex: method1Found.listOccurence){
        /**Change all names of methods which are overridden with the given method. */
        methodIndex.ref[methodIndex.idx].sNameOverriding = sNameCFinal;
      }
      primaryMethod = method1Found.method;
      declaringClassOfMethod = primaryMethod.declaringClass;
      sPathToMtbl = method1Found.sPathToMtbl;
      sPathToBase = method1Found.sPathToBase;
      /**Inherit the some mode bits from the overridden method: */
      modifier |= primaryMethod.mode & (Method.modeNoThCxt);
    } else {
      /**The method isn't found in a base class or interface. */
      declaringClassOfMethod = this;
      /**sPathToMtbl is used if the method can bei overridden. Set "" in this case.
       * If the method isn't able to override, the sPathToMtbl is not need anywhere.
       * Set it to null to mark 'not able to override' additionally.
       */ 
      sPathToMtbl = (modifier & Method.modeOverrideable) != 0 ? "" : null;
      sPathToBase = "";
      primaryMethod = null;
    }
      
    /**primary super class or interface which defines the method. */
    ClassData.InheritanceInfo primaryInheritanceInfo = null;
    /**Test of super classes...*/
    if(!sMethodNameJava.startsWith("ctor"))
      stop();

  	final int nrofArguments = argTypes == null ? 0 : argTypes.length;
    final boolean bVaArg;
    if(nrofArguments >=1 && argTypes[nrofArguments-1].typeClazz == CRuntimeJavalikeClassData.clazz_va_argRaw){
    	bVaArg = true;
    } else { bVaArg = false; }
    final String sKeyName = sMethodNameJava +"#" 
    	+ (bVaArg ? "*" :nrofArguments);
    
    Method method = new Method(this, primaryMethod, sKeyName, sMethodNameJava
        , sMethodNameUnambiguous, sImplementationName, modifier, retType, argTypes, sPathToMtbl, sPathToBase);
    
    Java2C_Main.singleton.console.reportln(Report.debug, "Java2C-ClassData.addMethod: to Class:" 
    	+ getClassIdentName() + ", method: " + method.writeStruct());
   
    addMethodToList(method);
    
    if(method1Found == null && method.isOverrideable()){
      /**A new override-able method, not defined in a base class or interface: */
      MethodOverrideable method1 = new MethodOverrideable(method, method.sImplementationName);
      if(methodsOverrideableNew == null){ methodsOverrideableNew = new LinkedList<MethodOverrideable>(); }
      methodsOverrideableNew.add(method1);
      
    }
      
    return method;
  }

  /**Adds the method to the lists {@link #methods} and {@link #methodsCname}
   * @param method
   */
  private void addMethodToList(Method method)
  { Object oMethods;
  	String sKeyName = method.sKeyName;
    if(sKeyName.startsWith("sendMsg"))
    	stop();
    if(methods == null)
    { methods = new TreeMap<String, Object>();
      oMethods = null;
    }
    else {
      oMethods = methods.get(sKeyName);
    }
    
    if(oMethods == null){
      /**methods with the given number of parameter is unknown, add as first method. */
      methods.put(method.sKeyName, method);
    }
    else {
      if(oMethods instanceof Method)
      { List<Method> list = new LinkedList<Method>();
        list.add((Method)oMethods);  //the first
        list.add(method);  //the second
        methods.put(sKeyName, list);  //now it is a list.
      }
      else
      { assert(oMethods instanceof List);
        @SuppressWarnings("unchecked")
        List<Method> list = (List<Method>)oMethods; //it is already a list
        list.add(method);
      }
    }
    
    if(methodsCname == null) { methodsCname = new TreeMap<String, Method>(); }
    methodsCname.put(method.sCName, method);
  }
  
  
  /**searches whether a method found in the first pass of a translated class is already declared
   * in an super class or interface of this class.
   * If the method is found more as one time in parallel interfaces, there is noted only one time.
   * But the occurrence is stored in the returned instance.
   * <br>
   * <img src="../../../../Java2C/img/MethodsOverride_omdJava2C.png" />
   * @param sMethodNameJava The method name in Java
   * @param argTypes arg types of the found method.
   * @return null if the method isn't declared already, elsewhere the override-able method description.
   */
  MethodOverrideCheck searchOverrideableMethod(String sMethodNameJava, FieldData[] argTypes)
  { MethodOverrideCheck methodFound;
    if(sMethodNameJava.equals("anotherIfcmethod"))
      stop();
    Object omethodFound = methodsCheckOverriding==null ? null : methodsCheckOverriding.get(sMethodNameJava);
    boolean bMatching;
    if(omethodFound != null) {
      if(omethodFound instanceof MethodOverrideCheck)
      { /**Only one method with that name is known. Check it whether the parameter set is equal. */
        methodFound = (MethodOverrideCheck)omethodFound;
        bMatching = methodFound.method.sameParameterTypes(argTypes);
        if(!bMatching){
          methodFound = null;
        }  
      }
      else
      { /**If it isn't instanceof MethodOverrideCheck, it is instanceof List and it has at least one element. 
         * Now check whether one of the methods have the same parameter types.
         */
        @SuppressWarnings("unchecked")
        List<MethodOverrideCheck> listMethods = (List<MethodOverrideCheck>)omethodFound;
        Iterator<MethodOverrideCheck> iterMethods = listMethods.iterator();
        bMatching = false;
        do 
        { methodFound = iterMethods.next();
          bMatching = methodFound.method.sameParameterTypes(argTypes);
          if(!bMatching){
            methodFound = null;
          }  
        } while(!bMatching && iterMethods.hasNext());
      }
    }
    else { 
      methodFound = null; //not found in methodsCheckOverriding
    }
    return methodFound;
  }
  
  
  
  
  

  /**Registers a given Method. This routine is used if a file.stc is parsed.
   * @param m The Method data.
   */
  public void addMethod(Method m)
  { addMethodToList(m);
  }



  /**If a type is used as enhanced reference in the code of the class, it is noted here and in Java2CMain.
   * The information will be used to define the enhanced references in the Headerfile,
   * it is also transformed to the stc file. (?)
   * @param sRefType The referenced type.
   */
  public void addEnhancedRefType(String sRefType)
  {
    fileContainsClass.addEnhancedRefType(sRefType);
  }


  /**Adds in inner class to the class.
   * @param innerClass
   */
  void addInnerClass(ClassData innerClass)
  {
    if(innerClasses ==null){ innerClasses = new LinkedList<ClassData>(); }
  	innerClasses.add(innerClass);
  }
  
  
  
  

  
  /**Copies all field idents of this class to its  non-static inner classes,
   * because the inner classes should know the idents of its outer class. */
  void completeFieldIdentsForInnerClasses()
  {
  	if(innerClasses !=null)
  	for(ClassData innerClassData: innerClasses){
  		//if(innerClassData.isNonStaticInner){
  		classLevelIdents.copyFieldsTo(innerClassData);
  		//}
  		innerClassData.completeFieldIdentsForInnerClasses();  //recursively for inner inner classes.
  	}
  }

  /**Completes the type information of this outer class for all its inner classes. 
   * This routine is called after {@link GenerateFile#runFirstPassFile(ZbnfParseResultItem, String, String, String)}
   * of this class is finished. Then all types of this class, they are the inner classes,
   * are gathered. All inner classes have to know all the other inner classes too, the inner classes
   * should be known together. Additional the inner classes should know its outer one as type too.
   * <br><br>
   * This routine is called recursively for all its own inner classes too. 
   */
  void completeTypesForInnerClasses()
  { if(innerClasses !=null){
      stop();
	  	for(ClassData innerClassData: innerClasses){
	      classLevelIdents.copyTypesTo(innerClassData);	
	      innerClassData.completeTypesForInnerClasses();  //call recursively if more levels of inner classes	
	  	}
    }
  }
  
  
  
  
  /**Sets the finalize bit. This method is called if any condition is met which requires
   * the generation of a finalize method. It is, if the class contains enhanced references
   * or if a finalize method is given in Java code.
   */
  public void needFinalize()
  {
    bFinalizeNeed = true;
  }
  
  
  
  /**If a type is used as method-table- reference in the code of the class, it is noted here.
   * The information will be used to define the enhanced references in the Headerfile,
   * it is also transformed to the stc file. (?)
   * @param sRefType The referenced type.
   */
  public void addMtblRefType(String sRefType, char intension)
  {
    fileContainsClass.addMtblRefType(sRefType, intension);
  }
  
  
  
  
  
  /*
  public ClassData nextArrayDimension()
  { if(arrayType == null)
    { //An arraytype doesn't exist, create it because it is required. 
      arrayType = new ClassData(this ); //special private constructor for next array dimension.
      //NOTE: special constructor is necessary because some final fields.
    }
    return arrayType;
  }
  */
  
  
  /**Tests whether a method is registered, registers it or set it to ambiguous if it exists already.
   * This routine is called processing the first pass of translation. 
   * It creates an entry with key sNameJava + "?" to check ambiguously.
   * In the second pass the methods are registered with full informations.
   * @param sNameJava The method name.
   */
  public void testAndSetAmbiguousnessOfMethod(String sNameJava)
  { if(methods == null) { methods = new TreeMap<String, Object>(); }
    Method m = (Method)methods.get(sNameJava+"?");
    if(m != null)
    { //The method with the same java name is present already:
      m.setAmbiguousness();
    }
    else
    { m = new Method(this, null, sNameJava+"?", sNameJava, null, null, 0, null, null, null, "");  //it is a preliminary instance.
      methods.put(sNameJava+"?", m);
    }
  }

  
  /**tests whether the method is ambiguous. The method have to be exist.
   * @param sNameJava
   * @return true if more as one method with the same Java name exists. 
   */
  public boolean isAmbiguousnessMethod(String sNameJava)
  { Method m = (Method)methods.get(sNameJava+"?");
    if(m == null)
    { stop();
      return false;
    }
    return m.isAmbigous();  //true if it is the second method
  }

  
  
  
  private boolean checkParameterTypesMethod(Method methodTest, FieldData[] paramsType)
  {
    boolean bMatching;
    int idxParam = 0;
    if(paramsType == null && (methodTest.paramsType == null || methodTest.paramsType.length == 0))
    { 
      bMatching = true;
    }
    else
    { bMatching = true; //default, abort test if false.
      int idxParamsType = 0;
      while(bMatching && idxParamsType < paramsType.length)
      { FieldData infoActParam = paramsType[idxParamsType++];
        FieldData typeParamMethod = methodTest.paramsType[idxParam++];
        if(typeParamMethod.typeClazz != infoActParam.typeClazz)
          //TODO test array properties
        { bMatching = false;
        }
      }
    }
    return bMatching;
  }              
  
  
  Method getMethodPerCname(String sNameC){ return methodsCname.get(sNameC); }
  
  
  /**Searches a method with the given Java name and all argument types.
   * @param sNameJavaP The name in Java.
   * @param paramsTypeCheck All argument types. It is tested whether the argument types 
   *                   are able to cast to the matching types.
   * 
   * @param strict true then the method is not created if if isn't found. It should be found.
   * @param sPathMtbl Starting with "", a seach in a super-class adds the path.
   * @return The appropriate method or null if no method matches.
   */
  public Method searchMethod( 
  		final String sNameJavaP, final List<CCodeData> paramsTypeCheck, boolean strict
    , String[] sPathMtbl
  )
  { boolean bFound = false;
    //String sNameTranslate = null;
    Method methodFound = null;
    String sPathMtbl1 = "";
    String sKeyName, sKeyNameVarg;
    if(sNameJavaP.equals("assert"))
    {
      return Java2C_Main.singleton.standardClassData.methodASSERT; 
    }
    else
    {
      if(sNameJavaP.equals("setBigEndian")) // && sClassNameJava.equals("Thread"))
        stop();
      sKeyName = sNameJavaP +"#"+ (paramsTypeCheck == null ? "0" : paramsTypeCheck.size());
      sKeyNameVarg = paramsTypeCheck == null ? "???" : sNameJavaP +"#*";
      if(methods != null) { 
      	if(sNameJavaP.equals("arraycopy") ) // && this.sClassNameJava.equals("StringFormatter"))
          stop();
      	if(sKeyName.equals("ctorO#1") && this.sClassNameJava.equals("StringFormatter"))
          stop();
        Object omethodFound;;
        if( (omethodFound = methods.get(sKeyName))!= null){
        	methodFound = checkParameterMethod(omethodFound, paramsTypeCheck);
        }
        /**methodFound is !=null if the parameters are matched. */
        if(methodFound ==null && (omethodFound = methods.get(sKeyNameVarg))!= null){
          /**method with matched parameters not found, 
           * but a method with variable argument list is found with given name, check it: */
         	methodFound = checkParameterMethod(omethodFound, paramsTypeCheck);
        }
      }//if methods!=null 
      if(methodFound == null)
      { /**Try to search in all super classes and their outer classes
      	 * It is a recursively call. First search in all super classes, than in the outer classes.
      	 * It is the order like in Java.
      	 */
        if(sNameJavaP.equals("setBigEndian"))
          stop();
        if(sNameJavaP.equals("ctorO"))
          stop();
        ClassData superClass = getSuperClassData();
        if(!sNameJavaP.startsWith("ctorO") && superClass != null) {
        	//it may be a recursively call.
        	String[] sPathMtblSuper = new String[1];
        	sPathMtblSuper[0] = sPathMtbl[0];
         	methodFound = superClass.searchMethod(sNameJavaP, paramsTypeCheck, true, sPathMtbl);
         	if(methodFound !=null){
         		sPathMtbl1 = superClass.getClassIdentName() + "." 
         		  + (sPathMtbl !=null && sPathMtbl[0] !=null ? sPathMtbl[0] : "");
         	}
        }
      }
      if(methodFound == null){
      	/**Try to search in all outer classes, and their super-classes.
      	 * It is a recursively call. First search in all super classes, than in the outer classes.
      	 * It is the order like in Java.
      	 */
        ClassData outerClass = getOuterClass();
        if(outerClass != null) {
          methodFound = outerClass.searchMethod(sNameJavaP, paramsTypeCheck, true, sPathMtbl);
        }
      }
      if(!strict && methodFound == null){
        /**Method not found: */
        String sNameC1 = sNameJavaP /* + "/*" + sKeyName + "?method(";
        String delim = "";
        if(paramsType != null) for(CCodeData param:paramsType)
        { sNameC1 += delim + param.identInfo.toString();
          delim = ",";
        }
        sNameC1 += ")* /" */;
        methodFound = new Method(this, null, sKeyName, sNameJavaP, sNameC1, null, Method.modeUnknownMethod
            , CRuntimeJavalikeClassData.clazz_unknown.classTypeInfo, null, null, "");
      }
      if(sPathMtbl !=null){ 
      	sPathMtbl[0] = sPathMtbl1;
      }
      return methodFound;
      //return sNameTranslate;
    }  
  }
  

	private Method checkParameterMethod(Object omethodFound, final List<CCodeData> paramsTypeCheck)
	{ Method methodFound = null;
		if(omethodFound instanceof Method)
	  { methodFound = (Method)omethodFound; 
	  }
	  else
	  { @SuppressWarnings("unchecked")
	  	List<Method> listMethods = (List<Method>)omethodFound;
	    //boolean bMatching = false;
	    Iterator<Method> iterMethods = listMethods.iterator();
	    //while(!bMatching && iterMethods.hasNext())
	    int scoreFound = 0;
	    //while(methodFound==null && iterMethods.hasNext())
	    while(iterMethods.hasNext())
	    { Method methodTest = iterMethods.next();
	      int scoreMethod = methodTest.checkParameter(paramsTypeCheck);
	      //if(methodTest.checkParameter(paramsTypeCheck))
	      if(scoreMethod > scoreFound )
	      { methodFound = methodTest;  //the last method with highest score wins.
	        scoreFound = scoreMethod;
	      }
	    }
	  }
		return methodFound; //maybe null
	}
  
  
  
  /**Returns the name of the class used as C-type. If it is an generated class, that type names
   * have an extension <code>_s</code>. The type name without this extension is useable, 
   * if a C++-wrapper-class of that type is desired. 
   */
  public String getClassCtype_s(){ return sClassNameC; }
  
  
  /**Returns the name of the class used in Java without package information.
   */
  public String getClassNameJava(){ return sClassNameJava; }
  
  
  /**Returns the indentification name of a class. If the class is generated from java2c, 
   * it is the name without the <code>_s</code> or <code>_i</code> on end.
   * The identification is used as postfix of the static visible class elements.
   * But the <code>struct</code> type have the additional postfix <code>_s</code> or <code>_i</code>
   * to to distinguish between its possible C++-class.
   */
  public String getClassIdentName()
  { if(sClassNameC.endsWith("_s") || sClassNameC.endsWith("_i"))
    { return sClassNameC.substring(0, sClassNameC.length()-2); }
    else{ return sClassNameC; }
  }
  
  
  public String getClassNameJavaFullqualified(){ return sClassNameJavaFullqualified; }
  
  /**Returns true if it is a primitive type without any own elements. It is, if the element 
   * {@link #classLevelIdents} is null.
   */
  public boolean isPrimitiveType(){ return classTypeInfo.modeAccess == '%'; } //return classLevelIdents == null; }

  /**Returns true if the type is 'String'.
   */
  public boolean isString(){ return classTypeInfo.modeAccess == 't'; } //return classLevelIdents == null; }


  public boolean xxxisObject(){ return inheritanceInfo != null; }
  public boolean xisObject(){ return basedOnObject; }
  
  
  /**Returns true if it is an interface.
   */
  public boolean isInterface(){ return bInterface; }


  /**Returns true if the finalize is need but it is not written in Java.
   */
  public boolean isFinalizeNeed(){ return bFinalizeNeed; }
  
  /**Returns the superclass or null. */
  public ClassData getSuperClassData()
  {
    final ClassData superClass;  //superclass of next level? 
    if(inheritanceInfo != null)
    { InheritanceInfo superInheritanceInfo = inheritanceInfo.superInheritance;
      superClass = (superInheritanceInfo == null) ? null : superInheritanceInfo.classData;
    }
    else
    { superClass = null;
    }
    return superClass;        
  }
  
  /**Returns the ClassData of outer class or null.
   */
  public ClassData getOuterClass()
  { return outerClazz;
  }
  
  
  /**Returns all interfaces of immediate level (not interfaces of base classes).
   * @return The interfaces are provided in an array.
   */
  public ClassData[] getInterfaces()
  { final ClassData[] ifc;
    //final TreeMap<String, InheritanceInfo> ifc;  //superclass of next level? 
    if(inheritanceInfo != null)
    { //Map<String, InheritanceInfo> ifcInheritanceInfo = inheritanceInfo.ifcInheritance;
      if(inheritanceInfo.ifcInheritance != null){
      //if(ifcInheritanceInfo != null){
        ifc = new ClassData[inheritanceInfo.ifcInheritance.length];
        //ifc = new ClassData[ifcInheritanceInfo.size()];
        int ix=0;
        //Set<Entry<String,InheritanceInfo>> entrySet = inheritanceInfo.ifcInheritance.entrySet();
        for(InheritanceInfo srcIfcInheritance : inheritanceInfo.ifcInheritance){
        //for(Entry<String,InheritanceInfo> entry : entrySet){
          ClassData ifcData = srcIfcInheritance.classData;
          //ClassData ifcData = entry.getValue().classData;
          ifc[ix++] = ifcData;
        }
      } else {
        ifc = null;
      }
    } else {
      ifc = null;
    }
    return ifc;        
  }
  
  
  /**returns a String, which describes the access to the ObjectJc-base, or return null,
   * if the class isn't based on any class. It assumes, all superclasses should have the name "super",
   * the ObjectJc-superclass should have the name "object".
   * At ex. returns "object"
   * 
   * @return
   */
  public String xxxgetRefObjectJc()
  { String ret = "";
    ClassData superClazz = getSuperClassData();
    while( superClazz != null)
    { if(superClazz == CRuntimeJavalikeClassData.clazzObjectJc)
      { ret += "base.object";
      }
      else
      { ret += "base.super";
      }
      superClazz = superClazz.getSuperClassData();
    }
    return ret.length() ==0 ? null: ret.substring(1);  //without first "."
  }
  
  
  
  /**returns a String, which describes the access to the ObjectJc-base, or return null,
   * if the class isn't based on any class. It assumes, all superclasses should have the name "super",
   * the ObjectJc-superclass should have the name "object".
   * At ex. returns "object"
   * 
   * @return
   */
  public String xxxxxgetRefObjectJc()
  { return "base.object";
  }
  
  
  
  public boolean isBasedOnObject(){ return basedOnObject; }
  public boolean xxxisBasedOnObject()
  { boolean ret = false;
    ClassData superClazz = getSuperClassData();
    while(superClazz != null)
    { if(superClazz == CRuntimeJavalikeClassData.clazzObjectJc)
      { ret = true;
      }
      superClazz = superClazz.getSuperClassData();
    }
    return ret;    
  }
  
  
  /**Returns true if the class isn't known in Java2C context. It is a external used class.*/
  public boolean isExternal(){ return bExternal; }
  
  
  /**Returns the info how to cast from this type to another.
   * @param typeSrc Name of the source type.
   * @return CastInfo contains pre- and suffix C-Code and the modeAccess.
   */
  public CastInfo getCastInfoFromType(String typeSrc)
  { if(castableFromType != null) { return castableFromType.get(typeSrc); } //may be null if not found.
    else return null;  //no cast info.
  }
  
  
  
  /**Returns the info how to downcast from this type to another.
   * @param typeSrc Name of the source type.
   * @return CastInfo contains pre- and suffix C-Code and the modeAccess.
   */
  public CastInfo getCastInfoToType(String typeSrc)
  { if(castableToType != null) { return castableToType.get(typeSrc); } //may be null if not found.
    else return null;  //no cast info.
  }
  
  
  /**Returns the info how to downcast from this type to another.
   * @param typeSrc Name of the source type.
   * @return CastInfo contains pre- and suffix C-Code and the modeAccess.
   */
  public CastInfo getCastInitializerFromType(String typeSrc)
  { if(castableInitializerFromType != null) { return castableInitializerFromType.get(typeSrc); } //may be null if not found.
    else return null;  //no cast info.
  }
  
  
  
  /**Checks whether the src matches to this class with or without type casting.
   * @param src The type to compare
   * @return the score. 0: doesn't match 1 .. 4 see {@link CastInfo#kCastAble}. 
   */
  int matchedToTypeSrc(ClassData src)
  { String typeSrc = src.sClassNameC;
    CastInfo castInfo;
    if(this.sClassNameC.equals(src.sClassNameC)) return CastInfo.kCastEqual;
    else if(  (castInfo = getCastInfoFromType(typeSrc)) != null  //the typeSrc is found in castableFromType, apropriate to some conversions
           || (castInfo = src.getCastInfoToType(sClassNameC)) != null){
    	if( (castInfo.pre == null || castInfo.pre.length()==0)
    		&&(castInfo.post == null || castInfo.post.length()==0)  
    		){
    		return CastInfo.kCastAutomatic;  //types are castable without any additional cast expression.
    	} else {
    	  return CastInfo.kCastAble;  //this is found in castableFromType of srctype, appropritate to base class
      }
    }
    else if(this.sClassNameJava.equals("va_argRaw")){
    	return CastInfo.kCastCommon;  //the src is matching to a raw variable argument always.
    }
    else return CastInfo.kCastNo;  //not able to cast.
  }

  ZbnfParseResultItem getParseResult(){ return zbnfClass; }

  /**relinquishes the parse result, because it is processed. It should not consumed memory.*/
  void relinquishParseResult(){ zbnfClass = null; }

  /**Helper variable to generate indentation, see {@link #indent(int)} */
  private final static String newLineIndent = "\n                                            ";
  
  /**Generates an indentation, used inside {@link #writeStructureClass(BufferedWriter, int)}
   * @param recursion nesting level
   * @return A newline char and the appropritate number of spaces for indentation. 
   */
  private String indent(int recursion){ return newLineIndent.substring(0, 2*recursion +1); } 
  
  
  public void reportContent(Report console, int reportlevel){
    console.reportln(reportlevel, "==Structure of class==");
  	StringWriter buffer = new StringWriter();
  	BufferedWriter out = new BufferedWriter(buffer);
  	try{ 
  		writeStructureClass(out,0);
  		out.close();
    } catch(IOException exc){
  		console.writeError("error", exc);
  	}
    String sReport = buffer.toString();
    LogMessage msg = console.getLogMessageOutputFile();
    msg.sendMsg(105, sReport);
    msg.sendMsg(106, "Ident of instance: " + sClassNameJavaFullqualified + ": %s", toString());
  	for(Map.Entry<String,JavaSources.ClassDataOrJavaSrcFile> setInnerClass: this.classLevelIdents.getTypeSet()){
  	  ClassData innerClass = setInnerClass.getValue().getClassData();
  	  msg.sendMsg(107, "Ident of innerclasses: " + innerClass.sClassNameJava + ": %s", innerClass.toString());
    	  
  	}
    msg.flush();
    //console.report(reportlevel, buffer.toString());
  	//console.flushReport();
  	//console.flush();
  }
  

  /**returns the definition of the methodtable of the class and all superclasses.
   */
  public String gen_MethodTableDefinitionContent()
  { String ret = "MtblHeadJc head;";
    if(inheritanceInfo != null) {
      if(inheritanceInfo.methodTable != null){
        /**Content of methods of the own class. */
        for(MethodOverrideable methodOverridden: inheritanceInfo.methodTable)
        { ret += "\n  MT_" + methodOverridden.method.sMethodTypeName + "* " + methodOverridden.method.sNameUnambiguous + ";";
        }
      }
      if(inheritanceInfo.superInheritance != null){
        /**Only the direct Superclass is written, it contains the inner superclasses and ObjectJc. */
        String sSuperName = inheritanceInfo.superInheritance.classData.getClassIdentName();
        ret += "\n  Mtbl_" + sSuperName + " " + sSuperName + ";";
      }
      if(inheritanceInfo.ifcInheritance != null){
        ret += "\n  //Method table of interfaces:";
        //Set<Entry<String,InheritanceInfo>> entrySet = inheritanceInfo.ifcInheritance.entrySet();
        for(InheritanceInfo ifcInheritance : inheritanceInfo.ifcInheritance){
        //for(Entry<String,InheritanceInfo> entry : entrySet){
          ClassData ifcData = ifcInheritance.classData;
          //ClassData ifcData = entry.getValue().classData;
          String sIfcName = ifcData.getClassIdentName();
          ret += "\n  Mtbl_" + sIfcName + " " + sIfcName + ";";
        }
      }
    }  
    return ret;
  }    
  
	/**Generates the content of the class definition for C++,
	 * to write in the header-file. 
	 * @return
	 */
	public String gen_ClassCppDefinitionContent()
	{
		StringBuilder u = new StringBuilder(5000);
		if(methodsCname !=null){
			Collection<Method> methods = methodsCname.values();
			for(Method method: methods){
				int bitsStringJcParam;
				if(method.sCName.startsWith("ctorM")){
				 //TODO
				}	
				else if(method.sCName.startsWith("finalize")){
					//TODO
				} else {
			    //bitsStringJcParam = 
			    gen_MethodCpp(u, method, 0); //StringJc-parameter-variant
			    /*
			    int bitZchar = 1;
			    while(bitZchar <= bitsStringJcParam){
			      while((bitZchar & bitsStringJcParam)==0){ bitZchar <<=1; }
			      gen_MethodCpp(u, method, bitZchar);
			      bitZchar <<=1;
			    } 
			    */ 
				}
			}
		}
		return u.toString();
	}
  

	private int gen_MethodCpp(StringBuilder u, Method method, int bitZchar)		
	{	int bitsStringJcParam = 0;

		String sReturnTypeDefinition = method.sReturnTypeDefinition;
		if(sReturnTypeDefinition == null){ sReturnTypeDefinition = "void"; }
		else if(method.isReturnThis()){ sReturnTypeDefinition = getClassIdentName() + "&";}
		if(method.sCName.startsWith("ctorO")){
			u.append("\n\n  ").append(getClassIdentName());  //ctor in C++ with class-name
		} else { 
			u.append("\n\n  ");
			if(method.isOverrideable()){
				u.append("virtual ");
			}
			u.append(sReturnTypeDefinition).append(" ").append(method.sJavaName);
		}
		u.append("(");
		String sSeparator = "";
		int mBitStringJcParam = 1;
		/**formal parameter for C++-method-head. */
		int bitZcharTest = 1;
		if(method.paramsType!=null)
		for(FieldData param: method.paramsType){
			if( param.typeClazz == CRuntimeJavalikeClassData.clazzStringJc
				&& param.getDimensionArray() == 0
				){
				bitsStringJcParam |= mBitStringJcParam;
				/**Use StringJcpp instead StringJc, because a type conversion from "literal" is supported.*/
				u.append(sSeparator).append("StringJcpp ").append(param.getName());
			} else {
				u.append(sSeparator).append(param.gen_VariableDefinition('b'));
			}
			/*
			if((bitZchar & mBitStringJcParam)!=0){
				u.append(sSeparator).append("char const* ").append(param.getName());
			} else {
				u.append(sSeparator).append(param.gen_VariableDefinition());
			}
			*/
			mBitStringJcParam <<=1;	
			sSeparator = ", ";
		}
		u.append(")");
		if(isInterface()){
			u.append("=0;");
		} else {
			u.append("{ ");
			if(method.sCName.startsWith("ctorO")){
				//u.append("memset((ObjectJc*)this, sizeof(").append(sClassNameC).append("), 0); ");
				u.append("init_ObjectJc(&this->base.object, sizeof(").append(sClassNameC).append("), 0); ");
				u.append("setReflection_ObjectJc(&this->base.object, &reflection_").append(sClassNameC).append(", 0); "); 
				u.append(method.sCName).append("(").append("&this->base.object");
			  sSeparator = ", ";
			}
			else { 
				if(!sReturnTypeDefinition.equals("void") && !method.isReturnThis()){ 
					u.append(" return "); 
				}
				/**Method name call:*/
				u.append(method.sCName);
				if(method.isOverrideable()){
					u.append("_F");
				}	
				u.append("(");
				if(method.isStatic()){ sSeparator = "";}
				else {
					sSeparator = ", ";
					if(method.sPathToBase.length() >0){
					  u.append("&this->").append(method.sPathToBase.substring(1));  //without first dot because ->	
			      if(method.firstDeclaringClass.isInterface()){
			      	u.append(".base.object");  //call an interface method via ObjectJc*. It is not part of sPathToBase, why?
			      }
					} else {
					  u.append("this");	
					}
				}
		  }
			mBitStringJcParam = 1;
			/**actual parameter for C-routine-call. */
			if(method.paramsType!=null)
			for(FieldData param: method.paramsType){
				/*
				if((bitZchar & mBitStringJcParam)!=0){
					u.append(sSeparator).append("z_StringJc(").append(param.getName()).append(")");
				} else */{
					u.append(sSeparator).append(param.getName());
				}
				mBitStringJcParam <<=1;
				sSeparator = ", ";
			}
			if(method.need_thCxt){ u.append(sSeparator).append(" null/*_thCxt*/"); }
			u.append("); ");
			if(method.isReturnThis()){ 
				u.append(" return *this; "); 
			}
			u.append("}");
		}
		return bitsStringJcParam;
	}



  /**returns the definition of the methodtable of the class and all superclasses.
   * @param classDataP
   * @return
   */
  String genMethodTableContent(ClassData.InheritanceInfo inheritanceInfo, String sClassName, int indent)
  { String ret;
    char cSeparator;
    String sClassIdentNameCurrent = inheritanceInfo.classData.getClassIdentName();  //from superclass etc.  
    if(sClassNameJava.equals("TestAllConcepts"))
      stop();
    assert(inheritanceInfo != null);
    if(indent==0){
      ret = GenerateClass.genIndent(indent) + "{ ";
    } else {
      ret = GenerateClass.genIndent(indent-1) + ", { ";
    }
    int nrofMethods = inheritanceInfo.methodTable == null ? 0 : inheritanceInfo.methodTable.length;
    ret += "{ sign_Mtbl_" + sClassIdentNameCurrent + "//J2C: Head of methodtable." 
    //    + ", (struct Size_Mtbl_t*)sizeof(Mtbl_" + sClassIdentNameCurrent + ") } //head";      
        + GenerateClass.genIndent(indent) + "  , (struct Size_Mtbl_t*)((" + nrofMethods + " +2) * sizeof(void*)) //size. NOTE: all elements are standard-pointer-types."      
        + GenerateClass.genIndent(indent) + "  }";      
    cSeparator = ',';
    if(nrofMethods >0 ){
      /**Content of methods of the own class. */
      for(MethodOverrideable methodOverridden: inheritanceInfo.methodTable)
      { ret += GenerateClass.genIndent(indent) + ", " + methodOverridden.sNameOverriding + " //" + methodOverridden.method.sNameUnambiguous;
        cSeparator = ',';
      }
    }

    
    if(inheritanceInfo.superInheritance != null){
      ret += genMethodTableContent(inheritanceInfo.superInheritance, sClassName, indent+1);
      cSeparator = ',';
    }
    
    if(inheritanceInfo.ifcInheritance != null){
      ret += GenerateClass.genIndent(indent) + "  /**J2C: Mtbl-interfaces of " + sClassName + ": */";
      //Set<Map.Entry<String,ClassData.InheritanceInfo>> entrySet = inheritanceInfo.ifcInheritance.entrySet();
      for(InheritanceInfo ifcData : inheritanceInfo.ifcInheritance){
      //for(Map.Entry<String,ClassData.InheritanceInfo> entry : entrySet){
        //ClassData.InheritanceInfo ifcData = entry.getValue();
        ret += genMethodTableContent(ifcData, sClassName, indent+1);
      }
    }
    
    ret += GenerateClass.genIndent(indent) + "}";
    return ret;
  }    
  
  
  
  String xxxwrite_OwnOverrideableMethods()
  { String sClassIdentName = getClassIdentName();
    String ret = "\n\n/* J2C:ClassData.write_OwnOverrideableMethods()" 
               + "\n * This method may be called in C, it implements the dynamic call. \n*/\n";
    for(MethodOverrideable method1: inheritanceInfo.methodTable){
      Method method = method1.method;
      ret += method.sReturnTypeDefinition + " " + method.sCName + method.sMethodFormalListDefiniton;
      if(method.firstDeclaringClass.isInterface()){
        ret += "\n{ Mtbl_" + sClassIdentName + " const* mtbl = (Mtbl_" + sClassIdentName 
          + " const*)getMtbl_ObjectJc(ithis, sign_Mtbl_" + sClassIdentName + ");";
      } else {
        ret += "\n{ Mtbl_" + sClassIdentName + " const* mtbl = (Mtbl_" + sClassIdentName 
          + " const*)getMtbl_ObjectJc(&ythis->base.object, sign_Mtbl_" + sClassIdentName + ");";
      }
      //ret += "\n  ASSERT(mtbl != null && mtbl->head.sign == sign_Mtbl_" + sClassIdentName +  ");";
      if(method.returnType != null && method.returnType != CRuntimeJavalikeClassData.clazz_void.classTypeInfo){
        ret += "\n  return ";
      } else {
        ret += "\n  ";
      }
      ret += "mtbl->" + method.sNameUnambiguous + "(";
      if(method.firstDeclaringClass.isInterface()){
        ret += "ithis"; //it is of type ObjectJc* and declared in sMethodFormalListDefiniton
      } else {
        /**Method is not defined first in an interface, but in an super class. */
        ret += "(" + method.firstDeclaringClass.sClassNameC + "*)ythis";
      }
      if(method.paramsType != null) for(FieldData param: method.paramsType){
        ret += ", " + param.getName();
      }
      if(method.need_thCxt){ ret += ", _thCxt"; }
      ret += ");";
      ret += "\n}\n";
    }
    return ret;
  }
  
  
  
  
  /**Fills the {@link #methodsCheckOverriding} with the informations given in {@link #inheritanceInfo}.
   * This routine is called only after {@link #inheritanceInfo} is initalized in the constructor of this.
   * <img src="../../../../Java2C/img/InheritanceInfo_omdJava2C.png" />
   * In the picture ...TODO
   * @param inheritanceInfo of the adequate level, this routine is called recursively for all supers and interfaces.
   */
  private void fillMethodsOverrideable(ClassData.InheritanceInfo inheritanceInfo, String sPathToMtbl, String sPathToBase)
  { assert(inheritanceInfo != null);
    if(sClassNameJava.equals("ImplIfc"))
      stop();
    if(inheritanceInfo.superInheritance != null){
      /**Superclass - recursively. */
      ClassData.InheritanceInfo superInfo = inheritanceInfo.superInheritance;
      String sPathToMtblSuper = sPathToMtbl + superInfo.classData.getClassIdentName() + "."; 
      String superName = superInfo.classData.getClassIdentName();
      if(superName.equals("ObjectJc")){
        superName = "object";
      } else {
        superName = "super";
      }
      String sPathToBaseSuper = sPathToBase + ".base." + superName; 
      /**Adds the cast and fills the methods related to this class. Don't use the derived classes. */
      addCastToType(superInfo.classData, "(?)" + sPathToBaseSuper, "$$");  //NOTE: ? expected as embedded instance, because .base
      fillMethodsOverrideable(superInfo, sPathToMtblSuper, sPathToBaseSuper);
    }
    
    if(inheritanceInfo.ifcInheritance != null){
      /**Interfaces: */
      //Set<Map.Entry<String,ClassData.InheritanceInfo>> entrySet = inheritanceInfo.ifcInheritance.entrySet();
      for(InheritanceInfo ifcData : inheritanceInfo.ifcInheritance){
      //for(Map.Entry<String,ClassData.InheritanceInfo> entry : entrySet){
        //ClassData.InheritanceInfo ifcData = entry.getValue();
        String sPathToMtblIfc = sPathToMtbl + ifcData.classData.getClassIdentName() + "."; 
        String sPathToBaseIfc = sPathToBase + ".base." + ifcData.classData.getClassIdentName(); 
        /**Adds the cast and fills the methods related to this class. Don't use the interface classes. */
        addCastToType(ifcData.classData, "(?)." + sPathToBaseIfc.substring(1), "$$");
        fillMethodsOverrideable(ifcData, sPathToMtblIfc, sPathToBaseIfc);
      }
    }

    if(inheritanceInfo.methodTable != null){
      /**Captures the methods of this inheritance level and adds it to {@link #methodsCheckOverriding}
       * of this-ClassData (left yellow class in description picture). */
      for(int ix=0; ix<inheritanceInfo.methodTable.length; ix++)
      { /**Steps through the methodTable of the inheritance level: */
        MethodOverrideable src = inheritanceInfo.methodTable[ix];
        /**Gets from the original Method instance: */
        String sNameUnambiguous = src.method.sNameUnambiguous;
        String sNameJava = src.method.sJavaName;
        if(sNameJava.equals("processIfcMethod"))
          stop();
        /**Check whether the method is existing in the class with other signature, 
         * sets the ambigous of a searched instance with key <code>sNameJava+"?"</code>,
         * It is important for ambigous of another method of class with same Java name.: */
        testAndSetAmbiguousnessOfMethod(sNameJava);  //to detect whether a method with equal name is defined here additionally
        /**Check whether the method is registered as override-able of this ClassData, 
         * it is if the method was found in another base class already: */
        MethodOverrideCheck methodOverideable = searchOverrideableMethod(sNameJava, src.method.paramsType);
        if(methodOverideable == null){
          /**If not found yet, create. */
          methodOverideable = new MethodOverrideCheck(src.method, sPathToMtbl, sPathToBase);
          /**The method is not registered yet, register it. */
          Object oMethodOverideable = methodsCheckOverriding.get(sNameJava);
          if(oMethodOverideable != null){
            if(oMethodOverideable instanceof List<?>){
              @SuppressWarnings("unchecked")
            	List<MethodOverrideCheck> listMethodOverrideable = ((List<MethodOverrideCheck>)oMethodOverideable); 
            	listMethodOverrideable.add(methodOverideable);
            } else {
              /**No List, than only one method is registered, instance of MethodOverrideCheck.
               * Build a list with now 2 methods.
               */
              MethodOverrideCheck firstmethod = (MethodOverrideCheck)oMethodOverideable;
              List<MethodOverrideCheck> listmethods = new LinkedList<MethodOverrideCheck>();
              listmethods.add(firstmethod);
              listmethods.add(methodOverideable);
              methodsCheckOverriding.put(sNameJava, listmethods); //exchange with a list.
            }
          }
          else {
            methodsCheckOverriding.put(sNameJava, methodOverideable);
          }  
        }
        /**Adds an occurrence to the override-able method. This is the finite action of this part of routine. 
         * If an implementation is found in the class later, all that occurrences are handled.*/
        methodOverideable.listOccurence.add(new MethodOverrideCheck.MethodIndex(inheritanceInfo.methodTable, ix));
      }
    }

  }    
  
 
  /**copies the new override-able methods of this class in the inheritance info.
   * The methods were captured in {@link #methodsOverrideableNew}. It will be written
   * to {@link #inheritanceInfo}.{@link InheritanceInfo#methodTable}.
   * The order of the given is important for method table Mtbl in C
   * <br>
   * The {@link #methodsOverrideableNew} and the {@link #methodsCheckOverriding} were cleared
   * (set to null) because its content isn't necessary for translation the other classes.  
   */
  public void completeInheritanceWithOwnMethods()
  { if(methodsOverrideableNew != null){
      int zMethods = methodsOverrideableNew.size();
      if(inheritanceInfo == null){
        inheritanceInfo = new InheritanceInfo(this, null, null);
      }
      assert(inheritanceInfo.methodTable == null);  
      inheritanceInfo.methodTable = new MethodOverrideable[zMethods];
      int ix= 0;
      for(MethodOverrideable entry: methodsOverrideableNew){
        inheritanceInfo.methodTable[ix++] = entry;
      }
      methodsOverrideableNew = null; //delete it, it isn't necessary furthermore.
      methodsCheckOverriding = null; //it isn't also necessary furthermore.
    }  
  }
  
  
  
  /**copies the override-able methods given in param  in the inheritance info.
   * It will be written
   * to {@link #inheritanceInfo}.{@link InheritanceInfo#methodTable}.
   * <br>
   * The {@link #methodsOverrideableNew} and the {@link #methodsCheckOverriding} were cleared
   * (set to null) because its content isn't necessary for translation the other classes.  
   * @param methodNames List of names with that methods, which are to write in the Inheritanceinfo.
   *   The order is important for method table Mtbl in C
   */
  public void completeInheritanceWithListMethods(List<String> methodNames)
  { int zMethods = methodNames.size();
    if(inheritanceInfo == null){
      inheritanceInfo = new InheritanceInfo(this, null, null);
    }
    assert(inheritanceInfo.methodTable == null);  
    inheritanceInfo.methodTable = new MethodOverrideable[zMethods];
    int ix= 0;
    for(String name: methodNames){
      Method method = getMethodPerCname(name);
      if(method == null){
        throw new IllegalArgumentException("Error: methods-overrideable-entry in " + getClassCtype_s() + ".stc fault: " + name);
      }
      MethodOverrideable methodOverrideable = new MethodOverrideable(method, method.sImplementationName);
      inheritanceInfo.methodTable[ix++] = methodOverrideable;
    }
    methodsOverrideableNew = null; //delete it, it isn't necessary furthermore.
    methodsCheckOverriding = null; //it isn't also necessary furthermore.
  }
  
  
  
  /**returns the definition of the method-table of a superclass or interface as embedded struct.
   */
  private String gen_MethodTableSuperStruct()
  { String ret = "";
    if(inheritanceInfo != null) // && classDataP.inheritanceInfo.methodTable != null)
    { String sClassNameC = getClassIdentName();
      ret += "\n  Mtbl_" + sClassNameC + " mtbl" + sClassNameC + ";  //methods from "  + sClassNameC;
    }
    return ret;
  }    
  
  
  
  
  
  /**Helpfull for debugging in eclipse. The infos from Object.toString() is given too.
   * @see java.lang.Object#toString()
   */
  public String toString()
  { String sObject = super.toString();
    int posAt = sObject.indexOf('@');
    return sClassNameJavaFullqualified + "=" + sClassNameC + ": ClassData" + sObject.substring(posAt); 
  }
  
  
  
  /* (non-Javadoc)
   * @see org.vishia.java2C.JavaSources.ClassDataOrJavaSrcFile#getLocalIdents()
   */
  public LocalIdents getLocalIdents(String sClassName)
  { return classLevelIdents;
  }



  /**Implements {@link JavaSources.ClassDataOrJavaSrcFile#getJavaPkg()}
   */
  public JavaSrcTreePkg getJavaPkg()
  {
    return null; // it isn't a JavaSrcTreePkg
  }



  /**Implements {@link JavaSources.ClassDataOrJavaSrcFile#getTypeName()}
   */
  public String getTypeName()
  {
    return sClassNameJava;
  }



	/**This method returns null because for ready-to-use types this informations are not necessary.
	 * @see org.vishia.java2C.JavaSources.ClassDataOrJavaSrcFile#getReplaceCinfo()
	 */
	@Override
	public org.vishia.java2C.ConfigSrcPathPkg_ifc.Set getReplaceCinfo() {
		//It isn't necessary here.
		return null;
	}



	/**Returns false any time, because all informations are ready to use here.
	 * @see org.vishia.java2C.JavaSources.ClassDataOrJavaSrcFile#isToTranslate()
	 */
	@Override
	public boolean isToTranslate() {
		return false;
	}



	/**Writes the structure data of the class in file.stc.
	 * @param out destination
	 * @param recursion nesting level
	 * @throws IOException
	 */
	public void writeStructureClass(final BufferedWriter out, final int recursion) 
	throws IOException
	{ if(sClassNameJava.equals("TestWaitNotify"))
		  stop();
		out.append(indent(recursion));
	  if(bExternal){ out.append("extern "); }
	  if(isFinal){ out.append("final "); }
	  if(bEmbedded){ out.append("embedded "); }
	  if(isAbstract){ out.append("abstract "); }
  	if(isNonStaticInner){ out.append("nonStaticInner "); }  //only inner classes may be static
	  if("PFC".indexOf(creationMode) <0){
	  	//an anonymous class or class at statement block level:
	  	switch(creationMode){
	  	case 'Y': out.append("anonymous "); break;
	  	default: out.append("statementBlock:").append(creationMode).append(' ');
	  	}
		}
	  
	  out.append(bInterface ? "interface " : "class "); 
	  out.append(sClassNameJava + "; nameC="+ sClassNameC + "; argIdent=" + sArgSensitiveName + ";");
	  if(inheritanceInfo != null) {
	    if(inheritanceInfo.superInheritance != null){
	      ClassData superClass = inheritanceInfo.superInheritance.classData;
	      out.append(" extends " + superClass.sClassNameJavaFullqualified);
	    }
	    if(inheritanceInfo.ifcInheritance != null){
	      String sSeparator = " implements ";
	      //Set<Entry<String, InheritanceInfo>> entrySet = inheritanceInfo.ifcInheritance.entrySet();
	      for(InheritanceInfo ifcInheritance : inheritanceInfo.ifcInheritance) {
	      //for(Entry<String, InheritanceInfo> entry : entrySet) {
	        ClassData ifcClass = ifcInheritance.classData;
	        //ClassData ifcClass = entry.getValue().classData;
	        out.append(sSeparator + ifcClass.sClassNameJavaFullqualified);
	        sSeparator = ", ";
	      }
	    }
	  }
	  out.append(" //creationMode=").append(creationMode);
	  out.append(indent(recursion) + "{  ");
	  Set<Map.Entry<String, JavaSources.ClassDataOrJavaSrcFile>> typeSet = classLevelIdents.getTypeSet();
	  if(typeSet.size() >0)
	  { //out.append(indent(recursion+1) + "InnerClassList" + "{ ");
	    for(Map.Entry<String, JavaSources.ClassDataOrJavaSrcFile> innerClassEntry: typeSet)
	    { JavaSources.ClassDataOrJavaSrcFile innerClass = innerClassEntry.getValue();
	      String sName = innerClass.getClassData().getClassIdentName();
	      out.append(indent(recursion+2)).append("//Innerclass: ").append(sName);
	  	}
	  }  
	  if(innerClasses !=null){
	  	out.append(indent(recursion+1) + "InnerClass" + "{ ");
	    for(ClassData innerClassdata: innerClasses){
	      innerClassdata.writeStructureClass(out, recursion+2);
		  } 
	    out.append(indent(recursion+1)).append("}");
		}
	  if(classLevelIdents.fieldIdents != null)
	  { out.append(indent(recursion)).append("fieldIdents {");
	    Set<Entry<String,FieldData>> listEntries = classLevelIdents.fieldIdents.entrySet();
	    for(Entry<String,FieldData> field: listEntries)
	    { //String sName = field.getKey();
	      FieldData identInfos = field.getValue();
	      if(identInfos.getName().equals("fixArray"))
	        stop();
	      out.append( indent(recursion+1));
	      if(identInfos.nClassLevel != 1 || identInfos.nOuterLevel != 1){
	        out.append("//outer=" + identInfos.nOuterLevel);
	        out.append(" ,super=" + identInfos.nClassLevel);
	        out.append(": ");
		    }
	      out.append(identInfos.writeStruct()).append(";");
	      
	    }
	    out.append(indent(recursion)).append("}");
	  }  
	  
	  if(methods != null)
	  { out.append(indent(recursion+1)).append("methods {  ");
	    Set<Map.Entry<String,Object>> listEntries = methods.entrySet();
	    for(Map.Entry<String,Object> field: listEntries)
	    { String sKey = field.getKey();
	      Object methodEntry = field.getValue();
	      if(methodEntry instanceof Method)
	      { Method method2 = (Method)(methodEntry);
	        if(method2.sCName != null)
	        { //out.append( indent(recursion+2)).append("method;").append(sKey).append(";").append(method2.writeStruct() + "\n");
	          out.append( indent(recursion+2)).append(method2.writeStruct());
	        }
	      }
	      else //it is a list, otherwise ClassCastException!
	      { @SuppressWarnings("unchecked")
	      	List<Method> listMethod1 = (List<Method>)methodEntry;
	        for(Method method: listMethod1)
	        { if(method.sCName != null)
	          { //out.append( indent(recursion+2) + "method;" + sKey + ";" + method.writeStruct());
	            out.append( indent(recursion+2)).append( method.writeStruct());
	          }
	        }  
	      }
	    }
	    out.append(indent(recursion+1) +  "}\n" );
	  }
	  
	  if(inheritanceInfo != null && inheritanceInfo.methodTable != null){
	    out.append(indent(recursion+1) + "methods-overrideable {");
	    for(MethodOverrideable methodOverrideable: inheritanceInfo.methodTable){
	      out.append( indent(recursion+2) + methodOverrideable.method.sCName + ";");
	    }
	    out.append(indent(recursion+1) +  "}\n" );
	  }
	  
	  if(castableToType != null){
	  	out.append(indent(recursion+1) + "castTo {");
	    for( Map.Entry<String, CastInfo>entry: castableToType.entrySet()){
	    	CastInfo castInfo = entry.getValue();
	    	out.append( indent(recursion+2))
	    		.append(castInfo.castType.sClassNameJavaFullqualified).append(": ")
	    		.append(castInfo.modeAccessDst)
	    		.append(" \"").append(castInfo.pre + "?").append(castInfo.post + "\" ")
	    		.append(castInfo.modeAccessSrc)
	    		.append(";");
	  	}
	    out.append(indent(recursion+1) +  "}\n" );
	  }
	  if(castableFromType != null){
	  	out.append(indent(recursion+1) + "castFrom {");
	    for( Map.Entry<String, CastInfo>entry: castableFromType.entrySet()){
	    	CastInfo castInfo = entry.getValue();
	    	out.append( indent(recursion+2))
	  		.append(castInfo.castType.sClassNameJavaFullqualified).append(": ")
	  		.append(castInfo.modeAccessSrc)
	  		.append(" \"").append(castInfo.pre + "?").append(castInfo.post + "\" ")
	  		.append(castInfo.modeAccessDst)
	  		.append(";");
	
	  	}
	    out.append(indent(recursion+1) +  "}\n" );
	  }
	  
	  
	  
	  
	  /*
	  Set<Map.Entry<String,Object>> listUsedEnhancedRefTypes = usedEnhancedRefTypes.entrySet();
	  if(listUsedEnhancedRefTypes.size() >0)
	  { out.append(indent(recursion+1) +  "usedEnhancedRefTypes{\n" );
	    for(Map.Entry<String,Object> usedType: listUsedEnhancedRefTypes)
	    {
	      out.append(indent(recursion+2) + usedType.getKey() + ";\n" );
	    }
	    out.append(indent(recursion+1) +  "}\n" );
	  }
	  */
	  out.append(indent(recursion) + "}\n");
	}

  @Override public void setClassData(ClassData data){
  	throw new IllegalArgumentException("internal: ClassData twice");
  }

	/**Helper method for debugging. */
	private void stop(){}
  
  
}
