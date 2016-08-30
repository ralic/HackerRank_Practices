package org.raliclo.HackerRank_Java.BigNumber;/**
 * Created by raliclo on 7/26/16.
 * Project Name : TestNG-1
 */

import java.math.BigInteger;
import java.util.Scanner;

public class JavaPrimalityTest {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        BigInteger n = in.nextBigInteger();
        in.close();
        String x = n.isProbablePrime(9) ? "prime" : "not prime";
        System.out.println(x);
    }

}
