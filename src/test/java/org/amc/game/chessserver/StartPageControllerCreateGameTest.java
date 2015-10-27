package org.amc.game.chessserver;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.messaging.OfflineChessGameMessager;
import org.amc.game.chessserver.spring.OfflineChessGameMessagerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StartPageControllerCreateGameTest {
    private Model model;
    private ConcurrentMap<Long, ServerChessGame> gameMap;
    private ChessGamePlayer whitePlayer;
    private StartPageController controller;
    private ServerChessGameFactory scgFactory;
    private static final String OPPONENT ="Kate Bush";
    

    
    @Before
    public void setUp() throws Exception {
        final OfflineChessGameMessager ocgMessager = mock(OfflineChessGameMessager.class);
        scgFactory = new ServerChessGameFactory();
        model=new ExtendedModelMap();
        gameMap =new ConcurrentHashMap<>();
        whitePlayer=new ChessGamePlayer(new HumanPlayer("Ted"), Colour.WHITE);
        controller=new StartPageController();
        
        OfflineChessGameMessagerFactory factory =new OfflineChessGameMessagerFactory() {

            @Override
            public OfflineChessGameMessager createOfflineChessGameMessager() {
                
                return ocgMessager;
            }
            
        };
        
        controller.setGameMap(gameMap);
        scgFactory.setOfflineChessGameMessagerFactory(factory);
        controller.setServerChessGameFactory(scgFactory);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testTwoViewServerGame() {
        assertSessionAttributeNull();    
        String viewName = controller.createGame(model, whitePlayer, GameType.NETWORK_GAME, null);
        assertGameMapNotEmpty();
        assertPlayerIsAddedToChessGame();
        assertLongStoreInSessionAttribute();
        assertEquals(StartPageController.CHESS_APPLICATION_PAGE, viewName);
    }
    
    @Test
    public void testOneViewServerGame() {
        assertSessionAttributeNull();    
        String viewName = controller.createGame(model, whitePlayer, GameType.LOCAL_GAME, OPPONENT);
        assertGameMapNotEmpty();
        assertPlayerIsAddedToChessGame();
        assertLongStoreInSessionAttribute();
        assertEquals(StartPageController.ONE_VIEW_CHESS_PAGE, viewName);
        assertNotNull(model.asMap().get(ServerConstants.GAME));
        assertNotNull(model.asMap().get(ServerConstants.CHESSPLAYER));
    }
    
    @Test
    public void testPlayersNameIsEmptyString() {
        String invalidPlayersName = "";
        assertSessionAttributeNull();
        String viewName = controller.createGame(model, whitePlayer, GameType.LOCAL_GAME, invalidPlayersName);
        assertTrue(gameMap.isEmpty());
        assertEquals(StartPageController.TWOVIEW_FORWARD_PAGE, viewName);
        assertEquals(invalidPlayersName, model.asMap().get(StartPageController.PLAYERS_NAME_FIELD));
        
    }
    
    @Test
    public void testPlayersNameIsNull() {
        String invalidPlayersName = null;
        assertSessionAttributeNull();
        String viewName = controller.createGame(model, whitePlayer, GameType.LOCAL_GAME, invalidPlayersName);
        assertTrue(gameMap.isEmpty());
        assertEquals(StartPageController.TWOVIEW_FORWARD_PAGE, viewName);
        assertEquals(invalidPlayersName, model.asMap().get(StartPageController.PLAYERS_NAME_FIELD));
        
    }
    
    private void assertSessionAttributeNull(){
        assertNull(model.asMap().get(ServerConstants.GAME_UUID.toString()));
    }
    
    private void assertLongStoreInSessionAttribute(){
        assertEquals(model.asMap().get(ServerConstants.GAME_UUID.toString()).getClass(),Long.class);
    }
    
    private void assertGameMapNotEmpty(){
        assertTrue(gameMap.size()==1);
    }
    
    private void assertPlayerIsAddedToChessGame(){
        List<ServerChessGame> games= new ArrayList<>(gameMap.values());
        assertTrue(ComparePlayers.comparePlayers(games.get(0).getPlayer(), whitePlayer));
    }
}