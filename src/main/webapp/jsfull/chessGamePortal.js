/*global $*/

/*global WebSocket*/
/*global updatePlayer*/
/*global chessboard_module*/
/*global Stomp*/
/*global SockJS*/
/*global headers*/


var chessgameportal_module = (function () {
    "use strict";
    /* makes Message disappear when chessboard is clicked*/
    function addMessageDialogListener() {
        $("#chessboard-surround").click(function () {
            $("#my-alert").css("opacity", "0");
        });
    }

    /**
     * Creates a new StompActions which is used in a Two View Chess Game
     * @class
     * @param {number} gameUID identifying number of the game
     * @param {string} playerName name of the white player
     * @param {string} opponentName name of the black player
     * @param {string} playerColour colour of the player playing the game
     */
    function StompActions(gameUID, playerName, opponentName, playerColour) {
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
        /**
         * Displays message in an alert box which will fade
         * @param {string} message to be displayed
         */
        showFadingAlertMessage : function (message) {
            this.alertBoxText.html(message);
            this.alertBox.css("display", "block");
            this.alertBox.css("opacity", "1");
        },

        /**
         * Displays message in an alert box
         * @param {string} message to be displayed
         */
        showAlertMessage : function (message) {
            this.alertBoxText.html(message);
            this.alertBox.css("display", "block");
            this.alertBox.css("opacity", "1");
        },

        /**
         * Creates a new chessbooard to be displayer to player
         * Saves the JSON string in case the board has to be recreated.
         * @param {string} JSON to be parsed into a chessboard object
         */
        updateChessBoard : function (chessBoardJson) {
            chessboard_module.createChessBoard(this.playerColour, chessBoardJson);
            this.oldChessBoard = chessBoardJson;
            updatePlayer(chessBoardJson);
        },

        /**
         * Update the Player if a user specific message is received from the Server
         * If an Error message is received then the chessboard is 
         * reset to the last configuration
         * @param {string} message to be displayed
         */
        userUpdate : function (message) {
            if (message.headers.TYPE === "ERROR") {
                this.showFadingAlertMessage(message.body);
                if (this.oldChessBoard !== undefined && !$.isEmptyObject(this.oldChessBoard)) {
                    chessboard_module.createChessBoard(this.playerColour, this.oldChessBoard);
                }
            } else if (message.headers.TYPE === "UPDATE") {
                this.updateChessBoard(message.body);
            } else if (message.headers.TYPE === "INFO") {
                if (/[A-Za-z]+/.test(message.body)) {
                    this.showFadingAlertMessage(message.body);
                }
            }
        },

        /**
         * Update the Player if a general message is received from the Server
         * If an Error message is received then the chessboard is 
         * reset to the last configuration
         * @param {string} message to be displayed
         */
        topicUpdate : function (message) {
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
                }
            } else if (message.headers.TYPE === "UPDATE") {
                this.updateChessBoard(message.body);
            }
        }
    };

    /**
     * Creates a new StompActions which is used in an One View Chess Game
     * @class
     * @constructor
     * @augments StompActions
     * @param {number} gameUID identifying number of the game
     * @param {string} playerName name of the white player
     * @param {string} opponentName name of the black player
     * @param {string} playerColour colour of the player playing the game
     */
    function OneViewStompActions(gameUID, playerName, opponentName, playerColour) {
        StompActions.call(this, gameUID, playerName, opponentName, playerColour);
    }

    OneViewStompActions.prototype = Object.create(StompActions.prototype);
    OneViewStompActions.constructor = StompActions;

    /**
     * @override
     */
    OneViewStompActions.prototype.updateChessBoard = function (chessBoardJson) {
        var board = $.parseJSON(chessBoardJson);
        this.playerColour = board.currentPlayer.colour;
        StompActions.prototype.updateChessBoard.call(this, chessBoardJson);
    };


    /**
     * Opens a Websocket connection or display a message if the connection fails
     * @param {url} websocketURL url to open a websocket connection
     * @param {object} headers contains values to place into the STOMP headers
     * @param {StompActions} stompCallBack callback object
     */
    function openStompConnection(websocketURL, headers, stompCallBack) {
        var stompClient,
            socket,
            USER_UPDATES = "/user/queue/updates",
            TOPIC_UPDATES = "/topic/updates/",
            APP_GET = "/app/get/",
            PRIORITY = {priority : 9},
            APP_QUIT = "/app/quit/",
            APP_SAVE = "/app/save/";

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

            $(".save-btn").click(function () {
                stompClient.send(APP_SAVE + stompCallBack.gameUID, PRIORITY, "save");
            });
            stompClient.send(APP_GET + stompCallBack.gameUID, PRIORITY, "Get ChessBoard");
        }, function onStompError() {
            stompCallBack.showAlertMessage.call(stompCallBack, "Couldn't connect to the STOMP server");
        });
        return stompClient;
    }

    /**
     * Sets up the required objects before calling for websocket connection
     * Sets up the connection for two view chess game
     * @param {object} stompObject contains values required to open a connection
     * @returns stompClient STOMP connection object
     */
    function setupStompConnection(stompObject) {
        var stompCallBack = new StompActions(stompObject.gameUUID, stompObject.playerName, stompObject.opponentName, stompObject.playerColour),
            stompClient = openStompConnection(stompObject.URL, stompObject.headers, stompCallBack);
        return stompClient;
    }


    /**
     * Sets up the required objects before calling for websocket connection
     * Sets up the connection for one view chess game
     * @param {object} stompObject contains values required to open a connection
     * @returns stompClient STOMP connection object
     */
    function setupOneViewStompConnection(stompObject) {
        var stompCallBack = new OneViewStompActions(stompObject.gameUUID, stompObject.playerName, stompObject.opponentName, stompObject.playerColour),
            stompClient = openStompConnection(stompObject.URL, stompObject.headers, stompCallBack);

        return stompClient;
    }

    return {
        openStompConnection : openStompConnection,
        StompActions : StompActions,
        OneViewStompActions : OneViewStompActions,
        addMessageDialogListener : addMessageDialogListener,
        setupStompConnection : setupStompConnection,
        setupOneViewStompConnection : setupOneViewStompConnection
    };
}());