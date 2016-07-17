package org.amc.game.chessserver.messaging;

import org.amc.DAOException;
import org.amc.User;
import org.amc.dao.DAOInterface;
import org.amc.game.GameObserver;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.amc.util.Subject;
import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.security.core.session.SessionRegistry;

import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;

public class OfflineChessGameMessager extends GameObserver {
	
    private SessionRegistry registry;
    private GameMessageService<EmailTemplate> messageService;
    private static final Logger logger = Logger.getLogger(OfflineChessGameMessager.class);
    private EmailTemplateFactory templateFactory;
    private DAOInterface<User> userDAO;

    @Override
    public void update(Subject subject, Object message) {
        if (subject instanceof AbstractServerChessGame) {
            AbstractServerChessGame serverChessGame = (AbstractServerChessGame) subject;
            try {
                Player player = serverChessGame.getChessGame().getCurrentPlayer(); 
                if (isOffline(player) && doesUserHaveEmailAddress(player)) {
                    if (message instanceof ChessGame) {
                        handleChessGameUpdate(serverChessGame, message);
                    } else if(message instanceof Player) {
                        handlePlayerUpdate(serverChessGame, message);
                    }
                }
            } catch (MailException | MessagingException | DAOException e) {
                logger.error(e);
            }
            
        }
    }
    
    boolean isOffline(Player player) throws DAOException {
        logger.debug(String.format("OfflineChessMessager: %s in Registry", registry.getAllPrincipals()));
        
        List<Object> principals = registry.getAllPrincipals(); 
        for(Object principal:principals) {
            if(principal instanceof org.springframework.security.core.userdetails.User) {
                org.springframework.security.core.userdetails.User user = 
                                (org.springframework.security.core.userdetails.User)principal;
                if(user.getUsername().equals(player.getUserName())){
                    return false;
                }
            }
        }
        logger.debug(String.format("OfflineChessMessager: %s is online", player));
        return true;
        
    }
    
    private boolean doesUserHaveEmailAddress(Player player) throws DAOException {
        User user = getUser(player);
        return user.getEmailAddress() != null;

    }
    
    private void handleChessGameUpdate(AbstractServerChessGame scg, Object message) throws MailException, MessagingException, DAOException{
        final ChessGame chessGame = (ChessGame) message;
        User user = getUser(chessGame);
        Player player = chessGame.getCurrentPlayer();
        logger.debug(String.format("OfflineChessMessager: %s is offline", player));
        messageService.send(user, newMoveUpdateEmail(player, scg));
        
    }
    
    private void handlePlayerUpdate(AbstractServerChessGame scg, Object message) throws MailException, MessagingException, DAOException{
    	User user = getUser(scg.getChessGame());
    	Player player = (Player) message;
    	logger.debug(String.format("OfflineChessMessager: %s is offline", player));
    	if(scg.getCurrentStatus() == ServerGameStatus.IN_PROGRESS) {
    	    messageService.send(user, newPlayerJoinGameEmail(player, scg));
    	} else if(scg.getCurrentStatus() == ServerGameStatus.FINISHED) {
    	    messageService.send(user, newPlayerQuitGameEmail(player, scg));
    	}
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
    
    private EmailTemplate newMoveUpdateEmail(Player player, AbstractServerChessGame serverChessGame) {
        EmailTemplate template = templateFactory.getEmailTemplate(ChessGame.class);
        template.setPlayer(player);
        template.setServerChessGame(serverChessGame);
        return template;
    }
    
    private EmailTemplate newPlayerJoinGameEmail(Player player, AbstractServerChessGame serverChessGame) {
    	EmailTemplate template = templateFactory.getEmailTemplate(Player.class);
        template.setPlayer(player);
        template.setServerChessGame(serverChessGame);
        return template;
    }
    
    private EmailTemplate newPlayerQuitGameEmail(Player player, AbstractServerChessGame serverChessGame) {
        EmailTemplate template = templateFactory.getEmailTemplate(Player.class, serverChessGame.getCurrentStatus());
        template.setPlayer(player);
        template.setServerChessGame(serverChessGame);
        return template;
    }
   
    public void setUserDAO(DAOInterface<User> userDAO) {
        this.userDAO = userDAO;
    }
    
    @Resource(name="messageService")
    public void setMessageService(GameMessageService<EmailTemplate> gameMessageService) {
        this.messageService = gameMessageService;
    }
    
    @Resource(name="emailTemplateFactory")
    public void setEmailTemplateFactory(EmailTemplateFactory templateFactory) {
		this.templateFactory = templateFactory;
	}
    
    @Resource(name="sessionRegistry")
    public void setSessionRegistry(SessionRegistry registry) {
        this.registry = registry;
    }
}
