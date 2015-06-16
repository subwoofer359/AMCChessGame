package org.amc.game.chessserver.messaging;


import org.amc.DAOException;
import org.amc.User;
import org.amc.dao.DAO;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.util.Observer;
import org.amc.util.Subject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;

public abstract class OfflineChessGameMessager implements Observer {

    @Autowired
    private WebApplicationContext springContext;
    private SessionRegistry registry;
    private GameMessageService<EmailTemplate> messageService;
    private static final Logger logger = Logger.getLogger(OfflineChessGameMessager.class);
    private DAO<User> userDAO;

    @Override
    public void update(Subject subject, Object message) {
        if (subject instanceof ServerChessGame) {
            ServerChessGame serverChessGame = (ServerChessGame) subject;
            try {
                Player player = serverChessGame.getChessGame().getCurrentPlayer(); 
                if (isOnline(player) || doesUserNotHaveEmailAddress(player)) {
                    return;
                } else if (message instanceof ChessGame) {
                    handleChessGameUpdate(serverChessGame, message);
                }
            } catch (MailException e) {
                logger.error(e);
            } catch(MessagingException me) {
                logger.error(me);
            } catch (DAOException de) {
                logger.error(de);
            }
            
        }
    }
    
    boolean isOnline(Player player) throws DAOException {
        logger.debug(String.format("OfflineChessMessager: %s in Registry", registry.getAllPrincipals()));
        
        List<Object> principals = registry.getAllPrincipals(); 
        for(Object principal:principals) {
            if(principal instanceof org.springframework.security.core.userdetails.User) {
                org.springframework.security.core.userdetails.User user = 
                                (org.springframework.security.core.userdetails.User)principal;
                if(user.getUsername().equals(player.getUserName())){
                    return true;
                }
            }
        }
        logger.debug(String.format("OfflineChessMessager: %s is online", player));
        return false;
        
    }
    
    private boolean doesUserNotHaveEmailAddress(Player player) throws DAOException {
        User user = getUser(player);
        if(user.getEmailAddress() == null) {
            logger.debug(String.format("OfflineChessMessager: %s has no email address", player.getName() ));
            return true;
        } else {
            return false;
        }
    }
    
    private void handleChessGameUpdate(ServerChessGame scg, Object message) throws MailException, MessagingException, DAOException{
        final ChessGame chessGame = (ChessGame) message;
        User user = getUser(chessGame);
        Player player = chessGame.getCurrentPlayer();
        logger.debug(String.format("OfflineChessMessager: %s is offline", player));
           messageService.send(user, newEmailTemplate(player, scg));
        
    }
    
    
    
    private User getUser(ChessGame chessgame) throws DAOException {
        Player otherPlayer = chessgame.getCurrentPlayer();
        return getUser(otherPlayer);
    }
    
    private User getUser(Player player) throws DAOException {
        List<User> users = userDAO.findEntities("userName", player.getUserName());
        if(users.size() == 1) {
            return users.get(0);
        } else {
            throw new DAOException("No User found with that username");
        }
    }
    
    private EmailTemplate newEmailTemplate(Player player, ServerChessGame serverChessGame) {
        EmailTemplate template = getEmailTemplate();
        template.setPlayer(player);
        template.setServerChessGame(serverChessGame);
        return template;
    }
    
    /**
     * Factory method
     * @return EmailTemplate
     */
    protected abstract EmailTemplate getEmailTemplate();
    
    @Autowired
    public void setUserDAO(DAO<User> userDAO) {
        this.userDAO = userDAO;
    }
    
    @Autowired
    public void setMessageService(GameMessageService<EmailTemplate> gameMessageService) {
        this.messageService = gameMessageService;
    }
    
    @Resource(name="sessionRegistry")
    public void setSessionRegistry(SessionRegistry registry) {
        this.registry = registry;
    }
}
