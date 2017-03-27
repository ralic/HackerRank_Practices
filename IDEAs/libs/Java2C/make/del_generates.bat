@echo on
if exist ..\tmp rmdir /S /Q ..\tmp
cd ..\examples\positionControl
call del_generates.bat
cd ..\testAllConcepts
call del_generates.bat
cd ..\Reflection_testMsc6
call del_generates.bat
cd ..\testGarbageCollector
call del_generates.bat
cd ..\..\CRuntimeJavalike\TestStringJc\del_generates.bat
call del_generates.bat
cd ..\..\CRuntimeJavalike\TestStringPart\del_generates.bat
call del_generates.bat
cd ..\..\make

pause

