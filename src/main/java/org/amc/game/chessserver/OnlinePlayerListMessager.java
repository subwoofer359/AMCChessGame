package org.amc.game.chessserver;

import com.google.gson.Gson;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class OnlinePlayerListMessager {

    private static final Logger logger = Logger.getLogger(OnlinePlayerListMessager.class);

    /**
     * STOMP messaging object to send stomp message to objects
     */
    SimpMessagingTemplate template;

    private SessionRegistry registry;

    /**
     * STOMP message subscription destination
     */
    static final String MESSAGE_DESTINATION = "/topic/updates/onlineplayerlist";

    public void sendPlayerList() {
        String message = getMessage();
        template.convertAndSend(MESSAGE_DESTINATION, message);
        logger.debug("Sent Player List:" + message);
    }

    private String getMessage() {
        Gson gson = new Gson();
        return gson.toJson(registry.getAllPrincipals());
    }

    @Resource(name = "sessionRegistry")
    public void setSessionRegistry(SessionRegistry registry) {
        this.registry = registry;
    }
    
    @Autowired
    public void setSimpMessagingTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }
}
