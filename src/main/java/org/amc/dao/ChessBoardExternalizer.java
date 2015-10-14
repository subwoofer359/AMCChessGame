package org.amc.dao;

import static org.amc.game.chess.ChessBoardSetupNotation.ChessPieceNotation.*;

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

public class ChessBoardExternalizer {

    private ChessBoardExternalizer() {
        
    }

    
    public static String getChessBoardString(ChessPiece[][] board) {
        StringBuilder sb = new StringBuilder();
        for(int t = 0; t < Coordinate.values().length; t++) {
            for(int i = 0; i < ChessBoard.BOARD_WIDTH; i++) {
                ChessPiece piece = board[t][i];
                if(piece != null) {
                    Location location = new Location(Coordinate.values()[t], ChessBoard.BOARD_WIDTH-i);
                    sb.append(getChessPieceSymbol(piece));
                    sb.append(location.asString());
                }
            }
        }
        return sb.toString();
    }
    
    private static ChessPieceNotation getChessPieceSymbol(ChessPiece piece) {

        if(piece.getColour().equals(Colour.BLACK))
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
    
    public static ChessPiece[][] getChessBoard(String chessBoardStr) throws ParseException {
        ChessBoardFactory factory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        factory.getChessBoard(chessBoardStr);
        return null;
    }
}
