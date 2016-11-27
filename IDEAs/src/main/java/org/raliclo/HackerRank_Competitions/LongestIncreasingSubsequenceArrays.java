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

package org.raliclo.HackerRank_Competitions;/**
 * Created by raliclo on 7/23/16.
 * Project Name : TestNG-1
 */
/*
We define the following:

A subsequence of an array is an ordered subset of the array's elements having the same sequential ordering as the original array. For example, the subsequences of array  are , , , , , , and .
The longest increasing subsequence of an array of numbers is the longest possible subsequence that can be created from its elements such that all elements are in increasing order.
Victoria has two integers,  and . She builds unique arrays satisfying the following criteria:

Each array contains  integers.
Each integer is .
The longest increasing subsequence she can create from the array has length .
Given  pairs of  and  values, print the number of arrays Victoria creates for each pair on a new line. As this number can be quite large, print your answer modulo .

Input Format

The first line contains a single positive integer, , denoting the number of pairs.
Each line  of the  subsequent lines contains two space-separated integers describing the respective  and values for a pair.

Constraints
1<=p<=50
1<=m<=5x10^5
1<=n<=10
n<=m

Output Format
10^9+7

On a new line for each pair, print a single integer denoting the number of different arrays Victoria creates modulo .

Sample Input
2
4 2
4 3
Sample Output

11
9
Explanation

Victoria wants to build arrays of integers having size  where each integer is  and each array has a longest increasing subsequence of length  (i.e., contains the subsequence ). She creates the following eleven arrays:
[1,1,1,2]  1 v
[1,2,1,2]  1 v
[2,1,1,2]  1 v
[2,2,1,2]  1 v

[1,1,2,1]  2 v
[1,1,2,2]  2 v
[2,1,2,1]  2 v
[2,1,2,2]  2 v

[1,2,1,2]    x
[1,2,1,1]    v
[1,2,2,1]    v
[1,2,2,2]    v

Victoria wants to build arrays of integers having size  where each integer is  and each array has a longest increasing subsequence of length  (i.e., contains the subsequence ). She creates the following nine arrays:
[1,1,2,3]  1
[2,1,2,3]  1
[3,1,2,3]  1

[1,2,3,1]  2
[1,2,3,2]  2
[1,2,3,3]  2

[1,2,1,3]  3
[1,2,2,3]  3
[1,2,3,3]  3


 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LongestIncreasingSubsequenceArrays {


    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> x = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        int lines = Integer.parseInt(x.get(0));
        x.remove(0);
        x.forEach((xx) -> ways(xx));
    }

    private static int ways(String xx) {
        int nums;
        int max;
        String[] xxx = xx.split(" ");
        nums = Integer.parseInt(xxx[0]);
        max = Integer.parseInt(xxx[1]);
        if (nums < max) {
            System.out.println(0);
            return 0;
        }
        if (nums == max) {
            System.out.println(1);
            return 1;
        }
        int ans = 0;
        HashMap<String, Integer> bag = new HashMap<>();

        int[] box = new int[nums];
        int i, j, k = 0;
        Stack<Integer> legal = new Stack<>();
        ArrayList<Integer> putindex = new ArrayList<>();

        HashSet<String> testset = StringHelper.brutalIndex(nums, max);
        HashSet<int[]> boxset = new HashSet<>();
//        System.out.println(testset);
        for (String tmp : testset) {
            j = 0;
            for (i = 0; i < tmp.length(); i++) {
                if (tmp.charAt(i) == '1') {
                    j++;
                    box[i] = j;
                } else {
                    box[i] = 0;
                }
            }
            ArrayList<Integer> permuteindex = new ArrayList<>();
            for (i = 0; i < box.length; i++) {
                if (box[i] == 0) {
                    permuteindex.add(i);
                }
            }
            recurbox(box, permuteindex, boxset, max); // backlog boxset and generate more possibility;
//            System.out.println(Arrays.toString(box));
        }
        boxcheck(box, boxset, bag);
        box = new int[nums];
        System.out.println(bag.size());

        return 1;
    }


    private static void boxcheck(int[] box, HashSet<int[]> boxset, HashMap<String, Integer> bag) {
        boxset.forEach((x) -> {
            boolean flag = true;
            for (int i = 0; i < x.length; i++) {
                if (x[i] == 0) {
                    flag = false;
                }
            }
            String unbox = Arrays.toString(x);
//            System.out.println(unbox + " " + flag);
            if (flag) {
                if (!bag.containsKey(unbox)) {
                    bag.put(unbox, 1);
                }
            }
        });
    }

    private static void recurbox(int[] box, ArrayList<Integer> permuteindex, HashSet<int[]> boxset, int max) {
        int[] tmp = Arrays.copyOf(box, box.length);
        ArrayList<Integer> tmpPermute = (ArrayList<Integer>) permuteindex.clone();
        if (permuteindex.size() > 0) {
            int replace = tmpPermute.remove(0);
            int[] legal = IntStream.range(1, max + 1).toArray();
            for (int i = 0; i < legal.length; i++) {
                tmp[replace] = legal[i];
                boxset.add(tmp);
                recurbox(tmp, tmpPermute, boxset, max);
//                System.out.println(Arrays.toString(tmp));
                boxset.add(tmp);
            }
        }
        boxset.add(tmp);
    }


    private static class StringHelper {
        public static HashSet<String> permutationFinder(String str) {
            HashSet<String> perm = new HashSet<String>();
            //Handling error scenarios
            if (str == null) {
                return null;
            } else if (str.length() == 0) {
                perm.add("");
                return perm;
            }
            char initial = str.charAt(0); // first character
            String rem = str.substring(1); // Full string without first character
            Set<String> words = permutationFinder(rem);
            for (String strNew : words) {
                for (int i = 0; i <= strNew.length(); i++) {
                    perm.add(charInsert(strNew, initial, i));
                }
            }
            return perm;
        }

        public static String charInsert(String str, char c, int j) {
            String begin = str.substring(0, j);
            String end = str.substring(j);
            return begin + c + end;
        }

        public static HashSet<String> brutalIndex(int digits, int ones) {
            if (ones > digits) {
                return null;
            }
            int[] pseudoIndex = new int[digits];
            for (int i = 0; i < digits; i++) {
                if (i < ones) {
                    pseudoIndex[i] = 1;
                }
            }
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < pseudoIndex.length; i++) {
                b.append(pseudoIndex[i]);
            }
            return permutationFinder(b.toString());
        }
    }
}
