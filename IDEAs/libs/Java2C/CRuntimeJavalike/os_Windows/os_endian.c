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
 * @version 0.92
 * @content Implementation of access for network payload data for the Intel-type-Processor
 * list of changes:
 * 2009-11-24: JcHartmut created as new idea.
 *
 ****************************************************************************/
#include <OSAL/inc/os_endian.h>

int64 getInt64BigEndian(int64BigEndian* addr)
{ int32 loBig,hiBig, lo, hi;
  int64 ret;
  //NOTE: do only 1 access to memory.
  hiBig = addr->hiBigEndian__;
  loBig = addr->loBigEndian__;
  hi = ((hiBig <<24) & 0xff000000) | ((hiBig <<8) & 0x00ff0000) |((hiBig >>8) & 0x0000ff00) |((hiBig >>24) & 0x0000ff);
  lo = ((loBig <<24) & 0xff000000) | ((loBig <<8) & 0x00ff0000) |((loBig >>8) & 0x0000ff00) |((loBig >>24) & 0x0000ff);
  ret = ((int64)hi)<<32 | lo;
  return ret;
}

int32 getInt32BigEndian(int32BigEndian* addr)
{ int32 loBig;
  int32 ret;
  //NOTE: do only 1 access to memory.
  loBig = addr->loBigEndian__;
  ret = ((loBig <<24) & 0xff000000) | ((loBig <<8) & 0x00ff0000) |((loBig >>8) & 0x0000ff00) |((loBig >>24) & 0x0000ff);
  return ret;
}

int16 getInt16BigEndian(int16BigEndian* addr)
{ int16 loBig;
  int16 ret;
  //NOTE: do only 1 access to memory.
  loBig = addr->loBigEndian__;
  ret = ((loBig <<8) & 0xff00) |((loBig >>8) & 0x00ff);
  return ret;
}

float getFloatBigEndian(floatBigEndian* addr)
{ int32 loBig;
  int32 lo;
  float ret;
  //NOTE: do only 1 access to memory.
  loBig = addr->floatBigEndian__;
  lo = ((loBig <<24) & 0xff000000) | ((loBig <<8) & 0x00ff0000) |((loBig >>8) & 0x0000ff00) |((loBig >>24) & 0x0000ff);
  ret = *(float*)&lo;
  return ret;
}

double getDoubleBigEndian(doubleBigEndian* addr)
{ int32 loBig,hiBig, lo, hi;
  int64 value;
  double ret;
  //NOTE: do only 1 access to memory.
  hiBig = addr->hiBigEndian__;
  loBig = addr->loBigEndian__;
  hi = ((hiBig <<24) & 0xff000000) | ((hiBig <<8) & 0x00ff0000) |((hiBig >>8) & 0x0000ff00) |((hiBig >>24) & 0x0000ff);
  lo = ((loBig <<24) & 0xff000000) | ((loBig <<8) & 0x00ff0000) |((loBig >>8) & 0x0000ff00) |((loBig >>24) & 0x0000ff);
  value = ((int64)hi)<<32 | lo;
  ret = *(double*)&value;
  return ret;
}

void* getPtrBigEndian(ptrBigEndian* addr)
{ int32 loBig, hiBig;
  struct ptr_t{int32 lo; int32 hi; }imgPtr = {0, 0};
  int sizePtr = sizeof(void*); //may be depending on several compiler options.
  int ix;
  void* ret;
  //NOTE: do only 1 access to memory.
  hiBig = ((int32*)addr)[0];  //reads 4 byte.
  if(sizePtr >4){
    loBig = ((int32*)addr)[1]; //reads next 4 byte, part of them are for the pointer.
  }
  for(ix=0; ix<4 && ix<sizePtr; ix++){
    imgPtr.lo <<=8;
    imgPtr.lo |= hiBig & 0xff;
    hiBig >>=8;
  }
  while(ix < sizePtr){  //continue with next bytes.
    imgPtr.hi <<=8;
    imgPtr.hi |= loBig & 0xff;
    loBig >>=8;
  }
  ret = *(void**)&imgPtr; //interprete it as pointer-image in stack.
  return ret;
}


int64 setInt64BigEndian(int64BigEndian* addr, int64 value)
{ int32 loBig,hiBig, lo, hi;
  hi = (int32)(value >> 32);
  lo = (int32)value;
  hiBig = ((hi <<24) & 0xff000000) | ((hi <<8) & 0x00ff0000) |((hi >>8) & 0x0000ff00) |((hi >>24) & 0x0000ff);
  loBig = ((lo <<24) & 0xff000000) | ((lo <<8) & 0x00ff0000) |((lo >>8) & 0x0000ff00) |((lo >>24) & 0x0000ff);
  //NOTE: do only 1 access to memory.
  addr->hiBigEndian__ = hiBig;
  addr->loBigEndian__ = loBig;
  return value;
}

int32 setInt32BigEndian(int32BigEndian* addr, int32 value)
{ int32 loBig;
  loBig = ((value <<24) & 0xff000000) | ((value <<8) & 0x00ff0000) |((value >>8) & 0x0000ff00) |((value >>24) & 0x0000ff);
  //NOTE: do only 1 access to memory.
  addr->loBigEndian__ = loBig;
  return value;
}

int16 setInt16BigEndian(int16BigEndian* addr, int16 value)
{ int16 loBig;
  loBig = ((value <<8) & 0xff00) |((value >>8) & 0x00ff);

  //NOTE: do only 1 access to memory.
  addr->loBigEndian__ = loBig;
  return value;
}

float setFloatBigEndian(floatBigEndian* addr, float value)
{ int32 img, imgBig;
  img = *(int32*)&value;
  imgBig = ((img <<24) & 0xff000000) | ((img <<8) & 0x00ff0000) |((img >>8) & 0x0000ff00) |((img >>24) & 0x0000ff);
  //NOTE: do only 1 access to memory.
  addr->floatBigEndian__ = imgBig;
  return value;
}

double setDoubleBigEndian(doubleBigEndian* addr, double value)
{ int32 loBig,hiBig, lo, hi;
  int64 img;
  img = *(int64*)&value;
  hi = (int32)(img >> 32);
  lo = (int32)img;
  hiBig = ((hi <<24) & 0xff000000) | ((hi <<8) & 0x00ff0000) |((hi >>8) & 0x0000ff00) |((hi >>24) & 0x0000ff);
  loBig = ((lo <<24) & 0xff000000) | ((lo <<8) & 0x00ff0000) |((lo >>8) & 0x0000ff00) |((lo >>24) & 0x0000ff);
  //NOTE: do only 1 access to memory.
  addr->hiBigEndian__ = hiBig;
  addr->loBigEndian__ = loBig;
  return value;
}


void* setPtrBigEndian(ptrBigEndian* addr, void const* value)
{ int sizePtr = sizeof(void*); //may be depending on several compiler options.
  switch(sizePtr){
    case 2: //it may be a near ptr
    { int16 ptrImg = *(int16*)&value;
      setInt16BigEndian((int16BigEndian*)addr, ptrImg);
    } break;
    case 4: //it may be a normal far ptr
    { int32 ptrImg = *(int32*)&value;
      setInt32BigEndian((int32BigEndian*)addr, ptrImg);
    } break;
    case 6: //it may be a far ptr
    { int32 ptrImgLo = *(int32*)&value;
      int16 ptrImgHi = ((int16*)&value)[2];
      setInt16BigEndian((int16BigEndian*)addr, ptrImgHi);
      setInt32BigEndian((int32BigEndian*)(((int16BigEndian*)addr)+1), ptrImgLo);  //set to next 16 bit position, but set 32 bit.
    } break;
    case 8: //it may be a far ptr
    { int32 ptrImgLo = *(int32*)&value;
      int32 ptrImgHi = ((int32*)&value)[1];
      setInt32BigEndian((int32BigEndian*)addr, ptrImgHi);
      setInt32BigEndian(((int32BigEndian*)addr)+1, ptrImgLo);  //set to next 32 bit position, set 32 bit.
    } break;
  }
  return (void*) value;
}


