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

/**This interface describes the access to a variable in the user space.
 * @author Hartmut Schorrig
 *
 */
public interface VariableAccess_ifc
{

	/**Gets the value from this variable.
	 * @param ixArray unused if it isn't an indexed variable.
	 * @return the value.
	 */
	int getInt(int ixArray);
	
	/**Sets the value into the variable
	 * @param value The value given as int.
	 * @param ixArray unused if it isn't an indexed variable.
	 * @return The value really set (maybe more imprecise).
	 */
	int setInt(int value, int ixArray);
	
	
	/**Gets the value from this variable.
	 * @param ixArray unused if it isn't an indexed variable.
	 * @return the value.
	 */
	String getString(int ixArray);
	
	/**Sets the value into the variable
	 * @param value The value given as int.
	 * @param ixArray unused if it isn't an indexed variable.
	 * @return The value really set (maybe shortened).
	 */
	String setString(String value, int ixArray);
	
	
	
}
