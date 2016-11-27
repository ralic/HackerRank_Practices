package org.raliclo.HackerRank_Java.ThrityDaysOfCode;

import java.util.Scanner;

public class _03IntroToConditionalStatements {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int input = in.nextInt();

        if ((input & 1) == 1) {
            System.out.println("Weird");
        } else if (input <= 5 && input >= 2) {
            System.out.println("Not Weird");
        } else if (input >= 6 && input <= 20) {
            System.out.println("Weird");
        } else if (input > 20) {
            System.out.println("Not Weird");
        }
    }
}

