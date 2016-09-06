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
//https://www.hackerrank.com/contests/infinitum16-firsttimer/challenges/solve-equations
/* Input

ax+by=c
1x + 2000y= 999

IN
9
1 2000 999
1 100 21
2 3 1
2 5 19
2 5 3
2 20 8
1000000 100000000 100000000
2134     1234567    98765432
77777 999998 100000000000000

OUT
999 0
21 0
2 -1
2 3
4 -1
4 0
100 0
307774 -452
142214 99989139
*/

// TODO Partially Solved

import java.math.BigDecimal;
import java.util.Scanner;

public class SolveEquations {
    static int precision = 10;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long q = in.nextLong();
        Point ans;
        while (q-- > 0) {
            BigDecimal a = BigDecimal.valueOf(in.nextLong()).setScale(0);
            BigDecimal b = BigDecimal.valueOf(in.nextLong()).setScale(0);
            BigDecimal c = BigDecimal.valueOf(in.nextLong()).setScale(0);
            ans = solver(a, b, c);
            System.out.println(ans.x + " " + ans.y);
        }
    }

    static Point solver(BigDecimal a, BigDecimal b, BigDecimal c) {
        BigDecimal x = BigDecimal.ONE;
        BigDecimal y;
        BigDecimal dist;
//        BigDecimal z;
//        // a*x+b*y=c
//        if (c.compareTo(b)==1) {
//            y=c.divide(b,BigDecimal.ROUND_DOWN);
//            z=c.subtract(b.multiply(y)).divide(x,BigDecimal.ROUND_DOWN).subtract(BigDecimal.ONE);
//            if (z.compareTo(BigDecimal.ONE)==1) {
//                x=z;
//            }
//        } else if (c.compareTo(b)==-1) {
//            z=c.divide(a,BigDecimal.ROUND_DOWN).subtract(BigDecimal.ONE);
//            if (z.compareTo(BigDecimal.ONE)==1) {
//                x=z;
//            }
//        }
        while (true) {
            //y= (c-ax)/b
            y = c.subtract(a.multiply(x)).divide(b, BigDecimal.ROUND_DOWN).setScale(precision, BigDecimal.ROUND_DOWN);
//            z = c.subtract(a.multiply(x)).divide(b,1); // (c-a*x)/b
//            System.out.println(x + " " + y );
//            System.out.println(c.subtract(a.multiply(x)).subtract(y.multiply(b)).equals(BigDecimal.ZERO)); // (c - a * x - (int) Math.floor(y) * b)
            // dist=c-b*y+a*x
            dist = c.subtract(a.multiply(x)).subtract(y.setScale(0).multiply(b)).setScale(precision, BigDecimal.ROUND_DOWN);
            ;
            if (dist.equals(BigDecimal.ZERO.setScale(precision, BigDecimal.ROUND_DOWN))) {
                return new Point(x, y);
            }
            x = x.add(BigDecimal.ONE);
        }
    }

    static class Point {
        BigDecimal x;
        BigDecimal y;

        Point(BigDecimal X, BigDecimal Y) {
            this.x = X.setScale(0, BigDecimal.ROUND_DOWN);
            this.y = Y.setScale(0, BigDecimal.ROUND_DOWN);
        }
    }
}
