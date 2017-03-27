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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.vishia.mainCmd.Report;
import org.vishia.util.FileSystem;
import org.vishia.zbnf.ZbnfParseResultItem;

/**This class processes the first and second pass of translating Java to C of a java file.
 * Parsing the content of the java file is done before it processed, see {@link Java2C_Main}.
 * 
 * @author JcHartmut
 *
 */
public class GenerateFile implements iWriteContent
{
	private Report report;

  /**Name without path and extension of the c- and h-file from java file. */
  final String sFileNameC;
  
  final JavaSrcTreeFile javaSrc;
  
  /**Package where the Java file is stored, with <code>/</code> as separators and at end.
   * This information is got in {@link #runFirstPass(ZbnfParseResultItem, LocalIdents, String, File)}
   * from the <code>package</code>-declaration in the Java file. The dots <code>.</code> in Java file 
   * are translated to slash <code>/</code> here.
   *  */
  String sPkgIdent;

  /**lines for c file mapping the definitions in the C-file before the content of the bodies,
   * see {@link sClassC}. */
  private final StringBuilder uFileCDefinitions = new StringBuilder(10000) ;

  /**lines for c file mapping the definitions in the C-file before the content of the bodies,
   * see {@link sClassC}. */
  private final StringBuilder uFileCSecondPath = new StringBuilder(100000) ;

  /**lines for c file mapping the definitions in the C-file before the content of the bodies,
   * see {@link sClassC}. */
  private final StringBuilder uFileHDefinitions = new StringBuilder(10000) ;

  /**lines for c file mapping the class content. */
  //private String sClassC = "" ;

  private final JavaSources javaSources;
  
  //private JavaSrcTreePkg itsPkg;
  
  /**Copy of the reference from {@link #javaSrc}.{@link JavaSrcTreeFile#itsPkg}.{@link JavaSrcTreePkg.pkgIdents}
   * for faster access.
   */
  private final LocalIdents pkgIdents;
  
  /**All Type identifier ({@link ClassData} which are visible at file level. It contains all classes
   * which are imported in this file. It doesn't contain the package visible identifier, see 
   * 
   */
  private final LocalIdents fileLevelIdents = new LocalIdents("~/");

  /**Index of all classes from which an enhanced reference is need in this compilation unit. 
   * Enhanced references are used for backward references for garbage collection and to store 
   * an index to the method table.
   * NOTE: the second part of Map, the Object, isn't necessary. Only the unique registering is necessary.*/
  private final Map<String, Object> usedEnhancedRefTypes = new TreeMap<String, Object>();
  
  /**Index of all classes from which an method-table reference is need in the header file. 
   * Method-table-references are used to work with dynamic call, 
   * the method table pointer and the instance pointer are contained there.
   * NOTE: the second part of Map, the Object, isn't necessary. Only the unique registering is necessary.*/
  private final Map<String, Object> usedMtblRefTypesH = new TreeMap<String, Object>();
  
  /**Index of all classes from which an method-table reference is need in this compilation unit. 
   * Method-table-references are used to work with dynamic call, 
   * the method table pointer and the instance pointer are contained there.
   * NOTE: the second part of Map, the Object, isn't necessary. Only the unique registering is necessary.*/
  private final Map<String, Object> usedMtblRefTypesC = new TreeMap<String, Object>();
  
  /**The file descriptor for the structure file.
   */
  private File fileStruct;
  
  /**List of all classes in the file. Inner classes are not stored here. 
   * Typically only one class is found in file. 
   * This information is used locally only at example to write all structure informations of classes in file*/
  private final List<ClassData> listClassInFile = new LinkedList<ClassData>();
  
  /**The interface to call a firstpass run of a needed type. */
  final RunRequiredFirstPass_ifc runRequiredFirstPass;


  /**All include declarations necessary in the C-file of this java file. 
   * The list will be filled also while running the second path.
   */
  private final TreeMap<String, String> includes = new TreeMap<String, String>();

  private final TreeMap<String, String> imports = new TreeMap<String, String>();

  /**List of all classes found in this file. This list is used to control, for which classes
   * the second path is to be run. The list contains inner classes also. 
   * That is because inner classes are translated to plain <code>typedef struct...</code> and plan C-routines.
   */
  final List<ClassData> listAllClassesForSecondPath = new LinkedList<ClassData>();

  
  
  
  
  
  /**initializes the instances with the given associations. */ 
  GenerateFile(JavaSources javaSources, JavaSrcTreeFile javaSrc
  		, RunRequiredFirstPass_ifc runRequiredFirstPass
  		, File fileStruct, Report report)
  { this.fileStruct = fileStruct;
    this.report = report;
  	this.javaSources = javaSources;
    this.javaSrc = javaSrc;
    this.pkgIdents = javaSrc.getPkgLevelTypes();
    this.sFileNameC = javaSrc.getFileNameC();
    this.runRequiredFirstPass = runRequiredFirstPass;
  }


  /**Adds a text to the C-definition-code
   */
  @Override public void writeCdefs(StringBuilder content)
  { uFileCDefinitions.append(content);
  }

  /**Adds a text to the H-code
   */
  @Override public void writeHdefs(StringBuilder content)
  { uFileHDefinitions.append(content);
  }

  /**Adds a text to the C-code
   * @see org.vishia.java2C.iWriteContent#writeClassC(java.lang.String)
   */
  @Override public void writeClassC(String content)
  { uFileCSecondPath.append( content);
  }

  /**Adds a text to the C-code
   * @see org.vishia.java2C.iWriteContent#writeClassC(java.lang.StringBuilder)
   */
  @Override public void writeClassC(StringBuilder content)
  { uFileCSecondPath.append( content);
  }

  /**Adds a text to the Headerfile-code
   * @see org.vishia.java2C.iWriteContent#writeClassH(java.lang.String)
   */
  public void writeClassH(String line)
  { uFileHDefinitions.append(line);
  }


  /**Adds a type to include the type-correspondent h-file in the c-file. */ 
  /* @see org.vishia.java2C.iWriteContent#addIncludeC(java.lang.String, java.lang.String)
   */
  public void addIncludeC(String sFileName, String comment)
  { if(sFileName == null)
      stop();
    if(sFileName !=null //it is null on clazz_s0
    	&& !sFileName.equals(this.sFileNameC))        //don't include itself.
    { if(sFileName.equals("DispatcherThread"))
        stop();
      if(includes.get(sFileName) == null)
      { includes.put(sFileName, "c" + comment);  //not put if addIncludesH will be called before.
      }
    }  
  }

  /**Adds a type to include the type-correspondent h-file in the h-file. */ 
  public void addIncludeH(String sFileName, String comment)
  { if(sFileName !=null   //null on standard types
  	  && !sFileName.equals(this.sFileNameC))        //don't include itself.
    { if(sFileName.equals("MsgDispatcher_MSG"))
        stop();
      includes.put(sFileName, "h" + comment);
    }  
  }



  private String unusedyet_getImport(String sType)
  { return imports.get(sType);
  }




  
  
  /**Generates the content of all classes in the h-file with given ZBNF parse result item of the class.
   * This routine is called from {@link Java2C_Main}.
   * <br>
   * First the h-file-content of all classes are generated into StringBuffer. If all runFirstPass
   * of all found classes are done, the file will be written. First the necessary includes will be written
   * in the destination h-file. Therefore the runFirstPass of all classes should be done before.
   *   
   * @param resultItem The first result item of ZBNF parsing of the whole java-File.
   * @param pkgIdents Global map of all classes of the whole translating process.
   * @param sPathOut output path for the h- and c-file, with slash on end
   * @throws IOException
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   */
  void runFirstPassFile(ZbnfParseResultItem resultItem, String sPrefixClassCname, String sSuffixClassCname
              , String sPathOut) //, LocalIdents identsParent)
  throws IOException, ParseException, IllegalArgumentException, IllegalAccessException, InstantiationException
  { //while(resultItem != null)
    ZbnfParseResultItem zbnfPkg = resultItem.getChild("PackageDefinition");
    { String pkg = "";
      Iterator<ZbnfParseResultItem> iterZbnfPkgName = zbnfPkg.iterChildren("packageClassName");
      while(iterZbnfPkgName.hasNext())
      { pkg += iterZbnfPkgName.next().getParsedString() + "/";
      }
      sPkgIdent = pkg; //.substring(0, pkg.length()-1); //without last dot. 
    }
    /**Copy all pkgIdents to fileLevelIdents initially, additional put the pkgIdents of import packages
     * and Idents of all imported classes:*/
    { fileLevelIdents.putClassTypesAll(pkgIdents);
      evaluateImports(resultItem);
    }  
    String sourceOfClassData = this.javaSrc.getFileJava().getAbsolutePath();
	  Iterator<ZbnfParseResultItem> iter= resultItem.iterChildren("classDefinition");
    while(iter != null && iter.hasNext())
    { resultItem = iter.next();
      String sSemantic = resultItem.getSemantic();
      boolean bInterface = sSemantic.equals("interfaceDefinition");
      if(sSemantic.equals("classDefinition") || bInterface)
      { FirstPass genClass = new FirstPass((iWriteContent)this, this, fileLevelIdents, runRequiredFirstPass, report);
      	StringBuilder uResult = new StringBuilder(10000); //will be increased if necessary.
        ClassData classData = genClass.buildType(
          uResult
        , sourceOfClassData
        , this
        , sFileNameC
        , sPkgIdent
        , sPrefixClassCname, sSuffixClassCname    
        , resultItem   //the ZbnfParseResultItem of the class
        , false        //write constructors, at least a default constructor
        , ""
        , null  //no outer class
        , listAllClassesForSecondPath   //to add this class and sub classes if found
        , bInterface
        , null
        , 'P'
        );
        genClass.classData.completeTypesForInnerClasses();
        //Now run the first pass of the class.
        genClass.runFirstPass2(uResult);
        
        writeClassH(uResult.toString());  //TODO inner classes in body: write in the C-file.
        classData.reportContent(report, Report.fineInfo);
      }
      //resultItem = resultItem.next(null);
    }
    iter= resultItem.iterChildren("interfaceDefinition");
    while(iter != null && iter.hasNext())
    { resultItem = iter.next();
      String sSemantic = resultItem.getSemantic();
      if(sSemantic.equals("classDefinition") || sSemantic.equals("interfaceDefinition"))
      { FirstPass genClass = new FirstPass((iWriteContent)this, this, fileLevelIdents, runRequiredFirstPass, report);
      	StringBuilder uResult = new StringBuilder(10000); //will be increased if necessary.
      	ClassData classData = genClass.buildType
        ( uResult, sourceOfClassData, this, sFileNameC, sPkgIdent, sPrefixClassCname, sSuffixClassCname
        , resultItem   //the ZbnfParseResultItem of the class
        , true         //don't write the default constructor
        , ""
        , null  //no outer class
        , listAllClassesForSecondPath   //to add this class and sub classes if found
        , true  //interface
        , null
        , 'F'   //intension: file level, Top-level-class
        );
      	//Now run the first pass of the class.
        genClass.runFirstPass2(uResult);
        writeClassH(uResult.toString());  //TODO inner classes in body: write in the C-file.
        classData.reportContent(report, Report.fineInfo);
      }
    }

    /**created header and c file. */
    BufferedWriter fileH; //, fileC;

    FileSystem.mkDirPath(sPathOut + sFileNameC);  //create directory if not exists.
    fileH = new BufferedWriter(new FileWriter(new File(sPathOut + sFileNameC + ".h")));

    String sFileNameIncludedef = "__" + sFileNameC.replace('/', '_') + "_h__";
    
    fileH.write("/**************************************************************************\n");
    fileH.write(" * This file is generated by Java2C\n");
    fileH.write(" **copyright***************************************************************\n");
    fileH.write(" *************************************************************************/\n");
    fileH.write("#ifndef " + sFileNameIncludedef + "\n");
    fileH.write("#define " + sFileNameIncludedef + "\n");
    fileH.write("\n#include \"Jc/ObjectJc.h\"        //basic concept\n");
    fileH.write("\n#include \"Jc/StringJc.h\"        //used often\n");
    fileH.write("\n#include \"Fwc/fw_Exception.h\"   //basic concept\n");
    //fileH.write("\n#include \"" + Java2C_Main.singleton.getFileAllReferences() + "\"\n");

    Set<Map.Entry<String,Object>> listUsedEnhancedRefTypes = usedEnhancedRefTypes.entrySet();
    if(listUsedEnhancedRefTypes.size() >0)
    { 
    	fileH.write("\n\n/* J2C: Enhanced references *********************************************************");
    	fileH.write("\n * In this part all here used enhanced references are defined conditionally.");
    	fileH.write("\n * The inclusion of all that header files isn't necessary, to prevent circular inclusion.");
    	fileH.write("\n * It is adequate a struct pointer forward declaration.");
    	fileH.write("\n */");
      for(Map.Entry<String,Object> usedType: listUsedEnhancedRefTypes)
      {
        String sRef = usedType.getKey();
        fileH.write("\n#ifndef " + sRef + "REFDEF");
        fileH.write("\n  #define " + sRef + "REFDEF");
        fileH.write("\n  typedef struct " + sRef + "REF_t { ObjectRefValuesJc refbase; struct " + sRef + "_t* ref; } " + sRef + "REF;");
        fileH.write("\n#endif");
      }
      fileH.write("\n");
    }
    
    Set<Map.Entry<String,Object>> listUsedMtblRefTypes = usedMtblRefTypesH.entrySet();
    if(listUsedMtblRefTypes.size() >0)
    { fileH.write("\n\n/* J2C: Method-table-references *********************************************************/");
      for(Map.Entry<String,Object> usedType: listUsedMtblRefTypes)
      {
        String sRef = usedType.getKey();
        fileH.write("\n#ifndef " + sRef + "MTBDEF");
        fileH.write("\n  #define " + sRef + "MTBDEF");
        fileH.write("\n  typedef struct " + sRef + "MTB_t { struct Mtbl_" + sRef + "_t const* mtbl; struct " + sRef + "_t* ref; } " + sRef + "MTB;");
        fileH.write("\n#endif\n");
      }
      fileH.write("\n");
    }
      
    /*
    for(ClassData classData : listClassInFile)
    { Set<Map.Entry<String,Object>> listUsedEnhancedRefTypes = classData.usedEnhancedRefTypes.entrySet();
      if(listUsedEnhancedRefTypes.size() >0)
      { for(Map.Entry<String,Object> usedType: listUsedEnhancedRefTypes)
        {
          String sRef = usedType.getKey();
          fileH.write("#ifndef " + sRef + "REFDEF\n");
          fileH.write("  #define " + sRef + "REFDEF\n");
          fileH.write("  typedef struct " + sRef + "REF_t { ObjectRefValuesJc refbase; struct " + sRef + "_t* ref; } " + sRef + "REF;\n");
          fileH.write("#endif\n");
        }
      }
      
    }
    */
    
    
    //write includes:
    if(sFileNameC.equals("MainController"))
      stop();
    Set<Map.Entry<String, String>> listIncludeTypes = includes.entrySet();
    fileH.write("\n\n/* J2C: includes *********************************************************/\n");
  	for(Map.Entry<String, String> entry: listIncludeTypes)
    { String sType = entry.getKey();
      String sValue = entry.getValue();
      if(sValue.startsWith("h"))
      { final String sComment = sValue.length() >1 ? "  //" + sValue.substring(1) : ""; 
        fileH.write("#include \"" + sType + ".h\"" + sComment + "\n");
      }
    }

    //fileH.write(sIncludeH);
  	fileH.append(uFileHDefinitions);
  	fileH.write("#endif //" + sFileNameIncludedef + "\n");
    fileH.flush();

  }


  
  void registerClassInFile(ClassData classData)
  {
    if(classData.getClassNameJava().equals(javaSrc.getTypeName())){
      /**The public class of the file is translated: set the classData. */
      javaSrc.setClassData(classData);
    } else {
      /**Additional classes in the file are found. They were unknown until now, register it. */
      pkgIdents.putClassType(classData); //replace JavaSrcFile with ClassData!
      fileLevelIdents.putClassType(classData.sClassNameJava, classData);
    }
    listClassInFile.add(classData);
    
  }
  
  
  

  /**Generates the content of a class in the c-file. The class field {@link listAllClassesForSecondPath}
   * is filled while running the first pass and contains all classes found in the java file.
   * The ZBNF parse result item of each class is stored there.
   * This routine is called from {@link Java2C_Main}.
   * <br>
   * First the c-file-content of all classes are generated into the StringBuffer {@link sClassC}. 
   * If all runSecondPass of all found classes are done, the file will be written. 
   * First the necessary includes will be written
   * in the destination c-file. Therefore the runSecondPass of all classes should be done before.
   * @param pkgIdents Global map of all classes of the whole translating process.
   * @throws IOException
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   */
  //void runSecondPass(TreeMap<String, ClassData> pkgIdents) throws IOException, ParseException
  void runSecondPassFile() throws IOException, ParseException, IllegalArgumentException, IllegalAccessException, InstantiationException
  {


    //sClassC = "";

    for(ClassData classData: listAllClassesForSecondPath)
    { ZbnfParseResultItem itemClass = classData.getParseResult();
      SecondPass genClass = new SecondPass((iWriteContent)this, this, fileLevelIdents, classData, runRequiredFirstPass, report);
      genClass.runSecondPassClass(itemClass, "");
      classData.relinquishParseResult();
    }


    /**created c file. */
    BufferedWriter fileC;
    String sPathOut = Java2C_Main.singleton.getPathOut();
    fileC = new BufferedWriter(new FileWriter(new File(sPathOut + sFileNameC + ".c")));
    fileC.write("/**************************************************************************\n");
    fileC.write(" * This file is generated by Java2C\n");
    fileC.write(" **copyright***************************************************************\n");
    fileC.write(" *************************************************************************/\n");
    fileC.write("#include \"" + sFileNameC + ".h\"\n");
    fileC.write("#include <string.h>  //because using memset()\n");
    fileC.write("#include <Jc/ReflectionJc.h>   //Reflection concept \n");
    fileC.write("#include <Fwc/fw_Exception.h>  //basic stacktrace concept\n");
    fileC.flush();
    //write includes:
    if(sFileNameC.equals("MainController"))
      stop();
    Set<Map.Entry<String, String>> listIncludeTypes = includes.entrySet();
    for(Map.Entry<String, String> entry: listIncludeTypes)
    { String sType = entry.getKey();
      String sValue = entry.getValue();
      if(sValue.startsWith("c"))
      { final String sComment = sValue.length() >1 ? "  //" + sValue.substring(1) : ""; 
        fileC.write("#include \"" + sType + ".h\"" + sComment + "\n");
      }
    }

    Set<Map.Entry<String,Object>> listUsedMtblRefTypes = usedMtblRefTypesC.entrySet();
    if(listUsedMtblRefTypes.size() >0)
    { fileC.write("\n\n/* J2C: Method-table-references *********************************************************/");
    	for(Map.Entry<String,Object> usedType: listUsedMtblRefTypes)
      {
        String sRef = usedType.getKey();
        fileC.write("\n#ifndef " + sRef + "MTBDEF");
        fileC.write("\n  #define " + sRef + "MTBDEF");
        fileC.write("\n  typedef struct " + sRef + "MTB_t { struct Mtbl_" + sRef + "_t const* mtbl; struct " + sRef + "_t* ref; } " + sRef + "MTB;");
        fileC.write("\n#endif\n");
      }
      fileC.write("\n");
    }
    fileC.append(uFileCDefinitions);
    fileC.append(uFileCSecondPath);
    fileC.flush();
    writeStructure(fileStruct, sFileNameC, listClassInFile, sPkgIdent);
  }

  
  
  
  

  /**If a type is used as enhanced reference in the code of the class, it is noted here.
   * The information will be used to define the enhanced references in the Headerfile,
   * it is also transformed to the stc file. (?)
   * @param sRefType The referenced type.
   */
  public void addEnhancedRefType(String sRefType)
  {
    usedEnhancedRefTypes.put(sRefType, sRefType);
  }
  
  /**If a type is used as method-table- reference in the code of the class, it is noted here.
   * The information will be used to define the enhanced references in the Headerfile,
   * it is also transformed to the stc file. (?)
   * @param sRefType The referenced type.
   */
  public void addMtblRefType(String sRefType, char intension)
  {
    if("b".indexOf(intension)>=0){ 
      usedMtblRefTypesC.put(sRefType, intension);
    } else {
      usedMtblRefTypesH.put(sRefType, intension);
    }
  }
  
  
  
  
  /**
   * @param fileStructP
   * @param sFilePathNameC Path and name for C,without extension
   * @param listClassInFile
   * @param sPkgIdent
   * @throws IOException
   */
  static void writeStructure(File fileStructP, String sFilePathNameC, List<ClassData> listClassInFile, String sPkgIdent) throws IOException
  {
    FileOutputStream fileOut = new FileOutputStream(fileStructP);
    OutputStreamWriter streamOut = new OutputStreamWriter(fileOut, "UTF8");
    String sEncoding = streamOut.getEncoding();
    BufferedWriter out = new BufferedWriter(streamOut);
    out.append("<?Java2C-stc www.vishia.org version=\"0.94\" encoding=\"" + sEncoding + "\" ?>\n");
    out.append("Structure of translated java-file \"" + sFilePathNameC + "\"\n");
    out.append("package " + sPkgIdent + ";\n");
    for(ClassData classData: listClassInFile)
    {
      classData.writeStructureClass(out,0);
      
    }
    out.close();
    
  }
  
  
  
  
 

  
  void evaluateImports(ZbnfParseResultItem resultFile) 
  throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException, ParseException
  {
    List<ZbnfParseResultItem> itemsImport = resultFile.listChildren("importStatement");
    if(itemsImport!=null)for(ZbnfParseResultItem itemImport: itemsImport)
    { List<ZbnfParseResultItem> listPkg = itemImport.listChildren("package");
      String sPkgPath = "";
      JavaSources.ClassDataOrJavaSrcFile pkgData = null;
      //LocalIdents pkgTypes1 = pkgIdents;  //start with own package, there are the commonly idents known too.
      LocalIdents pkgTypes1 = Java2C_Main.getRootLevelIdents();
      LocalIdents fileLevelIdents1 = this.fileLevelIdents;
      if(listPkg != null) //for...
      for(ZbnfParseResultItem zbnfPkg: listPkg){
        String sPkg = zbnfPkg.getParsedString();
        sPkgPath += sPkg + "/";
        if(sPkgPath.equals("org/vishia/util/"))
        	stop();
        //pkgData = pkgTypes1.getTypeInfo(sPkg, fileLevelIdents1);  //fileLevelIdents=null
        //if(pkgData == null){
        //  throw new ParseException("Package is unknown: " + sPkgPath, 0);
        //}
        //fileLevelIdents1 = null;
        //pkgTypes1 = pkgData.getLocalIdents(sPkg);
      }
      ZbnfParseResultItem importClass = itemImport.getChild("class");
      if(importClass != null){
        String sClass = importClass.getParsedString();
        if(sClass.equals("StringPart"))
        	stop();
        String sImportClassPath = sPkgPath + sClass;
        JavaSources.ClassDataOrJavaSrcFile typeClazz = 
        	pkgTypes1.getTypeInfo(sImportClassPath, fileLevelIdents);  //NOTE: The package path may be pkg-local too.
        if(typeClazz == null){
          throw new ParseException("Class is unknown: " + sPkgPath + "/" + sClass, 0);
        }
        fileLevelIdents.putClassType(sClass, typeClazz);
      }
      else {
        /**Import of a whole package: */
      	pkgData = pkgTypes1.getTypeInfo(sPkgPath, null);
      	JavaSrcTreePkg pkgTree = pkgData.getJavaPkg();
      	List<JavaSources.ClassDataOrJavaSrcFile> publicClasses = pkgTree.getPublicClasses();
      	
        for(JavaSources.ClassDataOrJavaSrcFile type: publicClasses){
          String sNameType = type.getTypeName();
          fileLevelIdents.putClassType(sNameType, type);
        }
      }
    }    
  }
  
  
  
  
  @Override public String toString(){ return sFileNameC; }
  
  /**It's a debug helper. The method is empty, but it is a mark to set a breakpoint. */
  void stop()
  { //debug
  }

}
