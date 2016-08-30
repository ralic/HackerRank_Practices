//https://www.hackerrank.com/contests/booking-com-passions-hacked-frontend/challenges/the-gps-coordination
"use strict";
// input
// { "p1": { "start": [0,0], "path": "RUDURURU" }, "p2": { "start": [4,4], "path": "LDUDLDLD" } }
// output
// 2,2 6
function processData(input) {
    var data = JSON.parse(input);
    // console.log(data);
    // console.log(typeof data)
    var Loc = function (x, y) {
        this.x = x;
        this.y = y;
        this.move = function (path) {
            switch (path) {
                case "U":
                    return new Loc(this.x, this.y + 1);
                case "D":
                    return new Loc(this.x, this.y - 1);
                case "L":
                    return new Loc(this.x - 1, this.y);
                case "R":
                    return new Loc(this.x + 1, this.y);
                default:
                    console.log("Error input");
            }
        }
    }
    var meetup = function (data) {
        var route1 = [];
        route1.push(new Loc(data.p1.start[0], data.p1.start[1]));
        var path = data.p1.path.split("");
        // console.log(path)
        for (var x in path) {
            route1.push(route1[route1.length - 1].move(path[x]));
        }
        // console.log(route1);
        var route2 = [];
        route2.push(new Loc(data.p2.start[0], data.p2.start[1]));
        var path = data.p2.path.split("");
        // console.log(path)
        for (var x in path) {
            route2.push(route2[route2.length - 1].move(path[x]));
        }
        // console.log(route2);
        var miles = 0;
        var meetpt = [];
        for (var i = 0; i < route1.length; i++) {
                if (route1[i].x === route2[i].x && route1[i].y === route2[i].y) {
                    var tmp = i ;
                    if (miles===0 && tmp!==0){
                        miles = tmp;
                        meetpt[0] = route1[i].x;
                        meetpt[1] = route1[i].y;
                    }
            }
        }
        console.log(meetpt[0]+","+meetpt[1],miles)
    }
    meetup(data);
}


process.stdin.resume();
process.stdin.setEncoding("utf-8");
var _input = "";
process.stdin.on("data", function (input) {
    _input += input;
});

process.stdin.on("end", function () {
    var beginT = new Date();
    processData(_input);
    console.log("Time Elapsed:" + (new Date() - beginT));
});
