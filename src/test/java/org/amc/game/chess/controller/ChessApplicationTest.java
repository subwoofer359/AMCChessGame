package org.amc.game.chess.controller;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.amc.game.ChessApplication;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chess.controller.ConsoleController;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Todo Redesign this test 
 */
@Ignore
public class ChessApplicationTest {

    private Player whitePlayer;
    private Player blackPlayer;
    private ChessBoard board;
    private ChessApplication game;
    private ChessGame chessGame;
    
    @Before
    public void setUp() throws Exception {
        whitePlayer=new HumanPlayer("Teddy", Colour.WHITE);
        blackPlayer=new HumanPlayer("Robin", Colour.BLACK);        
        board=new ChessBoard();
        game=new ChessApplication(whitePlayer,blackPlayer);
        chessGame=spy(new ChessGame(board, whitePlayer, blackPlayer));
        //doReturn(false).when(chessGame).isPlayersKingInCheck(any(HumanPlayer.class), any(ChessBoard.class));
        game.setChessGame(chessGame);
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    public static class MockUserInput implements ConsoleController.UserConsole{

        private String[] output={};
        private int counter=0;
        
        public MockUserInput(String[] desiredOutput) {
            this.output=desiredOutput;
        }
        
        @Override
        public String readLine(String fmt, Object... args) {
            return getNextOutput();
        }

        @Override
        public String readLine() {
            return getNextOutput();
        }
        
        private String getNextOutput(){
            if(counter<output.length){
                return output[counter++];
            }
            else
            {
                return null;
            }
        }      
        
    }
    
}
