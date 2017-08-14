/*global QUnit*/
/*global Chessboard*/
/*global $*/

import "jquery";
import * as Snap from "snapsvg";
import * as Chessboard from "../Chessboard";
import { ChessPiece, Colour} from "../Pieces/ChessPiece";

QUnit.module("ChessBoard Move test", {
    beforeEach : () => {
        $("#qunit-fixture").append('<div id="chessboard-surround"></div>');
        Chessboard.createChessBoard("WHITE",
            `{"squares":{"C8":"B","A2":"p","A1":"r","C7":"P","A8":"R","A7":"P","E7":"P",
                "E8":"K","G7":"P","G8":"N","E2":"p","E1":"k","G2":"p","G1":"n","C1":"b",
                "C2":"p","D8":"Q","B2":"p","D7":"P","B8":"N","B7":"P","F8":"B","F7":"P",
                "H8":"R","F1":"b","H7":"P","H2":"p","H1":"r","F2":"p","D2":"p","D1":"q",
                "B1":"n"}}`);
    },
});

QUnit.test("Test", (assert) => {
    const done = assert.async();
    Chessboard.move("A2-A3", () => {
        const board = Snap("svg");
        const movedPiece = board.select('g[id *= "A2"]');
        assert.equal(movedPiece.transform().local, "t0,312.5", "Should be equal");
        done();
     });
});

QUnit.test("Test Move excepted an empty move", (assert) => {
    let exceptionThrown = false;
    try {
        Chessboard.move("");
    } catch (e) {
        exceptionThrown = true;
    }
    assert.notOk(exceptionThrown, "Should accept empty string without throwing an exception");
});

QUnit.test("Test ChessBoard move throws error on invalid move", (assert) => {
    assert.throws(() => {
        Chessboard.move("A2-J3");
    }, "Should throw an exception for an invalid move string");
    assert.throws(() => {
        Chessboard.move("A2");
    }, "Should throw an exception for an invalid move string");
});

QUnit.test("Test ChessBoard move throws error on invalid move", (assert) => {
    assert.throws(() => {
        Chessboard.move("A2-J3");
    }, "Should throw an exception for an invalid move string");
    assert.throws(() => {
        Chessboard.move("A2");
    }, "Should throw an exception for an invalid move string");
});

/*
 * The case when a page loads and there is no chessboard
 */
QUnit.test("Test Move except an missing chessboard", (assert) => {
    let exceptionThrown = false;
    $("#chessboard").remove();
    try {
        Chessboard.move("A2-A3");
    } catch (e) {
        exceptionThrown = true;
    }
    assert.notOk(exceptionThrown, "Should accept move string without throwing exception");
});

QUnit.config.testTimeout = 3000;

QUnit.test("Test Move excepted no piece on square", (assert) => {
    const done = assert.async();

    Chessboard.move("A3-A4", () => {
        assert.ok(true);
        done();
    });
});

QUnit.test("Test Move excepted no piece on square and callback", (assert) => {
    let exceptionThrown = false;
    try {
        Chessboard.move("A3-A4");
    } catch (e) {
        exceptionThrown = true;
    }
    assert.notOk(exceptionThrown, "Should accept move string without throwing exception");
});
