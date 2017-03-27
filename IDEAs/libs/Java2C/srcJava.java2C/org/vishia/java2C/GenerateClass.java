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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//import org.vishia.java2C.StatementBlock;
import org.vishia.java2C.ClassData.MethodWithZbnfItem;
import org.vishia.mainCmd.Report;
import org.vishia.zbnf.ZbnfParseResultItem;

/**This is the superclass for {@link FirstPass} and {@link SecondPass}.
 * It contains commonly methods used in both passes and commonly data for both passes.
 * This class isn't store as instance between both passes, elsewhere the ClassData to store are stored
 * in the instance of {@link ClassData}, all the ClassData are contained in a list
 * inside {@link AllClassData}. 
 * The attributes of this class are copied from the {@link Java2C_Main} in construction 
 * and there are forgotten after running the pass.
 * 
 *  
 * 
 * @author Hartmut Schorrig
 *
 */ 
class GenerateClass
{
	protected final Report log;
	
  /**String to generate newline and indent. */
  final private static String sNewLine = "\n                                                                              ";

  //ZbnfParseResultItem resultItemClass;
  /**A writer to file. */
  protected iWriteContent writeContent;

  /**A TreeMap of all classData sorted by classname to find all other classData.
   * This list is complete if the second path runs.
   */
  //protected final TreeMap<String, ClassData> pkgIdents;
  protected final LocalIdents fileLevelIdents;
  
  protected final GenerateFile parentGenerateFile;
 
  /**The data of this class for first and second path. A nested class will be recognized as a normal class. */
  protected ClassData classData;
  
  /**The C-name of the current translated method, used for _Stacktrace and alloc_s_BlockHeapJc. 
   * This value is set and used inside {@link SecondPass#write_methodDefinition(ZbnfParseResultItem, String, LocalIdents)}.
   */
  protected String sMethodNameCurrent;
  
  /**The interface to call a firstpass run of a needed type. */
  final RunRequiredFirstPass_ifc runRequiredFirstPass;


  
  
  
  
  
  /**constructs the class. This constructor is called from the superclass. 
   * @param writeContent The writer to the C- and H-File.
   * @param fileLevelIdents List of all known Classes while translation Java2C
   * @param runRequiredFirstPass Reference to run the first pass of a required class. 
   *        If in the first or second pass any {@link ClassData} from another class are
   *        necessary but there was not found in {@link pkgIdents}, the first pass of this
   *        required class will called recursively while processing the pass of this class. 
   */
  //protected GenerateClass(iWriteContent writeContent, TreeMap<String, ClassData> pkgIdents, RunRequiredFirstPass_ifc runRequiredFirstPass)
  protected GenerateClass(iWriteContent writeContent, GenerateFile parentGenerateFile
  , LocalIdents fileLevelIdents
  , RunRequiredFirstPass_ifc runRequiredFirstPass
  , Report log
  )
  { //this.sClassName = sClassName;
    //this.resultItemClass = resultItem;
    this.log = log;
  	this.writeContent = writeContent;
    this.fileLevelIdents = fileLevelIdents;
    this.parentGenerateFile = parentGenerateFile;
    this.runRequiredFirstPass = runRequiredFirstPass;
  }




  /**writes the parsed description in the H-File.
   *
   * @param item description::= test_description::=
   * @throws IOException
   */
  protected void write_Description(ZbnfParseResultItem item, StringBuilder uHeader) throws IOException
  {
    Iterator<ZbnfParseResultItem> iterDescr = item.iteratorChildren();
    if(iterDescr != null)
    { while(iterDescr.hasNext())
      { ZbnfParseResultItem resultItemDesription = iterDescr.next();
        String sSemanticDescription = resultItemDesription.getSemantic();
        if(sSemanticDescription.equals("text"))
        { String sText = resultItemDesription.getParsedString();
          uHeader.append("\n/**" + sText + "*/\n");
        }
    } }
  }


  /**Generates a variable definition with syntax of < variableDefinition>, 
   * register the variable in the LocalIdents of the class or block 
   * and registers in the list of variables to init or static variables.
   * <br>
   * The variableDefinition is used both in first and second pass of translation,
   * in the first pass there are the class variables, in the second pass there are variables
   * in block statement. But also arguments for method and constructor calls with the syntax 
   * of a <?argument> in a <code>argumentList::=...</code> are translated with this method.
   * It is the same syntax.
   * <ul>
   * <li>The {@link LocalIdents} are given per reference, it is either the instance of this {@link classData}
   *     or the instance of the block-statement. 
   * <li>If the variable is a static variable, it is registered unconditionally in the 
   *     {@link #classData}.{@link ClassData#staticVariables}. Static variables are only able at class level.
   *     Static variables may have an initialization or not.
   * <li>If the variable is non-static and it has an initial < value> or < newValue> or < newArray> 
   *     or < constArray> as ZBNF-child, than the variable is registered in the localIdents of the block or class.
   * <li>The initialization is not execute at position of definition of the variable, but
   *     <ul>
   *     <li>for class variables in the constructor.
   *     <li>for block variables after the last variable definition (C language necessity).
   *     <li>for static variables in the C-file, where the extern declaration is written with this routine
   *         also in the Headerfile. Static variables are initialized only with const values yet,
   *         the calculation of values from other values is not supported yet. If it is necessary,
   *         an extra static initializator-routine is necessary per object-file and should be organized per executable.
   *     <li>The variant of const initialization of block variables at position of variable definition
   *         isn't used, the same result is gotten by assignment of the value after all variable definitions
   *         (is there an optimization effect at C/machine-code-level?)      
   *     </ul>   
   * </ul>
   * The {@link FieldData#gen_VariableDefinition()} is called inside. The properties of the variable are stored
   * in its {@link FieldData}, which are created here from the ZBNF parse results. 
   * So the C-code-String of variable definition is able to prepare only with knowledge of the {@link FieldData}.
   * <br>
   * There are some special cases all considered here:
   * <ul>
   * <li>If it is a <code> final static </code> variable with a immediate simple assignment like
   *   <code>final static int name = 5;</code>, a <code>#define name_Classname 5</code> is produced.
   * <li>If it is a <code>final Type ... = new Type(...)</code>, an embedded struct is defined in C.
   *   That's correct, because a final reference can't be changed and the reference type is equal the
   *   instanciated type. Embedded struct constructs are opportune in C to optimize access and 
   *   memory layout of the data.
   * <li>A reference is generated either as normal forward declaration like <code>struct Type_t* ...</code>
   *   or with using an enhanced reference like <code>Type_REF ...</code>. All enhanced references 
   *   are defined together in 1 headerfile In form <pre>
   *   typedef struct NAMEREF_t { ObjectRefValuesJc refbase; struct NAME_t* ref; } NAMEREF;
   *   </pre>, see {@link referenceNeeded()} and {@link Java2C_Main.writeFileRef()}.
   * </ul>      
   *
   * @param zbnfVariableDefinition from parsers result.
   * @param zbnfDescription The description to the variable. If the variable is a method argument,
   *        than this description is gotten as a part of the method description after tag param.
   *        The description may contain java2c-tags. The following tags are respected:
   *        <ul><li><code>zeroTermString</code>
   *        <li><code>zeroTermString</code>TODO
   *        <li><code>zeroTermString</code>
   *        <li><code>zeroTermString</code>
   *        <li><code>zeroTermString</code>
   *        <li><code>zeroTermString</code>
   *        <li><code>zeroTermString</code>
   *        <li><code>zeroTermString</code>
   *        <li><code>zeroTermString</code>
   *        <li><code>zeroTermString</code>
   *        <li><code>zeroTermString</code>
   *        </ul>
   * @param localIdents The environment identifiers. Register the variable there, get types from them.
   * @param listVariablesToInit The initialization of variables is stored in this list. 
   *        The initializations should be done in C not by the variables declaration 
   *        but in another part of code. For class variables that is the constructor(s). 
   *        Therefore this parameter should be the {@link ClassData#variablesToInit} of this.
   *        But if this routine is called inside a statement block, an extra List instance
   *        for the block should given locally.
   * @param intension intension of calling 
   *        <ul>
   *        <li>A = argument list of declaration of a method or constructor. 
   *        <li>a = argument list of definition of a method or constructor.
   *        <li>b = inside statement block
   *        <li>c = class level 
   *        <li>s = static class level
   *        </ul> 
   * @return Code snipped and informations of the variables declaration.
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   */
  protected CCodeData gen_variableDefinition
  ( ZbnfParseResultItem zbnfVariableDefinition
  , ZbnfParseResultItem zbnfDescription
  , LocalIdents localIdents
  , final StatementBlock blockEnvironment
  , List<ClassData.InitInfoVariable> listVariablesToInit
  , char intension
  )
  throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  {
    assert("CAabcsz".indexOf(intension)>=0);
    /**Get all type info and the definition code of variable. */
    FieldData fieldVariable;
    /**The return instance.*/
    CCodeData codeData;
    ZbnfParseResultItem zbnfInitValue = zbnfVariableDefinition.getChild("value");
    ZbnfParseResultItem zbnfInitConstArray = zbnfVariableDefinition.getChild("constArray");
    ZbnfParseResultItem zbnfNewObject = zbnfVariableDefinition.getChild("newObject");
    //an impliciteImplementationClass can be exist in any case.
    /**generates the core of variable definition calling inside FieldData. Hint: To Debug name of variable see there!*/
    { ZbnfParseResultItem zbnfType = zbnfVariableDefinition.getChild("type");
      ClassData declaringClass = "Cs".indexOf(intension)>=0 ? classData: null; 
      fieldVariable = createFieldInfo(zbnfVariableDefinition, zbnfType, zbnfDescription
      , zbnfNewObject
      , localIdents
      , blockEnvironment
      , declaringClass   //declaringClass only at class level and while static class members..
      , intension
      , '.'
      );
      if(fieldVariable.getName().equals("timetest1"))
        stop();
      StringBuilder code = new StringBuilder(50);
      code.append(fieldVariable.gen_VariableDefinition(intension));

      if("cmbf".indexOf(intension)>=0){
      	/*Inside a method body*/
      	if(zbnfInitValue != null){
      		CCodeData codeInit = genConstantValue(zbnfInitValue);
      		if(codeInit != null){
      			String sInit = fieldVariable.testAndCastInitializer(codeInit);
      			code.append(" = ").append(sInit);
      			zbnfInitValue = null;
      		}
      	} else  if(zbnfInitConstArray != null) {
      		/**Check whether all elements are constant. */
      		List<ZbnfParseResultItem> zbnfValues = zbnfInitConstArray.listChildren("value");
      		CCodeData[] codeInit = new CCodeData[zbnfValues.size()];
      		int ix = 0;
      		boolean allConst = true;
      		Iterator<ZbnfParseResultItem> iterZbnfValues = zbnfValues.iterator();
      		while(allConst && iterZbnfValues.hasNext()){
      		  ZbnfParseResultItem zbnfValue = iterZbnfValues.next();
      		  codeInit[ix] = genConstantValue(zbnfValue);
      		  if(codeInit[ix] == null){
      		  	allConst = false; /*Non-constant: */
      		  }
      		  ix +=1;
      		}
      		if(allConst){
      			zbnfInitConstArray = null;   //don't use while init.
      			code.append(" = ") ;
      			char cSep = '{';
      			for(CCodeData value: codeInit){
      				code.append(cSep).append(value.cCode);
      				cSep = ',';
      			}
      			code.append(" }");
      		}
      	} else if(fieldVariable.modeAccess == '%'){
      		code.append( " = 0");
      	} else if(fieldVariable.modeAccess == '$'){
      		code.append( " = { 0 }");
      	} else if(fieldVariable.modeAccess == '*'){
      		code.append( " = null");
      	} else if(fieldVariable.typeClazz == CRuntimeJavalikeClassData.clazzStringJc){
      		code.append( " = NULL_StringJc");
      	} else {
      		stop();
      		//code.append(" = { 0 }");
      	}
      }
      if("Aa".indexOf(intension) <0)  //no ; at end of argument definition in argument list
      { if(fieldVariable.modeStatic != 'd'){ code.append("; ");} //no ; at end of #define
        String sDescription = get_shortDescription(zbnfDescription);
        if(sDescription != null)
        { code.append("  /*" + sDescription + "*/");
        }
      }
        
      if(fieldVariable.modeAccess=='@' || fieldVariable.modeArrayElement=='@')
      { /**A variable-definition of typeREF of the type is needed in this source.
         * Registers the type for the class.
         * The reference will be defined in the dst.h at first. */
        classData.addEnhancedRefType(fieldVariable.typeClazz.getClassIdentName());  //to define the REF-Type.
        if("Cc".indexOf(intension)>=0){  
          classData.needFinalize();  //only if enhanced refs are given as class members:
        }
      }
      if(fieldVariable.modeAccess=='&' || fieldVariable.modeArrayElement=='&')
      { /**A variable-definition of TypeMTB of the type is needed in this source.
         * Registers the type for the class.
         * The reference will be defined in the dst.h or dst.c at first. */
        classData.addMtblRefType(fieldVariable.typeClazz.getClassIdentName(), intension);  //to define the REF-Type.
      }
      codeData = new CCodeData(code.toString(), fieldVariable);
    }
    
    /**Get and store the initialization zbnf-part of the variable: */
    if( fieldVariable.modeStatic != 'd')   //not for #define ... the value is stored already in fieldVariable.
    { ZbnfParseResultItem zbnfVariablesInitialization;
      zbnfVariablesInitialization = zbnfVariableDefinition.getChild("newArray");
      if(zbnfVariablesInitialization == null)
      { zbnfVariablesInitialization = zbnfNewObject;
        if(zbnfVariablesInitialization == null)
        { zbnfVariablesInitialization = zbnfInitValue;        //null if processed already
          if(zbnfVariablesInitialization == null)
          { zbnfVariablesInitialization = zbnfInitConstArray; //null if processed already
          }
        }
      }
      boolean bStatic = "Ss".indexOf(fieldVariable.modeStatic)>=0; 
      if(bStatic || zbnfVariablesInitialization != null) //add staticVariables always, also without init value. 
      { /**There is a initialize value. */
        ClassData.InitInfoVariable variableToInit = 
          new ClassData.InitInfoVariable(fieldVariable, zbnfVariablesInitialization);
        if(bStatic){
          classData.staticVariables.add(variableToInit);  //a static initializing.
        }else if(listVariablesToInit != null){
          listVariablesToInit.add(variableToInit);        //a dynamic initializing.
        }
      }
    }
    
    /**Register the variable definition in the LocalIdents of class or block. */  
    if(localIdents != null)  //when they are null?
    { /**Put identInfo.*/
      if("Cs".indexOf(intension)>=0)
      { /**at class level, class variable or static variable of the class. */
        localIdents.putClassElement(fieldVariable.getName(), fieldVariable);
      }
      else if(intension=='A')
      { /**elements in a formal argument list of declaration should not be stored, but in actual. */
      }
      else
      { localIdents.putLocalElement(fieldVariable.getName(), fieldVariable);
      }
    }
    
    return codeData;
  }
    
    
  
  
  
  
  
  /**Generate an anonymous class, in Java: <code>new Type(..){...}</code>.
   * <br><br>
   * <ul>
   * <li>Gets the Type of new as baseClass for the anonymous class
   * <li>Runs the {@link FirstPass#runFirstPassClass(StringBuilder, GenerateFile, String, String, String, String, ZbnfParseResultItem, boolean, String, ClassData, List, boolean, ClassData)}.
   * <li>Generates a constructor based on the found ctor of the baseClassData, from there
   *     the formal argument list and the return type is taken.
   *     see also {@link FirstPass#writeCtor(...)}
   * <li>For anonymous classes, which are found in the second pass, see param intension = 'b', an extra 
   *     {@link SecondPass#runSecondPassClass(ZbnfParseResultItem, String)} is called.
   *     The content of this second-pass is written using TODO before the running second-pass.
   * <li>For anonymous classes, which are found in the first pass to initialize variables,
   *     the normal second pass will regarded it as inner classes.          
   * </ul> 
   * <br><br>
   * @param zbnfNewObject The parse result from <code>newObject::=...</code>
   * @param zbnfNewObject
   * @param zbnfDescription
   * @param localIdents The idents of the context, either classLevelIdents if intensio TODO always classLevelIdents.
   * @param blockEnvironment
   * @param sNameField The name of the field which is initialized with <code>new Type(..){...}</code>
   *                   to build the name of the inner class, TODO name of routine.
   * @param intension  'b' if called in an statement block, otherwise: at class level.
   * @param fileContainsClass
   * @return
   * @throws FileNotFoundException
   * @throws IllegalArgumentException
   * @throws IOException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @throws ParseException
   */
  public ClassData gen_AnonymousClass
  ( ZbnfParseResultItem zbnfNewObject, ZbnfParseResultItem zbnfDescription
  , LocalIdents localIdents
  , final StatementBlock blockEnvironment
  , String sNameField
  ,	char intension
  , GenerateFile fileContainsClass
  ) 
  throws FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException, ParseException
  { 
  	if(sNameField == null){ 
  		//TODO: inner new
  		sNameField = "INNER";
  	} 
  	if(sNameField.equals("ref2"))
  		stop();
  	int modeCtor = Method.modeCtor | Method.modeCtorAnonymous;
  	if(zbnfNewObject.getChild("static")==null){  //keyword static not found:
  		modeCtor |= Method.modeCtorNonStatic;
  	}
  	final List<ClassData> listAllClassesForSecondPath;
  	if("Cs".indexOf(intension) >=0){
  		listAllClassesForSecondPath = parentGenerateFile.listAllClassesForSecondPath;
  	} else {
  	  //then the class is not added to such an list, because the secondpath is running immediately.
    	listAllClassesForSecondPath	= null;  
    }
    
  	/**Get the base type of the new-definition, it is the Type after "new". */
  	ClassData baseClassData = getType(zbnfNewObject.getChild("newClass"), localIdents);  //itemNewObject.getChild("newClass").getParsedString();
  	ZbnfParseResultItem zbnfImplicitClass = zbnfNewObject.getChild("impliciteImplementationClass");
		FirstPass firstPass = new FirstPass(writeContent, fileContainsClass, fileLevelIdents, runRequiredFirstPass, log);
    //Runs the first-pass-evaluation from the given ZBNF-result-part of the anonymous class.
		//The anonymousClass will be produced, the generated header-content is written 
		//either in the C- or in the H-file depending on the intension. 
		String sNameClass = "C_" + sNameField;
  	StringBuilder uResult = new StringBuilder(10000);  //may be increased if necessary
  	ClassData anonymousClass = firstPass.buildType(
			uResult
		, classData.sSourceOfClassData	//the outer class for this firstpass
		, fileContainsClass
		, fileContainsClass.sFileNameC
		, fileContainsClass.sPkgIdent
		, sNameClass
		, ""
    , zbnfImplicitClass
    , true                       //don't write constructor
    , classData.sClassNameJava
    , classData                  //classData from the environment (outer-) class where this method is called.
    , listAllClassesForSecondPath
    , false
    , baseClassData
    , intension == 'C' ? 'Y' : intension   //anonymous class at class level: sign with y
    );
  	//The types for all non-anonymous classes are distributed to the inner classes already,
  	//at end of buildType() of the outer class, that is done afterwards here
  	classData.classLevelIdents.copyTypesTo(anonymousClass);	
  	
  	//Now run the first pass of the class.
    firstPass.runFirstPass2(uResult);
    ////
		//Search the appropriate constructor:
		final Method ctorBaseClass;
		final FieldData[] paramsType;
		if(baseClassData.isInterface()){
			ctorBaseClass = null;
			paramsType = null;
		} else {
			List<CCodeData> actParams = blockEnvironment.gatherActParams(zbnfNewObject, zbnfDescription, "ctor");
	    ctorBaseClass = baseClassData.searchMethod("ctorO", actParams, true, null);
	    paramsType = ctorBaseClass.paramsType;
		}
    
    String sNameCtor = "ctorO_" +  anonymousClass.getClassNameJava();
    Method ctor = firstPass.gen_methodHeadAndRegisterMethod(
    	null                       //no ZbnfParseResult for body
    , modeCtor                   //static or non-static, anonymous
    , this.classData             //class of method, for non-static constructors the outer class.
    , sNameCtor
    , anonymousClass.classTypeInfo //return-type
    , paramsType   //argTypes-array from the base constructor.
  	, false
    );
   	
    uResult.append(ctor.gen_MethodForwardDeclaration());
    //add the ctor to the list of methods, which where evaluatet at second path,
   	//though they hasn't a body of ZBNF-parse-result.
   	ClassData.MethodWithZbnfItem methodDef = anonymousClass.new MethodWithZbnfItem(
   		ctor
    , null
    , ctorBaseClass
    , 'c'
    ); 
    anonymousClass.methodsWithZbnf.add(methodDef);
  
  	assert(anonymousClass !=null); //should be found anytime in the current class.
    if("Cs".indexOf(intension)>=0){
    	writeContent.writeHdefs(uResult);
    } else {
    	assert(intension == 'b');
    	//anonymous inner type in the body of a method, write it in the C-file.
    	writeContent.writeCdefs(uResult);
      
    	//it is done for non-anonymous classes on end of running all firstpasses,
      //but this class is created later. Do only for this class, also if it is on classlevel
      //or in an statement block. Hint: this.classData: this is the outer class.
    	this.classData.classLevelIdents.copyFieldsTo(anonymousClass);
      
    	//runs the second pass only if the class is inside a statement block. 
    	//If the class is an imediately inner class of the outer, the second pass is running
    	//in coherence with the second pass of all other classes of the file.
      SecondPass genClass = new SecondPass(
      		  writeContent  //to output the file
      		, parentGenerateFile
      		, localIdents
      		, anonymousClass        //the class, contains the ZbnfParseResult
      		, runRequiredFirstPass  //interface to run any firstpass if their are unknown yet types in the bodies of methods
      		, log
      );
      ZbnfParseResultItem zbnfClass = anonymousClass.getParseResult();
      genClass.runSecondPassClass(
      	zbnfClass                           //the parsers result is stored in the ClassData.
      , this.classData.getClassIdentName()  //the outer class Name
      );
      classData.relinquishParseResult();
      
    }

   	return anonymousClass;
  }
  
  
  
  
  
  
  
  /**Prepares the description from the < description>parse result item.
   * The description is shorted to one line, but only to ". ", the first sentence.
   * @param parent The item
   * @return Description guaranteed without newline.
   */
  String get_shortDescription(ZbnfParseResultItem zbnfDescription)
  { String sDescription = null;
    //ZbnfParseResultItem zbnfDescription = parent.getChild("description");
    if(zbnfDescription != null)
    { sDescription = zbnfDescription.getChild("text").getParsedString();
      int posNewline = sDescription.indexOf('\n');
      int posDot = sDescription.indexOf(". ");
      int posEnd = posDot > 0 && posDot < posNewline ? posDot : posNewline > 0 ? posNewline : -1;
      if(posEnd > 0){ sDescription = sDescription.substring(0, posEnd); }
    }
    return sDescription;
  }

  
  /**generates constants values from parse result of the syntaxterm simpleValue. 
   * It is called if a < simpleValue> is expected, but the simpleValue should or may be a constant.
   * At example generation a <code>#define NAME VALUE</code> expression in C where a 
   * <code>final static int NAME = VALUE;</code> is given in Java.  
   * 
   * @param item The part of a simpleValue what is  
   * <pre>
   * [ <""?simpleStringLiteral>
   * | <''?simpleCharLiteral>
   * | 0x<#x?hexNumber>
   * | - 0x<#x?hexNumberNegative>
   * | <#-?intNumber>[?\.]
   * | <#f?floatNumber>F
   * | <#f?doubleNumber>
   * ]</pre>
   *
   * @return null if it is not any of the named part, than it may be a non constant value, see
   *         syntax description in Java.zbnf,
   *         return the constant expression in C if it is a constant one.
   *         
   */
  protected CCodeData genConstantValue(ZbnfParseResultItem item)
  { final ClassData typeValue;
    String simpleValue;
    String sSemantic = item.getSemantic();
    if(sSemantic.equals("value"))
    { ZbnfParseResultItem zbnfChild = item.firstChild();
      return genConstantValue(zbnfChild);
    }
    else
    { if(sSemantic.equals("simpleStringLiteral"))
      { simpleValue = "\"" + item.getParsedString() + "\"";
        typeValue = CRuntimeJavalikeClassData.clazz_s0;
      }
      else if(sSemantic.equals("booleanConst"))
      { simpleValue = item.getParsedString();
        typeValue = CRuntimeJavalikeClassData.clazz_bool;
      }
      else if(sSemantic.equals("nullRef"))
      { simpleValue = item.getParsedString();
        typeValue = CRuntimeJavalikeClassData.clazzObjectJc;
      }
      else if(sSemantic.equals("simpleCharLiteral"))
      { simpleValue = "\'" + item.getParsedString() + "\'";
        typeValue = CRuntimeJavalikeClassData.clazz_char;
      }
      else if(sSemantic.equals("hexNumber"))
      { simpleValue = "0x" + Long.toHexString(item.getParsedInteger());
        typeValue = CRuntimeJavalikeClassData.clazz_int32;
      }
      else if(sSemantic.equals("hexNumberNegative"))
      { simpleValue = "-0x" + Long.toHexString(item.getParsedInteger());
        typeValue = CRuntimeJavalikeClassData.clazz_int32;
      }
      else if(sSemantic.equals("intNumber"))
      { long value = item.getParsedInteger();
      	simpleValue = "" + value;
      	if(value >= -128 && value <= 127){ typeValue = CRuntimeJavalikeClassData.clazz_int8;
        } else if(value >= -0x8000 && value <= 0x7fff){ typeValue = CRuntimeJavalikeClassData.clazz_int16;
        } else if(value >= 0 && value <= 0xffff){ typeValue = CRuntimeJavalikeClassData.clazz_uint16;
        } else if(value >= -0x80000000 && value <= 0x7fffffff){ typeValue = CRuntimeJavalikeClassData.clazz_int32;
        } else { typeValue = CRuntimeJavalikeClassData.clazz_int64;
        }
      }
      else if(sSemantic.equals("floatNumber"))
      { simpleValue = "" + item.getParsedFloat()+"F";
        typeValue = CRuntimeJavalikeClassData.clazz_float;
      }
      else if(sSemantic.equals("doubleNumber"))
      { simpleValue = "" + item.getParsedFloat();
        typeValue = CRuntimeJavalikeClassData.clazz_double;
      }
      else
      { simpleValue = null;
        typeValue = null;
      }
      if(simpleValue == null)
      { return null;
      }
      else
      { final CCodeData codeInfo = new CCodeData(simpleValue, typeValue.classTypeInfo);
        return codeInfo;
      }
    }  
  }
  

  
  
  /**generates a String with the correct indentation. 
   * 
   * @param indent nr of indentation, as counter for nesting
   * @return "\n" and the appropriate number of spaces.
   */
  public static String genIndent(int indent)
  { return sNewLine.substring(0, 2*indent +1);
  }



  
  /**Provides the {@link ClassData} for the given type. The type is given in form of a parse result item
   * with semantic 
   * <pre>
   * type::=
   * [ [<?name>List|LinkedList|ArrayList|Iterator|Class][ \< <type?ContainerElementType> \>] 
   * | [<?name>Map|TreeMap|ArrayList|Iterator|Class][ \< <type?ContainerElementType> \>\< <type?ContainerElementType2> \>] 
   * | <envIdent?typeIdent>
   * ] 
   * [{<?typeArray> \[ \]}].
   * </pre>. 
   * Especially because the possibility of <code>&lt;envIdent?typeIdent></code> the requested type
   * may be a type inside another class (an inner class, <code>Outerclass.Innerclass</code>), 
   * so this class should be provided before, and the type should be searched there.
   * <ul>
   * <li>If the type is known, it means the {@link ClassData} with the given key are found,
   *     starting in the {@link LocalIdents} of the current context, further searching in the 
   *     {@link Java2C_Main#userTypes} and the {@link Java2C_Main#standardClassData}, 
   *     if necessary searched as inner type. It are one or some simple accesses to the appropriate 
   *     {@link LocalIdents}. 
   * <li>If the type isn't found, or the environment type (<code>&lt;envIdent?typeIdent></code>)
   *     isn't found, it is possible that this type isn't translated yet, but it is a part of the
   *     users <code>type.java</code> sources to translate. In this case a <code>type.java</code>-file
   *     should be part of input files, given with cmd line arguments <code>-i:FILE</code> or
   *     inside the <code>-if:INPFILE</code>. Therefore 
   *     {@link RunRequiredFirstPass_ifc#runRequestedFirstPass(String type)} is called.
   *     This routine either processes the parsing and first pass of translation for that file,
   *     or it parses the appropriate <code>type.stc</code> File and provides therewith
   *     the {@link ClassData}. The parsing is a deep recursively process, because other types
   *     are need too. This routine is called than recursively.
   * <li>If a file with the requested type isn't found, it may be either an not provided standard type
   *     (this is a deficiency of this translator, it may be any yet not considered Java standard class
   *     is used), or it may be an external type which is provided in the C-handwritten environment, 
   *     where a only emulating <code>type.java</code>-File in the Java sources exists. In both cases
   *     a new {@link ClassData} with attribute "external" is created.    
   * </ul>
   * In all cases a useable {@link ClassData}-instance is returned.
   *  
   * @param zbnfType The zbnf-component with syntax <code>type::=...</code>. 
   * @param localIdents The idents of this level.
   * @return The ClassData of the type.
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   * @throws ParseException 
   */
  protected ClassData getType
  ( ZbnfParseResultItem zbnfType
	, final LocalIdents localIdentsP
	) throws FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException, ParseException
  { String sRet = "";
    ClassData classDataType = null;
    ZbnfParseResultItem itemEnv;
    //ZbnfParseResultItem itemTypeIdent = zbnfType.getChild("typeIdent");
    ZbnfParseResultItem itemTypeIdent = zbnfType;
    String sType;
    final LocalIdents localIdents;
    if(itemTypeIdent != null)
    { { sType = itemTypeIdent.getChild("@name").getParsedString();
        /**NOTE: if a inner type is used line EnvType.SubType.Type, the mostly inner type is named first. */
        if(sType.equals("StringBuffer"))
          stop();
        
        if( (itemEnv=itemTypeIdent.getChild("envIdent"))!= null)
        { /**Here at first the mostly outer type is named. */
          String envIdent = itemEnv.getChild("@name").getParsedString();
          if(envIdent.equals("java"))
          	stop();
          //it should be a known type, either as inner type or as global type.
          // if it is a global type, it may be converted first.
          if(localIdentsP!=null){ classDataType = localIdentsP.getType(envIdent, fileLevelIdents); }  
          if(classDataType == null){
          	throw new IllegalArgumentException("type not found: " + envIdent 
          		+ ", \nsearched in the fileLevelIdents. " 
          		+ "\nNote: It may be a type, which isn't regard to translate"
          		+ "\nand which is not given in a stc-file. Correct stc-files or the translation.cfg!");
          }
          final LocalIdents localIdentsEnv = classDataType.classLevelIdents;
          LocalIdents localIdentsUse = localIdentsEnv;
          while((itemEnv=itemEnv.getChild("subIdent"))!= null)
          { /**Type inside the outer type. */
            String subIdent = itemEnv.getChild("@name").getParsedString();
            classDataType = localIdentsUse.getType(subIdent, null);  //fileLevelIdents = null!
            localIdentsUse = classDataType.classLevelIdents;
          }
          localIdents = localIdentsUse;
        }
        else
        { localIdents = localIdentsP; //use from given parameter
        }
      }
    }
    else
    { //containertype
      assert(false); //TODO
      sType = zbnfType.getChild("name").getParsedString();
      ZbnfParseResultItem containerElementType = zbnfType.getChild("ContainerElementType");
      localIdents = null;
    }  
    if(sType.equals("void"))
      stop();

    /**Search to appropriate ClassData. If not found, run first pass or create new ClassData for extern type. */
    if(sType.equals("FieldJc"))
      stop();
    classDataType = localIdents.getType(sType, fileLevelIdents);
    if(classDataType == null)
    { /**The type is not knwon. A unknown type will be created yet and registered in userTypes.
       */
      classDataType = new ClassData("extern-auto-created", "extern/", sType, sType, sType, '.', sType, '*', "+extern+");
      Java2C_Main.externalTypes.putClassType(sType, classDataType);
    }
    return classDataType;
  }


  /**Gets the info whether the type is an array type written like <code>ident[][]</code>.
   * @param zbnfType The ZBNF parse result for a type.
   * @return 0 if <code>[]</code> isn't found (parse result <code>typeArray</code> isn't present,
   *         >0 if it is a array type.
   */
  public int getDimensionsArray(ZbnfParseResultItem zbnfType)
  {
    int dimensionArray;
    /**Check if it is an array type. */
    if(zbnfType.getChild("typeArray") != null)
    { List<ZbnfParseResultItem> zbnfArray = zbnfType.listChildren("typeArray");
      dimensionArray = zbnfArray.size();
    }
    else{ dimensionArray = 0; }
    return dimensionArray;
  }
  
  
  
  
  
  
  
  /**Prepare the type properties from a given ZBNF <code>type::=</code> or adequate info.
   * The type properties are stored in a new FieldData, but without field name.
   * The type properties includes array designation and some Java2CTag.
   * @param zbnfVariable It may be null, if only the type informations should be kept.
   *                     The ZBNF result which presents a variable definition.
   *                     Some informations about the type of variable are contained in that level of parse result,
   *                     like new Array[length], final, a static initialization etc.
   * @param zbnfType The ZBNF result which presents <code>type::=</code> 
   *        or <code>nonArrayType::=</code> or <code>typeIdent::=</code>
   * @param zbnfDescription The ZBNF result which presents the description part to the type.
   *        If it is a variable definition, it is the description to the them.
   *        But if it is a argument or return type of a method, it is that part of the description
   *        of the method, which describes the param or return. 
   *        It may contain a <code><$?Java2CTag></code> to the type element.
   *        This param may be null, if no description of them is available.
   * @param zbnfNewObject maybe given, to create a stack or embedded instance. 
   * @param localIdentsP The local visible identifiers.
   * @param intension
   * @return A new instance of FieldData containing all type informations, but without name of field
   *         and without any instance informations like fix size of array etc.
   * @throws IOException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException from called {@link #getType(ZbnfParseResultItem, LocalIdents)}.
   * @throws ParseException 
   * @since 2009-06-27
   */
  public FieldData createFieldInfo
  ( final ZbnfParseResultItem zbnfVariable 
  , final ZbnfParseResultItem zbnfType
  , final ZbnfParseResultItem zbnfDescription
  , final ZbnfParseResultItem zbnfNewObject
  , final LocalIdents localIdentsP
  , final StatementBlock blockEnvironment
  , ClassData declaringClass
  , char intension
  , char definitionCondition
  ) throws IllegalAccessException, InstantiationException, FileNotFoundException, IllegalArgumentException, IOException, ParseException
  { final ClassData typeClass1;
    final ClassData typeClass;
    final int dimensionArray;
    ZbnfParseResultItem zbnfEmbeddedType = null;
    ClassData instanceClass = null;
    
    boolean bImpliciteClass = zbnfVariable!=null && zbnfVariable.getChild("newObject/impliciteImplementationClass") !=null;
    final String sNameField = zbnfVariable == null ? null : zbnfVariable.getChild("variableName").getParsedString();
    if(sNameField!=null && sNameField.equals("timetest1"))
        stop();
    if( zbnfType.getChild("va_arg") != null)
    { typeClass = CRuntimeJavalikeClassData.clazz_va_argRaw; //field_int_va_arg; //clazzObjectJc;
      dimensionArray = 0; //1;
    } else {
    	typeClass1 = getType(zbnfType, localIdentsP);
    	/**Test special case, String representation. */
      if(typeClass1 == CRuntimeJavalikeClassData.clazzStringJc)
      { final boolean zeroTermString = zbnfDescription != null && zbnfDescription.getChild("zeroTermString")!= null;
        /**Special case, a char* in C: */
        if(zeroTermString){ typeClass = CRuntimeJavalikeClassData.clazz_s0;}
        else { typeClass = typeClass1;}  //standard case: a StringJc in C
      }
      else
      { typeClass = typeClass1;  //standard case, not a StringJc
      }
      dimensionArray = getDimensionsArray(zbnfType);
      //
      if( zbnfDescription != null && (zbnfEmbeddedType = zbnfDescription.getChild("embeddedType")) != null
	    	|| typeClass1.bEmbedded){
	    	if(zbnfEmbeddedType != null){
	    	  String sType = zbnfEmbeddedType.getParsedString();
		    	//dimensionArray = 0;
		      instanceClass = localIdentsP.getType(sType, fileLevelIdents);
		      if(instanceClass == null)
		      	throw new IllegalArgumentException("Java2c - instanceClass not found: " + sType);
		    } else {
	    		instanceClass = typeClass;  //it is an embedded type.
	    	}
      	if("Cc".indexOf(intension)>=0){
      	  writeContent.addIncludeH(typeClass.sFileName, "embedded type in class data");  //it is need, because the type should be known.
	      } else {
	        writeContent.addIncludeC(typeClass.sFileName, "embedded type in class data");  //it is need, because the type should be known.
		    }
	    }
	    else
	    { //forward reference declaration. 
	    }
    }
    //
    final char modeStatic;
    if(definitionCondition != '.'){
      modeStatic = definitionCondition;
    } else if(zbnfVariable != null && zbnfVariable.getChild("static")!= null){ 
        if(zbnfVariable.getChild("final")!= null && typeClass.isPrimitiveType()){
          modeStatic = 'S';  //a constant value of a simple type
        } else { //maybe final, but not simple: The content isn't final, don't declare as const.
        	modeStatic = 's';  //static variable.
        }
    } else if(zbnfDescription !=null && zbnfDescription.getChild("nonPersistent")!= null) {
     	modeStatic = 'r';  //It is admissible that it will be reference a non-persistent String or other referenced object. 
    } else {
    	modeStatic = '.'; 
    }
    
    
    char accessMode;
    String sType = typeClass.getClassCtype_s();
    if(typeClass == CRuntimeJavalikeClassData.clazz_va_argRaw){
    	accessMode ='+';
    }
    else if(sType.equals("StringJc"))
    { accessMode ='t';
    }
    else if(	zbnfEmbeddedType != null 
    	 			|| typeClass.bEmbedded
    				|| (zbnfVariable != null && (bImpliciteClass || zbnfVariable.getChild("impliciteImplementationClass") !=null))
    				){
    	accessMode = '$';
    }
    else if(typeClass.isPrimitiveType())
    { accessMode = '%';
    }
    else if("Aab".indexOf(intension)>=0  //only for arguments or inside statement-blocks
    		   && //(typeClass.isInterface() || 
    		  		(zbnfDescription!=null && zbnfDescription.getChild("dynamic-call")!= null)
    		   )
    { accessMode = '&';  //interface-reference with Method table.
    }
    else if("Cs".indexOf(intension)>=0 && (zbnfDescription==null || zbnfDescription.getChild("noGC")== null))
    { accessMode = '@';  //enhanced reference for class or static variables
    }
    else
    { accessMode = '*';  //a simple reference
    }
    
    final FieldData field;
    
    if(dimensionArray >0)
    { field = getTypeInfoArray(zbnfVariable, zbnfType, zbnfDescription, localIdentsP, sNameField, declaringClass, intension, accessMode, typeClass, modeStatic, dimensionArray);
    }
    else 
    { boolean isFinal = zbnfVariable !=null && zbnfVariable.getChild("final") !=null;
      if(typeClass.bEmbedded){
      	//A embedded type should be intitialized with new
      	if(sNameField != null //NOTE: if sNameField == null, then a return type of a method is created.
      		 && !isFinal && !typeClass.isString()  //the type String has extra procedures.
      		){ //check the Java-syntax for fields, use only the requested pattern.
      		throw new IllegalArgumentException(
      		"The embedded type "+ typeClass.sClassNameJavaFullqualified + " is embedded. "
      		+"\n  Therefore the instance \"" 
      		  + sNameField + "\" should be final and initialized only one time." +
      		"\n  use the set(newValues)-methods of the type instead = newInstance!");
      	}
        field = new FieldData(sNameField, typeClass, typeClass, null, null, modeStatic, accessMode
          , 0, null /*fixArraySize*/
          , '.' /*modeArrayElement*/
          , declaringClass ); 

      }
      else if(zbnfNewObject != null)
      { //TODO zbnfVariablesInitialization = itemNewObject;
        //TODO zbnfAssignment = null;
        field = createFieldDataNewObject(zbnfNewObject, zbnfDescription
        , localIdentsP   //for the new Object. 
        , localIdentsP   //for arguments
        , blockEnvironment
        , sNameField, declaringClass, intension, accessMode, modeStatic, isFinal
        );
      }  
      else 
      { //no new, no array
        //TODO zbnfVariablesInitialization = zbnfAssignment = itemVariableDefinition.getChild("value");  //it may be null
        field = getTypeInfoSimple(zbnfVariable, zbnfType, zbnfDescription
        		 , localIdentsP, sNameField, declaringClass, intension, accessMode
        		 , typeClass
        		 , instanceClass   //may be null, not null on embeddedType or instanceType-annotation java2c
        		 , modeStatic, zbnfNewObject);
      }
    }  
    return field;
  }
  
  
  
  
  FieldData getTypeInfoSimple
  ( final ZbnfParseResultItem zbnfVariable 
  , final ZbnfParseResultItem zbnfType
  , final ZbnfParseResultItem zbnfDescription
  , final LocalIdents localIdentsP
  , String sNameField
  , ClassData declaringClass
  , char intension
  , char accessMode
  , ClassData typeClass
  , ClassData instanceClass
  , char staticMode
  , final ZbnfParseResultItem itemNewObject
  ) throws FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException, ParseException
  { //no new, no array
    //TODO zbnfVariablesInitialization = zbnfAssignment = itemVariableDefinition.getChild("value");  //it may be null
    if(sNameField != null && sNameField.equals("kConstant"))
      stop();
    String[] sDefineValue;
    if(staticMode == 'S'){
      CCodeData codeConstantValue;
      ZbnfParseResultItem zbnfValue;
      List<ZbnfParseResultItem> listZbnfAssignments;
      if(  intension == 's'   //static variable
        && zbnfVariable!= null
        && (zbnfValue = zbnfVariable.getChild("value"))!= null
        && (listZbnfAssignments = zbnfValue.listChildren()).size()==1   //a simple constant assignemnt
        && (codeConstantValue = genConstantValue(listZbnfAssignments.get(0))) != null
        && !codeConstantValue.identInfo.typeClazz.sClassNameC.equals("char*")  //only a const char* is translated to a define.
        ){
        if(typeClass == CRuntimeJavalikeClassData.clazzStringJc){
          /**A StringJc isn't defined as #define, because it is a structure. */
        } else {
        	staticMode = 'd';  //it is a define.
        }
        sDefineValue = new String[1];  
        sDefineValue[0] = codeConstantValue.cCode; //use for initializing
      } else { sDefineValue = null; }
    } else { sDefineValue = null; }   
    if(instanceClass == null){
      /**Check whether an instanceClass is given by java2c= annotation: */
    	final ZbnfParseResultItem zbnfInstanceClass = zbnfDescription == null ? null : zbnfDescription.getChild("instanceType");
	    final String sInstanceClass = zbnfInstanceClass == null ? null: zbnfInstanceClass.getParsedString();
	    if(sInstanceClass == null) { instanceClass = null; }
	    else {
	      //instanceClass = fileLevelIdents.getTypeContingentlyRunFirstPass(sInstanceClass, runRequiredFirstPass); 
	      instanceClass = fileLevelIdents.getType(sInstanceClass, null); 
	    }
    }  
    FieldData type = new FieldData(sNameField, typeClass, instanceClass, null, null
    		, staticMode, accessMode, 0, sDefineValue /*fixArraySize*/
    		, '.' /*modeArrayElement*/, declaringClass); 
    return type;
  }
  
  
  
  
  /**Creates FieldData for a new Type(...)-construction.
   * If a variable is initialized with the new-construction, it may be an embedded instance,
   * or a instance from a given implementation class or others. This things are detected by evaluating
   * the java2c=annotation in the zbnfDescription, or in zbnfNewObject.
   * Then this method returns the complete FieldData for the variable.
   * <br><br>
   * If a new-construction is used in an expression, the param sNameField in null. 
   * But the other informations in FieldData may be a point of interesting, so the adequate form
   * is able to use. 
   * <ul>
   * <li>If the variable is final and the new Type is equal the type of variable (or always, TODO),
   *     then an embedded instance should created. 
   * <li>If the variable is final and it is a StringBuffer or new StringBuilder(size),
   *     then a embedded fix buffer with direct string should created.
   * </ul>
   * @param zbnfNewObject The parse result from expression <code>newObject::=</code>.
   * @param zbnfDescription The description to the variable
   * @param typeIdentsForNew
   * @param localIdentsP The environment for parameters. -not for the new Object. 
   * @param sNameField name of variable
   * @param declaringClass The class where the FieldData should be associated to. 
   *        May be null for statementblock-fields or new in Expression.
   * @param intension
   * @param accessModeP
   * @param staticMode
   * @param isFinal
   * @return
   * @throws FileNotFoundException
   * @throws IllegalArgumentException
   * @throws IOException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @throws ParseException
   */
  FieldData createFieldDataNewObject
  ( final ZbnfParseResultItem zbnfNewObject
  , final ZbnfParseResultItem zbnfDescription
  , final LocalIdents typeIdentsForNew
  , final LocalIdents localIdentsP
  , final StatementBlock blockEnvironment
  , String sNameField
  , ClassData declaringClass  //only to create the FieldData. TODO: create FieldData outside.
  , final char intension
  , final char accessModeP
  , char staticMode
  , boolean isFinal
  ) throws FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException, ParseException
  { char accessMode = accessModeP;
  	final ClassData instanceClass;
  	int sizeFixStringBuffer = 0;
    boolean bEmbeddedStruct;
    final ClassData classdataNewObject;
    if(sNameField !=null && sNameField.equals("timeTest1"))
    	stop();
    assert("Ccmbfs".indexOf(intension) >=0);  //class or block level
    { ZbnfParseResultItem zbnfTypeOfNew = zbnfNewObject.getChild("newClass");
  	  ClassData typeOfNew = getType(zbnfTypeOfNew, typeIdentsForNew);
      if(zbnfNewObject.getChild("impliciteImplementationClass")!=null){	
        //new Type(...){ derived class-content;}
      	//An inner anonymous class is instanciated. It is parsed alreay, 
        // its Classdata are stored in the this.classData already. Use it!
        // The type of new is the interface type mostly.  
      	classdataNewObject = gen_AnonymousClass(
        	zbnfNewObject, zbnfDescription
        ,	classData.classLevelIdents
        , blockEnvironment	
        , sNameField, intension
        , parentGenerateFile
        );
      } else {
      	classdataNewObject = typeOfNew;  //new Type(...)
      }

      //if(true || classdataNewObject == typeClass)
      //String sTypeNewObject = gen_EnvIdent(itemNewObject.getChild("newClass"), "__", idents);  //itemNewObject.getChild("newClass").getParsedString();
      //if(sTypeNewObject.equals(sType))
      { //it is a form Type final name = new Type(...), implement an embedded struct in C.
        String sTypeName = classdataNewObject.getClassNameJava();
        if(isFinal && sTypeName.equals("StringBuffer") || sTypeName.equals("StringBuilder")
        //if(typeVariable == CRuntimeJavalikeClassData.clazzStringBufferJc
          //&& zbnfDescription != null && zbnfDescription.getChild("fixStringBuffer")!= null
          )
        { //The StringBuffer should have a fix direct buffer.
          final ZbnfParseResultItem zbnfArg = zbnfNewObject.getChild("actualArguments");
          if(zbnfArg != null)
          { final ZbnfParseResultItem zbnfValue = zbnfArg.getChild("value");
            if(zbnfValue != null)
            { final ZbnfParseResultItem zbnfNumber = zbnfValue.getChild("intNumber");
              if(zbnfNumber != null)
              {
                sizeFixStringBuffer = (int)zbnfNumber.getParsedInteger();
                accessMode = '$'; //embedded struct.
                instanceClass = classdataNewObject;
              } else {
              	instanceClass = classdataNewObject;
              }
            } else {
            	instanceClass = classdataNewObject;
            }
            
          } else {
          	instanceClass = classdataNewObject;
          }
        }
        else if("CcsS".indexOf(intension)>=0)  //class variable, static class variable:
        { /**A "= new Type(...)" is found, it is an embedded struct only if the referenced is final. */
          if(isFinal)
          { accessMode = '$'; //embedded struct.
            writeContent.addIncludeH(classdataNewObject.sFileName, "embedded type in class data");  //it is need, because the type should be known.
            instanceClass = classdataNewObject;
          } else {
          	instanceClass = classdataNewObject;
          }
        }
        else
        { assert(intension=='b');
          //if it is in an statement block, it is only an embedded struct, in this case in stack,
          //  only if the @java2c=stackInstance. is set.
          if(zbnfDescription != null && zbnfDescription.getChild("stackInstance")!= null)
          { writeContent.addIncludeC(classdataNewObject.sFileName, "embedded type in block");  //it is need, because the type should be known.
            accessMode = '$'; //embedded struct.
          	instanceClass = classdataNewObject;
          } else {
          	instanceClass = classdataNewObject;
          }
        }
      }
    //} else {
    	//instanceClass = classdataNewObject;
    }
    FieldData type = new FieldData(sNameField, classdataNewObject, instanceClass, null, null, staticMode, accessMode
        , sizeFixStringBuffer, null /*fixArraySize*/
        , sizeFixStringBuffer > 0 ? 'B' : '.' /*modeArrayElement*/
        , declaringClass ); 
    return type;

  
  }
  
  
  
  FieldData getTypeInfoArray
  ( final ZbnfParseResultItem zbnfVariable 
  , final ZbnfParseResultItem zbnfType
  , final ZbnfParseResultItem zbnfDescription
  , final LocalIdents localIdentsP
  , String sNameField
  , ClassData declaringClass
  , char intension
  , char accessMode
  , ClassData typeClass
  , char staticMode
  , int dimensionArray
  ) throws FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException, ParseException
  { boolean bEmbeddedArrayElements = (zbnfDescription != null && zbnfDescription.getChild("embeddedYElements")!= null);
  	boolean bSimpleArray = zbnfDescription != null && zbnfDescription.getChild("simpleArray")!= null;
  	boolean bSimpleVariableRef = zbnfDescription != null && zbnfDescription.getChild("simpleVariableRef")!= null;
  	boolean bEmbeddedStruct = false;
    boolean bReference;
    boolean bFinalReference;
    if(sNameField !=null && sNameField.equals("idxP"))
    	stop();
    char modeArrayElement = bEmbeddedArrayElements ? '$' 
                          : accessMode;  //The normal access mode of the basic type, maybe % t * or @
    String[] fixArraySizes = null;
    if(zbnfVariable != null)
    {
      ZbnfParseResultItem zbnfNewArray = zbnfVariable.getChild("newArray");  //it may be or may null.
      ZbnfParseResultItem zbnfConstArray = zbnfVariable.getChild("constArray");  //it may be or may null.
      if(zbnfNewArray != null)
      { //TODO zbnfVariablesInitialization = zbnfNewArray;
        ZbnfParseResultItem zbnfTypeOfNew = zbnfNewArray.getChild("newClass");
        ClassData classdataNewArray = getType(zbnfTypeOfNew, localIdentsP);
        if(classdataNewArray == typeClass)
        { //it is a form Type final name = new Type(...), implement an embedded struct in C.
          if("CsS".indexOf(intension)>=0)
          { //if it is a variable of a class, static or non static,
            //it is an embedded array only if the attribute is final.  not typeClass.isFinal && 
            bEmbeddedStruct = zbnfVariable.getChild("final")!=null;
          }
          else
          { assert("Cb".indexOf(intension)>=0);
            //if it is in an statement block, it is only an embedded struct, in this case in stack,
            //  only if the @java2c=stackInstance. is set.
            bEmbeddedStruct = zbnfDescription != null && 
            ( zbnfDescription.getChild("stackInstance")!= null
            || zbnfDescription.getChild("simpleVariableRef")!= null
            );
          }
          if(bEmbeddedStruct)
          { bReference = false;
            bFinalReference = false;
            fixArraySizes = new String[dimensionArray];
            List<ZbnfParseResultItem> listSize = zbnfNewArray.listChildren("value");
            int idxArraySize = 0;
            for(ZbnfParseResultItem zbnfValueSize : listSize)
            { CCodeData codeValue = genConstantValue(zbnfValueSize);
              fixArraySizes[idxArraySize] = codeValue.cCode;
            }
          }
          else //not bEmbeddedStruct
          { bSimpleArray = false;
            fixArraySizes = null;
          }
        }
        else
        { //different Type1[] name = new Type2[]
          bSimpleArray = false; //unsued, but it have to be initzialized.
          fixArraySizes = null;
        }
        
      }
      else if(zbnfConstArray != null)
      { //TODO zbnfVariablesInitialization = zbnfConstArray;
        fixArraySizes = new String[dimensionArray];
        bEmbeddedStruct = typeClass.isFinal;
        bSimpleArray = bEmbeddedStruct;  //everytime simple if it is final, because it has only deterministic elements. 
        bReference = false;
        bFinalReference = false;
        List<ZbnfParseResultItem> listValues = zbnfConstArray.listChildren(); 
        fixArraySizes[0] = "" + listValues.size();   //only 1-dimensional, nr of values is the size 
      }
      else if(zbnfDescription != null && zbnfDescription.getChild("ByteStringJc") !=null){
      	assert(typeClass == CRuntimeJavalikeClassData.clazz_int8); //the type of the elements.
      	typeClass = CRuntimeJavalikeClassData.clazzByteStringJc;
      	dimensionArray = 1;
      	
      }
      else
      { //no ...new Type[]
        //TODO zbnfVariablesInitialization = null;
        bSimpleArray = false;
        fixArraySizes = null;
      }
                    
    }
    if(typeClass == CRuntimeJavalikeClassData.clazzByteStringJc){
    	accessMode = 'B';
    	modeArrayElement = '%';
    } else if(bSimpleVariableRef)  { accessMode = 'P';
    } else if(bSimpleArray) { accessMode = bEmbeddedStruct ? 'Q': 'P';
    } else                  { accessMode = bEmbeddedStruct ? 'Y': 'X';
    }
    FieldData type = new FieldData(sNameField, typeClass, null, null, null, staticMode, accessMode, dimensionArray, fixArraySizes, modeArrayElement, declaringClass); 
    return type;
  }
  
  
  
  

  
  
  /**It's a debug helper. The method is empty, but it is a mark to set a breakpoint. */
  protected void stop()
  { //debug
  }

  private boolean stopCond = false;
  
  protected void setStop()
  { stopCond = true;
  }
  
  protected void stopCond()
  { if(stopCond){
      stopCond = false; //set breakpoint here.
    }  
  }
  
}
