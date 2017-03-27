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
import java.io.FileFilter;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import org.vishia.mainCmd.Report;

/**This class is a helper to get the java source tree from given class path.
 * The class may be used only temporary, 
 * it is wrapped around its method {@link JavaSrcTreeGetter#gatherAllJavaSrcFiles(JavaFolder, List)}.
 * The usage is (new JavaSrcTreeGetter(report)).captureAllJavaSrcFiles(dst, src).
 *
 */
public class JavaSrcTreeGetter
{

  private final Report console;
  
  private final ConfigSrcPathPkg_ifc inputCfg;
  
  /**Aggregation to the Instance, which stores the produced data 
   * while call of {@link #captureAllJavaSrcFiles(List, JavaSrcTreePkg, List)}.
   */
  private final JavaSources dstJavaSrcData;
  
  private final List<String> listInputToTranslate;
  
  private FileFilter javaFileFilter = new FileFilter(){
    public boolean accept(File file)
    { return file.isFile() && file.getName().endsWith(".java");
      }
    };
    
  private FileFilter dirFilter = new FileFilter(){
      public boolean accept(File file)
      { return file.isDirectory();
      }
    };
    
 
  /**Constructs an instance, which should be used only temporary to capture the tree.
   * @param inputCfg Association to the instance, which knows the assignment between Java-Packages and C-Pre- and Suffixes.
   * @param console
   */
  public JavaSrcTreeGetter(JavaSources dstJavaSrcData, ConfigSrcPathPkg_ifc inputCfg, List<String> listInputToTranslate, Report console)
  {
    this.console = console;
    this.inputCfg = inputCfg;
    this.dstJavaSrcData = dstJavaSrcData;
    this.listInputToTranslate = listInputToTranslate;
  }

  /**Captures all Java Files, which are found at file system in all given Java Source pathes
   * evaluates the content of {@link ConfigSrcPathPkg_ifc} (association given on constructor)
   * and sets respectively completes the input files.
   * Uses the {@link #inputCfg} aggregation to get the source path and the package replacement
   * 
   * @param javaSrcTree The destination, it should be an empty List before.
   * @param listJavaSrcpath List of all source pathes. The list won't be changed here.
   * @param listInputToTranslate List of all sources to translate. The list won't be changed here.
   * @throws ParseException 
   */
  public void gatherAllJavaSrcFiles(LocalIdents identsToAssignFirstPkgIdents, List<String> listJavaSrcpath) throws ParseException
  { console.reportln(4, "gatherAllJavaSrcFiles");
	  for(String srcPath: listJavaSrcpath){
    	console.reportln(4, "* Folder: " + srcPath);
    	File javaFolder = new File(srcPath);
      if(!javaFolder.isDirectory()) throw new IllegalArgumentException("Java-Srcpath should be a directory: " + srcPath);
      File[] firstPkgs = javaFolder.listFiles(dirFilter);
      for(File firstPkg: firstPkgs){
        captureAllJavaSrcFilesRecursive(identsToAssignFirstPkgIdents, firstPkg, dstJavaSrcData.javaSrcTree, "", srcPath, false);
      }
    }
    if(console.getReportLevel()>=Report.fineInfo){
      console.reportln(Report.fineInfo, "===All found Java source files==="); 
      reportAllJavaSrcFiles(dstJavaSrcData.javaSrcTree);
    }
  }
  
  
  /**Captures the package folders and java files of the given file directory level.
   * Creates a {@link JavaSrcTreeFile} for all found files, but not if the FileInfo
   * exists already. It exists, if the file is found in another source-path already
   * or if it is defined in the {@link CRuntimeJavalikeClassData}
   * or it is defined by a package.file-replacement in the config-file, 
   * see {@link Java2C_Main#inputCfg}. 
   * 
   * @param identsToAssignPkgIdents
   * @param dirPkg The directory in file tree for package.
   * @param dstParent write packages and files into it.
   * @param sPkgPathParent package path with / as separator and at end for the given level
   * @param javaSrcPath The source path which is captured yet. Path before package tree.
   * @param bTranslatePkg
   * @throws ParseException 
   */
  private void captureAllJavaSrcFilesRecursive(LocalIdents identsToAssignPkgIdents
  		, File dirPkg, JavaSrcTreePkg dstParent, String sPkgPathParent
  		, String javaSrcPath, boolean bTranslatePkg) throws ParseException
  { String sNamePkg = dirPkg.getName();
    String sPkgPath = sPkgPathParent + sNamePkg + "/";    
    /**Register the package. */
    JavaSrcTreePkg dstJavaPkg = dstParent.getOrAddPkg(sPkgPath, sNamePkg, null); //, identsToAssignPkgIdents);
    dstJavaSrcData.indexJavaSrcPkgs.put(sPkgPath, dstJavaPkg);
    identsToAssignPkgIdents.putClassType(sNamePkg, dstJavaPkg);  //add in LocalIdents
    /**Check whether the whole package is to translate to C: */
    { Iterator<String> iter = listInputToTranslate.iterator();
      while(!bTranslatePkg && iter.hasNext()){
        String sPkgCheckTranslate = iter.next();
        if(sPkgCheckTranslate.equals(sPkgPath)){
          bTranslatePkg = true;
        }
      }
    }  
    console.reportln(Report.info, "  + Package" + (bTranslatePkg? "-transl:" : ":")+ sPkgPath + " dst:" + dstJavaPkg);
    /**check packages (directories) */
    File[] javaPkgs = dirPkg.listFiles(dirFilter);
    for(File subFolder: javaPkgs){
      captureAllJavaSrcFilesRecursive(dstJavaPkg.getLocalIdents(""), subFolder, dstJavaPkg, sPkgPath, javaSrcPath, bTranslatePkg);
    }
    /**check files */
    File[] javaFiles = dirPkg.listFiles(javaFileFilter);
    if(javaFiles.length>0){
      /**Get prefix and suffix for C-name: */
      //dstJavaPkg.addStdTypes(Java2C_Main.singleton.standardClassData.stdTypes);
      if(sPkgPath.equals("org/vishia/java2C/test/"))
        stop();
      /**Prefix and suffix from packageReplacement from config file. */
      final String sFilePrefixPkg, sFileSuffixPkg, sNamePrefixPkg, sNameSuffixPkg;
      ConfigSrcPathPkg_ifc.Set preSuffixPkg = inputCfg.getCPathPrePostfixForPackage(sPkgPath);
      if(preSuffixPkg == null){ /**The package is not found in package replacement: */
        sFilePrefixPkg = "";
        sFileSuffixPkg = "";
        sNamePrefixPkg = "";
        sNameSuffixPkg = "";
      }
      else{ /**Use the package replacement: */
        sFilePrefixPkg = preSuffixPkg.getFilePrefix();
        sFileSuffixPkg = preSuffixPkg.getFileSuffix();
        sNamePrefixPkg = preSuffixPkg.getNamePrefix();
        sNameSuffixPkg = preSuffixPkg.getNameSuffix();
      }
      /**Iterate all files: */
      for(File fileJava: javaFiles){
        String nameFileJava = fileJava.getName();
        String nameJava = nameFileJava.substring(0, nameFileJava.length()-5);  //always extension ".java", without it!
        /**Prefix and suffix from packageReplacement from config file. */
        final String sFilePrefix, sFileSuffix, sNamePrefix, sNameSuffix;
        ConfigSrcPathPkg_ifc.Set preSuffix = inputCfg.getCPathPrePostfixForPackage(sPkgPath+nameJava);
        if(preSuffix == null){ /**The file is not found in file replacement, use package replacement: */
          preSuffix = preSuffixPkg;
        	sFilePrefix = sFilePrefixPkg;
          sFileSuffix = sFileSuffixPkg;
          sNamePrefix = sNamePrefixPkg;
          sNameSuffix = sNameSuffixPkg;
        }
        else{ /**Use the file replacement: */
          sFilePrefix = preSuffix.getFilePrefix();
          sFileSuffix = preSuffix.getFileSuffix();
          sNamePrefix = preSuffix.getNamePrefix();
          sNameSuffix = preSuffix.getNameSuffix();
        }
        //String pathC = sFilePrefix + nameJava + sFileSuffix;
        /**Check whether it is to translate to C:*/
        assert(nameFileJava.endsWith(".java"));
        String sPublicClassName = nameFileJava.substring(0, nameFileJava.length()-5); // without ".java"
        if(sPublicClassName.equals("AllocInBlock"))
        	stop();
        String sFullPathName = sPkgPath + sPublicClassName;
        console.reportln(Report.info, "source: " + sFullPathName);
        //Check whether the file-info for the found file exists already.
        //create a JavaSrcTreeFile while checking the sources only if the file isn't known already.
        JavaSources.ClassDataOrJavaSrcFile javaFile = Java2C_Main.getRootLevelIdents().getTypeInfo(sFullPathName, null);
        if(javaFile == null){
          javaFile = Java2C_Main.singleton.standardClassData.stdTypes.getTypeInfo(sPublicClassName, null);
          if(javaFile != null)
          	stop();
        }
        if(javaFile == null){
          /**If the type is registered already as standard type (at ex from org/vishia/bridgeC), than do not handle.*/
        	console.report(Report.info, ": found in srcTree");
          javaFile = dstJavaPkg.setFileJava(
              javaSrcPath, fileJava, nameFileJava, preSuffix
            , sFilePrefix, sFileSuffix, sNamePrefix, sNameSuffix, null, false);
        } else {
        	console.report(Report.info, ": stdType");
        }
        boolean bTranslate= bTranslatePkg;
        if(!bTranslate)
        { /**Search whether the java-file is to translate: look in the listInputToTranslate. */
          Iterator<String> iter = listInputToTranslate.iterator();
          String sFilePathName = sPkgPath + nameFileJava;
          if(sFilePathName.startsWith("org/vishia/java2C/test"))
          	stop();
          while(!bTranslate && iter.hasNext()){
            String sCheckTranslate = iter.next();
            if(sFilePathName.startsWith(sCheckTranslate)) {
            //if(sCheckTranslate.startsWith(sFilePathName)){
              bTranslate = true;
            }
          }
        }  
        console.report(Report.info, bTranslate ? ": translate" : ": not translate");
        if(bTranslate){
        	assert(javaFile instanceof JavaSrcTreeFile);
        	JavaSrcTreeFile javaSrc = (JavaSrcTreeFile)javaFile;
        	javaSrc.setToTranslate(fileJava);
          dstJavaSrcData.listJavaSrcFilesToTranslate.add(javaSrc);
        }
        console.reportln(4, "    --File" + (bTranslate? "-transl:" : ":") + javaFile.toString());
      } 
    }
    
    
  }


  private void reportAllJavaSrcFiles(JavaSrcTreePkg javaPkgParent)
  {
    for(JavaSrcTreePkg javaPkg: javaPkgParent.listChildren()){
      console.reportln(Report.fineInfo, javaPkg.getPkgPath());
      javaPkg.reportClasses(console);
      reportAllJavaSrcFiles(javaPkg);
    }
  }
  
  void stop(){}

}  

