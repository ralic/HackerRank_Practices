package org.raliclo.HackerRank_Java.Introduction;/**
 * Created by raliclo on 8/2/16.
 * Project Name : TestNG-1
 */
//https://www.hackerrank.com/challenges/java-loops-i

import java.util.*;
import java.util.stream.IntStream;

public class JavaLoops1 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int a = in.nextInt();
        IntStream.range(1,11).forEach((m1)-> System.out.println(a+" x "+m1+" = "+m1*a));
    }
}
