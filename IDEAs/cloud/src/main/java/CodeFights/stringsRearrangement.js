/**
 * Created by raliclo on 08/02/2017.
 */
/**
 * Created by raliclo on 06/11/2016.
 */

'use strict';

var input1 = ["aba",
    "bbb",
    "bab"]; //false
var input2 = ["ab",
    "bb",
    "aa"];//true
var input8 = ["f",
    "g",
    "a",
    "h"];// true

// Failed Tests

var input3 = ["q",
    "q"] // false
var input4 = ["zzzzab",
    "zzzzbb",
    "zzzzaa"]; // true
var input6 = ["abc",
    "abx",
    "axx",
    "abc"];//false
var input7 = ["abc",
    "abx",
    "axx",
    "abx",
    "abc"];//true


function stringsRearrangement(inputArray) {
    inputArray = inputArray.sort(sorter);
    var i = inputArray.length;
    while (i-- > 1) {
        if (comparetwo(inputArray[i], inputArray[i - 1]) == false) {
            return false;
        }
    }
    return true;
}

var sorter = function (a, b) {
    var count = 0;
    for (var i = 0; i < a.length; i++) {
        // console.log(count, a[i], b[i])
        if (a[i] !== b[i]) {
            count++
        }
    }
    return count;
}


var comparetwo = function (a, b) {
    var count = 0;
    for (var i = 0; i < a.length; i++) {
        // console.log(count, a[i], b[i])
        if (a[i] !== b[i]) {
            count++;

        }
        if (count > 1) {
            return false;
        }
    }
    return true;
}

let processData = function (input) {
    console.log(input);
    console.log(stringsRearrangement(input2))
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

/*
 Given an array of equal-length strings, check if it is possible to rearrange the strings in such a way that after the rearrangement the strings at consecutive positions would differ by exactly one character.

 Example

 For inputArray = ["aba", "bbb", "bab"], the output should be
 stringsRearrangement(inputArray) = false;
 For inputArray = ["ab", "bb", "aa"], the output should be
 stringsRearrangement(inputArray) = true.
 Input/Output

 [time limit] 4000ms (js)
 [input] array.string inputArray

 A non-empty array of strings of lowercase letters.

 Constraints:
 2 ≤ inputArray.length ≤ 10,
 1 ≤ inputArray[i].length ≤ 15.

 [output] boolean
 */