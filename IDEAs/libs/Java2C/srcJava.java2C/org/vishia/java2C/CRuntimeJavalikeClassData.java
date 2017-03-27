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
package org.vishia.java2C;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

/**This class contains all standard types. It is instanciated as singleton. 
 * Most of its members are static. */
public final class CRuntimeJavalikeClassData
{
  /**The singleton association will be set as first operation in the constructor.
   * It means, the instance is available, if it is firstly created. 
   * A null-check ensures, that the constructor is called only one time.
   */
  public static CRuntimeJavalikeClassData singleton;
  
  /**Base of all types contained in {@link LocalIdents} and therefore {@link ClassData},
   * which are defined in {@link Java2C_Main.CRuntimeJavalikeClassData}. This types and identifier
   * are accessible in all scopes. See also {@link #userTypes}.
   */
  public final LocalIdents stdTypes;

  /**This class is used for unknown types for auto-declaration of methods, 
   * which are called but not found in translated sources or *.stc-files. */
  public final static ClassData clazz_unknown = new ClassData("CRuntimeJavalike-std", "", "void","void", "V", '.', "stdarg", '?', "+final+unknown+primitive+");
  
  /**This type identifies a place-holder for variable arguments. */
  public final static ClassData clazz_va_argRaw = new ClassData("CRuntimeJavalike-std", "", "va_argRaw","va_argRaw", "v", '.', null, '$', "+final+primitive+");
  /**This type identifies the va_list type defined in stdarg.h of standard-C. */
  public final static ClassData clazz_va_list = new ClassData("CRuntimeJavalike-std", "org/vishia/bridgeC/", "Va_list","Va_listFW", "v", '.', "Fwc/fw_LogMessage", '$', "+final+embedded+");
  
  public final static ClassData clazz_void = new ClassData("CRuntimeJavalike-std", "", "void","void", "V", '.', "stdarg", '%', "+final+primitive+");
  
  /**Base Type represents an untyped pointer.  */
  public final static ClassData clazz_voidPtr = new ClassData("CRuntimeJavalike-std", "", "void*","void*", "V", '.', "stdarg", '*', "+final+primitive+");
  
  public final static ClassData clazz_MemC = new ClassData("CRuntimeJavalike-std", "", "MemC","MemC", "M", '.', "fw_MemC", '$', "+final+");
  
  /**Base Type represents an untyped pointer, which is volatile. This pointer can be used as argument for a CouncurrentLinkedQueue or such adequate. */
  public final static ClassData clazz_voidVolatile = new ClassData("CRuntimeJavalike-std", "", "voidVolatilePtr","void volatile*", "V", '.', "stdarg", '*', "+final+primitive+");
  
  public final static ClassData clazz_bool = new ClassData("CRuntimeJavalike-std", "", "boolean", "bool", "b", 'Z', null, '%', "+final+primitive+");
  public final static ClassData clazz_int8 = new ClassData("CRuntimeJavalike-std", "", "byte", "int8", "i", 'B', null, '%', "+final+primitive+");
  public final static ClassData clazz_int16 = new ClassData("CRuntimeJavalike-std", "", "short", "int16", "i", 'S', null, '%', "+final+primitive+");
  public final static ClassData clazz_uint16 = new ClassData("CRuntimeJavalike-std", "", "ushort", "uint16", "i", 'S', null, '%', "+final+primitive+");
  public final static ClassData clazz_int32 = new ClassData("CRuntimeJavalike-std", "", "int", "int32", "i", 'I', null, '%', "+final+primitive+");
  public final static ClassData clazz_int64 = new ClassData("CRuntimeJavalike-std", "", "long", "int64", "l", 'J', null, '%', "+final+primitive+");
  public final static ClassData clazz_int = new ClassData("CRuntimeJavalike-std", "", "int", "int", "i", 'I', null, '%', "+final+primitive+");
  public final static ClassData clazz_s0 = new ClassData("CRuntimeJavalike-std", "", "s0", "char const*", "z", 'z', null, '%', "+final+primitive+");   //String-Literal "xyz"
  public final static ClassData clazz_char = new ClassData("CRuntimeJavalike-std", "", "char", "char", "c", 'C', null, '%', "+final+primitive+");
  public final static ClassData clazz_float = new ClassData("CRuntimeJavalike-std", "", "float", "float", "f", 'F', null, '%', "+final+primitive+");
  public final static ClassData clazz_double = new ClassData("CRuntimeJavalike-std", "", "double", "double", "f", 'D', null, '%', "+final+primitive+");
  public final static ClassData clazzClassJc = new ClassData("CRuntimeJavalike-std", "java/lang/", "Class", "ClassJc", "Cl", 'L', "Jc/ReflectionJc", '*', "");
  public final static ClassData clazzObjectJc = new ClassData("CRuntimeJavalike-std", "java/lang/", "Object", "ObjectJc", "O", 'L', "Jc/ObjectJc", '*', "");
  public final static ClassData clazzStringJc = new ClassData("CRuntimeJavalike-std", "java/lang/", "String", "StringJc", "S", 's', "Jc/StringJc", 't', "+final+embedded+primitive+");
  public final static ClassData clazzByteStringJc = new ClassData("CRuntimeJavalike-std", "", "ByteStringJc", "ByteStringJc", "M", 'm', "Jc/StringJc", 'B', "+final+embedded+primitive+");
  //public final static ClassData clazzAllocInBlockJc = new ClassData("CRuntimeJavalike-std", "org/vishia/bridgeC/", "AllocInBlock", "BlockHeapJc", "X", '*', "BlockHeap/BlockHeapJc", '*', "+final+embedded+primitive+");
  
  { //clazzClassJc.addMethod("getEnclosingClass", "getEnclosingClass", 0, clazzClassJc.classTypeInfo);
    //clazzClassJc.addMethod("getDeclaredFields", "getDeclaredFields", 0, clazzClassJc.classTypeInfo);
	
  }
  
  { clazzObjectJc.needFinalize();  //it forces generation of finalize for all derived classes.
  	clazzObjectJc.addMethod("clone", "clone", Method.modeOverrideable, clazzObjectJc.classTypeInfo);
    clazzObjectJc.addMethod("equals", "equals", Method.modeOverrideable, clazz_bool.classTypeInfo, new ClassData[]{clazzObjectJc} );
    clazzObjectJc.addMethod("finalize", "finalize", Method.modeOverrideable, clazz_void.classTypeInfo);
    clazzObjectJc.addMethod("hashCode", "hashCode", Method.modeOverrideable, clazz_int32.classTypeInfo);
    clazzObjectJc.addMethod("toString", "toString", Method.modeOverrideable, clazzStringJc.classTypeInfo);
    clazzObjectJc.addMethod("wait", "waitInfinity", 0, clazz_void.classTypeInfo);
    clazzObjectJc.addMethod("wait", "wait", 0, clazz_void.classTypeInfo, new ClassData[]{clazz_int32} );
    clazzObjectJc.addMethod("notify", "notify", 0, clazz_void.classTypeInfo);
    clazzObjectJc.addMethod("getClass", "getClass", Method.modeNoThCxt, clazzClassJc.classTypeInfo);
    clazzObjectJc.completeInheritanceWithOwnMethods();
  }

  public final static ClassData clazzExceptionJc = new ClassData("CRuntimeJavalike-std", "java/lang/", "Exception", "ExceptionJc", "Exc", 'x', "Fwc/fw_Exception", '*', "");
  
  
  /**All classes which are defined by stc-files in CRuntimeJavalike: */
  //public final ClassData clazzStringBuilderJc; // = new ClassData("CRuntimeJavalike-std", "java/lang/", "StringBuilder", "StringBuilderJc", "Sb", 'b', "Jc/StringBuilderJc", '*', "+thx++ObjectJc+");
  //public final ClassData clazzIntegerJc;
  
  
  
  
  //public final ClassData clazzLogMessageFW; // = new ClassData("CRuntimeJavalike-std", "org/vishia/util/", "LogMessage", "LogMessageFW_i", "LogM", 'L', "Fwc/fw_LogMessage", '*', "+final+ObjectJc+");
  public final ClassData clazzLogMessageStream = new ClassData("CRuntimeJavalike-std", "org/vishia/msgDispatch/", "LogMessageStream", "LogMessageStream_FW", "Logs", 'L', "Fwc/fw_LogMessage", '*', "+final++ObjectJc+");
  public final ClassData clazzVaArgBuffer = new ClassData("CRuntimeJavalike-std", "org/vishia/bridgeC/", "VaArgBuffer", "VaArgBuffer", "Vargb", 'L', "MsgDisp/VaArgBuffer", '*', "+final+");
  //public final ClassData clazzInteger = new ClassData("CRuntimeJavalike-std", "java/lang/", "Integer", "IntegerJc", "Intg", 'L', "Jc/ObjectJc", '*', "");
  //public final ClassData clazzDouble = new ClassData("CRuntimeJavalike-std", "java/lang/", "Double", "DoubleJc", "Double", 'L', "Jc/ObjectJc", '*', "");
  //public final ClassData clazzMath = new ClassData("CRuntimeJavalike-std", "java/lang/", "Math", "MathJc", "Math", 'L', "Jc/ObjectJc", '*', "");
  //public final ClassData clazzArrays = new ClassData("CRuntimeJavalike-std", "java/util/", "Arrays", "ArraysJc", "Arrays", 'L', "Jc/ArraysJc", '*', "");
  //public final ClassData clazzDate = new ClassData("CRuntimeJavalike-std", "java/util/", "Date", "DateJc_s", "Date", 'L', "Jc/DateJc", '*', "+ObjectJc+");
  //public final ClassData clazzSimpleDateFormat = new ClassData("CRuntimeJavalike-std", "java/text/", "SimpleDateFormat", "SimpleDateFormatJc_s", "Dateformat", 'L', "Jc/DateJc", '*', "+ObjectJc+");
  public final ClassData clazzTextFieldPositionJc = new ClassData("CRuntimeJavalike-std", "java/text/", "FieldPosition", "TextFieldPositionJc_s", "FieldPos", 'L', "Jc/DateJc", '*', "+ObjectJc+");
  //public final ClassData clazzFormatterJc = new ClassData("CRuntimeJavalike-std", "java/util/", "Formatter", "FormatterJc_s", "Fm", 'L', "Jc/FormatterJc", '*', "+ObjectJc+");
  public final ClassData clazzListJc = new ClassData("CRuntimeJavalike-std", "java/util/", "List", "ListJc", "Li", 'L', "Jc/ListJc", '*', "");
  public final ClassData clazzIteratorJc = new ClassData("CRuntimeJavalike-std", "java/util/", "Iterator", "IteratorJc", "Iter", 'L', "Jc/ListJc", '*', "");
  public final ClassData clazzCollectionJc = new ClassData("CRuntimeJavalike-std", "java/util/", "Collection", "CollectionJc", "Collect", 'L', "Jc/ListJc", '*', "");
  //public final ClassData clazzConcurrentLinkedQueue = new ClassData("CRuntimeJavalike-std", "java/util/", "ConcurrentLinkedQueue", "ConcurrentLinkedQueueJc", "Clq", 'L', "Jc/ConcurrentLinkedQueueJc", '*', "");

  //public final ClassData clazzThreadJc = new ClassData("CRuntimeJavalike-std", "java/lang/", "Thread", "ThreadJc", "Th", 'L', "Jc/ThreadJc", '*', "");

  
  public final ClassData clazzMemC = new ClassData("CRuntimeJavalike-std", "org/vishia/bridgeC/", "MemC", "MemC", "M", 'm', "Fwc/fw_MemC", '$', "+final+embedded+xxxprimitive+");
  //public final ClassData clazzOS_TimeStamp = new ClassData("CRuntimeJavalike-std", "org/vishia/bridgeC/", "OS_TimeStamp", "OS_TimeStamp", "t", 't', "OSAL/inc/os_time", '$', "+final+embedded+xxxprimitive+");
  
  //public final ClassData clazzSystemJc = new ClassData("CRuntimeJavalike-std", "java/lang/", "System", "SystemJc", "System", 'L', "Jc/SystemJc", '*', "+final+");
    
  //public final ClassData clazzPrintStreamJc = new ClassData("CRuntimeJavalike-std", "java/io/", "PrintStream", "PrintStreamJc", "PrintSt", 'L', "Jc/SystemJc", '*', "+final+");
  //public final ClassData clazzFileWriterJc = new ClassData("CRuntimeJavalike-std", "java/io/", "FileWriter", "FileWriterJc_s", "fWr", 'L', "Jc/FileIoJc", '*', "+ObjectJc+");

  public static final FieldData fieldObjectJc = new FieldData("ObjectJc", clazzObjectJc, null, null, null, '.', '*', 0, null, '.', null);
  
  
  
  /**Typical 1-param arrays. */
  private final ClassData[] param_s0 = { clazz_s0};
  private final ClassData[] param_int8 = { clazz_int8};
  private final ClassData[] param_int16 = { clazz_int16};
  private final ClassData[] param_int = { clazz_int};
  private final ClassData[] param_int32 = { clazz_int32};
  private final ClassData[] param_char = { clazz_char};
  private final ClassData[] paramStringJc = { clazzStringJc};
  private final ClassData[] param_float = { clazz_float};
  private final ClassData[] param_double = { clazz_double};
  
  //private final FieldData field_voidP = new FieldData("void", clazz_void, '.', '*', 0, null, '.', null);
  //private final FieldData field_raw = new FieldData("raw", clazz_void, null, null, null, '.', '%', 0, null, '.', null);
  private final FieldData field_boolean = new FieldData("boolean", clazz_bool, null, null, null, '.', '%', 0, null, '.', null);
  private final FieldData field_int = new FieldData("int", clazz_int, null, null, null, '.', '%', 0, null, '.', null);
  private final FieldData field_int16 = new FieldData("int", clazz_int16, null, null, null, '.', '%', 0, null, '.', null);
  private final FieldData field_int32 = new FieldData("int", clazz_int32, null, null, null, '.', '%', 0, null, '.', null);
  private final FieldData field_int64 = new FieldData("int", clazz_int64, null, null, null, '.', '%', 0, null, '.', null);

  //public final FieldData ident_byte_AY = new FieldData("byte_AY", clazz_int8, '.', '%', 1, null, '%', clazz_int8); 
  private final FieldData field_byteY = new FieldData("int", clazz_int8, null, null, null, '.', 'X', 1, null, '%', null);
  private final FieldData field_intY  = new FieldData("int", clazz_int32, null, null, null,  '.', 'X', 1, null, '%', null);
  private final FieldData field_longY  = new FieldData("long", clazz_int64, null, null, null,  '.', 'X', 1, null, '%', null);
  private final FieldData field_charY = new FieldData("char", clazz_char, null, null, null, '.', 'X', 1, null, '%', null);
  private final FieldData fieldStringJc = new FieldData("StringJc", clazzStringJc, null, null, null, '.', 't', 0, null, '.', null);
  private final FieldData fieldz0 = new FieldData("char const*", clazz_s0, null, null, null, '.', 't', 0, null, '.', null);
  private final FieldData fieldStringJcNonPersist = new FieldData("StringJc", clazzStringJc, null, null, null, 'r', 't', 0, null, '.', null);
  private final FieldData fieldObjectY = new FieldData("ObjectJc", clazzObjectJc, null, null, null, '.', 'X', 1, null, '*', null);
  
  /**This field type identifies a place-holder for variable arguments. */
  public final FieldData field_va_argRaw = clazz_va_argRaw.classTypeInfo; 
  
  /**The type MemC is used in C only as embedded type. */
  private final FieldData field_MemC = new FieldData("MemC", clazzMemC, null, null, null, '.', '$', 0, null, '.', null);
  //private final FieldData fieldOS_TimeStamp = new FieldData("OS_TimeStamp", clazzOS_TimeStamp, null, null, null, '.', '$', 0, null, '.', null);

  public final Method methodASSERT = new Method(null, null, "assert", "assert", "ASSERT", null, Method.modeNoThCxt + Method.modeStatic, field_boolean, null, null, "");
  
  public final FieldData field_int_va_arg = new FieldData("int_va_arg", clazz_int, null, null, null, '.', '$', 1, null/*new String[]{"11"}*/, '$', null);
  
  class PkgReplacementInfo implements ConfigSrcPathPkg_ifc.Set{

  	private final String filePrefix, fileSuffix, namePrefix, nameSuffix, sStcFile;
  	
  	public PkgReplacementInfo(String filePrefix, String fileSuffix,
				String namePrefix, String nameSuffix, String sStcFile) {
			super();
			this.filePrefix = filePrefix;
			this.fileSuffix = fileSuffix;
			this.namePrefix = namePrefix;
			this.nameSuffix = nameSuffix;
			this.sStcFile = sStcFile;
		
		}

		@Override	public String getFilePrefix() { return filePrefix; }
		
		@Override	public String getFileSuffix() { return fileSuffix; }

		@Override	public String getNamePrefix() { return namePrefix; }

		@Override	public String getNameSuffix() { return nameSuffix; }
  	
		@Override public String getInputPath() { return null; }

		@Override
		public String getStcFile() {
			return sStcFile;
		}

		@Override public String toString(){ 
			return "@PkgReplacementInfo: " 
			+ filePrefix + (fileSuffix !=null ? "*" + fileSuffix : "") 
			+ "(" + namePrefix + "*" + nameSuffix
			+ (sStcFile != null ? ", stc=" + sStcFile : "")
			+ ")";
		}

  }
  
  
  ConfigSrcPathPkg_ifc.Set pkgInfoJc = new PkgReplacementInfo("Jc/", "Jc", "","Jc", null); 
  /**The replace info for all Java-types, which are defined in Jc/ObjectJc.h respectively Jc/ObjectJc.stc. */
  ConfigSrcPathPkg_ifc.Set pkgInfoObjectJc = new PkgReplacementInfo("Jc/ObjectJc", null, "","Jc", null); //Note: The 2. parameter suffixFile is null, The prefixFile is the filename.
  ConfigSrcPathPkg_ifc.Set pkgInfoFileIoJc = new PkgReplacementInfo("Jc/FileIoJc", null, "","Jc", null); //Note: The 2. parameter suffixFile is null, The prefixFile is the filename.
  ConfigSrcPathPkg_ifc.Set pkgInfoFwException = new PkgReplacementInfo("Fwc/fw_Exception", null, "","Jc", null); //Note: The 2. parameter suffixFile is null, The prefixFile is the filename.
  ConfigSrcPathPkg_ifc.Set pkgInfoFwc = new PkgReplacementInfo("Fwc/fw_", "", "","Fwc", null); 
  ConfigSrcPathPkg_ifc.Set pkgInfoBridgeC = new PkgReplacementInfo("Jc/bridgeC", "", null,"", null); 
  
//Java2C_Main.singleton.javaSources
  JavaSrcTreePkg javaSrcPkgOrg =           Java2C_Main.getOrAddRootPkg("org");
  JavaSrcTreePkg javaSrcPkgVishia =        javaSrcPkgOrg.getOrAddPkg("org/vishia/", "vishia", null);
  JavaSrcTreePkg javaSrcPkgVishiaMsgDispatch = javaSrcPkgVishia.getOrAddPkg("org/vishia/msgDispatch/", "msgDispatch", null);
  JavaSrcTreePkg javaSrcPkgVishiaUtil =    javaSrcPkgVishia.getOrAddPkg("org/vishia/util/", "util", null);
  JavaSrcTreePkg javaSrcPkgVishiaBridgeC = javaSrcPkgVishia.getOrAddPkg("org/vishia/bridgeC/", "bridgeC", null); //pkgInfoBridgeC);
  

  
  
  CRuntimeJavalikeClassData(RunRequiredFirstPass_ifc runRequiredFirstPass) 
  throws FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException, ParseException
  { 
    
    assert(CRuntimeJavalikeClassData.singleton == null);
    CRuntimeJavalikeClassData.singleton = this;  //now CRuntimeJavalike.singleton.stdTypes are able to access.
    
    /**Create the package for java.lang and java.util and add JavaSrc in the package tree,
     * which have its representation with stc-files. The stc-Files will be translated if the types are needed.
     */
    //JavaSrcTreePkg javaSrcPkgJavaStd = new JavaSrcTreePkg("java/", "java");
    JavaSrcTreePkg javaSrcPkgJavaStd = Java2C_Main.getOrAddRootPkg("java");
    
    JavaSrcTreePkg javaSrcPkgLang = javaSrcPkgJavaStd.getOrAddPkg("java/lang/", "lang", null);
    stdTypes = javaSrcPkgLang.getPkgLevelIdents();
    
    //Adds all standard java classes, this classes have its stc-File in CRuntimeJavalike.
    //Then only the standard classes are known without declaration 'java/lang'
    //javaSrcPkgLang.setFileJava(null, null, "*.java", pkgInfoJc, "Jc/", "Jc", "","Jc", null, false);
    javaSrcPkgLang.setFileJava(null, null, "Runnable.java", pkgInfoJc, "Jc/", "Jc", "","Jc", null, false);
    javaSrcPkgLang.setFileJava(null, null, "Thread.java", pkgInfoJc, "Jc/", "Jc", "","Jc", null, false);
    javaSrcPkgLang.setFileJava(null, null, "System.java", pkgInfoJc, "Jc/", "Jc", "","Jc", null, false);
    javaSrcPkgLang.setFileJava(null, null, "Math.java", pkgInfoJc, "Jc/", "Jc", "","Jc", null, false);
    
    JavaSrcTreePkg javaSrcPkgUtil = javaSrcPkgJavaStd.getOrAddPkg("java/util/", "util", null);
    javaSrcPkgUtil.setFileJava(null, null, "Date.java", pkgInfoJc, "Jc/", "Jc", "","Jc", null, false);
    javaSrcPkgUtil.setFileJava(null, null, "LinkedList.java", pkgInfoJc, "Jc/", "Jc", "","Jc", null, false);
    //javaSrcPkgUtil.setFileJava(null, null, "Formatter.java", pkgInfoJc, "Jc/", "Jc", "","Jc", "Jc/FormatterJc.stc", false);
    
    javaSrcPkgUtil.setFileJava(null, null, "Arrays.java", pkgInfoJc, "Jc/", "Jc", "","Jc", null, false);
        
    
    
    javaSrcPkgUtil.setFileJava(null, null, "IllegalFormatConversionException.java", pkgInfoJc, "Jc/", "Jc", "","Jc", "Jc/FormatterJc.stc", false);
    
    
    //
    //JavaSrcTreeFile javaSrcStringBuffer = javaSrcPkgLang.setFileJava(null, null, "StringBuffer.java", pkgInfoJc, "Jc/", "Jc", "","Jc", null, false);
    /**Adds all standard java classes, this classes have its stc-File in CRuntimeJavalike. */

    javaSrcPkgLang.setFileJava(null, null, "RuntimeException.java", pkgInfoFwException, "Fwc/fw_Exception", null, "","Jc", null, false);
    javaSrcPkgLang.setFileJava(null, null, "ClassCastException.java", pkgInfoFwException, "Fwc/fw_Exception", null, "","Jc", null, false);
    javaSrcPkgLang.setFileJava(null, null, "NullPointerException.java", pkgInfoFwException, "Fwc/fw_Exception", null, "","Jc", null, false);
    javaSrcPkgLang.setFileJava(null, null, "IndexOutOfBoundsException.java", pkgInfoFwException, "Fwc/fw_Exception", null, "","Jc", null, false);
    javaSrcPkgLang.setFileJava(null, null, "ArrayIndexOutOfBoundsException.java", pkgInfoFwException, "Fwc/fw_Exception", null, "","Jc", null, false);
    javaSrcPkgLang.setFileJava(null, null, "StringIndexOutOfBoundsException.java", pkgInfoFwException, "Fwc/fw_Exception", null, "","Jc", null, false);
    javaSrcPkgLang.setFileJava(null, null, "ArrayStoreException.java", pkgInfoFwException, "Fwc/fw_Exception", null, "","Jc", null, false);
    javaSrcPkgLang.setFileJava(null, null, "IllegalArgumentException.java", pkgInfoFwException, "Fwc/fw_Exception", null, "","Jc", null, false);
    javaSrcPkgLang.setFileJava(null, null, "NoSuchFieldException.java", pkgInfoFwException, "Fwc/fw_Exception", null, "","Jc", null, false);
    javaSrcPkgLang.setFileJava(null, null, "NoSuchMethodException.java", pkgInfoFwException, "Fwc/fw_Exception", null, "","Jc", null, false);
    javaSrcPkgLang.setFileJava(null, null, "IllegalStateException.java", pkgInfoFwException, "Fwc/fw_Exception", null, "","Jc", null, false);
    javaSrcPkgUtil.setFileJava(null, null, "IllegalFormatPrecisionException.java", pkgInfoFwException, "Fwc/fw_Exception", null, "","Jc", null, false);
    javaSrcPkgUtil.setFileJava(null, null, "IllegalFormatConversionException.java", pkgInfoFwException, "Fwc/fw_Exception", null, "","Jc", null, false);
    
    javaSrcPkgLang.setFileJava(null, null, "Float.java", pkgInfoObjectJc, "Jc/ObjectJc", null, "","Jc", null, false);
    
    JavaSrcTreePkg javaSrcPkgIO = javaSrcPkgJavaStd.getOrAddPkg("java/io/", "io", null);
    javaSrcPkgIO.setFileJava(null, null, "File.java", pkgInfoFileIoJc, "Jc/FileIoJc", null, "","Jc", null, false);
    javaSrcPkgIO.setFileJava(null, null, "FileDescriptor.java", pkgInfoFileIoJc, "Jc/FileIoJc", null, "","Jc", null, false);
    javaSrcPkgIO.setFileJava(null, null, "OutputStream.java", pkgInfoFileIoJc, "Jc/FileIoJc", null, "","Jc", null, false);
    javaSrcPkgIO.setFileJava(null, null, "FileOutputStream.java", pkgInfoFileIoJc, "Jc/FileIoJc", null, "","Jc", null, false);
    javaSrcPkgIO.setFileJava(null, null, "IOException.java", pkgInfoFwException, "Fwc/fw_Exception", null, "","Jc", null, false);
    javaSrcPkgIO.setFileJava(null, null, "FileNotFoundException.java", pkgInfoFwException, "Fwc/fw_Exception", null, "","Jc", null, false);
    javaSrcPkgIO.setFileJava(null, null, "UnsupportedEncodingException.java", pkgInfoFwException, "Fwc/fw_Exception", null, "","Jc", null, false);
    //
    JavaSrcTreePkg javaSrcPkgText = javaSrcPkgJavaStd.getOrAddPkg("java/text/", "text", null);
    javaSrcPkgText.setFileJava(null, null, "ParseException.java", pkgInfoFwException, "Fwc/fw_Exception", null, "","Jc", null, false);
    //
    
    
    //The functionality of the java-file FileSystem.java is described in the Jc/FileSystemJc.stc, and the headerfile Jc/FileSystemJc is used.
    javaSrcPkgVishiaUtil.setFileJava(null, null, "FileSystem.java", pkgInfoJc, "Jc/", "Jc", "","Jc", null, false);
    
    //The file ConcurrentLinkedQueue is contained as an derived class in org/vishia/bridgC:
    //javaSrcPkgVishiaBridgeC.setFileJava(null, null, "ConcurrentLinkedQueue.java", pkgInfoJc, "Jc/", "Jc", "", "Jc", null, false);
    
    //stdTypes.putClassType("java", javaSrcPkgJavaStd);
    
    stdTypes.putClassType(clazz_void);
    stdTypes.putClassType(clazz_MemC);
    stdTypes.putClassType(clazz_voidVolatile);
    stdTypes.putClassType(clazz_bool);
    stdTypes.putClassType(clazz_int32);
    stdTypes.putClassType(clazz_s0);
    
    clazz_uint16.addCastFromType(clazz_int8, "?", "%%");
    clazz_int16.addCastFromType(clazz_int8, "?", "%%");
    
    clazz_char.addCastFromType(clazz_int8, "((char)(?))", "%%");
    clazz_char.addCastFromType(clazz_int16, "((char)(?))", "%%");
    clazz_char.addCastFromType(clazz_uint16, "((char)(?))", "%%");
    
    clazz_int32.addCastFromType(clazz_int8, "?", "%%");
    clazz_int32.addCastFromType(clazz_int16, "?", "%%");
    clazz_int32.addCastFromType(clazz_uint16, "?", "%%");
    clazz_int32.addCastFromType(clazz_int, "?", "%%");
    
    clazz_int.addCastFromType(clazz_int8, "?", "%%");
    clazz_int.addCastFromType(clazz_int16, "?", "%%");
    clazz_int.addCastFromType(clazz_uint16, "?", "%%");
    clazz_int.addCastFromType(clazz_int32, "?", "%%");
    
    clazz_int64.addCastFromType(clazz_int8, "?", "%%");
    clazz_int64.addCastFromType(clazz_uint16, "?", "%%");
    clazz_int64.addCastFromType(clazz_int16, "?", "%%");
    clazz_int64.addCastFromType(clazz_int32, "?", "%%");
    clazz_int64.addCastFromType(clazz_int32, "?", "%%");
    
    clazz_float.addCastFromType(clazz_int8, "?", "%%");
    clazz_float.addCastFromType(clazz_uint16, "?", "%%");
    clazz_float.addCastFromType(clazz_int16, "?", "%%");
    clazz_float.addCastFromType(clazz_int32, "?", "%%");  //lesser significant digits
    clazz_float.addCastFromType(clazz_int64, "?", "%%");
    
    clazz_double.addCastFromType(clazz_int8, "?", "%%");
    clazz_double.addCastFromType(clazz_uint16, "?", "%%");
    clazz_double.addCastFromType(clazz_int16, "?", "%%");
    clazz_double.addCastFromType(clazz_int32, "?", "%%");
    clazz_double.addCastFromType(clazz_int64, "?", "%%");
    clazz_double.addCastFromType(clazz_float, "?", "%%");
    
    
    stdTypes.putClassType("long", "int64", clazz_int64, "%", null);
    
    stdTypes.putClassType("short", "int16", clazz_int16, "%", null);
    stdTypes.putClassType(clazz_uint16);
    stdTypes.putClassType("byte", "int8", clazz_int8, "%", null);
    stdTypes.putClassType("float", "float", clazz_float, "%", null);
    stdTypes.putClassType("double", "double", clazz_double, "%", null);
    stdTypes.putClassType("char", "char", clazz_char, "%", null);
    //stdTypes.putClassTypeStandard(clazzClassJc);
    stdTypes.putClassTypeStandard(clazzObjectJc);
    stdTypes.putClassType(clazz_va_argRaw);
    javaSrcPkgVishiaBridgeC.putClassType(clazz_va_argRaw);
    { /**NOTE: get() isn't a method, it is the access immediately. In Java a method is necessary: */
      clazz_va_list.addMethod("get", "!", Method.modeNoThCxt, clazz_va_list.classTypeInfo);
      javaSrcPkgVishiaBridgeC.putClassType(clazz_va_list);
    }
    
    { clazzExceptionJc.addMethod("getMessage", "getMessage", 0, clazzStringJc.classTypeInfo);
      clazzExceptionJc.addMethod("printStackTrace", "printStackTrace", 0, clazz_void.classTypeInfo);
      clazzExceptionJc.addMethod("ctorM", "ctorM", 0, clazzExceptionJc.classTypeInfo, paramStringJc);
  	//clazzExceptionJc.addMethod("printStackTrace", "printStackTrace_", 0, clazz_void.classTypeInfo);
    	stdTypes.putClassType("Exception", clazzExceptionJc);
    }
    
    stdTypes.putClassTypeStandard(clazzStringJc);
    stdTypes.putClassTypeStandard(clazzByteStringJc);

    
    javaSrcPkgLang.setFileJava(null, null, "StringBuffer.java", pkgInfoJc, "Jc/", "Jc", "","Jc", "Jc/StringBufferJc.stc", false);
    javaSrcPkgLang.setFileJava(null, null, "StringBuilder.java", pkgInfoJc, "Jc/", "Jc", "","Jc", "Jc/StringBuilderJc.stc", false);
    
    /**Initializes all classes which are defined by stc-files in CRuntimeJavalike: */
    {
    }  
    
    //stdTypes.putClassType("LinkedList", "LinkedListJc", null, "@", null);
    
    //stdTypes.putClassType("List", "LinkedListJc", clazzListJc, "@", null);
    clazzListJc.addMethod("add", "add_Collection", clazz_int32.classTypeInfo, new ClassData[]{clazz_int32, clazzObjectJc});
    clazzListJc.addMethod("add", "add", clazz_int32.classTypeInfo, new ClassData[]{});
    clazzListJc.addMethod("addAll", "addAll", clazz_int32.classTypeInfo, new ClassData[]{clazzCollectionJc});
    clazzListJc.addMethod("addAll", "addAll_i", clazz_int32.classTypeInfo, new ClassData[]{clazz_int32, clazzCollectionJc});
    clazzListJc.addMethod("clear", "clear", 0, clazz_int32.classTypeInfo);
    clazzListJc.addMethod("contains", "contains", clazz_int32.classTypeInfo, new ClassData[]{clazzObjectJc});
    clazzListJc.addMethod("containsAll", "containsAll", clazz_int32.classTypeInfo, new ClassData[]{clazzCollectionJc});
    clazzListJc.addMethod("equals", "equals", clazz_int32.classTypeInfo, new ClassData[]{clazzObjectJc});
    clazzListJc.addMethod("get", "get", clazz_int32.classTypeInfo, new ClassData[]{clazz_int32});
    clazzListJc.addMethod("hashCode", "hashCode", 0, clazz_int32.classTypeInfo);
    clazzListJc.addMethod("indexOf", "indexOf", clazz_int32.classTypeInfo, new ClassData[]{clazzObjectJc});
    clazzListJc.addMethod("isEmpty", "isEmpty", 0, clazz_int32.classTypeInfo);
    clazzListJc.addMethod("iterator", "iterator", 0, clazz_int32.classTypeInfo);
    clazzListJc.addMethod("lastIndexOf", "lastIndexOf", clazz_int32.classTypeInfo, new ClassData[]{clazzObjectJc});
    clazzListJc.addMethod("listIterator", "listIterator", 0, clazz_int32.classTypeInfo);
    clazzListJc.addMethod("listIterator", "listIterator_i", clazz_int32.classTypeInfo, new ClassData[]{clazz_int32});
    clazzListJc.addMethod("remove", "remove_i", clazz_int32.classTypeInfo, new ClassData[]{clazz_int32});
    clazzListJc.addMethod("remove", "remove_O", clazz_int32.classTypeInfo, new ClassData[]{clazzObjectJc});
    clazzListJc.addMethod("removeAll", "removeAll", clazz_int32.classTypeInfo, new ClassData[]{clazzCollectionJc});
    clazzListJc.addMethod("retainAll", "retainAll", clazz_int32.classTypeInfo, new ClassData[]{clazzCollectionJc});
    clazzListJc.addMethod("set", "set", clazz_int32.classTypeInfo, new ClassData[]{clazz_int32, clazzObjectJc});
    clazzListJc.addMethod("size", "size", 0, clazz_int32.classTypeInfo);
    clazzListJc.addMethod("subList", "subList", clazz_int32.classTypeInfo, new ClassData[]{clazz_int32, clazz_int32});
    clazzListJc.addMethod("toArray", "toArray", 0, clazz_int32.classTypeInfo);
    clazzListJc.addMethod("toArray", "toArray_Array", clazz_int32.classTypeInfo, new ClassData[]{});
    javaSrcPkgUtil.putClassType(clazzListJc);
    
    
    //stdTypes.putClassType("Iterator", "IteratorJc", clazzIteratorJc, "@", null);
    clazzIteratorJc.addMethod("hasNext", "hasNext", 0, clazz_bool.classTypeInfo);
    clazzIteratorJc.addMethod("next", "next", 0, clazzObjectJc.classTypeInfo);
    clazzIteratorJc.addMethod("remove", "remove", 0, clazz_void.classTypeInfo);
    javaSrcPkgUtil.putClassType(clazzIteratorJc);
    
    //stdTypes.putClassType("ArrayList", "ListJc", null, "@", null);
    //stdTypes.putClassType("LinkedList", "ArrayListJc", null, "@", null);
    //stdTypes.putClassType("ConcurrentLinkedQueue", "ConcurrentLinkedQueueJc", null, "@", null);

    { ClassData[] params = { clazz_int32};
      //clazzStringJc.addMethod("append", "append_i_thcStringJc", clazzStringBuilderJc.classTypeInfo, params );
    	clazzStringJc.addMethod("new", "new_BYIICharset", 0, clazzStringJc.classTypeInfo, new FieldData[]{ field_byteY, clazz_int32.classTypeInfo, clazz_int32.classTypeInfo, clazzStringJc.classTypeInfo} );
    	clazzStringJc.addMethod("new", "new_vIICharset", 0, clazzStringJc.classTypeInfo, new FieldData[]{ clazzByteStringJc.classTypeInfo, clazz_int32.classTypeInfo, clazz_int32.classTypeInfo, clazzStringJc.classTypeInfo} );
    	clazzStringJc.addMethod("new", "new_CY", 0, clazzStringJc.classTypeInfo, new FieldData[]{ field_charY} );
      clazzStringJc.addMethod("new", "new_CYI", 0, clazzStringJc.classTypeInfo, new FieldData[]{ field_charY, field_int, field_int} );
      clazzStringJc.addMethod("charAt", "charAt", Method.modeNoThCxt, clazz_char.classTypeInfo, param_int32 );
      clazzStringJc.addMethod("equals", "equals", Method.modeNoThCxt, clazz_int32.classTypeInfo, new ClassData[]{ clazzStringJc} );
      clazzStringJc.addMethod("indexOf", "indexOf_C", Method.modeNoThCxt, clazz_int32.classTypeInfo, new ClassData[]{ clazz_char} );
      clazzStringJc.addMethod("indexOf", "indexOf_CI", Method.modeNoThCxt, clazz_int32.classTypeInfo, new ClassData[]{ clazz_char, clazz_int32} );
      clazzStringJc.addMethod("indexOf", "indexOf_s", Method.modeNoThCxt, clazz_int32.classTypeInfo, new ClassData[]{ clazzStringJc} );
      clazzStringJc.addMethod("indexOf", "indexOf_sI", Method.modeNoThCxt, clazz_int32.classTypeInfo, new ClassData[]{ clazzStringJc, clazz_int32} );
      clazzStringJc.addMethod("startsWith", "startsWith", Method.modeNoThCxt, clazz_bool.classTypeInfo, new ClassData[]{ clazzStringJc} );
      clazzStringJc.addMethod("lastIndexOf", "lastIndexOf_C", Method.modeNoThCxt, clazz_int32.classTypeInfo, new ClassData[]{ clazz_char} );
      clazzStringJc.addMethod("lastIndexOf", "lastIndexOf_CI", Method.modeNoThCxt, clazz_int32.classTypeInfo, new ClassData[]{ clazz_char, clazz_int32} );
      clazzStringJc.addMethod("lastIndexOf", "lastIndexOf_s", Method.modeNoThCxt, clazz_int32.classTypeInfo, new ClassData[]{ clazzStringJc} );
      clazzStringJc.addMethod("lastIndexOf", "lastIndexOf_sI", Method.modeNoThCxt, clazz_int32.classTypeInfo, new ClassData[]{ clazzStringJc, clazz_int32} );
      clazzStringJc.addMethod("length", "length", Method.modeNoThCxt, clazz_int32.classTypeInfo);
      clazzStringJc.addMethod("size", "size", Method.modeNoThCxt, clazz_int32.classTypeInfo);
      clazzStringJc.addMethod("substring", "substring", clazzStringJc.classTypeInfo, new ClassData[]{ clazz_int32, clazz_int32} );
      clazzStringJc.addMethod("substring", "substring_I", clazzStringJc.classTypeInfo, param_int32 );
      clazzStringJc.addMethod("replace", "replace", clazzStringJc.classTypeInfo, new ClassData[]{ clazz_char, clazz_char} );
      clazzStringJc.addMethod("getChars", "getChars", 0, clazz_void.classTypeInfo, new FieldData[]{ field_int, field_int, field_charY, field_int} );
      clazzStringJc.addMethod("getBytes", "getBytes", 0, clazzByteStringJc.classTypeInfo);
      clazzStringJc.addMethod("getBytes", "getBytesEncoding", 0, clazzByteStringJc.classTypeInfo, paramStringJc);
      clazzStringJc.addMethod("format", "format_a", Method.modeStatic, fieldStringJcNonPersist, new FieldData[]{ fieldStringJc} );
      clazzStringJc.addMethod("format", "format_a", Method.modeStatic, fieldStringJcNonPersist, new FieldData[]{ fieldStringJc, field_va_argRaw} );
      clazzStringJc.addMethod("format", "format_a", Method.modeStatic, fieldStringJcNonPersist, new FieldData[]{ fieldStringJc, field_va_argRaw, field_va_argRaw} );
      clazzStringJc.addMethod("format", "format_a", Method.modeStatic, fieldStringJcNonPersist, new FieldData[]{ fieldStringJc, field_va_argRaw, field_va_argRaw, field_va_argRaw} );
      clazzStringJc.addMethod("format", "format_a", Method.modeStatic, fieldStringJcNonPersist, new FieldData[]{ fieldStringJc, field_va_argRaw, field_va_argRaw, field_va_argRaw, field_va_argRaw} );
      clazzStringJc.addMethod("format", "format_a", Method.modeStatic, fieldStringJcNonPersist, new FieldData[]{ fieldStringJc, field_va_argRaw, field_va_argRaw, field_va_argRaw, field_va_argRaw, field_va_argRaw} );
      clazzStringJc.addMethod("format", "format_a", Method.modeStatic, fieldStringJcNonPersist, new FieldData[]{ fieldStringJc, field_va_argRaw, field_va_argRaw, field_va_argRaw, field_va_argRaw, field_va_argRaw, field_va_argRaw} );
      clazzStringJc.addMethod("format", "format_a", Method.modeStatic, fieldStringJcNonPersist, new FieldData[]{ fieldStringJc, field_va_argRaw, field_va_argRaw, field_va_argRaw, field_va_argRaw, field_va_argRaw, field_va_argRaw, field_va_argRaw} );
      clazzStringJc.addMethod("format", "format_a", Method.modeStatic, fieldStringJcNonPersist, new FieldData[]{ fieldStringJc, field_va_argRaw, field_va_argRaw, field_va_argRaw, field_va_argRaw, field_va_argRaw, field_va_argRaw, field_va_argRaw, field_va_argRaw} );
      clazzStringJc.addCastFromType(clazz_s0, "s0_StringJc(?)", "t%");  //NOTE: clazz_s0 is designated as skalar, because no struct type_t should be built.
      clazzStringJc.addCastInitializerFromType(clazz_s0, "CONST_z_StringJc(?)", "t%");
      //done above: stdTypes.putClassType("String", clazzStringJc);
    }
    {
    	/*
      clazzConcurrentLinkedQueue.addMethod("ctorO", "ctorO_Clq", Method.modeStatic, clazzConcurrentLinkedQueue.classTypeInfo, new ClassData[]{clazzConcurrentLinkedQueue});
      clazzConcurrentLinkedQueue.addMethod("ctorO", "ctorO_MemC", Method.modeStatic, clazzConcurrentLinkedQueue.classTypeInfo, new FieldData[]{field_MemC});
      clazzConcurrentLinkedQueue.addMethod("add", "add", clazz_bool.classTypeInfo, new ClassData[]{clazz_voidVolatile});
      clazzConcurrentLinkedQueue.addMethod("poll", "poll", 0, clazz_voidVolatile.classTypeInfo);
      stdTypes.putClassType("ConcurrentLinkedQueue", clazzConcurrentLinkedQueue);
      */
    }
    
    
    {
      clazzMemC.addMethod("alloc", "!alloc_MemC", Method.modeNoThCxt|Method.modeStatic, field_MemC, param_int);
      javaSrcPkgVishiaBridgeC.putClassType(clazzMemC);
    }
    
    { //clazzPrintStreamJc.addMethod("println", "println_s0", clazz_void.classTypeInfo, new ClassData[]{ clazz_s0});
      //clazzPrintStreamJc.addMethod("println", "println_s", clazz_void.classTypeInfo, new ClassData[]{ clazzStringJc});
      //clazzPrintStreamJc.addMethod("println", "println_sb", clazz_void.classTypeInfo, new ClassData[]{ clazzStringBuilderJc});
      //javaSrcPkgIO.putClassType(clazzPrintStreamJc);
      //stdTypes.putClassType("PrintStream", "PrintStreamJc", clazzPrintStreamJc, "@", null);
    }  
    { 
      clazzVaArgBuffer.addMethod("get_va_list", "get_va_list", 0, clazz_va_list.classTypeInfo);
      clazzVaArgBuffer.addMethod("ctorM", "!ctorM_VaArgBuffer", Method.modeStatic, clazzVaArgBuffer.classTypeInfo, new FieldData[]{field_int});
      clazzVaArgBuffer.addMethod("clean", "clean", 0, clazz_void.classTypeInfo);
      clazzVaArgBuffer.addMethod("copyFrom", "copyFrom", 0, clazz_void.classTypeInfo, new FieldData[]{clazz_s0.classTypeInfo, clazz_va_list.classTypeInfo});
      javaSrcPkgVishiaBridgeC.putClassType(clazzVaArgBuffer);
    }
    { //stdTypes.putClassType("Integer", "IntegerJc", clazzInteger, "S", null);
      //clazzInteger.classLevelIdents.putClassElement("MAX_VALUE", "int32", clazz_int32, 's', '%', "%", 0, null, '.', clazzInteger);
      //clazzInteger.classLevelIdents.putClassElement("MIN_VALUE", "int32", clazz_int32, 's', '%', "%", 0, null, '.', clazzInteger);
      //clazzInteger.addMethod("")
      
    }
    { //stdTypes.putClassType("Double", "DoubleJc", clazzDouble, "S", null);
      //clazzDouble.addMethod("toString", "toString", Method.modeStatic, clazzStringJc.classTypeInfo, new ClassData[]{clazz_double});
      //clazzInteger.addMethod("")
      
    }
    /*
    { stdTypes.putClassType("Math", "MathJc", clazzMath, "S", null);
    clazzMath.addMethod("pow", "pow", Method.modeStatic, clazz_double.classTypeInfo, new ClassData[]{clazz_double, clazz_double});  
    clazzMath.addMethod("sin", "sin", Method.modeStatic|Method.modeNoThCxt, clazz_double.classTypeInfo, new ClassData[]{clazz_double});  
    //clazzInteger.addMethod("")
      
    }
    */
    { //clazzArrays.addMethod("binarySearch", "binarySearch_int_ii", Method.modeStatic, field_int, new FieldData[]{field_intY, field_int32, field_int32, field_int32});        
      //clazzArrays.addMethod("binarySearch", "binarySearch_int", Method.modeStatic, field_int, new FieldData[]{field_intY, field_int32});        
      //clazzArrays.addMethod("binarySearch", "binarySearch_int", Method.modeStatic, field_int, new FieldData[]{field_longY, field_int64});        
      //javaSrcPkgUtil.putClassType(clazzArrays);
      //stdTypes.putClassType("Arrays", "ArraysJc", clazzArrays, "S", null);
    }
    
    //Getting the type System, the System.stc will be parsed. 
    //In that file some other types are defined too.
  //stdTypes.getType("Object", stdTypes);  //parse the stc-file for IntegerJc etc.
    javaSrcPkgLang.putClassType(stdTypes.getType("StringBuilder", null));
    javaSrcPkgIO.putClassType(stdTypes.getType("java.io.File", null));
    javaSrcPkgLang.putClassType(stdTypes.getType("System", null));
    javaSrcPkgUtil.putClassType(stdTypes.getType("java.util.Arrays", null));

    
  }
  
} 

