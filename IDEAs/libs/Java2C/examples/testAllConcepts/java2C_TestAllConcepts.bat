set TMP=tmp
if exist T:\tmp set TMP=T:\tmp\Java2C_testAllConcepts
if not exist %TMP% mkdir %TMP%

if not exist gensrc_c mkdir gensrc_c
rem call java -jar ../../Java2C.jar
rem set CLASSPATH=
call java -cp ../../Java2C.jar org.vishia.java2C.Java2C -if:java2c_TestAllConcepts.cfg -o:gensrc_c -syntax:../../zbnfjax/zbnf --rlevel:334 --report:%TMP%/java2c.rpt
pause
call genreflection_example.bat