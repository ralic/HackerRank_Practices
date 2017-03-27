/************************************************************************************************
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
 * This source may be used also with another licence, if the author 
 * and all other here named co-authors have agreed to this contract.
 * Especially a company can use a copy of this sources in its products without publishing.
 * The user should have a underwritten contract therefore.
 *
 * @author Hartmut Schorrig, Germany, Pinzberg, www.vishia.org
 *
 **copyright***************************************************************************************
 *
 * @content
 *
 * @author Hartmut Schorrig, Pinzberg, Germany
 *
 * @version 0.83
 *
 * @content This file is a sample for the users CRuntimeJavalike_SysConventions.h.
 * The user may move or copy this file in its own file folder known by the include path
 * and modify it. In the comments the possibilities of modifications are explained.
 * @author Jchartmut www.vishia.org
 * @version 0.81
 * list of changes:
 * 2006-07-23: JcHartmut creation
 * 2006-09-24: JcHartmut modification
 *
 ****************************************************************************/
#ifndef __fw_SysConventions_h__
#define __fw_SysConventions_h__

#include <os_types_def.h>

/**Usage of the BlockHeap management or not and define the size of a block.
 * The size of a normal block in all BlockHeaps are the same. The size must be (!) a power of 2.
 */
//#define SIZEBLOCK_BlockHeapJc 0x400
#undef SIZEBLOCK_BlockHeapJc

#define NO_DYNAMICALLY_MEMORY
//#define SMALL_DYNAMICALLY_MEMORY
//#define USE_DYNAMICALLY_MEMORY


//#define __CPLUSPLUSJcpp
#undef __CPLUSPLUSJcpp



/** If this define is setted, the TRY, CATCH and THROW makros use the C++ keywords
  * try, throw and catch. All sources, also the *.c-Sources of the CRuntimeJavalike,
  * must be compiled with a C++-Compiler.
  *
  * If the macro is not setted, the TRY, CATCH and THROW makros use
  * the longjmp version. See fw_Exception.h. It is also possible to use longjmp
  * in a C++ environment,
  * but destructors of local stack instances in skipped subroutines are ignored.
  * It must be secured that no critical destructors are used, or a FINALLY is used there.
  */
//#define __TRYCPPJc
#undef __TRYCPPJc

/**Under Test conditions, the check of Stacktrace consistence should be activated. 
 * Because a forgotten STACKTRC_LEAVE-macro call won't be detected else,
 * and the stacktrace is corrupt for later usage.
 * Deactivate this macro in release versions.
 */
#ifdef _DEBUG
  #define TEST_STACKTRCJc 
#else
  #undef TEST_STACKTRCJc
#endif	



//#define ASSERT(COND) assertJc(COND)

/** Definition of some types for C-language compatible to C++*/
#ifndef __cplusplus
   /** In C-language the keyword bool is not known. Define it as int.*/
  #define bool int
  /** In C-language the keyword true is not known. Define it as all bits set.*/
  #define true ~0
  /** In C-language the keyword false is not known. Define it as no bit set.*/
  #define false 0
#endif




/**
* In C-language all c-like Methods are C-Methods, no additional marking.
*/
#define METHOD_C
//#define METHOD_C extern "c"



/** This type is used inside a reference to hold the 2 informations:
  * index inside the virtual table and index of back reference for garbage collection.
  * The type is also used in StringJc in a special way, see there.
*/
typedef int32 ObjectRefValuesJc;
#define nrofBit_ObjectRefValuesJc 32

/** Bits representing the start index of a interface inside a C-like-virtual table
    inside the type ObjectRefValuesJc in enhanced references.
  * If this define is setted, the enhanced references are used.
*/
#define mIdxMtbl_ObjectJc              0x0000ffff
//#undef mIdxMtbl_ObjectJcRef


/** Nr of bitposition to shift the bits to the right, it should be complemental to mIdxMtbl_ObjectRef*/
//#define kBitBackRef_ObjectJc 16
#undef kBitBackRef_ObjectJc
/** Bits of index of backward reference, justified after shifting. */




/** Right shifted Bits representing the index of the backpointer from an object to the reference
    inside the type ObjectRefValuesJc in enhanced references.
  * If this define is setted, the enhanced references are used.
*/
//#define mBackRef_ObjectJc              0xffff0000
#undef mBackRef_ObjectJc


/** Bits of length of constant string, it have to be coordinated with mBackRef_ObjectJcRef*/
#define mLength__StringJc                 0x00003fff

  //_setCount_StringBufferJc(x, x); //

/**If this Bit is set, the StringJc referenced the whole string of a StringBufferJc to concat strings.*/
#define mNonPersists__StringJc            0x00004000

/**If all this Bits are set, the StringJc references a buffer in the thread context..*/
#define mThreadContext__StringJc               0xFFFF0000

/**If this Bit is set, the StringJc referenced the whole string of a StringBufferJc to concat strings.*/
#define xxxmBuffered__StringJc               0x00008000

/* Bits are setted to 1, if it is a constant string
    and the mLength_String_ObjectRef - bits contained the length.
    The rightest mask bit should be the same as rightest mask bit
    of mBackRef_ObjectRef shifted by kBitBackRef_ObjectRef,
    If this number is mask and shifted with mBackRef_ObjectRef, it should be greater as the
    maximal number of any index in maxBackRef_ObjectRef.
*/
//#define mIsConstant_StringJc     0xffff0000





/* Maximal length of path in a FileIoJc-structure.
 * The minimal value is defined in os_file.h with kMaxPathLength_OS_FileDescription.
 * That is a minimal length for less systems. The struct FileIOJc has an additional
 * char[]-buffer after the OS_FileDescription-structure, which stores the path.
 * This parameter is the max. length, it have to be 
 * greater than kMaxPathLength_OS_FileDescription (=24).
 */
#define kMaxPathLength_FileJc 224





/*@DEFINE xxx @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/

#define IDXBACKREFJc(REF) ((REF).value__ & mBackRef_ObjectJc)



/** Define the length of namefield in Reflection*/
#define kLengthNAME_CLASS_ReflectionJc 32
#define kLengthNAME_FIELD_ReflectionJc 30
#define kLengthNAME_METHOD_ReflectionJc 32

#endif  // __fw_SysConventions_h__
