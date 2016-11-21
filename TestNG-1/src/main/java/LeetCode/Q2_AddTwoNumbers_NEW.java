package LeetCode;

/**
 * Created by raliclo on 21/11/2016.
 * Project Name : TestNG-1
 */

public class Q2_AddTwoNumbers_NEW {

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        return helper(l1, l2, 0);
    }

    private static ListNode helper(ListNode l1, ListNode l2, int adder) {
        if (l1 == null) return helper(l2, adder);
        if (l2 == null) return helper(l1, adder);
        ListNode node = new ListNode((l1.val + l2.val + adder) % 10);
        node.next = helper(l1.next, l2.next, (l1.val + l2.val + adder) / 10);
        return node;
    }

    private static ListNode helper(ListNode node, int adder) {
        if (adder == 0) return node;
        if (node == null) return new ListNode(adder);
        node.next = helper(node.next, (node.val + adder) / 10);
        node.val = (node.val + adder) % 10;
        return node;
    }

    public static void main(String[] args) {

// Case1
        ListNode l1 = new ListNode(2);
        l1.next = new ListNode(4);
        l1.next.next = new ListNode(3);

        ListNode l2 = new ListNode(5);
        l2.next = new ListNode(6);
        l2.next.next = new ListNode(4);

        // Timer for Speed Test
        long speedX = System.currentTimeMillis();

        ListNode printer;
        printer = l1;
        while (printer != null) {
            System.out.println("l1 -" + printer.val);
            printer = printer.next;
        }

        printer = l2;
        while (printer != null) {
            System.out.println("l2 -" + printer.val);
            printer = printer.next;
        }
        printer = addTwoNumbers(l1, l2);
        while (printer != null) {
            System.out.println("ans -" + printer.val);
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
