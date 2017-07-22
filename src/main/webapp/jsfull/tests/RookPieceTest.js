/*global QUnit*/
/*global $*/
var piece,
    RookPiece = RookPieceModule.RookPiece,
    Colour = ChessPieceModule.Colour,
    testData = {
        location : "B2",
        id : 234,
        pieceColour : Colour.black
    },
    svg;

QUnit.module("RookPiece object test", {
    beforeEach : function () {
        "use strict";
        piece = new RookPiece("BLACK");
        svg = piece.toString(testData.id, testData.location, testData.pieceColour);
    }
});

QUnit.test("RookPiece toString for id attribute", function (assert) {
    "use strict";
    var idRex = new RegExp('id="' + testData.id + '"');

    assert.ok(idRex.exec(svg), "id attribute not found");
});

QUnit.test("RookPiece toString for translate attribute", function (assert) {
    "use strict";
    var coord = piece.parseSquareCoordinates(testData.location),
        coordRex = new RegExp('translate\\(' + piece.getCoordX(coord) + 
        ',' + piece.getCoordY(coord) + '\\)');

    assert.ok(coordRex.exec(svg), "transform attribute not found");
});

QUnit.test("RookPiece toString for classes attribute", function (assert) {
    "use strict";
    var classesRex = new RegExp('class="' + piece.getClasses(Colour.black) + '"');

    assert.ok(classesRex.exec(svg), "class attribute not found");
});