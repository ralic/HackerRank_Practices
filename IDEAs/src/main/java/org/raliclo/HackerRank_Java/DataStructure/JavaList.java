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

package org.raliclo.HackerRank_Java.DataStructure;/**
 * Created by raliclo on 8/12/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/challenges/java-list

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class JavaList {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));

        int n = Integer.parseInt(input.get(0));
        int[] info = Arrays.stream(input.get(1).split(" ")).mapToInt(Integer::parseInt).toArray();
        ArrayList<Integer> bag = new ArrayList<>();
        Arrays.stream(info).forEach((x) -> bag.add(x));

        int queops = Integer.parseInt(input.get(2));
        for (int i = 0; i < queops; i++) {
            String ops = input.get(i * 2 + 3);
            String details = input.get(i * 2 + 4);
//            System.out.println(input.get(i*2+3));
//            System.out.println(input.get(i*2+4));
            CRUD(ops, details, bag);
        }
        bag.stream().forEach((x) -> System.out.print(x + " "));
    }

    public static void CRUD(String ops, String details, ArrayList<Integer> bag) {
        switch (ops) {
            case "Insert":
                int[] args = Arrays.stream(details.split(" ")).mapToInt(Integer::parseInt).toArray();
                bag.add(args[0], args[1]);
                break;
            case "Delete":
                int arg = Integer.parseInt(details);
                bag.remove(arg);
                break;
        }
    }

}
