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
        int line3 = Integer.parseInt(input.get(2));

        int sum = 0;
        for (int i = 0; i < line2.length; i++) {
            if (i != k) {
                sum += line2[i];
            }
        }
        if (sum / 2 == line3) {
            System.out.println("Bon Appetit");
        } else {
            System.out.println(line3 - sum / 2);
        }
    }

}
