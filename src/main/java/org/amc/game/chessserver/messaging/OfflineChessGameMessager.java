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
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import javax.annotation.Resource;

public class OfflineChessGameMessager implements Observer {

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
                if (isOnline(scg.getOpponent())) {
                    return;
                }
                if (message instanceof ChessGame) {
                    final ChessGame chessGame = (ChessGame) message;
                    messageService.send(getUser(chessGame), newEmailTemplate(
                                    getOfflinePlayer(chessGame), scg));
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }
    
    boolean isOnline(Player player) throws DAOException {
        return registry.getAllPrincipals().contains(getUser(player));
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
    
    private EmailTemplate getEmailTemplate() {
        return (EmailTemplate)springContext.getBean(EmailTemplate.class);
    }
    
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
