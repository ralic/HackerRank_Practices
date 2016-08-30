
/*
 * Student Info: Name=Lo,WeiShun , ID= 13108
 * Subject: CourseNo_HWNo_Summer_2015
 * Author: raliclo
 * Filename: Leet1TwoSum.java
 * Date and Time: Feb 2, 2016 2:51:27 AM
 * Project Name: Leet-1-TwoSum
 */

 /*
Given an array of integers, find two numbers such that they add up to a specific target number.

The function twoSum should return indices of the two numbers such that they add up to the target, where index1 must be less than index2. Please note that your returned answers (both index1 and index2) are not zero-based.

You may assume that each input would have exactly one solution.

Input: numbers={2, 7, 11, 15}, target=9
Output: index1=1, index2=2
 */
package leet.pkg1.twosum;

import java.io.*;
import java.net.*;
import java.security.*;
import java.math.*;
import java.text.*;
import java.util.*;
import java.lang.*;
import java.time.*;
import java.applet.*;
import java.nio.*;
import java.beans.*;
import java.rmi.*;
import java.util.regex.*;
import java.util.logging.*;
import java.util.concurrent.*;
import java.util.stream.*;
import java.util.function.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.nio.file.spi.*;
import java.nio.file.attribute.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;
//import import java.awt.*; // Disabled, because of List Class conflict
//import java.sql.*; // Disbled, because of Connection Class conflict

/**
 *
 * @author raliclo
 * @Java : java version "1.8.0_65"
 */
public class Leet1TwoSum {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /**
         * @param speedX to monitor program runtime
         */
        int[] input = {2, 7, 11, 15};
        int target = 9;
        IntStream.of(twoSum(input, target)).forEach(System.out::println);
        // TODO code application logic here
        long speedX = System.currentTimeMillis();
        System.out.println("Time spent :" + (System.currentTimeMillis() - speedX) + "msec");

    }

    public static int[] twoSum(int[] nums, int target) {
        int[] ans = new int[2];
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                {
                    if (nums[i] + nums[j] == target) {
                        ans[0] = i + 1;
                        ans[1] = j + 1;
                        return ans;
                    }
                }
            }
        }
        return ans;
    }

    /**
     * @param x for command line
     * @throws java.io.IOException
     */
    public static Object runexec(String x) throws IOException {
        Process p = Runtime.getRuntime().exec(x, null, null);
        Object ans = null;
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        while ((line = input.readLine()) != null) {
            System.out.println(line);
        }
        return ans;
    }
}
