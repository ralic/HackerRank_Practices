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
 * @author JcHartmut: hartmut.schorrig@vishia.de
 * @version 2008-04-06  (year-month-day)
 * list of changes:
 * 2008-04-06 JcHartmut: some correction
 * 2008-03-15 JcHartmut: creation
 *
 ****************************************************************************/
package org.vishia.java2C;

import java.text.ParseException;

import org.vishia.mainCmd.MainCmd;
import org.vishia.mainCmd.MainCmd_ifc;
import org.vishia.mainCmd.Report;

/**Main class of Java2C-translator to organize call from cmd line.
 * TODO call from ANT should be implemented.
 * @author JcHartmut
 *
 */
public class Java2C extends MainCmd
{

  /**Aggregation to the Console ouput (implementation class of Report).*/
  Report console;




  /*---------------------------------------------------------------------------------------------*/
  /** main started from cmd line. A {@link Java2C_Main#singleton} is instanciated,
   * all cmd line arguments are setted to it. The special cmd line arguments are:
   * <table>
   * <tr><td><code>-if:CONFIGFILE </code></td><td>file contains configuration for translating, 
   *                              syntax see {@link Java2C_Main#setConfigFile(String)}.</td></tr>
   * <tr><td><code>-o:OUTPUT   </code></td><td>outputpath, to this folder the file are written</td></tr>
   * <tr><td><code>-syntax:SYNTAXDIR </code></td><td>path to the directory, for Java2C.zbnf, Java2Cstc.zbnf</td></tr>
   * </table> 
   * All standard cmd line arguments are the same described in {@link org.vishia.mainCmd.MainCmd#addStandardHelpInfo()}.
  */
  public static void main(String [] args)
  { Java2C mainCmdLine = new Java2C(args); //the instance to parse arguments and others.
    //Java2C_Main main = new Java2C_Main(mainCmdLine);     //the main instance
    Java2C_Main.CmdlineArgs argData = new Java2C_Main.CmdlineArgs();
    boolean bOk = true;
    try{ mainCmdLine.parseArguments(argData, args); }
    catch(Exception exception)
    { mainCmdLine.setExitErrorLevel(MainCmd_ifc.exitWithArgumentError);
      if(mainCmdLine.console != null){ mainCmdLine.console.writeError("cmd line parameter fault", exception); }
      else{ System.out.println(exception); }
      bOk = false;
    }
    if(bOk)
    { /** The execution class knows the Java2C Main class in form of the MainCmd super class
          to hold the contact to the command line execution.
      */
    	Java2C_Main.instanciateSingleton(argData, mainCmdLine);     //the main instance
      try{ Java2C_Main.singleton.execute(); }
      catch(Exception exception)
      { //catch the last level of error. No error is reported direct on command line!
        Java2C_Main.singleton.console.report("Uncatched Exception on main level:", exception);
        //exception.printStackTrace(System.err);
        Java2C_Main.singleton.console.setExitErrorLevel(MainCmd_ifc.exitWithErrors);
      }
    }
    mainCmdLine.exit();
  }



  /**The main instance for the Java2C translator. This is the {@link Java2C_Main#singleton} - instance.
   * Such an instance is also used if the translator is settled outside calling per cmdLine. 
   * The cmd line arguments are set to this instance. 
   * The instance is created before the command line arguments are parsed, 
   * because the cmd line argument values are set directly to this instance. 
   */
  private Java2C_Main.CmdlineArgs main;

  /*---------------------------------------------------------------------------------------------*/
  /** Constructor of the main class.
      The command line arguments are parsed here. After them the execute class is created as composition of Java2C.
  */
  private Java2C(String[] args)
  { super(args);
    //:TODO: user, add your help info!
    //super.addHelpInfo(getAboutInfo());
    super.addAboutInfo("Java2C translator");
    super.addAboutInfo("made by Hartmut Schorrig, 2008-03-24, Version 0.93a 2011-01-04");
    super.addStandardHelpInfo();
    //super.addHelpInfo("-i:INPUT    inputfilepathes, example -i:path/org/myPackage/*.java");
    super.addHelpInfo("-if:CFGFILE file contains some configuration for input, one per line, see examples.");
    super.addHelpInfo("-o:OUTPUT   outputpath, to this folder the file are written.");
    super.addHelpInfo("-syntax:SYNTAXDIR path to the directory, for Java2C.zbnf, Java2Cstc.zbnf.");

  }


  /**parses all cmd line arguments. This is a wrapper for parseArguments()-method from {@link MainCmd},
   * the MainCmd.parseArguments() is called inside. 
   * Before it is called, the aggregation to the Java2C-implementation class is set.
   */
  public void parseArguments(Java2C_Main.CmdlineArgs argData, String [] args)
  throws ParseException
  { this.main = argData;
    super.parseArguments(args);
  }



  /*---------------------------------------------------------------------------------------------*/
  /** Tests one argument. This method is invoked from {@link MainCmd.parseArgument(String[])}. 
   * It is abstract in the superclass MainCmd and have to be overwritten from the user.

      @param argc String of the actual parsed argument from cmd line
      @param nArg number of the argument in order of the command line, the first argument is number 1.
      @return true is okay,
              false if the argument doesn't match. The parseArgument method in MainCmd throws an exception,
              the application should be aborted.
  */
  protected boolean testArgument(String arg, int nArg)
  { boolean bOk = true;  //set to false if the argc is not passed

    //if(arg.startsWith("-i:"))      main.addInputFilemask(getArgument(3),null, null);
    //else 
      if(arg.startsWith("-if:")) main.setConfigFile(getArgument(4));
    else if(arg.startsWith("-o:")) main.setPathOut(getArgument(3));
    else if(arg.startsWith("-syntax:")) main.setSyntaxPath(getArgument(8));
    else bOk=false;

    return bOk;
  }

  /**invoked from parseArguments if no argument is given. In the default implementation a help info is written
   * and the application is terminated. The user should overwrite this method if the call without command line arguments
   * is meaningfully.
   * @throws ParseException
   *
   */
  protected void callWithoutArguments() throws ParseException
  { //:TODO: overwrite with empty method - if the calling without arguments
    //having equal rights than the calling with arguments - no special action.
    super.callWithoutArguments();  //it needn't be overwritten if it is unnecessary
  }




  /*---------------------------------------------------------------------------------------------*/
  /**Checks the cmdline arguments relation together.
     If there is an inconsistent, a message should be written. It may be also a warning.
  */
  protected boolean checkArguments()
  { return true;

  }


}