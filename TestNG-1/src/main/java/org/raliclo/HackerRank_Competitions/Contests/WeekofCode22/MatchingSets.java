package org.raliclo.HackerRank_Competitions.Contests.WeekofCode22;/**
 * Created by raliclo on 8/11/16.
 * Project Name : TestNG-1
 */
//https://www.hackerrank.com/contests/w22/challenges/box-moving
// TODO : Code Improvement Required



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MatchingSets {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        int n = Integer.parseInt(input.get(0));
        int[] X = Arrays.stream(input.get(1).split(" ")).mapToInt(Integer::parseInt).toArray();
        int[] Y = Arrays.stream(input.get(2).split(" ")).mapToInt(Integer::parseInt).toArray();
        if (sum(X) == sum(Y)) {
            delta(X,Y);
        } else {
            System.out.println(-1);
        }
    }

    public static int sum(int[] x) {
        int[] sum = new int[1];
        Arrays.stream(x).forEach((mm) -> {
            sum[0] += mm;
        });
        return sum[0];
    }

    public static void delta(int[] x, int[] y) {

        int[] delta = new int[x.length];

        int count=0;
        for (int i = 0; i < x.length; i++) {
            delta[i] = x[i] - y[i];
        }

        for ( int i=0;i<x.length-1;i++) {
            int tmp=Math.abs(delta[i]);
            if (delta[i] <0) {
                count+=tmp;
                delta[i]=0;
                delta[i+1]-=tmp;
            }
            if (delta[i]>0) {
                count+=tmp;
                delta[i]--;
                delta[i+1]+=tmp;
            }
        }


        System.out.println(count);
    }
}

/*
3
1 1 4
2 2 2
ans =3
 */

/*

        for ( int i=0;i<x.length-1;i++) {
            while (delta[i] <0) {
                count++;
                delta[i]++;
                delta[i+1]--;
            }
            while (delta[i]>0) {
                count++;

                delta[i]--;
                delta[i+1]++;
            }
        }

 */

/*

        for ( int i=0;i<x.length-1;i++) {
            while (delta[i] <0) {
                delta[i+1]-=delta[i];
                count-= delta[i];
                delta[i]=0;
            }
            while (delta[i]>0) {
                delta[i+1]+=delta[i];
                count+= delta[i];
                delta[i]=0;
            }
        }
        System.out.println(count);
 */