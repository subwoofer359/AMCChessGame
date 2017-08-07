import * as Chessboard from "./Chessboard";
import { StompActions } from "./StompActions";
import { StompObject } from "./StompObject";

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
export class OneViewStompActions extends StompActions {
    constructor(stompObject: StompObject) {
        super(stompObject);
    }

    /**
     * @override
     */
    public updateChessBoard(chessBoardJson): void {
        const board = $.parseJSON(chessBoardJson);
        this.playerColour = board.currentPlayer.colour;
        Chessboard.createChessBoard(this.playerColour, chessBoardJson);
        this.oldChessBoard = chessBoardJson;
        updatePlayer(chessBoardJson);
    }
}
