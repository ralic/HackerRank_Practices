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

import java.util.TreeMap;


/**This class represents the index of all java classes for Translation.
 * A singleton instance is created at start of translation.
 * It is filled successively if a new class is detected while executing the first pass of translation, the generation of header.
 * It is used for the second pass, the generation of C-file.
 */
public class AllData
{
  /**List of known types. */
  final TreeMap<String, ClassData> classes = new TreeMap<String, ClassData>();

  void add( String sClassName, ClassData clazz){ classes.put(sClassName, clazz); }

  ClassData get(String sClassName){ return classes.get(sClassName); }

}
