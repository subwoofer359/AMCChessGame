package org.amc.game.chessserver.observers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public abstract class MessagingGameObserverFactory implements ObserverFactory {

    @Autowired
    private SimpMessagingTemplate msgTemplate;
    
    void setMessagingTemplate(SimpMessagingTemplate  msgTemplate) {
        this.msgTemplate = msgTemplate;
    }
    
    SimpMessagingTemplate getMessagingTemplate() {
        return msgTemplate;
    }
}
