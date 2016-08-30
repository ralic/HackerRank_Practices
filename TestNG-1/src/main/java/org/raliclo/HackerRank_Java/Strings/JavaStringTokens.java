package org.raliclo.HackerRank_Java.Strings;/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */
//https://www.hackerrank.com/challenges/java-string-tokens?h_r=next-challenge&h_v=zen

import java.util.*;

public class JavaStringTokens {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        ArrayList<String> x = reader.lines().collect(Collectors.toCollection(ArrayList::new));
//        String s=x.get(0);
        Scanner scan = new Scanner(System.in);
        String s = scan.nextLine();
        scan.close();
        System.out.println(s.trim());
        if (s.trim().length()==1) {
            System.out.println(1);
            System.out.println(s.trim());
        } else {
            String[] list = s.trim().split("[ !,?._'@]+");
            if (list.length != 1) {
                System.out.println(list.length);
                Arrays.stream(list).forEach(System.out::println);
            } else {
                System.out.println(0);
            }
        }
    }

}
