QUnit.test("testing chessboard.js: creating a blank chess board", function (assert) {
    "use strict";
    var $board,
        $squares;

    assert.equal(boardWidth, 8);
    $("#qunit-fixture").append(createBlankChessBoardSVG());

    $board = $("#qunit-fixture svg");
    console.log($board);
    assert.equal($board.attr("width"), "500px");
    assert.equal($board.attr("height"), "500px");

    $squares = $("#qunit-fixture rect");
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


QUnit.test("testing chessboard.js: create a non blank chess board", function (assert) {
    "use strict";
    var json = '{"squares":{"C8":"B","A2":"p","A1":"r","C7":"P","A8":"R","A7":"P","E7":"P","E8":"K","G7":"P","G8":"N","E2":"p","E1":"k","G2":"p","G1":"n","C1":"b","C2":"p","D8":"Q","B2":"p","D7":"P","B8":"N","B7":"P","F8":"B","F7":"P","H8":"R","F1":"b","H7":"P","H2":"p","H1":"r","F2":"p","D2":"p","D1":"q","B1":"n"}}',
        $board,
        $chesspieces;
    
    
    $("#qunit-fixture").append("<div id='chessboard-surround'></div>");
    createChessBoard("WHITE", json);
    $board = $("#chessboard-surround svg");
    assert.equal($board.attr("width"), "500px");
    assert.equal($board.attr("height"), "500px");
    $chesspieces = $board.find("g.chesspiece");
    assert.equal($chesspieces.length, 32);
    
    $chesspieces.each(function () {
        var idExpr = /(knight|queen|king|bishop|rook|pawn)\-[A-H][1-8]/;
        assert.ok(idExpr.test($(this).attr("id")));
    });
    
    
});

