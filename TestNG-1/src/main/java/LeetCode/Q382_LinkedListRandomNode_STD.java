/*
 * Copyright 2016 Ralic Lo<raliclo@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package LeetCode;

import java.util.Random;

/**
 * Created by raliclo on 9/4/16.
 * Project Name : TestNG-1
 */

public class Q382_LinkedListRandomNode_STD {

    public static void main(String[] args) {
        // Init a singly linked list [1,2,3].
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);

// getRandom() should return either 1, 2, or 3 randomly.
// Each element should have equal probability of returning.

        for (int i = 1; i < 10; i++) {
            Solution solution = new Solution(head);
            int x = solution.getRandom();
            System.out.println(x);
        }

    }

    //    Definition for singly-linked list.
    static class ListNode {
        static int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }

        public String toString() {
            return Integer.toString(val);
        }
    }

    static class Solution {

        /**
         * @param head The linked list's head. Note that the head is guanranteed
         * to be not null, so it contains at least one node.
         */
        ListNode head = null;

        public Solution(ListNode head) {
            this.head = head;
        }

        /**
         * Returns a random node's value.
         */
        public int getRandom() {
            Random rand = new Random();
            int count = 0;
            ListNode node = head;
            ListNode candidate = head;
            while (true) {
//                System.out.println("count:" + count);
//                System.out.println("rand:"+(rand.nextInt(count + 1)));

                if (node == null) break;
                if (rand.nextInt(count + 1) == count) {
                    candidate = node;
                }
                node = node.next;
                count++;
            }
            return candidate.val;
        }
    }
}
