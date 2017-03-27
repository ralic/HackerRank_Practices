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
 * 4) But the LPGL is not appropriate for a whole software product,
 *    if this source is only a part of them. It means, the user
 *    must publish this part of source,
 *    but don't need to publish the whole source of the own product.
 * 5) You can study and modify (improve) this source
 *    for own using or for redistribution, but you have to license the
 *    modified sources likewise under this LGPL Lesser General Public License.
 *    You mustn't delete this Copyright/Copyleft inscription in this source file.
 *
 * @author Hartmut Schorrig: hartmut.schorrig@vishia.de, www.vishia.org
 * @version 0.93 2011-01-05  (year-month-day)
 *******************************************************************************/ 
package org.vishia.java2C.test;

public class AnyClass
{
  int x1 = 5;
  
  /**The ref is needed to test {@link #returnRefSimle(int)} for concatenations of references.
   */
  ImplIfc ref;

  
  /**Check whether a static instance of this class is translated to C.
   * 
   */
  //final static AnyClass instance1  = new AnyClass(null);
  
  AnyClass(ImplIfc ref)
  { this.ref = ref;
  }
  
  /**Any final method to call. */
  final int addValue(int value)
  { x1 += value;
    return x1;
  }
  
  /**Any override-able method to call. */
  int addValueOverrideable(int value)
  { x1 += value;
    return x1;
  }
  
  /**Returns itself (this)
   * @java2c=return-this.
   * @param value
   * @return
   */
  final AnyClass returnThis(int value)
  { x1 += value;
    return this;
  }
  
  /**Returns itself (this), but the method is able to override. Therefore a dynamic call should be used.
   * @java2c=return-this.
   */
  AnyClass returnThisOverrideable(int value)
  { x1 += value;
    return this;
  }
  
  /**Returns any other instance.
   * @param value
   * @return
   */
  final ImplIfc returnRef()
  { return ref;
  }

  /**Returns any other instance, but this method is able to override, non final.
   * @param value
   * @return
   */
  ImplIfc returnRefOverrideable()
  { return ref;
  }

}
