package org.raliclo.HackerRank_Java.ThrityDaysOfCode;

import java.util.Scanner;

/**
 * Created by raliclo on 12/10/2016.
 */
public class _05Loops {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        for (int i = 1; i < 11; i++) {
            System.out.println(n + " x " + i + " = " + n * i);
        }
    }
}
