package org.amc.dao;

import static org.mockito.Mockito.*;

import org.amc.DAOException;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGameFixture;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chess.StandardChessGameFactory;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.observers.GameFinishedListener;
import org.amc.game.chessserver.observers.JsonChessGameView;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * Creates a single ServerChessGame and stores it in the database
 * 
 * @author Adrian Mclaughlin
 *
 */
public class ServerChessGameTestDatabaseEntity {
    
    private Player whitePlayer;
    private Player blackPlayer;
    private ServerChessGame scgGame;
    private final long UID = 29393L;
    private final ServerChessGameDAO scgDAO;
    private final ChessGameFixture cgFixture;
    private ChessGameFactory chessGamefactory; 
    
    public ServerChessGameTestDatabaseEntity() throws DAOException {
        this.chessGamefactory = new StandardChessGameFactory();
        scgDAO = new ServerChessGameDAO();
        DAO<Player> playerDAO = new DAO<>(HumanPlayer.class);
        cgFixture = new ChessGameFixture();
        
        whitePlayer = playerDAO.findEntities("userName", "laura").get(0);
        blackPlayer = playerDAO.findEntities("userName", "nobby").get(0);
        
        cgFixture.getBoard().initialise();
        
        ServerChessGame scgGame = createServerGame(UID);
        addServerChessGameToDataBase(scgGame);
    }
    
    public ServerChessGameTestDatabaseEntity(int noOfDBEntries) throws DAOException {
        this();
        EntityManager em = scgDAO.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        for (int i = 0; i < noOfDBEntries; i++) {
            em.persist(createServerGame(i));
        }
        em.flush();
        transaction.commit();
        
    }
    
    private ServerChessGame createServerGame(long id) {
        ChessGame chessGame = chessGamefactory.getChessGame(cgFixture.getBoard(), 
                        new ChessGamePlayer(whitePlayer, Colour.WHITE),
                        new ChessGamePlayer(blackPlayer, Colour.BLACK));
        scgGame = new ServerChessGame(id, chessGame);
        JsonChessGameView jsonView = new JsonChessGameView(mock(SimpMessagingTemplate.class));
        GameFinishedListener listener = new GameFinishedListener();
        jsonView.setGameToObserver(scgGame);
        listener.setGameToObserver(scgGame);
        
        return scgGame;
    }
    
    private void addServerChessGameToDataBase(ServerChessGame scgGame) throws DAOException {
        scgDAO.addEntity(scgGame);
        scgDAO.getEntityManager().detach(scgGame);
    }
    
    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }
    
    

    public ChessGame getChessGame() {
        return scgGame.getChessGame();
    }

    public ServerChessGame getServerChessGame() {
        return scgGame;
    }

    public long getUID() {
        return UID;
    }
    
    
}
