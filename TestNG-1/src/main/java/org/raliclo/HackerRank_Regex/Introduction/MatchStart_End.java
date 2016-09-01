//https://www.hackerrank.com/challenges/matching-start-end
package org.raliclo.HackerRank_Regex.Introduction;/**
 * Created by raliclo on 8/31/16.
 * Project Name : TestNG-1
 */

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchStart_End {

    public static void main(String[] args) {
        Regex_Test tester = new Regex_Test();
        tester.checker("^(\\d){1}(\\w){4}\\.$");

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
