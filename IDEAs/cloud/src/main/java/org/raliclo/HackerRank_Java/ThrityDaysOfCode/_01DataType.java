//https://www.hackerrank.com/challenges/30-data-types
package org.raliclo.HackerRank_Java.ThrityDaysOfCode;

import java.util.Scanner;

public class _01DataType {

    public static void main(String[] args) {
        int i = 4;
        double d = 4.0;
        String s = "HackerRank ";
        Scanner scan = new Scanner(System.in);
        /* Declare second integer, double, and String variables. */
        /* Read and save an integer, double, and String to your variables.*/
        /* Print the sum of both integer variables on a new line. */
        /* Print the sum of the double variables on a new line. */
        /* Concatenate and print the String variables on a new line;
            the 's' variable above should be printed first. */

        System.out.println(i + scan.nextInt());
        System.out.println(d + scan.nextDouble());
//        while (scan.hasNext()) {
//            s=s.concat(scan.next()).concat(" ");
//        }
        scan.nextLine();
        System.out.println(s + scan.nextLine());
        scan.close();
    }
}