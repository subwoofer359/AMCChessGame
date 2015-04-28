/*global QUnit*/
/*global $*/
/*global ChessPieces*/
/*global StompActions*/
/*global createChessBoard:true*/
var gameUID,
    playerName,
    opponentName,
    playerColour,
    myStompActions,
    updatePlayerCall,
    updateChessBoardCall,
    oldCreateChessBoard;

/* mock function for player.js:updarePlayer */
function updatePlayer() {
    "use strict";
    updatePlayerCall = true;
}

/* mock function for chessboard.js:createChessBoard */
(function () {
    "use strict";
    oldCreateChessBoard = createChessBoard;
    createChessBoard = function (playerColour, json) {
        updateChessBoardCall = true;
        oldCreateChessBoard(playerColour, json);
    };
}());

QUnit.module("Stomp Message tests", {
    beforeEach : function () {
        "use strict";
        $("#qunit-fixture").html('<div id="my-alert"><div class="alert"></div></div>');
        gameUID = "1234";
        playerName = "testPlayer";
        opponentName = "testOpponent";
        playerColour = ChessPieces.prototype.colour.black;
        myStompActions = new StompActions(gameUID, playerName, opponentName, playerColour);
        updatePlayerCall = false;
        updateChessBoardCall = false;
    }
});

QUnit.test("testing StompActions: function updateChessBoard", function (assert) {
    "use strict";
    var json = '{"squares":{"C8":"B"}}';

    myStompActions.updateChessBoard(json);
    assert.equal(true, updatePlayerCall);
    assert.equal(true, updateChessBoardCall);
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