/************************************************************************************************
 * Copyright/Copyleft:
 *
 * For this source the LGPL Lesser General Public License,
 * published by the Free Software Foundation is valid.
 * It means:
 * 1) You can use this source without any restriction for any desired purpose.
 * 2) You can redistribute copies of this source to everybody.
 * 3) Every user of this source, also the user of redistribute copies
 *    with or without payment, must accept this license for further using.
 * 4) But the LPGL ist not appropriate for a whole software product,
 *    if this source is only a part of them. It means, the user
 *    must publish this part of source,
 *    but don't need to publish the whole source of the own product.
 * 5) You can study and modify (improve) this source
 *    for own using or for redistribution, but you have to license the
 *    modified sources likewise under this LGPL Lesser General Public License.
 *    You mustn't delete this Copyright/Copyleft inscription in this source file.
 *
 * This source may be used also with another licence, if the author 
 * and all other here named co-authors have agreed to this contract.
 * Especially a company can use a copy of this sources in its products without publishing.
 * The user should have a underwritten contract therefore.
 *
 * @author Hartmut Schorrig, Germany, Pinzberg, www.vishia.org
 *
 **copyright***************************************************************************************
 *
 * @content Definition of basicly types adequate to the operation system, cpu and compiler.
 *
 * @author Hartmut Schorrig
 *************************************************************************************************/

#ifndef   __os_types_def_h__
#define   __os_types_def_h__


#ifndef __cplusplus
  //If C-compiling is used, define the C++-keywords for C
  #define bool int
  #define false 0
  #define true ~0
#endif


#ifndef FALSE
	#define FALSE	((bool)0)           ///< Define FALSE: Note: type of BOOL!
#endif

#ifndef TRUE
	#define TRUE	((bool)(!FALSE))   ///< Define TRUE
#endif

#ifndef NULL
	#define NULL	0                   ///< Define NULL
#endif

//This functions may be platform dependend but for all our platforms identical, also in C-Standard.
//do nut use platform specific headers. 

#define OSAL_LITTLEENDIAN


// Folgender Schalter ist gesetzt zur Auswahl der Betriebssystemplattform Windows ist. Damit können Betriebssystemzugriffe bedingt compiliert werden.
#define __OS_IS_WINDOWS__

// Folgender Schalter ist gesetzt zur Auswahl des Compilers MSC6. Damit können spezifische Compilereigenschaften mittels bedingter Compilierung berücksichtigt werden.
#define __COMPILER_IS_MSC6__

// Folgender Schalter ist gesetzt zur Auswahl der C++-Compilierung. In einigen Quellen kann es einen Unterschied geben, wenn eine C-Abbildung der Quellen
// berücksichtigt werden muss. Das standardgemäß definierte Makro __cplusplus ist nicht immer für dieses Zweck verwendbar, da auch der Einsatz der C++-Compilierung
// für eine C-Abbildung erfolgen kann. Wenn die Gesamt-Source (beispielsweise ein Headerfile) gegebenenfalls auch als C-Abbildung
// compiliert werden muss, dann müssen alle Teile, die nur unter C++ lauffähig sind, mit diesem Schalter bedingt compiliert werden.
#define __CPLUSGEN

#define MemUnit char            //sizeof(MemUnit) muss 1 sein!
#define BYTE_IN_MemUnit 1       //im PC gilt: 1 MemUnit = 1 Byte
#define BYTE_IN_memUnit 1       //@deprecated

/**All types with fix byte-wide should be defined in a platform-valid form. */
typedef unsigned char        uint8;
typedef unsigned short       uint16;
typedef unsigned long        uint32;

typedef signed char          int8;
typedef short                int16;
typedef long                 int32;
typedef __int64              int64;

/**int-type which can represent a standard pointer. */
#define intPTR long


/**Die Definition spezieller Typen für variable Argumentlisten ist daher notwendig, 
 * weil der GNU-Compiler bei variablen Argumenten int8 und int16 auf int promoted.
 * Bei Visual Studio ist das aber nicht so.
 * Verwendung nur in va_arg(..,TYP)-Makro.
 */
typedef char                 char_va_list;
typedef bool                 bool_va_list;
typedef signed char          int8_va_list;
typedef short                int16_va_list;
//typedef float                float_va_list;
typedef double                float_va_list;


typedef float                float32;

//plattformunabhaengige Ergaenzungen
//folgende Typen sind besser schreib- und lesbar
#define ushort unsigned short int
#define uint unsigned int
#define ulong unsigned long int

//NULL soll nach wie vor fuer einen 0-Zeiger verwendet werden duerfen.
//Hinweis: (void*)(0) kann nicht einem typisiertem Zeiger zugewiesen werden, wohl aber 0
#undef  NULL
#define NULL 0
#undef null
#define null 0

// Folgendes Define wird nach einer Struktur insbesondere für GNU-Compiler verwendet. Es ist für MSC6 leer,
// weil stattdessen ein pragma pack(1) verwendet werden muss.
#define GNU_PACKED

// Das Makro METHOD_C soll für C++ regeln, ob eine Methode/Routine/Funktion ggf. auch in C++-Umgebungen nach den C-Konventionen definiert ist.
// Für eine reine C-Compilierung ist das Makro leer. Dann erscheint beim Linken nicht das Label nach C- sondern nach C++-Konventionen.
// Beide Varianten können zweckmäßig sein. Hier ist die C++-Labeling-Variante gewählt.   
#ifdef __cplusplus
  //#define METHOD_C extern "C"
  #define METHOD_C
#else
  #define METHOD_C
#endif

/**Ein EventHandle ist unter Windows ein HANDLE und dort als void* definiert. 
 * Der Anwender soll aber keine windows.h und ähnliches einziehen sollen (zuviel Müll), 
 * daher hier die Zurückführung auf die Schnittmenge zwischen windows.h und dem Rest der Welt.
 */
#define OS_HandleEvent void*   



/**A OS_intPTR is the representation of a pointer in a int variable. 
 * Often a pointer is representable in a normal int, 
 * but there may be some segmentation and other designations.
 * Using this type, the programming expresses what it is meaned.
 */
#define OS_intPTR int

/**This class provides basic working with memory allocation and its associated amount of bytes.
 * The Problem in C is: a Pointer to memory is not interpretable. It is a void* mostly.
 * How many bytes are there, it is unknown.
 *
 * The struct MemJc contains both:
 * * The pointer to the data as pointer to memory layout.
 * * The size of data in memory.
 * But there is no type info.
 */
typedef struct OS_PtrValue_t
{ void const* ptr__;
  int32 value__;
  //struct MemAreaC_t* ptr__;
  //union{ struct MemAreaC_t* memArea; char* str; } ptr__;
}OS_PtrValue;

/**A const definition takes 2 arguments, but the type of them depends from operation system.
 * @param VAL a value from a int-type
 * @param PTR a pointer from a type*-type.
 */
#define CONST_OS_PtrValue(VAL, PTR) { (char*) PTR, (int32)VAL}

/**An instance which contains null-values. */
extern OS_PtrValue null_OS_PtrValue;



#define value_OS_PtrValue(THIS) ((THIS).value__)

#define PTR_OS_PtrValue(THIS, TYPE) ((TYPE*)(THIS).ptr__)

#define set_OS_PtrValue(THIS, PTR, INT) { (THIS).ptr__ = (PTR); (THIS).value__ = (INT); } //(char* )

#define setValue_OS_PtrValue(THIS, INT) { (THIS).value__ = (INT); }

#define setPtr_OS_PtrValue(THIS, PTR) { (THIS).ptr__ = (char*)(PTR); }



#endif  //__os_types_def_h__
