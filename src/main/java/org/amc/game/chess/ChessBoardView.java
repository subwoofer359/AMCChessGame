package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.util.Observer;
import org.amc.util.Subject;


/**
 * Creates a simple console base view of the ChessBoard
 * @author Adrian Mclaughlin
 *
 */
public class ChessBoardView implements Observer{
    private ChessBoard chessBoard;
    final int WIDTH_OF_BOARD=ChessBoard.Coordinate.values().length;
    public ChessBoardView(ChessBoard chessBoard) {
        this.chessBoard=chessBoard;
        this.chessBoard.attachObserver(this);
    }
    
    @Override
    public void update(Subject subject, Object message) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * Prints the ChessBoard and position of the pieces on the screen
     */
    void displayTheBoard(){
        int row=8;
        printBoardHeader();
        while(row>0){
            int col=0;
            StringBuilder sb=new StringBuilder();
            while(col<8){
                sb.append('|');
                ChessPiece piece=chessBoard.getPieceFromBoardAt(col, row);
                if(piece==null){
                    sb.append("   ");
                }else{
                    sb.append(' ');
                    sb.append(piece.getClass().getSimpleName().charAt(0));
                    sb.append(' ');
                }
                col++;
            }
            sb.append('|');
            sb.append(row);
            sb.append('\n');
            System.out.print(sb.toString());
            printLine();
            row--;
        }
        //printBoardFooter();
    }
    
    private void printBoardHeader(){
        
        StringBuilder sb=getLetterHeader();
        
        sb.append(getLine());
        
        System.out.print(sb.toString());
    }
    
    private StringBuilder getLetterHeader(){
        StringBuilder sb=new StringBuilder();
        for(Coordinate coordinate:ChessBoard.Coordinate.values()){
            sb.append("  ");
            sb.append(coordinate.toString());
            sb.append(' ');
        }
        sb.append('\n');
        return sb;
    }

    private void printLine(){
        System.out.print(this.getLine().toString());
    }
    
    private StringBuilder getLine(){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<WIDTH_OF_BOARD;i++){
            sb.append("----");
        }
        sb.append('\n');
        return sb;
    }

    
}
