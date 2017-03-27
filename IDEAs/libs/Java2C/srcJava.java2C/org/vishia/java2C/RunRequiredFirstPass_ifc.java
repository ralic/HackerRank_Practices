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

/**This interface is used to run the first pass of a requested class
 * while running first or second pass of another file.
 */
public interface RunRequiredFirstPass_ifc
{
  /**searches a file and build the ClassData parsing and converting the file.
   * This method is called if an unknown type is used yet.
   * @param sClassName The name of the type in Java. The filename should be the same.
   *        The file is searched in any package given as input parameter calling java2C.
   * @return null if no file found. Otherwise the built ClassData. 
   *         This ClassData are registered in the list {@link Java2C_Main#userTypes}.
   * @throws ParseException 
   */
  ClassData runRequestedFirstPass(final JavaSrcTreeFile javaSrc, final String sClassName) //, final String sPkgName) 
  throws FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException, ParseException;
  
}
