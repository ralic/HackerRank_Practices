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


package org.raliclo.HackerRank_Competitions.Contests.C101Hack40;
/**
 * Created by raliclo on 8/23/16.
 * Project Name : TestNG-1
 */

import java.util.HashMap;
import java.util.Scanner;

// SOLVED
//#https://www.hackerrank.com/contests/101hack40/challenges/designer-pdf-viewer

public class DesignPDFViewer {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = 26;
        int begin = 'a';
        HashMap<Character, Integer> dict = new HashMap<>();
        for (int i = 0; i < n; i++) {
            dict.put((char) begin, in.nextInt());
            begin++;
        }
        String word = in.next();
        int max = Integer.MIN_VALUE;
        int k = word.length() - 1;
        while (k > 0) {
            int lookup = dict.get(word.charAt(k));
            max = max < lookup ? lookup : max;
            k--;
        }
        System.out.println(max * word.length());
    }
}
