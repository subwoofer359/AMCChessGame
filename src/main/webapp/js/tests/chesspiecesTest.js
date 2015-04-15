QUnit.test("testing chesspieces.js: function parseSquareCoordinates ", function (assert) {
    "use strict";
    var i,
        t,
        coordinate;

    for (i in coordinates) {
        if (coordinates.hasOwnProperty(i)) {
            for (t = 1; t <= boardWidth; t += 1) {
                coordinate = parseSquareCoordinates(i + t);
                assert.equal(coordinate.file, i);
                assert.equal(coordinate.rank, t);
            }
        }
    }
});

QUnit.test("testing chesspieces.js: function parseSquareCoordinates throws Exception ", function (assert) {
    "use strict";
    assert.throws(function () {
        parseSquareCoordinates("A9");
    }, /Not valid ChessBoard coordinate/);
    assert.throws(function () {
        parseSquareCoordinates("A");
    }, /Not valid ChessBoard coordinate/);
    assert.throws(function () {
        parseSquareCoordinates("I1");
    }, /Not valid ChessBoard coordinate/);
});