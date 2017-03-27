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

import java.util.List;

/**This class holds the data of one variable in a local context respectively a field of a class, 
 * see {@link LocalIdents} 
 * or describe the access to a instance, see {@link CCodeData}.
 */
public class FieldData
{
  /**The name of the identifier. <code>null</code> if it is the description of an access to an unnamed instance. */
  private final String sName;
  
  /**The type respectively reference type of the field. */
  public final ClassData typeClazz;
  
  /**The type of a key if it is a generic container type of keys and values. Elsewhere null. */
  public ClassData keyClazz;
  
  /**The type of a elements of a generic container type. Elsewhere null. */
  public ClassData elementClazz;
  
  /**If this field is set, the instance referenced in the described field is guaranteed an instance
   * of that type, nor from a derived type. It means, no dynamic linked call is necessary,
   * methods can called static using this instance type. This property saves some calculation times,
   * while calling override-able methods of the referenced class.
   * <br>
   * If references are used as interface references for Java test and software design (including
   * the documentation aspect), but the implementation uses the named instance in any case, 
   * this feature should be used.
   * <br>
   * For a reference variable this behavior is possible to control with @ java2c=instanceType:TYPE.
   * This behavior is also set at embedded instance automatically. 
   * This field is null, if the instance isn't predefined.
   */
  public final ClassData instanceClazz;
  
  /**The class where the identifier is member of, null if it is a local identifier. */
  public final ClassData declaringClazz;
  
  /**Char to indicate the location and kind of definition.
   * <ul><li>'S' static constant
   *     <li>'s' static non constant
   *     <li>'d' immediate constant with define
   *     <li>'n' stack-locally Field which stores a reference to a new created instance, to activate for garbage collection.
   *     <li>'r' nonPersistent.
   *     <li>'t' not used, TODO used 'r' String, which may be non persistent. Maybe return value with buffer in Thread-Context.
   *     <li>'.' non static, no special functionality.
   * </ul>     
   */
  public final char modeStatic;
  
  /**If the identifier is an array with fix size, programmed in java in form of
   * <pre>
   * Type[][] myArray = new Type[expr][expr];
   * </pre>
   * The <code>expr</code> translated in C-Form are stored here. This expr should be a simple value
   * in C. There are used to generate the initialization of an array.
   * <br>
   * <code>null</code> if it isn't an array or it is a dynamic array.
   */
  public final String[] fixArraySizes;
  
  /**0 if it isn't an array. 1.. if it is an array. 
   * if modeArrayElement=='B' it is the fix size of a StringBuffer with direct char*.
   */
  private final int dimensionArrayOrFixSize;
  
  
  /**Kind of the access to an element, if it is an array.
   * The adequate chars are used see {@link #modeAccess}.
   * Special case: If it is 'B', the field describes a <code>StringBufferJc</code> with a fix size.
   * In that case the {@link #dimensionArrayOrFixSize} contains the number of chars inclusive the 4 chars
   * storing in the <code>StringBufferJc</code>-instance with immediate buffer.
   */
  public final char modeArrayElement;
  
  /**Kind of the instance. For non-arrays:
   * <table>
   * <tr><td>$</td><td>an embedded struct</td></tr>
   * <tr><td>%</td><td>a variable of basic primitive type such as int32, float, char,
   *                   but also an embedded indivisible struct like StringJc, MemC, OS_TimeStamp,
   *                   whose elements aren't accessed in Java. </td></tr>
   * <tr><td>*</td><td>a simple reference, pointer in C, used for non-GC-controlled associations.</td></tr>
   * <tr><td>@</td><td>an enhanced reference, for GC (garbage collector) -controlled associations.</td></tr>
   * <tr><td>m</td><td>an method table-reference.</td></tr>
   * <tr><td>t</td><td>a StringJc, it is an enhanced reference ~REF also, but with special accesses</td></tr>
   * <tr><td>&</td><td>an method-table reference ~MTB, containing C-jump-table with index.
   *                   This type is only used for stack-local variables. GC is not possible</td></tr>
   * <tr><td>~</td><td>ythis as reference (call own method</td></tr>
   * <tr><td>+</td><td>A variable argument list</td></tr>
   * </table>
   * if {@link #dimensionArrayOrFixSize} >0, this element describes the access to the array head. 
   * The kind of elements respectively the access to the data of the elements are described in {@link #modeArrayElement}.
   * with the same key chars as shown above. Independent of the kind of referencing the array
   * the elements may be embedded struct ('$'), references ('*') etc., see {@link #modeArrayElement}.
   * <table>
   * <tr><td>B</td><td>A ByteStringJc. It is similar as StringJc, but it references bytes, not chars.
   *                   In Java it is a byte[] with java2c=ByteStringJc-designation.
   *                   Hint: A ByteStringJc is designated in this class with {@link #dimensionArrayOrFixSize}=1.
   *                   It is an array with 1 dimension. 
   *                   It is similar MemC (pointer to memory and size). </td></tr>
   * <tr><td>P</td><td>A simple reference to the array elements without head, pointer in C, 
   *                   used for non-GC-controlled associations.</td></tr>
   * <tr><td>Q</td><td>An embedded array without head. All elements are embedded. 
   *                   The {@link #fixArraySize} is set.</td></tr>
   * <tr><td>X</td><td>A simple reference to <code>ObjectArrayJc</code> head informations, pointer in C, to an array type, used for non-GC-controlled associations.</td></tr>
   * <tr><td>Y</td><td>An embedded array with <code>ObjectArrayJc</code> head informations. 
   *                   All elements are embedded. The {@link #fixArraySize} is set.</td></tr>
   * <tr><td>@</td><td>an enhanced reference to an array type, starting with ObjectArrayJc, 
   *                   for GC (garbage collector) -controlled associations.</td></tr>
   * </table>
   */
  public final char modeAccess;
  
  /**see {@link getClassLevel()}*/
  int nClassLevel;

  /**see {@link getOuterLevel()}. Note: this private member is set in the outer class, 
   * see {@link putClassElement(String, String, ClassData, String, ClassData)}
   * or  {@link putLocalElement(String, String, ClassData, String)} 
   */
  int nOuterLevel;
  
  /**Name of an associated StringBuilder. 
   * It is relevant only if the field is a StringJc-variable and it is temporary. */
  String sStringBuilderName;
  
  /**initializes the instance node. */
  FieldData
  ( String sName
  , ClassData typeClazz
  , ClassData instanceClass
  , ClassData elementClass
  , ClassData keyClass
  , char staticMode
  , char accessMode
  , int dimensionArray
  , String[] fixArraySizes
  , char modeArrayElement
  , ClassData declaringClazz
  )
  { this.sName = sName;
    if(sName !=null && sName.equals("timeTest1"))
    	stop();
    if(instanceClass!=null && instanceClass.sClassNameJava.equals("AnswerComm_ifc"))
    	stop();
	  assert(".?%*~$&@mXYPQt+B".indexOf(accessMode)>=0);
    this.modeAccess = accessMode;
    //this.sModifier = sModifier; 
    this.fixArraySizes = fixArraySizes; 
    this.declaringClazz = declaringClazz; 
    if(typeClazz==null) throw new IllegalArgumentException("typeClazz = null for:" + sName);
    
    this.typeClazz = typeClazz;
    this.instanceClazz = instanceClass;
    this.elementClazz = elementClass;
    this.keyClazz = keyClass;
    this.dimensionArrayOrFixSize = dimensionArray;  //NOTE: if modeArrayElement=='B' it is the fix size of a StringBuffer
    //assert(dimensionArray == 0 || dimensionArray == 1);
    if(modeArrayElement == '$')
      stop();
    assert(".%t*$&@B".indexOf(modeArrayElement)>=0);
    this.modeArrayElement = modeArrayElement;
    assert("sSdrn.".indexOf(staticMode)>=0);
    this.modeStatic = staticMode;
  }
  
  
  
  

  /**Copies given FieldData, but modify with given parameters
   * @param parent //the parent
   * @param classLevelAdd Adds the class level, 0: keep from parent. To copy instances for super classes
   * @param outerLevelAdd Adds the outer level, 0: keep from parent. To copy instances for inner classes
   * @param modeAccessP if '.' than keep parent.modeAccess, else take that.
   */
  FieldData(FieldData parent, int classLevelAdd, int outerLevelAdd, char modeAccessP, char modeStaticP)
  { this.sName = parent.sName;
    if(sName.equals("searchTrc") )//&& outerLevelAdd == 1)
    	stop();
  	this.modeAccess = modeAccessP == '.' ? parent.modeAccess : modeAccessP;
    this.declaringClazz = parent.declaringClazz; 
    this.typeClazz = parent.typeClazz;
    this.instanceClazz = parent.instanceClazz;
    this.keyClazz = parent.keyClazz;
    this.elementClazz = parent.elementClazz;
    this.dimensionArrayOrFixSize = parent.dimensionArrayOrFixSize; 
    this.fixArraySizes = parent.fixArraySizes;
    this.modeArrayElement = parent.modeArrayElement;
    this.modeStatic = modeStaticP=='.' ? parent.modeStatic: modeStaticP;
    this.nClassLevel = parent.nClassLevel + classLevelAdd;
    this.nOuterLevel = parent.nOuterLevel + outerLevelAdd;
  }
  
  
  /*
  FieldData(FieldData parent, int dimensionArrayOrFixSize)
  { //this.sTypeIdent = parent.sTypeIdent; 
    this.sName = parent.sName;
    this.modeAccess = parent.modeAccess;
    //this.sModifier = sModifier; 
    this.fixArraySizes = parent.fixArraySizes; 
    this.clazz = parent.clazz; 
    this.typeClazz = parent.typeClazz;
    this.dimensionArray = (byte)dimensionArrayOrFixSize; 
    this.modeArrayElement = parent.modeArrayElement;
    this.modeStatic = parent.modeStatic;
  }
  */
  
  /**level of the affiliation to a class in the inheritance level.
   * 0= it is a local visible variable. 1=element of this class, 2=element of super class etc.
   */
  public int getClassLevel(){ return nClassLevel; }
  
  
  /**Returns 0 if it isn't an array, 1.. for number of array dimension.*/
  public int getDimensionArray(){ return ".B".indexOf(modeArrayElement) <0 ? dimensionArrayOrFixSize : 0; }

  
  /**Returns the size >0 if the type is a StringBuffer with a immediate String with fix size, else 0.
   */
  public int getFixSizeStringBuffer(){return modeArrayElement=='B'? dimensionArrayOrFixSize : 0; }
  
  /**level of the affiliation to a class in the outer class relation.
   * 0= it is a local visible variable. 1=element of this class, 2=element of outer class etc.
   */
  public int getOuterLevel(){ return nOuterLevel; }
  
  /**returns the name of the element. */
  public String getName(){ return sName; }

  /**returns the name of the type of the element. */
  public String getTypeName(){ return typeClazz.getClassCtype_s(); }
  
  public String getTypeChar()
  { return "" + typeClazz.cVaArgIdent;
  }
  
 
  
  
  /**Generates the C type declaration appropriate to the type info in this FieldData.
   * It is the type code used for definition of the variable.
   * <ul>
   * <li>Simple embedded Arrays: At example type int. It returns only <code>int</code> because
   *     the complete definition is <code>int name[123]</code> or <code>int name[] = { 1, 2, 3}</code> 
   * </ul>
   * <br>
   * If it is an simple array, the type code designating the pointer to the elements requires an additional '*'
   * @return String with C type declaration.
   */
  public String gen_Declaration()
  { final String sPreModifier;
    if(sName != null && sName.equals("int32")) 
      stop();
    if(modeStatic == 'S') {
      sPreModifier = "const ";
    } else {
      sPreModifier = ""; 
    }
    final String sTypeReturn; //  .sTypeIdent;
    switch(modeAccess)
    { case 'B':
    	case '$':
      case 't':
      case '%': sTypeReturn = typeClazz.getClassCtype_s(); break;
      case 'C':
      case '*': sTypeReturn = "struct " + typeClazz.getClassIdentName() 
                            + (typeClazz.isConstant ? "_t const*":"_t*"); break;
      case '@': sTypeReturn = typeClazz.getClassIdentName() + "REF"; break;
      case '&': sTypeReturn = typeClazz.getClassIdentName() + "MTB"; break;
      case '+': sTypeReturn = "char const*"; break;
      case 'X': //reference to an ObjectArrayJc, reference or not of elements in modeArrayElement 
      	sTypeReturn = typeClazz.getClassIdentName() + (typeClazz.isConstant ? "_Y const*":"_Y*"); break;
      case 'Y': //instance of an ObjectArrayJc, reference or not of elements in modeArrayElement
      	sTypeReturn = typeClazz.getClassIdentName() + "_Y"; break;
      case 'P': 
      { switch(modeArrayElement)
        { case '$':
          case 't':
          case '%': sTypeReturn = typeClazz.getClassCtype_s() + "*"; ; break;
          case '*': sTypeReturn = "struct " + typeClazz.getClassIdentName() 
                                + (typeClazz.isConstant ? "_t const**":"_t**"); break;
          case '@': sTypeReturn = typeClazz.getClassIdentName() + "REF*"; break;
          default: sTypeReturn = null;
        }  
      } break;
      case 'Q': 
      { /**embedded array. to use in declaration, at ex. int name[] is produced, here only int.*/
        switch(modeArrayElement)
        { case '$':
          case 't':
          case '%': sTypeReturn = typeClazz.getClassCtype_s() + ""; ; break;
          case '*': sTypeReturn = "struct " + typeClazz.getClassIdentName() 
                                + (typeClazz.isConstant ? "_t const*":"_t*"); break;
          case '@': sTypeReturn = typeClazz.getClassIdentName() + "REF"; break;
          default: sTypeReturn = null;
        }  
      } break;
      default: sTypeReturn = null;
    }
    assert(sTypeReturn != null);
    return sPreModifier + sTypeReturn;
    
  }

  /**generates the code for a variable definition of the given FieldInfo.
   * @return code for definition of the variable, without ";" on end.
   * This method is only called inside {@link GenerateClass#gen_variableDefinition(org.vishia.zbnf.ZbnfParseResultItem, org.vishia.zbnf.ZbnfParseResultItem, LocalIdents, java.util.List, char)}
   * It supplies all informations which are able to gotten in this class.
   */
  String gen_VariableDefinition(char intension)
  {
    if(sName.equals("clazz"))
      stop();
    
    final String sNameC = "Ssd".indexOf(modeStatic)>=0 
                        ? sName + "_" + declaringClazz.getClassIdentName()
                        : sName;  //static variable with _Classname
    String typeCode = gen_Declaration();
    if(sName.equals("singleton"))
      stop();
    String ret = "???";
    if(dimensionArrayOrFixSize >0)
    { final String elementType;
      switch(modeArrayElement)
      { case '%': case 't': case '$': elementType = typeClazz.getClassCtype_s(); break;
        case '*': elementType = "struct " + typeClazz.getClassIdentName() 
                  + (typeClazz.isConstant ? "_t const*":"_t*"); break;
        case '@': elementType = typeClazz.getClassIdentName() + "REF"; break;
        case '&': elementType = typeClazz.getClassIdentName() + "MTB"; break;
        case 'B': elementType =null; assert(modeAccess == '$'); break;
        default: elementType =null; assert(false);
      }
      switch(modeAccess){
      	case 'B': {
      		ret = "ByteStringJc " + sNameC;
      	} break;
      	case 'Y':
        { /**A fix size embedded array, but with head: */
          ret = "struct " + sNameC + "_Y {"
                 + " ObjectArrayJc head; " + elementType + " data[" + fixArraySizes[0] + "]; }"
                 + sNameC;  //NOTE: the ";" after definition is set outside.
        }break;
        case 'Q':
        { /**An simple fix embedded array: */
          if(fixArraySizes != null)
          { ret = typeCode + " " + sNameC;
            for(String fixArraySize: fixArraySizes)
            { ret += "[" + fixArraySize + "]"; 
            }
          }
          else assert(false);  //it have to be fixArraySizes!    
        }break;
        case 'X':
        { ret = typeCode + " " + sNameC;
          //TODO arrays with more as one dimension.
        } break;
        case '$':
        {
          assert(modeArrayElement == 'B');
          { /**Its a fix String buffer. */
            ret = "struct SbY_" + sNameC + "_t {"
                 + " StringBufferJc sb; char _b[" + (dimensionArrayOrFixSize-4) + "]; }"
                 + sNameC;  //NOTE: the ";" after definition is set outside.
            
          }
          
        }break;
        case 'p':
        case 'P':{
        	ret = elementType+ "* " + sNameC;
        } break;
        default: assert(false);  
      }
    }
    else {
      if(modeStatic == 'd'){
        ret = "#define " + sNameC + " " + fixArraySizes[0];    
      }
      else {
        ret = typeCode + " " + sNameC;
        if(modeAccess == '+'){
        	ret += ", ...";
        }
      }
    }
    return ret;  
  }

  /**Writes the structure of this instance to a <code>*.stc</code>-file. 
   * It is an class level identifier, see {@link ClassData#classLevelIdents}, 
   * or a return type or argument of a method,
   * because only for such FieldData this method is called. 
   * See {@link LocalIdents#writeStruct(int)} and {@link ClassData.Method#writeStruct()}
   * @return The content to write to <code>*.stc</code>-file. The form of string is the same
   * described in the syntax <code>Java2Cstc.zbnf</code>.
   */
  public String xxxwriteStruct() 
  { 
    if(typeClazz == null)
      assert(false);
    if(typeClazz.sClassNameJava.equals("InnerTest"))
      stop();
    
    
    
    String out = "field:{ ";

    //out += typeClazz.sPackage;
    ClassData outerClazz = typeClazz; //start with it.
    while( (outerClazz = outerClazz.outerClazz) != null)
    { //insert left:
      //out += outerClazz.sClassNameJava + "." ;
      stop();
    }
    //out += typeClazz.sClassNameJava;
    out += typeClazz.sClassNameJavaFullqualified;
    
    if(keyClazz != null){
      out += "<" + keyClazz.sClassNameJavaFullqualified + "," + elementClazz.sClassNameJavaFullqualified + ">";
    }
    else if(elementClazz != null){
      out += "<" + elementClazz.sClassNameJavaFullqualified + ">";
    }
    
    if(instanceClazz != null){
      out += " (instance=" + instanceClazz.sClassNameJavaFullqualified + ")";
    }
    out += " "
          + modeAccess 
          + modeArrayElement 
          + modeStatic;
    assert(typeClazz.sPackage.length() == 0 || typeClazz.sPackage.endsWith("/"));
    if(modeArrayElement == 'B'){
      out += "-fixBufferSize[" + dimensionArrayOrFixSize + "]"; 
    }
    else {
      /**Array may be with fix size.Either show [??] per size or the size. */
      for(int ii=0; ii<dimensionArrayOrFixSize; ii++)
      { final String sFixArraySize = 
          fixArraySizes == null ? "??"
          : fixArraySizes[ii] == null ? "?"
          : fixArraySizes[ii];
        out += "[" + sFixArraySize + "]";
      }
    }  
    out += " "+ sName + "; ";
    out += "/*info"
        + dimensionArrayOrFixSize
        + nClassLevel + nOuterLevel
        + "*/ }";
    //if()
    return out;
  }    
          
  
  /**Writes the structure of this instance to a <code>*.stc</code>-file. 
   * It is an class level identifier, see {@link ClassData#classLevelIdents}, 
   * or a return type or argument of a method,
   * because only for such FieldData this method is called. 
   * See {@link LocalIdents#writeStruct(int)} and {@link ClassData.Method#writeStruct()}
   * @return The content to write to <code>*.stc</code>-file. The form of string is the same
   * described in the syntax <code>Java2Cstc.zbnf</code>.
   */
  public String writeStruct() 
  { 
    StringBuilder out = new StringBuilder(30);
  	if(typeClazz == null)
      assert(false);
    ClassData outerClazz = typeClazz; //start with it.
    while( (outerClazz = outerClazz.outerClazz) != null)
    { //insert left:
      //out += outerClazz.sClassNameJava + "." ;
      stop();
    }
    //out += typeClazz.sClassNameJava;
    out.append(typeClazz.sClassNameJavaFullqualified);
    
    if(keyClazz != null){
      out.append("<" + keyClazz.sClassNameJavaFullqualified + "," + elementClazz.sClassNameJavaFullqualified + ">");
    }
    else if(elementClazz != null){
      out.append("<" + elementClazz.sClassNameJavaFullqualified + ">");
    }
    if(modeArrayElement != 'B'){
      /**Array may be with fix size.Either show [??] per size or the size. */
      for(int ii=0; ii<dimensionArrayOrFixSize; ii++)
      { final String sFixArraySize = 
          fixArraySizes == null ? "??"
          : fixArraySizes[ii] == null ? "?"
          : fixArraySizes[ii];
        out.append( "[" + sFixArraySize + "]");
      }
    }  
    //additional informations:
    String sSeparator = "(";
    if(instanceClazz != null){
      out.append(sSeparator).append( "instance=").append(instanceClazz.sClassNameJavaFullqualified);
      sSeparator = ", ";
    }
    if(modeArrayElement == 'B'){
    	out.append(sSeparator).append("fixBufferSize=").append(dimensionArrayOrFixSize ); 
    }
    if(!sSeparator.equals("(")){ out.append(")");} //any content
    
    out.append( " ")
       .append( modeAccess) 
       .append( modeArrayElement) 
       .append( modeStatic);
    	 //.append(" c").append(nClassLevel)
 	     //.append(" o").append(nOuterLevel);
   assert(typeClazz.sPackage.length() == 0 || typeClazz.sPackage.endsWith("/"));
    out.append( " ").append(sName);
    return out.toString();
  }    
          
  
  /**Tests whether a cast to the given FieldData type is necessary and executes it.
   * The cast consists of two parts:
   * <ul><li>cast of type, for example to a base class type or another cast-able type
   * <li>cast of access, for example from an enhanced reference to the reference
   * </ul>
   * If srcType is the same as this, no cast occurs, the value is returned direct.
   * If srcType is a derive-able type of this, the cast is get with TODO.
   * <br><br>
   * The cast of access is gotten calling {@link #testAndChangeAccess(char, String, char)}.
   * @param srcType The type of the value
   * @param value The value string.
   * @return The value itself if no cast is necessary
   * @param src The Expression with its type and access modes
   * @param modeAccessDst A deviating mode of access for the destination or '.'. If '.', the {@link #modeAcess} 
   *        of this is valid.
   * @return String, either src.cCode if no cast is necessary, or the casted expression.
   */
  public String testAndcast(CCodeData src, char modeAccessDst)
  { if(src.cCode.equals("REFJc(ythis->implifc2)"))
      stop();
    
    final String sRet;
    if(modeAccessDst == '.'){ modeAccessDst = this.modeAccess; }
    assert(modeAccessDst != '&' || src.modeAccess == '&'); //no access conversion to Type_MTB
    if(false && modeAccessDst == '&'){  //it doesn't work anytime.
    	/**A method-table-reference is need: */
    	/**TypeMTBL is need. */
    	final String sSrc2;
    	switch(src.modeAccess){
    	case '*': sSrc2 = src.cCode; break;
    	case '$': sSrc2 = "&(" + src.cCode + ")"; break;
    	default: sSrc2 = "XXX"; assert(false);
    	}
    	sRet = "(" + typeClazz.getClassIdentName() + "MTB)mtblRef_ObjectJc(&(" + sSrc2 
    	     + ")->base.object, sign_Mtbl_" +  typeClazz.getClassIdentName() + ")";
    } else {
	    if( src.cCode.equals("null"))
	    { //a null-pointer should be compatible with all expect StringJc.
	    	if(typeClazz.bEmbedded && dimensionArrayOrFixSize == 0) {
	      	//Any embedded type should have a null_Name-instance, that content is assigned.
	      	sRet = "null_" + typeClazz.getClassIdentName();
	      } else if(typeClazz.sClassNameC.equals("StringJc")
	        && dimensionArrayOrFixSize == 0 //if >0, it is a String[] referenced.
	        ) {
	      	assert(false);  //condition above is met.
	        sRet = "null_StringJc"; 
	      } else if('B' == modeAccess){
	      	//a Byte-String represents a byte[], dimensionArrayOrFixSize =1 typical, not tested.
	      	//extension for future: use 'B' for more direct referenced types of arrays,
	      	//adequate to a MemC.
	      	sRet = "null_OS_PtrValue";
	      }else  {
	        sRet = "null"; 
	      } 
	    }
	    else {
	      
	      if(src.cCode.startsWith("requireWay1Sensor_iRequireMainController(broker, _thCxt)"))
	        stop();
	      
	      
	      ClassData.CastInfo castInfo;
	      if(  this.typeClazz == src.identInfo.typeClazz
	        || src.identInfo.typeClazz == CRuntimeJavalikeClassData.clazz_unknown  //unknown type, don't cast, it is okay:
	        || src.modeAccess == '?'                       //unknown type, don't cast, it is okay:
	        || this.typeClazz == CRuntimeJavalikeClassData.clazz_unknown  //unknown type, don't cast, it is okay:
	        || this.typeClazz == CRuntimeJavalikeClassData.clazz_void  //void as argument is a void*
	        || this.typeClazz == CRuntimeJavalikeClassData.clazz_va_argRaw  //any type is valid
	        ) {
	        sRet = testAndChangeAccess(modeAccessDst, src.cCode, src.modeAccess);
	      }
	      else if( (castInfo = src.identInfo.typeClazz.getCastInfoToType(typeClazz.sClassNameC)) != null
	             ||(castInfo = typeClazz.getCastInfoFromType(src.identInfo.typeClazz.sClassNameC)) != null
	             ){
	        /**cast src to this type: */
	        String sSrcCasted = testAndChangeAccess(castInfo.modeAccessSrc, src.cCode, src.modeAccess);
	        String sSrc1 = castInfo.pre + sSrcCasted + castInfo.post;
	        sRet = testAndChangeAccess(modeAccessDst, sSrc1, castInfo.modeAccessDst);
	      }
	      else if(src.modeAccess =='X' && this.typeClazz==CRuntimeJavalikeClassData.clazz_MemC){
	        /**Cast of a array to MemC: */
	        sRet = testAndChangeAccess(modeAccessDst, "buildFromArrayX_MemC(&(" + src.cCode + ")->head) ", '$');
	      }
	      else if(src.modeAccess =='Y' && this.typeClazz==CRuntimeJavalikeClassData.clazz_MemC){
	        /**Cast of a array to MemC: */
	        sRet = testAndChangeAccess(modeAccessDst, "buildFromArrayY_MemC(&(" + src.cCode + ")->head) ", '$');
	      }
	      else if(src.modeAccess =='X' && this.typeClazz==CRuntimeJavalikeClassData.clazzObjectJc){
	        /**Cast of a array to Object reference: */
	        sRet = testAndChangeAccess(modeAccessDst, "(" + src.cCode + ")->head.object", '$');
	      }
	      else if(src.modeAccess =='Y' && this.typeClazz==CRuntimeJavalikeClassData.clazzObjectJc){
	        /**Cast of a array to Object reference: */
	        sRet = testAndChangeAccess(modeAccessDst, "(" + src.cCode + ").head.object", '$');
	      }
	      else {
	        /**No castInfo, but the types are different. It is a derivation. */
	        String sClassNameDst = typeClazz.sClassNameC;
	        if(typeClazz.isPrimitiveType()){
	          String sSrcCasted = testAndChangeAccess('%', src.cCode, src.modeAccess);
	          sRet = "((/*J2C:cast% from "+src.identInfo.getTypeName()+"*/" + sClassNameDst + ")(" + sSrcCasted + "))";
	        } 
	        else if("t$".indexOf(typeClazz.classTypeInfo.modeAccess) >=0){
	          /**The dst should be a value (embedded). */
	          String sSrcCasted = testAndChangeAccess('$', src.cCode, src.modeAccess);
	          sRet = "((/*J2C:cast$ from "+src.identInfo.getTypeName()+"*/" + sClassNameDst + ")(" + sSrcCasted + "))";
	        } 
	        else {
	          /**Normal pointer cast. */
	          String sSrcCasted = testAndChangeAccess('*', src.cCode, src.modeAccess);
	          sRet = "((/*J2C:cast from "+src.identInfo.getTypeName()+"*/" + sClassNameDst + "*)(" + sSrcCasted + "))";
	        }
	      }
	    }
    }
    return sRet;
    
  }

  
  
  String testAndCastInitializer(CCodeData src)
  { String sRet;
    if( src.cCode.equals("null"))
    { //a null-pointer should be compatible with all expect StringJc.
      if(typeClazz.sClassNameC.equals("StringJc")
        && dimensionArrayOrFixSize == 0
        )
      { 
        sRet = "NULL_StringJc"; }
      else {
        sRet = "null"; } 
    }
    else {
      if(src.cCode.startsWith("xxx"))
        stop();
      ClassData.CastInfo castInfo;
      if(  this.typeClazz == src.identInfo.typeClazz
        || src.identInfo.typeClazz == CRuntimeJavalikeClassData.clazz_unknown  //unknown type, don't cast, it is okay:
        || src.modeAccess == '?'                       //unknown type, don't cast, it is okay:
        || this.typeClazz == CRuntimeJavalikeClassData.clazz_unknown  //unknown type, don't cast, it is okay:
        || this.typeClazz == CRuntimeJavalikeClassData.clazz_void  //void as argument is a void*
        || this.typeClazz == CRuntimeJavalikeClassData.clazz_va_argRaw  //any type is valid
        ) {
        sRet = src.cCode;
      } else if( 
      		  (castInfo = typeClazz.getCastInitializerFromType(src.identInfo.typeClazz.sClassNameC)) != null
      		||(castInfo = src.identInfo.typeClazz.getCastInfoToType(typeClazz.sClassNameC)) != null
          ||(castInfo = typeClazz.getCastInfoFromType(src.identInfo.typeClazz.sClassNameC)) != null
          ){
      	if(castInfo.pre.equals("CONST_z_StringJc("))
      		stop();  //TODO count number of chars.
      	sRet = castInfo.pre + src.cCode + castInfo.post;
      } else{
      	sRet = src.cCode + "/*J2C: no cast found from " + src.identInfo.typeClazz + "*/";
      }
  	}
    return sRet;
  }
  
  
  
  /**Changes the access.
   * The '@' is handled like '*', because the building of enhanced reference is done outside.
   * @param modeAccessDst Necessary access, ones of chars <code>&*@~%?</code>, see {@link #modeAccess}
   * @param sSrc1 The cCode of src
   * @param modeAccessSrc Existing access of sSrc1
   * @return
   */
  public static String testAndChangeAccess(char modeAccessDst, String sSrc1, char modeAccessSrc)
  { String sAccessParam;
    if(modeAccessSrc == modeAccessDst || modeAccessSrc == '?' || modeAccessDst == '?' ) {
      sAccessParam = sSrc1;
    } else if("&".indexOf(modeAccessSrc)>=0 && "*~".indexOf(modeAccessDst)>=0) {
      sAccessParam = " (" + sSrc1 + ".ref)";
    } 
    else if(modeAccessSrc == '@'){
      switch(modeAccessDst){
      case '@': sAccessParam = sSrc1; break;
      case '*': case '~': sAccessParam = "REFJc(" + sSrc1 + ")"; break;
      case '$': sAccessParam = "*(REFJc(" + sSrc1 + "))"; break;
      default: sAccessParam = sSrc1; assert(false);
      }
    }
    else if(modeAccessSrc == 'Q'){  //non-headed embedded array: int x[20] 
      switch(modeAccessDst){
      case 'P': case '*': sAccessParam = "&" + sSrc1 + "[0]"; break;  //pointer to first element
      default: sAccessParam = sSrc1; assert(false);
      }
    }
    
    
    else if("*~&".indexOf(modeAccessSrc)>=0 && "%$".indexOf(modeAccessDst)>=0) {
      sAccessParam = "* (" + sSrc1 + ")";
    } else if("%$".indexOf(modeAccessSrc)>=0 && "@*~&".indexOf(modeAccessDst) >=0){
      sAccessParam = "& (" + sSrc1 + ")";
    } else if("*~&".indexOf(modeAccessSrc)>=0 && "*~&".indexOf(modeAccessDst) >=0){
      /**Reference and this-reference are compatible. */
      sAccessParam = sSrc1;
    } else if("%$t".indexOf(modeAccessSrc)>=0 && "%$t".indexOf(modeAccessDst) >=0){
      /**embedded or primitive type are not different. */
      sAccessParam = sSrc1;
    } else if(false && "%$t".indexOf(modeAccessSrc)>=0 && "X".indexOf(modeAccessDst) >=0){
      /**embedded or primitive type get from an array. */
      sAccessParam = sSrc1 + "->data[] /*TODO*/";
    } else if(false && "X".indexOf(modeAccessSrc)>=0 && "$".indexOf(modeAccessDst) >=0){
      /**embedded or primitive type get from an array. */
      sAccessParam = sSrc1 + "->data[] /*TODO1*/";
    } 
    else {
      //assert(false);
    	sAccessParam = sSrc1 + "/*J2C-error testAndChangeAccess: " + modeAccessSrc + modeAccessDst + "*/";
      //sAccessParam = null;
    } 
    return sAccessParam;
  }
  
  
  
  private String xxxtestAndcast(ClassData srcType, String value)
  { String sRet;
    if( value.equals("null"))
    { //a null-pointer should be compatible with all expect StringJc.
      if(typeClazz.sClassNameC.equals("StringJc")
        && dimensionArrayOrFixSize == 0
        )
      { 
        sRet = "null_StringJc"; }
      else {
        sRet = value; } 
    }
    else
    { //TODO may be some array tests
      
      sRet = typeClazz.xxxtestAndcast(srcType, value, '.');
    }
    return sRet;    
  }


  /**builds a String representation especially to test under eclipse. */
  public String toString()
  { String sRet = typeClazz.getClassIdentName() + " " + sName + " " + nClassLevel + nOuterLevel + modeAccess + "[" + modeArrayElement + "]" + modeStatic + nClassLevel + nOuterLevel
           + (declaringClazz != null ? declaringClazz.sClassNameC : " -stacklocal");
    return sRet;
  }


  
  
  /**It's a debug helper. The method is empty, but it is a mark to set a breakpoint. */
  void stop()
  { //debug
  }



  

}
