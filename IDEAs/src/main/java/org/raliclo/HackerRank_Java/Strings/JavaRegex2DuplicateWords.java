package org.raliclo.HackerRank_Java.Strings;/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/challenges/duplicate-word?h_r=next-challenge&h_v=zen

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaRegex2DuplicateWords {
    public static void main(String[] args) {

        // link-1 : http://www.rubular.com/r/kn4ZMtBnny
        // link-2 : http://www.regular-expressions.info/anchors.html
        // link-3 : https://msdn.microsoft.com/en-us/library/az24scfc%28v=vs.110%29.aspx?f=255&MSPPError=-2147217396

        String pattern = "\\b(\\w+)(\\b\\W+\\b\\1\\b)*"; // regex definition
        // Make sure check the link-1 to know more
        // (\\w+) : repeated word any times.
        // \\b(word)\\b : allows you to perform a "whole words only"
        // \\W : Matches any non-word character.
        // \\1 : Joint repeated word / back reference
        Pattern r = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE); // pattern define

        Scanner in = new Scanner(System.in);
        int testCases = Integer.parseInt(in.nextLine());
        while (testCases > 0) {
            String input = in.nextLine();
            Matcher m = r.matcher(input);
            boolean findMatch = true;
            while (m.find()) {
                input = input.replaceAll(m.group(), m.group(1)); // replace
                findMatch = false;
            }
            System.out.println(input);
            testCases--;
        }
    }
}
