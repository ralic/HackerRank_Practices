/**
 * Created by raliclo on 8/10/16.
 */

//https://www.hackerrank.com/contests/booking-com-passions-hacked-frontend/challenges/the-elevator-superstition
//Input 12
//Output 15
"use strict";

function processData(input) {
    //Enter your code here
    var count = 0;
    var print = 0;

    for (count = 0; count < input;) {
        print++;
        if (print.toString().includes("4") || print.toString().includes("13")) {
            // console.log(print, "contains", "4 or 13")
        } else {
            count++
        }
    }
    console.log(print)


}

process.stdin.resume();
process.stdin.setEncoding("utf-8");
var _input = "";
process.stdin.on("data", function (input) {
    _input += input;
});

process.stdin.on("end", function () {
    var beginT = new Date();
    processData(_input);
    console.log("Time Elapsed:" + (new Date() - beginT));
});
