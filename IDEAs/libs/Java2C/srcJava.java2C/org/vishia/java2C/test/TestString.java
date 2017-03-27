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

//import java.io.File;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**This class contains some examples to test String functionality.
 * <br><br>
 * <b>Situation for String preparation in C:</b>
 * A String preparation in C in embedded applications is often necessary in situations of error reporting
 * or output of states. The typically choice is usage of <code>sprintf(buffer, "text-format", arguments, ...)</code>.
 * This approach have some risks which may produce errors in runtime:
 * <ul>
 * <li>The type of arguments should be matched to the designation of parts in text-format. 
 *   An error of detail is possible, the user should programmed carefully. The compiler doesn't detect errors.
 * <li>If the size of <code>buffer</code> is less, an overflow isn't detect. An overflow may occur, if a value is unexpected, 
 *   and the numbering formating produce a less longer output. <code>char*</code>-Arguments are critically too,
 *   because there length may be rate faulty because that is depending of outer code.
 * <li>If a <code>char*</code> is used as argument, and the pointer is faulty, an unexpected buffer overflow 
 *   may be produced in the <code>sprintf</code>.
 * <li>The buffer should be provided by the users environment. If the buffer is defined in Stack, 
 *   and the String is produced and used temporary, it's okay. But a software correction in calling routines,
 *   which doesn't uses to pointer to the buffer for copy, instead save the pointer, are not detected at compile time.
 * </ul>
 * The usage of <code>sprintf(...)</code> in C should implement carefully, it is sensitive. 
 * <br><br>
 * The usage of Strings in Java is some more unsensitive and simple. But the standard-Java uses the system of garbage collection
 * to accomplish this requirements. This may not able to use in all situations of embedded control-software.
 * The preparation of Strings may realized in Java in three ways:
 * <ul>
 * <li>Simple concatenations of Strings just like <code>String result = "value=" + value</code>. This variant needs a buffer,
 *   which is allocated in the heap and managed by garbage collection.
 * <li>Usage of a StringBuffer or StringBuilder. 
 *   The adequate code is <code>result.setLength(0); result.append("value=").append(value);</code>
 *   The StringBuffer-Object can be allocated as an element of any class, the memory space is allocated
 *   in the instance including the buffer itself, hence no dynamic memory allocation is necessary.
 * <li>Java knows a adequate to <code>sprintf</code> approach using <code>String.format(formatString, arguments ...)</code>. 
 *   This method is some more safety as the C-<code>sprintf</code>, because a buffer overflow is detected and the types
 *   of arguments are well known. But the method works with dynamic memory only.
 * </ul>
 * If embedded software written in Java and translated to C shouldn't used dynamic memory and garbage collection,
 * the second approach using <code>StringBuffer</code> is proper. The <code>StringBuffer</code> can be defined
 * as embedded Buffer with a fix length inside a class type. Examples using such a fix <code>StringBuffer</code>
 * allocated in the Stack too are test and shown in the method {@link #testStringBuffer()}.
 * <br><br>
 * If the garbage collector is able to use, a String concatenation is able to use. The user doesn't need to
 * provide any buffers.      
 *
 */
public class TestString
{
  
  
  /**A reference to another struct. */
  StringBuilder buffer1;
  
  /**A reference to another struct, but without need of garbage collection. @java2c=noGC. */
  final StringBuffer bufferInit;
  
  
  final StringBuilder bufferEmbedded = new StringBuilder(1000);

  final static String empty = "                                     ";
  
  /**A reference to a String in memory. The String may be a reference to a constant String 
   * or to the content inside a StringBuffer. see usages. */
  String stringRef;
  
  /**Creates an embedded instance as StringBuffer with immediate space for the chars. */
  private final StringBuffer sbufferFix = new StringBuffer(250);
  
  private final String[] strArray = new String[10];
  
  private String[] strArray2;
  
  private char[] charArray = new char[10];
  
  TestString(StringBuffer bufferInit)
  {
    buffer1 = new StringBuilder(122);
    this.bufferInit = bufferInit;    
  }
  
  /**Help method which processes a String. */
  static int processString(String str)
  { return str.length();
  }
  
  
  
  /**Examples for String concatenations.
   * <br><br>
   * This method tests and shows a concatenation of String without using dynamic memory.
   * it uses Stack instances only, because all Strings are used temporary.
   * This patterns are able to use in a system without any dynamically memory,
   * at example for String preparing in an interrupt routine of embedded control. 
   * <br><br>
   * See comments on the code line of java source-code in comparision with the produced C-code 
   * in <a href="../xxx">xxx</a>
   */
  void testStringConcatenationInStack(int value, float fValue)
  { /**Because it is set 'java2c=StringBuilderInStack=100'. The concatenation will be done
     * in an buffer created in Stack instead in an new allocated buffer in heap.
     * The operation toString builds a reference to this stack instance anyway.
     * The stack variable ssn refers the string in stack.
     * Because it is set 'java2c=toStringNonPersist'. The StringBuilderJc in stack
     * will not designated as 'freezed' for further usage. That are another aspect.
     * The result is a temporary non persistent String in stack.
     * @java2c=toStringNonPersist, StringBuilderInStack=100.
     */
  	String ssn = "test-Concatenation " + value + ", float " + fValue;
  	/**This String can be used as input for any method, which processed the String
  	 * but not references the original source-string. Most of routines do so.
  	 */
  	processString(ssn);
    /**
     * The next statement reuses the buffer and changes the String. The reuse is done
     * only because the same buffer in stack is dedicated with 'java2c=StringBuilderInStack',
     * not derived from the previous used buffer. 
     * It is another way of controlling as on operation '+='.
     * 
     * @java2c=toStringNonPersist, StringBuilderInStack=100. */
  	ssn = "second " + value;
  	processString(ssn);
      /**
     * The next statement appends something on the String. It is a typically operation
     * while preparing Strings in more as one line. The buffer can be enlarged, 
     * because the last operation for assign ssn was designated with 'java2c=toStringNonPersist'.
     * Therefore the Buffer is not dedicated as 'freezed'. Which buffer is used? 
     * It is the buffer, which is used for the previous preparation of the String.  
     * The designation with 'java2c=toStringNonPersist' would have an effect 
     * for further '+='-operations with this variable. In this example it has an effect
     * only for further usage of the same buffer. It is possible to use the buffer.
     * The enlarging-operation of the buffer (call of append) doesn't depend on that designation.
     * @java2c=toStringNonPersist.
     */
  	ssn += ", third";
    /**
     * This String should be built in an own buffer, because ssn is used for concatenation too. 
     * but it may be @java2c=toStringNonPersist.*/
  	//ssn = "forth, " + ssn;
    /**Use it: */
  	processString(ssn);
  	
  	int pos = ssn.indexOf('.');
  	if(pos < 0 ){
  		pos = 0;
  	}
  	{ int pos2 = ssn.indexOf("third");
  	  if(pos2 >=0){
  	  	pos += pos2;
  	  }
  	}
  }

  
  
  
  
  /**Examples for String concatenations.
   * <br><br>
   * This method tests and shows a concatenation of String using garbage collection. 
   * <br><br>
   * <b>First example: concatenation and assignment</b>: The Java-code is:
   * <pre class="Java">
    String ss = "test " + value + " miles";
    System.out.println(ss);
    this.stringRef = ss;
   * </pre>
   * A String will be built stack-locally with concatenation. Than this String will be used 
   * first as parameter for a method, than it is saved in the instance (persistent). 
   * In C it is translated to:
   * <pre>
    StringJc ss;
      ...
    StringBufferJc *_tempString1_1=null; //J2C: temporary Stringbuffer for String concatenation
      ...
    ss = 
      ( _tempString1_1 = new_StringBufferJc(-1, _thCxt)
      , setTemporary_StringBufferJc(_tempString1_1)
      , append_z_StringBufferJc(_tempString1_1, "test ", _thCxt)
      , append_I_StringBufferJc(_tempString1_1, value, _thCxt)
      , append_z_StringBufferJc(_tempString1_1, " miles", _thCxt)
      , toString_StringBufferJc(_tempString1_1)
      );
    println_s_PrintStreamJc(REFJc(out_SystemJc), ss, _thCxt);
    set_StringJc(&(ythis->stringRef), ss);
      ....
    activateGarbageCollectorAccess_BlockHeapJc(&_tempString1_1->base.object);
   * </pre>
   * The following principles are used:
   * <ul>
   * <li>Adequate to the implementation in Java the concatenation uses a StringBuffer internally 
   *   for the concatenation expression. The StringBuffer is allocated in heap 
   *   calling <code>new_StringBufferJc(...)</code>. 
   * <li>There is a possibility of working without allocated memory
   *   for such embedded software systems, which should not use any dynamic memory and garbage collection:
   *   The new StringBuffer can be a thread-local existing buffer. In this case only one concatenation of Strings
   *   is able to use per Thread. The content of the produced String should be copied in the next instruction.
   *   That is a restriction of programming, but it allows to work without garbage collection. Commonly
   *   a garbage collector and dynamic memory should be able to use. The CRuntimeJavalike-Library provides a solution
   *   using blocks with equal size (<code>BlockHeap</code>).
   * <li>The StringBuffer is dedicated as temporary. The reference to the StringBuffer isn't known
   *   in the users programming. Therefore the user can't work with it. Only the result String <code>ss</code>
   *   uses the buffer.
   * <li>All concatenations are implemented using <code>append...(...)</code>-methods.
   * <li>The last operation with the StringBuffer is the call of <code>toString(...)</code> to get the String.
   *   A String instance contains only the pointer to the text itself (<code>char* in Buffer</code>), 
   *   the number of chars and a few special bits. That are at maximum 2 Registers with 32 bit, 
   *   compendious in a <code>struct StringJc</code> and defined operation-system-depend 
   *   in a <code>struct OS_ValuePtr</code> to support optimal memory and register usage for the target platform.
   * <li>The String is stored and taken as parameter as value, never as a reference (not as <code>StringJc*</code>).
   *   The method <code>println(...)</code> got the String as value.
   * <li>To store a String in class variables (persistent), the method <code>setref_StringJc(...)</code> is used.
   *   This method sets the backward reference to the memory block of the block-heap, which contains
   *   the pointer to the text and sets the index of backward reference in the reference in class (<code>ythis->stringRef</code>)
   *   With this operation the block is referenced now.
   * <li>The temporary StringBufferJc is designated to managed by Garbage collection at least, before finish the routine.
   *   Because the String is referenced in this example, the garbage collector will test the reference.
   *   Elsewhere the block will be freed from garbage collector. That is typically if a String is prepared
   *   and used only locally.
   * </ul>    
   */
  void testStringConcatenationWithTemps(int value, float fValue)
  { String ssn = "test " + value + " miles";
	  processString(ssn);
    //System.out.println(ssn);
    //this.stringRef = ssn;
    ssn = "second " + value;
    ssn += ", third";
    ssn = "forth, " + ssn;
    //System.out.println(ssn);
  	processString(ssn);
    /**A buffer in stack: @java2c=stackInstance. */
    //StringBuffer buffer = new StringBuffer(40);
    //buffer.append("buffer:");
    //ssn = buffer + ssn;
    /**Test substring, indexof. */
    //int pos = ssn.substring(4).indexOf('i');

  }
 
  void testStringConcatenationUsingBuilder(int value)
  { /**A buffer in stack: @java2c=stackInstance. */
    StringBuffer buffer = new StringBuffer(40);
    buffer.append("buffer:");
    buffer.append(123); 
    String ssn = buffer.toString();
    /**Test substring, indexof. */
    int pos = ssn.substring(4).indexOf('i');

  }
 
  
  
  String testStringParameter(String s1, String s2)
  { /** @java2c=StringBuilderInThreadCxt.*/
    String ret = s1 + " to " + s2;
    return ret;
  	
  }
  
  
  void testStringParameter()
  { 
  	/**@java2c=stackInstance. */
  	Date dateFrom = new Date();
  	/**@java2c=stackInstance. */
  	Date dateTo = new Date(dateFrom.getTime() + 3600000);
  	/**@java2c=stackInstance. */
  	SimpleDateFormat dateFormat = new SimpleDateFormat("yy_MMM_dd_HHmmss_SS");
  	String sResult= testStringParameter(dateFormat.format(dateFrom), dateFormat.format(dateTo));
    System.out.println(sResult);
  }
  
  
  
  /**
   * @param value
   * @param fValue
   * @return
   * @java2c=return-new.
   */
  private String testFormat(int value, float fValue)
  {
  	/**@java2c=toStringNonPersist. */
  	String result = String.format("format intVal=%d floatVal=%3.4f test", value, fValue);
  	processString(result);
  	return result; 
  }
  
  
  private void testReplace()
  {
  	String src = "C:\\directory/myFolder/myFile.x";
  	String dst = src.replace('\\', '/');
  	buffer1.setLength(0);
  	buffer1.append(src.replace('\\', '/'));
  	buffer1.setLength(0); buffer1.append(src);
  	/**@java2c=toStringNonPersist. The replacement should done in the buffer itself. */
  	src = buffer1.toString();
  	/**This expression copies the replaced text in the buffer1.
  	 * It is the same, if the original text is copied and than replaced in the buffer.
  	 * But Java doesn't know a simple char replacement in a StringBuilder.
  	 * The Java2C translates to ...TODO
  	 */
  	buffer1.replace(0, buffer1.length(), src.replace('\\', '/'));
  
  }

  
  private void testInsertCharArray()
  { buffer1.setLength(0);
    buffer1.append("1234");
    charArray[0] = 'A';
    charArray[1] = 'Q';
    charArray[2] = 'X' ;
    buffer1.insert(2, charArray, 1,2);
    /**@java2c=toStringNonPersist. */
    assert(buffer1.toString().equals("12QX34"));
  }
  
  
  
  public String toString()
  { return stringRef;
  }
  

  void testGarbageString()
  {
    stringRef = "other String";
    
  }
  
  /**Example for StringBuffer usage.
   * <br><br>
   * The StringBuffer {@link #bufferEmbedded} is created as an embedded part of this class structure.
   * It has a fix length, the text is part of the instance, using immediate buffer.
   * The usage of such an buffer for string preparing is a proper decision if no dynamic memory
   * should be used, and the user has the controlling and responsibility about the String instances.
   * <br>
   * The Java-code to prepare a string is:
   * <pre class="Java">
    bufferEmbedded.setLength(0);
    bufferEmbedded.append("content:").append(stringRef);
    System.out.println(bufferEmbedded);
    String s1 = bufferEmbedded.toString();
    System.out.println(s1);
    this.stringRef = s1;
   * </pre>
   * In this example the buffer is cleared, than a content is appended. The Result is used to print out
   * with given StringBuffer. Than the buffer-content is stored in a String calling the <code>toString(...)</code>-method.
   * That result is used to print out too. At last the String is stored in a class variable.
   * <br><br>
   * The resulting C-code is:
   * <pre>
    StringJc s1;
    ...
    setLength_StringBufferJc(& (ythis->bufferEmbedded.sb), 0, _thCxt);
      ( append_s_StringBufferJc(& (ythis->bufferEmbedded.sb), s0_StringJc("content:"), _thCxt)
      , append_s_StringBufferJc(& (ythis->bufferEmbedded.sb), ythis->stringRef, _thCxt)
      );
    println_sb_PrintStreamJc(REFJc(out_SystemJc), & (ythis->bufferEmbedded.sb), _thCxt);
    s1 = toString_StringBufferJc(& (ythis->bufferEmbedded.sb));
    println_s_PrintStreamJc(REFJc(out_SystemJc), s1, _thCxt);
    set_StringJc(&(ythis->stringRef), s1);
   * </pre>
   * The C-code follows the Java-code. The concatenation of append is resolved like described in 
   * {@link TestAllConcepts#checkConcatenationSimple()}.
   * <br><br>
   * 
   */
  void testStringBuffer()
  { bufferEmbedded.setLength(0);
    bufferEmbedded.append("content:").append(stringRef);
    System.out.println(bufferEmbedded);
    /**@java2c=toStringNonPersist. */
    String s1 = bufferEmbedded.toString();
    System.out.println(s1);
    this.stringRef = s1;
     /**The StringBuffer is allocated in BlockHeapJc, containing the max possible buffer size, 
     * this size is limited in C implementation. */
    StringBuffer sb = new StringBuffer();
    sb.setLength(0);
    sb.append("test ").append(5).append(" Stringbuffer");
    System.out.println(sb);
    /**@java2c=toStringNonPersist. */
    /**The toString() is only used in the thread immediately. 
     * Therefore: @java2c=toStringNonPersist. */
    String ss = sb.toString();
    System.out.println(ss);
  }

  
  
  
  
  
  
  
  
  
  /**
   * <b>Persistence of Strings</b> 
   * The method <code>toString(...)</code> needs to turn one's attentions: Generally, a String is constant.
   * Because the buffer content of a StringBuffer may be changed after calling <code>toString(...)</code>, 
   * the text have to be copied to an independent buffer. The Java implementation do so. 
   * But this is an additional effort regarding typically applications in the fast real-time embedded world. 
   * Mostly the StringBuffer isn't change immediately, rather the got String is processed and thereby copied 
   * in the current thread like shown in the example {@link #testStringBuffer()}.
   * <br><br>
   * Thats why some methods <code>toString...(...)</code> in the CRuntimeJavalike-Library have two modes:
   * <ul>
   * <li>Compatible-mode with Java, but with more effort: <code>toString...(...)</code> creates an extra
   *   buffer for the returned String in heap.
   * <li>Small-effort-mode: <code>toString_StringBufferJc</code> references the String in the 
   *   StringBuffer-instance, no extra buffer is used. <code>toString...(...)</code> from other classes
   *   uses the thread-local Buffer, it can be used only one for one toString-result in thread, the result is to copy outside.
   *   This mode saves calculation time and prevent an extra memory allocation. 
   *   In this case the String is dedicated with a bit <code>mNonPersists__StringJc</code>.
   * </ul>    
   * Both modes are able to choice. The default is the compatible mode with Java. A method which uses 
   * the small-effort mode can be designated with a <code>@ java2c=optimize-toString</code> annotation 
   * in its description block. Than the small-effort-mode is valid for that method body. 
   * <br><br>
   * The prevention of copy a String in a allocated buffer is not only a question of calculation time effort.
   * If a routine in a fast interrupt uses a lightweight String preparation but an allocated buffer is necessary,
   * that module needs resources, which are inadequately for it. Therefore the optimize mode is recommended.
   * The Java-source should written in a form, which doesn't change a StringBuffer from which a derived 
   * temporary String is still in use. First do all with the String, than change the Buffer. 
   * Than the optimized C-code works correctly.
   * <br><br>
   * If the String is saved persistent in a class variable, it must copy to an own buffer. In this case
   * an alteration of the StringBuffer content is able to expect while the persistent String is ready to use.
   * It would be a non-obvious programming situation if the persistent String is referenced the buffer evermore.
   * Thats why the assignment using <code>set_StringJc(...)</code> checks whether the String is non persistent.
   * If it is so, a new StringBuffer is allocated in heap and the String is copied into. That StringBuffer
   * is only referenced by this String, and therefore it is fix and managed by garbage collection.
   * <br><br>
   * An adequate problem are given by the <code>toString(...)</code> implementations of some other Objects.
   * Because mostly the Strings will be processed immediately, at example as input for an concatenation,
   * extra buffers should be saved to minimize effort. For preparing the returned String a thread-local StringBuffer
   * is able to use. That is thread-save, but only one instance of that buffer exists per thread. 
   * This buffer have never to be referenced. Thats why the same dedication is used: <code>mNonPersists__StringJc</code>.
   * The user can produce a faulty code, a code which runs in Java because Java is save but it doesn't run in C after translation.
   * This is the disadvantage of this concept, the concession to the optimizing of calculation time and preventing
   * additional dynamically memory management. The user may write minimal other statements to get a safety code.
   * <br><br>
   * This routine is designated with the <code>@ java2c=optimize-toString.</code>, but the code is faulty for that:
   * <pre class="Java">
    bufferEmbedded.setLength(0);
    String ss = bufferEmbedded.append("content:").append(234).toString();
    bufferEmbedded.setLength(0);
    bufferEmbedded.append(" TEST ").append(ss);
    ss = bufferEmbedded.toString();
    System.out.println(ss);
   * </pre>
   * The buffer content is saved in <code>ss</code>, but after them the buffer is changed. The translated C-code is:
   * <pre>
  bool _bOptimizeStringSave;  //J2C: annotation optimize-toString
    ...
  _bOptimizeStringSave = optimizeString_ThCxt(_thCxt, true);
  { 
    StringJc ss;
    setLength_StringBufferJc(& (ythis->bufferEmbedded.sb), 0, _thCxt);
    ss = 
      ( append_s_StringBufferJc(& (ythis->bufferEmbedded.sb), s0_StringJc("content:"), _thCxt)
      , append_i_StringBufferJc(& (ythis->bufferEmbedded.sb), 234, _thCxt)
      , toString_StringBufferJc(& (ythis->bufferEmbedded.sb), _thCxt)
      );
    setLength_StringBufferJc(& (ythis->bufferEmbedded.sb), 0, _thCxt);
    
      ( append_s_StringBufferJc(& (ythis->bufferEmbedded.sb), s0_StringJc(" TEST "), _thCxt)
      , append_s_StringBufferJc(& (ythis->bufferEmbedded.sb), ss, _thCxt)
      );
    ss = toString_StringBufferJc(& (ythis->bufferEmbedded.sb), _thCxt);
    println_s_PrintStreamJc(REFJc(out_SystemJc), ss, _thCxt);
  }
  optimizeString_ThCxt(_thCxt, _bOptimizeStringSave);
   * </pre>
   * 
   * The optimizing is set in the Thread-context-data. After the routine the saved state of optimizing
   * is recovered.
   * 
   */
  void testNonPersistenceOfStrings()
  {
    bufferEmbedded.setLength(0);
    /**@java2c=toStringNonPersist. */
    String ss = bufferEmbedded.append("content:").append(234).toString();
    bufferEmbedded.setLength(0);
    bufferEmbedded.append(" TEST ").append(ss);
    /**@java2c=toStringNonPersist. */
    ss = bufferEmbedded.toString();
    System.out.println(ss);
  }
  
  
  
  /**This routine has the same content like {@link #testNonPersistenceOfStrings()}, 
   * but the persistence is set per default,
   * no <code>@ java2c=optimize-toString</code>. is written here.
   * The result is correct. Debug it in C!
   */
  void testPersistenceOfStrings()
  {
    bufferEmbedded.setLength(0);
    String ss = bufferEmbedded.append("content:").append(234).toString();
    bufferEmbedded.setLength(0);
    bufferEmbedded.append(" TEST ").append(ss);
    ss = bufferEmbedded.toString();
    System.out.println(ss);
  }
  
  
  /**This routine uses the class java.lang.Date und SimpleFormatter
   * to produce a String with a date and time information. 
   * The translated code uses the CRuntimeJavalike-classes Date etc.
   * Some annotations cause, that only stack-instances and the buffer in the thread context
   * is used, no dynamically memory.
   */
  String testDateString(String sPath, int ident)
  { /**@java2c=stackInstance. */
  	Date date = new Date();
  	/**@java2c=stackInstance. */
  	SimpleDateFormat dateFormat = new SimpleDateFormat("yy_MMM_dd_HHmmss_SS");
  	/**@java2c=toStringNonPersist.*/
  	String sDateString = dateFormat.format(date);
  	/**@java2c=StringBuilderInStack:150. */
  	String sFileName = sPath + "file_" + ident + "_" + sDateString + ".txt";
  	/**@java2c=returnInThreadCxt. */
  	return sFileName;
  }
  
  /**This routine uses the class java.lang.Date und SimpleFormatter
   * to produce a String with a date and time information. 
   * The translated code uses the CRuntimeJavalike-classes Date etc.
   * It is the same routine in Java like {@link #testDateString(String, int)},
   * but there are no annotations. Therefore dynamically memory is used.
   */
  String testDateStringDynamic(String sPath, int ident)
  { Date date = new Date();
  	SimpleDateFormat dateFormat = new SimpleDateFormat("yy_MMM_dd_HHmmss_SS");
  	String sDateString = dateFormat.format(date);
  	String sFileName = sPath + "file_" + ident + "_" + sDateString + ".txt";
  	return sFileName;
  }
  
  /**@java2c=embedded Type:FileOutputStream. */
  FileOutputStream oStream1, oStream2; 
	
  
  void testOutStream(String sPath)
  { try{
  		/**@java2c=StringBuilderInStack:100, toStringNonPersist. */
      oStream1 = new FileOutputStream(sPath+ "file1.txt");
      /**@java2c=StringBuilderInStack=256,toStringNonPersist. */
      oStream2 = new FileOutputStream(sPath+ "file2.txt");
      oStream1.close();
      oStream2.close();
    } catch(FileNotFoundException exc){
    	System.out.println("not found: " + sPath);
    } catch (IOException e) {
     	System.out.println("file error: " + sPath);
    }
  }	
  
  private int testSomeSimpleStringMethods()
  { int ret;
    int pos;
    boolean bOk;
    final String s1 = "abcdecde";
    final String s2 = "abc";
    char cc = s1.charAt(3);
    bOk = (cc == 'd');
    bOk = bOk && ! s1.equals(s2);
    bOk = bOk && s1.startsWith(s2);
    
    /**This case works in Java, but it is unnecessary. It doesn't work in Java2C. */
    //bOk = bOk && s1.equals((Object)s1);  //error while compiling, fault type conversion.
    //Object oString = s1;  //error while translating Java2C, a StringJc is not an ObjectJc
    
    ret = pos = s1.indexOf('c');
    ret += pos = s1.indexOf('c', 2);  //pos = 2, same as above, but seach from position 2 only  
    ret += pos = s1.indexOf('c', 3);  //pos = -1 because not found. 
  
    ret = pos = s1.indexOf("de");
    ret += pos = s1.indexOf("de", 2);  //pos = 3, same as above, but seach from position 2 only  
    ret += pos = s1.indexOf( "de", 4);  //pos = -1 because not found. 
  
    ret += pos = s1.indexOf("de", 99);  //pos = -1 because not found. Not an exception 
  
    ret += pos = s1.indexOf("de", -1);  //pos = 3 because search from 0. Not an exception.
  
    ret = pos = s1.lastIndexOf('c');
    ret += pos = s1.lastIndexOf('c', 2);  //pos = 2, same as above, but seach from position 2 only  
    ret += pos = s1.lastIndexOf('c', 3);  //pos = -1 because not found. 
  
    ret = pos = s1.lastIndexOf("de");
    ret += pos = s1.lastIndexOf("de", 2);  //pos = -1, because not found.  
    ret += pos = s1.lastIndexOf( "de", 3);  //pos = 3 because the string starts at 3.  
  
    ret += pos = s1.lastIndexOf("de", 99);  //pos = -1 because not found. Not an exception 
  
    ret += pos = s1.lastIndexOf("de", -1);  //pos = 3 because search from 0. Not an exception.
  
  
    return ret;
  }
  
  
  
  /**A CharSequence is the super-class of java.lang.String and java.lang.StringBuilder.
   * In C it is the same as a StringJc, because the StringJc has the necessary methods
   * and refers a sequence of chars.
   * <br><br>
   * The expression <code>CharSequence csq1 = buffer1</code> is a simple downcast 
   * from StringBuilder in Java, but in C it is a toString()-call. The text itself isn't copy
   * thereby, it is referenced. This is the equal functionality. Note that the StringJc is
   * a simple value-struct with reference and length.
   * 
   */
  private void testCharSequence()
  {
  	buffer1.setLength(0); buffer1.append("csq1");
  	CharSequence csq1 = buffer1;  //StringBuffer to charSequence
  	bufferEmbedded.setLength(0);
  	bufferEmbedded.append("be_").append(csq1);
  	/**@java2c=toStringNonPersist. */
  	assert(bufferEmbedded.toString().equals("be_csq1"));
  }
  
  
  
  /**Calls the test routines.
   * 
   */
  public String testStringProcessing()
  { long timeStart, timeEnd;
    int timeRun;
    float adjustTime;
    String str;
    timeStart = System.nanoTime();
  	try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
    timeEnd = System.nanoTime();
    timeRun = (int)(timeEnd - timeStart);
    /**Runtime output @java2c=StringBuilderInStack=100, toStringNonPersist. */
    System.out.println("10 ms: " + timeRun + "ns");
    
    timeStart = System.nanoTime();
  	try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
    timeEnd = System.nanoTime();
    timeRun = (int)(timeEnd - timeStart);
    adjustTime = 100000.0F / timeRun;
    /**Runtime output @java2c=StringBuilderInStack=100, toStringNonPersist. */
    System.out.println("100 ms: " + (timeRun * adjustTime) + "us");
    
    timeStart = System.nanoTime();
  	timeEnd = System.nanoTime();
    timeRun = (int)(timeEnd - timeStart);
    /**Runtime output @java2c=StringBuilderInStack=100, toStringNonPersist. */
    System.out.println("emtpy measurement: " +  (timeRun * adjustTime) + "us");
    
    
    timeStart = System.nanoTime();
    TestString_classic.concatenate(456, 3.14F);
    TestString_classic.concatenate(457, 3.14F);
    TestString_classic.concatenate(458, 3.14F);
    TestString_classic.concatenate(459, 3.14F);
    TestString_classic.concatenate(42, 3.14F);
    TestString_classic.concatenate(43, 3.14F);
    TestString_classic.concatenate(44, 3.14F);
    TestString_classic.concatenate(45, 3.14F);
    TestString_classic.concatenate(46, 3.14F);
    TestString_classic.concatenate(47, 3.14F);
    timeEnd = System.nanoTime();
    timeRun = (int)(timeEnd - timeStart);
    /**Runtime output @java2c=StringBuilderInStack=100, toStringNonPersist. */
    System.out.println("TestString_classic.concatenate: " + (timeRun * adjustTime) + "us");
    
    timeStart = System.nanoTime();
    testStringConcatenationInStack(456, 3.14F);
    testStringConcatenationInStack(456, 3.14F);
    testStringConcatenationInStack(456, 3.14F);
    testStringConcatenationInStack(456, 3.14F);
    testStringConcatenationInStack(456, 3.14F);
    testStringConcatenationInStack(456, 3.14F);
    testStringConcatenationInStack(456, 3.14F);
    testStringConcatenationInStack(456, 3.14F);
    testStringConcatenationInStack(456, 3.14F);
    testStringConcatenationInStack(456, 3.14F);
    timeEnd = System.nanoTime();
    timeRun = (int)(timeEnd - timeStart);
    /**Runtime output @java2c=StringBuilderInStack=100, toStringNonPersist. */
    System.out.println("testStringConcatenationInStack: " + (timeRun * adjustTime) + "us");
    
    timeStart = System.nanoTime();
    testStringConcatenationWithTemps(456, 3.14F);
    testStringConcatenationWithTemps(456, 3.14F);
    testStringConcatenationWithTemps(456, 3.14F);
    testStringConcatenationWithTemps(456, 3.14F);
    testStringConcatenationWithTemps(456, 3.14F);
    testStringConcatenationWithTemps(456, 3.14F);
    testStringConcatenationWithTemps(456, 3.14F);
    testStringConcatenationWithTemps(456, 3.14F);
    testStringConcatenationWithTemps(456, 3.14F);
    testStringConcatenationWithTemps(456, 3.14F);
    timeEnd = System.nanoTime();
    timeRun = (int)(timeEnd - timeStart);
    /**Runtime output @java2c=StringBuilderInStack=100, toStringNonPersist. */
    System.out.println("testStringConcatenationWithTemps: " + (timeRun * adjustTime) + "us");
    
    testFormat(234, 3.14F);
    
    testStringConcatenationUsingBuilder(456);
    testStringParameter();
    testStringBuffer();
    testReplace();
    testNonPersistenceOfStrings();
    testPersistenceOfStrings();
    str = testDateString("./", 123);
    str = testDateStringDynamic("./", 123);
    testInsertCharArray();
    String sPath = ".";
    /**@java2c=StringBuilderInStack:100, toStringNonPersist. */
    testOutStream(sPath + "/");
    testSomeSimpleStringMethods();
    testCharSequence();
    String s9 = toString(); //calls override-able method
    return s9;
  }
  

}
