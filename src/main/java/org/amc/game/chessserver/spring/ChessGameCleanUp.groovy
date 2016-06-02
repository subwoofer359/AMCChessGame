package org.amc.game.chessserver.spring

import org.amc.DAOException;
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
        def wac = arg0.getServletContext().getAttribute(SPRING_ROOT);
        def factory = wac.getBean("applicationEntityManagerFactory");
        def entityManager = factory.createEntityManager();
        
        ServerChessGameDAO scgDAO = new ServerChessGameDAO();
        scgDAO.setEntityManager(entityManager);
        try {
            deleteGames(scgDAO);
        } catch(DAOException de) {
            logger.error(de);
        } finally {
            entityManager.close();
        }
    }
    
    private void deleteGames(def scgDAO) {
        List games = scgDAO.findEntities("currentStatus", AbstractServerChessGame.ServerGameStatus.FINISHED);
        games.each({
            logger.info("======================================================");
            scgDAO.deleteEntity(it);
            logger.info("Server Chess Game:(" + it?.getUid() + ") deleted");
            logger.info("======================================================");
        });
    }
 
}
