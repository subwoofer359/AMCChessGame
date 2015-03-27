package org.amc.game;

import org.amc.game.chess.ChessGame;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Player;
import org.amc.game.chess.controller.Controller;
import org.amc.game.chess.view.ChessBoardView;

/**
 * Represents a game of Chess
 * 
 * @author Adrian Mclaughlin
 *
 */
public class ChessApplication {
    private ChessGame chessGame;
    private ChessBoardView view;
    private Controller controller;
    private Player playerOne;
    private Player playerTwo; 

    public ChessApplication(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public final void setChessGame(ChessGame chessGame) {
        this.chessGame = chessGame;
    }

    public final void setView(ChessBoardView view) {
        this.view = view;
    }

    public final void setController(Controller controller) {
        this.controller = controller;
    }
    
    /**
     * Start the Game
     */
    public void start() {
        view.displayTheBoard();
        START: while (!chessGame.isGameOver()) {
            try {
                controller.takeTurn();
            } catch (IllegalMoveException ime) {
                System.out.println(ime.getMessage());
                view.displayTheBoard();
                continue START;
            }

            chessGame.changePlayer();
        }
        switch (chessGame.getGameState()) {
        case STALEMATE:
            System.out.println("Game has ended in a stalemate");
            break;
        case BLACK_CHECKMATE:
            System.out.println(playerOne.getName() + " has won!");
            break;
        case WHITE_CHECKMATE:
            System.out.println(playerTwo.getName() + " has won!");
            break;
        default:
            System.out.println("Game has ended abnormally!!");
        }
    }

}
