/**
 * Created by raliclo on 8/30/16.
 */
//https://gist.github.com/tjanczuk/f133cc65977f5a8c4a7f

// How to connect 5 publishers with 5 subscribers
// over TCP using ZeroMQ's XPUB/XSUB proxy.

// sub (connect)
//   <-8701->
// (bind) xpub <---> xsub (bind)
//   <-8700->
// (connect) pub

var zmq = require('zmq');

// xsub/xpub

var xsub = zmq.socket('xsub');
xsub.bindSync('tcp://*:8700');

var xpub = zmq.socket('xpub');
xpub.setsockopt(zmq.ZMQ_XPUB_VERBOSE, 1);
xpub.bindSync('tcp://*:8701');

// Message pump
xsub.on('message', function (topic, data) {
    xpub.send([topic, data]);
});

// Subscription pump
xpub.on('message', function (data) {
    xsub.send(data);
});

// Subscribers
for (var i = 1; i <= 5; i++) {
    (function (i) {
        var subscriber = zmq.socket('sub');
        subscriber.subscribe('Topic 1');
        subscriber.on('message', function (topic, data) {
            console.log('Subscriber', i, 'received', data.toString(), 'on topic', topic.toString());
        });
        subscriber.connect('tcp://localhost:8701');
    })(i);
}

// Publishers
for (var i = 1; i <= 5; i++) {
    (function (i) {
        var publisher = zmq.socket('pub');
        publisher.connect("tcp://localhost:8700");
        var msg = 1;
        setInterval(function () {
            publisher.send(['Topic 1', 'message ' + msg++ + ' from publisher ' + i]);
        }, 500 * i);
    })(i);
}