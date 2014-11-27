package org.amc.game.chess;

public class ReversibleMove{

    private Move move;
    private ChessBoard board;
    private ChessPiece piece;
    public ReversibleMove(ChessBoard board,Move move) {
        this.board=board;
        this.move=move;
    }
    
    public void move(){
        piece=board.getPieceFromBoardAt(move.getEnd());
        board.move(this.move);
    }
    
    public void testMove(){
        piece=board.getPieceFromBoardAt(move.getEnd());
        board.quietMove(this.move);
    }
    
    public void undoMove(){
        Move undoMove=new Move(move.getEnd(),move.getStart());
        board.quietMove(undoMove);
        board.removeMoveFromMoveList(move);
        board.removeMoveFromMoveList(undoMove);
        board.putPieceOnBoardAt(piece, move.getEnd());
        piece=null;
    }

    public final Move getMove() {
        return move;
    }
}
