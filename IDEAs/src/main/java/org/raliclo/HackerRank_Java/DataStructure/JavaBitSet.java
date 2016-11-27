package org.raliclo.HackerRank_Java.DataStructure;/**
 * Created by raliclo on 8/12/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/challenges/java-bitset

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.stream.Collectors;

public class JavaBitSet {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));

        int[] NM = Arrays.stream(input.get(0).split(" ")).mapToInt(Integer::parseInt).toArray();
        BitSet b1 = new BitSet(NM[0]);
        BitSet b2 = new BitSet(NM[0]);
        for (int i = 1; i <= NM[1]; i++) {
            options(input.get(i), b1, b2);
        }


    }

    public static void options(String op, BitSet b1, BitSet b2) {
        String[] info = op.split(" ");
        BitSet target1;
        BitSet target2;
        if (info[1].equals("1")) {
            target1 = b1;
            target2 = b2;
        } else {
            target1 = b2;
            target2 = b1;
        }
        int opt2 = Integer.parseInt(info[2]);
        switch (info[0]) {
            case "AND":
                target1.and(target2);
                break;
            case "OR":
                target1.or(target2);
                break;
            case "XOR":
                target1.xor(target2);
                break;
            case "SET":
                target1.set(opt2);
                break;
            case "FLIP":
                target1.flip(opt2);
                break;
        }
        System.out.println(b1.cardinality() + " " + b2.cardinality());
    }
}
