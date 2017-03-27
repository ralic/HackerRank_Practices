
/*This file is generated from Header2Reflection. */
#define protected public  //only active inside this compilation unit
#define private public    //  to enable the access to all elements.
#include <Jc/ReflectionJc.h>
#include <stddef.h>
#include "Reflection_BlockHeap.h"
#include "BlockHeap/BlockHeapJc_admin.h"

extern const ClassJc reflection_BlockHeapBlockJc_debugArray;
extern const ClassJc reflection_BlockHeapBlockJc;
extern const ClassJc reflection_BlockHeapBlockJc;
extern const ClassJc reflection_BlockHeapBlockJc;
extern const ClassJc reflection_BlockHeapBlockJc;
extern const ClassJc reflection_ListMapEntryJc;
extern const ClassJc reflection_ListMapEntryJc;
extern const ClassJc reflection_LogMessageFW_i;
const struct Reflection_Fields_BlockHeapJc_t
{ ObjectArrayJc head;
  FieldJc data[18];
} reflection_Fields_BlockHeapJc =
{ CONST_ObjectArrayJc(FieldJc, 18, OBJTYPE_FieldJc, null, &reflection_Fields_BlockHeapJc)
, {
    { "nextHeap"
    , 0   //no Array, no Bitfield
    , REFLECTION_void
    , 0| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((BlockHeapJc*)(0x1000))->nextHeap) -(int32)(BlockHeapJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapJc
    }
  , { "idxHeapCtrl"
    , 0   //no Array, no Bitfield
    , REFLECTION_int
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((BlockHeapJc*)(0x1000))->idxHeapCtrl) -(int32)(BlockHeapJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapJc
    }
  , { "zBlock"
    , 0   //no Array, no Bitfield
    , REFLECTION_int
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((BlockHeapJc*)(0x1000))->zBlock) -(int32)(BlockHeapJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapJc
    }
  , { "bytesSmallBlock"
    , 0   //no Array, no Bitfield
    , REFLECTION_int
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((BlockHeapJc*)(0x1000))->bytesSmallBlock) -(int32)(BlockHeapJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapJc
    }
  , { "mAdressSmallBlock_BlockHeap"
    , 0   //no Array, no Bitfield
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((BlockHeapJc*)(0x1000))->mAdressSmallBlock_BlockHeap) -(int32)(BlockHeapJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapJc
    }
  , { "heapBegin"
    , 0   //no Array, no Bitfield
    , &reflection_BlockHeapBlockJc_debugArray
    , 0| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((BlockHeapJc*)(0x1000))->heapBegin) -(int32)(BlockHeapJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapJc
    }
  , { "heapEnd"
    , 0   //no Array, no Bitfield
    , REFLECTION_void
    , (8<<kBitPrimitiv_Modifier_reflectJc)| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((BlockHeapJc*)(0x1000))->heapEnd) -(int32)(BlockHeapJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapJc
    }
  , { "firstFreeBlock"
    , 0   //no Array, no Bitfield
    , &reflection_BlockHeapBlockJc
    , 0| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((BlockHeapJc*)(0x1000))->firstFreeBlock) -(int32)(BlockHeapJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapJc
    }
  , { "firstFreeBlockSmall"
    , 0   //no Array, no Bitfield
    , &reflection_BlockHeapBlockJc
    , 0| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((BlockHeapJc*)(0x1000))->firstFreeBlockSmall) -(int32)(BlockHeapJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapJc
    }
  , { "firstFreeBlockMapEntry"
    , 0   //no Array, no Bitfield
    , &reflection_BlockHeapBlockJc
    , 0| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((BlockHeapJc*)(0x1000))->firstFreeBlockMapEntry) -(int32)(BlockHeapJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapJc
    }
  , { "lastFreeBlockMapEntry"
    , 0   //no Array, no Bitfield
    , &reflection_BlockHeapBlockJc
    , 0| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((BlockHeapJc*)(0x1000))->lastFreeBlockMapEntry) -(int32)(BlockHeapJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapJc
    }
  , { "firstMapEntry"
    , 0   //no Array, no Bitfield
    , &reflection_ListMapEntryJc
    , 0| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((BlockHeapJc*)(0x1000))->firstMapEntry) -(int32)(BlockHeapJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapJc
    }
  , { "lastMapEntry"
    , 0   //no Array, no Bitfield
    , &reflection_ListMapEntryJc
    , 0| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((BlockHeapJc*)(0x1000))->lastMapEntry) -(int32)(BlockHeapJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapJc
    }
  , { "bitInitMode"
    , 0   //no Array, no Bitfield
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((BlockHeapJc*)(0x1000))->bitInitMode) -(int32)(BlockHeapJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapJc
    }
  , { "ctCollosionAlloc"
    , 0   //no Array, no Bitfield
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((BlockHeapJc*)(0x1000))->ctCollosionAlloc) -(int32)(BlockHeapJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapJc
    }
  , { "ctCollosionFree"
    , 0   //no Array, no Bitfield
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((BlockHeapJc*)(0x1000))->ctCollosionFree) -(int32)(BlockHeapJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapJc
    }
  , { "log"
    , 0   //no Array, no Bitfield
    , &reflection_LogMessageFW_i
    , 0| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((BlockHeapJc*)(0x1000))->log) -(int32)(BlockHeapJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapJc
    }
  , { "kIdentMsgBase"
    , 0   //no Array, no Bitfield
    , REFLECTION_int
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((BlockHeapJc*)(0x1000))->kIdentMsgBase) -(int32)(BlockHeapJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapJc
    }
} };


const ClassJc reflection_BlockHeapJc =
{ CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_BlockHeapJc, &reflection_ClassJc)
, "BlockHeapJc"
, 0
, sizeof(BlockHeapJc)
, (FieldJcArray const*)&reflection_Fields_BlockHeapJc  //attributes and associations
, null  //method
, null  //superclass
, null  //interfaces
, 0 
};



#include "BlockHeap/BlockHeapJc.h"

#include "BlockHeap/BlockHeapJc_internal.h"

const struct Reflection_Fields_ObjectJcREFArray_t
{ ObjectArrayJc head;
  FieldJc data[1];
} reflection_Fields_ObjectJcREFArray =
{ CONST_ObjectArrayJc(FieldJc, 1, OBJTYPE_FieldJc, null, &reflection_Fields_ObjectJcREFArray)
, {
    { "data"
    , 10   //nrofArrayElements
    , REFLECTION_void
    , 0| mReference_Modifier_reflectJc |kStaticArray_Modifier_reflectJc|kEmbeddedContainer_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((ObjectJcREFArray*)(0x1000))->data) -(int32)(ObjectJcREFArray*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_ObjectJcREFArray
    }
} };


const ClassJc reflection_ObjectJcREFArray =
{ CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_ObjectJcREFArray, &reflection_ClassJc)
, "ObjectJcREFArray"
, 0
, sizeof(ObjectJcREFArray)
, (FieldJcArray const*)&reflection_Fields_ObjectJcREFArray  //attributes and associations
, null  //method
, null  //superclass
, null  //interfaces
, 0 
};



extern const ClassJc reflection_BlockHeapBlockJc;
extern const ClassJc reflection_ObjectJcREFArray;
extern const ClassJc reflection_ObjectJc;
const struct Reflection_Fields_BlockHeapBlockJc_t
{ ObjectArrayJc head;
  FieldJc data[8];
} reflection_Fields_BlockHeapBlockJc =
{ CONST_ObjectArrayJc(FieldJc, 8, OBJTYPE_FieldJc, null, &reflection_Fields_BlockHeapBlockJc)
, {
    { "typeOrMaxRef"
    , 0   //no Array, no Bitfield
    , REFLECTION_uint16
    , (2<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((BlockHeapBlockJc*)(0x1000))->typeOrMaxRef) -(int32)(BlockHeapBlockJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapBlockJc
    }
  , { "heapidxAndMaxref"
    , 0   //no Array, no Bitfield
    , REFLECTION_uint16
    , (2<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((BlockHeapBlockJc*)(0x1000))->heapidxAndMaxref) -(int32)(BlockHeapBlockJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapBlockJc
    }
  , { "nextBlock"
    , 0   //no Array, no Bitfield
    , &reflection_BlockHeapBlockJc
    , 0| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((BlockHeapBlockJc*)(0x1000))->nextBlock) -(int32)(BlockHeapBlockJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapBlockJc
    }
  , { "heap"
    , 0   //no Array, no Bitfield
    , REFLECTION_void
    , 0| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((BlockHeapBlockJc*)(0x1000))->heap) -(int32)(BlockHeapBlockJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapBlockJc
    }
  , { "backRefs"
    , 0   //no Array, no Bitfield
    , &reflection_ObjectJcREFArray
    , 0| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((BlockHeapBlockJc*)(0x1000))->backRefs) -(int32)(BlockHeapBlockJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapBlockJc
    }
  , { "obj"
    , 0   //no Array, no Bitfield
    , &reflection_ObjectJc
    , 0| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((BlockHeapBlockJc*)(0x1000))->obj) -(int32)(BlockHeapBlockJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapBlockJc
    }
  , { "sCreaterInfo"
    , 0   //no Array, no Bitfield
    , REFLECTION_char
    , (1<<kBitPrimitiv_Modifier_reflectJc)| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((BlockHeapBlockJc*)(0x1000))->sCreaterInfo) -(int32)(BlockHeapBlockJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapBlockJc
    }
  , { "timeCreation"
    , 0   //no Array, no Bitfield
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((BlockHeapBlockJc*)(0x1000))->timeCreation) -(int32)(BlockHeapBlockJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapBlockJc
    }
} };


const ClassJc reflection_BlockHeapBlockJc =
{ CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_BlockHeapBlockJc, &reflection_ClassJc)
, "BlockHeapBlockJc"
, 0
, sizeof(BlockHeapBlockJc)
, (FieldJcArray const*)&reflection_Fields_BlockHeapBlockJc  //attributes and associations
, null  //method
, null  //superclass
, null  //interfaces
, 0 
};



extern const ClassJc reflection_BlockHeapBlockJc;
const struct Reflection_Fields_BlockHeapWholeBlockJc_t
{ ObjectArrayJc head;
  FieldJc data[2];
} reflection_Fields_BlockHeapWholeBlockJc =
{ CONST_ObjectArrayJc(FieldJc, 2, OBJTYPE_FieldJc, null, &reflection_Fields_BlockHeapWholeBlockJc)
, {
    { "blockCtrl"
    , 0   //no Array, no Bitfield
    , &reflection_BlockHeapBlockJc
    , 0 //bitModifiers
    , (int16)((int32)(&((BlockHeapWholeBlockJc*)(0x1000))->blockCtrl) -(int32)(BlockHeapWholeBlockJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapWholeBlockJc
    }
  , { "rest"
    , 0   //nrofArrayElements
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((BlockHeapWholeBlockJc*)(0x1000))->rest) -(int32)(BlockHeapWholeBlockJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapWholeBlockJc
    }
} };


const ClassJc reflection_BlockHeapWholeBlockJc =
{ CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_BlockHeapWholeBlockJc, &reflection_ClassJc)
, "BlockHeapWholeBlockJc"
, 0
, sizeof(BlockHeapWholeBlockJc)
, (FieldJcArray const*)&reflection_Fields_BlockHeapWholeBlockJc  //attributes and associations
, null  //method
, null  //superclass
, null  //interfaces
, 0 
};



extern const ClassJc reflection_BlockHeapWholeBlockJc;
const struct Reflection_Fields_BlockHeapBlockJc_debugArray_t
{ ObjectArrayJc head;
  FieldJc data[1];
} reflection_Fields_BlockHeapBlockJc_debugArray =
{ CONST_ObjectArrayJc(FieldJc, 1, OBJTYPE_FieldJc, null, &reflection_Fields_BlockHeapBlockJc_debugArray)
, {
    { "block"
    , 100   //nrofArrayElements
    , &reflection_BlockHeapWholeBlockJc
    , 0 |kStaticArray_Modifier_reflectJc|kEmbeddedContainer_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((BlockHeapBlockJc_debugArray*)(0x1000))->block) -(int32)(BlockHeapBlockJc_debugArray*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_BlockHeapBlockJc_debugArray
    }
} };


const ClassJc reflection_BlockHeapBlockJc_debugArray =
{ CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_BlockHeapBlockJc_debugArray, &reflection_ClassJc)
, "BlockHeapBlockJc_debugArray"
, 0
, sizeof(BlockHeapBlockJc_debugArray)
, (FieldJcArray const*)&reflection_Fields_BlockHeapBlockJc_debugArray  //attributes and associations
, null  //method
, null  //superclass
, null  //interfaces
, 0 
};



extern const ClassJc reflection_OS_TimeStamp;
extern const ClassJc reflection_OS_TimeStamp;
extern const ClassJc reflection_LogMessageFW_i;
const struct Reflection_Fields_GarbageCollectorJc_t
{ ObjectArrayJc head;
  FieldJc data[17];
} reflection_Fields_GarbageCollectorJc =
{ CONST_ObjectArrayJc(FieldJc, 17, OBJTYPE_FieldJc, null, &reflection_Fields_GarbageCollectorJc)
, {
    { "idxBlock"
    , 0   //no Array, no Bitfield
    , REFLECTION_int
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((GarbageCollectorJc*)(0x1000))->idxBlock) -(int32)(GarbageCollectorJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_GarbageCollectorJc
    }
  , { "idxReportBuffer"
    , 0   //no Array, no Bitfield
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((GarbageCollectorJc*)(0x1000))->idxReportBuffer) -(int32)(GarbageCollectorJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_GarbageCollectorJc
    }
  , { "reportBuffer"
    , 1000   //nrofArrayElements
    , REFLECTION_char
    , (1<<kBitPrimitiv_Modifier_reflectJc) |kStaticArray_Modifier_reflectJc|kEmbeddedContainer_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((GarbageCollectorJc*)(0x1000))->reportBuffer) -(int32)(GarbageCollectorJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_GarbageCollectorJc
    }
  , { "testedHeap"
    , 0   //no Array, no Bitfield
    , REFLECTION_void
    , 0| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((GarbageCollectorJc*)(0x1000))->testedHeap) -(int32)(GarbageCollectorJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_GarbageCollectorJc
    }
  , { "testMethod"
    , 0   //no Array, no Bitfield
    , REFLECTION_void
    , 0 //bitModifiers
    , (int16)((int32)(&((GarbageCollectorJc*)(0x1000))->testMethod) -(int32)(GarbageCollectorJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_GarbageCollectorJc
    }
  , { "countStep"
    , 0   //no Array, no Bitfield
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((GarbageCollectorJc*)(0x1000))->countStep) -(int32)(GarbageCollectorJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_GarbageCollectorJc
    }
  , { "bAbortTest"
    , 0   //no Array, no Bitfield
    , REFLECTION_int8
    , (1<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((GarbageCollectorJc*)(0x1000))->bAbortTest) -(int32)(GarbageCollectorJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_GarbageCollectorJc
    }
  , { "bAbortTestBecauseUserCall"
    , 0   //no Array, no Bitfield
    , REFLECTION_int8
    , (1<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((GarbageCollectorJc*)(0x1000))->bAbortTestBecauseUserCall) -(int32)(GarbageCollectorJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_GarbageCollectorJc
    }
  , { "dummy"
    , 2   //nrofArrayElements
    , REFLECTION_int8
    , (1<<kBitPrimitiv_Modifier_reflectJc) |kStaticArray_Modifier_reflectJc|kEmbeddedContainer_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((GarbageCollectorJc*)(0x1000))->dummy) -(int32)(GarbageCollectorJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_GarbageCollectorJc
    }
  , { "ctFreeBlocks"
    , 0   //no Array, no Bitfield
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((GarbageCollectorJc*)(0x1000))->ctFreeBlocks) -(int32)(GarbageCollectorJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_GarbageCollectorJc
    }
  , { "ctNonGcBlocks"
    , 0   //no Array, no Bitfield
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((GarbageCollectorJc*)(0x1000))->ctNonGcBlocks) -(int32)(GarbageCollectorJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_GarbageCollectorJc
    }
  , { "ctFreedBlocks"
    , 0   //no Array, no Bitfield
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((GarbageCollectorJc*)(0x1000))->ctFreedBlocks) -(int32)(GarbageCollectorJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_GarbageCollectorJc
    }
  , { "ctUsedBlocks"
    , 0   //no Array, no Bitfield
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((GarbageCollectorJc*)(0x1000))->ctUsedBlocks) -(int32)(GarbageCollectorJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_GarbageCollectorJc
    }
  , { "startTime"
    , 0   //no Array, no Bitfield
    , &reflection_OS_TimeStamp
    , 0 //bitModifiers
    , (int16)((int32)(&((GarbageCollectorJc*)(0x1000))->startTime) -(int32)(GarbageCollectorJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_GarbageCollectorJc
    }
  , { "endTime"
    , 0   //no Array, no Bitfield
    , &reflection_OS_TimeStamp
    , 0 //bitModifiers
    , (int16)((int32)(&((GarbageCollectorJc*)(0x1000))->endTime) -(int32)(GarbageCollectorJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_GarbageCollectorJc
    }
  , { "log"
    , 0   //no Array, no Bitfield
    , &reflection_LogMessageFW_i
    , 0| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((GarbageCollectorJc*)(0x1000))->log) -(int32)(GarbageCollectorJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_GarbageCollectorJc
    }
  , { "kIdentMsgBase"
    , 0   //no Array, no Bitfield
    , REFLECTION_int
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((GarbageCollectorJc*)(0x1000))->kIdentMsgBase) -(int32)(GarbageCollectorJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_GarbageCollectorJc
    }
} };


const ClassJc reflection_GarbageCollectorJc =
{ CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_GarbageCollectorJc, &reflection_ClassJc)
, "GarbageCollectorJc"
, 0
, sizeof(GarbageCollectorJc)
, (FieldJcArray const*)&reflection_Fields_GarbageCollectorJc  //attributes and associations
, null  //method
, null  //superclass
, null  //interfaces
, 0 
};


