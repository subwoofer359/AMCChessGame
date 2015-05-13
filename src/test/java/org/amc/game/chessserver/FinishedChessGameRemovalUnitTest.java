package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame.ServerGameStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class FinishedChessGameRemovalUnitTest {

    Map<Long, ServerChessGame> gameMap;
    ServerChessGame chessGame;
    long uid = 1234L;
    Player player = new HumanPlayer("Adrian McLaughlin");
    GameFinishedListener listener;
    ThreadPoolTaskScheduler scheduler;
    
    @Before
    public void setUp() throws Exception {
        scheduler=new ThreadPoolTaskScheduler();
        scheduler.afterPropertiesSet();
        gameMap = new ConcurrentHashMap<Long, ServerChessGame>();
        player.setUserName("adrian");
        chessGame = new ServerChessGame(uid, player);
        listener = new GameFinishedListener();
        listener.addServerChessGame(chessGame);
        listener.setGameMap(gameMap);
        listener.setTaskScheduler(scheduler);
        listener.setDelayTime(1);
        gameMap.put(uid, chessGame);
    }

    @After
    public void tearDown() throws Exception {
        
        gameMap.clear();
    }
    
    @Test
    public void test() throws Exception {
        assertTrue(gameMap.containsKey(uid));
        chessGame.setCurrentStatus(ServerGameStatus.AWAITING_PLAYER);
        assertTrue(gameMap.containsKey(uid));
        chessGame.setCurrentStatus(ServerGameStatus.FINISHED);
        scheduler.getScheduledThreadPoolExecutor().shutdown();
        scheduler.getScheduledThreadPoolExecutor().awaitTermination(60, TimeUnit.SECONDS);
        assertFalse(gameMap.containsKey(uid));
    }
}
