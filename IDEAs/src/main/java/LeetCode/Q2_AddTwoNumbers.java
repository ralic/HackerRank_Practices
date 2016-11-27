//https://leetcode.com/problems/add-two-numbers/

package LeetCode;

/**
 * Created by raliclo on 21/11/2016.
 * Project Name : TestNG-1
 */

public class Q2_AddTwoNumbers {


    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode ans;
        ListNode pt1 = l1;
        ListNode pt2 = l2;
        ListNode newNode;
        int adder = 0;

        if (l1 != null && l2 != null) {

            adder = (l1.val + l2.val) >= 10 ? 1 : 0;
            ans = new ListNode((l1.val + l2.val) % 10);
            pt1 = l1.next;
            pt2 = l2.next;
            newNode = ans;

            while (pt1 != null & pt2 != null) {
                newNode.next = new ListNode((adder + pt1.val + pt2.val) % 10);
                adder = (pt1.val + pt2.val + adder) >= 10 ? 1 : 0;
                pt1 = pt1.next;
                pt2 = pt2.next;
                newNode = newNode.next;
            }


            if (pt1 == null && pt2 != null) {
                while (pt2 != null) {
                    newNode.next = new ListNode((pt2.val + adder) % 10);
                    adder = (pt2.val + adder) >= 10 ? 1 : 0;
                    pt2 = pt2.next;
                    newNode = newNode.next;
                }
            }
            if (pt2 == null && pt1 != null) {
                while (pt1 != null) {
                    newNode.next = new ListNode((pt1.val + adder) % 10);
                    adder = (pt1.val + adder) >= 10 ? 1 : 0;
                    pt1 = pt1.next;
                    newNode = newNode.next;
                }
            }

            if (pt1 == null && pt2 == null && adder != 0) {
                newNode.next = new ListNode(adder);
            }
            return ans;
        }
        return null;
    }

    public static void main(String[] args) {

// Case1
        ListNode l1 = new ListNode(2);
        l1.next = new ListNode(4);
        l1.next.next = new ListNode(3);

        ListNode l2 = new ListNode(5);
        l2.next = new ListNode(6);
        l2.next.next = new ListNode(4);

// Case2
//        ListNode l1 = new ListNode(5);
//        ListNode l2 = new ListNode(5);

// Case3
//        ListNode l1 = new ListNode(1);
//        l1.next = new ListNode(8);
//        ListNode l2 = new ListNode(0);

        // Timer for Speed Test
        long speedX = System.currentTimeMillis();

        ListNode printer;
        printer = l1;
        while (printer != null) {
            System.out.println("l1-" + printer.val);
            printer = printer.next;
        }

        printer = l2;
        while (printer != null) {
            System.out.println("l2-" + printer.val);
            printer = printer.next;
        }
        printer = addTwoNumbers(l1, l2);
        while (printer != null) {
            System.out.println("ans-" + printer.val);
            printer = printer.next;
        }

        // Report for Speed Test
        System.out.println("Time spent :" + (System.currentTimeMillis() - speedX) + "msec");

    }

    public static class ListNode {

        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }

    }


}
