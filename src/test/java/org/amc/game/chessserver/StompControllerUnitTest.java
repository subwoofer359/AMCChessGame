package org.amc.game.chessserver;

import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.ChessBoard.Coordinate;
import org.junit.Before;
import org.junit.Test;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class StompControllerUnitTest {

    private StompController controller;
    private Player whitePlayer=new HumanPlayer("Stephen", Colour.WHITE);
    private Player blackPlayer=new HumanPlayer("Chris", Colour.BLACK);
    private long gameUUID=1234L;
    private ServerChessGame scg;
    private Map<String,Object> sessionAttributes;
    private Principal principal=new Principal() {
        
        @Override
        public String getName() {
            return "Stephen";
        }
    };
    @Before
    
    public void setUp(){
        this.controller=new StompController();
        scg=new ServerChessGame(whitePlayer);
        Map<Long,ServerChessGame> gameMap=new HashMap<Long, ServerChessGame>();
        gameMap.put(gameUUID, scg);
        controller.setGameMap(gameMap);
        sessionAttributes=new HashMap<String, Object>();
    }
  
    @Test
    public void testMove(){
        scg.addOpponent(blackPlayer);
        String move="A2-A3";
        sessionAttributes.put("PLAYER", whitePlayer);
        String result=controller.registerMove(principal, sessionAttributes, gameUUID, move);
        assertEquals("",result);
    }
    
   @Test
   public void testInvalidMove(){
       scg.addOpponent(blackPlayer);
       sessionAttributes.put("PLAYER", whitePlayer);
       String move="A1-A3";
       String result=controller.registerMove(principal, sessionAttributes, gameUUID, move);
       assertEquals("Error:Not a valid move",result);
   }
   
   @Test
   public void testNotPlayersMove(){
       scg.addOpponent(blackPlayer);
       sessionAttributes.put("PLAYER", blackPlayer);
       String move="A1-A3";
       String result=controller.registerMove(principal, sessionAttributes, gameUUID, move);
       assertEquals("Error:Not Player's turn",result);
   }
   
   @Test
   public void testChessGameNotInitialised(){
       String move="A1-A3";
       sessionAttributes.put("PLAYER", whitePlayer);
       String result=controller.registerMove(principal, sessionAttributes, gameUUID, move);
       assertEquals(String.format("Error:Move on game(%d) which hasn't got two players",gameUUID),result);
   }
   
   @Test
   public void testChessGameFinished(){
       scg.addOpponent(blackPlayer);
       scg.setCurrentStatus(ServerChessGame.status.FINISHED);
       String move="A1-A3";
       String result=controller.registerMove(principal, sessionAttributes, gameUUID, move);
       assertEquals(String.format("Error:Move on game(%d) which has finished",gameUUID),result);
   }

}
