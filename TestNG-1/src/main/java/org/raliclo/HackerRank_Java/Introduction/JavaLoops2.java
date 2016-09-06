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

package org.raliclo.HackerRank_Java.Introduction;
/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/challenges/java-loops?h_r=next-challenge&h_v=zen

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class JavaLoops2 {

    public static void main(String[] args) {
       /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> x = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        int lines = Integer.parseInt(x.get(0));
        x.remove(0);
        for (int i = 0; i < lines; i++) {
            String[] xx = x.get(i).split(" ");
            int prev = Integer.parseInt(xx[0]);
            int base = Integer.parseInt(xx[1]);
            for (int j = 0; j < Integer.parseInt(xx[2]); j++) {
                System.out.printf("%d ", prev += adder(base, j));
            }
            System.out.println();
        }
    }

    public static int adder(int base, int index) {
        return base * (int) Math.pow(2, index);
    }

}