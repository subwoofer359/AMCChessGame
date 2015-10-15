package org.amc.game.chess;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.amc.game.chess.ChessBoard.Coordinate;

public class ChessBoardUtilities {

    private ChessBoardUtilities() {
    }

    public static void compareBoards(ChessBoard firstBoard, ChessBoard secondBoard) {
        for(Coordinate coord : Coordinate.values()) {
            for(int i = 1; i <= ChessBoard.BOARD_WIDTH; i++) {
                Location location = new Location(coord, i);
                ChessPiece pieceOne = firstBoard.getPieceFromBoardAt(location);
                ChessPiece pieceTwo = secondBoard.getPieceFromBoardAt(location);
                if(pieceOne == null) {
                    if(pieceTwo == null) {
                        
                    } else {
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
}
