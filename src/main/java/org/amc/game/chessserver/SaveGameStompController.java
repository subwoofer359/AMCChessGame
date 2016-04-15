package org.amc.game.chessserver;

import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.SESSION_ATTRIBUTES;

import org.amc.DAOException;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.apache.log4j.Logger;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

import javax.persistence.OptimisticLockException;

@Controller
public class SaveGameStompController extends StompController {
    
    private static final Logger logger = Logger.getLogger(SaveGameStompController.class);
    
    static final String ERROR_UNKNOWN_PLAYER = "The player is not part of this chess game";
    
    static final String SAVE_ERROR_GAME_DOESNT_EXIST_ERROR = "Chess Game(%d) doesn't exist";
    
    static final String SAVE_ERROR_GAME_IS_OVER = "Chess Game can't be saved as it's finished";
    
    static final String SAVE_ERROR_CANT_BE_SAVED = "Error saving the Chess Game";
    
    static final String GAME_SAVED_SUCCESS = "Chess Game has been saved";

    /**
     * Saves the Chess Game in the Underlying Database
     * 
     * @param user Principal who has been authenicated
     * @param wsSession WebSocket Session
     * @param gameUUID ChessGame identifier
     * @param message String from client
     */
    @MessageMapping("/save/{gameUUID}")
    public void save(Principal user, 
                    @Header(SESSION_ATTRIBUTES)Map<String, Object> wsSession,
                    @DestinationVariable long gameUUID, @Payload String message) {
        AbstractServerChessGame serverChessGame = null;
        try {
            serverChessGame = getServerChessGameDAO().getServerChessGame(gameUUID);
        } catch (DAOException de) {
            logger.error(de);
            logger.error("SaveGameStompController: Failed to retrieve game from database");
        }
        String replyMessage="";
        logger.debug("IN STOMP SAVE METHOD");
        
        if(serverChessGame == null ){
            replyMessage = String.format(SAVE_ERROR_GAME_DOESNT_EXIST_ERROR, gameUUID); 
            logger.error(replyMessage);
        } else {
            Player player = (Player) wsSession.get(PLAYER);
            replyMessage = saveServerChessGameIfValidPlayer(player, serverChessGame);     
        }
        sendMessageToUser(user, replyMessage, MessageType.INFO);
        
    }
    
    private boolean isValidPlayer(Player player, AbstractServerChessGame serverChessGame) {
        return (ComparePlayers.comparePlayers(player, serverChessGame.getPlayer()) || 
                        ComparePlayers.comparePlayers(player, serverChessGame.getOpponent()));
    }
    
    private String saveServerChessGameIfValidPlayer(Player player, AbstractServerChessGame serverChessGame) {
        if(isValidPlayer(player, serverChessGame)) {
            return saveServerChessGameIfNotFinished(serverChessGame);
        } else {
            logger.debug(ERROR_UNKNOWN_PLAYER);
            return ERROR_UNKNOWN_PLAYER;
        }
    }

    private String saveServerChessGameIfNotFinished(AbstractServerChessGame serverChessGame) {
        if(ServerGameStatus.FINISHED.equals(serverChessGame.getCurrentStatus())) {
            logger.debug(SAVE_ERROR_GAME_IS_OVER);
            return SAVE_ERROR_GAME_IS_OVER;
        } else {
            try {
                getServerChessGameDAO().saveServerChessGame(serverChessGame);
                return GAME_SAVED_SUCCESS;
            } catch (DAOException | OptimisticLockException de) {
                logger.error(de);
                return SAVE_ERROR_CANT_BE_SAVED;
            }
            
        }
    }
   
}
