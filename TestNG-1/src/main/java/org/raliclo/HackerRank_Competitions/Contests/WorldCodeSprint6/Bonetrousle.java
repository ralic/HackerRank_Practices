//package org.raliclo.HackerRank_Competitions.Contests.WorldCodeSprint6;/**
// * Created by raliclo on 8/27/16.
// * Project Name : TestNG-1
// */
//
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.Scanner;
//
//public class Bonetrousle {
//
//    public static void main(String[] args) {
//        Scanner in = new Scanner(System.in);
//        int t = in.nextInt();
//        for (int i = 0; i < t; i++) {
//            BigInteger target_n = BigInteger.valueOf(in.nextInt());  // sum-of-box-number
//            BigInteger k = BigInteger.valueOf(in.nextInt()); // box-number
//            BigInteger b = BigInteger.valueOf(in.nextInt());  // box-purchased
//            buybox(target_n, k, b);
//        }
//    }
//
//    public static void buybox(BigInteger target, BigInteger k, BigInteger b) {
//        BigInteger max = k;
//        while (b.subtract(BigInteger.ONE).compareTo(BigInteger.ZERO) == 1) {
//            max.add(k.subtract(BigInteger.ONE));
//        }
//
//        ArrayList<Integer> memo = new ArrayList<>();
//        if (max.compareTo(target) < 0) {
//            System.out.println("-1");
//            return;
//        } else {
//            selectnumber(kk, memo, b, target);
//        }
//
//        for (int i = 0; i < memo.size() - 1; i++) {
//            System.out.print(memo.get(i) + " ");
//        }
//        System.out.println(memo.get(memo.size() - 1));
//
//    }
//
//    public static void selectnumber(int[] kk, ArrayList<Integer> memo, int b, int target) {
//        if (b > 0) {
//            Integer select = 0;
//            while (select < kk[0] || select > kk[kk.length - 1]) {
//                select = target / b;
//            }
//            while (memo.contains(select)) {
//                select = select - 1;
//            }
//            memo.add(select);
//            b--;
//            selectnumber(kk, memo, b, target - select);
//        }
//    }
//}