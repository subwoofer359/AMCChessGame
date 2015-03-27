package org.amc.game.chessserver;

import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGame.GameState;
import org.amc.util.Observer;
import org.amc.util.Subject;
import org.apache.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.HashMap;
import java.util.Map;

public class GameStateListener implements Observer {

    private static final Logger logger = Logger.getLogger(GameStateListener.class);
    
    /** 
     * STOMP messaging object to send stomp message to objects
     */
    private SimpMessagingTemplate template;

    /**
     * STOMP message subscription destination
     */
    private static final String MESSAGE_DESTINATION = "/topic/updates";
    
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
        if (message instanceof ChessGame.GameState) {
            GameState gameState = (GameState) message;
            this.template.convertAndSend(MESSAGE_DESTINATION, gameState, getDefaultHeaders());
            logger.debug("Message sent to" + MESSAGE_DESTINATION);
            logGameState(gameState);
            
        } else {
            // Ignore update notification
        }
    }
    
    private Map<String,Object> getDefaultHeaders(){
        Map<String,Object> headers = new HashMap<String, Object>();
        headers.put(StompConstants.MESSAGE_HEADER_TYPE.getValue(), MessageType.STATUS);
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
            
        default:
            break;
        }
    }

}
