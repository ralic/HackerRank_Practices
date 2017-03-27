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
 * @content Bodies of mutex routines for Windows-XP and Windows-2000.
 * The implementation is os-specific. A mutex should regard a second call from the same thread.
 * It is more complex as a simple semaphore request. 
 * Some operation systems supports this request immediately. 
 * If a opeation system does know only the semaphore mechanism, the thread identification
 *   should be stored in a mutex structure.
 *
 * @author Hartmut Schorrig
 * @version sf-0.83
 * list of changes:
 * 2008-02-01: Hartmut creation 
 *
 ************************************************************************************************/
#include "OSAL/inc/os_sync.h"
#include "OSAL/inc/os_error.h"
#include "OSAL/inc/os_mem.h"
#include "os_internal.h"
#include <windows.h>
#include <stdio.h>

/* CONSTANTS *************************************************************************************/


/* GLOBAL VARIABLES ******************************************************************************/


/* EXTERNALS *************************************************************************************/


/* PROTOTYPES ************************************************************************************/

int os_createMutex(char const* pName, OS_Mutex* pMutex)
{

    HANDLE    hIOMutex;
    DWORD err;
    struct OS_Mutex_t* mutex;
    const int zMutex = sizeof(struct OS_Mutex_t);
    
    hIOMutex = CreateMutex (NULL, FALSE, NULL);  // initially not owned
    if ( hIOMutex == NULL )
    {
		  err = GetLastError();
        os_NotifyError(-1, "os_createMutex: ERROR: create mutex failed with Win err= %d\n", err, 0 );
        *pMutex = null;
		  return OS_SYSTEM_ERROR;
    }
	  mutex = (struct OS_Mutex_t*)(malloc(zMutex));
    memset(mutex, 0, zMutex);
    mutex->handle  = hIOMutex;
    *pMutex = mutex;
    return OS_OK;
}


int os_deleteMutex(OS_Mutex mutex)
{ HANDLE handle = mutex->handle;
  os_freeMem((void*)mutex);
  if ( CloseHandle( handle ) == 0 ) 
  {
    DWORD err = GetLastError();
    os_NotifyError(-1, "os_deleteMutex: ERROR: CloseHandle failed with Win err=%d\n", err,0 );
	  if (err==ERROR_INVALID_HANDLE)
    {	return OS_INVALID_HANDLE;
	  }
    else
	  { return OS_SYSTEM_ERROR;
    }
  }
  else
  { return OS_OK;
  } 
}


int os_lockMutex(OS_Mutex mutexP)
{
	DWORD WinRet;
  OS_Mutex_s* mutex = (OS_Mutex_s*)mutexP; //the non-const variant.  	
    WinRet = WaitForSingleObject( (HANDLE)mutex->handle, -1 );
    if (WinRet == WAIT_FAILED)
    {	DWORD err = GetLastError();
      os_NotifyError( -1, "os_waitMutex: ERROR: WaitForSingleObject failed with Win err=%d\n", err, 0 );
 			if (err==ERROR_INVALID_HANDLE)
      { return OS_INVALID_HANDLE;
			}
			else { return OS_SYSTEM_ERROR; }
		}
		
//		if (WinRet==WAIT_ABANDONED){ // in case another thread terminated and did not release the mutex
//			return OS_SYSTEM_ERROR;
//		}
    /*
    if(mutex->threadOwner != null)
    { os_Error("Anybody has locked the mutex, but this thread have the lock of system.", (int)(mutex->threadOwner));
      //ignore it if os_Error() is not acute.
    }
    mutex->threadOwner = os_getCurrentThreadContext_intern();
    */
    return OS_OK; 
}


int os_unlockMutex(OS_Mutex mutexP)
{
  OS_Mutex_s* mutex = (OS_Mutex_s*)mutexP; //non const
  /*
    struct OS_ThreadContext_t const* pThread = os_getCurrentThreadContext_intern();
    struct OS_ThreadContext_t const* threadOwner = mutex->threadOwner;
    //it is neccesary to test whether the unlocker is the correct thread.
    if(pThread != threadOwner)
    { os_Error("faut thread", (int)pThread);
    }
    //first write null for locking thread, than release,
    //because anther thread may be locked immediately and will found an empty threadOwner!
    mutex->threadOwner = null;
  */
  if ( ReleaseMutex( (HANDLE)mutex->handle ) == 0 )
  { int32 err;
    //revert infos because the unlock isn't valid! It is important for debug
    //mutex->threadOwner = threadOwner;
    err = GetLastError();
    if (err == ERROR_NOT_OWNER)
    { /**It is helpfull to produce another error message if another thread release the mutex,
       * because it is a users programming error. */
      os_NotifyError(-1, "os_unlockMutex: ERROR: Faild thread releases the mutex, win-error=%d\n", err, 0);		
		  return OS_UNEXPECTED_CALL;
    }
    else
    { os_NotifyError(-1, "os_unlockMutex: ERROR: ReleaseMutex failed with win-error=%d\n", err, 0);		
		  if (err==ERROR_INVALID_HANDLE){ return OS_INVALID_HANDLE; }
		  else return OS_SYSTEM_ERROR;
    }	  
  }
  else 
  { return OS_OK; 
  }
}
