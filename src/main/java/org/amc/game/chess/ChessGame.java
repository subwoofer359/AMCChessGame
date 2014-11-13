package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.util.Observer;
import org.amc.util.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a game of Chess
 * 
 * @author Adrian Mclaughlin
 *
 */
public class ChessGame implements Observer {
    private ChessBoard board;
    private ChessBoardView view;
    private Controller controller;
    private Player playerOne;
    private Player playerTwo;
    private List<Move> allGameMoves;
    private static final Move EMPTY_MOVE=new EmptyMove(); 

    public ChessGame(){
        allGameMoves=new ArrayList<>();
    }
    
    public ChessGame(Player playerOne, Player playerTwo) {
        this();
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public final void setBoard(ChessBoard board) {
        this.board = board;
        this.board.attachObserver(this);
    }

    public final void setView(ChessBoardView view) {
        this.view = view;
    }

    public final void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Checks to see if the game has reached it's completion
     * 
     * @param playerOne
     * @param playerTwo
     * @return Boolean
     */
    boolean isGameOver(Player playerOne, Player playerTwo) {
        boolean playerOneHaveTheirKing = doesThePlayerStillHaveTheirKing(playerOne);
        boolean playerTwoHaveTheirKing = doesThePlayerStillHaveTheirKing(playerTwo);
        if (!playerOneHaveTheirKing) {
            playerTwo.isWinner(true);
            return true;
        } else if (!playerTwoHaveTheirKing) {
            playerOne.isWinner(true);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks to see if the Player still possesses their King
     * 
     * @param player
     * @return false if they lost their King ChessPiece
     */
    boolean doesThePlayerStillHaveTheirKing(Player player) {
        List<ChessPiece> allPlayersChessPieces = getAllPlayersChessPiecesOnTheBoard(player);
        for (ChessPiece piece : allPlayersChessPieces) {
            if (piece.getClass().equals(KingPiece.class)) {
                return true;
            }
        }
        return false;

    }

    /**
     * creates a List of all the Player's pieces still on the board
     * 
     * @param player
     * @return List of ChessPieces
     */
    List<ChessPiece> getAllPlayersChessPiecesOnTheBoard(Player player) {
        List<ChessPiece> pieceList = new ArrayList<ChessPiece>();
        for (Coordinate letter : Coordinate.values()) {
            for (int i = 1; i <= 8; i++) {
                ChessPiece piece = board.getPieceFromBoardAt(letter.getName(), i);
                if (piece == null) {
                    continue;
                } else {
                    if (piece.getColour().equals(player.getColour())) {
                        pieceList.add(piece);
                    }
                }
            }
        }
        return pieceList;
    }
    
    /**
     * Return the last Move make or null if no move has yet to be made
     * @return Move
     */
    Move getTheLastMove(){
        if(allGameMoves.isEmpty()){
            return EMPTY_MOVE;
        }
        else
        {
            return allGameMoves.get(allGameMoves.size()-1);
        }
    }

    /**
     * Start the Game
     */
    public void start() {
        view.displayTheBoard();
        START: while (!isGameOver(playerOne, playerTwo)) {
            try {
                controller.takeTurn();
            } catch (InvalidMoveException ime) {
                System.out.println(ime.getMessage());
                continue START;
            }

            controller.changePlayer();
        }
        if (playerOne.isWinner()) {
            System.out.println("Player " + playerOne.getName() + " has won");
        } else {
            System.out.println("Player " + playerTwo.getName() + " has won");
        }
    }

    @Override
    public void update(Subject subject, Object message) {
        if(message instanceof Move){
            allGameMoves.add((Move)message);
        }
    }
}
