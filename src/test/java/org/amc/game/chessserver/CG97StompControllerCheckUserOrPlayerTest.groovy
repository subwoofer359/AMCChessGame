package org.amc.game.chessserver


import static org.mockito.Mockito.*;
import java.security.Principal;

import org.amc.dao.SCGDAOInterface
import org.amc.game.chess.AbstractChessGame.GameState
import org.amc.game.chess.ChessGame;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.amc.game.chess.ChessGamePlayer
import org.amc.game.chess.Colour
import org.amc.game.chess.HumanPlayer
import org.amc.game.chess.RealChessGamePlayer;
import org.junit.Before
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations
import org.springframework.messaging.simp.SimpMessagingTemplate;;;

class CG97StompControllerCheckUserOrPlayerTest {

	GameActionsStompController gasController;
	
	final String PLAYER_ATTR = 'PLAYER';
	
	final String WHITE_PLAYER_NAME = 'sarah';
	
	final def GAME_UID = 124L;
	
	@Mock
	SCGDAOInterface serverChessGameDAO;
	
	@Mock
	SimpMessagingTemplate template;
	
	@Mock
	AbstractServerChessGame scg;
	
	
	Map<String, Object> wsSession;
	
	ChessGamePlayer whitePlayer = new RealChessGamePlayer(new HumanPlayer(WHITE_PLAYER_NAME), Colour.WHITE);
	ChessGamePlayer blackPlayer = new RealChessGamePlayer(new HumanPlayer('Chris'), Colour.BLACK);
	ChessGamePlayer otherPlayer = new RealChessGamePlayer(new HumanPlayer('Evil Doer'), Colour.WHITE);
	
	@Mock
	Principal user;
	
	@Before
	void setUp() {
		MockitoAnnotations.initMocks(this);
		gasController = new GameActionsStompController();
		gasController.serverChessDAO = serverChessGameDAO;
		gasController.template = template;
		
		wsSession = new HashMap<>();
		when(user.getName()).thenReturn(WHITE_PLAYER_NAME);
		when(serverChessGameDAO.getServerChessGame(anyLong())).thenReturn(scg);
		
		when(scg.getCurrentStatus()).thenReturn(ServerGameStatus.IN_PROGRESS);
		when(scg.getPlayer()).thenReturn(whitePlayer);
		when(scg.getOpponent()).thenReturn(blackPlayer);
	}
	
	@Test
	void testNonGameUserQuitingGame() {
		wsSession.put(PLAYER_ATTR, otherPlayer);
		
		String message = '';
		
		gasController.quitChessGame(user, wsSession, GAME_UID, message);
		
		verify(scg, never()).setCurrentStatus(ServerGameStatus.FINISHED);
	}
	
	@Test
	void testWhitePlayerQuitingGame() {
		wsSession.put(PLAYER_ATTR, whitePlayer);
		
		String message = '';
		
		gasController.quitChessGame(user, wsSession, GAME_UID, message);
		
		verify(scg, times(1)).setCurrentStatus(ServerGameStatus.FINISHED);
	}
	
	@Test
	void testBlackPlayerQuitingGame() {
		wsSession.put(PLAYER_ATTR, blackPlayer);
		
		String message = '';
		
		gasController.quitChessGame(user, wsSession, GAME_UID, message);
		
		verify(scg, times(1)).setCurrentStatus(ServerGameStatus.FINISHED);
	}
}
