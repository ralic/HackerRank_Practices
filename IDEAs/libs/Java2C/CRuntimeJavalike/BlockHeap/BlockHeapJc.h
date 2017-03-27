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
#ifndef __BlockHeapJc__h
#define __BlockHeapJc__h

#ifndef __ObjectJc_h__
  #include "Jc/ObjectJc.h"
#endif


/**Sets the current BlockHeapJc for this thread. */
METHOD_C struct BlockHeapJc_t* setCurrent_BlockHeapJc(struct BlockHeapJc_t* ythis, ThCxt* _thCxt);

/**Returns the current BlockHeap for this thread. */
METHOD_C struct BlockHeapJc_t* current_BlockHeapJc(ThCxt* _thCxt);

/**Sets the run mode for all instances of BlockHeapJc. Any new operation uses the block heap from up to now.
 * In Java it is an empty not necessary instruction, because all actions are done in normal heap.
 * In C this routine have to be implement from the user, because it is situational to all user-defined block heaps. 
 */
METHOD_C void setRunModeAll_BlockHeapJc(ThCxt* _thCxt);


/**Allocates a block which is non-based on ObjectJc. 
 * Note: If the block is based on ObjectJc, [[Jc:alloc_ObjectJc(...)]] have to be called. 
 *       It is necessary because the garbage collector should call [[Jc:finalize_ObjectJc(...)]].
 * @return 
 */
MemC allocMemC_BlockHeapJc(struct BlockHeapJc_t* ythis, int size, int nrofReferences, const char* sCallInfo, ThCxt* _thCxt);



/**Sets the log output for the current BlockHeap. */
void setLogMessageOutput_BlockHeapJc(struct LogMessageFW_t* log, int msgBase, ThCxt* _thCxt);



/**Returns the first BlockHeap in a system, able to use for new Threads without BlockHeap. */
METHOD_C struct BlockHeapJc_t* first_BlockHeapJc();


/**runs the garbage collector. One run tests one block cluster and returns after the test.
 * Either the blocks are freed or if at least one block is used, therefore no freeing is done.
 * @param logIdFreed The ident number of log system for the message of freed.
 *                      This message will be expected rarely.
 * @param logIdUsed  The ident number of log system for the message of used.
 *                      This message will be expected in every call.
 * @return 0-if a block is handled, but not freed.
 *         1-if a block is handled and freed.
 *         2-if the end is found
 */
METHOD_C int garbageCollection_BlockHeapJc(bool bUserCall, ThCxt* _thCxt);




/**Return values from [[garbageCollection_BlockHeapJc(...)]] 
 */
typedef enum EChecked_BlockHeapJc_t
{ checkGcBlockUsed_BlockHeapJc = 0
, checkGcBlockFreed_BlockHeapJc = 1
, checkGcFinished_BlockHeapJc = 2
}EChecked_BlockHeapJc_e;




bool runUserCalledGc_BlockHeapJc(ThCxt* _thCxt);



#endif //__BlockHeapJc__h
