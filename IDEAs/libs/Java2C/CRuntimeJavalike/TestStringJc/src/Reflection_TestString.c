
/*This file is generated from Header2Reflection. */
#define protected public  //only active inside this compilation unit
#define private public    //  to enable the access to all elements.
#include "Jc/ReflectionJc.h"
#include "Reflection_TestString.h"
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
extern const ClassJc reflection_StringJc;
const struct Reflection_Fields_DateJc_s_t
{ ObjectArrayJc head;
  FieldJc data[3];
} reflection_Fields_DateJc_s =
{ CONST_ObjectArrayJc(FieldJc, 3, OBJTYPE_FieldJc, null, &reflection_Fields_DateJc_s)
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


