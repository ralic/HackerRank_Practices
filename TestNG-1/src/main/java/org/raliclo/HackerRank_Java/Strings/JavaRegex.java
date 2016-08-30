package org.raliclo.HackerRank_Java.Strings;/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.regex.Pattern;
//import java.util.stream.Collectors;
//
//public class JavaRegex {
//
//    public static void main(String[] args) {
//        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        ArrayList<String> x = reader.lines().collect(Collectors.toCollection(ArrayList::new));
//        x.stream().forEach((xx) -> verify(xx));
//
//    }
//
//    public static void verify(String x) {
////        System.out.println(Pattern.matches("\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b", x));
//        System.out.println(Pattern.matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", x));
//    }
//
//}

import java.util.Scanner;

class JavaRegex{

    public static void main(String []args)
    {
        Scanner in = new Scanner(System.in);
        while(in.hasNext())
        {
            String IP = in.next();
            System.out.println(IP.matches(new myRegex().pattern));
        }

    }
}

class myRegex {
    String pattern;
    String pattern2;
    myRegex() {
        this.pattern2="^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
//        this.pattern="^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
//                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."+
//                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."+
//                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    }
}