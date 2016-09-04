/*
 * Copyright 2016 Ralic Lo<raliclo@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

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
