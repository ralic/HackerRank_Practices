package org.raliclo.HackerRank_Competitions.Contests.WeekofCode22;/**
 * Created by raliclo on 8/11/16.
 * Project Name : TestNG-1
 */
//https://www.hackerrank.com/contests/w22/challenges/number-of-sequences
// TODO : Code Improvement Required

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.stream.Collectors;

public class NumberofSequences {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));

        int n = Integer.parseInt(input.get(0));
        int[] list = Arrays.stream(input.get(1).split(" ")).mapToInt(Integer::parseInt).toArray();
        int counter = 0;
        Stack<Integer> bag = new Stack<>();
        for (int i = 0; i < list.length; i++) {
            if (list[i] == -1) {
                bag.add(i);
//                System.out.println(i);
                counter++;
            }
        }
        System.out.println(reply(counter, bag, n));
    }

    public static BigInteger reply(int counter, Stack<Integer> bag, int n) {
        if (counter == 0) {
            return BigInteger.ONE;
        }
        BigInteger ans = BigInteger.ONE;
        BigInteger moder = BigInteger.valueOf(1000000007);

        while (bag.size() > -1) {
            int mply = bag.pop();
            ans = ans.multiply(BigInteger.valueOf(mply+1));
            ans = ans.mod(moder);
            if (bag.size() == 0) {
                break;
            }
        }
        return ans;
    }
}


//    public static int reply(int counter, int n) {
//        if (counter == 0) {
//            return 1;
//        }
//
//        int ans = n;
//        int moder = 1000000007;
//        if (n > counter) {
//            while (counter > 1) {
//                ans *= n - 1;
//                if (n > moder) {
//                    n %= moder;
//                }
//                counter--;
//            }
//        }
//        if (n==counter) {
//
//        }
//        return ans;
//    }
//}
