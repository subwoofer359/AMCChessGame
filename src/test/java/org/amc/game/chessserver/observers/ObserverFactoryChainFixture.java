package org.amc.game.chessserver.observers;

import static org.mockito.Mockito.*;

import org.amc.dao.DAO;
import org.amc.dao.DatabaseGameMap;
import org.amc.game.GameObserver;
import org.amc.game.chessserver.messaging.EmailTemplateFactory;
import org.amc.game.chessserver.messaging.GameMessageService;
import org.amc.game.chessserver.messaging.OfflineChessGameMessager;
import org.amc.game.chessserver.observers.GameFinishedListener;
import org.amc.game.chessserver.observers.GameFinishedListenerFactory;
import org.amc.game.chessserver.observers.GameStateListenerFactory;
import org.amc.game.chessserver.observers.JsonChessGameViewFactory;
import org.amc.game.chessserver.observers.ObserverFactoryChain;
import org.amc.game.chessserver.observers.ObserverFactoryChainImpl;
import org.amc.game.chessserver.spring.OfflineChessGameMessagerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.core.session.SessionRegistry;

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
        
        OfflineChessGameMessagerFactory offlineCGMFactory = new OfflineChessGameMessagerFactory() {
            
            @SuppressWarnings("unchecked")
            @Override
            public GameObserver createObserver() {
                OfflineChessGameMessager messager = new OfflineChessGameMessager();
                messager.setMessageService(mock(GameMessageService.class));
                messager.setEmailTemplateFactory(mock(EmailTemplateFactory.class));
                messager.setSessionRegistry(mock(SessionRegistry.class));
                messager.setUserDAO(mock(DAO.class));
                return messager;
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
        
        ObserverFactoryChain oCGMChain = new ObserverFactoryChainImpl();
        oCGMChain.setObserverFactory(offlineCGMFactory);
    
        
        jsonViewChain.setSuccessor(gFLChain);
        gFLChain.setSuccessor(gSLChain);
        gSLChain.setSuccessor(oCGMChain);
        
        
        
        return jsonViewChain;
    }
}
