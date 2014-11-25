package org.amc.game.chess;

/**
 * Encapsulates the en passant capture rule of chess
 * @author Adrian Mclaughlin
 *
 */
public class EnPassantRule implements ChessRule {
    /**
     * @see ChessRule#applyRule(ChessBoard, Move)
     */
    @Override
    public void applyRule(ChessBoard board, Move move) {
        if(isMoveEnPassantCapture(board,move)){
            Location endSquare=move.getEnd();
            ChessPiece piece=board.getPieceFromBoardAt(move.getStart());
            board.move(move);
            if(piece.getColour().equals(Colour.WHITE)){
                Location capturedPawn=new Location(endSquare.getLetter(),endSquare.getNumber()-1);
                board.removePieceOnBoardAt(capturedPawn);
            }
            else{
                Location capturedPawn=new Location(endSquare.getLetter(),endSquare.getNumber()+1);
                board.removePieceOnBoardAt(capturedPawn);
            }    
        }
    }
    
    
    
    /**
     * Checks to see if the move is en passant
     * calls the PawnPiece.isEnPassantCapture
     * 
     * @param move
     * @return boolean
     */
    boolean isMoveEnPassantCapture(ChessBoard board,Move move){
        ChessPiece piece=board.getPieceFromBoardAt(move.getStart());
        if(piece instanceof PawnPiece){
            return isEnPassantCapture(board, move);
        }else{
            return false;
        }
    }
    /**
     * Checks to see if the pawn do an en passant capture move
     * 
     * @param board
     * @param move
     * @return true if it's a valid en passant move
     */
    boolean isEnPassantCapture(ChessBoard board, Move move) {
        Move lastMove = board.getTheLastMove();
        ChessPiece piece = board.getPieceFromBoardAt(lastMove.getEnd());
        if (isEndSquareNotEmpty(board, move)) {
            return false;
        }
        if (isPawnChessPiece(piece) && lastMove.getAbsoluteDistanceY() == 2
                        && moveToSameFile(move, lastMove)) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean isEndSquareNotEmpty(ChessBoard board,Move move){
        return !board.isEndSquareEmpty(move.getEnd());
    }
    
    private boolean isPawnChessPiece(ChessPiece piece) {
        return piece != null && piece instanceof PawnPiece;
    }
    
    private boolean moveToSameFile(Move myMove, Move lastOpposingMove) {
        return myMove.getEnd().getLetter().equals(lastOpposingMove.getEnd().getLetter());
    }

    /**
     * @see ChessRule#isRuleApplicable(ChessBoard, Move)
     */
    @Override
    public boolean isRuleApplicable(ChessBoard board, Move move) {
        return isMoveEnPassantCapture(board, move);
    }
}
