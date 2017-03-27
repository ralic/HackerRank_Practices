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


/**This interface is used to write content for c- and h-files.
 *
 * @author JcHartmut
 *
 */
interface iWriteContent
{
  void addIncludeH(String sType, String comment);

  /**registers an header file to include in the current header.
   * @param sFile The file path without .h
   * @param comment a commment written after the include line.
   */
  void addIncludeC(String sFile, String comment);

  void writeClassH(String line);

  /**Writes definitions into the C-file, which are placed before content 
   * written with {@link #writeClassC(String)}.
   * @param content the content with the necessary line feeds.
   */
  void writeCdefs(StringBuilder content);
  
  /**Writes definitions into the H-file.
   * @param content the content with the necessary line feeds.
   */
  void writeHdefs(StringBuilder content);
  
  void writeClassC(String line);

  void writeClassC(StringBuilder content);

  /**tests wether the type is used on import. */
  //String getImport(String sType);
}
