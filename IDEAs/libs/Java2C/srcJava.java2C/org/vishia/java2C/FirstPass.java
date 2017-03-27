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
 * @version 0.82
 * list of changes:
 * 2008-04-06 JcHartmut: some correction
 * 2008-03-15 JcHartmut: creation
 *
 ****************************************************************************/
package org.vishia.java2C;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.vishia.java2C.ClassData.InitInfoVariable;
import org.vishia.mainCmd.Report;
import org.vishia.zbnf.ZbnfParseResultItem;

/**This class processes the first pass of translating Java to C of a detect class in a java file.
 * Parsing the content of the java file is done before, see {@link GenerateFile}.
 * In the first pass all declared elements of a class are captured and store in the {@link ClassData}.  
 * 
 * @author JcHartmut
 *
 */
public class FirstPass extends GenerateClass
{

	private List<FirstPass> innerClasses;
	
	private ZbnfParseResultItem zbnfClass;
	
	boolean bDontWriteCtor;
	
  /**initializes the instances with the given associations. */ 
  //FirstPass(iWriteContent writeContent, TreeMap<String, ClassData> pkgIdents, RunRequiredFirstPass_ifc runRequiredFirstPass)
  FirstPass(iWriteContent writeContent, GenerateFile parentGenerateFile
  , LocalIdents fileLevelIdents, RunRequiredFirstPass_ifc runRequiredFirstPass
  , Report log
  )
  { super(writeContent, parentGenerateFile, fileLevelIdents, runRequiredFirstPass, log);
  }


  /**Builds the type information of the given class or interface and recursively 
   * all of its inner classes and interfaces. This creates the {@link ClassData} 
   * of the class or interface. It is the first pass of the translation. 
   * <br><br>
   * All internal classes and interfaces are detect, this method is called recursively with it.
   * @param sourceOfClassDataP Info for message on errors, where the ClassData comes from. 
   *        The name of the class will be added in form "sourceOfClassDataP-className" then. 
   * @param fileContainsClass The generation environment
   * @param sFileName Name of the file for C without extension .c or .h, not the Java-filename
   *        It is stored in the created ClassData of the class
   *        to allow the generating of a include statement, if another class uses this class.
   * @param sPkgIdent Package where the Java file is found. With <code>/</code> as Separator and at end.
   * @param sPrefix To Build the Classname for C
   * @param sPostfix To Build the Classname for C.
   * @param zbnfClass parse result of <code>class::=</code>
   * @param sOuterClassName "" or the name of the outer class. 
   *        The name of the outer class is set as prefix of the name of the struct in C
   * @param outerClassData The outer class data or null, to register the inner class in a outer.
   * @param listAllClassesForSecondPath List to add the detect ClassData. 
   *        The list is used as input for running the second path of classes. 
   *        See {@link GenerateFile.itemsClassesForSecondPath}.  
   * @param bInterface
   * @param identsParent
   * @return
   * @throws IOException
   * @throws ParseException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  /**
   * @param uResult
   * @param sourceOfClassDataP
   * @param fileContainsClass
   * @param sFileNameC
   * @param sPkgIdent
   * @param sPrefixClassCname
   * @param sSuffixClassCname
   * @param zbnfClass
   * @param bDontWriteCtor
   * @param sOuterClassName
   * @param outerClassData
   * @param listAllClassesForSecondPath
   * @param bInterface
   * @param classBase
   * @param intension C: inner class, P: file-level (primary) class, Y: class level anonymous, 
   *                     other: anonymous inner class at statement block level
   * @return
   * @throws IOException
   * @throws ParseException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  ClassData buildType
  ( StringBuilder uResult
  , String sourceOfClassDataP
  , GenerateFile fileContainsClass
  , String sFileNameC
  , String sPkgIdent    
  , String sPrefixClassCname, String sSuffixClassCname
  , ZbnfParseResultItem zbnfClass
  , boolean bDontWriteCtor
  , String sOuterClassName
  , ClassData outerClassData
  , List<ClassData> listAllClassesForSecondPath
  , boolean bInterface
  , ClassData classBase
  , char intension
  //, String sFirstInterfaceType
  )
  throws IOException, ParseException, IllegalArgumentException, IllegalAccessException, InstantiationException
  {
  	this.zbnfClass = zbnfClass;
  	this.bDontWriteCtor = bDontWriteCtor;
    ZbnfParseResultItem itemClassDescription = zbnfClass.getChild("description");
    /**true if the class has the modifier static, Java-syntax: */
    boolean bStaticClass = zbnfClass.getChild("static")!=null;
    boolean bAbstractClass = zbnfClass.getChild("abstract")!=null;
    /**true if the class isn't handled by GC, it is a java2c-annotation: */
    boolean bStaticInstance =  itemClassDescription != null
                            && itemClassDescription.getChild("staticInstance")!= null;
    boolean bFinal = zbnfClass.getChild("final")!= null;
    ZbnfParseResultItem itemClassIdent = zbnfClass.getChild("classident");
    /**itemClassIdent is null if it is an anonymous class. */
    final String sClassNameLocal;
    if(itemClassIdent !=null){
    	sClassNameLocal =itemClassIdent.getParsedString();
    } else {
    	/**Anonymous inner class: the variable name is given with the param sPrefixName. 
    	 * Build a className specially, the source code doesn't contain a class name.*/
    	sClassNameLocal = sPrefixClassCname;
    }
    if(sClassNameLocal.equals("TestThread"))
      stop();
    //The sourceOfClassData ist build with path/file.java-className-innerClassname
    final String sourceOfClassData = sourceOfClassDataP + "-" + sClassNameLocal;
    final String sClassNameC;
    final String sClassNameJava;
    
    LocalIdents localIdentsTypes = outerClassData !=null ? outerClassData.classLevelIdents
    		                         : fileLevelIdents;
    final ClassData firstInterface;
    final ClassData superClazz;
    if(classBase != null && !classBase.isInterface()){
    	superClazz = classBase;
    	firstInterface = null;
    } else {
      superClazz = zbnfResultSuperclass(zbnfClass, itemClassDescription, outerClassData);
      firstInterface = classBase;  //may be null
    }
    ClassData[] ifcClassData = zbnfResultInterfaces(zbnfClass, itemClassDescription, localIdentsTypes, firstInterface);
    
    String sProp = "+";
    if(bStaticInstance){sProp +="static+"; }
    if(bFinal){sProp +="final+"; }
    if(bAbstractClass){sProp +="abstract+"; }
    if(bInterface){sProp +="interface+"; }
    if(outerClassData != null)
    { //sClassNameC = outerClassData.getClassIdentName() + "__" + sClassNameLocal;
    	if(!bStaticClass){ sProp += "nonStaticInner+"; }
      sClassNameC = sClassNameLocal + "_" + outerClassData.getClassIdentName(); //+ sOuterClassName;
      sClassNameJava = sClassNameLocal;  ////
      Java2C_Main.singleton.console.reportln(Report.debug, "Java2C-FirstPass.buildType-inner: src=" + sourceOfClassData
      	+ ", name=" + sClassNameC);
      //NOTE: use sClassName for classData from outer class "__" + read from itemClassIdent 
      classData = new ClassData(sourceOfClassData, fileContainsClass, sFileNameC, sPkgIdent, sClassNameJava, sClassNameC + "_s"
          , "XX", 'L', outerClassData, superClazz, ifcClassData, zbnfClass
          , '*', sProp, intension);
      //An inner class is known in the outer by name. This type is known also in all other inner classes.
      //Therefore it is part of the classlevelIdents.
      outerClassData.classLevelIdents.putClassType(sClassNameLocal, classData);
      //The inner class is known to completion idents after running its first pass
      //and to build the stc-file.       
      outerClassData.addInnerClass(classData);
    }
    else
    { //NOTE: use sClassName for classData only read from itemClassIdent 
      //NOTE: if sSuffixClassCname == null, then the full class name is given as prefix.
    	//       The suffix may be empty. The suffix is != null but empty if "prefix*" is written in the config file.
    	sClassNameC = sPrefixClassCname + (sSuffixClassCname != null ?  sClassNameLocal + sSuffixClassCname : "");
      sClassNameJava = sClassNameLocal;
      Java2C_Main.singleton.console.reportln(Report.debug, "Java2C-FirstPass.buildType: src=" + sourceOfClassData
      	+ ", name=" + sClassNameC);
      classData = new ClassData(sourceOfClassData, fileContainsClass, sFileNameC, sPkgIdent, sClassNameJava, sClassNameC + "_s"
          , "XX", 'L', null, superClazz, ifcClassData, zbnfClass
          , '*', sProp, intension);
      //Put in list of this package.
      //A non-inner class is known in the package by name. Store it to find out later in package by name. */
      fileLevelIdents.putClassType(sClassNameLocal, classData); 
      //Register the class just now, to prevent circular call of first pass 
      // if an one-another internal usage of a class as reference is programmed in Java. */
      fileContainsClass.registerClassInFile(classData);  //TODO check also from inner classes???
      
    }
    //store the classData to run the second pass later.
    if(listAllClassesForSecondPath!=null){
    	listAllClassesForSecondPath.add(classData);  //to process in the second path sorted per file
    }
    //
    //run the buildType for all inner classes, so that all inner types are knwon,
    //before the class-definitions are processed. The inner types may be used for declarations in outer
    //not in the order of writing in the java-source-file.
    List<ZbnfParseResultItem> listSubClasses = zbnfClass.listChildren("classDefinition");
    if(listSubClasses != null)
    {	innerClasses = new LinkedList<FirstPass>();
      for(ZbnfParseResultItem itemSubClasses : listSubClasses) {
        FirstPass innerClassFirst = new FirstPass(writeContent, fileContainsClass, fileLevelIdents, runRequiredFirstPass, log);
        innerClasses.add(innerClassFirst);
        innerClassFirst.buildType(uResult, sourceOfClassData, fileContainsClass, sFileNameC, sPkgIdent, null, null
        , itemSubClasses, bInterface, sClassNameLocal, classData , listAllClassesForSecondPath
        , false, null, 'C'
        );
    } }
    //
    //classData.needFinalize();  //set it as default. TODO  
    return classData;
  }

  
  /**Generates the content of a class in the h-file with given ZBNF parse result item of the class.
   * This is the second part of the first pass of translation.
   * This routine is called from {@link GenerateFile}.  
   * <ul>
   * <li>The detected class-fields are written in a struct in the h-File. 
   * <li> The static-fields are written in h-file as extern declaration.
   * <li> The constructors are written as forward declaration in h-file of routines ctor...
   * <li> The methods are written as forward declaration in h-file.
   * </ul> 
   * For fields, the initial values are detected and stored in {@link ClassData#listVariablesToInit}.
   * This action is done inside the called routine {@link GenerateClass#gen_variableDefinition(ZbnfParseResultItem, LocalIdents, List, char, ClassData[])}.
   * <br>
   * The method heads are generated calling {@link #write_methodDeclaration(ZbnfParseResultItem, String, LocalIdents)}. 
   * @param uResult
   * @throws IllegalArgumentException
   * @throws IOException
   * @throws ParseException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  void runFirstPass2(StringBuilder uResult
    ) throws IllegalArgumentException, IOException, ParseException, IllegalAccessException, InstantiationException
  {
    //Then run the first pass of inner classes.
    if(innerClasses != null)
    { for(FirstPass genClass: innerClasses) {
        genClass.runFirstPass2(uResult);
    } }

  	Java2C_Main.singleton.console.reportln(Report.debug, "Java2C-FirstPass.runFirstPass2: name=" + classData.sClassNameC);
    
    if(classData.getClassNameJava().equals("ExpandedDataStruct"))
      stop();
    //setInitialTypeIdentifiers();

    /**Generate content of header: */
    { List<ZbnfParseResultItem> listSubInterfaces = zbnfClass.listChildren("interfaceDefinition");
      if(listSubInterfaces != null)
      { for(ZbnfParseResultItem itemSubInterfaces : listSubInterfaces)
        { write_Interface(itemSubInterfaces, classData.sClassNameJava + "__", classData, uResult);
      } }
    }

    /**Inner classes will be written before, The types {@link ClassData} are known. 
     * now generate the classdata-content of the own class.
     * This call may call recursive this method if some other files should be translated.  
     * */ 
    write_HeaderContent(uResult, classData.fileContainsClass, zbnfClass, bDontWriteCtor, classData.sClassNameC, classData.getSuperClassData(), classData.getInterfaces());
    //writeContent.writeClassH(uHeader.toString());  //TODO inner classes in body: write in the C-file.
    //
    /**The identifier of the outer class are known yet, 
     * it should be transported to all non-static inner classes too 
     * because they should be known there: */
    if(classData.sClassNameJava.equals("MainController"))
      stop();
    //After finishing the first pass, all indentifiers are known. The inner classes are built before.
    //But all identifiers of the outer class should be known to run the second pass.
    //Therefore they are completed now.
    classData.completeFieldIdentsForInnerClasses();
    
  }
  
  

  /**Gets the superClass from ZBNF parse result item.
   * @param zbnfClass The parse result item of <code>classDefinition::=...</code>
   * @param zbnfDescription The parse result item of <code>< description>... test_description::=</code>
   *        inside the <code>classDefinition::=...</code>  
   * @param outerClassData a given outerclass
   * @return The ClassData of the superClass. It is {@link Java2C_Main.CRuntimeJavalikeClassData#clazzObjectJc}
   *         if no superclass is given in Java-code (no <code>extends ...</code>). 
   *         It is null if the description contains <code>@java2c=noObject.</code> and no other superclass is given.
   * @throws FileNotFoundException The Superclass may be translated yet nested, it may be cause some errors. 
   * @throws IllegalArgumentException Errors see {@link #runFirstPassClass(GenerateFile, String, String, String, String, ZbnfParseResultItem, String, ClassData, List, boolean, LocalIdents)}.
   * @throws IOException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @throws ParseException 
   */
  ClassData zbnfResultSuperclass(ZbnfParseResultItem zbnfClass, ZbnfParseResultItem zbnfDescription, ClassData outerClassData) 
  throws FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException, ParseException
  { final ClassData superClazz;
    boolean bNoObject =  zbnfDescription != null && zbnfDescription.getChild("noObject")!= null;
    if(bNoObject)
      stop();
    final ZbnfParseResultItem zbnfSuperClass = zbnfClass.getChild("Superclass");
    if(zbnfSuperClass != null)
    { String sSuperClass = zbnfSuperClass.getParsedString();
      stop();
      if(outerClassData != null)
      { /*If there is an outer class, the superclass may be part of them or may be known in the file. */
        //superClazz = outerClassData.classLevelIdents.getTypeContingentlyRunFirstPass(sSuperClass, runRequiredFirstPass);
        superClazz = outerClassData.classLevelIdents.getType(sSuperClass, fileLevelIdents);
      }
      else {
        //superClazz = fileLevelIdents.getTypeContingentlyRunFirstPass(sSuperClass, runRequiredFirstPass);
        superClazz = fileLevelIdents.getType(sSuperClass, null);
      }
    }
    else
    {
      superClazz = bNoObject ? null : CRuntimeJavalikeClassData.clazzObjectJc;
    }
    return superClazz;
  }  

  
  
  
  
  /**Gets the interfaces from ZBNF parse result item.
   * @param zbnfClass The parse result item of <code>classDefinition::=...</code>
   * @param zbnfDescription The parse result item of <code>< description>... test_description::=</code>
   *        inside the <code>classDefinition::=...</code>  
   * @param outerClassData a given outerclass
   * @param sFirstInterface name of the interface for anonymous classes
   * @return Array of ClassData[] of all Interfaces. If the class hasn't interfaces, it is null.
   * @throws FileNotFoundException The Superclass may be translated yet nested, it may be cause some errors. 
   * @throws IllegalArgumentException Errors see {@link #runFirstPassClass(GenerateFile, String, String, String, String, ZbnfParseResultItem, String, ClassData, List, boolean, LocalIdents)}.
   * @throws IOException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @throws ParseException 
   */
  private ClassData[] zbnfResultInterfaces
  ( ZbnfParseResultItem zbnfClass, ZbnfParseResultItem zbnfDescription
  , LocalIdents localIdentsIfcType
  //		, ClassData outerClassData
  , ClassData firstInterface) 
  throws FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException, ParseException
  { final ClassData[] interfaceClazzes;
    int ixIfc = firstInterface !=null ? 1 : 0;
    final List<ZbnfParseResultItem> zbnflistInterfaces = zbnfClass.listChildren("ImplementedInterface");
    if(zbnflistInterfaces != null){
      interfaceClazzes = new ClassData[zbnflistInterfaces.size() + ixIfc];  //+1 if sFirstInterface given.
      for(ZbnfParseResultItem zbnfInterface: zbnflistInterfaces)
      { String sIdentInterface = zbnfInterface.getParsedString();
        //if(outerClassData != null)
        { /*If there is an outer class, the superclass may be part of them or may be known in the file. */
          //interfaceClazzes[ixIfc] = outerClassData.classLevelIdents.getType(sIdentInterface, fileLevelIdents);
        }
        //else {
          //interfaceClazzes[ixIfc] = fileLevelIdents.getTypeContingentlyRunFirstPass(sIdentInterface, runRequiredFirstPass);
          ClassData ifcClass = localIdentsIfcType.getType(sIdentInterface, fileLevelIdents);
          if(ifcClass == null) 
            throw new IllegalArgumentException("unknown class: " + sIdentInterface);
          interfaceClazzes[ixIfc] = ifcClass;  
        //}
        ixIfc +=1;
      }
    }
    else
    { if(ixIfc ==1){ interfaceClazzes = new ClassData[1]; 
      } else { interfaceClazzes = null;
      }
    }
    if(firstInterface !=null){
      interfaceClazzes[0] = firstInterface;  
    }
    return interfaceClazzes;
  }  

  
  
  
  
  /**Write out the header part of the class.
   * It writes:
   * <pre>
   * /*@CLASS_C NAME @@@@@@@@@@@@@@@@@@@@@* /
   * typedef struct NAME_t
   * { union base_{ ObjectJc object; SUPER super; IFC IFC; } base
   *   VARIABLETYPE VARIABLENAME;
   * } NAME_s;
   * 
   * typedef struct NAME_Y_t { ObjectArrayJc head; " + NAME + "_s data[50]; } NAME_Y;");
   * 
   * extern struct ClassJc_t const reflection_NAME_s;
   *
   * extern STATICVARIABLEDEF NAME;
   * 
   * ...etc TODO  
   * </pre>  
   * @param uHeader The buffer for output.
   * @param fileContainsClass The calling instance.
   * @param zbnfClass The parse result item of the class definition.
   * @param bDontWriteCtor if it is an interface or anonymous class.
   * @param sClassNameC The name, it is dissolved already from zbnfClass
   * @param superClazz The superclass or ObjectJc or null
   * @param ifcClassData null or interfaces
   *
   * @throws FileNotFoundException
   * @throws IllegalArgumentException
   * @throws ParseException
   * @throws IOException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  private void write_HeaderContent
  ( StringBuilder uHeader
  , GenerateFile fileContainsClass
  , ZbnfParseResultItem zbnfClass
  , boolean bDontWriteCtor
  , String XXXsClassNameC
  , ClassData superClazz
  , ClassData[] ifcClassData
  ) 
  throws FileNotFoundException, IllegalArgumentException, ParseException, IOException, IllegalAccessException, InstantiationException
  { String sClassNameC = classData.getClassIdentName();
  	uHeader.append("\n\n/*@CLASS_C " + sClassNameC + " @@@@@@@@@@@@@@@@@@@@@@@@*/");
    uHeader.append("\n\ntypedef struct " + sClassNameC + "_t\n{ ");
    ClassData superClass = classData.getSuperClassData();
    if(superClass != null || ifcClassData != null)
    { uHeader.append("\n  union { ");
      if(superClass !=null){
	    	final String sNameSuper; sNameSuper = (superClass).sClassNameC;
	      if(superClass.isBasedOnObject() || sNameSuper.equals("ObjectJc"))
	      { /**either the superclass based on or is an Object: */
	        uHeader.append("ObjectJc object; ");
	      }
	      if(!sNameSuper.equals("ObjectJc"))
	      { uHeader.append(sNameSuper + " super;");
	        fileContainsClass.addIncludeH(superClazz.sFileName, "superclass");
	      }
      }
      if(ifcClassData != null) for(ClassData ifc: ifcClassData)
      { String sNameIfc_s = ifc.sClassNameC;
        String sNameIfc = ifc.getClassIdentName();
        uHeader.append(sNameIfc_s+ " " + sNameIfc +";");  //Type and name are identical
        fileContainsClass.addIncludeH(ifc.sFileName, "interface");
      }
      uHeader.append("} base; ");
    }
    else
      stop();
    ClassData outerClass = classData.getOuterClass();
    //if( outerClass !=null)   ////TODO next is correct. 
    if(classData.isNonStaticInner)
    { /*implicit  reference to the outer class: */
      uHeader.append("\n  struct " + outerClass.getClassIdentName() + "_t* outer;  //J2C: Reference to outer class, implicit in Java");
    }
    //if(outerClass )
    
    if(sClassNameC.equals("ImplIfc_TestAll"))
      stop();
    
    //The statement block for any usage for classLevel variables.
    StatementBlock statementBlockClassLevel = new StatementBlock(this,classData.classLevelIdents, true, 1);

    
    //process the class Content, write all class variable in the struct
    List<ZbnfParseResultItem> listVariables = zbnfClass.listChildren("variableDefinition");
    if(listVariables != null)
    for(ZbnfParseResultItem itemVariable : listVariables)
    { if(itemVariable.getChild("static")== null)
      { //only non static members.
        { String sVariableName = itemVariable.getChild("variableName").getParsedString();
          if(sVariableName.equals("stressTest"))
            stop();
        }
        ZbnfParseResultItem zbnfVariableDescription = itemVariable.getChild("description"); //may be null.
        CCodeData sDef = gen_variableDefinition(
        	itemVariable, zbnfVariableDescription
        , classData.classLevelIdents
        , statementBlockClassLevel
        , classData.getVariablesToInit()
        , 'C'
        );
        uHeader.append("\n  " + sDef.cCode);
      }  
    }
    
    uHeader.append("\n} " + sClassNameC + "_s;\n  \n");
    uHeader.append("\n#define sizeof_" + sClassNameC + "_s sizeof(" + sClassNameC + "_s)\n");

    uHeader.append("\n\n/**J2c: Definitions of the enhanced reference. It's conditinally because it may be defined in a included header before. */");
    uHeader.append("\n#ifndef " + sClassNameC + "REFDEF");
    uHeader.append("\n  #define " + sClassNameC + "REFDEF");
    uHeader.append("\n  typedef struct " + sClassNameC + "REF_t { ObjectRefValuesJc refbase; struct " + sClassNameC + "_t* ref; } " + sClassNameC + "REF;");
    uHeader.append("\n#endif");
    uHeader.append("\n\n/**J2c: Definitions of the array forms. NOTE: The number of elements are a helper for debug, the really used number depends on the memory size! */");
    uHeader.append("\ntypedef struct " + sClassNameC + "_X_t { ObjectArrayJc head; " + sClassNameC + "REF data[50]; } " + sClassNameC + "_X;");
    uHeader.append("\ntypedef struct " + sClassNameC + "_Y_t { ObjectArrayJc head; " + sClassNameC + "_s data[50]; } " + sClassNameC + "_Y;");
    
    uHeader.append("\n\n extern struct ClassJc_t const reflection_" + sClassNameC + "_s;\n  \n");

    uHeader.append("\n\n/**CONST_Type useable as initializer for embedded/stack-instances*/\n");
    uHeader.append("#define CONST_").append(sClassNameC).append("(OBJP) { CONST_ObjectJc(sizeof(")
           .append(sClassNameC).append("_s), OBJP, &reflection_").append(sClassNameC).append("_s), 0 }\n\n");

    uHeader.append("/**J2C: finalize declaration. It is called by Garbage collector and inside other finalized methods.\n")
           .append(" * It should be called by the user if the instance is removed. */\n")
           .append("void finalize_")
           .append(sClassNameC);
    if(classData.isBasedOnObject()){
    	uHeader.append("_F(ObjectJc* othis, ThCxt* _thCxt);\n\n");
    } else {
    	uHeader.append("_F(").append(sClassNameC).append("_s* ythis, ThCxt* _thCxt);\n\n");
    }
    
    if(listVariables != null)for(ZbnfParseResultItem itemVariable : listVariables)
    { if(itemVariable.getChild("static")!= null)
      { //only static members.
        ZbnfParseResultItem zbnfVariableDescription = itemVariable.getChild("description"); //may be null.
        { String sVariableName = itemVariable.getChild("variableName").getParsedString();
          if(sVariableName.equals("empty"))
            stop();
        }
        List<InitInfoVariable> variablesToInit = classData.getVariablesToInit();  //maybe add the variable there.
        CCodeData sDef = gen_variableDefinition(
        	itemVariable, zbnfVariableDescription
        , classData.classLevelIdents
        , statementBlockClassLevel
        , variablesToInit
        , 's');
        if(sDef.identInfo.modeStatic=='d'){
          uHeader.append("\n" + sDef.cCode);
        }else{
          uHeader.append("\n extern " + sDef.cCode);
        }
      }  
    }
    uHeader.append("\n\n");
    
    if(!bDontWriteCtor){
      writeCtor(zbnfClass, outerClass, uHeader);
    }
    
    { List<ZbnfParseResultItem> listMethods = zbnfClass.listChildren("methodDefinition");
      if(listMethods != null)
      { 
        for(ZbnfParseResultItem itemMethod : listMethods)
        { String sName = itemMethod.getChild("name").getParsedString();
          //register the method with following "?" to test if it is ambiguous.
          classData.testAndSetAmbiguousnessOfMethod(sName);
        } 
        for(ZbnfParseResultItem itemMethod : listMethods)
        { write_methodDeclaration(itemMethod, sClassNameC, classData.classLevelIdents, uHeader);
        } 
      }
    }
    if(classData.isFinalizeNeed()){
      /**Finalize is not written in Java, but need because enhanced references: */
      classData.addMethod("finalize", "finalize", Method.modeOverrideable, CRuntimeJavalikeClassData.clazz_void.classTypeInfo);
    }
    if(classData.inheritanceInfo != null){
      classData.completeInheritanceWithOwnMethods();
    }  
    if(classData.isBasedOnObject()){
      write_Mtbl(uHeader);
    }
    write_ClassCpp(uHeader);
  }  
  
  
  
  
  /**
   * @param zbnfClass
   * @param outerClass
   * @param uHeader
   * @throws IllegalArgumentException
   * @throws IOException
   * @throws ParseException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  private void writeCtor(ZbnfParseResultItem zbnfClass, ClassData outerClass
  //, boolean bInterface
  , StringBuilder uHeader
  ) 
  throws IllegalArgumentException, IOException, ParseException, IllegalAccessException, InstantiationException
  {
    /**Generate ctor. */
    final ClassData classOfCtor;  //
    final ClassData outerClassOfCtor;  //TODO check classOfCtor-usage.
    final String sNameCtor; // = classData.isBasedOnObject()?  "ctorO" : "ctorM";
    int modeCtor = Method.modeCtor;
    if(classData.isNonStaticInner){
    	stop();
    	/**Register the ctor in the outer class, because it is a method of them. */
    	classOfCtor = outerClass;
    	modeCtor |= Method.modeCtorNonStatic;
    	sNameCtor = (classData.isBasedOnObject()?  "ctorO_" : "ctorM_") + classData.getClassNameJava();
      //outerClassOfCtor = outerClass;
    } else {
    	/**Register the ctor as static method in the class, where it constructs. */
    	classOfCtor = classData;
    	sNameCtor = classData.isBasedOnObject()?  "ctorO" : "ctorM";
    }
    //if(!bInterface)
    { List<ZbnfParseResultItem> listConstructors = zbnfClass.listChildren("constructorDefinition");
      if(listConstructors != null)
      { boolean bArgumentSensitive = (listConstructors.size() >1);
        for(ZbnfParseResultItem zbnfConstructor : listConstructors)
        { Method method = gen_methodHeadAndRegisterMethod(
        		zbnfConstructor
        	, modeCtor
        	, classOfCtor  //may be outer class if it is a non-static inner class
        	, sNameCtor
        	, classData.classTypeInfo //return-type
          , null                    //argTypes-array uninitialized, because zbnfConstructor is given.
        	, bArgumentSensitive
        	);
          uHeader.append(method.gen_MethodForwardDeclaration());
          ClassData.MethodWithZbnfItem methodDef = classOfCtor.new MethodWithZbnfItem(method, zbnfConstructor, null, 'c'); 
          classData.methodsWithZbnf.add(methodDef);
        }
      }
      else
      { //no constructor found, create a default constructor because all variable initializations should be done.
        Method method = gen_methodHeadAndRegisterMethod(
        	null
        , modeCtor
        , classOfCtor  //may be outer class if it is a non-static inner class
        , sNameCtor
        , classData.classTypeInfo //return-type
        , null                    //argTypes-array uninitialized, because it haven't args.
      	, false                   //bArgumentSensitive = false. Only one.
        );
        uHeader.append(method.gen_MethodForwardDeclaration());
        ClassData.MethodWithZbnfItem methodDef = classOfCtor.new MethodWithZbnfItem(method, null, null, 'c'); 
        classData.methodsWithZbnf.add(methodDef);
      }
      //uHeader.append("\n\n" + "void finalize_" + sClassNameC + "(ObjectJc* yObj, ThCxt* _thCxt);\n\n");
    }
  }
  
  
  private void write_Mtbl(StringBuilder uHeader)
  {  
    String sClassIdentName = classData.getClassIdentName();
    String sClassName_s = classData.getClassCtype_s();
    boolean isObject;
    uHeader.append("\n\n/* J2C: Method table contains all dynamic linked (virtual) methods");      
    uHeader.append("\n * of the class and all super classes and interfaces. */");      
    uHeader.append("\n extern const char sign_Mtbl_" + sClassIdentName + "[]; //marker for methodTable check");
    uHeader.append("\ntypedef struct Mtbl_" + sClassIdentName + "_t");      
    uHeader.append("\n{ ");      
    String sMtbl = classData.gen_MethodTableDefinitionContent();  //from this class and all its super classes and interfaces.
    uHeader.append(sMtbl);
    uHeader.append("\n} Mtbl_" + sClassIdentName + ";\n\n");      
  }  
  
  
  
  
  
  private void write_ClassCpp(StringBuilder uHeader)
  {  
    String sClassIdentName = classData.getClassIdentName();
    String sClassName_s = classData.getClassCtype_s();
    boolean isObject;
    uHeader.append("\n\n#if defined(__CPLUSPLUSJcpp) && defined(__cplusplus)");
    uHeader.append("\n/* J2C: The C++-class-definition. */");      
    uHeader.append("\nclass " + sClassIdentName + " : private " + sClassIdentName + "_s" );      
    uHeader.append("\n{ public:");      
    String sMethods = classData.gen_ClassCppDefinitionContent();  //from this class and all its super classes and interfaces.
    uHeader.append(sMethods);
    uHeader.append("\n};\n\n");      
    uHeader.append("#endif /*__CPLUSPLUSJcpp*/\n\n");
  }  
  
  
  
  
  
  /**Generates the content of an interface in c- and h-file. 
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException */
  void write_Interface(ZbnfParseResultItem itemClass, String sOuterClassName
  		, ClassData outerClassData, StringBuilder uHeader)
  throws IOException, ParseException, IllegalArgumentException, IllegalAccessException, InstantiationException
  {
    //ZbnfParseResultItem resultItem = resultItemClass.nextSkipIntoComponent(resultItemClass);

    ZbnfParseResultItem itemClassIdent = itemClass.getChild("classident");
    if(itemClassIdent == null) 
    	throw new IllegalArgumentException("<$?classident> not found in " + itemClass.toString());
    ZbnfParseResultItem itemDescription = itemClass.getChild("description");
    if(itemDescription != null)
    { write_Description(itemDescription, uHeader);
    }


    String sClassName = sOuterClassName + itemClassIdent.getParsedString();
    uHeader.append("typedef struct " + sClassName + "_t  /*interface*/\n{");
    uHeader.append("ObjectJc object;");

    if(sClassName == null) throw new ParseException("no className found", 0);

    Iterator<ZbnfParseResultItem> iterMethod = itemClass.iterChildren("methodDefinition");
    if(iterMethod != null)
    { while(iterMethod.hasNext())
      { ZbnfParseResultItem itemMethod = iterMethod.next();
        write_methodDeclaration(itemMethod, sClassName, classData.classLevelIdents, uHeader);
    } }

    uHeader.append("\n} " + sClassName + "_i;\n  \n");
    
    List<ZbnfParseResultItem> listSubClasses = itemClass.listChildren("classDefinition");
    if(listSubClasses != null)
    { for(ZbnfParseResultItem itemSubClasses : listSubClasses)
      { //runFirstPass(itemSubClasses, sClassName + "__", itemsxxx);
      }
    }
  }



  /**writes a method-forward-declaration in Headerfile and registers the method to generate definition.
   * It calls 
   *
   * @param zbnfMethod  with syntax methodDefinition::=.
   * @throws IOException
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   */
  public void write_methodDeclaration(ZbnfParseResultItem zbnfMethod, String sClassName
  		, LocalIdents localIdents, StringBuilder uHeader)
  throws IOException, ParseException, IllegalArgumentException, IllegalAccessException, InstantiationException
  { String sNameJava = zbnfMethod.getChild("name").getParsedString();
    if(sNameJava.equals("createAddressSocket"))
      stop();
    //if(!sName.equals("finalize"))  //NOTE: finalize Method see write_finalizeDefinition()
    {    
      //Method method = classData.searchMethod(sName + "?", null);
      boolean bArgumentSensitive = classData.isAmbiguousnessMethod(sNameJava); //method.isAmbigous();
      ZbnfParseResultItem itemType = zbnfMethod.getChild("type");
      ZbnfParseResultItem zbnfDescription = zbnfMethod.getChild("description");
      ZbnfParseResultItem zbnfReturnDescription = zbnfDescription == null ? null : zbnfDescription.getChild("returnDescription");
      boolean bReturnNew = zbnfDescription != null && zbnfDescription.getChild("return-new")!= null;
      char definitionCondition = bReturnNew ? 'n' : 'r';
      FieldData methodType = createFieldInfo(null, itemType, zbnfReturnDescription, null, localIdents, null, null, 'a', definitionCondition); 
      if(methodType.modeAccess=='&' || methodType.modeArrayElement=='&')
      { /**A variable-definition of TypeMTB of the type is needed in this source.
         * Registers the type for the class.
         * The reference will be defined in the dst.h or dst.c at first. */
        classData.addMtblRefType(methodType.typeClazz.getClassIdentName(), 'a');  //to define the REF-Type.
      }
      Method method = gen_methodHeadAndRegisterMethod(
      	zbnfMethod
      , 0
      , classData
      , sNameJava
      , methodType         //return-type
      , null               //argTypes-array uninitialized, because zbnfMethod is given.
    	, bArgumentSensitive
      );
      if(sNameJava.equals("finalize")){
        /**NOTE: finalize Method see write_finalizeDefinition()*/
        classData.setBodyForFinalize(zbnfMethod);
      }
      else {
        uHeader.append(method.gen_MethodForwardDeclaration());
      }
      ClassData.MethodWithZbnfItem methodDef = classData.new MethodWithZbnfItem(method, zbnfMethod, null, 'm'); 
      classData.methodsWithZbnf.add(methodDef);
             
    }
  }


  

  /**Generates a method head with syntax <code>methodDefinition::=</code> from given ZBNF parse result item
   * and registers the method in the classData. The ambiguousness of the method should be tested before this call,
   * but the unambiguous method name for C is built here: Methods with equal names but different parameter types 
   * are different methods.
   * 
   * @param zbnfMethod Zbnf item defined in <code>&lt;methodDefinition></code> 
   *        or <code>&lt;constructorDefinition></code>. The head hasn't an own zbnf-result item.
   *        The following contained elements are tested:
   *        <ul>
   *        <li><code>&lt;description></code>, calling {@link #get_shortDescription(ZbnfParseResultItem)}
   *        <li><code>&lt;static></code> defined in <code>Modifier::=<?></code> 
   *        <li><code>&lt;argumentList></code>, calling {@link #gen_methodArguments(ZbnfParseResultItem, LocalIdents, char, String[], List)}
   *        </ul>
   *        If this argument is null, a default constructor is generated.
   * @param ctor '.' if it isn't a ctor, 'C'-static ctor of class 'n'-ctor of non-static inner class. 'y'-ctor of anonymous class      
   * @param classOfMethod environment class, for non-static constructors the outer class.
   * @param sMethodNameJava Name of the method, "ctorO" if a constructor from given ObjectJc should be generated.
   * @param typeReturn Type of the method, get outside because this routine is also used to generate constructors.
   *        On constructor generation this argument should be equal the environment class type. 
   * @param bArgumentSensitive true than more as one method with the same Java-name exists. 
   *        It is tested before calling {@link ClassData#isAmbiguousnessMethod(String)}.
   *        The C-name of the method is built regarding the type of the arguments.
   * @return The created Method. It contains the head definition in one line in its private attribute 
   *         {@link Method#sMethodFormalListDefiniton} 
   *         promoted in {@link Method#gen_MethodForwardDeclaration()} and 
   *         {@link Method#gen_MethodHeadDefinition()}. 
   * @throws IOException
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   */
  Method gen_methodHeadAndRegisterMethod
  ( ZbnfParseResultItem zbnfMethod
  , int modeCtor
  , ClassData classOfMethod    
  , String sMethodNameJava
  , FieldData typeReturn
  , FieldData[] argTypes
  , boolean bArgumentSensitive
  )
  throws IOException, ParseException, IllegalArgumentException, IllegalAccessException, InstantiationException
  { LocalIdents methodIdents = new LocalIdents(classOfMethod.classLevelIdents, null);
    ZbnfParseResultItem zbnfDescription = zbnfMethod == null ? null : zbnfMethod.getChild("description");
    String sDescription = zbnfMethod!=null ? get_shortDescription(zbnfDescription) : "Default constructor. ";
    //assert(classOfMethod == classData);  //the argument classOfMethod is unnecessary.
    String sNameUnambiguous = sMethodNameJava;
    String sArguments;
    String sClassName = classOfMethod.getClassIdentName();
    String sMethodClassType_s = classOfMethod.getClassCtype_s();
    if(sMethodNameJava.equals("sendMsg") && sClassName.equals("LogMessageFile_MSG"))
      stop();
    final boolean isStatic = (zbnfMethod !=null && zbnfMethod.getChild("static") != null)
                          || (((modeCtor & Method.modeCtor) !=0) && ((modeCtor & Method.modeCtorNonStatic)==0)); 
    final boolean bFinal = zbnfMethod ==null || zbnfMethod.getChild("final")!=null;  
    if(bFinal)
      stop();
    int modifier = modeCtor;
    if( classOfMethod.isInterface() 
    	||(  !sMethodNameJava.startsWith("ctor") 
    		&& !bFinal                 //final keyword on method
    		&& !isStatic
    		&& !classOfMethod.isFinal  //if the class is final, its methods can't be overridden
    	) )
    { //dynamic called method. 
      modifier |= Method.modeOverrideable;
    }
    if(isStatic){  //static keyword or static ctor
      modifier |= Method.modeStatic;
    }
    if(zbnfDescription != null){
      //if(zbnfDescription.getChild("override-able")!= null){ modifier |= Method.modeOverrideable;}
      if(zbnfDescription.getChild("return-this")!= null)  { modifier |= Method.modeReturnThis; }
      if(zbnfDescription.getChild("return-new")!= null)  { modifier |= Method.modeReturnNew; }
      if(zbnfDescription.getChild("noStacktrace")!= null)  { modifier |= (Method.modeNoStacktrace | Method.modeNoThCxt); }
      if(zbnfDescription.getChild("noThCxt")!= null)  { modifier |= Method.modeNoThCxt; }
    }
    if(argTypes == null && zbnfMethod!=null) //not the default constructor
    { //NOTE: call without zbnfMethod if a default ctor is created.
      String[] sArgSensitive = bArgumentSensitive ? new String[1] : null;
      List<FieldData> listArgTypes = new LinkedList<FieldData>(); //more than 30 args are not expected.
      char intensionArgument = 'a'; //(intension == 'h') ? 'A' : 'a'; //declaration or definition list.
      sArguments = gen_methodArguments(zbnfMethod, methodIdents, intensionArgument, sArgSensitive, listArgTypes);
      if(bArgumentSensitive && sArgSensitive[0].length() >0)
      { sNameUnambiguous += "_" + sArgSensitive[0];
      }
      if(listArgTypes != null && listArgTypes.size() >0)
      { argTypes = new FieldData[listArgTypes.size()];
        listArgTypes.toArray(argTypes);
      }
      else
      { argTypes = null;
      }
    }
    else if(argTypes != null){
    	StringBuilder uArguments = new StringBuilder(100);
    	String sSeparator = "";
    	for(FieldData argType: argTypes){
    		uArguments.append(sSeparator).append(argType.getTypeName()).append(" ").append(argType.getName());
    	  sSeparator = ", ";
    	}
    	sArguments = uArguments.toString();
    } else { 
    	sArguments = "";
    }
    
    final Method methodData = classOfMethod.addMethod( sMethodNameJava, sNameUnambiguous 
      , modifier, typeReturn, argTypes);

    methodData.setMethodIdents(methodIdents);  //classLevelIdents plus arguments.
    String sTypeReturn = typeReturn.gen_Declaration();
    String sMethodHead = "(";  //use the really name.
    String sSep = ", ";
    //if(sMethodNameJava.equals("ctorO"))
    if( (modeCtor & Method.modeCtor) != 0)  //if it is a ctor
    { if((modeCtor & Method.modeCtorNonStatic) != 0){
    	  //ctor of non-static or anonymous class
    	  sMethodHead += "struct " + classOfMethod.getClassIdentName() + "_t* outer, ";
    	}
    	//ClassData outerClass = classData.getOuterClass();
  		//if(outerClass!=null && !classData.isStaticInner){
    	//	sMethodHead += outerClass.getClassCtype_s() + "* ythis, ";
    	//}
      if(classData.isBasedOnObject()){
      	sMethodHead += "ObjectJc* othis";
      } else {
      	sMethodHead += "MemC mthis";	
      }
    }
    else if(methodData.firstDeclaringClass.isInterface())
    { /**An overridden method of an interface-declared method. */
      sMethodHead += "ObjectJc* ithis";  //Note: all interface methods are called with an Object-reference.
    }
    else if(methodData.firstDeclaringClass != classOfMethod)
    { /**An overridden method of an superclass-declared method. */
      sMethodHead += methodData.firstDeclaringClass.getClassCtype_s() + "* ithis";
    }
    else if(!isStatic)
    { sMethodHead += sMethodClassType_s + "* ythis";
    }
    else
    { sMethodHead += "/*static*/ ";
      sSep = "";
    }
    if(sArguments.length() >0){
      sMethodHead += sSep + sArguments;
      sSep = ", ";
    }  
    if(methodData.need_thCxt){
      sMethodHead += sSep + "ThCxt* _thCxt)";
    }
    else{
      sMethodHead += ")";
    }
      
    methodData.setMethodHeadDefiniton(sDescription, sTypeReturn, sMethodHead);
    
    return methodData;
  }


  
  
  
  /**Generates the formal argument list of a method used both by generating method declaration or method definition.
   * @param zbnfMethod Zbnf item defined in <code>argumentList::=<?>...</code>.
   * @param localIdents The known identificators of the calling environment, the type identificators are used especially.
   *        This parameter is used calling {@link #gen_variableDefinition(ZbnfParseResultItem, LocalIdents, List, char intension, ClassData[])}
   *        inside with the parameters of the head. If the intension=='a' (Method definition in C-File),the parameter-variables are added to the localIdents.
   * @param intension Intension of call: 'A'-declaration in h-File, 'a'-definition in c-file, passing on <code>gen_variableDefinition()</code>. 
   * @param retArgSensitiveName [0] is filled with the short name of arg types if not null.
   * @param retArgTypes The list is filled with the detect argument types, used for argument sensitive method call.
   * @return String of formal arguments.
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   */
  String gen_methodArguments
  ( ZbnfParseResultItem zbnfMethod
  , LocalIdents localIdents
  , char intension
  , String[] retArgSensitiveName
  , List<FieldData> retArgTypes
  ) 
  throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  { final Map<String, ZbnfParseResultItem> indexParamDescription;
    ZbnfParseResultItem zbnfDescription = zbnfMethod.getChild("description");
    if(zbnfDescription != null) /**Creates a Map sorted by parameter name with param description: */
    { final List<ZbnfParseResultItem> listZbnfParamDescription = zbnfDescription.listChildren("param");
      if(listZbnfParamDescription != null)
      { indexParamDescription = new TreeMap<String, ZbnfParseResultItem>();
        for(ZbnfParseResultItem zbnfParamDescription: listZbnfParamDescription)
        { String sName = zbnfParamDescription.getChild("variableName").getParsedString();
          indexParamDescription.put(sName, zbnfParamDescription);  
        }
      }
      else { indexParamDescription = null; }
    }
    else { indexParamDescription = null; }
    
    Iterator<ZbnfParseResultItem> iterParam = zbnfMethod.iterChildren("argument");
    boolean bArgumentSensitive = (retArgSensitiveName != null);
    String sArgs = "";
    String sArgSensitiveName = "";
    String sArgSeparator = "";
    if(iterParam != null)
    { byte idxArg = 0;
      boolean bFirst = true;
      while(iterParam.hasNext())
      { ZbnfParseResultItem zbnfParam = iterParam.next();
        String sName = zbnfParam.getChild("variableName").getParsedString();
        ZbnfParseResultItem zbnfParamDescription = indexParamDescription == null ? null : indexParamDescription.get(sName);
        if(zbnfParamDescription != null)
          stop();
        if(sName.equals("args"))
          stop();
        CCodeData codeArg = gen_variableDefinition(
        	zbnfParam
        , zbnfParamDescription
        , localIdents
        , null              //blockEnvironment unused because no intial values possible.
        , null
        , intension
        );
        //sArgs += sArgSeparator + sArg;
        if(bFirst){ 
          bFirst = false;
        } else { 
          sArgs += ", ";
        }  
        sArgs += codeArg.cCode;
        if(bArgumentSensitive)
        { //sArgSensitiveName += typeVariable[0].sArgSensitiveName;
          sArgSensitiveName += codeArg.identInfo.typeClazz.sArgSensitiveName;
          int dimension = codeArg.identInfo.getDimensionArray();
          while(dimension >0)
          { dimension -=1;
            sArgSensitiveName += "Y";  //array  
          }
        }
        if(retArgTypes != null) { retArgTypes.add(codeArg.identInfo); }
        idxArg +=1;
        sArgSeparator = ", ";
      }
    }
    if(bArgumentSensitive){ retArgSensitiveName[0] = sArgSensitiveName; }
    return sArgs;
  }  


  
  
  
  
}
