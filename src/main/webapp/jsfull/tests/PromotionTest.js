/*global QUnit*/
/*global chessboard_module*/
/*global $*/
/*global promotion*/
/*global promotionFixture*/
var json,
    message,
    stompObject,
    $fixture,
    promotionModule;
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
        
        promotionModule = promotion(stompObject, promotionAction);
        $fixture = $("#qunit-fixture");
        $fixture.append('<div id="promotionDialog" class="container hidePromotionDialog"><div class="row"><div class="panel-primary"><div class="panel-heading"><h3 class="panel-title">Promote Chess Piece</h3></div><div class="panel-body">      <div class="row"><div class="col-xs-12 col-md-3"><button id="queenBtn" class="btn btn-primary" type="button"><img src="../../img/Queen.svg" alt="Queen"></button></div><div class="col-xs-12 col-md-3"><button id="rookBtn" class="btn btn-primary" type="button"><img src="../../img/Rook.svg" alt="Rook"></button></div><div class="col-xs-12 col-md-3">             <button id="knightBtn" class="btn btn-primary" type="button"><img src="../../img/Knight.svg" alt="Knight"></button>         </div><div class="col-xs-12 col-md-3"><button id="bishopBtn" class="btn btn-primary" type="button"><img src="../../img/Bishop.svg" alt="Bishop"></button></div></div></div></div></div></div>');
    }
});

QUnit.test("testing STATUS message from Stomp Server to User receiver", function (assert) {
    "use strict";

    var squareOfPawn = promotionFixture.sendStatusMessageToUser(promotionModule, promotionAction);
    assert.equal(squareOfPawn, "a1");
});

QUnit.test("testing STATUS message from Stomp Server to Topic receiver", function (assert) {
    "use strict";
    var squareOfPawn = promotionFixture.sendStatusMessageToTopic(promotionModule, promotionAction);
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

    promotionModule.setUpStompConnection(stompClient, promotionAction.handleUserInteract);
    message.body = "CHESSBOARD";
    squareOfPawn =  stompClient.getUserSubscribe().call(stompClient, message);

    assert.equal(undefined, squareOfPawn);
});

QUnit.test("Testing promotion dialogue is displayed using User", function (assert) {
    "use strict";
    
    var $dialog = $("#promotionDialog");
    
    assert.ok($dialog.hasClass("hidePromotionDialog"));
    
    promotionFixture.sendStatusMessageToUser(promotionModule, promotionAction);
    
    assert.ok($dialog.hasClass("displayPromotionDialog"));
});

QUnit.test("Testing promotion dialogue is displayed using Topic", function (assert) {
    "use strict";
    
    var $dialog = $("#promotionDialog");
    
    assert.ok($dialog.hasClass("hidePromotionDialog"));
    
    promotionFixture.sendStatusMessageToTopic(promotionModule, promotionAction);
    
    assert.ok($dialog.hasClass("displayPromotionDialog"));
});

QUnit.test("Testing promotion dialogue is hidden", function (assert) {
    "use strict";
    
    var $dialog = $("#promotionDialog");
    
    assert.ok($dialog.hasClass("hidePromotionDialog"));
    
    promotionFixture.sendStatusMessageToUser(promotionModule, promotionAction);
    
    assert.ok($dialog.hasClass("displayPromotionDialog"));
    
    $("#queenBtn").trigger("click");
    
    assert.ok($dialog.hasClass("hidePromotionDialog"));
});

QUnit.test("Testing promotion dialogue is hidden for Topic", function (assert) {
    "use strict";
    
    var $dialog = $("#promotionDialog");
    
    assert.ok($dialog.hasClass("hidePromotionDialog"));
    
    promotionFixture.sendStatusMessageToTopic(promotionModule, promotionAction);
    
    assert.ok($dialog.hasClass("displayPromotionDialog"));
    
    $("#queenBtn").trigger("click");
    
    assert.ok($dialog.hasClass("hidePromotionDialog"));
});

QUnit.test("Testing promotion handling interact white", function (assert) {
    "use strict";
    
    var player = {},
        testFunction = function(piece) {assert.equal("Q", piece); },
        handler = promotionAction.handleUserInteract(player, testFunction);
    player.colour = "BLACK"; 
    $("#queenBtn").trigger("click");
        
});

QUnit.test("Testing promotion handling interact white", function (assert) {
    "use strict";
    
    var player = {},
        testFunction = function(piece) {assert.equal("q", piece); },
        handler = promotionAction.handleUserInteract(player, testFunction);
    player.colour = "WHITE";
    $("#queenBtn").trigger("click");
        
});