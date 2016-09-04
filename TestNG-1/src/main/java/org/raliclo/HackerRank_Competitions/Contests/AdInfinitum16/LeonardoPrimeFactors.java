package org.raliclo.HackerRank_Competitions.Contests.AdInfinitum16;/**
 * Created by raliclo on 9/3/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/contests/infinitum16-firsttimer/challenges/leonardo-and-prime
/*
IN
8
1
2
3
500
5000
10000000000
1000000000000000001
1000000000000000000

OUT
0
1
1
4
5
10
 */

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class LeonardoPrimeFactors {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long q = in.nextLong();

        HashMap<Long, Boolean> map = new HashMap<>();
        for (long i = 0; i < q; i++) {
            long query = in.nextLong();
            HashSet<Long> factors = new HashSet<>();
            BigInteger temp = BigInteger.ONE;
            for (long j = 2; (j <= query) && (temp.compareTo(BigInteger.valueOf(query)) == -1); j++) {
                if (isPrime(j, map)) {
                    temp = temp.multiply(BigInteger.valueOf(j));
                    if (temp.compareTo(BigInteger.valueOf(query)) <= 0) {
                        factors.add(j);
//                        System.out.println("Query:" + query + " j:" + j + " temp:" + temp + " count:" + factors.size());
                    }
                }
            }
            System.out.println(factors.size());
        }

    }

    static boolean isPrime(long n, HashMap<Long, Boolean> map) {
        if (map.containsKey(n)) {
//            System.out.println("repeated query:"+n);
            return map.get(n);
        }
        switch (String.valueOf(n)) {
            case "1":
                map.put((long) 1, false);
                return false;
            case "2":
                map.put((long) 2, true);
                return true;
            default:
                if (n % 2 == 0) {
                    map.put(n, false);
                    return false;
                } else {
                    for (long i = 3; i * i <= n; i += 2) {
                        if (n % i == 0) {
                            map.put(n, false);
                            return false;
                        }
                    }
                    map.put(n, true);
                    return true;
                }
        }
    }
}
