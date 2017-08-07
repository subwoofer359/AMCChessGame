
import * as Chessboard from "./Chessboard";
import "./player";
import { StompObject } from "./StompObject";

/**
 * Creates a new StompActions which is used in a Two View Chess Game
 * @class
 * @param {number} gameUID identifying number of the game
 * @param {string} playerName name of the white player
 * @param {string} opponentName name of the black player
 * @param {string} playerColour colour of the player playing the game
 */
export class StompActions {

    public oldChessBoard: any = {};

    public alertBox = $("#my-alert");

    public alertBoxText = $("#my-alert .alert");

    public gameUID;

    public playerName: string;

    public opponentName: string;

    public playerColour;

    public readonly GAME_STATUS = {
        BLACK_CHECKMATE : "BLACK_CHECKMATE",
        BLACK_IN_CHECK : "BLACK_IN_CHECK",
        STALEMATE : "STALEMATE",
        WHITE_CHECKMATE : "WHITE_CHECKMATE",
        WHITE_IN_CHECK : "WHITE_IN_CHECK",
    };

    /*
     * Injection is possible
     */
    private chessboard = Chessboard;

    /*
     * Injection is possible
     */
    private updatePlayer: (message) => void = updatePlayer;

    constructor(stompObject: StompObject) {
        this.gameUID = stompObject.gameUUID;
        this.playerName = stompObject.playerName;
        this.opponentName = stompObject.opponentName;
        this.playerColour = stompObject.playerColour;
    }

    /**
     * Displays message in an alert box which will fade
     * @param {string} message to be displayed
     */
    public showFadingAlertMessage(message: string): void {
        this.alertBoxText.html(message);
        this.alertBox.css("display", "block");
        this.alertBox.css("opacity", "1");
    }

    /**
     * Displays message in an alert box
     * @param {string} message to be displayed
     */
    public showAlertMessage(message: string): void {
        this.alertBoxText.html(message);
        this.alertBox.css("display", "block");
        this.alertBox.css("opacity", "1");
    }

    /**
     * Creates a new chessbooard to be displayer to player
     * Saves the JSON string in case the board has to be recreated.
     * @param {string} JSON to be parsed into a chessboard object
     */
    public updateChessBoard(chessBoardJson): void {
        const board = $.parseJSON(chessBoardJson);
        let move = "";

        if (board.lastMove) {
            move = board.lastMove;
        }

        this.chessboard.move(move, () => {
            this.chessboard.createChessBoard(this.playerColour, chessBoardJson);
            this.oldChessBoard = chessBoardJson;
            this.updatePlayer(chessBoardJson);
        });
    }

    /**
     * Update the Player if a user specific message is received from the Server
     * If an Error message is received then the chessboard is
     * reset to the last configuration
     * @param {string} message to be displayed
     */
    public userUpdate(message: any): void {
        if (message.headers.TYPE === "ERROR") {
            this.showFadingAlertMessage(message.body);
            if (this.oldChessBoard !== undefined && !$.isEmptyObject(this.oldChessBoard)) {
                this.chessboard.createChessBoard(this.playerColour, this.oldChessBoard);
            }
        } else if (message.headers.TYPE === "UPDATE") {
            this.updateChessBoard(message.body);
        } else if (message.headers.TYPE === "INFO") {
            if (/[A-Za-z]+/.test(message.body)) {
                this.showFadingAlertMessage(message.body);
            }
        }
    }

    /**
     * Update the Player if a general message is received from the Server
     * If an Error message is received then the chessboard is
     * reset to the last configuration
     * @param {string} message to be displayed
     */
    public topicUpdate(message: any): void {
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
}