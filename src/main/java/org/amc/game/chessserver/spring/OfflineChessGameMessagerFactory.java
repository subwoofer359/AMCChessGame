package org.amc.game.chessserver.spring;

import org.amc.game.chessserver.messaging.OfflineChessGameMessager;

public abstract class OfflineChessGameMessagerFactory {

    public abstract OfflineChessGameMessager createOfflineChessGameMessager();
}
