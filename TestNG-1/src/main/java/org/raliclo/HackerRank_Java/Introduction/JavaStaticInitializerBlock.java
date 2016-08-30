package org.raliclo.HackerRank_Java.Introduction;/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */
// https://www.hackerrank.com/challenges/java-static-initializer-block

import java.util.*;


public class JavaStaticInitializerBlock {
    static Scanner in = new Scanner(System.in);
    static int B=in.nextInt();
    static int H=in.nextInt();
    static boolean flag=B>0&&H>0;

    static {
            if(!flag) {
                System.out.println("java.lang.Exception: Breadth and height must be positive");
            }
    }

    public static void main(String[] args){
        if(flag){
            int area=B*H;
            System.out.print(area);
        }
    }//end of main

}//end of class

// Optional
//    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        ArrayList<String> x = reader.lines().collect(Collectors.toCollection(ArrayList::new));
//
//        int B = Integer.parseInt(x.get(0));
//        int H = Integer.parseInt(x.get(1));
//
//        if (B>0 && H >0) {
//            System.out.println(B*H);
//        } else {
//            System.out.println("java.lang.Exception: Breadth and height must be positive");
//        }
//    }

//}
