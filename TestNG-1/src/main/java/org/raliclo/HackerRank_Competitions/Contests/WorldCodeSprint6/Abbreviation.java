package org.raliclo.HackerRank_Competitions.Contests.WorldCodeSprint6;/**
 * Created by raliclo on 8/27/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/contests/world-codesprint-6/challenges/abbr

import java.util.Scanner;

public class Abbreviation {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int q = in.nextInt();
        for (int i = 0; i < q; i += 2) {
            String a = in.next();
            String b = in.next();
            System.out.println(querystring(a, b));
        }
    }

    public static String querystring(String a, String b) {
        if (a.length() < b.length()) {
            return "NO";
        }
        if (a.length() >= b.length()) {
            String aa = a.toUpperCase();
            for (int i = 0; i < a.length() - b.length(); i++) {
                if (aa.substring(i, i + b.length()).equals(b)) {
                   return doublecheck(a, b, i);
                }
            }
        }
        return "NO";
    }

    public static String doublecheck(String a, String b, int ind) {
        int ai = 'A';
        int zi = 'Z';
        String[] bb = b.split("");
        String[] tempa = a.split("");
        String[] aa = new String[bb.length];
        for (int i = 0; i < ind; i++) {
            if ((int) tempa[i].charAt(0) >= ai && (int) tempa[i].charAt(0) <= zi) {
                return "NO";
            }
        }
        for (int i = ind + bb.length; i < a.length(); i++) {
            if ((int) tempa[i].charAt(0) >= ai && (int) tempa[i].charAt(0) <= zi) {
                return "NO";
            }
        }
        return "YES";
    }
}
