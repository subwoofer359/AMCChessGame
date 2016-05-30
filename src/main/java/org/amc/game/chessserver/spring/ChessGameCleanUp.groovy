package org.amc.game.chessserver.spring

import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.ServerChessGame;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

class ChessGameCleanUp implements ServletContextListener {
    
    private static final Logger logger = Logger.getLogger(ChessGameCleanUp.class);
    
    static final String SPRING_ROOT = 'org.springframework.web.context.WebApplicationContext.ROOT';
    
    @Override
    void contextDestroyed(ServletContextEvent arg0) {
        //Do nothing
    }

    @Override
    void contextInitialized(ServletContextEvent arg0) {
        WebApplicationContext wac = (WebApplicationContext)arg0.getServletContext().getAttribute(SPRING_ROOT);
        def factory = wac.getBean("applicationEntityManagerFactory");
        
        ServerChessGameDAO scgDAO = wac.getBean("myServerChessGameDAO");
        List games = scgDAO.findEntities("currentStatus", AbstractServerChessGame.ServerGameStatus.FINISHED);
        games.each({
            logger.info("======================================================");
            scgDAO.deleteEntity(it);
            logger.info("Server Chess Game:(" + it?.getUid() + ") deleted");
            logger.info("======================================================");
        });
    }
 
}
