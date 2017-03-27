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

import org.vishia.java2C.SecondPass;


/*Test

 */


/**This class contains no code, only documentation. Every spanned subject of the translator 
 * is described associated to a method. The description contains some links to the java code.
 * So javadoc generates a well readable documentation. Use the private-view documentation
 * to trace to all links, because most of methods and fields are package private or private.  
 * 
 * @author JcHartmut
 *
 */
public class Docu
{
  
  /**This chapter provides an overview, where and how Java2C should be used. 
   */
  public class A_InvokationAndEnvironmentConditions
  {
    /**The writing of sources in Java instead in C or C++ has the advantage, that all features
     * of new programming systems, where Java is a signifying member, can be used for the software development.
     * The programming language C should be recognized as a language for machine-implementing closeness.
     * C isn't the best appropriate language for modern software engineering. At example
     * using UML with C as base language would be a non-effective solution.
     * <br><br>
     * The Java2C-Translator allows firstly writing a few sources in Java beside other sources in C, 
     * than more and more using Java as source language by recommendation of using UML and Object Oriented technologies.
     * The deciding matter is the usage of a modern software technology. Java2C is only a helper
     * to link this software technology with the classic software development processes.   
     */
    void A1_applying_goals(){}
    
    /**The Java sources, which can translated to C, are not typical Java applications 
     * in a Java-possible environment. For such requirements a Java-virtual machine should be used. 
     * If some C- or C++-written parts should be integrated thereby, either
     * the JNI (Java Native Interface) should be used, or some interfaces at example socket connection
     * can be used to connect parts, which runs under Java, with parts, which are written in C(++).
     * <br><br>
     * The Java-sources can be conceptualize regarding requirements of embedded programming, 
     * which are typical thinking in a C-matter. But it are formulated in the world of Java.
     * At example that is:
     * <ul>
     * <li>Static data definition instead elaborately using of new Objects: 
     *   Typical Java applications use the new-operator to define
     *   data when it are necessary. The programming is more simple for dynamic tasks, because 
     *   the data needn't be planned in detail. Last not least the garbage collection concept helps
     *   to prevent unnecessary data garbage and to supply the necessary memory space.
     *   <br><br> 
     *   But in embedded applications such dynamic memory allocations
     *   are undesirable. Therefore a static assessment of all necessary data can be done in Java too, 
     *   all data should defined in an initial phase. In a UML-thinking they are <i>composition</i>-data than.
     *   Java supports the building of compositions with the <code>final</code> keyword on references
     *   used together with a new statement:
     *   <pre>
     *   final Type data = new Type(initialArguments);
     *   </pre>
     *   Such data are disposed in C as embedded data. It is possible to define the data in only one
     *   memory block, which can instantiated as a static area in the ambient C main-definitions.
     * <li>The usage of interfaces is another example. Interfaces are the first way to break dependencies
     *   between software modules. The modules can be programmed and tested independently. The exchange
     *   of data and control is defined using Java-interfaces. The interfaces can be implemented
     *   in some test environment classes to test a module, though they are implemented 
     *   in the conclusive classes for using. The interface concept is known in UML as a basic concept too.
     *   <br><br>
     *   The usage of interfaces needs a concept of <i>virtual methods</i> (C++-slang). It is either
     *   an additional effort to use such, or it is a problem of safety. In C simple prototype declarations
     *   of routines are used instead, the implementation of the routines is separated from them,
     *   the implementation can be written and test as an independent module too while providing
     *   test routines for the linking of a module test for the environment.
     *   <br><br>
     *   Java2C supports using interfaces at Java-level (source-writing, UML) and translating it to
     *   simple method calls for the C-implementation level, if the implementation in the target system
     *   is defined fix and no polymorphism is need.  
     * <li>There are some other details too for embedded-like Java-programming, see some examples.
     * </ul>
     * 
     *       
     */
    void A2_Java_sources_for_embeddedSoftware(){}
    
    
    /**While translation of one Java files all types, which are used there, should be known, 
     * a simple plain generation from one Java file to one C-file isn't possible. 
     * There are some informations about used types to respect.
     * Therefore either all Java sources which are depending together are translated in one session,
     * or used classes have to be presented with so named stc-files (STruCture of classes). 
     * The stc-files were produced from an previous other session of translating of the appropriate
     * sources, or they were translated from header files of manual written C-parts (TODO) respectively hand written.
     * The order of generation depends on the usage of the types. If a type-information is need, 
     * the appropriate source is start to translate nested in the current translation. 
     * To prevent cyclically dependencies, the translation process is separated in two passes, first and second.
     * The first pass detects all types, the header file thereby, the second pass creates the C-file.
     * <br><br>
     * The java sources should be provided in the Java-well-known package structure maybe located at more as one
     * path on file the system (the Java source path for Java2C). All found files are gathered, 
     * a tree of known java files is built to declare package and file identifiers.  
     * There is an assignment between Java package and file-in-package positions 
     * and directories and pre- and suffixes for file names and class (<code>struct</code>-) names at C-side 
     * to map the Java-file and class-structure to a C-file and namespace structure. The assignment is given
     * as input for the translater in the config-file.
     * <br><br>
     * In the config-file is defined, which sources should be translated, and which sources mustn't translate
     * because there are existent in C either in libraries provided from an extra session 
     * or there are existent manually written in C. The Java2C-translator searches the appropriate stc-file
     * for existent C-sources, therefore a stc-search-path is used. The stc-files for several libraries and
     * other C-modules can be dispersed on the file system, adequate to Java-sources. 
     * <br><br>
     * It is possible that some types are not found as stc-file, because there contain 
     * simple methods or data in a simple class. In this case their existence is presumed, and the types
     * of identifiers are accepted as {@link CRuntimeJavalikeClassData#clazz_unknown}. 
     * Than no additional type conversions are done. The expression with such identifier is submitted in C like found in Java.
     * 
     */
    void A2_Java_sources_to_translate(){}
    
    /**The invocation of Java2C is command-line-oriented. Some settings are given in a config-file. Some options
     * may be given per command-line argument. In this form the translator is able to embed at example
     * in a ANT-environment of another maker.
     * <br><br>
     * <ul>
     * <li>The command line options are explained appropriated to the {@link Java2C#main(String[])}.
     * <li>The content of config-file is explained appropriated to 
     * {@link Java2C_Main#setConfigFile(String)}. 
     * see also {@link Docu.B_ProcessOfTranslation#B3_packageReplacement()}.
     * <ul>
     * In the Java2C-supplying bundle hosted at {@linkplain http://sourceforge.net/projects/java2c/} contains examples of usage.
     * 
     */
    void A4_invoke_Java2C(){}
  }
  
  
  
  /**In this chapter the tranlation process of all files is described. It is an overview-chapter.
   */
  public class B_ProcessOfTranslation
  {
    
    /**This chapter describes the translation process in Java2C.
     * Basic coherences are explained in {@link #B3_packageReplacement()} and {@link #b6_translatingOrGetStc()}.
     * <br><br>
     * The translation process starts calling {@link Java2C_Main#execute()}. The command line arguments 
     * are parsed and stored before. 
     * <ul><li>
     * First the parsers for Java source code and stc-files are initialized 
     * calling {@link Java2C_Main#initZbnfParser()}. Their syntax is defined in the external files 
     * <code>Java2C.zbnf</code> and <code>Java2Cstc.zbnf</code> located in the syntax path given by
     * command line argument <code>-syntax:SYNTAXDIR</code>, see {@link Java2C_Main#sSyntaxPath}.
     * <li>
     * Second {@link Java2C_Main#readConfigFile(java.util.Map, org.vishia.java2C.Java2C_Main.ListFileIn, String, org.vishia.mainCmd.Report)}
     * is called to get the content of the config file. The config file describes 
     * the package and file replacement from Java 2 C especially.
     * The result is retrievable via {@link Java2C_Main#inputCfg}. 
     * <li>
     * Third the standard types are created. It is done while creating the instance {@link CRuntimeJavalikeClassData}.
     * The standard types contains the language scalar types, some special types which are present in C especially
     * ({@link CRuntimeJavalikeClassData#clazz_s0} for zero terminate string literals, {@link CRuntimeJavalikeClassData#clazz_va_argRaw}
     * for variable arguments etc). Some classes of <code>java.lang</code> 
     * or <code>java.util</code> are defined their too. Therefore the standard packages are created 
     * as instances of {@link JavaSrcTreePkg}.
     * The instance {@link JavaSrcTreePkg#pkgIdents} of the instance for package <code>java.lang</code>
     * is used as {@link CRuntimeJavalikeClassData#stdTypes}. The language scalar types are stored there too.
     * The types in <code>java.lang</code> are available without any package or import declaration, 
     * adequate to the behaviour of the Java compiling process.
     * <br><br>
     * The existence of all respectively some files from the 
     * standard Java classes are declared also in {@link CRuntimeJavalikeClassData}. 
     * Its content is defined either in the associated stc-Files or it is defined hard-coded. 
     * The stc-Files are placed in the directories of the <code>CRuntimeJavalike</code>-Implementation 
     * of the basic system in C associated to the manually programmed C-sources of them.
     * <li>
     * Fourth all available Java source files in the given source path are gathered. 
     * The java source path is given by command line argument
     * <code>-srcpath:</code> or in the config file. All files at all locations at file system
     * are gathered. The Java files itself are not red, only their existence is tested. 
     * The packages (directories in file system) are red out. In future extension jar files and class files
     * should be regarded too (not implemented in version 0.9). The class files are non-translate-able, 
     * but their existence for using are gathered. With this extension only that packages should be analyzed, 
     * which are really used, not all.
     * The gathering process should be dispersed, first only the first needed packages etc. It is necessary
     * because voluminous directory trees and jar archives shouldn't produce unnecessary informations. 
     * <br><br>
     * The result of this gathering processes is a tree of instances of {@link JavaSrcTreePkg} and 
     * {@link JavaSrcTreeFile}, which are available when calling {@link LocalIdents#getTypeInfo(String, LocalIdents)}.
     * The first level packages are assigned to the {@link CRuntimeJavalikeClassData#stdTypes}.
     * Therefore all used types of the Java sources are able to locate starting with the first package.
     * <br><br> 
     * The return type of {@link LocalIdents#getTypeInfo(String, LocalIdents)} is a instance of {@link JavaSrcTreeFile}
     * or {@link JavaSrcTreePkg}. The files may be translated yet or not. If the {@link ClassData}
     * are need for another translation process, the translating process of the found file is started. 
     * Especially it is done calling 
     * {@link LocalIdents#getType(String, LocalIdents)} to get ClassData.
     *     
     * <li>
     * Fifth all files of {@link JavaSources#listJavaSrcFilesToTranslate} are used to run the first pass 
     * calling {@link Java2C_Main#runFirstPassFile(JavaSrcTreeFile, boolean)}. 
     * The List JavaSourceFilesToTranslate were filled while gathering the Java Source tree.
     * <br><br>
     * The first pass checks, whether the translation is necessary testing the time stamp. It is done
     * in the called routine {@link Java2C_Main#runFirstPassFile(JavaSrcTreeFile, boolean)}.
     * Thereby instances of {@link GenerateFile} are created and stored in a List 
     * {@link Java2C_Main#allJavaFilesToRunSecondPass}.
     * 
     * <li>
     * Sixth the second pass is run for all classes which are processed in the first pass 
     * calling {@link GenerateFile#runSecondPassFile(Report)}. The files for second pass
     * are stored in the {@link Java2C_Main#allJavaFilesToRunSecondPass}. 
     * </ul>
     * The sequence diagram shows this operations: 
     * <img src="../../../../Java2C/img/TranslationPrc_sqdJava2C.png" />
     * <br><br>
     * The translation process is conditional and has its own order:
     * <ul>
     * <li>
     * To translate? It is tested whether the a requested file is to translate or not, 
     * evaluating {@link JavaSrcTreeFile#isToTranslate()}. If it isn't to translate,
     * the stc-file is parsed calling {@link Java2C_Main#readStructToClassData(JavaSrcTreeFile, String, File)}
     * to get necessary {@link ClassData}.
     * It is for library-represented C-compilation units of for hand-written C-Files.
     * <li> 
     * should translate? If the file is to translate, the timestamp of java source and the translating results:
     * C-source, header, stc-file are tested. If the java source is older, the stc-file is read too 
     * to get the {@link ClassData}. The result files are existing yet. 
     * The reasons to do so are twice:
     * <ul><li>First calculation time of translating is economized, because the parsing and translating 
     *   of a complex Java source file needs some more calculation time (may be 1..2 seconds) 
     *   in comparison to parsing the stc-file (milliseconds).
     * <li>Second a new produces C-source should be tested more carefully in comparison to a source,
     *   which is not changed. The C-sources may be stored in a source-version-system, unnecessary
     *   produced sources are disturbing.
     * </ul>    
     * <li>
     * Translation process: The files will be parsed with the ZBNF-parser,
     * than an instance of {@link GenerateFile} is created and added to the list {@link Java2C_Main#allJavaFilesToRunSecondPass}.
     * With that instance the {@link GenerateFile#runFirstPassFile(JavaSrcTreeFile, boolean)} is execute.
     * It produces and sets {@link ClassData} to the {@link JavaSrcTreeFile#classData}. 
     * The ClassData contains all informations to run the second pass and to use the class in other 
     * first or second passed.
     * <li>
     * Recursive call of first pass: If a type is need in any first or second pass, 
     * either its first pass is running recursively yet calling
     * {@link LocalIdents#getType(String, LocalIdents)} and inside
     * {@link runFirstPassFile(JavaSrcTreeFile, boolean)} with the found instance, or the stc-file is parsed instead 
     * to get the {@link ClassData}. The dependencies of sources determine the order of first passes.
     * </ul>
     */
    public void B1_makeAndDependentTypeProcessing(){}


    
    /**The diagramm shows the classes respectively instances which are created in translation process.
     * <br>
     * <img src="../../../../Java2C/img/overview_omdJava2C.png" />
     * <br>
     * The diagram shows the main instance {@link Java2C} top left. It contains the command line process
     * and creates the singleton instance {@link Java2C_Main#singleton}. All blue colored instances are created
     * before the translation process starts. The role of {@link CRuntimeJavalikeClassData} 
     * is explained in {@link #B1_makeAndDependentTypeProcessing()} in step Three.
     * <br><br>
     * Below the JavaSources, referenced with {@link JavaSources#javaSrcTree}, there are the top-level packages.
     * The package <code>java</code> is also found there, which is the root of <code>java/lang</code>, <code>java/io</code> etc.
     * The package <code>org</code> as root of <code>org/vishia</code> or adequate are found there too. 
     * A user root-package is arranged there also. All packages below the {@link JavaSrcTreePkg#subPkgs} are sub-packages from the root
     * respectively from any other sub-package. The {@link JavaSrcTreePkg#pkgIdents} 
     * then contain the {@link JavaSrcTreeFile}-instances of the package in its reference {@link LocalIdents#typeIdents}.
     * The file may be translated or not. If its {@link ClassData} should be necessary, it will be translated.
     * <br><br>
     * The blue colored instances below {@link JavaSources} are a representation of the Java source tree 
     * gathered in {@link JavaSrcTreeGetter#gatherAllJavaSrcFiles(LocalIdents, java.util.List)}.
     * Only two packages and one file are shown, but the instances which are created an assigned in this tree
     * are much more: Any Java file is representing with an instance of {@link JavaSrcTreeFile},
     * any package is representing with an instance of {@link JavaSrcTreePkg}. 
     * <br><br>
     * The red bordered shadowy presented instance of {@link LocalIdents} is the instance of the
     * package <code>java.lang</code> in the package tree. This instance is referenced 
     * from {@link CRuntimeJavalikeClassData#stdTypes} too and contains also the standard scalar types
     * and special types. This instance is used to search types per name in any case at least.
     * <br><br>
     * The dark yellow colored instances are created while running the first pass 
     * and they are used to run the second pass.
     * But the gray colored instances are temporary created while running the translation process of one file.
     * <br><br>
     * The cyan colored instances are created while running the first pass or read the stc-file,
     * and they are stored in the {@link LocalIdents#typeIdents}-Index. They are able to search in the package path
     * while using in any translation. The {@link ClassData} contains the relevant informations 
     * about a class type to use from outside.
     * <br><br>
     * The dark green bordered instances are the instances of {@link LocalIdents}, which are created 
     * associated to packages, files, classes. They are necessary to search types which were used.
     * <br><br>
     * The <code>fileLevelIdents</code> are built for translating a file and are stored between first and second pass.
     * It references all identifiers which are visible at file level regarding the import statements
     * and all types of the own package.
     */
    public void B2_umlDiagram_overview_Java2C(){}
    
    /**The package replacement describes, how java files, organized in packages, are mapped to C files, 
     * maybe organized in sub directories and in abbreviated named files.
     * <br><br>
     * The situation in Java is: All files are placed in a well organized package structure. 
     * Any java-file takes place in a directory, which is the package directory 
     * as part of the package directory tree. 
     * The package, which contains the file, is written in the package declaration in the Java source code.
     * The javac-Compiler check that. Any java-file contains only one public class with the same name as the file.
     * The class and file names are package-local valid. 
     * It is possible, that more as one file respectively class with the same name is existing, 
     * the distinction between files and classes with equal names are its arrangement in the package tree.
     * If any public class is need in compilation process, the file with the equivalent name is searched
     * and translated to get the class. The import statement in Java code declares the visible class names
     * known without their package qualifier. Classes can accessed any time with the full qualified name 
     * including the package tree, for example <code>java.util.List</code> instead <code>List</code>,
     * than an import statement isn't necessary, but it's possible. The package tree is defined
     * in a world wide unambiguousness, because it is a practiced rule, 
     * that the package tree of a source pool follows the internet address (URL) of the owners,
     * at example <b>org.vishia</b> for sources, which are hosted associated with the internet address
     * <code>www.vishia.org</code>. That is the world of java.
     * <br><br>
     * In C an adequate file and name-space structure isn't conventional. 
     * A namespace problematic is resolved manually often, at example with fine control of includes. 
     * A typically problem is: An additional include necessary by software enhancement may cause name conflicts.
     * The file and package structure in C is hand-made, without established rules.
     * <br><br>
     * The solution: A mapping between the Java-package-ruled identifications of classes and files 
     * and its representation in C is necessary. That mapping is defined in the config file, 
     * the config file is a input parameter of the translator: <code>-if:file</code>, 
     * see {@link Java2C_Main#readConfigFile(java.util.Map, org.vishia.java2C.Java2C_Main.ListFileIn, String, org.vishia.mainCmd.Report)},
     * {@link Java2C_Main#inputCfg}, {@link Java2C_Main#setConfigFile(String)}.
     * <br><br>
     * The config file contains lines such as
     * <pre>
     *   replace: org/vishia/mainCmd/* =: Jc/*Jc; 
     *   replace: org.vishia.util.MsgDispatcher =: MsgDisp/*_MSG;
     *   replace: org.vishia.util.LogMessageFile =: MsgDisp/*_MSG;
     *   replace: org.vishia.util.* =: J1c/*Jc;
     *   replace: org/vishia/java2C/test.* =: Java2cTest/*_TestAll;
     * </pre>  
     * At left side a package path for java files is named, the right side names the equivalent 
     * for the C files and class names. A directory given at the right side (C) is regarded 
     * to store the C- and header-files, and get or store the stc-files. The path is a relative path
     * starting at the given output path (cmd line argument <code>-o:outpath</code>) for the generated files.
     * For stc-Files the base is the output path for files, which are to translate, but for stc-files,
     * which are not to translate, the here named path starts at any location of the stc-search path.
     * <br><br>
     * The parts of file-name before and after the asterisk <code>*</code> are pre- and suffixes both for the file name 
     * and for the name of the <code>struct</code> which reperesents the java-class. The <code>*</code>-part 
     * is replaced with the java file respectively class name. If no <code>*</code> is given at right side 
     * in the file name part, the file name is given complete. Than the class replacement may be written
     * in parenthesis after them with or without asterisk. 
     * Therewith a file with an abbreviating name can named. 
     * The <code>struct</code> in the file can be abbreviating too, if only one class is contained 
     * in the Java-file, or it can be built regarding the Java class name.
     * <br><br>
     * This settings allow a relative free assignment between Java files and class names 
     * and their representation in C. The user is responsible for the order of files and classes at C-side.
     * It may be recommended to find out an adequate transparent rule to mapping the C files.
     * <br><br>
     * The named pathes of package and file replacement in this division are relative pathes 
     * starting from the java source and class search-path (Java-side) respectively (C-side) 
     * starting from the include and stc search path or starting from the output path. 
     * The here named Java-package pathes should be equivalent the package structure. It means, 
     * it should start with the first package (<code>org</code> etc.). The separator between package identifier
     * may be the slash or backslash or the dot. Internally the slash is used. In opposite in Java sources
     * the dot have to be used as separator in package and import statements. The synonymous using 
     * of slash or dot allows the user to copy a path without need of correction of coseparators from several sources.
     * <br><br>
     * See also {@link #b6_translatingOrGetStc()}.
     * <br><br>
     * <b>Technical informations</b>:
     * <br><br>
     * <ul>
     * <li>The config-file is read and parsed in the method {@link Java2C_Main#readConfigFile(Java2C_Main.ListFileIn, String, Report)}.
     * <li>The configuration is written in {@link Java2C_Main.InputFileParseResult} using {@link org.vishia.zbnf.ZbnfJavaOutput}.
     * <li>The package replacement is contained in {@link Java2C_Main#inputCfg} and there in
     *     the implementation instance {@link Java2C_Main.InputFileParseResult#packageReplacement}.
     * <li>A replacement for a given package can be searched using the public known {@link Java2C_Main#inputCfg}
     *     and there calling {@link ConfigSrcPathPkg_ifc#getCPathPrePostfixForPackage(String)}.
     * <li>The implementation of that interface method is {@link Java2C_Main.InputFileParseResult#getCPathPrePostfixForPackage(String)}.
     *     It accesses the private known TreeMap {@link Java2C_Main.InputFileParseResult#packageReplacement}.
     * <li>This method is called only inside {@link JavaSrcTreeGetter#gatherAllJavaSrcFiles(LocalIdents, java.util.List)}.
     *     The replacement for the found Java-file is stored in the {@link JavaSrcTreeFile#sFilePathC} and adequate elements.    
     * <li>Whether or not a Java-file is to translate to get the type informations, or whether a stc-file is to be read,
     *     depends on the existing of a Java-file. It is tested calling TODO            
     * </ul>     
     */
    public void B3_packageReplacement(){}
    
    
    /**After the package replacement is recognized, all given source-pathes in the config-file are gathered
     * to detect all java-source-files. Not all of them are necessary to translate, but all of them are known then.
     * If any type is necessary, it will be looked after the java-file, which represents this type. If a java-file is found,
     * then it's tested, whether the equivalent C-file with the {@link #B3_packageReplacement()} is existing 
     * and whether it is newer as the Java-file. If it is newer, it must not be translated once again. 
     * It is strongly, because the C-file may be checked in an source configuration management, it may be tested and so on.
     * Then the stc-file should be exist, see {@link #b6_translatingOrGetStc()}. But if the Java-file are newer than the c-file,
     * then it is translated. Therefore it is necessary to know all java-sources.
     *  
     * <br><br>
     * <b>Technical informations</b>:
     * <ul>
     * <li>The config-file is read and parsed in the method {@link Java2C_Main#readConfigFile(Java2C_Main.ListFileIn, String, Report)}.
     * <li>The configuration is written in {@link Java2C_Main.InputFileParseResult}.
     * <li>The post-processing of the parse-result is done in {@link Java2C_Main#execute()}. There is filled or set:
     * <li>{@link Java2C_Main#listJavaSrcpath}: All pathes in file system (maybe relative) where java-src-files are searched.
     * <li>{@link Java2C_Main#listInputToTranslate}: All Input files to translate primary. More files will be translated of dependencies exists.
     * <li>The class {@link JavaSrcTreeGetter} is created temporary,  
     * <li>{@link JavaSrcTreeGetter#gatherAllJavaSrcFiles(LocalIdents, java.util.List)} is called. It checks all given pathes
     *     in the file system, containing in the second argument {@link Java2C_Main#listJavaSrcpath}.
     * <li>For all detect directories in the source-paths, instances of {@link JavaSrcTreePkg} are created and arranged 
     *     as children of the top-level-container for the root-packages are created.
     *     The root-packages are referenced in {@link Java2C_Main#javaSources} and there in
     *     {@link JavaSources#javaSrcTree}.       
     * <li>For all detect java-source-files in the source-paths, instances of {@link JavaSrcTreeFile}
     *     are created and arranged in the {@link JavaSrcTreePkg#pkgIdents}.{@link LocalIdents#typeIdents}-reference. 
     * <li>At least a report is written, which contains the situation of sources. 
     *     This is included in the generated report-file in the chapter <code>===All found Java source files===</code>,
     *     done in {@link JavaSrcTreeGetter#gatherAllJavaSrcFiles(LocalIdents, java.util.List)}
     * </ul>     
     * 
     */
    public void B4_gatherAllSourceFiles(){}
    
    /**If any type is need for the translation, its {@link ClassData} should be present. 
     * They can be gotten calling {@link LocalIdents#getType(String, LocalIdents)} 
     * with the String-given type-name. The type-name can be consists of a full qualified 
     * package path, or of a partial path. 
     * <br><br>
     * If a partial path or only the type-name is given, either a import-statement is determined the package path,
     * or the type is found in the current package, or it is a standard-type:
     * <ul>
     * <li>All classes of the package (in all files) are copied from the {@link JavaSrcTreePkg#pkgIdents} 
     *     in the File-local valid {@link GenerateFile#fileLevelIdents}.         
     * <li>The {@link GenerateFile#fileLevelIdents} is then filled with the types from all import-statements.
     *     Either an import-statement represents one class, or it is written with <code>.*</code>
     *     and all classes of the named package are added there.
     *     This information is put calling {@link GenerateFile#evaluateImports(org.vishia.zbnf.ZbnfParseResultItem)},
     *     which is called at begin of the first pass of translation.
     * <li>All All type-informations of classes, which are contained in the own file beside the public class,
     *     are contained in {@link JavaSrcTreePkg#pkgIdents}. 
     *     There are filled calling {@link GenerateFile#registerClassInFile(ClassData)} in the first pass,
     *     before any detail evaluation of a class of this file.
     * </ul>
     * <br><br>
     * To get the {@link ClassData} for any string-given type, 
     * the method {@link LocalIdents#getType(String, LocalIdents)} have to be called.
     * The instance of {@link LocalIdents} should be the locally used one. 
     * It may be the {@link ClassData#classLevelIdents}, if elements of a class are translated, 
     * or a locally instance created in the parenthesis of code blocks.
     * In that case the locally created instances are recognized and searched first. 
     * Note, that it is possible to define a class in a code block. 
     * <br><br>
     * The second parameter of <code>getType(name, fileLevelIdents)</code> should be the
     * {@link GenerateClass#fileLevelIdents} or {@link GenerateFile#fileLevelIdents},
     * which refers the {@link LocalIdents} at the file level. It includes the import-statement-given one,
     * and the indents of the package (see above). A separate parameter is need, because the fileLevelIdents
     * are that idents from the translated file, where the instance to call 
     * {@link LocalIdents#getType(String, LocalIdents)} can be a referenced instance with path. 
     * Then the fileLevelIdents of the environment of that {@link LocalIdents} are not valid to search.
     * Therefore they are not contained in any {@link LocalIdents} as member, instead there are either given
     * from the {@link GenerateFile#fileLevelIdents} or they have to be <code>null</code>, if a referenced
     * package is used:  
     * <ul>
     * <li><code>Type</code> - than the {@link GenerateFile#fileLevelIdents} should be used.
     * <li><code>pkg.Type</code> - than the fileLevelIdents-parameter should be <code>null</code>
     * to search <code>type</code>
     * </ul> 
     * <br><br>
     * If {@link LocalIdents#getType(String, LocalIdents)} is called, it is not determined, 
     * whether the requested type is existing and whether it is translated. Therefore,
     * internally {@link LocalIdents#getTypeInfo(String, LocalIdents)} is called first. 
     * This routine can be called from outside too. It doesn't return the ready-to-use {@link ClassData},
     * but the {@link JavaSources.ClassDataOrJavaSrcFile}, which are either the ClassData already,
     * or the instance for a translated or not-translated Java-file or package. 
     * The interface is implemented at all three variants. 
     * If the type isn't known, this routine returns <code>null</code>.
     * <br><br>
     * The {@link ClassData} are gotten then calling {@link JavaSources.ClassDataOrJavaSrcFile#getClassData()}.
     * If the implementing instance are of type {@link ClassData}, it returns this. 
     * A {@link JavaSrcTreePkg#getClassData()} returns <code>null</code> any time, 
     * because a package isn't a class.
     * But the {@link JavaSrcTreeFile#getClassData()} returns the {@link ClassData} of its public class,
     * if the file is translated already, otherwise <code>null</code>.
     * <br><br>
     * If the gotten ClassData are <code>null</code>, the translation of the Java-Source 
     * or the parsing of the stc-file is started inside {@link LocalIdents#getType(String, LocalIdents)}.
     * Therefore {@link Java2C_Main#runRequestedFirstPass(JavaSrcTreeFile, String)} is called 
     * with the given {@link JavaSrcTreeFile} - instance. 
     * There, it is detected whether a translation is necessary
     * or the parsing of the stc-file is proper to get the {@link ClassData} of the type.
     * See {@link #b6_translatingOrGetStc()}.
     * <br><br>  
     * The standard-types from <code>java.lang</code> and the simple types <code>int</code>, <code>float</code> etc.
     * are found because {@link LocalIdents#getTypeInfo(String, LocalIdents)} 
     * searches in the {@link Java2C_Main#standardClassData}.{@link CRuntimeJavalikeClassData#stdTypes} too.
     * <br><br>  
     * Any type, which isn't a standard-type created and contained in {@link CRuntimeJavalikeClassData#singleton},
     * is represented by a instance of {@link JavaSrcTreeFile}, independent whether it should be never translated. 
     * But it isn't necessary, that a Java-file is represented too. Some files are existing as Java-files, 
     * but there are not taken into account for translation, because they are present in C as library-content.
     * Then they have to be presented by a stc-file (structure of Java-file). The stc-file maps the content 
     * of the appearance in C (in the header-file) too.  
     * <br><br>
     * See also {@link #B3_packageReplacement()}. If a unknown Java-file is used, 
     * but its package is named in the package-replacement it the form <code>pkg.*</code>, then a {@link JavaSrcTreeFile}
     * is created with the known replacement-informations and the information 'not to translate'. 
     * Because of the existence of the {@link JavaSrcTreeFile}, the associated stc-file is searched and parsed.
     * It builds the {@link ClassData} then.
     * 
     * 
     */
    public void b5_getTypeInfoAndTranslate(){}
    
    /**Some Java sources should be translated, but some other sources mustn't translate, 
     * because they are parts of built C libraries. There may be the absence of parallelism 
     * in translation at Java side and at C side. At Java side the translation may be prevented by supply
     * class files instead java sources. But in practice all files may be given as Java source. 
     * The translation process detects the necessity of translation, it is lightweight alterable
     * <br><br>
     * But in C it is much stronger. Tested functionality is compiled into libraries 
     * and offered to use in several applications. Therefore some Java sources may be translated to C
     * and provided in a C library. They shouln't be re-translated, also if they may be adjusted in Java.
     * If their modification is necessary to use to test in C, first the libraries should be built newly.
     * than the usage is translated and linked.
     * <br><br>
     * Another issue for non-translating is: Some Java-files emulates parts of functionality, 
     * which are implemented in another way in the target system. The implementing files of target 
     * are written in C immediately, they are fitted to hardware requirements mostly.   
     * <br><br>
     * Therefore not all found Java sources should be translated into C in the current session. 
     * The sources which may be translated are named in the config-file with lines (at ex)
     * <pre>
     *  translate: org / vishia/exampleJava2C/java4c/ *.java;
     *  translate: org / vishia/util/MsgDispatcher.java;
     * </pre>
     * There may be named individually files or whole packages with all files. 
     * <br><br>
     * If a Java file is found as depending type, and this file is not in the list of files to translate,
     * a equivalent <code>.stc</code>-File should be found (structure file). The stc-file is searched 
     * with the same path and name as the translated C- or header-file, but with extension <code>.stc</code>.
     * The physically directory is determined by the stc-search path. It is named in the config-file 
     * with setting (at example):
     * <pre>
     * stcPath: ., ../../CRuntimeJavalike, "../../CRuntimeJavalike/J1c" , "../../CRuntimeJavalike/stc"; 
     * </pre>
     * or with cmd line argument <code>-stcpath:</code> too (planned).
     * The shown relative path is related to the calling working directory, an absolute path is possible too.
     * Thereby a library-provided C file may have its stc files adequate to header files for C compilation
     * in a library-oriented stc-directory. It may be the same as the include directory, where the related
     * headeres are stored.
     * <br><br>
     * While running the Java2C translation process found C- and header files are tested in timestamp
     * against the Java source. If the java source is older as both C and H, and a actual stc-file 
     * is existing too, the translation of the Java file is suppressed in favour of reading the stc-file.
     * The stc-file contains all informations, which are necessary by usage of the class 
     * in another translation context (available fields and methods of the class, their types, 
     * override-able properties of methods etc). 
     * <br><br>
     * The stc-files are built automatically in the translation process, and they are is placed 
     * beside the generated header files. There they are found for a later translation. 
     * The stc-files which are associated to libraries or to manual written C code are searched 
     * in the stc-search-path which can name more as one position in the file system. 
     * 
     */
    public void b6_translatingOrGetStc(){}
    
    /**The import statements are not able to use for include generation, 
     * because files from the same package are not imported in Java. But its include is necessary.
     * The import statements are used to bring the imported classes in visibility.
     * <br><br>
     * An <code>#include "..."</code>statement is generated if an external type is used 
     * and its definition have to be known. An include is not necessary 
     * if a type is used only as a reference-type. For references a forward declaration of the type
     * (<code>struct TYPE_t* name;</code>) declares the type as structure pointer. 
     * The type-definition itself should not be known. It is a able-to-use property of C to do so
     * and it economized the number of included files. That precaution is necessary to prevent
     * circular includes: If a type is needed, but in that structure the current declared type is needed too,
     * a circular include would be produced elsewhere. That would be force a compiler error at C level.
     * <br><br>
     * The {@link ClassData} of the type is searched
     * calling {@link LocalIdents#getType(String, LocalIdents)}.
     * There the {@link ClassData#sFileName} contains the file name contingently with a directory 
     * where the header file is located adequate to the association between Java package path and C path 
     * in the config file.
     * <br><br>
     * The using of the type may be occurred in the first or in the second pass of translation. 
     * The first pass produces the .h-file, the second pass produced the .c-File. So a using of a type leads to include
     * in the .h- or .c-File. The methods {@link GenerateFile#addIncludeC(String sFileName, String comment)} 
     * and {@link GenerateFile#addIncludeH(String sFileName, String comment)} represents both possibilities. 
     * This methods may be called more as one time for the same file. Therefore the TreeMap {@link GenerateFile#includes} 
     * saves the types for including while running the first or second pass for this file, 
     * and the lines for <code>#include "..."</code> in the H- and C-file are produced after finishing the passes.
     * <br><br>
     * The routine {@link GenerateFile#addIncludeH(String sFileName, String comment)} is called inside the first pass 
     * <ul><li>in the routine {@link GenerateFile#write_HeaderContent(GenerateFile, ZbnfParseResultItem, String, ClassData, ClassData[])}
     *   for super classes and interfaces.
     * <li>in the routine {@link GenerateClass#createFieldDataNewObject(ZbnfParseResultItem, ZbnfParseResultItem, ZbnfParseResultItem, LocalIdents, String, ClassData, char, char, ClassData, char, ZbnfParseResultItem)}
     *   if the new Object is created as class variable or static variable.
     * <ul>
     * In this cases an include of the type is necessary. The including of super classes and interfaces 
     * can't force circular includes, 
     * because the current class can't be a superclass of its superclass in Java too.
     * <br><br>
     * The routine {@link GenerateFile#addIncludeC(String,String)} is called sometimes in the second pass,
     * if a type is need. It may be necessary mostly, because a needed type will be accessed mostly,
     * than its structure should be known. Not necessary includes in the C-file are not a problem any time.
     */
    public void b7_includeGeneration(){}
    
  }
  
  
  
  /**While the translation processes the type or affiliation of some identifiers should be evaluated. 
   * In java this is done by the javac-Compiler. In C some more informations should be appropriated
   * to the C-Compiler. Therefore all identifiers are stored in Lists. This lists are TreeMap mostly
   * to support a fast searching.
   * <br><br>
   * <img src="../../../../Java2C/img/ClassDataLocalIdents_omd.png" />
   */
  public class C_StaticDataOfConversion
  {

    /**<b>LocalIdents</b>
     * <br>
     * Identifiers are valid always in a local context. This context may be a class context, 
     * a method context or a context of a statement block (some statements in <code>{...}</code>). 
     * The class {@link LocalIdents} holds references to identifiers and types,
     * with some add- and access methods, for all the contexts:
     * <ul> 
     * <li>{@link StatementBlock#localIdents}: for any statement block. 
     * <li>{@link ClassData#classLevelIdents} for the class level context.
     * <li>{@link JavaSrcTreePkg#pkgIdents} for the idents, which are associated to packages-local 
     *   classes respectively files.
     * <li>{@link GenerateFile#fileLevelIdents}: All visible types while translation a file are stored here.
     *   It are a copy from the {@link JavaSrcTreePkg#pkgIdents} of the current package, the same one
     *   form all imported packages and all imported class types. This LocalIdents are assembled
     *   for translating a file running first and second passes. This LocalIdents aren't be visible or used
     *   while accessing any classes from outside.  
     * <li>{@link Method#methodIdents}: The methodIdents are the LocalIdents visible at start of body of the method.
     *   It contains the parameter of the method especially. The body's LocalIdents is created as an own instance than,
     *   because it is handled like an normal {@link StatementBlock}.
     * <li>{@link CRuntimeJavalikeClassData#stdTypes} This referenced instance contains the global visible types.
     *   It contains 
     *   <ul>
     *   <li>the C-language standard types <code>int</code> etc.,
     *   <li>all packages visible at root of packages,
     *   <li>all types from package <code>java.lang</code>, mostly implemented in CRuntimeJavalike-C-environment.
     *   </ul>
     *   This instance is the {@link JavaSrcTreePkg#pkgIdents} of the JavaSrcTreePkg-instance for <code>java.lang</code>. 
     * </ul>
     * <br>
     * The {@link LocalIdents} contains TreeMaps for 
     * <ul>
     * <li>{@link LocalIdents#fieldIdents} identifier of all variables in this context. 
     *     Especially in statement block some variable may be defined, they aren't known in the class context.
     * <li>{@link LocalIdents#typeIdents} identifier of all types in this context. 
     *     In a statement block local visible classes (defined in the block) are possible too.
     * <li>Method identifications are not contained there, they are hold in Class context, see
     *   {@link ClassData#methods} respectively {@link ClassData#searchMethod(String, java.util.List, boolean)}.       
     * </ul>
     * An instance of {@link LocalIdents} in any scope contains all field and type identifiers 
     * from the super (parent) scope too as a copy of the identifier Strings and references 
     * from the parents TreeMaps. To prevent effort, In {@link StatementBlock} 
     * a new LocalIdents is created only with the first definition of an type or variable. But the types
     * of the standard language level, it is {@link CRuntimeJavalikeClassData#stdTypes},isn't copy
     * to prevent effort. It contains only types and the root packages. Therefore searching of types
     * (see {@link LocalIdents#getTypeInfo(String, LocalIdents)}) should regard three locations:
     * <ul>
     * <li>The LocalIdents of the given local scope, it contains inner classes and classes defined
     *   in a statement block (rarely used in practice),
     * <li>The file level LocalIdents, given as extra parameter because it depends from the file to generate.
     *   This LocalIdents contains the package level LocalIdents of the own package too, and with them
     *   the own Class. Especially imported classes are contained there additionally.
     * <li>The standard language idents given in the static {@link CRuntimeJavalikeClassData#stdTypes}. 
     * </ul>
     *     
     * <br><br> 
     * The searching of a identifier uses always the Java name of identifier. 
     * In the found {@link FieldData} than the C name is contained.
     */
    void B1_LocalIdents(){}
    
    /** <br><br>
     * <b>Identifiers of super and outer levels</b>
     * <br>
     * Super levels are the levels above a statement block; 
     * <ul>
     * <li>the parent statement block, 
     * <li>the whole method,
     * <li>the class
     * <li>the super class and the outer class
     * <li>the super.super class etc, the outer.outer class etc.
     * <li>All global visible identifiers: {@link Java2C_Main#userTypes} and {@link Java2C_Main#stdTypes} 
     * </ul>
     * In generally, there may be two ways to search identifiers of super levels:
     * <ol>
     * <li>An instance of {@link LocalIdents} contains only the identifiers of the current level. 
     *     It contains a reference to the super and outer LocalIdents. Than the searching process is lengthy,
     *     if a identifier isn't found locally, it should be searched in the next super and outer level and so on.
     * <li>An instance of {@link LocalIdents} contains all identifiers visible at this level. 
     *     If a new instance of LocalIdents is created, the identifiers of the parent level should be copied
     *     and designated with its context oriented from the current level. This copy process necessitates 
     *     some calculation time, but only one time per new level. Some more memory spaces is needed, but only temporary
     *     (the garbage collector have to be tidy up). But the searching is simple and fast.       
     * </ol>
     * The solution is: 
     * <ul>
     * <li>fields, variables are contained complete in the current {@link LocalIdents#fieldIdents}.
     *   If a new level of statement block is created, first the reference to the {@link LocalIdents} 
     *   of the super level is set. But if any block-local variable is found 
     *   (calling {@link GenerateClass#gen_variableDefinition(org.vishia.zbnf.ZbnfParseResultItem, org.vishia.zbnf.ZbnfParseResultItem, LocalIdents, java.util.List, char)}),
     *   a new instance of {@link LocalIdents} is created and all fieldIdent of the parent are copied into it.
     *   So any instance of {@link LocalIdents} contains all visible identifier of variables and class fields.
     *   The association is stored in the {@link FieldData}.
     * <li>Types of a inner level starting from Class level ({@link ClassData#classLevelIdents}) 
     *   are contained complete in the current {@link LocalIdents#fieldIdents} too. 
     *   But Language Level types are found in {@link CRuntimeJavalikeClassData#stdTypes}. 
     *   Third, all imported classes and the classes of the own package are found in the {@link GenerateFile#fileLevelIdents}.
     *   Therefore to find out a type three search operations should be do.  
     * <li>Methods are stored only in the accordingly instance of {@link ClassData}. 
     *   If methods from the super classes are searched, the search operation is called for all super-classes.
     *   The adequate problem is for interfaces and outer classes. TODO: It may be opportune to copy the methods 
     *   in one TreeMap too.
     * </ul>
     * <br><br>
     * The knowledge of field-identifiers and types of the outer classes and the sibling- inner classes
     * in inner classes and the knowledge of this one of the super classes in the derived classes
     * is an adequate topic. But additionally the field-idents should be designated as outer or super ones.
     * That designation is contained in the fields {@link FieldData#nClassLevel} 
     * and {@link FieldData#nOuterLevel}. See {@link Docu.E_Inner_classes}.
     * 
     */
    void B2_identifiersForOuterOrSuperLevels(){}
    
   /** <br>
     * <br>
     * <br>
     * <b>searching a method identifier with parameters</b>
     * <br>
     * Methods can't defined in a local context, only at class level. To know and search methods
     * in the actual context, the {@link ClassData} of the appropriate class of the local context 
     * can be used. They are access-able in the methods of {@link SecondPass} via {@link GenerateClass#classData}
     * See {@link #methodCall_WithParameterSensitivity()}
     */
    void B3_searchMethodIdentifier(){}
    
    
   /** <br>
     * <br>
     * <b>{@link ClassData}</b>
     * <br>
     * That are the type informations. The {@link ClassData} are referenced in 
     * {@link LocalIdents.typeIdents}. Classes have their own {@link ClassData#classLevelIdents}.
     * <br>
     * <br>
     * <b>{@link FieldData}</b>
     * <br>
     * That are the variable and field informations. The {@link FieldData} are referenced in 
     * {@link LocalIdents.fieldIdents}. Fields based on {@link FieldData#typeClazz}, but have
     * some additional properties, also especially for the C-Code-representation. 
     * Also arrays with theire special properties are considered.
     * <br>
     * <br>
     * <b>{@link CCodeData}</b>
     * <br>
     * That are peaces of C-Code with its type-information {@link CCodeData#identInfo},
     * but also special properties. If an array element is addressed from a array-field,
     * it may be possible to store the access information in an extra {@link CCodeData#identInfo}.
     * But the solution is: The {@link FieldData} of the whole array is referenced, 
     * and the access properties to the array element are stored in extra data fields:
     * {@link CCodeData#dimensionArrayOrFixSize} and {@link CCodeData#modeAccess} of the element.
     */
    public void B4_InstancesWhichContainsStaticData(){}
    
  }
  
  
  
  
  
  
  
  
  
  
  
  /**Java knows simple inheritance but multiple usage of interfaces. 
   * Interfaces may contain static final variables, they are constants, and method declarations.
   * All method declarations should be implemented in a non-abstract inheriting class. 
   * The super classes may contain also method declarations (using abstract keyword), which have to be 
   * implemented in an inheriting class. 
   * <br><br>
   * Interface may contain complete classes as inner classes too.
   * <br><br>
   * All variables of super classes are available in the data of the inheriting class too, 
   * only a private modifier prevents an access. Interface hasn't any class variable
   * 
   */
  public class D_SuperClassesAndInterfaces
  {
    
    /**A super class in Java is mapped in C using a base <code>struct</code> as first element 
     * of the class representing <code>struct</code>. The following commonly form is used:
     * <pre>
     * typedef struct TheClass_t
     * { union { ObjectJc object; SuperClass_s super; Interface1_s Interface1; Ifc2_s Ifc2; } base;
     *   //rest of class data
     * }
     * </pre>
     * A union is built in C because the <code>object</code>, all interfaces 
     * and the data of the super class represents the same data: 
     * The super class starts with the <code>object</code>, all interfaces contains only the same
     * <code>object</code>. The SuperClass determines the size of the union's data. 
     * <br><br>
     * <ul>
     * <li>The access to the Object base class is possible independent of the inheritance structure. 
     * It is always able writing <code>&ref->base.object</code>. 
     * <li>The access to the super class data
     * should written like <code>&ref->base.super</code>. If a super-super-class should accessed,
     * in Java written using <code>super.super</code>, the C-equivalent form is <code>&ref->base.super.base.super</code>.
     * <li>The reference to interfaces is got
     * writing <code>&ref->base.Interface1</code>, where the identified element is the name of the interface
     * like in the Java code.
     * </ul> 
     * <br><br>
     * The Types of super, Interfaces etc. are the built C-Types. It may have pre- and suffixes 
     * (see {@link Docu.ProcessOfTranslation#packageReplacement()}) and it has the usual suffix <code>_s</code> to different it
     * from a possible C++ type definition.         
     */
    public void D1_baseStructures(){}

    /**The concept of virtual methods is necessary for implementation of interfaces and 
     * for overridden methods in derived classes.
     * <br><br>
     * The class {@link ClassData} contains a field {@link ClassData#inheritanceInfo}. The Type
     * {@link ClassData.InheritanceInfo} contains the reference to the superclass's and interfaces InheritanceInfo. 
     * The referenced instances of the type {@link ClassData.InheritanceInfo} are not instances 
     * of the inherited class, they are independent instances and built a data-private own InheritanceInfo-tree. 
     * The reason for that plurality of InheritanceInfo is: 
     * They contain the override-able (virtual) method names: {@link ClassData.InheritanceInfo#methodTable}. 
     * The virtual methods are differently 
     * for a class as superclass of another class (contains the derived virtual method) 
     * and for the class able to instantiate in another context. The field 
     * {@link ClassData.InheritanceInfo#methodTable} contains the overriding names of all 
     * override-able methods of the current class. The override-able and overridden methods of interfaces
     * and super classes are located in the {@link ClassData.InheritanceInfo#methodTable} 
     * of the super-classes and interface referenced with {@link ClassData.InheritanceInfo#superInheritance}
     * and {@link ClassData.InheritanceInfo#ifcInheritance}.
     * <br><br>
     * The constructor {@link ClassData.InheritanceInfo#InheritanceInfo(ClassData, ClassData, ClassData[])}
     * uses the superclass and all interfaces to build its own tree of Inheritance objects. Thereby
     * the names in {@link ClassData.MethodOverrideable#sNameOverriding} are copied from the original instance,
     * the overridden method is the same: 
     * <br><br>
     * <br>
     * <img src="../../../../Java2C/img/InheritanceInfo_omdJava2C.png" />
     * <br><br>
     */
    public void D2_virtualMethodsAndInheritanceInfo(){}
    
     /**If a method of the current translated class is processed and adds to the class using
     * {@link ClassData#addMethod(String, String, int, FieldData, FieldData[])}, 
     * it is searched in all super classes
     * and interfaces calling {@link ClassData#searchOverrideableMethod(String, FieldData[])}. 
     * If it is found there, it is an overridden method. Therefore a new Instance of {@link Method}
     * is created, but the information about the {@link Method#declaringClass} is taken 
     * from the found method. The name in the {@link ClassData.InheritanceInfo#methodTable} is replaced
     * with the actual method.
     */  
    public void D3_detectOverriddenMethods(){}
    
    
     /** If a new method is created, and it is able to override, it is registered in 
     * {@link ClassData#methodsOverrideableNew} just now. On finishing of translation the class
     * the method {@link ClassData#completeInheritanceWithOwnMethods()} is called. It creates
     * the array in {@link ClassData.InheritanceInfo#methodTable} with the correct array size 
     * and adds the methods. Thereby the override-able methods of the current class, which are not
     * defined in super classes or interfaces, where registered. A method is able to override 
     * if it is non-final.
     */
    public void D4_newOverrideAbleMethods(){}
    
    
    /**The overridden methods have the type of <code>ythis</code> from the first declaring class,
     * because they have to be the same signature (type of method definition) as the first declaring ones.
     * The type of <code>ythis</code> of interface-defined methods is <code>ObjectJc*</code> any time
     * and not, like able to expect, the type of the interface. It is, because a method can be declared
     * in more as one interface. If a class implements more as one interface with the same
     * method declaration, it exists only one implementation. This implementation can't regard
     * a type of one of the interfaces, it should be resolved both. The type <code>ObjectJc*</code>
     * is the commonly of all.
     * <br><br>
     * The implementation of an override-able method is designated with the suffix <code>_F</code>
     * (means Final). This method name is used as entry in the method table of the class.
     * This name is used too if a method of a dedicated type is called 
     * (annotation <code>@ java2c=instanceType:"Type".</code>).
     * <br><br>
     * Additionally a method with the normal built name of non-override-able methods are generated,
     * but that method contains (example):
     * <pre>
     * / * J2C: dynamic call variant of the override-able method: * /
     * int32 processIfcMethod_i_ImplIfc_Test(ObjectJc* ithis, int32 input, ThCxt* _thCxt)
     * { Mtbl_Ifc_Test const* mtbl = (Mtbl_Ifc_Test const*)getMtbl_ObjectJc(ithis, sign_Mtbl_Ifc_Test);
     *   return mtbl->processIfcMethod(ithis, input, _thCxt);
     * }
     * </pre>
     * It is the variant ready to call at C-level which gets the pointer to the method table internally.
     * A simple call from outside C sources can use this variant of method. But it isn't optimal
     * in calculation time, because the call of <code>getMtbl_ObjectJc(...)</code> 
     * needs additional time. See {@link #callingOverrideableMethods()} in its variants.
     * <br><br>
     * The implementation of a method in a derived class starts with a casting from the given data type,
     * at example:
     * <pre>
     * int32 processIfcMethod_i_ImplIfc_Test_F(ObjectJc* ithis, int32 input, ThCxt* _thCxt)
     * { ImplIfc_Test_s* ythis = (ImplIfc_Test_s*)ithis;
     * </pre>
     * The pointer casting should be accept as safe, because this method is only called in an context,
     * where the really instance is of the correct type or a derived type, which contains the correct type
     * as first part of data. The name of method is only used in an programming context of the method table,
     * and in an context where the user declares a reference pointer from base or interface type
     * as a pointer of a given instance using <code>@ java2c=instanceType:"Type".</code> That positions of code
     * should be checked carefully.
     * <br><br>
     * It is possible to check the instance additionally, but this check needs additional calculation time.
     * If it may be needed, at Java-level an <code>instanceof</code> operation can be written 
     * either in the implementing method or, it may be better, at calling position of a method with designation
     * <code>@ java2c=instanceType:"Type".</code> Than the declaration of a determined instance type
     * will be checked at run time too. The <code>assert(ref instanceof Type)</code> produces a C-code like
     * <pre>
     * ASSERT(instanceof_ObjectJc(& ((* (ref)).base.object), &reflection_Type_s));
     * </pre>
     */
    public void D5_cCodeForOverriddenMethods(){}
    

    
    /**If a method is called in Java, which is override-able, the generated C-code depends on several conditions:
     * <br><br>
     * <b>Stack-local reference:</b><br>
     * If the reference is a stack-local reference in Java, it may be generated in C as a so named 
     * method-table-reference. The conditions for that are:
     * <ul>
     * <li>The referenced type is an interface type. Than all methods are dynamic, and the method table preparation
     *   is proper.
     * <li>The definition of this stack-local reference is designated with <code>@ java2c=dynamic-call.</code>,
     *   at example
     *   <pre class="Java">
          /**Use local variable to enforce only one preparation of the method table for dynamic call:
             <code>@ java2c=dynamic-call. </code>* /
          final TestWaitNotify.WaitNotifyData theNotifyingDataMtbl = theNotifyingData;
     *   </pre>
     *   The translated C-code is than
     *   <pre class="CCode">
          TestWaitNotify_Test__WaitNotifyDataMTB theNotifyingDataMtbl;  
          ...
          SETMTBJc(theNotifyingDataMtbl, REFJc(ythis->theNotifyingData), TestWaitNotify_Test__WaitNotifyData);
     *   </pre>   
     * </ul>
     * The method-table-reference is defined locally in the C-file in form (example)
     * <pre>
     *   typedef struct Type_t { struct Mtbl_Type_t const* mtbl; struct Type_t* ref; } TypeMTB;
     * </pre>
     * The named class in C is <code>Type</code>, The method-table-reference contains 
     * the reference (pointer) to the data and additionally the reference to the method table.
     * The reference variable should be set before using of course, The setting is generated (example)
     * <pre>
     *   SETMTBJc(ifc3, & ((ythis->implifc).base.Ifc_Test), Ifc_Test);
     * </pre>
     * It is a macro, defined in <code>ObjectJc.h</code>. The first parameter is the reference to set,
     * the second parameter is the source, in this case the class-locally reference <code>implifc</code>,
     * correct casted to the interface type using the access to base classes. The third parameter is the type.
     * The implementation of this macro is done with (objectJc.h)
     * <pre>
     *   #define SETMTBJc(DST, REF, TYPE) 
     *   { (DST).ref = REF; 
     *     (DST).mtbl = (Mtbl_##TYPE const*)
     *                  getMtbl_ObjectJc(&(DST).ref->base.object, sign_Mtbl_##TYPE); 
     *   } 
     * </pre>
     * The data-reference is set, the reference to the method table is got calling the showed method.
     * Therefore the pointer to the method table is checked, it isn't only a lightweight pointer in data area,
     * it is got with two significance checks, and therefore secure. Because the pointer is stored in the stack range,
     * it should be secure in the current thread, no other thread can disturb it (importend for safety critical software).
     * <br><br>
     * The access to virtual methods is generated in form (example)
     * <pre>
     *   ifc3.mtbl->processIfcMethod(&(( (ifc3.ref))->base.object), 5, _thCxt);
     * </pre>
     * It is an immediate access to the method table reference (in stack, therefore safety) with selecting
     * the correct method (a C-function-pointer in the method table). The first parameter is 
     * the reference to the data in form of the <code>ObjectJc*</code>-Type, the following parameters are normal.
     * <br><br>
     * If the method is called some times in the same context, or the method-table-reference is passed 
     * as parameter to called methods, the method table reference is used immediately, no additional
     * calculation time is need to get the method-tables reference once more. This is the optimized version
     * if dynamic linked calls are necessary, able to use in very hard realtime too.
     * <br><br>
     * 
     * 
     * <b>ythis-reference, own methods:</b>
     * <br>
     * If own methods are called in a subroutine, it can't be assumed that the methods are 
     * methods of the current type, it can be methods from a inheriting instance too. It is because 
     * the instance can be inherited, but the current method is a method implemented in base-class.
     * Therefore a call via method table is generated. If any own method is called, at start of routine
     * the reference to the method table of the instance <code>mthis</code> is built generating (example):
     * <pre>
     * Mtbl_TestAllConcepts_Test const* mtthis = 
     *     (Mtbl_TestAllConcepts_Test const*)
     *     getMtbl_ObjectJc(&ythis->base.object, sign_Mtbl_TestAllConcepts_Test);
     * </pre>
     * This reference is used to call own override-able methods (example):
     * <pre>
     * mtthis->Ifc_Test.processIfcMethod(&
     *           ((& ((* (ythis)).base.super.base.Ifc_Test))->base.object)
     *           , 4, _thCxt);
     * </pre>          
     * In the example a method from a super class is called which is override-able in the current class too.
     * It is an interface-defined method. Therefore the data pointer is the pointer to <code>ObjectJc*</code>,
     * got with access to the <b>&(...)->base.object</b>. The building of interface reference 
     * starting with the reference to the base class in the example <code>& ((* (ythis)).base.super.base.Ifc_Test</code>
     * is a unnecessary but automatic generated complexity, which are resolved from the C-compiler to a simple pointer,
     * because all offsets are zero. It isn't disturb.
     * <br><br>
     * The built of the <code>mthis</code> locally in the subroutine isn't optimal 
     * if it is repeated in called subroutines. It should be optimized (later versions): 
     * If a method calls own override-able methods, it shouldn't get the <code>ythis</code>-pointer of the data,
     * but instead a method-table-enhanced reference. The calling method, which has this enhanced reference already,
     * can use it directly without additional effort. Only a method which calls such a routine firstly, should built
     * the reference to the methodtable (calling <code>getMtbl_ObjectJc(...)</code>. The reference to the method table
     * can recognize as safety, because it is stored only in the stack area, not in thread-unbound data areas.
     * 
     * 
     * <br><br>
     * <b>reference in data area (ythis->ref)</b>
     * A dynamic call with a reference in the data area is the most non-economic, 
     * because the pointer to the method table is built in specially for this call. 
     * It is translated from Java2C in form (example):
     * <pre>
     * ((Mtbl_Ifc_Test const*)getMtbl_ObjectJc
     *   (&(REFJc(ythis->ifc))->base.object, sign_Mtbl_Ifc_Test) )->processIfcMethod
     *   (&((REFJc(ythis->ifc))->base.object), 56, _thCxt);
     * </pre>
     * The getting of the method table is generated inline before call the method (first+second line).
     * The third line contains the normal parameter (in reality its one line).
     * <br><br>
     * Because this operation need the call of <code>getMtbl_ObjectJc(...)</code> only for this intention,
     * it should only used for a simple single dynamic call. If the algorithm should be optimized
     * in calculation time, the class-visible reference is transfered 
     * in a reference in stack (statement-block-local)- variable). Than the call of 
     * <code>getMtbl_ObjectJc(...)</code> is done only one time, maybe before start of an loop,
     * and it is used many times. It is a mission for the Java programming. In pure-Java it is indifferent
     * using a class visible or block visible reference. But if an optimized C-source is need, 
     * use the block-local variant.
     * 
     * <br><br>
     * <b>prevent dynamic call, use static instead</b>
     * See {@link #preventCallingViaMethodTable()}, it is a signifying feature for optimal C code. 
     */
    public void D6_callingOverrideableMethods(){}
    
    
    
    /**In Java the methods are override-able normally, because a <code>final</code> designation
     * to prevent overriding is written only if the ability to override should prevent in inheriting classes.
     * Therefore the most of methods should be called in a override-able mode, using the method table-call.
     * But that is not economically in calculation time, and in some cases it is unnecessary. 
     * It is against the C-style of programming and testing.
     * <br><br>
     * The difference provocation is: In a object oriented architecture interfaces should be used 
     * to divide software in independent parts. Interfaces are the main choice to do so, base classes
     * are the other choice. So specific implementations can be implemented without cross effects.
     * But therefore the overridden methods appear as the only one solution. 
     * <br><br>
     * In opposite to Java the independence of modules are realizes in C using forward declarations of methods
     * in header files and their implementation in separated C-files (compiling units). The linker
     * have to link only with knowledge of the labels (method names) without knowledge of any implementation details.
     * This form of independence can't realize the polymorphism in opposite to interfaces and super classes, 
     * only the aspect of independence is regarded. But this aspect is the prior aspect mostly. 
     * <br><br>
     * The solution of this provocation is found in the following way: If it is known, that a reference
     * references a determined instance in the C-implementation, it can be designated with a
     * <code>@ java2c=instanceType:"Type".</code>-annotation in its comment block. Another way is 
     * using a final assignment <code>final IfcType ref = new Type();</code>, what generates 
     * an embedded instance. Than the Java2C-translator
     * generates a non-overridden calling of the method of the designated instance type 
     * for using that reference. The annotation is the decision written in the source 
     * in knowledge of the implementation goals. In Java it isn't active. So in Java several implementations
     * can be implemented, at example for testing.     
     * <br><br>
     * If the user is deceived in the usage of the reference, it is not detected in Java 
     * neither by compiling nor by testing, because it isn't active there. 
     * But it should be attracted attention in testing at C-(implementation)-level.
     * The Java2C-compiler may test the correctness of the designation <code>@ java2c="instancetype".</code>,
     * because it translates the assignments too. But than all temporary used references should be designated
     * too. That don't may be helpfully.
     * <br><br>
     * But the designated reference can be tested in Java in Runtime, whether at least the designated type
     * is referenced, using a <code>reference instanceof Type</code>-Java-sourcecode. 
     * Than fatal errors are excluded, only if the instance is from a derived type, it isn't detected.
     * The <code>instanceof</code>-check needs a small part of calculation time, 
     * if the instance is from the expected type. Such tests are slowly only if the instance is from a far derived type,
     * than the implementation type should be searched in reflections. In the current case
     * only 2 indirect accesses and a compare operation is necessary in the implementation of
     * <code>instanceof_ObjectJc(ref, reflection_Type).</code>  
     * <br><br>
     * A reference, which has a dedicated instance type, is determined in its {@link FieldData#instanceClazz}- element.
     * This element is able to seen in the stc-File of the translated class with notation <code>instance:<Type></code>
     * as part of the <code>fieldIdents {...}</code>.
     * <br><br>
     * The method-call is translated to C using the {@link Method#sImplementationName}.
     *   
     */
    public void D7_preventCallingViaMethodTable(){}
    
    /**The structure of a method table is generated into the header-file, but the definition
     * of a method table is generated into the C-file. 
     * <br><br>
     * <b>Method type definition:</b></br>
     * A method type definition is a type definition of a method. At example the definition of the 
     * basicly method <code>Object.toString</code> is contained in <code>Object.h</code> in the form
     * <pre>
     * typedef METHOD_C StringJc MT_toString_ObjectJc(ObjectJc* ythis);
     * </pre>
     * The generated form is the same (example):
     * <pre>
     * typedef int32 MT_testOverrideAble_ImplIfc_Test(ImplIfc_Test_s* ythis, float value, ThCxt* _thCxt);
     * </pre>
     * It looks like a simple forward declaration of a method, but it is the typedef of the so named <i>C-function pointer</i>.
     * The typedef of an method of an interface is at example:
     * <pre>
     * typedef int32 MT_processIfcMethod_Ifc_Test(ObjectJc* ithis, int32 input, ThCxt* _thCxt);
     * </pre>
     * The reference to the data for interface defined methods is the <code>ObjectJc*</code>-pointer in any time.
     * The defined interface pointer, in this case <code>struct Ifc_Test_t*</code> isn't use,
     * because if the same method is definded in more as one interface, it is implemented only one time.
     * The data reference should be the same. Therefore the base of all data is used. On calling an interface method
     * the correct type of reference is generated accessing the <code>&ref->base.object</code>-Part of a data structure. 
     * For methods not defined in interfaces the associated type is used.
     * <br><br>
     * <b>Method table definition:</b></br>
     * The form is (example):
     * <pre>
     * extern const char sign_Mtbl_ImplIfc_Test[]; //marker for methodTable check
     * typedef struct Mtbl_ImplIfc_Test_t
     * { MtblHeadJc head;
     *   MT_testOverrideAble_ImplIfc_Test* testOverrideAble;
     *   MT_returnThisA_ImplIfc_Test* returnThisA;
     *   Mtbl_SimpleClass_Test SimpleClass_Test;
     *   //Method table of interfaces:
     *   Mtbl_Ifc_Test Ifc_Test;
     *   Mtbl_Ifc2_Test Ifc2_Test;
     * } Mtbl_ImplIfc_Test;
     * </pre>
     * The example shows the generated method table of the class {@link org.vishia.java2C.test.ImplIfc}.
     * This <code>struct</code> is used as part of a method table of a class, which extends this class,
     * in the same kind like the method table <code>Mtbl_SimpleClass_Test</code> is used here. That
     * used method table is generated from {@link org.vishia.java2C.test.SimpleClass} with C-code:
     * <pre>
     * extern const char sign_Mtbl_SimpleClass_Test[]; //marker for methodTable check
     * typedef struct Mtbl_SimpleClass_Test_t
     * { MtblHeadJc head;
     *   Mtbl_ObjectJc ObjectJc;
     * } Mtbl_SimpleClass_Test;
     * </pre>
     * The method table definitions have the following parts:
     * <ul>
     * <li>The declaration of the <code>sign_Mtbl_<i>Type</i></code>: It is a zero-terminated string.
     *   But only the address is used to identify the method tables.
     * <li>The <code>MtblHeadJc</code> is defined in <code>ObjectJc.h</code> and contains 2 elements:
     *   <ul><li><code>char const* sign</code>: The value of sign hava to be identically 
     *     with the address of the sign_Mtbl_TYPE. It is checked for safe access respectively used
     *     to find out the method table part of a base class.
     *   <li><code>int sizeTable</code>, it is the number of byte (size) of the appropriate table.
     *   </ul>
     * <li>All override-able methods of the own class are defined after them, 
     *   its types starts with <code>MT_</code> (Method Type) anyway.
     * <li>The method table of the immediate super class follows. That method table contains 
     *   a <code>MtblHeadJc head</code> and than the own methods, its immediate superclass and so on.
     *   In this kind the method tables of derived classes are nested.
     * <li>The method tables of all interfaces follows after them.
     * </ul>
     * Because the <code>SimpleClass</code> in simple only, it contains no override-able method
     * and only the method table of its superclass <code>Mtbl_ObjectJc</code>. That is defined
     * in <code>ObjectJc</code> and contains:
     * <pre>
     * typedef struct Mtbl_ObjectJc_t
     * { MtblHeadJc head;
     *   MT_clone_ObjectJc*    clone;
     *   MT_equals_ObjectJc*   equals;
     *   MT_finalize_ObjectJc* finalize;
     *   MT_hashCode_ObjectJc* hashCode;
     *   MT_toString_ObjectJc* toString;
     * } Mtbl_ObjectJc;
     * </pre>         
     * It contains the 5 methods, which are override-able for any class.
     * <br>
     * <br>
     * <b>Definition of the instance of a method table for a Type/Class:</b></br>
     * The instance is defined in the C-file in the following form (example):
     * <br>
     * At start of C-file the constant is declared because it may be used inside the C-file to detect 
     * sub method table parts. The used type of the method table contains its commonly type declared in headerfile
     * plus the end-sign.
     * <pre>
     * typedef struct MtblDef_ImplIfc_Test_t { Mtbl_ImplIfc_Test mtbl; MtblHeadJc end; } MtblDef_ImplIfc_Test;
     * extern MtblDef_ImplIfc_Test const mtblImplIfc_Test;
     * </pre>
     * At end of C-file or methods are defined, than the content can be filled without additional prototype declarations
     * of implementing methods:
     * <pre>
     * const MtblDef_ImplIfc_Test mtblImplIfc_Test = {
      { { sign_Mtbl_ImplIfc_Test//J2C: Head of methodtable.
        , (struct Size_Mtbl_t*)((3 +2) * sizeof(void*)) //size. NOTE: all elements are standard-pointer-types.
        }
      , testOverrideAble_ImplIfc_Test_F //testOverrideAble
      , testOverridden_ImplIfc_Test_F //testOverridden
      , returnThisA_ImplIfc_Test_F //returnThisA
      , { { sign_Mtbl_SimpleClass_Test//J2C: Head of methodtable.
          , (struct Size_Mtbl_t*)((0 +2) * sizeof(void*)) //size. NOTE: all elements are standard-pointer-types.
          }
        , { { sign_Mtbl_ObjectJc//J2C: Head of methodtable.
            , (struct Size_Mtbl_t*)((5 +2) * sizeof(void*)) //size. NOTE: all elements are standard-pointer-types.
            }
          , clone_ObjectJc_F //clone
          , equals_ObjectJc_F //equals
          , finalize_ImplIfc_Test_F //finalize
          , hashCode_ObjectJc_F //hashCode
          , toString_ImplIfc_Test_F //toString
          }
        }
        / **J2C: Mtbl-interfaces of ImplIfc_Test: * /
      , { { sign_Mtbl_Ifc_Test//J2C: Head of methodtable.
          , (struct Size_Mtbl_t*)((3 +2) * sizeof(void*)) //size. NOTE: all elements are standard-pointer-types.
          }
        , processIfcMethod_i_ImplIfc_Test_F //processIfcMethod
        , anotherIfcmethod_i_ImplIfc_Test //anotherIfcmethod_i
        , anotherIfcmethod_f_ImplIfc_Test_F //anotherIfcmethod_f
        , { { sign_Mtbl_ObjectJc//J2C: Head of methodtable.
            , (struct Size_Mtbl_t*)((5 +2) * sizeof(void*)) //size. NOTE: all elements are standard-pointer-types.
            }
          , clone_ObjectJc_F //clone
          , equals_ObjectJc_F //equals
          , finalize_ImplIfc_Test_F //finalize
          , hashCode_ObjectJc_F //hashCode
          , toString_ImplIfc_Test_F //toString
          }
        }
      , { { sign_Mtbl_Ifc2_Test//J2C: Head of methodtable.
          , (struct Size_Mtbl_t*)((3 +2) * sizeof(void*)) //size. NOTE: all elements are standard-pointer-types.
          }
        , processIfcMethod_f_ImplIfc_Test //processIfcMethod
        , testIfc2_f_ImplIfc_Test //testIfc2
        , anotherIfcmethod_f_ImplIfc_Test_F //anotherIfcmethod
        , { { sign_Mtbl_ObjectJc//J2C: Head of methodtable.
            , (struct Size_Mtbl_t*)((5 +2) * sizeof(void*)) //size. NOTE: all elements are standard-pointer-types.
            }
          , clone_ObjectJc_F //clone
          , equals_ObjectJc_F //equals
          , finalize_ImplIfc_Test_F //finalize
          , hashCode_ObjectJc_F //hashCode
          , toString_ImplIfc_Test_F //toString
          }
        }
      }, { signEnd_Mtbl_ObjectJc, null } }; //Mtbl
      
     * </pre>
     * It is a long term, because all method table parts of super classes and interfaces are contained
     * with the here implemented methods. The definition follows the type definition. Because the type definition
     * contains nested data, the data are extensive here in comparison to the type definition. 
     *  
     */
    public void D8_gen_MethodTable(){}
    
  }
  
  
  /**This chapter describes details of translation of inner classes. 
   * Inner classes are a essential element of programming. There are seven types:
   * <ul>
   * <li>Named static inner classes at class level: For that, the enclosing class is only a name spaces. 
   *     It is a strategy of building a union of closely depending parts. Such inner classes
   *     should be considerably only in the coherence with the enclosing class. It means,
   *     they should be private or protected mostly, or they should be need only in the same 
   *     code snippet together with the outer class. Elsewhere a primary class with its own file
   *     may be better to use. In static inner classes, all types and all static elements
   *     of the enclosing class are known directly without prefixing of the outer class. 
   * <li>Named non-static inner classes at class level: They have direct access to all elements of its enclosing 
   *     (outer) class, and they can be created with specification of an instance of the outer class 
   *     only. Internally there will be stored an aggregation (reference) to this outer class. 
   *     Such inner classes shouldn't be extensive. Elsewhere it may be better to have a primary class 
   *     with a named aggregation to the formerly other class.
	 * <li>Anonymous non-static inner classes at class level: 
	 *     They will be built in coherence with a class-level variable with a Java-construct like:
   *     <pre>
	 *     final RefType name = new BaseType(param){
	 *       //classBody of enhancing.
	 *     };
	 *     </pre>
	 *     That classes are able to use in coherence with that variable only. Nevertheless there 
	 *     are not binded to the variable, the variable holds the reference only. It is possible
	 *     to assign the reference to another element. The class exists independently
	 *     as definition and code snippet but is is access-able via the base-typed-reference only. 
	 *     The variable-initialization may be final or not. The final usage is typical though
	 *     the final keyword is missed in Java-sources often.
	 *     <br><br>
	 *     A static unnamed (anonymous) class can be implement
	 *     and override methods of its BaseType to provide a specialized functionality. The access
	 *     to a anonymous class can be done to the methods of the base type only. The anonymous class
	 *     can have own variables, inner classes etc., all of the things like any other class-definition.
	 *     But this elements are strong private. An access to private elements of a named inner class
	 *     form the outer class is possible in Java, but an access to the elements of an anonymous class
	 *     isn't possible from outside, because the class-type is not visible. 
	 *     <br><br>
	 *     Non-static classes have access to all elements of the enclosing (outer) class.
	 *     Therefore anonymous non-static classes are probably able to use for implementing interfaces. 
	 *     It may be the main usage
	 *     of such classes and it substantiated the necessity of such constructs: If a primary class
	 *     implements an interface, the implementing functionality may be subordinate, and it isn't
	 *     documented and visible well enough. But if an inner class, which can or should be anonymous,
	 *     and which need to be non-static, implements the interface, the implementing methods
	 *     are the only one of these class. The access to all elements of the enclosing class
	 *     supports usage of functionality of them.
	 *     <br><br>
	 *     For the Java2C-translation the anonymous class builds a normal struct adequate to named 
	 *     non-static inner classes.
	 *     The name of that CLASS_C-definition is derived from the variable-name.
	 *     The access to the elements of the outer class is done in C using the <code>outer</code>-reference.
	 *     
   * <li>Anonymous static inner classes at class level. 
   *     They will be built in coherence with static class-level variable with a Java-construct like:
   *     <pre>
	 *     final static RefType name = new BaseType(param){
	 *       //classBody of enhancing.
	 *     };
	 *     </pre>
	 *     Its usage with the variable are equivalent with non-static-inner classed,
	 *     but the variable is static and inside that class only static elements can be used.
	 *     <br><br>
	 *     For the Java2C-translation the anonymous class builds a normal struct adequate to named 
	 *     static inner classes.
	 *     The name of that CLASS_C-definition is derived from the variable-name.
	 *     
	 * <li>Static named classes at statement-block-level. 
	 * <li>Non-static named classes at statement-block-level. 
	 *        
	 * <li>Non-static anonymous classes at statement-block-level.        
	 *     They will be built inside a statement block. It is possible to initialize a block-level-
	 *     variable with it, or use inside a expression like:
   *     <pre>
	 *       methodCall(parameter, new BaseType(param){ ...body }, ...)
	 *     </pre>
	 *     or
	 *     <pre>
	 *       (new BaseType(param){ ...body }).executeMethod();
	 *     </pre>
	 *     In any case an instance is built with that type. The instance is referenced as is the rule.
	 *     The reference can be assigned inside the called method. In the second example
	 *     the reference may be stored in the <code>executeMethod()</code> because it is given
	 *     as <code>this</code> there. The class is access-able with that reference only.
	 * </ul>    
   */
  public class E_Inner_classes
  {
  	
  	/**Anonymous classes are a special construct of Java. They are written like:
  	 * <pre>
  	 * static RefType name = new BaseType(param){
  	 *   //classBody of enhancing.
  	 * };
  	 * </pre>
  	 * The <code>RefType</code> may be a super- or interface-Type of <code>BaseType</code>
  	 * or typical the same as <code>BaseType</code>. With this type the anoymous class is used.
  	 * <br><br>
  	 * The <code>BaseType</code> is the direct super type of the defined instance.
  	 * <br><br>
  	 * The <code>classBody for enhancing</code> may be overwrite some methods 
  	 * of the <code>BaseType</code> and can contain some variables or inner classes etc. too,
  	 * all what a class can be have. But it isn't possible to access other than the methods,
  	 * which are overridden. Only the overridden methods are visible in the <code>RefType</code>.
  	 * <br><br>
  	 * The constructor which is used is the adequate constructor of the <code>BaseType</code>.
  	 * The <code>classBody for enhancing</code> may define variable which can be initialized,
  	 * but an extra constructor is not possible to write.
  	 * <br><br>
  	 * The methods of the <code>classBody for enhancing</code> can access especially all elements
  	 * from the outer class, if this class is non-static, without the shown <b>static</b>-keyword.
  	 * Then a portal for example from an interface to the outer class through the anonymous class
  	 * is given. This is the most interisting aspect of inner anonymous classes. They implements
  	 * methods of the interface or overrides methods, but the methods are seperated from the outer class.
  	 * Not the outer class plays the role of the interface-implementer, but only the anonymous class.
  	 * That makes the implementing better supervise-able, respectively it allows the usage
  	 * of method names, which may be in coflicting with other interface implementations. 
  	 * Therefore it is an important concept for programming.
  	 * <br><br>
  	 * Such an anonymous class can be used whenever a new instance can be created. 
  	 * The anonymous class is bound only to the <code>new Type(param){ }</code>.
  	 * The suggestion, the declared <i>variable</i> is related immediate to the anonymous class is false.
  	 * The variable is only the reference to the instance of the anonymous class. 
  	 * A creation of an anonymous class-instance can also be done as part of an expression. 
  	 * If the anonymous class is defined inside a statement block, it is equal to definition it
  	 * as normal inner class at class level. It means the variables of the outer class body
  	 * can be accessed, but not the variables of the enclosing statement block.
  	 * <br><br>
  	 * <b>Translation of inner anoymous classes</b>
  	 * <br><br>
  	 * The inner anonymous class is translated in its first pass when the new-expression is evaluated.
  	 * This is inside {@link GenerateClass#createFieldDataNewObject(org.vishia.zbnf.ZbnfParseResultItem, org.vishia.zbnf.ZbnfParseResultItem, LocalIdents, LocalIdents, StatementBlock, String, ClassData, char, char, char, boolean)}
  	 * There the method {@link GenerateClass#gen_AnonymousClass(org.vishia.zbnf.ZbnfParseResultItem, org.vishia.zbnf.ZbnfParseResultItem, LocalIdents, StatementBlock, String, char, GenerateFile)}
  	 * is called. Inside that, the {@link FirstPass#runFirstPassClass(GenerateFile, String, String, String, String, org.vishia.zbnf.ZbnfParseResultItem, String, ClassData, java.util.List, boolean, ClassData)}
  	 * is called with an own instance of {@link FirstPass} and the {@link ClassData} 
  	 * of the anonymous type are produced.
  	 * <br><br>
  	 * The Definition of the struct of the inner class is placed in the header file in the same way
  	 * as for all other innerClasses, if the inner class can be used from outside.
  	 * The Definition of the struct is placed in the C-file, if the anonymous inner class is 
  	 * detected in the second pass only. But it is placed before the code of the current second path,
  	 * by using the {@link iWriteContent#writeCdefs(StringBuilder)}-method, 
  	 * which writes to {@link GenerateFile#uFileCDefinitions} buffer. This is placed before the 
  	 * adequate {@link GenerateFile#uFileCSecondPath} for the bodies. The definition of anonymous classes
  	 * in bodies of methods are never direct access-able outside, only via its base classes. 
  	 * So it shouldn't be defined in Headerfiles. But they are able to reference from outside.
  	 * Therefore, the reflection information is able to use unconditionally.
  	 * <br><br>
  	 * A adequate contruct of a 
  	 * <pre>
  	 * { //inside method body
  	 *   struct { data x; } stackVariable;
  	 * </pre>
  	 * which may be usual in C, it is not used in Java2C-translated codes.The machine code effort
  	 * is not lesser which such an construct against the extra definition of the struct with a internal-build-name
  	 * und use it with its name. But the representation of a extra definition is better. It is necessary 
  	 * for reflection and others. The anonymous class in Java has more capability as the anonymous struct
  	 * in C like shown above.
  	 * <br><br>
  	 * The constructor in an anonymous class is a overridden form of one of the constructors 
  	 * from the <code>BaseType</code>, selected by the actual parameters. It is overridden,
  	 * because the own local variables should be intialized too.
  	 * <br><br>
  	 * The anonymous classes are named in C. The name is built with a 
  	 * <code>C_</code><i>NameOfVariable</i><i>SuffixOuterClass</i> 
  	 * for all anonymous classes of variable initialization. if a new(..){...} outside of an variable
  	 * is translated, then <code>C</code><i>Nr</i><code>_</code><i>NameOfMethod</i><i>SuffixOuterClass</i>
  	 * is used to build the name, where <i>Nr</i> is the current number of anonymous class in the method,
  	 * started with 1 and mostly 1. This names mustn't be used as class-names by the user for other elements.
  	 * But there are specialized so that isn't suggest to do so. 
  	 *     
  	 */
  	public void e1_anonymousClasses(){}
  	
  	
  	/**The first pass of the inner classes are running while the first pass of the enclosing class
  	 * is processed. The inner classes are processed first, because there content may be known
  	 * while the first pass of the outer class runs.  At all events the {@link ClassData} of the 
  	 * inner classes may necessary to know, the ClassData are created as result of running its
  	 * first pass.
  	 * <br><br>
  	 * On the other hand, all elements of the outer class
  	 * are used only in the second pass of the inner class.
  	 * Inside the firstPass of the inner class, only types of other inner classes may be necessary to know.
  	 * TODO test complex constructs.  
  	 * 
  	 */
  	public void firstPassOfInnerClasses(){}
  	
  	
  	/**
  	 * The knowledge of field-identifiers and types of the outer classes and the sibling- inner classes
     * in inner classes and the knowledge of this one of the super classes in the derived classes
     * is necessary. But additionally the field-idents should be designated as outer or super ones.
     * That designation is contained in the fields {@link FieldData#nClassLevel} 
     * and {@link FieldData#nOuterLevel}. See {@link Docu.E_Inner_classes}    
     * 
     * The field-identifier of the outer class are registered for the inner class running 
     * {@link ClassData#completeFieldIdentsForInnerClasses()} at the end of the first pass. 
     * That is necessary because all idents are known only at the end of the first pass of the
     * outer class, but the first pass of the inner class is finished already, without knowledge
     * of the outer idents. All idents should be known while running the second pass only.
     * <br><br>
     * The method {@link ClassData#completeFieldIdentsFromOuterClass(LocalIdents)} is called unlike
     * if a class is generated in the second pass inside a block statement. In this case the 
     * first pass of this class is running only in the second pass of the environment.
     * <br><br>
     * The situation of same identifier in the outer and inner class is detected in that
     * complete-methods. The inner identifier covers the outer one.
     * 
  	 */
  	public void fieldIdentsOfOuterClassAndSiblings(){}
  	
  	/**
     * The types of the outer class are registered for the inner class running 
     * {@link ClassData#completeTypesForInnerClasses()} at the end of the first stage of the 
     * first pass of the outer class. This is done after calling {@link FirstPass#buildType(StringBuilder, String, GenerateFile, String, String, String, String, org.vishia.zbnf.ZbnfParseResultItem, boolean, String, ClassData, java.util.List, boolean, ClassData, char)}
     * in the {@link GenerateFile#runFirstPassFile(org.vishia.zbnf.ZbnfParseResultItem, String, String, String)}
     * -routine respectively in {@link ReadStructure#postPrepare(org.vishia.java2C.ReadStructure.ZbnfToplevel)}
     * for creation of ClassData from the stc-file. 
     * That is necessary because all types are known only at the end of the first stage 
     * of the first pass of the
     * outer class, but the first passes of the inner classes are finished already, without knowledge
     * of the outer idents. All idents should be known while running the second stage of first pass
     * ({@link FirstPass#runFirstPass2(StringBuilder)}) and the second pass only.
     * <br><br>
     * For anonymous inner classes the types of the outer class are copied always in the 
     * {@link GenerateClass#gen_AnonymousClass(org.vishia.zbnf.ZbnfParseResultItem, org.vishia.zbnf.ZbnfParseResultItem, LocalIdents, StatementBlock, String, char, GenerateFile)}
     * -routine after processing the {@link FirstPass#buildType(StringBuilder, String, GenerateFile, String, String, String, String, org.vishia.zbnf.ZbnfParseResultItem, boolean, String, ClassData, java.util.List, boolean, ClassData, char)}
     * of the anonymous class. In that time all types are known already, because the first stage
     * of the outer class is running already and all types except the anonymous are known. 
     * <br><br>
     * The {@link ClassData} of the anonymous classes shouldn't be known as types, because there are
     * anonymous. It pertains to all class-level anonymous class and statement-block anonymous classes.
     * <br><br>
     * The {@link ClassData} of statement-block-level classes shouldn't be known as types, 
     * because there are visible only at that statement-block level.
     * 
  	 */
  	public void typeIdentsOfOuterClassAndSiblings(){}
  }

  /**This chapter describes details of translation.
   */
  public class F_Translation_Secondpass
  {

  /**In Java a environment-type for a static method and a reference of a dynamic method are not differenced.
   * Both are written with a dot as separator:
   * <pre>
   * Arrays.binarySearch(..) //an example of static method of class Arrays
   * myInstance.method(...)  //a normal class method call.
   * </pre> 
   * The difference is detectable only searching the identifier in translator tables.
   * Therefore the identifier is tested in the method
   * in {@link StatementBlock#gen_reference(String[], ZbnfParseResultItem, LocalIdents, char, IdentInfos[] retIdentInfo)}.
   * The got association String from ZBNF-parse-result is tested calling {@link LocalIdents#getType(String, LocalIdents)}
   * in the given local environment. It includes especially standard types, 
   * but also local types in the class definition and the file-level types (package and import).
   * If the association String is recognized as Type, the associated type is used in the next reference levels
   * respectively it is returned in parameter retIdent. Because that type is the base type info for searching
   * the referenced identifier, the method calls in C are translated with the name_Type-info.    
   */  
    public void f1_referencesOrEnvironmentTypes()
    {
    }

    
    
    /**There are four types of castings, it is the combination of 
     * <ul>
     * <li>implicitly or explicitly casts,
     * <li>casts of scalar types (int etc) or reference types.
     * </ul>
     * The casts of scalar types is equal in C and Java: Cast to a type with higher precision may be implicitly,
     * the cast to a type with less precision need s a explicitly cast. In Java it is necessary, 
     * in C it is a warning of compiler mostly, but it should done.
     * <br><br>
     * The casts of reference types may be separated in two cases:
     * <ul>
     * <li>downcast: From a class to a super class or interface.
     * <li>upcast: From a interface or super type to a derived type.
     * </ul>
     * The downcast may be implicit or explicit written in Java, both is correct. 
     * The compiler knows all possibilities of down-casting, tests and accepts it. 
     * In C a downcast have to be programmed  with an access to the correct and given super class or interface.
     * A simple pointer cast is the second properly variant. 
     * A pointer cast is always a possible sourse of errors. Therefore it should be avoid to use
     * if it isn't necessary. An access to a super class can be write with <code>&ref->base.super.base.super</code>
     * instead an equivalent <code>(SuperType*)(ref)</code>. The machine code is equal for both variants.
     * <br><br> 
     * A upcast is written explicitly also in Java. 
     * A upcast is correct if a reference given as super-class or interface reference references
     * really a instance which is from the casted type. In Java a runtime check is execute. 
     * The demand of upcast from programmer is an assumption. Therefore it should be done explicitely.
     * In C the adequate construct should be a cast also. Another way of solution isn't able. 
     * But the C runtime doesn't check the legitimacy of that cast. A goal to explain the cast
     * in a safety software check session is, the code is tested in Java. Another possibility may be,
     * also execute a instance type test. The second decision needs some calculation time, 
     * it's the same problem as check the array indices for IndexOutOfBoundsException. 
     * The decision in Java2C for this problem is: If a failed access is able to assume, the programmer
     * should be write a instance type test in Java explicitly. It is <code>assert(ref instance of Type);</code>.
     * It will be translated and executed in C too, and it is conspicuously and should sufficing for a safety software check.
     * <br><br>
     * For Java2C-translation the check of cast-ability and the production of a cast expression 
     * is necessary for some cases:
     * <ul>
     * <li>Check of type compatibleness for arguments of methods: See {@link #methodCall_WithParameterSensitivity()}.
     *   it should be tested if a given argument is matching to formal argument types. 
     *   The method {@link ClassData#matchedToTypeSrc(ClassData)} is provided for such check.
     * <li>Production of explicitly casts where Java allows an implicitly cast.
     * </ul>   
     * Another problem is the adaption of access. In Java only either primitive types or references
     * are addressed in a variable or with an expression. In C at least the embedded instances needs
     * a special handling, the reference operator <code>&ref->embeddedInstance</code>. 
     * Additionally there are some special cases like a <code>StringJc</code> or <code>OS_TimeStamp</code>,
     * which are taken as value arguments and returned as values in methods.
     * <br><br> 
     * The method {@link FieldData#testAndcast(CCodeData)} produces the correct expression 
     * with given C-code including type and access mode of it in respect to a given field-type-description.
     * It is used at any assignments: method arguments, return value preparation and value assignments.
     * At example in Java it is coded <code>ref-embeddedInstance</code>, but a parameter or destination
     * variable is of an base or interface type. Than the method returns the necessary adaption 
     * for C language in form <code>&(ref->embeddedInstance.base.IfcType)</code>.
     * <br><br>
     * The methods {@link ClassData#addCastFromType(ClassData, String, String)} 
     * and {@link ClassData#addCastToType(ClassData, String, String)} adds a possibility of cast
     * to the a class. It is used explicitely for standard types, 
     * but also for declaring the possibility of access super and interface types, 
     * see {@link Docu.SuperClassesAndInterfaces}. 
     * It is called inside {@link ClassData#fillMethodsOverrideable(ClassData.InheritanceInfo inheritanceInfo, String sPathToMtbl, String sPathToBase)}
     * 
     */
    public void f2_casting(){}
    
    
    
    /**Methods in Java are parameter sensitive. This feature is known also as <i>overloading of methods</i>. 
     * It means, that an method of class StringBuilder
     * <code> append(int value)</code>
     * is another method as
     * <code> append(String value)</code>.
     * It is clear for Java users. Okay. In C++ it is the same concept.
     * <br><br>
     * But in C, the methods should have an unambiguously name. 
     * Therefore, the same steps have to be done which were done by a javac-compiler:
     * <ul>
     * <li>Ascertaining the type of all parameters of method call.
     * <li>Searching the appropriate method variant in the environment class, where the method is member of.
     * <li>If no appropriate method is found, variation of the type of parameters to their 
     *     generalized types, it means int from short, long from int or float from int etc., 
     *     but also detection of super classes and interface types of references. Search again.
     * <li>Building the C-like method name in a special style, considering the parameter types and the environment class name.
     * </ul>
     * This steps are done in Java2C-translation time. The result is visible in the generated C-code. 
     * This steps are done only if ambiguously method with the same names are exist. 
     * If only unambiguously method names in one class are given, a simple rule is used 
     * to build an unambiguously method name common spanned: <code>methodname_classname</code>.
     * <br><br><br>
     * For some lang and util-classes the methods have their fix names in the 
     * [[CRuntimeJavalike]]-C-Library, that is also useable for C-level-programming. Thats why, and also 
     * to support a testing at C-level the names of methods considering parameter types should be 
     * shortly, readable and understandable 
     * <br><br>
     * The translation of  Java method name plus parameter types to that C- method names are given 
     * in a translation table. There is no common fix rule to built it. 
     * But for method names of the users Java code to translate to C a rule  is given. 
     * <br><br>
     * The following things are implemented in the Java2C-Translator:
     * <ul>
     * <li>The <b>type of arguments</b> in the method call is detect calling 
     *     {@link StatementBlock#gen_value(ZbnfParseResultItem, char)}. 
     *     The type is returned in its {@link CCodeData}-return data. 
     *     The type of a value is build from 
     *     <ul>
     *     <li>The type of a variable, returned from 
     *         {@link StatementBlock#gen_variable(org.vishia.zbnf.ZbnfParseResultItem, LocalIdents, char, CCodeData)}
     *         {@link CCodeData}-return data.
     *     <li>The type of a constant value, returned from retType in
     *         {@link GenerateClass#genConstantValue(org.vishia.zbnf.ZbnfParseResultItem)}.
     *         The type of the constant value is detect while parsing the Java source code. The type of a numeric value
     *         is detect additional by testing the value range of the number. Especially the type of an string literal 
     *         in "string" is designated as {@link Java2C_Main.CRuntimeJavalikeClassData#clazz_s0}. 
     *         The <code>s0</code> means, C-like 0-terminated string. It is a special case of a String represented by
     *         { {@link Java2C_Main.CRuntimeJavalikeClassData#clazzStringJc}.
     *     <li>The type of a method is the return type. It is supplied in argument retType in
     *         {@link StatementBlock#gen_simpleMethodCall(org.vishia.zbnf.ZbnfParseResultItem, CCodeData, LocalIdents)}.
     *     <li>The type of a new Object is returned in retTypeValue from
     *         {@link StatementBlock#gen_newObject(org.vishia.zbnf.ZbnfParseResultItem, LocalIdents)} 
     *         or {@link StatementBlock#gen_newArray(org.vishia.zbnf.ZbnfParseResultItem, ClassData[], LocalIdents)}.
     *     </ul>
     * <li>Both a <b>method call and a constructor is translated</b> using 
     *     {@link StatementBlock#gen_simpleMethodCall(org.vishia.zbnf.ZbnfParseResultItem, CCodeData, LocalIdents)}.
     *     While translating the actual parameters are read from the ZbnfParseResultItem. The values are generated
     *     calling {@link StatementBlock#gen_value(org.vishia.zbnf.ZbnfParseResultItem, char)}.
     *     This method returns the type of the parameters. The types are used to search the method.
     * <li>To <b>search the method</b> {@link ClassData#searchMethod(String sNameJava, java.util.List paramsType)} 
     *     is called with the {@link ClassData}-instance of the environment type. 
     *     That is the type of the instance in Java left from point of method call. This ClassData are referenced from
     *     the parameter <code>envInstanceInfo</code> of <code>gen_simpleMethodCall()</code>.
     *     If the environment instance is <code>this</code>, the <code>envInstanceInfo</code> came from 
     *     {@link ClassData#classTypeInfo} of the own ClassData-Instance. 
     *     <ul>
     *     <li>The method is searched first with their number of arguments. It is possible that they are 
     *       more as one ambiguous method with the same name and the same number of arguments. 
     *       If there is only one method, its okay and unambiguous. Otherwise the types of the arguments
     *       are tested now.
     *     <li>If the types are matched directly, its okay. Elsewhere the routine {@link ClassData#matchedToTypeSrc(ClassData)}
     *         arbitrates whether the type is compatible. Than it is possible that a cast expression
     *         is necessary in C, without though an automatic casting in Java is done. 
     *         The cast expression is generated in 
     *         {@link StatementBlock#gen_simpleMethodCall(org.vishia.zbnf.ZbnfParseResultItem, CCodeData, LocalIdents)}.
     *     </ul> 
     * <li>The <b>method translating tables</b> from Java-name + parameter types to C-name are <b>created 
     *     for the CRuntimeJavalike-methods</b> in 
     *     {@link Java2C_Main.CRuntimeJavalikeClassData} - constructor. It is hand-made. 
     * <li>The method translating tables for translating classes will be created and filled 
     *     while executing 
     *     {@link FirstPass#runFirstPassFile(String, org.vishia.zbnf.ZbnfParseResultItem, String, ClassData, java.util.List, LocalIdents)}.
     *     <ul>
     *     <li>Before any other translation occurs, for all methods of the class,  {@link ClassData#testAndSetAmbiguousnessOfMethod(String)}
     *         is called with the java name of the method. If more as one method with the same name is found,
     *         the method name is ambiguous. Only than a complex method name for C should be built.
     *     <li>The methods are registered in first pass calling 
     *         {@link FirstPass#wrwrite_methodDeclaration(ZbnfParseResultItem parent, String sClassName, LocalIdents}.
     *         In this routine it is known whether or not the method is ambiguous, because all methods are tested before.
     *     <li>From there {@link FirstPass#gen_methodHeadAndRegisterMethod(org.vishia.zbnf.ZbnfParseResultItem, String, String, FieldData, boolean)},
     *         is called. In this routine the C-method-name is build either with argument-type-naming parts or not. 
     *         The argument-type-naming parts of the C-method-name are simple chars for standard types,
     *         see registration of standard types in {@link Java2C_Main.CRuntimeJavalikeClassData},
     *         calling the constructor of {@link ClassData#ClassData(String, String, String, String, String)}.
     *         A short name for user-Type-arguments is built with prominent chars of the type, 
     *         testing its unambiguously in global scope.
     *     <li>With that built C-Name and the argument types the method is registered in ClassData calling
     *         {@link ClassData#addMethod(String, String, FieldData, FieldData[])}.
     *     </ul>                 
     * </ul>        
     * 
     */
    public void f3_methodCall_WithParameterSensitivity()
    { SecondPass a;
      
    }
    
    
    /**Variable arguments are known both in Java and in C. 
     * In C the handling of variable arguments is more simple-primitive. 
     * The type of the arguments should be described somewhere else. The typical known application
     * of them is printf. Hereby the type of arguments are described in the printf-text 
     * combined with the format specification, at example 
     * <code>printf("values: %3.3f, %4d\n", floatValue, intValue);</code>. It is classic. 
     * Because the type of values may not correct (float or double, long, short), the compiler 
     * may convert a float value in double etc. to prevent errors. The originally idea was 
     * to determine the fine specification of the type with modifiers such as <code>%3hd</code> for short.
     * But already float and double are not possible to difference. Some compilers operates variedly.
     * That is C/C++.
     * <br><br>
     * In Java the problem of a printf-like formatting is some more separated from the variable argument handling.
     * Variable arguments are accepted as array of the same types. Because any complex type can downcast 
     * to the base Type Object, and any simple type has a representation as Object (int as class Integer etc.),
     * any argument can be provided as an Object:
     * <pre>
     *   method(Object ...){...} //declaration of a method
     *   method(5, 3.4, intVal, classInstance); //call of method.
     * </pre>
     * Java2C generated a type String as argument before a variable argument list in any case.
     * The type String contains the detect type of any actual argument as one letter. Especially
     * the primitive and some Standard types are detect in this way. All complex types are supplied as ObjectJc.
     * Therefore the method from example above has the following representation in C:
     * <pre>
     *   method(char const* typeArgs, ...);   //declaration of method
     *   method("IDIL", 5, 3.4, intVal, classInstance); //call
     * </pre>    
     *    ...TODO
     * 
     */
    public void f4_methodCallWithVariableArguments(){}
    
    
    
    
    public void f5_methodCallFromSuperClasses(){}
    
    public void f6_methodCallFromOuterClasses(){}
    
    
    /**A constructor is either a static method of the associated class, or it is understand as
     * a non-static method of the outer class, if the class is an non-static inner class.
     * The constructor is searched calling {@link ClassData#searchMethod(String, java.util.List, boolean)},
     * where the name is
     * <ul>
     * <li>"ctorO" for a static constructor with a class based on Object,
     * <li>"ctorM" for a static constructor with a class non-based on Object,
     * <li>"ctorO_InnerClassName" for a non-static constructor of a inner class based on Object.
     *     In this case the constructor is searched first in the given ClassData of the inner class,
     *     but it isn't found there. Because the inner-class has an outer one, the searching is continued
     *     in the outer class, and there the constructor is found as a non-static one.
     * <li>"ctorM_InnerClassName" adequate non-Object-based.
     * </ul>
     * To difference whether or not the associated class is a non-static-inner one,
     * the property {@link ClassData#isNonStaticInner} is checked.
     * This rules are regarded in 
     * <ul>
     * <li>{@link StatementBlock#genInitEmbeddedInstance(org.vishia.zbnf.ZbnfParseResultItem, org.vishia.zbnf.ZbnfParseResultItem, FieldData, String, int)}
     * <li>{@link StatementBlock#gen_newObject(org.vishia.zbnf.ZbnfParseResultItem, CCodeData, LocalIdents)}
     * </ul>
     * The correct constructor is found depending on its parameter, which are given as second param
     * while calling {@link ClassData#searchMethod(String, java.util.List, boolean)}.     
     * 
     */
    public void f7_constructorCall(){}
    
    
    
    /**The translation process of details should know informations about its environment of call. 
     * At example it is a difference whether a expression is evaluated for an assignment 
     * or to provide an actual parameter. Therefore a parameter named 'intension' is used often.
     * <ul>
     * <li> 'C' Class level variable def or inner class
     * <li> 'c' constructor body, 
     * <li> 'm' gen_statementBlock: -method body, 
     * <li> 'b' internal block, 
     * <li> 'z' part of if, while etc., 
     * <li> 'f' finalize body. 
     * <li> '=' . The argument retIdentInfo is a call by returned reference.
     * <li> 'R' the left element of a reference.
     * <li> 'r' for the next nested reference
     * <li> 'e' gen_value:-value in an expression, enhanced references are taken with .ref
     * <li> 'l' gen_value:-left value, 
     * <li> 'a' gen_value:-argument in c-file (head of routine)
     * <li> 'A' gen_value:-argument in h-file (prototype-definition)
     * <li> 't' gen_simpleMethodCall: String concatenation
     * <li> 'i' gen_assignValue: init value
     * <li> 's' Static variable
     * <li> 'P' top-level class
     * <li> 'Y' anonymous inner class at class level
     * <li> 'x' extern
     * </ul>       
     * 
     * <table>
     * <th><td><code>Ccmbzf=RrelaAtisPY-</code></td><td>method</td></th>
     * <tr><td><code>-------------------</code></td><td>{@link SecondPass#gen_statementBlock(org.vishia.zbnf.ZbnfParseResultItem, int, org.vishia.java2C.StatementBlock, FieldData, char)}</td></tr>
     * <tr><td><code>-cmb-f-----aA------</code></td><td>{@link SecondPass#gen_variableDefinition(org.vishia.zbnf.ZbnfParseResultItem, org.vishia.zbnf.ZbnfParseResultItem, LocalIdents, java.util.List, char)}</td></tr>
     * <tr><td><code>C--------------s---</code></td><td>{@link GenerateClass#gen_AnonymousClass(org.vishia.zbnf.ZbnfParseResultItem, LocalIdents, String, char, GenerateFile)}</td></tr>
     * <tr><td><code>Ccmb------------P--</code></td><td> {@link FirstPass#buildType(StringBuilder, String, GenerateFile, String, String, String, String, org.vishia.zbnf.ZbnfParseResultItem, boolean, String, ClassData, java.util.List, boolean, ClassData, char)}</td></tr>
     * <tr><td><code>Ccmb------------PYx</code></td><td> {@link ClassData#ClassData(String, GenerateFile, String, String, String, String, String, char, ClassData, ClassData, ClassData[], org.vishia.zbnf.ZbnfParseResultItem, String, char, LocalIdents)}</td></tr>
     * </table>
     */
    public void f9_intension_of_call(){}
    
  }
  
  
  
  /**This chapter describes some cohesion of allocation, initializing (construction), finalizing, 
   * garbage collection. 
   * 
   *
   */
  class G_Instantiation
  {
  	/**In Java there are two kinds of instances:
  	 * <ul><li>Primitive types such as int, float. This instances are created embedded or in stack.
  	 *   Their content is copied always if there are taken in a method call.
  	 * <li>Instances based on Object. This instances are created in the heap always, they are referenced.
  	 *   It the content is taken in a method call, a reference will be copied.
  	 * </ul>
  	 * In opposite, in C there are given four kinds of instances:
  	 * <ul>
  	 * <li>Primitive type, same as in Java.
  	 * <li>Embedded types. Their are only some types like <code>MemC</code>, <code>StringJC</code>
  	 *   or <code>OS_TimeStamp</code>, which are presented in that kind. This instances are used
  	 *   embedded only. They are taken as arguments or as return-value of methods per value anytime.
  	 *   They are never referenced. It is similar like primitive types. The embedded types are
  	 *   short structures, only consisting of less memory words. They should pass through registers
  	 *   as return value.   
  	 * <li>Structured instances not base on Object.
  	 * <li>Structured instances based on Object. Both kinds of structured instances can be allocated
  	 *   in the heap, may be embedded in another structure and may be created in static memory space. 
  	 *   It can be used with a reference, or maybe with given instance. If they are used as arguments
  	 *   or return values from methods, they are referenced anyway.   
  	 * </ul>
  	 * Embedded types are types, which are deployed in C with a simple small struct, 
  	 * handled call and return per value. There are applications for that concept for special cases.
  	 * * {@link FieldData#modeAccess} = '$'
  	 */
  	void g1_kindOfInstances(){}
  	
  	void g2_allocation(){}

  	/**In the CRuntimeJavalike context a <code>struct MemC {int32 size_; MemAreaC* address;} </code>
  	 * is defined. 
  	 * It is placed in the header file <code>fw_MemC.h</code>, because it is not a own concept of 
  	 * CRuntimeJavalike, 
  	 * but an own concept of common C-programming. This struct MemC is an answer about the question: 
  	 * <i>I have a void*-pointer to the memory, but which and how many bytes....</i> 
  	 * In generally a void*-pointer is a undefined thing. No type is given, no size is known. 
  	 * A <code>void*</code> is a well concept in the C-language with its closure to machine level, 
  	 * but not for user-level programming.
  	 * <br><br>
  	 * An instance described with <code>MemC</code> is undefined in type, but defined in size. 
  	 * It is a representation of raw memory. An allocation of memory with <code>alloc_MemC(size)</code> 
  	 * returns this struct with the found address and the associated size. It is one parameter for using. 
  	 * Otherwise the size is separated from the void*-pointer and mistakes are possible. 
  	 * The MemC comprises only 2 register values. Therefore most of C compilers return it 
  	 * in machine level in 2 register, not in a copied struct. It is efficient.
  	 * 
  	 */
  	void g3_referencingWithMemC(){}
  	
  	/**The four kinds of instances (see {@link #g1_kindOfInstances()}) uses several kinds of constructors
  	 * to initial them with given arguments:
  	 * <ul>
  	 * <li>Primitive types doesn't need a constructor. Initial values are assigned immediately.
  	 * <li>Embedded types are initialized calling a <code>INIT...(THIS, ...)</code>-routine. The first
  	 *   argument is the instance itself, not referenced. The <code>INIT...(THIS, ...)</code> should be
  	 *   implemented as a macro anyway. Mostly it is a simple macro.
  	 * <li>A non-ObjectJc-based instance is initialized with a <code>ctorM...(MemC mthis, ...)</code>-
  	 *   constructor. The memory space for the instance is given in a MemC-structure, which contains
  	 *   the pointer and the length information about the memory space of the instance. The MemC-value
  	 *   may be a return value of a <code>alloc_MemC(size)</code>-call or it is build from a given
  	 *   embedded instance using a build_MemC(ref, length)-method. The ctorM-method should check
  	 *   whether the memory space is large enough. It should not be assumed, that the data are initialized
  	 *   with 0. The initializing should be done in the constructor.
  	 * <li>A ObjectJc-based instance is initialized with a <code>ctorO...(ObjectJc* othis, ...)</code>-
  	 *   constructor. The memory space for the instance is given with the ObjectJc-pointer. The 
  	 *   ObjectJc-data should be initialized already, only the reflection information can't be set.
  	 *   The size of the instance is contained in the ObjectJc-objectIdentSize value. It can be seen
  	 *   as guaranteed, that the rest of values are initialized with 0 already. Either the 
  	 *   <code>alloc_ObjectJc(...)</code> method has initialized all allocated memory bytes with 0, 
  	 *   and it has initialized the ObjectJc-base data, or the <code>init_ObjectJc(...)</code>-method
  	 *   should be do that.
  	 *   <br><br>
  	 *   The ObjectJc-pointer
  	 *   may be a return value of a <code>alloc_ObjectJc(size)</code>-call or it is build from a given
  	 *   embedded instance with referencing it. The ctorO-method should check
  	 *   whether the memory space is large enough.< 
  	 *   <br><br>
  	 *   The initializing of an embedded instance should be done before calling the method
  	 *   <code>init_ObjectJc(ObjectJc* ythis, int sizeObj, int identObj)</code>.
  	 *   This routine sets the initial values of the ObjectJc-data and sets the rest of data to 0.
  	 * <ul>
  	 */
  	void g5_ctor(){}

  	void g10_enhancedRefs(){}

  	void g11_garbageCollection(){}

  	void g12_blockHeap(){}

  	/**Java knows a method Object.finalize(), which is called from the system before the instance
  	 * is removed from memory. It is invoked in cohesion with the garbage collector. The finalize()-
  	 * method can be overridden in any instance. But this method should be used carefully. 
  	 * It is not deterministic when and whether at all this method is invoked because the garbage-
  	 * collector has its own rules. If the instance isn't use anymore, it is possible that it isn't
  	 * removed by the garbage collector, because there is enough memory and thereby no need of
  	 * freeing memory. A programmer shouldn't use this method for normal clearing up actions
  	 * for example to close files.
  	 * <br><br>
  	 * In the case of using the {@link #g5_blockHeap()} the finalize method is used especially
  	 * to remove enhanced references. The references may refer other instances which were in use,
  	 * although the instance itself isn't use and it is freed therefore. 
  	 * The referred instances contains backward references to the freed instance. If the enhanced
  	 * references are not cleaned, that backward references are faulty.
  	 * <br><br>
  	 * Therefore the finalize-method is generated from the Java2C-translator to clean the 
  	 * enhanced references. In the finalize()-method the finalized-methods of embedded instances
  	 * and of the super classes are called too. If a user-written finalize()-method is present in
  	 * the Java-code, its content will be included firstly.
  	 * <br><br>
  	 * Only for instances which aren't base on Object (simple data) and which doesn't contain
  	 * enhanced references no finalize method is generated. The property {@link ClassData#bFinalizeNeed}
  	 * contains the information about. It is set calling {@link ClassData#needFinalize()}
  	 * or {@link ClassData#setBodyForFinalize(org.vishia.zbnf.ZbnfParseResultItem)}.
  	 * It is able to quest calling {@link ClassData#isFinalizeNeed()}.
  	 * <br><br>
  	 * The finalize()-method is generated with 
  	 * {@link SecondPass#write_finalizeDefinition(org.vishia.zbnf.ZbnfParseResultItem, String, LocalIdents)}.
  	 */
  	void g16_finalizing(){}

  	
  	
  	/**Embedded types are types, which are deployed in C with a simple small struct, 
  	 * handled call and return per value. There are applications for that concept for special cases.
  	 * * {@link FieldData#modeAccess} = '$'
  	 * 
  	 */
  	void g7_embeddedTypes(){}
  	
  	
  }
  
  
  class H_StringProcessing
  {
  	
  }
  
  /**All annotations
   * <ul>
   * <li>nonPersistent: to an reference: Designates, that the referenced data 
   *   may be non-persistent. The reference can be assigned only to another nonPersistent reference.
   *   Especially it can't be assigned to class-level references. But the reference can be used
   *   as instance-reference for method-calls.
   * <li>nonPersistent: to a method-parameter: Designates, that the parameter is used non-persistent
   *   in the called routine. It means, the referenced content isn't referenced independently
   *   with operations inside this routine in any other class-reference or static-reference.
   *   The Java2C-translator checks the assignment to a non-nonPersistent reference and causes
   *   an error if it os done.
   * <li>toStringNonPersist: to an expression containing a <code>toString()</code> especially
   *   with a StringBuilder.toString():
   *   Determines that the String is processed immediately before any other change on the StringBuilder-
   *   instance may be done. An access in any other thread is excluded too. The programmer should 
   *   declare this behavior, it should be able to supervise in less instructions. The java2c-translator
   *   can build a StringJc from the StringBuilder-content without any additional operation.
   *   It is optimal respectively usage of memory. 
   * </ul>
   * 
   */
  class N_annotations_Java2C
  {
  	
  }
  

  /**There are some Examples showing Java code and the generated C-Code. This Examples containing in
   * {@link org.vishia.java2C.test} are similar the type test exemplars.
   * <br>
   * <br>
   * See 
   * <ul>
   * <li>{@link org.vishia.java2C.test.SimpleClass}: A simple class
   * <li>{@link org.vishia.java2C.test.SimpleDataStruct}
   * <li>{@link org.vishia.java2C.test.ExpandedDataStruct}
   * <li>{@link org.vishia.java2C.test.Ifc}, {@link org.vishia.java2C.test.Ifc2}: interface definition
   * <li>{@link org.vishia.java2C.test.ImplIfc}: standard methods of implementation interfaces
   * <li>{@link org.vishia.java2C.test.TestString}: string processing
   * <li>{@link org.vishia.java2C.test.TestAllConcepts}: virtual methods etc.
   * </ul>
   */
  class X_Examples
  {
  }
  
  
}
