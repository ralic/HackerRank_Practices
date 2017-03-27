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
 *
 * @version 0.91
 *
 * list of changes:
 *   
 * 2009-11-25: Hartmut: chg: ctor_s0ii_Address_InterProcessComm_Socket_s uses ObjectJc* instead MemC to makes sure the correct initialization
 * 2005-09-09: Hartmut creation
 */
#ifndef __InterProcessCommSocket_h__
#define __InterProcessCommSocket_h__

//#include "fw_MemC.h"
#include "Ipc/InterProcessComm.h"
#include "Fwc/fw_MemC.h"

//extern Mtbl_InterProcessComm const mtbl_InterProcessCommSocket;

InterProcessComm_i* create_InterProcessCommSocket(struct Address_InterProcessComm_t* ownAddress);

struct Address_InterProcessComm_t* ctor_s0ii_Address_InterProcessComm_Socket_s(ObjectJc* othis, MemC rawMem, char const* protocol, int32 ipAddr, int nPort);

struct Address_InterProcessComm_t* ctorO_Address_InterProcessCommSocket(ObjectJc* othis, char const* protocol, int32 ipAddr, int nPort);

struct Address_InterProcessComm_t* ctorO_s0_Address_InterProcessCommSocket(ObjectJc* othis, char const* address);

//to encapsulate the stucture (prevent further dependencies:

/**The sizeof is calculated from a translator or manual. It is checked in constructor. It may be greater here.*/
#define sizeof_InterProcessCommSocket_s 2000

METHOD_C struct InterProcessCommSocket_t* ctorO_InterProcessCommSocket(ObjectJc* othis, struct Address_InterProcessComm_t* addr, struct ThreadContextFW_t* _thCxt);

#endif //__InterProcessCommSocket_h__
