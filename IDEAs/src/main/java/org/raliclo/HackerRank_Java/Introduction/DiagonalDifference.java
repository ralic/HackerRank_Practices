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

package org.raliclo.HackerRank_Java.Introduction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */
public class DiagonalDifference {

    public static void main(String[] args) throws IOException {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int lines = Integer.parseInt(reader.readLine());
//        System.out.println(lines);
        ArrayList<String> llist = reader.lines().collect(Collectors.toCollection(ArrayList::new));
//        System.out.println(llist);
        int l2r = 0;
        int r2l = 0;
        for (int i = 0; i < lines; i++) {
            String[] tmp = llist.get(i).split(" ");
            l2r += Integer.parseInt(tmp[i]);
            r2l += Integer.parseInt(tmp[lines - i - 1]);
        }
        int ans = (l2r > r2l) ? l2r - r2l : r2l - l2r;
        System.out.println(ans);
    }

}
