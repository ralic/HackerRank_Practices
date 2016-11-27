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
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.regex.Pattern;
//import java.util.stream.Collectors;
//
//public class JavaRegex {
//
//    public static void main(String[] args) {
//        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        ArrayList<String> x = reader.lines().collect(Collectors.toCollection(ArrayList::new));
//        x.stream().forEach((xx) -> verify(xx));
//
//    }
//
//    public static void verify(String x) {
////        System.out.println(Pattern.matches("\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b", x));
//        System.out.println(Pattern.matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", x));
//    }
//
//}

import java.util.Scanner;

class JavaRegex {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String IP = in.next();
            System.out.println(IP.matches(new myRegex().pattern));
        }

    }
}

class myRegex {
    String pattern;
    String pattern2;

    myRegex() {
        this.pattern2 = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
//        this.pattern="^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
//                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."+
//                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."+
//                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    }
}