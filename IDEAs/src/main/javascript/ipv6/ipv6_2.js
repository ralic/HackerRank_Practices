'use strict';

const interfaces = require('os').networkInterfaces();

const addresses = Object.keys(interfaces)
    .reduce((results, name) => results.concat(interfaces[name]), [])
    .filter((iface) => iface.family === 'IPv6' && !iface.internal)
    .map((iface) => iface.address);

console.log(addresses);;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

const ipv6_local = addresses[addresses.length - 1];;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

console.log(ipv6_local);;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;