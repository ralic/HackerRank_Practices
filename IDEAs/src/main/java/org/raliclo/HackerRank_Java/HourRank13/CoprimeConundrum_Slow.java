//https://www.hackerrank.com/contests/hourrank-13/challenges/arthur-and-coprimes
package org.raliclo.HackerRank_Java.HourRank13;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;

/*
input 141195
output 423288
input 12
output 3
input 50
output 30
 */

/**
 * Created by raliclo on 04/10/2016.
 */
public class CoprimeConundrum_Slow {

    public static void main(String[] args) {
        long sum = 0;
        Scanner in = new Scanner(System.in);
        long n = in.nextLong();
        while (n > 1) {
            sum += pairs(n);
            n--;
        }
        System.out.print(sum);
    }

    public static int pairs(long n) {
        int count = 0;
        long divisor1 = 1;
        HashMap<Long, Boolean> divisors = new HashMap<>();
        while (++divisor1 < n) {
            long divisor2;
            if (n % divisor1 == 0 && !divisors.containsKey(divisor1)) {
                divisor2 = n / divisor1;
//                System.out.println(coPrime(divisor1, divisor2) + " " + divisor1 + " " + divisor2);
                if (coPrime(divisor1, divisor2)) {
                    divisors.put(divisor1, true);
                    divisors.put(divisor2, true);
                    count++;
                }
            }
        }
        return count;
    }

    public static boolean coPrime(long n1, long n2) {
        BigInteger ans = BigInteger.valueOf(n1).gcd(BigInteger.valueOf(n2));
        return ans.equals(BigInteger.ONE);
    }
}
