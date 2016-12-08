// https://www.hackerrank.com/contests/booking-com-passions-hacked-frontend/challenges/the-temperature-configuration

// Input
// {"speed":10,"inputs":[{"time":"2016-09-11 11:00","temperature":25},{"time":"2016-09-11 12:00","temperature":35}],"endTime":"2016-09-11 12:30","initialTemperature":15}

// 30

// {"speed":10,"inputs":[{"time":"2016-09-11 11:00","temperature":25},{"time":"2016-09-11 12:00","temperature":35}],"endTime":"2016-09-11 11:30","initialTemperature":15}

// 20

// {"speed":10,"inputs":[{"time":"2016-09-11 11:00","temperature":25},{"time":"2016-09-11 12:00","temperature":35}],"endTime":"2017-09-11 15:30","initialTemperature":15}

// 35

// {"speed":10,"inputs":[{"time":"2016-09-11 11:00","temperature":25},{"time":"2016-09-11 12:00","temperature":35}],"endTime":"2015-09-11 15:30","initialTemperature":15}

// 15

// {"speed":10,"inputs":[{"time":"2016-09-11 11:00","temperature":25},{"time":"2016-09-11 12:00","temperature":35}],"endTime":"2016-09-11 12:45","initialTemperature":15}

// 32


// {"speed":10,"inputs":[{"time":"2016-09-11 11:00","temperature":25},{"time":"2016-09-11 12:00","temperature":10}],"endTime":"2016-09-11 12:45","initialTemperature":15}

// 18

// {"speed":10,"inputs":[{"time":"2016-09-11 11:00","temperature":25},{"time":"2016-09-11 12:00","temperature":10}],"endTime":"2016-09-11 13:00","initialTemperature":15}

// 18


// {"speed":10,"inputs":[{"time":"2016-09-11 11:00","temperature":25},{"time":"2016-09-11 12:00","temperature":10}],"endTime":"2017-09-11 15:00","initialTemperature":15}


// {"speed":10,"inputs":[{"time":"2016-09-11 11:00","temperature":25},{"time":"2016-09-11 12:00","temperature":40}],"endTime":"2016-09-11 12:30","initialTemperature":15}

"use strict";

function processData(input) {
    var data = JSON.parse(input);
    // console.log(data);
    // console.log(typeof data)
    var speed = data.speed;
    var iTemp = data.initialTemperature;
    var inputs = data.inputs;
    // console.log(inputs[0].time);
    // console.log(Date.parse(inputs[0].time));
    // console.log(inputs[1].time);
    // console.log(Date.parse(inputs[1].time));
    // console.log((Date.parse(inputs[1].time)-Date.parse(inputs[0].time))/60/60/1000); // To Hour
    var tempStamp = function (date, temp) {
        this.date = Date.parse(date) / 60 / 60 / 1000; // To Hour
        this.temp = temp;
        this.move = function (newdate, newtemp) {
            var flag = 0;
            if (newtemp > this.temp) {
                flag = 1;
            } else if (newtemp < this.temp) {
                flag = -1;
            }
            return new tempStamp(newdate, ( this.temp + (Date.parse(newdate) / 60 / 60 / 1000 - this.date) * flag * data.speed));
        }
    };;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

    var templog = [];
    for (var x in inputs) {
        if (x === '0') {
            templog.push(new tempStamp(inputs[x].time, iTemp));
        } else {
            templog.push(templog[templog.length - 1].move(inputs[x].time, inputs[x - 1].temperature))
        }
    }
    templog.push(new tempStamp(inputs[inputs.length - 1].time, inputs[inputs.length - 1].temperature));
    templog[templog.length - 1].date = templog[templog.length - 1].date + Math.abs((templog[templog.length - 1].temp - templog[templog.length - 2].temp) / data.speed);;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

    // console.log(inputs)
    // console.log(templog);
    var results = {};
    var searchTemp = function (templog, time) {
        var searchdate = Date.parse(time) / 60 / 60 / 1000;
        for (var i = 0; i < templog.length - 1; i++) {
            // console.log(templog[i])
            if (searchdate <= templog[0].date) {
                results.temp = templog[0].temp;
                break;
            } else if (searchdate >= templog[templog.length - 1].date) {
                results.temp = templog[templog.length - 1].temp;
                break;
            } else if (searchdate > templog[i].date) {
                results = templog[i].move(time, templog[i + 1].temp)
            }
        }
        // console.log(final)
        console.log(Math.round(results.temp))
    };;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
    searchTemp(templog, data.endTime);
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
