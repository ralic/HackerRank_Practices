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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map;

import org.vishia.mainCmd.Report;
import org.vishia.util.SortedTree;

/**Representation of an existing Folder in the Java pkg tree.
 */
public class JavaSrcTreePkg implements SortedTree<JavaSrcTreePkg>, JavaSources.ClassDataOrJavaSrcFile
{
	private final String sPkgPath;
  
  private final String sPkgName;
  
  /**The parent pkg, it is only to see in debug and reports.*/
  private final JavaSrcTreePkg parent; 
  
  private TreeMap<String, JavaSrcTreePkg> subPkgs;
  
  /**All file.java containing in this package. */
  private final TreeMap<String, JavaSrcTreeFile> XXXjavaFiles = new TreeMap<String, JavaSrcTreeFile>();

  
  /**Same content as {@link #javaFiles}, but the key is the class name. */
  //private final TreeMap<String, JavaSrcTreeFile> javaPublicClasses = new TreeMap<String, JavaSrcTreeFile>();
  private final List<JavaSources.ClassDataOrJavaSrcFile> javaPublicClasses = new LinkedList<JavaSources.ClassDataOrJavaSrcFile>();
  
  /**Set to true if {@link #setFileJava(String, File, String, String, String, boolean)}
   * is called the first time. */
  private boolean pkgHasFiles = false;
  
  /**If this info is found, a stc-file is determined for the package to replace structure info
   * instead given Java-files.
   */
  private ConfigSrcPathPkg_ifc.Set replaceCinfo;
  
  /**Index of all sub-packages and classes available in the package, package privates too. 
   * Only the field {@link LocalIdents#typeIdents} is used, because a package has no fields. 
   * But the type {@link LocalIndents} is necessary to implement 
   * the method {@link JavaSources.ClassDataOrJavaSrcFile#getLocalIdents(String)}
   * to support getting data to an identifier inside the package (may be sub-package or a file or a class known in package).
   */
  private final LocalIdents pkgIdents;

  
  /**
   * @param pkgIdent
   * @param javaSrcPath
   */
  public JavaSrcTreePkg(JavaSrcTreePkg parent, String pkgPath, String pkgName, ConfigSrcPathPkg_ifc.Set replaceCinfo) //, LocalIdents identsToAssignPkgIdents)
  { this.parent = parent;
  	this.sPkgPath = pkgPath;
    this.sPkgName = pkgName;
    this.replaceCinfo = replaceCinfo; //maybe null
    pkgIdents = new LocalIdents(parent ==null ? null : parent.getPkgLevelIdents(), null);
  }
  
  

  /**returns the Instance, which represents the given package name. 
   * If the Instance doesn't exist, it is created.
   * If it exists already before this call, and its {@link #replaceCinfo} is null, 
   * then the given parameter replaceCinfo is used to set it. 
   * If the {@link #replaceCinfo} was given before, and the parameter replaceCinfo is given here too,
   * it is tested whether it contains the same. Elsewhere a IllegalArgumentException is thrown.
   * 
   * @param pkgPath The full path from root
   * @param pkgName The name of the new package
   * @param replaceCinfo if not null, then it is the info to build the C-files /search the stc-file
   *                     for all non-existing, but needed Java-files of the package. 
   * @return The package management instance. 
   */
  public JavaSrcTreePkg getOrAddPkg(String pkgPath, String pkgName, ConfigSrcPathPkg_ifc.Set replaceCinfo) //, LocalIdents identsToAssignPkgIdents)
  {
  	JavaSrcTreePkg ret;
  	if(subPkgs == null){ 
    	subPkgs = new TreeMap<String, JavaSrcTreePkg>(); 
      ret = null;
    } else {
    	ret = subPkgs.get(pkgName);  //try, maybe not found
    }
    if(ret == null){
      ret = new JavaSrcTreePkg(this, pkgPath, pkgName, replaceCinfo); //, identsToAssignPkgIdents);
      Java2C_Main.singleton.javaSources.indexJavaSrcPkgs.put(pkgPath, ret);
      subPkgs.put(pkgName,ret);
      //javaPublicClasses.putClassType(pkgName, ret);
      this.pkgIdents.putClassType(pkgName, ret);   //to find as identifier while translating.
    } else {
    	//found, check to replace the replaceCinfo
    	if(replaceCinfo != null){
    		//maybe todo: check whether it is the same info, not only null-check
    		if(ret.replaceCinfo != null) throw new IllegalArgumentException("package: " + pkgName + " - second replaceinfo: " + replaceCinfo.toString());
    		ret.replaceCinfo = replaceCinfo;
    	}
    }
    return ret;
  }
  
  /**Adds the given type info to the pkgIdents.
   * @param data The type info.
   */
  public void putClassType(ClassData data){
  	pkgIdents.putClassType(data);
  }
  
  
  public String getPkgPath(){ return sPkgPath; }
  
  public final TreeMap<String, JavaSrcTreeFile> XXXgetJavaFiles(){

  	return XXXjavaFiles;
  }



  public final List<JavaSources.ClassDataOrJavaSrcFile> getPublicClasses()
  {
    return javaPublicClasses;
  }



  /**Adds all standard types to the package. This method will be called if any file is containing
   * in the package. It means, the package is the immediate package of any class. 
   * This method will be called too while creating a standard package (java.lang).
   * @param types
   */
  public void xxxaddStdTypes(LocalIdents types)
  {
    //javaPublicClasses.putClassTypesAll(types);
    pkgIdents.putClassTypesAll(types);
  }
  
  
  /**Registers the first occurrence of a found Java file or set the {@link ConfigSrcPathPkg_ifc.Set}
   * for an existing file without replaceCinfo 
   * @param itsPkg The package where the file is member of.
   * @param javaSrcPath The source path where the file is found first time.
   * @param fileJava The File instance ready to open source file.
   * @param sFileNameJava The file name without path but with extension <code>.java</code> without directories.
   * @param prefixCFile The prefix to build the C class name and the C file name. 
   *        The part after the last slash <code>/</code> is the prefix for class names.
   *        The prefix should ended with slash if the file name has no prefix, 
   *        but a path to store the to create *.c and *.h files are given.
   *        The prefix may contain <code>""</code>.
   * @param suffixCFile The suffix to build the C class name and the C file name.
   * @param stcPath null or a special path where the stc-file is located.
   * @param translateToC false than the file should not be translate to C,
   *        instead the stc-file should be used any time. This is if it is a standard class 
   *        or a users class, which is located in a library.
   * @return
   */
  public JavaSources.ClassDataOrJavaSrcFile setFileJava(String javaSrcPath, File fileJava, String sFileNameJava
  		, ConfigSrcPathPkg_ifc.Set info
  	  , String prefixCFile, String suffixCFile, String prefixNames, String suffixNames, String stcPath
  , boolean translateToC
  )//(String javaSrcPath, File fileJava, String nameFileJava, String pkgPath, String pathC, boolean bToTranslateToC)
  { JavaSources.ClassDataOrJavaSrcFile javaSrc;
    if(!pkgHasFiles){
      /** First call of this method, add all standard types at first. */
      pkgHasFiles =true;
    }
        
    //javaSrc = javaFiles.get(sFileNameJava);  //check whether the file is known yet.
    String sJavaClass = sFileNameJava.substring(0, sFileNameJava.indexOf(".java"));
    javaSrc = pkgIdents.getTypeInfo(sJavaClass, null);  //check whether the file is known yet.
    if( javaSrc== null){
      /**register only the first occurrence of a Java file. If the file is located more than one time,
       * the first occurrence should be used.
       */
      javaSrc = new JavaSrcTreeFile( this, javaSrcPath, fileJava, sFileNameJava, info
                                   , stcPath, translateToC);
      /**assume that the file contains a public class and put the name in type list, without yet ClassData. */
      javaPublicClasses.add(javaSrc);
      //TODO: a first first pass is necessary, with parsing file, to get all classes.
      //up to now: only one class per file.
      //it is done already: pkgIdents.putClassType(javaSrc.getPublicClassName(), javaSrc);
    }
    else{
    	//The package replacements are set already.
    	//notice, that it is found and maybe to translate!
    	//The Java-file may be exist already therefore, it is created in package replacement.
      JavaSrcTreeFile javaSrcFile = (JavaSrcTreeFile)javaSrc;
    	javaSrcFile.infoSecondFile(fileJava, translateToC);
    }
    return javaSrc;
  }


  public void reportClasses(Report report)
  {
  	Set<Map.Entry<String, JavaSources.ClassDataOrJavaSrcFile>> entries = pkgIdents.getTypeSet();
    for(Map.Entry<String, JavaSources.ClassDataOrJavaSrcFile> entry: entries){
    	JavaSources.ClassDataOrJavaSrcFile javaFile= entry.getValue();
      report.reportln(Report.fineInfo, "  +- " + entry.getKey() + " = "+ (javaFile !=null ? javaFile.toString(): "???"));
    }
    
  }
  

  public JavaSrcTreePkg getChild(String key)
  { return subPkgs.get(key);
  }

  /**gets the pkg-level-idents. It contains all types which are known 
   * at this package level (all files of package.). 
   */
  public LocalIdents getPkgLevelIdents(){ return pkgIdents; }

  public Iterator<JavaSrcTreePkg> iterChildren()
  {
    // TODO Auto-generated method stub
    return null;
  }



  public Iterator<JavaSrcTreePkg> iterChildren(String key)
  {
    // TODO Auto-generated method stub
    return null;
  }



  public List<JavaSrcTreePkg> listChildren()
  {
    List<JavaSrcTreePkg> ret = new LinkedList<JavaSrcTreePkg>();
    if(subPkgs != null){
      Set<Map.Entry<String, JavaSrcTreePkg>> entries = subPkgs.entrySet();
      for(Map.Entry<String, JavaSrcTreePkg> entry: entries){
        JavaSrcTreePkg folder= entry.getValue();
        ret.add(folder);
      }
    }  
    return ret;
  }



  public List<JavaSrcTreePkg> listChildren(String key)
  {
    // TODO Auto-generated method stub
    return null;
  }



  public ClassData getClassData()
  {
    return null;
  }



  /**Implements {@link JavaSources.ClassDataOrJavaSrcFile#getJavaSrc()}
   */
  public JavaSrcTreeFile getJavaSrc()
  {
    return null; // it isn't a JavaSrcTreeFile
  }

  /**Implements {@link JavaSources.ClassDataOrJavaSrcFile#getJavaPkg()}
   */
  public JavaSrcTreePkg getJavaPkg()
  {
    return this; 
  }
  



  /**Returns the identifier of classes or packages, which are available in this package.
   * It may be sub-package or a file or a class known in package.
   * implements {@link org.vishia.java2C.JavaSources.ClassDataOrJavaSrcFile#getLocalIdents(java.lang.String)}
   * @param sPkgName: It is ignored. 
   */
  @Override public LocalIdents getLocalIdents(String sPkgName)
  {
    return pkgIdents;
  }
  
  /**Implements {@link JavaSources.ClassDataOrJavaSrcFile#getTypeName()}
   */
  public String getTypeName()
  {
    return sPkgName;
  }
  
  @Override public String toString(){
  	return sPkgPath;
  	/*
  	StringBuilder ret = new StringBuilder(200);
  	ret.append(sPkgPath);
  	if(subPkgs != null){
  		ret.append(':').append(subPkgs.toString());
  	}
  	return ret.toString();
  	*/ 
  }



	/* (non-Javadoc)
	 * @see org.vishia.java2C.JavaSources.ClassDataOrJavaSrcFile#getReplaceCinfo()
	 */
	@Override
	public org.vishia.java2C.ConfigSrcPathPkg_ifc.Set getReplaceCinfo() {
		return replaceCinfo;
	}



	/**returns false because a package isn't to translate.
	 * @see org.vishia.java2C.JavaSources.ClassDataOrJavaSrcFile#isToTranslate()
	 */
	@Override
	public boolean isToTranslate() {
		return false;
	}  

	@Override public void setClassData(ClassData data){
  	throw new IllegalArgumentException("internal: ClassData for package");
  }

  
}
