package org.raliclo.HackerRank_Java.Strings;/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/challenges/java-strings-introduction

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class JavaStringsIntroduction {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> x = reader.lines().collect(Collectors.toCollection(ArrayList::new));

        String A = x.get(0);
        String B = x.get(1);
        String yesno = A.charAt(0) > B.charAt(0) ? "Yes" : "No";

        System.out.println(A.length() + B.length());
        System.out.println(yesno);
        System.out.println(firstCap(A)+" "+firstCap(B));
    }

    public static String firstCap(String x) {
       char first=(char) ((int)(x.charAt(0)-32));
       return first+x.substring(1,x.length());
    }
}
