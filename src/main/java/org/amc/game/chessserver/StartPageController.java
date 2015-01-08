package org.amc.game.chessserver;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessGame;
import org.amc.game.chess.Colour;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes("PLAYER")
public class StartPageController {
    enum Views {
        CHESS_APPLICATION_PAGE("ChessApplicationPage");
        private String pageName;

        private Views(String pageName) {
            this.pageName = pageName;
        }

        public String getPageName() {
            return this.pageName;
        }
    };
    
    @RequestMapping("/test")
    public String test(HttpSession session){
        Player player=new HumanPlayer("Adrian Mclaughlin",Colour.WHITE);
        session.setAttribute(ServerConstants.PLAYER.toString(), player);
        return "forward:/app/chessgame/chessapplication";
    }
    
    @RequestMapping("/chessapplication")
    public ModelAndView chessGameApplication(HttpSession session){
        ModelAndView mav=new ModelAndView();
        mav.getModel().put(ServerConstants.PLAYER.toString(), session.getAttribute(ServerConstants.PLAYER.toString()));
        mav.getModel().put(ServerConstants.GAMEMAP.toString(),getGameMap(session.getServletContext()));
        mav.setViewName(Views.CHESS_APPLICATION_PAGE.getPageName());
        return mav;
    }
    
    
    @RequestMapping(value="/createGame")
    public void createGame(HttpSession session,@ModelAttribute("PLAYER") Player player){
        ChessGame chessGame=new ChessGame(new ChessBoard(),player,null);
        long uuid=UUID.randomUUID().getMostSignificantBits();
        ConcurrentMap<Long, ChessGame> gameMap=getGameMap(session.getServletContext());
        gameMap.put(uuid, chessGame);
        session.setAttribute(ServerConstants.GAME_UUID.toString(), uuid);
    }
    
    @RequestMapping("/joinGame")
    public void joinGame(Player player,@RequestParam long gameUUID){
        
    }
    
    @SuppressWarnings(value = "unchecked")
    private ConcurrentMap<Long, ChessGame> getGameMap(ServletContext context) {
        synchronized (context) {
            return (ConcurrentMap<Long, ChessGame>) context.getAttribute(ServerConstants.GAMEMAP
                            .toString());
        }
    }
}
