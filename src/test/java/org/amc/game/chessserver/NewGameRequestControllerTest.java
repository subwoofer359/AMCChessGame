package org.amc.game.chessserver;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Callable;

import org.amc.User;
import org.amc.dao.DAO;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

public class NewGameRequestControllerTest {
	private NewGameRequestController controller;
	private WebApplicationContext wac;
	private StartPageController spController;
	private ServerJoinChessGameController sjController;
	private DAO<User> userDAO;
	private Player player;
	private Player opponent;
	private static final String userToPlay = "Robert";
	private User user;
	private User opponentUser;
	private static final Long gameUid = 1234L;

	@SuppressWarnings("unchecked")
    @Before
	public void setUp() throws Exception {
		wac = mock(GenericWebApplicationContext.class);
		spController = mock(StartPageController.class);
		sjController = mock(ServerJoinChessGameController.class);
		
		when(wac.getBean(StartPageController.class)).thenReturn(spController);
		when(wac.getBean(ServerJoinChessGameController.class)).thenReturn(sjController);
		userDAO = mock(DAO.class);
		controller = new NewGameRequestController();
		controller.setUserDAO(userDAO);
		
		controller.context = wac;
		
		player = new HumanPlayer("Chris");
		user = new User();
		user.setName(player.getName());
		user.setUserName(player.getUserName());
		user.setPlayer(player);
		
		opponent = new HumanPlayer(userToPlay);
		opponent.setUserName(userToPlay);
		opponentUser = new User();
		opponentUser.setUserName(opponent.getUserName());
		opponentUser.setName(opponent.getName());
		opponentUser.setPlayer(opponent);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		Model model = new ExtendedModelMap();
		model.addAttribute(ServerConstants.GAME_UUID, gameUid);
		
		when(userDAO.findEntities(eq("userName"), eq(userToPlay))).thenReturn(Arrays.asList(opponentUser));
		Callable<Boolean> m = controller.requestNewChessGame(model, player, userToPlay);
		assertTrue(m.call());
		
		verify(spController, times(1)).createGame(eq(model), eq(opponent), eq(GameType.NETWORK_GAME), anyString());
		verify(sjController, times(1)).joinGame(eq(player), eq(gameUid));
	}
	
	@Test
	public void testNoUser() throws Exception {
		Model model = new ExtendedModelMap();
		model.addAttribute(ServerConstants.GAME_UUID, gameUid);
		
		when(userDAO.findEntities(eq("userName"), eq(userToPlay))).thenReturn(Collections.<User>emptyList());
		Callable<Boolean> m = controller.requestNewChessGame(model, player, userToPlay);
		assertFalse(m.call());
	}
	
	@Test
	public void testAgainstOneSelf() throws Exception {
		Model model = new ExtendedModelMap();
		model.addAttribute(ServerConstants.GAME_UUID, gameUid);
		
		when(userDAO.findEntities(eq("userName"), eq(userToPlay))).thenReturn(Arrays.asList(user));
		Callable<Boolean> m = controller.requestNewChessGame(model, player, userToPlay);
		
		verify(spController, never()).createGame(eq(model), any(Player.class), eq(GameType.NETWORK_GAME), anyString());
		verify(sjController, never()).joinGame(any(Player.class), eq(gameUid));
		assertFalse(m.call());
	}

}
