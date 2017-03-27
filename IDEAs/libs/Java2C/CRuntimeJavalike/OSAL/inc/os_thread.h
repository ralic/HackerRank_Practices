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
 * @content Definition of memory access routines. 
 * This routines are implemented in a operation system specific way. 
 *
 * @author Hartmut Schorrig
 * @version sf-0.83
 * list of changes:
 * 2008-10: re-creation of the ThreadContext, it was present in the older sources.
 * 2008-06: Marcos Rodriguez: some improvements.
 * 2008-02-01: Hartmut creation from older sources: ThreadJc-os-adaption.
 *
 ************************************************************************************************/
#ifndef __os_thread_h__
#define __os_thread_h__

#include <os_types_def.h>






/**Handle to a thread. The internal data structure is not known here. 
 * A ,,OS_HandleThread,, may also be a simple integer, which is converted to this pointer type.
 */
typedef struct OS_HandleThread_t* OS_HandleThread;

/**The thread routine should have the followed prototype.
 * @param data Pointer to data. Mostly it is a class structure. 
 *        It is the same value given in createThread, param pUserData.
 * @param _thC The ThreadContext structure. 
 * @return The exit value of the thread. The operation system can protocol or show the exit value in a special kind.
 */
typedef int OS_ThreadRoutine(void* data);





/**This method should be called on startup of the system. 
 * It is called elsewhere from the routines, which needs them.
 */
int os_initLib();


/**Returns the os-specific value for a thread priority.
 *
 * @param abstractPrio A value between 1 and 255. 1 is the lowest priority, 255 the highest one.
 * @return associated priority of the os.
 */
int os_getRealThreadPriority(int abstractPrio);


/**Creates and starts a thread.
 * The thread will be end if the thread routine returns.
 * @param pHandle reference to a numerical identificator of the thread.
 *        Note: all internal management data of the thread are stored os-internally.
 * @param routine Pointer to the thread routine, which is started.
 * @param pUserData This value will be transported to the first parameter of the user routine.
 *        Typically it are the user instance data of the thread method.
 * @param sThreadName A textual identification for the operation system.
 * @param abstractPrio Priority between 1 (lowest) and 255 (highest).
 *        Note: The opeation system may assign different abstract priorities 
 *        to the same real priority. It is not guaranteed, that such threads have different priorities. 
 * @param stackSize The necessary size of stack. If 0 or -1,than the default stack size is used.
 *        The default stack size is defined in the implementation and depends of the os-possibilities.
 *        Note: Possible stack sizes depends on possibilities of the operation system.
 *        It is a good style that the stack size should be in range of 2..16 kBytes, 
 *        the user shouldn't placed large data in stack.
 *        Note, that interrupt handlers uses the stack also. 
 * @return 0 if ok, negativ value on error.
 */ 
int os_createThread
( OS_HandleThread* pHandle
, OS_ThreadRoutine routine
, void* pUserData
, char const* sThreadName
, int abstactPrio
, int stackSize
);

/**Changes the thread priority.
 */
int os_setThreadPriority(OS_HandleThread handle, uint abstractPrio);


/**NOTE: suspend and resumed must not called from another thread. Therefore this routines
 * are not defined here!!! 
 * If a thread is suspended from outside, and it is the owner of a ressource, the system may hang.
 */
//do not use: int os_suspendThread(OS_HandleThread handle);
//do not use: int os_resumeThread(OS_HandleThread handle);


/**Gets the current thread handle. This routine should have a short calculation time.
 */
OS_HandleThread os_getCurrentThreadHandle(void);

/**Gets the user-thread-context of the current thread. The user-thread-context is a memory area,
 * assigned to any thread, which contains thread-local but routine-global data. 
 * The structure of the user-thread-context can be defined in a user-adaption layer. 
 * It should not be defined depending of the users algorithm, but in a common valid kind of users algorithm.
 * @return the pointer and the size of the users thread context. If the users threadcontext
 * was not set, the return structure contains {null, 0}
 */
METHOD_C OS_PtrValue os_getCurrentUserThreadContext();


/**Sets the users thread context. This method can only be called one time for each thread.
 * @return error OS_UNEXPECTED_CALL if the users thread context is set already.
 */
METHOD_C int os_setCurrentUserThreadContext(OS_PtrValue mem);



#endif //__os_thread_h__
