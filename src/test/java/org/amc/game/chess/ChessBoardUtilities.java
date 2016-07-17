package org.amc.game.chess;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chess.view.ChessBoardView;

public class ChessBoardUtilities {
	private ChessBoard board;
     
    public ChessBoardUtilities(ChessBoard board) {
        this.board = board;
    }
    
    public Move createMove(String start, String end) {
        return new Move(start + Move.MOVE_SEPARATOR + end);
    }
    
    public void addChessPieceToBoard(ChessPiece piece, String location) {
        board.putPieceOnBoardAt(piece, new Location(location));
    }
    
    public void addPawnPieceToBoard(Colour colour, String location) {
        addChessPieceToBoard(PawnPiece.getPawnPiece(colour), location);
    }
    
    public ChessPiece getPieceOnBoard(String location) {
        return board.getPieceFromBoardAt(new Location(location));
    }
    
    public void removePiece(String location) {
    	board.removePieceOnBoardAt(new Location(location));
    }

    public static void compareBoards(ChessBoard firstBoard, ChessBoard secondBoard) {
        for(Coordinate coord : Coordinate.values()) {
            for(int i = 1; i <= ChessBoard.BOARD_WIDTH; i++) {
                Location location = new Location(coord, i);
                ChessPiece pieceOne = firstBoard.getPieceFromBoardAt(location);
                ChessPiece pieceTwo = secondBoard.getPieceFromBoardAt(location);
                if(pieceOne == null) {
                    if(pieceTwo != null) {
                        fail("First board is missing a Chess Piece");
                    }
                } else {
                    if(pieceTwo == null) {
                        fail("Second board is missing a Chess Piece");
                    } else {
                        assertTrue(pieceOne.getColour().equals(pieceTwo.getColour()));
                        assertTrue(pieceOne.getClass().equals(pieceTwo.getClass()));
                    }
                }
            }
        }
    }
    
    public static void compareBoardsAndDisplay(ChessBoard firstBoard, ChessBoard secondBoard) {
        ChessBoardView view1 = new ChessBoardView(firstBoard);
        ChessBoardView view2 = new ChessBoardView(secondBoard);
        
        view1.displayTheBoard();
        view2.displayTheBoard();
        compareBoards(firstBoard, secondBoard);
    }
}
