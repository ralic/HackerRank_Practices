package org.raliclo.HackerRank_Competitions.Contests.WorldCodeSprint6;/**
 * Created by raliclo on 8/27/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/contests/world-codesprint-6/challenges/bon-appetit

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Bon_Appétit {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));

        int[] NK = Arrays.stream(input.get(0).split(" ")).mapToInt(Integer::parseInt).toArray();
        int n = NK[0];
        int k = NK[1];
        int[] line2 = Arrays.stream(input.get(1).split(" ")).mapToInt(Integer::parseInt).toArray();
        int line3  = Integer.parseInt(input.get(2));

        int sum= 0;
        for (int i=0;i<line2.length;i++) {
            if (i!=k) {
                sum+=line2[i];
            }
        }
        if (sum/2==line3) {
            System.out.println("Bon Appetit");
        } else {
            System.out.println(line3-sum/2);
        }
    }

}
