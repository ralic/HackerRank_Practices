if test ! -d tmp; then mkdir -p tmp; fi
if test ! -d gensrc_c; then mkdir -p gensrc_c; fi
## call java -jar ../../Java2C.jar
## set CLASSPATH=
java -cp ../../Java2C.jar org.vishia.java2C.Java2C -if:inputs.cfg -o:gensrc_c -syntax:../../zbnfjax/zbnf --rlevel:334 --report:tmp/java2c.rpt

#call genreflection_example.bat
#pause

#/home/hartmut/winhome/hartmut/vishia/Java2C/sf/Java2C/examples/positionControl/java2C_example.sh
