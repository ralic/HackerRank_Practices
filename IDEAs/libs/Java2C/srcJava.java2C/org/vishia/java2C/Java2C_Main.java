/****************************************************************************
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
import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map;

import org.vishia.mainCmd.Report;
import org.vishia.util.FileSystem;


import java.io.BufferedWriter;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

import org.vishia.util.StringPart;
import org.vishia.util.StringPartFromFileLines;
import org.vishia.zbnf.ZbnfJavaOutput;
import org.vishia.zbnf.ZbnfParseResultItem;
import org.vishia.zbnf.ZbnfParser;


/**This class is the main class of translation Java2C. 
 * It is used from the command line call-implementation class {@link Java2C}.  
 * @author JcHartmut
 *
 */
public class Java2C_Main implements RunRequiredFirstPass_ifc
{

  /**Aggregation to the Console implementation class.*/
  Report console;

  /**One file entry to translate. Instances of this class are created while processing 
   * {@link org.vishia.util.FileSystem#addFileToList(String, org.vishia.util.FileSystem.AddFileToList)}
   * calling {@link }
   * called in {@link #addInputFilemask(String sMask, String, String)}
   * evaluating the input arguments of command line call.
   * 
   * @author JcHartmut
   *
   */
  private static class FileIn
  { final File file; 
    final String sPrefix, sPostfix;
    
    private boolean bTranslated = false;
    
    FileIn(File file, String sPrefix, String sPostfix){ this.file = file; this.sPrefix = sPrefix; this.sPostfix = sPostfix; }
    
    public void setTranslated(){ bTranslated = true; }
    
    public boolean isTranslated(){ return bTranslated; }
  }
  
  
  /**Helper class to create instances of {@link Java2C_Main.FileIn} for each detect input file. 
   * The class holds informations for pre- and postfixes for the C-filenames.
   * This class implements the {@link org.vishia.util.FileSystem.AddFileToList}-interface.
   * <br>
   * using:
   * <ul>
   * <li>Only one private instances {@link Java2C_Main#listFileIn} is created in the outer class 
   * {@link Java2C_Main}. 
   * <li>The instance is used  processing all input files. That is either on any <code>-i:INPUT</code>-argument
   * or evaluating a given <code>-if:INPFILE</code> inside {@link #readConfigFile(String)}.
   * <li>Both routines call {@link #addInputFilemask(String sMask, String sCPrefix, String sCPostfix)}.
   * <li>Inside that routine 
   * {@link org.vishia.util.FileSystem#addFileToList(String, org.vishia.util.FileSystem.AddFileToList)}
   * is called with this instance. 
   * <li>Inside <code>addFileToList</code> the interface method 
   * {@link org.vishia.util.FileSystem.AddFileToList#add(File)} is called. 
   * This routine is implemented here and adds instances of {@link Java2C_Main.FileIn} per detected file.
   * 
   * @author JcHartmut
   *
   */
  private static class ListFileIn implements FileSystem.AddFileToList
  {
    /**List of all files, which are inputs for Java2C-translation. */
    private List<FileIn> listFileIn = new LinkedList<FileIn>();
    
    /**Prefix and Postfix for actual parsing. It is temporary used. The Prefix and Postfix 
     * for translated C-Files is stored in {@link FileIn#sCName} using this informations.
     */
    private String sPrefix = "", sPostfix = "";
    
    
    /**Adds a file called in {@link org.vishia.util.FileSystem#addFileToList(String, org.vishia.util.FileSystem.AddFileToList)}.
     * @see org.vishia.util.FileSystem.AddFileToList#add(java.io.File)
     */
    public void add(File file)
    { String sJavaFileName = file.getName();
      //String sCName = sPrefix + sJavaFileName + sPostfix;
      listFileIn.add(new FileIn(file, sPrefix, sPostfix));
    }

    /**Sets the pre- and postfix for the next detected files. */
    public void setPrePostfix(String sPrefix, String sPostfix)
    { this.sPrefix = sPrefix == null ? "" : sPrefix; 
      this.sPostfix = sPostfix == null ? "" : sPostfix;
    }
    
  }
  
  /**The gotten arguments from the command line are hold in this extra static class. 
   * It is created before the instance of the main class will be created. 
   * Firstly, the arguments are separated in an easily comprehensible class.
   * Secondly, it is possible to give the arguments with another way than calling as command line.
   * Thirdly, the instance of Java2C_Main can be created with known arguments. 
   * Thereby, the constructor can access the parameters and some fields can be initialized,
   * so they can be final. 
   */
  public static class CmdlineArgs
  {
    /**Path to file given with command line argument <code>-if:INPFILE</code>.*/
    private String sConfigFile;
    
    /**Path to the syntax-zbnf files, given with cmd line argument <code>-syntax:SYNTAXDIR</code>. */
    private String sSyntaxPath;

    /**Path to the *.c, *.h and *.stc output files, given with cmd line argument <code>-o:OUTPUT</code>. */
    private String sPathOut;

    /**Sets the file given with cmd line argument <code>-if:CFGFILE</code>.
     * The syntax of the config file is described in ZBNF with:
     * <pre>
           "input::={ " 
         + "   stcPath : <stcPath?> ;" 
         + " | srcPath : <srcPath?> ; " 
         + " | replace : <packageReplacement> ; " 
         + " | translate : <*\\s;?srcToTranslate> ;" 
         + "} \\e. "
         + "packageReplacement::=<* ;=?inputpath> =: <* ;*(?cfile_prefix> [*<* ;?cfile_suffix>] "
         + " [ ( <* ;*)?cname_prefix>[*<* ;)?cname_suffix>])]."
         + "stcPath::=<?>{ <\"\"?stcPath>| <*\\s:,;?stcPath> ? : | , }."  //write path with spaces or : or ; in quotion.
         + "srcPath::=<?>{ <\"\"?srcPath>| <*\\s:,;?srcPath> ? : | ,  }."  
     * </pre>
     * The elements are:
     * <table>
     * <tr><td><code>stcPath</code></td>
     * <td>Path where stc-files are located. A path may be absolute or relative from the current directory.
     *   A stc-File may located in the addressed directory, and than in that sub-dir,
     *   where the package replacement of the appropriate java file is located.
     *   If a stc-file is searched, it may be contained in any of this paths. The first location
     *   in the order of paths in the configFile are used, independent of the content of other concurrently existent stc-files.<br>
     *   <br>
     *   This entry can be written in the config file more as one time. 
     *   Any entry can contain more as one path like represented in the syntax. 
     * </td></tr>  
     * <tr><td><code>srcPath</code></td>
     * <td>Path where Java source-files are located. A path may be absolute or relative from the current directory.
     *   A Java-File may located in the addressed directory, and than in the package sub-dir,
     *   If a Java-file is searched, it may be contained in any of this paths.The first location
     *   in the order of paths in the configFile are used, independent of the content of other concurrently existent java-files.<br>
     *   <br>
     *   This entry can be written in the config file more as one time. 
     *   Any entry can contain more as one path like represented in the syntax. 
     * </td></tr>  
     * <tr><td><code>packageReplacement</code></td>
     * <td>Rule how a package structure is mapped to a C-, Header- and stc-file-structure and the names in C. 
     * </td></tr>  
     * <tr><td><code>packageReplacement .inputpath</code></td>
     * <td>java package/file-path written adequate like <code>import</code>-statement. 
     *   Either with <code>.*</code> on end or <code>.javaFile</code> on end, than without extension <code>.java</code>.
     *   In opposite to the <code>import</code>-statement it is possible too 
     *   to use <code>/</code> or <code>\</code> as separator between directories of the path and the <code>/javaFile</code>.
     * </td></tr>  
     * <tr><td><code>packageReplacement .cfile_prefix</code></td>
     * <td>directory and/or prefix for file names at C-side, written with <code>/</code> as separators. 
     *   at example <code>c_dir/pre</code> if the file should stored in the output sub-dir <code>c_dir</code>
     *   and all filenames should start with <code>pre</code>.
     * </td></tr>  
     * <tr><td><code>packageReplacement .cfile_suffix</code></td>
     * <td>suffix for file names at C-side. If the option isn't use, it means no <code>*</code> is written,
     *   than the <code>packageReplacement.cfile_prefix</code> is the whole file name. Normally the C-filename
     *   is built with prefix, the Java-file-name and the suffix.   
     * </td></tr>  
     * <tr><td><code>packageReplacement .cname_prefix</code></td>
     * <td>prefix for class identifier at C-side. If this option isn't use, the file-prefix is used as name-prefix.
     *   
     * </td></tr>  
     * <tr><td><code>packageReplacement .cname_suffix</code></td>
     * <td>suffix for names at C-side. If the option isn't use, it means no <code>*</code> is written,
     *   than the <code>packageReplacement.cname_prefix</code> is the whole name of the class at C-side. 
     *   Normally the class-name is built with prefix, the Java-class-name and the suffix. The class-name
     *   is the name of the <code>struct</code> containing the data and the suffix of all C-routines 
     *   and other identifier associated to the Java-class.
     *   <br><br>
     *   If the whole option <code>(prefix*suffix)</code> isn't use, the file-prefix and suffix are used instead.
     *   That should be the standard usage, because the files and classes should built normally with the same pre-
     *   and suffixes, see examples.
     * </td></tr>  
     * <tr><td><code>srcToTranslate</code></td>
     * <td>The sources to translate have to be named in its package tree, not with the full path.
     *   The here named sources will be translated, if the Java-files are newer against the c-, header and stc-files.
     *   Java-files, which are not named here, won't be translated. If their type-informations are necessary,
     *   the appropriate stc-file is red, independent of the timestamp of the Java file in comparision with the C-file.
     *   This is, because a translated version of the Java-file may be contained in a used C-library, 
     *   the stc-file is matching to the library more closed as to the Java-File.
     * </td>
     * </table>
     * Example: It is the content of the example to test Java2C.
     * <pre>
      stcPath: ../../CRuntimeJavalike, "../../CRuntimeJavalike/J1c" , "../../CRuntimeJavalike/stc"; 
      
      srcPath: ../../srcJava,../../srcJava.zbnf,../positionControl/srcJava;
      
      replace: java.lang.Exception =: Fwc/fw_Exception;
      replace: java.lang.* =: Jc/*Jc;
      replace: java.util.* =: Jc/*Jc;
      replace: org.vishia/bridgeC.*  =: Jc/*Jc; 
      replace: org/vishia/byteData/* =: Jc/*Jc; 
      replace: org/vishia/mainCmd/*  =: Jc/*Jc; 
      replace: org.vishia.util/*     =: J1c/*Jc;
      replace: org/vishia/java2C/test/* =: *_Test;
      replace: org/vishia/java2C/test/ImplIfc =: Java2cTest/ImplTest (Test_IfcImpl);
      
      replace: org/vishia/exampleJava2C/emulationEnv/* =: PositionCtrl/test/*_PosCtrl; 
      replace: org/vishia/exampleJava2C/java4c/* =: PositionCtrl/*_PosCtrl; 
      
      #import: iRequireMainController =: testenv; 
      
      translate: org/vishia/java2C/test/*.java;
      translate: org/vishia/util/StringPart.java

     * </pre> 
     */
    public void setConfigFile(String s){ sConfigFile = s; }
    
    public void setPathOut(String path)
    { path = path.replace("\\", "/");
      if(!path.endsWith("/")){ path+= "/"; }
      sPathOut = path;
    }


    public void setSyntaxPath(String path)
    { sSyntaxPath = path;
    }



    
  	
  }
  
  private final CmdlineArgs args;
  
  
  /**Instance of {@link Java2C_Main.ListFileIn}. */
  private final ListFileIn listFileIn = new ListFileIn();

  
  /**List of all source pathes given either in command line option <code>-srcpath:</code> 
   * or in the input config file given with given with command line argument <code>-if:INPFILE</code>,
   * see {@link #setConfigFile(String)}.
   */
  private List<String> listJavaSrcpath = new LinkedList<String>();
  
  /**List of all java sources which are to translate  given either in command line option<code>-i</code> 
   * or in the input config file given with given with command line argument <code>-if:INPFILE</code>,
   * see {@link #setConfigFile(String)}.
   * <br>
   * It is possible to declare whole packages or singe files to translate. Packages have / as separator and on end.
   */
  private List<String> listInputToTranslate = new LinkedList<String>();
  
  final JavaSources javaSources = new JavaSources();
  
  /**The parser for *.java input files. The parser is initialized with syntax 
   * from file <code>Java2C.zbnf</code> in directory given with cmd line args 
   * <code>-syntax:SYNTAXDIR</code> in the routine {@link #init()} 
   * called in {@link #execute()}. */
  final ZbnfParser parser;
  
  /**Writer for a file containing all enhanced references. */
  BufferedWriter fileRef;

  /**List of known types. */
  final TreeMap<String, ClassData> xxxallClassData = new TreeMap<String, ClassData>();
  //final AllData pkgIdents;

  /**List of all files processed in the first pass to consider in the second pass. */
  private final List<GenerateFile> allJavaFilesToRunSecondPass = new LinkedList<GenerateFile>();

  /**Access to the input configuration set with cmdLine option -if:INPUTCFG. @Uml=aggregate. */ 
  ConfigSrcPathPkg_ifc inputCfg; 
  
  //private String sAllReferenceFilePath;

  /**Assignment between an import statement and a Headerfile. */
  private Map<String, String> importHeaders = new TreeMap<String, String>();
  
  /**Contains all types 
   * which are searched but not found in the Java2C translation. If a type is necessary but not known,
   * a unknown type is created. It is possible that the type isn't known in the given pool of Java files.
   * Typically it may be a Type which may existent at the C-side because it is manually written.
   * Another problem may be, it is an unregistered standard type.
   * <br><br>
   * This types are automatic created and are accessible in all scopes. The reportfile contains
   * the list of all here registered types.  
   */
  public final static LocalIdents externalTypes = new LocalIdents("extern/");
  
  /**Instance with all {@link ClassData} and {@link LocalIdents} for the classes 
   * appropriated in the CRuntimeJavalike C runtime environment. All this {@link ClassData}
   * are stored in {@link #stdTypes}, which is the base of all {@link LocalIdents}, 
   * therefore this types are accessible in all scopes.
   * <br>
   * The content of this class is used directly if {@link ClassData} of this standard types are need, 
   * the access is done via the {@link Java2C_Main#singleton}.standardClassData.  
   */
  public CRuntimeJavalikeClassData standardClassData;
  
  /**A CCodeInfo instance useable as dummy for local variable. There havn't a reference. */
  public final CCodeData localReferenceDummy = new CCodeData("", null, '%', 0);
  
  /**A CCodeInfo instance useable as dummy for static variable. There havn't a reference. */
  public final CCodeData staticReferenceDummy = new CCodeData("", null, '%', 0);
  
  
  //fault, because recurcively call
  //fault: private final ReadStructure readStructure;
  
  /**Recursion respectively deepness of recursive call of {@link #runFirstPassFile(JavaSrcTreeFile, int)}.
   * 
   */
  private int recursion;
  
  private String spaces = "                                                  ";
  
  public boolean addInputFilemask(String sMask, String sCPrefix, String sCPostfix)
  { boolean bOk = true;
    listFileIn.setPrePostfix(sCPrefix, sCPostfix);
    try{ bOk = FileSystem.addFileToList(sMask, listFileIn); }
    catch(FileNotFoundException exc)
    { bOk = false;
    }
    return bOk;
  }

  
  
  /**The instance of this class is filled with the result of parsing an input file 
   * given with <code>-if:INPFILE</code> cmd line calling argument. 
   * The instance is created and used
   * inside the routine {@link Java2C_Main#readConfigFile(String sFileName)}. 
   * That routine parses the result and calls 
   * {@link org.vishia.zbnf.ZbnfJavaOutput#setOutputStrict(Object result, ZbnfParseResultItem, Report)}.
   * The result is this instance.
   * <br>
   * All inner elements follows the syntax.zbnf, see explaination on {@link Java2C#main(String[])}
   * 
   */ 
  public static final class InputFileParseResult implements ConfigSrcPathPkg_ifc
  { 
    /**Subclass to pour in the result of the <code>set::=...</code> subsyntax.
     * Any instance is created if a <code>&lt;set></code> is parsed with syntax 
     * <pre>
     * set::=<* ;=?inputpath>[ =: [[ <* ;*?Cname_prefix>]*] <* ;?Cname_postfix>].
     * </pre>.
     */
    public static final class Set implements ConfigSrcPathPkg_ifc.Set
    { /**ZBNF-parser-result: Ingests semantic for inputpath. &lt;* ;=?inputpath>. */
      private String inputpath; 
      
      /**ZBNF-parser-result: Ingests semantic for Cname_prefix. &lt;* ;*?Cname_prefix>. */
      public String cfile_prefix;
      
      /**ZBNF-parser-result: Ingests semantic Cname_postfix. &lt;* ;?Cname_postfix>. */
      public String cfile_suffix = null; 
      
      /**ZBNF-parser-result: Ingests semantic for Cname_prefix. &lt;* ;*?Cname_prefix>. */
      public String cname_prefix = null;
      
      /**ZBNF-parser-result: Ingests semantic Cname_postfix. &lt;* ;?Cname_postfix>. */
      public String cname_suffix = null;
      
      public String stcFile = null;
      
      public void set_inputpath(String value)
      { { inputpath = value; }
      }
      
      public String toString()
      { return inputpath + " =: " + cfile_prefix + "*" + cfile_suffix
               + (cname_prefix == null ? "" 
                 : "(" + cname_prefix
                   + (cname_suffix == null ? "" : "*" + cname_suffix)
                   + ")"
                 ); 
      }
    
      /* (non-Javadoc)
       * @see org.vishia.java2C.ConfigSrcPathPkg_ifc.Set#getFilePrefix()
       */
      public String getFilePrefix(){ return cfile_prefix == null ? "" : cfile_prefix; }
      
      /* (non-Javadoc)
       * @see org.vishia.java2C.ConfigSrcPathPkg_ifc.Set#getFileSuffix()
       */
      public String getFileSuffix() { return cfile_suffix; } //may be null.
      
      /* (non-Javadoc)
       * @see org.vishia.java2C.ConfigSrcPathPkg_ifc.Set#getNamePrefix()
       */
      public String getNamePrefix()
      { if(cname_prefix != null) return cname_prefix;
        else if(cfile_prefix == null) return "";
        else{
          int posSeparator = cfile_prefix.lastIndexOf('/');
          if(posSeparator <0){ return cfile_prefix; }
          else return cfile_prefix.substring(posSeparator+1);
        }
      }
      
      /* (non-Javadoc)
       * @see org.vishia.java2C.ConfigSrcPathPkg_ifc.Set#getNameSuffix()
       */
      public String getNameSuffix() 
      { return cname_prefix == null    //TRICKY: its correct, test prefix, than a expression (pre[*post]) is given. 
        ? getFileSuffix() : cname_suffix; //cname_postfix is null if it isn't parsed, return null is correct
      }
    
      @Override public String getInputPath() { return inputpath; }
      @Override public String getStcFile() { return stcFile; }

      
    }

    
    /**Set from semantic. */
    //public String fileAllReferences = "allReferences.h";
    
    /**ZBNF-parser-result: Creates an appropriate instance  for semantic set. <code>&lt;set></code> */
    public Set new_set(){ return new Set(); }
    /**ZBNF-parser-result: Adds the instance after it is filled with the content of a <code>&lt;set></code>. */ 
    public void add_srcToTranslate(String value)
    { value = value.replace('\\', '/');
      value = value.replace('.', '/');
      int zValue = value.length();
      if(value.endsWith("*/java")){ //input is *.java
        value = value.substring(0, zValue-6); //an package, ends with /
      }
      else if(value.endsWith("/java")){
        value = value.substring(0, zValue-5) + ".java"; //correct the extension
      }
      else{ //posExt <0, no extension .java
        if(!value.endsWith("/")){
          value = value + "/";
        }
      }
      srcToTranslate.add(value); 
    }
    
    /**ZBNF-parser-result: adds an element <code>&lt;...?stcPsth></code>. */ 
    public void add_stcPath(String value){ listStcPath.add(value); }
    
    /**ZBNF-parser-result: adds an element <code>&lt;...?stcPsth></code>. */ 
    public void add_srcPath(String value){ listSrcPath.add(value); }
    
    /**List to hold all <code>&lt;set></code>. */
    private final List<String> srcToTranslate = new LinkedList<String>();

    /**List to hold all stcPath. <code>&lt;...?stcPath></code>. */
    private final List<String> listStcPath = new LinkedList<String>();
    
    /**List to hold all stcPath. <code>&lt;...?stcPath></code>. */
    private final List<String> listSrcPath = new LinkedList<String>();
  
    public List<String> getStcPathes(){ return listStcPath; }

    public List<String> getSrcPathes(){ return listSrcPath; }
  
    /**List to hold all <code>&lt;set></code>. */
    private final Map<String, ConfigSrcPathPkg_ifc.Set> packageReplacement = new TreeMap<String, ConfigSrcPathPkg_ifc.Set>();
    
    /**ZBNF-parser-result: new component <code>&lt;...?packageReplacement></code>. */ 
    public Set new_packageReplacement(){ return new Set(); }
    
    
    /**ZBNF-parser-result: adds a component<code>&lt;...?packageReplacement></code>. */ 
    public void add_packageReplacement(Set value)
    { final String key;
      final String sValue1 = value.inputpath.replace('.', '/'); 
      if(     sValue1.endsWith("/*")){ key = sValue1.substring(0, sValue1.length()-2); }
      else if(sValue1.endsWith("/")) { key = sValue1.substring(0, sValue1.length()-1); }
      else                           { key = sValue1; }
      packageReplacement.put(key, value);
  }
    
    
    public ConfigSrcPathPkg_ifc.Set getCPathPrePostfixForPackage(String javaPackagePath)
    { if(javaPackagePath.endsWith("/")){
        javaPackagePath = javaPackagePath.substring(0, javaPackagePath.length()-1);
      }
      
      return packageReplacement.get(javaPackagePath);
    }
    
    public java.util.Set<Map.Entry<String, ConfigSrcPathPkg_ifc.Set>> getListPackageReplacements(){
    	return packageReplacement.entrySet();
    }
    
    
    
    public List<String> getSrcToTranslate()
    {
      return srcToTranslate;
    }
    
    
    public void reportConfig(Report report, int reportLevel)
    {
      report.reportln(reportLevel, "===Config-File===");
      report.reportln(reportLevel, "====Package-Replacement====");
      for(ConfigSrcPathPkg_ifc.Set entry: packageReplacement.values()){
        report.reportln(reportLevel, entry.toString());
      }
      //TODO rest
    }
  }
      
  
  /**The singleton instance of this class. */
  public static Java2C_Main singleton;
  
  /**The create routine for the singleton. */
  public static void instanciateSingleton(CmdlineArgs args, Report report)
  { try{ singleton = new Java2C_Main(args, report); }
    catch (Exception exc)
    {
      System.out.println("Exception");
    }
  }
  
  
  /**Private Constructor for singleton.  
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   * @throws ParseException */
  private Java2C_Main(CmdlineArgs args, Report report) 
  throws FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException, ParseException
  { this.console = report;
    this.args = args;
    singleton = this;
    //stdTypes = new LocalIdents();
    //userTypes = new LocalIdents(stdTypes);
  
    parser = new ZbnfParser(console, 10);
    parser.setReportIdents(Report.error, Report.info, Report.fineDebug, Report.fineDebug);

    //readStructure = new ReadStructure(this, console, args.sSyntaxPath);
    
    //pkgIdents = new AllData();
  }


  /**Parses the given File and build the input file information for translating.
   * This method is called in the phase of evaluating the calling parameters. 
   * The content of the file will be parsed with ZBNF parsing with the fix syntax:
   * <pre>
   * input::={ translate: <set> ;}. 
   * set::=<* ;=?inputpath>[ =: [*<* ;?Cname_prefix>|<**?Cname_postfix>*]].</pre>
   * A example of file content:
   * <pre>
   * srcJava/org/vishia/exampleJava2C/java4c/*.java = *_PosCtrl; //the files for position control
   * ..//TODO
   * </pre>
   * @param sFileName
   * @return true if successful, false if any error.
   * @throws ParseException 
   */
  private static ConfigSrcPathPkg_ifc readConfigFile(ListFileIn listFileIn, String sFileName, Report console) 
  throws ParseException
  { boolean bOk = true;
    ConfigSrcPathPkg_ifc inputCfg = null;
    File fInput = new File(sFileName);
    StringPart spInput = null;
    try{ spInput = new StringPartFromFileLines(fInput,10000, null, null); }
    catch(Exception exc)
    { console.writeError("fault input file", exc);
      bOk = false;
    }
    if(bOk)
    { ZbnfParser parserInput = new ZbnfParser(console, 10);
      parserInput.setSkippingEndlineComment("#", false);
      String syntaxInput = 
         "input::={ " 
       + "   stcPath : <stcPath?> ;" 
       + " | srcPath : <srcPath?> ; " 
       + " | replace : <packageReplacement> ; " 
       + " | translate : <*\\s;?srcToTranslate> ;" 
       + "} \\e. "
       + "packageReplacement::=<* ;=?inputpath> =: <* ;*(,?cfile_prefix> [*<* ;(,?cfile_suffix>] "
       + " [ ( <* ;*)?cname_prefix>[*<* ;)?cname_suffix>])][ , stc = <* ;?stcFile>]."
       + "stcPath::=<?>{ <\"\"?stcPath>| <*\\s:,;?stcPath> ? : | , }."  //write path with spaces or : or ; in quotion.
       + "srcPath::=<?>{ <\"\"?srcPath>| <*\\s:,;?srcPath> ? : | ,  }."  
       ;
      try{ parserInput.setSyntax(syntaxInput);} 
      catch(ParseException exc){ throw new RuntimeException(exc); }
      
      //parse the config file:
      bOk = parserInput.parse(spInput, null);
      if(bOk)
      { InputFileParseResult parseResult = new InputFileParseResult();
        parserInput.reportStore(console, Report.debug, sFileName);
        try{ ZbnfJavaOutput.setOutputStrict(parseResult, parserInput.getFirstParseResult(), console);}
        catch(Exception exc){ throw new RuntimeException(exc); }
        //sAllReferenceFilePath = parseResult.fileAllReferences;
        /*
        for(InputFileParseResult.Set set: parseResult.srcToTranslate)
        { 
          listFileIn.setPrePostfix(set.cname_prefix, set.cname_postfix);
          try{ bOk = FileSystem.addFileToList(set.inputpath, listFileIn); }
          catch(FileNotFoundException exc)
          { bOk = false;
            console.writeError("not found: " + set.inputpath);
          }
        }
        */
        inputCfg = parseResult;
      }
      else throw new ParseException(parserInput.getSyntaxErrorReport(),0);
    }
    return inputCfg;
  }
  
  
  /**Executes the translation for all files. 
   * @throws ParseException 
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   */
  public void execute() throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  { boolean bOk = true;
    initZbnfParser();
    if(bOk && args.sConfigFile != null)
    { inputCfg = readConfigFile(listFileIn, args.sConfigFile, console);
      /**Adds all source pathes from inputCfg. */
      for(String srcPath: inputCfg.getSrcPathes()){
        listJavaSrcpath.add(srcPath); 
      }
      for(String srcToTranslate: inputCfg.getSrcToTranslate()){
        listInputToTranslate.add(srcToTranslate); 
      }
      for(Map.Entry<String, ConfigSrcPathPkg_ifc.Set> setPkgReplacement: inputCfg.getListPackageReplacements()){
      	String sPkg = setPkgReplacement.getKey();
      	ConfigSrcPathPkg_ifc.Set info = setPkgReplacement.getValue();
      	createJavaPkgFileTreeFromCfg(info.getInputPath(), info);  //create packages for stc-files only.
      }
    }
    inputCfg.reportConfig(console, Report.info);
    //NOTE: inputCfg is necessary yet, because pkg-C-FilePath assignment.
    standardClassData = new CRuntimeJavalikeClassData(this);
    JavaSrcTreeGetter getterAllSourcefiles = new JavaSrcTreeGetter(javaSources, inputCfg, listInputToTranslate, console);
    //getterAllSourcefiles.gatherAllJavaSrcFiles(standardClassData.stdTypes, listJavaSrcpath);
    getterAllSourcefiles.gatherAllJavaSrcFiles(javaSources.javaSrcTree.getPkgLevelIdents(), listJavaSrcpath);
    
    if(bOk)
    { for(JavaSrcTreeFile javaSrc: javaSources.listJavaSrcFilesToTranslate)
      { if(javaSrc.isTranslated())
        { console.writeInfoln("* input file, first pass already processed, was depended: " + javaSrc.getPublicClassName()); 
        }
        else
        { //console.writeInfoln( file.file.getAbsolutePath() + "* processing..."); 
          recursion = 0;
          GenerateFile genFile = runFirstPassFile(javaSrc);
        	if(genFile != null){
            genFile.runSecondPassFile();
        		//allJavaFilesToRunSecondPass.add(genFile);
        	}
        }
      }
      for(GenerateFile genFile : allJavaFilesToRunSecondPass)
      { genFile.runSecondPassFile(); //userTypes); //pkgIdents);
      }
      //writeFileRef();
      console.writeInfoln("...done.");
    }
  }


  /**Creates the package tree for all package replacements found in the configuration file.
   * It is assumed firstly, that a java-file isn't existing.
   * Therefore the stc-file should be used.
   * <br><br>
   * If a java-file is found later in the java-source-path, it is replaced.
   * @param sPkgPath The path given in config-file replace:-statement.
   *        The pathes can be separated with / or dot.
   */
  private void createJavaPkgFileTreeFromCfg(String sPkgPath, ConfigSrcPathPkg_ifc.Set info){
  	String sIdent = sPkgPath.replace('/','.');  //NOTE: if '/' isn't contain, the method is optimized.
    if(sPkgPath.equals("java.lang.*"))
    	stop();
  	JavaSrcTreePkg pkg = javaSources.javaSrcTree;
  	String sPkgPath1 = "";
    int posDot;
    while( (posDot = sIdent.indexOf('.')) >=0){
      String sEnvIdent = sIdent.substring(0, posDot);
      sIdent = sIdent.substring(posDot+1);
      sPkgPath1 += sEnvIdent + "/";
      
      if(sIdent.charAt(0) =='*'){
        //add the replacing info to the whole package
      	pkg = pkg.getOrAddPkg(sPkgPath1, sEnvIdent, info);
      } else {
      	//normal: no replacing info for pre-packages.
      	//Note: The package may be existing.
        pkg = pkg.getOrAddPkg(sPkgPath1, sEnvIdent, null);
      }
    }
    //assume that the java-file doesn't exist, use stc:
    if(sIdent.charAt(0) !='*'){
    	//add a Java-file only if it is named in the replace-string.
	    String sJavaFile = sIdent;  //Name without .java is used to select stc-file. May be '*' here.
	    String stcPath = info.getStcFile();
	    JavaSrcTreeFile javaFile = new JavaSrcTreeFile(pkg, null, null, sJavaFile, info 
	    		, stcPath, false);	
	    pkg.getPkgLevelIdents().putClassType(sIdent, javaFile);  //will be replaced if a java-source is found.
    }
  }
  
  //String getFileAllReferences(){ return sAllReferenceFilePath; }
  
  //String getIdentAllReferences()
  //{  String sIdent1 = sAllReferenceFilePath.replaceAll("\\W", "_");
  //   return sIdent1;
  //}
  
  String getPathOut(){ return args.sPathOut; }
  
  
  /**Initializes before {@link #execute()}.
   * <ul>
   * <li>Reads the {@link #sConfigFile} if an cmd line argument <code>-if:INPFILE</code> is given.
   * <li>Initializes the {@link #parser} with the syntax file <code>Java2C.zbnf</code>
   *     using the path from cmd line argument <code>-syntax:PATH</code>.
   * <li>Initializes the {@link #parserStruct} with the syntax file <code>Java2Cstc.zbnf</code>
   *     using the path from cmd line argument <code>-syntax:PATH</code>.
   * </ul>       
   * @return true if successful.
   * @throws ParseException
   * @throws FileNotFoundException
   * @throws IOException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   */
  boolean initZbnfParser()
  throws ParseException, FileNotFoundException, IOException, IllegalArgumentException, IllegalAccessException, InstantiationException
  { boolean bOk = true;
    if(bOk)
    { File fileSyntaxJava = null;
      fileSyntaxJava= new File(args.sSyntaxPath + "/Java2C.zbnf"); //"../../srcJava/org/vishia/Java2C/Java2C.zbnf");
      String sSyntaxJava;
      sSyntaxJava = FileSystem.readFile(fileSyntaxJava);
      if(sSyntaxJava == null)
      { console.writeError("syntaxfile not found, error arg -syntax:" + args.sSyntaxPath + "/Java2C.zbnf");
        bOk = false;
      }
      else
      { parser.setSyntax(sSyntaxJava);
        //parser.reportSyntax(console);
      }
    }  
    return bOk;
  }



  /**parses and translates one java file. It is called 
   * <ul>
   * <li> either from {@link execute()} for any given input file,
   * <li> or from {@link runRequestedFirstPass(String sClassName)} for any depended file.
   * </ul>
   * The following actions are done:
   * <ul>
   * <li>Tests whether the destination file exists and the source isn't newer. In this case 
   *     {@link readStructToClassData(String)} is called, and nothing else. This produces the 
   *     {@link ClassData for using the informations of this file.}
   * <li>If the destination file doesn't exists or the source is newer:
   * <li>Reads the file content in a {@link org.vishia.util.StringPartFromFileLines}.
   * <li>Parses the file with the {@link #parser}.
   * <li>Creates an instance of {@link GenerateFile} and adds it to {@link allJavaFilesToRunSecondPass}.
   *     This list will be processed for second path of translation.
   * <li>Calls {@link GenerateFile#runFirstPass(ZbnfParseResultItem, LocalIdents, String)}
   *     to build the {@link ClassData} of all contained classes.
   * </ul>            
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   * */
  private GenerateFile runFirstPassFile(JavaSrcTreeFile javaSrc)
  throws FileNotFoundException, IOException, ParseException, IllegalArgumentException, IllegalAccessException, InstantiationException
  { recursion +=1;
  	GenerateFile genFile = null;
  	assert(!javaSrc.isTranslated());
    final boolean shouldTranslate;
    final File javaFile = javaSrc.getFileJava();
    String sFileNameC = javaSrc.getFileNameC();
    final String sPublicClassName = javaSrc.getPublicClassName();
    final LocalIdents pkgIdents = javaSrc.getPkgLevelTypes();
    if(sPublicClassName.equals("OS_TimeStamp"))
      stop();
    File fileStc;
    if(javaSrc.isToTranslate()){
      fileStc = new File(args.sPathOut + sFileNameC + ".stc");  //stc-File at regular output path.
      long dateJavaFile = javaFile.lastModified();
      File fileC = new File(args.sPathOut + sFileNameC + ".c");
      File fileH = new File(args.sPathOut + sFileNameC + ".h");
      //TODO search it in a search path.
      if(fileC.exists() && fileH.exists() && fileStc.exists())
      { long dateCfile = fileC.lastModified();
        long dateHfile = fileH.lastModified();
        long dateSfile = fileStc.lastModified();
        shouldTranslate = dateJavaFile > dateCfile || dateJavaFile > dateHfile || dateJavaFile > dateSfile;
      }
      else{ shouldTranslate = true; } //because file not exists
      if(shouldTranslate){
      	fileC.renameTo(new File(fileC.getAbsolutePath()+".bak"));
      	fileH.renameTo(new File(fileH.getAbsolutePath()+".bak"));
      	fileStc.renameTo(new File(fileStc.getAbsolutePath()+".bak"));
        console.writeInfoln( (recursion==1 ? "* input file" : spaces.subSequence(0, 2* recursion) + "* depending type") +", first pass, parse: " + sPublicClassName + " ... ");
        StringPart spInput = null;
        //if(pkgIdents.get(sFileName) == null)
        if(sPublicClassName.equals("PID_controller"))
          stop();
        int lenBuffer = (int)javaFile.length();
        assert(lenBuffer < 1000000);  //max. 1 MByte source.java
        spInput = new StringPartFromFileLines(javaFile,lenBuffer, null, null);
        //parse the input.java-file
        boolean bOk = parser.parse(spInput);
        if(!bOk)
        { String sError = parser.getFoundedInputOnError();
          console.writeError(sError);
          sError = parser.getExpectedSyntaxOnError();
          console.writeError(sError);
          sError = parser.getSyntaxErrorReport();
          console.writeError(sError);
        }
        else
        { parser.reportStore(console, Report.debug, javaFile.getPath());
          console.writeInfo(" OK parsing ...");
          ZbnfParseResultItem resultItem = parser.getFirstParseResult();
          if(resultItem.getSemantic().equals("JavaSrc"))
          { //the new instance is returned and stored then in {@link allJavaFilesToRunSecondPass}.
          	genFile = new GenerateFile(javaSources, javaSrc, this, fileStc, console);
            //parsing and first pass:
            console.writeInfo("OK.");
            //LocalIdents pkgTypes = javaSrc.getPkgLevelTypes();
            genFile.runFirstPassFile(
            		resultItem, javaSrc.getClassCNamePrefix(), javaSrc.getClassCNameSuffix()
                , args.sPathOut);
          
          }
        }
      }
      else
      { //not to translate
        console.writeInfoln( (recursion==1 ? "* input file" : spaces.subSequence(0, 2* recursion) + "* depending type") +", is actual, read stc, parse: " + sPublicClassName + " ... ");
        //Note: This routine may be called recursively, use an own instance of ReadStructure!
        ReadStructure readStructure = new ReadStructure(this, console, args.sSyntaxPath);
        readStructure.readStructToClassData(javaSrc, sPublicClassName, fileStc); //ClassData will be created and assigned to pkgIdents  
        console.writeInfo("OK.");
      }
    }
    else{
      /**The java file is not an input. The stc-File should be used to input the structure informations for ClassData.
       * But the stc-file may be at another position in structure path as the regular output. Search it!
       */
      console.writeInfoln( (recursion==1 ? "* input file:" : spaces.subSequence(0, 2* recursion) + "* depending type:") + sPublicClassName + ", not to translate, ");
      String sStcPath = javaSrc.getStcPath();
      if(sStcPath.equals("J1c/StringFormatterJc.stc"))
      	stop();
      fileStc = searchFileStc(sStcPath);
      if(fileStc == null)
      	throw new IllegalArgumentException("stc-File not found:" + sStcPath + " for: " 
            + javaSrc.getFileJava()+ ", fileC: " + javaSrc.getFileNameC());
      //Note: This routine may be called recursively, use an own instance of ReadStructure!
      ReadStructure readStructure = new ReadStructure(this, console, args.sSyntaxPath);
      readStructure.readStructToClassData(javaSrc, sPublicClassName, fileStc); //ClassData will be created and assigned to pkgIdents  
      console.writeInfo("OK.");
    }
    recursion -=1;
    return genFile;  //may be null
  }


  
  
  /**Creates a external type because no informations are available.
   * @param name
   * @param pkg
   * @return the created type. It is put also in the global userTypes:
   */
  public final static ClassData createExternalType(String name, String pkg)
  {
    ClassData classData = new ClassData("external-auto-created", null, null, pkg, name, name, name, 'L', null, null, null, null, '*', "", 'x');
    //Java2C_Main.singleton.userTypes.putClassType(classData);
    Java2C_Main.singleton.standardClassData.stdTypes.putClassType(classData);
    return classData;
  }
  
  
  /**Creates or searches a package which is a root package.
   * Inside the returned package, the method {@link JavaSrcTreePkg#getOrAddPkg(String, String)}
   * can be invoked to get or add a sub package or the method
   * {@link JavaSrcTreePkg#setFileJava(String, File, String, String, String, String, String, String, boolean)
   * can be invoked.
   * @param sName The name of the root package
   * @return The root package management instance.
   */
  public final static JavaSrcTreePkg getOrAddRootPkg(String sName){
  	return Java2C_Main.singleton.javaSources.javaSrcTree.getOrAddPkg(sName +"/", sName, null);
  }
  
  /**Searches a package which is a root package.
   * @param sName The name of the root package
   * @return The root package management instance.
   */
  public final static JavaSrcTreePkg getRootPkg(String sName){
  	return Java2C_Main.singleton.javaSources.javaSrcTree.getChild(sName);
  }
  
  /**Gets the FileLevelidents of the root level. It are such as the packages "org" or "java".
   * @return The fileLevelIdents for the root level.
   */
  public final static LocalIdents getRootLevelIdents(){
  	return Java2C_Main.singleton.javaSources.javaSrcTree.getPkgLevelIdents();
  }
  

  /**It's a debug helper. The method is empty, but it is a mark to set a breakpoint. */
  void stop()
  { //debug
  }


  /**Runs the first pass. Searches the file and build the ClassData parsing and converting the file.
   * This method is called if an unknown type is used yet.
   * @param sClassName The name of the type in Java. The filename should be the same.
   *        The file is searched in any package given as input parameter calling java2C.
   * @return null if no file found. Otherwise the built ClassData. 
   *         This ClassData are registered in the list {@link Java2C_Main#userTypes}.
   * @throws ParseException 
   * @see org.vishia.java2C.RunRequiredFirstPass_ifc#runRequestedFirstPass(java.lang.String)
   */
  public ClassData runRequestedFirstPass(final JavaSrcTreeFile javaSrc, final String sPkgClassName) //, String sPkgName) 
  throws FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException, ParseException
  {
    if(javaSrc != null){ 
    	/**Translate the java-file or read the stc-file. */
      GenerateFile genFile = runFirstPassFile(javaSrc);
    	if(genFile != null){
        //NOTE: The secondPass must not run immediately, because the first pass
    		// of the file, which needs this firts pass, isn't ready yet. 
    		//If this file needs something from the first file in its second pass,
    		//it is wrong.
    		//doNot: genFile.runSecondPassFile(console);
    		//instead: save the file to run the second pass after finishing the first pass of the first file.
    		allJavaFilesToRunSecondPass.add(genFile);
    	}
      if(sPkgClassName.equals("InspcDataExchange") || sPkgClassName.equals("LogMessage"))
    		stop();
     	/**The ClassData of all classes in this file should be posed in the package of the javaSrc. */
      LocalIdents pkgIdents = javaSrc.getPkgLevelTypes();
    	final JavaSources.ClassDataOrJavaSrcFile infos = pkgIdents.getTypeInfo(sPkgClassName, null);  //fileLevelIdents = null, because it are the fileLevelIdents.
      assert(infos !=null);
      ClassData retClassData = infos.getClassData();
      return retClassData;
      //return pkgIdents.getType(sPkgClassName, null);  //fileLevelIdents = null, because it are the fileLevelIdents.
    }
    else{
      throw new IllegalArgumentException("no javaSrc available for. " + sPkgClassName);
      //return xxxrunRequestedFirstPass(null,sPkgClassName, sPkgName);
    }
  }
  
  
  /**Searches a class in any *.stc-File. This method is called if a yet translated ClassData
   * isn't found and a source to runfirstpass isn't found too. 
   * @param sClassName
   * @return
   */
  private File searchFileStc(String sFileNameStc)
  {
    if(sFileNameStc.equals("Jc/StringBufferJc.stc"))
      stop();
    File fileStc = null;

    Iterator<String> stcPathes = inputCfg.getStcPathes().iterator();
    while(fileStc == null && stcPathes.hasNext())
    { String stcPath = stcPathes.next();
      String sFile = stcPath + "/" + sFileNameStc;
      fileStc = new File(sFile);
      if(!fileStc.exists())
      { fileStc = null;
      }
    }
    if(fileStc != null)
    { stop();
      
    }
    return fileStc;
  }
  
  
 
}