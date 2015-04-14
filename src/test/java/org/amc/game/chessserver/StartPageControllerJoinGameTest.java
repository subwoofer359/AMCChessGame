package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chessserver.ServerChessGame.ServerGameStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StartPageControllerJoinGameTest {
    private ConcurrentMap<Long, ServerChessGame> gameMap;
    private ServerJoinChessGameController controller;
    private ChessGamePlayer whitePlayer;
    private ChessGamePlayer blackPlayer;
    private long gameUUID = 1234L;

    @Before
    public void setUp() throws Exception {
        gameMap = new ConcurrentHashMap<>();
        controller = new ServerJoinChessGameController();
        controller.setGameMap(gameMap);
        whitePlayer = new ChessGamePlayer(new HumanPlayer("Ted"), Colour.WHITE);
        blackPlayer = new ChessGamePlayer(new HumanPlayer("Chris"), Colour.WHITE);
        ServerChessGame chessGame = new ServerChessGame(whitePlayer);
        gameMap.put(gameUUID, chessGame);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        ModelAndView mav = controller.joinGame(blackPlayer, gameUUID);
        ModelAndViewAssert.assertViewName(mav, "chessGamePortal");
        ModelAndViewAssert.assertModelAttributeAvailable(mav, "GAME_UUID");
        ModelAndViewAssert.assertModelAttributeAvailable(mav, "GAME");
        ModelAndViewAssert.assertModelAttributeAvailable(mav, "CHESSPLAYER");
        ServerChessGame chessGame = gameMap.get(gameUUID);
        assertEquals(ServerChessGame.ServerGameStatus.IN_PROGRESS, chessGame.getCurrentStatus());
        assertEquals(chessGame.getOpponent(), blackPlayer);
    }

    @Test
    public void testPlayerJoinsOwnGame() {
        ModelAndView mav = controller.joinGame(whitePlayer, gameUUID);
        ModelAndViewAssert.assertViewName(mav, "forward:/app/chessgame/chessapplication");
        ModelAndViewAssert.assertModelAttributeAvailable(mav, "ERRORS");
        ServerChessGame chessGame = gameMap.get(gameUUID);
        assertEquals(ServerChessGame.ServerGameStatus.AWAITING_PLAYER, chessGame.getCurrentStatus());
        ModelAndViewAssert.assertModelAttributeValue(mav, "ERRORS", ServerJoinChessGameController.ERROR_GAME_HAS_NO_OPPONENT);
    }

    @Test
    public void testPlayerReJoinsGameAlreadyInProgress() {
        ServerChessGame chessGame = gameMap.get(gameUUID);
        chessGame.addOpponent(blackPlayer);
        assertEquals(ServerChessGame.ServerGameStatus.IN_PROGRESS,
                        chessGame.getCurrentStatus());
        assertTrue(chessGame.getOpponent().equals(blackPlayer));
        ModelAndView mav = controller.joinGame(blackPlayer, gameUUID);
        ModelAndViewAssert.assertViewName(mav, "chessGamePortal");
        ModelAndViewAssert.assertModelAttributeAvailable(mav, "GAME_UUID");
        ModelAndViewAssert.assertModelAttributeAvailable(mav, "GAME");
        ModelAndViewAssert.assertModelAttributeAvailable(mav, "CHESSPLAYER");

    }
    
    @Test
    public void testPlayerJoinsGameAlreadyInProgress() {
        ServerChessGame chessGame = gameMap.get(gameUUID);
        chessGame.addOpponent(new HumanPlayer("Test"));
        assertNotEquals(ServerChessGame.ServerGameStatus.AWAITING_PLAYER,
                        chessGame.getCurrentStatus());
        assertFalse(chessGame.getOpponent().equals(blackPlayer));
        ModelAndView mav = controller.joinGame(blackPlayer, gameUUID);
        ModelAndViewAssert.assertViewName(mav, "forward:/app/chessgame/chessapplication");
        ModelAndViewAssert.assertModelAttributeAvailable(mav, "ERRORS");
        ModelAndViewAssert.assertModelAttributeValue(mav, "ERRORS", ServerJoinChessGameController.ERROR_PLAYER_NOT_OPPONENT);

    }
    
    @Test
    public void testPlayerJoinsGameWhichHasFinished() {
        ServerChessGame chessGame = gameMap.get(gameUUID);
        chessGame.addOpponent(new HumanPlayer("Test"));
        chessGame.setCurrentStatus(ServerGameStatus.FINISHED);
        assertEquals(ServerChessGame.ServerGameStatus.FINISHED,
                        chessGame.getCurrentStatus());
        assertFalse(chessGame.getOpponent().equals(blackPlayer));
        ModelAndView mav = controller.joinGame(blackPlayer, gameUUID);
        ModelAndViewAssert.assertViewName(mav, "forward:/app/chessgame/chessapplication");
        ModelAndViewAssert.assertModelAttributeAvailable(mav, "ERRORS");
        ModelAndViewAssert.assertModelAttributeValue(mav, "ERRORS", ServerJoinChessGameController.ERROR_GAMEOVER);
    }
    
    @Test
    public void testPlayerJoinsCurrentGameWhichHasFinished() {
        ServerChessGame chessGame = gameMap.get(gameUUID);
        chessGame.addOpponent(blackPlayer);
        chessGame.setCurrentStatus(ServerGameStatus.FINISHED);
        assertEquals(ServerChessGame.ServerGameStatus.FINISHED,
                        chessGame.getCurrentStatus());
        assertTrue(chessGame.getOpponent().equals(blackPlayer));
        ModelAndView mav = controller.joinGame(blackPlayer, gameUUID);
        ModelAndViewAssert.assertViewName(mav, "forward:/app/chessgame/chessapplication");
        ModelAndViewAssert.assertModelAttributeAvailable(mav, "ERRORS");
        ModelAndViewAssert.assertModelAttributeValue(mav, "ERRORS", ServerJoinChessGameController.ERROR_GAMEOVER);
    }

    
    @Test
    public void testPlayerJoinsOwnGameAlreadyInProgress() {
        ServerChessGame chessGame = gameMap.get(gameUUID);
        chessGame.addOpponent(blackPlayer);
        assertEquals(ServerChessGame.ServerGameStatus.IN_PROGRESS,
                        chessGame.getCurrentStatus());
        assertTrue(chessGame.getOpponent().equals(blackPlayer));
        assertTrue(chessGame.getPlayer().equals(whitePlayer));
        ModelAndView mav = controller.joinGame(whitePlayer, gameUUID);
        ModelAndViewAssert.assertModelAttributeAvailable(mav, "GAME_UUID");
        ModelAndViewAssert.assertModelAttributeAvailable(mav, "GAME");
        ModelAndViewAssert.assertModelAttributeAvailable(mav, "CHESSPLAYER");
        

    }
}
