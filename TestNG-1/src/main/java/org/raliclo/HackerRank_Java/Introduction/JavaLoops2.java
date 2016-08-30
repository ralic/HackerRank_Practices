package org.raliclo.HackerRank_Java.Introduction;
/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/challenges/java-loops?h_r=next-challenge&h_v=zen

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class JavaLoops2 {

    public static void main(String[] args) {
       /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> x = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        int lines = Integer.parseInt(x.get(0));
        x.remove(0);
        for (int i = 0; i < lines; i++) {
            String[] xx = x.get(i).split(" ");
            int prev = Integer.parseInt(xx[0]);
            int base = Integer.parseInt(xx[1]);
            for (int j = 0; j < Integer.parseInt(xx[2]); j++) {
                System.out.printf("%d ",prev+=adder(base,j));
            }
            System.out.println();
        }
    }

    public static int adder(int base, int index) {
        return base * (int) Math.pow(2, index);
    }

}