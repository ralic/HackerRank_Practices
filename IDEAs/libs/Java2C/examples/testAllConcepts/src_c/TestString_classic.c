#include "TestString_classic_Test.h"
#include <Fwc/fw_basic.h>
#include <string.h>
#include <stdio.h>
#include "TestString_Test.h"

#include <os_types_def.h>

int processString_classic(char* str, int lenStr)
{ return lenStr;
}

int test(){ return 5;}

int32 concatenate_TestString_classic_Test(int32 value, float fValue, ThCxt*_thCxt)
{
  char* str;
  int lenStr;
  int len1;
  int pos = 0;
  
  char buffer[100];
  STACKTRC_TENTRY("concatenateString_classic");
  /**Simple preparation of a String with numbers. */
  lenStr = sprintf(buffer, "test %i, float %f", value, fValue);
  str = buffer;
  lenStr = strlen(buffer);
  processString_classic(str, lenStr);
  
  /**Second preparation using the same buffer. */
  len1 = sprintf(buffer, "second %i", value);
  processString_classic(str, lenStr);
  
  /**Append any other String to the buffer. */
  strcpy(buffer + len1, "third");
  processString_classic(str, lenStr);

  /**Search a char in the buffer: */
  { char const* sPos = strchr(str, '.');
    if(sPos != null){
      pos = sPos - str;
    }
  }
  /**Search a String in the buffer: */
  { char const* sPos = strstr(str, "third");
    if(sPos != 0){
      pos += sPos - str;
    }
  }


  STACKTRC_LEAVE; return pos;
}
