# Microsoft Developer Studio Project File - Name="TestCVtbl_Msc6" - Package Owner=<4>
# Microsoft Developer Studio Generated Build File, Format Version 6.00
# ** DO NOT EDIT **

# TARGTYPE "Win32 (x86) Console Application" 0x0103

CFG=TestCVtbl_Msc6 - Win32 PreProcessor
!MESSAGE This is not a valid makefile. To build this project using NMAKE,
!MESSAGE use the Export Makefile command and run
!MESSAGE 
!MESSAGE NMAKE /f "testAllConcepts_Msc6.mak".
!MESSAGE 
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "testAllConcepts_Msc6.mak" CFG="TestCVtbl_Msc6 - Win32 PreProcessor"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "TestCVtbl_Msc6 - Win32 Release" (based on "Win32 (x86) Console Application")
!MESSAGE "TestCVtbl_Msc6 - Win32 Debug" (based on "Win32 (x86) Console Application")
!MESSAGE "TestCVtbl_Msc6 - Win32 DebugCpp" (based on "Win32 (x86) Console Application")
!MESSAGE "TestCVtbl_Msc6 - Win32 PreProcessor" (based on "Win32 (x86) Console Application")
!MESSAGE 

# Begin Project
# PROP AllowPerConfigDependencies 0
# PROP Scc_ProjName ""
# PROP Scc_LocalPath ""
CPP=cl.exe
RSC=rc.exe

!IF  "$(CFG)" == "TestCVtbl_Msc6 - Win32 Release"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 0
# PROP BASE Output_Dir "Release"
# PROP BASE Intermediate_Dir "Release"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 0
# PROP Output_Dir "T:\Java2C\testAllConcepts\release"
# PROP Intermediate_Dir "T:\Java2C\testAllConcepts\release"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_CONSOLE" /D "_MBCS" /YX /FD /c
# ADD CPP /nologo /G6 /Zp1 /MT /W3 /GX /O2 /I "src_c" /I "gensrc_c" /I "../../CRuntimeJavalike/os_Windows" /I "../../CRuntimeJavalike" /I "../../CRuntimeJavalike/OSAL/inc" /I "../../CRuntimeJavalike/FwConvC" /D "WIN32" /D "NDEBUG" /D "_CONSOLE" /D "_MBCS" /FR /FD /c
# SUBTRACT CPP /YX
# ADD BASE RSC /l 0x407 /d "NDEBUG"
# ADD RSC /l 0x407 /d "NDEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:console /machine:I386
# ADD LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:console /machine:I386

!ELSEIF  "$(CFG)" == "TestCVtbl_Msc6 - Win32 Debug"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "debug"
# PROP BASE Intermediate_Dir "debug"
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "T:\Java2C\testAllConcepts\dbgC"
# PROP Intermediate_Dir "T:\Java2C\testAllConcepts\dbgC"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /W3 /Gm /GX /ZI /Od /D "WIN32" /D "_DEBUG" /D "_CONSOLE" /D "_MBCS" /YX /FD /GZ /c
# ADD CPP /nologo /Zp1 /MTd /W3 /vmg /ZI /Od /I "src_c" /I "gensrc_c" /I "../../CRuntimeJavalike/os_Windows" /I "../../CRuntimeJavalike" /I "../../CRuntimeJavalike/OSAL/inc" /I "../../CRuntimeJavalike/FwConvC" /D "WIN32" /D "_DEBUG" /D "_CONSOLE" /D "_MBCS" /FAcs /FR /FD /GZ /c
# ADD BASE RSC /l 0x407 /d "_DEBUG"
# ADD RSC /l 0x407 /d "_DEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib kernel32.lib user32.lib gdi32.lib winspool.lib comdlg32.lib advapi32.lib shell32.lib ole32.lib oleaut32.lib uuid.lib odbc32.lib odbccp32.lib /nologo /subsystem:console /debug /machine:I386 /pdbtype:sept
# ADD LINK32 ws2_32.lib /nologo /subsystem:console /incremental:no /map /debug /machine:I386 /pdbtype:sept
# SUBTRACT LINK32 /nodefaultlib

!ELSEIF  "$(CFG)" == "TestCVtbl_Msc6 - Win32 DebugCpp"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "TestCVtbl_Msc6___Win32_DebugCpp"
# PROP BASE Intermediate_Dir "TestCVtbl_Msc6___Win32_DebugCpp"
# PROP BASE Ignore_Export_Lib 0
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "T:\Java2C\testAllConcepts\dbgCpp"
# PROP Intermediate_Dir "T:\Java2C\testAllConcepts\dbgCpp"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /Zp1 /MTd /W3 /vmg /ZI /Od /I "src_c" /I "gensrc_c" /I "../../CRuntimeJavalike/os_Windows" /I "../../CRuntimeJavalike" /I "../../CRuntimeJavalike/OSAL/inc" /I "../../CRuntimeJavalike/FwConvC" /D "WIN32" /D "_DEBUG" /D "_CONSOLE" /D "_MBCS" /FAcs /FR /FD /GZ /c
# ADD CPP /nologo /Zp1 /MTd /W3 /vmg /ZI /Od /I "src_c" /I "gensrc_c" /I "../../CRuntimeJavalike/os_Windows" /I "../../CRuntimeJavalike" /I "../../CRuntimeJavalike/OSAL/inc" /I "../../CRuntimeJavalike/FwConvC" /D "WIN32" /D "_DEBUG" /D "_CONSOLE" /D "_MBCS" /FAcs /FR /FD /GZ /TP /c
# ADD BASE RSC /l 0x407 /d "_DEBUG"
# ADD RSC /l 0x407 /d "_DEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 ws2_32.lib /nologo /subsystem:console /incremental:no /map /debug /machine:I386 /pdbtype:sept
# SUBTRACT BASE LINK32 /nodefaultlib
# ADD LINK32 ws2_32.lib /nologo /subsystem:console /incremental:no /map /debug /machine:I386 /pdbtype:sept
# SUBTRACT LINK32 /nodefaultlib

!ELSEIF  "$(CFG)" == "TestCVtbl_Msc6 - Win32 PreProcessor"

# PROP BASE Use_MFC 0
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "TestCVtbl_Msc6___Win32_PreProcessor"
# PROP BASE Intermediate_Dir "TestCVtbl_Msc6___Win32_PreProcessor"
# PROP BASE Ignore_Export_Lib 0
# PROP BASE Target_Dir ""
# PROP Use_MFC 0
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "TestCVtbl_Msc6___Win32_PreProcessor"
# PROP Intermediate_Dir "TestCVtbl_Msc6___Win32_PreProcessor"
# PROP Ignore_Export_Lib 0
# PROP Target_Dir ""
# ADD BASE CPP /nologo /Zp1 /MTd /W3 /vmg /ZI /Od /I "src_c" /I "gensrc_c" /I "../../CRuntimeJavalike/os_Windows" /I "../../CRuntimeJavalike" /I "../../CRuntimeJavalike/OSAL/inc" /I "../../CRuntimeJavalike/FwConvC" /D "WIN32" /D "_DEBUG" /D "_CONSOLE" /D "_MBCS" /FAcs /FR /FD /GZ /TP /c
# ADD CPP /nologo /Zp1 /MTd /W3 /vmg /ZI /Od /I "src_c" /I "gensrc_c" /I "../../CRuntimeJavalike/os_Windows" /I "../../CRuntimeJavalike" /I "../../CRuntimeJavalike/OSAL/inc" /I "../../CRuntimeJavalike/FwConvC" /D "WIN32" /D "_DEBUG" /D "_CONSOLE" /D "_MBCS" /FAcs /FR /FD /P /GZ /TP /c
# ADD BASE RSC /l 0x407 /d "_DEBUG"
# ADD RSC /l 0x407 /d "_DEBUG"
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
LINK32=link.exe
# ADD BASE LINK32 ws2_32.lib /nologo /subsystem:console /incremental:no /map /debug /machine:I386 /pdbtype:sept
# SUBTRACT BASE LINK32 /nodefaultlib
# ADD LINK32 ws2_32.lib /nologo /subsystem:console /incremental:no /map /debug /machine:I386 /pdbtype:sept
# SUBTRACT LINK32 /nodefaultlib

!ENDIF 

# Begin Target

# Name "TestCVtbl_Msc6 - Win32 Release"
# Name "TestCVtbl_Msc6 - Win32 Debug"
# Name "TestCVtbl_Msc6 - Win32 DebugCpp"
# Name "TestCVtbl_Msc6 - Win32 PreProcessor"
# Begin Group "Ressourcendateien"

# PROP Default_Filter "ico;cur;bmp;dlg;rc2;rct;bin;rgs;gif;jpg;jpeg;jpe"
# End Group
# Begin Group "Quellcodedateien"

# PROP Default_Filter "cpp;c;cxx;rc;def;r;odl;idl;hpj;bat"
# Begin Group "gensrc_c"

# PROP Default_Filter ""
# Begin Group "Java2cTest"

# PROP Default_Filter ""
# Begin Source File

SOURCE=.\gensrc_c\Java2cTest\IfcTest.c
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\Java2cTest\IfcTest.h
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\Java2cTest\ImplTest.c
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\Java2cTest\ImplTest.h
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\TestStringFormatter_Test.c
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\TestStringFormatter_Test.h
# End Source File
# End Group
# Begin Source File

SOURCE=.\gensrc_c\AnyClass_Test.c
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\AnyClass_Test.h
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\ExpandedDataStruct_Test.c
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\ExpandedDataStruct_Test.h
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\ExtendsImpl_Test.c
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\ExtendsImpl_Test.h
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\Ifc2_Test.c
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\Ifc2_Test.h
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\SimpleClass_Test.c
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\SimpleClass_Test.h
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\SimpleDataStruct_Test.c
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\SimpleDataStruct_Test.h
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\TestAllConcepts_Test.c
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\TestAllConcepts_Test.h
# End Source File
# Begin Source File

SOURCE=.\GENSRC_C\TestAnonymous_Test.c
# End Source File
# Begin Source File

SOURCE=.\GENSRC_C\TestAnonymous_Test.h
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\TestContainer_Test.c
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\TestContainer_Test.h
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\TestgarbageCollector_Test.c
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\TestgarbageCollector_Test.h
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\TestString_classic_Test.h
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\TestString_Test.c
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\TestString_Test.h
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\TestThread_Test.c
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\TestThread_Test.h
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\TestWaitNotify_Test.c
# End Source File
# Begin Source File

SOURCE=.\gensrc_c\TestWaitNotify_Test.h
# End Source File
# End Group
# Begin Group "CRuntimeJavalike"

# PROP Default_Filter ""
# Begin Group "Jc"

# PROP Default_Filter ""
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\AbstractListJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ArraysJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ArraysJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ComparatorJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ConcurrentLinkedQueueJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ConcurrentLinkedQueueJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ConcurrentRingBufferJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ConcurrentRingBufferJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\DateJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\DateJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\FileIoJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\FileIoJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\FormatterJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\FormatterJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\LinkedListJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\LinkedListJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ListIteratorJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ListJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ListMapEntryJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\LocaleJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\LocaleJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\MathJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ObjectJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ObjectJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ObjectRefJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\OsWrapperJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\PrintStreamJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ReflectionBaseTypesJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ReflectionJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ReflectionJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ReflMemAccessJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ReflMemAccessJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\StringBufferJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\StringBufferJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\StringJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\StringJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\StringJc_intern.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\StringValueConvJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\SystemJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\SystemJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ThreadJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\ThreadJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\TimeZoneJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\TimeZoneJc.h
# End Source File
# End Group
# Begin Group "Fwc"

# PROP Default_Filter ""
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Fwc\fw_Exception.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Fwc\fw_Exception.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Fwc\fw_Formatter.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Fwc\fw_Formatter.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Fwc\fw_LogMessage.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Fwc\fw_LogMessage.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Fwc\fw_LogMsgConsole.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Fwc\fw_MemC.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Fwc\fw_MemC.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Fwc\fw_Object.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Fwc\fw_SimpleC.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Fwc\fw_SimpleC.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Fwc\fw_String.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Fwc\fw_threadContext.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Fwc\fw_ThreadContext.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Fwc\fw_timeconversions.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Fwc\fw_timeconversions.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Fwc\objectBaseC.h
# End Source File
# End Group
# Begin Group "OSAL"

# PROP Default_Filter ""
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\OSAL\inc\os_adapter.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\OSAL\inc\os_AtomicAccess.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\OSAL\src\os_common.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\OSAL\inc\os_error.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\OSAL\inc\os_file.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\OSAL\inc\os_mem.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\OSAL\inc\os_sync.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\OSAL\inc\os_thread.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\OSAL\inc\os_time.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\OSAL\inc\os_waitnotify.h
# End Source File
# End Group
# Begin Group "gen"

# PROP Default_Filter ""
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Jc\Reflection_AllJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\BlockHeap\Reflection_BlockHeap.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\Fwc\Reflection_Fwc.c
# End Source File
# End Group
# Begin Group "BlockHeap"

# PROP Default_Filter ""
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\BlockHeap\BlockHeapJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\BlockHeap\BlockHeapJc_admin.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\BlockHeap\BlockHeapJc_Alloc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\BlockHeap\BlockHeapJc_GarbageCol.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\BlockHeap\BlockHeapJc_internal.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\BlockHeap\BlockHeapJc_References.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\BlockHeap\BlockHeapNoButDynCall.c
# End Source File
# End Group
# Begin Group "FwConv"

# PROP Default_Filter ""
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\FwConvC\fw_Platform_conventions.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\FwConv\fw_PlatformConvSimpleStop.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\FwConv\RemoteCpu_Dummy.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\FwConv\UmlContainer_Dummy.c
# End Source File
# End Group
# Begin Group "os_Windows"

# PROP Default_Filter ""
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\os_Windows\os_atomic.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\os_Windows\os_endian.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\os_Windows\os_error.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\os_Windows\os_file.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\os_Windows\os_mem.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\os_Windows\os_mutex.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\os_Windows\os_sync.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\os_Windows\os_thread.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\os_Windows\os_time.c
# End Source File
# End Group
# End Group
# Begin Group "src_c"

# PROP Default_Filter ""
# Begin Source File

SOURCE=.\src_c\testAllConcepts_Main.c
# End Source File
# Begin Source File

SOURCE=.\src_c\TestString_classic.c
# End Source File
# Begin Source File

SOURCE=.\src_c\TestString_classic.h
# End Source File
# End Group
# Begin Group "J1c"

# PROP Default_Filter ""
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\J1c\ByteDataAccessJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\J1c\ByteDataAccessJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\J1c\LeapSecondsJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\J1c\LeapSecondsJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\J1c\LogMessageFile_MSG.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\J1c\LogMessageFile_MSG.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\J1c\MsgDispatcher_MSG.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\J1c\MsgDispatcher_MSG.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\J1c\RawDataAccessJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\J1c\RawDataAccessJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\J1c\SpecialCharStringsJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\J1c\SpecialCharStringsJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\J1c\StringFormatterJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\J1c\StringFormatterJc.h
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\J1c\StringPartJc.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\J1c\StringPartJc.h
# End Source File
# End Group
# Begin Group "MsgDispC"

# PROP Default_Filter ""
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\MsgDisp\VaArgBuffer.c
# End Source File
# Begin Source File

SOURCE=..\..\CRuntimeJavalike\MsgDisp\VaArgBuffer.h
# End Source File
# End Group
# End Group
# End Target
# End Project
