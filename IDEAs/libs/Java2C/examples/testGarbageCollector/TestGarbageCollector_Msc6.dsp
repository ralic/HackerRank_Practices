# Microsoft Developer Studio Project File - Name="TestGarbageCollector" - Package Owner=<4>
# Microsoft Developer Studio Generated Build File, Format Version 6.00
# ** DO NOT EDIT **

# TARGTYPE "Win32 (x86) Console Application" 0x0103

CFG=TestGarbageCollector - Win32 Release
!MESSAGE This is not a valid makefile. To build this project using NMAKE,
!MESSAGE use the Export Makefile command and run
!MESSAGE 
!MESSAGE NMAKE /f "TestGarbageCollector_Msc6.mak".
!MESSAGE 
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "TestGarbageCollector_Msc6.mak" CFG="TestGarbageCollector - Win32 Release"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "TestGarbageCollector - Win32 Release" (based on "Win32 (x86) Console Application")
!MESSAGE "TestGarbageCollector - Win32 Debug" (based on "Win32 (x86) Console Application")
!MESSAGE 

# Begin Project
# PROP AllowPerConfigDependencies 0
# PROP Scc_ProjName ""
# PROP Scc_LocalPath ""
CPP=cl.exe
RSC=rc.exe

!IF  "$(CFG)" == "TestGarbageCollector - Win32 Release"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 0
# PROP BASE Output_Dir "Release"
# PROP BASE Intermediate_Dir "Release"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 0
# PROP Output_Dir "Release"
# PROP Intermediate_Dir "Release"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_CONSOLE" /D "_MBCS" /YX /FD /c
# ADD CPP /nologo /Zp1 /W3 /GX /O2 /I "." /I "../../CRuntimeJavalike/SysConventions_CVtbl" /I "../../CRuntimeJavalike/platform_Windows" /I "../../CRuntimeJavalike" /I "../src" /D "WIN32" /D "NDEBUG" /D "_CONSOLE" /D "_MBCS" /FR /FD /c
# SUBTRACT CPP /YX
# ADD BASE RSC /l 0x407 /d "NDEBUG"
# ADD RSC /l 0x407 /d "NDEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:console /machine:I386
# ADD LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:console /machine:I386

!ELSEIF  "$(CFG)" == "TestGarbageCollector - Win32 Debug"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "debug"
# PROP BASE Intermediate_Dir "debug"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "debug"
# PROP Intermediate_Dir "debug"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /Gm /GX /ZI /Od /D "WIN32" /D "_DEBUG" /D "_CONSOLE" /D "_MBCS" /YX /FD /GZ /c
# ADD CPP /nologo /Zp1 /MDd /W3 /Gm /vmg /GX /ZI /Od /I "src_c" /I "gensrc_c" /I "../../CRuntimeJavalike/platform_Windows" /I "../../CRuntimeJavalike" /D "WIN32" /D "_DEBUG" /D "_CONSOLE" /D "_MBCS" /FAcs /FR /FD /GZ /c
# ADD BASE RSC /l 0x407 /d "_DEBUG"
# ADD RSC /l 0x407 /d "_DEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:console /debug /machine:I386 /pdbtype:sept
# ADD LINK32 ws2_32.lib /nologo /subsystem:console /incremental:no /map /debug /machine:I386 /pdbtype:sept
# SUBTRACT LINK32 /nodefaultlib

!ENDIF 

# Begin Target

# Name "TestGarbageCollector - Win32 Release"
# Name "TestGarbageCollector - Win32 Debug"
# Begin Group "Ressourcendateien"

# PROP Default_Filter "ico;cur;bmp;dlg;rc2;rct;bin;rgs;gif;jpg;jpeg;jpe"
# End Group
# Begin Group "Quellcodedateien"

# PROP Default_Filter "cpp;c;cxx;rc;def;r;odl;idl;hpj;bat"
# Begin Group "gensrcJava"

# PROP Default_Filter ""
# Begin Source File

SOURCE=.\gensrc_c\allReferences.h
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\TestClass.c
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\TestClass.h
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\TestGarbageCollector.c
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\TestGarbageCollector.h
# End Source File
# End Group
# Begin Group "CRuntimeJavalike"

# PROP Default_Filter ""
# Begin Group "platform_Windows"

# PROP Default_Filter ""
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\platform_Windows\complctr.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\platform_Windows\os_mem.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\platform_Windows\os_sync.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\platform_Windows\os_thread.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\platform_Windows\OS_ThreadContext.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\platform_Windows\os_time.c
# End Source File
# End Group
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\ArraysJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\ArraysJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\BlockHeapJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\BlockHeapJc_admin.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\BlockHeapJc_Alloc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\BlockHeapJc_GarbageCol.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\BlockHeapJc_internal.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\BlockHeapJc_References.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\BlockHeapNoButDynCall.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\ComparatorJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\CRuntimeJavalike_SysConventions.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\DateJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\ExceptionJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\ExceptionJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\ListMapEntryJc_i.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\LogMessageJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\LogMessageJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\MemC.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\objectBaseC.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\ObjectJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\ObjectJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\ObjectRefJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\os_wrapperJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\ReflectionConstJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\ReflectionJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\ReflectionJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\simpleC.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\simpleC.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\StringBufferJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\StringBufferJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\StringJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\StringJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\SysConventionsJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\UmlContainer_Dummy.c
# End Source File
# End Group
# Begin Group "src_c"

# PROP Default_Filter ""
# Begin Source File

SOURCE=.\src_c\TestGarbageCollector_main.c
# End Source File
# Begin Source File

SOURCE=.\src_c\TestGarbageCollector_main.h
# End Source File
# End Group
# End Group
# End Target
# End Project
