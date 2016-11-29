'use strict';

const interfaces = require('os').networkInterfaces();

const addresses = Object.keys(interfaces)
    .reduce((results, name) => results.concat(interfaces[name]), [])
    .filter((iface) => iface.family === 'IPv6' && !iface.internal)
    .map((iface) => iface.address);

const netinfo = Object.keys(interfaces)
    .reduce((results, name) => results.concat(interfaces[name]), [])
    .map((item) => console.log(item));;;;;;

let nanoT1 = process.hrtime();

console.log(addresses);
const ipv6_local = addresses[addresses.length - 2];
const ipv6host = "http://[" + ipv6_local + "]";
console.log(ipv6host);
let nanoT2 = process.hrtime();

console.log("Time Elapsed: " + ( (nanoT2[0] - nanoT1[0]) * 1000 + (nanoT2[1] - nanoT1[1] ) / 1000000) + " msec");
console.log("Begin Time:", nanoT1, "\nEnd   Time:", nanoT2);
