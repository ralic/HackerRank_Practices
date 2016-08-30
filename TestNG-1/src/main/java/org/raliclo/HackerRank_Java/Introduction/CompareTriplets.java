package org.raliclo.HackerRank_Java.Introduction;

//https://www.hackerrank.com/challenges/compare-the-triplets?h_r=next-challenge&h_v=zen

/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */

import java.util.Scanner;

public class CompareTriplets {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        String[] a = in.nextLine().split(" ");
        String[] b = in.nextLine().split(" ");
        int aa = 0;
        int bb = 0;
        for (int i = 0; i < a.length; i++) {
            if (Integer.parseInt(a[i]) > Integer.parseInt(b[i]))
            { aa++;}
            if (Integer.parseInt(a[i]) < Integer.parseInt(b[i]))
            { bb++;}
        }
        System.out.println(aa+" "+bb);

    }
}
