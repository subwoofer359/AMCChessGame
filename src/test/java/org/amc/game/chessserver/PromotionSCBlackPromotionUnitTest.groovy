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
import org.springframework.messaging.handler.annotation.support.PayloadArgumentResolver
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

class PromotionSCBlackPromotionUnitTest extends StompControllerFixture {

    PromotionStompController controller;
	
	AbstractServerChessGame oneViewSCG;
	
	long oneViewUUID = 234L;
	
	private static final def message = 'promote Qa1';
    
    @Before
    void setUp() {
        super.setUp();
		
		oneViewSCG = new OneViewServerChessGame(gameUUID, whitePlayer);
		
		when(serverChessGameDAO.getServerChessGame(eq(oneViewUUID))).thenReturn(oneViewSCG);
		
		sessionAttributes.put('PLAYER', whitePlayer);
		
        controller = new PromotionStompController();
        controller.setTemplate(template);
        controller.setServerChessDAO(serverChessGameDAO);
        scg.addOpponent(blackPlayer);
        
        def chessBoardFactory = new ChessBoardFactoryImpl(new SimpleChessBoardSetupNotation());
        def board  = chessBoardFactory.getChessBoard("Ke8:ke1:pa7:Pa2");
        def factory = new StandardChessGameFactory();
        def chessGame = factory.getChessGame(board, whitePlayer, blackPlayer);
		
		oneViewSCG.setChessGameFactory(new ChessGameFactory() {
			@Override
			public ChessGame getChessGame(ChessBoard aboard, ChessGamePlayer playerWhite,
							ChessGamePlayer playerBlack) {
				return chessGame;
			}
		});
		
        scg.setChessGame(chessGame);
		chessGame.changePlayer();
        scg.move(blackPlayer, new Move("a2-a1"));
		
		oneViewSCG.addOpponent(blackPlayer);
    }

    @Test
    void testPromotion() {		       
        controller.promotePawnTo(principal, sessionAttributes, oneViewUUID, message);
        
		//Not Sent 
		verifyZeroInteractions(template);
    }
	
	@Test
	void testOneViewSCGMoveNotInPromotionState() {
		oneViewSCG.setChessGame(null);
		
		controller.promotePawnTo(principal, sessionAttributes, oneViewUUID, message);
		
		verifySimpMessagingTemplateCallToUser();
		
		def returnMessage = payloadArgument.value;
		
		assert returnMessage == PawnPromotionRule.ERROR_CAN_ONLY_PROMOTE_PAWN;
	}
}
