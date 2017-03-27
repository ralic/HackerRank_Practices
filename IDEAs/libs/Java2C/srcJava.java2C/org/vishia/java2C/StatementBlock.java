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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.vishia.zbnf.ZbnfParseResultItem;

/**This class generates all statements in one block. It is instanciated temporary
 * for any statement block. 
 * @author Hartmut
 *
 */
public class StatementBlock
{
	private final GenerateClass genClass;
	private final SecondPass secondpass;

  /**The number of call of <code>new</code> in this blockStatment. 
   * The call of new is counted, generates some special statements in C.
   */
  private int nrofNew; 
  
  private int nrofPersistentStrings = 0;
  
  private LinkedList<FieldData> tempRef;
  
  /**Counter for <code>"_temp" + nrofTempRefForConcat</code> of this block.
   */
  private int nrofTempRefForConcat;
  
  /**Counter for <code>"_temp" + nrofStringBufForConcat</code> of this block.
   */
  private int nrofStringBufForConcat;
  
  /**Counter for <code>"_mtbl8_9"</code> of this block.
   */
  private int nrofMTBRef;
  
  /**If set >0, a StringBuilderJc-instance with this direct buffer size 
   * should be generate in this block-level. */
  private int sizeStringBuilderInStack = 0;
  
  private boolean needPtrStringBuilderInThCxt = false;
  
  private LinkedList<Integer> sizesStringBufferConcat;
  
  
  /**Indentation of the block.
   * It is valid for the statements. The opened and close curly bracket have indent-1. 
   */
  private final int indent;
  
  /**The deepness of block nesting. */
  public final int blockNestingCnt;

  /**Saves, whether the last generated statement is a return. If it is not so, 
   * some instructions at end of block have to be done.
   */
  public boolean lastWasReturn = false;

  /**The identifiers valid at this block level. 
   * Note: The definition is not final because at first the LocalIdents of the level above
   * are referenced here. Only if any variable or type defined in this block, an own instance
   * is created and the reference is replaced.*/
  public LocalIdents localIdents;
 
  
  /**If any dynamic call occurs in this statement block without local knowledge of the correct interface type,
   * a helpness variable is created. This list contains all of it.
   */
  private List<FieldData> mtblVariables;
  
  /**The parent statement block. It is necessary at ex. for gen_ActivateGarbageCollection()
   * called at return statement.
   * @param parentIdents
   */
  final StatementBlock parent;
  
  StatementBlock(StatementBlock parent)
  { //without any own variable of the block, it are equal to parent idents.
    //if any variable definition is found, a new LocalIdents(parent) is called.
    this.genClass = parent.genClass;
    this.secondpass = parent.secondpass;
    this.parent = parent;
    this.indent = parent.indent +1;
    if(parent != null){
      blockNestingCnt = parent.blockNestingCnt +1;
      nrofNew = 0; //(parent.nrofNew & 0xff) + 0x100;
      localIdents = parent.localIdents;
    }
    else {
      blockNestingCnt = 0;
      nrofNew = 0;
      localIdents = null;
    }
  }
  
  StatementBlock(GenerateClass genClass, LocalIdents parentLocalIdents, boolean bClassLevel, int indent)
  { //without any own variable of the block, it are equal to parent idents.
    //if any variable definition is found, a new LocalIdents(parent) is called.
  	this.genClass = genClass;
  	this.secondpass = genClass instanceof SecondPass ? (SecondPass)genClass : null; //unused then
    	this.parent = null;
      this.indent = indent;
      blockNestingCnt = 0;
      nrofNew = 0;
      localIdents = parentLocalIdents;
    }
 
    
    
    /**Returns a new identifier for a newObject. Counts the {@link #nrofNew} thereby.
   */
  public String gen_newObj()
  { nrofNew +=1;
    return "newObj" + blockNestingCnt + "_"  + nrofNew;
  }

  /**Generates all variables which are used for newObject (<code>new</code> operator).
   * @param indent indentation.
   * @return String with C-Code
   */
  String gen_NewObjReferences(int indent)
  { String ret = "";
    if(nrofNew >0)
    { ret += //genIndent(indent+1) + 
             "ObjectJc ";
      int ctNrofNew = 0;
      while(++ctNrofNew < nrofNew)
      { ret += "*newObj" + blockNestingCnt + "_" + ctNrofNew + "=null, "; //1..n-1 with ','
      }
      ret += "*newObj" + blockNestingCnt + "_"  + ctNrofNew + "=null;";  //the last without ','
      ret += " //J2C: temporary Objects for new operations" + GenerateClass.genIndent(indent+1);
    }
    return ret;
  }    
  
  
  
  /**Returns a new identifier for a persistent String. Counts the {@link #nrofPersistentStrings} thereby.
   */
  public String gen_persistringVariable()
  { nrofPersistentStrings +=1;
    return "_persistring" + blockNestingCnt + "_"  + nrofPersistentStrings;
  }

  /**Generates all StringJc-variables which are used to build persistent Strings.
   * @param indent indentation.
   * @return String with C-Code
   */
  String gen_persistringVarDefinitions(int indent)
  { String ret = "";
    if(nrofPersistentStrings >0)
    { ret += //genIndent(indent+1) + 
             "StringJc ";
      int ctNrofPersistentStrings = 0;
      while(++ctNrofPersistentStrings < nrofPersistentStrings)
      { ret += "_persistring" + blockNestingCnt + "_" + ctNrofPersistentStrings + "=NULL_StringJc, "; //1..n-1 with ','
      }
      ret += "_persistring" + blockNestingCnt + "_"  + ctNrofPersistentStrings + "=NULL_StringJc;";  //the last without ','
      ret += " //J2C: temporary persistent Strings" + GenerateClass.genIndent(indent+1);
    }
    return ret;
  }    
  
  
  
  /**Counts the {@link #nrof} and returns the name of a reference for new Objects.
   */
  public String gen_tempString()
  { nrofStringBufForConcat +=1;
    return "_tempString" + blockNestingCnt + "_"  + nrofStringBufForConcat;
  }

  
  /**Generates all variables which are used for new statements.
   * @param indent indentation.
   * @return String with C-Code
   */
  String gen_TempStringBufferReferences(int indent)
  { String ret = "";
    if(sizeStringBuilderInStack >0){
		  ret += "struct _stringBuilder_t{ StringBuilderJc u; char _b[" + sizeStringBuilderInStack + "-4]; }"
  	      + "_stringBuilder = { CONST_addSizeStack_StringBuilderJc(&_stringBuilder.u, "+ sizeStringBuilderInStack + "-4), {0}};"
  	      + GenerateClass.genIndent(indent+1);
    }
    if(needPtrStringBuilderInThCxt){
      ret += "StringBuilderJc* _stringBuilderThCxt = threadBuffer_StringBuilderJc(_thCxt);"
  	      + GenerateClass.genIndent(indent+1);
    }
    if(nrofStringBufForConcat >0)
    { Iterator<Integer> iSize = (sizesStringBufferConcat != null) ? sizesStringBufferConcat.iterator() : null;
    	int sizeInfo = (iSize != null && iSize.hasNext()) ? iSize.next() : 0;
      int ctnrofStringBufForConcat = 0;
      ret += " //J2C: temporary Stringbuffer for String concatenation" + GenerateClass.genIndent(indent+1);
      while(++ctnrofStringBufForConcat <= nrofStringBufForConcat)
      { if((sizeInfo >>16) == ctnrofStringBufForConcat){
      	  String sName = "_tempString" + blockNestingCnt + "_" + ctnrofStringBufForConcat; 
      	  int sizeBuffer = sizeInfo & 0xffff;
      	  ret += "struct " + sName + "_t{ StringBuilderJc u; char _b[" + sizeBuffer + "-4]; }"
      	      + sName + " = { CONST_addSizeStack_StringBuilderJc(&" + sName + ".u, "+ sizeBuffer + "-4), {0}};"
      	      + GenerateClass.genIndent(indent+1);
      	  //next sizeInfo, maybe 0:
      	  sizeInfo = (iSize != null && iSize.hasNext()) ? iSize.next() : 0;
      	} else {
      	  ret += "StringBuilderJc* _tempString" + blockNestingCnt + "_" + ctnrofStringBufForConcat + "=null; "
      	      + GenerateClass.genIndent(indent+1);
        }
      }
    }
    return ret;
  }    
  
  
  
  
  /**Generates a new identifier for a _temp8_9-variable and fills it with the given value.
   * @param src the type which should stores in the ref
   * @param modeDef 'n':_new8_9 created '&':_mtb8_9 else:_temp8_9
   * @return
   */
  private final String tempRefForConcat(FieldData src)
  { if(tempRef == null)
    { /**First call*/
      nrofTempRefForConcat = 0;
      tempRef = new LinkedList<FieldData>(); 
    }
    nrofTempRefForConcat +=1;
    if(src.modeAccess == '%')
    	stop();
    final String name1;
    final char accessMode;
    final char accessModeNoMtb;
    switch(src.modeAccess){
    case 't': accessModeNoMtb = 't'; break;
    case '&': accessModeNoMtb = '*'; break; //store MTB-ref in normal ref. 
    case '@': accessModeNoMtb = '*'; break; //store enhanced ref in normal ref. 
    case '*': accessModeNoMtb = '*'; break;
    default: accessModeNoMtb = '?'; assert(false);
    }
    char modeDef = src.modeAccess == '&' ? '&'  //dedicates an MTB-ref
    		         : src.modeStatic;              //'n' for stack-local field which contains new instance
    switch(modeDef){
    case 'n': name1 = "_new"; accessMode = accessModeNoMtb; break;   //stores result of return-new method
    case '&': name1 = "_mtb"; accessMode = '&'; break;   //stores MTB
    default:  name1 = "_temp"; accessMode = accessModeNoMtb; break;  //stores concatenation temp result. 
    }
    String name = name1 + blockNestingCnt + "_" + nrofTempRefForConcat; 
    if(name.equals("_mtb0_1"))
    	stop();
  	final FieldData ref = new FieldData(name, src.typeClazz, null, null, null
    		, src.modeStatic, accessMode, 0, null, '.', null); 
    tempRef.add(ref);
    return name;
  }

  /**Generates a new identifier for a _mtbl8_9-variable and fills it with the given value.
   * @param ref the value which should stores in the ref
   * @return
   */
  private final String genTemp_mtblRef(FieldData typeMtb, CCodeData ref)
  { final String sRet;
  	final String sMtbl = tempRefForConcat(typeMtb); 
    final String sTypeMtb = typeMtb.typeClazz.getClassIdentName();
    final String sSrcRef = typeMtb.testAndcast(ref, '*');
    sRet = "( " + sMtbl + ".ref = " + sSrcRef 
         + GenerateClass.genIndent(indent+2) + ", "  + sMtbl + ".mtbl = (Mtbl_" + sTypeMtb + " const*)getMtbl_ObjectJc(&" + sMtbl 
         + ".ref->base.object, sign_Mtbl_" + sTypeMtb + ")" 
         + GenerateClass.genIndent(indent+2) + ", " + sMtbl + ")"
         + GenerateClass.genIndent(indent+1);
    return sRet;
  }

  /**Generates all variables which are used for temporary references for concatenation-disentangle.
   * @param indent indentation.
   * @return String with C-Code
   */
  String gen_TempRefs(int indent)
  { final String ret;
  	if(tempRef != null)
    { StringBuilder uRet = new StringBuilder(1000);
      int idx = 1;
      //ret = GenerateClass.genIndent(indent+1);
      for(FieldData tempRefField: tempRef){
        if(tempRefField.getName().equals("_mtb0_1"))
        	stop();
      	if(tempRefField.modeAccess != '?'){
        	String sType = tempRefField.typeClazz.sClassNameC;
          /*
        	final char modeDefinition = tempRefType.modeStatic;
          String sName = (modeDefinition == 'n' ? " _new" : " _temp") + blockNestingCnt + "_" + idx; 
          final String sAccess;
          final String sInit;
          switch(tempRefType.modeAccess)
          { case '~': case '*': sAccess = "*"; sInit = " = null;"; break;
            case 't': sAccess = ""; sInit = " = NULL_StringJc;"; break;
            default: sAccess = null; sInit = null; assert(false);
          }
          ret += sType + sAccess + sName + sInit;  //all in one line, it may be not some more
          */
        	String sVariable = tempRefField.gen_VariableDefinition('b');
          uRet.append(sVariable).append("; ");  
        }
        idx +=1;
      }
      uRet.append("//J2C: temporary references for concatenation")
          .append(GenerateClass.genIndent(indent+1));
      ret = uRet.toString();
    } else {
    	ret = "";
    }
    return ret;
  }    
  
  
  /**generates the statement for <code>activateGarbageCollectorAccess_BlockHeapJc()</code>.
   * This is placed on end of a statement block or before an <code>return</code>-statement,
   * if inside new-statements are generated.
   * 
   * @param indent number of indentations. 2 spaces per indentation are produced.
   * @return The statement as string with indentation +1, but without newline.
   */
  public String gen_ActivateGarbageCollection(int indent, boolean bRet, CCodeData cCodeReturn)
  { String ret = "";
    //final boolean bReturnRef;
    //final int indent1;
    //final String retBlock;
    //String retDeduceStatement;
    final String sAddrExcl;
    StatementBlock blockLevel = this;
    if(  cCodeReturn != null 
      && cCodeReturn.identInfo.typeClazz.isString()
      ) {
      sAddrExcl = "PTR_StringJc(" + cCodeReturn.cCode + ")";
	  } else if(  cCodeReturn != null 
      && !cCodeReturn.identInfo.typeClazz.isPrimitiveType()
      && !cCodeReturn.cCode.equals("ythis")
      ) {
      /**Problem: The method may return an instance in the block heap. Don't activate garbage collection for it: */
      //retDeduceStatement = GenerateClass.genIndent(indent) + "{ struct BlockHeapJc_t* heap = null; struct BlockHeapBlockJc_t* returnedBlock = deduceBlockHeapBlockFromObject(" + cCodeReturn.cCode + ", &heap);";
      //bReturnRef = true;
      //indent1 = indent+1;
      //retBlock = "returnedBlock";
      sAddrExcl = cCodeReturn.cCode;
    } else {  
      //bReturnRef = false;
      //retDeduceStatement = null;
      //indent1 = indent;
      //retBlock = "null";
      sAddrExcl = "null";
    }
    do {
      int ctNrofNew = 0;
      while(++ctNrofNew <= blockLevel.nrofNew)
      { //if(retDeduceStatement != null){ 
        //  ret += retDeduceStatement; retDeduceStatement = null;
        //}
        ret += GenerateClass.genIndent(indent) + "activateGarbageCollectorAccess_BlockHeapJc(newObj" 
               + blockLevel.blockNestingCnt + "_"  + ctNrofNew + ", " + sAddrExcl + ");";
      }
      ctNrofNew = 0;
      //
      while(++ctNrofNew <= blockLevel.nrofPersistentStrings)
      { //if(retDeduceStatement != null){ 
        //  ret += retDeduceStatement; retDeduceStatement = null;
        //}
        ret += GenerateClass.genIndent(indent) + "activateGarbageCollectorAccess_BlockHeapJc(PTR_StringJc(_persistring" 
               + blockLevel.blockNestingCnt + "_"  + ctNrofNew + "), " + sAddrExcl + ");";
      }
      ctNrofNew = 0;
      //
      Iterator<Integer> iSize = (sizesStringBufferConcat != null) ? sizesStringBufferConcat.iterator() : null;
    	int sizeInfo = (iSize != null && iSize.hasNext()) ? iSize.next() : 0;
      while(++ctNrofNew <= blockLevel.nrofStringBufForConcat)
      { //if(retDeduceStatement != null){ 
        //  ret += retDeduceStatement; retDeduceStatement = null;
        //}
        if((sizeInfo >>16) == ctNrofNew){
      	  //no garbage, get next sizeInfo, maybe 0:
      	  sizeInfo = (iSize != null && iSize.hasNext()) ? iSize.next() : 0;
        } else {
      	  ret += GenerateClass.genIndent(indent) + "activateGarbageCollectorAccess_BlockHeapJc(&_tempString" 
               + blockLevel.blockNestingCnt + "_"  + ctNrofNew + "->base.object, " + sAddrExcl + ");";
        }  
      }
      if(blockLevel.tempRef != null)
      { /**Activates Garbage Collection for all temp-references, 
         * which holds references to return-new data.
         */
      	//int idx = 1;
        for(FieldData tempRefType: blockLevel.tempRef){
          //String sType = tempRefType.typeClazz.sClassNameC;
          final char modeDefinition = tempRefType.modeStatic;
          if(modeDefinition == 'n') {
            String sName = tempRefType.getName(); //(modeDefinition == 'n' ? "_new" : "_temp") + blockLevel.blockNestingCnt + "_" + idx; 
            final String sAccess;
            switch(tempRefType.modeAccess)
            { case '~': case '*': sAccess = sName; break;
              case 't': sAccess = "PTR_StringJc(" + sName + ")"; break;
              case '$': case '%':  assert(false);
              default: sAccess = null; assert(false);
            }
            //if(retDeduceStatement != null){ 
            //  ret += retDeduceStatement; retDeduceStatement = null;
            //}
            ret += GenerateClass.genIndent(indent) + "activateGarbageCollectorAccess_BlockHeapJc(" + sAccess + ", "+ sAddrExcl + ");";
            //idx +=1;
          }  
        }
      }
    } while(bRet && (blockLevel = blockLevel.parent) != null);  //repeat it only if return for all block levels.
    /*if(   bReturnRef                   //a deduce statement is prepared.
       && retDeduceStatement == null   //and it is written.
       ){
      ret += GenerateClass.genIndent(indent) + "}";
      }
    */
    return ret;
  }
  
  
  
  
  /**generates a statement from given parse result item < statement>.
   * It may be a simple call, an assignment, an if-statement and so on or a < statementBlock>
   * <ul>
   * <li>A comment in /*...* / found in Java-code before the statement are generated also in C
   *     before the statement in an extra line calling {@link get_description(ZbnfParseResultItem)}
   * <li>On < assignment> in ZBNF, {@link gen_assignment(ZbnfParseResultItem, int indent, LocalIdents)} is be called immediate.    
   * <li>On < statementBlock> in ZBNF, {@link gen_statementBlock(ZbnfParseResultItem, int indent, LocalIdents)} 
   *     is be called immediate with a new instance of {@link StatementBlock}.    
   * <li>An < if_statement> in ZBNF consists of a < condition>, a < statement> and optional a < elseStatement>.
   *    The condition will be translated in C with {@link gen_value(ZbnfParseResultItem, LocalIdents, char)}.
   *    As well as the condition occupies more as one line in Java, in C it is written yet in one line.
   *    Typically it is a suitable behavior. But if the condition expression is complex,
   *    it is not so ideal in C. The reason for this behavior: The parser ignore new lines, 
   *    all are white spaces. The C-code is correct, but possibly not ideal readable. It is a good style
   *    for writing comprehensible code separating a complex expressions in smaller parts. 
   *    To divide a conditional expression in smaller parts, some local boolean variable may be used.
   *    It is anyhow effective at machine level.
   *    <br>
   *    If the < statement> is really a simple statement, it will be written after the <code>if(...)</code> in the same line.
   *    But typically it is a statement block, and will be written in extra lines with the correct indentation.
   *    <br>
   *    If a construct <code>else if(...)</code> is given in Java, the <code>if(...)</code> after <code>else</code>
   *    is a simple statement. Therefore it was be produced as <code>else if(...)</code> also in C.
   *    It can be understand as a chain of if. 
   * <li>The behavior of the other control statements are adequate.
   * <li>On < return> statement some additional statements to finish a subroutine are generated,
   *     especially the handling of the <code>StacktraceJc</code>. 
   *     The {@link gen_ActivateGarbageCollection(int)} is called 
   *     because the statement block may contain one or some new-Statements.
   * <li>on < methodCall> in Java2C.zbnf-script, the {@link gen_simpleValue(ZbnfParseResultItem, LocalIdents, char intension)}
   *    is called with intension='m'. The methodcall is translated like a simple value, 
   *    it is syntactically the same. (void-value).
   * <li>On < throwNew> in Java2C.zbnf-script, the {@link gen_throwNew(ZbnfParseResultItem, LocalIdents)} is be called.
   * <li>On < break_statement> in Java2C.zbnf-script, a simple <code>break</code> is generated. 
   *    It are the same relations in Java likewise in C.
   * </ul>    
   * 
   * @param parent The ZBNF parse result item which is a < statement>
   * @param indent Number of nesting level of the block to generate indentations of a line.
   * @param localIdents The indentation of the block: TODO use it as class element.
   * @param typeReturn The return type of the superior method if it contains a return statement.
   * @param intension Intension of call: 'c'-constructor body, 'm'-method body, 'b'-internal block, 'z'-part of if, while etc., 'f'-finalize body. 
   * @return The statement in C stated without indentation and ended without newline, but with the end-semicolon.
   *         If the statement occupies more as one line, the indentation is generated correctly using the indent argument. 
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   */
  public String gen_statement
  ( ZbnfParseResultItem parent
  , int indent
  , LocalIdents localIdents
  , FieldData typeReturn
  , char intension
  )
  throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  { String ret = "";
    ClassData[] typeValue = new ClassData[1];  //classData of the part of expression
    //String ret = GenerateClass.genIndent(indent);
    ZbnfParseResultItem zbnfDescription = parent.getChild("description");
    if(true){
      String sDescription = genClass.get_shortDescription(zbnfDescription);
      if(sDescription != null)
      { ret += "/*" + sDescription + "*/" + GenerateClass.genIndent(indent);
        if(sDescription.equals("*Determines the position of an asterisk"))
        	stop();
      }
    } else {
      /**long description: */
      String sDescription1 = zbnfDescription.getChild("text").getParsedString();
      ret += "/*" + sDescription1 + "*/" + GenerateClass.genIndent(indent);
    }
    /**Iterate all parts of statement::=... */
    Iterator<ZbnfParseResultItem> iterStatement= parent.iterChildren();  //may consist of some parts one after another, not a loop.
    //NOTE: a <statement> has only one child because a statement syntax component is a alternative without loop.
    while(iterStatement.hasNext())
    { ZbnfParseResultItem itemStatement = iterStatement.next();
      lastWasReturn = false;  //default, set to true if it is a return.
      
      String semanticStatement = itemStatement.getSemantic();
      if(semanticStatement.equals("assignment"))
      { ret += gen_assignment(itemStatement, zbnfDescription, indent, localIdents, intension) + ";";
      }
      else if(semanticStatement.equals("return"))
      { ret += gen_returnStatement(itemStatement, zbnfDescription, typeReturn);
      }
      else if(semanticStatement.equals("statementBlock"))
      { ret += secondpass.gen_statementBlock(itemStatement, indent, this, typeReturn, 'b');
      }
      else if(semanticStatement.equals("if_statement"))
      { ZbnfParseResultItem itemCondition = itemStatement.getChild("condition");
        String sCondition = gen_value(itemCondition, zbnfDescription, typeValue, localIdents, true, 'e');
        ret += "if(" + sCondition + ") ";
        ZbnfParseResultItem ifStatement = itemStatement.getChild("statement");
        String sIfStatement = ifStatement != null ? gen_statement(ifStatement, indent, localIdents, typeReturn, 'z') : ";";
        ret += sIfStatement;
        ZbnfParseResultItem elseStatement = itemStatement.getChild("elseStatement");
        if(elseStatement != null)
        { ret += GenerateClass.genIndent(indent) + "else ";
          String sElseStatement = gen_statement(elseStatement, indent, localIdents, typeReturn, 'z');
          ret += sElseStatement;
        }
      }
      else if(semanticStatement.equals("switch_statement"))
      { ZbnfParseResultItem itemSwitchValue = itemStatement.getChild("switchValue");
        String sSwitchValue = gen_value(itemSwitchValue, zbnfDescription, typeValue, localIdents, true, 'e');
        ret += "switch(" + sSwitchValue + "){";
        List<ZbnfParseResultItem> listCases = itemStatement.listChildren("case");
        if(listCases != null)for(ZbnfParseResultItem itemCase: listCases)
        { List<ZbnfParseResultItem> zbnfCaseValues = itemCase.listChildren("caseValue");
          for(ZbnfParseResultItem zbnfCaseValue: zbnfCaseValues)
          { String sCaseValue = gen_value(zbnfCaseValue, zbnfDescription, typeValue, localIdents, true, 'e');  //should be a constant
            ret += GenerateClass.genIndent(indent+1) + "case " + sCaseValue + ": ";
          }
          List<ZbnfParseResultItem> zbnfCaseStatements = itemCase.listChildren("statement");
          if(zbnfCaseStatements != null)for(ZbnfParseResultItem zbnfCaseStatement: zbnfCaseStatements)
          { String sCaseStatement = gen_statement(zbnfCaseStatement, indent+1, localIdents, typeReturn, 'z');
            ret += sCaseStatement;
          }
        }
        ZbnfParseResultItem zbnfDefault = itemStatement.getChild("default");
        if(zbnfDefault != null)
        { ret += GenerateClass.genIndent(indent+1) + "default: ";
          List<ZbnfParseResultItem> zbnfCaseStatements = zbnfDefault.listChildren("statement");
          if(zbnfCaseStatements != null)for(ZbnfParseResultItem zbnfCaseStatement: zbnfCaseStatements)
          { String sCaseStatement = gen_statement(zbnfCaseStatement, indent+1, localIdents, typeReturn, 'z');
            ret += sCaseStatement;
          }
        }  
        ret += GenerateClass.genIndent(indent) + "}/*switch*/;";
      }
      else if(semanticStatement.equals("while_statement"))
      { ZbnfParseResultItem itemCondition = itemStatement.getChild("condition");
        String sCondition = gen_value(itemCondition, zbnfDescription, typeValue, localIdents, true, 'e');
        ret += GenerateClass.genIndent(indent) + "while(" + sCondition + ")";
        ZbnfParseResultItem itemWhileStatement = itemStatement.getChild("statement");
        String sWhileStatement = itemWhileStatement != null 
                               ? gen_statement(itemWhileStatement, indent+1, localIdents, typeReturn, 'z') 
                               : ";";
        ret += sWhileStatement;
      }
      else if(semanticStatement.equals("dowhile_statement"))
      { ZbnfParseResultItem itemCondition = itemStatement.getChild("condition");
        String sCondition = gen_value(itemCondition, zbnfDescription, typeValue, localIdents, true, 'e');
        ZbnfParseResultItem itemWhileStatement = itemStatement.getChild("statement");
        ret += "do ";
        String sWhileStatement = itemWhileStatement != null ? gen_statement(itemWhileStatement, indent+1, localIdents, typeReturn, 'z') : ";";
        ret += sWhileStatement;
        ret += "while(" + sCondition + ");";
                  
      }
      else if(semanticStatement.equals("for_statement")) 
      { ret += secondpass.gen_for_statement(itemStatement, indent, this, typeReturn);
      }
      else if(semanticStatement.equals("comment"))
      { String sComment = itemStatement.getParsedString();
        ret += "/*" + sComment + "*/" + GenerateClass.genIndent(indent);
      }
      else if(semanticStatement.equals("methodCall"))
      { CCodeData methodCall = gen_simpleValue(itemStatement, zbnfDescription, localIdents, true, 'm', false);
        ret += methodCall.cCode + ";";
      }
      else if(semanticStatement.equals("throwNew"))
      { String sMethodCall = gen_throwNew(itemStatement, zbnfDescription, localIdents, typeReturn);
        ret += sMethodCall + ";";
      }
      else if(semanticStatement.equals("try_statement"))
      { String sTry = gen_try_statement(itemStatement, indent, localIdents);
        ret += sTry;
      }
      else if(semanticStatement.equals("break_statement"))
      { ret += "break;";
      }
      else if(semanticStatement.equals("emtypStatementBlock"))
      { ret += GenerateClass.genIndent(indent) + "{ }";
      }
      else if(semanticStatement.equals("emptyStatement"))
      { ret += GenerateClass.genIndent(indent) + ";";
      }
      else if(semanticStatement.equals("variable"))
      { //it may be an assignment or such as i++
        CCodeData codeVariable = gen_simpleValue(itemStatement, zbnfDescription, localIdents, true, 'm', false);
        ret += codeVariable.cCode + ";";
      }
      else if(semanticStatement.equals("description"))
      { /**ignore it, shown above. */ 
      }
      else if(semanticStatement.equals("synchronizedBock"))
      { String cCode = gen_synchronizedBlock(itemStatement, typeReturn, indent);
        ret += cCode;
      }
      else if(semanticStatement.equals("descriptionInline"))
      { String sDescription = itemStatement.getParsedString();
      	ret += "//" + sDescription + GenerateClass.genIndent(indent);
      }
      else
      { ret += "unknownStatement(); /*unknown statement with semantic: " + semanticStatement + "*/";
        //throw new ParseException("Java2C-ERROR:unknown statement semantic: " + semanticStatement, 0);
      }
    }
    return ret;
  }

	
  
  /**generates a return statement.
   * 
   * @param itemStatement
   * @param zbnfDescription
   * @param typeReturn
   * @return
   * @throws FileNotFoundException
   * @throws IllegalArgumentException
   * @throws ParseException
   * @throws IOException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  private String gen_returnStatement
	( ZbnfParseResultItem itemStatement
	, ZbnfParseResultItem zbnfDescription
	, FieldData typeReturn
	) 
	throws FileNotFoundException, IllegalArgumentException, ParseException, IOException, IllegalAccessException, InstantiationException
	{ String ret = "";
  	if(! secondpass.noStacktrace){
      ret += "{ STACKTRC_LEAVE;";   //the brace is necessary if the return in Java is a single statement of an if.
    }
    
    ZbnfParseResultItem itemExpr = itemStatement.firstChild();
    final String returnStatement;
    final CCodeData retValue;
    if(itemExpr != null && itemExpr.getSemantic().equals("value"))
    { //String retValue = gen_value(itemExpr, typeValue, localIdents, 'e');
      retValue = gen_value(itemExpr, zbnfDescription, typeReturn.modeStatic=='r', 'e');
      String sRetValue = typeReturn.testAndcast(retValue, '.');
      if(typeReturn.typeClazz == CRuntimeJavalikeClassData.clazz_bool)
        stop();
      //ret += "return " + typeReturn.testAndcast(typeValue[0], retValue, 'r') + ";";
      if(zbnfDescription != null && zbnfDescription.getChild("returnInThreadCxt")!=null){
	    	if(  typeReturn.typeClazz == CRuntimeJavalikeClassData.clazzStringJc
	    		&& typeReturn.getDimensionArray()==0
	    		){
	    		sRetValue = "copyToThreadCxt_StringJc(" + sRetValue + ", _thCxt)";
	    	} else {
	    	  //other types are not supported yet
	    		throw new IllegalArgumentException("@java2c=returnInThreadCxt unexpected here.");
	    	}
      }
      returnStatement = "return " + sRetValue + ";";
	  }
    else
    { retValue = null;
      returnStatement = "return;";
    }
    ret += gen_ActivateGarbageCollection(indent+1, true, retValue) + GenerateClass.genIndent(indent+1);
    ret += returnStatement;
    if(! secondpass.noStacktrace){
      ret += GenerateClass.genIndent(indent) + "}";
    }
    //to indicate that no additional 'STACKTRC_LEAVE;' should be generate:
    lastWasReturn = true;
    return ret;
	}
  
  
  
  String gen_synchronizedBlock(ZbnfParseResultItem zbnfSync, FieldData typeReturn, int indent) 
  throws FileNotFoundException, IllegalArgumentException, ParseException, IOException, IllegalAccessException, InstantiationException
  {
    StringBuilder ret = new StringBuilder(10000);
    
    ZbnfParseResultItem zbnfSyncObj = zbnfSync.getChild("synchronizedObject").firstChild();
    CCodeData cCodeSyncObj = gen_simpleValue(zbnfSyncObj, null, localIdents, false, 'm', true);
    String sSyncObj = CRuntimeJavalikeClassData.fieldObjectJc.testAndcast(cCodeSyncObj, '*');
    
    ZbnfParseResultItem zbnfSyncBlock = zbnfSync.getChild("statementBlock");
    String cCodeSyncBlock = secondpass.gen_statementBlock(zbnfSyncBlock, indent+1, this, typeReturn, 'b');
    
    ret.append(GenerateClass.genIndent(indent)).append("synchronized_ObjectJc(").append(sSyncObj).append("); {")
       .append(GenerateClass.genIndent(indent+1));
    ret.append(cCodeSyncBlock);
    ret.append(GenerateClass.genIndent(indent)).append("}").append(" endSynchronized_ObjectJc(").append(sSyncObj).append(");");
    return ret.toString();
  }
  
  
  
  
  //String gen_initEmbeddedInstance(CCodeData reference, int indent)
  String genInitEmbeddedInstance(ZbnfParseResultItem zbnfNewObject, ZbnfParseResultItem zbnfDescription
  	, FieldData fieldInfo, String sCCodeInstance, int indent) 
  throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  { final String ret; // = "";
    final String sName = fieldInfo.getName();
    if(sName.equals("objWhereFieldIsFound"))
    	stop();
    //final String sTypeIdentC = fieldInfo.typeClazz.getClassIdentName();
    /*The reference to the embedded instance: */
    String sInstanceRef = null;  //TODO ##a if the instance is a non static inner class, in Java: instance.new(...) 
    CCodeData reference = gen_reference(null, zbnfNewObject.getChild("reference"),zbnfDescription, localIdents, genClass.classData.thisCodeInfo, 'b'); //new CCodeData(sInstanceRef, fieldInfo);
    /*Generate init of the ObjectJc part: */
    if(fieldInfo.modeArrayElement == 'B')
    { /*A fix StringBuffer is to be init: final Java StringBuffer buffer = new StringBuffer(123); */
      int fixSizeStringBuffer = fieldInfo.getFixSizeStringBuffer();
      String sInit = "//J2C: constructor for embedded fix-size-StringBuffer"
      + GenerateClass.genIndent(indent)
      + "init_ObjectJc(&" + sCCodeInstance + ".sb.base.object, sizeof(StringBuilderJc) + " 
      +         fixSizeStringBuffer + " - 4"   //it contains the size of the StringBuffer
      +         ", 0);"
      + GenerateClass.genIndent(indent)
      + "ctorO_I_StringBuilderJc(&" + sCCodeInstance + ".sb.base.object, "
      +         fixSizeStringBuffer   //it contains the size of the StringBuffer
      +         ", _thCxt)"
      ;
      ret = sInit;
    }
    else if(fieldInfo.typeClazz == CRuntimeJavalikeClassData.clazz_va_list){
    	/**A special case: Va_listFW should be initialized with variable argument list. */
    	ZbnfParseResultItem args = zbnfNewObject == null ? null : zbnfNewObject.getChild("actualArguments");
    	ZbnfParseResultItem arg1 = args.firstChild();     //the only one parameter for the 'new Va_list(...)'
    	CCodeData actParam = gen_value(arg1, null, false, 'a');  //it is a <value>-expression
    	ret = "va_start(" + sCCodeInstance + ".args, " + actParam.cCode + "); " + sCCodeInstance + ".typeArgs = " + actParam.cCode;
    }
    else
    { /*All other cases, not a @java2c=fixStringBuffer. : */
      final CCodeData codeCtor;
      final String sClassNameNew; 
      ClassData classNew = genClass.getType(zbnfNewObject.getChild("newClass"), localIdents);
      ZbnfParseResultItem zbnfEnvClass = zbnfNewObject.getChild("envIdent");
      if(zbnfEnvClass != null){
      	stop();
      }
      if(zbnfNewObject.getChild("impliciteImplementationClass")!=null){
      	sClassNameNew = "C_" + sName;
      	if(sClassNameNew.equals("C_refTempSimpleClass"))
        	stop();
        /**The anonymous class is an inner class of this, use this.classData to search. */
      	classNew = localIdents.getType(sClassNameNew, genClass.fileLevelIdents);
      } else {
      	//sClassNameNew = zbnfNewObject.getChildString("newClass/@name");
      }
      //final ClassData declaringClass = classData.classLevelIdents.getType(sClassNameNew, fileLevelIdents);
      final String sCtorSuffix;
      if(classNew !=null && classNew.isNonStaticInner){
      	//A ctor of an inner non-static class is registered as a method ctor_InnerName in the outer class,
      	//because the this-reference of the outer class should be given.
      	//The ClassData.searchMethod(...)-method searches in the outer class automatically
      	//and uses the correct this-reference then.
      	sCtorSuffix = "_" + classNew.getClassNameJava();
      } else {
      	sCtorSuffix = "";
      }
      //
      if(fieldInfo.typeClazz.bEmbedded){
        //if(fieldInfo.modeAccess == '%'){
      	String sNameCtor = "INIT" + sCtorSuffix;
      	String sReference = sCCodeInstance;
      	//NOTE: the 'reference' is need on non-static calls, but INIT is static anyway.
        codeCtor = gen_InternalMethodCall(zbnfNewObject, null, sNameCtor, classNew, reference, sReference); //, localIdents);
        ret = "//J2C: constructor for embedded element"
            + GenerateClass.genIndent(indent) + codeCtor.cCode;
      } else if(!fieldInfo.typeClazz.isBasedOnObject())
      { /**The embedded instance doesn't base on Object: */
        String sReference = "build_MemC(&" + sCCodeInstance + ", sizeof(" + sCCodeInstance + "))";
        String sNameCtor = "ctorM" + sCtorSuffix;
        //NOTE: the 'reference' is need on non-static calls, if it is a non-static inner class-ctor.
        codeCtor = gen_InternalMethodCall(zbnfNewObject, null, sNameCtor, classNew, reference, sReference); //, localIdents);
        ret = "//J2C: constructor for embedded element-MemC"
        	  //+ "clear_MemC("
            + GenerateClass.genIndent(indent) + codeCtor.cCode;
      }
      else
      { String sReference = sCCodeInstance;
        String sRefObject = "&(" + sReference + ".base.object)";
        String sNameCtor = "ctorO" + sCtorSuffix;
        //Note: searches the ctor in the classNew firstly, search in the outer class than.
        codeCtor = gen_InternalMethodCall(zbnfNewObject, zbnfDescription, sNameCtor, classNew, reference, sRefObject); //, localIdents);
        ret = "//J2C: constructor for embedded element-ObjectJc"
            + GenerateClass.genIndent(indent) 
            + "init_ObjectJc(" + sRefObject + ", sizeof(" + sReference + "), 0); "
            + GenerateClass.genIndent(indent)
            + codeCtor.cCode;
      }
    }  
    return ret;
  }
  
  
  

  /**generates an assignment-statement from given parse result item < statement>.
   * The < assignment> consist of a < ?leftValue>, an < assignOperator> and a < value>.
   * <br>
   * The < leftValue> is translated via call of 
   * {@link gen_variable(ZbnfParseResultItem, LocalIdents , char intension, LocalIdents.IdentInfos[] retIdentInfo)}
   * with intension='='. The argument retIdentInfo is a call by returned reference. 
   * The returned object contains informations about the kind of the left-value variable, 
   * especially if it is an enhanced reference.
   * <br>
   * The value is translated via call of {@link gen_value(ZbnfParseResultItem, LocalIdents, char intension)}
   * with intension='e'.
   * <br>
   * If the left value is an enhanced reference, a call of <code>clearBackRefJc(variable)</code>
   * is produced before the new reference is set to it, and <code>setBackRefJc(variable)</code>
   * after it is set. This subroutines implement the necessities of Garbage Collection for that enhanced references.
   * 
   * @param zbnfAssignment The ZBNF parse result item which is a < assignment>
   * @param indent Number of nesting level of the block to generate indentations of a line.
   * @param localIdents The indentation of the block: TODO use it as class element.
   * @param intension Intension of call: 'c'-constructor body, 'm'-method body, 'b'-internal block, 'z'-part of if, while etc., 'f'-finalize body. 
   * @return
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   */
  public String gen_assignment(ZbnfParseResultItem zbnfAssignment, ZbnfParseResultItem zbnfDescription, int indent, LocalIdents localIdents, char intension) 
  throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  { //String ret = GenerateClass.genIndent(indent);
    final String ret; // = "";
    ClassData[] typeValue = new ClassData[1];  //classData of the part of expression
    //LocalIdents.IdentInfos typeLeftValue[] = new LocalIdents.IdentInfos[1];
    //String sVariableC;
    final CCodeData leftVariable;
    final String sAssignOperatorC;
    final boolean isIncrementOrDecrement;
    
    { ZbnfParseResultItem zbnfLeftValue = zbnfAssignment.getChild("leftValue");  //##a
      if(zbnfLeftValue == null)
      { throw new ParseException("leftValue expected",0);
      }
      { //test
        String sName = zbnfLeftValue.getChild("variableName").getParsedString();
        if(sName.equals("xb"))
          stop();
      }
      
      leftVariable = gen_variableAccess(zbnfLeftValue, zbnfDescription, localIdents, '=', genClass.classData.thisCodeInfo); //, typeLeftValue); 
    }
    { ZbnfParseResultItem zbnfAssignOperator = zbnfAssignment.getChild("@assignOperator");;
      if(zbnfAssignOperator != null)
      { sAssignOperatorC = zbnfAssignOperator.getParsedText();
        isIncrementOrDecrement = false;
      }
      else if(zbnfAssignment.getChild("increment") != null)
      { sAssignOperatorC = "++";
        isIncrementOrDecrement = true;
      }
      else if(zbnfAssignment.getChild("decrement") != null)
      { sAssignOperatorC = "--";
        isIncrementOrDecrement = true;
      }
      else
      { assert(false);
        isIncrementOrDecrement = false;
        sAssignOperatorC = null;
      }
    }
    if(isIncrementOrDecrement)
    { /**no zbnfValue given, no right value to assign. */ 
      ret = leftVariable.cCode + sAssignOperatorC;
    }
    else
    { ZbnfParseResultItem zbnfValue = zbnfAssignment.getChild("value");
      ret = gen_assignValue(leftVariable, sAssignOperatorC, typeValue, zbnfValue, zbnfDescription, indent, localIdents, intension); 
    }  
    return ret;

  }


  
  
  /**Generates an assignment with given value.
   * @param leftVariable The code snippet to access the variable to which the value should be assigned.
   *                     The access modes are important. At example it may be an array type.
   * @param sAssignOperatorC
   * @param typeValue
   * @param zbnfAssignment
   * @param indent
   * @param localIdents
   * @param intension
   * @return
   * @throws FileNotFoundException
   * @throws IllegalArgumentException
   * @throws ParseException
   * @throws IOException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  /**
   * @param leftVariable
   * @param sAssignOperatorC
   * @param typeValue
   * @param zbnfValue
   * @param zbnfDescription
   * @param indent
   * @param localIdents
   * @param intension
   * @return
   * @throws FileNotFoundException
   * @throws IllegalArgumentException
   * @throws ParseException
   * @throws IOException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  String gen_assignValue
  ( final CCodeData leftVariable
  , final String sAssignOperatorC
  , ClassData[] typeValue
  , ZbnfParseResultItem zbnfValue
  , ZbnfParseResultItem zbnfDescription
  , int indent, LocalIdents localIdents, char intension
  ) throws FileNotFoundException, IllegalArgumentException, ParseException, IOException, IllegalAccessException, InstantiationException
  { String ret;
    if(leftVariable.cCode.equals("ifc3"))
      stop();
    final ZbnfParseResultItem zbnfNewObject;
		final ZbnfParseResultItem zbnfNewArray;
		{ String sSemantic = zbnfValue.getSemantic();
		  if(sSemantic.equals("newObject"))
		  { zbnfNewObject = zbnfValue;
		    zbnfNewArray = null;            //variante at variable declaration
		  }
		  else if(sSemantic.equals("newArray"))
		  { zbnfNewArray = zbnfValue;
		    zbnfNewObject = null;
		  }
		  else
		  { zbnfNewObject = zbnfValue.getChild("newObject"); //inside a value.
		    zbnfNewArray = zbnfValue.getChild("newArray");
		  }
		}  
		if(leftVariable.dimensionArrayOrFixSize == 0 && leftVariable.getTypeName().equals("StringJc"))
		{
		  ret = gen_StringAssignment(indent, leftVariable, sAssignOperatorC, zbnfValue, zbnfDescription, localIdents, intension);  
		}
		else if(  (  leftVariable.modeAccess == '$' 
		          || leftVariable.modeAccess == '%'   //some call-by-value types are designated with %, OS_Timestamp, MemC, 
		          || leftVariable.modeAccess == 't' ) //a StringJc, from Java: String text = new String(..); 
		       && zbnfNewObject != null)
		{ /*variable = new Type(), variable is embedded:
		    generate no new, but call of constructor. 
		    The Type of new should be the same as the type of the embedded instance.
		    It is so, because an embedded instance is only used, if this condition is met. */
		  ClassData typeOfNew = genClass.getType(zbnfNewObject.getChild("newClass"), localIdents);  //itemNewObject.getChild("newClass").getParsedString();
		  /*induce to generate the necesarry include statement:
		    typeOfNew may be null if the type is a external type. */
		  if(typeOfNew != null)
		  { genClass.writeContent.addIncludeC(typeOfNew.sFileName, "embedded ctor");
		  }
		  ret = genInitEmbeddedInstance(zbnfNewObject, zbnfDescription, leftVariable.identInfo, leftVariable.cCode, indent);
		}
		else if(leftVariable.modeAccess == 'Y' && zbnfNewObject != null)
		{ /**Java: variable = new Type(). The variable is embedded:
		   * generate no new, because the variable is embedded, but call the constructor: */
		  ClassData typeOfNew = genClass.getType(zbnfNewObject.getChild("newClass"), localIdents);  //itemNewObject.getChild("newClass").getParsedString();
		  /**induce to generate the necesarry include statement:
		   * typeOfNew may be null if the type is a external type. */
		  if(typeOfNew != null)
		  { genClass.writeContent.addIncludeC(typeOfNew.sFileName, "embedded ctor-Y");
		  }
		  String sInstanceRef = null;  //TODO if the instance is a non static inner class, in Java: instance.new(...) 
		  CCodeData reference = new CCodeData(sInstanceRef, typeOfNew.classTypeInfo);
		  //ClassData[] retTypeValue = new ClassData[1];
		  String sObject = "(ObjectJc*)&(" + leftVariable.cCode + ")";
		  CCodeData codeCtor = gen_InternalMethodCall(zbnfNewObject, null, "ctorO", reference.identInfo.typeClazz, reference, sObject); //, localIdents);
		  ret = codeCtor.cCode;
		}
		else if(zbnfNewArray != null)
		{ /**On initialization, if it is a fix array, the zbnfNewArray is evaluated already 
		   * to determine the array size. It is an expression like <code>new int[23]</code>. 
		   * Only if it is an pointer to an array, the zbnfNewArray contains relevant informations.
		   */
		  ret = "/*J2C: newArray*/" + GenerateClass.genIndent(indent);
		  if(leftVariable.cCode.equals("ythis->yTEST"))
		    stop();
		  if(leftVariable.identInfo.getDimensionArray() >0 )
		  {
		    stop();
		    char kind = leftVariable.identInfo.modeAccess;
		    String sReference = leftVariable.cCode;
		    switch(kind)
		    { case 'Y':
		      case '$':
		      { //embedded array with head and fix size, call constructor
		        String sElementType = leftVariable.identInfo.getTypeName();
		        String sizeArray = leftVariable.identInfo.fixArraySizes[0];
		        String sCtor = "init_ObjectJc(&" + sReference 
		                     + ".head.object, sizeof_ARRAYJc(" + sElementType + ", " + sizeArray + ")" 
		                     + ", 0);   //J2C: ctor embedded array."
		                     + GenerateClass.genIndent(indent)
		                     + "ctorO_ObjectArrayJc(&" + sReference 
		                     + ".head.object, " + sizeArray + ", sizeof(" + sElementType + "), null, 0);"
		                     ;
		        ret += sCtor + "//J2C: constructor for embedded array";
		      }break;
		      case 'X':
		      case '*':
		      { /**reference to an ObjectArrayJc, initialize it with a new instance. */
		        String sNewArray = gen_newArray(zbnfNewArray, typeValue, localIdents, leftVariable.identInfo);
		        ret = leftVariable.cCode + " = " + sNewArray + ";  //J2C: assign a new ObjectArrayJc. ";
		      }break;
		      case 'Q':
		      case '%':
		      { /**embedded simple array. Fill it with 0. */
		        String sElementType = leftVariable.identInfo.getTypeName();
		        String sizeArray = leftVariable.identInfo.fixArraySizes[0];
		        ret= "init0_MemC(build_MemC(&" + leftVariable.cCode + ", " 
		             + sizeArray + " * sizeof(" + sElementType + "))); //J2C: init the embedded simple array";
		        
		      }break;
		      case 'P':
		      { /**Reference to a simple array without head, get memory for it. */
		        ret = "/*TODO reference to simple array */";
		      }break;
		      case '@':
		      {
		        ret = "/*TODO enhanced reference to ObjectArrayJc*/";
		        
		      }break;
		      default: assert(false);
		    }
		  }
		}
		else 
		{ /**A value to assign is given. */
		  CCodeData value;
		  if(zbnfNewObject != null)
		  { /**the zbnfValue is a newObject*/
		  	ZbnfParseResultItem itemReference = zbnfNewObject.getChild("reference");
        CCodeData reference;
        if(itemReference != null)
        { String[] unused = new String[1];
          reference = gen_reference(unused, itemReference, zbnfDescription, localIdents, genClass.classData.thisCodeInfo, 'm'); 
        } else {
        	reference = genClass.classData.thisCodeInfo;
        }
          //if(zbnfNewObject.getChild)
		  	value = gen_newObject(zbnfNewObject, reference); //, localIdents);
		  }
		  else if(zbnfNewArray != null)
		  { String sValue = gen_newArray(zbnfNewArray, typeValue, localIdents, null);
		    value = new CCodeData(sValue, typeValue[0].classTypeInfo);
		  }
		  else
		  {
		    //String sValueC = gen_value(zbnfValue, typeValue, localIdents, 'e');
		    value = gen_value(zbnfValue, zbnfDescription, leftVariable.identInfo.modeStatic=='r', intension);
		  }
		  typeValue[0] = value.identInfo.typeClazz;
		  ret = gen_AssignCheckCast(leftVariable, sAssignOperatorC, value);
		}
		      
    return ret;
  }
  
  
  
  /**Generates the assignment with check of necessity of cast.
   * @param leftVariable The destination variable with name, type etc.
   * @param sAssignOperatorC The operator, mostly "=", maybe "+=" etc.
   * @param value The right value with name, type etc. 
   * @return The assignment code in C
   */
  String gen_AssignCheckCast(final CCodeData leftVariable, final String sAssignOperatorC, final CCodeData value)
  { String ret;
  	String sValueC = value.cCode;
    char modeAccessDst = leftVariable.modeAccess == '@' || leftVariable.modeAccess == '&' 
    	                 ? '*'   //access to the .ref-element
    	                 : leftVariable.modeAccess; 
    if(leftVariable.cCode.equals("byteRepresentation"))
      stop();
    final String dstValue = leftVariable.identInfo.testAndcast(value, modeAccessDst);
    if(!sValueC.equals(dstValue))
      stop();
    if(leftVariable.cCode.equals("ifc"))
      stop();
    if(leftVariable.modeAccess == '@')
    { 
      /**it is an enhanced reference: */
      if(sValueC.equals("null"))
      { ret = "CLEARREFJc(" + leftVariable.cCode + ")"; 
      }
      else
      { /**For reflection_Name. */
        String sLeftType = leftVariable.identInfo.typeClazz.getClassCtype_s();
        ret = "SETREFJc(" + leftVariable.cCode + ", " + dstValue + ", " + sLeftType + ")"; 
      }
    }
    else if(leftVariable.modeAccess == '&')
    { 
      //it is an mtbl reference:
      if(leftVariable.cCode.equals("ifc22"))
        stop();
      String sLeftType = leftVariable.identInfo.typeClazz.getClassIdentName();
      ret = "SETMTBJc(" + leftVariable.cCode + ", " + dstValue + ", " + sLeftType + ")"; 
    }
    else
    { //normal assignment, at ex. a numerical value to a int variable
      ret = leftVariable.cCode + " " + sAssignOperatorC + " " + dstValue; 
    }
    return ret;
  } 

  
  
  /**Generates an assignment to a String.
   * @param indent
   * @param sVariableC
   * @param typeLeftValue
   * @param sAssignOperatorC
   * @param sValueC
   * @param typeValue
   * @param intension Intension of call: 'c'-constructor body, 'm'-method body, 'b'-internal block, 'z'-part of if, while etc., 'f'-finalize body. 
   * @return
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   */
  public String gen_StringAssignment
  ( int indent
  , CCodeData leftValue  //, LocalIdents.IdentInfos typeLeftValue
  , String sAssignOperatorC
  , ZbnfParseResultItem zbnfValue
  , ZbnfParseResultItem zbnfDescription
  , LocalIdents localIdents
  , char intension
  ) throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  { final String ret; // = "";
    String sVariableC = leftValue.cCode;
    /**The variable name of a StringBuilder-instance, which is used to this String already.
     * It may be null. */
    if(sVariableC.equals("ssn"))
      stop();
    genClass.writeContent.addIncludeC("Jc/StringJc", "string assignment");
    final String sStringExpr;  
    if(sAssignOperatorC.equals("+="))
    { //concatenation required!
      final String sFirstString;
      String sStringBuilderToUse = leftValue.identInfo.sStringBuilderName;  
      if(sStringBuilderToUse != null){
      	sFirstString = null;  //force append to existing buffer
      } else {
      	sFirstString = sVariableC;  //start with the variable to build a new String appended on that.
      }
    	CCodeData value  = gen_ConcatenatedStrings
      ( sFirstString    //sFirstString
      , leftValue.identInfo.typeClazz  //firstType
      , leftValue.modeAccess  
      , zbnfValue.iterChildren()  //iterZbnf
      , zbnfDescription
      , sStringBuilderToUse
      , localIdents
      , intension
      );
      sStringExpr =  value.cCode;
    }
    else
    { ClassData[] retType = new ClassData[1];
      //String zTest = zbnfValue.toString();
      //if(zTest.startsWith(" [2731..2740]"))
      //  stop();
      /**Use 'gen_value', do not call 'gen_ConcatenatedStrings', because the expression
       * may be a simple value, not an concatenation. */
      CCodeData codeValue = gen_value(zbnfValue, zbnfDescription, leftValue.identInfo.modeStatic=='r', intension);
      String sValueC = codeValue.cCode;
      retType[0] = codeValue.identInfo.typeClazz;
      String sType = codeValue.identInfo.typeClazz.getClassIdentName();
      /**The codeValue.sTempRef is not null, if a annotation java2c=toStringNonPersist
       * is found for the statement. */
      if(codeValue.sTempRef != null)
      	stop();
      leftValue.identInfo.sStringBuilderName = codeValue.sTempRef;
      
      /*Iterator<ZbnfParseResultItem> iter= zbnfValue.iterChildren();
        String sValueC = gen_ConcatenatedStrings("", null, '.', iter, zbnfDescription, null, localIdents, intension);
      */
      if(sValueC.equals("null"))
      { sStringExpr = "null_StringJc";
      }
      //else if(sType.equals("char const*") && sAssignOperatorC.equals("="))
      else if(codeValue.identInfo.typeClazz == CRuntimeJavalikeClassData.clazz_s0 && sAssignOperatorC.equals("="))
      { //The called expression in C returns a 0-terminated string, cast it to StringJc:
      	sStringExpr = "z_StringJc(" +sValueC + ")"; 
      }
      //else if(sType.equals("char const*") && sAssignOperatorC.equals("+="))
      else if(codeValue.identInfo.typeClazz == CRuntimeJavalikeClassData.clazz_s0 && sAssignOperatorC.equals("+="))
      { sStringExpr = "add_s0_StringJc(" + sVariableC + ", " + sValueC + ")"; 
      }
      else if(sType.equals("StringBuilderJc") && sAssignOperatorC.equals("="))
      { sStringExpr = "toString_StringBuilderJc(&(" +sValueC + ")->base.object, _thCxt)"; 
      }
      else if(sType.equals("StringBuilderJc") && sAssignOperatorC.equals("+="))
      { sStringExpr = "add_StringJc(" + sVariableC + ", toString_StringBuilderJc(&(" +sValueC + ")->base.object, _thCxt), _thCxt)"; 
      }
      else if(sType.equals("StringJc") && sAssignOperatorC.equals("="))
      { sStringExpr = "" +sValueC; 
      }
      else if(sType.equals("StringJc") && sAssignOperatorC.equals("+="))
      { sStringExpr = "add_StringJc(" + sVariableC + ", " + sValueC + ")"; 
      }
      else if(sAssignOperatorC.equals("="))
      { //An Object, use the toString Method.
        sStringExpr = "/*#*/toString_"+ sType + "((ObjectJc*)(" + sValueC + "), _thCxt)"; 
      }
      else if(sAssignOperatorC.equals("+="))
      { //An Object, use the toString Method.
        sStringExpr = "add_StringJc(" + sVariableC + ", toString_"+ sType + "((ObjectJc*)(" + sValueC + "), _thCxt), _thCxt)"; 
      }
      else throw new ParseException("unexpected syntax",0);
    }    
    final String sStringExpr2;
    if(zbnfDescription != null && zbnfDescription.getChild("declarePersist")!=null){
    	sStringExpr2 = "declarePersist_StringJc(" + sStringExpr + ")";
    } else {
    	sStringExpr2 = sStringExpr;
    }
      
    if(leftValue.identInfo.nClassLevel ==0
    	|| (zbnfDescription != null && zbnfDescription.getChild("toStringNonPersist")!=null)
    	){
    	/**Simple assignment to local variable or if it shouldn't be persistent. */
    	ret = sVariableC + " = " + sStringExpr2 + "/*J2C:non-persistent*/";
    } else {
      ret = "set_StringJc(&(" + sVariableC + "), " + sStringExpr2 + ")";
    }
    return ret;
  }
  

  /**generates an access to a value in a variable or a using of a variable as left value. 
   * The variable may be referenced. It means, it is a variable in the referenced object.
   * <br>
   * If no reference is given, it may be either it a local variable or a class variable. 
   * With help of the argument localIdents the variable can be found in the context.
   * <ul><li>If it is a class variable, In C <code>ythis-></code> is written before.
   *     <li>If it is a variable of a super class, In C <code>ythis->super.</code> is written before.
   *     <li>If it is a variable of a outer class, In C <code>ythis->outer-></code> is written before.
   *     <li>If it is a local stack variable, the name is directly used in C like in Java.
   * </ul>    
   * <br>
   * If it is a referenced variable, the identifier info of the referencing object is used,
   * The reference is build correctly either with <code>-></code> or <code>.</code>,
   * depended on the reference kind. It may be an embedded struct.
   * The LocalIdents of the referencing object are used to desire it.
   * <br>
   * If the variable is an enhanced reference and the intension is ones of "Rrmex", 
   * the code <code>REFJc(variable)</code> is generated  to get the stored reference as value.
   * <code>REFJc(variable)</code> is a Macro to get the reference pointer inside an enhanced referende.
   * Especially if it is left value, the code of enhanced reference itself is generated.  
   * <br>
   * A variable may have a pre- or post- increment or -decrement like --x or y++.
   * 
   * @param itemVariable The ZBNF parse result item of the < ?variable>-semantic.
   *                     It is a part of a simpleValue-syntax-prescript.
   * @param localIdents The Identifier info of the environment.
   * @param intension Info about the location respectively cause to call this method.
   *                  e-expression R-first reference r-nested reference =:leftvalue ...
   * @param retIdentInfo information about the variable in its context.
   * @return generated String of variable access for the C-code
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   */
  public CCodeData gen_variableAccess(
  		final ZbnfParseResultItem itemVariable, final ZbnfParseResultItem zbnfDescription
  		, LocalIdents localIdents
    , char intension, CCodeData cCodeReferenceInput
    ) 
  throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  { //String ret = "";
    String cCodeBase = "";
    int nrofParanthesis = 0;
    
    final FieldData identInfo; // = retIdentInfo[0];  
    
    String sIdent = itemVariable.getChild("variableName").getParsedString();
    if(sIdent.equals("length"))
      stop();
    ZbnfParseResultItem itemReference = itemVariable.getChild("reference");
    final CCodeData reference;
    boolean bFinish = false;
    String[] sConcatenatedReference = new String[1]; 
    if(itemReference != null)
    { reference = gen_reference(sConcatenatedReference, itemReference, zbnfDescription, localIdents, genClass.classData.thisCodeInfo, intension); //, typeReference); // retIdentInfo);
      if(reference.sTempRef != null) {
        cCodeBase = "(" + reference.cCode;
        nrofParanthesis +=1;
        reference.cCode = reference.sTempRef;
      }
      if(   (  reference.dimensionArrayOrFixSize >0 
      	    || reference.identInfo.typeClazz == CRuntimeJavalikeClassData.clazzByteStringJc ) 
      	 && sIdent.equals("length")){
        //the length of an array is to be get dependend from the modeAccess: 
        identInfo = CRuntimeJavalikeClassData.clazz_int32.classTypeInfo;
        switch(reference.modeAccess){
          case 'B': cCodeBase += "length_ByteStringJc(" + reference.cCode + ")"; break; //simple array
        	case '%': cCodeBase += "ARRAYLEN(" + reference.cCode + ")"; break; //simple array
        	case 'Y': case '$': cCodeBase += reference.cCode + ".head.length";         break; //array struct embedded
          case 'Q': case '&': cCodeBase += "ARRAYLEN(" + reference.cCode + ")"; break; //simple array of pointer
          case 'X': case '*': cCodeBase += reference.cCode + "->head.length";        break; //array struct simple referenced
          case '@': cCodeBase += "REFJc(" + reference.cCode + ")->head.length";    break; //array struct enhanced referenced
          case 'P': throw new ParseException("A .length of a simple referenced array isn't able to determine.", 0);
          default: assert(false);
        }
        bFinish = true;
      }
      else
      { localIdents = reference.getClassLevelIdents(); //retIdentInfoRef[0].typeClazz.classLevelIdents;
        identInfo = localIdents.get(sIdent);  //search it. localIdents are them from a reference,
      }
    }
    else
    { if(sIdent.equals("this"))
      { identInfo = genClass.classData.classTypeInfo;
        cCodeBase = "ythis";
        bFinish = true;
      }
      else
      { if(sIdent.equals("linkedList")&& genClass.classData.sClassNameC.equals("stressTest_TestContainer_Test_s"))
          stop();
        identInfo = localIdents.get(sIdent);  //search it. localIdents are from input,
      }
      
      //if(cCodeReferenceInput == null || cCodeReferenceInput.modeAccess == 'C')
      { if(identInfo == null)
        { reference = null;
        }
        else if("Ssd".indexOf(identInfo.modeStatic)>=0)
        { reference = Java2C_Main.singleton.staticReferenceDummy;
        }
        else if(identInfo.getClassLevel()>0)
        { reference = cCodeReferenceInput;  //it is a ident of the environment.
                         //classData.thisCodeInfo;
        }
        else
        { reference = Java2C_Main.singleton.localReferenceDummy;
        }
      }
      //else
      { //reference = cCodeReferenceInput;
      }
    }
    while(nrofParanthesis > 0){
      nrofParanthesis-=1;
      cCodeBase += ")";
    }
    //Info about the identifier of the variable in local context:
    if(identInfo == null) 
    	throw new IllegalArgumentException("unknown identifier: \""+ sIdent + "\" in environment of: " + localIdents
    		+ ". \nTip: A hand-written stc-file may be incomplete.");
    char cModeAccess = identInfo.modeAccess;
    
    if(!bFinish)
    { /*bFinish means, the code generation of the base access is done. */
      //the ident Info should be exists, otherwise an exception is thrown here.
      int nClassLevel;
      assert(identInfo != null); // || sIdent.equals("exc"));  //TODO exc in CATCH
      { nClassLevel = identInfo.getClassLevel();
        //char cModifier = identInfo.sModifier.charAt(0);
        if("Ssd".indexOf(identInfo.modeStatic)>=0)
        { //the identifier is static or defined with #define
          cCodeBase += sIdent + "_" + identInfo.declaringClazz.getClassIdentName();
        }
        else
        { String superOuter = "";
          int nOuterLevel = identInfo.getOuterLevel();
          while(--nOuterLevel > 0)
          { superOuter += "outer->";  //member of a outer class. It is referenced with 'outer'
          }
          while(--nClassLevel > 0)
          { superOuter += "base.super.";  //member of a super class. It is an embedded struct named 'super'
          }
          String referenceSeparator;
          switch(reference.modeAccess)
          { case '@': referenceSeparator = "->"; break; //the identifier of this scope is an enhanced reference.
            case '$':  //the identifier of this scope is an embedded struct.
            case 'Y':  //the identifier of this scope is an embedded array element
            case 't':  //the identifier of this scope is a StringJc.
                      referenceSeparator = "."; break;
            case '~':
            case '*': referenceSeparator = "->"; break;
            //case '%': referenceSeparator = ""; assert(reference.cCode.equals("")); break;
            case '%': referenceSeparator = reference.cCode.equals("") ? "" : "."; 
              stop();  //note: OS_TimeStamp as reference was here, time_sec ?
              break;
            case 'C': //class static variable 
              referenceSeparator = ""; assert(reference.cCode.equals("")); 
              break;
            default: throw new ParseException("unexpected modeAccess", reference.modeAccess);
          }
          if(reference.modeAccess == '@')
          {
            cCodeBase += "REFJc(" + reference.cCode + ")" + referenceSeparator + superOuter + sIdent;
          }
          else
          {
            cCodeBase += reference.cCode + referenceSeparator + superOuter + sIdent;
          }  
          if(identInfo.modeArrayElement == 'B')
          { cCodeBase += ".sb";  //access to the StringBuilderJc inside the struct.
            assert(cModeAccess == '$'); 
            cModeAccess = '$';   //because access to StringBuffer-element.
          }
          //TODO test wether it is a local overwritten variable against a class element.
        }
      }
    }  
    int dimensionArray = identInfo.getDimensionArray();
    List<ZbnfParseResultItem> listArrayIndices = itemVariable.listChildren("arrayIndex");
    final String cCode3;
    if(listArrayIndices != null) 
    { final String cCodeArray;
      switch(identInfo.modeAccess){
        case 'B': cCodeArray = "data_ByteStringJc(" + cCodeBase + ")";    break; //simple array
      	case 'Q': case '%': cCodeArray = cCodeBase;    break; //simple array
      	case 'Y': case '$': cCodeArray = cCodeBase + ".data"; cModeAccess = identInfo.modeArrayElement; break; //array struct embedded
        case 'P': case '&': cCodeArray = cCodeBase;    break; //simple array of pointer
        case 'X': case '*': cCodeArray = cCodeBase + "->data"; cModeAccess = identInfo.modeArrayElement; break; //array struct simple referenced
        case '@': cCodeArray = "REFJc(" + cCodeBase + ")->data"; cModeAccess = '*'; break; //array struct enhanced referenced
        default: throw new ParseException("unexpected modeAccess", identInfo.modeAccess);
      }      
      String cCodeIndices = "";
      for(ZbnfParseResultItem item: listArrayIndices)
      { ClassData[] typeIndex = new ClassData[1];  //classData of the part of expression
        String sIdxValue = gen_value(item, null, typeIndex, localIdents, true, 'e');
        cCodeIndices+= "[" + sIdxValue + "]";  //fix array
        dimensionArray -=1;
      }
      cCode3 = cCodeArray + cCodeIndices;
      if(dimensionArray == 0)
      { cModeAccess = identInfo.modeArrayElement; //access to the element.
      }
    }
    else
    { cCode3 = cCodeBase;
    }
    final String cCode4;
    if(itemVariable.getChild("postDecrement")!= null)
    { cCode4 = cCode3 + "--"; 
    }
    else if(itemVariable.getChild("postIncrement")!= null)
    { cCode4 = cCode3 + "++";
    }
    else if(itemVariable.getChild("preDecrement")!= null)
    { cCode4 = "--" + cCode3; 
    }
    else if(itemVariable.getChild("preIncrement")!= null)
    { cCode4 ="++" + cCode3;
    }
    else
    { cCode4 = cCode3;
    }
    final String cCode5;
    if(sConcatenatedReference[0]!=null){
      /**A call of methods and maybe assignment to internal temporary references is returned: */
      cCode5 = GenerateClass.genIndent(indent+1) + "(" + sConcatenatedReference[0] + cCode4 + GenerateClass.genIndent(indent+1) + ")";
    }
    else {
      cCode5 = cCode4;
    }
    //The modeAccess is the access to an array element if the variable is so. identInfo is the info of the array variable type.
    final CCodeData retData = new CCodeData(cCode5, identInfo, cModeAccess, dimensionArray); //identInfo.modeArray);
    return retData;
  }



  /**generates an reference to a variable or method call from parse result < reference>. 
   * <ul>
   * <li>If <code>this.</code> is found in Java, designed with < this> in zbnf parse result,
   *  <code>ythis-></code> is generated.
   *  
   * <li>If <code>super.</code> is found in Java, designed with < super> in zbnf parse result,
   *  <code>ythis->super.</code> is generated.
   *  
   * <li>If a < referenceAssociation> is found, the access to it is generated in C calling
   *   {@link gen_variable(ZbnfParseResultItem, LocalIdents, char intension, FieldData[])}.
   
   *   It follows either by <code>.</code> or <code>-></code> or the <code></code>
   *   The type of the variable returned in the FieldData[] 
   *   is stored as retIdentInfo and is used to determine if <code>.</code> or <code>-></code>
   *   follows after the variable. The variable may be an embedded reference,
   *   than <code>.</code> should be following. 
   *   The <code>.ref</code> part of an enhanced reference is generated in gen_variable().
   *    
   * <li>If a < referenceMethod> is found, {@link gen_simpleMethodCall(ZbnfParseResultItem, String sInstanceRef, FieldData, ClassData, LocalIdents)}
   *   is called to produce an call of the method in C. The type of return-value of the method
   *   supplied in the FieldData-arg is used as retIdentInfo.
   * </ul>
   * 
   * A reference can be referenced again, writing at ex.<code>myRef.itsRef.</code> or 
   * <code>myRef.method().itsRef.</code>. In Java2C.zbnf the references are understand syntactically 
   * as repetition. Therefore here all references are concatenated. The following separator
   * <code>.</code> or <code>-></code> is determined always from type of the reference before.
   * The type-info of the last reference is returned in retInfo. 
   * 
   * @param concatedReference Part of reference, which are build with concatenated methods in Java.
   *        In the result it is to be write as a expression separated with comma.
   * @param zbnfReferences The ZBNF parse result item of the < reference>.
   * @param localIdents The Identifier info of the environment.
   * @param intension Info about the location respectively cause to call this method.
   *                  e-expression R-first reference r-nested reference =:leftvalue ...
   * @param retIdentInfo information about the last reference type.
   * @return String representing the reference in C.
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   */
  CCodeData gen_reference
  ( String[] concatenatedReference
  , final ZbnfParseResultItem zbnfReferenceP
  , final ZbnfParseResultItem zbnfDescription
  , final LocalIdents localIdentsParent
  , CCodeData envClassCode
  , char intension
  ) 
  throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  { LocalIdents refIdents = localIdentsParent;
    int countNestedRef = 0; //to test
    String ret = "";
    CCodeData referenceCode = envClassCode;
    if(zbnfReferenceP !=null){  //if null then a reference isn't given, envClassCode is ok.
      ZbnfParseResultItem zbnfReference = zbnfReferenceP;
      do
      { Iterator<ZbnfParseResultItem> iterReferences = zbnfReference.iteratorChildren();
        intension = 'R';  //the left element of a reference.
        
        ZbnfParseResultItem itemRef = null;
        //The initial values are returned if hasNext() fails at begin.
        /**Only the first child should be evaluated. a second child is <reference>, it will be tested after them.
         * The syntax in Java2C.zbnf was changed. In the past it was a repetition, therefore while was okay.
         * Not it is a recursion, but here solved with a do...while (next-child != null).
         */
        int countWhile = 0;
        while(  iterReferences.hasNext())
        { if(++countNestedRef >= 2)
            stop();
          itemRef = iterReferences.next();
          String semantic = itemRef.getSemantic();
          if(!semantic.equals("reference"))
          { assert(++countWhile == 1);
            if(semantic.equals("this"))
            { referenceCode = genClass.classData.thisCodeInfo;
            }
            else if(semantic.equals("super"))
            { CCodeData superAccessCode = genClass.classData.inheritanceInfo.superInheritance.classData.thisCodeInfo;
              referenceCode = new CCodeData("(&ythis->base.super)", superAccessCode.identInfo);
            }
            else if(semantic.equals("referenceMethod"))
            { /**the reference is built from a method call. That are concatenated methods.
               * Either the reference is stored in an _temp# 
               * or it is a return this-method. Than the same reference is used.
               */
              CCodeData methodCall = gen_simpleMethodCall
                    ( itemRef
                    , zbnfDescription    
                    , referenceCode //envInstance             //the generated instance reference, it is the reference up to now.
                    , localIdentsParent  //the idents for build values for method arguments 
                    , true  //nonPersistent
                    , 'r'
                    );
              if(concatenatedReference[0]==null){ concatenatedReference[0] = ""; }
              if(!methodCall.isReturnThis()){
                /**use a _temp# */
                String sTempRef = tempRefForConcat(methodCall.identInfo);
                concatenatedReference[0] += sTempRef + "= " + methodCall.cCode + GenerateClass.genIndent(indent+1) + ", ";
                referenceCode = methodCall;
                referenceCode.cCode = sTempRef;
              } else {
                /**use the same reference, because the method returns this. */
                concatenatedReference[0] += methodCall.cCode + GenerateClass.genIndent(indent+1) +  ", ";
                /**Let the reference equals as it is. */
              }
            }
            else if(semantic.equals("referenceAssociation"))
            { String sAssociationName = itemRef.getChild("variableName").getParsedString();
              //sAssociationName may be a type.
              if(sAssociationName.equals("SpecialCharStrings") || sAssociationName.equals("singleton_LeapSecondsJc") || sAssociationName.equals("singleton"))
                stop();
              /**Check whether the pretended association is a type, 
               * thus the referenced element is a static one and the pretended association is the class defines it.
               */
              ClassData typeClass = refIdents.getType(sAssociationName, genClass.fileLevelIdents);
              if(typeClass != null)
              { //it is a type, not a variable:
                referenceCode = typeClass.typeCodeInfo;
              }
              else
              {
                referenceCode = gen_variableAccess(itemRef, zbnfDescription, refIdents, intension, referenceCode); //, retIdentInfo); //itemRef.getParsedString();
                //NOTE: retIdentInfo[0] = setted in gen_variable with the type info of the detected variable.
                if(referenceCode.cCode.equals("leapSeconds"))
                  stop();
              }
    
            } //if referenceAssociation
    
            //if(referenceCode.type != null)
            { String sFileName = referenceCode.getTypeHeaderfilename(); //retIdentInfo[0].typeClazz.sFileName;
              if(sFileName != null)
              { genClass.writeContent.addIncludeC(sFileName, "reference-association: " + referenceCode.identInfo.getName());
              }
              refIdents = referenceCode.getClassLevelIdents();  
            }
            //else
            { //a type without typeClass is not recognize in Java2C, it will be an external type.
              //refIdents = null;  //there don't have to use in a next nested reference, because the type is unknown. 
              //stop();
            }
            intension = 'r'; //for the next nested reference.
          }  
        } //while, all references are evaluated one after another.
        
        zbnfReference = zbnfReference.getChild("reference"); //nested reference is a next reference!
        if(zbnfReference != null)
          stop();
      } while(zbnfReference != null); 
      if(ret.length()>0)
      { assert(false);
        //referenceCode.bUseTempRef =true;
        referenceCode.cCode = ret;
      }
    }
    return referenceCode;
  }

  
  public String gen_value(ZbnfParseResultItem parent, ZbnfParseResultItem zbnfDescription, ClassData[] retType, LocalIdents localIdents, boolean maybeNonPersistent, char intension) 
  throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  { CCodeData codeValue = gen_value(parent, zbnfDescription, maybeNonPersistent, intension);
    retType[0] = codeValue.identInfo.typeClazz;
    return codeValue.cCode;
  }

  /**generates the expression to get a value. From syntax item <...?value>.
   * In the syntax of < value> it is a repetion of <code>{ [|< unaryOperator>] < simpleValue?> ? < binaryOperator> }</code>.
   * Therefore {@link #gen_simpleValue(ZbnfParseResultItem, LocalIdents, char)} is called inside.
   * All components of the < value> are processed in one while-loop because it is stored one after another.
   * That includes also the <code>[< ?conditional>...</code> construct 
   * in which is this method is called recursively.
   * <br>
   * The priority of operators is checked in the Java context. The input to the Java2C-translator
   * should be a well compiled java source code. Therefore it needn't considered here.
   * 
   * @param parent The parse result item which has the semantic <...?value>.
   * @param intension intension of generating: 
   *        <ul><li>'e'-value in an expression, enhanced references are taken with .ref
   *            <li>'l'-left value, 
   *            <li>'a'-argument
   *        </ul>    
   * @return
   * @throws ParseException 
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   * @throws InstantiationException 
   */
  public CCodeData gen_value
  ( final ZbnfParseResultItem parent
  , ZbnfParseResultItem zbnfDescription
  , boolean maybeNonPersistent
  , final char intension
  ) 
  throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  { String expr = ""; //"/*gen_value=" + intension + "*/";
    /**Maybe used to concatenate Strings. */
    String sTempRef = null;   
    Iterator<ZbnfParseResultItem> iter= parent.iterChildren();
    ClassData typeVal = null;
    boolean isBoolean = false;
    char modeAccess ='.', modeStatic = '.', modeArrayElement = '.';
    int dimensionArray = 0;
    String sSemantic;;
    String sLiteral = null;
    CCodeData codeValue = null;
    /**If true, then a reference is need. If the value is a enhanced or MTBL-reference, 
     * then .ref should be generated. It depends from the operator before. */
    boolean bRefNeed = false;
    while(iter.hasNext())
    { ZbnfParseResultItem item = iter.next();
      sSemantic = item.getSemantic();
      /**Because the Java.zbnf-script defines a < value> respectively < simpleValue?> in a flatten way,
       * the < simpleValue> is not an extra result item. All items of a value expression are disposed
       * one after another: an optional < unaryOperator> is always followed by a peculiarity of a 
       * < simpleValue>, after them a < binaryOperator> may be followed. 
       * The < conditional> is the last one in this disposal. 
       * But this correct order of items should not be tested here. It is dedicated by the syntax of Java
       * and the syntax of ZBNF script.
       */
      if(sSemantic.equals("StringLiteralMethod"))
      { /* ZBNF-syntax: simpleValue::=<?> ... <""?simpleStringLiteral>[ \. <simpleMethodCall?StringLiteralMethod> ]
         * Because a simpleValue doesn't create a own ZBNF-component, its components are visible here. 
         * Inside {@link #gen_simpleValue(ZbnfParseResultItem, ClassData[], LocalIdents, char) 
         * the <""?simpleStringLiteral> isn't known. It is the sInstance.
         * NOTE: The form z_StringJc("literal") is not able to use in C because forex the literal
         * "example\0second" is a String of 14 chars in Java, including the \0.
         * The routine z_StringJc("example\0second") will build only a String with 7 chars, ending on \0.
         * That is false. Therefore the number of chars is calculated here and given. 
         */
      	assert(sLiteral.charAt(0) == '\"');
      	int zLiteral = sLiteral.length();
      	assert(sLiteral.charAt(zLiteral -1) == '\"');
      	int nrofBackslash =0;  //any backslash builds one char with the following one.
      	for(int ii = 1; ii < zLiteral; ii++){
      		if(sLiteral.charAt(ii) == '\\'){
      			nrofBackslash +=1;
      		}
      	}
      	int nrofCharsLiteral = zLiteral - 2 -nrofBackslash;  //without ""
        
      	String sInstance = "zI_StringJc(" + sLiteral + "," + nrofCharsLiteral + ")";
        
        CCodeData envInstance = new CCodeData(sInstance, CRuntimeJavalikeClassData.clazzStringJc.classTypeInfo);  
        CCodeData codeExpr =
        gen_simpleMethodCall( item, null, envInstance, localIdents, maybeNonPersistent, '.');     //localIdents used for method arguments. 
        expr += codeExpr.cCode;
        typeVal = codeExpr.identInfo.typeClazz;  //the return type of the method, at ex. "xyz".indexOf(char) returns int.
        modeAccess = codeExpr.modeAccess;
        modeStatic = codeExpr.identInfo.modeStatic;  //maybe 'r' for nonPersistent
        sLiteral = null;
      }
      else
      { if(sLiteral != null)
        { expr += sLiteral;
          sLiteral = null;
        }
        
        if(sSemantic.equals("unaryOperator"))
        { String operator = item.getParsedText();
          expr += operator;
        }
        else if(sSemantic.equals("binaryOperator"))
        { String operator = item.getParsedText();
          if(  typeVal != null 
            && operator.equals("+")         
            && ( !typeVal.isPrimitiveType()  //+ on not primitive type can only be a String concatenation.
            		//|| typeVal.getClassIdentName().equals("char const*")
            		|| typeVal == CRuntimeJavalikeClassData.clazz_s0
                )
            )
          { CCodeData firstValue = new CCodeData(expr, typeVal.classTypeInfo, modeAccess);
          	CCodeData cString = gen_ConcatenatedStrings(expr, typeVal, modeAccess, iter, zbnfDescription, null, localIdents, intension);
            expr = cString.cCode;
            typeVal = cString.identInfo.typeClazz; //Java2C_Main.singleton.standardClassData.clazzStringJc;
            modeStatic = cString.identInfo.modeStatic;  //maybe 'r' for nonPersistent
            sTempRef = cString.sTempRef; //maybe null
          }
          else
          {
            expr += " " + operator + " ";
          }  
        }
        else if(sSemantic.equals("cmpOperator"))
        { String operator = item.getParsedText();
          if( codeValue != null 
            && (  operator.equals("==")        //NOTE: compare to null
               || operator.equals("!=")
               )
            && codeValue.getTypeName().equals("StringJc")
            && codeValue.dimensionArrayOrFixSize == 0
            )
          { //normally a StringJc comes as struct, here the pointer to the chars are compared.
            //StringJc is equal the osal-oriented OS_ValuePtr.
            //expr = "getPtr_OS_ValuePtr((" + expr + "), char const*) " + operator + " ";  
            expr = expr + ".ptr__" + operator + " ";  //TODO use isNull_StringJc(expr), 
            //which strings are equal?
          }
          else if(codeValue != null && "@&".indexOf(codeValue.modeAccess)>=0){
          	expr += ".ref" + operator + " ";
          }
          else
          {
            expr += " " + operator + " ";
          }
          bRefNeed = true;
          typeVal = CRuntimeJavalikeClassData.clazz_bool;
          modeAccess = '%';  //boolean immediate value.
          modeStatic = '.';  //not non-persistent
          isBoolean = true;
        }
        else if(sSemantic.equals("booleanOperator"))
        { String operator = item.getParsedText();
          expr += " " + operator + " ";
          typeVal = CRuntimeJavalikeClassData.clazz_bool;
          modeAccess = '%';  //boolean immediate value.      
          modeStatic = '.';  //not non-persistent
          isBoolean = true;
        }
        else if(sSemantic.equals("conditional")) // condition ? truevalue : falsevalue
        { //it should be the last one in the disposal of parts of value.
          expr += " ? ";
          ZbnfParseResultItem zbnfTrueValue = item.getChild("trueValue");
          CCodeData trueValue = gen_value(zbnfTrueValue, zbnfDescription, true, intension);
          if(trueValue.identInfo.modeStatic =='r'){ modeStatic = 'r'; } //it is non-persistent.
          expr += trueValue.cCode;
          ZbnfParseResultItem zbnfFalseValue = item.getChild("falseValue");
          CCodeData falseValue = gen_value(zbnfFalseValue, zbnfDescription, true, intension);
          expr += " : ";
          expr += falseValue.cCode;
          if(falseValue.identInfo.modeStatic =='r'){ modeStatic = 'r'; } //it is non-persistent.
          //the trueValue and the falseValue should have the same properties.
          typeVal = trueValue.identInfo.typeClazz;
          modeAccess = trueValue.modeAccess;
          modeArrayElement = trueValue.identInfo.modeArrayElement;
          isBoolean = false;   
        }    
        else if(sSemantic.equals("assignment"))
        { expr += " = /*? assignment*/";
        }
        else if(sSemantic.equals("simpleStringLiteral"))
        { sLiteral = "\"" + item.getParsedString() + "\"";
          if(sLiteral.startsWith("\"integral"))
            stop();
          typeVal = CRuntimeJavalikeClassData.clazz_s0;
          modeAccess = 't';
          modeStatic = '.'; //not non-persistent 
        }  
        else if(sSemantic.equals("instanceType"))
        { ClassData instanceType = genClass.getType(item, localIdents);
        	genClass.writeContent.addIncludeC(instanceType.sFileName, "instanceof"); 
          String sInstanceType = instanceType.getClassCtype_s();
          CCodeData cCodeExpr = new CCodeData(expr, new FieldData(null, typeVal, null,null,null, '.', modeAccess, dimensionArray, null, modeArrayElement, null));
          String cCodeReference = CRuntimeJavalikeClassData.fieldObjectJc.testAndcast(cCodeExpr, '*');
          expr = " instanceof_ObjectJc(" + cCodeReference + ", &reflection_" + sInstanceType + ")";
          /**Result of expression with current content is the following type: */ 
          typeVal = CRuntimeJavalikeClassData.clazz_bool;
          modeAccess = '&';
          modeArrayElement = '.';
          modeStatic = '.';
          dimensionArray = 0;
        }
        else //it is any peculiarity of simpleValue, need not: if(semantic.equals("simpleValue"))
        { codeValue = gen_simpleValue(item, zbnfDescription, localIdents, maybeNonPersistent, intension, bRefNeed);
          expr += codeValue.cCode;
          modeStatic = codeValue.identInfo.modeStatic;
          if(expr.startsWith("ifc22"))
            stop();
          if(!isBoolean)
          { //the right operator wins, TODO test both!
            ClassData typeValNew = codeValue.identInfo.typeClazz;
            int castScore;
            if(typeVal ==null){
            	//first time to determine the type of the expression, it is the first expression part.
             	typeVal = typeValNew;
            	modeAccess = codeValue.modeAccess;
            	//Note: The codeValue.identInfo may be an array, but codeValue may be the access to an element.
            	dimensionArray = codeValue.dimensionArrayOrFixSize;  
            	modeArrayElement = dimensionArray >0 ? codeValue.identInfo.modeArrayElement : '.'; 
            	modeStatic =  codeValue.identInfo.modeStatic;
            } else if(typeValNew == typeVal || (castScore = typeValNew.matchedToTypeSrc(typeVal)) == ClassData.CastInfo.kCastEqual) {
            	//no change of type.
            	stop();
            } else if(castScore >=ClassData.CastInfo.kCastAutomatic){
            	//The current type value is able to cast in the new automaticly. 
            	//Then take the new because it is more common:
            	typeVal = typeValNew;
            	modeAccess = codeValue.modeAccess;
            } else {
            	//the new typevalue is lesser, because the current is not able to cast.
            	stop();
            }
          }
        }
      }//not "StringLiteralMethod"  
    }//while
    //the expression is a repetition of the parts in while loop, 
    //they are concatenated together in expr.
    if(sLiteral != null)
    { expr += sLiteral;  //the first and only was a simpleStringLiteral
    }
    assert(modeAccess != '.');
    assert(typeVal != null);
    
    if(expr.equals("ythis->formatField"))
      stop();
    
    /*switch(modeAccess)
    { case '$':
      { //the result is an embedded struct. because a value should be either an immediate value
        //or a pointer, it should be referenced.
        modeAccess = '*'; //a simple reference
        expr = "&(" + expr + ")";  //build the pointer.
      }break;
      case '@':
      { stop();
        
      }break;
    }*/  
    FieldData valueInfo = new FieldData
      ("$value", typeVal, null, null, null, modeStatic, modeAccess, dimensionArray, null, modeArrayElement, null); 
    CCodeData retCode = new CCodeData(expr, valueInfo);
    retCode.sTempRef = sTempRef;  //maybe null
    return retCode;
  }


  /**This routine generates an concatenated String.  
   * 
   * @param sFirstString The first part of String expression found in gen_Value before calling this routine.
   * @param firstType The associated type to the sFirstString
   * @param firstModeAccess
   * @param iterZbnf iterator through parse result.
   * @param zbnfDescription description parse result of the whole expression (statement)
   * @param sStringBuilderTmp A given temporary StringBuilder to use.
   * @param localIdents
   * @param creationMode
   * @return
   * @throws ParseException
   * @throws FileNotFoundException
   * @throws IllegalArgumentException
   * @throws IOException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  CCodeData gen_ConcatenatedStrings
  ( String sFirstString
  , ClassData firstType
  , char firstModeAccess
  , Iterator<ZbnfParseResultItem> iterZbnf
  , ZbnfParseResultItem zbnfDescription
  , String sStringBuilderToUse
  , LocalIdents localIdents, char xxxintension
  ) throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  { //String sString;
    StringBuilder uExpr = new StringBuilder(200); 
    ClassData type = firstType;
    String sType;
    genClass.writeContent.addIncludeC("Jc/StringJc", "string concatenation");
    /**The first part of concatenation is given, test the type and build a new StringBuffer to concatenate.
     * The new StringBuffer should be managed in 'newObjx' like all new Objects, see {@link nrofNew}.
     */
    //NOTE: the newObj is of type ObjectJc*, cast it directly, it is the same mem location.
    //sString = "(StringBuilderJc*)(" + gen_newObj() + " = (ObjectJc*)";
    final String sTempString;
    ZbnfParseResultItem zbnfStringBuilderInStack;
    if(sStringBuilderToUse != null){
    	/**An append operation with known StringBuilder-buffer. */
    	sTempString = sStringBuilderToUse;
      if(sFirstString != null){
      	/**The call of the routine is invoked from gen_value(...) or from gen_StringAssignment(...)
      	 * with new used buffer: */
      	assert(false);
      	uExpr.append( GenerateClass.genIndent(indent+1) + "( setLength_StringBuilderJc(" + sStringBuilderToUse + ", 0, _thCxt)"
                + GenerateClass.genIndent(indent+1) + ", ");        
      } else {
      	/**The call of the routine is not invoked from gen_value(...) but from gen_StringAssignment(...)
      	 * with operation +=: */
        uExpr.append( GenerateClass.genIndent(indent+1) + "( ");  //append, do not change
      }
    }
    else if(zbnfDescription != null && (zbnfStringBuilderInStack = zbnfDescription.getChild("StringBuilderInStack")) != null){
      int sizeStringBuilderInStack = (int)zbnfStringBuilderInStack.getParsedInteger();
      //sTempString = gen_StringBuilderInStack(sizeStringBuilderInStack);
      if(this.sizeStringBuilderInStack < sizeStringBuilderInStack){
      	this.sizeStringBuilderInStack = sizeStringBuilderInStack;
      }
      sTempString = "&_stringBuilder.u";
    	uExpr.append( GenerateClass.genIndent(indent+1) + "( setLength_StringBuilderJc(" + sTempString + ", 0, _thCxt)"
              + GenerateClass.genIndent(indent+1) + ", ");        
    } 
    else if(zbnfDescription != null && (zbnfStringBuilderInStack = zbnfDescription.getChild("StringBuilderInThreadCxt")) != null){
      sTempString = "_stringBuilderThCxt";
      this.needPtrStringBuilderInThCxt = true;
    	uExpr.append( GenerateClass.genIndent(indent+1) + "( setLength_StringBuilderJc(" + sTempString + ", 0, _thCxt)"
              + GenerateClass.genIndent(indent+1) + ", ");        
    } 
    else {
      sTempString= gen_tempString();
      uExpr.append( GenerateClass.genIndent(indent+1) + "( " + sTempString + " = new_StringBuilderJc(-1, _thCxt)" 
              + GenerateClass.genIndent(indent+1) + ", setStringConcatBuffer_StringBuilderJc(" + sTempString + ")" 
              + GenerateClass.genIndent(indent+1) + ", ");
    }
    sType = type.getClassIdentName();
    boolean bNext = false;
    if(sFirstString != null){
    	bNext = true;
    	//if     (sType.equals("char const*")){ 
    	if (type == CRuntimeJavalikeClassData.clazz_s0){ 
        uExpr.append( "append_z_StringBuilderJc(" + sTempString + ", " + sFirstString + ", _thCxt)");  
      }
      else if(sType.equals("StringJc")){      
        uExpr.append( "append_s_StringBuilderJc(" + sTempString + ", " + sFirstString + ", _thCxt)");  
      }
      else if(sType.equals("StringBuilderJc")){
        sFirstString = FieldData.testAndChangeAccess('*', sFirstString, firstModeAccess);
        uExpr.append( "append_u_StringBuilderJc(" + sTempString + ", " + sFirstString + ", _thCxt)");  
      }
      else {
      	int scoreInt32 = type.matchedToTypeSrc(CRuntimeJavalikeClassData.clazz_int32);
      	int scoreFloat = type.matchedToTypeSrc(CRuntimeJavalikeClassData.clazz_float);
      	int scoreDouble = type.matchedToTypeSrc(CRuntimeJavalikeClassData.clazz_double);
      	if(scoreDouble == ClassData.CastInfo.kCastEqual){
      	  uExpr.append( "append_D_StringBuilderJc(" + sTempString + ", " + sFirstString + ", _thCxt)");  
        } else if(scoreFloat ==ClassData.CastInfo.kCastEqual){
      	  uExpr.append( "append_F_StringBuilderJc(" + sTempString + ", " + sFirstString + ", _thCxt)");  
        } else if(scoreInt32 >ClassData.CastInfo.kCastNo){ //castable to int32
    	    uExpr.append( "append_I_StringBuilderJc(" + sTempString + ", " + sFirstString + ", _thCxt)");  
        } else {
        	throw new IllegalArgumentException("conversion to append_StringBuilder failed, type: " + sType);
        }
      } 
    }
    while(iterZbnf.hasNext())
    { ZbnfParseResultItem item = iterZbnf.next();
      StringBuilder expr = new StringBuilder(100);
      //ClassData[] retType1 = new ClassData[1];  //classData of the part of expression
      String sSemantic = item.getSemantic();
      
      if(sSemantic.equals("binaryOperator"))
      { //there is only possible a '+'
        //String operator = item.getParsedText();
          
      }
      else
      { if(sSemantic.equals("conditional"))
        { //it should be the last one in the disposal of parts of value.
          expr.append(" ? ");
          ZbnfParseResultItem zbnfTrueValue = item.getChild("trueValue");
          CCodeData trueValue = gen_value(zbnfTrueValue, null, true, 't');
          expr.append(trueValue.cCode);
          ZbnfParseResultItem zbnfFalseValue = item.getChild("falseValue");
          CCodeData falseValue = gen_value(zbnfFalseValue, null, true, 't');
          expr.append(" : ");
          expr.append(falseValue.cCode);
          type = falseValue.identInfo.typeClazz;
        }    
        else //it is any peculiarity of simpleValue, need not: if(semantic.equals("simpleValue"))
        { CCodeData methodCode = gen_simpleValue(item, null, localIdents, true, 't', true);
          expr.append(methodCode.cCode);
          type = methodCode.identInfo.typeClazz;
        }
        //TODO calculate retType
        //type = retType1[0];  //the simple calc
        sType = type == null ? "void" : type.getClassIdentName();
        final String sConcat;
        if     (sType.equals("char const*"))         {  sConcat = "append_z_StringBuilderJc(" + sTempString + ", "+ expr + ", _thCxt)";  }
        else if(sType.equals("StringJc"))      {  sConcat = "append_s_StringBuilderJc("  + sTempString + ", "+ expr + ", _thCxt)";  }
        else if(sType.equals("StringBuilderJc")){  sConcat = "append_u_StringBuilderJc(" + sTempString + ", "+ expr + ", _thCxt)";  }
        else if(sType.equals("int16"))         {  sConcat = "append_I_StringBuilderJc(" + sTempString + ", "+ expr + ", _thCxt)";  }
        else if(sType.equals("int32"))         {  sConcat = "append_I_StringBuilderJc(" + sTempString + ", "+ expr + ", _thCxt)";  }
        else if(sType.equals("int64"))         {  sConcat = "append_J_StringBuilderJc(" + sTempString + ", "+ expr + ", _thCxt)";  }
        else if(sType.equals("float"))         {  sConcat = "append_F_StringBuilderJc(" + sTempString + ", "+ expr + ", _thCxt)";  }
        else if(sType.equals("double"))        {  sConcat = "append_D_StringBuilderJc(" + sTempString + ", "+ expr + ", _thCxt)";  }
        else                                   {  sConcat = "append_L_StringBuilderJc/*" + sType + "*/(" + sTempString + ", " + expr + ", _thCxt)";  }
        if(bNext){
          uExpr.append( GenerateClass.genIndent(indent+1) + ", ");
        }
        uExpr.append( sConcat);
        bNext = true;
      }  
    }
    
    boolean toStringNonPersist = zbnfDescription != null && zbnfDescription.getChild("toStringNonPersist")!= null;    
    if(toStringNonPersist){
    	uExpr.append( GenerateClass.genIndent(indent+1) + ", toStringNonPersist_StringBuilderJc(&(" + sTempString + ")->base.object, _thCxt)" + GenerateClass.genIndent(indent+1) +")");
    } else {
    	uExpr.append( GenerateClass.genIndent(indent+1) + ", toString_StringBuilderJc(&(" + sTempString + ")->base.object, _thCxt)" + GenerateClass.genIndent(indent+1) +")");
    }
    CCodeData codeRet = new CCodeData(uExpr.toString(), CRuntimeJavalikeClassData.clazzStringJc.classTypeInfo);
    if(zbnfDescription !=null && zbnfDescription.getChild("toStringNonPersist") !=null){
    	/**Because the String Buffer should not be persistent, it can be used for append operation to the same String.
    	 * To transport the information, which StringBuilder is used, its name will be placed
    	 * in the return data. This is only done because the toString can be non-persistent.
    	 * In the other way the StringBuilder is freezed and it have not be used for further operations.
    	 * Therefore its name isn't meanfully. */
    	codeRet.sTempRef = sTempString;
    }
    return codeRet;      
    //return "toString_StringBuilderJc(" + sString + ")";
  }




  /**generates the code for ZBNF-< simpleValue>. 
   * A simpleValue is a value without operators, a non calculated value, in opposite to a < value>.
   * But a simpleValue may be an expression accessing a referenced value.
   * Examples are: var, this.var, super.var, ref.var, "xyz", method(a.b),
   *               "xyz".indexof(cc), method().val
   * <ul>
   * <li>A simpleValue may be a < variable>, converted with {@link #gen_variable(ZbnfParseResultItem, LocalIdents, char, org.vishia.java2C.FieldData[])},
   *     but a < variable> can be more as that, especially a left value. This is not considered here.
   * <li>A simple value may be a casted value, written in the syntax in Java2C.zbnf with < ?casting> ( < type> ) < value>.
   * <li>There is a special case of casting: (String)null will be converted to null_StringJc, because the simple null fails in C.    
   * <li>A simple value may be such as <code>this</code> or <code>super</code>.
   * <li>A simple value can be a new Object, generated with {@link #gen_newObject(ZbnfParseResultItem, CCodeData, LocalIdents)}. 
   * <li>A < methodCall> is also a variant of simple value. Thats why this method is also used to generate a < methodCall>
   *     in statements. A < methodCall> tests wether it is referenced, than {@link #gen_simpleMethodCall(ZbnfParseResultItem, String, org.vishia.java2C.FieldData, LocalIdents)}
   *     is called.
   * </ul>
   * @param zbnfItem One of the alternatives in <code>simpleValue::=...</code>
   * @param localIdents of the environment
   * @param intension calling intension.
   * @return
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   */
  public CCodeData gen_simpleValue(
  	  ZbnfParseResultItem zbnfItem, ZbnfParseResultItem zbnfDescription
  	, final LocalIdents localIdents
  	, boolean maybeNonPersistent
    , final char intension, final boolean bRefNeed)
  throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  { CCodeData simpleValue; // = new CCodeData(); // = "";
    ClassData[] retType = new ClassData[1];
    String sSemantic = zbnfItem.getSemantic();
    simpleValue = genClass.genConstantValue(zbnfItem);  //simpleStringLiteral, hexValue etc.
    if(simpleValue != null)
    { //done
      retType[0] = simpleValue.identInfo.typeClazz;
    }
    else if(sSemantic.equals("casting"))
    { ZbnfParseResultItem zbnfValue = zbnfItem.getChild("value");
    	if(zbnfValue == null){ 
    		zbnfValue = zbnfItem;  //it contains the value elements. 
    	}
    	CCodeData codeValue = gen_value(zbnfValue, zbnfDescription, true, 'e');
    	ZbnfParseResultItem zbnfType = zbnfItem.getChild("type");    //<typeIdent>
    	ClassData typeClazz = genClass.getType(zbnfType, localIdents);
      String castType = typeClazz.getClassIdentName();
      final FieldData castedField;
      if(codeValue.identInfo.modeStatic =='r'){
      	castedField = new FieldData(typeClazz.classTypeInfo, 0, 0, '.', 'r');
      } else {
      	castedField = typeClazz.classTypeInfo;
      }
      simpleValue = new CCodeData("(" + castType + ")", castedField);
      zbnfItem = zbnfItem.next();                     //<simpleValue>
      String valueToCast = codeValue.cCode; 
      	//gen_value(zbnfItem, zbnfDescription, retType, localIdents, true, 'e');
      if(valueToCast.equals("null") && castType.equals("StringJc"))
      { //special case (StringJc)null
        simpleValue.cCode = "null_StringJc";
      }
      else
      { //common case: (castType) value
        simpleValue.cCode += valueToCast;
      }  
      retType[0] = typeClazz;  //overwrites the retType from simpleValue().
    }
    else if(sSemantic.equals("parenthesisExpression"))
    { simpleValue = gen_value(zbnfItem, zbnfDescription, true, 'e');
      simpleValue.cCode = "(" + simpleValue.cCode + ")";
    }
    else if(sSemantic.equals("variable"))
    { //FieldData typeLastVariable[] = new FieldData[1];
      simpleValue = gen_variableAccess(zbnfItem, zbnfDescription, localIdents, intension, genClass.classData.thisCodeInfo); //, typeLastVariable);
      if(simpleValue.modeAccess == '@'){
        //simpleValue.cCode += ".ref"; }  
        simpleValue.cCode = "REFJc(" + simpleValue.cCode + ")"; 
        simpleValue.modeAccess = '*';
      } else if(simpleValue.modeAccess == '&' && bRefNeed){
      	simpleValue.cCode += ".ref";  //method-table-reference, get the reference itself
      }
      //if(  simpleValue.identInfo.fixArraySizes != null 
      //  && simpleValue.identInfo.modeAccess == '$'
      //  && simpleValue.dimensionArrayOrFixSize >0
      //  )
      if(simpleValue.modeAccess == 'Y')
      { /**it is defined as an embedded array structure:
         * cast it to its known array pointer type. Otherwise it isn't useable.
         * NOTE: The representation of a value is a reference in all cases. 
         */
        String sArrayPostifx = simpleValue.identInfo.modeArrayElement == '*' ? "_YP_t*" : "_Y_t*";
        simpleValue.cCode = "(struct " + simpleValue.identInfo.typeClazz.getClassIdentName() + sArrayPostifx + ")" 
                          + "(&( " + simpleValue.cCode + "))";
        simpleValue.modeAccess = 'X'; //it is now a reference to an array.
      }
      
      retType[0] = simpleValue.identInfo.typeClazz; //typeLastVariable[0].typeClazz;
    }
    else if(sSemantic.equals("newObject"))
    { simpleValue = gen_newObject(zbnfItem, null); //, localIdents);
    }
    else if(sSemantic.equals("newArray"))
    { String sValue = gen_newArray(zbnfItem, retType, localIdents, null);
      simpleValue = new CCodeData(sValue, retType[0].classTypeInfo);
    }
    else if(sSemantic.equals("methodCall"))
    { { String sMethodName = zbnfItem.getChild("methodName").getParsedString();
        if(sMethodName.equals("format"))
          stop();
      }
      ZbnfParseResultItem itemReference = zbnfItem.getChild("reference");
      CCodeData reference;
      final String cCode;
      String[] sConcatenatedReference = new String[1]; 
      if(itemReference != null)
      { 
        reference = gen_reference(sConcatenatedReference, itemReference, zbnfDescription, localIdents, genClass.classData.thisCodeInfo, 'm'); //, typeReference); // retIdentInfo);
        if(true){}
        else if(reference.isReturnThis()){
          cCode = "(" + reference.cCode;
          reference.cCode = "_temp" + nrofTempRefForConcat;
        }
        else if(reference.sTempRef !=null) {
          cCode = "(" + reference.cCode;
          reference.cCode = reference.sTempRef;
        }
        else{ cCode = null; }
      }
      else
      { //no reference before method call, either it is a static method or this-method.
        reference = genClass.classData.thisCodeInfo;
        cCode = null;
      }
      //if it is a static method, the sInstance will be ignored.
      simpleValue = gen_simpleMethodCall
                    ( zbnfItem
                    , zbnfDescription    
                    , reference //sInstanceRef
                    , localIdents     //used for method arguments. 
                    , maybeNonPersistent
                    , intension
                    );
      if(sConcatenatedReference[0]!=null){
        /**A call of methods and maybe assignment to internal temporary references is returned: */
        simpleValue.cCode = GenerateClass.genIndent(indent+1) + "( " + sConcatenatedReference[0] + simpleValue.cCode + GenerateClass.genIndent(indent+1) + ")";
      } 
      if(false && cCode != null){
        /**_temp is used, take the setting of _temp in simpleValue.cCode. */
        simpleValue.cCode = cCode + simpleValue.cCode + ")";
      }  
    }
    else
    { throw new ParseException("unexpected semantic:" + sSemantic,0);
    }
    return simpleValue;
  }


  /**generates a simple method call. It includes the evaluation of actual parameters 
   * using {@link #gen_value(ZbnfParseResultItem, ClassData[], LocalIdents, char)}.
   * The method-name is built with the Java-method-name, following by the class name as postfix.
   * In C all methods should have a unique name.
   * <br>
   * If it is a class method, the reference of the class is generated as the first argument.
   * Depended on the kind of the class reference, a <code>&(ref)</code> is generated if it is an embedded reference.
   * @param parent
   * @param sInstanceRef The generated C-Code for the reference to the methods class-instance. 
   *        It is the output from {@link #gen_reference(String[], ZbnfParseResultItem, LocalIdents, char, org.vishia.java2C.FieldData[])}
   *        For static methods this parameter is null.
   *        If it is a constructor call, this  is either the reference to the outer class or null.
   * @param envInstanceInfo The type-info of the class from which the method is member of.
   * @param localIdents The local identifier of this statement block level used for parameter values.
   * @param maybeNonPersist The result is accepted as non-persistent too.
   * @return
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   * @throws InstantiationException 
   */
  private CCodeData gen_simpleMethodCall
  ( ZbnfParseResultItem zbnfMethod
  , ZbnfParseResultItem zbnfDescription    
  , CCodeData envInstance
  , LocalIdents localIdents
  , boolean maybeNonPersistent
  , char intension
  )
  throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  { String sMethodNameJava = zbnfMethod.getChild("methodName").getParsedString();
    if(sMethodNameJava.equals("txAnswer"))
      stop();
    String sNewObject = null; //no new Object, for param nrofNew
    if(sMethodNameJava.equals("_sizeof"))
    { 
      return new CCodeData("sizeof(" + envInstance.identInfo.getName() + ")", CRuntimeJavalikeClassData.clazz_int.classTypeInfo);
    }
    else { 
      ClassData classOfMethod = envInstance.identInfo.instanceClazz != null 
                              ? envInstance.identInfo.instanceClazz 
                              : envInstance.identInfo.typeClazz ; 
      CCodeData methodCode = gen_InternalMethodCall(
      		zbnfMethod, zbnfDescription, sMethodNameJava, classOfMethod, envInstance
      		, sNewObject//, localIdents
      		);
      if(methodCode.cCode.contains("createInstance"))
        stop();
      if(methodCode.identInfo.modeStatic == 'n'){
        /**Because the return value of the method may be a new instance, which isn't activated for garbage collection yet,
         * it is stored in a temp reference and activated on return. */
        String sTempRef = tempRefForConcat(methodCode.identInfo);
        String cCode = "(" + sTempRef + " = " + methodCode.cCode + ")";
        methodCode.cCode = cCode;
      }
      final String sTypeRetrurn = methodCode.getTypeName();
          
      if(methodCode.identInfo.modeStatic == 'r' //non-persistent StringJc
      	&& intension != 't'  //no string concatenation.
      	&& (zbnfDescription== null || zbnfDescription.getChild("toStringNonPersist") == null)
      	&& !maybeNonPersistent
      	&& methodCode.identInfo.modeAccess == 't'  //String type
      	){
        /**If the type of the parameter is 'StringJc', the cCode represents any expression
         * which expression type is 'StringJc'. The String may be stored non-persistent.
         * To establish cleaned data conditions, the String should be made persistent.
         * But this action is not done, if the user prevent it by setting an annotation against it.
         */ 
      	String sTempVariable = gen_persistringVariable();
      	String sEnvType = envInstance.identInfo.typeClazz.getClassIdentName();
      	if(sMethodNameJava.equals("toString") && sEnvType.equals("StringBuilderJc")){
      	  /**Change the name of the called method. */
      		methodCode.cCode = sTempVariable + " = toStringPersist" + methodCode.cCode.substring(8); 
      	} else {
      		methodCode.cCode = sTempVariable + " = persist_StringJc(" + methodCode.cCode + ")";
      	}
      }
      return methodCode;
    }  
  }


  
  
  
  /**generates either a new or a simple method call. It includes the evaluation of actual parameters 
   * using {@link #gen_value(ZbnfParseResultItem, ClassData[], LocalIdents, char)}.
   * The method-name is built with the Java-method-name, following by the class name as postfix.
   * In C all methods should have a unique name.
   * <br>
   * If it is a class method, the reference of the class is generated as the first argument.
   * Depended on the kind of the class reference, a <code>&(ref)</code> is generated if it is an embedded reference.
   TODO
   * @param zbnfMethod Zbnf parse result item from <code>simpleMethodCall::=</code> 
   *        or <code>newObject::=</code> or <code>[<?superCall> super ...]</code>,
   *        may be null if a default constructor is called. 
   * @param sMethodNameJava The methodname from Java
   * @param declaringClass The class where the method should be member of. 
   *        Mostly it is the ClassData of the envInstance: {@link CCodeData#identInfo} and there
   *        {@link FieldData#typeClazz}, but if the super class is accessed, it is the super class of them. 
   * @param envInstance Type and name of the reference to the instance, from which the method is called. 
   *        It is the output from {@link #gen_reference(String[], ZbnfParseResultItem, LocalIdents, char, org.vishia.java2C.FieldData[])}
   *        For static methods this parameter is null.<br>
   *        If it is a constructor call, this  is either the reference to the outer class: 
   *        The constructor respectively new(...) is a method of the outer class: outer.new(...)
   *        Or null it should be null: The constructor respectively new(...) of not-inner classes is a static method.
   * @param sNewObject If it is a constructor call, the generated C-Code for access the new Object, else null.
   *        it is used as second argument of ctor(...) respectively first argument of ctor(...) if envInstance == null.
   * @param localIdents The local identifier of this statement block level used for parameter values.
   * @return
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   * @throws InstantiationException 
   */
  CCodeData gen_InternalMethodCall
  ( ZbnfParseResultItem zbnfMethod
  , ZbnfParseResultItem zbnfDescription    
  , String sMethodNameJava
  , ClassData declaringClass
  , final CCodeData envInstance
  , String sNewObject
  //, LocalIdents localIdents
  )
  throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  //, InstantiationException
  {
    String ret;
    boolean bEmbeddedType = false;
    String sMethodEnvType;
    boolean ctorCall = false;
    boolean toStringNonPersist = zbnfDescription != null && zbnfDescription.getChild("toStringNonPersist")!= null;    
    if(sMethodNameJava.equals("this")){
    	//call of another ctor of the same class, search ctor
    	sMethodNameJava = "ctorO";
    	ctorCall = true;
    } else if(sMethodNameJava.equals("super")){
    	stop();
    }
    if(sMethodNameJava.equals("setBigEndian") || sMethodNameJava.equals("ctorO_C_INNER"))
    	stop();
    if(envInstance != null) // it is not a implicit this-access
    { //The reference type is defined in an other class, include it!
      boolean bTypeSign = true;
      switch(envInstance.modeAccess)       //envInstanceInfo.sModifier.charAt(0))
      { case '%': case '$': bEmbeddedType = true; break;
        default: bTypeSign = false;
      }
      if(bTypeSign)
      { //sMethodEnvType = sMethodEnvType.substring(1);
      }
      sMethodEnvType = envInstance.getTypeName();
    }
    else
    { //a static method, no environment instance is given.
      sMethodEnvType = genClass.classData.sClassNameC;
    }
    if(sMethodEnvType.equals("FormatterJc")) //&& sMethodEnvType.equals("Target"))
      stop();
    //List<String> paramsC;
    //List<ClassData> paramsType;
    if(declaringClass != null)
    { genClass.writeContent.addIncludeC(declaringClass.sFileName, "method call");
    }
    final List<CCodeData> actParams = gatherActParams(zbnfMethod, zbnfDescription, sMethodNameJava);
    final Method method;
    String[] sPathMtbl = new String[1];
    if(sMethodNameJava.equals("txAnswer"))
      stop();
    method = declaringClass.searchMethod(sMethodNameJava, actParams, true, sPathMtbl);
    if(method == null){
    	//Because methods are searched strict, it may not be found though there are known in Java.
    	//reasons: typical, missed in stc-file.
    	//Give a explicite message:
    	StringBuilder uMsg = new StringBuilder(200);
    	uMsg.append("method not found: ").append(sMethodNameJava).append("-_$: void %..return");
    	String sSep = "(";
    	if(actParams !=null){
    	  for(CCodeData param: actParams){
	    		uMsg.append(sSep).append(param.identInfo.writeStruct());  //the type info
	    		sSep = ", ";
	    	}
	    } uMsg.append(");");
	    if(actParams !=null){
    	  uMsg.append(" called with param: ");
	    	for(CCodeData param: actParams){
	    		uMsg.append(param.cCode).append(", ");
	    	}
	    }
    	uMsg.append("\nsearched in class: ");
    	uMsg.append(declaringClass.getClassNameJavaFullqualified()).append(".java");
    	uMsg.append(".\nSource of class: \"").append(declaringClass.sSourceOfClassData);
    	uMsg.append("\".\nHint: Check the pre-translated or manual given stc-file. The method may be defined also in outer- or super- classes. ");
    	throw new IllegalArgumentException(uMsg.toString()); 
    }
    if(method.sCName.startsWith("ctor"))
    	stop();
    if(method.sCName.equals("ctorO_f_FileOutputStreamJc")) // && envInstance.cCode.equals(""))
      stop();
    final String sInstanceRef; //param value for ythis
    //assert(method.isStatic() && envInstance == null || method.sCName.startsWith("ctor"));
    if(!method.isStatic() &&  envInstance != null && envInstance.cCode != null)
    //if(envInstance != null && envInstance.cCode != null)
    { /**The C-reference of this for calling the method. A cast to super types and/or a access correction is done. 
       * At example from embedded instance to a reference, writing &cCode. */
      if(envInstance.modeAccess == 'C'){
        /**A class type, it should be a static method! */
        if(method.isUnknownMethod()){
          sInstanceRef = null;   //force exception if used.
        } else {
      	  throw new IllegalArgumentException("method is not static:" + method.sCName);
        }  
      } else {
      	/**cast to the  method's class, but the cast ability from the envInstance to the method's class is tested too. */ 
      	sInstanceRef = method.firstDeclaringClass.classTypeInfo.testAndcast(envInstance, '.');
      }
    } else { 
      sInstanceRef = null;   //force exception if used.
    }
    
    /**Method name for C is found, it is the complete name inclusively type information.
     * at example append_i_StringBuilderJc when searched StringBuffer.append(int) */
    if(envInstance != null && envInstance.identInfo.instanceClazz != null){
      /**call non-dynamic, type cast necessary: */
      /**Simple method call: */
      if(method.sCName.equals("toString_StringBuilderJc") && toStringNonPersist){
        ret = "toStringNonPersist_StringBuilderJc(";
      } else {
      	ret = method.sImplementationName + "(";
      }
    }
    else if(method.isOverrideable()){
      if(envInstance.modeAccess == '&' && method.declaringClass == envInstance.identInfo.typeClazz){
        /**The envInstance contains the required method table. */
        ret = envInstance.cCode + ".mtbl->" + method.sPathToMtbl + method.primaryMethod.sNameUnambiguous + "(";
      } 
      //else if(envInstance.cCode.equals("ythis")){
      else if(envInstance.modeAccess == '~'){
        /**Access of the own instance. */
        secondpass.bUse_mtthis = true;
        if(declaringClass == method.declaringClass){
          ret = "mtthis->" + method.sPathToMtbl + method.primaryMethod.sNameUnambiguous + "(";
        } else {
        	//Method of a base class ////
        
        	ret = "mtthis->" + sPathMtbl[0] + method.sPathToMtbl + method.primaryMethod.sNameUnambiguous + "(";
        }
      }
      else if(false && envInstance.modeAccess == '@' && method.declaringClass == envInstance.identInfo.typeClazz){
        /**The envInstance is an enhanced reference with the appropriate type. */
        ret = "/*enhancedRef-Instance*/";
      } 
      else {
        /**A method table should be build temporary. Problem is: it is in line. 
         * A variable should be defined already.
         * Therefore an auto generated method table reference is need as stack variable. 
         * It may be possible it is exitsting already: */
        String nameMtbl = envInstance.cCode.replace("->", "_");
        String sMtblRef = "mtbl_" + nameMtbl + "_";
        FieldData mtblRef = localIdents.get(sMtblRef);
        if(mtblRef == null){
          /**Create it: */
          mtblRef = new FieldData(sMtblRef, envInstance.identInfo.typeClazz, null, null, null, '.', 'm', 0, null, '.', genClass.classData);
          localIdents.putLocalElement(sMtblRef, mtblRef);
          if(mtblVariables == null){ mtblVariables = new LinkedList<FieldData>(); }
          mtblVariables.add(mtblRef);
        }
        String sMtblType = mtblRef.typeClazz.getClassIdentName(); 
        //ret =  GenerateClass.genIndent(indent) + "//J2C: set mtbl reference";
        //ret = "( " +  sMtblRef + " =(Mtbl_" + sMtblType 
        ret = "((Mtbl_" + sMtblType 
             + " const*)getMtbl_ObjectJc(&(" + sInstanceRef + ")->base.object, sign_Mtbl_"  
             + sMtblType + ") )->" + method.sPathToMtbl + method.primaryMethod.sNameUnambiguous + "(";
        //ret = sMtblRef + "->" + method.sNameUnambiguous + "(";
      }
    } else if(method.sCName.equals("toString_StringBuilderJc") && toStringNonPersist){
      ret = "toStringNonPersist_StringBuilderJc(";
    } else {
      /**Simple method call: */
      ret = method.sCName + "(";
    }
    if(method.returnType.typeClazz == CRuntimeJavalikeClassData.clazz_unknown){
      ret += "/*J2C:unknownMethod*/";
    }
    if(ret.equals("alloc_MemC("))
      stop();
    
    FieldData[] formalParams = method.paramsType;
  
    String sParamSep;
    if( !method.isStatic()   //mode cant't be a static
      &&( sInstanceRef != null && sInstanceRef.length()>0)
      )
    { if(method.firstDeclaringClass.isInterface()){
        /**The reference to the class instance as first parameter is reference to an interface via ObjectJc. */
        ret += "&((" + sInstanceRef + ")->base.object)";  //type of interface, but this is expected.
      }
      else {
        /**The reference to the class instance as first parameter. */
        if(envInstance.identInfo.instanceClazz != null){
          ret += sInstanceRef;  //type of interface, but this is expected.
        }
        else if(method.sPathToBase.length()>0){
        	if(method.firstDeclaringClass != null){
            ret += sInstanceRef + "/*J2cT1*/";
        	} else {	
            ret += "(&(" + sInstanceRef + ")->" + method.sPathToBase.substring(1) + ")"; //NOTE: starts with "."  
        	}
        } 
        else {
          ret += sInstanceRef;
        }
      }  
      sParamSep = ", ";
    }  
    else
    { //call of static method because no sInstanceRef or its a static method
      if(( sInstanceRef != null && sInstanceRef.length()>0))
        assert(sInstanceRef.equals("ythis")); //NOTE: ythis-reference is provided always, if no reference is given. 
      sParamSep = "/*static*/";
    }
    if(ctorCall){
    	//call of another ctorO of the same class. Java: this(param); as first statement.
      ret += "othis";     //it should be defined in the head of the enclosing ctor
      sParamSep = ", ";
    }
    if(sNewObject != null)
    { //to generate new Object with ctor(alloc...)
      ret += sParamSep + sNewObject;
      sParamSep = ", ";
    }
    
    if(actParams != null)
    { int idxParam = 0;
      /*
      if(paramsType == null || formalParams == null)
      { for(String param: paramsC)
        { ret += sParamSep + param;
          sParamSep = ", ";
        }
      }
      else
      */
      { //Iterator<ClassData> iterActParam = paramsType.iterator();
        StringBuilder sTypeVaArg = null;
        StringBuilder sValueVaArg = null;
        //for(String param: paramsC)
        for(CCodeData actParam: actParams)
        { //ClassData actParam = iterActParam.next();
          final String actParamFinit;
          if(sTypeVaArg != null)
          { //there are variable arguments.
            sValueVaArg.append(sParamSep).append(actParam.cCode);
            sTypeVaArg.append(actParam.identInfo.getTypeChar());
            actParamFinit = "";
          }
          else if(formalParams == null)
          { //no method found
            if(actParam.modeAccess == '$'){
              /**Argument of a unknown method, if it is embedded, dereference it" */
              actParamFinit = sParamSep + "(&(" + actParam.cCode + "))";
              
            } else {
              /**Argument of a unknown method, take it without casting. " */
              actParamFinit = sParamSep + actParam.cCode;
            }
          }
          else
          { FieldData formalParam = formalParams[idxParam];
            idxParam +=1;
          	if(formalParam.typeClazz == CRuntimeJavalikeClassData.clazz_va_argRaw)
            { //all following arguments are variable.
              idxParam -=1;  //don't increment.
          		sTypeVaArg = new StringBuilder();
              sValueVaArg = new StringBuilder();
              sValueVaArg.append(sParamSep).append(actParam.cCode);
              sTypeVaArg.append(actParam.identInfo.getTypeChar());
              actParamFinit = "";
            } else if(formalParam.modeAccess == '&'){
              final String sActParam = genTemp_mtblRef(formalParam, actParam);
              actParamFinit = GenerateClass.genIndent(indent+1) + sParamSep + sActParam;
            } else {
              /**Normal assignment, access correction may be neccessary. */
              final String sActParam = formalParam.testAndcast(actParam, '.');
              actParamFinit = sParamSep + sActParam;
            }
          }
          ret+= actParamFinit;
          sParamSep = ", ";
        }
        if(sTypeVaArg != null)
        { //variable arguments
          ret += sParamSep + "\"" + sTypeVaArg + '\"' + sValueVaArg; 
        }
      }  
    }  
    if(method.sCName.equals("start_ThreadJc")){
      ret += ", " + gen_StackSize(zbnfDescription);
    }
    if(method.need_thCxt)
    { ret += sParamSep + "_thCxt)";
    }
    else
    { ret += ")";
    }
    final FieldData retInfo;
    final char modeAccess;
    final char returnMode = method.isReturnNew() ? 'n' : method.isReturnThis() ? 't' : '.'; 
    final boolean bReturnThis = method.isReturnThis();
    if(bReturnThis){
      if(  envInstance.modeAccess == method.returnType.modeAccess
        || envInstance.modeAccess == '~' && method.returnType.modeAccess == '*'
        ){
        retInfo = envInstance.identInfo;   //same as calling instance, there are stored at ex. an instanceClass. 
        modeAccess = envInstance.modeAccess;
      } 
      else if(envInstance.modeAccess== '~'){
        /**The type is the same, but the special case 'call own method' is detect. */
        assert(false);
        assert(method.returnType.modeAccess == '*');
        retInfo = new FieldData(envInstance.identInfo, 0, 0, '~', '.');
        modeAccess = envInstance.modeAccess;
      }
      else {
        /**The type is the same, but the provision instance has another access,
         * typical it is: The calling instance is an embedded one, but the return is a reference.
         */
        retInfo = new FieldData(envInstance.identInfo, 0, 0, method.returnType.modeAccess, '.');
        modeAccess = method.returnType.modeAccess;
      }
    } else {
      /**Other return as this. */
      retInfo = method.returnType;       //standard FieldData-description of a returned instance.
      modeAccess = method.returnType.modeAccess;
    }  
    return new CCodeData(ret, retInfo, modeAccess, returnMode);
    
  }
  
  
  
  
  /**
   * @param zbnfMethod
   * @param zbnfDescription
   * 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws ParseException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   * @throws InstantiationException 
   */
  List<CCodeData> gatherActParams(ZbnfParseResultItem zbnfMethod, ZbnfParseResultItem zbnfDescription
  , String sMethodNameJava
  ) throws FileNotFoundException, IllegalArgumentException, ParseException, IOException, IllegalAccessException, InstantiationException
  { final List<CCodeData> actParams;
    final FieldData[] actParamsArray;
    if(sMethodNameJava.equals("arraycopy"))
    	stop();
    ZbnfParseResultItem args = zbnfMethod == null ? null : zbnfMethod.getChild("actualArguments");
    /**The actual arguments are evaluated in value AND in theire type.
     * The types are stored in paramsType. They are relevant for searching the correct method.
     */
    if(args != null)
    { //paramsC = new LinkedList<String>();
      //paramsType = new LinkedList<ClassData>();
      actParams = new LinkedList<CCodeData>();
      Iterator<ZbnfParseResultItem> iterArgs = args.iterChildren();
      while(iterArgs.hasNext())
      { ZbnfParseResultItem arg = iterArgs.next();
        String argSemantic = arg.getSemantic();
        //ClassData[] typeValue = new ClassData[1];  //classData of the part of expression
        if(argSemantic.equals("value"))
        { CCodeData actParam = gen_value(arg, zbnfDescription, true, 'a');
          actParams.add(actParam);
          //String sValue = gen_value(arg, typeValue, localIdents, 'a');
          //paramsC.add(sValue);
          //paramsType.add(typeValue[0]);
        }
        else if(argSemantic.equals("objectAccess"))
        { actParams.add(new CCodeData("?objectAccess", CRuntimeJavalikeClassData.clazz_void.classTypeInfo));
        }
        else
        { actParams.add(new CCodeData("?unknown", CRuntimeJavalikeClassData.clazz_void.classTypeInfo));
        }
      }
      actParamsArray = new FieldData[actParams.size()];
      int idxActParams = 0;
      for(CCodeData actParam: actParams)
      { actParamsArray[idxActParams++] = actParam.identInfo; //NOTE: not used, searchMethod needs CCodeData, not only FieldData, because an array access should be recognized.
      }
    }
    else
    { //no parameter of the method call.
      //paramsC = null;
      //paramsType = null;
      actParams = null;
      actParamsArray = null;
    }
    //if(envInstanceInfo.typeClazz != null)
    //if(envInstance.type != null)
    /**In Java the methods are recognized with their parameter types, so in C++ 
     * (overload methods, parameter sensitive method calls). 
     * But in C the method name is unambiguously assigned to one method.
     * Therefore the method name have to be built sensitive to the java parameter.
     * The method will be searched with knowledge of actual parameter types.
     */
    //ClassData.Method method = envInstanceInfo.typeClazz.searchMethod(sMethodName, paramsType);
    /**test if call via method table is necessary: */
    return actParams;
  }
  
  
  
  String gen_StackSize(ZbnfParseResultItem zbnfDescription) //, LocalIdents localIdents) 
  throws FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException, ParseException
  { StringBuilder ret = new StringBuilder(100);
    ret.append("-1"); //default stacktrace
    if(zbnfDescription != null){
      ZbnfParseResultItem zbnfStackSize = zbnfDescription.getChild("stackSize");
      if(zbnfStackSize != null){
        List<ZbnfParseResultItem> listItems = zbnfStackSize.listChildren();
        String separator = "";
        ret.setLength(0);
        for(ZbnfParseResultItem item: listItems){
          String semantic = item.getSemantic();
          if(semantic.equals("type")){
            String sType = item.getParsedString();
            /**It may be called a nested translation: */
            ClassData instanceType = localIdents.getType(sType, genClass.fileLevelIdents); 
            String sInstanceType = instanceType.getClassCtype_s();
            ret.append(separator).append("sizeof(").append(sInstanceType).append(")");
          }
          else if(semantic.equals("bytes")){
            long bytes = item.getParsedInteger();
            ret.append(separator).append(bytes);
          }
          separator = "+";
        }
      }
    }
    return ret.toString();
  }
  
  /**generates the expression for a new Type(...) expression.
   * For a new Object, a MemC-instance is necessary. It will be generated in
   * {@link #gen_statementBlock(ZbnfParseResultItem, int, LocalIdents)}.
   * The variable {@link #nrofNew} is used and incremented for that.
   * 
   * @param zbnfNewObject The zbnf parse result item of the < newObject>
   * @param reference A reference before .new, used for inner non-static classes, or null for static or first-level classes.
   * @param idents The identifier of the environment.
   * @return generated C-code.
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   */
  private CCodeData gen_newObject(
  	ZbnfParseResultItem zbnfNewObject
  , final CCodeData referenceP
  //, LocalIdents idents
  ) 
  throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  { final ClassData typeOfNew;
    final ClassData refClass;
	  final LocalIdents localIdentsNewObj;
	  final LocalIdents idents = this.localIdents;
	  if(referenceP != null){
      //typeOfNew = getType(zbnfNewObject.getChild("newClass"), reference.identInfo.typeClazz.classLevelIdents);  //itemNewObject.getChild("newClass").getParsedString();
      refClass = referenceP.identInfo.typeClazz;
      localIdentsNewObj = referenceP.getClassLevelIdents();  //search the new Type only in reference context.
	  } else {
      //typeOfNew = getType(zbnfNewObject.getChild("newClass"), idents);  //itemNewObject.getChild("newClass").getParsedString();
      refClass = genClass.classData;
      localIdentsNewObj = idents;     //search it in context.
	  }
  	
  	FieldData fieldNew = genClass.createFieldDataNewObject(zbnfNewObject, null, localIdentsNewObj, idents, this, null, refClass, 'b', '*', '.', true);
  	typeOfNew = fieldNew.instanceClazz;
  	//retTypeValue[0] = typeOfNew;
    String sTypeNewObject = typeOfNew.getClassCtype_s();
    if(sTypeNewObject.equals("StringJc"))
    {
      //reference = new CCodeData(null, typeOfNew.classTypeInfo);//static method call but the method class is given here. 
      /**call of a new_StringJc(...)-method: */
      return gen_InternalMethodCall(zbnfNewObject, null, "new", typeOfNew, referenceP, null); //, idents);
      //return gen_InternalMethodCall(zbnfNewObject, null, "new", reference.identInfo.typeClazz, reference, null, idents);
    }
    else
    { //induce to generate the necesarry include statement:
      //typeOfNew may be null if the type is a external type.
      if(typeOfNew != null)
      { genClass.writeContent.addIncludeC(typeOfNew.sFileName, "new object");
      }
      //retTypeValue[0] = typeOfNew;
      if(sTypeNewObject.equals("C_INNER_TestAnonymous_Test_s"))
      	stop();
      //String sInstanceRef = null;  //TODO if the instance is a non static inner class, in Java: instance.new(...) 
      //String sInstanceRef = genClass.classData.thisCodeInfo;
      String sInstanceRef = "ythis";
      String sNewObject = "(" + gen_newObj() + " = alloc_ObjectJc(sizeof_" + sTypeNewObject + ", 0, _thCxt))";
      final CCodeData reference =
      	typeOfNew.isNonStaticInner 
      	? ( referenceP !=null //if the reference is given, use it.
      		? referenceP
      		: new CCodeData(sInstanceRef, refClass.classTypeInfo))  //this  ////
      	: null;   //!nonStaticInner, then no reference, the referenceP is the type.
      final ClassData declaringClass;
      final String sNameCtor;
      if(typeOfNew.isNonStaticInner){
      	/**ctor of a non-static inner class: The ctor is defined in the outer class
      	 * because it needs the this-reference of the outer class. */
      	declaringClass = reference.identInfo.typeClazz;
      	sNameCtor = (typeOfNew.isBasedOnObject()?  "ctorO_" : "ctorM_") + typeOfNew.getClassNameJava();
      } else {
      	/**ctor of a static inner or package-level-class: Search the ctor
      	 * inside the type-of-new-class, it will be static there. */
      	declaringClass = typeOfNew;
      	sNameCtor = fieldNew.typeClazz.isBasedOnObject()?  "ctorO" : "ctorM";
      }
      //return gen_InternalMethodCall(zbnfNewObject, null, "ctorO", reference.identInfo.typeClazz, reference, sNewObject, idents);
      return gen_InternalMethodCall(zbnfNewObject, null, sNameCtor, declaringClass, reference, sNewObject); //, idents);
    }
  }



  /**generates the expression for a new Type[...] expression.
   * For a new Object, a Object-instance is necessary. It will be generated using
   * {@link #gen_newObj()}.
   * The variable {@link #nrofNew} is used and incremented for that.
   * <br>
   * If a variable is given, and it is an embedded instance, no new Object is allocated,
   * but the constructor for the given embedded instance is called.
   * 
   * @param zbnfNewArray The zbnf parse result item of the < newObject>
   * @param idents The identifier of the environment.
   * @param variable The variable to assign to, or null
   * @return generated C-code.
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   */
  public String gen_newArray(ZbnfParseResultItem zbnfNewArray, ClassData[] retTypeValue, LocalIdents idents, FieldData variable) 
  throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  { String sRet;
    ClassData typeOfNew = genClass.getType(zbnfNewArray.getChild("newClass"), idents);  //itemNewObject.getChild("newClass").getParsedString();
    retTypeValue[0] = typeOfNew;
    String sTypeNewObject = typeOfNew.getClassCtype_s();
    String sIdentNameNewObject = typeOfNew.getClassIdentName();
    //induce to generate the necesarry include statement:
    //typeOfNew may be null if the type is a external type.
    if(typeOfNew != null && typeOfNew.sFileName != null)
    { genClass.writeContent.addIncludeC(typeOfNew.sFileName, "new array");
    }
    //retTypeValue[0] = typeOfNew;
    List<ZbnfParseResultItem> listValues = zbnfNewArray.listChildren("value");
    int dimension = listValues.size();
    ZbnfParseResultItem zbnfValue = listValues.get(0);
    ClassData[] retType = new ClassData[1];
    String nrofElements = gen_value(zbnfValue, null, retType, idents, true, 'e');
    final String sInstance;
    assert(variable.modeAccess != '$');  //X and Y are used.
    if(variable.modeAccess == 'Q')
    { //use the embedded given variable
      String sName = variable.getName();
      sInstance = "&" + sName;  
    }
    else if(variable.modeAccess == 'Y')
    { //use the embedded given variable
      String sName = variable.getName();
      sInstance = "&" + sName + ".head.object";  //TODO: inherition, than not only .object
    }
    else
    { //create a new Object
      //String sInstanceRef = null;  //TODO if the instance is a non static inner class, in Java: instance.new(...) 
      sInstance = "(" + gen_newObj() + " = alloc_ObjectJc( sizeof(ObjectArrayJc) + (" + nrofElements + ") * sizeof(" + sTypeNewObject + "), mIsLargeSize_objectIdentSize_ObjectJc, _thCxt))";
    }
    //call the constructor
    String sReflection = (typeOfNew.isPrimitiveType() ? "REFLECTION_" : "&reflection_") + sTypeNewObject;  //reflections of primitive type are defined as simple constants.
    sRet = "(" + sIdentNameNewObject + "_Y*)ctorO_ObjectArrayJc(" + sInstance + ", " + nrofElements + ", sizeof(" + sTypeNewObject + ")," + sReflection + ", 0)";
    return sRet;
  }



  /**generates the expression for a < try_Statment> .
   * 
   * @param zbnfThrowNew
   * @param localIdents
   * @return
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   */
  public String gen_try_statement(ZbnfParseResultItem zbnfThrowNew, int indent, LocalIdents localIdents)
  throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  { String expr = "";
    //ClassData[] typeValue = new ClassData[1];  //classData of the part of expression
    
    ZbnfParseResultItem zbnfStatement = zbnfThrowNew.getChild("statementBlock");
    String statementBlock = 
    	secondpass.gen_statementBlock
      ( zbnfStatement, indent, this
      , CRuntimeJavalikeClassData.clazz_void.classTypeInfo 
      , 'b'
      );
    expr += "TRY" + statementBlock + "_TRY";
    List<ZbnfParseResultItem> listZbnfCatch = zbnfThrowNew.listChildren("catchBlock");
    if(listZbnfCatch!=null) for(ZbnfParseResultItem zbnfCatch : listZbnfCatch)
    { ZbnfParseResultItem zbnfExcType  = zbnfCatch.getChild("ExceptionType");
      ClassData typeClazz = genClass.getType(zbnfExcType, localIdents);
      String sExceptionType = typeClazz.getClassIdentName();
      String sExceptionVariable = zbnfCatch.getChild("exceptionVariable").getParsedString();
      ZbnfParseResultItem zbnfExceptionStatement = zbnfCatch.getChild("statementBlock");
      LocalIdents catchIdents = new LocalIdents(localIdents, null);
      catchIdents.putLocalElement(sExceptionVariable, Java2C_Main.singleton.standardClassData.clazzExceptionJc.classTypeInfo);
      StatementBlock statementBlockCatchFrame = new StatementBlock(genClass, catchIdents, true, indent+1);
      String sExceptionStatement = 
      	secondpass.gen_statementBlock
        ( zbnfExceptionStatement, indent+1, statementBlockCatchFrame
        , CRuntimeJavalikeClassData.clazz_void.classTypeInfo 
        , 'b'
        );
      expr += GenerateClass.genIndent(indent) + "CATCH(" + sExceptionType + ", " + sExceptionVariable + ")"
            + GenerateClass.genIndent(indent) + sExceptionStatement;
    }
    expr += GenerateClass.genIndent(indent) + "END_TRY";
    return expr;
  }





  /**generates the expression for a <throwNew> .
   * 
   * @param zbnfThrowNew
   * @param localIdents
   * @return
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   */
  public String gen_throwNew(ZbnfParseResultItem zbnfThrowNew, ZbnfParseResultItem zbnfDescription
  	, LocalIdents localIdents, FieldData typeReturn)
  throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  { String expr = "{ ";
    ClassData[] typeValue = new ClassData[1];  //classData of the part of expression

    //String sExceptionClass = zbnfThrowNew.getChild("exceptionClass").getParsedString();
    ZbnfParseResultItem zbnfType = zbnfThrowNew.getChild("exceptionClass");
    ClassData exceptionType = genClass.getType(zbnfType, localIdents);
    String sExceptionClass = exceptionType.getClassIdentName(); 
    ZbnfParseResultItem itemText = zbnfThrowNew.getChild("text");
    if(itemText != null)
    { //String sText = itemText.getParsedString();
      String sText = gen_value(itemText, zbnfDescription, typeValue, localIdents, true, 'a');
      if(sText.contains("Not available in expand mode."))
      	stop();
      String sThrow;
      if(typeValue[0] == CRuntimeJavalikeClassData.clazz_s0) { sThrow = "throw_s0Jc(ident_";}
      else if(typeValue[0] == CRuntimeJavalikeClassData.clazzStringJc) { sThrow = "throw_sJc(ident_";}
      else if(typeValue[0] == CRuntimeJavalikeClassData.clazzExceptionJc) { sThrow = "throw_EJc(ident_"; }
      else { 
      	sThrow = "throw_??(ident_";
      	assert(false);
      }
      expr += sThrow + sExceptionClass + "Jc, " + sText + ", ";
    }
    else
    { expr += "THROW_s(" + sExceptionClass + ", xxx, "  ;
    }
    ZbnfParseResultItem itemValue = zbnfThrowNew.getChild("value2");
    String sValue;
    if(itemValue == null)
    {
      sValue = "0";
    }
    else
    {
      sValue = gen_value(itemValue, null, typeValue, localIdents, true, 'a');
    }
    expr += sValue + ", &_thCxt->stacktraceThreadContext, __LINE__);";
    if(typeReturn.typeClazz != CRuntimeJavalikeClassData.clazz_void){
    	if(typeReturn.typeClazz == CRuntimeJavalikeClassData.clazzStringJc){
      	//if(typeReturn.testAndcast(null, 0))
      	  expr += " return null_StringJc; }";
      } else if(typeReturn.typeClazz.bEmbedded){
      	//if(typeReturn.testAndcast(null, 0))
    	  expr += " return null_"+ typeReturn.typeClazz.getClassIdentName() +"; }";
      } else {
        //if(typeReturn.testAndcast(null, 0))
      	expr += " return 0; }";  //should match to all types.
      }
    }else {
    	expr += " }";
    }
    return expr;
  }





  /**generates the initial assignments to variables. Called only inside a 
   * {@link #gen_statementBlock(ZbnfParseResultItem , int, StatementBlock, ClassData, char)}
   *
   * @param zbnfVariableDefinition Item of &lt; variableDefinition>
   * @throws IOException
   * @throws ParseException
   * @throws InstantiationException 
   * @throws IllegalAccessException 
   * @throws IOException 
   * @throws IllegalArgumentException 
   * @throws FileNotFoundException 
   */
  public String gen_VariableInitAssignment(ZbnfParseResultItem zbnfVariableDefinition, int indent) //, LocalIdents idents)
  throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  {
    String ret;
    final LocalIdents idents = this.localIdents;
    ClassData[] typeValue = new ClassData[1];  //classData of the part of expression
    ZbnfParseResultItem zbnfDescription = zbnfVariableDefinition.getChild("description"); //may be null.
    { //ZbnfParseResultItem itemAttrib = iterAttrib.next();
      CCodeData leftVariable = gen_variableAccess(zbnfVariableDefinition, zbnfDescription, localIdents, 'i', genClass.classData.thisCodeInfo);
      
      String sName = zbnfVariableDefinition.getChild("variableName").getParsedString();
      FieldData infoVariable = localIdents.get(sName);
      if(sName.equals("ifc3"))
        stop();
      assert(infoVariable != null);
      ZbnfParseResultItem zbnfAssignment = zbnfVariableDefinition.getChild("value");
      if(zbnfAssignment != null)
      { 
        ret = gen_assignValue(leftVariable, "=", typeValue, zbnfAssignment, zbnfDescription, indent, localIdents, 'i') 
              + ";";
        /*
        
        final String value = gen_value(zbnfAssignment, typeValue, idents, 'e');
        final FieldData typeVariable = idents.get(sName);
        final String dstValue = typeVariable.testAndcast(typeValue[0], value);
        //TODO: assignment to StringJc!
        ret = GenerateClass.genIndent(indent) + sName + " = " + dstValue + ";";
        */
      }
      else if( (zbnfAssignment = zbnfVariableDefinition.getChild("newObject")) != null)
      { //an assignment with new...
        if(infoVariable.modeAccess == '%'){
        	//a type, which are only used in form of an value, forex MemSegmJc. 
        	//It is initialized in Java calling a new(param). But the reference is a instance in C.
        	//In Java the reference should be final because an assignment isn't admissible.
          String sCtor = genInitEmbeddedInstance(zbnfAssignment, zbnfDescription, infoVariable, sName, indent);
          ret = GenerateClass.genIndent(indent) + sCtor + ";";
        	//ret = GenerateClass.genIndent(indent) + "INIT_null_" + infoVariable.typeClazz.getClassIdentName() + "(" + sName + ");  //TODO parameter"; 
        }
        else if("$".indexOf(infoVariable.modeAccess) >=0)
        { //call the constructor
          String sCtor = genInitEmbeddedInstance(zbnfAssignment, zbnfDescription, infoVariable, sName, indent);
          ret = GenerateClass.genIndent(indent) + sCtor + ";";
        }
        else
        { CCodeData codeNewObject = gen_newObject(zbnfAssignment, null); //, localIdents); //, idents);
          ret = GenerateClass.genIndent(indent) + sName + " = " + codeNewObject.cCode + ";";
        }  
      }
      else if( (zbnfAssignment = zbnfVariableDefinition.getChild("newArray")) != null)
      { //an assignment with new...
      	if(infoVariable.getName()!=null && infoVariable.getName().equals("idxP"))
      		stop();
      	String sNewArray = gen_newArray(zbnfAssignment, typeValue, idents, infoVariable);
        if(infoVariable.modeAccess == 'Y')
        { ret = GenerateClass.genIndent(indent) + sNewArray + ";"; //no assignment, it is embedded.
        }
        else if(infoVariable.modeAccess == 'X') //X: array reference.
        { ret = GenerateClass.genIndent(indent) + sName + " = " + sNewArray + ";";
        }
        else if(infoVariable.modeAccess == 'Q'){ //embedded simple array. 
        	//It is initialized on definition already.
        	ret = "";  //no initializatin
        } else {
        	ret = "";
        	assert(false);
        }
      }
      else
      { //no asignment TODO use 3. param from gen_variableDefinition: variablesToInit, but not at classlevel
        //ret = GenerateClass.genIndent(indent) + sName + " = 0; /*assignment not found*/";
        ret = "/*no initvalue*/";
      }
    }
    return ret;
  }



  /**Generates the definition of variable with its initialization.
   * It is able to use first for static variable, it is not able to use for complexly initilizations,
   * which depends from pre-calculated values.
      **TODO: To sophisticate something obout the sources of the value, it should be returned
       * whether the value is only build with constants, or with some variables.
       * It should be returned from gen_value.
       * Than here only a constant value may be considered. It is necessary for C static variable,
       * but also for immediately initializiation of variable.
       * If any dynamically values are used, the initialization don't may execute here.
       * This routine should return null.
       *
   * @param variable
   * @param zbnfInitAssignment
   * @return
   * @throws ParseException
   * @throws FileNotFoundException
   * @throws IllegalArgumentException
   * @throws IOException
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  String gen_VariableDefWithSimpleInitValue(FieldData variable, ZbnfParseResultItem zbnfInitAssignment) 
  throws ParseException, FileNotFoundException, IllegalArgumentException, IOException, IllegalAccessException, InstantiationException
  {
    String ret;
    String leftVariableDef = variable.gen_VariableDefinition('b');
    
    final String semanticInitAssignment = zbnfInitAssignment.getSemantic();
    if(semanticInitAssignment.equals("constArray"))
    { String separator = "{ ";
      List<ZbnfParseResultItem> listValues = zbnfInitAssignment.listChildren();  //all are kind of simpleValue
      FieldData identInfoElement = null;
      ret = leftVariableDef + " = ";
      for(ZbnfParseResultItem zbnfValue: listValues)
      { CCodeData codeValue = genClass.genConstantValue(zbnfValue);
        ret += separator + codeValue.cCode;
        separator = ", ";
        identInfoElement = codeValue.identInfo;  //the last wins. But there should be all the same.
      }
      ret += "};";
      //return ret;
      //return new CCodeData(ret, identInfoElement, '%', 1);
    }
    else if(semanticInitAssignment.equals("newObject")){
      ret = leftVariableDef + ";";
      //return ret;
    }
    else if(semanticInitAssignment.equals("newArray")){
      ret = leftVariableDef + ";";
      //return ret;
    }
    else if(semanticInitAssignment.equals("value")){
      /**There are able to use only special constellations of value, especially constant values.
       */
      CCodeData value = gen_value(zbnfInitAssignment, null, variable.modeStatic=='r', 's');
      /**TODO: To sophisticate something obout the sources of the value, it should be returned
       * whether the value is only build with constants, or with some variables.
       * It should be returned from gen_value.
       * Than here only a constant value may be considered. It is necessary for C static variable,
       * but also for immediately initializiation of variable.
       * If any dynamically values are used, the initialization don't may execute here.
       * This routine should return null.
       */
      if(  variable.typeClazz == CRuntimeJavalikeClassData.clazzStringJc
        && value.identInfo.typeClazz == CRuntimeJavalikeClassData.clazz_s0
        ){
        ret = leftVariableDef + " = CONST_z_StringJc(" + value.cCode + ");";
      }
      else{
        /**The value is gotten, it may be constant. But a simple possible cast may be necessary: */
        final String dstValue = variable.testAndcast(value, '.');
        ret = leftVariableDef + " = " + dstValue + ";"; 
      }
      //return ret;
    }
    else
    { assert(false);
      ClassData[] typeValue1 = new ClassData[1];  //classData of the part of expression
      CCodeData leftVariable = new CCodeData(leftVariableDef, variable);
      ret = gen_assignValue(leftVariable, "=", typeValue1
                   , zbnfInitAssignment, null, 0, genClass.classData.classLevelIdents, 'i') + ";";
    }
    return ret;
  }

  void stop(){}
  
}//class StatementBlock



