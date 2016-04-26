package org.amc.game.chessserver;


import static org.mockito.Mockito.*;
import static org.amc.game.chessserver.StompController.MESSAGE_HEADER_TYPE;

import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.RealChessGamePlayer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import groovy.transform.CompileStatic;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

class GameMoveStompControllerUnitTest extends StompControllerFixture {

    private GameMoveStompController controller;

    

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.controller = new GameMoveStompController();
        this.controller.setTemplate(template);
        controller.setServerChessDAO(serverChessGameDAO);
    }

    @Test
    public void testMove() {
        scg.addOpponent(blackPlayer);
        String move = "A2-A3";
        sessionAttributes.put("PLAYER", whitePlayer);
        controller.registerMove(principal, sessionAttributes, gameUUID, move);
        verifySimpMessagingTemplateCallToUser();
        assertEquals("", payoadArgument.getValue());
        assertEquals(MessageType.INFO, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
    }

    @Test
    public void testInvalidMove() {
        scg.addOpponent(blackPlayer);
        sessionAttributes.put("PLAYER", whitePlayer);
        String move = "A1-A3";
        controller.registerMove(principal, sessionAttributes, gameUUID, move);
        verifySimpMessagingTemplateCallToUser();
        assertEquals("Error:Not a valid move", payoadArgument.getValue());
        assertEquals(MessageType.ERROR, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
    }

    @Test
    public void testNotPlayersMove() {
        scg.addOpponent(blackPlayer);
        sessionAttributes.put("PLAYER", blackPlayer);
        String move = "A1-A3";
        controller.registerMove(principal, sessionAttributes, gameUUID, move);
        verifySimpMessagingTemplateCallToUser();
        assertEquals("Error:Not Player's turn", payoadArgument.getValue());
        assertEquals(MessageType.ERROR, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
    }

    @Test
    public void testChessGameNotInitialised() {
        String move = "A1-A3";
        sessionAttributes.put("PLAYER", whitePlayer);
        controller.registerMove(principal, sessionAttributes, gameUUID, move);
        verifySimpMessagingTemplateCallToUser();
        assertEquals(String.format("Error:Move on game(%d) which hasn't got two players", gameUUID),
                        payoadArgument.getValue());
        assertEquals(MessageType.ERROR, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
    }

    @Test
    public void testChessGameFinished() {
        scg.addOpponent(blackPlayer);
        scg.setCurrentStatus(AbstractServerChessGame.ServerGameStatus.FINISHED);
        String move = "A1-A3";
        controller.registerMove(principal, sessionAttributes, gameUUID, move);
        verifySimpMessagingTemplateCallToUser();
        assertEquals(String.format("Error:Move on game(%d) which has finished", gameUUID),
                        payoadArgument.getValue());
        assertEquals(MessageType.ERROR, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
    }
    
    /**
     * JIRA http://192.168.1.5:8081/browse/CG-49
     */
    @Test
    public void testUnParsableMove()
    {
        scg.addOpponent(blackPlayer);
        sessionAttributes.put("PLAYER", whitePlayer);
        String move = "-A3";
        controller.registerMove(principal, sessionAttributes, gameUUID, move);
    }
}
