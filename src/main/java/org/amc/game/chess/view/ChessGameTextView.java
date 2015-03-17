package org.amc.game.chess.view;

import org.amc.game.chess.BishopPiece;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.Colour;
import org.amc.game.chess.KingPiece;
import org.amc.game.chess.KnightPiece;
import org.amc.game.chess.Location;
import org.amc.game.chess.ObservableChessGame;
import org.amc.game.chess.PawnPiece;
import org.amc.game.chess.QueenPiece;
import org.amc.game.chess.RookPiece;
import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.util.Observer;
import org.amc.util.Subject;

public class ChessGameTextView implements Observer{

    private ObservableChessGame oChessGame;
    public ChessGameTextView(ObservableChessGame chessGame) {
        this.oChessGame=chessGame;
        this.oChessGame.attachObserver(this);
    }
    @Override
    public void update(Subject subject, Object message) {
        if(message instanceof ChessBoard){
            displayTheBoard((ChessBoard)message);
        }else
        {
            //Ignore update notification
        }
    }
    
    
    /**
     * Prints the ChessBoard and position of the pieces on the screen
     */
    public String displayTheBoard(ChessBoard board) {
        StringBuilder sb = new StringBuilder();
        int row = 8;
        sb.append(printBoardHeader());
        while (row > 0) {
            for(Coordinate coord:ChessBoard.Coordinate.values()){
                sb.append(printSquare(board,coord, row));
            }
            sb.append(addRowEnd(row));

            sb.append(getLine());
            row--;
        }
        System.out.print(sb.toString());
        // printBoardFooter();
        return sb.toString();
    }

    /**
     * Creates the Square and it's contents
     * 
     * @param col
     * @param row
     * @return StringBuilder
     */
    private StringBuilder printSquare(ChessBoard chessBoard,Coordinate coordinate, int row) {
        StringBuilder sb = new StringBuilder();
        sb.append('|');
        ChessPiece piece = chessBoard.getPieceFromBoardAt(new Location(coordinate,row));//getPieceFromBoardAt(col, row);
        if (piece == null) {
            sb.append("   ");
        } else {
            sb.append(' ');
            sb.append(ChessPieceTextSymbol.getChessPieceTextSymbol(piece));
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
        for (int i = 0; i < ChessBoard.BOARD_WIDTH; i++) {
            sb.append("----");
        }
        sb.append('\n');
        return sb;
    }
}
