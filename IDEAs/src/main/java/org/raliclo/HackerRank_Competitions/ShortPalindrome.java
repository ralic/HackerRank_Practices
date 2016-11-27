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
Consider a string, , of  lowercase English letters where each character,  (, denotes the letter at index  in . We define an  palindromic tuple of  to be a sequence of indices in  satisfying the following criteria:

, meaning the characters located at indices  and  are the same.
, meaning the characters located at indices  and  are the same.
, meaning that , , , and  are ascending in value and are valid indices within string .
Given , find and print the number of  tuples satisfying the above conditions. As this value can be quite large, print it modulo .

Input Format

A single string denoting .

Constraints

It is guaranteed that  only contains lowercase English letters.
Output Format

Print the the number of  tuples satisfying the conditions in the Problem Statement above. As this number can be very large, your answer must be modulo .

Sample Input 0

kkkkkkz
Sample Output 0

15
Explanation 0

The letter z will not be part of a valid tuple because you need at least two of
the same character to satisfy the conditions defined above. Because all tuples consisting
of four k's are valid, we just need to find the number of ways that we can choose four of
the six k's. This means our answer is  C(6,4) mod (10^9+7)=15.

Sample Input 1

ghhggh

Sample Output 1

4
Explanation 1

The valid tuples are:
(0,1,2,3)
(0,1,2,4)
(1,3,4,5)
(2,3,4,5)

Thus, our answer is 4 mod (10^9+7)=4 .
 */


import java.math.BigInteger;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ShortPalindrome {
    static BigInteger ans = BigInteger.ZERO;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String x = in.nextLine();
        HashSet<String> example = StringHelper.brutalIndex(x.length(), 4);
        for (String tmp : example) {
            int i = 0;
            char[] box = new char[4];
            for (int j = 0; j < tmp.length(); j++)
                if (tmp.charAt(j) == '1') {
                    box[i] = x.charAt(j);
                    i++;
                }
            checkPal(box);

        }
        ans = ans.divideAndRemainder(BigInteger.valueOf(1000000007))[1];
        System.out.println(ans);
    }

    public static void checkPal(char[] box) {
        if (box[3] == box[0] && box[1] == box[2]) {
            System.out.println(box);
            ans = ans.add(BigInteger.ONE);
        }
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


