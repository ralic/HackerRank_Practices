var express = require('express');
var proxy = require('http-proxy-middleware');

var options = {
    changeOrigin: true,               // needed for virtual hosted sites
    ws: true
};


let portProxy = function (port, addr, api) {
    try {
        console.log("[Port]" + port);
        options.target = addr;
        let app = new express();
        app.use(api, proxy(options));
        app.listen(port);
    } catch (er) {
        console.log(er)
    }
};

portProxy(3001, 'https://www.facebook.com', '/');
portProxy(3002, 'https://npu-cs557-nodejs-express.herokuapp.com', '/');
// portProxy(80, 'https://www.google.com', '/');


