package org.amc.game.chessserver;

import org.amc.DAOException;
import org.amc.dao.SCGDAOInterface;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.Player;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

/**
 * Handles a WebSocket message received for a move in a chess game
 * 
 * @author Adrian Mclaughlin
 *
 */
public class StompController {

    private static final Logger logger = Logger.getLogger(StompController.class);

    /**
     * STOMP messaging object to send stomp message to objects
     */

    private SimpMessagingTemplate template;
    
    private SCGDAOInterface serverChessGameDAO;
    
    public static final String MESSAGE_HEADER_TYPE = "TYPE";
    
    static final String MESSAGE_USER_DESTINATION = "/queue/updates";
    
    static final String MESSAGE_DESTINATION = "/topic/updates/%d";
    
    static final String PLAYER = "PLAYER";
    
    /**
     * Sends a reply to the user
     * 
     * @param user
     *            User to receive message
     * @param message
     *            containing either an error message or information update
     */
    void sendMessageToUser(Principal user, String message, MessageType type) {
        template.convertAndSendToUser(user.getName(), MESSAGE_USER_DESTINATION, 
                        message, getHeaders(type));
    }
    
    /**
     * Sends a reply to the user
     * 
     * @param user
     *            User to receive message
     * @param message
     *            containing either an error message or information update
     */
    void sendMessage(String message, long gameUUID, MessageType type) {
        String msgDestination = String.format(MESSAGE_DESTINATION, gameUUID);
        template.convertAndSend(msgDestination, message, getHeaders(type));
    }

    private Map<String, Object> getHeaders(MessageType type) {
        Map<String, Object> headers = new HashMap<>();
        headers.put(MESSAGE_HEADER_TYPE, type);
        return headers;
    }
    
    boolean isMessageNotEmpty(String message) {
    	return !"".equals(message);
    }
    /**
     * For adding a {@link SimpMessagingTemplate} object to be used for send
     * STOMP messages
     * 
     * @param template
     *            SimpMessagingTemplate
     */
    @Autowired
    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }
    
    @Resource(name="wsServerChessGameDAO")
    public void setServerChessDAO(SCGDAOInterface serverChessGameDAO) {
        this.serverChessGameDAO = serverChessGameDAO;
    }

    public SimpMessagingTemplate getTemplate() {
        return template;
    }

    public SCGDAOInterface getServerChessGameDAO() {
        return serverChessGameDAO;
    }
    
    AbstractServerChessGame getServerChessGame(long gameUid) {
        try {
            return getServerChessGameDAO().getServerChessGame(gameUid);
        } catch(DAOException de) {
            logger.error(de);
            return null;
        }
        
    }
    
    boolean isValidPlayer(Player player, AbstractServerChessGame serverChessGame) {
        return (ComparePlayers.comparePlayers(player, serverChessGame.getPlayer()) || 
                        ComparePlayers.comparePlayers(player, serverChessGame.getOpponent()));
    }
}
