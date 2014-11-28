package org.amc.game.chess;

/**
 * Create a reversible move so a move can be undone
 * 
 * @author Adrian Mclaughlin
 *
 */
public class ReversibleMove{

    private Move move;
    private ChessBoard board;
    private ChessPiece piece;
    public ReversibleMove(ChessBoard board,Move move) {
        this.board=board;
        this.move=move;
    }
    
    /**
     * Make a Chess move
     */
    public void move(){
        piece=board.getPieceFromBoardAt(move.getEnd());
        board.move(this.move);
    }
    
    
    /**
     * Make a Chess move which is not visible
     */
    public void testMove(){
        piece=board.getPieceFromBoardAt(move.getEnd());
        board.quietMove(this.move);
    }
    
    /**
     * Undo a move
     * 
     * The move to be undone must be the last move which was made.  
     */
    public void undoMove()throws InvalidMoveException{
        if(isNotTheLastMoveMade(move)){
            throw new InvalidMoveException("The move being undone wasn't the last move made");
        }
        Move undoMove=new Move(move.getEnd(),move.getStart());
        board.quietMove(undoMove);
        board.removeMoveFromMoveList(undoMove);
        board.removeMoveFromMoveList(move);
        board.putPieceOnBoardAt(piece, move.getEnd());
        piece=null;
    }

    private boolean isNotTheLastMoveMade(Move move){
        return !board.getTheLastMove().equals(move);
    }
    
    public final Move getMove() {
        return move;
    }
}
