#!/bin/bash
java -cp ../../zbnfjax/Header2Reflection.jar:../../zbnf.jar org.vishia.header2Reflection.CmdHeader2Reflection -out.c:gensrc_c/PosCtrl/reflection_Posctrl.c -c_only -i:src_c/*.h -b:../../CRuntimeJavalike/ReflectionBlockedTypes.ctr -z:../../zbnfjax/zbnf/Cheader.zbnf --report:gensrc_c/PosCtrl/genReflection.rpt --rlevel:334

##pause
