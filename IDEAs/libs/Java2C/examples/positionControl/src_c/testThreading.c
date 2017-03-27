/****************************************************************************
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
 * @author Jchartmut from www.vishia.org
 * @version 2008-03-23  (year-month-day)
 * list of changes:
 * 2008-03-23: JcHartmut creation. It is the sunday of eastern.
 *
 ****************************************************************************/

/**This source is written to test and show some threading concepts
 */

//#include "testSomeDynamicLinkedMethodConcepts.h"
#include "Jc/ObjectJc.h"
#include "Fwc/fw_Exception.h"
#include <stdio.h>
/*
void testWaitNotify()
{
  STACKTRC_ENTRY("testWaitNotify");
  { ObjectJc* obj = ctor_ObjectJc(alloc_MemC(sizeof(ObjectJc)));
    printf("\nwait");
    wait_ObjectJc(obj, 1000, _thCxt);
    printf(".");
    wait_ObjectJc(obj, 1000, _thCxt);
    printf(".");
  }
  STACKTRC_LEAVE;
}
*/
