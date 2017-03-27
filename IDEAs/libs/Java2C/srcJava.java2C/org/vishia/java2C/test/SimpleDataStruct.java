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
package org.vishia.java2C.test;



/**It is not a final (able to expand)  
 * data class. It doesn't base on ObjectJc in C. 
 * In C the <code>struct</code> is defined as:
 * <pre>
typedef struct ExpandableDataStruct_Test_t
{ 
  int16 xb;
  int16 yb;
  double db;
} ExpandableDataStruct_Test_s;
 *</pre>
 * It is very simple. Such an class may be used in an array. 
 * The inheritance from Object is a basic feature in Java anytime, but this feature doesn't may be used mostly.
 * Therefore in C it can be optimized writing a <code>@ java2c=noObject.</code>-tag in the comment.
 * <br><br>
 * See also {@link ExpandedDataStruct}. Inherited classes don't base on Object too.
 *  
 * @java2c=noObject.
 */
public class SimpleDataStruct
{
  /**Some variables in the data class. <code>short</code>in Java will be translated to <code>int16</code> in C. */
  short xb,yb;
 
  /**A double variable in Java is a double variable in C too. (8 Byte float).
   */
  double db;
}

