package org.amc.game.chessserver

import static org.junit.Assert.*;
import static org.mockito.Matchers.any
import static org.mockito.Mockito.*;

import org.amc.DAOException
import org.amc.dao.ServerChessGameDAO
import org.amc.game.chess.ChessGameFixture
import org.amc.game.chess.ChessGamePlayer
import org.amc.game.chess.ComparePlayers
import org.amc.game.chessserver.ServerChessGameFactory.GameType
import org.amc.game.chessserver.observers.ObserverFactoryChainFixture
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.ui.ExtendedModelMap
import org.springframework.ui.Model

import groovy.transform.TypeChecked

@TypeChecked
class CreateLocalChessGameTest {
	
	private static final String OPPONENT = "Kate Bush";
	
	private Model model;
	
	private NewLocalChessGameController controller;
	
	private final ChessGameFixture fixture = new ChessGameFixture();
	
	@Mock
	private ServerChessGameDAO sCGDAO;
	
	private ChessGamePlayer player;
	
	private GameControllerTestHelper helper;

	@Before
	void setUp() {
		MockitoAnnotations.initMocks(this);
		model = new ExtendedModelMap();
		ServerChessGameFactory scgFactory = new ServerChessGameFactory();
		scgFactory.setObserverFactoryChain(ObserverFactoryChainFixture.getUpObserverFactoryChain());
		controller = new NewLocalChessGameController();
		controller.setServerChessGameFactory(scgFactory);
		controller.setServerChessGameDAO(sCGDAO);
		player = fixture.whitePlayer;
		
		helper = new GameControllerTestHelper(model, sCGDAO);
	}
	
	@Test
	void createLocalChessGameTest() {
		helper.assertSessionAttributeNull();
		
		String viewName = controller.createGame(model, player, GameType.LOCAL_GAME, OPPONENT);
		
		helper.assertPlayerIsAddedToChessGame(player);
		helper.assertLongStoreInSessionAttribute();
		assertEquals(GameControllerHelper.CHESSGAME_PORTAL, viewName);
		assertNotNull(model.asMap().get(ServerConstants.GAME));
		assertNotNull(model.asMap().get(ServerConstants.CHESSPLAYER));
	}
	
	@Test
	public void testPlayersNameIsEmptyString() throws DAOException {
		String invalidPlayersName = "";
		helper.assertSessionAttributeNull();
		String viewName = controller.createGame(model, player, GameType.LOCAL_GAME,
						invalidPlayersName);

		verify(sCGDAO, never()).saveServerChessGame(any(AbstractServerChessGame.class));
		
		assertEquals(GameControllerHelper.TWOVIEW_FORWARD_PAGE, viewName);
		assertEquals(invalidPlayersName, model.asMap().get(NewLocalChessGameController.PLAYERS_NAME_FIELD));

	}

	@Test
	public void testPlayersNameIsNull() throws DAOException {
		String invalidPlayersName = null;
		helper.assertSessionAttributeNull();
		String viewName = controller.createGame(model, player, GameType.LOCAL_GAME,
						invalidPlayersName);
		verify(sCGDAO, never()).saveServerChessGame(any(AbstractServerChessGame.class));
		
		assertEquals(GameControllerHelper.TWOVIEW_FORWARD_PAGE, viewName);
		assertEquals(invalidPlayersName, model.asMap().get(NewLocalChessGameController.PLAYERS_NAME_FIELD));

	}
}
