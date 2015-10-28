package org.amc.dao;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.amc.DAOException;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.ServerChessGameFactory;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.observers.ObserverFactoryChainFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DatabaseGameMapTest {

    private ServerChessGameDAO chessGameDAO;
    private List<ServerChessGame> chessGamesList;
    private Long gameUid = 1234L;
    private Player player = new HumanPlayer("Ted");
    private ServerChessGameFactory scgfactory;
    private DatabaseGameMap gameMap;
    private ServerChessGame game;
    
    private Map<Long, ServerChessGame> hashMap;

    @Before
    public void setUp() throws Exception {
        hashMap = mock(HashMap.class);
        
        scgfactory = new ServerChessGameFactory();
        scgfactory.setObserverFactoryChain(ObserverFactoryChainFixture.getUpObserverFactoryChain());

        game = scgfactory.getServerChessGame(GameType.LOCAL_GAME, gameUid, player);
        chessGameDAO = mock(ServerChessGameDAO.class);
        chessGamesList = Arrays.asList(game);
        when(chessGameDAO.findEntities()).thenReturn(chessGamesList);
        when(chessGameDAO.findEntities(eq("uid"), eq(gameUid))).thenReturn(Arrays.asList(game));
        when(chessGameDAO.getGameUids()).thenReturn(Sets.newSet(gameUid));
        when(chessGameDAO.getServerChessGame(eq(gameUid))).thenReturn(game);

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
    public void sizeThrowsExceptionTest() throws DAOException {
        when(chessGameDAO.findEntities()).thenThrow(new DAOException("sizeThrowsExceptionTest"));
        assertEquals(0, gameMap.size());
    }

    @Test
    public void isEmptyTest() {
        assertFalse(gameMap.isEmpty());
    }

    @Test
    public void isEmptyTestThrowsDAOException() throws DAOException {
        when(chessGameDAO.findEntities()).thenThrow(
                        new DAOException("isEmptyTestThrowsDAOException"));
        assertTrue(gameMap.isEmpty());
    }

    @Test
    public void containsKeyTest() {
        gameMap.setDatabaseHashMap(hashMap);
        assertTrue(gameMap.containsKey(gameUid));
        verify(hashMap, times(1)).containsKey(eq(gameUid));
    }

    @Test
    public void doesNotContainsKeyTest() {
        assertFalse(gameMap.containsKey(23445L));
    }

    @Test
    public void doesNotContainsNonLongKeyTest() {
        assertFalse(gameMap.containsKey("key"));
    }
    
    @Test
    public void containsNullKeyTest() {
        assertFalse(gameMap.containsKey(null));
    }

    @Test
    public void containsKeyThrowsDAOExceptionTest() throws DAOException {
        when(chessGameDAO.findEntities(eq("uid"), anyObject())).thenThrow(
                        new DAOException("containsKeyThrowsDAOExceptionTest"));
        this.gameMap.setServerChessGameDAO(chessGameDAO);
        assertFalse(gameMap.containsKey(3234L));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void containsValue() {
        gameMap.containsValue(null);
    }

    @Test
    public void getTest() {
        gameMap.setDatabaseHashMap(hashMap);
        ServerChessGame retrievedChessGame = gameMap.get(gameUid);
        assertEquals(game, retrievedChessGame);
        verify(hashMap, times(1)).get(eq(gameUid));
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
    public void getKeyNotLong() {
        ServerChessGame retrievedChessGame = gameMap.get("key");
        assertNull(retrievedChessGame);
    }

    @Test
    public void getThrowsDAOException() throws DAOException {
        when(chessGameDAO.getServerChessGame(anyLong())).thenThrow(
                        new DAOException("getThrowsDAOException"));
        ServerChessGame retrievedChessGame = gameMap.get(2343L);
        assertNull(retrievedChessGame);
    }

    @Test
    public void putTest() throws DAOException {
        final Long newUid = 1000L;
        gameMap.setDatabaseHashMap(hashMap);
        final ServerChessGame newGame = scgfactory.getServerChessGame(
                        GameType.LOCAL_GAME, newUid, player);
        gameMap.put(newUid, newGame);
        verify(this.chessGameDAO, times(1)).addEntity(eq(newGame));
        verify(this.hashMap, times(1)).put(newUid, newGame);
    }

    @Test
    public void putTestThrowsDAOException() throws DAOException {
        doThrow(new DAOException("putTestThrowsDAOException")).when(chessGameDAO).addEntity(
                        any(ServerChessGame.class));
        final Long newUid = 1000L;
        final ServerChessGame newGame = scgfactory.getServerChessGame(
                        GameType.LOCAL_GAME, newUid, player);
        gameMap.put(newUid, newGame);
        verify(this.chessGameDAO, times(1)).addEntity(eq(newGame));

    }

    @Test(expected = UnsupportedOperationException.class)
    public void putAllTest() {
        gameMap.putAll(null);
    }

    @Test
    public void removeTest() throws DAOException {
        gameMap.setDatabaseHashMap(hashMap);
        gameMap.remove(gameUid);
        verify(this.chessGameDAO, times(1)).deleteEntity(eq(game));
        verify(this.hashMap, times(1)).remove(eq(gameUid));
    }

    @Test
    public void removeThrowDAOExceptionTest() throws DAOException {
        doThrow(new DAOException("removeThrowDAOExceptionTest")).when(chessGameDAO).deleteEntity(
                        any(ServerChessGame.class));
        gameMap.remove(gameUid);
        verify(this.chessGameDAO, times(1)).deleteEntity(eq(game));
    }

    @Test
    public void removeNullTest() throws DAOException {
        assertNull(gameMap.remove(null));
        verify(this.chessGameDAO, times(0)).deleteEntity(eq(game));

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
    public void valuesTestThrowsException() throws DAOException {
        when(chessGameDAO.findEntities()).thenThrow(new DAOException("valuesTestThrowsException"));
        Collection<ServerChessGame> games = gameMap.values();
        assertFalse(games.contains(game));
    }

    @Test
    public void entrySetTest() {
        Set<Map.Entry<Long, ServerChessGame>> entries = gameMap.entrySet();
        assertFalse(entries.isEmpty());
    }

    @Test
    public void entrySetThrowsExceptionTest() throws DAOException {
        when(chessGameDAO.findEntities())
                        .thenThrow(new DAOException("entrySetThrowsExceptionTest"));
        Set<Map.Entry<Long, ServerChessGame>> entries = gameMap.entrySet();
        assertTrue(entries.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void clearTest() {
        gameMap.clear();
    }
}
