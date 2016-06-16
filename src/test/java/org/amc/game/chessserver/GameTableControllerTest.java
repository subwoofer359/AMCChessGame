package org.amc.game.chessserver;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.amc.dao.ChessGameInfo;
import org.amc.dao.SCGDAOInterface;
import org.amc.game.chess.ChessGameFixture;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.observers.ObserverFactoryChain;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GameTableControllerTest {

	private GameTableController gtController;
	private AbstractServerChessGame scgGame;
    private ChessGameInfo scgGameInfo;
    private ChessGameFixture cgFixture;
	private SCGDAOInterface serverChessGameDAO;
	
	private static final long GAME_UID = 1234L;
	
	
	@Before
	public void setUp() throws Exception {
		gtController = new GameTableController();
		serverChessGameDAO = mock(SCGDAOInterface.class);
		ServerChessGameFactory scgFactory = new ServerChessGameFactory();
		ObserverFactoryChain chain = mock(ObserverFactoryChain.class);
		scgFactory.setObserverFactoryChain(chain);
		cgFixture = new ChessGameFixture();
		scgGame = scgFactory.getServerChessGame(GameType.LOCAL_GAME, GAME_UID, cgFixture.getWhitePlayer());
        scgGameInfo = new ChessGameInfo(scgGame.getUid(), scgGame.getCurrentStatus(),
                scgGame.getPlayer().getUserName(), null);
		gtController.setServerChessGameDAO(serverChessGameDAO);
	}
	
	@Test
	public void getGamesTest() throws Exception {
	    Map<Long, ChessGameInfo> games = new HashMap<>();
	    games.put(GAME_UID, scgGameInfo);

		when(serverChessGameDAO.getGameInfoForPlayer(any(Player.class)))
	    .thenReturn((Map)games);
	    
		Callable<String> callable = gtController.getGames(scgGame.getPlayer());
		String result = callable.call();
		Gson gson = new Gson();
		Type mapType = new TypeToken<Map<Long, ChessGameInfo>>() {
		}.getType();
		Map<Long, ChessGameInfo> serverCGInfoMap = gson.fromJson(result, mapType);

		ChessGameInfo serverCGInfo = serverCGInfoMap.get(GAME_UID);
		assertEquals(GAME_UID, serverCGInfo.getUid());
		assertEquals(scgGame.getPlayer().getUserName(),	serverCGInfo.getPlayer());
		assertEquals(scgGame.getCurrentStatus(), serverCGInfo.getCurrentStatus());
		assertNull(serverCGInfo.getOpponent());
	}

    @Test
	public void getNoGames() throws Exception {
		Callable<String> callable = gtController.getGames(scgGame.getPlayer());
		String result = callable.call();
		Gson gson = new Gson();
		Type mapType = new TypeToken<Map<Long, ChessGameInfo>>() {
		}.getType();
		Map<?,?> aMap = gson.fromJson(result, mapType);
		assertTrue(aMap.isEmpty());
	}
}
