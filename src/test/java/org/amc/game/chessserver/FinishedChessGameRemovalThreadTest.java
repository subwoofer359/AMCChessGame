package org.amc.game.chessserver;

import static org.junit.Assert.*;

import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame.ServerGameStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class FinishedChessGameRemovalThreadTest {

    int NUMBER_OF_GAMES = 1000;
    CountDownLatch latch = new CountDownLatch(NUMBER_OF_GAMES);
    ExecutorService threadService;
    Map<Long, ServerChessGame> gameMap;
    ServerChessGame[] chessGames = new ServerChessGame[NUMBER_OF_GAMES];
    long[] uids = new long[NUMBER_OF_GAMES];
    Player player = new HumanPlayer("Adrian McLaughlin");
    GameFinishListener listener;
    
    @Before
    public void setUp() throws Exception {
        threadService = Executors.newFixedThreadPool(NUMBER_OF_GAMES);
        gameMap = new ConcurrentHashMap<Long, ServerChessGame>();
        player.setUserName("adrian");
        for(int i = 0; i < uids.length; i++) {
            uids[i] = i;
            chessGames[i] = new ServerChessGame(uids[i], player);
            new GameFinishListener(gameMap, chessGames[i]);
            gameMap.put(uids[i], chessGames[i]);
        }
        
        
    }

    @After
    public void tearDown() throws Exception {
        
        gameMap.clear();
    }
    
    @Test
    public void test() throws Exception {
        for(int i = 0; i < uids.length; i++){
            threadService.submit((new GameRemover(latch, gameMap, uids[i], chessGames[i])));
        }
        threadService.shutdown();
        threadService.awaitTermination(30, TimeUnit.SECONDS);
        assertTrue(gameMap.isEmpty());
    }
    
    public static class GameRemover implements Callable<String> {
        private Map<?,?> gameMap;
        private ServerChessGame chessGame;
        private long uid;
        private CountDownLatch latch;
        
        public GameRemover(CountDownLatch latch, Map<?,?> gameMap, long uid, ServerChessGame chessGame) {
            this.latch = latch;
            this.gameMap = gameMap;
            this.chessGame = chessGame;
            this.uid = uid;
            
        }
        @Override
        public String call() throws Exception {
            Thread.sleep(ThreadLocalRandom.current().nextLong(2000));
            chessGame.setCurrentStatus(ServerGameStatus.FINISHED);
            latch.countDown();
            latch.await();
            return "";
        }
        
    }

}
