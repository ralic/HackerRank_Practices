/**
 * Created by raliclo on 28/11/2016.
 * Following Code proxy localhost:3000 to www.google.com
 */

'use strict';


let nanoT1 = process.hrtime();

// include dependencies
var express = require('express');
var proxy = require('http-proxy-middleware');

// proxy middleware options
var options = {
    target: 'http://www.google.com', // target host
    changeOrigin: true,               // needed for virtual hosted sites
    ws: true,                         // proxy websockets
    pathRewrite: {
        // '^/old/api': '/new/api',     // rewrite path
        // '^/remove/api': '/api'       // remove path
    },
    router: {
        // when request.headers.host == 'dev.localhost:3000',
        // override target 'http://www.example.org' to 'http://localhost:8000'
        'dev.localhost:3000': 'http://localhost:8000'
    }
};

// create the proxy (without context)
var exampleProxy = proxy(options);

// mount `exampleProxy` in web server
var app = express();
app.use('/', exampleProxy);
app.listen(3000);

let nanoT2 = process.hrtime();
console.log("Time Elapsed: " + ( (nanoT2[0] - nanoT1[0]) * 1000 + (nanoT2[1] - nanoT1[1] ) / 1000000) + " msec");
console.log("Begin Time:", nanoT1, "\nEnd Time:", nanoT2);
