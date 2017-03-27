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

/**This is a helper class to return a peace of c-code with associated type information.
 * 
 */
public class CCodeData
{
  /**The generated code snippet for this expression. 
   * This information is not important for further using, only as return string in gen_-routines.
   * Therefore it may be deleted using removeCode();
   */
  String cCode;
  
  
  /**If true, than the cCode represents a setting of a temporary reference. 
   * The reference may be used as reference. It is the last used temporary reference.
   * The result code should be consist of the setting of the temporary reference, with following
   * comma separator of expressions. This strategy is used to disentangle the nesting of method calls
   * in paramter for concatenated calls in Java.
   * 
   */
  String sTempRef;
  //boolean bUseTempRef;
  
  
  /**If true, than the cCode represents a method call, which returned the same reference
   * as used for call, it means the method returns this. This information can be used for concatenation,
   * the reference needn't be taken from the return value. 
   */
  //final boolean bReturnThis;
  private final char returnMode;
  
  boolean isReturnThis(){ return returnMode == 't'; }
  boolean isReturnNew(){ return returnMode == 'n'; }
  
  char getReturnMode(){ return returnMode; }
  
  
  /**The access mode may be changed in comparison with identInfos, if a array element is accessed
   * in this code snippet. This information should be used for evaluating the kind of code snippet. 
   */
  char modeAccess;
  
  /**This is the identInfo of the basicly used variable or value type {@link ClassData#classTypeInfo}.
   * Therefore the information about the type of the code snipped maybe alternating,
   * <ul><li>at ex. <code>var</code> is an array variable with its identinfo here,
   *         but the code snipped contains var[2], therefore the {@link LocalIdents.IdentInfos#dimensionArrayOrFixSize}
   *         doesnot match to the codesnipped. See {@link #dimensionArrayOrFixSize} of this class.
   *     <li>at ex. <code>{ 1, 2, 3}</code> is a result of a &lt;constArray> in Java2C.zbnf,
   *         but its identInfo here is of the basicly type of the elements.     
   */
  final FieldData identInfo;
  
  /** 0 if the value in the code snipped is scalar, 1.. if it is an array. 
   * The dimensionArrayOrFixSize may be changed in comparison with identInfos, if a array element is accessed
   * in this code snippet. This information should be used for evaluating the kind of code snippet.
   */
  final int dimensionArrayOrFixSize;
  
  /**Initializes the detail information {@link #modeAccess} and {@link #dimensionArrayOrFixSize}
   * with the same values as identInfo.
   * @param cCode code snipped
   * @param identInfo the type and access infos to the code snipped.
   */
  CCodeData(String cCode, FieldData identInfo)
  { this(cCode, identInfo, identInfo.modeAccess);
  }
  
  /**Initializes the detail information {@link #modeAccess} and {@link #dimensionArrayOrFixSize}
   * with the same values as identInfo.
   * @param cCode code snipped
   * @param identInfo the type and access infos to the code snipped.
   */
  CCodeData(String cCode, FieldData identInfo, char modeAccess)
  { this(cCode, identInfo, modeAccess
        , identInfo.modeArrayElement=='B'  ? identInfo.getFixSizeStringBuffer(): identInfo.getDimensionArray()
        , '.');
  }
  
  
  /**Initializes the detail information {@link #modeAccess} and {@link #dimensionArrayOrFixSize}
   * with the same values as identInfo.
   * @param cCode code snipped
   * @param identInfo the type and access infos to the code snipped.
   */
  CCodeData(String cCode, FieldData identInfo, char modeAccess, char returnMode) //boolean bReturnThis)
  { this(cCode, identInfo, modeAccess
        , identInfo.modeArrayElement=='B'  ? identInfo.getFixSizeStringBuffer(): identInfo.getDimensionArray()
        , returnMode);
            //, bReturnThis);
  }
  
  
  /**Initializes the detail information {@link #modeAccess} and {@link #dimensionArrayOrFixSize}
   * in a special kind.
   * @param cCode code snipped
   * @param identInfo the type and access infos to the code snipped.
   * @param modeAccess
   * @param dimensionArrayOrFixSize
   */
  CCodeData(String cCode, FieldData identInfo, char modeAccess, int dimensionArray )
  { this(cCode, identInfo, modeAccess, dimensionArray, '.');
  }
  
  
  /**Initializes the detail information {@link #modeAccess} and {@link #dimensionArrayOrFixSize}
   * in a special kind.
   * @param cCode code snipped
   * @param identInfo the type and access infos to the code snipped.
   * @param modeAccess
   * @param dimensionArrayOrFixSize
   */
  CCodeData(String cCode, FieldData identInfo, char modeAccess, int dimensionArray, char returnMode) //boolean bReturnThis )
  { //super(cCode, identInfo == null ? null : identInfo.typeClazz);
    this.cCode = cCode; 
    //this.type = identInfo == null ? null : identInfo.typeClazz; 
    this.modeAccess = modeAccess; 
    this.dimensionArrayOrFixSize = (byte)dimensionArray; 
    this.identInfo = identInfo;
    this.returnMode = returnMode;
    //this.bReturnThis = bReturnThis;
  }
  
  
  /**Tests and generates a cast if a cast to the destination form is necessary.
   * <br>
   * If it is a static array, a <code>{ CONST_ObjectJc(...), fixArraySize, 0, cCode }</code> is generated
   * to initialize a static array.
   * <br>
   * Otherwise {@link ClassData#testAndcast(ClassData, String, char)} is called 
   * for the given {@link FieldData#typeClazz} from parameter <code>dstIdentInfo</code>
   *  
   * @param dstIdentInfo The type of necessity appearance of the code snipped, 
   *        enclosing the representation of the type. 
   * @param intension see {@link ClassData#testAndcast(ClassData, String, char)}.
   * @return The original {@link cCode} or the casted form.
   */
  private String xxxtestAndCast(FieldData dstIdentInfo, char intension)
  { String ret = "";
    if("Ss".indexOf(dstIdentInfo.modeStatic) >=0)
    { if(dimensionArrayOrFixSize ==1 && modeAccess == '%' && dstIdentInfo.modeAccess == '$')
      { //given is a simple immediate array, but requested is a embedded array struct:
        ret = "{ CONST_ObjectJc(0, &" + dstIdentInfo.getTypeName() + "_" + dstIdentInfo.getTypeName() + ", null), " 
            + dstIdentInfo.fixArraySizes[0] 
            + ", sizeof(" + dstIdentInfo.getTypeName() + "), 0, " 
            + cCode + "}";  
        
      }
      else ret = dstIdentInfo.typeClazz.xxxtestAndcast(identInfo.typeClazz, cCode, intension); //no casting      
    }
    else
    {
      ret = dstIdentInfo.typeClazz.xxxtestAndcast(identInfo.typeClazz, cCode, intension);
    }
    return ret;
  }
  
  /**Returns the String representation of the type of the code snipped, 
   * detected from the {@link #identInfo} and its {@link FieldData#typeClazz}. */
  public String getTypeName(){ return identInfo.typeClazz.getClassIdentName(); }
  
  
  /**Returns all class level idents, if the code snipped is a reference of a class type.
   * Especially it able to use, if the code snipped is a reference to a method or field
   * of another class.
   */
  public LocalIdents getClassLevelIdents()
  { return identInfo.typeClazz.classLevelIdents;
  }

  /**Returns the name of the Headerfile, which defines the class, 
   * if the code snipped is a reference of a class type.
   * Especially it able to use, if the code snipped is a reference to a method or field
   * of another class.
   */
  public String getTypeHeaderfilename()
  { return identInfo.typeClazz.sFileName;
  }
  
  
  public String toString()
  { return cCode + ":" + modeAccess + identInfo.toString(); }
  
}

