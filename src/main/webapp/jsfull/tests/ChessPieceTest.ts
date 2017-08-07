/*global QUnit*/
/*global $*/

import { ChessPiece, Colour } from "../Pieces/ChessPiece";

QUnit.module("Chesspiece object test");

QUnit.test("Colour properties test", (assert) => {
    "use strict";
    assert.expect(4);
    assert.ok(Colour.white.fill, "Property fill should exist");
    assert.ok(Colour.white.stroke, "Property stroke should exist");
    assert.ok(Colour.black.fill, "Property fill should exist");
    assert.ok(Colour.black.stroke, "Property Stroke should exist");
});

QUnit.test("ChessPiece object creation", (assert) => {
    "use strict";
    const chessPiece = new ChessPiece("BLACK");

    assert.equal(chessPiece.pieceColour, Colour.black);
});

QUnit.test("ChessPiece object creation with Colour object", (assert) => {
    const chessPiece = new ChessPiece(Colour.black);
    assert.equal(chessPiece.pieceColour, Colour.black, "Should be set to Colour black object");
});

QUnit.test("ChessPiece constants", (assert) => {
    "use strict";
    const chessPiece = new ChessPiece("BLACK");

    assert.equal(chessPiece.x, 0);
    assert.equal(chessPiece.y, 0);
    assert.equal(chessPiece.offsetXY, 62.5);
});

QUnit.test("ChessPiece getClasses for black player and black chesspiece", (assert) => {
    "use strict";
    const chessPiece = new ChessPiece("BLACK");

    assert.equal(chessPiece.getClasses(Colour.black), "chesspiece draggable");
});

QUnit.test("ChessPiece getClasses for white player and white chesspiece", (assert) => {
    "use strict";
    const chessPiece = new ChessPiece("WHITE");

    assert.equal(chessPiece.getClasses(Colour.white), "chesspiece draggable");
});

QUnit.test("ChessPiece getClasses for white player and black chesspiece", (assert) => {
    "use strict";
    const chessPiece = new ChessPiece("WHITE");

    assert.equal(chessPiece.getClasses(Colour.black), "chesspiece");
});

QUnit.test("ChessPiece getClasses for black player and white chesspiece", (assert) => {
    "use strict";
    const chessPiece = new ChessPiece("BLACK");

    assert.equal(chessPiece.getClasses(Colour.white), "chesspiece");
});

QUnit.test("ChessPiece getCoordX", (assert) => {
    "use strict";
    const chessPiece = new ChessPiece("BLACK");
    const location = {
            file: "A",
            rank: 1,
        };

    assert.equal(chessPiece.getCoordX(location), 0);

    location.file = "B";

    assert.equal(chessPiece.getCoordX(location), 62.5);

    location.file = "H";

    assert.equal(chessPiece.getCoordX(location), 62.5 * 7);
});

QUnit.test("ChessPiece getCoordY", (assert) => {
    "use strict";
    const chessPiece = new ChessPiece("BLACK");
    const location = {
            file: "A",
            rank: 1,
        };

    assert.equal(chessPiece.getCoordY(location), 62.5 * 7);

    location.rank = 2;
    assert.equal(chessPiece.getCoordY(location), 62.5 * 6);

    location.rank = 8;

    assert.equal(chessPiece.getCoordY(location), 0);
});

QUnit.test("ChessPiece parseSquareCoordinates", (assert) => {
    "use strict";
    const location = ChessPiece.parseSquareCoordinates("A1");

    assert.equal(location.file, "A");
    assert.equal(location.rank, "1");
});

QUnit.test("ChessPiece parseSquareCoordinates", (assert) => {
    "use strict";
    assert.throws(() => {
        ChessPiece.parseSquareCoordinates("T1");
    });
});

QUnit.test("ChessPiece toString", (assert) => {
    "use strict";
    assert.equal(new ChessPiece("BLACK").toString("", "", Colour.black), "");
});
