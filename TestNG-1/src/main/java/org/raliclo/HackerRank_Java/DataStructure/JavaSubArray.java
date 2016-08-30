package org.raliclo.HackerRank_Java.DataStructure;/**
 * Created by raliclo on 7/30/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/challenges/java-negative-subarray
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class JavaSubArray {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        int[] info = Arrays.stream(input.get(1).split(" ")).mapToInt(Integer::parseInt).toArray();
//        Arrays.stream(info).forEach(System.out::println);
        int ans = 0;
        for (int i = 0; i < info.length ; i++) {
            for (int j = i; j < info.length; j++) {
//                System.out.println(i+" "+j);
                ans += check(info, i, j);
            }
        }
        System.out.println(ans);
    }

    public static int check(int[] array, int i, int j) {
        int sum = 0;
        for (int k = i; k <= j; k++) {
            sum += array[k];
        }
//        System.out.println(sum);
        return sum < 0 ? 1 : 0;
    }
}
/*
100
-463 -744 -589 -405 -321 -427 -164 -581 -613 -468 -246 -685 -869 -966 -889 -217 -712 -888 -251 -859 -969 -582 -603 -658 -49 -973 -185 -241 -439 -511 -479 -902 -255 -420 -660 -576 -848 -824 -157 -461 -644 -404 -498 -513 -722 -387 -82 -434 -275 -686 -645 -597 -268 -248 -255 -669 -573 -792 -910 -364 -303 -742 -267 -910 -162 -279 -487 -362 -103 -644 -823 -747 -400 -674 -612 -474 -61 -46 -260 -689 -732 -905 -286 -353 -505 -893 -22 -78 -37 -285 -443 -341 -27 -62 -603 -541 -341 -90 -904 -796
 */