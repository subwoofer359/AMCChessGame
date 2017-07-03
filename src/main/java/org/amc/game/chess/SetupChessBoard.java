package org.amc.game.chess;

import static org.amc.game.chess.StartingSquare.*;

import org.amc.game.chess.ChessBoard.Coordinate;

public final class SetupChessBoard {
	
	private static final int WHITE_PAWN_RANK = 2;
	private static final int BLACK_PAWN_RANK = 7;

    public static void setUpChessBoardToDefault(ChessBoard board) {
        board.put(BishopPiece.getPiece(Colour.WHITE), WHITE_BISHOP_LEFT.getLocation());
        board.put(BishopPiece.getPiece(Colour.WHITE), WHITE_BISHOP_RIGHT.getLocation());
        board.put(KingPiece.getPiece(Colour.WHITE), WHITE_KING.getLocation());
        board.put(QueenPiece.getPiece(Colour.WHITE), WHITE_QUEEN.getLocation());
        board.put(KnightPiece.getPiece(Colour.WHITE), WHITE_KNIGHT_LEFT.getLocation());
        board.put(KnightPiece.getPiece(Colour.WHITE), WHITE_KNIGHT_RIGHT.getLocation());
        board.put(RookPiece.getPiece(Colour.WHITE), WHITE_ROOK_LEFT.getLocation());
        board.put(RookPiece.getPiece(Colour.WHITE), WHITE_ROOK_RIGHT.getLocation());
        for (Coordinate coord : Coordinate.values()) {
            board.put(PawnPiece.getPiece(Colour.WHITE), new Location(coord, WHITE_PAWN_RANK));
        }
        board.put(BishopPiece.getPiece(Colour.BLACK), BLACK_BISHOP_LEFT.getLocation());
        board.put(BishopPiece.getPiece(Colour.BLACK), BLACK_BISHOP_RIGHT.getLocation());
        board.put(KingPiece.getPiece(Colour.BLACK), BLACK_KING.getLocation());
        board.put(QueenPiece.getPiece(Colour.BLACK), BLACK_QUEEN.getLocation());
        board.put(KnightPiece.getPiece(Colour.BLACK), BLACK_KNIGHT_LEFT.getLocation());
        board.put(KnightPiece.getPiece(Colour.BLACK), BLACK_KNIGHT_RIGHT.getLocation());
        board.put(RookPiece.getPiece(Colour.BLACK), BLACK_ROOK_LEFT.getLocation());
        board.put(RookPiece.getPiece(Colour.BLACK), BLACK_ROOK_RIGHT.getLocation());
        for (Coordinate coord : Coordinate.values()) {
            board.put(PawnPiece.getPiece(Colour.BLACK), new Location(coord, BLACK_PAWN_RANK));
        }
    }

    private SetupChessBoard() {
        throw new AssertionError("Can't be instantiated");
    }
}
