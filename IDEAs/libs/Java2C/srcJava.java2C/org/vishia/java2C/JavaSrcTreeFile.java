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

import org.vishia.java2C.ConfigSrcPathPkg_ifc.Set;

/**Representation of an existing Java file.
 */
public class JavaSrcTreeFile implements JavaSources.ClassDataOrJavaSrcFile
{
  
  private final String sFileName;
  
  /**The name of the public class of this file. In Java only one class can be public in a file.
   */
  private final String sPublicClassName;
  
  private final String sJavaSrcPath;

  /**Pre- and Suffixes to build the class name in C additional to the java-name of classes. */
  private final String sPrefixClassNameC, sSuffixClassNameC;
  
  private final String sStcPath;
  
  private final String sFilePathC;

  private final JavaSrcTreePkg itsPkg;
  
  private File fileJava;
  
  /**True if this file is to be translate. */
  private boolean bToTranslateToC;
  
  /**informations to find out where the C-file is found and which pre/suffix are valid. */
  private final ConfigSrcPathPkg_ifc.Set replaceCinfo;
  
  /**If set, the file is translated, the classData presents its content for foreign use.
   */
  ClassData classData = null;
  
  /**Initializes the instance.
   * @param itsPkg The package where the file is member of.
   * @param javaSrcPath The source path where the file is found first time.
   * @param fileJava The File instance ready to open source file.
   * @param sFileNameJava The file name without path but with extension <code>.java</code> without directories.
   * @param prefixCFile The prefix to build the C class name and the C file name. 
   *        The part after the last slash <code>/</code> is the prefix for class names.
   *        The prefix should ended with slash if the file name has no prefix, 
   *        but a path to store the to create *.c and *.h files are given.
   *        The prefix may contain <code>""</code>.
   *        <br>
   *        If the prefix is null, the param suffixCFile contains the complete C-File-name. 
   *        This is only possible for Files, which are not translated from Java2C, 
   *        but it is parsed their stc-File. In this case the stc-File contains the valid 
   *        <code>struct</code> names for C. No {@link#sPrefixClassNameC} or suffix is necessary than.
   * @param suffixCFile The suffix to build the C class name and the C file name.
   * @param prefixNames
   * @param suffixNames
   * @param stcPath null or a special path where the stc-file is located.
   * @param translateToC false than the file should not be translate to C,
   *        instead the stc-file should be used any time. This is if it is a standard class 
   *        or a users class, which is located in a library.
   */
  public JavaSrcTreeFile(JavaSrcTreePkg itsPkg
  , String javaSrcPath, File fileJava, String sFileNameJava
  , ConfigSrcPathPkg_ifc.Set info
  , String stcPath
  , boolean translateToC
  )
  {
    super();
    this.replaceCinfo = info;
    if(sFileNameJava.endsWith(".java")){
	    this.sPublicClassName = sFileNameJava.substring(0, sFileNameJava.length()-5); // without ".java"
    } else {
    	this.sPublicClassName = sFileNameJava;  //may be a package, for package replacement.
    }
	  this.sJavaSrcPath = javaSrcPath;
    this.fileJava = fileJava;
    this.sFileName = sFileNameJava;
    if(info !=null){
	    String prefixCFile = info.getFilePrefix();
    	int posFolderPrefix1 = prefixCFile.lastIndexOf('/');
	    int posFolderPrefix2 = prefixCFile.lastIndexOf('\\');
	    int posFolderPrefix = posFolderPrefix1 >=0 && posFolderPrefix1 > posFolderPrefix2 ? posFolderPrefix1 : posFolderPrefix2;
	    //sPrefixClassNameC = posFolderPrefix >=0 ? prefixCFile.substring(posFolderPrefix+1): prefixCFile; //may be empty, but not null.
	    sPrefixClassNameC = info.getNamePrefix(); //may be empty, but not null.
	    sSuffixClassNameC = info.getNameSuffix();
	    String suffixCFile = info.getFileSuffix();
	    sFilePathC = prefixCFile + (suffixCFile == null ? "" : sPublicClassName + suffixCFile);
    } else {
	    sPrefixClassNameC = null; //may be empty, but not null.
	    sSuffixClassNameC = null;
	    sFilePathC = null;
    }
	  sStcPath = stcPath;
    this.bToTranslateToC = translateToC;
    this.itsPkg = itsPkg;
    itsPkg.getPkgLevelIdents().putClassType(sPublicClassName, this);
  }
  
  /**returns <code>null</code>if the file is not translated, returns the ClassData if it is translated.
   * @see org.vishia.java2C.JavaSources.ClassDataOrJavaSrcFile#getClassData()
   */
  public ClassData getClassData()
  {
    return classData;  //null if not translated
  }


  /**returns the instance because it is.
   * @see org.vishia.java2C.JavaSources.ClassDataOrJavaSrcFile#getJavaSrc()
   */
  public JavaSrcTreeFile getJavaSrc()
  {
    return this;
  }
  
  
  
  /**
   * @param fileJava
   */
  void infoSecondFile(File fileJava, boolean shouldTranslated)
  { if(shouldTranslated){
      if(this.fileJava == null){
       	//if it is created while package replacement, the file isn't associated. do it now.
        this.fileJava = fileJava; 
      }
  	  bToTranslateToC = true;
    }
  
  }
  
  public String getStcPath(){
  	if(sStcPath != null) return sStcPath;  //special convention
  	else return sFilePathC + ".stc";                //standard convention
  }
  
  public String getPublicClassName(){ return sPublicClassName; }
  
  public LocalIdents getPkgLevelTypes(){ return itsPkg.getPkgLevelIdents(); }
  
  public String getFileNameC(){ return sFilePathC; } 
  
  /**Returns the source path where the file is found. 
   * It is only a info, where the file comes from, not to process.
   */
  private String getJavaSrcPathInfo(){ return sJavaSrcPath; }
  
  public String toString()
  { String info= (sJavaSrcPath!=null ? sJavaSrcPath: "noJavaSrc") + "/" + this.itsPkg.getPkgPath() 
  	+ this.sFileName 
    + (bToTranslateToC ? " -> " + this.getFileNameC() : " : " + this.sStcPath );
    return info;
  }  

  public String getPkgPath(){ return itsPkg.getPkgPath(); }
  
  
  
  public String getClassCNamePrefix(){ return sPrefixClassNameC; }

  public String getClassCNameSuffix(){ return sSuffixClassNameC; }
  
  @Override public void setClassData(ClassData data)
  { //assert(classData == null);  //set only one time.
    assert(sPublicClassName.equals(data.sClassNameJava));
    classData = data;
  }
  
  public boolean isToTranslate(){ return bToTranslateToC; }

  public void setToTranslate(File fileJava){ 
  	bToTranslateToC = true;
  	this.fileJava = fileJava;
  }

  
  
  public boolean isTranslated(){ return classData != null; }
  
  
  public final File getFileJava()
  {
    return fileJava;
  }

  public LocalIdents xxxgetLocalIdents()
  {
    if(classData != null){
      return classData.classLevelIdents;
    } else {
      return null;  //no local idents available.
    }
  }

	/**Gets the local idents of the givenclass of the file. 
	 * The file may be translated or not. If it isn't translated, it will be translated now
	 * respectively the structure-file (stc) is translated. 
	 * @param sClassName name of the class in file, it may the public class or another one.
   * implements {@link org.vishia.java2C.JavaSources.ClassDataOrJavaSrcFile#getLocalIdents(java.lang.String)} 
	 * */
  public LocalIdents getLocalIdents(String sClassName)
  {
  	if(classData == null){
  		/**An inner type of a class in file is given. The file isn't translate,
  		 * therefore the localIdents doesn't exist. */
  	  if( true ){ //isToTranslate()){
      	//assert(!isTranslated()); //elsewhere the localIdents should exist
        JavaSrcTreeFile javaFile = this;
        try
        { /**If a identifier is found, but it has not ClassData, it is a non-translated file.
           * Than run the first pass to get the ClassData. It may be that a stc-file is read.
           */
          classData = Java2C_Main.singleton.runRequestedFirstPass(javaFile, sClassName);
        } 
        catch (FileNotFoundException e){ throw new IllegalArgumentException("file not found: " + javaFile.toString()); }
        catch (IOException e){ throw new IllegalArgumentException("file error: " + javaFile.toString()); }
        catch (ParseException e)
        { //e.printStackTrace();
          throw new IllegalArgumentException("parse exception: " + e.getMessage() + " in "+ javaFile.toString()); 
        }
        catch (InstantiationException e){ throw new IllegalArgumentException("instanciation exception: " + javaFile.toString()); }
        catch (IllegalAccessException e){ throw new IllegalArgumentException("illegal access: " + javaFile.toString()); }
        //catch (IllegalArgumentException e){ throw new IllegalArgumentException("file not found: " + javaFile.toString()); }
        
      }
    }
    return classData.classLevelIdents;
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
    return sPublicClassName;
  }

	/* (non-Javadoc)
	 * @see org.vishia.java2C.JavaSources.ClassDataOrJavaSrcFile#getReplaceCinfo()
	 */
	@Override
	public Set getReplaceCinfo() {
		return replaceCinfo;
	}
  

}


