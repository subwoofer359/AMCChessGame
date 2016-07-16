package org.amc.game.chessserver.observers;

import static org.mockito.Mockito.*;

import org.amc.dao.ServerChessGameDAO;
import org.amc.game.GameSubject;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.amc.game.chessserver.TwoViewServerChessGame;
import org.amc.game.chessserver.observers.GameFinishedListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.TimeUnit;

public class FinishedChessGameRemovalUnitTest {

    private AbstractServerChessGame serverChessGame;
    private static final long uid = 1234L;
    private Player player = new HumanPlayer("Adrian McLaughlin");
    private GameFinishedListener listener;
    private ThreadPoolTaskScheduler scheduler;
    
    private static final int SHUTDOWN_TIMEOUT = 60;

    @Mock
    private ServerChessGameDAO serverChessGameDAO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        scheduler = new ThreadPoolTaskScheduler();
        scheduler.afterPropertiesSet();

        player.setUserName("adrian");
        serverChessGame = new TwoViewServerChessGame(uid, player);
        listener = new GameFinishedListener();
        listener.setServerChessGameDAO(serverChessGameDAO);
        listener.setTaskScheduler(scheduler);
        listener.setDelayTime(1);

    }

    @Test
    public void test() throws Exception {
        listener.setGameToObserver(serverChessGame);
        serverChessGame.setCurrentStatus(ServerGameStatus.AWAITING_PLAYER);
        serverChessGame.setCurrentStatus(ServerGameStatus.FINISHED);

        checkServerChessGameDeleted();
    }


    private void checkServerChessGameDeleted() throws Exception {
        waitForThreadPoolShutdown();
        verify(serverChessGameDAO, times(1)).deleteEntity(eq(serverChessGame));
    }

    private void waitForThreadPoolShutdown() throws Exception {
        scheduler.getScheduledThreadPoolExecutor().shutdown();
        scheduler.getScheduledThreadPoolExecutor().awaitTermination(SHUTDOWN_TIMEOUT, TimeUnit.SECONDS);
    }

    @Test
    public void setGameToObserverTest() throws Exception {
        serverChessGame.setCurrentStatus(ServerGameStatus.FINISHED);
        listener.setGameToObserver(serverChessGame);
        checkServerChessGameDeleted();
    }

    @Test
    public void setGameToObserverObjectTest() throws Exception {
        serverChessGame.setCurrentStatus(ServerGameStatus.FINISHED);
        GameSubject serverChessGameObject = serverChessGame;
        listener.setGameToObserver(serverChessGameObject);
        checkServerChessGameDeleted();
    }

    @Test
    public void setGameToObserverObjectNotServerChessGameTest() throws Exception {
        serverChessGame.setCurrentStatus(ServerGameStatus.FINISHED);
        GameSubject subject = new GameSubject();
        listener.setGameToObserver(subject);
        checkServerChessGameNotDeleted();
    }

    private void checkServerChessGameNotDeleted() throws Exception {
        waitForThreadPoolShutdown();
        verify(serverChessGameDAO, times(0)).deleteEntity(eq(serverChessGame));
    }
}
