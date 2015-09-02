package org.amc.dao;

import static org.junit.Assert.*;

import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.DatabaseSignUpFixture;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.ServerChessGame.ServerGameStatus;
import org.amc.game.chessserver.ServerChessGameFactory;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class DatabaseGameMapIntegrationTest {

    private Player stephen;
    private Player laura;
    private Player nobby;
    private Player opponent;
    private DatabaseSignUpFixture signUpfixture = new DatabaseSignUpFixture();
    private DAO<Player> playerDAO;
    private static final long numOfEntries = 5l;
    private List<ServerChessGame> serverChessGameList;
    private ServerChessGameFactory serverChessGamefactory;
    private DatabaseGameMap gameMap;
    private ServerChessGameDAO dao;
    
    @Before
    public void setUp() throws Exception {
        this.serverChessGameList  = new ArrayList<>();
        this.signUpfixture.setUp();
        playerDAO = new DAO<Player>(HumanPlayer.class);
        stephen = playerDAO.findEntities("userName", "stephen").get(0);
        laura = playerDAO.findEntities("userName", "laura").get(0);
        nobby = playerDAO.findEntities("userName", "nobby").get(0);
        serverChessGamefactory = new ServerChessGameFactory();
        
        gameMap = new DatabaseGameMap();
        dao = new ServerChessGameDAO();
        gameMap.setServerChessGameDAO(dao);
    }

    @After
    public void tearDown() throws Exception {
        this.signUpfixture.tearDown();
    }

    @Test
    public void testGameOneFinished() {
        
        final long UID = 1234l;
        ServerChessGame game = serverChessGamefactory.getServerChessGame(GameType.LOCAL_GAME, UID, laura);
        
        gameMap.put(UID, game);
        
        game = gameMap.get(UID);
        
        assertEquals(game.getCurrentStatus(), ServerChessGame.ServerGameStatus.AWAITING_PLAYER);
        assertTrue(ComparePlayers.comparePlayers(laura, game.getPlayer()));
        
        game.addOpponent(nobby);
        
        game = gameMap.get(UID);
        assertEquals(game.getCurrentStatus(), ServerChessGame.ServerGameStatus.IN_PROGRESS);
        assertTrue(ComparePlayers.comparePlayers(nobby, game.getOpponent()));
        
        game.setCurrentStatus(ServerGameStatus.FINISHED);
        
        game = gameMap.get(UID);
        assertEquals(game.getCurrentStatus(), ServerChessGame.ServerGameStatus.FINISHED);
    }
    
    @Test
    public void testGameTwo() {
        
        final long UID = 12324l;
        ServerChessGame game = serverChessGamefactory.getServerChessGame(GameType.LOCAL_GAME, UID, nobby);
        
        gameMap.put(UID, game);
        
        game = gameMap.get(UID);
        
        assertEquals(game.getCurrentStatus(), ServerChessGame.ServerGameStatus.AWAITING_PLAYER);
        assertTrue(ComparePlayers.comparePlayers(nobby, game.getPlayer()));
        
        game.addOpponent(stephen);
        
        dao = new ServerChessGameDAO();
        gameMap.setServerChessGameDAO(dao);
        
        game = gameMap.get(UID);
        
        assertEquals(game.getCurrentStatus(), ServerChessGame.ServerGameStatus.IN_PROGRESS);
        assertTrue(ComparePlayers.comparePlayers(stephen, game.getOpponent()));
    }

}
