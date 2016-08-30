package org.raliclo.HackerRank_Competitions.Contests.WorldCodeSprint6;/**
 * Created by raliclo on 8/27/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/contests/world-codesprint-6/challenges/combination-lock

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CombinationLock {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        int[] line1 = Arrays.stream(input.get(0).split(" ")).mapToInt(Integer::parseInt).toArray();
        int[] line2 = Arrays.stream(input.get(1).split(" ")).mapToInt(Integer::parseInt).toArray();
        int[] delta = new int[line1.length];
        for (int i = 0; i < line1.length; i++) {
            delta[i] = line1[i] - line2[i];
        }
        int sum=0;
        for (int i=0;i<delta.length;i++) {
            if (Math.abs(delta[i])<=5) {
                sum+=Math.abs(delta[i]);
            } else {
                sum+=(10-Math.abs(delta[i]));
            }
        }
        System.out.println(sum);
    }
}
