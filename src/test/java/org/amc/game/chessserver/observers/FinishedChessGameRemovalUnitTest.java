package org.amc.game.chessserver.observers;

import static org.junit.Assert.*;

import org.amc.game.GameSubject;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.amc.game.chessserver.TwoViewServerChessGame;
import org.amc.game.chessserver.observers.GameFinishedListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class FinishedChessGameRemovalUnitTest {

    Map<Long, ServerChessGame> gameMap;
    ServerChessGame serverChessGame;
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
        serverChessGame = new TwoViewServerChessGame(uid, player);
        listener = new GameFinishedListener();
        listener.setGameMap(gameMap);
        listener.setTaskScheduler(scheduler);
        listener.setDelayTime(1);
        gameMap.put(uid, serverChessGame);
    }

    @After
    public void tearDown() throws Exception {
        
        gameMap.clear();
    }
    
    @Test
    public void test() throws Exception {
        listener.setGameToObserver(serverChessGame);
        assertTrue(gameMap.containsKey(uid));
        serverChessGame.setCurrentStatus(ServerGameStatus.AWAITING_PLAYER);
        assertTrue(gameMap.containsKey(uid));
        serverChessGame.setCurrentStatus(ServerGameStatus.FINISHED);
        
        isServerChessGameDeleted();
    }
    
    private boolean isServerChessGameDeleted() throws IllegalStateException, InterruptedException {
        scheduler.getScheduledThreadPoolExecutor().shutdown();
        scheduler.getScheduledThreadPoolExecutor().awaitTermination(60, TimeUnit.SECONDS);
        return !gameMap.containsKey(uid);
    }
    
    @Test
    public void setGameToObserverTest() throws Exception {
        serverChessGame.setCurrentStatus(ServerGameStatus.FINISHED);
        listener.setGameToObserver(serverChessGame);
        assertTrue(isServerChessGameDeleted());
    }
    
    @Test
    public void setGameToObserverObjectTest() throws Exception {
        serverChessGame.setCurrentStatus(ServerGameStatus.FINISHED);
        GameSubject serverChessGameObject = serverChessGame;
        listener.setGameToObserver(serverChessGameObject);
        assertTrue(isServerChessGameDeleted());
    }
    
    @Test
    public void setGameToObserverObjectNotServerChessGameTest() throws Exception {
        serverChessGame.setCurrentStatus(ServerGameStatus.FINISHED);
        GameSubject subject = new GameSubject();
        listener.setGameToObserver(subject);
        assertFalse(isServerChessGameDeleted());
    }
}
