package org.amc.dao;

import static org.junit.Assert.*;

import org.amc.DAOException;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Location;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chess.ChessBoard.Coordinate;
import org.amc.game.chessserver.DatabaseSignUpFixture;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.ServerChessGame.ServerGameStatus;
import org.amc.game.chessserver.ServerChessGameFactory;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.observers.ObserverFactoryChain;
import org.amc.game.chessserver.observers.ObserverFactoryChainFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;
import java.util.concurrent.TimeUnit;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"/GameServerWebSockets.xml", "/SpringTestConfig.xml", "/GameServerSecurity.xml", "/EmailServiceContext.xml"})
public class DatabaseGameMapIntegrationTest {

    @Autowired
    private WebApplicationContext wac;
    
    private Player stephen;
    private Player laura;
    private Player nobby;
    private DatabaseSignUpFixture signUpfixture = new DatabaseSignUpFixture();
    private DAO<Player> playerDAO;
    private ServerChessGameFactory serverChessGamefactory;
    private DatabaseGameMap gameMap;
    private ServerChessGameDAO dao;
    
    @Before
    public void setUp() throws Exception {
        this.signUpfixture.setUp();
        playerDAO = new DAO<Player>(HumanPlayer.class);
        stephen = playerDAO.findEntities("userName", "stephen").get(0);
        laura = playerDAO.findEntities("userName", "laura").get(0);
        nobby = playerDAO.findEntities("userName", "nobby").get(0);
        serverChessGamefactory = new ServerChessGameFactory();
        serverChessGamefactory.setObserverFactoryChain(ObserverFactoryChainFixture.getUpObserverFactoryChain());
        
        gameMap = new DatabaseGameMap();
        dao = new ServerChessGameDAO();
        ObserverFactoryChain chain = (ObserverFactoryChain) wac.getBean("defaultObserverFactoryChain");
        dao.setObserverFactoryChain(chain);
        
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
        
        gameMap.put(UID, game);
    }
    
    @Test
    public void testGameTwo() {
        
        final long UID = 12323334l;
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
    
    @Test
    public void testGameThree() {
        
        final long UID = 12443324l;
        
        ServerChessGame game = serverChessGamefactory.getServerChessGame(GameType.NETWORK_GAME, UID, nobby);
        
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

    @Test
    public void testChessGameLocal() throws Exception{
        final long UID = 1222334324l;
        ServerChessGame game = serverChessGamefactory.getServerChessGame(GameType.LOCAL_GAME, UID, nobby);
        game.addOpponent(laura);
        Move move = new Move(new Location(Coordinate.A,2), new Location(Coordinate.A,3));
        game.move(game.getPlayer(nobby), move);
        gameMap.put(UID, game);
        
        game = gameMap.get(UID);
        
        assertEquals(move, game.getChessGame().getTheLastMove());
        
    }
    
    @Test
    public void testChessGameNetwork() throws Exception{
        final long UID = 1232224l;
        
        ServerChessGame game = serverChessGamefactory.getServerChessGame(GameType.NETWORK_GAME, UID, nobby);
        game.addOpponent(laura);
        
        Move move = new Move(new Location(Coordinate.A,2), new Location(Coordinate.A,3));
        game.move(game.getPlayer(nobby), move);
        gameMap.put(UID, game);
        
        dao.detachEntity(game);
        
        game = gameMap.get(UID);
        
        assertEquals(move, game.getChessGame().getTheLastMove());
        assertEquals(4, game.getNoOfObservers());
        
    }
    
    
    @Test
    public void testRetrieve() throws Exception {
        final long UID = 1222334324l;
        ServerChessGame game = serverChessGamefactory.getServerChessGame(GameType.LOCAL_GAME, UID, nobby);
        game.addOpponent(laura);
        game.move(game.getPlayer(nobby), new Move("A2-A3"));
        game.move(game.getPlayer(laura), new Move("A7-A6"));
        game.move(game.getPlayer(nobby), new Move("B2-B3"));
        Move move = new Move("B7-B6");
        game.move(game.getPlayer(laura), move);
        gameMap.put(UID, game);
        dao.detachEntity(game);
        dao.getEntityManager().getTransaction().begin();
        dao.getEntityManager().flush();
        dao.getEntityManager().getTransaction().commit();
        game = gameMap.get(UID);
        assertEquals(move, game.getChessGame().getTheLastMove());
        
    }
    
    @Test 
    public void  keySet() {
        final long UID1 = 1222334324l;
        final long UID2 = 1395l;
        ServerChessGame game1 = serverChessGamefactory.getServerChessGame(GameType.LOCAL_GAME, UID1, nobby);
        game1.addOpponent(laura);
        
        ServerChessGame game2 = serverChessGamefactory.getServerChessGame(GameType.LOCAL_GAME, UID2, laura);
        game2.addOpponent(nobby);
        
        gameMap.put(UID1, game1);
        gameMap.put(UID2, game2);
        
        Set<Long> keys = gameMap.keySet();
        
        
        assertEquals(2, keys.size());
        assertTrue(keys.contains(UID1));
        assertTrue(keys.contains(UID2));
    }
    
    @Test
    public void testPlayerIsNotRemovedOnServerChessGameRemove() throws DAOException {
    	ServerChessGameTestDatabaseEntity scEntity = new ServerChessGameTestDatabaseEntity();
    	checkPlayerExistsInTheDatabase(scEntity.getWhitePlayer().getUserName());
    	ServerChessGame game = gameMap.get(scEntity.getUID());
    	gameMap.remove(game.getUid());
    	checkPlayerExistsInTheDatabase(scEntity.getWhitePlayer().getUserName());
    }
    
    @Test
    public void testSize() throws DAOException {
        final int gameToCreate = 1000;
        new ServerChessGameTestDatabaseEntity(gameToCreate);
        long startTime = System.nanoTime();
        long noOfGames = gameMap.size();
        long endTime = System.nanoTime();
        
        System.out.println("Time taken:" + TimeUnit.NANOSECONDS.toSeconds((endTime - startTime)));
        System.out.println("Time taken:" + TimeUnit.NANOSECONDS.toMillis((endTime - startTime)));
        assertEquals(gameToCreate, (noOfGames - 1));
        
    }
    
    private void checkPlayerExistsInTheDatabase(String userName) throws DAOException {
    	assertTrue(playerDAO.findEntities("userName", userName).size() ==1);
    }
}
