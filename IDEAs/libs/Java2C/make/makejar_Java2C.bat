echo off
set TMP=..\tmp
if exist %TMP% rmdir %TMP% /S /Q
mkdir %TMP%

call setZBNFJAX_HOME.bat

::only choice of primary sources:
set INPUT=
::set INPUT=%INPUT% ../srcJava/org/vishia/header2Reflection/CmdHeader2Reflection.java
set INPUT=%INPUT% ../srcJava.java2C/org/vishia/java2C/Java2C.java

echo javac %INPUT%
echo
rem set CLASSPATH=../zbnf.jar
set CLASSPATH=xxx
set SRCPATH=../srcJava.java2C;../srcJava.util;../srcJava.zbnf
echo on
%JAVA_HOME%\bin\javac.exe -deprecation -d %TMP% -sourcepath %SRCPATH% -classpath %CLASSPATH% %INPUT% 1>>%TMP%\javac_ok.txt 2>%TMP%\error.txt
echo off
if errorlevel 1 goto :error
echo copiling successfull, generate jar:

cd %TMP%
rem echo jar -x
rem if errorlevel 1 goto :error
echo jar -c
jar.exe -cvfm ../Java2C.jar ../make/Java2C.manifest org/*  >>error.txt
if errorlevel 1 goto :error

pause
goto :ende

:error
  type %TMP%\error.txt
  pause
  goto :ende

:ende
