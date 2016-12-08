// npm install express http-proxy-middleware http-proxy-rules karma

var express = require('express');
var proxy = require('http-proxy-middleware');

var options = {
    changeOrigin: true,               // needed for virtual hosted sites
    ws: true
};

const interfaces = require('os').networkInterfaces();
const addresses = Object.keys(interfaces)
    .reduce((results, name) => results.concat(interfaces[name]), [])
    .filter((iface) => iface.family === 'IPv6' && !iface.internal)
    .map((iface) => iface.address);

const ipv6_local = addresses[addresses.length - 2];
const ipv6host = "http://[" + ipv6_local + "]";
// const link = `<a href="${ipv6host}">${ipv6host}</a>`;

console.log("[IPV6 Hostname]: " + ipv6host);

let portProxy = function (port, addr, api) {
    try {
        console.log("[Port]" + port);
        options.target = addr;
        let app = new express();
        app.use(api, proxy('**', options));
        app.listen(port);
    } catch (er) {
        console.log(er)
    }
};

process.on('uncaughtException', function (err) {
    if (err.errno === 'EADDRINUSE') {
        console.log("Bug");
    } else {
        console.log(err);
        console.log("[uncaughtException] Program Continued ");
    }
    // process.exit(1);
});

portProxy(2000, 'http://github.com/', '/');
// Github
// http://stackoverflow.com/questions/128035/how-do-i-pull-from-a-git-repository-through-an-http-proxy
portProxy(3000, 'https://github.com/', '/');
portProxy(3001, 'https://www.facebook.com', '/');
portProxy(3002, 'https://npu-cs557-nodejs-express.herokuapp.com', '/');
portProxy(3003, 'http://stackoverflow.com', '/');
portProxy(3004, 'http://stackoverflow.com', '/');

portProxy(80, 'https://www.google.com', '/');


