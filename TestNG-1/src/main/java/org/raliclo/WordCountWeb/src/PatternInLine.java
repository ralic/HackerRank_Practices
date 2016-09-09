package org.raliclo.WordCountWeb.src;/**
 * Created by raliclo on 9/9/16.
 * Project Name : TestNG-1
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternInLine {
    public static void checker(String Regex_Pattern, String Test_String) {
        Pattern p = Pattern.compile(Regex_Pattern);
        Matcher m = p.matcher(Test_String);
        System.out.println(m.find());
    }
}
