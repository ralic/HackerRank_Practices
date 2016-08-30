package org.raliclo.HackerRank_Java.Introduction;

/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */

import java.util.Scanner;

public class Java_If_Else {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int x = in.nextInt();
        if (x % 2 == 1) {
            System.out.println("Weird");
        } else if (x<=5&&x>=2) {
            System.out.println("Not Weird");
        } else if (x<=20&&x>=6) {
            System.out.println("Weird");
        }else if(x>20) {
            System.out.println("Not Weird");
        }
    }
}
