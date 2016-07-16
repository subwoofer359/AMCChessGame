package org.amc.game.chessserver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.amc.game.chessserver.StompController.MESSAGE_HEADER_TYPE;

import org.junit.Before;
import org.junit.Test;

import groovy.transform.CompileStatic;

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
        verifyZeroInteractions(template);
        //verifySimpMessagingTemplateCallToUser();
        //assertEquals("", payoadArgument.getValue());
        //assertEquals(MessageType.INFO, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
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
