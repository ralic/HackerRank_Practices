set TMP=..\tmp
if exist T:\tmp set TMP=T:\tmp\java2c_Jc
::del /F /Q ..\CRuntimeJavalike\J1c\*.*
if not exist %TMP% mkdir %TMP%
call java -ea -cp ../Java2C.jar org.vishia.java2C.Java2C -if:JavaJc.java2c.cfg -o:../CRuntimeJavalike -syntax:../zbnfjax/zbnf --rlevel:334 --report:%TMP%/java2cJc.rpt
pause
