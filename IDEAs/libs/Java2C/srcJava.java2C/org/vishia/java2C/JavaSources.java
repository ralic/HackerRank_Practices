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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**This class contains all information of available java sources while translation process.
 * It is created only one time for {@link Java2C_Main#javaSources}. 
 * But this class contains the interface {@link ClassDataOrJavaSrcFile}, 
 * which is implemented in {@link JavaSrcTreeFile} and {@link JavaSrcTreePkg}.
 */
public class JavaSources
{
  
  public interface ClassDataOrJavaSrcFile
  {
    /**Returns the ClassData of the instance, or <code>null</code>. If the instance is of type ClassData,
     * it returns the instance itself. If the instance is of type {@link JavaSrcTreeFile}, it returns
     * the associated ClassData, if the file is translated yet or the stc-file is read already.
     * If the file isn't translated, it returns null.*/
    ClassData getClassData();
    
    /**Returns the instance if it is a JavaSrcTreeFile, else <code>null</code>. */
    JavaSrcTreeFile getJavaSrc();
  
    /**Returns the instance if it is a JavaSrcTreePkg, else <code>null</code>. */
    JavaSrcTreePkg getJavaPkg();
  
    /**Returns the name of the class or package. It is the public class in a file.
     */
    String getTypeName();
    
    
    /**Returns the identifier of classes or packages, which are available in the implementing
     * package, file or class.
     * 
     * @param sName Name of the element. Especially if it is a {@link JavaSrcTreeFile},
     *        it may be the name of the public class or another class in the file. 
     * @return Especially list of all types in the element. If it is a package, 
     *         the types may be the files in the package or sub-packages. 
     */
    LocalIdents getLocalIdents(String sName);
   
    /**Returns the informations to find out where the C-file is found and which pre/suffix are valid.
     * @return null if there are not such informations. That is especially for super-paths.
     */
    ConfigSrcPathPkg_ifc.Set getReplaceCinfo();

    /**Returns true if the translation from Java is set. 
     * It doesn't mean that it should be translated any time, the necessity of translation
     * depends on the time stamps of Java- and C-files. But the Java-File have to be existing.
     * @return false if always the stc-File is to be read, or it is a ClassData.
     */
    boolean isToTranslate();
    
    void setClassData(ClassData data);
    
}
  
  
  /**Tree of all found java-files in the source path, independent of their usage as source for translation.
   * The files at the leafs of the tree {@link JavaSrcTreeFile} contains the information, 
   * whether they should translate.
   * <br>
   * This JavaFolder is the root folder, without name and package designation. 
   * It contains the first level package instances. 
   */
  public final JavaSrcTreePkg javaSrcTree = new JavaSrcTreePkg(null, "", "", null);

   /**All files to translate.
   */
  final List<JavaSrcTreeFile> listJavaSrcFilesToTranslate = new LinkedList<JavaSrcTreeFile>();

  /**Index (Map) of all known packages to fast search via its name. 
   * The key contains the complete package path separated and ending with <code>/</code>.
   */
  final Map<String, JavaSrcTreePkg> indexJavaSrcPkgs = new TreeMap<String, JavaSrcTreePkg>();
  
  public JavaSources()
  {
  	
  }
  
  
}
