# Microsoft Developer Studio Project File - Name="TestFileJc" - Package Owner=<4>
# Microsoft Developer Studio Generated Build File, Format Version 6.00
# ** DO NOT EDIT **

# TARGTYPE "Win32 (x86) Console Application" 0x0103

CFG=TestFileJc - Win32 DebugCpp
!MESSAGE This is not a valid makefile. To build this project using NMAKE,
!MESSAGE use the Export Makefile command and run
!MESSAGE 
!MESSAGE NMAKE /f "TestFileJc.mak".
!MESSAGE 
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "TestFileJc.mak" CFG="TestFileJc - Win32 DebugCpp"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "TestFileJc - Win32 Release" (based on "Win32 (x86) Console Application")
!MESSAGE "TestFileJc - Win32 Debug_C" (based on "Win32 (x86) Console Application")
!MESSAGE "TestFileJc - Win32 DebugCpp" (based on "Win32 (x86) Console Application")
!MESSAGE 

# Begin Project
# PROP AllowPerConfigDependencies 0
# PROP Scc_ProjName ""
# PROP Scc_LocalPath ""
CPP=cl.exe
RSC=rc.exe

!IF  "$(CFG)" == "TestFileJc - Win32 Release"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 0
# PROP BASE Output_Dir "Release"
# PROP BASE Intermediate_Dir "Release"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 0
# PROP Output_Dir "Release"
# PROP Intermediate_Dir "Release"
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_CONSOLE" /D "_MBCS" /YX /FD /c
# ADD CPP /nologo /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_CONSOLE" /D "_MBCS" /YX /FD /c
# ADD BASE RSC /l 0x407 /d "NDEBUG"
# ADD RSC /l 0x407 /d "NDEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:console /machine:I386
# ADD LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:console /machine:I386

!ELSEIF  "$(CFG)" == "TestFileJc - Win32 Debug_C"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "TestBlockHeap___Win32_Debug_C"
# PROP BASE Intermediate_Dir "TestBlockHeap___Win32_Debug_C"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "T:\Jc\TestBlockHeap_C"
# PROP Intermediate_Dir "T:\Jc\TestBlockHeap_C"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /Gm /GX /ZI /Od /I ".." /D "WIN32" /D "_DEBUG" /D "_CONSOLE" /D "_MBCS" /FR /YX /FD /GZ /TP /c
# ADD CPP /nologo /Zp1 /W3 /Gm /GX /ZI /Od /X /I "../.." /I "../../FwConvNoBheapC" /I "../../OSAL/inc" /I "../../os_Windows" /D "WIN32" /D "_DEBUG" /D "_CONSOLE" /D "_MBCS" /FAcs /FR /FD /GZ /c
# SUBTRACT CPP /YX
# ADD BASE RSC /l 0x407 /d "_DEBUG"
# ADD RSC /l 0x407 /x /d "_DEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:console /debug /machine:I386 /pdbtype:sept
# ADD LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:console /incremental:no /map /debug /machine:I386 /pdbtype:sept /libpath:"D:\Progs\MSC6\act\Microsoft Visual Studio\VC98\Lib"
# SUBTRACT LINK32 /nodefaultlib

!ELSEIF  "$(CFG)" == "TestFileJc - Win32 DebugCpp"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "TestBlockHeap___Win32_DebugCpp"
# PROP BASE Intermediate_Dir "TestBlockHeap___Win32_DebugCpp"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "T:\Jc\TestFileJc"
# PROP Intermediate_Dir "T:\Jc\TestFileJc"
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /Gm /GX /ZI /Od /I ".." /I "../../FwConvCpp" /I "../../OSAL/inc" /I "../../os_Windows" /D "WIN32" /D "_DEBUG" /D "_CONSOLE" /D "_MBCS" /FR /YX /FD /GZ /TP /c
# ADD CPP /nologo /W3 /Gm /GX /ZI /Od /I ".." /I "../../FwConvNoBheapCpp" /I "../../OSAL/inc" /I "../../os_Windows" /I "../.." /D "WIN32" /D "_DEBUG" /D "_CONSOLE" /D "_MBCS" /FR /YX /FD /GZ /TP /c
# ADD BASE RSC /l 0x407 /d "_DEBUG"
# ADD RSC /l 0x407 /d "_DEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:console /debug /machine:I386 /pdbtype:sept
# ADD LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:console /debug /machine:I386 /pdbtype:sept

!ENDIF 

# Begin Target

# Name "TestFileJc - Win32 Release"
# Name "TestFileJc - Win32 Debug_C"
# Name "TestFileJc - Win32 DebugCpp"
# Begin Group "Source Files"

# PROP Default_Filter "cpp;c;cxx;rc;def;r;odl;idl;hpj;bat"
# Begin Group "Jc"

# PROP Default_Filter ""
# Begin Source File

SOURCE=..\..\Jc\DateJc.c
# End Source File
# Begin Source File

SOURCE=..\..\Jc\DateJc.h
# End Source File
# Begin Source File

SOURCE=..\..\Jc\FileIoJc.c
# End Source File
# Begin Source File

SOURCE=..\..\Jc\FileIoJc.h
# End Source File
# Begin Source File

SOURCE=..\..\Jc\ObjectJc.c
# End Source File
# Begin Source File

SOURCE=..\..\Jc\ObjectJc.h
# End Source File
# Begin Source File

SOURCE=..\..\Jc\ReflectionJc.c

!IF  "$(CFG)" == "TestFileJc - Win32 Release"

!ELSEIF  "$(CFG)" == "TestFileJc - Win32 Debug_C"

# PROP BASE Exclude_From_Build 1
# PROP Exclude_From_Build 1

!ELSEIF  "$(CFG)" == "TestFileJc - Win32 DebugCpp"

# PROP BASE Exclude_From_Build 1
# PROP Exclude_From_Build 1

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\..\Jc\StringBufferJc.c
# End Source File
# Begin Source File

SOURCE=..\..\Jc\StringJc.c
# End Source File
# Begin Source File

SOURCE=..\..\Jc\StringJc.h
# End Source File
# Begin Source File

SOURCE=..\..\Jc\StringJc_intern.h
# End Source File
# End Group
# Begin Group "Test"

# PROP Default_Filter ""
# Begin Source File

SOURCE=src\TestFileJc.c
# End Source File
# Begin Source File

SOURCE=.\src\TestFileJc.h
# End Source File
# Begin Source File

SOURCE=.\src\TestFileJcMain.c
# End Source File
# End Group
# Begin Group "Fwc"

# PROP Default_Filter ""
# Begin Source File

SOURCE=..\..\Fwc\fw_Exception.c
# End Source File
# Begin Source File

SOURCE=..\..\Fwc\fw_Exception.h
# End Source File
# Begin Source File

SOURCE=..\..\Fwc\fw_Formatter.c
# End Source File
# Begin Source File

SOURCE=..\..\Fwc\fw_Formatter.h
# End Source File
# Begin Source File

SOURCE=..\..\Fwc\fw_LogMessage.c
# End Source File
# Begin Source File

SOURCE=..\..\Fwc\fw_MemC.c
# End Source File
# Begin Source File

SOURCE=..\..\Fwc\fw_MemC.h
# End Source File
# Begin Source File

SOURCE=..\..\Fwc\fw_Object.c
# End Source File
# Begin Source File

SOURCE=..\..\Fwc\fw_SimpleC.c
# End Source File
# Begin Source File

SOURCE=..\..\Fwc\fw_SimpleC.h
# End Source File
# Begin Source File

SOURCE=..\..\Fwc\fw_String.h
# End Source File
# Begin Source File

SOURCE=..\..\Fwc\fw_threadContext.c
# End Source File
# Begin Source File

SOURCE=..\..\Fwc\fw_ThreadContext.h
# End Source File
# Begin Source File

SOURCE=..\..\Fwc\objectBaseC.h
# End Source File
# End Group
# Begin Group "OSAL"

# PROP Default_Filter ""
# Begin Source File

SOURCE=..\..\os_Windows\os_atomic.c
# End Source File
# Begin Source File

SOURCE=..\..\OSAL\inc\os_error.h
# End Source File
# Begin Source File

SOURCE=..\..\os_Windows\os_file.c

!IF  "$(CFG)" == "TestFileJc - Win32 Release"

!ELSEIF  "$(CFG)" == "TestFileJc - Win32 Debug_C"

!ELSEIF  "$(CFG)" == "TestFileJc - Win32 DebugCpp"

# PROP Exclude_From_Build 1

!ENDIF 

# End Source File
# Begin Source File

SOURCE=..\..\OSAL\inc\os_file.h
# End Source File
# Begin Source File

SOURCE=..\..\os_Windows\os_fileLoLevel.c
# End Source File
# Begin Source File

SOURCE=..\..\os_Windows\os_mem.c
# End Source File
# Begin Source File

SOURCE=..\..\OSAL\inc\os_mem.h
# End Source File
# Begin Source File

SOURCE=..\..\os_Windows\os_mutex.c
# End Source File
# Begin Source File

SOURCE=..\..\os_Windows\os_thread.c
# End Source File
# Begin Source File

SOURCE=..\..\OSAL\inc\os_thread.h
# End Source File
# Begin Source File

SOURCE=..\..\os_Windows\os_time.c
# End Source File
# Begin Source File

SOURCE=..\..\OSAL\inc\os_time.h
# End Source File
# End Group
# Begin Group "Reflection"

# PROP Default_Filter ""
# Begin Source File

SOURCE=..\..\Jc\Reflection_AllJc.c
# End Source File
# Begin Source File

SOURCE=..\..\Fwc\Reflection_Fwc.c
# End Source File
# Begin Source File

SOURCE=..\..\Jc\ReflectionBaseTypesJc.c
# End Source File
# End Group
# Begin Group "FwConv"

# PROP Default_Filter ""
# Begin Source File

SOURCE=..\..\FwConv\allocOnlyAtStartup.c
# End Source File
# Begin Source File

SOURCE=..\..\FwConv\fw_PlatformConvSimpleStop.c
# End Source File
# End Group
# End Group
# Begin Group "Header Files"

# PROP Default_Filter "h;hpp;hxx;hm;inl"
# End Group
# Begin Group "Resource Files"

# PROP Default_Filter "ico;cur;bmp;dlg;rc2;rct;bin;rgs;gif;jpg;jpeg;jpe"
# End Group
# End Target
# End Project
