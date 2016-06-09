package org.amc.game.chessserver

import static org.mockito.Mockito.*;

import java.security.Principal;

import org.amc.dao.SCGDAOInterface
import org.amc.game.chess.AbstractChessGame
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGamePlayer
import org.amc.game.chess.ChessPiece;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer
import org.amc.game.chess.Location;
import org.amc.game.chess.Player
import org.amc.game.chess.RealChessGamePlayer;
import org.junit.Before;
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate

import groovy.transform.TypeChecked;

@TypeChecked
class CG105PromotionControllerNoPlayerCheckingTest {
	PromotionStompController controller;
	
	@Mock
	SCGDAOInterface serverChesGameDAO;
	
	@Mock
	SimpMessagingTemplate template;
	
	@Mock
	Principal player;

	@Mock
	AbstractServerChessGame scg;
	
	@Mock
	ChessGame chessGame;
	
	final Long gameUid = 1L;
	
	final String message = 'promote qa8';
	
	Map<String, Object> wsSession;
	
	final String name = 'Ted';
	
	ChessGamePlayer blackPlayer;
	ChessGamePlayer whitePlayer;
	
	@Before
	void setUp() {
		MockitoAnnotations.initMocks(this);
		
		blackPlayer = new RealChessGamePlayer(new HumanPlayer(name), Colour.BLACK);
		whitePlayer = new RealChessGamePlayer(new HumanPlayer('Sarah'), Colour.WHITE);
		
		controller = new PromotionStompController();
		controller.serverChessDAO = serverChesGameDAO;
		controller.template = template;
		
		wsSession = new HashMap<>();
		
		
		when(player.getName()).thenReturn(name);
		when(serverChesGameDAO.getServerChessGame(eq(gameUid))).thenReturn(scg);
		when(scg.getChessGame()).thenReturn(chessGame);
		when(chessGame.getCurrentPlayer()).thenReturn(whitePlayer);
	}
	
	@Test
	void testPromotionByOtherPlayerNotAllowed() {
		String msgDestination = StompController.MESSAGE_USER_DESTINATION;
		wsSession.put('PLAYER', blackPlayer);
		
		controller.promotePawnTo(player, wsSession, gameUid, message);
		
		verify(scg, never()).promotePawnTo(any(ChessPiece.class), any(Location.class));
	}
	
	@Test
	void testPromotionByPlayerIsAllowed() {
		String msgDestination = StompController.MESSAGE_USER_DESTINATION;
		wsSession.put('PLAYER', whitePlayer);
		
		controller.promotePawnTo(player, wsSession, gameUid, message);
		
		verify(scg, times(1)).promotePawnTo(any(ChessPiece.class), any(Location.class));
	}
	
}
