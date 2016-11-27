package org.raliclo.HackerRank_Java.Introduction;

/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class JavaOutputFormatting {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        BufferedReader lines = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> x = lines.lines().collect(Collectors.toCollection(ArrayList::new));
        System.out.println("================================");
        for (int i = 0; i < 3; i++) {
            String[] tmp = x.get(i).split(" ");
            System.out.printf("%-15s%03d\n", tmp[0], Integer.parseInt(tmp[1]));
        }
        System.out.println("================================");

    }

}
