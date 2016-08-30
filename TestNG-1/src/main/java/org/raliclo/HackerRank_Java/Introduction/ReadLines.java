package org.raliclo.HackerRank_Java.Introduction;

/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class ReadLines {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> lines= reader.lines().collect(Collectors.toCollection(ArrayList::new));
        for (String x: lines
             ) {
            System.out.println(x);
        }
    }

}
