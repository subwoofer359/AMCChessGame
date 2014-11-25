package org.amc.game.chess;

public class EnPassantRule implements ChessRule {


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
        if (!isEndSquareEmpty(board, move)) {
            return false;
        }
        if (isPawnChessPiece(piece) && lastMove.getAbsoluteDistanceY() == 2
                        && moveToSameFile(move, lastMove)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Checks to see if the end Square is empty
     * @param board
     * @param move
     * @return true if empty
     */
    boolean isEndSquareEmpty(ChessBoard board, Move move){
        Location endSquare = move.getEnd();
        ChessPiece piece = board.getPieceFromBoardAt(endSquare.getLetter().getName(),
                        endSquare.getNumber());
        return piece==null;
    }
    
    private boolean isPawnChessPiece(ChessPiece piece) {
        return piece != null && piece instanceof PawnPiece;
    }
    
    private boolean moveToSameFile(Move myMove, Move lastOpposingMove) {
        return myMove.getEnd().getLetter().equals(lastOpposingMove.getEnd().getLetter());
    }

    @Override
    public boolean isRuleApplicable(ChessBoard board, Move move) {
        // TODO Move code from PawnPiece.isEnPassantCapture to here
        return isMoveEnPassantCapture(board, move);
    }
}
