package org.raliclo.HackerRank_Java.BigNumber;

/**
 * Created by raliclo on 7/26/16.
 * Project Name : TestNG-1
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

//https://www.hackerrank.com/challenges/java-bigdecimal
/*
Sample Input

9
-100
50
0
56.6
90
0.12
.12
02.34
000.000
Sample Output

90
56.6
50
02.34
0.12
.12
0
000.000
-100
 */

public class JavaBigDecimal {


    public static void main(String[] args) {
        //Input
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String[] s = new String[n + 2];
        for (int i = 0; i < n; i++) {
            s[i] = sc.next();
        }
        sc.close();
        // Code starts here

        ArrayList<String> xx = new ArrayList<>();
        for (int i = 0; i < s.length; i++) {
            if (s[i] != null) {
                xx.add(s[i]);
            }
        }

        xx.sort((a, b) -> new BigDecimal(b).compareTo(new BigDecimal(a)));

        for (int i = 0; i < s.length; i++) {
            if (s[i] != null) {
                s[i] = xx.get(i);
            }
        }

        //Output
        for (int i = 0; i < n; i++) {
            System.out.println(s[i]);
        }
    }
}
