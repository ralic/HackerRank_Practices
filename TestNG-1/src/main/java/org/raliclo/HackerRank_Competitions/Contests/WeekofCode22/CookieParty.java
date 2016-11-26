package org.raliclo.HackerRank_Competitions.Contests.WeekofCode22;/**
 * Created by raliclo on 8/8/16.
 * Project Name : TestNG-1
 */

// SOLVED
// https://www.hackerrank.com/contests/w22/challenges/cookie-party/submissions/code/6561291


// Input 2 4
// Output 0

import java.util.Scanner;

public class CookieParty {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt(); // guests
        int m = in.nextInt(); // cookies

        if (m % n == 0) {
            System.out.println(0);
        } else if (n == 1) {
            System.out.println(0);
        } else {
            System.out.println(n - (m % n));
        }
    }
}
