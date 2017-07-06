/*global QUnit*/
/*global $*/
QUnit.module("chesspieces test");

/*global coordinates*/
/*global chessboardModule*/
/*global chesspiecesModule*/
/*global parseSquareCoordinates*/
QUnit.test("testing chesspieces.js: function parseSquareCoordinates ", function (assert) {
    "use strict";
    var i,
        t,
        coordinate,
        coordinates = chesspiecesModule.coordinates,
        chesspieces = chesspiecesModule,
        boardWidth = chessboardModule.boardWidth;

    for (i in coordinates) {
        if (coordinates.hasOwnProperty(i)) {
            for (t = 1; t <= boardWidth; t += 1) {
                coordinate = chesspieces.parseSquareCoordinates(i + t);
                assert.equal(coordinate.file, i);
                assert.equal(coordinate.rank, t);
            }
        }
    }
});

QUnit.test("testing chesspieces.js: function parseSquareCoordinates throws Exception ", function (assert) {
    "use strict";
    assert.throws(function () {
        chesspiecesModule.parseSquareCoordinates("A9");
    }, /Not valid ChessBoard coordinate/);
    assert.throws(function () {
        chesspiecesModule.parseSquareCoordinates("A");
    }, /Not valid ChessBoard coordinate/);
    assert.throws(function () {
        chesspiecesModule.parseSquareCoordinates("I1");
    }, /Not valid ChessBoard coordinate/);
});

/*global ChessPieces*/

QUnit.test("testing chesspieces.js: white player pawn creation", function (assert) {
    "use strict";
    var chesspieces = new chesspiecesModule.ChessPieces("WHITE"),
        whiteId = "whiteId",
        blackId = "blackId",
        whiteChesspiece,
        blackChesspiece,
        whitePawn = chesspieces.pawn(whiteId, "A1", chesspieces.colour.white),
        blackPawn = chesspieces.pawn(blackId, "A2", chesspieces.colour.black);

    $("#qunit-fixture").append(whitePawn);
    $("#qunit-fixture").append(blackPawn);
    whiteChesspiece = $("g#" + whiteId);
    blackChesspiece = $("g#" + blackId);
    assert.equal(whiteChesspiece.attr("id"), whiteId);
    assert.equal(whiteChesspiece.attr("class"), "chesspiece draggable");
    assert.equal(blackChesspiece.attr("id"), blackId);
    assert.equal(blackChesspiece.attr("class"), "chesspiece");
});

QUnit.test("testing chesspieces.js: black player pawn creation", function (assert) {
    "use strict";
    var chesspieces = new chesspiecesModule.ChessPieces("BLACK"),
        whiteId = "whiteId",
        blackId = "blackId",
        whiteChesspiece,
        blackChesspiece,
        whitePawn = chesspieces.pawn(whiteId, "A1", chesspieces.colour.white),
        blackPawn = chesspieces.pawn(blackId, "A2", chesspieces.colour.black);

    $("#qunit-fixture").append(whitePawn);
    $("#qunit-fixture").append(blackPawn);
    whiteChesspiece = $("g#" + whiteId);
    blackChesspiece = $("g#" + blackId);
    assert.equal(whiteChesspiece.attr("id"), whiteId);
    assert.equal(whiteChesspiece.attr("class"), "chesspiece");
    assert.equal(blackChesspiece.attr("id"), blackId);
    assert.equal(blackChesspiece.attr("class"), "chesspiece draggable");
});