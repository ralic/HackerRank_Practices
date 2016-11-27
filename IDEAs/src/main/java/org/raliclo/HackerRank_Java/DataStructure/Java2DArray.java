package org.raliclo.HackerRank_Java.DataStructure;/**
 * Created by raliclo on 7/26/16.
 * Project Name : TestNG-1
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Java2DArray {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        int[][] arr2D = new int[6][6];
        int[][] arr2DSum = new int[6][6];
        int[] x = new int[1];
        int[] y = new int[1];
        input.forEach((m1) -> {
            x[0] = 0;
            Arrays.stream(m1.split(" ")).forEach((m2) -> {
                arr2D[y[0]][x[0]] = Integer.parseInt(m2);
                x[0]++;
            });
            y[0]++;
        });
//        System.out.println(arr2D[1][0]);
//        Arrays.stream(arr2D).forEach((m) -> {
//            Arrays.stream(m).forEach(System.out::print);
//            System.out.println();
//        });
        for (int i = 1; i < arr2D.length - 1; i++) {
            for (int j = 1; j < arr2D[0].length - 1; j++) {
                arr2DSum[i][j] = arr2D[i][j]
                        + arr2D[i - 1][j]
                        + arr2D[i + 1][j]
//                        + arr2D[i][j - 1]
//                        + arr2D[i][j + 1]
                        + arr2D[i - 1][j - 1]
                        + arr2D[i + 1][j + 1]
                        + arr2D[i - 1][j + 1]
                        + arr2D[i + 1][j - 1];
            }
        }
        Integer max = Integer.MIN_VALUE;
        for (int i = 1; i < arr2DSum.length - 1; i++) {
            for (int j = 1; j < arr2D[0].length - 1; j++) {
//                System.out.print(arr2DSum[i][j]+" ");
                max = max > arr2DSum[i][j] ? max : arr2DSum[i][j];
            }
//            System.out.println();
        }

        System.out.println(max);
    }

}
