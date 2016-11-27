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

//https://www.hackerrank.com/contests/world-codesprint-6/challenges/abbr

import java.util.Scanner;

public class Abbreviation {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int q = in.nextInt();
        for (int i = 0; i < q; i += 2) {
            String a = in.next();
            String b = in.next();
            System.out.println(querystring(a, b));
        }
    }

    public static String querystring(String a, String b) {
        if (a.length() < b.length()) {
            return "NO";
        }
        if (a.length() >= b.length()) {
            String aa = a.toUpperCase();
            for (int i = 0; i < a.length() - b.length(); i++) {
                if (aa.substring(i, i + b.length()).equals(b)) {
                    return doublecheck(a, b, i);
                }
            }
        }
        return "NO";
    }

    public static String doublecheck(String a, String b, int ind) {
        int ai = 'A';
        int zi = 'Z';
        String[] bb = b.split("");
        String[] tempa = a.split("");
        String[] aa = new String[bb.length];
        for (int i = 0; i < ind; i++) {
            if ((int) tempa[i].charAt(0) >= ai && (int) tempa[i].charAt(0) <= zi) {
                return "NO";
            }
        }
        for (int i = ind + bb.length; i < a.length(); i++) {
            if ((int) tempa[i].charAt(0) >= ai && (int) tempa[i].charAt(0) <= zi) {
                return "NO";
            }
        }
        return "YES";
    }
}
