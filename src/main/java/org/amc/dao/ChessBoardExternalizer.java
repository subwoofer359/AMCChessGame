package org.amc.dao;

import static org.amc.game.chess.NoChessPiece.NO_CHESSPIECE;

import static org.amc.game.chess.ChessBoardSetupNotation.ChessPieceNotation.*;
import static org.amc.game.chess.SimpleChessBoardSetupNotation.MOVE_TOKEN;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chess.ChessBoardFactory;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.ChessBoardSetupNotation.ChessPieceNotation;
import org.amc.game.chess.BishopPiece;
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.Colour;
import org.amc.game.chess.KingPiece;
import org.amc.game.chess.KnightPiece;
import org.amc.game.chess.Location;
import org.amc.game.chess.QueenPiece;
import org.amc.game.chess.RookPiece;
import org.amc.game.chess.SimpleChessBoardSetupNotation;

import java.text.ParseException;

public final class ChessBoardExternalizer {

    private ChessBoardExternalizer() {
        
    }

    /**
     * Creates a String which represents the ChessBoard in SimpleChessBoardSetupNotation
     * Location coordinates are converted to lower case
     * 
     * @param board ChessBoard
     * @return String representation of the ChessBoard
     */
    public static String getChessBoardString(ChessBoard board) {
        StringBuilder sb = new StringBuilder();
        for(Coordinate coord : Coordinate.values()) {
            for(int i = 0; i < ChessBoard.BOARD_WIDTH; i++) {
                Location location = new Location(coord, ChessBoard.BOARD_WIDTH - i);
                ChessPiece piece = board.get(location);
                if(piece != NO_CHESSPIECE) {
                    sb.append(getChessPieceSymbol(piece));
                    sb.append(location.asString().toLowerCase());
                    if(piece.hasMoved()) {
                    	sb.append(MOVE_TOKEN);
                    }
                }
            }
        }
        return sb.toString();
    }
    
    private static ChessPieceNotation getChessPieceSymbol(ChessPiece piece) {

        if(Colour.BLACK == piece.getColour())
        {
            if(piece instanceof KingPiece) {
                return K;
            } else if (piece instanceof QueenPiece) {
                return Q;
            } else if (piece instanceof BishopPiece) {
                return B;
            } else if (piece instanceof RookPiece) {
                return R;
            } else if (piece instanceof KnightPiece) {
                return N;
            } else {
                return P;
            }
        } else
        {
            if(piece instanceof KingPiece) {
                return k;
            } else if (piece instanceof QueenPiece) {
                return q;
            } else if (piece instanceof BishopPiece) {
                return b;
            } else if (piece instanceof RookPiece) {
                return r;
            } else if (piece instanceof KnightPiece) {
                return n;
            } else {
                return p;
            }
        }
    }
    
    /**
     * Given a String representation of ChessBoard in SimpleChessBoardSetupNotation
     * returns a configured ChessBoard
     *  
     * @param chessBoardStr String in SimpleChessBoardSetupNotation
     * @return ChessBoard
     * @throws ParseException if String is not valid SimpleChessBoardSetupNotation
     */
    public static ChessBoard getChessBoard(String chessBoardStr) throws ParseException {
        ChessBoardFactory factory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        return factory.getChessBoard(chessBoardStr);
    }
}
