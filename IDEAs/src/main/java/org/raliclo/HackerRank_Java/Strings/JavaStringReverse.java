package org.raliclo.HackerRank_Java.Strings;/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/challenges/java-string-reverse?h_r=next-challenge&h_v=zen

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class JavaStringReverse {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> x = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        System.out.println(palindrome(x.get(0)));
    }

    public static String palindrome(String x) {
        int size = x.length();
        for (int i = 0; i < size / 2; i++)
            if (x.charAt(i) == x.charAt(size - i - 1)) {
                i++;
            } else {
                return "No";
            }
        return "Yes";
    }
}
