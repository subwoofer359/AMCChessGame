package org.amc.game.chess.view;

import org.amc.game.ChessApplication;
import org.amc.game.chess.BishopPiece;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.KingPiece;
import org.amc.game.chess.KnightPiece;
import org.amc.game.chess.Location;
import org.amc.game.chess.PawnPiece;
import org.amc.game.chess.QueenPiece;
import org.amc.game.chess.RookPiece;
import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chess.controller.ConsoleController;
import org.amc.util.Observer;
import org.amc.util.Subject;

/**
 * Creates a simple console base view of the ChessBoard
 * 
 * @author Adrian Mclaughlin
 *
 */
public class ChessBoardView implements Observer {
    private final ChessBoard chessBoard;
    final int WIDTH_OF_BOARD = ChessBoard.Coordinate.values().length;

    public ChessBoardView(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
        this.chessBoard.attachObserver(this);
    }

    /**
     * @see org.amc.util.Observer#update(Subject, Object)
     */
    @Override
    public void update(Subject subject, Object message) {
        displayTheBoard();

    }

    /**
     * Prints the ChessBoard and position of the pieces on the screen
     */
    public void displayTheBoard() {
        StringBuilder sb = new StringBuilder();
        int row = 8;
        sb.append(printBoardHeader());
        while (row > 0) {
            for(Coordinate coord:ChessBoard.Coordinate.values()){
                sb.append(printSquare(coord, row));
            }
            sb.append(addRowEnd(row));

            sb.append(getLine());
            row--;
        }
        System.out.print(sb.toString());
        // printBoardFooter();
    }

    /**
     * Creates the Square and it's contents
     * 
     * @param col
     * @param row
     * @return StringBuilder
     */
    private StringBuilder printSquare(Coordinate coordinate, int row) {
        StringBuilder sb = new StringBuilder();
        sb.append('|');
        ChessPiece piece = chessBoard.getPieceFromBoardAt(new Location(coordinate,row));//getPieceFromBoardAt(col, row);
        if (piece == null) {
            sb.append("   ");
        } else {
            sb.append(' ');
            sb.append(getChessPieceMapping(piece));
            sb.append(' ');
        }
        return sb;
    }

    /**
     * Creates a | and Chess board number coordinate for the right side of the
     * board
     * 
     * @param row
     * @return StringBuilder
     */
    private StringBuilder addRowEnd(int row) {
        StringBuilder sb = new StringBuilder();
        sb.append('|');
        sb.append(row);
        sb.append('\n');
        return sb;
    }

    /**
     * Creates the complete top header for the Chess board
     * 
     * @return
     */
    private StringBuilder printBoardHeader() {
        StringBuilder sb = getLetterHeader();
        sb.append(getLine());
        return sb;
    }

    /**
     * Creates a line of the Chess board letter coordinates for the top of the
     * board
     * 
     * @return StringBuilder
     */
    private StringBuilder getLetterHeader() {
        StringBuilder sb = new StringBuilder();
        for (Coordinate coordinate : ChessBoard.Coordinate.values()) {
            sb.append("  ");
            sb.append(coordinate.toString());
            sb.append(' ');
        }
        sb.append('\n');
        return sb;
    }

    /**
     * Creates a line of '-----' the width of the entire board
     * 
     * @return StringBuilder
     */
    private StringBuilder getLine() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < WIDTH_OF_BOARD; i++) {
            sb.append("----");
        }
        sb.append('\n');
        return sb;
    }

    /**
     * @param piece
     *            ChessPiece
     * @return a Character representing the ChessPiece
     */
    private Character getChessPieceMapping(ChessPiece piece) {
        Character[] blackSymbols = { 'K', 'Q', 'B', 'N', 'R', 'P' };
        Character[] whiteSymbols = { 'k', 'q', 'b', 'n', 'r', 'p' };
        int index = -1;

        if (piece.getClass().equals(KingPiece.class)) {
            index = 0;
        } else if (piece.getClass().equals(QueenPiece.class)) {
            index = 1;
        } else if (piece.getClass().equals(BishopPiece.class)) {
            index = 2;
        } else if (piece.getClass().equals(KnightPiece.class)) {
            index = 3;
        } else if (piece.getClass().equals(RookPiece.class)) {
            index = 4;
        } else if (piece.getClass().equals(PawnPiece.class)) {
            index = 5;
        }

        if (piece.getColour().equals(Colour.BLACK)) {
            return blackSymbols[index];
        } else {
            return whiteSymbols[index];
        }
    }

    
    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        board.initialise();
        ChessGamePlayer playerOne = new ChessGamePlayer(new HumanPlayer("Stephen"),Colour.WHITE);
        ChessGamePlayer playerTwo = new ChessGamePlayer(new HumanPlayer("Chris"), Colour.BLACK);
        ChessGame chessGame=new ChessGame(board, playerOne, playerTwo);
        ConsoleController controller = new ConsoleController(chessGame);
        ChessBoardView view = new ChessBoardView(board);
        ChessApplication game = new ChessApplication(playerOne, playerTwo);
        game.setChessGame(chessGame);
        game.setController(controller);
        game.setView(view);

        game.start();
    }
}
