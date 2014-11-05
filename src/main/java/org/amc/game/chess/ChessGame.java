package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class ChessGame {
    private ChessBoard board;
    private ChessBoardView view;
    private Controller controller;
    private Player playerOne;
    private Player playerTwo;
    
    public ChessGame(Player playerOne,Player playerTwo) {
        this.playerOne=playerOne;
        this.playerTwo=playerTwo;
    }

    public final void setBoard(ChessBoard board) {
        this.board = board;
    }

    public final void setView(ChessBoardView view) {
        this.view = view;
    }

    public final void setController(Controller controller) {
        this.controller = controller;
    }

    boolean isGameOver(Player playerOne,Player playerTwo){
        boolean playerOneHaveTheirKing=doesThePlayerStillHaveTheirKing(playerOne);
        boolean playerTwoHaveTheirKing=doesThePlayerStillHaveTheirKing(playerTwo);
        if(!playerOneHaveTheirKing){
            playerTwo.isWinner(true);
            return true;
        }else if(!playerTwoHaveTheirKing){
            playerOne.isWinner(true);
            return true;
        }
        else
        {
            return false;
        }
    }
    
    boolean doesThePlayerStillHaveTheirKing(Player player){
        List<ChessPiece> allPlayersChessPieces=getAllPlayersChessPiecesOnTheBoard(player);
        for(ChessPiece piece:allPlayersChessPieces){
            if(piece.getClass().equals(KingPiece.class)){
                return true;
            }
        }
        return false;
        
    }
    
    List<ChessPiece> getAllPlayersChessPiecesOnTheBoard(Player player){
        List<ChessPiece> pieceList=new ArrayList<ChessPiece>();
        for(Coordinate letter:Coordinate.values()){
            for(int i=1;i<=8;i++){
                ChessPiece piece=board.getPieceFromBoardAt(letter.getName(),i);
                if(piece==null){
                    continue;
                }
                else{
                    if(piece.getColour().equals(player.getColour())){
                        pieceList.add(piece);
                    }
                }
            }
        }
        return pieceList;   
    }
    
    public void start(){
        view.displayTheBoard();
        START:while(!isGameOver(playerOne, playerTwo)){
            try{
                controller.takeTurn();
            }catch(InvalidMoveException ime){
                System.out.println(ime.getMessage());
                continue START;
            }
        
            controller.changePlayer();
        }
        if(playerOne.isWinner()){
            System.out.println("Player "+playerOne.getName()+" has won");
        }
        else
        {
            System.out.println("Player "+playerTwo.getName()+" has won");
        }
    }
}
