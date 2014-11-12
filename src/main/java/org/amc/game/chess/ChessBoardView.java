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
            sb.append(getChessPieceMapping(piece));
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
        ChessBoard board=new ChessBoard();
        board.initialise();
        Player playerOne=new HumanPlayer("Stephen", Colour.WHITE);
        Player playerTwo=new HumanPlayer("Chris", Colour.BLACK);
        ConsoleController controller=new ConsoleController(board, playerOne, playerTwo);
        ChessBoardView view=new ChessBoardView(board);
        ChessGame game=new ChessGame(playerOne, playerTwo);
        game.setBoard(board);
        game.setController(controller);
        game.setView(view);
        
        game.start();
       
        
    }
    
    private Character getChessPieceMapping(ChessPiece piece) {
        Character[] whiteSymbols = { 'K', 'Q', 'B', 'k', 'R', 'p' };
        Character[] blackSymbols = { '\u0136', '\u0150', '\u00DF', '\u0138', '\u0158', '\u03F8' };
        int index = -1;

        if (piece.getClass().equals(KingPiece.class)) {
            index = 0;
        }else if (piece.getClass().equals(QueenPiece.class)){
            index=1;
        } else if (piece.getClass().equals(BishopPiece.class)) {
            index = 2;
        } else if (piece.getClass().equals(KnightPiece.class)) {
            index = 3;
        } else if (piece.getClass().equals(RookPiece.class)) {
            index = 4;
        } else if (piece.getClass().equals(PawnPiece.class)) {
            index = 5;
        }

        if (piece.getColour().equals(Colour.BLACK)) {
            return blackSymbols[index];
        } else {
            return whiteSymbols[index];
        }
    }
}
