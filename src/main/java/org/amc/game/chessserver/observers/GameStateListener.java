package org.amc.game.chessserver.observers;

import org.amc.DAOException;
import org.amc.dao.SCGDAOInterface;
import org.amc.game.GameObserver;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.AbstractChessGame.GameState;
import org.amc.game.chess.ChessBoard.ChessPieceLocation;
import org.amc.game.chessserver.MessageType;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.amc.game.chessserver.StompController;
import org.amc.util.Subject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.OptimisticLockException;

@Component
@Scope(value="prototype")
public class GameStateListener extends GameObserver {

    /**
     * STOMP user message subscription destination
     */
	static final String MESSAGE_USER_DESTINATION = "/queue/updates";
	
	/**
     * STOMP message subscription destination
     */
    static final String MESSAGE_DESTINATION = "/topic/updates";
	
    private static final Logger logger = Logger.getLogger(GameStateListener.class);
    
    /** 
     * STOMP messaging object to send stomp message to objects
     */
    @Autowired
    private SimpMessagingTemplate template;
    
    private SCGDAOInterface serverChessGameDAO;

    /**
     * Called by Model(ObservableChessGame) on change
     * 
     * Converts a ChessBoard to a JSON String and sends it
     * to the subscribed clients
     */
    @Override
    public void update(Subject subject, Object message) {
        if(subject instanceof AbstractServerChessGame) {
            AbstractServerChessGame serverChessGame = (AbstractServerChessGame)subject;
               
            handleGameState(serverChessGame, message);
            handleGameStatusFinished(serverChessGame, message);    
        }
    }

    void handleGameState(AbstractServerChessGame serverChessGame, Object message) {
        if (message instanceof ChessGame.GameState) {
            GameState gameState = (GameState) message;
            if(GameState.PAWN_PROMOTION.equals(gameState)) {
                handleGameStatePromotion(serverChessGame, gameState);
            } else {
                sendMessage(serverChessGame, gameState.toString());
                logGameState(gameState);
            }
        }
    }

    void handleGameStatePromotion(AbstractServerChessGame serverChessGame, GameState gameState) {
            ChessGamePlayer player = serverChessGame.getChessGame().getCurrentPlayer();
            ChessPieceLocation cpl = serverChessGame.getChessGame().getChessBoard().getPawnToBePromoted(player.getColour());
            sendMessageToUser(player.getUserName(), gameState.toString() + " " + cpl.getLocation()); 
    }
    
    void handleGameStatusFinished(AbstractServerChessGame serverChessGame, Object message) {
        if(message instanceof AbstractServerChessGame.ServerGameStatus) {
            ServerGameStatus status = (ServerGameStatus)message;
            if(status == ServerGameStatus.FINISHED) {
                sendMessage(serverChessGame, serverChessGame.getChessGame().getGameState().toString());
                saveGameStateInDatabase(serverChessGame);
            }
        }
    }
    
    void sendMessage(AbstractServerChessGame serverChessGame, String message) {
        this.template.convertAndSend(getMessageDestination(serverChessGame), message, getDefaultHeaders()); 
        logger.debug("Message sent to" + getMessageDestination(serverChessGame));
    }
    
    private void sendMessageToUser(String user, String message) {
    	this.template.convertAndSendToUser(user, MESSAGE_USER_DESTINATION, message, getDefaultHeaders());
    }
    
    private String getMessageDestination(AbstractServerChessGame serverChessGame) {
        return String.format("%s/%d", MESSAGE_DESTINATION, serverChessGame.getUid());
    }
    
    private Map<String,Object> getDefaultHeaders(){
        Map<String,Object> headers = new HashMap<>();
        headers.put(StompController.MESSAGE_HEADER_TYPE, MessageType.STATUS);
        return headers;
    }
    
    private void logGameState(GameState gameState){
        switch(gameState){
        
        case BLACK_CHECKMATE:
            logger.info("White Player has won");
            break;
            
        case WHITE_CHECKMATE:
            logger.info("Black Player has won");
            break;
            
        case STALEMATE:
            logger.info("Game has ended in a draw");
            break;
            
        default:
            break;
        }
    }
    
    private void saveGameStateInDatabase(AbstractServerChessGame serverChessGame) {
        try {
            serverChessGameDAO.saveServerChessGame(serverChessGame);
        } catch (DAOException | OptimisticLockException de) {
            logger.error(de);
        }
    }
    
    @Resource(name = "myServerChessGameDAO")
    public void setServerChessGameDAO(SCGDAOInterface serverChessGameDAO) {
        this.serverChessGameDAO = serverChessGameDAO;
    }
    
    public void setSimpMessagingTemplate(SimpMessagingTemplate simpMessagingTemplate) {
        this.template = simpMessagingTemplate;
    }
    
    SimpMessagingTemplate getSimpMessagingTemplate() {
        return template;
    }
}
