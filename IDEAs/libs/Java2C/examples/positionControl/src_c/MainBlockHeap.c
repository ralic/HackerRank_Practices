/**This file organizes the calling of system-Methods from CRuntimeJavalike for BlockHeap with Garbage Collection.
   It is a part from the Example to Java2C.
 */
#include "Main.h"
#include "BlockHeap/BlockHeapJc_admin.h"
/*
struct MyHeapCtrl_t
{ ObjectArrayJc head;
  BlockHeapJc data[10];
} myHeapCtrl;
*/


BlockHeapJc blockHeapModul1;

BlockHeapJc blockHeapModul2;


BlockHeapJc* pBlockHeapModul1 = &blockHeapModul1;


void initBlockHeap()
{ 
  { //MemC mem_blockHeapModul1 = build_MemC(&blockHeapModul1, sizeof(blockHeapModul1));
    init_ObjectJc(&blockHeapModul1.base.object, sizeof(blockHeapModul1),0);
    ctorO_BlockHeapJc(&blockHeapModul1.base.object, alloc_MemC(0x40000), 0x400, 0x7C);
  }
}



void setRunModeBlockHeap(struct LogMessageFW_t* log)
//void setRunModeBlockHeap(LogMessageFW_i* log)
{ setRunMode_BlockHeapJc(&blockHeapModul1, log, 2000);
}


void garbageCollection()
{
  STACKTRC_ENTRY("garbageCollection");
  garbageCollection_BlockHeapJc(false, _thCxt);
  STACKTRC_LEAVE;
}



