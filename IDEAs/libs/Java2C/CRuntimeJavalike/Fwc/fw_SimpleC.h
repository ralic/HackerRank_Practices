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
 * @content This file contains basicly definitions and declarations using in C
 * independent of the CRuntimeJavalike concept.
 *
 * @author Jchartmut www.vishia.org
 * @version 0.82
 * list of changes:
 * 2010-02-20: Hartmut new: OFFSETinTYPE_SimpleC and OFFSETinDATA_SimpleC, but the same functionality is conained in fw_MemC.h, see there.
 * 2008-04-22: JcHartmut some changes in definitions to treat with memory:
 *                       * MemAreaC is shown with 32-bit-integer in debugger, not with char.
 *                       * The macros addOffset_MemAreaC and offset_MemAreaC work with any type of pointer.
 *                       * The element size_ of MemC should not be directly used outside, use the macro size_MemC
 *                         and setSize_MemC instead. size_ stores also an offset for treating in BlockHeap.
 * 2007-10-00: JcHartmut creation
 *
 ****************************************************************************/
#ifndef __simpleC_h__
#define __simpleC_h__
/**This file contains some usefull definitions for simpe C programming.

*/
#include <os_types_def.h>


/*@DEFINE_C specialDefines @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/

/** This type represents a pointer to a void name(void) - method.
  * It is needed on some locations in software.
  */
typedef void(* MT_void_Method_void)(void);

/** This type represents a pointer to a int name(int) - method.
  * It is needed on some locations in software.
  */
typedef int(* MT_int_Method_int)(int);



/**Calculates on compile time the number of elements of given array instance.
 * @param ARRAY a instance of an array type.
 */
#define ARRAYLEN(ARRAY) (sizeof(ARRAY)/sizeof(ARRAY[0]))

/**Calculates the offset of an element within the given structure type.
 */
/* NOTE: offsetof in stddef.h works insufficient.
 * NOTE: The pointer to 0x1000 is used because any compiler generates an error if a 0-pointer is used. 
 * @deprecated see fw_MemC.h
 */
#define OFFSETinTYPE_SimpleC(TYPE, ELEMENT)  ( (MemUnit*)( &( ((TYPE*)0x1000)->ELEMENT) )  - (MemUnit*)0x1000)

/**Calculates the offset of an element within the referenced structure data.
 * @deprecated see fw_MemC.h
 */
#define OFFSETinDATA_SimpleC(PTR, ELEMENT)  ( (MemUnit*)( &( (PTR)->ELEMENT) )  - (MemUnit*)(PTR))


#define SIMPLE_CAST(Type, value) ((Type)(value))

#if defined(__cplusplus) && defined(__CPLUSGEN)
  #define STATIC_CAST(type, value) (static_cast<type>(value))

#else //not __CPLUSGEN

  #define STATIC_CAST(Type, value) ((Type)(value))
#endif


/*@CLASS_C Int32Array256 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/

/** This structure is useable if raw data should be mapped, it may typical used as pointer type.
  * The size of the array is a free choiced number, appropriate to show the data in debugger.
*/
typedef struct Int32Array256_t
{ int32 data[256];
}Int32Array256;






/*@CLASS_C Fwc @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/

/**Counts the number of chars till a \0-char. 
 * In opposite to the C-standard strlen, this routine may not crash on undefined strings. 
 * @param text The string where the length till \0 is to detect.
 * @param maxnrofChars maximal number of chars to detect. This parameter may be set by a suitable value,
 *        at example the maximal size of a buffer. The possibility of a memory fault access 
 *        is some more less than as in comparision with the strlen-call without any limits.
 * @return the number of chars till \0 or maxNrofChars. 
 */
int strlen_Fwc(char const* text, int maxNrofChars);




/*@CLASS_C BinarySearch @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/
/**Searches the position of key inside the sorted array data[].
 * This algorithm is equal java.util.binarySearch(int[] a, int fromIndex, int toIndex, int key)
 * but without rangeCheck of input values.
 * The algoritm works only if the data are sorted and the indices are matching.
 *
 * @param data array of values in ascending sorted order
 * @param fromIndex The first index of data inside array, typically 0
 * @param toIndex The last+1 index of data (exclusive), typically the number of values.
 * @param key the value to be searched for 
 * @return index of the search key, if it is contained in the array within the specified range; 
 * otherwise, (-(insertion point) - 1). The insertion point is defined as the point 
 * at which the key would be inserted into the array: 
 * the index of the first element in the range greater than the key, 
 * or toIndex if all elements in the range are less than the specified key. 
 * Note that this guarantees that the return value will be >= 0 if and only if the key is found. 
 */ 
METHOD_C int binarySearch_int(int32 const* data, int fromIndex, int toIndex, int32 key);
METHOD_C int binarySearch_int64(int64 const* data, int fromIndex, int toIndex, int64 key);

#define binarySearch_int_simpleC binarySearch_int




/**Parses a given String and convert it to the integer number.
 * The String may start with a negativ sign ('-') and should contain digits after them.
 * The digits for radix > 10 where built by the numbers 'A'..'Z' respectively 'a'..'z',
 * known as hexa numbers A..F or a..f. 
 * @param src The String, non 0-terminated, see ,,size,,.
 * @param size The number of chars of the String.
 * @param radix The radix of the number, typical 2, 10 or 16, max 36.
 * @param retSize return-integer with the rest of size, which is non-assignable to a number.
 * @return the Number.
 * @throws never. All possible digits where scanned, the rest of non-scanable digits are returned.
 *  At example the String contains "-123.45" it returns -123, and the retSize is 3.
 */
METHOD_C int parseInt_Fwc(const char* src, int size, int radix, int* retSize);



#endif  //__simpleC_h__
