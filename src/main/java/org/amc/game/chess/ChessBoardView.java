package org.amc.game.chess;

import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.util.Observer;
import org.amc.util.Subject;

import java.io.Console;


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
        displayTheBoard();
        
    }
    
    /**
     * Prints the ChessBoard and position of the pieces on the screen
     */
    void displayTheBoard(){
        StringBuilder sb=new StringBuilder();
        int row=8;
        sb.append(printBoardHeader());
        while(row>0){
            int col=0;
            while(col<8){
                sb.append(printSquare(col, row));
                col++;
            }
            sb.append(addRowEnd(row));
            
            sb.append(getLine());
            row--;
        }
        System.out.print(sb.toString());
        //printBoardFooter();
    }
    
    private StringBuilder printSquare(int col,int row){
        StringBuilder sb=new StringBuilder();
        sb.append('|');
        ChessPiece piece=chessBoard.getPieceFromBoardAt(col, row);
        if(piece==null){
            sb.append("   ");
        }else{
            sb.append(' ');
            sb.append(piece.getClass().getSimpleName().charAt(0));
            sb.append(' ');
        }
        return sb;
    }
    
    private StringBuilder addRowEnd(int row){
        StringBuilder sb=new StringBuilder();
        sb.append('|');
        sb.append(row);
        sb.append('\n');
        return sb;
    }
    
    private StringBuilder printBoardHeader(){
        
        StringBuilder sb=getLetterHeader();
        
        sb.append(getLine());
        
        return sb;
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
    
    private StringBuilder getLine(){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<WIDTH_OF_BOARD;i++){
            sb.append("----");
        }
        sb.append('\n');
        return sb;
    }

    public static void main(String[] args){
        Console console=System.console();
        if(console==null){
            System.err.println("A Console couldn't be retrieved");
            System.exit(1);
        }
        ChessBoard board=new ChessBoard();
        board.initialise();
        Player playerOne=new HumanPlayer("Stephen", Colour.WHITE);
        Player playerTwo=new HumanPlayer("Chris", Colour.BLACK);
        Player currentPlayer=playerOne;
        
        ChessBoardView view=new ChessBoardView(board);
        view.displayTheBoard();
        START:while(true){
            String input=console.readLine("Player(%s) move:",currentPlayer.getName());
            if(input.length()!=4){
                System.out.println("Move must be enter like so: a1b2");
            }else{
                String startString=input.substring(0, 2);
                String endString=input.substring(2,4);
                System.out.println(startString);
                System.out.println(endString);
                
                Location startSquare=new Location(Coordinate.valueOf(String.valueOf(startString.charAt(0))),
                                Integer.parseInt(String.valueOf(startString.charAt(1))));
                Location endSquare=new Location(Coordinate.valueOf(String.valueOf(endString.charAt(0))),
                                Integer.parseInt(String.valueOf(endString.charAt(1))));
                
                try {
                    board.move(currentPlayer, new Move(startSquare,endSquare));
                } catch (InvalidMoveException e) {
                    System.out.println(e.getMessage());
                    continue START;
                }
            }
            
            if(currentPlayer.equals(playerOne)){
                currentPlayer=playerTwo;
            }else{
                currentPlayer=playerOne;
            }
        }
    }
    
}
