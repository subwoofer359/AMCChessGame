package org.amc.game.chessserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

@Component
public class UserLoggedListener  implements ApplicationListener<SessionDestroyedEvent>{

    @Autowired
    private OnlinePlayerListMessager messager;
    
    public void onApplicationEvent(SessionDestroyedEvent arg0) {
        messager.sendPlayerList();
    }

}
