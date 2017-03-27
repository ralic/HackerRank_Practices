#use a special java directory, then activate the next line:
export set JAVA_HOME="/usr/share/java/jdk1.6.0_21"

#The tmp dir stores the class-files
export set TMP_JAVAC="../tmp"

#clean the tmp
if test -d $TMP_JAVAC_JAVAC; then rm -r $TMP_JAVAC; fi
mkdir -p $TMP_JAVAC

#input file to compile:
export set INPUT="../srcJava/org/vishia/java2C/Java2C.java"

echo javac $INPUT
echo
## set CLASSPATH=../zbnf.jar
export set CLASSPATH="xxx"
export set SRCPATH="../srcJava:../srcJava.zbnf"

$JAVA_HOME/bin/javac -deprecation -d $TMP_JAVAC -sourcepath $SRCPATH -classpath $CLASSPATH $INPUT 1>>$TMP_JAVAC/javac_ok.txt 2>$TMP_JAVAC/javac_error.txt
if test $? -ge 1; then
  echo ===Compiler error===
  cat $TMP_JAVAC/javac_ok.txt $TMP_JAVAC/javac_error.txt
  echo ===Compiler error.
  exit 1
fi

echo copiling successfull, generate jar:

echo jar -c
##it doesn't work, argument for jar: -C $TMP_JAVAC
export set LASTDIR=$PWD
cd $TMP_JAVAC
$JAVA_HOME/bin/jar -cvfm ../Java2C.jar ../make/Java2C.manifest org/* 1>$TMP_JAVAC/jar_log.txt 2>$TMP_JAVAC/jar_error.txt
if test $? -ge 1; then
  echo ===Javac:
  cat $TMP_JAVAC/javac_ok.txt $TMP_JAVAC/javac_error.txt
  echo ===Jar:
  cat $TMP_JAVAC/jar_log.txt
  echo ===Jar error: at $PWD:
  cat $TMP_JAVAC/jar_error.txt
  cd $LASTDIR
  exit 1
fi
cd $LASTDIR

