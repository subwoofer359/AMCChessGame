package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.GameObserver;
import org.amc.game.GameSubject;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.messaging.OfflineChessGameMessager;
import org.amc.game.chessserver.observers.ObserverFactoryChainFixture;
import org.amc.util.Observer;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

public class ServerChessGameFactoryTest {

	private ServerChessGameFactory scgfactory;
	private static final long GAME_UID = 1233L;
	private ChessGamePlayer whitePlayer;

	@Before
	public void setUp() throws Exception {
		scgfactory = new ServerChessGameFactory();
		whitePlayer = new RealChessGamePlayer(new HumanPlayer("Ted"), Colour.WHITE);
		scgfactory.setObserverFactoryChain(ObserverFactoryChainFixture.getUpObserverFactoryChain());

	}

	@Test
	public void testCreateNetworkGame() throws Exception {
		ServerChessGame scgGame = scgfactory.getServerChessGame(GameType.NETWORK_GAME, GAME_UID, whitePlayer);

		assertTrue(scgGame instanceof ServerChessGame);
		assertFalse(scgGame instanceof OneViewServerChessGame);
		assertTrue("OfflineChessGameMessager should have been added",
				hasObserverBeenAdded(scgGame, OfflineChessGameMessager.class));

		int expectedNoOfObservers = ServerChessGameFactory.ObserversConfig.NETWORK_OBSERVERS.getNumberOfObservers();
		assertEquals("Wrong number of Observers", expectedNoOfObservers, scgGame.getNoOfObservers());
	}

	@Test
	public void testCreateLocalGame() throws Exception {
		ServerChessGame scgGame = scgfactory.getServerChessGame(GameType.LOCAL_GAME, GAME_UID, whitePlayer);
		assertTrue(scgGame instanceof ServerChessGame);
		assertTrue(scgGame instanceof OneViewServerChessGame);
		assertFalse("Should be no OfflineChessGameMessager added",
				hasObserverBeenAdded(scgGame, OfflineChessGameMessager.class));
		int expectedNoOfObservers = ServerChessGameFactory.ObserversConfig.LOCAL_OBSERVERS.getNumberOfObservers();
		assertEquals("Wrong number of Observers", expectedNoOfObservers, scgGame.getNoOfObservers());
	}

	private boolean hasObserverBeenAdded(ServerChessGame scgGame, Class<? extends GameObserver> clazz)
			throws Exception {
		Field observerField = GameSubject.class.getDeclaredField("observers");
		observerField.setAccessible(true);

		@SuppressWarnings("unchecked")
		List<Observer> list = (List<Observer>) observerField.get(scgGame);

		for (Observer obs : list) {
			if (clazz.equals(obs.getClass())) {
				return true;
			}
		}
		return false;
	}
}
