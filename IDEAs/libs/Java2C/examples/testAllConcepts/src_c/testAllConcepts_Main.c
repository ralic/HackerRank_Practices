#include "TestAllConcepts_Test.h"

#include <Fwc/fw_basic.h>
#include <Jc/StringJc.h>
#include <BlockHeap/BlockHeapJc_admin.h>

//METHOD_C int32 main_TestAllConcepts_Test(/*static*/ StringJc_Y* args, ThCxt* _thCxt);


BlockHeapJc blockHeap;
    

void main()
{
  STACKTRC_ENTRY("main");
  //os_initLib();
  { struct LogMessageFW_t* log = null;
    MemC memHeap = alloc_MemC(0x400000);
    init_ObjectJc(&blockHeap.base.object, sizeof(blockHeap), 0);
    ctorO_BlockHeapJc(&blockHeap.base.object, memHeap, 0x400, 0x7C);
    setCurrent_BlockHeapJc(&blockHeap, _thCxt);
    //init-pass
    //setRunMode_BlockHeapJc(&blockHeap, log, 2001, 2002, 2003);
    
    main_SY_TestAllConcepts_Test(null, _thCxt);
  }
  STACKTRC_LEAVE;

}



void setRunModeAll_BlockHeapJc(ThCxt* _thCxt)
{ setRunMode_BlockHeapJc(&blockHeap, null, 2000);

}
