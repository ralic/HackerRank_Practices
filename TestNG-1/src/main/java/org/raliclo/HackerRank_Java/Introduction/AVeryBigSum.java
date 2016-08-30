package org.raliclo.HackerRank_Java.Introduction;

import java.math.BigInteger;
import java.util.Scanner;

/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/challenges/a-very-big-sum?h_r=next-challenge&h_v=zen

public class AVeryBigSum {
    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */

        Scanner in = new Scanner(System.in);
        String[] xx = in.nextLine().split(" ");
        String[] yy = in.nextLine().split(" ");
        in.close();
        BigInteger ans = new BigInteger("0");
        for (int i = 0; i < yy.length; i++) {
            ans = ans.add(new BigInteger(yy[i]));
        }
        System.out.println(ans);
    }
}
