package org.amc.game.chessserver.observers;

import org.amc.game.GameObserver;

public class JsonChessGameViewFactory extends MessagingGameObserverFactory {

    @Override
    public GameObserver createObserver() {
        return new JsonChessGameView(getMessagingTemplate());
    }

    @Override
    public Class<? extends GameObserver> forObserverClass() {
        return JsonChessGameView.class;
    }
}
