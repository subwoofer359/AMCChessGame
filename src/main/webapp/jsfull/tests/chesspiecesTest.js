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
        coordinates = ChessPiecesModule.coordinates,
        chesspieces = ChessPiecesModule,
        boardWidth = chessboardModule.boardWidth,
        piece = new ChessPiecesModule.BishopPiece(Colour.black);

    for (i in coordinates) {
        if (coordinates.hasOwnProperty(i)) {
            for (t = 1; t <= boardWidth; t += 1) {
                coordinate = piece.parseSquareCoordinates(i + t);
                assert.equal(coordinate.file, i);
                assert.equal(coordinate.rank, t);
            }
        }
    }
});

QUnit.test("testing chesspieces.js: function parseSquareCoordinates throws Exception ", function (assert) {
    "use strict";
    var piece = new ChessPiecesModule.BishopPiece(Colour.black);
    assert.throws(function () {
        piece.parseSquareCoordinates("A9");
    }, /Not valid ChessBoard coordinate/);

    assert.throws(function () {
        piece.parseSquareCoordinates("A");
    }, /Not valid ChessBoard coordinate/);
    
    assert.throws(function () {
        piece.parseSquareCoordinates("I1");
    }, /Not valid ChessBoard coordinate/);
});