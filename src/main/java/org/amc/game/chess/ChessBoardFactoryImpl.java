package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;

import java.text.ParseException;
import java.util.List;

public class ChessBoardFactoryImpl implements ChessBoardFactory {

    private SimpleChessBoardSetupNotation notation;

    public ChessBoardFactoryImpl(SimpleChessBoardSetupNotation notation) {
        this.notation = notation;
    }

    /* (non-Javadoc)
     * @see org.amc.game.chess.ChessBoardFactory#getChessBoard(java.lang.String)
     */
    @Override
    public ChessBoard getChessBoard(String setupNotation) throws ParseException {
        if (notation.isInputValid(setupNotation)) {
            List<ChessPieceLocation> cpl = notation.getChessPieceLocations(setupNotation);
            return getChessBoard(cpl);
        } else {
            throw new ParseException("Not valid setup notation", 0);
        }
    }

    private ChessBoard getChessBoard(List<ChessPieceLocation> pieceLocations) {
        ChessBoard board = new ChessBoard();
        for (ChessPieceLocation cpl : pieceLocations) {
            board.putPieceOnBoardAt(cpl.getPiece(), cpl.getLocation());
        }
        return board;
    }
}
