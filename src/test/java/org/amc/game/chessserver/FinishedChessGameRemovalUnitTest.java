package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame.ServerGameStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FinishedChessGameRemovalUnitTest {

    Map<Long, ServerChessGame> gameMap;
    ServerChessGame chessGame;
    long uid = 1234L;
    Player player = new HumanPlayer("Adrian McLaughlin");
    GameFinishListener listener;
    
    @Before
    public void setUp() throws Exception {
        gameMap = new ConcurrentHashMap<Long, ServerChessGame>();
        player.setUserName("adrian");
        chessGame = new ServerChessGame(uid, player);
        new GameFinishListener(gameMap, chessGame);
        gameMap.put(uid, chessGame);
    }

    @After
    public void tearDown() throws Exception {
        
        gameMap.clear();
    }
    
    @Test
    public void test() {
        assertTrue(gameMap.containsKey(uid));
        chessGame.setCurrentStatus(ServerGameStatus.AWAITING_PLAYER);
        assertTrue(gameMap.containsKey(uid));
        chessGame.setCurrentStatus(ServerGameStatus.FINISHED);
        assertFalse(gameMap.containsKey(uid));
    }
}
