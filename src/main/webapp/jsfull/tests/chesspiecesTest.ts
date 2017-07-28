/*global QUnit*/
/*global $*/
import { chessboardModule } from "../chessboard";
import { coordinates, Colour, ChessPiece } from "../Pieces/ChessPiece";
import { BishopPiece } from "../Pieces/BishopPiece"

QUnit.module("chesspieces test");

/*global coordinates*/
/*global chessboardModule*/
/*global chesspiecesModule*/
/*global parseSquareCoordinates*/
QUnit.test("testing chesspieces.js: function parseSquareCoordinates ", function (assert) {
    "use strict";
    var i,
        t,
        Colour = Colour,
        coordinate,
        //coordinates = coordinates,
        boardWidth = chessboardModule.boardWidth,
        piece = new BishopPiece(Colour.black);

    for (i in coordinates) {
        if (coordinates.hasOwnProperty(i)) {
            for (t = 1; t <= boardWidth; t += 1) {
                coordinate = ChessPiece.parseSquareCoordinates(i + t);
                assert.equal(coordinate.file, i);
                assert.equal(coordinate.rank, t);
            }
        }
    }
});

QUnit.test("testing chesspieces.js: function parseSquareCoordinates throws Exception ", function (assert) {
    "use strict";
    var piece = new BishopPiece(Colour.black);
    assert.throws(function () {
        ChessPiece.parseSquareCoordinates("A9");
    }, /Not valid ChessBoard coordinate/);

    assert.throws(function () {
        ChessPiece.parseSquareCoordinates("A");
    }, /Not valid ChessBoard coordinate/);
    
    assert.throws(function () {
        ChessPiece.parseSquareCoordinates("I1");
    }, /Not valid ChessBoard coordinate/);
});