package org.amc.dao;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.amc.DAOException;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.ServerChessGameFactory;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DatabaseGameMapTest {

    private ServerChessGameDAO chessGameDAO;
    private List<ServerChessGame> chessGamesList;
    private Long gameUid = 1234L;
    private Player player = new HumanPlayer("Ted");
    private DatabaseGameMap gameMap;
    private ServerChessGame game;
    
    @Before
    public void setUp() throws Exception {
        game = new ServerChessGameFactory().getServerChessGame(GameType.LOCAL_GAME, 
                        gameUid , player);
        
        chessGameDAO = mock (ServerChessGameDAO.class);
        chessGamesList = Arrays.asList(game);
        when(chessGameDAO.findEntities()).thenReturn(chessGamesList);
        when(chessGameDAO.findEntities(eq("uid"), eq(gameUid))).thenReturn(Arrays.asList(game));
        when(chessGameDAO.getGameUids()).thenReturn(Sets.newSet(gameUid));
        
        gameMap = new DatabaseGameMap();
        gameMap.setServerChessGameDAO(chessGameDAO);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void sizeTest() {
        assertEquals(chessGamesList.size(), gameMap.size());
    }
    
    @Test
    public void isEmptyTest() {
        assertFalse(gameMap.isEmpty());
    }
    
    @Test
    public void containsKeyTest() {
        assertTrue(gameMap.containsKey(gameUid));
    }
    
    @Test
    public void doesNotContainsKeyTest() {
        assertFalse(gameMap.containsKey(23445L));
    }
    
    @Test
    public void getTest() {
        ServerChessGame retrievedChessGame = gameMap.get(gameUid);
        assertEquals(game, retrievedChessGame);
    }
    
    @Test
    public void getTestNull() {
        ServerChessGame retrievedChessGame = gameMap.get(2343L);
        assertNull(retrievedChessGame);
    }
    
    @Test
    public void getKeyNotLongNull() {
        ServerChessGame retrievedChessGame = gameMap.get(null);
        assertNull(retrievedChessGame);
    }
    
    @Test
    public void putTest() throws DAOException {
        final Long newUid = 1000L;
        final ServerChessGame newGame = new ServerChessGameFactory().getServerChessGame(GameType.LOCAL_GAME, 
                        newUid , player);
        gameMap.put(newUid, newGame);
        verify(this.chessGameDAO, times(1)).addEntity(eq(newGame));
    }
    
    @Test
    public void removeTest() throws DAOException {
        gameMap.remove(gameUid);
        verify(this.chessGameDAO, times(1)).deleteEntity(eq(game));
    }
    
    @Test
    public void keySetTest() {
        Set<Long> gameUids = gameMap.keySet();
        assertTrue(gameUids.contains(gameUid));
    }

    @Test
    public void valuesTest() {
        Collection<ServerChessGame> games = gameMap.values();
        assertTrue(games.contains(game));
    }
    
    @Test
    public void entrySetTest() {
        Set<Map.Entry<Long, ServerChessGame>> entries = gameMap.entrySet();
        assertFalse(entries.isEmpty());
    }
}
