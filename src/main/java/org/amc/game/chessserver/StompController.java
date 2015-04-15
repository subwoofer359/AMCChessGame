package org.amc.game.chessserver;

import com.google.gson.Gson;

import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.JsonChessGameView.JsonChessGame;
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

import javax.annotation.Resource;

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

    private Map<Long, ServerChessGame> gameMap;

    /**
     * STOMP messaging object to send stomp message to objects
     */

    private SimpMessagingTemplate template;

    private static final String ERROR_MSG_NOT_ENOUGH_PLAYERS = "Error:Move on game(%d) which hasn't got two players";

    private static final String ERROR_MSG_GAME_OVER = "Error:Move on game(%d) which has finished";

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

        if (game.getCurrentStatus().equals(ServerChessGame.ServerGameStatus.IN_PROGRESS)) {
            message = moveChessPiece(game, player, moveString);
        } else if (game.getCurrentStatus().equals(ServerChessGame.ServerGameStatus.AWAITING_PLAYER)) {
            message = String.format(ERROR_MSG_NOT_ENOUGH_PLAYERS, gameUUID);
        } else if (game.getCurrentStatus().equals(ServerChessGame.ServerGameStatus.FINISHED)) {
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
            game.move(gamePlayer, getMoveFromString(moveString));
        } catch (IllegalMoveException e) {
            message = "Error:" + e.getMessage();
        } catch (MalformedMoveException mme) {
            message = "Error:" + mme.getMessage();
        }

        return message;
    }

    private Move getMoveFromString(String moveString) {
        MoveEditor convertor = new MoveEditor();
        convertor.setAsText(moveString);
        logger.debug(convertor.getValue());
        return (Move) convertor.getValue();
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

    @Resource(name = "gameMap")
    public void setGameMap(Map<Long, ServerChessGame> gameMap) {
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

}
