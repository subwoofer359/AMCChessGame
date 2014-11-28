package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.*;

/**
 * Encapsulates the Castling move rule in chess
 * 
 * @author Adrian Mclaughlin
 *
 */
public class CastlingRule implements ChessMoveRule {

    private ReversibleMove kingsMove;
    private ReversibleMove rooksMove;

    /**
     * @see ChessMoveRule#applyRule(ChessBoard, Move)
     */
    @Override
    public void applyRule(ChessBoard board, Move move) {
        if (isRuleApplicable(board, move)) {
            kingsMove = new ReversibleMove(board, move);
            kingsMove.move();
            moveRook(board, move);
        }
    }

    private void moveRook(ChessBoard board, Move move) {
        int rank = move.getStart().getNumber();
        Location rookLocation = null;
        Location rookNewLocation = null;
        if (isKingCastlingToTheRight(move)) {
            rookLocation = new Location(H, rank);
            rookNewLocation = new Location(F, rank);

        } else {
            rookLocation = new Location(A, rank);
            rookNewLocation = new Location(D, rank);
        }
        rooksMove = new ReversibleMove(board, new Move(rookLocation, rookNewLocation));
        rooksMove.move();
    }

    private boolean isKingCastlingToTheRight(Move move) {
        return move.getStart().compareTo(move.getEnd()) < 0;
    }

    /**
     * @see ChessMoveRule#unapplyRule(ChessBoard, Move)
     */
    @Override
    public void unapplyRule(ChessBoard board, Move move) {
        if (board.getTheLastMove().equals(rooksMove.getMove())) {
            try{
                rooksMove.undoMove();
                kingsMove.undoMove();
            }catch(InvalidMoveException ime){
                
            }
        }
    }

    private boolean isCastlingMove(ChessBoard board, Move move) {
        ChessPiece piece = board.getPieceFromBoardAt(move.getStart());
        if (piece instanceof KingPiece) {
            return isKingsFirstMoveTwoSquaresRightOrLeft(piece, move);
        } else {
            return false;
        }

    }

    private boolean isKingsFirstMoveTwoSquaresRightOrLeft(ChessPiece piece, Move move) {
        return !piece.hasMoved() && move.getAbsoluteDistanceX() == 2
                        && move.getAbsoluteDistanceY() == 0;
    }

    /**
     * @see ChessMoveRule#isRuleApplicable(ChessBoard, Move)
     */
    @Override
    public boolean isRuleApplicable(ChessBoard board, Move move) {
        if (isCastlingMove(board, move)) {
            int rank = move.getStart().getNumber();
            if (isKingCastlingToTheRight(move)) {
                return canKingCastleToTheRight(board, rank);
            } else {
                return canKingCastleToTheLeft(board, rank);
            }
        } else {
            return false;
        }
    }

    private boolean canKingCastleToTheRight(ChessBoard board, int rank) {
        ChessPiece rook = board.getPieceFromBoardAt(new Location(H, rank));
        return isNoChessPiecesBetweenKingAndRightRook(board, rank, rook) && hasRookNotMoved(rook);

    }

    private boolean isNoChessPiecesBetweenKingAndRightRook(ChessBoard board, int rank,
                    ChessPiece piece) {
        return board.isEndSquareEmpty(new Location(F, rank))
                        && board.isEndSquareEmpty(new Location(G, rank));
    }

    private boolean canKingCastleToTheLeft(ChessBoard board, int rank) {
        ChessPiece rook = board.getPieceFromBoardAt(new Location(A, rank));
        return isNoChessPiecesBetweenKingAndLeftRook(board, rank, rook) && hasRookNotMoved(rook);
    }

    private boolean isNoChessPiecesBetweenKingAndLeftRook(ChessBoard board, int rank,
                    ChessPiece piece) {

        return board.isEndSquareEmpty(new Location(B, rank))
                        && board.isEndSquareEmpty(new Location(C, rank))
                        && board.isEndSquareEmpty(new Location(D, rank));
    }

    private boolean hasRookNotMoved(ChessPiece piece) {
        return piece instanceof RookPiece && !piece.hasMoved();
    }

}
