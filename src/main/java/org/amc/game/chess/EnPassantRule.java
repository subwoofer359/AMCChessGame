package org.amc.game.chess;

/**
 * Encapsulates the en passant capture rule of chess
 * @author Adrian Mclaughlin
 *
 */
public class EnPassantRule extends PawnPieceRule{
    /**
     * @see ChessMoveRule#applyRule(ChessBoard, Move)
     */
    @Override
    public void applyRule(ChessBoard board, Move move) {
        if(isEnPassantCapture(board,move)){
            Location endSquare=move.getEnd();
            ChessPiece piece=board.getPieceFromBoardAt(move.getStart());
            board.move(move);
            removeCapturedPawnFromTheChessBoard(board, piece, endSquare);
                
        }
    }
    
    private void removeCapturedPawnFromTheChessBoard(ChessBoard board,ChessPiece piece,Location endSquare){
        Location capturedPawnLocation;
        if(piece.getColour().equals(Colour.WHITE)){
            capturedPawnLocation=new Location(endSquare.getLetter(),endSquare.getNumber()-1);
            
        }
        else{
            capturedPawnLocation=new Location(endSquare.getLetter(),endSquare.getNumber()+1);
        }
        board.removePieceOnBoardAt(capturedPawnLocation);
    }
    
    /**
     * Checks to see if the pawn does an en passant capture move
     * 
     * @param board
     * @param move
     * @return true if it's a valid en passant move
     */
    boolean isEnPassantCapture(ChessBoard board, Move move) {
        Move opponentsMove = board.getTheLastMove();
        ChessPiece opponentsPiece = getOpponentsChessPieceThatMovedLast(board, opponentsMove);
        if (isEndSquareNotEmpty(board, move)) {
            return false;
        }
        return isPawnChessPiece(opponentsPiece) && 
                        opponentsMove.getAbsoluteDistanceY() == 2
                        && isMoveToSameFile(move, opponentsMove);
        
    }
    
    private ChessPiece getOpponentsChessPieceThatMovedLast(ChessBoard board,Move opponentsMove){
        return board.getPieceFromBoardAt(opponentsMove.getEnd());
    }
    
    private boolean isEndSquareNotEmpty(ChessBoard board,Move move){
        return !board.isEndSquareEmpty(move.getEnd());
    }
   
    private boolean isMoveToSameFile(Move myMove, Move lastOpposingMove) {
        return myMove.getEnd().getLetter().equals(lastOpposingMove.getEnd().getLetter());
    }

    /**
     * @see ChessMoveRule#isRuleApplicable(ChessBoard, Move)
     */
    @Override
    public boolean isRuleApplicable(ChessBoard board, Move move) {
        return isEnPassantCapture(board, move);
    }
}
