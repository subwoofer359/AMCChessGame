package org.amc.game.chessserver.messaging;

import org.amc.util.Subject;
import org.apache.log4j.Logger;

public class MockOfflineChessGameMessager extends OfflineChessGameMessager {

    private static final Logger logger = Logger.getLogger(MockOfflineChessGameMessager.class);
    
    @Override
    public void update(Subject subject, Object message) {
        logger.debug(toString() + "update method called with (" + message + ")");
    }

    
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }    
}
