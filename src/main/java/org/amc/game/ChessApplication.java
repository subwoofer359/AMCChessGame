package org.amc.game;

import org.amc.game.chess.ChessGame;
import org.amc.game.chess.InvalidMoveException;
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
        START: while (!chessGame.isGameOver(playerOne, playerTwo)) {
            try {
                controller.takeTurn();
            } catch (InvalidMoveException ime) {
                System.out.println(ime.getMessage());
                view.displayTheBoard();
                continue START;
            }

            chessGame.changePlayer();
        }
        if (playerOne.isWinner()) {
            System.out.println("Player " + playerOne.getName() + " has won");
        } else {
            System.out.println("Player " + playerTwo.getName() + " has won");
        }
    }

}
