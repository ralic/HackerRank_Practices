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

package org.raliclo.HackerRank_Algorithm.Exams;/**
 * Created by raliclo on 9/1/16.
 * Project Name : TestNG-1
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;


public class Segment {


    static int segment(int x, int[] arr) {
        int max = Integer.MIN_VALUE;
//        java.util.ArrayList<Integer> list = new java.util.ArrayList<>();

        for (int i = 0; i <= arr.length - x; i++) {
            int[] newarr = new int[x];
            int k = i;
            for (int j = 0; j < x; j++) {
                newarr[j] = arr[k++];
            }
//            Arrays.stream(newarr).forEach(System.out::print);
//            System.out.println();
//            list.add(findmin(newarr));
            int localmin =findmin(newarr);
            if (max < localmin) {
                max = localmin;
            }
        }
        System.out.println(max);
//        Object[] xx = list.stream().sorted().toArray();
//        System.out.println(list);
//        System.out.println(Integer.parseInt(xx[xx.length - 1].toString()));
//        return Integer.parseInt(xx[xx.length - 1].toString());
        return max;
    }

    static int findmin(int[] arr) {
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < ans) {
                ans = arr[i];
            }
        }
        return ans;
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        final String fileName = "Test";
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
        int res;
        int _x;
        _x = Integer.parseInt(in.nextLine().trim());


        int _arr_size = 0;
        _arr_size = Integer.parseInt(in.nextLine().trim());
        int[] _arr = new int[_arr_size];
        int _arr_item;
        for (int _arr_i = 0; _arr_i < _arr_size; _arr_i++) {
            _arr_item = Integer.parseInt(in.nextLine().trim());
            _arr[_arr_i] = _arr_item;
        }

        res = segment(_x, _arr);
        bw.write(String.valueOf(res));
        bw.newLine();

        bw.close();
    }
}
