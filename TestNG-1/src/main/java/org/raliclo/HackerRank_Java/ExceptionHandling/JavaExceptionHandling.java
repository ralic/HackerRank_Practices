package org.raliclo.HackerRank_Java.ExceptionHandling;/**
 * Created by raliclo on 8/2/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/challenges/java-exception-handling-try-catch

import java.util.Scanner;

public class JavaExceptionHandling {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        try {
            int in1, in2;
            in1 = in.nextInt();
            in2 = in.nextInt();
            if (in2 == 0) {
                throw new ArithmeticException();
            }
            System.out.println(in1 / in2);
        } catch (Exception ex) {
            if (ex.getClass().getName() == "java.lang.ArithmeticException") {
                System.out.println(ex.getClass().getName()+": / by zero");
            } else {
                System.out.println(ex.getClass().getName());
            }
        }
    }

}
