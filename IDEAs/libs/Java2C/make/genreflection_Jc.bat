@echo off
set TMP=..\tmp
if exist T:\tmp set TMP=T:\tmp\Jc
if not exist %TMP% mkdir %TMP%

::java -cp ../zbnfjax/zbnf.jar;../zbnfjax/header2Reflection.jar org.vishia.header2Reflection.CmdHeader2Reflection -out.c:../CRuntimeJavalike/gen/AllReflectionsJc.c -c_only -i:../CRuntimeJavalike:BlockHeap/*.h -i:../CRuntimeJavalike:Fwc/*.h -i:../CRuntimeJavalike:Jc/*.h -i:../CRuntimeJavalike:MsgDisp/*.h -i:../CRuntimeJavalike:OSAL/inc/*.h -b:../CRuntimeJavalike/ReflectionBlockedTypes.ctr -z:../zbnfjax/zbnf/Cheader.zbnf --report:%TMP%/genReflection.rpt --rlevel:334

set INPUT=
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/AbstractListJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/ArraysJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/byteDataJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/ComparatorJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/ConcurrentLinkedQueueJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/ConcurrentRingBufferJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/DateJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/LocaleJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/SystemJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/ObjectJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/LinkedListJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/ListIteratorJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/ListJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/ListMapEntryJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/StringJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/OsWrapperJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/ReflectionJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/ReflMemAccessJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/StringBufferJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/FileIoJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/ObjectRefJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/ThreadJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/FormatterJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/TimeZoneJc.h
set INPUT=%INPUT% -i:../CRuntimeJavalike:Jc/StringJc_intern.h

set INPUT=%INPUT% -i:../CRuntimeJavalike:MsgDisp/*.h
set INPUT=%INPUT%  -i:../CRuntimeJavalike:OSAL/inc/*.h

java -cp ../zbnfjax/zbnf.jar;../zbnfjax/header2Reflection.jar org.vishia.header2Reflection.CmdHeader2Reflection -out.c:../CRuntimeJavalike/Jc/Reflection_AllJc.c -c_only %INPUT% -b:../CRuntimeJavalike/ReflectionBlockedTypes.ctr -z:../zbnfjax/zbnf/Cheader.zbnf --report:%TMP%/genReflection.rpt --rlevel:334
if errorlevel 1 goto :error
goto :ende

:error
  @echo ======= error ========================
  pause
:ende
pause