/*global QUnit*/
/*global chessboard_module*/
/*global $*/
var json;
QUnit.module("Promotion tests", {
    beforeEach: function () {
        "use strict";
        json = '{"squares":{"C8":"B","A8":"p","A1":"P","C7":"P","A5":"R","A7":"P","E7":"P","E8":"K","G7":"P","G8":"N","E2":"p","E1":"k","G2":"p","G1":"n","C1":"b","C2":"p","D8":"Q","B2":"p","D7":"P","B8":"N","B7":"P","F8":"B","F7":"P","H8":"R","F1":"b","H7":"P","H2":"p","H1":"r","F2":"p","D2":"p","D1":"q","B1":"n"}}';

    }
});

QUnit.test("testing search through chessboard for white pawns for Promotion", function (assert) {
    "use strict";
    json = '{"squares":{"H7":"p", "A8":"p"}}';
    var promotionCheck = promotion.findPawnForPromotion;
    var parsedJson = JSON.parse(json);
    var square = promotionCheck("WHITE", parsedJson);
    assert.equal(square, "a8");
});

QUnit.test("testing search through chessboard for black pawns for Promotion", function (assert) {
    "use strict";
    json = '{"squares":{"H2":"P", "A1":"P"}}';
    var promotionCheck = promotion.findPawnForPromotion;
    var parsedJson = JSON.parse(json);
    var square = promotionCheck("BLACK", parsedJson);
    assert.equal(square, "a1");
});

QUnit.test("testing search through chessboard for no valid Promotion", function (assert) {
    "use strict";
    json = '{"squares":{"H2":"P", "A2":"P", "H7":"P", "Gp":"p"}}';
    var promotionCheck = promotion.findPawnForPromotion;
    var parsedJson = JSON.parse(json);
    var square = promotionCheck("BLACK", parsedJson);
    assert.equal(square, null);
});

QUnit.test("testing parsing promotion string from server", function (assert) {
    "use strict";
    var message = "PAWN_PROMOTION (A,1)";
    var square = promotion.parsePromotionMessage(message);
    assert.equal(square, "a1" , "Not parsing message correctly");
});

QUnit.test("testing parsing promotion string from server", function (assert) {
    "use strict";
    var message = "PAWN_PROMOTION (A,8)";
    var square = promotion.parsePromotionMessage(message);
    assert.equal(square, "a8" , "Not parsing message correctly");
});

QUnit.test("throws", function (assert) {
    "use strict";
    var message = "PAWN_PROMOTION (K,8)";
    assert.throws(function() {promotion.parsePromotionMessage(message);});
});

QUnit.test("testing open stomp connection to Stomp Controller", function (assert) {
    "use strict";
    function stompClient() {
    	
    }
    stompClient.prototype = {
    	connect : function(header, callback) {
    		
    	}
    };
    var stompObject = {
    		gameUUID : "1234",
    	    playerColour : "WHITE",
    	    headers : { headerName : "token"},
    	    URL : "some url"
    };
    var stomp = promotion.setUpStompConnection(stompClient, stompObject);
    assert.notNull(stomp);
});