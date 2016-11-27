package org.raliclo.HackerRank_Java.Introduction;

/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class JavaSTDinSTDout {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> x = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        System.out.println("String: " + x.get(2));
        System.out.println("Double: " + Double.parseDouble(x.get(1)));
        System.out.println("Int: " + Integer.parseInt(x.get(0)));
    }

}
