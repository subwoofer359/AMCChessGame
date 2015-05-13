package org.amc.game.chessserver;

import org.amc.game.chess.ChessGame;
import org.amc.util.Observer;
import org.amc.util.Subject;
import org.apache.log4j.Logger;

import java.util.Map;

public class GameFinishListener implements Observer {

    private Map<Long, ServerChessGame> gameMap;

    private static final Logger logger = Logger.getLogger(GameFinishListener.class);

    public GameFinishListener(Map<Long, ServerChessGame> gameMap, ServerChessGame chessGame) {
        this.gameMap = gameMap;
        chessGame.attachObserver(this);
    }

    @Override
    public void update(Subject subject, Object message) {
        if (message instanceof ChessGame.GameState && subject instanceof ServerChessGame) {
            ServerChessGame chessGame = (ServerChessGame) subject;
            chessGame.removeAllObservers();
            gameMap.remove(chessGame.getUid());
            this.gameMap = null;
            logger.debug(String.format("Game(%d) has been removed from gameMap", chessGame.getUid()));
        }
    }

}
