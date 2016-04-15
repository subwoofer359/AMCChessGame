package org.amc.game.chessserver.observers;

import static org.mockito.Mockito.*;

import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.amc.game.chessserver.TwoViewServerChessGame;
import org.amc.game.chessserver.observers.GameFinishedListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class FinishedChessGameRemovalThreadTest {

    private int NUMBER_OF_GAMES = 100;
    private ThreadPoolTaskScheduler scheduler;
    // Countdown Latch used to synchonise removal threads
    private CountDownLatch latch = new CountDownLatch(NUMBER_OF_GAMES);
    private ExecutorService threadService;
    private AbstractServerChessGame[] chessGames = new AbstractServerChessGame[NUMBER_OF_GAMES];
    private long[] uids = new long[NUMBER_OF_GAMES];
    private Player player = new HumanPlayer("Adrian McLaughlin");
    
    @Mock
    private ServerChessGameDAO serverChessGameDAO;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        threadService = Executors.newFixedThreadPool(NUMBER_OF_GAMES);
        scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(NUMBER_OF_GAMES);
        scheduler.afterPropertiesSet();
        player.setUserName("adrian");
        for(int i = 0; i < uids.length; i++) {
            uids[i] = i;
            chessGames[i] = new TwoViewServerChessGame(uids[i], player);
            GameFinishedListener listener =new GameFinishedListener();
            listener.setServerChessGameDAO(serverChessGameDAO);

            listener.setGameToObserver(chessGames[i]);
            listener.setTaskScheduler(scheduler);
            listener.setDelayTime(1);
        }
        
        
    }

    @After
    public void tearDown() throws Exception {

    }
    
    @Test
    public void test() throws Exception {
        for(int i = 0; i < uids.length; i++){
            threadService.submit((new GameRemover(latch, chessGames[i])));
        }
        latch.await();
        waitForThreadServicesShutdown();
        verify(serverChessGameDAO, times(NUMBER_OF_GAMES)).deleteEntity(any(AbstractServerChessGame.class));
    }
    
    private void waitForThreadServicesShutdown() throws Exception {
        threadService.shutdown();
        threadService.awaitTermination(60, TimeUnit.SECONDS);
        
        scheduler.getScheduledThreadPoolExecutor().shutdown();
        scheduler.getScheduledThreadPoolExecutor().awaitTermination(60, TimeUnit.SECONDS);
    }
    
    /**
     * Execute Thread to call {@link ServerChessGame#setCurrentStatus(ServerGameStatus)} method
     * which in turn calls {@link GameFinishedListener#update(org.amc.util.Subject, Object)}
     * 
     * @author Adrian Mclaughlin
     *
     */
    public static class GameRemover implements Callable<String> {
        private AbstractServerChessGame chessGame;
        private CountDownLatch latch;
        
        public GameRemover(CountDownLatch latch, AbstractServerChessGame chessGame) {
            this.latch = latch;
            this.chessGame = chessGame;
            
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
