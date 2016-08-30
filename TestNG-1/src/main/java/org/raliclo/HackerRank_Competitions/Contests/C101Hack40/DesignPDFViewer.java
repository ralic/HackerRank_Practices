package org.raliclo.HackerRank_Competitions.Contests.C101Hack40;/**
 * Created by raliclo on 8/23/16.
 * Project Name : TestNG-1
 */

import java.util.HashMap;
import java.util.Scanner;

public class DesignPDFViewer {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = 26;
        int begin = 'a';
        HashMap<Character, Integer> dict = new HashMap<>();
        for (int i = 0; i < n; i++) {
            dict.put((char) begin, in.nextInt());
            begin++;
        }
        String word = in.next();
        int max = Integer.MIN_VALUE;
        int k = word.length()-1;
        while (k > 0) {
            int lookup = dict.get(word.charAt(k));
            max = max < lookup ? lookup : max;
            k--;
        }
        System.out.println(max*word.length());
    }
}
