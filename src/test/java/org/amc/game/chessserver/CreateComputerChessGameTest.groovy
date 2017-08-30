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
	
	private GameControllerTestHelper helper;
	
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
		
		helper = new GameControllerTestHelper(model, sCGDAO);
	}

	@Test
    public void createComputerChessGameBlack() throws DAOException {
        helper.assertSessionAttributeNull();
        String viewName = controller.createComputerBlackGame(model, player, GameType.COMPUTER_BLACK_GAME);
        helper.assertPlayerIsAddedAsWhitePlayer(player);

        helper.assertLongStoreInSessionAttribute();
        assertEquals(GameControllerHelper.CHESSGAME_PORTAL, viewName);
        assertNotNull(model.asMap().get(ServerConstants.GAME));
        assertNotNull(model.asMap().get(ServerConstants.CHESSPLAYER));
        assertTrue("Should be computer Server Chess game", 
        		model.asMap().get(ServerConstants.GAME) instanceof ComputerServerChessGame);
    }
	

	
	@Test
	public void createComputerChessGameWhite() throws DAOException {
		helper.assertSessionAttributeNull();
		String viewName = controller.createComputerWhiteGame(model, player, GameType.COMPUTER_WHITE_GAME);
		helper.assertPlayerIsAddedAsBlackPlayer(player);

		helper.assertLongStoreInSessionAttribute();
		assertEquals(GameControllerHelper.CHESSGAME_PORTAL, viewName);
		assertNotNull(model.asMap().get(ServerConstants.GAME));
		assertNotNull(model.asMap().get(ServerConstants.CHESSPLAYER));
		assertTrue("Should be computer Server Chess game",
				model.asMap().get(ServerConstants.GAME) instanceof ComputerServerChessGame);
	}
}
