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

//https://www.hackerrank.com/challenges/phone-book

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class JavaMap {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));

        int n = Integer.parseInt(input.get(0));

        HashMap<String, Integer> map = new HashMap<>();

        for (int i = 0; i < n; i++) {
//            System.out.println(input.get(1+i*2));
//            System.out.println(input.get(2+i*2));
            map.put(input.get(1 + i * 2), Integer.parseInt(input.get(2 + i * 2)));
        }
//        System.out.println(map);

        for (int i = 2 * n + 1; i < input.size(); i++) {
//            System.out.println(input.get(i));
            String arg = input.get(i);
            if (map.containsKey(arg)) {
                System.out.println(arg + "=" + map.get(arg));
            } else {
                System.out.println("Not found");
            }
        }
    }

}
