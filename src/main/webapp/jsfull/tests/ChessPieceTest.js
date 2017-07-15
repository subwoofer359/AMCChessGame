/*global QUnit*/
/*global $*/


var Colour,
    ChessPiece = ChessPieceModule.ChessPiece;
    
QUnit.module("Chesspiece object test", function () {
    Colour = ChessPieceModule.Colour;
});

QUnit.test("ChessPiece object creation", function (assert) {
    "use strict";
    var chessPiece = new ChessPiece("BLACK");

    assert.equal(chessPiece.colour, "BLACK");
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

/*
QUnit.test("ChessPiece embed in string", function (assert) {
    "use strict";
    var chessPiece = new ChessPiece("BLACK"),
        values = {
            name : 'Ted',
            phoneNumber : '9394993'
        },
        str = '${name}\'s phone number is ${phone number}',
        result;

        result = chessPiece.embedIntoString(str, values);

        assert.equal(result, values.name + '\'s phone number is ' + values.phoneNumber);
});
*/
