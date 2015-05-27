package org.amc.game.chessserver.messaging;

import com.google.gson.Gson;

import org.amc.game.chess.ChessGame;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.JsonChessGameView;
import org.amc.game.chessserver.ServerChessGame;
import org.amc.util.Observer;
import org.amc.util.Subject;
import org.apache.log4j.Logger;
import org.springframework.security.core.session.SessionRegistry;

public class OfflineChessGameMessager implements Observer {

    private SessionRegistry registry;
    private GameMessageService<String> messageService;
    private static final Logger logger = Logger.getLogger(OfflineChessGameMessager.class);


    @Override
    public void update(Subject subject, Object message) {
        ServerChessGame scg = (ServerChessGame)subject;
        
        if(isOnline(scg.getOpponent())){
            return;
        }
        
        if(subject instanceof ServerChessGame) {
            if(message instanceof ChessGame) {
                try {
                    final Gson gson = new Gson();
                    String jsonBoard = gson.toJson(new JsonChessGameView.JsonChessGame((ChessGame) message));
                    messageService.send(scg.getOpponent(), jsonBoard);
                } catch (Exception e) {
                    logger.error(message);
                }
            }
        }
    }
    
    boolean isOnline(Player player) {
        return registry.getAllPrincipals().contains(player);
    }
}
