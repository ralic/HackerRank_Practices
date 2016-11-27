/**
 * Created by raliclo on 21/11/2016.
 */

'use strict';
/**
 * Definition for singly-linked list.
 * function ListNode(val) {
 *     this.val = val;
 *     this.next = null;
 * }
 */
let ListNode = function (val) {
    this.val = val;
    this.next = null;
};

let detectCycle = function (head) {
    let slow = head;
    let fast = head;
    while (head !== null && head.next !== null) {
        slow = slow.next;
        if (fast === null || fast.next === null) {
            return null;
        }
        fast = fast.next.next;
        if (fast === slow) {
            let slow2 = head;
            while (slow2 !== slow) {
                slow = slow.next;
                slow2 = slow2.next;
            }
            return slow;
        }
    }
    return null;
};

let processData = function (input) {
    let testList = new ListNode(9);
    testList.next = new ListNode(10);
    testList.next.next = new ListNode(6);
    testList.next.next.next = new ListNode(3);
    testList.next.next.next.next = testList.next;

    var result = detectCycle(testList);
    console.log(result);
};

process.stdin.resume();
process.stdin.setEncoding("utf-8");
let _input = "";
process.stdin.on("data", function (input) {
    _input += input;
});

process.stdin.on("end", function () {
    console.log("Please enter data : ");
    let beginT = new Date();
    processData(_input);
    console.log("Time Elapsed:" + (new Date() - beginT));

});
