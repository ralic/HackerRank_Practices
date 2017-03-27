@echo off
if not exist ..\..\tmp mkdir ..\..\tmp  
if not exist ..\..\tmp\BaseC mkdir ..\..\tmp\BaseC
if not exist ..\..\tmp\BaseC\o mkdir ..\..\tmp\BaseC\o
if not exist ..\..\tmp\BaseC\lst mkdir ..\..\tmp\BaseC\lst
  
set LOG=..\..\tmp\BaseC\zmake.log
set ERRLOG=..\..\tmp\BaseC\zmake.err.log
set TMP_ZBNFJAX=..\..\tmp\BaseC
call setZBNFJAX_HOME.bat >%LOG%
set PATH=C:\hyNet32xs\USR\local\lib\gcc-lib\e1-coff\2.95.2;C:\hyNet32xs\USR\local\e1-coff\bin;C:\hyNet32xs\USR\local\bin;%PATH%
call %ZBNFJAX_HOME%\batch\zmake.bat ant_gcc.mak.bat %LOG% %ERRLOG%
if "%@NO_PAUSE%" == "" pause
exit  %ERRORLEVEL%

ZMAKE_RULES:


$src_make=fileset
( srcpath=".."
, TestStringJc/TestString.c 
      
);





//Line for generating objects for GNU-cc
$GnuCC = 
  cmd("rm-gcc.exe")
//, curdir("../../tmp/BaseC")  
, arg("-x c++ -c")                                         //force C++ compile always.
, arg("-Wa,-adhls=../tmp/lst/" + @file + ".lst")      //lst output
, arg("-fno-common")                                       //was weis ich, Rhapsody wei?'s
, arg("-I .. -I ../FwConvNoBheapCpp -I ../OSAL/inc -I ../os_Windows -I d:/Progs/RM3DEV/INC")
, arg(" -o ../../tmp/BaseC/O/" + @file + ".o")             //object.
, arg(@srcpath + @path + @file + @ext)                                //Quelle
;

:cc: ../../tmp/BaseC/O/** := for($src_make) exec{ $GnuCC; };



//Build the lib, simple with cmd: e1-coff-ar r LIB.a AllObjects
//../../lib/hynet/BaseC.a := exec{ cmd("e1-coff-ar"), curdir("../../tmp/BaseC"),  arg("r ../../lib/hynet/BaseC.a o/*.o"); };
