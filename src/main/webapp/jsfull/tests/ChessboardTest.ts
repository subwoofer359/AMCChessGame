/*global QUnit*/
/*global Chessboard*/
/*global $*/

import * as Chessboard from "../Chessboard";

let json;
QUnit.module("chessboard tests", {
    beforeEach: () => {
        json = `{"squares":{"C8":"B","A2":"p","A1":"r","C7":"P","A8":"R","A7":"P","E7":"P","E8":"K","G7":"P","G8":"N",
            "E2":"p","E1":"k","G2":"p","G1":"n","C1":"b","C2":"p","D8":"Q","B2":"p","D7":"P","B8":"N","B7":"P","F8":"B"
            ,"F7":"P","H8":"R","F1":"b","H7":"P","H2":"p","H1":"r","F2":"p","D2":"p","D1":"q","B1":"n"}}`;
    },
});

/*global createBlankChessBoardSVG*/
/*global boardWidth*/
QUnit.test("testing chessboard.js: creating a blank chess board", (assert) => {
    "use strict";
    let $board;
    let $squares;
    const idExpr = /[A-H][1-8]/;

    assert.equal(Chessboard.boardWidth, 8);
    $("#qunit-fixture").append(Chessboard.createBlankChessBoardSVG());

    $board = $("#qunit-fixture svg");

    $squares = $("#qunit-fixture rect");
    assert.equal($squares.length, 64);
    $squares.each(function() {
        assert.equal($(this).attr("width"), 62.5);
        assert.equal($(this).attr("height"), 62.5);
        assert.equal($(this).attr("class"), "dropzone");
        assert.equal($(this).attr("stroke"), "none");
        assert.equal($(this).attr("fill-opacity"), 1);
        assert.ok(idExpr.test($(this).attr("id")));
    });
});

/*global createChessBoard*/
QUnit.test("testing chessboard.js: create a non blank chess board", (assert) => {
    "use strict";
    let $board;
    let $chesspieces;
    const idExpr = /(knight|queen|king|bishop|rook|pawn)\-[A-H][1-8]/;

    $("#qunit-fixture").append("<div id='chessboard-surround'></div>");
    Chessboard.createChessBoard("WHITE", json);
    $board = $("#chessboard-surround svg");
    $chesspieces = $board.find("g.chesspiece");
    assert.equal($chesspieces.length, 32);

    $chesspieces.each(function() {
        assert.ok(idExpr.test($(this).attr("id")));
    });
});

QUnit.test("testing chessboard.js: test no of chess pieces on chessboard", (assert) => {
    "use strict";
    let $board;

    $("#qunit-fixture").append("<div id='chessboard-surround'></div>");
    Chessboard.createChessBoard("WHITE", json);
    $board = $("#chessboard-surround svg");
    assert.equal($board.find('g[id^="pawn"]').length, 16);
    assert.equal($board.find('g[id^="knight"]').length, 4);
    assert.equal($board.find('g[id^="bishop"]').length, 4);
    assert.equal($board.find('g[id^="rook"]').length, 4);
    assert.equal($board.find('g[id^="queen"]').length, 2);
    assert.equal($board.find('g[id^="king"]').length, 2);
});
