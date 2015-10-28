package org.amc.game.chessserver;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.amc.game.GameSubject;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.messaging.OfflineChessGameMessager;
import org.amc.game.chessserver.observers.ObserverFactoryChainFixture;

import org.amc.util.Observer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

public class ServerChessGameFactoryTest {

    private ServerChessGameFactory scgfactory;
    private long GAME_UID = 1233L;
    private ChessGamePlayer whitePlayer;
    
    @Before
    public void setUp() throws Exception {
        scgfactory = new ServerChessGameFactory();
        whitePlayer = new ChessGamePlayer(new HumanPlayer("Ted"), Colour.WHITE);
        scgfactory.setOfflineChessGameMessagerFactory(
                        MockOfflineChessGameMessageFactory.getOfflineChessGameMessageFactory());
        scgfactory.setObserverFactoryChain(ObserverFactoryChainFixture.getUpObserverFactoryChain());
        
    }
    
    

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCreateNetworkGame() throws Exception {
        ServerChessGame scgGame = scgfactory.getServerChessGame(GameType.NETWORK_GAME, GAME_UID, whitePlayer);
        assertTrue(scgGame instanceof ServerChessGame);
        assertFalse(scgGame instanceof OneViewServerChessGame);
        checkOfflineChessGameMessagerHasBeenAdded(scgGame, true);
        assertEquals(4, scgGame.getNoOfObservers());
    }
    
    @Test
    public void testCreateLocalGame() throws Exception {
        ServerChessGame scgGame = scgfactory.getServerChessGame(GameType.LOCAL_GAME, GAME_UID, whitePlayer);
        assertTrue(scgGame instanceof ServerChessGame);
        assertTrue(scgGame instanceof OneViewServerChessGame);
        checkOfflineChessGameMessagerHasBeenAdded(scgGame, false);
        assertEquals(3, scgGame.getNoOfObservers());
    }
    
    private void checkOfflineChessGameMessagerHasBeenAdded(ServerChessGame scgGame, boolean pass) throws Exception {
        Field observerField = GameSubject.class.getDeclaredField("observers");
        observerField.setAccessible(true);
        
        List<Observer> list = (List<Observer>)observerField.get(scgGame);
        
        for(Observer obs: list) {
            if(obs instanceof OfflineChessGameMessager) {
                assertTrue(pass);
                return;
            }
        }
        assertTrue(!pass);
    }

}
