package org.amc.game.chessserver;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.amc.dao.ChessGameInfo;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.observers.ObserverFactoryChain;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ServerChessGameSerilaiserTest {
	private GsonBuilder builder;
	private ServerChessGameFactory scgFactory;
	private final long gameUID = 2345L;
	private Player whitePlayer;
	private Player blackPlayer;
	private ChessGameInfo chessGameInfo;

	@Before
	public void setUp() throws Exception {
		builder = new GsonBuilder();
		builder.registerTypeHierarchyAdapter(ChessGameInfo.class,
				new GameTableController.ChessGameInfoSerialiser());
		builder.setPrettyPrinting();
		builder.serializeNulls();
		
		scgFactory = new ServerChessGameFactory();
		ObserverFactoryChain chain = mock(ObserverFactoryChain.class);
		scgFactory.setObserverFactoryChain(chain);
		
		whitePlayer = new HumanPlayer("Sarah");
		whitePlayer.setUserName("username_sarah");
		
		blackPlayer = new HumanPlayer("Robert");
		blackPlayer.setUserName("username_bobby");

		chessGameInfo = new ChessGameInfo(gameUID, ServerGameStatus.IN_PROGRESS,
				whitePlayer.getUserName(), blackPlayer.getUserName());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Gson gson = builder.create();
		String output = gson.toJson(chessGameInfo);
		ChessGameInfo info = gson.fromJson(output, ChessGameInfo.class);
		assertEquals(gameUID, info.getUid());
		assertEquals(whitePlayer.getUserName(), info.getPlayer());
		assertEquals(blackPlayer.getUserName(), info.getOpponent());
		assertEquals(ServerGameStatus.IN_PROGRESS, info.getCurrentStatus());
	}
	
	@Test
	public void testNull() {
		chessGameInfo = null;
		Gson gson = builder.create();
		String output = gson.toJson(chessGameInfo);
		ChessGameInfo info = gson.fromJson(output, ChessGameInfo.class);
		assertNull(info);
	}
	
	@Test
	public void nullPlayerTest() {
	    chessGameInfo = new ChessGameInfo(gameUID, ServerGameStatus.IN_PROGRESS, null, blackPlayer.getUserName());
        Gson gson = builder.create();
        String output = gson.toJson(chessGameInfo);
        ChessGameInfo info = gson.fromJson(output, ChessGameInfo.class);
        assertNotNull(info);
        assertNull(info.getPlayer());
	}
	
	@Test
	public void testNullOpponent() {
		chessGameInfo = new ChessGameInfo(gameUID, ServerGameStatus.IN_PROGRESS, whitePlayer.getUserName(), null);
		Gson gson = builder.create();
		String output = gson.toJson(chessGameInfo);
		ChessGameInfo info = gson.fromJson(output, ChessGameInfo.class);
		assertEquals(gameUID, info.getUid());
		assertNull(info.getOpponent());
		assertEquals(ServerGameStatus.IN_PROGRESS, info.getCurrentStatus());
	}
}
