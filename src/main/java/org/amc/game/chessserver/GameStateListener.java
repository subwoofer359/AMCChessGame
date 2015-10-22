package org.amc.game.chessserver;

import org.amc.game.GameObserver;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGame.GameState;
import org.amc.game.chessserver.ServerChessGame.ServerGameStatus;
import org.amc.util.Observer;
import org.amc.util.Subject;
import org.apache.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.HashMap;
import java.util.Map;

public class GameStateListener extends GameObserver {

    private static final Logger logger = Logger.getLogger(GameStateListener.class);
    
    /** 
     * STOMP messaging object to send stomp message to objects
     */
    private final SimpMessagingTemplate template;

    /**
     * STOMP message subscription destination
     */
    static final String MESSAGE_DESTINATION = "/topic/updates";
    
    public GameStateListener(ServerChessGame chessGame, SimpMessagingTemplate template) {
        this.template = template;
        chessGame.attachObserver(this);
    }
    
    /**
     * Called by Model(ObservableChessGame) on change
     * 
     * Converts a ChessBoard to a JSON String and sends it
     * to the subscribed clients
     */
    @Override
    public void update(Subject subject, Object message) {
        if(subject instanceof ServerChessGame) {
            ServerChessGame serverChessGame = (ServerChessGame)subject;
            if (message instanceof ChessGame.GameState) {
                GameState gameState = (GameState) message;
                sendMessage(serverChessGame, gameState.toString());
                logGameState(gameState);   
            } else if(message instanceof ServerChessGame.ServerGameStatus) {
                ServerGameStatus status = (ServerGameStatus)message;
                if(status == ServerGameStatus.FINISHED) {
                    sendMessage(serverChessGame, serverChessGame.getChessGame().getGameState().toString());
                }
            }
        }
    }
    
    private void sendMessage(ServerChessGame serverChessGame, String message) {
        this.template.convertAndSend(getMessageDestination(serverChessGame), message, getDefaultHeaders()); 
        logger.debug("Message sent to" + getMessageDestination(serverChessGame));
    }
    
    private String getMessageDestination(ServerChessGame serverChessGame) {
        return String.format("%s/%d", MESSAGE_DESTINATION, serverChessGame.getUid());
    }
    
    private Map<String,Object> getDefaultHeaders(){
        Map<String,Object> headers = new HashMap<String, Object>();
        headers.put(StompConstants.MESSAGE_HEADER_TYPE, MessageType.STATUS);
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

}
