/*global QUnit*/
/*global $*/


var Colour,
    ChessPiece = ChessPiecesModule.ChessPiece;
    
QUnit.module("Chesspiece object test", {
    beforeEach : function () {
        Colour = ChessPiecesModule.Colour;
    }
});

QUnit.test("Colour properties test", function (assert) {
    "use strict";
    assert.expect(4);
    assert.ok(ChessPiecesModule.Colour.white.fill, "Property fill should exist");
    assert.ok(ChessPiecesModule.Colour.white.stroke, "Property stroke should exist");
    assert.ok(ChessPiecesModule.Colour.black.fill, "Property fill should exist");
    assert.ok(ChessPiecesModule.Colour.black.stroke, "Property Stroke should exist");
});

QUnit.test("ChessPiece object creation", function (assert) {
    "use strict";
    var chessPiece = new ChessPiece("BLACK");

    assert.equal(chessPiece.pieceColour, Colour.black);
});

QUnit.test("ChessPiece object creation with Colour object", function (assert) {
    var chessPiece = new ChessPiece(Colour.black);
    assert.equal(chessPiece.pieceColour, Colour.black, "Should be set to Colour black object");
});

QUnit.test("ChessPiece constants", function (assert) {
    "use strict";
    var chessPiece = new ChessPiece("BLACK");

    assert.equal(chessPiece.x, 0);
    assert.equal(chessPiece.y, 0);
    assert.equal(chessPiece.offsetXY, 62.5);
});

QUnit.test("ChessPiece getClasses for black player and black chesspiece", function (assert) {
    "use strict";
    var chessPiece = new ChessPiece("BLACK");

    assert.equal(chessPiece.getClasses(Colour.black), "chesspiece draggable");
});

QUnit.test("ChessPiece getClasses for white player and white chesspiece", function (assert) {
    "use strict";
    var chessPiece = new ChessPiece("WHITE");

    assert.equal(chessPiece.getClasses(Colour.white), "chesspiece draggable");
});

QUnit.test("ChessPiece getClasses for white player and black chesspiece", function (assert) {
    "use strict";
    var chessPiece = new ChessPiece("WHITE");

    assert.equal(chessPiece.getClasses(Colour.black), "chesspiece");
});

QUnit.test("ChessPiece getClasses for black player and white chesspiece", function (assert) {
    "use strict";
    var chessPiece = new ChessPiece("BLACK");

    assert.equal(chessPiece.getClasses(Colour.white), "chesspiece");
});

QUnit.test("ChessPiece getCoordX", function (assert) {
    "use strict";
    var chessPiece = new ChessPiece("BLACK"),
        location = {
            file : 'A',
            rank : 1
        };

    assert.equal(chessPiece.getCoordX(location), 0);

    location.file = 'B';

    assert.equal(chessPiece.getCoordX(location), 62.5);

    location.file = 'H';

    assert.equal(chessPiece.getCoordX(location), 62.5 * 7);
});

QUnit.test("ChessPiece getCoordY", function (assert) {
    "use strict";
    var chessPiece = new ChessPiece("BLACK"),
        location = {
            file : 'A',
            rank : 1
        };

    assert.equal(chessPiece.getCoordY(location), 62.5 * 7);

    location.rank = '2';

    assert.equal(chessPiece.getCoordY(location), 62.5 * 6);

    location.rank = '8';

    assert.equal(chessPiece.getCoordY(location), 0);
});

QUnit.test("ChessPiece parseSquareCoordinates", function (assert) {
    "use strict";
    var chessPiece = new ChessPiece("BLACK"), 
    location = chessPiece.parseSquareCoordinates("A1");

    assert.equal(location.file, "A");
    assert.equal(location.rank, "1");
});

QUnit.test("ChessPiece parseSquareCoordinates", function (assert) {
    "use strict";
    var chessPiece = new ChessPiece("BLACK");
    assert.throws(function () {
        chessPiece.parseSquareCoordinates("T1")
    });
});

QUnit.test("ChessPiece toString", function (assert) {
    "use strict";
    var chessPiece = new ChessPiece("BLACK");
    assert.equal(chessPiece.toString(), "");
});