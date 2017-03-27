REM Temporary files should store at a location outside this directory tree 'Java2C', 
REM because a zip of the dir-tree saves the temps too otherwise.
REM In unix systems a temporary path can be defined in the home directory tree using ~/tmp.
REM But in windows it isn't so. Therefore the drive letter T: is used for temporary things.
REM A drive letter can be mounted to a local path using the DOS/Windows-subst command.

if exist T:\ subst T: /D

REM use a appropriate path:
subst T: D:\tmp

REM at that location a tmp should be visible and to use.
if not exist T:\tmp mkdir T:\tmp
dir T:\
pause 

