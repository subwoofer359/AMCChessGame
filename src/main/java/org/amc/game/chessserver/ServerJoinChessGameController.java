package org.amc.game.chessserver;

import org.amc.game.chess.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletContext;

@Controller
@SessionAttributes({"GAME_UUID","PLAYER"})
@RequestMapping("/joinGame")
public class ServerJoinChessGameController {
    private ServletContext context;
    
    @RequestMapping(method=RequestMethod.POST)
    public void joinGame(@ModelAttribute("PLAYER") Player player,@RequestParam long gameUUID){
        
    }

    @Autowired
    void setServletContext(ServletContext context){
        this.context=context;
    }
    
    @ModelAttribute("GAMEMAP")
    @SuppressWarnings(value = "unchecked")
    private ConcurrentMap<Long, ServerChessGame> getGameMap() {
        synchronized (context) {
            return (ConcurrentMap<Long, ServerChessGame>) context.getAttribute(ServerConstants.GAMEMAP
                            .toString());
        }
    }
}
