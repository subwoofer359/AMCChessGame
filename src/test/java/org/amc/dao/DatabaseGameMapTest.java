package org.amc.dao;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.amc.DAOException;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.ComparePlayers;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DatabaseGameMapTest {

    private ServerChessGameDAO chessGameDAO;
    private List<ServerChessGame> chessGamesList;
    private Long gameUid = 1234L;
    private Player player = new HumanPlayer("Ted");
    private ServerChessGameFactory scgfactory;
    private DatabaseGameMap gameMap;
    private ServerChessGame game;
    private ConcurrentMap<Long, ServerChessGame> hashMap;

    @Before
    public void setUp() throws Exception {
        hashMap = mock(ConcurrentHashMap.class);
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
        gameMap.setInternalHashMap(hashMap);
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
    public void isEmptyFalseTest() {
        assertFalse(gameMap.isEmpty());
    }
    
    @Test
    public void isEmptyFalseDatabaseNotEmpty() throws DAOException {
        when(hashMap.isEmpty()).thenReturn(true);
        assertFalse(gameMap.isEmpty());
    }
    
    @Test
    public void isEmptyFalseCacheNotEmpty() throws DAOException {
        when(hashMap.isEmpty()).thenReturn(false);
        assertFalse(gameMap.isEmpty());
    }
    
    @Test
    public void isEmptyTrueTest() throws DAOException {
        when(hashMap.isEmpty()).thenReturn(true);
        when(chessGameDAO.findEntities()).thenReturn( Collections.<ServerChessGame>emptyList());
        assertTrue(gameMap.isEmpty());
    }

    @Test
    public void isEmptyTestThrowsDAOException() throws DAOException {
        when(chessGameDAO.findEntities()).thenThrow(
                        new DAOException("isEmptyTestThrowsDAOException"));
        when(hashMap.isEmpty()).thenReturn(true);
        assertTrue(gameMap.isEmpty());
    }

    @Test
    public void containsKeyTest() throws DAOException {
        assertTrue(gameMap.containsKey(gameUid));
        verify(hashMap,times(1)).containsKey(eq(gameUid));
        verify(chessGameDAO, times(1)).findEntities(eq("uid"), eq(gameUid));
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
    public void getTest() throws DAOException {
        ServerChessGame retrievedChessGame = gameMap.get(gameUid);
        assertEquals(game, retrievedChessGame);
        verify(hashMap, times(1)).get(eq(gameUid));
        verify(chessGameDAO, times(1)).getServerChessGame(eq(gameUid));
        verify(hashMap, times(1)).putIfAbsent(eq(gameUid), eq(retrievedChessGame));
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
        final ServerChessGame newGame = scgfactory.getServerChessGame(
                        GameType.LOCAL_GAME, newUid, player);
        gameMap.put(newUid, newGame);
        verify(this.chessGameDAO, never()).addEntity(eq(newGame));
        verify(hashMap, never()).put(eq(gameUid), eq(newGame));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void putAllTest() {
        gameMap.putAll(null);
    }

    @Test
    public void removeTest() throws DAOException {
        gameMap.remove(gameUid);
        verify(hashMap, times(1)).remove(eq(gameUid));
        verify(this.chessGameDAO, times(1)).deleteEntity(eq(game));
    }

    @Test
    public void removeThrowDAOExceptionTest() throws DAOException {
        doThrow(new DAOException("removeThrowDAOExceptionTest")).when(chessGameDAO).deleteEntity(
                        any(ServerChessGame.class));
        gameMap.remove(gameUid);
        verify(hashMap,times(1)).remove(eq(gameUid));
        verify(this.chessGameDAO, times(1)).deleteEntity(eq(game));
    }

    @Test
    public void removeNullTest() throws DAOException {
        assertNull(gameMap.remove(null));
        verify(this.chessGameDAO, never()).deleteEntity(eq(game));
        verify(this.hashMap, never()).remove(null);

    }

    /**
     * Using a real Map as the mock map throws an exception
     */
    @Test
    public void keySetTest() {
        ConcurrentMap<Long, ServerChessGame> realMap = new ConcurrentHashMap<>();
        this.gameMap.setInternalHashMap(realMap);
        Set<Long> gameUids = gameMap.keySet();
        assertTrue(gameUids.contains(gameUid));
    }

    @Test
    public void valuesTest() {
        Collection<ServerChessGame> games = gameMap.values();
        assertTrue(games.contains(game));
    }
    
    @Test
    public void valuesBugTest() {
        ConcurrentMap<Long, ServerChessGame> conHashMap = new ConcurrentHashMap<Long, ServerChessGame>();
        gameMap.setInternalHashMap(conHashMap);
        ServerChessGame newGame = scgfactory.getServerChessGame(GameType.LOCAL_GAME, gameUid, player);
        gameMap.put(gameUid, newGame);
        Player opponent = new HumanPlayer("Mary");
        newGame.addOpponent(new ChessGamePlayer(opponent, Colour.BLACK));
        Collection<ServerChessGame> games = gameMap.values();
        for(ServerChessGame scg : games) {
            assertTrue(ComparePlayers.comparePlayers(opponent, scg.getOpponent()));
        }
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
