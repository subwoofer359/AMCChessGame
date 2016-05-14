/*global QUnit*/
/*global chessboard_module*/
/*global $*/
/*global promotion*/
/*global promotionAction*/
/*global promotionFixture*/
/*jslint unparam: true*/

var message,
    stompObject,
    $fixture,
    promotionModule;
QUnit.module("Promotion tests", {
    beforeEach: function () {
        "use strict";
        message = {};
        message.headers = {};
        stompObject = {
            gameUUID : "1234",
            playerColour : "WHITE",
            headers : { headerName : "token"},
            URL : "some url"
        };

        promotionModule = promotion(stompObject, promotionAction);
        promotionModule.setGameState("PAWN_PROMOTION");
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
    var stompClient,
        squareOfPawn,
        connection;

    stompClient = promotionFixture.getStompClient();

    connection = new promotionModule.OneViewStompConnection();
    connection.stompClient = stompClient;
    message.headers.TYPE = "UPDATE";

    connection.setUpStompConnection(promotionAction.handleUserInteract);
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

QUnit.test("Testing promotion dialogue is reshown for Topic promotion ERROR message", function (assert) {
    "use strict";

    var $dialog = $("#promotionDialog");

    assert.ok($dialog.hasClass("hidePromotionDialog"));

    promotionFixture.sendStatusMessageToTopic(promotionModule, promotionAction);

    assert.ok($dialog.hasClass("displayPromotionDialog"));

    $("#queenBtn").trigger("click");

    assert.ok($dialog.hasClass("hidePromotionDialog"));

    promotionFixture.sendErrorMessageToTopic(promotionModule, promotionAction);

    assert.ok($dialog.hasClass("displayPromotionDialog"), "Dialog should be display on promotion error");
});

QUnit.test("Testing promotion dialogue is reshown for User promotion ERROR message", function (assert) {
    "use strict";

    var $dialog = $("#promotionDialog");

    assert.ok($dialog.hasClass("hidePromotionDialog"));

    promotionFixture.sendStatusMessageToUser(promotionModule, promotionAction);

    assert.ok($dialog.hasClass("displayPromotionDialog"));

    $("#queenBtn").trigger("click");

    assert.ok($dialog.hasClass("hidePromotionDialog"));

    promotionFixture.sendErrorMessageToUser(promotionModule, promotionAction);

    assert.ok($dialog.hasClass("displayPromotionDialog"), "Dialog should be display on promotion error");
});

QUnit.test("Testing promotion dialogue is not reshown for Topic non promotion ERROR message", function (assert) {
    "use strict";

    var $dialog = $("#promotionDialog");

    assert.ok($dialog.hasClass("hidePromotionDialog"));

    promotionModule.setGameState("RUNNING");

    promotionFixture.sendErrorMessageToTopic(promotionModule, promotionAction);

    assert.ok($dialog.hasClass("hidePromotionDialog"), "Dialog should not be display on promotion error");
});

QUnit.test("Testing promotion dialogue is not reshown for User non promotion ERROR message", function (assert) {
    "use strict";

    var $dialog = $("#promotionDialog");

    assert.ok($dialog.hasClass("hidePromotionDialog"));

    promotionModule.setGameState("RUNNING");

    promotionFixture.sendErrorMessageToUser(promotionModule, promotionAction);

    assert.ok($dialog.hasClass("hidePromotionDialog"), "Dialog should not be display on promotion error");
});

QUnit.test("Testing promotion handling interact white", function (assert) {
    "use strict";

    var player = {},
        testFunction = function (piece) {assert.equal("Q", piece); };

    promotionAction.handleUserInteract(player, testFunction);
    player.colour = "BLACK";
    $("#queenBtn").trigger("click");

});

QUnit.test("Testing promotion handling interact white", function (assert) {
    "use strict";

    var player = {},
        testFunction = function (piece) {assert.equal("q", piece); };
    promotionAction.handleUserInteract(player, testFunction);
    player.colour = "WHITE";
    $("#queenBtn").trigger("click");
});

QUnit.test("test chessboard is already in promotion state", function (assert) {
    "use strict";
    var $dialog = $("#promotionDialog");

    assert.ok($dialog.hasClass("hidePromotionDialog"));

    promotionModule.checkBoardInPromotionState();

    assert.ok($dialog.hasClass("displayPromotionDialog"), "Dialog should be display on promotion check");
});