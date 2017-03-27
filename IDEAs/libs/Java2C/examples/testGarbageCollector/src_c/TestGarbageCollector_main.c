#include "TestGarbageCollector_main.h"
#include "ExceptionJc.h"
#include "TestGarbageCollector.h"

#include "BlockHeapJc_admin.h"
#include "BlockHeapJc.h"



int testInsidegarbageCollection(int step)
{ STACKTRC_ENTRY("testInsidegarbageCollection");
  if(step == 5)
  {
    run1_TestGarbageCollector(theInstance_TestGarbageCollector.ref, _threadContext);
  }
  STACKTRC_LEAVE; return 0;
}

/**Should be a static variable because some thread may use it. */
struct BlockHeapJc_t* heap;

void main()
{
  STACKTRC_ENTRY("main");
  /**Initializing of heaps: */
  heap = ctor_BlockHeapJc(alloc_MemC(sizeof(BlockHeapJc)), alloc_MemC(0x4000), 0x400, 0x7C);
  /**Every thread should define its current (standard) heap. */
  setCurrent_BlockHeapJc(heap, _threadContext);
  /**Only after the next instruction the heap will be used. Elsewhere allocations will be done in C-standard-heap. */
  setRunMode_BlockHeapJc(heap);
  
  /**This is to set a callback method to test the garbage collector itself. */
  setTestMethod(testInsidegarbageCollection);

  /**Now call of the java static main routine. TODO: static routines are not full supported yet. The first parameter will be this, but it is not needed. */
  main_TestGarbageCollector(null, null_StringJc, _threadContext);

  /**After main, the program is finished. Now call garbage collection to test it. */
  while(true)
  { garbageCollection_BlockHeapJc(null, 0,0);
  }
  STACKTRC_LEAVE; 
}

