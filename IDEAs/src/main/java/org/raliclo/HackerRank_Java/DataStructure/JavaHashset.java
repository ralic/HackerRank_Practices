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
//https://www.hackerrank.com/challenges/java-hashset

import java.util.HashSet;
import java.util.Scanner;

public class JavaHashset {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int t = s.nextInt();
        String[] pair_left = new String[t];
        String[] pair_right = new String[t];

        for (int i = 0; i < t; i++) {
            pair_left[i] = s.next();
            pair_right[i] = s.next();
        }

        // Code starts here
        HashSet<String> set = new HashSet<>();
        for (int i = 0; i < t; i++) {
            String joint = pair_left[i].concat(" ").concat(pair_right[i]);
//            System.out.println(joint);
            set.add(joint);
            System.out.println(set.size());
        }
    }

}
