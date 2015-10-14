package org.amc.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.amc.game.chess.ChessBoardFactory;
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
import org.amc.game.chessserver.messaging.OfflineChessGameMessager;
import org.amc.game.chessserver.spring.OfflineChessGameMessagerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatabaseGameMapIntegrationTest {

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
        final OfflineChessGameMessager offlineCGM = mock(OfflineChessGameMessager.class);
        serverChessGamefactory.setOfflineChessGameMessagerFactory(new OfflineChessGameMessagerFactory() {
            
            @Override
            public OfflineChessGameMessager createOfflineChessGameMessager() {
                return offlineCGM;
            }
        });
        
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
        
        final OfflineChessGameMessager offlineCGM = mock(OfflineChessGameMessager.class);
        serverChessGamefactory.setOfflineChessGameMessagerFactory(new OfflineChessGameMessagerFactory() {
            
            @Override
            public OfflineChessGameMessager createOfflineChessGameMessager() {
                return offlineCGM;
            }
        });
        ServerChessGame game = serverChessGamefactory.getServerChessGame(GameType.NETWORK_GAME, UID, nobby);
        game.addOpponent(laura);
        Move move = new Move(new Location(Coordinate.A,2), new Location(Coordinate.A,3));
        game.move(game.getPlayer(nobby), move);
        gameMap.put(UID, game);
        
        game = gameMap.get(UID);
        
        assertEquals(move, game.getChessGame().getTheLastMove());
        
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
}
