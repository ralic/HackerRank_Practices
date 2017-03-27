
/*This file is generated from Header2Reflection. */
#define protected public  //only active inside this compilation unit
#define private public    //  to enable the access to all elements.
#include <Jc/ReflectionJc.h>
#include <stddef.h>
#include "reflection_Posctrl.h"
#include "../../src_c/Main.h"

#include "../../src_c/testenv.h"

#include "../../src_c/MainEmulation.h"

const struct Reflection_Fields_Hardware_t
{ ObjectArrayJc head;
  FieldJc data[4];
} reflection_Fields_Hardware =
{ CONST_ObjectArrayJc(FieldJc, 4, OBJTYPE_FieldJc, null, &reflection_Fields_Hardware)
, {
    { "way1"
    , 0   //no Array, no Bitfield
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((Hardware*)(0x1000))->way1) -(int32)(Hardware*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_Hardware
    }
  , { "way2"
    , 0   //no Array, no Bitfield
    , REFLECTION_int32
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((Hardware*)(0x1000))->way2) -(int32)(Hardware*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_Hardware
    }
  , { "motor1"
    , 0   //no Array, no Bitfield
    , REFLECTION_int16
    , (2<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((Hardware*)(0x1000))->motor1) -(int32)(Hardware*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_Hardware
    }
  , { "motor2"
    , 0   //no Array, no Bitfield
    , REFLECTION_int16
    , (2<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((Hardware*)(0x1000))->motor2) -(int32)(Hardware*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_Hardware
    }
} };


const ClassJc reflection_Hardware =
{ CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_Hardware, &reflection_ClassJc)
, "Hardware"
, 0
, sizeof(Hardware)
, (FieldJcArray const*)&reflection_Fields_Hardware  //attributes and associations
, null  //method
, null  //superclass
, null  //interfaces
, 0 
};



extern const ClassJc reflection_ObjectJc;
extern const ClassJc reflection_Hardware;
const struct Reflection_Fields_MainEmulation_s_t
{ ObjectArrayJc head;
  FieldJc data[3];
} reflection_Fields_MainEmulation_s =
{ CONST_ObjectArrayJc(FieldJc, 3, OBJTYPE_FieldJc, null, &reflection_Fields_MainEmulation_s)
, {
    { "object"
    , 0   //no Array, no Bitfield
    , &reflection_ObjectJc
    , 0 //bitModifiers
    , (int16)((int32)(&((MainEmulation_s*)(0x1000))->object) -(int32)(MainEmulation_s*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_MainEmulation_s
    }
  , { "hardware"
    , 0   //no Array, no Bitfield
    , &reflection_Hardware
    , 0 //bitModifiers
    , (int16)((int32)(&((MainEmulation_s*)(0x1000))->hardware) -(int32)(MainEmulation_s*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_MainEmulation_s
    }
  , { "nAmplificationActuator"
    , 0   //no Array, no Bitfield
    , REFLECTION_float
    , (4<<kBitPrimitiv_Modifier_reflectJc) //bitModifiers
    , (int16)((int32)(&((MainEmulation_s*)(0x1000))->nAmplificationActuator) -(int32)(MainEmulation_s*)0x1000)
    , 0  //offsetToObjectifcBase
    , &reflection_MainEmulation_s
    }
} };


const ClassJc reflection_MainEmulation_s =
{ CONST_ObjectJc(OBJTYPE_ClassJc + sizeof(ClassJc), &reflection_MainEmulation_s, &reflection_ClassJc)
, "MainEmulation_s"
, 0
, sizeof(MainEmulation_s)
, (FieldJcArray const*)&reflection_Fields_MainEmulation_s  //attributes and associations
, null  //method
, null  //superclass
, null  //interfaces
, 0  |mObjectJc_Modifier_reflectJc
};


