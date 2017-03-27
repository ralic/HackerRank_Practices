del /F /Q *.ncb
del /F /Q *.opt
del /F /Q *.plg
del /F /Q *.dsw
del /F /Q *.txt
del /F /Q *.rpt
if exist *.i del /F /Q *.i
rmdir /S /Q debug
rmdir /S /Q tmp
rmdir /S /Q testjava
del /F /Q gensrc_c\*


