package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.util.Observer;
import org.amc.util.Subject;

/**
 * Creates a simple console base view of the ChessBoard
 * 
 * @author Adrian Mclaughlin
 *
 */
public class ChessBoardView implements Observer {
    private ChessBoard chessBoard;
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
    void displayTheBoard() {
        StringBuilder sb = new StringBuilder();
        int row = 8;
        sb.append(printBoardHeader());
        while (row > 0) {
            int col = 0;
            while (col < 8) {
                sb.append(printSquare(col, row));
                col++;
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
    private StringBuilder printSquare(int col, int row) {
        StringBuilder sb = new StringBuilder();
        sb.append('|');
        ChessPiece piece = chessBoard.getPieceFromBoardAt(col, row);
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
        Character[] whiteSymbols = { 'K', 'Q', 'B', 'k', 'R', 'p' };
        Character[] blackSymbols = { '\u0136', '\u0150', '\u00DF', '\u0138', '\u0158', '\u03F8' };
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
        Player playerOne = new HumanPlayer("Stephen", Colour.WHITE);
        Player playerTwo = new HumanPlayer("Chris", Colour.BLACK);
        ConsoleController controller = new ConsoleController(board, playerOne, playerTwo);
        ChessBoardView view = new ChessBoardView(board);
        ChessGame game = new ChessGame(playerOne, playerTwo);
        game.setBoard(board);
        game.setController(controller);
        game.setView(view);

        game.start();
    }
}
