/*global $*/

/*global WebSocket*/
/*global createChessBoard*/
/*global updatePlayer*/
/*global console*/
/*global Stomp*/
/*global SockJS*/
/*global headers*/

/* makes Message disappear when chessboard is clicked*/
function addMessageDialogListener() {
    "use strict";
    $("#chessboard-surround").click(function () {
        $("#my-alert").css("opacity", "0");
    });
}

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

function openStompConnection(websocketURL, headers, stompCallBack) {
    "use strict";
    var stompClient,
        socket,
        USER_UPDATES = "/user/queue/updates",
        TOPIC_UPDATES = "/topic/updates/",
        APP_GET = "/app/get/",
        PRIORITY = {priority : 9},
        APP_QUIT = "/app/quit/";

    if (!(typeof stompCallBack === 'object' && stompCallBack instanceof StompActions)) {
        throw "callback function isn't an instance of StompActions";
    }
    socket = new SockJS(websocketURL);
    stompClient = Stomp.over(socket);
    stompClient.connect(headers, function onStompConnect() {
        stompClient.subscribe(USER_UPDATES, function (message) {
            stompCallBack.userUpdate.call(stompCallBack, message);
        });

        stompClient.subscribe(TOPIC_UPDATES + stompCallBack.gameUID, function (message) {
            stompCallBack.topicUpdate.call(stompCallBack, message);
        });

        $(".quit-btn").click(function () {
            stompClient.send(APP_QUIT + stompCallBack.gameUID, PRIORITY, "quit");
        });
        stompClient.send(APP_GET + stompCallBack.gameUID, PRIORITY, "Get ChessBoard");
    }, function onStompError() {
        stompCallBack.showAlertMessage.call(stompCallBack, "Couldn't connect to the STOMP server");
        console.log("Stomp connection failure");
    });
    return stompClient;
}

function setupStompConnection(stompObject) {
    "use strict";
    var stompCallBack = new StompActions(stompObject.gameUUID, stompObject.playerName, stompObject.opponentName, stompObject.playerColour),
        stompClient = openStompConnection(stompObject.URL, stompObject.headers, stompCallBack);
    return stompClient;
}