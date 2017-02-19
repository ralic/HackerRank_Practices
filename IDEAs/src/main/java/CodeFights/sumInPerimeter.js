/**
 * Created by raliclo on 08/02/2017.
 */


function sumInPerimeter(grid, p) {
    var box = p / 3;
    var max = -Infinity;
    var gridx = grid.length - 1;
    var gridy = grid[0].length - 1;
    // console.log(gridx,gridy)
    var i = 0;
    var j = 0;
    for (i = 0; i < gridx; i++) {
        for (j = 0; j < gridy; j++) {
            // console.log(grid[i][j], grid[i][j + 1]);
            // console.log(grid[i][j], grid[i + 1][j]);
            var localmax = Math.max(
                (parseInt(grid[i][j]) + parseInt(grid[i][j + 1])),
                (parseInt(grid[i][j]) + parseInt(grid[i + 1][j]))
            );
            if (localmax > max) {
                max = localmax;
            }
        }
    }
    for (i = gridx, j = 0; j < gridy; j++) {
        var localmax = Math.max(
            (parseInt(grid[i][j]) + parseInt(grid[i][j + 1]))
        );
        if (localmax > max) {
            max = localmax;
        }


    }
    for (j = gridy, i = 0; i < gridx; i++) {
        var localmax = Math.max(
            (parseInt(grid[i][j]) + parseInt(grid[i+1][j]))
        );
        if (localmax > max) {
            max = localmax;
        }
    }

    // console.log(box)
    console.log(max)
}


let processData = function (input) {
    var input = [[1, 2, 3],
        [4, 5, 6],
        [7, 8, 9]];
    console.log(input);
    sumInPerimeter(input,6 );

};

process.stdin.resume();
process.stdin.setEncoding("utf-8");
let _input = "";
process.stdin.on("data", function (input) {
    _input += input;
});

process.stdin.on("end", function () {
    console.log("Please enter data : ");
    let beginT = new Date();
    processData(_input);
    console.log("Time Elapsed:" + (new Date() - beginT));
});

/*
 You are given a grid of integers and an integer p. Each cell of the grid is a 1 x 1 square. Your task is to find a 4-connected set of elements in the grid that has a perimeter equal to p and has the highest possible sum. Return this sum.

 Example

 For

 grid = [[1, 2, 3],
 [4, 5, 6],
 [7, 8, 9]]
 and

 p = 6
 the output should be
 sumInPerimeter(grid, p) = 17.

 A perimeter of 6 will surround two elements in a grid.
 The two elements with a perimeter of 6 in this grid that have the maximum possible value are 8 and 9, with a sum of 17.



 Input/Output

 [time limit] 25000ms (js)
 [input] array.array.integer grid

 A two-dimensional matrix consisting of integers.

 Constraints:
 1 ≤ grid.length ≤ 20,
 1 ≤ grid[i].length ≤ 20,
 -105 ≤ grid[i][j] ≤ 105.

 [input] integer p

 Constraints:
 4 ≤ p ≤ 24.

 It is guaranteed that p is an even integer.

 [output] integer

 The maximum possible sum of cells inside of a perimeter with a length of p.

 In order to get a partial score and get on the leaderboard for this challenge, your solution needs to pass all tests in which p is less than or equal to 10
 */