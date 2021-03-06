package org.amc.game.chessserver;

import static org.amc.game.chess.NoChessGame.NO_CHESSGAME;
import static org.amc.game.chess.NoPlayer.NO_PLAYER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.junit.Before;
import org.junit.Test;

public class OneViewServerChessGameConstructorTest {

    private OneViewServerChessGame ovscGame;
    private Player player;
    private ChessGameFactory chessGameFactory;
    private static final long GAME_UID = 2L;

    @Before
    public void setUp() throws Exception {
        player = new HumanPlayer("Ted");
        chessGameFactory = new ChessGameFactory() {
            @Override
            public ChessGame getChessGame(ChessBoard board, ChessGamePlayer playerWhite,
                            ChessGamePlayer playerBlack) {
                return new ChessGame(board, playerWhite, playerBlack);
            }
        };
    }

    @Test
    public void constructorTest() {
        ovscGame = new OneViewServerChessGame();
        assertEquals(NO_CHESSGAME, ovscGame.getChessGame());
        assertEquals(NO_PLAYER, ovscGame.getPlayer());
        assertEquals(NO_PLAYER, ovscGame.getOpponent());
        assertEquals(ServerGameStatus.NEW, ovscGame.getCurrentStatus());
        assertNull(ovscGame.getChessGameFactory());
        assertEquals(0, ovscGame.getUid());
        assertEquals(0, ovscGame.getNoOfObservers());
    }

    @Test
    public void constructorUidDPlayerTest() {
        ovscGame = new OneViewServerChessGame(GAME_UID, player);
        ovscGame.setChessGameFactory(chessGameFactory);

        assertEquals(NO_CHESSGAME, ovscGame.getChessGame());
        assertTrue(ComparePlayers.isSamePlayer(player, ovscGame.getPlayer()));
        assertEquals(NO_PLAYER, ovscGame.getOpponent());
        assertEquals(ServerGameStatus.AWAITING_PLAYER, ovscGame.getCurrentStatus());
        assertNotNull(ovscGame.getChessGameFactory());
        assertEquals(GAME_UID, ovscGame.getUid());
        assertEquals(0, ovscGame.getNoOfObservers());
    }
}
