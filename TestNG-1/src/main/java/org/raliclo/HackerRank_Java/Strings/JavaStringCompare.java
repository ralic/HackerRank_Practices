package org.raliclo.HackerRank_Java.Strings;/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/challenges/java-string-compare

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class JavaStringCompare {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> x = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        String input = x.get(0);
        int nums = Integer.parseInt(x.get(1));
        ArrayList<String> bag = new ArrayList<>();
        for (int i = 0; i < input.length() - nums + 1; i++) {
            bag.add( input.substring(i, i + nums));
        }
        String[] xx= new String[bag.size()];
        bag.toArray(xx);
        Arrays.sort(xx);
//        Stream.of(xx).forEach(System.out::println);
        System.out.println(xx[0]);
        System.out.println(xx[xx.length-1]);
    }

}
