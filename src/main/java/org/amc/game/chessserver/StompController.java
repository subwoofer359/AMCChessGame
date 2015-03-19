package org.amc.game.chessserver;

import com.google.gson.Gson;

import org.amc.game.chess.InvalidMoveException;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.JsonChessBoardView.JsonChessBoard;
import org.apache.log4j.Logger;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
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

    @MessageMapping("/move/{gameUUID}")
    @SendToUser(value = "/queue/updates", broadcast = false)
    public String registerMove(Principal user,
                    @Header(SESSION_ATTRIBUTES) Map<String, Object> wsSession,
                    @DestinationVariable long gameUUID, @Payload String moveString) {

        logger.debug(String.format("USER(%s)'s move received for game:%d", user.getName(), gameUUID));
        MoveEditor convertor = new MoveEditor();
        convertor.setAsText(moveString);
        logger.debug(convertor.getValue());
        Move move = (Move) convertor.getValue();

        Player player = (Player) wsSession.get("PLAYER");

        logger.debug("PLAYER:" + player);

        ServerChessGame game = gameMap.get(gameUUID);
        String message = "";
        if (game.getCurrentStatus().equals(ServerChessGame.status.IN_PROGRESS)) {
            try {
                game.move(player, move);
            } catch (InvalidMoveException e) {
                message = "Error:" + e.getMessage();
            }
        } else if (game.getCurrentStatus().equals(ServerChessGame.status.AWAITING_PLAYER)) {
            message = String.format("Error:Move on game(%d) which hasn't got two players", gameUUID);

        } else if (game.getCurrentStatus().equals(ServerChessGame.status.FINISHED)) {
            message = String.format("Error:Move on game(%d) which has finished", gameUUID);
        }
        logger.error(message);
        return message;
    }


    @MessageMapping("/get/{gameUUID}")
    @SendToUser(value = "/queue/updates", broadcast = false)
    public String getChessBoard(Principal user,
                    @Header(SESSION_ATTRIBUTES) Map<String, Object> wsSession,
                    @DestinationVariable long gameUUID, @Payload String message) {
        ServerChessGame serverGame = gameMap.get(gameUUID);
        Gson gson = new Gson();
        JsonChessBoard jcb=new JsonChessBoard(serverGame.getChessGame());
        logger.debug(wsSession.get("PLAYER")+" requested update for game:"+gameUUID);
        return gson.toJson(jcb);
    }
    
    @Resource(name = "gameMap")
    public void setGameMap(Map<Long, ServerChessGame> gameMap) {
        this.gameMap = gameMap;
    }

}
