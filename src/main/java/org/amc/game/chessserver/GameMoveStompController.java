package org.amc.game.chessserver;

import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.SESSION_ATTRIBUTES;

import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.IllegalMoveException;
import org.amc.game.chess.Move;
import org.amc.game.chess.Player;
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
public class GameMoveStompController extends StompController {
    
    private static final Logger logger = Logger.getLogger(GameMoveStompController.class);
    
    static final String ERROR_MSG_GAME_OVER = "Error:Move on game(%d) which has finished";
    
    static final String ERROR_MSG_NOT_ENOUGH_PLAYERS = "Error:Move on game(%d) which hasn't got two players";

    @MessageMapping("/move/{gameUUID}")
    @SendToUser(value = "/queue/updates", broadcast = false)
    public void registerMove(Principal user,
                    @Header(SESSION_ATTRIBUTES) Map<String, Object> wsSession,
                    @DestinationVariable long gameUUID, @Payload String moveString) {

        logger.debug(String.format("USER(%s)'s move received for game:%d", user.getName(), gameUUID));

        AbstractServerChessGame game = getServerChessGame(gameUUID);
        
        Player player;
        
        if (game instanceof OneViewServerChessGame) {
        	player = game.getChessGame().getCurrentPlayer();
        } else  {
        	player = (Player) wsSession.get("PLAYER");
        }

        logger.debug("PLAYER:" + player);

        String message;

        if (AbstractServerChessGame.ServerGameStatus.IN_PROGRESS.equals(game.getCurrentStatus())) {
            message = moveChessPiece(game, player, moveString);
        } else if (AbstractServerChessGame.ServerGameStatus.AWAITING_PLAYER.equals(game.getCurrentStatus())) {
            message = String.format(ERROR_MSG_NOT_ENOUGH_PLAYERS, gameUUID);
        } else {
            message = String.format(ERROR_MSG_GAME_OVER, gameUUID);
        }

        logger.info(message);

        if(isMessageNotEmpty(message)) { 
            sendMessageToUser(user, message, MessageType.ERROR);
        }
    }
    
    
    private String moveChessPiece(AbstractServerChessGame game, Player player, String moveString) {
        String message = "";

        try {
            ChessGamePlayer gamePlayer = game.getPlayer(player);
            game.move(gamePlayer, new Move(moveString));
        } catch (IllegalMoveException | IllegalArgumentException e) {
            message = "Error:" + e.getMessage();
        }

        return message;
    }
}
