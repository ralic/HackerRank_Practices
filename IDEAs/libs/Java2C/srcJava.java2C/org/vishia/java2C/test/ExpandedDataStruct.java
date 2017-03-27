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



/**This data structure contains the {@link SimpleDataStruct} as super class.
 * In C the <code>struct</code> is defined as:
 * <pre class="CCode">
 * typedef struct ExpandedDataStruct_Test_t
 * { 
 *   union { ExpandableDataStruct_Test_s super;} base; 
 *   double e;
 *   double f;
 * } ExpandedDataStruct_Test_s;
 *</pre> 
 * The superclass is wrapped with an union, because the access is written anytime <code>ref.base.super</code>,
 * but the <code>union</code> doesn't contain an element <code>ObjectJc object</code> here. That is
 * because the base class in not based on ObjectJc. 
 */
public final class ExpandedDataStruct extends SimpleDataStruct
{ 
	/**Some variables in the data class respectively <code>struct</code>. */
	double e,f; ////
}
  
