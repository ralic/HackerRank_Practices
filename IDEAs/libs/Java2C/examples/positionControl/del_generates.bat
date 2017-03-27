del /F /Q *.ncb
del /F /Q *.opt
del /F /Q *.plg
del /F /Q *.dsw
del /F /Q *.mak
del /F /Q *.txt
del /F /Q *.rpt
if exist *.i del /F /Q *.i
rmdir /S /Q debug
rmdir /S /Q tmp
rmdir /S /Q testJava
rmdir /S /Q testLog
mkdir testJava
mkdir testJava\out
mkdir testLog
call del__testall.bat


