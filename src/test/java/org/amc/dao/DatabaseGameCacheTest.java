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
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DatabaseGameCacheTest {

    private ServerChessGameDAO chessGameDAO;
    private Long gameUid = 1234L;
    private Player player = new HumanPlayer("Ted");
    private ServerChessGameFactory scgfactory;
    private DatabaseGameMap gameMap;
    private ServerChessGame game;
    
    private List<ServerChessGame> gamesInDatabase;
    private ConcurrentMap<Long, ServerChessGame> gamesInCache;
    private Set<Long> uidsInDatabase;
    
    private static final int NO_OF_GAMES_IN_DATABASE = 5;
    private static final int NO_OF_GAMES_IN_CACHE = 2;
    private static final int NO_OF_GAMES_CACHED = 2;
    private static final int TOTAL_OF_GAMES = NO_OF_GAMES_IN_DATABASE + 
                    NO_OF_GAMES_IN_CACHE;
    
    @Before
    public void setUp() throws Exception {
        chessGameDAO = mock(ServerChessGameDAO.class);
        
        scgfactory = new ServerChessGameFactory();
        scgfactory.setObserverFactoryChain(ObserverFactoryChainFixture.getUpObserverFactoryChain());
        
        gamesInCache = new ConcurrentHashMap<>();
        gamesInDatabase = new ArrayList<>();
        uidsInDatabase = new HashSet<>();
        
        populateMockDatabase();
        
        copyGamesToCache();
        
        populateMockCache();
        
        when(chessGameDAO.findEntities()).thenReturn(gamesInDatabase);
        when(chessGameDAO.findEntities(eq("uid"), eq(gameUid))).thenReturn(Arrays.asList(game));
        when(chessGameDAO.getGameUids()).thenReturn(uidsInDatabase);
        when(chessGameDAO.getServerChessGame(eq(gameUid))).thenReturn(game);
        
        gameMap = new DatabaseGameMap();
        gameMap.setServerChessGameDAO(chessGameDAO);
        gameMap.setInternalHashMap(gamesInCache);
    }
    
    private void populateMockDatabase() {
        Random generator = new Random();
        for(int i = 0; i < NO_OF_GAMES_IN_DATABASE; i++) {
            long uid = generator.nextLong();
            gamesInDatabase.add(scgfactory.getServerChessGame(GameType.LOCAL_GAME, uid, player));
            uidsInDatabase.add(uid);
        }
    }
    
    private void populateMockCache() {
        Random generator = new Random();
        for(int i = 0 ; i < NO_OF_GAMES_IN_CACHE; i++) {
            final long cacheUid = generator.nextLong(); 
            gamesInCache.put(cacheUid, scgfactory.getServerChessGame(GameType.LOCAL_GAME, cacheUid, player));
        }
    }
    
    private void copyGamesToCache() {
        for(int i = 0; i < NO_OF_GAMES_CACHED; i++) {
            gamesInCache.put(gamesInDatabase.get(i).getUid(),gamesInDatabase.get(i));
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void sizeTest() {
        Set<ServerChessGame> gameSet = new HashSet<>(gamesInDatabase);
        gameSet.addAll(gamesInCache.values());
        assertEquals(TOTAL_OF_GAMES, gameSet.size());
        assertEquals(TOTAL_OF_GAMES, gameMap.size());
    }
    
    @Test
    public void isEmptyTest() throws DAOException {
        assertFalse(gameMap.isEmpty());
        when(chessGameDAO.findEntities()).thenReturn(new ArrayList<ServerChessGame>());
        assertFalse(gameMap.isEmpty());
        gameMap.clearCache();
        assertTrue(gameMap.isEmpty());
        
    }
    
    @Test
    public void putTest() {
        ServerChessGame scg = scgfactory.getServerChessGame(GameType.LOCAL_GAME, gameUid, player);
        gameMap.put(gameUid, scg);
        assertTrue(gameMap.containsKey(gameUid));
    }
    
    @Test
    public void removeTest() throws DAOException {
        ServerChessGame scg = scgfactory.getServerChessGame(GameType.LOCAL_GAME, gameUid, player);
        gameMap.put(gameUid, scg);
        gameMap.remove(gameUid);
        assertFalse(gameMap.containsKey(gameUid));
        verify(chessGameDAO, times(1)).deleteEntity(eq(scg));
    }
    
    @Test
    public void keySetTest() {
        assertEquals(TOTAL_OF_GAMES, gameMap.keySet().size());
        Set<Long> keys = new HashSet<>();
        for(ServerChessGame game : gamesInDatabase) {
            keys.add(game.getUid());
        }
        keys.addAll(gamesInCache.keySet());
        assertEquals(gameMap.keySet(), keys);
    }

    
    @Test
    public void valuesTest() {
        assertEquals(TOTAL_OF_GAMES, gameMap.values().size());
        Set<ServerChessGame> games = new HashSet<>();
        for(ServerChessGame game : gamesInDatabase) {
            games.add(game);
        }
        games.addAll(gamesInCache.values());
        assertEquals(gameMap.values(), games);
    }
    
    @Test
    public void entrySet() {
        Map<Long, ServerChessGame> games = new HashMap<Long, ServerChessGame>();
        
        for(ServerChessGame game : gamesInDatabase) {
            games.put(game.getUid(), game);
        }
        games.putAll(gamesInCache);
        
        assertEquals(games.entrySet(), gameMap.entrySet());
    }
}
