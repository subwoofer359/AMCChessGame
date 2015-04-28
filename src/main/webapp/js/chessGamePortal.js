/*global $*/
/*global WebSocket*/
/*global createChessBoard*/
/*global updatePlayer*/
/*global console*/
/*global Stomp*/

function StompActions(gameUID, playerName, opponentName, playerColour) {
    "use strict";
    this.oldChessBoard = {};
    this.alertBox = $("#my-alert");
    this.alertBoxText = $("#my-alert .alert");
    this.GAME_STATUS = {
        WHITE_CHECKMATE : "WHITE_CHECKMATE",
        BLACK_CHECKMATE : "BLACK_CHECKMATE",
        STALEMATE : "STALEMATE",
        WHITE_IN_CHECK : "WHITE_IN_CHECK",
        BLACK_IN_CHECK : "BLACK_IN_CHECK"
    };
    this.gameUID = gameUID;
    this.playerName = playerName;
    this.opponentName = opponentName;
    this.playerColour = playerColour;
}

StompActions.prototype = {
    showFadingAlertMessage : function (message) {
        "use strict";
        this.alertBoxText.html(message);
        this.alertBox.css("display", "block");
        this.alertBox.css("opacity", "1");
        this.alertBox.fadeOut(5000);
    },

    showAlertMessage : function (message) {
        "use strict";
        this.alertBoxText.html(message);
        this.alertBox.css("display", "block");
        this.alertBox.css("opacity", "1");
    },

    updateChessBoard : function (chessBoardJson) {
        "use strict";
        createChessBoard(this.playerColour, chessBoardJson);
        this.oldChessBoard = chessBoardJson;
        updatePlayer(chessBoardJson);
    },

    userUpdate : function (message) {
        "use strict";
        if (message.headers.TYPE === "ERROR") {
            this.showFadingAlertMessage(message.body);
            if (this.oldChessBoard !== undefined && !$.isEmptyObject(this.oldChessBoard)) {
                createChessBoard(this.playerColour, this.oldChessBoard);
            }
        } else if (message.headers.TYPE === "UPDATE") {
            this.updateChessBoard(message.body);
        } else if (message.headers.TYPE === "INFO") {
            console.log(message.body);
        }
    },
    topicUpdate : function (message) {
        "use strict";
        if (message.headers.TYPE === "ERROR") {
            this.updateChessBoard(message.body);
        } else if (message.headers.TYPE === "STATUS") {
            switch (message.body) {
            case this.GAME_STATUS.WHITE_CHECKMATE:
                this.showAlertMessage(this.opponentName + " has won the game");
                break;
            case this.GAME_STATUS.BLACK_CHECKMATE:
                this.showAlertMessage(this.playerName + " has won the game");
                break;
            case this.GAME_STATUS.STALEMATE:
                this.showAlertMessage("Game has ended in a draw");
                break;
            case this.GAME_STATUS.WHITE_IN_CHECK:
                this.showFadingAlertMessage(this.playerName + "'s king is in check");
                break;
            case this.GAME_STATUS.BLACK_IN_CHECK:
                this.showFadingAlertMessage(this.opponentName + "'s king is in check");
                break;
            default:
                break;
            }
        } else if (message.headers.TYPE === "INFO") {
            if (/has quit the game$/.test(message.body)) {
                this.showAlertMessage(message.body);
            } else {
                console.log("INFO:" + message.body);
            }
        } else if (message.headers.TYPE === "UPDATE") {
            this.updateChessBoard(message.body);
        }
    }
};

function openStompConnection(websocketURL, stompCallBack) {
    "use strict";
    var stompClient,
        socket,
        PRIORITY = {priority : 9},
        APP_QUIT = "/app/quit/",
        APP_GET = "/app/get/",
        USER_UPDATES = "/user/queue/updates",
        TOPIC_UPDATES = "/topic/updates/";

    if (!(typeof stompCallBack === 'function' && stompCallBack instanceof StompActions)) {
        throw "callback function isn't an instance of StompActions";
    }
    socket = new WebSocket(websocketURL);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        $("#quit-btn").click(function () {
            stompClient.send(APP_QUIT + stompCallBack.gameUID, PRIORITY, "quit");
        });

        stompClient.subscribe(USER_UPDATES, function (message) {
            stompCallBack.userUpdate.call(stompCallBack, message);
        });

        stompClient.subscribe(TOPIC_UPDATES + stompCallBack.gameUID, function (message) {
            stompCallBack.topicUpdate.call(stompCallBack, message);
        });

        stompClient.send(APP_GET + stompCallBack.gameUID, {priority: 9}, "Get ChessBoard");
    });
    return stompClient;
}