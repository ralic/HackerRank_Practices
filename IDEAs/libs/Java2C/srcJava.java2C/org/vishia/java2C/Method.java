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

import java.util.Iterator;
import java.util.List;

import org.vishia.zbnf.ZbnfParseResultItem;


/**Describes a method of a class.
 * <br>
 * <img src="../../../../Java2C/img/MethodData_omdJava2C.png" />
 * <br>
 * All methods of a class are stored in the private attribute {@link ClassData#methods},
 * filled using {@link #addMethod(String sNameJava, String sCName, int modifier, LocalIdents.IdentInfos returnType, ClassData[] paramsType)}
 * and there derivatives.
 * <br>
 * A Method of class can be find out calling {@link ClassData#searchMethod(String, List, boolean)} 
 * with given name and given actual Parameters
 * 
 * @author JcHartmut
 *
 */
public class Method
{ 
  /**Name of the method used in Java. */
  public final String sJavaName;
  
  /**Search name of method in Map {@link ClassData#methods}. It is the {@link #sJavaName} plus <code>#9</code> 
   * where <code>9</code> symbols the number of arguments. 
   * */
  public final String sKeyName;
  
  /**Name of the method used in C, with suffixes. 
   * If the method is able to override, this is not the implementation name 
   * but the name of calling from user. 
   * The implementation of this method uses the method table to call the correct implementation. */
  public final String sCName;
  
  /**<code>The first name of the method in an overridden-sequence. The method has the postfix 
   * of the first declaring interfaces or class, see {@link #declaringClass}. 
   * This identifier is used to build the <code>MT_<i>NAME</i></code>-identifier in the method table,
   * see {@link ClassData#gen_MethodTableDefinitionContent()}.
   * <code>null</code> if the method doesn't override any other and the method won't be overridden.
   * 
   */
  public final String sMethodTypeName;
  
  
  /**Name of the implementation of the method, null if not exists (interface). 
   * If the method is able to override, than the name has the suffix _F.
   */
  public final String sImplementationName;
  
  
  /**Name of the method in method tables, base name of the method. It is the Java-name 
   * if the method is only found with one set of parameter types. 
   * If the same Java-name is used for several methods with different parameter sets, 
   * this name is unambiguous. It contains the type of paramter as short information.
   */
  public final String sNameUnambiguous;
  
  
  
  /**Reference to a primary method if the method overloads another method, else null.
   */
  public final Method primaryMethod;
  
  
  /**If the method is declared in a base class or interface too, it is the path to the method table-part.
   * Elsewhere it is null. This attribute is able to use also to detect whether the method is virtual.
   */
  public final String sPathToMtbl;
  
  /**If the method is declared in a base class or interface too, it is the path necessary to address
   * the base class or interface started from <code>ythis</code>
   */
  public final String sPathToBase;
  
  /**All argument type of the method. */
  public final FieldData[] paramsType;
  
  /**Return type of the method. */
  public final FieldData returnType;
  
  /**The class which declares the method primary. It may be an interface or superclass
   * or the creating class itself, if the method are not found in deeper base.
   */
  public final ClassData firstDeclaringClass;
 
  /**The class which declares the implementation of the method. . */
  public ClassData declaringClass;
  
  /**The idents which are seen in the methods focus. 
   * This element remains with null, if the methode is only declared, no body is generated
   * (external classes without code generation). */
  private LocalIdents methodIdents;
  
  /**Set if _thCxt is need in method calls */
  public final boolean need_thCxt;
  
  /**The method head definition is set, if a method is parsed in first pass.
   * It is used in the second path.
   */
  private String sMethodShortDescription;

  String sReturnTypeDefinition;
  
  /**The parameter definition after the method name. */
  String sMethodFormalListDefiniton;
  
  /**Set if it is a ctor. */
  public final static int modeCtor = 0x1;
  
  /**Set if it is a ctor of a non-static inner class. */
  public final static int modeCtorNonStatic = 0x2;
  
  /**Set if it is a ctor of an anonymous class. */
  public final static int modeCtorAnonymous = 0x4;
  
  /**Modifier bit, when set the method has ThCxt as last argument. 
   * If not set, than it is a simple method without exceptions, 
   * so the thread context pointer may be overmuch. 
   */
  public final static int modeNoThCxt = 0x0040;
  
  public final static int modeNoStacktrace = 0x0080;
  
  /**Modifier bit, when set the method is static. */
  public final static int modeStatic = 0x0200;
  
  /**Modifier bit, when set the method is overrideable (virtual). */
  public final static int modeOverrideable = 0x0400;
  
  /**Modifier bit, when set the method returns the same instance as the calling one. */
  public final static int modeReturnThis = 0x0800;
  
  /**Modifier bit, when set the method returns a new created instance which isn't activated for garbabe collection. */
  public final static int modeReturnNew = 0x1000;
  
  /**Modifier bit, when set, the method returns a StringJc-instance, which's String is replaced in the thread context
   * or it isn't persistent in other way. */
  public final static int modeReturnNonPersistring = 0x2000;
  
  public final static int modeUnknownMethod = 0x8000;
  
  /**Some mode bits, ones of {@link #modeNoThCxt}, {@link #modeStatic}. */
  public final int mode;
  
  /**Set true if more as one methode with the same name is stored. */
  private boolean bUnambiguousness;

  /**If it is an overrideable method, its number in InheritanceInfo. Else -1. */
  private short idxMtbl= -1;
  
  /**Initializes a method to store in ClassData.
   * @param declaringClass The class which declares this method.
   * @param primaryMethodP <code>null</code> or the method of the super-class or interface.
   *                            If the method is not an implementation of a super- or interface-declared method,
   *                            it is <code>null</code>
   * @param sKeyName Name to search
   * @param sNameJava Name of the method in Java
   * @param sNameUnambiguous Name of the method to translate in C. If more than 1 method 
   *                         with the same sNameJava is given, the sCName should be unambiguous.
   *                         But it is without suffix of className. 
   * @param sImplementationName null if the implementation is not override-able. 
   *                         If the method is override-able and this param is null, than the suffix for the 
   *                         implementationName is "_F". Elsewhere it defines the sImplementationName-attribute of the class.  
   * @param modifier see {@link #mode}
   *        <ul> 
   *        <li> {@link #modeNoThCxt}
   *        <li> {@link #modeStatic}
   *        <li> {@link #modeOverrideable}
   *        <li> {@link #modeUnknownMethod}
   *        </ul>
   * @param returnType The type of return. It isn't a Classdata, but a IdentInfo, 
   *                   because some additional properties like return by value or reference should be present.
   * @param params Array of all argument types.
   * @param sPathToMtbl
   * @param sPathToBase
   */
  public Method(ClassData declaringClass, Method primaryMethodP
      , String sKeyName, String sNameJava, String sNameUnambiguous, String sImplementationName
      , int modifier, FieldData returnType
      , FieldData[] params, String sPathToMtbl, String sPathToBase)
  { this.declaringClass = declaringClass;
    //this.firstDeclaringClass = firstDeclaringClass;
    this.primaryMethod = primaryMethodP == null ? this : primaryMethodP;
    this.firstDeclaringClass =  this.primaryMethod.declaringClass;
    this.sKeyName = sKeyName; this.sJavaName = sNameJava;
    this.sPathToMtbl = sPathToMtbl;
    this.sPathToBase = sPathToBase;
    if(sNameUnambiguous != null && sNameUnambiguous.startsWith("testIfc"))
      stop();
    if(sNameUnambiguous != null){
      boolean bSimpleName = sNameUnambiguous.startsWith("!");
      if(bSimpleName)
      { sNameUnambiguous = sNameUnambiguous.substring(1);
      }
      this.sNameUnambiguous = sNameUnambiguous;
      if(bSimpleName){
        this.sCName = this.sMethodTypeName = sNameUnambiguous;
        if(sImplementationName == null){ this.sImplementationName = sNameUnambiguous;
        } else { this.sImplementationName = sImplementationName;
        }
      } else {
        this.sCName = sNameUnambiguous + (declaringClass != null ? "_" + declaringClass.getClassIdentName() : ""); 
        this.sMethodTypeName = (firstDeclaringClass != null) 
                               ? sNameUnambiguous + "_" + firstDeclaringClass.getClassIdentName() 
                               : null;
        this.sImplementationName = declaringClass != null && declaringClass.isInterface() ? null 
          : sImplementationName != null ? sImplementationName
          : sCName  + ((modifier & modeOverrideable)!= 0  || sKeyName.charAt(0)=='!' ? "_F" : "");
          
      }    
    } else {
      /**The method is only registered to detect ambiguously. */
      this.sNameUnambiguous = this.sCName = this.sMethodTypeName = this.sImplementationName = null;
    }
      
    this.paramsType = params; this.returnType = returnType;
    assert( (modifier & ~(modeUnknownMethod|modeNoThCxt|modeStatic|modeOverrideable|modeReturnThis|modeReturnNew|modeNoStacktrace|modeReturnNonPersistring|modeCtor|modeCtorAnonymous|modeCtorNonStatic)) == 0);
    this.mode = modifier;
    this.need_thCxt = (modifier & modeNoThCxt) == 0;
    bUnambiguousness = true;
  }
  
  
  /**Arguments of the method. */
  //private List<MethodArg> args;

  /**Sets the definiton of a method (C-code). This method could only call one time. */
  public void setMethodHeadDefiniton(String description, String retType, String def)
  { assert(sMethodFormalListDefiniton == null); //set only 1 time.
    sMethodShortDescription = description;
    sReturnTypeDefinition = retType;
    sMethodFormalListDefiniton = def;
  }
  
  /**Gets the definition of a method head for the C-code. */
  public String gen_MethodForwardDeclaration()
  { String ret = "";
    if(sMethodShortDescription != null){
      ret += "\n/**" + sMethodShortDescription + "*/";
    }
    if((mode & modeOverrideable) != 0 && declaringClass == firstDeclaringClass){
      /**Only in first declaration level: Method type declaration for Method table. */
      ret += "\ntypedef " + sReturnTypeDefinition + " MT_" + sCName + sMethodFormalListDefiniton + ";";
    }
    /**The normal forward declaration: */
    if((mode & modeOverrideable)==0){
      ret += "\nMETHOD_C " + sReturnTypeDefinition + " " + sCName + sMethodFormalListDefiniton + ";";
    }
    else if(  sImplementationName != null          //null only on interface definition.
      && sCName.equals(sImplementationName)) {
      /**final method, which implements any override-able method: */
      ret += "\n/* J2C:Implementation of the method, non-dynamic in this class: */";
      ret += "\nMETHOD_C " + sReturnTypeDefinition + " " + sCName + sMethodFormalListDefiniton + ";";
    } else {
      /**Dynamic override-able method: */
      if(sImplementationName != null){
        ret += "\n/* J2C:Implementation of the method, used for an immediate non-dynamic call: */";
        ret += "\nMETHOD_C " + sReturnTypeDefinition + " " + sImplementationName + sMethodFormalListDefiniton + ";";
      }
      ret += "\n/* J2C:Call of the method at this class level, executes a dynamic call of the override-able method: */";
      ret += "\nMETHOD_C " + sReturnTypeDefinition + " " + sCName + sMethodFormalListDefiniton + ";";
    }
    ret += "\n";   
    return ret; 
  }
  
  /**Gets the definition of a method (C-code), used in second pass. */
  public String gen_MethodHeadDefinition()
  { if(sImplementationName.startsWith("testIfc_ImplIfc_Test"))
      stop();
    return sReturnTypeDefinition + " " + sImplementationName + sMethodFormalListDefiniton; 
  }
  
  
  public void putFieldIdent(FieldData field)
  {
  	methodIdents.putElement(field.getName(), field);
  }
  
  
  /**Sets the method ambiguous. This method is called, if a method with the same java name 
   * and the same number of arguments, but other argument types are found in translation process.
   */
  void setAmbiguousness(){ bUnambiguousness = false; }
  
  
  
  
  /**Checks whether all parameters from an actual method call are matching
   * @param paramsTypeCheck
   * @return The score 0..4 see {@link ClassData.CastInfo#kCastAble}.
   */
  int checkParameter(List<CCodeData> paramsTypeCheck)
  { //boolean bMatching = false;
  	int score;
    int idxParam = 0;
    if(paramsTypeCheck == null && (this.paramsType == null || this.paramsType.length == 0))
    { score = 3;
      //bMatching = true;
    }
    else
    { //bMatching = true; //default, abort test if false.
      score = Integer.MAX_VALUE;  //start with highest
    	Iterator<CCodeData> iterParamAct = paramsTypeCheck.iterator();
      //while(bMatching && iterParamAct.hasNext())
      boolean bVarg = false;
    	while(!bVarg && score > 0 && iterParamAct.hasNext())
      { CCodeData actParam = iterParamAct.next();
        FieldData infoActParam = actParam.identInfo;
        FieldData typeParamMethod = this.paramsType[idxParam++];
        int scoreParam;
        if(actParam.cCode.equals("null") && !typeParamMethod.typeClazz.isPrimitiveType()){
        	scoreParam = 3;  //null-pointer matches to all references
        }
        else if(typeParamMethod.typeClazz == CRuntimeJavalikeClassData.clazz_va_argRaw){
        	scoreParam = ClassData.CastInfo.kCastCommon;  //maybe matched.
        	bVarg = true; //abort check.
        } else if(infoActParam.getDimensionArray() >0){
        	//it is an array,
        	if(typeParamMethod.typeClazz == infoActParam.typeClazz){
        		scoreParam = ClassData.CastInfo.kCastEqual;
        	} else if(typeParamMethod.typeClazz == CRuntimeJavalikeClassData.clazzByteStringJc){
            scoreParam = ClassData.CastInfo.kCastAble;  //all is able to present as ByteStringJc
        	} else if( typeParamMethod.typeClazz == CRuntimeJavalikeClassData.clazzObjectJc
        					&& "XY".indexOf(infoActParam.modeAccess) >=0  //an ObjectArrayJc-based array
        					 ){
            scoreParam = ClassData.CastInfo.kCastAble;  //Object as Baseclass
        	} else {
        		scoreParam = ClassData.CastInfo.kCastNo;  //does match only to itself or Object
        	}
        } else {
        	scoreParam = typeParamMethod.typeClazz.matchedToTypeSrc(infoActParam.typeClazz);
        }
        if(scoreParam < score)
        { //bMatching = false;
          score = scoreParam;  //may be 0, than abort check.
        }
      }
    }  
    return score;            
  }               
  
  
  /**Checks all parameter of the method with given parameter set.
   * @param paramsTypeCheck
   * @return true only if are the same parameter. 
   */
  boolean sameParameterTypes(FieldData[] paramsTypeCheck)
  { boolean bMatching = false;
    int idxParam = 0;
    int zParamsCheck = paramsTypeCheck == null ? 0: paramsTypeCheck.length;
    int zParamsThis = this.paramsType == null ? 0: this.paramsType.length;
    bMatching = (zParamsCheck == zParamsThis); //false if different nr of params.
    while(bMatching && idxParam < zParamsCheck)
    { //Check params:
      FieldData infoActParam = paramsTypeCheck[idxParam];
      FieldData typeParamMethod = this.paramsType[idxParam++];
      //if(!typeParamMethod.typeClazz.matchedTo(infoActParam.typeClazz))
      if(typeParamMethod.typeClazz != (infoActParam.typeClazz))
      { bMatching = false;
      }
    }
    return bMatching;            
  }               
  
  
  /**Returns true, if more as one method with the same java name and the same number of arguments exists.
   */
  public boolean isAmbigous(){ return !bUnambiguousness; }

  /**Returns true if the method is able to override (dynamic linked) in an inheriting class. */
  public boolean noStacktrace(){ return (mode & modeNoStacktrace) != 0; }
  
  /**Returns true if the method is able to override (dynamic linked) in an inheriting class. */
  public boolean isOverrideable(){ return (mode & modeOverrideable) != 0; }
  
  /**Returns true if the method returns its own calling instance. */
  public boolean isReturnThis(){ return (mode & modeReturnThis) != 0; }
  
  /**Returns true if the method returns a new instance which isn't activated for garbage collection. */
  public boolean isReturnNew(){ return (mode & modeReturnNew) != 0; }
  
  /**Returns true if the method returns a new instance which isn't activated for garbage collection. */
  public boolean isReturnNonPersistring(){ return (mode & modeReturnNonPersistring) != 0; }
  
  /**Returns true if the method is static. */
  public boolean isStatic(){ return (mode & modeStatic) != 0; }
  
  /**Returns true if the method is auto-created while used, it is not defined. */
  public boolean isUnknownMethod(){ return (mode & modeUnknownMethod) != 0; }
  
  
  /**Writes the structure information of the method for file.stc. The structure information is parsed
   * to know the class with its method without processing the first pass of translation,
   * if the file.java is older as existing file.h and file.c
   * @return String with the informations for the file.stc.
   */
  public String writeStruct()
  { StringBuffer out = new StringBuffer(200);
    out.append( sJavaName);
    
    //if(sNameUnambiguous != sJavaName && sNameUnambiguous.startsWith(sJavaName)){
    if(sNameUnambiguous != null && sNameUnambiguous.length() >0 && !sNameUnambiguous.equals(sJavaName)){
    	if(sNameUnambiguous.startsWith(sJavaName)){
        out.append("+").append(sNameUnambiguous.substring(sJavaName.length()));
	    } else {
	      assert(false); //should start always with sJavaName
	    }
		}
    String sClassIdentName = declaringClass.getClassIdentName();
    int posClassSuffix = sCName.lastIndexOf(sClassIdentName);
    if(posClassSuffix >= sNameUnambiguous.length()){
      int posClassSuffixEnd = posClassSuffix + sClassIdentName.length();
      assert(sCName.startsWith(sNameUnambiguous));
      out.append("-").append(sCName.substring(sNameUnambiguous.length(), posClassSuffix)); //may be empty.
      out.append("$");  //ZBNF: [$<?classSuffixName>]
      if(posClassSuffixEnd > sCName.length()){
        out.append("%").append(sCName.substring(posClassSuffixEnd));  //ZBNF: %<$?implementationSuffix>
      }
    }
    if(primaryMethod != null && primaryMethod != this){
      out.append(", defined: ").append(primaryMethod.declaringClass.sClassNameJavaFullqualified);
      out.append(".").append(primaryMethod.sNameUnambiguous);
    }
    
    char cSep = ' ';
    if(mode != 0){
      out.append(", mode=");
      if( (mode & modeCtor) != 0){ out.append(cSep).append("ctor"); cSep = '+'; }
      if( (mode & modeCtorAnonymous) != 0){ out.append(cSep).append("anonymousCtor"); cSep = '+'; }
      if( (mode & modeCtorNonStatic) != 0){ out.append(cSep).append("nonStaticCtor"); cSep = '+'; }
      if( (mode & modeNoStacktrace) != 0){ out.append(cSep).append("noStacktrace"); cSep = '+'; }
      if( (mode & modeNoThCxt) != 0)     { out.append(cSep).append("noThCxt");      cSep = '+'; }
      if( (mode & modeOverrideable) != 0){ out.append(cSep).append("overrideable"); cSep = '+'; }
      if( (mode & modeReturnThis) != 0)  { out.append(cSep).append("returnThis");   cSep = '+'; }
      if( (mode & modeReturnNew) != 0)  { out.append(cSep).append("returnNew");   cSep = '+'; }
      if( (mode & modeReturnNonPersistring) != 0)  { out.append(cSep).append("returnNonPersistring");   cSep = '+'; }
      if( (mode & modeStatic) != 0)      { out.append(cSep).append("static");       cSep = '+'; }
    }
    
    out.append(": ");
    //out = sJavaName + "; " + sNameUnambiguous + "; " + sCName + "; " + sImplementationName + "; " + sMethodTypeName + "; ";
    out.append(returnType == null ? "void_ C.. return; " : returnType.writeStruct());
    out.append("(");
    { String separator = "";
      if(paramsType != null && paramsType.length >0)
      for(FieldData param: paramsType)
      { out.append(separator).append(param.writeStruct());
        separator = ",";
      }
      out.append(")");
    }
    out.append(";");
    /*
    out.append("//info" 
        + "; mode=0x"+ Integer.toHexString(mode)
        + "; sKeyName=" + sKeyName 
        + "; sCName=" + sCName
        + "; sMethodTypeName=" + sMethodTypeName
        + "; sImplementationName="+ sImplementationName
        + "; primaryMethod.sCName="+ primaryMethod.sCName
        + "; sPathtoBase="+ sPathToBase
        + "; sPathToMtbl="+ sPathToMtbl
        + "; firstDeclaringClass.sClassNameC="+ firstDeclaringClass.sClassNameC
        //only for own generation: + "; sMethodFormalListDefiniton="+ sMethodFormalListDefiniton
        + "\n";
    */    
    return out.toString();
  }
  
  /**Returns -1 if it is a final method, or 0..., the index in the InheritanceInfo. */
  public int getIdxMtbl(){ return idxMtbl; }
  
  
  /**Helpfull for debugging in eclipse.
   * @see java.lang.Object#toString()
   */
  public String toString()
  { return sCName;
  }

  public final LocalIdents getMethodIdents(){ return methodIdents;}

  public final void setMethodIdents(LocalIdents methodIdents)
  { assert(this.methodIdents == null);  //call only one time!
    this.methodIdents = methodIdents;
  }
  
  void stop(){}






}

