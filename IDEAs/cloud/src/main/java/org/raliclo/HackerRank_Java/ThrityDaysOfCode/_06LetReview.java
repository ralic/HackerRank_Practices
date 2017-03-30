package org.raliclo.HackerRank_Java.ThrityDaysOfCode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class _06LetReview {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));

        int n = Integer.parseInt(input.get(0));

        for (int i = 1; i <= n; i++) {
            int linelength = input.get(i).length();
            List<Character> buffer = new ArrayList<>();

            for (int j = 0; j < linelength; j++) {
                if ((j & 1) != 1) {
                    System.out.print(input.get(i).charAt(j));
                } else {
                    buffer.add(input.get(i).charAt(j));
                }
            }
            System.out.print(" ");
            int prints = buffer.size();
            for (int index = 0; index < prints; index++) {
                System.out.print(buffer.remove(0));
            }
            System.out.println();
        }
    }

}
