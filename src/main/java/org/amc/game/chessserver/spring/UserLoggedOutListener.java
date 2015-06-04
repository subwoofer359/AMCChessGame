package org.amc.game.chessserver.spring;

import org.amc.game.chessserver.OnlinePlayerListMessager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

@Component
public class UserLoggedOutListener  implements ApplicationListener<SessionDestroyedEvent>{

    private static final Logger logger = Logger.getLogger(UserLoggedOutListener.class);
    
    private OnlinePlayerListMessager messager;
    
    public void onApplicationEvent(SessionDestroyedEvent arg0) {
        messager.sendPlayerList();
        logger.debug("UserLoggedInListener:SessionDestroyedEvent received");
    }
    
    @Autowired
    public void setMessager(OnlinePlayerListMessager messager) {
        this.messager = messager;
    }

}
