package org.raliclo.HackerRank_Java.DataStructure;/**
 * Created by raliclo on 8/12/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/challenges/phone-book

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class JavaMap {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));

        int n = Integer.parseInt(input.get(0));

        HashMap<String, Integer> map = new HashMap<>();

        for (int i = 0; i < n; i++) {
//            System.out.println(input.get(1+i*2));
//            System.out.println(input.get(2+i*2));
            map.put(input.get(1 + i * 2), Integer.parseInt(input.get(2 + i * 2)));
        }
//        System.out.println(map);

        for (int i = 2 * n + 1; i < input.size(); i++) {
//            System.out.println(input.get(i));
            String arg = input.get(i);
            if (map.containsKey(arg)) {
                System.out.println(arg+"="+map.get(arg));
            } else {
                System.out.println("Not found");
            }
        }
    }

}
