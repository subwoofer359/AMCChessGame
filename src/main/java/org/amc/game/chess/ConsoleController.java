package org.amc.game.chess;

import java.io.Console;
import java.text.ParseException;

public class ConsoleController implements Controller{

    private ChessBoard board;
    private Player currentPlayer;
    private Player playerOne;
    private Player playerTwo;
    private Console console=System.console();
    private InputParser parser=new SimpleInputParser();
    
    public ConsoleController(ChessBoard board,Player playerOne,Player playerTwo) {
        this.board=board;
        this.playerOne=playerOne;
        this.playerTwo=playerTwo;
        this.currentPlayer=this.playerOne;
    }
    
    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public void changePlayer() {
        if(currentPlayer.equals(playerOne)){
            currentPlayer=playerTwo;
        }else{
            currentPlayer=playerOne;
        }
    }

    @Override
    public InputParser getInputParser() {
        return this.parser;
    }
    
    public void takeTurn() throws InvalidMoveException{
        String input=console.readLine("Player(%s) move:",currentPlayer.getName());
        try{
            Move move=getInputParser().parseMoveString(input);
            board.move(currentPlayer, move);
        }catch(ParseException pe){
            throw new InvalidMoveException(pe);
        }
    }
    
    void setConsole(Console console){
        this.console=console;
    }

}
