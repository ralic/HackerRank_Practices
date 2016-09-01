package org.raliclo.HackerRank_Regex.Introduction;/**
 * Created by raliclo on 8/31/16.
 * Project Name : TestNG-1
 */
//https://www.hackerrank.com/challenges/matching-word-non-word

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchWords {

    public static void main(String[] args) {
        Regex_Test tester = new Regex_Test();
        tester.checker("(\\w){3}(\\W+){1}(\\w+){9}(\\W+){1}(\\w+){3}"); // Use \\ instead of using \
    }

    static class Regex_Test {

        public void checker(String Regex_Pattern) {

            Scanner Input = new Scanner(System.in);
            String Test_String = Input.nextLine();
            Pattern p = Pattern.compile(Regex_Pattern);
            Matcher m = p.matcher(Test_String);
            System.out.println(m.find());
        }

    }

}