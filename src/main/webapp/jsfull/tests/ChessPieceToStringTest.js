/*global QUnit*/
/*global $*/
var pieces,
    Colour = ChessPiecesModule.Colour,
    testData = {
        location : "B2",
        id : 234,
        playerColour : Colour.black
    },
    svg;

QUnit.module("ChessPiece object test", {
    beforeEach : function () {
        "use strict";
        pieces = [
            new ChessPiecesModule.RookPiece(Colour.black),
            new ChessPiecesModule.QueenPiece(Colour.white),
            new ChessPiecesModule.BishopPiece(Colour.black),
            new ChessPiecesModule.KingPiece(Colour.white),
            new ChessPiecesModule.KnightPiece(Colour.black),
            new ChessPiecesModule.PawnPiece(Colour.white)
        ];
    }
});

QUnit.test("ChessPiece toString for id attribute", function (assert) {
    "use strict";
    var idRex = new RegExp('id="' + testData.id + '"');
    pieces.forEach(function (element) {
        svg = element.toString(testData.id, testData.location, testData.playerColour);
        assert.ok(idRex.exec(svg), "id attribute not found");
    });
});

QUnit.test("ChessPiece toString for translate attribute", function (assert) {
    "use strict";

    pieces.forEach(function (element) {
        var coord = element.parseSquareCoordinates(testData.location),
            coordRex = new RegExp('translate\\(' + element.getCoordX(coord) + 
        ',' + element.getCoordY(coord) + '\\)');

        svg = element.toString(testData.id, testData.location, testData.playerColour);
        assert.ok(coordRex.exec(svg), "transform attribute not found");
    });
        

    
});

QUnit.test("ChessPiece toString for classes attribute", function (assert) {
    "use strict";
    pieces.forEach(function (element) {
        var classesRex = new RegExp('class="' + element.getClasses(Colour.black) + '"');

        svg = element.toString(testData.id, testData.location, testData.playerColour);
        assert.ok(classesRex.exec(svg), "class attribute not found");
    });
});

QUnit.test("ChessPiece toString for classes attribute with String", function (assert) {
    "use strict";
    pieces.forEach(function (element) {
        var classesRex = new RegExp('class="' + element.getClasses(Colour.black) + '"');

        svg = element.toString(testData.id, testData.location, testData.playerColour.toString());
        assert.ok(classesRex.exec(svg), "class attribute not found");
    });
});

QUnit.test("ChessPiece toString for fill property", function (assert) {
    "use strict";
    pieces.forEach(function (element) {
        var classesRex = new RegExp('fill:' + element.getColour().fill);

        svg = element.toString(testData.id, testData.location, "BLACK");
        assert.ok(classesRex.exec(svg), "class attribute not found");
    });
});
