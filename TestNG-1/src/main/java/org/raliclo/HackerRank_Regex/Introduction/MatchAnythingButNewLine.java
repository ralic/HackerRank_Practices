package org.raliclo.HackerRank_Regex.Introduction;/**
 * Created by raliclo on 8/31/16.
 * Project Name : TestNG-1
 */
//https://www.hackerrank.com/challenges/matching-anything-but-new-line

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchAnythingButNewLine {

    public static void main(String[] args) {
        Regex_Test tester = new Regex_Test();
        tester.check("^(\\S{3}\\.){3}\\S{3}$");
    }

    static class Regex_Test {

        public void check(String pattern) {

            Scanner scanner = new Scanner(System.in);
            String testString = scanner.nextLine();
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(testString);
            boolean match = m.matches();
            System.out.format("%s", match);
        }

    }

}