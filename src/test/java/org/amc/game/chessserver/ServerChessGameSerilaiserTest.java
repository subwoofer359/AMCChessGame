package org.amc.game.chessserver;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
	Player whitePlayer;
	Player blackPlayer;
	
	@Before
	public void setUp() throws Exception {
		builder = new GsonBuilder();
		builder.registerTypeHierarchyAdapter(ServerChessGame.class, 
				new GameTableController.ServerChessGameSerialiser());
		builder.setPrettyPrinting();
		builder.serializeNulls();
		
		scgFactory = new ServerChessGameFactory();
		ObserverFactoryChain chain = mock(ObserverFactoryChain.class);
		scgFactory.setObserverFactoryChain(chain);
		
		whitePlayer = new HumanPlayer("Sarah");
		whitePlayer.setUserName("username_sarah");
		
		blackPlayer = new HumanPlayer("Robert");
		blackPlayer.setUserName("username_bobby");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		ServerChessGame scgGame = scgFactory.getServerChessGame(GameType.LOCAL_GAME, gameUID, whitePlayer);
		scgGame.addOpponent(blackPlayer);
		Gson gson = builder.create();
		String output = gson.toJson(scgGame);
		ServerChessGameInfo info = gson.fromJson(output, ServerChessGameInfo.class);
		assertEquals(gameUID, info.getUid());
		assertEquals(whitePlayer.getUserName(), info.getPlayer());
		assertEquals(blackPlayer.getUserName(), info.getOpponent());
		assertEquals(scgGame.getCurrentStatus(), info.getCurrentStatus());
	}
	
	@Test
	public void testNull() {
		ServerChessGame scgGame = null;
		Gson gson = builder.create();
		String output = gson.toJson(scgGame);
		ServerChessGameInfo info = gson.fromJson(output, ServerChessGameInfo.class);
		assertNull(info);
	}
	
	@Test
	public void testNullOpponent() {
		ServerChessGame scgGame = scgFactory.getServerChessGame(GameType.LOCAL_GAME, gameUID, whitePlayer);
		Gson gson = builder.create();
		String output = gson.toJson(scgGame);
		ServerChessGameInfo info = gson.fromJson(output, ServerChessGameInfo.class);
		assertEquals(gameUID, info.getUid());
		assertNull(info.getOpponent());
		assertEquals(scgGame.getCurrentStatus(), info.getCurrentStatus());
	}
	
	static class ServerChessGameInfo {
		private long uid;
		private ServerGameStatus currentStatus;
		private String player;
		private String opponent;
		
		public long getUid() {
			return uid;
		}
		public ServerGameStatus getCurrentStatus() {
			return currentStatus;
		}

		public String getPlayer() {
			return player;
		}

		public String getOpponent() {
			return opponent;
		}
	}

}
