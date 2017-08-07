/*global QUnit*/
/*global $*/
/*global Chessboard*/
/*global chessgameportalModule*/

import * as chessGamePortal from "../chessGamePortal";
import { OneViewStompActions } from "../OneViewStompActions";
import { Colour } from "../Pieces/ChessPiece";
import { StompActions } from "../StompActions";
import { StompObject } from "../StompObject";
import * as Chessboard from "./MockChessBoard";
import * as UpdatePlayer from "./MockPlayer";

let stompObj: StompObject;

let myStompActions;

const playerObj = `"currentPlayer":{"colour":"WHITE","player":{"id":6,"name":"Caleb Doyle","userName":"Caleb"}}`;

const chessboardString = `{"squares":{"G1":"n","G2":"p","E1":"k","E2":"p","C1":"b","C2":"p","A1":"r","G7":"P","A2":"p",
    "G8":"N","E7":"P","E8":"K","C7":"P","C8":"B","A7":"P","A8":"R","H1":"r","H2":"p","F1":"b","F2":"p","D1":"q",
    "D2":"p","B1":"n","H7":"P","B2":"p","H8":"R","F7":"P","F8":"B","D7":"P","D8":"Q","B7":"P","B8":"N"},${playerObj}}`;

QUnit.module("Stomp Message tests", {
    beforeEach : () => {
        $("#qunit-fixture").html('<div id="my-alert"><div class="alert"></div></div>');
        stompObj = new StompObject();
        stompObj.gameUUID = "1234";
        stompObj.playerName = "testPlayer";
        stompObj.opponentName = "testOpponent";
        stompObj.playerColour = Colour.black.toString;

        myStompActions = new StompActions(stompObj);
        myStompActions.chessboard = Chessboard;
        myStompActions.updatePlayer = UpdatePlayer.updatePlayer;
    },
});

QUnit.test("testing StompActions: function updateChessBoard", (assert) => {
    const json = chessboardString;

    myStompActions.updateChessBoard(json);
    assert.ok(UpdatePlayer.updatePlayerCall, "UpdatePlayer should be called");
    assert.ok(Chessboard.updateChessBoardCall, "UpdateChessBoard should be called");
});

QUnit.test("testing StompActions: function updateChessBoard", (assert) => {
    const json = chessboardString;
    const oneViewStompActions: any = new OneViewStompActions(stompObj);
    oneViewStompActions.updateChessBoard(json);
    assert.ok(UpdatePlayer.updatePlayerCall, "UpdatePlayer should be called");
    assert.ok(Chessboard.updateChessBoardCall, "UpdateChessBoard should be called");
    assert.equal("WHITE", oneViewStompActions.playerColour, "Player's colour should be white");
});

QUnit.test("testing StompActions: function userUpdate(ERROR)", (assert) => {
    const message: any = {};
    const json = `{"squares":{"C8":"B"}, ${playerObj}}`;
    let $alertBox;
    message.headers = {};
    message.headers.TYPE = "ERROR";
    message.body = "Test message";
    $alertBox = $("#my-alert .alert");

    // Call with oldChessBoard an empty object
    myStompActions.userUpdate(message);
    assert.equal($alertBox.html(), message.body);

    // Call with json stored in oldChessBoard
    myStompActions.oldChessBoard = json;
    myStompActions.userUpdate(message);
    assert.equal($alertBox.html(), message.body);
    assert.equal(Chessboard.updateChessBoardCall, true);

    // Call with oldChessBoard set to undefined
    Chessboard.setUpdateChessBoardCall(false); // reset check
    myStompActions.oldChessBoard = undefined;
    myStompActions.userUpdate(message);
    assert.equal($alertBox.html(), message.body);
    assert.equal(Chessboard.updateChessBoardCall, false);

});

QUnit.test("testing StompActions: function userUpdate(UPDATE)", (assert) => {
    const message: any = {};
    const json = `{"squares":{"C8":"B"}, ${playerObj}}`;

    message.headers = {};
    message.headers.TYPE = "UPDATE";
    message.body = json;

    myStompActions.userUpdate(message);
    assert.equal(Chessboard.updateChessBoardCall, true);
});

QUnit.test("testing StompActions: function topicUpdate(ERROR)", (assert) => {
    const message: any = {};
    const json = `{"squares":{"C8":"B"}, ${playerObj}}`;

    message.headers = {};
    message.headers.TYPE = "ERROR";
    message.body = json;

    myStompActions.topicUpdate(message);
    assert.equal(Chessboard.updateChessBoardCall, true);
});

QUnit.test("testing StompActions: function topicUpdate(STATUS - White Checkmate) ", (assert) => {
    const message: any = {};
    let $alertBox;
    message.headers = {};
    message.headers.TYPE = "STATUS";
    message.body = myStompActions.GAME_STATUS.WHITE_CHECKMATE;
    $alertBox = $("#my-alert .alert");

    // Call with oldChessBoard an empty object
    myStompActions.topicUpdate(message);
    assert.equal($alertBox.html(), myStompActions.opponentName + " has won the game");

});

QUnit.test("testing StompActions: function topicUpdate(STATUS - Black Checkmate) ", (assert) => {
    const message: any = {};
    let $alertBox;

    message.headers = {};
    message.headers.TYPE = "STATUS";
    message.body = myStompActions.GAME_STATUS.BLACK_CHECKMATE;
    $alertBox = $("#my-alert .alert");

    // Call with oldChessBoard an empty object
    myStompActions.topicUpdate(message);
    assert.equal($alertBox.html(), myStompActions.playerName + " has won the game");

});

QUnit.test("testing StompActions: function topicUpdate(STATUS - Stalemate) ", (assert) => {
    const message: any = {};
    let $alertBox;

    message.headers = {};
    message.headers.TYPE = "STATUS";
    message.body = myStompActions.GAME_STATUS.STALEMATE;
    $alertBox = $("#my-alert .alert");

    // Call with oldChessBoard an empty object
    myStompActions.topicUpdate(message);
    assert.equal($alertBox.html(), "Game has ended in a draw");
});

QUnit.test("testing StompActions: function topicUpdate(STATUS - White king in check) ", (assert) => {
    const message: any = {};
    let $alertBox;

    message.headers = {};
    message.headers.TYPE = "STATUS";
    message.body = myStompActions.GAME_STATUS.WHITE_IN_CHECK;
    $alertBox = $("#my-alert .alert");

    // Call with oldChessBoard an empty object
    myStompActions.topicUpdate(message);
    assert.equal($alertBox.html(), myStompActions.playerName + "'s king is in check");
});

QUnit.test("testing StompActions: function topicUpdate(STATUS - Black king in check) ", (assert) => {
    const message: any = {};
    let $alertBox;

    message.headers = {};
    message.headers.TYPE = "STATUS";
    message.body = myStompActions.GAME_STATUS.BLACK_IN_CHECK;
    $alertBox = $("#my-alert .alert");

    // Call with oldChessBoard an empty object
    myStompActions.topicUpdate(message);
    assert.equal($alertBox.html(), myStompActions.opponentName + "'s king is in check");
});

QUnit.test("testing StompActions: function topicUpdate(INFO) ", (assert) => {
    const message: any = {};
    let $alertBox;

    message.headers = {};
    message.headers.TYPE = "INFO";
    message.body = "TestPlayer has quit the game";
    $alertBox = $("#my-alert .alert");

    myStompActions.topicUpdate(message);
    assert.equal($alertBox.html(), message.body);
});

QUnit.test("testing StompActions: function topicUpdate(INFO) ", (assert) => {
    const message: any = {};

    message.headers = {};
    message.headers.TYPE = "UPDATE";
    message.body = `{"squares":{"C8":"B"},
                    "currentPlayer":
                        {"player":{"id":24,"name":"Nicole O\u0027Brien","userName":"nicole"},
                        "id":115,"colour":"WHITE","version":1}}`;

    myStompActions.topicUpdate(message);

    assert.equal(Chessboard.updateChessBoardCall, true);
});

