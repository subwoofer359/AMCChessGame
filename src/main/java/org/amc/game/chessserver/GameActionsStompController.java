package org.amc.game.chessserver;

import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.SESSION_ATTRIBUTES;

import com.google.gson.Gson;

import org.amc.game.chess.ChessGame;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.amc.game.chessserver.observers.JsonChessGameView.JsonChessGame;
import org.apache.log4j.Logger;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Controller
public class GameActionsStompController extends StompController {

    private static final Logger logger = Logger.getLogger(GameActionsStompController.class);
    
    static final String MSG_PLAYER_HAS_QUIT = "%s has quit the game";
    
    static final String ERROR_CHESSBOARD_DOESNT_EXIST = "The requested Chess board doesn't exist";
    
    static final String MSG_GAME_ALREADY_OVER = "Game already over";
    
    @MessageMapping("/get/{gameUUID}")
    @SendToUser(value = "/queue/updates", broadcast = false)
    public void getChessBoard(Principal user,
                    @Header(SESSION_ATTRIBUTES) Map<String, Object> wsSession,
                    @DestinationVariable long gameUUID, @Payload String message) {
        ChessGame chessGame = getGameMap().get(gameUUID) == null ? null : getGameMap()
                        .get(gameUUID).getChessGame();
        String payLoadMessage = null;
        MessageType messageType;
        if(chessGame == null) {
            payLoadMessage = ERROR_CHESSBOARD_DOESNT_EXIST;
            logger.error("Game:" + gameUUID + " has a null ChessGame");
            messageType = MessageType.ERROR;
        } else {
            Gson gson = new Gson();
            JsonChessGame jcb = new JsonChessGame(chessGame);
            logger.debug(wsSession.get("PLAYER") + " requested update for game:" + gameUUID);
            payLoadMessage = gson.toJson(jcb);
            messageType = MessageType.UPDATE;
        }
        sendMessageToUser(user, payLoadMessage, messageType);
        
    }
    
    @MessageMapping("/quit/{gameUUID}")
    public void quitChessGame(Principal user,
                    @Header(SESSION_ATTRIBUTES) Map<String, Object> wsSession,
                    @DestinationVariable long gameUUID, @Payload String message) {
        ServerChessGame serverGame = getGameMap().get(gameUUID);
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
}
