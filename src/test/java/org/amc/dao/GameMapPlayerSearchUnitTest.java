package org.amc.dao;

import static org.junit.Assert.*;

import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chess.StandardChessGameFactory;
import org.amc.game.chessserver.OneViewServerChessGame;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.TwoViewServerChessGame;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class GameMapPlayerSearchUnitTest {
    
    
    private static final List<Player> players;
    private static final int NO_OF_SEPARATE_GAMES = 100;
    private static final int NO_OF_GAMES = 5;
    private static final int NO_OF_PLAYER = 100;
    private static final ConcurrentMap<Long, AbstractServerChessGame> actualGameMap;
    
    static {
        players = new ArrayList<Player>();
        actualGameMap = new ConcurrentHashMap<>();
    }
    
    private GameMapPlayerSearch gmps;
    private DatabaseGameMap gameMap;
    
    @Mock
    private ServerChessGameDAO serverChessGameDAO;

    @BeforeClass
    public static void gameMapSetUp() {
        setUpPlayers();
        setUpGameMap();
    }
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        gmps = new GameMapPlayerSearch();
        gameMap = new DatabaseGameMap();
        gameMap.setServerChessGameDAO(serverChessGameDAO);
        gameMap.setInternalHashMap(actualGameMap);
    }
    
    
    private static void setUpPlayers() {
        for(int i = 0; i < NO_OF_PLAYER; i++) {
            Player tempPlayer = new HumanPlayer("name:" + i);
            tempPlayer.setUserName("userName:" + i);
            players.add(tempPlayer);
        }
    }
    
    public static void setUpGameMap() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for(Player player : players) {
            for(int i = 0; i < NO_OF_SEPARATE_GAMES; i++) {
                Long gameUid = random.nextLong();
                actualGameMap.put(gameUid, new OneViewServerChessGame(gameUid, player));
            }
        }
        
        ChessGameFactory standardFactory = new StandardChessGameFactory();
        
        for(int p = 0; p < players.size() - 1; p++) {
            Player player = players.get(p);
            for(int i = 0; i < NO_OF_GAMES; i++) {
                Long gameUid = random.nextLong();
                AbstractServerChessGame game = new TwoViewServerChessGame(gameUid, player);
                game.setChessGameFactory(standardFactory);
                game.addOpponent(players.get(p + 1));
                actualGameMap.put(gameUid, game);
            }
        }
        
        //create game for last player against first player
        for(int i = 0; i < NO_OF_GAMES; i++) {
            Player player = players.get(players.size()-1);
            Long gameUid = random.nextLong();
            AbstractServerChessGame game = new TwoViewServerChessGame(gameUid, player);
            game.setChessGameFactory(standardFactory);
            game.addOpponent(players.get(0));
            actualGameMap.put(gameUid, game);
        }
        
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGamesNo() {
        Map<Long, AbstractServerChessGame> games = gmps.getGames(gameMap, players.get(0));
        assertEquals(NO_OF_SEPARATE_GAMES + NO_OF_GAMES * 2, games.size());
    }
    
 
    @Test
    public void testTwoThreadSearch() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(8);
        List<Future<Integer>> resultList = new ArrayList<>();
        
        for(Player player : players) {
            resultList.add(service.submit(new GameSearchThread(gameMap, player)));
        }
 
        for(Future<Integer> result : resultList) {
            assertEquals(NO_OF_SEPARATE_GAMES + NO_OF_GAMES * 2, result.get(2, TimeUnit.MINUTES).intValue());
        }
        service.shutdown();
        service.awaitTermination(10, TimeUnit.SECONDS);
    }
    
    private static class GameSearchThread implements Callable<Integer> {
        private ConcurrentMap<Long, AbstractServerChessGame> gameMap;
        private Player player;
        
        public GameSearchThread(ConcurrentMap<Long, AbstractServerChessGame> gameMap, Player player) {
            this.gameMap = gameMap;
            this.player = player;
        }
        @Override
        public Integer call() throws Exception {
            GameMapPlayerSearch search = new GameMapPlayerSearch();
            return search.getGames(gameMap, player).size();
        }
        
        
    }
}
