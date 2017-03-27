package org.vishia.exampleJava2C.java4c;

import java.text.ParseException;

import org.vishia.util.StringPart;

public class ReadTargetFromText
{

  final String textTarget = "1000, 2000, 1500";
  
  /**Class to hold the values for one target.
   * 
   * @xxxjava2c=noObject. It should be never used as Object.
   *
   */
  public static final class Target
  { short position;
    short velocity;
  }
  
  /**Some space to store target points. This is static created space in C
   * @java2c = embeddedArrayElements.
   */
  final Target[] targets = new Target[100];
  
  
  final StringPart spTextTarget = new StringPart();
  
  
  ReadTargetFromText()
  {
    //org.vishia.util.FixArray.init(targets);
  }
  
  void test()
  { try{ readTargetsFromText(textTarget);}
    catch (ParseException e)
    { 
      // TODO Auto-generated catch block
      //e.printStackTrace();
    }
  }
  
  
  int readTargetsFromText(String input) throws ParseException
  { spTextTarget.assign(input);
    spTextTarget.setIgnoreWhitespaces(true);
    short target;
    short velocity;
    int idxTargets = 0;
    for(idxTargets = 0; idxTargets < targets.length; idxTargets++)
    { targets[idxTargets] = new Target();
    }
    idxTargets = 0;
    boolean bNext;
    do
    { if(spTextTarget.scanInteger().scanOk())
      { target = (short)spTextTarget.getLastScannedIntegerNumber();
        if(spTextTarget.scan(":").scanInteger().scanOk())
        {
          velocity = (short)spTextTarget.getLastScannedIntegerNumber();
        }
        else
        { //no velocity given:
          velocity = 100;
        }
      }
      else
      { throw new ParseException("integer value expected", spTextTarget.getLineCt());
      }
      targets[idxTargets].position = target;
      targets[idxTargets].velocity = velocity;
      idxTargets +=1;
      bNext = spTextTarget.scan(",").scanOk();
    }while(bNext ); // && idxTargets < targets.length);
    return idxTargets;
  }
  
  
  
}
