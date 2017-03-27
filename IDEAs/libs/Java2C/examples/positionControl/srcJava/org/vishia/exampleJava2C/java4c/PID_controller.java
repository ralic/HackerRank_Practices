package org.vishia.exampleJava2C.java4c;


/**This class provides a simple PID-Controller generateable to C with 16-bit arithmetic 
 * and a 32-bit-integral value. 
 * A fix point 16-bit arithmetic should be used, because in the embedded world 
 * with low-cost-C-controllers without floating point arithmetic unit 
 * but fast real-time requirements it is a appropriate decision. 
 * This example of 16-bit-PID-controller should show,
 * that such relationships are also programmable and testable in the Java world.
 * 
 * @author JcHartmut
 *
 */
public class PID_controller implements CtrlBase
{
  /**The integral value of the controller. */
  private int intgVal = 0;
  
  /**The last value to build the difference. */
  private short lastForDiff;
  
  /**The I amplification, unit per sample step. Range -1.00000 to 0.99998 coded with 0x8000...0x7FFF
   * At example, the value 0x4000 means, in one cycle time the integral value 
   *   will be increase quater the half input value. It is a integral time of 2 * cycle-time.
   * At example, the value 0x1 means, the integral time is 32768 * cycle-time.
   * The cycle time itself is unconsidered. The kI constants have to be calculated as ratio
   *   32768 * cycle-time / integral-time.
   */
  short kI = 0x01000;
  
  /**The P amplification. Range -32.768 to 32.767 coded with 0x8000...0x7FFF. 
   * At example the value 1.0 is 0x0400. 
   */
  private short kP = 0x080;
  
  /**The D amplification. Range -32.768 to 32.767 coded with 0x8000...0x7FFF.  
   * The value 1.0 is 0x0400. 
   * TODO not used yet.
   */
  private short kD = 0;
  
  /**The reference to the input value. @java2c=simpleArray.  */
  private short[] xPID;
  
  /**The output value. @java2c=simpleArray. */
  private short[] yPID;
  
  /**the step-method, called one-time per cycle-time to calculate the next value.
   * The integral value is incremented by input. The result value is calculated with PI-algorithm.
   * 
   * @param input Input value. The range is related to the users unit system. 
   *        It may be, the user have a normalized unit system between -1.0 ... 1.0, 
   *        but with overflow in range -4.0..4.0. Than the value 1.0 is coded with 0x2000.
   * @return
   */ 
  public short calculate(short input)
  {
    /*The integral value after this step.
     * NOTE: the 16-bit-multiplication should produce a 32 bit result.
     */
    int intgValNew = intgVal + (input * kI);
    int out = (intgValNew >> 16) + ((kP * input)>>6); 
    /*The out may be overdriven, but it can be limited because it is caluclated in 32 bit.
     */
    if(out > 0x7FFF)
    { out = 0x7FFF;
    }
    else if(out < -0x8000)
    { out = -0x8000;
    }
    
    /*Test if the integral value is overdriven. */
    if( (  (intgVal > 0 && input > 0)
        || (intgVal < 0 && input < 0) 
        )
      &&((intgValNew ^ intgVal ) & 0x80000000 ) == 0x80000000
      )
    { /* The sign of new value is changed, but the absolute integral value is increased,
       * it means, we have an overflow of integration.
       * It may be happen only if the control loop is open.
       */
      throw new RuntimeException("integral value overdriven");
    }
    else
    { intgVal = intgValNew; //the integral value is valid.
    }
    
    return (short)(out);  
  }
  
  /**Sets the integral value to a predefined value.
   * This method is typical for PID-control, if the necessary output value of the controller
   * is known.
   * @param setValue in the same range as the return value if calling step().
   */
  public void setIntg(short setValue)
  { intgVal = ((int)(setValue)) << 16;
  }
  
  
  /**Gets the value of the internal integrator for displayment. The value is shown in float with position after decimal point
   * as low value. The 1.0-unit is equal to 1 mV output value while controlling.
   * @return
   */ 
  public float getIntg()  { return intgVal/65536.0F; }

  /**Connect the Input of PID controller to a short variable.
   * @param valueTEST Reference to any short variable. @pjava2c=simpleArray.
   */
  void connectInput(short[] ref)
  { xPID = ref;
  }

  /**provides the reference to the output for wiring to any connection.
   * @return reference to output. @java2c=simpleArray.
   */
  short[] provideOutput()
  { return yPID;
  }
  
  @Override
  public void init()
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void parametrize()
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void reset()
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void step(int time)
  {
    yPID[0] = calculate(xPID[0]);
    
  }
  
}
