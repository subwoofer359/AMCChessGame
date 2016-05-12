/*global QUnit*/
/*global promotion*/
/*global promotionAction*/
var json,
    message,
    stompObject,
    $fixture,
    promotionModule;
QUnit.module("Promotion Message parsing tests", {
    beforeEach: function () {
        "use strict";
        json = '{"squares":{"C8":"B","A8":"p","A1":"P","C7":"P","A5":"R","A7":"P","E7":"P","E8":"K","G7":"P","G8":"N","E2":"p","E1":"k","G2":"p","G1":"n","C1":"b","C2":"p","D8":"Q","B2":"p","D7":"P","B8":"N","B7":"P","F8":"B","F7":"P","H8":"R","F1":"b","H7":"P","H2":"p","H1":"r","F2":"p","D2":"p","D1":"q","B1":"n"}}';
        message = {};
        message.headers = {};
        stompObject = {
            gameUUID : "1234",
            playerColour : "WHITE",
            headers : { headerName : "token"},
            URL : "some url"
        };

        promotionModule = promotion(stompObject, promotionAction);
    }
});

QUnit.test("testing search through chessboard for white pawns for Promotion", function (assert) {
    "use strict";
    json = '{"squares":{"H7":"p", "A8":"p"}}';
    var promotionCheck = promotionModule.findPawnForPromotion,
        parsedJson = JSON.parse(json),
        square = promotionCheck("WHITE", parsedJson);
    assert.equal(square, "a8");
});

QUnit.test("testing search through chessboard for black pawns for Promotion", function (assert) {
    "use strict";
    json = '{"squares":{"H2":"P", "A1":"P"}}';
    var promotionCheck = promotionModule.findPawnForPromotion,
        parsedJson = JSON.parse(json),
        square = promotionCheck("BLACK", parsedJson);
    assert.equal(square, "a1");
});

QUnit.test("testing search through chessboard for no valid Promotion", function (assert) {
    "use strict";
    json = '{"squares":{"H2":"P", "A2":"P", "H7":"P", "Gp":"p"}}';
    var promotionCheck = promotionModule.findPawnForPromotion,
        parsedJson = JSON.parse(json),
        square = promotionCheck("BLACK", parsedJson);
    assert.equal(square, null);
});

QUnit.test("testing parsing promotion string from server", function (assert) {
    "use strict";
    var message = "PAWN_PROMOTION (A,1)",
        square = promotionModule.parsePromotionMessage(message);
    assert.equal(square, "a1", "Not parsing message correctly");
});

QUnit.test("testing parsing promotion string from server", function (assert) {
    "use strict";
    var message = "PAWN_PROMOTION (A,8)",
        square = promotionModule.parsePromotionMessage(message);
    assert.equal(square, "a8", "Not parsing message correctly");
});

QUnit.test("throws", function (assert) {
    "use strict";
    var message = "PAWN_PROMOTION (K,8)";
    assert.throws(function () {promotionModule.parsePromotionMessage(message); });
});