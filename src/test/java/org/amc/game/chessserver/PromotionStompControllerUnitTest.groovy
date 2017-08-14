package org.amc.game.chessserver;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoardFactoryImpl;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Move
import org.amc.game.chess.PawnPromotionRule;
import org.amc.game.chess.SimpleChessBoardSetupNotation;
import org.amc.game.chess.StandardChessGameFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

class PromotionStompControllerUnitTest extends StompControllerFixture {

    PromotionStompController controller;
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
		
		sessionAttributes.put('PLAYER', whitePlayer);
		
        controller = new PromotionStompController();
        controller.setTemplate(template);
        controller.setServerChessDAO(serverChessGameDAO);
        scg.addOpponent(blackPlayer);
        
        def chessBoardFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        def board  = chessBoardFactory.getChessBoard("Ke8:ke1:pa7:pa6");
        def factory = new StandardChessGameFactory();
        def chessGame = factory.getChessGame(board, whitePlayer, blackPlayer);
        scg.setChessGame(chessGame);
        scg.move(whitePlayer, new Move("a7-a8")); 
    }

    @Test
    public void test() {
        def message = 'promote qa8';
		       
        controller.promotePawnTo(principal, sessionAttributes, gameUUID, message);
        verifyZeroInteractions(template);
    }
    
    @Test
    public void testNoPromoteHeaderToString() {
        def message = 'prom qa8';
        
        controller.promotePawnTo(principal, sessionAttributes, gameUUID, message);
        this.verifySimpMessagingTemplateCallToUser();
        assert headersArgument.value[StompController.MESSAGE_HEADER_TYPE] == MessageType.ERROR;
		assert this.payloadArgument.getValue() == PromotionStompController.PARSE_ERROR;
    }
    
    @Test
    public void testPromotionOfNonEligiblePiece() {
        def message = 'promote qe1';
        controller.promotePawnTo(principal, sessionAttributes, gameUUID, message);
        this.verifySimpMessagingTemplateCallToUser();
        assert headersArgument.value[StompController.MESSAGE_HEADER_TYPE] == MessageType.ERROR;
		assert this.payloadArgument.getValue() == PawnPromotionRule.ERROR_CAN_ONLY_PROMOTE_PAWN;
    }
	
	@Test
	public void testPromotionOfNonEligiblePawn() {
		def message = 'promote qa6';
		controller.promotePawnTo(principal, sessionAttributes, gameUUID, message);
		this.verifySimpMessagingTemplateCallToUser();
		assert headersArgument.value[StompController.MESSAGE_HEADER_TYPE] == MessageType.ERROR;
		assert this.payloadArgument.getValue() == PawnPromotionRule.ERROR_PAWN_CANT_BE_PROMOTED;
	}
}
