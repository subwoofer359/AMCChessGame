/*global $*/
/*global WebSocket*/
/*global createChessBoard*/
/*global updatePlayer*/
/*global console*/
/*global Stomp*/

function openStompConnection(websocketURL, gameUID, playerName, opponentName, playerColour) {
    "use strict";
    var stompClient,
        socket;

    socket = new WebSocket(websocketURL);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        var oldChessBoard = {},
            alertBox = $("#my-alert"),
            alertBoxText = $("#my-alert .alert"),
            GAME_STATUS = {
                WHITE_CHECKMATE : "WHITE_CHECKMATE",
                BLACK_CHECKMATE : "BLACK_CHECKMATE",
                STALEMATE : "STALEMATE",
                WHITE_IN_CHECK : "WHITE_IN_CHECK",
                BLACK_IN_CHECK : "BLACK_IN_CHECK"
            };

        function showFadingAlertMessage(message) {
            alertBoxText.html(message);
            alertBox.css("display", "block");
            alertBox.css("opacity", "1");
            alertBox.fadeOut(5000);
        }

        function showAlertMessage(message) {
            alertBoxText.html(message);
            alertBox.css("display", "block");
            alertBox.css("opacity", "1");
        }

        $("#quit-btn").click(function () {
            /*stompClient.send("/app/quit/${GAME_UUID}", {priority: 9},"quit");*/
            stompClient.send("/app/quit/" + gameUID, {priority: 9}, "quit");
        });

        function updateChessBoard(playerColour, chessBoardJson) {
            createChessBoard(playerColour, chessBoardJson);
            oldChessBoard = chessBoardJson;
            updatePlayer(chessBoardJson);
        }

        stompClient.subscribe("/user/queue/updates", function (message) {
            if (message.headers.TYPE === "ERROR") {
                showFadingAlertMessage(message.body);
                if (oldChessBoard !== undefined || !$.isEmptyObject(oldChessBoard)) {
                    createChessBoard(playerColour, oldChessBoard);
                }
            } else if (message.headers.TYPE === "UPDATE") {
                updateChessBoard(playerColour, message.body);
            } else if (message.headers.TYPE === "INFO") {
                console.log(message.body);
            }
        });

        stompClient.subscribe("/topic/updates/" + gameUID, function (message) {
            if (message.headers.TYPE === "ERROR") {
                updateChessBoard(message.body);
            } else if (message.headers.TYPE === "STATUS") {
                switch (message.body) {
                case GAME_STATUS.WHITE_CHECKMATE:
                    showAlertMessage(opponentName + " has won the game");
                    break;
                case GAME_STATUS.BLACK_CHECKMATE:
                    showAlertMessage(playerName + " has won the game");
                    break;
                case GAME_STATUS.STALEMATE:
                    showAlertMessage("Game has ended in a draw");
                    break;
                case GAME_STATUS.WHITE_IN_CHECK:
                    showFadingAlertMessage(playerName + "'s king is in check");
                    break;
                case GAME_STATUS.BLACK_IN_CHECK:
                    showFadingAlertMessage(opponentName + "'s king is in check");
                    break;
                default:
                    break;
                }
            } else if (message.headers.TYPE === "INFO") {
                if (/has quit the game$/.test(message.body)) {
                    showAlertMessage(message.body);
                } else {
                    console.log("INFO:" + message.body);
                }
            } else if (message.headers.TYPE === "UPDATE") {
                updateChessBoard(playerColour, message.body);
            }
        });

        stompClient.send("/app/get/" + gameUID, {priority: 9}, "Get ChessBoard");
    });
    return stompClient;
}