package org.amc.game.chessserver;

import org.amc.game.chess.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletContext;

@Controller
@SessionAttributes({"PLAYER","GAME_UUID"})
public class StartPageController {
    
    private ServletContext context;
    
    enum Views {
        CHESS_APPLICATION_PAGE("ChessApplicationPage"),
        CREATE_PLAYER_PAGE("CreatePlayer");
        private String pageName;

        private Views(String pageName) {
            this.pageName = pageName;
        }

        public String getPageName() {
            return this.pageName;
        }
    };
    
    @RequestMapping("/chessapplication")
    public ModelAndView chessGameApplication(Model model){
        ModelAndView mav=new ModelAndView();
        Player player=(Player)model.asMap().get(ServerConstants.PLAYER.toString());
        if(player==null){
            mav.setViewName(Views.CREATE_PLAYER_PAGE.getPageName());
            return mav;
        }
        mav.getModel().put(ServerConstants.PLAYER.toString(),player);
        mav.getModel().put(ServerConstants.GAMEMAP.toString(),getGameMap(context));
        mav.setViewName(Views.CHESS_APPLICATION_PAGE.getPageName());
        return mav;
    }
    
    @RequestMapping(value="/createGame")
    public String createGame(Model model ,@ModelAttribute("PLAYER") Player player){
        ServerChessGame serverGame=new ServerChessGame(player);
        long uuid=UUID.randomUUID().getMostSignificantBits();
        ConcurrentMap<Long, ServerChessGame> gameMap=getGameMap(context);
        gameMap.put(uuid, serverGame);
        model.addAttribute(ServerConstants.GAME_UUID.toString(), uuid);
        return "forward:/app/chessgame/chessapplication";
    }

    @SuppressWarnings(value = "unchecked")
    private ConcurrentMap<Long, ServerChessGame> getGameMap(ServletContext context) {
        synchronized (context) {
            return (ConcurrentMap<Long, ServerChessGame>) context.getAttribute(ServerConstants.GAMEMAP
                            .toString());
        }
    }
    
    /**
     * Intercept HttpSessionRequiredExceptions and cause a redirect to chessGameApplication handler
     * 
     * @param hsre HttpSessionRequiredException
     * @return String redirect url
     */
    @ExceptionHandler(HttpSessionRequiredException.class)
    public String handleMissingSessionAttributes(HttpSessionRequiredException hsre){
        return "redirect:/app/chessgame/chessapplication";
    }
    
    @Autowired
    void setServletContext(ServletContext context){
        this.context=context;
    }
    
}
