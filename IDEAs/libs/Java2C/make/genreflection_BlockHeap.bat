@echo off
set TMP=..\tmp
if exist T:\tmp set TMP=T:\tmp\Jc
if not exist %TMP% mkdir %TMP%

java -cp ../zbnfjax/zbnf.jar;../zbnfjax/header2Reflection.jar org.vishia.header2Reflection.CmdHeader2Reflection -out.c:../CRuntimeJavalike/BlockHeap/Reflection_BlockHeap.c -c_only -i:../CRuntimeJavalike:BlockHeap/*.h -b:../CRuntimeJavalike/ReflectionBlockedTypes.ctr -z:../zbnfjax/zbnf/Cheader.zbnf --report:%TMP%/genReflection.rpt --rlevel:334
if errorlevel 1 goto :error
goto :ende

:error
  @echo ======= error ========================
  pause
:ende
pause