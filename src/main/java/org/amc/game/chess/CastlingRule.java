package org.amc.game.chess;

import static org.amc.game.chess.ChessBoard.Coordinate.*;

/**
 * Encapsulates the Castling move rule in chess
 * 
 * @author Adrian Mclaughlin
 *
 */
public class CastlingRule implements ChessMoveRule{
	private static final CastlingRule instance = new CastlingRule();
	
	public static final CastlingRule getInstance() {
		return instance;
	}
    
	private CastlingRule() {
	}
    /**
     * @see ChessMoveRule#applyRule(ChessGame, Move)
     */
    @Override
    public void applyRule(ChessGame chessGame, Move move) {
        if (isRuleApplicable(chessGame, move)) {
            chessGame.getChessBoard().move(move);
            moveRook(chessGame.getChessBoard(), move);
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
        board.move(new Move(rookLocation, rookNewLocation));
        
    }

    private boolean isKingCastlingToTheRight(Move move) {
        return move.getStart().compareTo(move.getEnd()) < 0;
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
     * @see ChessMoveRule#isRuleApplicable(ChessGame, Move)
     */
    @Override
    public boolean isRuleApplicable(ChessGame game, Move move) {
        if (isCastlingMove(game.getChessBoard(), move)) {
            int rank = move.getStart().getNumber();
            if (isKingCastlingToTheRight(move)) {
                return canKingCastleToTheRight(game.getChessBoard(), rank);
            } else {
                return canKingCastleToTheLeft(game.getChessBoard(), rank);
            }
        } else {
            return false;
        }
    }

    private boolean canKingCastleToTheRight(ChessBoard board, int rank) {
        ChessPiece rook = board.getPieceFromBoardAt(new Location(H, rank));
        return isNoChessPiecesBetweenKingAndRightRook(board, rank) && hasRookNotMoved(rook);

    }

    private boolean isNoChessPiecesBetweenKingAndRightRook(ChessBoard board, int rank) {
        return board.isEndSquareEmpty(new Location(F, rank))
                        && board.isEndSquareEmpty(new Location(G, rank));
    }

    private boolean canKingCastleToTheLeft(ChessBoard board, int rank) {
        ChessPiece rook = board.getPieceFromBoardAt(new Location(A, rank));
        return isNoChessPiecesBetweenKingAndLeftRook(board, rank) && hasRookNotMoved(rook);
    }

    private boolean isNoChessPiecesBetweenKingAndLeftRook(ChessBoard board, int rank) {

        return board.isEndSquareEmpty(new Location(B, rank))
                        && board.isEndSquareEmpty(new Location(C, rank))
                        && board.isEndSquareEmpty(new Location(D, rank));
    }

    private boolean hasRookNotMoved(ChessPiece piece) {
        return piece instanceof RookPiece && !piece.hasMoved();
    }

}
