package org.amc.game.chessserver.observers;

import org.amc.game.GameObserver;
import org.amc.game.GameSubject;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.amc.util.Subject;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Map;

import javax.annotation.Resource;

/**
 * 
 * A Listener for the ServerChessGame FINISHED Game Status
 * 
 * @author Adrian Mclaughlin
 *
 */
@Component
@Scope("prototype")
public class GameFinishedListener extends GameObserver {

    /**
     * Time in seconds to wait before deleting the chessGame from the gameMap.
     */
    private int delayTime = 120;

    private TaskScheduler scheduler;

    private Map<Long, AbstractServerChessGame> gameMap;

    private static final Logger logger = Logger.getLogger(GameFinishedListener.class);

    public GameFinishedListener() {
    }

    @Resource(name = "gameMap")
    public void setGameMap(Map<Long, AbstractServerChessGame> gameMap) {
        this.gameMap = gameMap;
    }

    @Resource(name = "myScheduler")
    public void setTaskScheduler(TaskScheduler taskScheduler) {
        this.scheduler = taskScheduler;
    }

    /**
     * Calls {@link #setGameToObserver(AbstractServerChessGame)}
     */
    @Override
    public void setGameToObserver(GameSubject subject) {
        if(subject instanceof AbstractServerChessGame) {
            setGameToObserver((AbstractServerChessGame)subject);
        }
    }
    
    /**
     * Checks to see if the {@link AbstractServerChessGame} is a FINISHED state
     * and schedules it for deletion if it is.
     * @param serverChessGame
     */
    public void setGameToObserver(AbstractServerChessGame serverChessGame) {
        super.setGameToObserver(serverChessGame);
        if(ServerGameStatus.FINISHED.equals(serverChessGame.getCurrentStatus())) {
            scheduleDeleteThread(serverChessGame);
        }
    }
    
    @Override
    public void update(Subject subject, Object message) {
        if (message instanceof ServerGameStatus && subject instanceof AbstractServerChessGame) {
            ServerGameStatus status = (ServerGameStatus) message;
            if (status == ServerGameStatus.FINISHED) {
                AbstractServerChessGame chessGame = (AbstractServerChessGame) subject;
                scheduleDeleteThread(chessGame);
            }
        }
    }
    
    private void scheduleDeleteThread(AbstractServerChessGame serverChessGame) {
        serverChessGame.removeObserver(this);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, delayTime);
        scheduler.schedule(new DeleteChessGameJob(gameMap, serverChessGame), c.getTime());
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    private static class DeleteChessGameJob implements Runnable {

        private Map<Long, AbstractServerChessGame> gameMap;
        private AbstractServerChessGame chessGame;

        public DeleteChessGameJob(Map<Long, AbstractServerChessGame> gameMap, AbstractServerChessGame chessGame) {
            this.gameMap = gameMap;
            this.chessGame = chessGame;
        }

        @Override
        public void run() {
            if(chessGame != null) {
                synchronized (chessGame) {
                    gameMap.remove(chessGame.getUid());
                    chessGame.destroy();
                }
                logger.debug(String.format("Game(%d) has been removed from gameMap", chessGame.getUid()));
            } else 
            {
                logger.debug("GameFinishedLisnter:ServerChessGame is null, ignoring");
            }
        }

    }
}
