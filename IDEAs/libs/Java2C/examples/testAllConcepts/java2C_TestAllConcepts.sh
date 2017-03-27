export set TMP="tmp"
if test ! -d $TMP; then mkdir -p $TMP; fi
if test ! -d gensrc_c; then mkdir -p gensrc_c; fi
## call java -jar ../../Java2C.jar
## set CLASSPATH=
java -cp ../../Java2C.jar org.vishia.java2C.Java2C -if:java2c_TestAllConcepts.cfg -o:gensrc_c -syntax:../../zbnfjax/zbnf --rlevel:334 --report:$TMP/java2c.rpt

#pause

