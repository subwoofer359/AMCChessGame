/*global $*/
/*global SockJS*/
/*global Stomp*/

/**
 * A Callback function to encapsulate UI actions to be taken on
 * chess piece promotion
 *
 */
var promotionAction = (function () {
    "use strict";
    function showPromotionDialog() {
        var $dialog = $("#promotionDialog");
        $dialog.removeClass("hidePromotionDialog");
        $dialog.addClass("displayPromotionDialog");
    }

    function hidePromotionDialog() {
        var $dialog = $("#promotionDialog");
        $dialog.removeClass("displayPromotionDialog");
        $dialog.addClass("hidePromotionDialog");
    }

    /**
     * Adds event listeners to buttons in the UI
     * Using the player's colour passes the correct piece symbol to the
     * callback function
     *
     * @param {object} player - represents a player
     * @param {function} stompSendPromotion - callback function
     */
    function handleUserInteract(player, stompSendPromotion) {
        $("#queenBtn").click(function () {
            var piece = player.colour === "WHITE" ? "q" : "Q";
            stompSendPromotion(piece);
            hidePromotionDialog();
        });
        $("#rookBtn").click(function () {
            var piece = player.colour === "WHITE" ? "r" : "R";
            stompSendPromotion(piece);
            hidePromotionDialog();
        });
        $("#knightBtn").click(function () {
            var piece = player.colour === "WHITE" ? "n" : "N";
            stompSendPromotion(piece);
            hidePromotionDialog();
        });
        $("#bishopBtn").click(function () {
            var piece = player.colour === "WHITE" ? "b" : "B";
            stompSendPromotion(piece);
            hidePromotionDialog();
        });
    }

    return {
        showPromotionDialog : showPromotionDialog,
        hidePromotionDialog : hidePromotionDialog,
        handleUserInteract : handleUserInteract
    };
}());

/**
  * @module promotion
  * Contains methods for parsing promotion messages
  * from the Stomp server and setting up connection to
  * to the Stomp server
  */
var promotion = function (stompObject, promotionHandler) {
    "use strict";
    var messageRegex = /PAWN_PROMOTION\s\(([A-Ha-h]),([1-8])\)/,
        gameUUID = stompObject.gameUUID,
        gameState,
        USER_UPDATES = "/user/queue/updates",
        TOPIC_UPDATES = "/topic/updates/",
        APP_GET = "/app/get/",
        PRIORITY = {priority : 9},
        APP_PROMOTE = "/app/promote/",
        PROMOTE = "promote ",
        player = {
            colour : stompObject.playerColour
        },
        squareOfPawn;

    function findPawnForPromotion(colour, chessBoardObj) {
        var WHITE_RANK = 8,
            BLACK_RANK = 1,
            rank = colour === "WHITE" ? WHITE_RANK : BLACK_RANK,
            pawn = colour === "WHITE" ? 'p' : 'P',
            s,
            rankExp;
        for (s in chessBoardObj.squares) {
            if (chessBoardObj.squares[s] === pawn) {
                rankExp = new RegExp(rank + "$");
                if (rankExp.test(s)) {
                    return s.toLowerCase();
                }
            }
        }
    }

    function parsePromotionMessage(message) {
        if (messageRegex.test(message)) {
            var find = messageRegex.exec(message),
                letter = find[1].toLowerCase(),
                number = find[2];
            return letter + number;
        }
        throw "Message can't be parsed";
    }

    function doPromotionHandlerAction() {
        promotionHandler.showPromotionDialog();
    }

    function checkBoardInPromotionState() {
        if (gameState === "PAWN_PROMOTION") {
            doPromotionHandlerAction();
        }
    }

    function updateMessageHandler(message) {
        var board = $.parseJSON(message.body);
        player.colour = board.currentPlayer.colour;
        gameState = board.gameState;
    }

    function handleUserMessage(message) {
        if (gameState === "PAWN_PROMOTION") {
            switch (message.headers.TYPE) {
            case "STATUS":
                squareOfPawn = parsePromotionMessage(message.body);
                doPromotionHandlerAction();
                return squareOfPawn;
            case "ERROR":
                doPromotionHandlerAction();
                break;
            }
        } else {
            switch (message.headers.TYPE) {
            case "UPDATE":
                updateMessageHandler(message);
                checkBoardInPromotionState();
                break;
            }
        }
    }

    function handleTopicMessage(message) {
        switch (message.headers.TYPE) {
        case "UPDATE":
            updateMessageHandler(message);
            break;
        }
        if (gameState === "PAWN_PROMOTION") {
            switch (message.headers.TYPE) {
            case "STATUS":
                squareOfPawn = parsePromotionMessage(message.body);
                doPromotionHandlerAction();
                return squareOfPawn;
            case "ERROR":
                doPromotionHandlerAction();
                break;
            }
        }
    }

    function twoViewHandleTopicMessage(message) {
        switch (message.headers.TYPE) {
        case "UPDATE":
            updateMessageHandler(message);
            break;
        }
        if (gameState === "PAWN_PROMOTION") {
            switch (message.headers.TYPE) {
            case "STATUS":
                squareOfPawn = parsePromotionMessage(message.body);
                doPromotionHandlerAction();
                return squareOfPawn;
            case "ERROR":
                doPromotionHandlerAction();
                break;
            }
        }
    }

    function OneViewStompConnection() {
        if (this === undefined) {
            return new OneViewStompConnection();
        }
        this.socket = null;
        this.stompClient = null;
    }

    OneViewStompConnection.prototype = {
        setUpStompConnection : function (uiHandler) {
            var that = this;
            this.stompClient.connect(stompObject.headers, function () {
                that.stompClient.subscribe(USER_UPDATES, handleUserMessage);
                that.stompClient.subscribe(TOPIC_UPDATES + gameUUID, that.getHandleTopicMessage());
                that.stompClient.send(APP_GET + gameUUID, PRIORITY, "Get ChessBoard");
                uiHandler(player, function (piece) {
                    that.stompClient.send(APP_PROMOTE + gameUUID, PRIORITY, PROMOTE + piece + squareOfPawn);
                });
            });
        },
        connect : function () {
            this.socket = new SockJS(stompObject.URL);
            this.stompClient = Stomp.over(this.socket);
            this.setUpStompConnection();
        },
        getHandleTopicMessage : function () {
            return handleTopicMessage;
        }
    };

    function TwoViewStompConnection() {
        if (this === undefined) {
            return new TwoViewStompConnection();
        }
        OneViewStompConnection.call(this);
    }

    TwoViewStompConnection.prototype = Object.create(OneViewStompConnection.prototype);

    TwoViewStompConnection.prototype.getHandleTopicMessage = function () {
        return twoViewHandleTopicMessage;
    };

    function setGameState(state) {
        gameState = state;
    }

    return {
        parsePromotionMessage : parsePromotionMessage,
        findPawnForPromotion : findPawnForPromotion,
        checkBoardInPromotionState : checkBoardInPromotionState,
        OneViewStompConnection : OneViewStompConnection,
        TwoViewStompConnection : TwoViewStompConnection,
        setGameState : setGameState
    };
};