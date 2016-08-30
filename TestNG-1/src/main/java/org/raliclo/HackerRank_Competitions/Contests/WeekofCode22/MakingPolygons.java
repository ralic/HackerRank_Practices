package org.raliclo.HackerRank_Competitions.Contests.WeekofCode22;/**
 * Created by raliclo on 8/9/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/contests/w22/challenges/polygon-making

import java.util.Scanner;

public class MakingPolygons {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt(); // sticks
        int a[] = new int[n];
        for (int a_i = 0; a_i < n; a_i++) {
            a[a_i] = in.nextInt();
        }
        int left = 0;
        int right = a[a.length - 1];
        for (int i = 0; i < a.length - 1; i++) {
            left += a[i];
        }
//        System.out.println(left+" "+right);
        if (left > right) {
            System.out.println(0);
        } else {
            System.out.println(count(a,left, right));
        }
    }

    public static int count(int[] a,int left, int right) {
        int count = 0;
        while (right >= left) {
//            System.out.println(left + " " + right);
            left += (right - a[a.length-2]);
            right -= (right - a[a.length-2]);
//            System.out.println(left + " " + right);

            count++;
        }
        return count;
    }
}