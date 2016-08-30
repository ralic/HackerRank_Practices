
/*
 * Student Info: Name=Lo,WeiShun , ID= 13108
 * Subject: CourseNo_HWNo_Summer_2015
 * Author: raliclo
 * Filename: Leet2AddTwoNumbers.java
 * Date and Time: Feb 2, 2016 3:15:43 AM
 * Project Name: Leet-2-Add_Two_Numbers
 */

 /*
You are given two linked lists representing two non-negative numbers. The digits are stored in reverse order and each of their nodes contain a single digit. Add the two numbers and return it as a linked list.

Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
Output: 7 -> 0 -> 8
 */
package leet.pkg2.add.two.numbers;

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
public class Leet2AddTwoNumbers {

    public static class ListNode {

        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /**
         * @param speedX to monitor program runtime
         */
//
//        ListNode l1 = new ListNode(2);
//        l1.next = new ListNode(4);
//        l1.next.next = new ListNode(3);
//        ListNode l2 = new ListNode(5);
//        l2.next = new ListNode(6);
//        l2.next.next = new ListNode(4);
//
//        ListNode l1 = new ListNode(1);
//        l1.next = new ListNode(8);
//        ListNode l2 = new ListNode(0);
//
//        ListNode l1 = new ListNode(9);
//        l1.next = new ListNode(8);
//        ListNode l2 = new ListNode(1);
//        

//        ListNode l1 = new ListNode(1);
//        ListNode l2 = new ListNode(9);
//        l2.next = new ListNode(9);
//
//        ListNode l1 = new ListNode(9);
//        l1.next = new ListNode(9);
//        ListNode l2 = new ListNode(9);
//
        ListNode l1 = new ListNode(5);
        ListNode l2 = new ListNode(5);
        long speedX = System.currentTimeMillis();
        printNode(l1);
        printNode(l2);
        ListNode l3 = addTwoNumbers(l1, l2);
        printNode(l3);
        System.out.println(
                "Time spent :" + (System.currentTimeMillis() - speedX) + "msec");

    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode ans = new ListNode(0);
        ListNode run = ans;

        int balance = 0;
        while (l1.next != null && l2.next != null) {
            run.val = (l1.val + l2.val + balance) % 10;
            balance = (l1.val + l2.val + balance) >= 10 ? 1 : 0;
            run.next = new ListNode(0);
            l1 = l1.next;
            l2 = l2.next;
            run = run.next;
        }

        if (l1.next != null && l2.next == null) {
            while (l1.next != null) {
                run.val = (l1.val + l2.val + balance) % 10;
                balance = (l1.val + l2.val + balance) >= 10 ? 1 : 0;
                l2.val = 0;
                run.next = new ListNode(0);
                l1 = l1.next;
                run = run.next;
            }
        }
        if (l1.next == null && l2.next != null) {
            while (l2.next != null) {
                run.val = (l2.val + l1.val + balance) % 10;
                balance = (l2.val + l1.val + balance) >= 10 ? 1 : 0;
                l1.val = 0;
                run.next = new ListNode(0);
                l2 = l2.next;
                run = run.next;
            }
        }

        run.val = (l2.val + l1.val + balance) % 10;
        balance = (l2.val + l1.val + balance) >= 10 ? 1 : 0;

        if (balance == 1) {
            balance = 0;
            run.next = new ListNode(1);
        }
        return ans;
    }

    public static void printNode(ListNode k) {
        while (k.next != null) {
            System.out.print(k.val);
            k = k.next;
        }
        System.out.println(k.val);
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
