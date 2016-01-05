package org.amc.game.chessserver;

import org.amc.dao.ServerChessGameDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

/**
 * Handles a WebSocket message received for a move in a chess game
 * 
 * @author Adrian Mclaughlin
 *
 */
public class StompController {

    private ConcurrentMap<Long, AbstractServerChessGame> gameMap;

    /**
     * STOMP messaging object to send stomp message to objects
     */

    private SimpMessagingTemplate template;
    
    private ServerChessGameDAO serverChessGameDAO;
    
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
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put(MESSAGE_HEADER_TYPE, type);
        return headers;
    }
    
    @Resource(name = "gameMap")
    public void setGameMap(ConcurrentMap<Long, AbstractServerChessGame> gameMap) {
        this.gameMap = gameMap;
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
    
    @Autowired
    public void setServerChessDAO(ServerChessGameDAO serverChessGameDAO) {
        this.serverChessGameDAO = serverChessGameDAO;
    }

    public ConcurrentMap<Long, AbstractServerChessGame> getGameMap() {
        return gameMap;
    }

    public SimpMessagingTemplate getTemplate() {
        return template;
    }

    public ServerChessGameDAO getServerChessGameDAO() {
        return serverChessGameDAO;
    }
}
