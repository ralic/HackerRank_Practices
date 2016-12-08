// The Computer Language Benchmarks Game
// http://benchmarksgame.alioth.debian.org/
//
// contributed by Ian Osgood
// Optimized by Roy Williams and Jos Hirth
// modified for Node.js by Isaac Gouy
//http://benchmarksgame.alioth.debian.org/u64q/program.php?test=spectralnorm&lang=node&id=5
/*
 Input
 5500
 Output
 1.274224153
 Time Elapsed
 5888
 */
function processData(input) {

    function A(i, j) {
        return 1 / (((i + j) * (i + j + 1) >>> 1) + i + 1);
    }

    function Au(u, v) {
        var n = u.length & 0x3FFFFFFF; // max smi 32
        for (let i = 0; i < n; ++i) {
            var t = 0;
            for (let j = 0; j < n; ++j)
                t += A(i, j) * u[j];
            v[i] = t;
        }
    }

    function Atu(u, v) {
        let n = u.length & 0x3FFFFFFF; // max smi 32
        for (let i = 0; i < n; ++i) {
            let t = 0;
            for (let j = 0; j < n; ++j)
                t += A(j, i) * u[j];
            v[i] = t;
        }
    }

    function AtAu(u, v, w) {
        Au(u, w);
        Atu(w, v);
    }

    function spectralnorm(n) {
        let storage_ = new ArrayBuffer(n * 24);
        var u = new Float64Array(storage_, 0, n),
            v = new Float64Array(storage_, 8 * n, n),
            w = new Float64Array(storage_, 16 * n, n);
        let i, vv = 0, vBv = 0;
        i = 0;
        while (i++ < n) {
            u[i] = 1;
            v[i] = w[i] = 0;
        }
        for (i = 0; i < 10; ++i) {
            AtAu(u, v, w);
            AtAu(v, u, w);
        }
        for (i = 0; i < n; ++i) {
            vBv += u[i] * v[i];
            vv += v[i] * v[i];
        }
        return Math.sqrt(vBv / vv);
    }

    console.log(spectralnorm(input).toFixed(9));


}

process.stdin.resume();
process.stdin.setEncoding("utf-8");
let _input = "";
process.stdin.on("data", function (input) {
    _input += input;
});

process.stdin.on("end", function () {
    var beginT = new Date();
    processData(_input);;;;;;;;;;;;;;;;;;;;;;;
    console.log("Time Elapsed:" + (new Date() - beginT) + "msec");
});
