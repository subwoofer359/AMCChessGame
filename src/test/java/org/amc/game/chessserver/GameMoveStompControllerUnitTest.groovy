package org.amc.game.chessserver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.amc.game.chessserver.StompController.MESSAGE_HEADER_TYPE;

import org.junit.Before;
import org.junit.Test;

class GameMoveStompControllerUnitTest extends StompControllerFixture {

	private GameMoveStompController controller;

    private static final String moveStr = 'A1-A3';
	
	private static final String SESSION_VAR = 'PLAYER';

    @Before
    void setUp() {
        super.setUp();
        controller = new GameMoveStompController();
        controller.template = template;
        controller.serverChessDAO = serverChessGameDAO;
    }

    @Test
    void testMove() {
        scg.addOpponent(blackPlayer);
        String move = 'A2-A3';
        addWhitePlayerSessionAttr();
        
        controller.registerMove(principal, sessionAttributes, gameUUID, move);
        
        verifyZeroInteractions(template);
    }

    private void addWhitePlayerSessionAttr() {
        sessionAttributes.put(SESSION_VAR, whitePlayer);
    }

    @Test
    void testInvalidMove() {
        scg.addOpponent(blackPlayer);
        addWhitePlayerSessionAttr();
        
        controller.registerMove(principal, sessionAttributes, gameUUID, moveStr);
        
        verifySimpMessagingTemplateCallToUser();
        assertEquals('Error:Not a valid move', payloadArgument.value);
        assertEquals(MessageType.ERROR, headersArgument.value.get(MESSAGE_HEADER_TYPE));
    }

    @Test
    void testNotPlayersMove() {
        scg.addOpponent(blackPlayer);
        sessionAttributes.put(SESSION_VAR, blackPlayer);

        controller.registerMove(principal, sessionAttributes, gameUUID, moveStr);
        
        verifySimpMessagingTemplateCallToUser();
        assertEquals('Error:Not Player\'s turn', payloadArgument.value);
        assertEquals(MessageType.ERROR, headersArgument.value.get(MESSAGE_HEADER_TYPE));
    }

    @Test
    void testChessGameNotInitialised() {
        addWhitePlayerSessionAttr();
        
        controller.registerMove(principal, sessionAttributes, gameUUID, moveStr);

        verifySimpMessagingTemplateCallToUser();
        assertEquals(String.format('Error:Move on game(%d) which hasn\'t got two players', gameUUID),
                        payloadArgument.getValue());
        assertEquals(MessageType.ERROR, headersArgument.value.get(MESSAGE_HEADER_TYPE));
    }

    @Test
    void testChessGameFinished() {
        scg.addOpponent(blackPlayer);
        scg.setCurrentStatus(AbstractServerChessGame.ServerGameStatus.FINISHED);
        
        controller.registerMove(principal, sessionAttributes, gameUUID, moveStr);
        
        verifySimpMessagingTemplateCallToUser();
        assertEquals(String.format('Error:Move on game(%d) which has finished', gameUUID),
                        payloadArgument.getValue());
        assertEquals(MessageType.ERROR, headersArgument.value.get(MESSAGE_HEADER_TYPE));
    }
    
    /**
     * JIRA http://192.168.1.5:8081/browse/CG-49
     */
    @Test
    void testUnParsableMove() {
        scg.addOpponent(blackPlayer);
        addWhitePlayerSessionAttr();
        String move = '-A3';
        
        controller.registerMove(principal, sessionAttributes, gameUUID, move);
    }
}
