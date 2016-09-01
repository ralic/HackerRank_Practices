//https://www.hackerrank.com/challenges/matching-specific-string

package org.raliclo.HackerRank_Regex.Introduction;/**
 * Created by raliclo on 8/31/16.
 * Project Name : TestNG-1
 */

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchSpecificString {

    public static void main(String[] args) {
        Regex_Test tester = new Regex_Test();
        tester.checker("(hackerrank)");

    }

    static class Regex_Test {

        public void checker(String Regex_Pattern) {

            Scanner Input = new Scanner(System.in);
            String Test_String = Input.nextLine();
            Pattern p = Pattern.compile(Regex_Pattern);
            Matcher m = p.matcher(Test_String);
            int Count = 0;
            while (m.find()) {
                Count += 1;
            }
            System.out.format("Number of matches : %d", Count);
        }

    }
}
