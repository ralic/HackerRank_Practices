package org.vishia.exampleJava2C.java4c;

import org.vishia.msgDispatch.LogMessage;
import org.vishia.util.StringFormatter;


/**This class represents a set value generator for a movement.
 * The idea is, a new target point may be given calling {@link setTarget(short)}
 * and the set value for a way controller is generate in a linear movement from actual position
 * to the new target point. If the target is reached, a possible next target point is taken
 * or the movement stands idle on the target.
 * 
 * It is possible and reasonable for a good control to smooth the motion. 
 * It would be also reasonable to set a velocity and a time for stand idle on target.
 * 
 * In the second stage of Java2C-example only one target is stored. But a new construction is used
 * to test the garbage collection.
 * 
 * The third stage will be implemented with a LinkedList to store a higher number of targets, 
 * but new should be also used to create a new target instance.
 * 
 * @author JcHartmut
 *
 */
public class SetValueGenerator implements SetValueGenerator_ifc
{
  /**The message interface.*/
  final LogMessage msg;
  
  private final OamVariables oamVariables;
  
  /**The last and next set value. Initial it is the position 1 meter.*/
  protected short value = 10000;
  
  /**The increment of value in actual generating. Initial it is 1 mm per step. 
   * It may be 1 m/s if the cycle time is 1 millisecond.  
   */
  protected short dvalue = 10;
  
  /**The counter to count the time to stand still. */
  private int countStandStill;
  
  private boolean isWaitingForTarget = false;
  
  /**State Movement to the target. */
  private static final int kToTarget_State = 1;
  
  /**State stand still on target. */
  private static final int kStandStill_State = 2;
  
  /**State stand still on target. */
  private static final int kWaitForNewTarget_State = 3;
  
  /**State of the movement. Use state variables xxx_State. */
  short state = kWaitForNewTarget_State;
  
  private final StringBuffer bufferFormatter = new StringBuffer(400); 
  
  private final StringFormatter sFormatter = new StringFormatter(bufferFormatter);
  
  /**A class contains the target values. */
  protected static class Target
  { /**The target value, the target point in 0.1mm steps from 0 .. about 3 m . */
    short target;
    
    /**The increment per cycle step in 0.1 mm as relation to the velocity.*/
    short velocity;
    
    /**Time to stand idle on target in steps. */
    short timeStandIdleOnTarget = 10;
    
    SetValueGenerator testGC;
    
    static class OnlyTest
    {
      
      static class InnerTest
      {
        int a;
      }  
    }
    
    
    Target()
    { 
    }
    
    public void finalize()
    { 
      //System.out.println("finalize Target");
      MainController.singleton.msg.sendMsg(ProcessMessageIdents.kLifeCycle, "finalize Target instance");
    }
  }
  
  /**The instances for actual and next target point. */
  //protected SetValueGenerator.Target actTarget, nextTarget;
  protected Target actTarget, nextTarget;
  
  
  /**Constructor needs to know a message interface:
   * 
   */
  SetValueGenerator(LogMessage msg, OamVariables oamVariables)
  { this.msg = msg;
    this.oamVariables = oamVariables;
    //sFormatter.
  }
  
  /**generates the next reference value. 
   * This method is to be called in the cycle of controlling.
   * @return the next set value for controller
   */
  @Override public short step()
  { short newState;
    /**@java2c=dynamic-call. */
  	LogMessage msg = this.msg;
  	do
    { newState = -1;
      switch(state)
      { case kToTarget_State:
        { //assert actTarget != null;
          /*Difference to the target point. */
        	int dToTarget = actTarget.target - value;
          
          if(  //TODO it does not work yet in Java2C: Math.abs(dToTarget) < Math.abs(dvalue))
              dvalue > 0 ? dToTarget < dvalue : dToTarget > dvalue)  //incremental: diff to target less than dvalue...
            
          { /*The last step: add only the diff to the target. */
            dvalue = (short)dToTarget;
            newState = kStandStill_State;
            countStandStill = 10; //it is the onentry-action in standStill-state
            msg.sendMsg(ProcessMessageIdents.endPositionReached, "end position reached", value,0,0,0);
            //Example for String processing:
            Target testTarget;
            testTarget = actTarget;
          }
          value+= dvalue;
        } break;
        case kStandStill_State:
        { if(--countStandStill == 0)
          { newState = kWaitForNewTarget_State;
          }
        } break;
        case kWaitForNewTarget_State:
        { if(nextTarget != null)
          { /**The current nextTarget is used as actTarget, the nextTarget is null then. */
            actTarget = nextTarget;
            nextTarget = null;
            dvalue = (short)(actTarget.target > value ? 10 : -10);
            
            if(value > actTarget.target)
            { //dvalue = (short)-dvalue; //drive down.
            }
            if(isWaitingForTarget){
            	msg.sendMsg(-ProcessMessageIdents.waitForTargetPosition, "wait for target %d", value,0,0,0);
              isWaitingForTarget = false;
            }
            msg.sendMsg(ProcessMessageIdents.newTargetPosition, "new target %d = %3.2e", actTarget.target, actTarget.target * 1.0F);
            //TODO: it doesnot work yet in Java2C msg.sendMsg(ProcessMessageIdents.kNewTarget, "new target", actTarget.target);
            newState = kToTarget_State;  
          }
          else
          { countStandStill -=1; //count for debug.
            if(!isWaitingForTarget){
            	msg.sendMsg(ProcessMessageIdents.waitForTargetPosition, "wait for target %d", value,0,0,0);
              isWaitingForTarget = true;
            }
          }
        } break;
      }
      if(newState > 0)
      { //a state switch:
        state = newState;
      }
    }while(newState >=0);  //continue on stateSwitch, run with new state
    oamVariables.stateSetValueGen = state;
    
    oamVariables.targetWay = actTarget == null ? 0: actTarget.target;
  	oamVariables.wway = value;
  	return value;
  }

  /**stores a next target point. This method may be called in a slower thread.
   * If there is no space for storing the demand, the value isn't be stored.
   * The method should be called later once more.
   * 
   * @param targetValue the target point value.
   * @return true if it is stored, false if there is no space to storing. 
   */ 
  @Override public boolean setTarget(short targetValue)
  { if(nextTarget != null) return false;
    else
    { /**In C mentality a switching buffer of fix instances or such will be used.
       * But in Java the creation of a new instance is more suggesting.
       * It is more simple because no switching buffer administration is needed! 
       * Here the garbage collection should be demonstrate.
       */
      SetValueGenerator.Target newTarget = new SetValueGenerator.Target();
      newTarget.target = targetValue;
      nextTarget = newTarget;
      msg.sendMsg(ProcessMessageIdents.setNewTargetPosition, "new Target position: %d", targetValue);
      return true;
    }
  }
  
  /**returns true if the next target is required.
   * This is a polling variant of provision a next target.
   * In a later revision of this software a callback interface should be used.
   */
  boolean requiresNextTarget()
  { return nextTarget == null;
  }
  
}
