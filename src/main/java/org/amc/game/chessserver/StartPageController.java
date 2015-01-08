package org.amc.game.chessserver;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.Player;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

@Controller
public class StartPageController {
    
    @RequestMapping(value="/createGame")
    public void createGame(ServletContext context,HttpSession session,Player player){
        ChessGame chessGame=new ChessGame(new ChessBoard(),player,null);
        long uuid=UUID.randomUUID().getMostSignificantBits();
        ConcurrentMap<Long, ChessGame> gameMap=getGameMap(context);
        gameMap.put(uuid, chessGame);
        session.setAttribute(ServerConstants.GAME_UUID.toString(), uuid);
    }
    
    @RequestMapping("/joinGame")
    public void joinGame(Player player,@RequestParam long gameUUID){
        
    }
    
    @SuppressWarnings(value = "unchecked")
    private ConcurrentMap<Long, ChessGame> getGameMap(ServletContext context) {
        synchronized (context) {
            return (ConcurrentMap<Long, ChessGame>) context.getAttribute(ServerConstants.GAME_MAP
                            .toString());
        }
    }
}
