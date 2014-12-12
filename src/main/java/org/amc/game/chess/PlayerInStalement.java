package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.ChessPieceLocation;

import java.util.List;
import java.util.Set;

public class PlayerInStalement {
    
    private Player player;
    private Player opponent;
    private ChessBoard board;
    private PlayerKingInCheckCondition inCheck;
    
    public PlayerInStalement(Player player,Player opponent,ChessBoard board) {
        inCheck=new PlayerKingInCheckCondition();
        this.player=player;
        this.opponent=opponent;
        this.board=board;
    }
    
    public boolean isStalemate(){
        if(inCheck.isPlayersKingInCheck(player, opponent, board)){
            return false;
        }
        List<ChessPieceLocation> playersPieces=board.getListOfPlayersPiecesOnTheBoard(player);
        for(ChessPieceLocation cpl:playersPieces){
            Set<Location> possibleMoveLocations=cpl.getPiece().getPossibleMoveLocations(board, cpl.getLocation());
            for(Location moveLocation:possibleMoveLocations){
                Move move = new Move(cpl.getLocation(),moveLocation);
                if(cpl.getPiece().isValidMove(board, move)){
                    if(willPlayerBeInCheck(move)){
                        continue;
                    }else{
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private boolean willPlayerBeInCheck(Move move){
        ReversibleMove checkMove = new ReversibleMove(board, move);
        checkMove.testMove();
        boolean playersKingInCheck=isPlayersKingInCheck();
        undoMove(checkMove);
        return playersKingInCheck;
    }
    
    private void undoMove(ReversibleMove move) {
        try {
            move.undoMove();
        } catch (InvalidMoveException ime) {
            throw new RuntimeException(
                            "move couldn't be undone therefore board is in an inconsistent state");
        }
    }
    
    boolean isPlayersKingInCheck() {
        return this.inCheck.isPlayersKingInCheck(player, opponent, board);
    }

}
