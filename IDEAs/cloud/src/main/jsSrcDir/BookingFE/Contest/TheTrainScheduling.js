/**
 * Created by raliclo on 8/10/16.
 */
// https://www.hackerrank.com/contests/booking-com-passions-hacked-frontend/challenges/javascript-compiler

/* in
 6
 msg 3 : ((750)1000)
 msg 4 : (1000)
 msg 1 : (((250)250)1000)
 msg 2 : ((1000)1000)
 msg 5 : ()
 msg 6 : ()
 */
/* out
 msg 5.
 msg 6.
 msg 4.
 msg 1.
 msg 3.
 msg 2.
 */
/* in
 6
 msg 3 : ((500)500)
 msg 4 : (1000)
 msg 1 : (((125)125)250)
 msg 2 : ((1000)1000)
 msg 5 : ()
 msg 6 : ()
 */
"use strict";

function processData(input) {
    var info = input.split("\n");;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    info = info.splice(1, info.length - 2);;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    var list = [];
    for (var i in info) {
        var box = info[i].split(":");;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
        var obj = {};
        // console.log(box[1].split(/\(|\)/));
        var values = box[1].split(/\(|\)/);
        var sum = 0;
        var list2 = [];
        for (var j in values) {
            var num = parseInt(values[j]);
            if (!isNaN(num)) {
                list2.push(num);;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
                sum += parseInt(num);
            }
        }
        obj.k = box[0].trim() + ".";
        obj.v = sum;
        obj.list = list2.reverse();
        list.push(obj)
    }

    list.sort(function (a, b) {
        if (a.v > b.v) {
            return 1;
        }
        if (a.v < b.v) {
            return -1;
        }
        if (a.v == b.v) {
            if (a.list.length > b.list.length) {
                return 1;
            }
            if (a.list.length > b.list.length) {
                return -1;
            }
            if (a.list.length == b.list.length) {
                for (var i = 0; i < a.list.length; i++) {
                    if (a.list[i] > b.list[i]) {
                        return 1;
                    }
                    if (a.list[i] < b.list[i]) {
                        return -1;
                    }
                }
                return 0;
            }
        }
    });;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

    // console.log(list);
    for (var x in list) {
        console.log(list[x].k)
    }
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
