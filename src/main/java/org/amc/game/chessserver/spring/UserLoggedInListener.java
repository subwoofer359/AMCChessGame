package org.amc.game.chessserver.spring;

import org.amc.game.chessserver.OnlinePlayerListMessager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionCreationEvent;
import org.springframework.stereotype.Component;

@Component
public class UserLoggedInListener implements ApplicationListener<SessionCreationEvent>{

    @Autowired
    private OnlinePlayerListMessager messager;
    
    public void onApplicationEvent(SessionCreationEvent arg0) {
        messager.sendPlayerList();
    }
}
