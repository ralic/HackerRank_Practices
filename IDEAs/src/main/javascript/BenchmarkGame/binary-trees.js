function processData(input) {
    // Code Starts here
    function TreeNode(left, right, item) {
        this.left = left;
        this.right = right;
        this.item = item;
        this.itemCheck = function () {
            if (this.left == null) return this.item;
            else return this.item + this.left.itemCheck() - this.right.itemCheck();
        }
    }

    function bottomUpTree(item, depth) {
        if (0 < depth) {
            return new TreeNode(
                bottomUpTree(2 * item - 1, depth - 1) // left
                , bottomUpTree(2 * item, depth - 1) // right
                , item
            );
        }
        else {
            return new TreeNode(null, null, item);
        }
    }

    var minDepth = 4;
    var maxDepth = Math.max(minDepth + 2, input);
    var stretchDepth = maxDepth + 1;

    var check = bottomUpTree(0, stretchDepth).itemCheck();
    console.log("stretch tree of depth " + stretchDepth + "\t check: " + check);

    var longLivedTree = bottomUpTree(0, maxDepth);
    for (var depth = minDepth; depth < maxDepth + 1; depth += 2) {
        var iterations = 1 << (maxDepth - depth + minDepth);

        check = 0;
        var i = 1;
        while (i++ < iterations + 1) {
            check += bottomUpTree(i, depth).itemCheck();
            check += bottomUpTree(-i, depth).itemCheck();
        }
        console.log(iterations * 2 + "\t trees of depth " + depth + "\t check: " + check);
    }

    console.log("long lived tree of depth " + maxDepth + "\t check: "
        + longLivedTree.itemCheck());
    // Code Ends here
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
