package org.raliclo.HackerRank_Java.Strings;/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */

import java.util.Arrays;
import java.util.Scanner;

public class JavaAnagrams {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        String a = scan.next();
        String b = scan.next();
        scan.close();
        boolean ret = isAnagram(a, b);
        System.out.println((ret) ? "Anagrams" : "Not Anagrams");
    }

    static boolean isAnagram(String a, String b) {
        if (a.length() != b.length()) {return false;}
        String[] aa = a.toLowerCase().split("");
        String[] bb = b.toLowerCase().split("");
        Arrays.sort(aa);
        Arrays.sort(bb);
        for (int i=0;i<aa.length;i++) {
//            System.out.println(aa[i]+" "+bb[i]);
            if (!aa[i].equals(bb[i])) {
                return false;
            }
        }
        return true;
    }
}