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

package org.raliclo.HackerRank_Java.Strings;/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */
//https://www.hackerrank.com/challenges/java-string-tokens?h_r=next-challenge&h_v=zen

import java.util.Arrays;
import java.util.Scanner;

public class JavaStringTokens {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        ArrayList<String> x = reader.lines().collect(Collectors.toCollection(ArrayList::new));
//        String s=x.get(0);
        Scanner scan = new Scanner(System.in);
        String s = scan.nextLine();
        scan.close();
        System.out.println(s.trim());
        if (s.trim().length() == 1) {
            System.out.println(1);
            System.out.println(s.trim());
        } else {
            String[] list = s.trim().split("[ !,?._'@]+");
            if (list.length != 1) {
                System.out.println(list.length);
                Arrays.stream(list).forEach(System.out::println);
            } else {
                System.out.println(0);
            }
        }
    }

}
