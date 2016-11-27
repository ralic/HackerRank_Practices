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
package org.raliclo.HackerRank_Competitions.Contests.AdInfinitum16;/**
 * Created by raliclo on 9/3/16.
 * Project Name : TestNG-1
 */
//https://www.hackerrank.com/contests/infinitum16-firsttimer/challenges/hyperspace-travel

// TODO New problem definition
// This program provide how they shall travel so overall the distance for travel is the shortest.
// This program also helps to show the location

/*
Input
4 3
1 1 2
2 2 3
3 3 4
5 5 7

Output
2 2

 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

public class HyperspaceTravel_Advanced {
    static int precision = 3;
    static BigDecimal dist_min = null;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        reader.close();
        int n = Integer.parseInt(input.get(0).split(" ")[0]);
        int m = Integer.parseInt(input.get(0).split(" ")[1]);

        ArrayList<Point> list = new ArrayList<>();
        int i = 0;
        while (i++ < n) {
            list.add(new Point(input.get(i), m));
        }
        System.out.println(list);
        HashMap<ArrayList<Point>, BigDecimal> test = new HashMap<>();
        permute(list, 0, test);
        BigDecimal min = Collections.min(test.values());
        Point ans = new Point();
        getmin(test, min, ans);
        System.out.println(ans.x);
    }

    // BigDecimal square root
    public static BigDecimal bigSqrt(BigDecimal A) {
        final BigDecimal TWO = BigDecimal.valueOf(2).setScale(precision);
        BigDecimal x0 = BigDecimal.ZERO.setScale(precision);
        BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()));
        while (!x1.equals(x0)) {
            x0 = x1;
            x1 = A.divide(x0, precision, BigDecimal.ROUND_HALF_UP);
            x1 = x1.add(x0);
            x1 = x1.divide(TWO, precision, BigDecimal.ROUND_HALF_UP);
        }
        return x1;
    }

    static void permute(ArrayList<Point> arr, int k, HashMap<ArrayList<Point>, BigDecimal> test) {
        for (int i = k; i < arr.size(); i++) {
            Collections.swap(arr, i, k);
            permute(arr, k + 1, test);
            Collections.swap(arr, k, i);
            BigDecimal total = BigDecimal.ZERO;
            for (int j = 0; j < arr.size() - 1; j++) {
                total = total.add(arr.get(j).getdist(arr.get(j + 1)));
            }
            test.put((ArrayList<Point>) arr.clone(), total);
        }
    }

    static void getmin(HashMap<ArrayList<Point>, BigDecimal> test, BigDecimal min, Point ans) {
        test.forEach(
                (k, v) -> {
                    System.out.println(k + " " + v);
                    if (v.equals(min)) {
                        System.out.println(k.get((k.size() - 1) / 2));
                        ans.x = k.get((int) Math.floor(k.size() / 2)).x;
                    }
                }
        );
    }

    static class Point {
        String x;
        String[] data;
        int dim;

        Point() {
        }

        Point(String input, int m) {
            this.x = input;
            this.data = input.split(" ");
            this.dim = m;
        }

        public String toString() {
            return x;
        }

        public BigDecimal getdist(Point b) {
            BigDecimal ans = BigDecimal.ZERO;
            for (int i = 0; i < this.dim; i++) {
                BigDecimal x1 = new BigDecimal(this.data[i]);
                BigDecimal x2 = new BigDecimal(b.data[i]);
                BigDecimal p2p = x1.subtract(x2);
//                ans = p2p;
                ans = ans.add(p2p.multiply(p2p));
            }
            return bigSqrt(ans);
//            return ans;
        }
    }
}