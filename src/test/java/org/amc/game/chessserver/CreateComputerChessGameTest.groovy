package org.amc.game.chessserver

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.DAOException
import org.amc.dao.ServerChessGameDAO
import org.amc.game.chess.ChessGameFixture
import org.amc.game.chess.ChessGamePlayer
import org.amc.game.chess.ComparePlayers
import org.amc.game.chessserver.ServerChessGameFactory.GameType
import org.amc.game.chessserver.observers.ObserverFactoryChainFixture;
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.ui.ExtendedModelMap
import org.springframework.ui.Model

import groovy.transform.TypeChecked

@TypeChecked
class CreateComputerChessGameTest {

	private Model model;
	
	private NewComputerGameController controller;
	
	private final ChessGameFixture fixture = new ChessGameFixture();
	
	@Mock
	private ServerChessGameDAO sCGDAO;
	
	private ChessGamePlayer player;
	
	private ArgumentCaptor<AbstractServerChessGame> gameCaptor = ArgumentCaptor
	.forClass(AbstractServerChessGame.class);
	
	@Before
	void setUp() {
		MockitoAnnotations.initMocks(this);
		model = new ExtendedModelMap();
		ServerChessGameFactory scgFactory = new ServerChessGameFactory();
		scgFactory.setObserverFactoryChain(ObserverFactoryChainFixture.getUpObserverFactoryChain());
		controller = new NewComputerGameController();
		controller.setServerChessGameFactory(scgFactory);
		controller.setServerChessGameDAO(sCGDAO);
		player = fixture.whitePlayer;
	}

	@Test
    public void createComputerChessGameBlack() throws DAOException {
        assertSessionAttributeNull();
        String viewName = controller.createComputerBlackGame(model, player, GameType.COMPUTER_BLACK_GAME);
        assertPlayerIsAddedAsWhitePlayer();

        assertLongStoreInSessionAttribute();
        assertEquals(GameControllerHelper.CHESSGAME_PORTAL, viewName);
        assertNotNull(model.asMap().get(ServerConstants.GAME));
        assertNotNull(model.asMap().get(ServerConstants.CHESSPLAYER));
        assertTrue("Should be computer Server Chess game", 
        		model.asMap().get(ServerConstants.GAME) instanceof ComputerServerChessGame);
    }

	private void assertSessionAttributeNull() {
		assertNull(model.asMap().get(ServerConstants.GAME_UUID.toString()));
	}
	
	private void assertLongStoreInSessionAttribute() {
		assertEquals(model.asMap().get(ServerConstants.GAME_UUID.toString()).getClass(), Long.class);
	}
	
	private void assertPlayerIsAddedAsWhitePlayer() throws DAOException {
		verify(sCGDAO, times(1)).saveServerChessGame(gameCaptor.capture());
		AbstractServerChessGame game = gameCaptor.getValue();
		assertTrue(ComparePlayers.isSamePlayer(game.getPlayer(), this.player));
	}
	
	private void assertPlayerIsAddedAsBlackPlayer() throws DAOException {
		verify(sCGDAO, times(1)).saveServerChessGame(gameCaptor.capture());
		AbstractServerChessGame game = gameCaptor.getValue();
		assertTrue(ComparePlayers.isSamePlayer(game.getOpponent(), this.player));
	}
	
	@Test
	public void createComputerChessGameWhite() throws DAOException {
		assertSessionAttributeNull();
		String viewName = controller.createComputerWhiteGame(model, player, GameType.COMPUTER_WHITE_GAME);
		assertPlayerIsAddedAsBlackPlayer();

		assertLongStoreInSessionAttribute();
		assertEquals(GameControllerHelper.CHESSGAME_PORTAL, viewName);
		assertNotNull(model.asMap().get(ServerConstants.GAME));
		assertNotNull(model.asMap().get(ServerConstants.CHESSPLAYER));
		assertTrue("Should be computer Server Chess game",
				model.asMap().get(ServerConstants.GAME) instanceof ComputerServerChessGame);
	}
}
