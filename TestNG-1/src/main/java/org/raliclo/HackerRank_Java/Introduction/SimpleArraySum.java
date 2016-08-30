package org.raliclo.HackerRank_Java.Introduction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */
public class SimpleArraySum {

    public static void main(String[] args) throws IOException {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] nums = reader.lines().collect(Collectors.toCollection(LinkedList::new)).get(1).split(" ");
        int ans=0;
        for (String k: nums) {
            ans+=Integer.parseInt(k);
        }
        System.out.println(ans);
    }
}
