package org.amc.game.chessserver;

import static org.amc.game.chess.NoPlayer.NO_PLAYER;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.observers.ObserverFactoryChain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ServerChessGameUnitTest {

    private ServerChessGame scgGame;
    private Player player;
    private Player opponent;
    private static final long GAME_UID = 2L;
    private GameType gameType;

    public ServerChessGameUnitTest(GameType gameType) {
        this.gameType = gameType;
    }

    @Parameters
    public static Collection<?> testServerChessGames() {

        return Arrays.asList(new Object[][] { { GameType.LOCAL_GAME }, { GameType.NETWORK_GAME } });
    }

    @Before
    public void setUp() throws Exception {
        opponent = new HumanPlayer("Chris");
        player = new HumanPlayer("Ted");

        ObserverFactoryChain observerFactoryChain = mock(ObserverFactoryChain.class);
        ServerChessGameFactory factory = new ServerChessGameFactory();
        factory.setObserverFactoryChain(observerFactoryChain);

        scgGame = factory.getServerChessGame(gameType, GAME_UID, player);

        ChessGameFactory chessGameFactory = new ChessGameFactory() {
            @Override
            public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite,
                            ChessGamePlayer playerBlack) {
                return new ChessGame(board, playerWhite, playerBlack);
            }
        };
        scgGame.setChessGameFactory(chessGameFactory);
    }

    @Test
    public void testAddOpponent() {
        scgGame.addOpponent(opponent);
        assertTrue(ComparePlayers.isSamePlayer(scgGame.getPlayer(), player));
        assertNotNull(scgGame.getChessGame());
        assertEquals(ServerChessGame.ServerGameStatus.IN_PROGRESS, scgGame.getCurrentStatus());
        assertEquals(Colour.BLACK, scgGame.getOpponent().getColour());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullAddOpponent() {
        scgGame.addOpponent(null);
    }

    @Test
    public void testAddPlayerAsOpponent() {
        scgGame.addOpponent(player);
        assertTrue(ComparePlayers.isSamePlayer(scgGame.getPlayer(), player));
        assertNull(scgGame.getChessGame());
        assertEquals(ServerChessGame.ServerGameStatus.AWAITING_PLAYER, scgGame.getCurrentStatus());
    }

    @Test
    public void testAddOpponentToFinishedGame() {
        scgGame.setCurrentStatus(ServerChessGame.ServerGameStatus.FINISHED);
        scgGame.addOpponent(opponent);
        assertTrue(ComparePlayers.isSamePlayer(scgGame.getPlayer(), player));
        assertNull(scgGame.getChessGame());
        assertEquals(ServerChessGame.ServerGameStatus.FINISHED, scgGame.getCurrentStatus());
    }

    @Test
    public void testAddOpponentToInProgressGame() {
        scgGame.setCurrentStatus(ServerChessGame.ServerGameStatus.IN_PROGRESS);
        scgGame.addOpponent(opponent);
        assertTrue(ComparePlayers.isSamePlayer(scgGame.getPlayer(), player));
        assertNull(scgGame.getChessGame());
        assertEquals(ServerChessGame.ServerGameStatus.IN_PROGRESS, scgGame.getCurrentStatus());
    }

    @Test
    public void getPlayerOpponentNotAddedTest() {
        assertEquals(NO_PLAYER, scgGame.getPlayer(opponent));
    }

    @Test(expected=RuntimeException.class)
    public void getPlayerUnknownPlayer() {
        scgGame.addOpponent(opponent);
        Player unknownPlayer = new HumanPlayer("Evil Ralph");
        scgGame.getPlayer(unknownPlayer);
    }

    @Test
    public void getPlayerTest() {
        scgGame.addOpponent(opponent);
        assertTrue(ComparePlayers.isSamePlayer(opponent, scgGame.getPlayer(opponent)));
        assertTrue(ComparePlayers.isSamePlayer(player, scgGame.getPlayer(player)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPlayerNullPlayer() {

        scgGame.addOpponent(opponent);
        scgGame.getPlayer(null);
    }

}
