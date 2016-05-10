/*global QUnit*/
/*global chessboard_module*/
/*global $*/
/*global promotion*/
var json,
    message,
    stompObject,
    $fixture;
QUnit.module("Promotion tests", {
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
        
        $fixture = $("#qunit-fixture");
        $fixture.append('<div id="promotionDialog" class="container hidePromotionDialog"><div class="row"><div class="panel-primary"><div class="panel-heading"><h3 class="panel-title">Promote Chess Piece</h3></div><div class="panel-body">      <div class="row"><div class="col-xs-12 col-md-3"><button id="queenBtn" class="btn btn-primary" type="button"><img src="../../img/Queen.svg" alt="Queen"></button></div><div class="col-xs-12 col-md-3"><button id="rookBtn" class="btn btn-primary" type="button"><img src="../../img/Rook.svg" alt="Rook"></button></div><div class="col-xs-12 col-md-3">             <button id="knightBtn" class="btn btn-primary" type="button"><img src="../../img/Knight.svg" alt="Knight"></button>         </div><div class="col-xs-12 col-md-3"><button id="bishopBtn" class="btn btn-primary" type="button"><img src="../../img/Bishop.svg" alt="Bishop"></button></div></div></div></div></div></div>');
    }
});

QUnit.test("testing search through chessboard for white pawns for Promotion", function (assert) {
    "use strict";
    json = '{"squares":{"H7":"p", "A8":"p"}}';
    var promotionCheck = promotion.findPawnForPromotion,
        parsedJson = JSON.parse(json),
        square = promotionCheck("WHITE", parsedJson);
    assert.equal(square, "a8");
});

QUnit.test("testing search through chessboard for black pawns for Promotion", function (assert) {
    "use strict";
    json = '{"squares":{"H2":"P", "A1":"P"}}';
    var promotionCheck = promotion.findPawnForPromotion,
        parsedJson = JSON.parse(json),
        square = promotionCheck("BLACK", parsedJson);
    assert.equal(square, "a1");
});

QUnit.test("testing search through chessboard for no valid Promotion", function (assert) {
    "use strict";
    json = '{"squares":{"H2":"P", "A2":"P", "H7":"P", "Gp":"p"}}';
    var promotionCheck = promotion.findPawnForPromotion,
        parsedJson = JSON.parse(json),
        square = promotionCheck("BLACK", parsedJson);
    assert.equal(square, null);
});

QUnit.test("testing parsing promotion string from server", function (assert) {
    "use strict";
    var message = "PAWN_PROMOTION (A,1)",
        square = promotion.parsePromotionMessage(message);
    assert.equal(square, "a1", "Not parsing message correctly");
});

QUnit.test("testing parsing promotion string from server", function (assert) {
    "use strict";
    var message = "PAWN_PROMOTION (A,8)",
        square = promotion.parsePromotionMessage(message);
    assert.equal(square, "a8", "Not parsing message correctly");
});

QUnit.test("throws", function (assert) {
    "use strict";
    var message = "PAWN_PROMOTION (K,8)";
    assert.throws(function () {promotion.parsePromotionMessage(message); });
});


function sendStatusMessageToUser() {
        var StompClient = function StompClient() {},
        stompClient,
        squareOfPawn;

    StompClient.prototype = {
        connect : function (header, callback) {
            callback();
        },
        subscribe : function (destination, callback) {
            if ("/user/queue/updates" === destination) {
                this.userSubscribe = callback;
            }
        },
        send : function (destination, priority, message) {

        },
        getUserSubscribe : function () {
            return this.userSubscribe;
        }
    };

    stompClient = new StompClient();
    message.headers.TYPE = "STATUS";

    promotion.setUpStompConnection(stompClient, stompObject);


    message.body = "PAWN_PROMOTION (A,1)";

    squareOfPawn =  stompClient.getUserSubscribe().call(stompClient, message);
    return squareOfPawn;
}

QUnit.test("testing STATUS message from Stomp Server to User receiver", function (assert) {
    "use strict";

    var squareOfPawn = sendStatusMessageToUser();
    assert.equal(squareOfPawn, "a1");
});

QUnit.test("testing UPDATE message from Stomp Server to User receiver", function (assert) {
    "use strict";
    var StompClient = function () {},
        stompClient,
        squareOfPawn;

    StompClient.prototype = {
        connect : function (header, callback) {
            callback();
        },
        subscribe : function (destination, callback) {
            if ("/user/queue/updates" === destination) {
                this.userSubscribe = callback;
            }
        },
        send : function (destination, priority, message) {

        },
        getUserSubscribe : function () {
            return this.userSubscribe;
        }
    };

    stompClient = new StompClient();
    message.headers.TYPE = "UPDATE";

    promotion.setUpStompConnection(stompClient, stompObject);
    message.body = "CHESSBOARD";
    squareOfPawn =  stompClient.getUserSubscribe().call(stompClient, message);

    assert.equal(undefined, squareOfPawn);
});

QUnit.test("Testing promotion dialogue is displayed", function (assert) {
    "use strict";
    
    var $dialog = $("#promotionDialog");
    
    assert.ok($dialog.hasClass("hidePromotionDialog"));
    
    sendStatusMessageToUser();
    
    assert.ok($dialog.hasClass("displayPromotionDialog"));
});

QUnit.test("Testing promotion dialogue is hidden", function (assert) {
    "use strict";
    
    var $dialog = $("#promotionDialog");
    
    assert.ok($dialog.hasClass("hidePromotionDialog"));
    
    sendStatusMessageToUser();
    
    assert.ok($dialog.hasClass("displayPromotionDialog"));
    
    $("#queenBtn").trigger("click");
    
    assert.ok($dialog.hasClass("hidePromotionDialog"));
});
