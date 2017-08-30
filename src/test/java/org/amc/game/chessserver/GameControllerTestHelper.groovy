package org.amc.game.chessserver

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertTrue
import static org.mockito.Matchers.any
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify

import org.amc.DAOException
import org.amc.dao.ServerChessGameDAO
import org.amc.game.chess.ComparePlayers
import org.amc.game.chess.Player
import org.mockito.ArgumentCaptor
import org.springframework.ui.Model

/**
 * Contains methods used by test cases for create chess game controllers
 * @author Adrian Mclaughlin
 *
 */
class GameControllerTestHelper {

	ServerChessGameDAO sCGDAO;

	Model model;

	ArgumentCaptor<AbstractServerChessGame> gameCaptor = ArgumentCaptor
	.forClass(AbstractServerChessGame.class);


	GameControllerTestHelper(Model model, ServerChessGameDAO sCGDAO) {
		this.sCGDAO = sCGDAO;
		this.model = model;
	}


	void assertGameIsSavedInDatabase() throws DAOException {
		verify(sCGDAO, times(1)).saveServerChessGame(any(AbstractServerChessGame.class));
	}

	void assertPlayerIsAddedToChessGame(Player player) throws DAOException {
		verify(sCGDAO, times(1)).saveServerChessGame(gameCaptor.capture());
		AbstractServerChessGame game = gameCaptor.getValue();
		assertTrue(ComparePlayers.isSamePlayer(game.getPlayer(), player));
	}

	private void assertPlayerIsAddedAsWhitePlayer(Player player) throws DAOException {
		verify(sCGDAO, times(1)).saveServerChessGame(gameCaptor.capture());
		AbstractServerChessGame game = gameCaptor.getValue();
		assertTrue(ComparePlayers.isSamePlayer(game.getPlayer(), player));
	}

	private void assertPlayerIsAddedAsBlackPlayer(Player player) throws DAOException {
		verify(sCGDAO, times(1)).saveServerChessGame(gameCaptor.capture());
		AbstractServerChessGame game = gameCaptor.getValue();
		assertTrue(ComparePlayers.isSamePlayer(game.getOpponent(), player));
	}

	void assertSessionAttributeNull() {
		assertNull(model.asMap().get(ServerConstants.GAME_UUID.toString()));
	}

	void assertLongStoreInSessionAttribute() {
		assertEquals(model.asMap().get(ServerConstants.GAME_UUID.toString()).getClass(), Long.class);
	}
}
