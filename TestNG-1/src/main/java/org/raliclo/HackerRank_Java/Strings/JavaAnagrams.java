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

import java.util.Arrays;
import java.util.Scanner;

public class JavaAnagrams {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        String a = scan.next();
        String b = scan.next();
        scan.close();
        boolean ret = isAnagram(a, b);
        System.out.println((ret) ? "Anagrams" : "Not Anagrams");
    }

    static boolean isAnagram(String a, String b) {
        if (a.length() != b.length()) {
            return false;
        }
        String[] aa = a.toLowerCase().split("");
        String[] bb = b.toLowerCase().split("");
        Arrays.sort(aa);
        Arrays.sort(bb);
        for (int i = 0; i < aa.length; i++) {
//            System.out.println(aa[i]+" "+bb[i]);
            if (!aa[i].equals(bb[i])) {
                return false;
            }
        }
        return true;
    }
}