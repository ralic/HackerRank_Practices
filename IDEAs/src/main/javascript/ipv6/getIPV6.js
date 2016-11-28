/**
 * Created by raliclo on 28/11/2016.
 */

'use strict';;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
console.log("Please enter data or Press Command-D to start: ");

let processData = function (input) {
    console.log(input);
    const interfaces = require('os').networkInterfaces();

    const addresses = Object.keys(interfaces)
        .reduce((results, name) => results.concat(interfaces[name]), [])
        .filter((iface) => iface.family === 'IPv6' && !iface.internal)
        .map((iface) => iface.address);

    console.log(addresses);;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

    const ipv6_local = addresses[addresses.length - 1];;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

    console.log(ipv6_local)
};;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

// process.stdin.resume();
// process.stdin.setEncoding("utf-8");
let _input = "";
// process.stdin.on("data", function (input) {
//     _input += input;
// });
//
// process.stdin.on("end", function () {
let beginT = new Date();
let nanoT1 = process.hrtime();
processData(_input);
let nanoT2 = process.hrtime();
console.log("Time Elapsed: " + ( (nanoT2[1] - nanoT1[1] ) / 1000000) + " msec");
console.log("Begin Time:", nanoT1, "\nEnd  Time:", nanoT2);;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
// });
// http://stackoverflow.com/questions/11725691/how-to-get-a-microtime-in-node-js
