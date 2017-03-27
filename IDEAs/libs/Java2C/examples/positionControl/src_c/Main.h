#ifndef __Main_h__
#define __Main_h__
#include <stdio.h>

struct LogMessageFW_t;

void initBlockHeap();

extern struct BlockHeapJc_t* pBlockHeapModul1;

void setRunModeBlockHeap(struct LogMessageFW_t* log);

void garbageCollection();

#endif //__Main_h__
