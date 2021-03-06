package org.amc.dao;

import static org.mockito.Mockito.*;

import org.amc.DAOException;
import org.amc.game.chess.AbstractChessGame;
import org.amc.game.chess.ChessGameFactory;
import org.amc.game.chess.ChessGameFixture;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chess.RealChessGamePlayer;
import org.amc.game.chess.StandardChessGameFactory;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.TwoViewServerChessGame;
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
public class ServerChessGameDatabaseEntityFixture {
    
    private Player whitePlayer;
    private Player blackPlayer;
    private AbstractServerChessGame scgGame;
    private AbstractChessGame chessGame;
    private final long UID = 29393L;
    private final ServerChessGameDAO scgDAO;
    private ChessGameFactory chessGamefactory; 
    private EntityManager entityManager;
    
    public ServerChessGameDatabaseEntityFixture(EntityManager entityManager) throws DAOException {
        this.chessGamefactory = new StandardChessGameFactory();
        this.entityManager = entityManager;
        scgDAO = new ServerChessGameDAO();
        scgDAO.setEntityManager(entityManager);
        DAOInterface<HumanPlayer> playerDAO = new DAO<>(HumanPlayer.class);
        playerDAO.setEntityManager(entityManager);
        chessGame = new ChessGameFixture().getChessGame();
        
        whitePlayer = playerDAO.findEntities("userName", "laura").get(0);
        blackPlayer = playerDAO.findEntities("userName", "nobby").get(0);
        
        chessGame.getChessBoard().initialise();
        
        AbstractServerChessGame scgGame = createServerGame(UID);
        addServerChessGameToDataBase(scgGame);
    }
    
    public ServerChessGameDatabaseEntityFixture(EntityManager entityManager, int noOfDBEntries) throws DAOException {
        this(entityManager);
        EntityManager em = scgDAO.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        for (int i = 0; i < noOfDBEntries; i++) {
            em.persist(createServerGame(i));
        }
        em.flush();
        transaction.commit();
        
    }
    
    private AbstractServerChessGame createServerGame(long id) {
        AbstractChessGame chessGame = chessGamefactory.getChessGame(this.chessGame.getChessBoard(), 
                        new RealChessGamePlayer(whitePlayer, Colour.WHITE),
                        new RealChessGamePlayer(blackPlayer, Colour.BLACK));
        scgGame = new TwoViewServerChessGame(id, chessGame);
        JsonChessGameView jsonView = new JsonChessGameView(mock(SimpMessagingTemplate.class));
        GameFinishedListener listener = new GameFinishedListener();
        jsonView.setGameToObserver(scgGame);
        listener.setGameToObserver(scgGame);
        
        return scgGame;
    }
    
    private void addServerChessGameToDataBase(AbstractServerChessGame scgGame) throws DAOException {
        scgDAO.addEntity(scgGame);
        scgDAO.getEntityManager().detach(scgGame);
    }
    
    public Player getWhitePlayer() {
        return whitePlayer;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }
    
    public AbstractChessGame getChessGame() {
        return scgGame.getChessGame();
    }

    public AbstractServerChessGame getServerChessGame() {
        return scgGame;
    }

    public long getUID() {
        return UID;
    }

	public EntityManager getEntityManager() {
		return entityManager;
	}    
}
