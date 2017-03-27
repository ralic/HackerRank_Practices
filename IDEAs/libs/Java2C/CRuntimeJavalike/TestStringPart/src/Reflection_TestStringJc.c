
/*This file is generated from Header2Reflection. */
#define protected public  //only active inside this compilation unit
#define private public    //  to enable the access to all elements.
#include "Jc/ReflectionJc.h"
#include "Reflection_TestStringJc.h"
#include "../../Jc/DateJc.h"

const struct Reflection_Fields_DateValuesJc_t
{ ObjectArrayJc head;
  FieldJc data[2];
} reflection_Fields_DateValuesJc =
{ CONST_ObjectArrayJc(FieldJc, 2, OBJTYPE_FieldJc, null, &reflection_Fields_DateValuesJc)
, {
    { "time_nsec"
    , 0 //nrofArrayElements
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((DateValuesJc*)(0x1000))->time_nsec) - (int32)(DateValuesJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_DateValuesJc
    }
  , { "time_sec"
    , 0 //nrofArrayElements
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((DateValuesJc*)(0x1000))->time_sec) - (int32)(DateValuesJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_DateValuesJc
    }
} };


const ClassJc reflection_DateValuesJc =
{ CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_DateValuesJc, null)
, "DateValuesJc"
, 0
, sizeof(DateValuesJc)
, (FieldJcArray const*)&reflection_Fields_DateValuesJc  //attributes and associations
, null  //method
, null  //superclass
, null  //interfaces
, 0 
};



extern const ClassJc reflection_OS_TimeStamp;
const struct Reflection_Fields_DateJc_s_t
{ ObjectArrayJc head;
  FieldJc data[1];
} reflection_Fields_DateJc_s =
{ CONST_ObjectArrayJc(FieldJc, 1, OBJTYPE_FieldJc, null, &reflection_Fields_DateJc_s)
, {
    { "val"
    , 0 //nrofArrayElements
    , &reflection_OS_TimeStamp
    , 0 //bitModifiers
    , (int16)((int32)(&((DateJc_s*)(0x1000))->val) - (int32)(DateJc_s*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_DateJc_s
    }
} };


const ClassJc reflection_DateJc_s =
{ CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_DateJc_s, null)
, "DateJc_s"
, 0
, sizeof(DateJc_s)
, (FieldJcArray const*)&reflection_Fields_DateJc_s  //attributes and associations
, null  //method
, null  //superclass
, null  //interfaces
, 0 
};



extern const ClassJc reflection_StringJc;
const struct Reflection_Fields_SimpleDateFormatJc_s_t
{ ObjectArrayJc head;
  FieldJc data[2];
} reflection_Fields_SimpleDateFormatJc_s =
{ CONST_ObjectArrayJc(FieldJc, 2, OBJTYPE_FieldJc, null, &reflection_Fields_SimpleDateFormatJc_s)
, {
    { "sFormat"
    , 0 //nrofArrayElements
    , &reflection_StringJc
    , 0 //bitModifiers
    , (int16)((int32)(&((SimpleDateFormatJc_s*)(0x1000))->sFormat) - (int32)(SimpleDateFormatJc_s*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_SimpleDateFormatJc_s
    }
  , { "timeZoneAdjustHours"
    , 0 //nrofArrayElements
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((SimpleDateFormatJc_s*)(0x1000))->timeZoneAdjustHours) - (int32)(SimpleDateFormatJc_s*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_SimpleDateFormatJc_s
    }
} };


const ClassJc reflection_SimpleDateFormatJc_s =
{ CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_SimpleDateFormatJc_s, null)
, "SimpleDateFormatJc_s"
, 0
, sizeof(SimpleDateFormatJc_s)
, (FieldJcArray const*)&reflection_Fields_SimpleDateFormatJc_s  //attributes and associations
, null  //method
, null  //superclass
, null  //interfaces
, 0 
};



const struct Reflection_Fields_TextFieldPositionJc_s_t
{ ObjectArrayJc head;
  FieldJc data[1];
} reflection_Fields_TextFieldPositionJc_s =
{ CONST_ObjectArrayJc(FieldJc, 1, OBJTYPE_FieldJc, null, &reflection_Fields_TextFieldPositionJc_s)
, {
    { "x"
    , 0 //nrofArrayElements
    , REFLECTION_int
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((TextFieldPositionJc_s*)(0x1000))->x) - (int32)(TextFieldPositionJc_s*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_TextFieldPositionJc_s
    }
} };


const ClassJc reflection_TextFieldPositionJc_s =
{ CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_TextFieldPositionJc_s, null)
, "TextFieldPositionJc_s"
, 0
, sizeof(TextFieldPositionJc_s)
, (FieldJcArray const*)&reflection_Fields_TextFieldPositionJc_s  //attributes and associations
, null  //method
, null  //superclass
, null  //interfaces
, 0 
};



#include "../../OSAL/inc/os_time.h"

const struct Reflection_Fields_OS_TimeStamp_t
{ ObjectArrayJc head;
  FieldJc data[2];
} reflection_Fields_OS_TimeStamp =
{ CONST_ObjectArrayJc(FieldJc, 2, OBJTYPE_FieldJc, null, &reflection_Fields_OS_TimeStamp)
, {
    { "time_sec"
    , 0 //nrofArrayElements
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((OS_TimeStamp*)(0x1000))->time_sec) - (int32)(OS_TimeStamp*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_OS_TimeStamp
    }
  , { "time_nsec"
    , 0 //nrofArrayElements
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((OS_TimeStamp*)(0x1000))->time_nsec) - (int32)(OS_TimeStamp*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_OS_TimeStamp
    }
} };


const ClassJc reflection_OS_TimeStamp =
{ CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_OS_TimeStamp, null)
, "OS_TimeStamp"
, 0
, sizeof(OS_TimeStamp)
, (FieldJcArray const*)&reflection_Fields_OS_TimeStamp  //attributes and associations
, null  //method
, null  //superclass
, null  //interfaces
, 0 
};



const struct Reflection_Fields_MinMaxTime_Fwc_t
{ ObjectArrayJc head;
  FieldJc data[8];
} reflection_Fields_MinMaxTime_Fwc =
{ CONST_ObjectArrayJc(FieldJc, 8, OBJTYPE_FieldJc, null, &reflection_Fields_MinMaxTime_Fwc)
, {
    { "minCyclTime"
    , 0 //nrofArrayElements
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((MinMaxTime_Fwc*)(0x1000))->minCyclTime) - (int32)(MinMaxTime_Fwc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_MinMaxTime_Fwc
    }
  , { "midCyclTime"
    , 0 //nrofArrayElements
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((MinMaxTime_Fwc*)(0x1000))->midCyclTime) - (int32)(MinMaxTime_Fwc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_MinMaxTime_Fwc
    }
  , { "maxCyclTime"
    , 0 //nrofArrayElements
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((MinMaxTime_Fwc*)(0x1000))->maxCyclTime) - (int32)(MinMaxTime_Fwc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_MinMaxTime_Fwc
    }
  , { "minCalcTime"
    , 0 //nrofArrayElements
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((MinMaxTime_Fwc*)(0x1000))->minCalcTime) - (int32)(MinMaxTime_Fwc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_MinMaxTime_Fwc
    }
  , { "midCalcTime"
    , 0 //nrofArrayElements
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((MinMaxTime_Fwc*)(0x1000))->midCalcTime) - (int32)(MinMaxTime_Fwc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_MinMaxTime_Fwc
    }
  , { "maxCalcTime"
    , 0 //nrofArrayElements
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((MinMaxTime_Fwc*)(0x1000))->maxCalcTime) - (int32)(MinMaxTime_Fwc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_MinMaxTime_Fwc
    }
  , { "_lastTime"
    , 0 //nrofArrayElements
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((MinMaxTime_Fwc*)(0x1000))->_lastTime) - (int32)(MinMaxTime_Fwc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_MinMaxTime_Fwc
    }
  , { "_startTime"
    , 0 //nrofArrayElements
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((MinMaxTime_Fwc*)(0x1000))->_startTime) - (int32)(MinMaxTime_Fwc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_MinMaxTime_Fwc
    }
} };


const ClassJc reflection_MinMaxTime_Fwc =
{ CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_MinMaxTime_Fwc, null)
, "MinMaxTime_Fwc"
, 0
, sizeof(MinMaxTime_Fwc)
, (FieldJcArray const*)&reflection_Fields_MinMaxTime_Fwc  //attributes and associations
, null  //method
, null  //superclass
, null  //interfaces
, 0 
};



#include "../../Jc/ListMapEntryJc.h"

extern const ClassJc reflection_ListMapEntryJc;
extern const ClassJc reflection_ListMapEntryJc;
extern const ClassJc reflection_ListMapEntryJc;
extern const ClassJc reflection_ObjectJc;
const struct Reflection_Fields_ListMapEntryJc_t
{ ObjectArrayJc head;
  FieldJc data[6];
} reflection_Fields_ListMapEntryJc =
{ CONST_ObjectArrayJc(FieldJc, 6, OBJTYPE_FieldJc, null, &reflection_Fields_ListMapEntryJc)
, {
    { "parent"
    , 0 //nrofArrayElements
    , &reflection_ListMapEntryJc
    , 0| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((ListMapEntryJc*)(0x1000))->parent) - (int32)(ListMapEntryJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_ListMapEntryJc
    }
  , { "previous"
    , 0 //nrofArrayElements
    , &reflection_ListMapEntryJc
    , 0| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((ListMapEntryJc*)(0x1000))->previous) - (int32)(ListMapEntryJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_ListMapEntryJc
    }
  , { "next"
    , 0 //nrofArrayElements
    , &reflection_ListMapEntryJc
    , 0| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((ListMapEntryJc*)(0x1000))->next) - (int32)(ListMapEntryJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_ListMapEntryJc
    }
  , { "xxxallocater"
    , 0 //nrofArrayElements
    , &reflection_ObjectJc
    , 0| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((ListMapEntryJc*)(0x1000))->xxxallocater) - (int32)(ListMapEntryJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_ListMapEntryJc
    }
  , { "key"
    , 0 //nrofArrayElements
    , REFLECTION_void
    , 0 //bitModifiers
    , (int16)((int32)(&((ListMapEntryJc*)(0x1000))->key) - (int32)(ListMapEntryJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_ListMapEntryJc
    }
  , { "element"
    , 0 //nrofArrayElements
    , REFLECTION_void
    , 0 //bitModifiers
    , (int16)((int32)(&((ListMapEntryJc*)(0x1000))->element) - (int32)(ListMapEntryJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_ListMapEntryJc
    }
} };


const ClassJc reflection_ListMapEntryJc =
{ CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_ListMapEntryJc, null)
, "ListMapEntryJc"
, 0
, sizeof(ListMapEntryJc)
, (FieldJcArray const*)&reflection_Fields_ListMapEntryJc  //attributes and associations
, null  //method
, null  //superclass
, null  //interfaces
, 0 
};



#include "../../Jc/ObjectJc.h"

const struct Reflection_Fields_MtblHeadJc_t
{ ObjectArrayJc head;
  FieldJc data[2];
} reflection_Fields_MtblHeadJc =
{ CONST_ObjectArrayJc(FieldJc, 2, OBJTYPE_FieldJc, null, &reflection_Fields_MtblHeadJc)
, {
    { "sign"
    , 0 //nrofArrayElements
    , REFLECTION_char
    , (1<<kBitPrimitiv_Modifier_reflectJc)| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((MtblHeadJc*)(0x1000))->sign) - (int32)(MtblHeadJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_MtblHeadJc
    }
  , { "sizeTable"
    , 0 //nrofArrayElements
    , REFLECTION_int
    , (4<<kBitPrimitiv_Modifier_reflectJc)| mReference_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((MtblHeadJc*)(0x1000))->sizeTable) - (int32)(MtblHeadJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_MtblHeadJc
    }
} };


const ClassJc reflection_MtblHeadJc =
{ CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_MtblHeadJc, null)
, "MtblHeadJc"
, 0
, sizeof(MtblHeadJc)
, (FieldJcArray const*)&reflection_Fields_MtblHeadJc  //attributes and associations
, null  //method
, null  //superclass
, null  //interfaces
, 0 
};



extern const ClassJc reflection_ObjectJc;
const struct Reflection_Fields_ObjectArrayJc_t
{ ObjectArrayJc head;
  FieldJc data[4];
} reflection_Fields_ObjectArrayJc =
{ CONST_ObjectArrayJc(FieldJc, 4, OBJTYPE_FieldJc, null, &reflection_Fields_ObjectArrayJc)
, {
    { "object"
    , 0 //nrofArrayElements
    , &reflection_ObjectJc
    , 0 //bitModifiers
    , (int16)((int32)(&((ObjectArrayJc*)(0x1000))->object) - (int32)(ObjectArrayJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_ObjectArrayJc
    }
  , { "length"
    , 0 //nrofArrayElements
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((ObjectArrayJc*)(0x1000))->length) - (int32)(ObjectArrayJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_ObjectArrayJc
    }
  , { "sizeElement"
    , 0 //nrofArrayElements
    , REFLECTION_int16
    , (2<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((ObjectArrayJc*)(0x1000))->sizeElement) - (int32)(ObjectArrayJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_ObjectArrayJc
    }
  , { "mode"
    , 0 //nrofArrayElements
    , REFLECTION_int16
    , (2<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((ObjectArrayJc*)(0x1000))->mode) - (int32)(ObjectArrayJc*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_ObjectArrayJc
    }
} };


const ClassJc reflection_ObjectArrayJc =
{ CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_ObjectArrayJc, null)
, "ObjectArrayJc"
, 0
, sizeof(ObjectArrayJc)
, (FieldJcArray const*)&reflection_Fields_ObjectArrayJc  //attributes and associations
, null  //method
, null  //superclass
, null  //interfaces
, 0  |mObjectJc_Modifier_reflectJc
};



extern const ClassJc reflection_ObjectArrayJc;
const struct Reflection_Fields_int8ARRAY_t
{ ObjectArrayJc head;
  FieldJc data[2];
} reflection_Fields_int8ARRAY =
{ CONST_ObjectArrayJc(FieldJc, 2, OBJTYPE_FieldJc, null, &reflection_Fields_int8ARRAY)
, {
    { "head"
    , 0 //nrofArrayElements
    , &reflection_ObjectArrayJc
    , 0 //bitModifiers
    , (int16)((int32)(&((int8ARRAY*)(0x1000))->head) - (int32)(int8ARRAY*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_int8ARRAY
    }
  , { "data"
    , 256 //nrofArrayElements
    , REFLECTION_int8
    , (1<<kBitPrimitiv_Modifier_reflectJc) |mStaticArray_Modifier_reflectJc //bitModifiers
    , (int16)((int32)(&((int8ARRAY*)(0x1000))->data) - (int32)(int8ARRAY*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_int8ARRAY
    }
} };


const ClassJc reflection_int8ARRAY =
{ CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_int8ARRAY, null)
, "int8ARRAY"
, 0
, sizeof(int8ARRAY)
, (FieldJcArray const*)&reflection_Fields_int8ARRAY  //attributes and associations
, null  //method
, null  //superclass
, null  //interfaces
, 0 
};


