package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ServerJoinControllerJoinGameTest {
    private ConcurrentMap<Long, AbstractServerChessGame> gameMap;
    private ServerJoinChessGameController controller;
    private Player whitePlayer;
    private Player blackPlayer;
    private long gameUUID = 1234L;

    @Before
    public void setUp() throws Exception {
        gameMap = new ConcurrentHashMap<>();
        controller = new ServerJoinChessGameController();
        controller.setGameMap(gameMap);
        whitePlayer = new HumanPlayer("Ted");
        blackPlayer = new HumanPlayer("Chris");
        AbstractServerChessGame chessGame = new TwoViewServerChessGame(gameUUID, whitePlayer);
        chessGame.setChessGameFactory(new ChessGameFactory() {
            @Override
            public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite,
                            ChessGamePlayer playerBlack) {
                return new ChessGame(board, playerWhite, playerBlack);
            }
        });
        gameMap.put(gameUUID, chessGame);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        ModelAndView mav = controller.joinGame(blackPlayer, gameUUID);
        AbstractServerChessGame chessGame = gameMap.get(gameUUID);
        assertEquals(AbstractServerChessGame.ServerGameStatus.IN_PROGRESS, chessGame.getCurrentStatus());
        assertTrue(ComparePlayers.comparePlayers(chessGame.getPlayer(), whitePlayer));
        assertTrue(ComparePlayers.comparePlayers(chessGame.getOpponent(), blackPlayer));
        assertModelAndViewAttributesOnSuccess(mav, blackPlayer);
    }

    @Test
    public void testPlayerJoinsOwnGame() {
        ModelAndView mav = controller.joinGame(whitePlayer, gameUUID);
        AbstractServerChessGame chessGame = gameMap.get(gameUUID);
        assertEquals(AbstractServerChessGame.ServerGameStatus.AWAITING_PLAYER, chessGame.getCurrentStatus());
        assertNull(chessGame.getOpponent());
        assertModelAndViewAttributesOnFail(mav,
                        ServerJoinChessGameController.ERROR_GAME_HAS_NO_OPPONENT);
    }

    @Test
    public void testPlayerReJoinsGameAlreadyInProgress() {
        AbstractServerChessGame chessGame = gameMap.get(gameUUID);
        chessGame.addOpponent(blackPlayer);
        assertEquals(AbstractServerChessGame.ServerGameStatus.IN_PROGRESS, chessGame.getCurrentStatus());
        assertTrue(ComparePlayers.comparePlayers(chessGame.getOpponent(), blackPlayer));
        ModelAndView mav = controller.joinGame(blackPlayer, gameUUID);
        ModelAndViewAssert.assertViewName(mav, "chessGamePortal");
        assertModelAndViewAttributesOnSuccess(mav, blackPlayer);

    }

    @Test
    public void testPlayerJoinsGameAlreadyInProgress() {
        AbstractServerChessGame chessGame = gameMap.get(gameUUID);
        chessGame.addOpponent(new HumanPlayer("Test"));
        assertNotEquals(AbstractServerChessGame.ServerGameStatus.AWAITING_PLAYER,
                        chessGame.getCurrentStatus());
        assertFalse(ComparePlayers.comparePlayers(chessGame.getOpponent(), blackPlayer));
        ModelAndView mav = controller.joinGame(blackPlayer, gameUUID);
        assertModelAndViewAttributesOnFail(mav,
                        ServerJoinChessGameController.ERROR_PLAYER_NOT_OPPONENT);

    }

    @Test
    public void testPlayerJoinsGameWhichHasFinished() {
        AbstractServerChessGame chessGame = gameMap.get(gameUUID);
        chessGame.addOpponent(new HumanPlayer("Test"));
        chessGame.setCurrentStatus(ServerGameStatus.FINISHED);
        assertEquals(AbstractServerChessGame.ServerGameStatus.FINISHED, chessGame.getCurrentStatus());
        assertFalse(ComparePlayers.comparePlayers(chessGame.getOpponent(), blackPlayer));
        ModelAndView mav = controller.joinGame(blackPlayer, gameUUID);
        assertModelAndViewAttributesOnFail(mav, ServerJoinChessGameController.ERROR_GAMEOVER);
    }

    @Test
    public void testPlayerJoinsCurrentGameWhichHasFinished() {
        AbstractServerChessGame chessGame = gameMap.get(gameUUID);
        chessGame.addOpponent(blackPlayer);
        chessGame.setCurrentStatus(ServerGameStatus.FINISHED);
        assertEquals(AbstractServerChessGame.ServerGameStatus.FINISHED, chessGame.getCurrentStatus());
        assertTrue(ComparePlayers.comparePlayers(chessGame.getOpponent(), blackPlayer));
        ModelAndView mav = controller.joinGame(blackPlayer, gameUUID);
        assertModelAndViewAttributesOnFail(mav, ServerJoinChessGameController.ERROR_GAMEOVER);
    }

    @Test
    public void testPlayerJoinsOwnGameAlreadyInProgress() {
        AbstractServerChessGame chessGame = gameMap.get(gameUUID);
        chessGame.addOpponent(blackPlayer);
        assertEquals(AbstractServerChessGame.ServerGameStatus.IN_PROGRESS, chessGame.getCurrentStatus());
        assertTrue(ComparePlayers.comparePlayers(chessGame.getOpponent(), blackPlayer));
        assertTrue(ComparePlayers.comparePlayers(chessGame.getPlayer(), whitePlayer));
        ModelAndView mav = controller.joinGame(whitePlayer, gameUUID);
        assertModelAndViewAttributesOnSuccess(mav, whitePlayer);

    }

    private void assertModelAndViewAttributesOnSuccess(ModelAndView mav, Player player) {
        ModelAndViewAssert.assertViewName(mav, ServerJoinChessGameController.TWO_VIEW_CHESS_PAGE);
        ModelAndViewAssert.assertModelAttributeAvailable(mav, ServerConstants.GAME_UUID);
        ModelAndViewAssert.assertModelAttributeAvailable(mav, ServerConstants.GAME);
        ModelAndViewAssert.assertModelAttributeAvailable(mav, ServerConstants.CHESSPLAYER);
        Player actualPlayer = ModelAndViewAssert.assertAndReturnModelAttributeOfType(mav, ServerConstants.CHESSPLAYER, Player.class);
        assertTrue(ComparePlayers.comparePlayers(player, actualPlayer));
    }

    private void assertModelAndViewAttributesOnFail(ModelAndView mav, String errorMessage) {
        ModelAndViewAssert.assertViewName(mav, ServerJoinChessGameController.ERROR_FORWARD_PAGE);
        ModelAndViewAssert.assertModelAttributeAvailable(mav, ServerConstants.ERRORS);
        ModelAndViewAssert.assertModelAttributeValue(mav, ServerConstants.ERRORS, errorMessage);
    }

}
