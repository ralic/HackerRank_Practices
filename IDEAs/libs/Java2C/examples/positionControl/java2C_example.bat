if not exist tmp mkdir tmp
if not exist gensrc_c mkdir gensrc_c
rem call java -jar ../../Java2C.jar
rem set CLASSPATH=
::java -cp ../../Java2C.jar org.vishia.java2C.Java2C -i:srcJava\org\vishia\exampleJava2C\java4c\*.java -o:gensrc_c -syntax:../../syntax/Java2C.zbnf --rlevel:334 --report:tmp/java2c.rpt
call java -cp ../../Java2C.jar org.vishia.java2C.Java2C -if:inputs.cfg -o:gensrc_c -syntax:../../zbnfjax/zbnf --rlevel:334 --report:tmp/java2c.rpt
pause
call genreflection_example.bat
pause