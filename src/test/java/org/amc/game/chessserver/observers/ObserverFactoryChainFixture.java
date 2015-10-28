package org.amc.game.chessserver.observers;

import static org.mockito.Mockito.*;

import org.amc.dao.DatabaseGameMap;
import org.amc.game.GameObserver;
import org.amc.game.chessserver.observers.GameFinishedListener;
import org.amc.game.chessserver.observers.GameFinishedListenerFactory;
import org.amc.game.chessserver.observers.GameStateListenerFactory;
import org.amc.game.chessserver.observers.JsonChessGameViewFactory;
import org.amc.game.chessserver.observers.ObserverFactoryChain;
import org.amc.game.chessserver.observers.ObserverFactoryChainImpl;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;

public class ObserverFactoryChainFixture {

    
    public static ObserverFactoryChain getUpObserverFactoryChain() {
        JsonChessGameViewFactory jsonviewFactory = new JsonChessGameViewFactory();
        jsonviewFactory.setMessagingTemplate(mock(SimpMessagingTemplate.class));
        
        GameFinishedListenerFactory gameFinishedListenerFactory = new GameFinishedListenerFactory() {

            @Override
            public GameObserver createObserver() {
                GameFinishedListener listener = new GameFinishedListener();
                listener.setGameMap(mock(DatabaseGameMap.class));
                listener.setTaskScheduler(mock(TaskScheduler.class));
                return listener;
            }
            
        };
        GameStateListenerFactory gameStateListenerFactory = new GameStateListenerFactory();
        gameStateListenerFactory.setMessagingTemplate(mock(SimpMessagingTemplate.class));
        
        ObserverFactoryChain jsonViewChain = new ObserverFactoryChainImpl();
        jsonViewChain.setObserverFactory(jsonviewFactory);
        
        ObserverFactoryChain gFLChain = new ObserverFactoryChainImpl();
        gFLChain.setObserverFactory(gameFinishedListenerFactory);
        
        ObserverFactoryChain gSLChain = new ObserverFactoryChainImpl();
        gSLChain.setObserverFactory(gameStateListenerFactory);
        
        
        jsonViewChain.setSuccessor(gFLChain);
        gFLChain.setSuccessor(gSLChain);
        return jsonViewChain;
    }
}
