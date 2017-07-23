/*global QUnit*/
/*global $*/
/*global chessboardModule*/
/*global chesspiecesModule*/
/*global chessgameportalModule*/
var stompObject = {},
    gameUID,
    playerName,
    opponentName,
    playerColour,
    myStompActions,
    updatePlayerCall,
    updateChessBoardCall,
    oldCreateChessBoard,
    chessboardString = '{"squares":{"G1":"n","G2":"p","E1":"k","E2":"p","C1":"b","C2":"p","A1":"r","G7":"P","A2":"p","G8":"N","E7":"P","E8":"K","C7":"P","C8":"B","A7":"P","A8":"R","H1":"r","H2":"p","F1":"b","F2":"p","D1":"q","D2":"p","B1":"n","H7":"P","B2":"p","H8":"R","F7":"P","F8":"B","D7":"P","D8":"Q","B7":"P","B8":"N"},"currentPlayer":{"colour":"WHITE","player":{"id":6,"name":"Caleb Doyle","userName":"Caleb"}}}';

/* mock function for player.js:updatePlayer */
function updatePlayer() {
    "use strict";
    updatePlayerCall = true;
}

/* mock function for chessboard.js:createChessBoard */
(function () {
    "use strict";
    oldCreateChessBoard = chessboardModule.createChessBoard;
    chessboardModule.createChessBoard = function (playerColour, json) {
        updateChessBoardCall = true;
        oldCreateChessBoard(playerColour, json);
    };
}());

QUnit.module("Stomp Message tests", {
    beforeEach : function () {
        "use strict";
        $("#qunit-fixture").html('<div id="my-alert"><div class="alert"></div></div>');
        stompObject.gameUID = "1234";
        stompObject.playerName = "testPlayer";
        stompObject.opponentName = "testOpponent";
        stompObject.playerColour = ChessPiecesModule.Colour.black;
        stompObject.headers = {};
        myStompActions = new chessgameportalModule.StompActions(stompObject.gameUID, stompObject.playerName,
                                          stompObject.opponentName, stompObject.playerColour);
        updatePlayerCall = false;
        updateChessBoardCall = false;
    }
});

QUnit.test("testing StompActions: function updateChessBoard", function (assert) {
    "use strict";
    var json = chessboardString;

    myStompActions.updateChessBoard(json);
    assert.equal(true, updatePlayerCall);
    assert.equal(true, updateChessBoardCall);
});

QUnit.test("testing StompActions: function updateChessBoard", function (assert) {
    "use strict";
    var json = chessboardString,
        oneViewStompActions = new chessgameportalModule.OneViewStompActions(stompObject.gameUID, stompObject.playerName,
                                          stompObject.opponentName, stompObject.playerColour);
    oneViewStompActions.updateChessBoard(json);
    assert.equal(true, updatePlayerCall);
    assert.equal(true, updateChessBoardCall);
    assert.equal("WHITE", oneViewStompActions.playerColour);
});

QUnit.test("testing StompActions: function userUpdate(ERROR)", function (assert) {
    "use strict";
    var message = {},
        json = '{"squares":{"C8":"B"}}',
        $alertBox;
    message.headers = {};
    message.headers.TYPE = "ERROR";
    message.body = "Test message";
    $alertBox = $("#my-alert .alert");

    //Call with oldChessBoard an empty object
    myStompActions.userUpdate(message);
    assert.equal($alertBox.html(), message.body);

    //Call with json stored in oldChessBoard
    myStompActions.oldChessBoard = json;
    myStompActions.userUpdate(message);
    assert.equal($alertBox.html(), message.body);
    assert.equal(updateChessBoardCall, true);

    //Call with oldChessBoard set to undefined
    updateChessBoardCall = false; //reset check
    myStompActions.oldChessBoard = undefined;
    myStompActions.userUpdate(message);
    assert.equal($alertBox.html(), message.body);
    assert.equal(updateChessBoardCall, false);

});

QUnit.test("testing StompActions: function userUpdate(UPDATE)", function (assert) {
    "use strict";
    var message = {},
        json = '{"squares":{"C8":"B"}}';
    message.headers = {};
    message.headers.TYPE = "UPDATE";
    message.body = json;
    myStompActions.userUpdate(message);
    assert.equal(updateChessBoardCall, true);
});

QUnit.test("testing StompActions: function topicUpdate(ERROR)", function (assert) {
    "use strict";
    var message = {},
        json = '{"squares":{"C8":"B"}}';
    message.headers = {};
    message.headers.TYPE = "ERROR";
    message.body = json;

    myStompActions.topicUpdate(message);
    assert.equal(updateChessBoardCall, true);
});

QUnit.test("testing StompActions: function topicUpdate(STATUS - White Checkmate) ", function (assert) {
    "use strict";
    var message = {},
        $alertBox;
    message.headers = {};
    message.headers.TYPE = "STATUS";
    message.body = myStompActions.GAME_STATUS.WHITE_CHECKMATE;
    $alertBox = $("#my-alert .alert");

    //Call with oldChessBoard an empty object
    myStompActions.topicUpdate(message);
    assert.equal($alertBox.html(), myStompActions.opponentName + " has won the game");

});

QUnit.test("testing StompActions: function topicUpdate(STATUS - Black Checkmate) ", function (assert) {
    "use strict";
    var message = {},
        $alertBox;
    message.headers = {};
    message.headers.TYPE = "STATUS";
    message.body = myStompActions.GAME_STATUS.BLACK_CHECKMATE;
    $alertBox = $("#my-alert .alert");

    //Call with oldChessBoard an empty object
    myStompActions.topicUpdate(message);
    assert.equal($alertBox.html(), myStompActions.playerName + " has won the game");

});

QUnit.test("testing StompActions: function topicUpdate(STATUS - Stalemate) ", function (assert) {
    "use strict";
    var message = {},
        $alertBox;
    message.headers = {};
    message.headers.TYPE = "STATUS";
    message.body = myStompActions.GAME_STATUS.STALEMATE;
    $alertBox = $("#my-alert .alert");

    //Call with oldChessBoard an empty object
    myStompActions.topicUpdate(message);
    assert.equal($alertBox.html(), "Game has ended in a draw");
});

QUnit.test("testing StompActions: function topicUpdate(STATUS - White king in check) ", function (assert) {
    "use strict";
    var message = {},
        $alertBox;
    message.headers = {};
    message.headers.TYPE = "STATUS";
    message.body = myStompActions.GAME_STATUS.WHITE_IN_CHECK;
    $alertBox = $("#my-alert .alert");

    //Call with oldChessBoard an empty object
    myStompActions.topicUpdate(message);
    assert.equal($alertBox.html(), myStompActions.playerName + "'s king is in check");

});

QUnit.test("testing StompActions: function topicUpdate(STATUS - Black king in check) ", function (assert) {
    "use strict";
    var message = {},
        $alertBox;
    message.headers = {};
    message.headers.TYPE = "STATUS";
    message.body = myStompActions.GAME_STATUS.BLACK_IN_CHECK;
    $alertBox = $("#my-alert .alert");

    //Call with oldChessBoard an empty object
    myStompActions.topicUpdate(message);
    assert.equal($alertBox.html(), myStompActions.opponentName + "'s king is in check");

});


QUnit.test("testing StompActions: function topicUpdate(INFO) ", function (assert) {
    "use strict";
    var message = {},
        $alertBox;
    message.headers = {};
    message.headers.TYPE = "INFO";
    message.body = "TestPlayer has quit the game";
    $alertBox = $("#my-alert .alert");

    myStompActions.topicUpdate(message);
    assert.equal($alertBox.html(), message.body);

});

QUnit.test("testing StompActions: function topicUpdate(INFO) ", function (assert) {
    "use strict";
    var message = {};
    message.headers = {};
    message.headers.TYPE = "UPDATE";
    message.body = '{"squares":{"C8":"B"}}';

    myStompActions.topicUpdate(message);
    assert.equal(updateChessBoardCall, true);
});

QUnit.test("testing openStompConnection: fail with error", function (assert) {
    "use strict";
    assert.throws(function () {
        chessgameportalModule.openStompConnection("", function () { chessboardModule.createChessBoard(""); });
    }, "callback function isn't an instance of StompActions");
});