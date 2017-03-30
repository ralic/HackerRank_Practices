package org.raliclo.HackerRank_Competitions.Contests.C101Hack47;

//https://www.hackerrank.com/contests/101hack47/challenges/permutation-possibility

/**
 * Created by raliclo on 21/03/2017.
 */

import java.util.HashMap;
import java.util.Scanner;

public class PermutationPossibility {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int m = in.nextInt();
        int[] s = new int[m];
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        HashMap<Integer, Integer> book = new HashMap<>();
        for (int s_i = 0; s_i < m; s_i++) {
            s[s_i] = in.nextInt();
//            System.out.println(s[s_i]);
            if (book.containsKey(s[s_i])) {
                System.out.println("NO");
                return;
            } else {
                book.putIfAbsent(s[s_i], 1);
            }
            if (s[s_i] >= max) {
                max = s[s_i];
            }
            if (s[s_i] < min) {
                min = s[s_i];
            }
        }
//        System.out.println(book);
//        System.out.println(book.size() + " " + max);
        if (book.size() < max) {
            System.out.println("YES");
        } else if (book.size() == max) {
            if (max == m) {
                System.out.println("YES");
            } else {
                System.out.println("NO");
            }
        } else {
            System.out.println("YES");
        }
    }
}
