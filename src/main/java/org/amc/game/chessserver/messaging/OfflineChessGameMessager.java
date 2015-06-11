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
            ServerChessGame scg = (ServerChessGame) subject;
            try {
                if (message instanceof ChessGame) {
                    final ChessGame chessGame = (ChessGame) message;
                    if (isOnline(getOfflinePlayer(chessGame))) {
                        logger.debug(String.format("OfflineChessMessager: %s is online", getOfflinePlayer(chessGame)));
                        return;
                    }
                    logger.debug(String.format("OfflineChessMessager: %s is offline", getOfflinePlayer(chessGame)));
                    messageService.send(getUser(chessGame), newEmailTemplate(
                                    getOfflinePlayer(chessGame), scg));
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
        return false;
        
    }
    
    private User getUser(ChessGame chessGame) throws DAOException {
        Player otherPlayer = getOfflinePlayer(chessGame);
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
    
    protected abstract EmailTemplate getEmailTemplate();
    
    private Player getOfflinePlayer(ChessGame chessGame) {
        if(ComparePlayers.comparePlayers(chessGame.getCurrentPlayer(), chessGame.getWhitePlayer())) {
            return chessGame.getWhitePlayer();
        } else {
            return chessGame.getBlackPlayer();
        }
    }
    
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
