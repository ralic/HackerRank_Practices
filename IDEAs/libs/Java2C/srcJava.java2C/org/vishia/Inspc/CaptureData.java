package org.vishia.Inspc;

import java.lang.reflect.Field;


/**This class helps to capture data from a application using choice of variable via reflection.
 * The Implementation is used in C too, translated with Java2C.
 *
 */
public class CaptureData implements GetInspcData
{
  private final static InspcClassData inspcClassData = new InspcClassData(CaptureData.class);
  
  
  /**Memory address of the data. */
  int addr;



  /** Mask to capture only dedicated Bits. If 0 than not used.*/
  int maskValue; 
            
  /**Convertion info:
   * Bit 7..0 as char: upper case letter: conversion to 16 bit, 
   * lower case letter: conversion to float.
   * * 'F': float to 16 bit, using factor and zeroLine.
   * * 'D': double to 16 bit, using factor and zeroLine.
   * * 'f': float to float, using factor and zeroLine.
   * * 'd': double to float, using factor and zeroLine.
   * * 'I': int32 to int16, using maskValue and Shift Value.
   * * 'i': int32 to float.
   *
   * Bit 12..8: Shift Value if should be shifted.
   *
   * Bit 15..13 using for trigger.
   * Bit 31..17: Offset
   */
  int conversion;

  public static final int mTriggerRising_EntryCtrl_Capture_Inspc = 0x2000;
  public static final int mTriggerFalling_EntryCtrl_Capture_Inspc = 0x4000;



  /**Factor if it should be scaled. It is the factor which maps the value to 1 in integer. 
   * The range for 16-bit-mapping is about factor * 32000.
   */
  float factor;
  
  /**Null-value for shifting range of value. This value is subtract before a value will be factored.
   * Example: A normal value is 2500, and the technology range to visit it is 2000...3000.
   * To get a fine soluted information, set the zero-line to 2500 and the factor to 50. 
   * Than the value is mapping to a solution of 0.02 to visit it.
   */
  float zeroLine;

  /**A trigger value. The recording may be started if the signal is crossed this value.*/
  float triggerValue;
  
  /**The number of values of buffer. The number of bytes is depending of the type. */
  int sizeBuffer;

  /**The use-able size is up to 4095 words. */
  public static final int mSizeBuffer_Capture_Inspc = 0x00000FFF;

  /**The size for pre-trigger range. It is the higher 16-bit-word. */
  public static final int mSizePreTrigger_Capture_Inspc = 0x0FFF0000;

  /**current index in the buffer. */
  int ixBuffer;

  /**Some state bits. */
  int bitState;

  public static final int mWaitTrigger_Capture_Inspc = 0x0008;
  
  /**The  buffer. If this reference is set, the conversion info have to be converted to int16. */
  short[] bufferInt;

  /**The  buffer. If this reference is set, the conversion info have to be converted to int16,
   * But 2 values are be united in a 32-bit-value. It is a special case for Digital Signal Processors. */
  int[] bufferInt32;

  /**The  buffer. If this reference is set, the conversion info have to be converted to float. */
  float[] bufferFloat;

  
  
  @Override
  public float getFloatInspcData(int addr)
  {
    switch(addr){
      case 1: return factor;
    }
    return 0.0F;
  }

  @Override
  public int getIntInspcData(int addr)
  { int value;
    String element = inspcClassData.names[addr];
    if     (element.equals("addr"))         return addr;
    else if(element.equals("maskValue"))    return maskValue;
    else if(element.equals("conversion"))   return conversion;
    else if(element.equals("factor"))       return (int)factor;
    else if(element.equals("zeroLine"))     return (int)zeroLine;
    else if(element.equals("triggerValue")) return (int)triggerValue;
    else if(element.equals("sizeBuffer"))   return sizeBuffer;
    else if(element.equals("ixBuffer"))     return ixBuffer;
    else if(element.equals("bitState"))     return bitState;
    else return -1;
    /*
    try{
      value = inspcClassData.fields[addr].getInt(this);
    } catch(IllegalAccessException exc){
      
    }
    switch(addr){
      case 1: return addr;
      default: return 0;
    }
    */
  }

  
  public int getAddr(Field element)
  {
    String sElement = element.getName();
    if(element.equals("addr")) return 1;
    else if(element.equals("addr")) return 1;
    else if(element.equals("maskValue")) return 1;
    else if(element.equals("conversion")) return 1;
    else if(element.equals("factor")) return 1;
    else if(element.equals("zeroLine")) return 1;
    else if(element.equals("triggerValue")) return 1;
    else if(element.equals("sizeBuffer")) return 1;
    else if(element.equals("ixBuffer")) return 1;
    else if(element.equals("bitState")) return 1;
    else if(element.equals("addr")) return 1;
    else return -1;
  }
  
  
  
}
