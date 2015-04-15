QUnit.test("testing chessboard.js: creating a blank chess board", function (assert) {
    "use strict";
    var $board,
        $squares;

    assert.equal(boardWidth, 8);
    $("#qunit").append(createBlankChessBoardSVG());

    $board = $("#qunit svg");
    assert.equal($board.attr("width"), "500px");
    assert.equal($board.attr("height"), "500px");

    $squares = $("#qunit rect");
    assert.equal($squares.length, 64);
    $squares.each(function () {
        assert.equal($(this).attr("width"), 62.5);
        assert.equal($(this).attr("height"), 62.5);
        assert.equal($(this).attr("class"), "dropzone");
        assert.equal($(this).attr("stroke"), "none");
        assert.equal($(this).attr("fill-opacity"), 1);
        var idExpr = /[A-H][1-8]/;
        assert.ok(idExpr.test($(this).attr("id")));
    });
});
