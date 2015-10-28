package org.amc.game.chessserver;

import static org.mockito.Mockito.mock;

import org.amc.game.GameObserver;
import org.amc.game.chessserver.messaging.OfflineChessGameMessager;
import org.amc.game.chessserver.spring.OfflineChessGameMessagerFactory;

public class MockOfflineChessGameMessageFactory {

    public static OfflineChessGameMessagerFactory getOfflineChessGameMessageFactory() {
        final OfflineChessGameMessager ocgMessager = mock(OfflineChessGameMessager.class);
        return new OfflineChessGameMessagerFactory() {

            @Override
            public GameObserver createObserver() {
                return ocgMessager;
            }

            @Override
            public Class<? extends GameObserver> forObserverClass() {
                return OfflineChessGameMessager.class;
            }

        };
    }

}
