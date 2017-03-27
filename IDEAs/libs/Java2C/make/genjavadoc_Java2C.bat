@echo off
rmdir /S /Q ..\..\javadoc
mkdir ..\..\javadoc
if not exist ..\..\tmp mkdir ..\..\tmp
rem pause
call setZBNFJAX_HOME.bat

set docuSrc=
set docuSrc=%docuSrc% ../srcJava.zbnf/org/vishia/mainCmd/*.java
set docuSrc=%docuSrc% ../srcJava.zbnf/org/vishia/util/*.java
set docuSrc=%docuSrc% ../srcJava.zbnf/org/vishia/xmlSimple/*.java
set docuSrc=%docuSrc% ../srcJava.zbnf/org/vishia/zbnf/*.java
set docuSrc=%docuSrc% ../srcJava.java2C/org/vishia/java2C/*.java
set docuSrc=%docuSrc% ../srcJava.java2C/org/vishia/java2C/test/*.java

echo generate docu: %docuSrc%
::this batch should be accessable in the PATH and should be set the JAVA_HOME environment variable:
::call setANT_HOME.bat
::%JAVA_HOME%\bin\javadoc.exe -d ../javadoc -public -notimestamp %docuSrc%  1>..\tmp\javadoc.rpt 2>..\tmp\javadoc.err
echo on
%JAVA_HOME%\bin\javadoc.exe -d ../../javadoc -private -linksource -notimestamp %docuSrc%  1>..\..\tmp\javadoc.rpt 2>..\..\tmp\javadoc.err
copy stylesheet_javadoc.css ..\..\javadoc\stylesheet.css
pause

