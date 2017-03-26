package org.raliclo.HackerRank_Competitions.Contests.C101Hack47;
// https://www.hackerrank.com/contests/101hack47/challenges/modular-game-of-numbers

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by raliclo on 21/03/2017.
 */

public class ModularGameOfNumbers {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out);
        int n = sc.nextInt();
        int x = sc.nextInt(), y = sc.nextInt();

        int [] a = new int[x], b = new int[y];
        for (int i = 0; i < x; i++)
            a[i] = sc.nextInt();
        for (int i = 0; i < y; i++)
            b[i] = sc.nextInt();

        int [] cnt = new int[n];
        for (int i = 0; i < x; i++)
            for (int j = 0; j < y; j++)
                cnt[(a[i] + b[j]) % n]++;

        int min = x + y + 3;
        int res = 0;
        for (int i = n - 1; i >= 0; i--)
        {
            if (cnt [i] < min) {
                min = cnt[i];
                res = n - i;
            }
        }
        out.println(res);
        out.flush();
        out.close();
    }

}
