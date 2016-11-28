var express = require('express');
var proxy = require('http-proxy-middleware');

var options = {
    changeOrigin: true,               // needed for virtual hosted sites
    ws: true
};


let portProxy = function (port, addr, api) {
    console.log("[Port]" + port);
    options.target = addr;
    let app = new express();
    app.use(api, proxy(options));
    app.listen(port);
};

portProxy(3000, 'https://www.facebook.com', '/');
portProxy(3001, 'https://www.google.com', '/');
portProxy(3002, 'https://npu-cs557-nodejs-express.herokuapp.com', '/');
