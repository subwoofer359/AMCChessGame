package org.amc.game.chessserver;

import org.amc.game.chessserver.ServerChessGame.ServerGameStatus;
import org.amc.util.Observer;
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
public class GameFinishedListener implements Observer {

    /**
     * Time in seconds to wait before deleting the chessGame from the gameMap.
     */
    private int delayTime = 120;

    private TaskScheduler scheduler;

    private Map<Long, ServerChessGame> gameMap;

    private static final Logger logger = Logger.getLogger(GameFinishedListener.class);

    public GameFinishedListener() {
    }

    public void addServerChessGame(ServerChessGame chessGame) {
        chessGame.attachObserver(this);
    }

    @Resource(name = "gameMap")
    public void setGameMap(Map<Long, ServerChessGame> gameMap) {
        this.gameMap = gameMap;
    }

    @Resource(name = "myScheduler")
    public void setTaskScheduler(TaskScheduler taskScheduler) {
        this.scheduler = taskScheduler;
    }

    @Override
    public void update(Subject subject, Object message) {
        if (message instanceof ServerGameStatus && subject instanceof ServerChessGame) {
            ServerGameStatus status = (ServerGameStatus) message;
            if (status == ServerGameStatus.FINISHED) {
                ServerChessGame chessGame = (ServerChessGame) subject;
                chessGame.removeObserver(this);
                Calendar c = Calendar.getInstance();
                c.add(Calendar.SECOND, delayTime);
                scheduler.schedule(new DeleteChessGameJob(gameMap, chessGame), c.getTime());
            }
        }
    }

    public int getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    private static class DeleteChessGameJob implements Runnable {

        private Map<Long, ServerChessGame> gameMap;
        private ServerChessGame chessGame;

        public DeleteChessGameJob(Map<Long, ServerChessGame> gameMap, ServerChessGame chessGame) {
            this.gameMap = gameMap;
            this.chessGame = chessGame;
        }

        @Override
        public void run() {
            gameMap.remove(chessGame.getUid());
            chessGame.destory();
            logger.debug(String.format("Game(%d) has been removed from gameMap", chessGame.getUid()));
        }

    }
}
