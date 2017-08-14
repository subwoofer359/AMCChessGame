/*global QUnit*/
/*global $*/
import * as Chessboard from "../Chessboard";
import { BishopPiece } from "../Pieces/BishopPiece";
import { ChessPiece, Colour, coordinates } from "../Pieces/ChessPiece";

QUnit.module("chesspieces test");

/*global coordinates*/
/*global Chessboard*/
/*global chesspiecesModule*/
/*global parseSquareCoordinates*/
QUnit.test("testing chesspieces.js: function parseSquareCoordinates ", (assert) => {
    "use strict";
    let i;
    let t;
    let coordinate;
    const boardWidth = Chessboard.boardWidth;

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

QUnit.test("testing chesspieces.js: function parseSquareCoordinates throws Exception ", (assert) => {
    "use strict";
    assert.throws(() => {
        ChessPiece.parseSquareCoordinates("A9");
    }, /Not valid ChessBoard coordinate/);

    assert.throws(() => {
        ChessPiece.parseSquareCoordinates("A");
    }, /Not valid ChessBoard coordinate/);

    assert.throws(() => {
        ChessPiece.parseSquareCoordinates("I1");
    }, /Not valid ChessBoard coordinate/);
});
