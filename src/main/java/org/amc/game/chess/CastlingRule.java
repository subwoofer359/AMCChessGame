package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.*;

/**
 * Encapsulates the Castling move rule in chess
 * @author Adrian Mclaughlin
 *
 */
public class CastlingRule implements ChessRule {

    private ReversibleMove kingsMove;
    private ReversibleMove rooksMove;
    /**
     * @see ChessRule#applyRule(ChessBoard, Move)
     */
    @Override
    public void applyRule(ChessBoard board, Move move) {
        if(isRuleApplicable(board, move)){
            kingsMove=new ReversibleMove(board,move);
            kingsMove.move();
            moveRook(board,move);
        }
    }
    
    /**
     * @see ChessRule#unapplyRule(ChessBoard, Move)
     */
    @Override
    public void unapplyRule(ChessBoard board,Move move){
        if(board.getTheLastMove().equals(rooksMove.getMove())){
            rooksMove.undoMove();
            kingsMove.undoMove();
        }
    }
    
    private boolean isCastlingMove(ChessBoard board,Move move){
        ChessPiece piece = board.getPieceFromBoardAt(move.getStart());
        if(piece instanceof KingPiece){
            return !piece.hasMoved() && move.getAbsoluteDistanceX()==2 && move.getAbsoluteDistanceY()==0;
        }else{
            return false;
        }
        
    }
    
    /**
     * @see ChessRule#isRuleApplicable(ChessBoard, Move)
     */
    @Override
    public boolean isRuleApplicable(ChessBoard board, Move move) {    
        if (isCastlingMove(board, move)) {
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
    
    private void moveRook(ChessBoard board,Move move){
        int rank=move.getStart().getNumber();
        Location rookLocation=null;
        Location rookNewLocation=null;
        if(isKingCastlingToTheRight(move)){
            rookLocation=new Location(H,rank);
            rookNewLocation=new Location(F,rank);
            
        }else{
            rookLocation=new Location(A,rank);
            rookNewLocation=new Location(D,rank);
        }
        rooksMove=new ReversibleMove(board,new Move(rookLocation,rookNewLocation));
        rooksMove.move();
    }
    
    private boolean isKingCastlingToTheRight(Move move){
        return move.getStart().compareTo(move.getEnd()) < 0;
    }
}
