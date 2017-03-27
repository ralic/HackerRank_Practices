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
 * @content
 *
 * @author Hartmut Schorrig, Pinzberg, Germany
 *
 * @version 0.83
 *
 * @content Implementation of ObjectJc
 *
 * @author Hartmut Schorrig
 * @version 0.82
 * list of changes:
 * 2010-02-05: Hartmut corr: new_ObjectArrayJc(...) was an old implementation, used and improved.
 * 2009-06-23: Hartmut corr: setSizeAndIdent_ObjectJc(), first try to use small size mask.
 * 2008-04-22: Hartmut adap:The elements idSemaphore and idWaitingThread are combined to idSyncHandles.
 * 2006-05-00: Hartmut creation
 *
 ****************************************************************************/

#include <stdlib.h>     //malloc
#include <string.h>     //memset
#include "Jc/ObjectJc.h"  //:NOTE: after stdlib.h because malloc maybe defined.
//#include "StringJc.h"
#include "Jc/ReflectionJc.h"
#include "Fwc/fw_Exception.h"
#include "Fwc/fw_ThreadContext.h"
//#include "os_wrapper.h"

/********************************************************************************************/
//Object

bool castOk_ObjectJc;

char const sign_Mtbl_ObjectJc[] = "sign_Mtbl_ObjectJc";
char const signEnd_Mtbl_ObjectJc[] = "signEnd_Mtbl_ObjectJc";


ObjectJc* ctorM_ObjectJc(MemC mem) //ObjectJc* ythis)
{ ObjectJc* ythis = PTR_MemC(mem, ObjectJc);
  init0p_MemC(ythis, sizeof(ObjectJc));  //note: ctorc don't initialze with 0.
  //obsolete: ythis->offsetToHeapBlock = offsetBlock_MemC(mem);  //NOTE 0 if no offset is given.
  ctorc_ObjectJc(ythis);
  return ythis;
}

//old version, clears ythis-area.
void ctor_Object_Jc(ObjectJc* ythis)
{
  init0p_MemC(ythis, sizeof(ObjectJc));  //note: ctorc don't initialze with 0.
  ctorc_ObjectJc(ythis);
}



void ctorc_ObjectJc(ObjectJc* ythis)
{ 
  if(ythis->ownAddress != ythis)  //if the ownAddress is already set, it is initalized!
  { ythis->ownAddress = ythis;
    ythis->idSyncHandles = kNoSyncHandles_ObjectJc;
    ythis->offsetToStartAddr = 0;
    //
    //NOTE: the both next values cannot be setted from focus ObjectJc,
    //      they should be setted outside the ctor behind them from focus of the derivated class.
    //      use setReflection_ObjectJc(..) therefore.
    ythis->objectIdentSize = 0;
    ythis->reflectionClass = null;
  }
  else
  { //it is already initialized.
  }
}



/*
ObjectJc* ctorO_ii_ObjectJc(ObjectJc* othis, const int size, const int32 typeInstanceIdent, ThCxt* _thCxt)
{ int32 objectIdentSize;
  checkConsistenceOrInit_ObjectJc(othis, size, null, _thCxt);
  //set the size of the Object, regarding the typsInstanceIdent limits for size:
  if( (typeInstanceIdent & mSizeBits_objectIdentSize_ObjectJc) == kIsSmallSize_objectIdentSize_ObjectJc)
  { if(size > mSizeSmall_objectIdentSize_ObjectJc)
    { THROW_s0(IndexOutOfBoundsException, "too large", size);
    }
    objectIdentSize = (typeInstanceIdent & ~mSizeSmall_objectIdentSize_ObjectJc) | size;
  }
  else if( (typeInstanceIdent & mSizeBits_objectIdentSize_ObjectJc) == kIsMediumSize_objectIdentSize_ObjectJc)
  { if(size > mSizeMedium_objectIdentSize_ObjectJc)
    { THROW_s0(IndexOutOfBoundsException, "too large", size);
    }
    objectIdentSize = (typeInstanceIdent & ~mSizeMedium_objectIdentSize_ObjectJc) | size;
  }
  else if(typeInstanceIdent & mIsLargeSize_objectIdentSize_ObjectJc)
  { if(size > mSizeLarge_objectIdentSize_ObjectJc)
    { THROW_s0(IndexOutOfBoundsException, "too large", size);
    }
    objectIdentSize = (typeInstanceIdent & ~mSizeLarge_objectIdentSize_ObjectJc) | size;
  }
  else
  { objectIdentSize = 0;
    THROW_s0(IndexOutOfBoundsException, "undefined size", typeInstanceIdent);
  }
  othis->objectIdentSize = objectIdentSize;
  STACKTRC_LEAVE; return othis;
}
*/


void checkConsistence_ObjectJc(ObjectJc* ythis, int size, struct ClassJc_t const* clazzReflection, ThCxt* _thCxt)
{ STACKTRC_TENTRY("checkConsistence_ObjectJc");
  { //it is initialized, check it
    int sizeObj = getSizeInfo_ObjectJc(ythis);
    if(ythis->ownAddress == null)
    { //not initialized
      ctorc_ObjectJc(ythis);
      setReflection_ObjectJc(ythis, clazzReflection, 0);
      setSizeAndIdent_ObjectJc(ythis, size, 0);
    }
    if(ythis->ownAddress != ythis) THROW_s0(IllegalArgumentException, "faut ownAddress", (int)ythis->ownAddress);
    if(sizeObj == 0)
    { //not initialized
      sizeObj = size;
    }
    else if(sizeObj < size) THROW_s0(IllegalArgumentException, "faut size", sizeObj);
    
    if(clazzReflection != null)
    { if(ythis->reflectionClass == null)
      { ythis->reflectionClass = clazzReflection;
      }
      else
      { //TODO test Reflection, it can be a derived class.
      }
    }
  }
  STACKTRC_LEAVE;
}



void checkConsistenceOrInit_ObjectJc(ObjectJc* ythis, int size, struct ClassJc_t const* clazzReflection, ThCxt* _thCxt)
{ STACKTRC_TENTRY("checkConsistence_ObjectJc");
  if(ythis->ownAddress == null)
  { //not initialized
    ctorc_ObjectJc(ythis);
    setReflection_ObjectJc(ythis, clazzReflection, 0);
    setSizeAndIdent_ObjectJc(ythis, size, 0);
  }
  else
  { checkConsistence_ObjectJc(ythis, size, clazzReflection,_thCxt);
  }
  STACKTRC_LEAVE;
}






ObjectJc* clone_ObjectJc_F(ObjectJc const* ythis, MemC buffer)
{
  return null;
}

bool equals_ObjectJc_F(ObjectJc* ythis, ObjectJc* cmp, ThCxt* _thCxt)
{
  return false;
}

void finalize_ObjectJc_F(ObjectJc* ythis, ThCxt* _thCxt)
{
}

int32 hashCode_ObjectJc_F(ObjectJc const* ythis, ThCxt* _thCxt)
{
  return 0;
}


/************************************************************************/



/**calculates the index insided the jumptable of a dynamic call,
 * regarding the type of reference and the type of the instance.
 * @param reflectionObj The reflection of the referenced instance.
 * @param reflectionRef The reflection of the reference. It should be the same as reflectionObj
 *        or it should be found as a superclass or interface of the reflectionObj.
 *        This param is only used with its pointer value, no access to the referenced memory location will be done.
 * @return The index of the part of jumptable of the reference inside the jump table of the object.
 *         * It is 0, if reflectionObj == reflectionRef, it means the reference is from the same type as the Object.
 *         * It is mIdxMtbl_ObjectJc if reflectionObj is null. This case is possible if the Object has no reflection infos.
 *           If the index with this value is used as an index of jumptable, an exception occurs.
 *           But if it is not used, it is a valid case, especially if no dynamic linked call occurs.
 */
int getIdxMtbl_ClassJc(ClassJc const* reflectionObj, ClassJc const* reflectionRef)
{ int idxMtbl = -1;
  ClassOffset_idxMtblJcARRAY const* reflectionSuper;
  STACKTRC_ENTRY("getIdxMtbl_ClassJc");
  if(reflectionObj == null)
  { //if no reflection is used, it is able in C++ environment or if no dynamic linked methods are used.
    idxMtbl = -1; //mIdxMtbl_ObjectJc;  //causes an error if it will be used!
  }
  else
  { if(reflectionRef == null)  //if no reflection is prescribed:
    { idxMtbl = 0;  //returns the Mtbl_ObjectJc
    }
    else if(reflectionRef == reflectionObj)
    { idxMtbl = 0;  //returns the whole Mtbl for the type.
    }
    else
    { ClassOffset_idxMtblJcARRAY const* reflectionIfc = 
        (ClassOffset_idxMtblJcARRAY const*)reflectionObj->interfaces;
      if( reflectionIfc != null)
      { int idxIfc;
        for(idxIfc = 0; idxMtbl < 0 && idxIfc < reflectionIfc->head.length; idxIfc++)
        { ClassOffset_idxMtblJc const* reflectionChild;
          reflectionChild = &reflectionIfc->data[idxIfc];
          if(reflectionChild->clazz == reflectionRef)
          { idxMtbl = reflectionChild->idxMtbl;
          }
        }
      }
      else
      { idxMtbl = -1;
      }
    }
    if(idxMtbl < 0 && (reflectionSuper = reflectionObj->superClasses) != null)
    { int idxSuper = 0;
      for(idxSuper = 0; idxMtbl < 0 && idxSuper < reflectionSuper->head.length; idxSuper++)
      { ClassOffset_idxMtblJc const* reflectionChild;
        reflectionChild = &reflectionSuper->data[idxSuper];
        if(reflectionChild->clazz == reflectionRef)
        { idxMtbl = reflectionChild->idxMtbl;
        }
        else
        { //Recursive call because deeper inheritance:
          idxMtbl = getIdxMtbl_ClassJc(reflectionChild->clazz, reflectionRef);
        }
      }
    }
    { //search in superclasses and there interfaces
      //old if only 1 superclass:
      //idxMtbl = getIdxMtbl_ClassJc(reflectionObj->superClass, reflectionRef);
    }

  }
  STACKTRC_LEAVE; return(idxMtbl);
}






Mtbl_ObjectJc const* xxxgetMtbl_ObjectJc(ObjectJc const* ythis, ClassJc const* type)
{ ClassJc const* clazz;
  Mtbl_ObjectJc const* mtbl;
  int idxMtbl;
  STACKTRC_ENTRY("getMtbl_ObjectJc");
  if(ythis == null) THROW_s0(NullPointerException,"this is null", 0);
  if(ythis != ythis->ownAddress) THROW_s0(RuntimeException, "ownAddress fault", (int)ythis->ownAddress);
  clazz = ythis->reflectionClass;
  //if(clazz->object.reflectionClass != &reflection_ClassJc) THROW_s0(RuntimeException, "reflection fault", (int)ythis->ownAddress);
  //may be a derivated type!!! TODO.
  //if(clazz != type) THROW_s0(RuntimeException, "fault type", (int)ythis->ownAddress);
  if(clazz == null)
  { mtbl = null;  //no reflection is given, no mtbl, but no error
  }
  else
  {
    idxMtbl = getIdxMtbl_ClassJc(clazz, type);
    if(idxMtbl < 0) THROW_s0(RuntimeException, "fault type", (int)ythis->ownAddress);
    ASSERT(clazz->mtbl != null);
    //sign is the first reference of ObjectJc-methodtable, indices the first reference of the interface or superclass.
    mtbl = (Mtbl_ObjectJc const*)( &(&clazz->mtbl->sign)[idxMtbl]);  
    //mtbl = (struct MT_TypeJc_t const*)( ((MT_void_Method_void*)(clazz->mtbl)) + idxMtbl);
  }
  STACKTRC_LEAVE; return mtbl;
}



MtblHeadJc const* getMtbl_ObjectJc(ObjectJc const* ythis, char const* sign)
{ MtblHeadJc const* head = null; //nullpointer-return possible
  ClassJc const* reflection;
  STACKTRC_ENTRY("getMtbl_ObjectJc");
  ASSERT(ythis->ownAddress == ythis);
  reflection = ythis->reflectionClass;
  if( reflection != null) { 
    ASSERT(reflection->object.reflectionClass == &reflection_ClassJc);
    head = ythis->reflectionClass->mtbl;  
    if(head != null)//nullpointer exception outside possible.
    {
      while(  head->sign != sign 
           && head->sign != signEnd_Mtbl_ObjectJc 
           )
      { int sizeTable = (int)head->sizeTable;
        ASSERT(sizeTable >0 && sizeTable < (302 * sizeof(void*)));  //no more as 300 virtual methods per class, detect false content and step forward!
        //The next part of method table is found after the current.
        head = (MtblHeadJc const*)( (MemUnit*)head + sizeTable );
      }
      if(head->sign == signEnd_Mtbl_ObjectJc){
        THROW_s0(ClassCastException, "baseclass not found", (int)sign);
        head = null;  //The null pointer may be tested outside, or it should cause an exception outside if it is unexpected.
  } } }
  STACKTRC_LEAVE; return head;
}




bool instanceof_ObjectJc(ObjectJc const* ythis, struct ClassJc_t const* reflection)
{ 
  int idxMtbl = getIdxMtbl_ClassJc(ythis->reflectionClass, reflection);
  return idxMtbl >=0;
}



StringJc toString_ObjectJc_F(ObjectJc* ythis, ThCxt* _thCxt)
{ //StringBuffer* ss = StringBuffer::new_();
  //ss->append("Object at @").append(Integer::toHexString((int)(this)));
  StringJc ss = CONST_StringJc("No information", 14); //only constant
  return ss;   //return by value, that is 2 words (8 Byte).
}


/*J2C: dynamic call variant of the override-able method: */
StringJc toString_ObjectJc(ObjectJc* ithis, ThCxt* _thCxt)
{ Mtbl_ObjectJc const* mtbl = (Mtbl_ObjectJc const*)getMtbl_ObjectJc(ithis, sign_Mtbl_ObjectJc);
  return mtbl->toString(ithis, _thCxt);
}


/********************************************************************************************/
//Object__Array


void* get_i_ObjectArrayJc(ObjectArrayJc const* ythis, int idx)
{
  if(idx < 0 || idx > ythis->length){ return null; } //IndexOutOfBoundaryException
  else
  {
    int offset = idx * ythis->sizeElement;
    if(true) //ythis->mode == kDirect_ObjectArrayJc)
    {
      return ((MemUnit*)(ythis + 1)) + offset;
    }
    else
    { return 0;
    }
  }
}


int size_Object__ArrayJc(const ObjectArrayJc* ythis)
{
  return ythis->length;
}


/** Gets the number of bytes of the whole array calculated from the informations inside the head.
  */
METHOD_C int getNrOfBytes_ObjectArrayJc(ObjectArrayJc const* ythis)
{ return sizeof(ObjectArrayJc) + ythis->length * ythis->sizeElement;
}


void_Y* ctorO_AYJc(ObjectJc* othis, int nBytesPerElement, int nSize)
{ void_Y* ythis = (void_Y*)(othis);
  if(ythis != null)
  { ctorc_ObjectJc(&ythis->head.object);
    ythis->head.object.objectIdentSize |= mArray_objectIdentSize_ObjectJc;
    ythis->head.length = nSize;
    ythis->head.sizeElement = nBytesPerElement;
    ythis->head.mode = 0; //kDirect_ObjectArrayJc;
  }
  return ythis;
}



ObjectArrayJc* ctorc_ObjectArrayJc(ObjectArrayJc* ythis, int nSize, int nBytesPerElement, struct ClassJc_t const* reflection, int32 typeInstanceIdent)
{ //ObjectArrayJc* arrayHead = (ObjectArrayJc*)(ythis);  //the universal type of ARRAYJc
  STACKTRC_ENTRY("ctorc_ObjectArrayJc");
  if(ythis != null)
  { int32 sizeArray;
    int32 sizeInfoObject;
    ctorc_ObjectJc(&ythis->object);
    sizeArray = nSize * nBytesPerElement + sizeof(ObjectArrayJc);
    sizeInfoObject = getSizeInfo_ObjectJc(&ythis->object);  //may be setted or it should be 0
      if(sizeInfoObject > 0 && sizeInfoObject < sizeArray)
      { THROW_s0(IllegalArgumentException, "size mismatch", sizeArray);
      }    
      if( (typeInstanceIdent & mSizeBits_objectIdentSize_ObjectJc) == kIsSmallSize_objectIdentSize_ObjectJc)
      { if(sizeArray > mSizeSmall_objectIdentSize_ObjectJc)
        { THROW_s0(IndexOutOfBoundsException, "too large", sizeArray);
        }
        typeInstanceIdent &= ~mSizeSmall_objectIdentSize_ObjectJc;
      }
      else if( (typeInstanceIdent & mSizeBits_objectIdentSize_ObjectJc) == kIsMediumSize_objectIdentSize_ObjectJc)
      { if(sizeArray > mSizeMedium_objectIdentSize_ObjectJc)
        { THROW_s0(IndexOutOfBoundsException, "too large", sizeArray);
        }
        typeInstanceIdent &= ~mSizeMedium_objectIdentSize_ObjectJc;
      }
      else if(typeInstanceIdent & mIsLargeSize_objectIdentSize_ObjectJc)
      { if(sizeArray > mSizeLarge_objectIdentSize_ObjectJc)
        { THROW_s0(IndexOutOfBoundsException, "too large", sizeArray);
        }
        typeInstanceIdent &= ~mSizeLarge_objectIdentSize_ObjectJc;
      }
      else
      { THROW_s0(IndexOutOfBoundsException, "undefined size", typeInstanceIdent);
      }
      typeInstanceIdent |= sizeArray;
      //it is always 1-dimension.
      typeInstanceIdent = (typeInstanceIdent & ~mArray_objectIdentSize_ObjectJc) | mArray_objectIdentSize_ObjectJc;
    }
    setReflection_ObjectJc(&ythis->object, reflection, typeInstanceIdent);

    ythis->length = nSize;
    ythis->sizeElement = nBytesPerElement;
    ythis->mode = 0; //kDirect_ObjectArrayJc;
    { MemUnit* data = (MemUnit*)(ythis) + sizeof(ObjectArrayJc);  //The data are assigned always immediately after the head. This is the law.
      memset(data, 0, nSize * nBytesPerElement);
    }
  STACKTRC_LEAVE;
  return ythis;
}


ObjectArrayJc* ctorO_ObjectArrayJc(ObjectJc* othis, int size, int nBytesPerElement, struct ClassJc_t const* reflection, int32 typeInstanceIdent)
{ ObjectArrayJc* ythis = (ObjectArrayJc*)othis;
//  ctorc_ObjectArrayJc(ythis, size, nBytesPerElement, reflection, typeInstanceIdent);
  STACKTRC_ENTRY("ctorO_ObjectArrayJc");
  if(ythis != null)
  { int32 sizeArray;
    int32 sizeInfoObject;
    int32 objectIdentSize;
    //ctorc_ObjectJc(&ythis->object);
    sizeArray = size * nBytesPerElement + sizeof(ObjectArrayJc);
    sizeInfoObject = getSizeInfo_ObjectJc(&ythis->object);
    if(sizeArray > sizeInfoObject) THROW_s0(IllegalArgumentException, "less sizeObject, require=", sizeArray);
    objectIdentSize = (othis->objectIdentSize & ~mArray_objectIdentSize_ObjectJc) | mArray_objectIdentSize_ObjectJc;
    setReflection_ObjectJc(&ythis->object, reflection, objectIdentSize);

    ythis->length = size;
    ythis->sizeElement = nBytesPerElement;
    ythis->mode = 0; //kDirect_ObjectArrayJc;
    { MemUnit* data = (MemUnit*)(ythis) + sizeof(ObjectArrayJc);  //The data are assigned always immediately after the head. This is the law.
      memset(data, 0, size * nBytesPerElement);
    }
  }
  STACKTRC_LEAVE; return ythis;
}



METHOD_C ObjectArrayJc* new_ObjectArrayJc(int SIZE, int BYTESELEMENT, struct ClassJc_t const* REFLECTION, int32 typeInstanceIdent)
{ int nrOfBytes = sizeof(ObjectArrayJc) + SIZE * BYTESELEMENT;
  ObjectArrayJc* nthis;
  ObjectJc* othis;
  STACKTRC_ENTRY("new_ObjectArrayJc");
  othis = alloc_ObjectJc( sizeof(ObjectArrayJc) + (SIZE) * (BYTESELEMENT), mIsLargeSize_objectIdentSize_ObjectJc, _thCxt);
  nthis = ctorO_ObjectArrayJc(othis, SIZE, BYTESELEMENT, REFLECTION, typeInstanceIdent);
  STACKTRC_LEAVE;
  return nthis;
  //return &address->object;

}


MemUnit* getAddrSizeof_ObjectArrayJc(ObjectArrayJc* ythis, int32* size)
{ MemUnit* addr;
  int dimensions = (ythis->mode & mDimension_ObjectArrayJc) >> kBitDimension_ObjectArrayJc;
  if(dimensions == 0){ dimensions = 1; }
  addr = ((MemUnit*)ythis) + sizeof(ObjectArrayJc) + (dimensions-1)*sizeof(int32);
  if(size !=null){
    int idim;
    int* lengthptr = (int*)(&ythis->length);
    int32 nrofBytes = (ythis->mode & mPointered_ObjectArrayJc) ? sizeof(void*) : ythis->sizeElement;
    for(idim = 0; idim < dimensions; ++idim){
      int lengthDim = *lengthptr++;
      nrofBytes *= lengthDim;
    }
    *size = nrofBytes;
  }
  return addr;
}



MemC buildFromArrayX_MemC(ObjectArrayJc* objArray)
{
  MemC mem;
  int32 nrofBytes;
  void* addr = getAddrSizeof_ObjectArrayJc(objArray, &nrofBytes);
  set_MemC(mem, addr, nrofBytes);
  return mem;
}



int32ARRAY* ctor_int32ARRAY(int32ARRAY* ythis, int nrOfElements)
{ if(ythis != null)
  { ythis->head.length = nrOfElements;
    ythis->head.sizeElement = sizeof(int32);
    ythis->head.mode = 0; //kDirect_ObjectArrayJc;
    memset(ythis->data, 0, nrOfElements * ythis->head.sizeElement);
  }
  return ythis;
}




#ifndef USE_BlockHeapJc

/**If no block heap is used, this routine returns {0, null} because there is no rest. */
MemC getRestBlock_ObjectJc(ObjectJc* ythis, int size, ThCxt* _thCxt)
{ MemC ret = NULL_MemC();
  return ret;
}


#endif





