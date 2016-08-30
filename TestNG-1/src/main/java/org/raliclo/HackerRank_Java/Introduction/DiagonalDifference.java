package org.raliclo.HackerRank_Java.Introduction;

import java.io.*;
import java.util.stream.Collectors;
import java.util.*;

/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */
public class DiagonalDifference {

    public static void main(String[] args) throws IOException {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int lines = Integer.parseInt(reader.readLine());
//        System.out.println(lines);
        ArrayList<String> llist=reader.lines().collect(Collectors.toCollection(ArrayList::new));
//        System.out.println(llist);
        int l2r=0;
        int r2l=0;
        for (int i=0;i<lines;i++) {
            String[] tmp =llist.get(i).split(" ");
            l2r+=Integer.parseInt(tmp[i]);
            r2l+=Integer.parseInt(tmp[lines-i-1]);
        }
        int ans=(l2r>r2l)? l2r-r2l:r2l-l2r;
        System.out.println(ans);
    }

}
