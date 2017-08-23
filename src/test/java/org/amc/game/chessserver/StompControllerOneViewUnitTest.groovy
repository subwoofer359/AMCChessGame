package org.amc.game.chessserver;

import static org.mockito.Mockito.*;
import static org.amc.game.chessserver.StompController.MESSAGE_HEADER_TYPE;

import org.amc.DAOException;
import org.amc.dao.SCGDAOInterface
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

import java.security.Principal;
import java.util.Map;

import static org.junit.Assert.*;

class StompControllerOneViewUnitTest extends StompControllerFixture {

    GameMoveStompController controller;
	
	def chessGameFactory
	
	@Mock
	SCGDAOInterface sCGDAO;
    
    @Before
    void setUp() {
        super.setUp();
        
        controller = new GameMoveStompController();
        
		controller.setServerChessDAO(sCGDAO);
		
		controller.setTemplate(template);
		
		chessGameFactory = new ChessGameFactory() {
            @Override
            ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite,
                            ChessGamePlayer playerBlack) {
                return new ChessGame(board, playerWhite, playerBlack);
            }
        };
		
        scg = new OneViewServerChessGame(gameUUID, whitePlayer);
        scg.setChessGameFactory(chessGameFactory);

		when(sCGDAO.getServerChessGame(eq(gameUUID))).thenReturn(scg);
    }

    @Test
    void testMove() {
		String move = "A2-A3";
		scg.addOpponent(blackPlayer);
        
        controller.registerMove(principal, sessionAttributes, gameUUID, move);
        
		verifyZeroInteractions(template);
    }

    @Test
    void testInvalidMove() {
		String move = "A1-A3";
        scg.addOpponent(blackPlayer);
        
		controller.registerMove(principal, sessionAttributes, gameUUID, move);
        
		verifySimpMessagingTemplateCallToUser();
        assertEquals("Error:Not a valid move", payloadArgument.getValue());
        assertEquals(MessageType.ERROR, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
    }

    @Test
    void testChessGameNotInitialised() {
        String move = "A1-A3";
        
		controller.registerMove(principal, sessionAttributes, gameUUID, move);
        
		verifySimpMessagingTemplateCallToUser();
        assertEquals(String.format("Error:Move on game(%d) which hasn't got two players", gameUUID),
                        payloadArgument.getValue());
        assertEquals(MessageType.ERROR, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
    }

    @Test
    void testChessGameFinished() {
		String move = "A1-A3";
		scg.addOpponent(blackPlayer);
        scg.setCurrentStatus(AbstractServerChessGame.ServerGameStatus.FINISHED);
        
        controller.registerMove(principal, sessionAttributes, gameUUID, move);
        
		verifySimpMessagingTemplateCallToUser();
        assertEquals(String.format("Error:Move on game(%d) which has finished", gameUUID),
                        payloadArgument.getValue());
        assertEquals(MessageType.ERROR, headersArgument.getValue().get(MESSAGE_HEADER_TYPE));
    }
    
    /**
     * JIRA http://192.168.1.5:8081/browse/CG-49
     */
    @Test
    void testUnParsableMove()
    {
		String move = "-A3";
        
		scg.addOpponent(blackPlayer);
        
        controller.registerMove(principal, sessionAttributes, gameUUID, move);
    }
}
