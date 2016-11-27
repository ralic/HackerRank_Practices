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

//https://www.hackerrank.com/tests/71cm75bcsli/questions/6h5sacnq5l3

import java.util.Scanner;

public class AsheleyLoveNumbers {
    // Code starts here
    static void countNumbers(int[][] arr) {


        int x = arr.length;
        int y = arr[0].length; // always <2
//        System.out.println(x + " " + y);
        for (int i = 0; i < x; i++) {
//            for (int j = 0; j < y; j++) {
//                System.out.println(arr[i][0]+" "+arr[i][1]);
//            int total = arr[i][1] - arr[i][0];
            int count = 0;

            for (int j = arr[i][0]; j <= arr[i][1]; j++) {
                String[] digits = String.valueOf(j).split("");
                if (checker(digits)) {
                    count++;
                }
            }
            System.out.println(count);
//            }
        }
    }


    static boolean checker(String[] digits) {
        for (int i = 0; i < digits.length - 1; i++) {
            if (digits[i].compareTo(digits[i + 1]) == 0) {
                return false;
            }
        }
        return true;
    }

    // Code end here

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int _arr_rows = 0;
        int _arr_cols = 0;
        _arr_rows = Integer.parseInt(in.nextLine().trim());
        _arr_cols = Integer.parseInt(in.nextLine().trim());

        int[][] _arr = new int[_arr_rows][_arr_cols];
        for (int _arr_i = 0; _arr_i < _arr_rows; _arr_i++) {
            for (int _arr_j = 0; _arr_j < _arr_cols; _arr_j++) {
                _arr[_arr_i][_arr_j] = in.nextInt();

            }
        }

        if (in.hasNextLine()) {
            in.nextLine();
        }

        countNumbers(_arr);

    }
}



