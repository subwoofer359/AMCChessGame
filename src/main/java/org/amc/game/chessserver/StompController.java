package org.amc.game.chessserver;

import com.google.gson.Gson;

import org.amc.DAOException;
import org.amc.EntityManagerThreadLocal;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame.ServerGameStatus;
import org.amc.game.chessserver.observers.JsonChessGameView.JsonChessGame;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;
import javax.persistence.OptimisticLockException;

import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.SESSION_ATTRIBUTES;

/**
 * Handles a WebSocket message received for a move in a chess game
 * 
 * @author Adrian Mclaughlin
 *
 */
@Controller
public class StompController {

    private static final Logger logger = Logger.getLogger(StompController.class);

    private ConcurrentMap<Long, ServerChessGame> gameMap;

    /**
     * STOMP messaging object to send stomp message to objects
     */

    private SimpMessagingTemplate template;
    
    private ServerChessGameDAO serverChessGameDAO;
    
    static final String ERROR_UNKNOWN_PLAYER = "The player is not part of this chess game";

    static final String ERROR_MSG_NOT_ENOUGH_PLAYERS = "Error:Move on game(%d) which hasn't got two players";

    static final String ERROR_MSG_GAME_OVER = "Error:Move on game(%d) which has finished";
    
    static final String MSG_PLAYER_HAS_QUIT = "%s has quit the game";
    
    static final String MSG_GAME_ALREADY_OVER = "Game already over";
    
    static final String SAVE_ERROR_GAME_DOESNT_EXIST_ERROR = "Chess Game(%d) doesn't exist";
    
    static final String SAVE_ERROR_GAME_IS_OVER = "Chess Game can't be saved as it's finished";
    
    static final String SAVE_ERROR_CANT_BE_SAVED = "Error saving the Chess Game";
    
    static final String GAME_SAVED_SUCCESS = "Chess Game has been saved";

    @MessageMapping("/move/{gameUUID}")
    @SendToUser(value = "/queue/updates", broadcast = false)
    public void registerMove(Principal user,
                    @Header(SESSION_ATTRIBUTES) Map<String, Object> wsSession,
                    @DestinationVariable long gameUUID, @Payload String moveString) {

        logger.debug(String.format("USER(%s)'s move received for game:%d", user.getName(), gameUUID));

        Player player = (Player) wsSession.get("PLAYER");

        logger.debug("PLAYER:" + player);

        ServerChessGame game = gameMap.get(gameUUID);

        String message = "";

        if (ServerChessGame.ServerGameStatus.IN_PROGRESS.equals(game.getCurrentStatus())) {
            message = moveChessPiece(game, player, moveString);
        } else if (ServerChessGame.ServerGameStatus.AWAITING_PLAYER.equals(game.getCurrentStatus())) {
            message = String.format(ERROR_MSG_NOT_ENOUGH_PLAYERS, gameUUID);
        } else {
            message = String.format(ERROR_MSG_GAME_OVER, gameUUID);
        }

        logger.error(message);

        MessageType type = "".equals(message) ? MessageType.INFO : MessageType.ERROR;
        sendMessageToUser(user, message, type);
    }

    private String moveChessPiece(ServerChessGame game, Player player, String moveString) {
        String message = "";

        try {
            ChessGamePlayer gamePlayer = ComparePlayers.comparePlayers(game.getPlayer(), player) ? game.getPlayer() : game.getOpponent();
            game.move(gamePlayer, new Move(moveString));
        } catch (IllegalMoveException | IllegalArgumentException e) {
            message = "Error:" + e.getMessage();
        } catch (MalformedMoveException mme) {
            message = "Error:" + mme.getMessage();
        }

        return message;
    }

    /**
     * Sends a reply to the user
     * 
     * @param user
     *            User to receive message
     * @param message
     *            containing either an error message or information update
     */
    private void sendMessageToUser(Principal user, String message, MessageType type) {
        template.convertAndSendToUser(user.getName(), "/queue/updates", message, getHeaders(type));
    }
    
    @MessageMapping("/oneViewMove/{gameUUID}")
    @SendToUser(value = "/queue/updates", broadcast = false)
    public void registerOneViewMoveMove(Principal user,
                    @DestinationVariable long gameUUID, @Payload String moveString) {

        ServerChessGame game = gameMap.get(gameUUID);
        
        if(!(game instanceof OneViewServerChessGame)) {
            logger.error("Can only move Chess Piece on an One View Chess game");
            return;
        }

        String message = "";
       
        if (ServerGameStatus.IN_PROGRESS.equals(game.getCurrentStatus())) {
            Player player = game.getChessGame().getCurrentPlayer();
            message = moveChessPiece(game, player, moveString);
           } else if (ServerGameStatus.AWAITING_PLAYER.equals(game.getCurrentStatus())) {
               message = String.format(ERROR_MSG_NOT_ENOUGH_PLAYERS, gameUUID);
               logger.error(message);
           } else {
               message = String.format(ERROR_MSG_GAME_OVER, gameUUID);
               logger.error(message);
           }
        MessageType type = "".equals(message) ? MessageType.INFO : MessageType.ERROR;
        sendMessageToUser(user, message, type);
         
    }
    
    /**
     * Sends a reply to the user
     * 
     * @param user
     *            User to receive message
     * @param message
     *            containing either an error message or information update
     */
    private void sendMessage(String message, long gameUUID, MessageType type) {
        String msgDestination = String.format("/topic/updates/%d", gameUUID);
        template.convertAndSend(msgDestination, message, getHeaders(type));
    }

    private Map<String, Object> getHeaders(MessageType type) {
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put(StompConstants.MESSAGE_HEADER_TYPE, type);
        return headers;
    }

    @MessageMapping("/get/{gameUUID}")
    @SendToUser(value = "/queue/updates", broadcast = false)
    public void getChessBoard(Principal user,
                    @Header(SESSION_ATTRIBUTES) Map<String, Object> wsSession,
                    @DestinationVariable long gameUUID, @Payload String message) {
        ServerChessGame serverGame = gameMap.get(gameUUID);
        Gson gson = new Gson();
        JsonChessGame jcb = new JsonChessGame(serverGame.getChessGame());
        logger.debug(wsSession.get("PLAYER") + " requested update for game:" + gameUUID);
        sendMessageToUser(user, gson.toJson(jcb), MessageType.UPDATE);
    }
    
    @MessageMapping("/quit/{gameUUID}")
    public void quitChessGame(Principal user,
                    @Header(SESSION_ATTRIBUTES) Map<String, Object> wsSession,
                    @DestinationVariable long gameUUID, @Payload String message) {
        ServerChessGame serverGame = gameMap.get(gameUUID);
        Player player = (Player) wsSession.get("PLAYER");
        
        String replyMessage="";
        
        if(ServerGameStatus.FINISHED.equals(serverGame.getCurrentStatus())){
            replyMessage = MSG_GAME_ALREADY_OVER;
        } else {        
            serverGame.setCurrentStatus(ServerGameStatus.FINISHED);
            serverGame.notifyObservers(player);
            replyMessage = String.format(MSG_PLAYER_HAS_QUIT, player.getName());
        }
        sendMessage(replyMessage, gameUUID, MessageType.INFO);
    }
    
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
        ServerChessGame serverChessGame = gameMap.get(gameUUID);
        String replyMessage="";
        logger.debug("IN STOMP SAVE METHOD");
        
        if(serverChessGame == null ){
            replyMessage = String.format(SAVE_ERROR_GAME_DOESNT_EXIST_ERROR, gameUUID); 
            logger.error(replyMessage);
        } else {
            Player player = (Player) wsSession.get("PLAYER");
            replyMessage = saveServerChessGameIfValidPlayer(player, serverChessGame);     
        }
        sendMessageToUser(user, replyMessage, MessageType.INFO);
        
    }
    
    private boolean isValidPlayer(Player player, ServerChessGame serverChessGame) {
        return (ComparePlayers.comparePlayers(player, serverChessGame.getPlayer()) || 
                        ComparePlayers.comparePlayers(player, serverChessGame.getOpponent()));
    }
    
    private String saveServerChessGameIfValidPlayer(Player player, ServerChessGame serverChessGame) {
        if(isValidPlayer(player, serverChessGame)) {
            return saveServerChessGameIfNotFinished(serverChessGame);
        } else {
            logger.debug(ERROR_UNKNOWN_PLAYER);
            return ERROR_UNKNOWN_PLAYER;
        }
    }

    private String saveServerChessGameIfNotFinished(ServerChessGame serverChessGame) {
        if(ServerGameStatus.FINISHED.equals(serverChessGame.getCurrentStatus())) {
            logger.debug(SAVE_ERROR_GAME_IS_OVER);
            return SAVE_ERROR_GAME_IS_OVER;
        } else {
            return saveServerChessGame(serverChessGame);
        }
    }
    
    private String saveServerChessGame(ServerChessGame serverChessGame) {
        try {
            gameMap.replace(serverChessGame.getUid(), serverChessGameDAO.saveServerChessGame(serverChessGame));
            EntityManagerThreadLocal.closeEntityManager();
            logger.debug(GAME_SAVED_SUCCESS);
            return GAME_SAVED_SUCCESS;
        } catch(OptimisticLockException | DAOException de) {
            logger.error(de);
            logger.debug(SAVE_ERROR_CANT_BE_SAVED);
            return SAVE_ERROR_CANT_BE_SAVED;
        }
    }

    @Resource(name = "gameMap")
    public void setGameMap(ConcurrentMap<Long, ServerChessGame> gameMap) {
        this.gameMap = gameMap;
    }

    /**
     * For adding a {@link SimpMessagingTemplate} object to be used for send
     * STOMP messages
     * 
     * @param template
     *            SimpMessagingTemplate
     */
    @Autowired
    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }
    
    @Autowired
    public void setServerChessDAO(ServerChessGameDAO serverChessGameDAO) {
        this.serverChessGameDAO = serverChessGameDAO;
    }

}
