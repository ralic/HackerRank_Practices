package org.raliclo.HackerRank_Java.BigNumber;/**
 * Created by raliclo on 7/26/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/challenges/java-biginteger

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class JavaBigInteger {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> x = reader.lines().collect(Collectors.toCollection(ArrayList::new));


        BigInteger a= new BigInteger(x.get(0));
        BigInteger b = new BigInteger(x.get(1));

        System.out.println(a.add(b));
        System.out.println(a.multiply(b));

    }

}
