/**
 * Created by raliclo on 06/11/2016.
 */

'use strict';

let processData = function (input) {
    console.log(input);
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
