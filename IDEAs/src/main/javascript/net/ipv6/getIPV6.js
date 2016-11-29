/**
 * Created by raliclo on 28/11/2016.
 */

'use strict';

let getIPV6 = function () {
    const interfaces = require('os').networkInterfaces();

    const addresses = Object.keys(interfaces)
        .reduce((results, name) => results.concat(interfaces[name]), [])
        .filter((iface) => iface.family === 'IPv6' && !iface.internal)
        .map((iface) => iface.address);

    console.log(addresses);

    const ipv6_local = addresses[addresses.length - 1];

    console.log(ipv6_local);
};

let nanoT1 = process.hrtime();
getIPV6();
let nanoT2 = process.hrtime();
console.log("Time Elapsed: " + ( (nanoT2[0] - nanoT1[0]) * 1000 + (nanoT2[1] - nanoT1[1] ) / 1000000) + " msec");
console.log("Begin Time:", nanoT1, "\nEnd   Time:", nanoT2);

// http://stackoverflow.com/questions/11725691/how-to-get-a-microtime-in-node-js
