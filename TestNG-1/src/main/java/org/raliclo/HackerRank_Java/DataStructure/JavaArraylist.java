package org.raliclo.HackerRank_Java.DataStructure;/**
 * Created by raliclo on 8/2/16.
 * Project Name : TestNG-1
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class JavaArraylist {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        int lines = Integer.parseInt(input.get(0));
        input.remove(0);
        input.remove(lines);

        for (int i=0;i<input.size()-lines;i++) {
            int[] query= Arrays.stream(input.get(lines+i).split(" ")).mapToInt(Integer::parseInt).toArray();
            String[] data=input.get((query[0]-1)).split(" ");
//            System.out.println(query[0]+" "+query[1]);
//            Arrays.stream(data).forEach(System.out::println);
//            System.out.println(query[1]);
            if ((query[1])<data.length) {
                System.out.println(data[(query[1])]);
            } else {
                System.out.println("ERROR!");
            }
        }
    }
}

/*
Input:

8
7 43 35 26 34 78 99 70
3 71 11 16
8 70 19 42 30 84 20 57 45
0
1 20
0
0
0
10
5 1
5 7
1 7
5 3
5 2
4 7
5 2
1 2
4 1
2 3
 */