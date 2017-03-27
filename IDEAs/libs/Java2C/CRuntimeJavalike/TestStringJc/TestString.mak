# Microsoft Developer Studio Generated NMAKE File, Based on TestString.dsp
!IF "$(CFG)" == ""
CFG=TestString - Win32 DebugCpp
!MESSAGE No configuration specified. Defaulting to TestString - Win32 DebugCpp.
!ENDIF 

!IF "$(CFG)" != "TestString - Win32 Release" && "$(CFG)" != "TestString - Win32 Debug_C" && "$(CFG)" != "TestString - Win32 DebugCpp"
!MESSAGE Invalid configuration "$(CFG)" specified.
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "TestString.mak" CFG="TestString - Win32 DebugCpp"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "TestString - Win32 Release" (based on "Win32 (x86) Console Application")
!MESSAGE "TestString - Win32 Debug_C" (based on "Win32 (x86) Console Application")
!MESSAGE "TestString - Win32 DebugCpp" (based on "Win32 (x86) Console Application")
!MESSAGE 
!ERROR An invalid configuration is specified.
!ENDIF 

!IF "$(OS)" == "Windows_NT"
NULL=
!ELSE 
NULL=nul
!ENDIF 

!IF  "$(CFG)" == "TestString - Win32 Release"

OUTDIR=.\Release
INTDIR=.\Release
# Begin Custom Macros
OutDir=.\Release
# End Custom Macros

ALL : "$(OUTDIR)\TestString.exe"


CLEAN :
	-@erase "$(INTDIR)\allocOnlyAtStartup.obj"
	-@erase "$(INTDIR)\DateJc.obj"
	-@erase "$(INTDIR)\fw_Exception.obj"
	-@erase "$(INTDIR)\fw_Formatter.obj"
	-@erase "$(INTDIR)\fw_MemC.obj"
	-@erase "$(INTDIR)\fw_Object.obj"
	-@erase "$(INTDIR)\fw_PlatformConvSimpleStop.obj"
	-@erase "$(INTDIR)\fw_SimpleC.obj"
	-@erase "$(INTDIR)\fw_threadContext.obj"
	-@erase "$(INTDIR)\ObjectJc.obj"
	-@erase "$(INTDIR)\os_mem.obj"
	-@erase "$(INTDIR)\os_mutex.obj"
	-@erase "$(INTDIR)\os_thread.obj"
	-@erase "$(INTDIR)\os_time.obj"
	-@erase "$(INTDIR)\Reflection_TestString.obj"
	-@erase "$(INTDIR)\ReflectionBaseTypesJc.obj"
	-@erase "$(INTDIR)\ReflectionJc.obj"
	-@erase "$(INTDIR)\StringBufferJc.obj"
	-@erase "$(INTDIR)\StringJc.obj"
	-@erase "$(INTDIR)\TestString.obj"
	-@erase "$(INTDIR)\TestStringCpp.obj"
	-@erase "$(INTDIR)\vc60.idb"
	-@erase "$(OUTDIR)\TestString.exe"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP=cl.exe
CPP_PROJ=/nologo /ML /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_CONSOLE" /D "_MBCS" /Fp"$(INTDIR)\TestString.pch" /YX /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /c 

.c{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.c{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

RSC=rc.exe
BSC32=bscmake.exe
BSC32_FLAGS=/nologo /o"$(OUTDIR)\TestString.bsc" 
BSC32_SBRS= \
	
LINK32=link.exe
LINK32_FLAGS=kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:console /incremental:no /pdb:"$(OUTDIR)\TestString.pdb" /machine:I386 /out:"$(OUTDIR)\TestString.exe" 
LINK32_OBJS= \
	"$(INTDIR)\DateJc.obj" \
	"$(INTDIR)\ObjectJc.obj" \
	"$(INTDIR)\ReflectionJc.obj" \
	"$(INTDIR)\StringBufferJc.obj" \
	"$(INTDIR)\StringJc.obj" \
	"$(INTDIR)\TestString.obj" \
	"$(INTDIR)\TestStringCpp.obj" \
	"$(INTDIR)\fw_Exception.obj" \
	"$(INTDIR)\fw_Formatter.obj" \
	"$(INTDIR)\fw_MemC.obj" \
	"$(INTDIR)\fw_Object.obj" \
	"$(INTDIR)\fw_SimpleC.obj" \
	"$(INTDIR)\fw_threadContext.obj" \
	"$(INTDIR)\os_mem.obj" \
	"$(INTDIR)\os_mutex.obj" \
	"$(INTDIR)\os_thread.obj" \
	"$(INTDIR)\os_time.obj" \
	"$(INTDIR)\Reflection_TestString.obj" \
	"$(INTDIR)\ReflectionBaseTypesJc.obj" \
	"$(INTDIR)\allocOnlyAtStartup.obj" \
	"$(INTDIR)\fw_PlatformConvSimpleStop.obj"

"$(OUTDIR)\TestString.exe" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"

OUTDIR=T:\Jc\TestString_C
INTDIR=T:\Jc\TestString_C
# Begin Custom Macros
OutDir=T:\Jc\TestString_C
# End Custom Macros

ALL : "$(OUTDIR)\TestString.exe" "$(OUTDIR)\TestString.bsc"


CLEAN :
	-@erase "$(INTDIR)\allocOnlyAtStartup.obj"
	-@erase "$(INTDIR)\allocOnlyAtStartup.sbr"
	-@erase "$(INTDIR)\DateJc.obj"
	-@erase "$(INTDIR)\DateJc.sbr"
	-@erase "$(INTDIR)\fw_Exception.obj"
	-@erase "$(INTDIR)\fw_Exception.sbr"
	-@erase "$(INTDIR)\fw_Formatter.obj"
	-@erase "$(INTDIR)\fw_Formatter.sbr"
	-@erase "$(INTDIR)\fw_MemC.obj"
	-@erase "$(INTDIR)\fw_MemC.sbr"
	-@erase "$(INTDIR)\fw_Object.obj"
	-@erase "$(INTDIR)\fw_Object.sbr"
	-@erase "$(INTDIR)\fw_PlatformConvSimpleStop.obj"
	-@erase "$(INTDIR)\fw_PlatformConvSimpleStop.sbr"
	-@erase "$(INTDIR)\fw_SimpleC.obj"
	-@erase "$(INTDIR)\fw_SimpleC.sbr"
	-@erase "$(INTDIR)\fw_threadContext.obj"
	-@erase "$(INTDIR)\fw_threadContext.sbr"
	-@erase "$(INTDIR)\ObjectJc.obj"
	-@erase "$(INTDIR)\ObjectJc.sbr"
	-@erase "$(INTDIR)\os_mem.obj"
	-@erase "$(INTDIR)\os_mem.sbr"
	-@erase "$(INTDIR)\os_mutex.obj"
	-@erase "$(INTDIR)\os_mutex.sbr"
	-@erase "$(INTDIR)\os_thread.obj"
	-@erase "$(INTDIR)\os_thread.sbr"
	-@erase "$(INTDIR)\os_time.obj"
	-@erase "$(INTDIR)\os_time.sbr"
	-@erase "$(INTDIR)\Reflection_TestString.obj"
	-@erase "$(INTDIR)\Reflection_TestString.sbr"
	-@erase "$(INTDIR)\ReflectionBaseTypesJc.obj"
	-@erase "$(INTDIR)\ReflectionBaseTypesJc.sbr"
	-@erase "$(INTDIR)\StringBufferJc.obj"
	-@erase "$(INTDIR)\StringBufferJc.sbr"
	-@erase "$(INTDIR)\StringJc.obj"
	-@erase "$(INTDIR)\StringJc.sbr"
	-@erase "$(INTDIR)\TestString.obj"
	-@erase "$(INTDIR)\TestString.sbr"
	-@erase "$(INTDIR)\TestStringCpp.obj"
	-@erase "$(INTDIR)\TestStringCpp.sbr"
	-@erase "$(INTDIR)\vc60.idb"
	-@erase "$(INTDIR)\vc60.pdb"
	-@erase "$(OUTDIR)\TestString.bsc"
	-@erase "$(OUTDIR)\TestString.exe"
	-@erase "$(OUTDIR)\TestString.map"
	-@erase "$(OUTDIR)\TestString.pdb"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP=cl.exe
CPP_PROJ=/nologo /Zp1 /MLd /W3 /Gm /GX /ZI /Od /X /I ".." /I "../FwConvNoBheapC" /I "../OSAL/inc" /I "../os_Windows" /I "D:\Progs\MSC6\act\Microsoft Visual Studio\VC98\Include" /D "WIN32" /D "_DEBUG" /D "_CONSOLE" /D "_MBCS" /FAcs /Fa"$(INTDIR)\\" /FR"$(INTDIR)\\" /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /GZ /c 

.c{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.c{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

RSC=rc.exe
BSC32=bscmake.exe
BSC32_FLAGS=/nologo /o"$(OUTDIR)\TestString.bsc" 
BSC32_SBRS= \
	"$(INTDIR)\DateJc.sbr" \
	"$(INTDIR)\ObjectJc.sbr" \
	"$(INTDIR)\StringBufferJc.sbr" \
	"$(INTDIR)\StringJc.sbr" \
	"$(INTDIR)\TestString.sbr" \
	"$(INTDIR)\TestStringCpp.sbr" \
	"$(INTDIR)\fw_Exception.sbr" \
	"$(INTDIR)\fw_Formatter.sbr" \
	"$(INTDIR)\fw_MemC.sbr" \
	"$(INTDIR)\fw_Object.sbr" \
	"$(INTDIR)\fw_SimpleC.sbr" \
	"$(INTDIR)\fw_threadContext.sbr" \
	"$(INTDIR)\os_mem.sbr" \
	"$(INTDIR)\os_mutex.sbr" \
	"$(INTDIR)\os_thread.sbr" \
	"$(INTDIR)\os_time.sbr" \
	"$(INTDIR)\Reflection_TestString.sbr" \
	"$(INTDIR)\ReflectionBaseTypesJc.sbr" \
	"$(INTDIR)\allocOnlyAtStartup.sbr" \
	"$(INTDIR)\fw_PlatformConvSimpleStop.sbr"

"$(OUTDIR)\TestString.bsc" : "$(OUTDIR)" $(BSC32_SBRS)
    $(BSC32) @<<
  $(BSC32_FLAGS) $(BSC32_SBRS)
<<

LINK32=link.exe
LINK32_FLAGS=kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:console /incremental:no /pdb:"$(OUTDIR)\TestString.pdb" /map:"$(INTDIR)\TestString.map" /debug /machine:I386 /out:"$(OUTDIR)\TestString.exe" /pdbtype:sept /libpath:"D:\Progs\MSC6\act\Microsoft Visual Studio\VC98\Lib" 
LINK32_OBJS= \
	"$(INTDIR)\DateJc.obj" \
	"$(INTDIR)\ObjectJc.obj" \
	"$(INTDIR)\StringBufferJc.obj" \
	"$(INTDIR)\StringJc.obj" \
	"$(INTDIR)\TestString.obj" \
	"$(INTDIR)\TestStringCpp.obj" \
	"$(INTDIR)\fw_Exception.obj" \
	"$(INTDIR)\fw_Formatter.obj" \
	"$(INTDIR)\fw_MemC.obj" \
	"$(INTDIR)\fw_Object.obj" \
	"$(INTDIR)\fw_SimpleC.obj" \
	"$(INTDIR)\fw_threadContext.obj" \
	"$(INTDIR)\os_mem.obj" \
	"$(INTDIR)\os_mutex.obj" \
	"$(INTDIR)\os_thread.obj" \
	"$(INTDIR)\os_time.obj" \
	"$(INTDIR)\Reflection_TestString.obj" \
	"$(INTDIR)\ReflectionBaseTypesJc.obj" \
	"$(INTDIR)\allocOnlyAtStartup.obj" \
	"$(INTDIR)\fw_PlatformConvSimpleStop.obj"

"$(OUTDIR)\TestString.exe" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"

OUTDIR=.\TestString___Win32_DebugCpp
INTDIR=.\TestString___Win32_DebugCpp
# Begin Custom Macros
OutDir=.\TestString___Win32_DebugCpp
# End Custom Macros

ALL : "$(OUTDIR)\TestString.exe" "$(OUTDIR)\TestString.bsc"


CLEAN :
	-@erase "$(INTDIR)\allocOnlyAtStartup.obj"
	-@erase "$(INTDIR)\allocOnlyAtStartup.sbr"
	-@erase "$(INTDIR)\DateJc.obj"
	-@erase "$(INTDIR)\DateJc.sbr"
	-@erase "$(INTDIR)\fw_Exception.obj"
	-@erase "$(INTDIR)\fw_Exception.sbr"
	-@erase "$(INTDIR)\fw_Formatter.obj"
	-@erase "$(INTDIR)\fw_Formatter.sbr"
	-@erase "$(INTDIR)\fw_MemC.obj"
	-@erase "$(INTDIR)\fw_MemC.sbr"
	-@erase "$(INTDIR)\fw_Object.obj"
	-@erase "$(INTDIR)\fw_Object.sbr"
	-@erase "$(INTDIR)\fw_PlatformConvSimpleStop.obj"
	-@erase "$(INTDIR)\fw_PlatformConvSimpleStop.sbr"
	-@erase "$(INTDIR)\fw_SimpleC.obj"
	-@erase "$(INTDIR)\fw_SimpleC.sbr"
	-@erase "$(INTDIR)\fw_threadContext.obj"
	-@erase "$(INTDIR)\fw_threadContext.sbr"
	-@erase "$(INTDIR)\ObjectJc.obj"
	-@erase "$(INTDIR)\ObjectJc.sbr"
	-@erase "$(INTDIR)\os_mem.obj"
	-@erase "$(INTDIR)\os_mem.sbr"
	-@erase "$(INTDIR)\os_mutex.obj"
	-@erase "$(INTDIR)\os_mutex.sbr"
	-@erase "$(INTDIR)\os_thread.obj"
	-@erase "$(INTDIR)\os_thread.sbr"
	-@erase "$(INTDIR)\os_time.obj"
	-@erase "$(INTDIR)\os_time.sbr"
	-@erase "$(INTDIR)\Reflection_TestString.obj"
	-@erase "$(INTDIR)\Reflection_TestString.sbr"
	-@erase "$(INTDIR)\ReflectionBaseTypesJc.obj"
	-@erase "$(INTDIR)\ReflectionBaseTypesJc.sbr"
	-@erase "$(INTDIR)\StringBufferJc.obj"
	-@erase "$(INTDIR)\StringBufferJc.sbr"
	-@erase "$(INTDIR)\StringJc.obj"
	-@erase "$(INTDIR)\StringJc.sbr"
	-@erase "$(INTDIR)\TestString.obj"
	-@erase "$(INTDIR)\TestString.sbr"
	-@erase "$(INTDIR)\TestStringCpp.obj"
	-@erase "$(INTDIR)\TestStringCpp.sbr"
	-@erase "$(INTDIR)\vc60.idb"
	-@erase "$(INTDIR)\vc60.pdb"
	-@erase "$(OUTDIR)\TestString.bsc"
	-@erase "$(OUTDIR)\TestString.exe"
	-@erase "$(OUTDIR)\TestString.ilk"
	-@erase "$(OUTDIR)\TestString.pdb"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP=cl.exe
CPP_PROJ=/nologo /MLd /W3 /Gm /GX /ZI /Od /I ".." /I "../FwConvNoBheapCpp" /I "../OSAL/inc" /I "../os_Windows" /D "WIN32" /D "_DEBUG" /D "_CONSOLE" /D "_MBCS" /FR"$(INTDIR)\\" /Fp"$(INTDIR)\TestString.pch" /YX /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /GZ /TP /c 

.c{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.c{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

RSC=rc.exe
BSC32=bscmake.exe
BSC32_FLAGS=/nologo /o"$(OUTDIR)\TestString.bsc" 
BSC32_SBRS= \
	"$(INTDIR)\DateJc.sbr" \
	"$(INTDIR)\ObjectJc.sbr" \
	"$(INTDIR)\StringBufferJc.sbr" \
	"$(INTDIR)\StringJc.sbr" \
	"$(INTDIR)\TestString.sbr" \
	"$(INTDIR)\TestStringCpp.sbr" \
	"$(INTDIR)\fw_Exception.sbr" \
	"$(INTDIR)\fw_Formatter.sbr" \
	"$(INTDIR)\fw_MemC.sbr" \
	"$(INTDIR)\fw_Object.sbr" \
	"$(INTDIR)\fw_SimpleC.sbr" \
	"$(INTDIR)\fw_threadContext.sbr" \
	"$(INTDIR)\os_mem.sbr" \
	"$(INTDIR)\os_mutex.sbr" \
	"$(INTDIR)\os_thread.sbr" \
	"$(INTDIR)\os_time.sbr" \
	"$(INTDIR)\Reflection_TestString.sbr" \
	"$(INTDIR)\ReflectionBaseTypesJc.sbr" \
	"$(INTDIR)\allocOnlyAtStartup.sbr" \
	"$(INTDIR)\fw_PlatformConvSimpleStop.sbr"

"$(OUTDIR)\TestString.bsc" : "$(OUTDIR)" $(BSC32_SBRS)
    $(BSC32) @<<
  $(BSC32_FLAGS) $(BSC32_SBRS)
<<

LINK32=link.exe
LINK32_FLAGS=kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:console /incremental:yes /pdb:"$(OUTDIR)\TestString.pdb" /debug /machine:I386 /out:"$(OUTDIR)\TestString.exe" /pdbtype:sept 
LINK32_OBJS= \
	"$(INTDIR)\DateJc.obj" \
	"$(INTDIR)\ObjectJc.obj" \
	"$(INTDIR)\StringBufferJc.obj" \
	"$(INTDIR)\StringJc.obj" \
	"$(INTDIR)\TestString.obj" \
	"$(INTDIR)\TestStringCpp.obj" \
	"$(INTDIR)\fw_Exception.obj" \
	"$(INTDIR)\fw_Formatter.obj" \
	"$(INTDIR)\fw_MemC.obj" \
	"$(INTDIR)\fw_Object.obj" \
	"$(INTDIR)\fw_SimpleC.obj" \
	"$(INTDIR)\fw_threadContext.obj" \
	"$(INTDIR)\os_mem.obj" \
	"$(INTDIR)\os_mutex.obj" \
	"$(INTDIR)\os_thread.obj" \
	"$(INTDIR)\os_time.obj" \
	"$(INTDIR)\Reflection_TestString.obj" \
	"$(INTDIR)\ReflectionBaseTypesJc.obj" \
	"$(INTDIR)\allocOnlyAtStartup.obj" \
	"$(INTDIR)\fw_PlatformConvSimpleStop.obj"

"$(OUTDIR)\TestString.exe" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ENDIF 


!IF "$(NO_EXTERNAL_DEPS)" != "1"
!IF EXISTS("TestString.dep")
!INCLUDE "TestString.dep"
!ELSE 
!MESSAGE Warning: cannot find "TestString.dep"
!ENDIF 
!ENDIF 


!IF "$(CFG)" == "TestString - Win32 Release" || "$(CFG)" == "TestString - Win32 Debug_C" || "$(CFG)" == "TestString - Win32 DebugCpp"
SOURCE=..\Jc\DateJc.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\DateJc.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\DateJc.obj"	"$(INTDIR)\DateJc.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\DateJc.obj"	"$(INTDIR)\DateJc.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=..\Jc\ObjectJc.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\ObjectJc.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\ObjectJc.obj"	"$(INTDIR)\ObjectJc.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\ObjectJc.obj"	"$(INTDIR)\ObjectJc.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=..\Jc\ReflectionJc.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\ReflectionJc.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"

!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"

!ENDIF 

SOURCE=..\Jc\StringBufferJc.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\StringBufferJc.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\StringBufferJc.obj"	"$(INTDIR)\StringBufferJc.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\StringBufferJc.obj"	"$(INTDIR)\StringBufferJc.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=..\Jc\StringJc.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\StringJc.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\StringJc.obj"	"$(INTDIR)\StringJc.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\StringJc.obj"	"$(INTDIR)\StringJc.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=src\TestString.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\TestString.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\TestString.obj"	"$(INTDIR)\TestString.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\TestString.obj"	"$(INTDIR)\TestString.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=.\src\TestStringCpp.cpp

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\TestStringCpp.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\TestStringCpp.obj"	"$(INTDIR)\TestStringCpp.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\TestStringCpp.obj"	"$(INTDIR)\TestStringCpp.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=..\Fwc\fw_Exception.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\fw_Exception.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\fw_Exception.obj"	"$(INTDIR)\fw_Exception.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\fw_Exception.obj"	"$(INTDIR)\fw_Exception.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=..\Fwc\fw_Formatter.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\fw_Formatter.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\fw_Formatter.obj"	"$(INTDIR)\fw_Formatter.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\fw_Formatter.obj"	"$(INTDIR)\fw_Formatter.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=..\Fwc\fw_MemC.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\fw_MemC.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\fw_MemC.obj"	"$(INTDIR)\fw_MemC.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\fw_MemC.obj"	"$(INTDIR)\fw_MemC.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=..\Fwc\fw_Object.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\fw_Object.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\fw_Object.obj"	"$(INTDIR)\fw_Object.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\fw_Object.obj"	"$(INTDIR)\fw_Object.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=..\Fwc\fw_SimpleC.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\fw_SimpleC.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\fw_SimpleC.obj"	"$(INTDIR)\fw_SimpleC.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\fw_SimpleC.obj"	"$(INTDIR)\fw_SimpleC.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=..\Fwc\fw_threadContext.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\fw_threadContext.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\fw_threadContext.obj"	"$(INTDIR)\fw_threadContext.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\fw_threadContext.obj"	"$(INTDIR)\fw_threadContext.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=..\os_Windows\os_mem.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\os_mem.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\os_mem.obj"	"$(INTDIR)\os_mem.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\os_mem.obj"	"$(INTDIR)\os_mem.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=..\os_Windows\os_mutex.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\os_mutex.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\os_mutex.obj"	"$(INTDIR)\os_mutex.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\os_mutex.obj"	"$(INTDIR)\os_mutex.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=..\os_Windows\os_thread.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\os_thread.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\os_thread.obj"	"$(INTDIR)\os_thread.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\os_thread.obj"	"$(INTDIR)\os_thread.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=..\os_Windows\os_time.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\os_time.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\os_time.obj"	"$(INTDIR)\os_time.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\os_time.obj"	"$(INTDIR)\os_time.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=.\src\Reflection_TestString.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\Reflection_TestString.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\Reflection_TestString.obj"	"$(INTDIR)\Reflection_TestString.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\Reflection_TestString.obj"	"$(INTDIR)\Reflection_TestString.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=..\Jc\ReflectionBaseTypesJc.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\ReflectionBaseTypesJc.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\ReflectionBaseTypesJc.obj"	"$(INTDIR)\ReflectionBaseTypesJc.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\ReflectionBaseTypesJc.obj"	"$(INTDIR)\ReflectionBaseTypesJc.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=..\FwConv\allocOnlyAtStartup.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\allocOnlyAtStartup.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\allocOnlyAtStartup.obj"	"$(INTDIR)\allocOnlyAtStartup.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\allocOnlyAtStartup.obj"	"$(INTDIR)\allocOnlyAtStartup.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 

SOURCE=..\FwConv\fw_PlatformConvSimpleStop.c

!IF  "$(CFG)" == "TestString - Win32 Release"


"$(INTDIR)\fw_PlatformConvSimpleStop.obj" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 Debug_C"


"$(INTDIR)\fw_PlatformConvSimpleStop.obj"	"$(INTDIR)\fw_PlatformConvSimpleStop.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "TestString - Win32 DebugCpp"


"$(INTDIR)\fw_PlatformConvSimpleStop.obj"	"$(INTDIR)\fw_PlatformConvSimpleStop.sbr" : $(SOURCE) "$(INTDIR)"
	$(CPP) $(CPP_PROJ) $(SOURCE)


!ENDIF 


!ENDIF 

