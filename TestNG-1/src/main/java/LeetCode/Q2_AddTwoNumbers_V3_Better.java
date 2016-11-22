package LeetCode;

/**
 * Created by raliclo on 21/11/2016.
 * Project Name : TestNG-1
 */

public class Q2_AddTwoNumbers_V3_Better {


    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode head1 = l1;
        ListNode head2 = l2;
        ListNode dummy = new ListNode(0);
        ListNode head = dummy;
        int carry = 0;
        while (head1 != null || head2 != null || carry != 0) {
            int val = carry;
            if (head1 != null) {
                val += head1.val;
                head1 = head1.next;
            }
            if (head2 != null) {
                val += head2.val;
                head2 = head2.next;
            }
            ListNode tmp = new ListNode(val % 10);
            carry = val / 10;
            head.next = tmp;
            head = head.next;
        }
        return dummy.next;
    }

    public static void main(String[] args) {


// Case1
        ListNode l1 = new ListNode(2);
        l1.next = new ListNode(9);
        l1.next.next = new ListNode(4);

        ListNode l2 = new ListNode(5);
        l2.next = new ListNode(9);
        l2.next.next = new ListNode(5);

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
