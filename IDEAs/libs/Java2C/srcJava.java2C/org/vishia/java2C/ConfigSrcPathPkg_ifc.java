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
import java.util.List;
import java.util.Map;

import org.vishia.mainCmd.Report;


/**Interface to access to getted values of a input cfg file. 
 */
public interface ConfigSrcPathPkg_ifc
{
  /**Sub-interface to get values from a prefix/suffix-set. */
  public interface Set
  { 
    /**Gets the path and maybe a file prefix. Return "" if it isn't given in input.cfg. */
    String getFilePrefix();
    /**Gets the suffix for files. Return null if it isn't given in input.cfg. Than the prefix contains the whole filename. */
    String getFileSuffix();
    /**Gets the prefix for names. Return "" if it isn't given in input.cfg. */
    String getNamePrefix();
    /**Gets the suffix for names. Return null if it isn't given in input.cfg. Than the prefix contains the whole classname*/
    String getNameSuffix();
    /**Gets the input path. */
    String getInputPath();
    
    String getStcFile();
    
  }
  
  /**Returns the list of all pathes parsed with 
   * <pre>
   * stcPath::=<?>{ <\"\"?stcPath>| <* :;?stcPath> ? : }.
   * </pre>
   */ 
  List<String> getStcPathes();

  /**Returns the list of all pathes parsed with 
   * <pre>
   * srcPath::=<?>{ <\"\"?srcPath>| <* :;?srcPath> ? : }.
   * </pre>
   */ 
  List<String> getSrcPathes();

  /**Returns the list of all sources to translate.
   * A member of the list is either a Java file with package from the pkg root with <code>/</code> at separator,
   * at example <code>org/vishia/util/StringPart.java</code>
   * or it is a package with <code>/</code> as separator and on end, 
   * at example <code>org/vishia/util/</code>. 
   */ 
  List<String> getSrcToTranslate();

  /**Returns the Set of path, prefix and postfix for a given package path.
   * @return If the package path isn't found, returns null.
   */
  Set getCPathPrePostfixForPackage(String javaPackagePath);
  
  void reportConfig(Report report, int reportLevel);

  public java.util.Set<Map.Entry<String, ConfigSrcPathPkg_ifc.Set>> getListPackageReplacements();
    
}
