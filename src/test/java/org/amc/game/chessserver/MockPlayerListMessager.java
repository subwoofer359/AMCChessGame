package org.amc.game.chessserver;


import static org.mockito.Mockito.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.session.SessionRegistry;

import javax.annotation.Resource;

public class MockPlayerListMessager extends OnlinePlayerListMessager{

    private static final Logger logger = Logger.getLogger(MockPlayerListMessager.class);
    
    public MockPlayerListMessager() {
        this.template = mock(SimpMessagingTemplate.class);
        logger.debug("MOCK PlayerListMessager added to Application Contexted");
    }
    
    @Override
    @Autowired
    public void setSimpMessagingTemplate(SimpMessagingTemplate template) {
        //do nothing
    }
    
    public SimpMessagingTemplate getSimpMessagingTemplate() {
        return template;
    }

}
