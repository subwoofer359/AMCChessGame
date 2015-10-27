package org.amc.dao;

import static org.mockito.Mockito.*;

import org.amc.DAOException;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGameFixture;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.game.chessserver.observers.GameFinishedListener;
import org.amc.game.chessserver.observers.JsonChessGameView;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * Creates a single ServerChessGame and stores it in the database
 * 
 * @author Adrian Mclaughlin
 *
 */
public class ServerChessGameTestDatabaseEntity {
    
    private Player whitePlayer;
    private Player blackPlayer;
    private ChessGame game;
    private ServerChessGame scgGame;
    private final long UID = 29393L;
    
    public ServerChessGameTestDatabaseEntity() throws DAOException {
        ServerChessGameDAO scgDAO = new ServerChessGameDAO();
        DAO<Player> playerDAO = new DAO<>(HumanPlayer.class);
        ChessGameFixture cgFixture = new ChessGameFixture();
        
        whitePlayer = playerDAO.findEntities("userName", "laura").get(0);
        blackPlayer = playerDAO.findEntities("userName", "nobby").get(0);
        
        
        
        game = new ChessGame(cgFixture.getBoard(), new ChessGamePlayer(whitePlayer, Colour.WHITE),
                        new ChessGamePlayer(blackPlayer, Colour.BLACK));
        scgGame = new ServerChessGame(UID, game);
        
        JsonChessGameView jsonView = new JsonChessGameView(mock(SimpMessagingTemplate.class));
        GameFinishedListener listener = new GameFinishedListener();
        
        jsonView.setGameToObserver(scgGame);
        listener.setGameToObserver(scgGame);
        
        cgFixture.getBoard().initialise();
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
        return game;
    }

    public ServerChessGame getServerChessGame() {
        return scgGame;
    }

    public long getUID() {
        return UID;
    }
    
    
}
