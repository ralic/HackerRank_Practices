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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.vishia.mainCmd.Report;
import org.vishia.util.FileSystem;
import org.vishia.util.StringPart;
import org.vishia.util.StringPartFromFileLines;
import org.vishia.zbnf.ZbnfJavaOutput;
import org.vishia.zbnf.ZbnfParseResultItem;
import org.vishia.zbnf.ZbnfParser;

/**An instance of this class is filled with the result of parsing an <code>*.stc</code> file
 * which contains the structure informations of a already translated file.
 * <br>
 * Such a file is parsed inside {@link #readStructToClassData(String sClassFileName)} 
 * called inside {@link Java2C_Main#runrunFirstPass(java.io.File javaFile, boolean)},
 * if either the source <code>*.java</code> is not present to parse (for basics) 
 * or the destination file should not be generated because it exists and the 
 * source <code>*.java</code> file isn't newer than the already translated destination.
 * <br><br>
 * That routine parses the result and calls 
 * {@link org.vishia.zbnf.ZbnfJavaOutput#setOutputStrict(Object result, ZbnfParseResultItem, Report)}.
 * The result is this instance.
 * <br>
 * The syntax of parsing is given in the outside file <code>Java2Cstc.zbnf</code> in form:
 * <pre>
 * Structure::= J2C: structure of file-class: <$?fileName> { class <ClassData> } \e.
 * 
 * ClassData::= <*; ?nameJava> ; <$?nameC> ; 
 * \{ [ fieldIdents \{ [{ field; <field> }] \}]
 *    [ typeIdents \{ [{ <type> | class <ClassData?InnerClassData> }] \}]
 *    [ methods \{ [{ <method> }] \}]
 *    [ InnerClass \{ [{ class <ClassData?InnerClassData> }] \}]
 * \}.
 * 
 * field::= <1*?modeAccess><1*?modeArrayElement><1*?modeStatic>;
 *          [{\[ [\?\?<?arrayDimension>|<*\]?fixArraySizes>]\]}] ;
 *          <*; ?typeJava> ; <$?typeC> ; <$?name> ; 
 *          .
 *          
 * type::= type; <$?typeName>.
 * 
 * method::= method ; <*;?keyName> ; <$?javaName> ; <$?CName> ;
 *           return ; <field?returnType> 
 *           [{ param ( <field?param> ) }]
 *           .
 * </pre>.
 * All syntax/semantic elements have a representation in this class, because
 * {@link org.vishia.zbnf.ZbnfJavaOutput#setOutputStrict(Object result, ZbnfParseResultItem, Report)}.
 * sets the result of parsing directly in the instance <code>result</code> of this class.
 */ 
public class ReadStructure
{
	static private Map<String, Object> indexProcessedFiles = new TreeMap<String, Object>();
	
  private final Report msg;
  
  /**Aggregation to the global data of java2C translator. */
  final private Java2C_Main java2c;
  
  /**The parser for *.stc input files. The parser is initialized with syntax 
   * from file <code>Java2Cstc.zbnf</code> in directory given with cmd line args 
   * <code>-syntax:SYNTAXDIR</code> in the routine {@link #init()} 
   * called in {@link #execute()}. */
  final ZbnfParser parserStruct;

  //final private List<ClassData> listClassInFile;
  
  private String sSourceOfClassData;
  
  /**The Java source file for which this instance is created.
   * 
   */
  private JavaSrcTreeFile javaSrc;
  
  /**The classData of the top level are to add to userTypes, inner classes are add to classLevelIdents. */
  private LocalIdents fileLevelIdents;
    
  private LocalIdents pkgIdents;
  
  public static class Zbnf_EnvIdent
  {
    private String[] ident;
    
    private String delim;
    
    private String name;
    
    private Zbnf_EnvIdent subIdent;
    
    public Zbnf_EnvIdent new_subIdent(){ return this; } //return new Zbnf_EnvIdent(); }
    
    public void add_subIdent(Zbnf_EnvIdent value)
    {
      //ident[0] += value + delim;
      //subIdent = value;  
    }
    
    public void set_name(String value)
    {
      ident[0] += value + delim;
    }
    
  }
  
  
  /**Subclass to pour in the result of the <code>field::=...</code> subsyntax.
   * Only some singleton instances are created:
   * <ul>
   * <li>{@link Java2C_Main.ReadStructure.Zbnf_Method#returnType1} to save the return type.
   *     It is an own instance because the return type will be processed after alle param are processed.
   * <li>{@link Java2C_Main.ReadStructure.Zbnf_Method#zbnfParam} to save the current param.
   * <li>{@link Java2C_Main.ReadStructure.Zbnf_ClassData#field} to save the current field.
   * </ul>
   * They are reused, calling {@link #init()} before, if 
   * <ul>
   * <li>{@link Java2C_Main.ReadStructure.Zbnf_Method#new_param()}
   * <li>{@link Java2C_Main.ReadStructure.Zbnf_Method#new_returnType()} 
   * <li>{@link Java2C_Main.ReadStructure.Zbnf_ClassData#new_field()} 
   * </ul>
   * are called because any <code>&lt;field?...></code> were parsed with syntax 
   * <pre>
   * field::= <1*?modeAccess><1*?modeArrayElement><1*?modeStatic>;
   *          [{\[ [\?\?<?arrayDimension>|<*\]?fixArraySizes>]\]}] ;
   *          <*; ?typeJava> ; <$?typeC> ; <$?name> ; 
   * </pre>.
   * If the appropriate methods
   * <ul>
   * <li>{@link Java2C_Main.ReadStructure.Zbnf_Method#add_param(org.vishia.java2C.Java2C_Main.ReadStructure.Zbnf_Field)}
   * <li>{@link Java2C_Main.ReadStructure.Zbnf_Method#add_returnType(org.vishia.java2C.Java2C_Main.ReadStructure.Zbnf_Field)} 
   * <li>{@link Java2C_Main.ReadStructure.Zbnf_ClassData#add_field(org.vishia.java2C.Java2C_Main.ReadStructure.Zbnf_Field)} 
   * </ul>
   * are called, the content of the singleton is read out and stored 
   * in the appropriate destination data structure, that is {@link FieldData}.
   * This temporary instance is used straighten out, because the structure of syntax 
   * is not so opportune for storing in {@link FieldData} directly.
   */
  public static class Zbnf_Field
  { private char modeAccess, modeStatic, modeArrayElement;
    
    /**Stores the parsed result
     * inside {@link org.vishia.zbnf.ZbnfJavaOutput#setOutputStrict(Object result, ZbnfParseResultItem, Report)}.
     * To access the field from there via reflection, it should be public.
     */
    public String name, typeJava, instanceType;
    
    //private Zbnf_EnvIdent outerClassData;
    private String[] outerClassData = new String[1];;
    
    //private Zbnf_EnvIdent pkgType;
    private String[] pkgType = new String[1];
    
    private final Zbnf_EnvIdent envStore = new Zbnf_EnvIdent();
    
    /**If <code>&lt;*\]?fixArraySizes></code> is parsed, the String result is added to this list
     * inside {@link org.vishia.zbnf.ZbnfJavaOutput#setOutputStrict(Object result, ZbnfParseResultItem, Report)}.
     * To access the field from there via reflection, it should be public.
     */
    private List<String> listFixArraySizes = new LinkedList<String>();
    
    private int dimensionArrayOrFixSize;
    
    private Zbnf_Field()
    { modeAccess= '?'; dimensionArrayOrFixSize = 0;
    }
    
    /**Initializes the instance to use.*/
    void init()
    { modeArrayElement = modeStatic = modeAccess= '?'; 
      typeJava = ""; 
      instanceType = null;
      name = null;
      outerClassData[0] = "";
      pkgType[0] = "";
      listFixArraySizes.clear();
      dimensionArrayOrFixSize = 0;
    }

    public Zbnf_EnvIdent new_outerJava()
    { //return new Zbnf_EnvIdent(); 
      envStore.delim = ".";
      envStore.ident = outerClassData;
      outerClassData[0] = "";
      return envStore;
    }
    
    public void add_outerJava(Zbnf_EnvIdent value)
    {  envStore.ident = null;
      //outerClassData = value;
    }
    
    public Zbnf_EnvIdent new_packageType()
    { //return new Zbnf_EnvIdent(); 
      envStore.delim = "/";
      envStore.ident = pkgType;
      pkgType[0] = "";
      return envStore;
    }
    
    public void add_packageType(Zbnf_EnvIdent value)
    {  envStore.ident = null;
      //pkgType = value;
    }
    
    public void set_vaarg()
    {
    	typeJava = "#*";
    }
    
    /**Sets the parsed <code>modeAccess</code>. Only the first character is used and valid. */
    public void set_modeAccess(String value){ modeAccess = value.charAt(0);}
    /**Sets the parsed <code>modeArrayElement</code>. Only the first character is used and valid. */
    public void set_modeArrayElement(String value){ modeArrayElement = value.charAt(0);}
    /**Sets the parsed <code>modeStatic</code>. Only the first character is used and valid. */
    public void set_modeStatic(String value){ modeStatic = value.charAt(0);}
    
    //public void set_name(String value){ name = value; }

    /**FromZBNF: field::=  ~~~[??&lt;?arrayDimension>]~~~ : counts the {@link #dimensionArrayOrFixSize}.  */
    public void set_arrayDimension(){ dimensionArrayOrFixSize +=1; }
    
    /**FromZBNF: field::= ~~~  <*\] ?fixArraySizes> ~~~*/
    public void add_fixArraySizes(String value){ dimensionArrayOrFixSize += 1; listFixArraySizes.add(value); }
    
    /**FromZBNF: field::= ~~~ fixSize \[ <#?fixSize> \] ~~~*/
    public void set_fixSize(int value){ dimensionArrayOrFixSize = value; }
    
    public String toString(){ return typeJava; }
  }
  
  /**Subclass to pour in the result of the <code>type::=...</code> subsyntax.
   * Only a singleton instance is created. It is reused, calling {@link #init()} before, if 
   * <ul>
   * <li>{@link Java2C_Main.ReadStructure.Zbnf_ClassData#new_type()()} 
   * </ul>
   * is called because any <code>&lt;type></code> were parsed with syntax 
   * <pre>
   * type::= type; <$?typeName>.
   * </pre>.
   * If the appropriate method
   * <ul>
   * <li>{@link Java2C_Main.ReadStructure.Zbnf_ClassData#add_type(org.vishia.java2C.Java2C_Main.ReadStructure.Zbnf_Type)} 
   * </ul>
   * is called, the content of the singleton is read out and stored 
   * in the appropriate destination data structure, that is {@link ClassData}.
   * This temporary instance is used straighten out, because the structure of syntax 
   * is not so opportune for storing in {@link ClassData} directly.
   */
  private static class Zbnf_Type
  { public String typeName;
    void init(){typeName = null; }
  }
  
  /**Subclass to pour in the result of the <code>method::=...</code> subsyntax.
   * Only a singleton instance is created. It is reused, calling {@link #init()} before, if 
   * <ul>
   * <li>{@link Java2C_Main.ReadStructure.Zbnf_ClassData#new_method()} 
   * </ul>
   * is called because any <code>&lt;method></code> were parsed with syntax 
   * <pre>
   * method::= method ; <*;?keyName> ; <$?javaName> ; <$?CName> ;
   *           return ; <field?returnType> 
   *           [{ param ( <field?param> ) }]
   * </pre>.
   * If the appropriate methods
   * <ul>
   * <li>{@link Java2C_Main.ReadStructure.Zbnf_ClassData#add_method(org.vishia.java2C.Java2C_Main.ReadStructure.Zbnf_Method)} 
   * </ul>
   * is called, the content of the singleton is read out and stored 
   * in the appropriate destination data structure, that is {@link Method}.
   * This temporary instance is used straighten out, because the structure of syntax 
   * is not so opportune for storing in {@link Method} directly.
   */
  public static class Zbnf_Method
  {
    //private final ReadStructure outer;

  	private final String sClassIdentNameC;
  	
    private String sNameJava, sCName, sNameUnambiguous, sImplementationName, sOverriddenName, sKeyName; 
    
    private boolean bClassSuffixName;
    
    private String sDefPkg, sDefClass, sDefMethodUnambigous;
    
    //private final List<FieldData> listParams = new LinkedList<FieldData>();
    
    private final Zbnf_Field returnType1 = new Zbnf_Field();
    
    private List<Zbnf_Field> zbnfParam;
    
    private int modifier;
    
    public String implementationSuffix;
    
    public Zbnf_Field new_returnType(){ returnType1.init(); return returnType1; }
    public void add_returnType(Zbnf_Field dummy){} //NOTE: nothing to do, let it filled.
    
    public Zbnf_Field new_param(){ 
    	if(zbnfParam==null){ zbnfParam = new LinkedList<Zbnf_Field>(); }
    	Zbnf_Field param1 = new Zbnf_Field();
    	return param1; 
    }
    
    public void add_param(Zbnf_Field param1)
    {
      zbnfParam.add(param1);
    }
    
    public void set_keyName(String value){ sKeyName = value; }
    
    /**From ZBNF: method::= ... <$?javaName>... */
    public void set_javaName(String value){ sNameJava = value; }
    
    public void set_NameUnambiguous(String value){ sNameUnambiguous = value; }
    
    public void set_CName(String value){ sCName = value; }
    
    public void set_ImplementationName(String value){ sImplementationName = value; }
    
    
    
    public void set_OverriddenName(String value){ sOverriddenName = value; }
    
    /**From ZBNF: method::= ... mode: { ... noThCxt<?modeNoThCxt>...} */
    public void set_modeNoThCxt(){modifier |= Method.modeNoThCxt; }
    
    /**From ZBNF: method::= ... mode: { ... overrideable<?modeOverrideable>...} */
    public void set_modeOverrideable(){modifier |= Method.modeOverrideable; }
    
    /**From ZBNF: method::= ... mode: { ... returnThis<?modeReturnThis>...} */
    public void set_modeReturnThis(){modifier |= Method.modeReturnThis; }
    
    /**From ZBNF: method::= ... mode: { ... returnThis<?modeReturnThis>...} */
    public void set_modeReturnNew(){modifier |= Method.modeReturnNew; }
    
    /**From ZBNF: method::= ... mode: { ... returnThis<?modeReturnThis>...} */
    public void set_modeReturnNonPersistring(){modifier |= Method.modeReturnNonPersistring; }
    
    /**From ZBNF: method::= ... mode: { ... static<?modeStatic>...} */
    public void set_modeStatic(){modifier |= Method.modeStatic; }
    
    /**From ZBNF: method::= ... mode: { ... noStacktrace<?modeNoStacktrace>...} */
    public void set_modeNoStacktrace(){modifier |= Method.modeNoStacktrace; }
    
    /**From ZBNF: method::= ... mode: { ... ctor<?modeCtor>...} */
    public void set_modeCtor(){modifier |= Method.modeCtor; }
    
    /**From ZBNF: method::= ... mode: { ... <?modeCtorNonStatic>...} */
    public void set_modeCtorNonStatic(){modifier |= Method.modeCtorNonStatic; }
    
    /**From ZBNF: method::= ... mode: { ... <?modeCtorAnonymous>...} */
    public void set_modeCtorAnonymous(){modifier |= Method.modeCtorAnonymous; }
    
    /**From ZBNF: method::= ... <$?unambigouosName>... */
    public void set_unambigouosName(String value){ sNameUnambiguous = sNameJava + value; }
    
    /**From ZBNF: method::= ... <$?suffixName>... */
    public void set_suffixName(String value)
    { if(sNameUnambiguous == null){ sNameUnambiguous = sNameJava; } 
      sCName = sNameUnambiguous + value; 
    }
    
    
    /**From ZBNF: method::= ... [$<?classSuffixName>]... */
    public void set_classSuffixName()
    { if(sNameUnambiguous == null){ sNameUnambiguous = sNameJava; } 
      if(sCName == null){ sCName = sNameUnambiguous; } 
      sCName += sClassIdentNameC; 
      bClassSuffixName = true;
    }
    
    /**From ZBNF: method::= ... <.toLastCharIncl:/?defPkg>... */
    public void set_defPkg(String value){ sDefPkg = value; }
    
    /**From ZBNF: method::= ... <*\.?defClass>... */
    public void set_defClass(String value){ sDefClass = value; }
    
    /**From ZBNF: method::= ... <$?defMethodUnambigous>... */
    public void set_defClassAndMethodUnambigous(String value)
    { sDefMethodUnambigous = value; 
    }
    
    
    //Zbnf_Method(Java2C_Main java2c)
    //{ this.java2c = java2c;
    Zbnf_Method(String sClassIdentNameC) //ReadStructure outer)
    { this.sClassIdentNameC = sClassIdentNameC;
    	//this.outer = outer;
    }
    
    private void init()
    { returnType1.init();
      //param.init();
      //listParams.clear();
      sNameJava = sCName = sKeyName = sNameUnambiguous = sOverriddenName = sDefMethodUnambigous = null;
      sDefClass = sDefPkg = null;
      modifier = 0;
    }
  }
  
  /** @Uml=composite*/
  //final Zbnf_Method zbnfMethod; 
  
  public static class Zbnf_Cast
  {
  	public String typeJava;
  	public String accessDst;
  	public String accessSrc;
  	public String castExpr;
  	
  	void init()
  	{ 
  		
  	}
  }
  
  
  /**Subclass to pour in the result of the <code>ClassData::=...</code> subsyntax.
   * Only a singleton instance {@link Java2C_Main.ReadStructure#zbnfClassData} is created. 
   * It is reused, calling {@link #init()} before, if 
   * <ul>
   * <li>{@link Java2C_Main.ReadStructure#new_ClassData()} 
   * </ul>
   * is called because any <code>&lt;ClassData>></code> were parsed with syntax 
   * <pre>
   * ClassData::= <*; ?nameJava> ; <$?nameC> ; 
   * \{ [ fieldIdents \{ [{ field; <field> }] \}]
   *    [ typeIdents \{ [{ <type> | class <ClassData?InnerClassData> }] \}]
   *    [ methods \{ [{ <method> }] \}]
   *    [ InnerClass \{ [{ class <ClassData?InnerClassData> }] \}]
   * \}.
   * </pre>.
   * If the appropriate method
   * <ul>
   * <li>{@link Java2C_Main.ReadStructure#add_ClassData(org.vishia.java2C.Java2C_Main.ReadStructure.Zbnf_ClassData)} 
   * </ul>
   * is called, the content of the singleton is read out and stored 
   * in the appropriate destination data structure, that is {@link ClassData}.
   * This temporary instance is used straighten out, because the structure of syntax 
   * is not so opportune for storing in {@link ClassData} directly.
   */
  public static class Zbnf_ClassData
  { 
    private String sFileName; 
    
    /**Some properties. */
    private boolean bIsStaticInstance, bFinal, bInterface, bStaticInner, bExtern, bEmbedded, bAbstract, bConst;
    private boolean nonStaticInner, anonymous;
    
    private char intension;
    
    private final ReadStructure outer;
    
    private String sNameC, sClassIdentNameC;
    
    public String header, nameJava, argIdent;
    
    /**temporary list of IdentInfos to add to ClassData. */
    private final List<Zbnf_Field> listFields = new LinkedList<Zbnf_Field>();
    
    /**temporary list of Method to add to ClassData. */
    private final List<Zbnf_Method> listMethods = new LinkedList<Zbnf_Method>();
    
    /**temporary list of Method to add to ClassData. */
    private final List<String> listMethodsOverrideableC = new LinkedList<String>();
    
    /**The ClassData were set if the first field, method is set.
     * it is because the ClassData of the own class may be necessary in the own fields, methods.
     */
    private ClassData classData;
    
    private String sSuperClass;
    
    private List<String> listInterfaceClass;
    
    private final Zbnf_Field zbnf_field = new Zbnf_Field();
    
    private final Zbnf_Type zbnf_type = new Zbnf_Type();
    
    private List<Zbnf_ClassData> zbnfInnerClassData; 
    
    private final List<Zbnf_Cast> zbnfCastTo = new LinkedList<Zbnf_Cast>();
    
    private final List<Zbnf_Cast> zbnfCastFrom = new LinkedList<Zbnf_Cast>();

    private final ClassData outerClass;
    
    private Zbnf_ClassData(ClassData outerClass, ReadStructure outer)
    { assert(false);
    	this.outerClass = outerClass;
      this.outer= outer;
    }
    
    private Zbnf_ClassData()
    { this.outerClass = null;
    	this.outer= null;
    }
  
    
    /**From ZBNF: ClassData::= ... interface<?interface>... */
    public void set_interface(){ bInterface = true; }
    
    /**From ZBNF: ClassData::= ... final<?final>... */
    public void set_final(){ bFinal = true; }
    
    /**From ZBNF: ClassData::= ... extern<?extern>... */
    public void set_extern(){ bExtern = true; }
    
    /**From ZBNF: ClassData::= ... embedded<?embedded>... */
    public void set_embedded(){ bEmbedded = true; }
    
    /**From ZBNF: ClassData::= ... embedded<?embedded>... */
    public void set_abstract(){ bAbstract = true; }
    
    /**From ZBNF: ClassData::= ... embedded<?embedded>... */
    public void set_const(){ bConst = true; }
    
    /**From ZBNF: ClassData::= ... embedded<?embedded>... */
    public void set_nonStaticInner(){ nonStaticInner = true; }
    
    /**From ZBNF: ClassData::= ... embedded<?embedded>... */
    public void set_anonymous(){ anonymous = true; intension = 'Y'; }
    
    /**From ZBNF: ClassData::= ... embedded<?embedded>... */
    public void set_statementBlock(String value){ intension = value.charAt(0); }
    
    /**From ZBNF: ClassData::=  nameC = <$?nameC>.
     * Sets the {@link #sClassIdentNameC} too. 
     * Note: if the nameC ends with "_s" or "_i", then the {@link #sClassIdentNameC} is without this suffix.
     * */
    public void set_nameC(String value){
    	sNameC = value;
    	if(sNameC.endsWith("_s") || sNameC.endsWith("_i"))
      { sClassIdentNameC = sNameC.substring(0, sNameC.length()-2); }
      else{ sClassIdentNameC = sNameC; }
    }
    
    /**From ZBNF: ClassData::= ... extends <$?superClassC>... */
    public void set_superClass(String value){ sSuperClass = value; }
    
    /**From ZBNF: ClassData::= ... implements { <?interfaceClassC> ? ,}... */
    public void add_interfaceClass(String value)
    { if(listInterfaceClass == null){ listInterfaceClass = new LinkedList<String>(); }
      listInterfaceClass.add(value); 
    }
    
    public Zbnf_Field new_field()
    { return new Zbnf_Field();
    	//zbnf_field.init();
      //return zbnf_field;
    }
    
    public void add_field(Zbnf_Field field) throws IllegalArgumentException, IllegalAccessException, InstantiationException, ParseException
    { listFields.add(field);      
    }

    /**From ZBNF: ClassData::= ... methods \{ [{ <method> }] \}... */
    public Zbnf_Method new_method()
    { return new Zbnf_Method(sClassIdentNameC);
    }
    
    /**From ZBNF: ClassData::= ... methods \{ [{ <method> }] \}... 
     * @throws ParseException */
    public void add_method(Zbnf_Method method) 
    { listMethods.add(method);
    }
    
    /**From ZBNF: ClassData::= ... { <$?methodOverridableC> ; }... */
    public void add_methodOverridableC(String value){ listMethodsOverrideableC.add(value); }
    
    /**From ZBNF: ClassData::= ... castTo \{ [{ castTo> }] \}... */
    public Zbnf_Cast new_castTo()
    { return new Zbnf_Cast(); 
    }
    
    
    public void add_castTo(Zbnf_Cast value)
    { zbnfCastTo.add(value);
    }
    
    
    /**From ZBNF: ClassData::= ... castTo \{ [{ castTo> }] \}... */
    public Zbnf_Cast new_castFrom()
    { return new Zbnf_Cast(); 
    }
    
    
    public void add_castFrom(Zbnf_Cast value)
    { zbnfCastFrom.add(value);
    }
    
    
    public Zbnf_Type new_type()
    { zbnf_type.init();
      return zbnf_type; 
    }
    
    /**A Inner class is detected in ZBNF-parse-result because < ClassData?InnerClassData >.
     * Based on this, an empty instance of Zbnf_ClassData is returned to fill it. 
     * @return
     */
    public Zbnf_ClassData new_InnerClassData()
    { /**creates the outer class. */
      /*
    	createClassDataIfNotDone();
      if(zbnfInnerClassData == null) 
      { zbnfInnerClassData = new Zbnf_ClassData(classData, outer); }
      zbnfInnerClassData.init(sFileName);
      return zbnfInnerClassData;
      */
    	return new Zbnf_ClassData();
    }

    /**The inner class is filled now. Invoked from ZbnfJavaOutput, creates a new ClassData instance, fills it with given Input
     * and add it to the outer class clazzLevelEndents.
     * @param zbnfInput
     */
    public void add_InnerClassData(Zbnf_ClassData zbnfInput)
    { if(zbnfInnerClassData ==null){ zbnfInnerClassData = new LinkedList<Zbnf_ClassData>(); }
    	zbnfInnerClassData.add(zbnfInput);
    }

    public void add_type(Zbnf_Type value)
    { 
      
    };
    
    
    
    //public void set_className(String value){ sName = value; }
    
    private void init(String sFileName)
    { this.sFileName = sFileName; sNameC = nameJava = null; bIsStaticInstance = false; bFinal = false;
      bFinal = bIsStaticInstance = bInterface = bExtern = bEmbedded = bAbstract = bConst = false;
      sSuperClass = sNameC = nameJava = null;
      if(listInterfaceClass != null) { listInterfaceClass.clear(); }
      listFields.clear();
      listMethods.clear();
      listMethodsOverrideableC.clear();
      zbnf_field.init();
      zbnf_type.init();
      //if(zbnfInnerClassData != null){ zbnfInnerClassData.init(sFileName); }
      classData = null;
    }
    
    void stop(){}
  }
  
  public static class ZbnfToplevel
  {
	  /** @Uml=composite. */
	  final private List<Zbnf_ClassData> zbnfClassData = new LinkedList<Zbnf_ClassData>(); 
	  
	  public String sFileName;
	  
	  /**The package to which the classes are associated in the stc-file. */
	  private String packageStc;
	  
	  public void set_package(String value){ packageStc = value; }
	  
	  /**set content, able to call from ZbnfJavaOutput. */
	  public void set_fileName(String value)
	  { sFileName= value; }
	  
	  
	  public void set_version(float value)
	  { if(value < 0.939)
	    { throw new IllegalArgumentException("Version conflict."); }
	  }
	  
	  public void set_encoding(String value)
	  {
	    
	  }
	  
	  public Zbnf_ClassData new_ClassData()
	  { //ZbnfClassData obj = new ZbnfClassData();
	    //obj.sFileName = sFileName;
	    //return obj;
	    //zbnfClassData.init(sFileName);
	    return new Zbnf_ClassData();
	  }
	  
	  /**Invoked from ZbnfJavaOutput, creates a new ClassData instance, fills it with given Input
	   * and add it to the userTypes.
	   * @param zbnfInput
	   */
	  public void add_ClassData(Zbnf_ClassData zbnfInput)
	  {
	  	zbnfClassData.add(zbnfInput);
	  }
	  
	  
  }
  
  //ReadStructure(Java2C_Main java2c, List<ClassData> listClassInFile, Report msg)
  ReadStructure(Java2C_Main java2c, Report msg, String sSyntaxPath) throws ParseException
  { this.java2c = java2c;
    //this.listClassInFile = listClassInFile;
    this.msg = msg;
    parserStruct = new ZbnfParser(msg, 10);
    parserStruct.setReportIdents(Report.error, Report.info, Report.fineDebug, Report.fineDebug);
    { File fileSyntaxStruct = null;
      fileSyntaxStruct= new File(sSyntaxPath + "/Java2Cstc.zbnf"); //"../../srcJava/org/vishia/Java2C/Java2C.zbnf");
      String sSyntaxStruct;
      sSyntaxStruct = FileSystem.readFile(fileSyntaxStruct);
      if(sSyntaxStruct == null)
      { throw new IllegalArgumentException("syntaxfile not found, error arg -syntax:" + sSyntaxPath + "/Java2Cstc.zbnf");
      }
      else
      { parserStruct.setSyntax(sSyntaxStruct);
        //parser.reportSyntax(console);
      }
    }  

  }

  
  
  /**reads the structure of a class from a *.stc-File and save it to the {@link #userTypes}-{@link ClassData}.
   * @param sClassFileName The name of the Java class.
   * @throws FileNotFoundException
   * @throws IOException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @throws ParseException 
   */
  public void readStructToClassData(JavaSrcTreeFile javaSrc, String sClassFileName, File fileStruct) 
  throws FileNotFoundException, IOException, IllegalArgumentException, IllegalAccessException, InstantiationException, ParseException
  { //???
    //ClassData classForwardOrExternal = new ClassData(sClassName, sClassName, sClassName, null, "+extern+");
    //The put class will be replaced if it is found.
    //userTypes.putClassType(sClassName, sClassName, classForwardOrExternal, "C", null);
    
    //File fileStruct = new File(sPathOut + sClassFileName + ".stc");
    if(fileStruct == null){
      throw new ParseException("no stc-File found for: " 
          + javaSrc.getFileJava()+ ", header: " + javaSrc.getFileNameC(), 0);
    }
    Report console = msg;
    String sAbsPath = fileStruct.getAbsolutePath();
    Object sTest = indexProcessedFiles.get(sAbsPath);
    if(sTest !=null)
    	stop();
    indexProcessedFiles.put(sAbsPath, sAbsPath);
    this.sSourceOfClassData = sAbsPath; 
    this.javaSrc = javaSrc;
    if(javaSrc.getPublicClassName().equals("InspcDataExchange"))
    	stop();
    this.pkgIdents = javaSrc.getPkgLevelTypes();
    this.fileLevelIdents = new LocalIdents(javaSrc.getPkgPath()+ "/@/");
    this.fileLevelIdents.putClassTypesAll(pkgIdents); //copy because used types imported in file are added.
    if(fileStruct.exists())
    { StringPart spStruct = new StringPartFromFileLines(fileStruct, 100000, null, null);
      boolean bOk = parserStruct.parse(spStruct);
      if(!bOk)
      { console.writeError("parse error in file:" + fileStruct.getAbsolutePath() + ": " + parserStruct.getSyntaxErrorReport());
      }
      else
      { console.writeInfo("parse OK:" + fileStruct.getAbsolutePath() + ".");
        parserStruct.reportStore(console, Report.debug);
        ZbnfParseResultItem zbnfStruct = parserStruct.getFirstParseResult();

        final ZbnfToplevel zbnfToplevel = new ZbnfToplevel();
        ZbnfJavaOutput.setOutputStrict(zbnfToplevel, zbnfStruct, console);
        postPrepare(zbnfToplevel);
        writeResultAsSecondToCompare(zbnfToplevel, fileStruct, javaSrc.getPkgPath(), javaSrc.getFileNameC());
      }
    } else
    { console.writeInfoln("read stc - file not found: " + fileStruct.getCanonicalPath());
    }
  }
  


  void writeResultAsSecondToCompare(ZbnfToplevel zbnfToplevel, File fileStruct, String sPkgPath, String sClassFileName) 
  throws IOException{
  	String sFileName = fileStruct.getName();
    String sPath = fileStruct.getParent();
  	File dir = new File(sPath+"/stcCmp");
  	if(dir.exists() && dir.isDirectory()){
	    List<ClassData> listClassInFile = new LinkedList<ClassData>();
	  	for(Zbnf_ClassData zbnfClass: zbnfToplevel.zbnfClassData){
	  		listClassInFile.add(zbnfClass.classData);
	  	}
	    
	    //File fileStructSecond = new File(fileStruct.getAbsoluteFile()+".sec");
	    File fileStructSecond = new File(sPath+"/stcCmp/"+sFileName);
	    if(sClassFileName.equals("LogMessage"))
	    	stop();
	    GenerateFile.writeStructure(fileStructSecond, sClassFileName, listClassInFile, sPkgPath);
  	}
  }



  
  
  
  /**Build the classData of the read stc-data.
   * @throws ParseException 
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   * @throws IOException 
   * @throws FileNotFoundException 
   * 
   */
  public void postPrepare(ZbnfToplevel zbnfToplevel) 
  throws IllegalArgumentException, IllegalAccessException, InstantiationException, ParseException, FileNotFoundException, IOException
  {
  	for(Zbnf_ClassData zbnfClass: zbnfToplevel.zbnfClassData){
  		prepareClassData(zbnfClass, zbnfToplevel, null);
  	}
  	for(Zbnf_ClassData zbnfClass: zbnfToplevel.zbnfClassData){
  		prepareClassFieldMethod(zbnfClass);
  	}  
  	for(Zbnf_ClassData zbnfClass: zbnfToplevel.zbnfClassData){
	    ClassData classData = zbnfClass.classData;
	    if(classData.outerClazz == null){
	      //only for top-level classes:
	    	classData.completeTypesForInnerClasses();
		  	classData.completeFieldIdentsForInnerClasses();
	  	}
  	}
    //at last remove the parsed meta-informations, let the garbage collector tidy up.
    zbnfToplevel = null;
    System.gc();
    //System.getProperty("");
  }
  
  
  
  private void prepareClassFieldMethod(Zbnf_ClassData zbnfInput) 
  throws IllegalArgumentException, IllegalAccessException, InstantiationException, ParseException, FileNotFoundException, IOException
  { ClassData classData = zbnfInput.classData;
  	for(Zbnf_Field field: zbnfInput.listFields)
    { FieldData fieldData = prepareField(field, classData);
    	classData.classLevelIdents.putClassElement(fieldData.getName(), fieldData);
    }
    for(Zbnf_Method zbnfMethod: zbnfInput.listMethods)
    { 
    	prepareMethod(zbnfMethod, classData);
    }
    for(Zbnf_Cast zbnfCast: zbnfInput.zbnfCastTo){
    	ClassData dstType = classData.classLevelIdents.getType(zbnfCast.typeJava, fileLevelIdents);
    	classData.addCastToType(dstType, zbnfCast.castExpr, zbnfCast.accessDst + zbnfCast.accessSrc);
    }
    for(Zbnf_Cast zbnfCast: zbnfInput.zbnfCastFrom){
    	ClassData srcType = classData.classLevelIdents.getType(zbnfCast.typeJava, fileLevelIdents);
    	classData.addCastFromType(srcType, zbnfCast.castExpr, zbnfCast.accessDst + zbnfCast.accessSrc);
    }
    //do not call zbnfInput.classData.completeInheritanceWithOwnMethods();
    //because the methods are ordered in another way.
    //Instead, the order of overrideable methods are contained in an extra part
    //of the stc-file. This is the order of method-table-entries. Use it!
    if(zbnfInput.listMethodsOverrideableC.size() >0){
      zbnfInput.classData.completeInheritanceWithListMethods(zbnfInput.listMethodsOverrideableC);
    }
    if(zbnfInput.zbnfInnerClassData !=null)
    for(Zbnf_ClassData zbnfInnerClass : zbnfInput.zbnfInnerClassData){
    	prepareClassFieldMethod(zbnfInnerClass);
    }
  }
  
  
  
  public void prepareClassData(Zbnf_ClassData zbnfInput, ZbnfToplevel zbnfToplevel
  	, ClassData outerClassData) 
  throws IllegalArgumentException, IllegalAccessException, InstantiationException, ParseException
  { //zbnfInput.createClassDataIfNotDone();
    ClassData superClass = null;
    if(zbnfInput.sSuperClass != null){
      
      try{ 
        //superClass = outer.fileLevelIdents.getTypeContingentlyRunFirstPass(sSuperClass, outer.java2c); 
        superClass = fileLevelIdents.getType(zbnfInput.sSuperClass, null); 
      }
      catch(Exception exc){
        msg.reportln(Report.error, "Exception reading " + zbnfInput.sSuperClass + ": " + exc.getMessage());
        throw new IllegalArgumentException(exc.getMessage());
        //superClass = null;
      }
    }
    
    ClassData[] ifcClasses = null;
    if(zbnfInput.listInterfaceClass != null){
      ifcClasses = new ClassData[zbnfInput.listInterfaceClass.size()];
      int ixIfcClasses = 0;
      for(String sIfcClass: zbnfInput.listInterfaceClass){
        ClassData ifcClass;
        try{ 
          //ifcClass = outer.fileLevelIdents.getTypeContingentlyRunFirstPass(sIfcClass, outer.java2c); 
          if(sIfcClass.equals("org/vishia/msgDispatch/LogMessage"))
          	stop();
        	ifcClass = fileLevelIdents.getType(sIfcClass, null); 
        }
        catch(Exception exc){
          ifcClass = null;
          msg.reportln(Report.error, "Exception \"" + exc.getMessage() + "\" reading " + sIfcClass );
          msg.report("", exc);
        }
        ifcClasses[ixIfcClasses++] = ifcClass;
      }
    }
    
    if(zbnfInput.nameJava.equals("LogMessage") || zbnfInput.nameJava.equals("ExpandedDataStruct"))
      stop();
    String fileC = outerClassData !=null ? outerClassData.sFileName 
    	           : zbnfInput.header != null ? zbnfInput.header 
    	           : zbnfToplevel.sFileName;
    String sProp = "+";
    if(zbnfInput.bIsStaticInstance){sProp +="static+"; }
    if(zbnfInput.bFinal){sProp +="final+"; }
    if(zbnfInput.bEmbedded){sProp +="embedded+"; }
    if(zbnfInput.bAbstract){sProp +="abstract+"; }
    if(zbnfInput.bConst){sProp +="const+"; }
    if(zbnfInput.bExtern){sProp +="extern+"; }
    if(zbnfInput.bInterface){sProp +="interface+"; }
    if(zbnfInput.nonStaticInner){ sProp += "nonStaticInner+"; }
    if(fileC.endsWith(".h")){ fileC = fileC.substring(0, fileC.length()-2); }
    final char intension = zbnfInput.intension != '\0' ? zbnfInput.intension 
    	                   : outerClassData ==null? 'P' : 'C';  //intension only given for inner classes
    final boolean bStringJc = zbnfInput.sNameC.equals("StringJc");
    final char modeAccess = zbnfInput.bEmbedded ? '$' : bStringJc ? 't' : '*';
    ClassData classData = new ClassData(sSourceOfClassData, null, fileC, zbnfToplevel.packageStc, zbnfInput.nameJava, zbnfInput.sNameC
        , zbnfInput.argIdent, 'L', outerClassData ,superClass, ifcClasses, null
        , modeAccess, sProp, intension);
    
    zbnfInput.classData = classData;  //note it there for adding fields, methods.
    
    if(zbnfInput.zbnfInnerClassData !=null)
    for(Zbnf_ClassData zbnfInnerClass : zbnfInput.zbnfInnerClassData){
    	prepareClassData(zbnfInnerClass, zbnfToplevel, classData);
    }

    
    if(outerClassData != null){
      //An inner class: put the type to its classlevelIdents, 
      outerClassData.classLevelIdents.putClassType(classData);
      //and remark the inner class to complete it with the type too (see next)
    	outerClassData.addInnerClass(classData);
    }
    else if(zbnfInput.nameJava.equals(javaSrc.getTypeName())){
      //The public class of the file is translated: set the classData. */
    	javaSrc.setClassData(classData);
    } else {
      /**Additional classes in the file are found. They were unknown until now, register it. */
      pkgIdents.putClassType(classData); //replace JavaSrcFile with ClassData!
      fileLevelIdents.putClassType(classData.sClassNameJava, classData);
    }
    
  }
  
  
  
  
  
  public FieldData prepareField(Zbnf_Field field, ClassData classData) 
  throws IllegalArgumentException, IllegalAccessException, InstantiationException, ParseException
  { //createClassDataIfNotDone(); 
    if(field.typeJava.equals("java/io/FileWriter"))
    	stop();
  	if(field.name.equals("sFormatter") && classData.sClassNameJava.equals("SetValueGenerator"))
      stop();
    ClassData typeClass;
    if(field.outerClassData[0] != null && field.outerClassData[0].length() >0)
    { int posDelim = field.outerClassData[0].indexOf('.');
      String sOuterName = field.outerClassData[0].substring(0, posDelim);
      //ClassData outerType = outer.fileLevelIdents.getTypeContingentlyRunFirstPass(sOuterName, outer.java2c);  
      ClassData outerType = fileLevelIdents.getType(sOuterName, null);  
      sOuterName = field.outerClassData[0].substring(posDelim+1);
      //Zbnf_EnvIdent itemEnv = field.outerClassData;
      //while((itemEnv = itemEnv.subIdent)!= null)
      while((posDelim = sOuterName.indexOf('.'))>0)
      { /**Type inside the outer type. */
        //String subIdent = itemEnv.name;
        String subIdent = sOuterName.substring(0, posDelim);
        outerType = outerType.classLevelIdents.getType(subIdent, null);  //fileLevelIdents=null
        sOuterName =sOuterName.substring(posDelim+1);
      }
      typeClass = outerType.classLevelIdents.getType(field.typeJava, null);
    }
    else
    { /**Gets the typeclass also as local visibility inner class (?) */
      //typeClass = classData.classLevelIdents.getTypeContingentlyRunFirstPass(field.typeJava, outer.java2c);
      typeClass = classData.classLevelIdents.getType(field.typeJava, fileLevelIdents);
    }
    if(typeClass == null)
    { throw new IllegalArgumentException("Type:" + field.typeJava + " not found for field:" + field.name);
    	//typeClass = Java2C_Main.createExternalType(field.typeJava, field.pkgType[0]);
    }
    
    ClassData instanceClass = null;
    if(field.instanceType != null){
      //instanceClass = classData.classLevelIdents.getTypeContingentlyRunFirstPass(field.instanceType, outer.java2c);
      instanceClass = classData.classLevelIdents.getType(field.instanceType, fileLevelIdents);
    }
    
    if(field.name.equals("ifc"))
      stop();
    FieldData identInfo = createFieldData
    ( field.name, typeClass, instanceClass, null, null, field.modeStatic, field.modeAccess
    , field.modeArrayElement, field.dimensionArrayOrFixSize, field.listFixArraySizes, classData
    );
    identInfo.nClassLevel = 1;  //class elements.
    identInfo.nOuterLevel = 1;  //not copied from outer class
    return identInfo;      
  }
  
  
  public void prepareMethod(Zbnf_Method m, ClassData classData) throws FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException, ParseException
  {
    if(m.sCName == null){
    	m.sCName = m.sNameJava;  //it is the same, only by simple basicly classes.
    }
    	
  	if(m.sNameJava.equals("os_getDateTime"))
      stop();
    //ClassData typeClass = classData.classLevelIdents.getTypeContingentlyRunFirstPass(m.returnType1.typeJava, outer.java2c);
    ClassData typeClass = classData.classLevelIdents.getType(m.returnType1.typeJava, fileLevelIdents);
    if(typeClass ==null)throw new IllegalArgumentException("no ClassData found for:" + m.returnType1.typeJava);
    final char modeStatic;
    if((m.modifier & Method.modeReturnNonPersistring) !=0){
      assert(m.returnType1.modeStatic == '.');
      modeStatic = 'r';
    } else {
    	modeStatic = m.returnType1.modeStatic;  
    }
    FieldData returnType = createFieldData
    ( m.returnType1.name, typeClass, null, null, null, modeStatic, m.returnType1.modeAccess
    , m.returnType1.modeArrayElement, m.returnType1.dimensionArrayOrFixSize, m.returnType1.listFixArraySizes, null);
    final FieldData[] params;
    final int sizeParams;
    if(m.zbnfParam !=null){
      sizeParams = m.zbnfParam.size();
	    params = new FieldData[sizeParams];
	    int idxParam = -1;  //use pre-increment
	    //for(FieldData param: m.listParams)
	    for(Zbnf_Field zbnfParam: m.zbnfParam)
	    { if(zbnfParam.typeJava.equals("#*")){
	    	  //last is a variable argument identification:
	    	  params[++idxParam] = CRuntimeJavalikeClassData.clazz_va_argRaw.classTypeInfo;
	        if(idxParam != params.length-1) throw new IllegalArgumentException("method: "+ m.sCName + ": The ..." + zbnfParam.name + "designation should be the last argument!");      
	      }else {
		    	FieldData paramField = prepareParam(zbnfParam, classData);
		    	params[++idxParam] = paramField;
	      }	
	    }
    } else {
    	params = null;
    	sizeParams = 0;
    }
    /**complete non-given data: */
    if(m.sNameUnambiguous == null){ m.sNameUnambiguous = m.sNameJava; } 
    if(m.sCName == null){ m.sCName = m.sNameUnambiguous; } 
    String sKeyName = m.sNameJava + '#' + sizeParams;
    if(m.sNameJava.equals("anotherIfcmethod"))
      stop();
    final String sImplementationName = m.implementationSuffix == null ? null 
    		: m.sCName + m.implementationSuffix;
    if(sImplementationName != null)
    	stop();
    final String sNameUnambiguous = (!m.bClassSuffixName ? "!":"") + m.sNameUnambiguous; //special case !Name
    classData.addMethod(m.sNameJava, sNameUnambiguous, sImplementationName, m.modifier, returnType, params);
    //Method method = new Method(classData, null, sKeyName, m.sNameJava, m.sNameUnambiguous, m.modifier, returnType, params, null, "");
    //listMethods.add(method);
  }
  
  public void prepareCastTo(Zbnf_Cast value, ClassData classData) throws ParseException
  {
  	ClassData dstType = classData.classLevelIdents.getType(value.typeJava, fileLevelIdents);
  	classData.addCastToType(dstType, value.castExpr, value.accessDst + value.accessSrc);
  }
  
  public FieldData prepareParam(Zbnf_Field param, ClassData classData) 
  throws FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException, ParseException
  {
    //ClassData typeClass = outer.fileLevelIdents.getTypeContingentlyRunFirstPass(param.typeJava, outer.java2c);
    if(param.typeJava.equals("va_argRaw") || param.typeJava.equals("org/vishia/java2C/test/TestWaitNotify.WaitNotifyData"))
    	stop();
    if(param.name.equals("timestamp"))
    	stop();
    ClassData typeClass = classData.classLevelIdents.getType(param.typeJava, fileLevelIdents);
  	if(typeClass == null)
  		throw new IllegalArgumentException("Type not found: " + param.typeJava
  			+ "\n  Hint: A type should be given with full path while using in an stc-files, to find it in the package replacement.");
    //ClassData typeClass = outer.fileLevelIdents.getType(param.typeJava, null);
    
    FieldData identInfo = createFieldData
    ( param.name, typeClass, null, null, null, param.modeStatic, param.modeAccess
    , param.modeArrayElement, param.dimensionArrayOrFixSize, param.listFixArraySizes, null);
    return identInfo;
  }

  
  FieldData createFieldData
  ( String sName
    , ClassData typeClazz
    , ClassData instanceClass
    , ClassData elementClass
    , ClassData keyClass
    , char modeStatic
    , char modeAccess
    , char modeArrayElement
    , int dimensionArray
    , List<String> listFixArraySizes
    , ClassData declaringClazz
    ) {
    final String[] fixArraySizes;
	  if(listFixArraySizes != null){
	    int zFixArraySizes = listFixArraySizes.size();
	    fixArraySizes = zFixArraySizes == 0 ? null : new String[zFixArraySizes];
	    int ixFixArraySizes = 0;
	    for(String fixArraySize: listFixArraySizes) {
	      fixArraySizes[ixFixArraySizes++] = fixArraySize;
	    }
	  }else{
	    fixArraySizes = null;
	  }
	  //
  	FieldData fieldData = new FieldData(sName, typeClazz, instanceClass, elementClass, keyClass
  		, modeStatic, modeAccess, dimensionArray, fixArraySizes, modeArrayElement, declaringClazz);       
    return fieldData;
  }
  
  
  void stop(){}
  
}

