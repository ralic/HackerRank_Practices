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
 * @content Access to memory with address and size.
 *
 * @author JcHartmut www.vishia.org
 * @version 0.81ad
 * list of changes:
 * 2007-10-01: JcHartmut creation
 *
 ****************************************************************************/
#include "fw_MemC.h"
#include <os_types_def.h>
#include "OSAL/inc/os_mem.h"
#include "Fwc/fw_Exception.h"
//#include "fw_ThreadContext.h"
#include <fw_Platform_conventions.h> //mLength_StringJc
#include <string.h>

MemC alloc_MemC(int size)
{ MemC mem;
  void* addr = os_allocMem(size);
  set_MemC(mem, addr, size);
  init0_MemC(mem);
  return mem;
}


MemC alloc0_MemC(int size)
{ MemC mem;
  void* addr = os_allocMem(size);
  set_MemC(mem, addr, size);
  init0_MemC(mem);
  return mem;
}


int free_MemC(MemC mem)
{ return os_freeMem(PTR_MemC(mem, void));
}



MemC init0_MemC(MemC mem)
{ memset(PTR_MemC(mem, void), 0, size_MemC(mem));
  return mem;
}


void init0p_MemC(void* ptr, int size)
{ memset(ptr, 0, size);
}

MemC build_MemC(void* address, int size)
{ MemC mem;
  set_MemC(mem, address, size);
  //setSizeAndOffset_MemC(mem, size, 0);
  return mem;
}


void checkSize_MemC(MemC mem, int size, struct ThreadContextFW_t* _thCxt)
{
  if(size < (int)size_MemC(mem)) THROW_s0(IllegalArgumentException, "sufficient size", size);  
}



MemC fromArea_MemC(void* address, void* endAddress)
{ MemC mem;
  int size = (MemUnit*)endAddress - (MemUnit*)address;
  ASSERT(size >=0);
  set_MemC(mem, address, size);
  return mem;
}


METHOD_C MemC subset_MemC(MemC parent, int offsetStart, int offsetEnd)
{ MemC mem;
  int sizeParent = size_MemC(parent);
  int subSize;
  void* subPtr;
  STACKTRC_ENTRY("address_MemC");
  if(offsetEnd <=0)
  { offsetEnd = size_MemC(parent) - offsetEnd;
  }
  if(offsetStart >= sizeParent)   THROW_s0(IndexOutOfBoundsException,"offsetStart to large", offsetStart);
  else if(offsetEnd > sizeParent) THROW_s0(IndexOutOfBoundsException,"offsetEnd to large", offsetEnd);
  else if(offsetStart < 0)        THROW_s0(IndexOutOfBoundsException,"offsetStart negative", offsetStart);
  else if(offsetEnd < 0)          THROW_s0(IndexOutOfBoundsException,"offsetEnd negative", offsetEnd);

  subSize = offsetEnd - offsetStart;
  subPtr = PTR_MemC(parent, MemUnit) + offsetStart;
  set_MemC(mem, subPtr, subSize);
  STACKTRC_LEAVE; return mem;
}



struct MemAreaC_t* address_MemC(MemC mem, int offset, int nrofBytes)
{ int size = size_MemC(mem);
  STACKTRC_ENTRY("address_MemC");
  if(offset >= size)                 THROW_s0(IndexOutOfBoundsException,"offset to large", offset);
  else if(offset + nrofBytes > size) THROW_s0(IndexOutOfBoundsException,"nrofBytes to large", nrofBytes);
  else if(offset < 0)                THROW_s0(IndexOutOfBoundsException,"offset negative", offset);
  else if(nrofBytes < 0)             THROW_s0(IndexOutOfBoundsException,"nrofBytes negative", offset);
  STACKTRC_LEAVE; return (struct MemAreaC_t*)(PTR_MemC(mem, MemUnit) + offset);
}



void insert_MemC(MemC mem, void const* insertAddress, int nrofBytes)
{ int size = size_MemC(mem);
  int idx = (MemUnit*)insertAddress - PTR_MemC(mem, MemUnit);
  STACKTRC_ENTRY("insert_MemC");
  if(idx < 0 || (idx + nrofBytes) > size || nrofBytes < 0)
  { THROW_s0(IndexOutOfBoundsException,"idx=", idx);
  }
  else
  { 
    int nrofBytesToEnd = size_MemC(mem) - idx - nrofBytes;
    if(nrofBytesToEnd > 0)
    { MemUnit* dst = (MemUnit*)insertAddress + nrofBytes;
      memmove(dst, insertAddress, nrofBytesToEnd);
    }
  }
  STACKTRC_LEAVE;
}







