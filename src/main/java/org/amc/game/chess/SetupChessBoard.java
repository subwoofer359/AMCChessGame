package org.amc.game.chess;

import static org.amc.game.chess.StartingSquare.*;

import org.amc.game.chess.ChessBoard.Coordinate;

public final class SetupChessBoard {

    public static void setUpChessBoardToDefault(ChessBoard board) {
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), WHITE_BISHOP_LEFT.getLocation());
        board.putPieceOnBoardAt(new BishopPiece(Colour.WHITE), WHITE_BISHOP_RIGHT.getLocation());
        board.putPieceOnBoardAt(new KingPiece(Colour.WHITE), WHITE_KING.getLocation());
        board.putPieceOnBoardAt(new QueenPiece(Colour.WHITE), WHITE_QUEEN.getLocation());
        board.putPieceOnBoardAt(new KnightPiece(Colour.WHITE), WHITE_KNIGHT_LEFT.getLocation());
        board.putPieceOnBoardAt(new KnightPiece(Colour.WHITE), WHITE_KNIGHT_RIGHT.getLocation());
        board.putPieceOnBoardAt(new RookPiece(Colour.WHITE), WHITE_ROOK_LEFT.getLocation());
        board.putPieceOnBoardAt(new RookPiece(Colour.WHITE), WHITE_ROOK_RIGHT.getLocation());
        for (Coordinate coord : Coordinate.values()) {
            board.putPieceOnBoardAt(PawnPiece.getPawnPiece(Colour.WHITE), new Location(coord, 2));
        }
        board.putPieceOnBoardAt(new BishopPiece(Colour.BLACK), BLACK_BISHOP_LEFT.getLocation());
        board.putPieceOnBoardAt(new BishopPiece(Colour.BLACK), BLACK_BISHOP_RIGHT.getLocation());
        board.putPieceOnBoardAt(new KingPiece(Colour.BLACK), BLACK_KING.getLocation());
        board.putPieceOnBoardAt(new QueenPiece(Colour.BLACK), BLACK_QUEEN.getLocation());
        board.putPieceOnBoardAt(new KnightPiece(Colour.BLACK), BLACK_KNIGHT_LEFT.getLocation());
        board.putPieceOnBoardAt(new KnightPiece(Colour.BLACK), BLACK_KNIGHT_RIGHT.getLocation());
        board.putPieceOnBoardAt(new RookPiece(Colour.BLACK), BLACK_ROOK_LEFT.getLocation());
        board.putPieceOnBoardAt(new RookPiece(Colour.BLACK), BLACK_ROOK_RIGHT.getLocation());
        for (Coordinate coord : Coordinate.values()) {
            board.putPieceOnBoardAt(PawnPiece.getPawnPiece(Colour.BLACK), new Location(coord, 7));
        }
    }

    private SetupChessBoard() {
        throw new RuntimeException("Can't be instantiated");
    }
}
