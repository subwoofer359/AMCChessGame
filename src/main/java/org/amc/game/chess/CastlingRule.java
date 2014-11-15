package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.A;
import static org.amc.game.chess.ChessBoard.Coordinate.B;
import static org.amc.game.chess.ChessBoard.Coordinate.C;
import static org.amc.game.chess.ChessBoard.Coordinate.D;
import static org.amc.game.chess.ChessBoard.Coordinate.F;
import static org.amc.game.chess.ChessBoard.Coordinate.G;
import static org.amc.game.chess.ChessBoard.Coordinate.H;

public class CastlingRule implements ChessRule {

    @Override
    public void applyRule(ChessBoard board, Move move) {
        if(isCastlingMove(board, move)){
            moveRook(board,move);
        }
    }
    
    public boolean isCastlingMove(ChessBoard board, Move move) {
        ChessPiece piece = board.getPieceFromBoardAt(move.getStart());
        if (piece instanceof KingPiece && ((KingPiece) piece).isCastlingMove(move)) {
            int rank=move.getStart().getNumber();
            if (isKingCastlingToTheRight(move)) {
                ChessPiece rook = board.getPieceFromBoardAt(new Location(H, rank));
                if (board.isEndSquareEmpty(new Location(F, rank))
                                && board.isEndSquareEmpty(new Location(G, rank))
                                && rook instanceof RookPiece) {
                    return !rook.hasMoved();
                } else {
                    return false;
                }
            } else {
                ChessPiece rook = board.getPieceFromBoardAt(new Location(A, rank));
                if (board.isEndSquareEmpty(new Location(B, rank))
                                && board.isEndSquareEmpty(new Location(C, rank))
                                && board.isEndSquareEmpty(new Location(D, rank))
                                && rook instanceof RookPiece) {
                    return !rook.hasMoved();
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
    
    public void moveRook(ChessBoard board,Move move){
        int rank=move.getStart().getNumber();
        Location rookLocation=null;
        Location rookNewLocation=null;
        ChessPiece piece=null;
        if(isKingCastlingToTheRight(move)){
            rookLocation=new Location(H,rank);
            rookNewLocation=new Location(F,rank);
            piece=board.getPieceFromBoardAt(rookLocation);
            
        }else{
            rookLocation=new Location(A,rank);
            rookNewLocation=new Location(D,rank);
            piece=board.getPieceFromBoardAt(rookLocation);
        }
        board.removePieceOnBoardAt(rookLocation);
        board.putPieceOnBoardAt(piece, rookNewLocation);
    }
    
    private boolean isKingCastlingToTheRight(Move move){
        return move.getStart().compareTo(move.getEnd()) < 0;
    }
}
