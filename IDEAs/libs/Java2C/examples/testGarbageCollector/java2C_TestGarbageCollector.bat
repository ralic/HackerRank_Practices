if not exist tmp mkdir tmp
if not exist gensrc_c mkdir gensrc_c
rem call java -jar ../../Java2C.jar
rem set CLASSPATH=
java -cp ../../Java2C.jar org.vishia.java2C.Java2C -i:srcJava\org\vishia\exampleJava2C\testGarbageCollector\*.java -o:gensrc_c -syntax:../../Xml_Toolbase/zbnf --rlevel:334 --report:tmp/java2c.rpt
pause
