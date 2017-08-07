/*global QUnit*/
/*global $*/

import { BishopPiece } from "../Pieces/BishopPiece";
import { ChessPiece, Colour } from "../Pieces/ChessPiece";
import { KingPiece } from "../Pieces/KingPiece";
import { KnightPiece } from "../Pieces/KnightPiece";
import { PawnPiece } from "../Pieces/PawnPiece";
import { QueenPiece } from "../Pieces/QueenPiece";
import { RookPiece } from "../Pieces/RookPiece";

let pieces;
const testData = {
        id : 234,
        location : "B2",
        playerColour : Colour.black,
    };
let svg;

QUnit.module("ChessPiece object test", {
    beforeEach : () => {
        "use strict";
        pieces = [
            new RookPiece(Colour.black),
            new QueenPiece(Colour.white),
            new BishopPiece(Colour.black),
            new KingPiece(Colour.white),
            new KnightPiece(Colour.black),
            new PawnPiece(Colour.white),
        ];
    },
});

QUnit.test("ChessPiece toString for id attribute", (assert) => {
    "use strict";
    const idRex = new RegExp(`id="${testData.id}"`);
    pieces.forEach((element) => {
        svg = element.toString(testData.id, testData.location, testData.playerColour);
        assert.ok(idRex.exec(svg), "id attribute not found");
    });
});

QUnit.test("ChessPiece toString for translate attribute", (assert) => {
    "use strict";

    pieces.forEach((element) => {
        const coord = ChessPiece.parseSquareCoordinates(testData.location);
        const coordRex = new RegExp(`translate\\(${element.getCoordX(coord)},${element.getCoordY(coord)}\\)`);
        svg = element.toString(testData.id, testData.location, testData.playerColour);

        assert.ok(coordRex.exec(svg), "transform attribute not found");
    });
});

QUnit.test("ChessPiece toString for classes attribute", (assert) => {
    "use strict";
    pieces.forEach((element) => {
        const classesRex = new RegExp(`class="${element.getClasses(Colour.black)}"`);
        svg = element.toString(testData.id, testData.location, testData.playerColour);

        assert.ok(classesRex.exec(svg), "class attribute not found");
    });
});

QUnit.test("ChessPiece toString for classes attribute with String", (assert) => {
    "use strict";
    pieces.forEach((element) => {
        const classesRex = new RegExp(`class="${element.getClasses(Colour.black)}"`);
        svg = element.toString(testData.id, testData.location, testData.playerColour.toString);

        assert.ok(classesRex.exec(svg), "class attribute not found");
    });
});

QUnit.test("ChessPiece toString for fill property", (assert) => {
    "use strict";
    pieces.forEach((element) => {
        const classesRex = new RegExp(`fill:${element.pieceColour.fill}`);
        svg = element.toString(testData.id, testData.location, "BLACK");

        assert.ok(classesRex.exec(svg), "class attribute not found");
    });
});
