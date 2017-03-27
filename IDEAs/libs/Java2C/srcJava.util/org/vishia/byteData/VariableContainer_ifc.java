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
package org.vishia.byteData;


/**This interface defines the access to search a variable in any container. 
 * It is used as universal concept to hold named variables.
 * @author Hartmut Schorrig
 */
public interface VariableContainer_ifc
{

	/**Searches a variable in the given container by textual given name.
	 * @param name The name of the variable. It may contain indices in textual form.
	 * @param index may be null. If not null, either an index is expected, or indices in textual form
	 *        contained in name are stored there.
	 * @return null if the variable not found, else the variable access description.
	 */
	VariableAccess_ifc getVariable(String name, int[] index);
}
