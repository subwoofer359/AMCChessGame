package org.amc.game.chessserver.observers;

import static org.mockito.Mockito.*;

import org.amc.dao.ServerChessGameDAO;
import org.amc.game.GameObserver;
import org.amc.game.chessserver.ServerChessGame;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;

import java.util.ArrayList;
import java.util.List;

public class ObserverFactoryChainImplTest { 
    private ServerChessGame chessGame;
    private ObserverFactoryChain chain;
    private String observerStr;
    private ObserverFactory[] factories = {
                    new JsonChessGameViewFactory(),
                    new GameFinishedListenerFactory(){
                        @Override
                        public GameObserver createObserver() {
                            GameFinishedListener listener = new GameFinishedListener();
                            listener.setServerChessGameDAO(mock(ServerChessGameDAO.class));
                            listener.setTaskScheduler(mock(TaskScheduler.class));
                            return listener;
                        }
                    },
                    new GameStateListenerFactory() {

                        @Override
                        public GameObserver createObserver() {
                            GameStateListener listener = new GameStateListener();
                            return listener;
                        }
                        
                    }
                    };
    @Before
    public void setUp() throws Exception {
        chessGame = mock(ServerChessGame.class);
        
        final SimpMessagingTemplate template = mock(SimpMessagingTemplate.class);
        List<ObserverFactoryChain> chains = new ArrayList<>();
       
        for(ObserverFactory factory : factories) {
            if(factory instanceof MessagingGameObserverFactory) {
                ((MessagingGameObserverFactory)factory).setMessagingTemplate(template);
            }
            
            ObserverFactoryChain chain = new ObserverFactoryChainImpl();
            chain.setObserverFactory(factory);
            chains.add(chain);
        }
        
        
        for(int i = 0; i < chains.size() - 1 ; i++) {
            chains.get(i).setSuccessor(chains.get(i + 1));
        }
        chain = spy(chains.get(0));
        
    }

    @Test
    public void test() {
        observerStr = JsonChessGameView.class.getSimpleName();
        chain.addObserver(observerStr, chessGame);
        verify(chessGame, times(1)).attachObserver(any(GameObserver.class));    
    }
    
    @Test
    public void test2() {
        observerStr = JsonChessGameView.class.getSimpleName() + GameFinishedListener.class.getSimpleName();
        chain.addObserver(observerStr, chessGame);
        verify(chessGame,times(2)).attachObserver(any(GameObserver.class));
    }
    
    @Test
    public void test3() {
        observerStr = JsonChessGameView.class.getSimpleName() + GameFinishedListener.class.getSimpleName() +
                        GameStateListener.class.getSimpleName();
        chain.addObserver(observerStr, chessGame);
        verify(chessGame,times(3)).attachObserver(any(GameObserver.class));
    }

}
