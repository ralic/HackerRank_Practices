/**
 * Created by raliclo on 28/11/2016.
 */


'use strict';
let processData = function (input) {
    var ipaddr = require('ipaddr.js');

    var addr = ipaddr.parse("2001:db8:1234::1");
    var range = ipaddr.parse("2001:db8::");

    console.log(addr);
    console.log(range);
    console.log(addr.match(range, 32))

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
