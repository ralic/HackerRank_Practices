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
 * @content: All declarations to use from user level to work with BlockHeapJc
 * for References of ObjectJc and derivated classes for C-language.
 * The references are used for garbage collection and virtual methods in C.
 *
 * @author JcHartmut www.vishia.org
 *
 ****************************************************************************/
#ifndef __ObjectRefJc__h
#define __ObjectRefJc__h

/**NOTE: This file is also included in ObjectJc.h, but in this case the __ObjectJc__ is set.
 */
#ifndef __ObjectJc_h__
  #include "ObjectJc.h"
#endif


struct StringBufferJc_t;

/**This file is included inside ObjectJc.h only if the appropriate defines
  , #if defined(mBackRef_ObjectJc) || defined(mIdxMtbl_ObjectJc)
  are setted in the file ,,CRuntimeJavalike_SysConventions.h,,.
  In this cases the enhanced references are used.

  This file should not be included directly from user.
*/

/*@CLASS_C ObjectJcREF @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/

typedef struct ObjectJcREF_t
{/** The followed attribute TODO
      *
      * @sizeof=4 This attribute is defined in the user modificable CRuntimeJavalike_Sysconventions.h
      *           and should be have the same size like a pointer or int
      */
    ObjectRefValuesJc refbase;

    /** The pointer to the string itself.*/
    ObjectJc* ref;

}ObjectJcREF;

/**Condition-define.
 * NOTE: This define have to be set because REF-types are generated automatically in Java2C and they are compiled conditionaly. */
#define ObjectJcREFDEF

//compatibility.
#define REF_ObjectJc ObjectJcREF;
#define REFP_ObjectJc ObjectJcREF;

/**Macro to define the enhanced reference type for TYPE. It defines TYPE##REF and TYPE##REFP.
 * @param TYPE the identifier of the TYPE itself.
 */
#define REFTYPEDEFJc(TYPE) typedef struct TYPE##REF_t{ ObjectRefValuesJc refbase; struct TYPE##_t* ref; }TYPE##REF;  typedef TYPE##REF* TYPE##REFP;

/**Macro to define the reflection of the enhanced reference of any type, able to use in a C-File.
 * @param TYPE the type of the reference.
 */
#define DEFINE_REFLECTION_REF(TYPE) \
  extern struct ClassJc_t const reflection_##TYPE; \
  extern struct ClassJc_t const reflection_##TYPE##REF; \
  const struct Reflection_Fields_##TYPE##REF_t{ ObjectArrayJc head; FieldJc data[2];} reflection_Fields_##TYPE##REF = \
  { CONST_ObjectArrayJc(FieldJc, 2, OBJTYPE_FieldJc, null, &reflection_Fields_##TYPE##REF) \
  , { { "refbase", 0 , &reflection__intJc, 0, 0, 0, &reflection_##TYPE##REF } \
    , { "ref", 0 , &reflection_##TYPE##_s, 0, 0, 0, &reflection_##TYPE##REF } \
  } }; \
  const ClassJc reflection_##TYPE##REF =\
  { CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_##TYPE##REF, null) \
  , "Ptr", 0, sizeof(TYPE##REF), (FieldJcArray const*)&reflection_Fields_##TYPE##REF \
  }




/** Assigns a reference between the Block of heap, which src is contained in, and the given enhanced reference.
  * This method doesn't store the reference to the src in the enhanced reference, 
  * it should be done outside because better pointer type checking. 
  * A back reference to refbase is stored in the block of the heap,
  * and the Index of this back reference is stored in refbase.

  * If src is not inside a block heap, a 0 value is setted to *refbase.

  @ param refbase the address of the enhanced reference. The pointer to refbase is given because
                  several typed enhanced references are from different type.
  @ param src any location inside a block in BlockHeap
  @ return true if successfull, false if thrown
  */
//METHOD_C bool setBackRefJc(int* refbase, void const* src);
METHOD_C bool setBackRefJc(ObjectRefValuesJc* refbase, void const* src);


/** Clears a reference between the Block of heap, which src is contained in, and refbase.
    The reference itself will be not stored here, it should be done outside because better
    pointer type checking. A back reference to refbase is stored in the block of the heap,
    and the Index of this back reference is stored in refbase.

    If src is not inside a block heap, a 0 value is setted to *refbase.

  @ param refbase the reference
  @ return true if successfull, false if thrown
  */
METHOD_C bool clearBackRefJc(ObjectRefValuesJc* refbase);



/** Assigns a reference between the Block of heap, which src is contained in, and refbase.
    The reference itself will be not stored here, it should be done outside because better
    pointer type checking. A back reference to refbase is stored in the block of the heap,
    and the Index of this back reference is stored in refbase.

    If src is not inside a block heap, a 0 value is setted to *refbase.

  @ param refbase the reference
  @ param src any location inside a block in BlockHeap
  @ return true if successfull, false if thrown
  */
METHOD_C bool setBackRefIdxMtblJc(ObjectRefValuesJc* refbase, void const* src, struct ClassJc_t const* reflection);


/**Makro to set a enhanced reference, able to use in primary sources for enhanced references or not enhanced references or C++. 
 * Note: The reference itself will be set an extra assignment tests the type by compiler.
 * Implementation note: The method ,,setBackRefIdxMtblJc(...),, checks whether the reference is used yet, it frees it first.
 * Calling ,,clearBackRefJc(...),, is not necessarry.
 */ 
#define SETREFJc(REF, OBJP, REFTYPE) { clearBackRefJc(&(REF).refbase); (REF).ref = OBJP; setBackRefIdxMtblJc(&(REF).refbase, (REF).ref, null); }
//NOTE: important: do not use OBJP more as one, because it may be a complex method.
//therefore the following variant is false:
//#define SETREFJc(REF, OBJP, REFTYPE) { setBackRefIdxMtblJc(&(REF).refbase, OBJP, null); (REF).ref = OBJP; }
//#define SETREFJc(REF, OBJP, REFTYPE) { setBackRefIdxMtblJc(&(REF).refbase, OBJP, &reflection_##REFTYPE); (REF).ref = (REFTYPE*)OBJP; }


#define REFJc(REF) (REF).ref

#define CLEARREFJc(REF) { clearBackRefJc(&(REF).refbase); (REF).ref = null; }


/**Makro to set a enhanced reference. 
 * Note: because the makro should write in form
 *   ASSIGN_REF(dst, value);
 * the ending semicolon is set outside the makro.
 * @param REF The reference as value
 * @param OBJP pointer to the instance (a pointer) 
 */
#define xxxASSIGN_REFJc(REF, OBJP) if((REF).ref != null) clearBackRefJc(&((REF).refbase)); (REF).ref = (OBJP); setBackRefJc(&(REF).refbase, (REF).ref)



/**Makro to set an StringJc, it may be an enhanced reference. 
 * Note: because the makro should write in form
 *   SETREF_STRINGJc(dst, value);
 * the ending semicolon is set outside the makro.
 *
 * Implemenation note: A StringJc is defined as OS_ValuePtr, see ,,Fwc/fw_String.h,,. That definition is presumed here. 
 * @param DST The dst StringJc per reference.
 * @param STR The string as value 
 */
#define xxxSETREF_StringJc(DST, STR) if((DST)->value__ != null) clearBackRefJc(&(DST)->value__); *(DST) = STR; setBackRefJc(&(DST)->value__, (STR).ptr__)
#define xxxASSIGN_STRINGJc(REF, STR) if((REF).ref != null) clearBackRefJc(&((REF).value__)); REF = STR; setBackRefJc(&(REF).value__, (STR).ptr__)

     
/**Makro to set a enhanced reference to null. 
 * Note: because the makro should write in form
 *   CLEAR_REF(dst, value);
 * the ending semicolon is set outside the makro.
 * @param REF The reference as value
 */
#define CLEAR_REFJc(REF) clearBackRefJc(&((REF).refbase)); (REF).ref = null
     
/**Macro to initialize the value of a defined reference staticly to null. */
#define NULL_REFJc { 0, null}

/*@CLASS_C BlockHeapJc @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/


/**Allocates a block in the current Blockheap or initial with standard malloc.
 */
METHOD_C MemC alloc_sBlockHeapJc(int nrofBytes);

/**allocates a block in the standard BlockHeap.
 * @param sCallInfo A pointer to a zero-terminate constant string to report the creater of the block usage.
*/
METHOD_C MemC alloc_s_sBlockHeapJc(int size, const char* sCallInfo, ThCxt* _thCxt);
METHOD_C ObjectJc* allocObject_s_sBlockHeapJc(int sizeObj, int identObj, const char* sCallInfo, ThCxt* _thCxt);

METHOD_C MemC alloc_s_BlockHeapJc(struct BlockHeapJc_t* ythis, int size, const char* sCallInfo, ThCxt* _thCxt);
METHOD_C ObjectJc* allocObject_s_BlockHeapJc(struct BlockHeapJc_t* ythis, int sizeObj, int identObj, const char* sCallInfo, ThCxt* _thCxt);


METHOD_C MemC alloc_iis_BlockHeapJc(struct BlockHeapJc_t* ythis, int sizeObj, int noOfReferences, const char* sCallInfo, ThCxt* _thCxt);
METHOD_C ObjectJc* allocObject_IIs_BlockHeapJc(struct BlockHeapJc_t* ythis, int sizeObj, int identObj, int noOfReferences, const char* sCallInfo, ThCxt* _thCxt);

/**activates the access of garbage collector if it is not done already.
 * If the reference is not stored in any enhanced reference using setBackRefJc
 * it is not testet by garbage collector until this time.
 * This method should be called on end of a statement block which allocs the mem.
 *
 * @param addr Memory location of any object. It will be tested whether it is in range of any Blockheap-block.
 * @param exclAddr If this addr isn't null and it is in the same block, the block doesn't activate for garbage collection.
 *        It is because a returned address may be use in the calling environment. 
 *        The activating for garbage collection have to be organized there. The Java2C-Translator consideres this situation.
 */
METHOD_C void activateGarbageCollectorAccess_BlockHeapJc(void const* addr, void const* exclAddr);

/**searches the block in any BlockHeap and returns the base address of the block. 
 * @param address Any address inside the block or any other address of any Object. null is admissible too.
 * @param retHeap Pointer to the return-pointer instance, 
 *        it will be filled with the reference to the Heap-Management-Data.
 * @return null if the address-parameter is not localized in any heap, elsewhere the address of the block.
 */
METHOD_C struct BlockHeapBlockJc_t* searchBlockHeapBlock_BlockHeapJc(void const* address, struct BlockHeapJc_t** retHeap);

/**deduces from any address inside the block to the base address of the block.
 * @param address Adress inside any block.of the given heap.
 */
METHOD_C struct BlockHeapBlockJc_t* deduceBlockHeapBlock_BlockheapJc(void const* address);


METHOD_C void free_sBlockHeapJc(MemC, ThCxt* _thCxt);

METHOD_C void free_BlockHeapJc(struct BlockHeapJc_t* ythis, struct BlockHeapBlockJc_t* block, ThCxt* _thCxt);


/** Allocates the memory for the given type in current BlockHeap*/
#define alloc_T_sBlockHeapJc(TYPE) ((TYPE*)(alloc_BlockHeapJc(current_BlockHeapJc(), sizeof(TYPE))))





/** The current BlockHeap. This reference may be dependent on the software component and thread. */
METHOD_C struct BlockHeapJc_t* current_BlockHeapJc();





METHOD_C bool isReferenced_StringBufferJc(struct StringBufferJc_t* ythis, ThCxt* _thCxt);


#endif //__ObjectRefJc__h
