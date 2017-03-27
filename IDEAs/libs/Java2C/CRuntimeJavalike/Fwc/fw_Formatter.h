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
 * @changes
 * 2010-01-21 Hartmut: Timer functionality moved to an extra include file fw_timeconversions.h
 *
 */
#ifndef __fw_Formatter_h__
#define __fw_Formatter_h__

#include "OSAL/inc/os_time.h"
#include <Fwc/fw_Va_list.h>
#include <Fwc/fw_String.h>

struct ThreadContextFW_t;
//#include "Fwc/fw_LogMessage.h" //only because Va_listFW, TODO

/*@CLASS_C ParseResultPrintfStyle_fwFormatter @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/

/**Element of a array of parse results for printf style.
 * It is a union. The common member is specifier. 
 * The element references either a text (specifier == 't') or a argument access.
 */
typedef union ParseResultPrintfStyle_fwFormatter_t
{
  /**If it is a String. */
  struct
  { /**Type of entry. 
     * t if text is present, do not use an argument value.
    */
    int8 specifier;

    /** number of chars for precesion etc, the second number. */
    int8 dummy;
  
    int16 nrofChars;
    char const* text;
    //}value
  } text;
  struct
  { /**Type of entry. 
     * * s const char*
     * * d integer
     */
    int8 specifier;

    /**Default Type of the argument. The type of argument may be determined instead from an extra string.
     */
    int8 argument;
    
    /**Nr of chars of representation. */
    int8 width;
    /**second number of chars for representation. */
    int8 precision;
    /**The argument number*/
    int8 indexArg; 
    /**The time conversion specifier, it specifier is 't' */
    int8 timeSpecifier;
  } value;
  
  
}ParseResultPrintfStyle_fwFormatter;


/**A string literal which designates a variable argument list with information.
 * It contains a type string. This is used as the first value in variable arguments
 */
extern char const typedVaArg_VaArgBuffer[];


/**Parses the printf-style control string.
 * @param dst Array of entries for parts of the control string.
 *        Each %-char, but each constant text too, creates one entry.
 * @param zDst Number of array-elements of dst. If the control-string starts with a '%'
 *        then zDst=1 is admissible. Then only this item is parsed.
 * @param src The control string.
 * @param zSrc in: Number of chars of src, out: Number of parsed chars. 
 *        The number of parsed chars is less than the in-value, if the dst-array contains 
 *        not enaugh elements to store all parse results.
 *        Especially zDst==1, then zSrc returns the number of chars for this only one item.
 *        This feature is new since 2010-06
 * @return number of found entries in dst
 */
METHOD_C int parsePrinfStyleString_fwFormatter(ParseResultPrintfStyle_fwFormatter* dst, int zDst, char const* src, int* zSrc);

/**Returns a string of argument types from given printf-style-string. 
 * This method is necessary especially for routines which processed variable arguments in a special way,
 * but don't use the printf-style format string.
 * @param typeArgs a char[..] to affiliate the type chars.
 * @param zTypeArgs lenght of the char[...]
 * @param text the text containing the type characters in the printf-style.  
 * @return number of type args chars.
 */ 
int detectTypeArgs_fwFormatter(char* typeArgs, int zTypeArgs, StringJc text);

/**Converts the value into a String representation.
 * @param zBuffer size of the buffer. If the string representation will be longer, only the left character are written.
 * @param radix of the conversion, typically 10 or 16 for hexa conversion. A value between 1..16.
 * @param minNrofCharsAndNegative If this value is negativ, and the value is negativ, a ,,-,, is written first and the value is negated before conversion.
 *                                The absolute value is the minimum of numbers, If the number will be less, left 0 are written.
 * @return number of written chars. A 0 after the string won't be written! 
           If the return value is equal zBuffer, the string representation may be incomplete because clipping of following chars.
 */
METHOD_C int toString_Integer_FW(char* buffer, int zBuffer, int32 value, int radix, int minNrofCharsAndNegative, struct ThreadContextFW_t* _thCxt);


METHOD_C int format_va_arg_Formatter_FW(struct ThreadContextFW_t* _thCxt, const char* sText, int zText, char* buffer, int zBuffer, Va_listFW vargList);


/*@CLASS_C TimeYearSecond_Formatter_FW @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/

/**structure to store the timestamp in year, month, day etc. as numeric arguments.
 */
typedef struct XXXTimeYearSecond_Formatter_FW_t
{
  int32 year; 
  int8 month;
  int8 weekInYear;
  int8 dayOfWeek;
  int8 day;  //NOTE: nDays is max 0xC22E, it should be either uint16 or int32.
  int8 hour; 
  int8 minute; 
  int8 sec;
  int8 reserve;
  

}XXXTimeYearSecond_Formatter_FW;

/**Converts the given seconds in year...second. It is the constructor of a instance of this type.
 * @param seconds The seconds in a continoues count, typical seconds from Jan, 1, 1970.
 * @param baseyear The year for start date. Mostly it should be the value 1970. 
 * @param offsetJan Number of seconds which should be added to second for the Jan, 1th of the baseyear.
 * @param GPS true than the leap seconds will be regarded. The table of leap seconds is given as default,
 *        But it is able to override globally in the application, see [[>setLeapSecondTable_TimeYearSecond_Formatter_FW]]
 */
//void ctor_TimeYearSecond_Formatter_FW(TimeYearSecond_Formatter_FW* ythis
//  , int32 seconds, int baseyear, int32 offsetJan, bool GPS
//  );



#include <Fwc/fw_timeconversions.h>

#endif //__fw_Formatter_h__
