package org.vishia.exampleJava2C.java4c;

import org.vishia.byteData.ByteDataAccess;

/**This class contains some variables, which are sent to a visualization app, 
 * an "Operation and Monitoring" app (OaM).
 * 
 * @author Hartmut Schorrig
 *
 */
public class OamVariables
{
  /**The current way, the measurement value. 1 um-unit. */
  public int way;
  
  /**The current way, set value. 100 um-unit*/
  public short wway;
  
  /**The target way, 100 um-unit. */
  public short targetWay;
  
  /**The abbreviation of the way-controller. 1 m-unit */ 
  public short dway;
  
  /**The output value of the controller. */
  public short output;
  
  public short stateSetValueGen;
  
  public byte ctController, ctSetValue;
  
  public static class OamVariablesByteAccess extends ByteDataAccess
  {
  	public final static int k_way = 0,
  	                  k_wway=4,
  	                  k_targetWay=6,
  	                  k_dway = 8,
  	                  k_output = 10,
  	                  k_stateSetValueGen = 12,
  	                  k_ctController = 14,
  	                  k_ctSetValue = 15,
  	                  sizeofHead = 16;
  	

		@Override protected void specifyEmptyDefaultData()
		{
			// TODO Auto-generated method stub
			
		}

		@Override protected int specifyLengthElement() throws IllegalArgumentException
		{ return sizeofHead;
		}

		@Override public int specifyLengthElementHead()
		{ return sizeofHead;
		}

		public void addToAndSetBinData(ByteDataAccess parent, OamVariables data)
		{
			parent.addChild(this);
			setInt32(k_way, data.way);
			setInt16(k_wway, data.wway);
			setInt16(k_targetWay, data.targetWay);
			setInt16(k_dway, data.dway);
			setInt16(k_output, data.output);
			setInt16(k_stateSetValueGen, data.stateSetValueGen);
			setInt8(k_ctController, data.ctController);
			setInt8(k_ctSetValue, data.ctSetValue);
	  }
		
		public void addToAndGetBinData(ByteDataAccess parent, OamVariables data)
		{
			parent.addChild(this);
			data.way = getInt32(k_way);
			data.wway = getInt16(k_wway);
			data.targetWay = getInt16(k_targetWay);
			data.dway = getInt16(k_dway);
			data.output = getInt16(k_output);
			data.stateSetValueGen = getInt16(k_stateSetValueGen);
			data.ctController = getInt8(k_ctController);
			data.ctSetValue = getInt8(k_ctSetValue);
	  }
		
  }
  
  
  
}
