package org.raliclo.HackerRank_Java.ExceptionHandling;/**
 * Created by raliclo on 8/2/16.
 * Project Name : TestNG-1
 */

import java.util.Scanner;
//Write your code here

class MyCalculator {

    public int power(int n, int p) throws Exception{
        if (n < 0 || p < 0) {
                throw new Exception("n and p should be non-negative");
        }
        int ans = 1;
        while (p > 0) {
            ans *= n;
            p--;
        }
        return ans;
    }
}

public class JavaExceptionHandling2 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        while (in.hasNextInt()) {
            int n = in.nextInt();
            int p = in.nextInt();
            MyCalculator my_calculator = new MyCalculator();
            try {
                System.out.println(my_calculator.power(n, p));
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
