package org.amc.game.chess;

import static org.amc.game.chess.StartingSquare.*;

import org.amc.game.chess.ChessBoard.Coordinate;

public final class SetupChessBoard {
	
	private static final int WHITE_PAWN_RANK = 2;
	private static final int BLACK_PAWN_RANK = 7;

    public static void setUpChessBoardToDefault(ChessBoard board) {
        board.putPieceOnBoardAt(BishopPiece.getBishopPiece(Colour.WHITE), WHITE_BISHOP_LEFT.getLocation());
        board.putPieceOnBoardAt(BishopPiece.getBishopPiece(Colour.WHITE), WHITE_BISHOP_RIGHT.getLocation());
        board.putPieceOnBoardAt(KingPiece.getKingPiece(Colour.WHITE), WHITE_KING.getLocation());
        board.putPieceOnBoardAt(QueenPiece.getQueenPiece(Colour.WHITE), WHITE_QUEEN.getLocation());
        board.putPieceOnBoardAt(KnightPiece.getKnightPiece(Colour.WHITE), WHITE_KNIGHT_LEFT.getLocation());
        board.putPieceOnBoardAt(KnightPiece.getKnightPiece(Colour.WHITE), WHITE_KNIGHT_RIGHT.getLocation());
        board.putPieceOnBoardAt(RookPiece.getRookPiece(Colour.WHITE), WHITE_ROOK_LEFT.getLocation());
        board.putPieceOnBoardAt(RookPiece.getRookPiece(Colour.WHITE), WHITE_ROOK_RIGHT.getLocation());
        for (Coordinate coord : Coordinate.values()) {
            board.putPieceOnBoardAt(PawnPiece.getPawnPiece(Colour.WHITE), new Location(coord, WHITE_PAWN_RANK));
        }
        board.putPieceOnBoardAt(BishopPiece.getBishopPiece(Colour.BLACK), BLACK_BISHOP_LEFT.getLocation());
        board.putPieceOnBoardAt(BishopPiece.getBishopPiece(Colour.BLACK), BLACK_BISHOP_RIGHT.getLocation());
        board.putPieceOnBoardAt(KingPiece.getKingPiece(Colour.BLACK), BLACK_KING.getLocation());
        board.putPieceOnBoardAt(QueenPiece.getQueenPiece(Colour.BLACK), BLACK_QUEEN.getLocation());
        board.putPieceOnBoardAt(KnightPiece.getKnightPiece(Colour.BLACK), BLACK_KNIGHT_LEFT.getLocation());
        board.putPieceOnBoardAt(KnightPiece.getKnightPiece(Colour.BLACK), BLACK_KNIGHT_RIGHT.getLocation());
        board.putPieceOnBoardAt(RookPiece.getRookPiece(Colour.BLACK), BLACK_ROOK_LEFT.getLocation());
        board.putPieceOnBoardAt(RookPiece.getRookPiece(Colour.BLACK), BLACK_ROOK_RIGHT.getLocation());
        for (Coordinate coord : Coordinate.values()) {
            board.putPieceOnBoardAt(PawnPiece.getPawnPiece(Colour.BLACK), new Location(coord, BLACK_PAWN_RANK));
        }
    }

    private SetupChessBoard() {
        throw new AssertionError("Can't be instantiated");
    }
}
