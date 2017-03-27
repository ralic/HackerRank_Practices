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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.vishia.mainCmd.Report;
import org.vishia.zbnf.ZbnfParseResultItem;


  
  
/**This class handle the second pass of the translation.
 * An instance is built only temporary to run the pass. All Data are stored in the {@link ClassData}.
 * Instances of ClassData will be created while running the first pass, there are stored in 
 * the singleton {@link AllData}. Than they are used to run the Second Pass.
 * 
 * @author JcHartmut
 *
 */
public class SecondPass extends GenerateClass
{
  
  /**Stores the necessity of generating a <code>mtthis</code> reference for the own method table.
   * This variable is set to false on start of any {@link #write_methodDefinition(org.vishia.java2C.ClassData.MethodWithZbnfItem, String, LocalIdents)}
   * and set to true if any dynamic call with <code>ythis</code>-reference is generated.
   * Depending on this, a line to prepare a <code>mtthis</code> variable: The reference to the
   * own method table, is prepared. 
   */
  boolean bUse_mtthis;
  
  /**Stores the necessity of generating a <code>STACKTRC_ENTRY</code> and <code>STACKTRC_LEAVE</code>.
   * This variable is set on start of any {@link #write_methodDefinition(org.vishia.java2C.ClassData.MethodWithZbnfItem, String, LocalIdents)}
   * depending on the {@link Method#noStacktrace()}.
   */
  boolean noStacktrace;

  /**This class holds the common values of a statement block and handles the generation of a statement block.
   * 
   * {@link StatementBlock#gen_simpleMethodCall(ZbnfParseResultItem, String, LocalIdents.IdentInfos, ClassData[] retType, LocalIdents) }
   */

  /**Initializes an instance to run the second pass of a given class.
   * The instance is generated only temporary to run the passes, see {@link GenerateClass}. 
   * @param genClass.writeContent Interface to write the content. Note: The content is not written immediately 
   *   in a file, but temporary stored in some StringBuilder. The reason is the order of parts of C-File and H-File.
   * @param pkgIdents singleton information of all classes there are knwon yet.
   * @param classData The data of this class saves between parsing, first and second pass. 
   *        This classData also contain the ZBNF parse result items.
   * @param runRequiredFirstPass Interface to cause the parsing and running of a first pass
   *        of a up-to-now unknown class. 
   *        This interface is implemented at the main level of the Java2C-translator
   */
  //SecondPass(iWriteContent genClass.writeContent, TreeMap<String, ClassData> pkgIdents, ClassData classData, RunRequiredFirstPass_ifc runRequiredFirstPass)
  SecondPass(iWriteContent writeContent, GenerateFile parentGenerateFile
  , LocalIdents fileLevelIdents, ClassData classData
  , RunRequiredFirstPass_ifc runRequiredFirstPass
  , Report log
  )
  { super(writeContent, parentGenerateFile, fileLevelIdents, runRequiredFirstPass, log);
    this.classData = classData;
  }


  /**writes the constructor-, method-, static variable- definition and reflection into the C-File.
   * Inside it is written and called:<ul>
   * <li>all definitions of static variables with there initialization or 0-initiatialization.
   * <li>{@link #write_constructorDefinition(ZbnfParseResultItem, String)}
   * <li>{@link #write_methodDefinition(ZbnfParseResultItem, String)}
   * </ul>
   *  
   * @param itemClass The class start item of zbnf parse result.
   * @param sOuterClassName Name of the environment class not used yet
   * @throws IOException
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   */
  void runSecondPassClass(ZbnfParseResultItem itemClass, String sOuterClassName)
  throws IOException, ParseException, IllegalArgumentException, IllegalAccessException, InstantiationException
  {
    String sClassNameWithout_s = classData.getClassIdentName();
    //String sClassName = classData.getClassCtype_s();
    log.writeInfoln("2. pass: " + classData.sClassNameJavaFullqualified + "->" + sClassNameWithout_s);
    
    ZbnfParseResultItem itemDescription = itemClass.getChild("description");
    if(itemDescription != null)
    { StringBuilder uResult = new StringBuilder();
      write_Description(itemDescription, uResult);
      writeContent.writeClassC(uResult.toString());
    }
    
    writeContent.writeClassC("\n\nconst char sign_Mtbl_" + sClassNameWithout_s + "[] = \"" 
                     + sClassNameWithout_s + "\"; //to mark method tables of all implementations\n");      
    //A method table isn't need if the class is a simple data class or an interface.
    //If the class doesn't base on Object, a method table is not able to use.
    boolean bMtbl = (classData.inheritanceInfo != null && classData.isBasedOnObject() 
    	&& !classData.isInterface() && !classData.isAbstract);
    //boolean bMtbl = (classData.inheritanceInfo != null && !classData.isInterface());
    if(bMtbl){
      /**Writes the declaration of the method table definition, because it may be necessary for <code>mtthis</code>. 
       * See also usage of MtblDef_*/   
      writeContent.writeClassC("\ntypedef struct MtblDef_" + sClassNameWithout_s + "_t { Mtbl_" 
                       + sClassNameWithout_s + " mtbl; MtblHeadJc end; } MtblDef_" + sClassNameWithout_s + ";");      
      writeContent.writeClassC("\n extern MtblDef_" + sClassNameWithout_s + " const mtbl" + sClassNameWithout_s + ";");      
    }    

    Java2C_Main.singleton.console.reportln(Report.debug, "Java2C-SecondPass.run: class=" // + sourceOfClassData
    	+ classData.getClassIdentName() + (bMtbl ? ", mtbl" : "")
    	);
    
    StatementBlock staticQuasiBlock = new StatementBlock(this, classData.classLevelIdents, true, 1);
    for(ClassData.InitInfoVariable staticVariable : classData.staticVariables)
    { String sName = staticVariable.identInfos.getName();
      if(sName.equals("searchTrc"))
        stop();
      FieldData identInfos = classData.classLevelIdents.get(sName);
      String sTypeCode = identInfos.gen_Declaration();
      char kind = staticVariable.identInfos.modeAccess;
      String sStaticDef = "";
      //if(staticVariable.bConst)
      if(identInfos.modeStatic=='S')
      { sStaticDef += "const ";   //final type in java, in headerfile with const static 
      }
      sStaticDef += sTypeCode;
      //sStaticDef += sTypeClassIdent + (kind=='@' ? "REF " : " ") + sName + "_" + sClassNameWithout_s;
      sStaticDef += " " + sName + "_" + sClassNameWithout_s;
      for(int dimension1 = 0; dimension1 < staticVariable.identInfos.getDimensionArray(); dimension1++)
      { final String sDimension = staticVariable.identInfos.fixArraySizes == null ? null
                                : staticVariable.identInfos.fixArraySizes[dimension1];
        sStaticDef += "[" + ( sDimension == null ? "" : sDimension) + "]"; 
      }
      
      if(staticVariable.zbnfInit == null)
      { //no init value
        if("@".indexOf(kind)>=0)
        { //embedded struct or enhanced reference, null-initializing
          sStaticDef += " = { 0, null };";
        }
        else if("$".indexOf(kind)>=0)
        { //embedded struct or enhanced reference, null-initializing
          sStaticDef += " = { 0 };";
        }
        else if("*".indexOf(kind)>=0)
        { //normal reference, null-initializing
          sStaticDef += " = null;";
        }
        else
        { sStaticDef += "= 0;";
        }
      }
      else
      { if(sName.equals("intArrayStatic"))
          stop();  //kk1
        sStaticDef = staticQuasiBlock.gen_VariableDefWithSimpleInitValue(staticVariable.identInfos, staticVariable.zbnfInit); 
      }
      writeContent.writeClassC("\n" + sStaticDef);      
    }
    
    
    /**Don't write methods if it is an interface. */
    //for(ClassData.MethodWithZbnfItem methodDef: classData.listMethodWithZbnf){ 
    ClassData.MethodWithZbnfItem methodDef;
    while((methodDef = classData.methodsWithZbnf.poll()) !=null){
    //Note: The methods will be ^ed in the first-pass.  
      if(methodDef.isConstructor())
      { //Note: at least the default constructor is defined in the first-pass.
        boolean bArgumentSensitive = classData.nrofConstructor >1;
      	write_constructorDefinition(methodDef, sClassNameWithout_s, bArgumentSensitive);
      }
      else
      { write_methodDefinition(methodDef, sClassNameWithout_s, classData.classLevelIdents);
      }
    }
    
    if(classData.isFinalizeNeed()){
      /* NOTE: the write_finalizeDefinition() have to be called after all write_methodDefinition(),
       * because the ClassData.zbnfFinalizeMethod is set in write_methodDefinition().
       */
      write_finalizeDefinition(itemClass, sClassNameWithout_s, classData.classLevelIdents);
    }

    write_Reflections();
        
    
  }

  
  
  
  
  
  
  
  /**Writes out the reflection inclusively the method table definition.
   * A method table isn't need if the class is either a simple data class without virtual methods
   * or if it is an interface: The method table refers the implementation methods of the instantiation, 
   * an interface hasn't an instantiation. 
   */
  private void write_Reflections()
  {  
    String sClassNameWithout_s = classData.getClassIdentName();
    if(sClassNameWithout_s.equals("SimpleDataStruct_Test"))
      stop();
    String sClassName = classData.getClassCtype_s();
    boolean isObject;
    //A method table isn't need if the class is a simple data class or an interface.
    //If the class doesn't base on Object, a method table is not able to use.
    boolean bMtbl = (classData.inheritanceInfo != null && classData.isBasedOnObject()
    	 && !classData.isInterface() && !classData.isAbstract);
    if(bMtbl){
      writeContent.writeClassC("\n\n\n/**J2C: Reflections and Method-table *************************************************/");      
      writeContent.writeClassC("\nconst MtblDef_" + sClassNameWithout_s + " mtbl" + sClassNameWithout_s + " = {");      
      String content = classData.genMethodTableContent(classData.inheritanceInfo, sClassNameWithout_s, 0);
      writeContent.writeClassC(content);
      writeContent.writeClassC(", { signEnd_Mtbl_ObjectJc, null } }; //Mtbl\n\n");
    }

    final String referenceReflectionSuperClass;
    final String referenceReflectionIfc;
    //String sPathSuper;
    final ClassData superClass = classData.getSuperClassData();
    if(superClass != null)
    { final String sNameSuper = superClass.getClassCtype_s();
      //final String reflectionSuperClass = superClass == null ? null : "&reflection_";
      final String offsetMtbl = bMtbl 
        ? ", OFFSET_Mtbl(Mtbl_" + sClassNameWithout_s + ", " + superClass.getClassIdentName() + ")" 
        : ", 0 /*J2C: no Mtbl*/";
    	writeContent.writeClassC("\n extern struct ClassJc_t const reflection_" + sNameSuper + ";");      
      writeContent.writeClassC("\n static struct superClasses_" + sClassName + "_t");      
      writeContent.writeClassC("\n { ObjectArrayJc head;");      
      writeContent.writeClassC("\n   ClassOffset_idxMtblJc data[1];");      
      writeContent.writeClassC("\n }superclasses_" + sClassName + " =");      
      writeContent.writeClassC("\n { CONST_ObjectArrayJc(ClassOffset_idxMtblJc, 1, OBJTYPE_ClassOffset_idxMtblJc, null, null)");      
      writeContent.writeClassC("\n , { {&reflection_" + sNameSuper + offsetMtbl + " }");      
      writeContent.writeClassC("\n   }");      
      writeContent.writeClassC("\n };\n");      
      referenceReflectionSuperClass = "(ClassOffset_idxMtblJcARRAY*)&superclasses_" + sClassName;
      isObject = classData.isBasedOnObject();
    }
    else
    { referenceReflectionSuperClass = "null";
      isObject = false;
    }

    final ClassData[] ifcs = classData.getInterfaces();
    if(ifcs  != null)
    { for(ClassData ifc: ifcs){
        final String sNameIfc = ifc.getClassCtype_s();
        writeContent.writeClassC("\n extern struct ClassJc_t const reflection_" + sNameIfc + ";");      
      }
      writeContent.writeClassC("\n static struct ifcClasses_" + sClassName + "_t");      
      writeContent.writeClassC("\n { ObjectArrayJc head;");      
      writeContent.writeClassC("\n   ClassOffset_idxMtblJc data[" + ifcs.length + "];");      
      writeContent.writeClassC("\n }interfaces_" + sClassName + " =");      
      writeContent.writeClassC("\n { CONST_ObjectArrayJc(ClassOffset_idxMtblJc, 1, OBJTYPE_ClassOffset_idxMtblJc, null, null)");      
      String sSep = "\n, {";
      for(ClassData ifc: ifcs){
        final String sNameIfc = ifc.getClassCtype_s();
        writeContent.writeClassC(sSep+ " {&reflection_" + sNameIfc + ", OFFSET_Mtbl(Mtbl_" + sClassNameWithout_s + ", " + ifc.getClassIdentName() + ") }");      
        sSep = "\n  ,";
      }
      writeContent.writeClassC("\n  }");      
      writeContent.writeClassC("\n};\n");      
      referenceReflectionIfc = "(ClassOffset_idxMtblJcARRAY*)&interfaces_" + sClassName;
      isObject = classData.isBasedOnObject();
    }
    else
    { referenceReflectionIfc = "null";
      isObject = false;
    }

    final String sFieldReference;
    //if(classData.classLevelIdentsOwn != null && classData.classLevelIdentsOwn.size() >0)
    int nrofReflectionField = 0;
    if(classData.classLevelIdentsOwn != null) 
    { for(FieldData field: classData.classLevelIdentsOwn)
      { if(field.modeStatic != 'd')  //defines not.
        { nrofReflectionField +=1; }
      }
    }  
    
    if(nrofReflectionField >0)
    { sFieldReference = "(FieldJcArray const*)&reflection_Fields_" + sClassName;
      writeContent.writeClassC("\nextern struct ClassJc_t const reflection_" + sClassName + ";");      
      Collection<ClassData> types = classData.classLevelUsageTypes.values();
      for(ClassData type : types)
      { if(!type.isPrimitiveType())
        writeContent.writeClassC("\nextern struct ClassJc_t const reflection_" + type.sClassNameC + ";");      
      }
      writeContent.writeClassC("\nconst struct Reflection_Fields_" + sClassName + "_t");      
      writeContent.writeClassC("\n{ ObjectArrayJc head; FieldJc data[" + nrofReflectionField + "];");      
      writeContent.writeClassC("\n} reflection_Fields_" + sClassName + " =");      
      writeContent.writeClassC("\n{ CONST_ObjectArrayJc(FieldJc, " + nrofReflectionField + ", OBJTYPE_FieldJc, null, &reflection_Fields_" + sClassName + ")");      
      writeContent.writeClassC("\n, {");
      char cSeparator = ' ';
      for(FieldData field: classData.classLevelIdentsOwn)
      { if(field.getName().equals("object"))
          stop();
        if(field.modeStatic != 'd')  //defines not.
        {
          int dimensionArray = field.getDimensionArray();
          //if(field.)
          if(field.getName().equals("constString"))
            stop();
          writeContent.writeClassC("\n   " + cSeparator + " { \"" + field.getName() + "\"");
          if(field.getDimensionArray() ==0){
          	writeContent.writeClassC("\n    , 0 //nrofArrayElements"); //dimensionArray ==0");
          } else {
          	writeContent.writeClassC("\n    , " + (field.fixArraySizes == null ? "0" : field.fixArraySizes[0]) + " //nrofArrayElements");
          }
          { final char modeElement;
            final String sModeContainerReference;
            if(dimensionArray > 0)  //TODO also on List<Type> etc.
            { modeElement = field.modeArrayElement;
              switch(field.modeAccess)
              { case 'P': case 'X': case '*': sModeContainerReference = "|kReferencedContainer_Modifier_reflectJc "; break;
                case 'Y': case '$': sModeContainerReference = "|kEmbeddedContainer_Modifier_reflectJc "; break; 
                case '@':
                case 't':
                case '&': sModeContainerReference = "|kEnhancedRefContainer_Modifier_reflectJc "; break; 
                case 'Q':
                case '%': sModeContainerReference = ""; break;
                default: throw new IllegalArgumentException("illegal FieldData.modeAccess: " + field.modeAccess);
              }
            }
            else
            { modeElement = field.modeAccess;
              sModeContainerReference = "";
            }
            if(field.typeClazz.isPrimitiveType())
            { int nrofBytes;
              switch(field.typeClazz.cVaArgIdent)
              { case 'Z': nrofBytes = 4; break;
                case 'B': nrofBytes = 1; break;
                case 'S': nrofBytes = 2; break;
                case 'J': nrofBytes = 8; break;
                case 'D': nrofBytes = 8; break;
                default: nrofBytes = 4;
              }
              if(field.typeClazz == CRuntimeJavalikeClassData.clazz_s0)
              { writeContent.writeClassC("\n    , REFLECTION_char");
                writeContent.writeClassC("\n    , mReference_Modifier_reflectJc");
              }
              else
              {
                writeContent.writeClassC("\n    , REFLECTION_" + field.getTypeName());
                writeContent.writeClassC("\n    , " + nrofBytes + " << kBitPrimitiv_Modifier_reflectJc ");
              }
              //TODO arrays of primitives, now showing as simple primitive.
            }
            else
            { writeContent.writeClassC("\n    , &reflection_" + field.getTypeName());
              final String sModeElement;
              switch(modeElement)
              { case '*': sModeElement = "kReference_Modifier_reflectJc "; break;
                case '$': sModeElement = "kEmbedded_Modifier_reflectJc "; break; 
                case '@':
                case 't':
                case '&': sModeElement = "kEnhancedReference_Modifier_reflectJc /*" + modeElement + "*/ "; break; 
                case '%': sModeElement = "0"; break;
                default: throw new IllegalArgumentException("illegal modeElement: " + modeElement);
              }
              writeContent.writeClassC("\n    , " + sModeElement); 
              
              ClassData fieldSuperClass = field.typeClazz; 
              //search the last superclass, may be ObjectJc:
              while(fieldSuperClass != null && fieldSuperClass != field.typeClazz.getSuperClassData())
              { fieldSuperClass = fieldSuperClass.getSuperClassData();
              }
              if(fieldSuperClass == CRuntimeJavalikeClassData.clazzObjectJc)
              { writeContent.writeClassC("|mObjectJc_Modifier_reflectJc ");
              }
              
            }
            
            if(field.fixArraySizes!=null){ writeContent.writeClassC("|kStaticArray_Modifier_reflectJc "); }
            else if(field.getDimensionArray() == 1){ writeContent.writeClassC("|kObjectArrayJc_Modifier_reflectJc "); }
            
            if("sS".indexOf(field.modeStatic)>=0){ writeContent.writeClassC("|mSTATIC_Modifier_reflectJc "); }
            
            writeContent.writeClassC(sModeContainerReference + "//bitModifiers");
          }   
          //offsetToObjectifcBase
          if("sS".indexOf(field.modeStatic)>=0)
          {
            writeContent.writeClassC("\n    , 0 //compiler problem, not a constant,TODO: (int16)(&" + field.getName() + "_" + field.declaringClazz.getClassIdentName()
                + ") //lo part of memory address of static member");
            writeContent.writeClassC("\n    , 0 //compiler problem, not a constant,TODO: (int16)((int32)(&" + field.getName() + "_" + field.declaringClazz.getClassIdentName()
                + ")>>16) //hi part of memory address of static member instead offsetToObjectifcBase, TRICKY because compatibilty.");
          }
          else
          {
            writeContent.writeClassC("\n    , (int16)((int32)(&((" + sClassName + "*)(0x1000))->" + field.getName()
                + ") - (int32)(" + sClassName + "*)0x1000)");  //offset calculated by C-compiler
            writeContent.writeClassC("\n    , 0  //offsetToObjectifcBase");
          
          }
          writeContent.writeClassC("\n    , &reflection_" + sClassName);
          writeContent.writeClassC("\n    }");
          cSeparator = ',';
        }
      }
      writeContent.writeClassC("\n} };");
    }
    else{ sFieldReference = "null //attributes and associations"; }
    
    final String sClassNameReflection;
    if(sClassName.length() > 28)
    { int len = sClassName.length();
      sClassNameReflection = sClassName.substring(0, 18) + "_" + sClassName.substring(len-9, len);
    }
    else
    { sClassNameReflection = sClassName;
    }
    writeContent.writeClassC("\nconst ClassJc reflection_" + sClassName + " = ");      
    writeContent.writeClassC("\n{ CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_ObjectJc, &reflection_ClassJc) ");      
    writeContent.writeClassC("\n, \"" + sClassNameReflection + "\"");      
    if(isObject)
    { writeContent.writeClassC("\n, (int16)((int32)(&((" + sClassName + "*)(0x1000))->base.object"
          + ") - (int32)(" + sClassName + "*)0x1000)");  //offset calculated by C-compiler
      
    }
    else
    { writeContent.writeClassC("\n,  0 //position of ObjectJc");      
    }
    writeContent.writeClassC("\n, sizeof(" + sClassName + ")");      
    writeContent.writeClassC("\n, " + sFieldReference);      
    writeContent.writeClassC("\n, null //method");      
    writeContent.writeClassC("\n, " + referenceReflectionSuperClass + " //superclass");      
    writeContent.writeClassC("\n, " + referenceReflectionIfc + " //interfaces");      
    writeContent.writeClassC("\n, " + (isObject ? "mObjectJc_Modifier_reflectJc" : "0    //modifiers"));      
    if(bMtbl){
      writeContent.writeClassC("\n, &mtbl" + sClassNameWithout_s + ".mtbl.head");      
    }
    writeContent.writeClassC("\n};\n");      
  }
  
  
  
  
  
  /**writes a constructor definition as C-method into the C-File.
   * The interface-method {@link iWriteContent#writeClassC(String)} is used to do it.
   * The content isn't written directly in the file, because some include-lines 
   * generated while running any methods and constructors of second pass should be written
   * before the text of this method.
   * <br>
   * Inside it is written and called:<ul>
   * <li><code>MemC rawMem</code> as first and <code>ThCxt* _thCxt</code>
   *   as last argument of constructor head.
   * <li>{@link gen_variableDefinition(ZbnfParseResultItem, LocalIdents, List, char intension)}
   *   to generate the arguments of the constructor, in Java2C.zbnf <code>< argumentList></code>
   *   and <code>< argument></code>
   * <li><code>{  StacktraceJc stacktrace...</code>-expressions.  
   * <li>Test of size of rawMem, RuntimeException if it is to less.
   * <li><code>memset(ythis, 0, sizeof(*ythis));</code> of the used mem area of the class (C-struct).
   *   Like in Java all class elements of this instance are set to zero in default.
   * <li>call of super constructors or call of <code>ctorc_ObjectJc(rawMem);</code>
   * <li><code>setReflection_ObjectJc(...)</code> to set reflection and jump table infos for the instance.       
   * <li>call of constructor of all initialized references and especially embedded instances.  
   * <li>{@link StatementBlock#gen_value(ZbnfParseResultItem, ClassData[], LocalIdents, char)}
   *   to generate the initial value assignment of class variable. The list {@link ClassData.variablesToInit}
   *   of the {@link classData} is used to get all variables to initialze. That list elements contain
   *   the reference to the parse result item elements of parsed initial values in Java code.
   * <li>{@link StatementBlock#gen_statementBlock(ZbnfParseResultItem, int, LocalIdents)}
   *   is used to generate the statement block of the constructor.  
   * </ul>
   * While generating the constructor it is possible that a type is used inside, which is unknown yet.
   * Than an include is generated. Because the internal structure of the type should be known,
   * the parsing of the Java Code and the run of its first pass is processed while running the method generation.
   * The second pass of this file is running later. This actions are done using 
   * {@link RunRequiredFirstPass_ifc#runRequestedFirstPass(String)}.
   *  
   * @param itemConstructor Parse result of <code>constructorDefinition::=...</code>
   *        If it is null, than this method is used to create a default constructor with all initializations.
   * @param sClassName Full Name of the class in C-Style where the constructor is member of.
   *        The class name is used as return type and as C-methodidentifier-postfix. 
   * @throws IOException
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   */
  private void write_constructorDefinition
  ( ClassData.MethodWithZbnfItem methodDef
  , String sClassName
  , boolean bArgumentSensitive
  )
  throws IOException, ParseException, IllegalArgumentException, IllegalAccessException, InstantiationException
  { //LocalIdents localIdents = new LocalIdents(classData.classLevelIdents);
    String sDeclaringClassIdentName = classData.getClassIdentName();
    assert(sClassName.equals(sDeclaringClassIdentName));
    //String sClassName_s = classData.getClassCtype_s();
    boolean isBasedOnObject = classData.isBasedOnObject();
    ZbnfParseResultItem zbnfConstructor = methodDef.zbnfMethod;
    //StatementBlock statementBlockCtorFrame = new StatementBlock(new LocalIdents(classData.classLevelIdents, null), true);
    StatementBlock statementBlockCtorFrame = new StatementBlock(this, methodDef.method.getMethodIdents(), true, 1);

    if(sClassName.equals("ReadTargetFromText"))
      stop();
    
    /**Generate the body. */
    final StringBuilder uBody = new StringBuilder(10000);
    final String sLine_mtthis;
    bUse_mtthis = false;
    if(zbnfConstructor != null){
      /**NOTE: first generate the body, because the bUse_mtthis may be set. */
      uBody.append(gen_statementBlock(zbnfConstructor, 1, statementBlockCtorFrame, classData.classTypeInfo, 'c'));  //it is a statementBlock
    } else {
      uBody.append("/*J2C:No body for constructor*/\n");
    }
    if(bUse_mtthis){
      sLine_mtthis = "\n  Mtbl_" + sDeclaringClassIdentName + " const* mtthis = &mtbl" + sDeclaringClassIdentName + ".mtbl;";
    } else {
      sLine_mtthis = "";
    }
    StringBuilder uContent = new StringBuilder(10000);
    writeContent.writeClassC("\n\n/*Constructor */");
    if(zbnfConstructor == null)
    { writeContent.writeClassC("/**J2C: autogenerated as default constructor. */");
    }
    //NOTE: is part of gen_methodHead(): else { writeContent.writeClassC("/**" + get_description(itemConstructor) + "*/"); }
      
    //Method method = gen_methodHead(itemConstructor, classData, "ctorO", statementBlockCtorFrame.localIdents, classData.classTypeInfo, bArgumentSensitive, 'c'); 
    //String sMethodHead = method.getMethodHeadDefiniton();
    writeContent.writeClassC("\n" + methodDef.method.gen_MethodHeadDefinition()); //sMethodHead);

    if(isBasedOnObject)
    { writeContent.writeClassC("\n{ " + sClassName + "_s* ythis = (" + sClassName + "_s*)othis;  //upcasting to the real class.");
      //writeContent.writeClassC("\n  int sizeObj = getSizeInfo_ObjectJc(othis);");
      //writeContent.writeClassC("\n  extern ClassJc const reflection_" + sClassName + "_s;");
      writeContent.writeClassC(sLine_mtthis); //may be empty
      writeContent.writeClassC("\n  STACKTRC_TENTRY(\"ctorO_" + sClassName + "\");");
  
      //TODO: super(xyz)
      writeContent.writeClassC("\n  checkConsistence_ObjectJc(othis, sizeof(" + sClassName + "_s), null, _thCxt);  ");
    }
    else
    { writeContent.writeClassC("\n{ " + sClassName + "_s* ythis = PTR_MemC(mthis, " + sClassName + "_s);  //reference casting to the real class.");
      writeContent.writeClassC("\n  int sizeObj = size_MemC(mthis);");
      writeContent.writeClassC(sLine_mtthis); //may be empty
      writeContent.writeClassC("\n  STACKTRC_TENTRY(\"ctor_" + sClassName + "\");");
      writeContent.writeClassC("\n  if(sizeof(" + sClassName + "_s) > sizeObj) THROW_s0(IllegalArgumentException, \"faut size\", sizeObj);");
    }
    /**Super call*/
    { ClassData superClass = classData.getSuperClassData();
      if(superClass != null && superClass != CRuntimeJavalikeClassData.clazzObjectJc){
        final String nameCtor = classData.isBasedOnObject()?  "ctorO" : "ctorM";
        ZbnfParseResultItem zbnfSuperCall = zbnfConstructor == null ? null : zbnfConstructor.getChild("superCall");
        CCodeData ccode;
        StringBuilder uSuperCtor = new StringBuilder(4000);
        if(zbnfSuperCall != null){
          stop();
          ccode = statementBlockCtorFrame.gen_InternalMethodCall(zbnfSuperCall, null, nameCtor, superClass, null, classData.ctorCodeInfo.cCode); //, superClass.classLevelIdents);
          uSuperCtor.append(ccode.cCode);
        } else if(methodDef.supermethod != null){
        	//In anonymous classes, the super constructor should be called with the values of param.
        	//The user writes new SuperType(param){....}
        	
        	uSuperCtor.append(methodDef.supermethod.sCName).append("(");
        	uSuperCtor.append("&ythis->base.object");
        	if(methodDef.supermethod.paramsType !=null){
	        	for(FieldData param: methodDef.supermethod.paramsType){
	        		uSuperCtor.append(", ").append(param.getName());
        	} }
        	if(methodDef.supermethod.need_thCxt){
        		uSuperCtor.append(", _thCxt)");  
          } else {
           	uSuperCtor.append(")");  
          }
        }

        else {
          /**Call the default constructor: */
          ccode = statementBlockCtorFrame.gen_InternalMethodCall(null, null, nameCtor, superClass, null, classData.ctorCodeInfo.cCode); //, superClass.classLevelIdents);
          uSuperCtor.append(ccode.cCode);
        }
        writeContent.writeClassC("\n  //J2C:super Constructor\n  ");
        uSuperCtor.append(";");
        writeContent.writeClassC(uSuperCtor.toString());
      }  
    }  
    if(isBasedOnObject) {
      /**Sets the reflection after call of super constructor, because the super constructor sets it also, but to super class. */
      writeContent.writeClassC("\n  setReflection_ObjectJc(othis, &reflection_" + sClassName + "_s, sizeof(" + sClassName + "_s));  ");
    }
    if(classData.isNonStaticInner){
    	writeContent.writeClassC("\n  ythis->outer = outer;");
    }
    /**Initialize all class variables. */
    String sAllInit= "";
    writeContent.writeClassC("\n  //j2c: Initialize all class variables:\n  {");
    for(ClassData.InitInfoVariable variableToInit : classData.getVariablesToInit())
    { 
      String sName = variableToInit.identInfos.getName();
      if(sName.equals("timeOpen"))
        stop();
      if("sSd".indexOf(variableToInit.identInfos.modeStatic)<0)  //don't regard static variable here!
      {
        ClassData[] typeValue1 = new ClassData[1];  //classData of the part of expression
        CCodeData cVariableToInit = new CCodeData("ythis->" + sName, variableToInit.identInfos);
        if(cVariableToInit.cCode.equals("ythis->main2"))
          stop();  //kk1
        String cInit = statementBlockCtorFrame.gen_assignValue(cVariableToInit, "=", typeValue1, variableToInit.zbnfInit, null, 3, classData.classLevelIdents, 'i');
        sAllInit += "\n    " + cInit + ";";
      }      
    }      
    writeContent.writeClassC(statementBlockCtorFrame.gen_NewObjReferences(2));
    writeContent.writeClassC(statementBlockCtorFrame.gen_TempStringBufferReferences(2));
    writeContent.writeClassC(statementBlockCtorFrame.gen_persistringVarDefinitions(2));
    writeContent.writeClassC(statementBlockCtorFrame.gen_TempRefs(2));
    writeContent.writeClassC(sAllInit);
    writeContent.writeClassC(statementBlockCtorFrame.gen_ActivateGarbageCollection(2, false, null));
    writeContent.writeClassC("\n  }");
    
    /**Writes the body. */
    writeContent.writeClassC(uBody.toString());
    writeContent.writeClassC("\n  STACKTRC_LEAVE;");
    writeContent.writeClassC("\n  return ythis;\n}\n\n");
  }

  
  
  
  public void write_finalizeDefinition
  ( ZbnfParseResultItem zbnfClass
  , String sClassName
  , LocalIdents idents
  ) throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  { String sClearRef = "";
    /*
  	List<ZbnfParseResultItem> listVariables = zbnfClass.listChildren("variableDefinition");
    if(listVariables != null)for(ZbnfParseResultItem zbnfVariable : listVariables)
    { if(zbnfVariable.getChild("static")== null)
      { //only non static members.
        String sName = zbnfVariable.getChild("variableName").getParsedString();
        if(sName.equals("msg"))
          stop();
        //ZbnfParseResultItem itemType = zbnfVariable.getChild("typeIdent");
        FieldData identInfo = idents.get(sName);
        char cModifier = identInfo.modeAccess;      
        if("@&".indexOf(cModifier) >=0)
        { //it is a reference, generate the test and clearBackRefJc
          //writeContent.writeClassC("\n  if(ythis->" + sName + ".ref != null) clearBackRefJc(&(ythis->" + sName + ".refbase));");
          sClearRef += "\n  CLEAR_REFJc(ythis->" + sName + ");";
        }
      }  
    }
    */
    StringBuilder uClearRef = new StringBuilder(1000);
    for(FieldData field: classData.classLevelIdentsOwn){
    	final String sName = field.getName();
  		if(field.modeStatic == '.' && field.modeAccess == '@'){ //only for non-static enhanced refs:
    		uClearRef.append("  CLEAR_REFJc(ythis->").append(sName).append(");\n");
    	}
    	if(field.modeStatic == '.' && field.modeAccess == '$'){ //non-static embedded instance
    		ClassData typeClazz = field.typeClazz;
    		while(typeClazz != null && !typeClazz.isFinalizeNeed()){
    			typeClazz = typeClazz.getSuperClassData();
    		}
    		if(typeClazz != null){
	    		final String sTypeName = typeClazz.getClassIdentName();
	    		uClearRef.append("  finalize_").append(sTypeName).append("_F(&ythis->")
	    		  .append(sName);
	    		if(field.modeArrayElement == 'B'){  //StringBuilder with fix buffer following
	    			uClearRef.append(".sb");
	      	}
	    		if(field.typeClazz.isBasedOnObject()){
	    			uClearRef.append(".base.object");
	    		}
	    		uClearRef.append(", _thCxt); //J2C: finalizing the embedded instance.\n");
    		}
    	}
    }
    //superclass-finalizing:
    final ClassData superclass = classData.getSuperClassData();
    if(superclass != null && superclass.isFinalizeNeed()){
	    final String sNameSuperclass = superclass.getClassIdentName();
	    uClearRef.append("  finalize_").append(sNameSuperclass).append("_F(");
	    if(superclass.isBasedOnObject() || superclass == CRuntimeJavalikeClassData.clazzObjectJc){
				uClearRef.append("&ythis->base.object");
			} else {
				uClearRef.append("&ythis->base.super");
			}
	  	uClearRef.append(", _thCxt); //J2C: finalizing the superclass.\n");
    }
	  //
    { /**Only if the finalize method has any content, it should be written: */  
      String sClassCtype_s = classData.getClassCtype_s();
      String sClassIdent = classData.getClassIdentName();
      sMethodNameCurrent = "finalize_" + sClassIdent + "_F";
      if(sClassIdent.equals("SetValueGenerator"))
        stop();
      writeContent.writeClassC("\n\n" + "void " + sMethodNameCurrent);
      if(classData.isBasedOnObject()){
        writeContent.writeClassC("(ObjectJc* othis, ThCxt* _thCxt)\n");
      	writeContent.writeClassC("{ " + sClassCtype_s + "* ythis = (" + sClassCtype_s + "*)othis;  //upcasting to the real class.\n ");
      } else {
        writeContent.writeClassC("(" + sClassIdent + "_s* ythis, ThCxt* _thCxt)\n{ ");
      }
      writeContent.writeClassC("STACKTRC_TENTRY(\"" + sMethodNameCurrent + "\");\n");
      if(classData.getBodyForFinalize() != null)
      { ZbnfParseResultItem itemBody = classData.getBodyForFinalize().getChild("methodbody");
        if(itemBody != null)
        { String sBody = gen_statementBlock(itemBody, 1, null, CRuntimeJavalikeClassData.clazz_void.classTypeInfo, 'f');  //it is a statementBlock
          writeContent.writeClassC(sBody);
        }
      }
      writeContent.writeClassC(uClearRef);  
      { writeContent.writeClassC("  STACKTRC_LEAVE;\n");
      }
      writeContent.writeClassC("}\n\n");
    }     
  }
  
  
  
  
  
  

  /**writes a method definition as C-method into the C-File.
   * The interface-method {@link iWriteContent#writeClassC(String)} is used to do it.
   * The content isn't written directly in the file, because some include-lines 
   * generated while running any methods and constructors of second pass should be written
   * before the text of this method.
   * <br>
   * Inside it is written or called:<ul>
   * <li><code>Type* ythis</code> as first and <code>ThCxt* _thCxt</code>
   *   as last argument of method head.  
   * <li>{@link gen_variableDefinition(ZbnfParseResultItem, LocalIdents, List, char intension)}
   *   to generate the arguments of the constructor, in Java2C.zbnf <code>< argumentList></code>
   *   and <code>< argument></code>
   * <li><code>{  StacktraceJc stacktrace...</code>-expressions.  
   * <li>{@link StatementBlock#gen_statementBlock(ZbnfParseResultItem, int, LocalIdents)}
   *   is used to generate the statement block of the constructor.  
   * </ul>
   * While generating the method it is possible that a type is used inside, which is unknown yet.
   * Than an include-line is generated. Because the internal structure of the type should be known,
   * the parsing of the Java Code and the run of its first pass is processed while running the method generation.
   * The second pass of this file is running later. This actions are done using 
   * {@link RunRequiredFirstPass_ifc#runRequestedFirstPass(String)}.
   *  
   * @param parent Parse result of <code>methodDefinition::=...</code>
   * @param sClassName Full Name of the class in C-Style where the method is member of.
   *        The class name is used as ythis-type and as C-method-identifier-postfix. 
   * @throws IOException
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IllegalArgumentException 
   */
  public void write_methodDefinition
  ( ClassData.MethodWithZbnfItem methodDef
  , String sClassName
  , LocalIdents parentIdents
  )
  throws IOException, ParseException, IllegalArgumentException, IllegalAccessException, InstantiationException
  { ZbnfParseResultItem zbnfMethod = methodDef.zbnfMethod;
    //A statement block as frame for the whole method, especially the formal arguments are localIdents of it:
    //StatementBlock statementBlockMethodFrame = new StatementBlock(new LocalIdents(classData.classLevelIdents,null), true);
    StatementBlock statementBlockMethodFrame = new StatementBlock(this, methodDef.method.getMethodIdents(), true, 1);
    //LocalIdents localIdents = new LocalIdents(classData.classLevelIdents);
    this.sMethodNameCurrent = methodDef.method.sImplementationName;  //used for STACKTRC_ENTRY.
    assert(classData == methodDef.method.declaringClass);
    String sDeclaringClassIdentName = classData.getClassIdentName();
    
    this.noStacktrace = methodDef.method.noStacktrace();
    
    /**Return type: */
    //ZbnfParseResultItem itemType = zbnfMethod.getChild("type");
    //ClassData typeClazz = getType(itemType, parentIdents);
    //String sType = typeClazz.getClassIdentName();
    final FieldData typeClazz = methodDef.method.returnType;
    //String sName = zbnfMethod.getChild("name").getParsedString();
    final String sName = methodDef.method.sJavaName;
    if(sName.equals("run"))  //NOTE: finalize Method see write_finalizeDefinition()
      stop();
    if(sName.equals("finalize"))  //NOTE: finalize Method see write_finalizeDefinition()
    { //done in first pass: classData.setBodyForFinalize(zbnfMethod);
    }
    else
    { 
      ZbnfParseResultItem zbnfMethodDescription = zbnfMethod.getChild("description");
      String sDescription = get_shortDescription(zbnfMethodDescription);
      if(sDescription != null)
      { writeContent.writeClassC("\n\n/**" + sDescription + "*/");
      }
      ZbnfParseResultItem zbnfBody = zbnfMethod.getChild("methodbody");
      if(!classData.isInterface() && zbnfBody != null) {
        bUse_mtthis = false;
        final String sBody;
        if(zbnfBody != null)
        { /**NOTE: first generate the body, because the bUse_mtthis may be set. */
          sBody = gen_statementBlock(zbnfBody, 1, statementBlockMethodFrame, typeClazz, 'm');  //it is a statementBlock
        }
        else {
          sBody = "/*J2C:No body of method given. It is abstract. */";
          assert(false);
        }
        
        writeContent.writeClassC("\n" + methodDef.method.gen_MethodHeadDefinition()); //sMethodHead);
        writeContent.writeClassC("\n{ ");
        if(classData != methodDef.method.firstDeclaringClass)
        { /*the method is overridden, the reference to the instance is given as ithis, cast and test it. */
          String sClassType_s = classData.getClassCtype_s();
          writeContent.writeClassC(sClassType_s + "* ythis = (" + sClassType_s + "*)ithis;\n  ");
        }
        if(bUse_mtthis){
          /**The method table should be prepared for this method.*/
          String sLine = "Mtbl_" + sDeclaringClassIdentName + " const* mtthis = (Mtbl_" + sDeclaringClassIdentName 
            + " const*)getMtbl_ObjectJc(&ythis->base.object, sign_Mtbl_" + sDeclaringClassIdentName + ");\n  ";
          writeContent.writeClassC(sLine);
        }
        if(methodDef.method.need_thCxt){
          writeContent.writeClassC("\n  STACKTRC_TENTRY(\"" + sMethodNameCurrent + "\");");
        } else if(! noStacktrace){
          writeContent.writeClassC("\n  STACKTRC_ENTRY(\"" + sMethodNameCurrent + "\");");
        }
        
        writeContent.writeClassC("\n  ");  
        writeContent.writeClassC(sBody);
        
        //if(!genBlockStatment.lastWasReturn)
        if(! noStacktrace)
        { writeContent.writeClassC("\n  STACKTRC_LEAVE;");
        }
        writeContent.writeClassC("\n}\n");
      }
      if(methodDef.method.isOverrideable()) {
        String sClassIdentNameFirst = methodDef.method.firstDeclaringClass.getClassIdentName();
        Method methodFirst = methodDef.method.primaryMethod;
        if( methodFirst.sNameUnambiguous.equals("flush"))
          stop();
        /**Writes the dynamic call implementation variant: */
        writeContent.writeClassC("\n/*J2C: dynamic call variant of the override-able method: */");
        writeContent.writeClassC("\n" + methodDef.method.sReturnTypeDefinition + " " 
                                + methodDef.method.sCName + methodDef.method.sMethodFormalListDefiniton);
        final String ythis;
        final String othis;
        if(  methodFirst.declaringClass.isInterface() 
          || methodFirst.declaringClass == CRuntimeJavalikeClassData.clazzObjectJc){
          ythis = "ithis";
          othis = "ithis";
        }
        else if(methodDef.method != methodFirst) {
          /**It is a overridden method: */
          ythis = "(" + methodFirst.declaringClass.sClassNameC + "*)" + "ithis";
          othis = "&ithis->base.object";
        }
        else {
          /** It is the first defined method: */
          ythis = "ythis";
          othis = "&ythis->base.object";
        }
        
        writeContent.writeClassC("\n{ Mtbl_" + sClassIdentNameFirst + " const* mtbl = (Mtbl_" 
            + sClassIdentNameFirst + " const*)getMtbl_ObjectJc(" + othis + ", sign_Mtbl_" + sClassIdentNameFirst + ");");
        if(methodDef.method.returnType != null && methodDef.method.returnType.typeClazz != CRuntimeJavalikeClassData.clazz_void){
          writeContent.writeClassC("\n  return ");
        } else {
          writeContent.writeClassC("\n  ");
        }
        writeContent.writeClassC("mtbl->" + methodFirst.sNameUnambiguous + "(" + ythis);
        if(methodDef.method.paramsType != null) for(FieldData param: methodDef.method.paramsType){
          writeContent.writeClassC(", " + param.getName());
        }
        if(methodDef.method.need_thCxt){ 
          writeContent.writeClassC(", _thCxt"); 
        }
        writeContent.writeClassC(");");
        writeContent.writeClassC("\n}\n");
        
      }
    }  
  }


  /**Generates C-code from a parsed statement block.
   * <ul>
   * <li>All < variableDefinition> of the < statement> block were generated using 
   * {@link GenerateClass#gen_variableDefinition(ZbnfParseResultItem, LocalIdents, List, char)}.
   * It should be done first because in C all variable should be define on the begin of a block.
   * The variable identifiers and types are stored in a local copy of {@link LocalIdents}, which are intialized
   * with the content of the localIdentsP given as argument, which comes either from the parent block or from the class.
   * <li>All assignments to variable are generated using {@link #gen_VariableInitAssignment(ZbnfParseResultItem, int, LocalIdents)}. 
   * It is separated from the variable definition and it isn't implemented as initializing of variable,
   * because in Java there are more variants. In C the <code>type name = value;</code> is an initializing of the variable,
   * but <code>type name; ...; name = value;</code> it is an assignment. It is a fine difficult explainable difference.
   * The Java2C produce assignments, not initializing.  
   * <li>All < statement> are generated, using {@link #gen_statement(ZbnfParseResultItem, int, LocalIdents)}.
   *   Therefore a separate String variable is used, because MemC-elements, see next:
   * <li>For all <code>new</code>-statements, the <code>MemC</code> definitions are generated. 
   * They are named with a incremental number. They are placed after the variable definitions of the block
   * but before the statements. This is necessary because the <code>MemC memX</code> is an variable also.
   * <li>The call of <code>activateGarbageCollectorAccess_BlockHeapJc(memX)</code> is generated for all MemC-elements.
   *   The MemC-Elements are countered on level of the statement block, represented by this class.
   *   This is also done in {@link #gen_statement(ZbnfParseResultItem, int, LocalIdents)} before a <code>return</code> statement.
   *   It is not done if the last statement was a <code>return</code>.
   *   Thats why it is done with an extra method {@link #gen_ActivateGarbageCollection(int)}.
   * </ul>
   * @param zbnfStatementBlock The ZBNF parse result item which is a < statementBlock>
   * @param indent number of indentation in the generated C-code. It is the level of blocks.
   * @param localIdentsP The local identifiers of the parent block.
   * @param intension Intension of call: 'c'-constructor body, 'm'-method body, 'b'internal block, 'f'-finalize body. 
   * @return The generated C-code for the block inclusive newlines, indentation and { }.
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   */
  public String gen_statementBlock
  ( ZbnfParseResultItem zbnfStatementBlock
  , int indent
  , StatementBlock parentBlock
  //, LocalIdents localIdentsP
  , FieldData typeReturn
  , char intension
  )
  throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  { boolean bFirst = true;
    String ret = GenerateClass.genIndent(indent) + "{ ";
    //LocalIdents localIdents = localIdentsP;
    StatementBlock statementBlock = parentBlock != null 
	  ? new StatementBlock(parentBlock)
	  : new StatementBlock(this, classData.classLevelIdents, true, indent+1)
	  ;
	  //
	  //comment block
	  Iterator<ZbnfParseResultItem> iterZbnfStatementBlock = zbnfStatementBlock.iterChildren("descriptionOfBlock");
	  if(iterZbnfStatementBlock != null)
	  { //if at least one variableDefinition exists, the localIdents should be local:
	  	while(iterZbnfStatementBlock !=null && iterZbnfStatementBlock.hasNext())
	  	{ ZbnfParseResultItem item = iterZbnfStatementBlock.next();
	  	 	ret += "//:" + item.getParsedString() + genIndent(indent+1); 
	    }
	  }
	 
	  //all variableDefinitions writing at begin of block, it is necessary in C-language:
    //The initialization of this variable were done at that position, were the variable is defined in Java.
    iterZbnfStatementBlock = zbnfStatementBlock.iterChildren("variableDefinition");
    if(iterZbnfStatementBlock != null)
    { //if at least one variableDefinition exists, the localIdents should be local:
      statementBlock.localIdents = new LocalIdents(statementBlock.localIdents, null);  //new based on existing
      while(iterZbnfStatementBlock !=null && iterZbnfStatementBlock.hasNext())
      { ZbnfParseResultItem item = iterZbnfStatementBlock.next();
        //don't init the variables like class variables, instead generate the assignment.
        //Because: The execution order of assignment should be bewared. Don't init at top of statement block.
        //The 3. argument is variablesToInit.
        ZbnfParseResultItem zbnfVariableDescription = item.getChild("description"); //may be null.
        CCodeData codeVariable = gen_variableDefinition(item, zbnfVariableDescription, statementBlock.localIdents, statementBlock, null, 'b');
        ret += genIndent(indent+1) + codeVariable.cCode;
      }
      ret += genIndent(indent+1);
    }
  
    /**accumulate the content of block in a variable, because before it the newObj-variables should be written. */
    String content = "";
    /**Statements of the block. They may contain several new(..) operations.
     * Every reference to a new Object is written in a newObj-variable. See nrofNew. 
     */
    //iter = zbnfStatement.iterChildren("statement");
    iterZbnfStatementBlock = zbnfStatementBlock.iterChildren();
    while(iterZbnfStatementBlock !=null && iterZbnfStatementBlock.hasNext())
    { ZbnfParseResultItem zbnfStatement = iterZbnfStatementBlock.next();
      final String semantic = zbnfStatement.getSemantic();
      final boolean isStatement = semantic.equals("statement");
      final boolean isVariableInitAssignemnt = semantic.equals("variableDefinition");
      //Note: beware the order of execution of variable initialization. Don't init at top of statement block,
      if(isStatement || isVariableInitAssignemnt)
      { if(bFirst)
        { //no indentation before first statement. 
          bFirst = false; 
        }
        else
        { //indentation for a next line.
          content += genIndent(indent+1); 
        }
      }
      if(isVariableInitAssignemnt)
      { //all variabledefinitions should have its initial value assignment.
        //the definition itself is translated before, therefore the zbnfStatement is evaluated twice. 
        content += statementBlock.gen_VariableInitAssignment(zbnfStatement, indent+1); //, statementBlock.localIdents);
      }
      else if(isStatement)        
      { content += statementBlock.gen_statement(zbnfStatement, indent+1, statementBlock.localIdents, typeReturn, intension);
      }
      else
      { //other items may be description etc.
        //ignore it here.
        stop();
      }
    }
    
    /**The statements of the block are generated, containing in 'content'.
     * Now write all variable definitions for newObj:
     */
    ret += genIndent(indent+1);
    ret += statementBlock.gen_NewObjReferences(indent);
    ret += statementBlock.gen_TempStringBufferReferences(indent);
    ret += statementBlock.gen_persistringVarDefinitions(indent);
    //ret += statementBlock.gen_MtblReferences(indent);
    ret += statementBlock.gen_TempRefs(indent);
    ret += genIndent(indent+1);
    
    
    /**Add the content. */
    ret += content; 
    
    /**All references t new Objects should be managed by Garbage Collector. this.nrofNew is used. */
    if(!statementBlock.lastWasReturn)
    { //This call was be done also on a return statement. If it is the last, don't write second, -unreachable code.
      ret += statementBlock.gen_ActivateGarbageCollection(indent+1, false, null);
    }
    /**The end*/
    ret += genIndent(indent) + "}";
    return ret;
  }


  public String gen_for_statement
  ( final ZbnfParseResultItem itemStatement
  , int indent
  , final StatementBlock parentStatementBlock
  //, final LocalIdents localIdentsP
  , FieldData typeReturn
  ) throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException  
  { String ret = ""; //new StringBuffer();
    //final LocalIdents localIdents;
    ZbnfParseResultItem zbnfIteratorObject = itemStatement.getChild("variableDefinition");
    final String startAssignment;
    final StatementBlock statementBlock;
    if(zbnfIteratorObject != null)
    { statementBlock = new StatementBlock(parentStatementBlock);
      statementBlock.localIdents = new LocalIdents(statementBlock.localIdents, null);  //new based on existing
      List<ClassData.InitInfoVariable> variablesToInit1 = new LinkedList<ClassData.InitInfoVariable>();
      CCodeData codeVariable = gen_variableDefinition(zbnfIteratorObject, null, statementBlock.localIdents, statementBlock, variablesToInit1, 'z');
      indent +=1;
      ret += "{ "+ codeVariable.cCode + genIndent(indent); 
      ClassData.InitInfoVariable variableToInit = variablesToInit1.get(0);
      { String sName = variableToInit.identInfos.getName();
        //String sType = variableToInit.identInfos.getTypeName();
        ClassData[] typeValue = new ClassData[1];  //classData of the part of expression
        final String startValue = statementBlock.gen_value(variableToInit.zbnfInit, null, typeValue
        	, statementBlock.localIdents, codeVariable.identInfo.modeStatic=='n', 'e');
        startAssignment = sName + " = " + startValue; 
      }          
    }
    else
    { statementBlock = parentStatementBlock;
      ZbnfParseResultItem zbnfStartAssignment = itemStatement.getChild("startAssignment");
      startAssignment = statementBlock.gen_assignment(zbnfStartAssignment, null, indent+1, statementBlock.localIdents, 'b');
    }
    
    ZbnfParseResultItem zbnfEndCondition = itemStatement.getChild("endCondition");
    ClassData[] retTypeValue = new ClassData[1];
    String sCondition = statementBlock.gen_value(zbnfEndCondition, null, retTypeValue, statementBlock.localIdents, true, 'e');
    
    final String sIteratorExpression;
    final ZbnfParseResultItem zbnfIteratorExpression = itemStatement.getChild("iteratorAssignment");
    if(zbnfIteratorExpression !=null){
      sIteratorExpression = statementBlock.gen_assignment(zbnfIteratorExpression, null, indent, statementBlock.localIdents, 'z');
    } else {
    	//If a iteratorAssignment isn't found, an iteratorExpression is present. See syntax.
    	//There the child is relevant for the simple value.
    	final ZbnfParseResultItem zbnfIteratorVariable = itemStatement.getChild("iteratorExpression").firstChild();
      CCodeData cIterExpression = statementBlock.gen_simpleValue(zbnfIteratorVariable, null, statementBlock.localIdents, false, 'z', false);
      sIteratorExpression = cIterExpression.cCode;
    }
    //String sIteratorExpression = statementBlock.gen_value(zbnfIteratorExpression, retTypeValue, statementBlock.localIdents, 'e');
    
    ret += "for(" + startAssignment + "; "
        + sCondition + "; " + sIteratorExpression + ")"; // + (genIndent(indent));
    
    ZbnfParseResultItem itemWhileStatement = itemStatement.getChild("statement");
    String sStatement = itemWhileStatement != null 
                      ? statementBlock.gen_statement(itemWhileStatement, indent+1, statementBlock.localIdents, typeReturn, 'z') 
                      : ";";
    ret += sStatement;
    
    if(zbnfIteratorObject != null)
    { ret += genIndent(indent-1) + "}";
    }
    return ret;          
  }


  
  

  void _assert(boolean cond){
    if(!cond)
      throw new RuntimeException("assertion");
  }


}
