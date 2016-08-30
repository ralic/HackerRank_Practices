package org.raliclo.HackerRank_Competitions.Contests.BookingBE;/**
 * Created by raliclo on 8/9/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/contests/booking-passions-hacked-backend/challenges/destinations-together-3

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class DestinationTogether {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        String[] info = input.get(0).split(" ");
        int n = Integer.valueOf(info[0]);
        int m = Integer.valueOf(info[1]);
        int c = Integer.valueOf(info[2]);
        int uniq = n + m - c;
        System.out.println(ways(uniq-1));
    }

    public static long ways (long ways) {
        long ans=1;
        while (ways>1) {
            ans*=ways;
            ways --;
        }
        return ans;
    }

}
