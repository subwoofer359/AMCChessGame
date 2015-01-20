package org.amc.game.chessserver;

import org.amc.game.chess.Player;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

@Controller
@SessionAttributes({"PLAYER","GAME_UUID"})
public class StartPageController {
    
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
    
    private Map<Long, ServerChessGame> gameMap;
    
    @RequestMapping("/chessapplication")
    public ModelAndView chessGameApplication(Model model){
        ModelAndView mav=new ModelAndView();
        
        System.out.println("******************"+model.containsAttribute("PLAYER")+"***************");
        for(String key:model.asMap().keySet()){
            System.out.println(">>>>>>>"+key);
        }
        
        Player player=(Player)model.asMap().get(ServerConstants.PLAYER.toString());
        if(player==null){
            System.out.println("==================Player is Null===================");
            mav.setViewName(Views.CREATE_PLAYER_PAGE.getPageName());
            return mav;
        }
        mav.getModel().put(ServerConstants.PLAYER.toString(),player);
        mav.getModel().put(ServerConstants.GAMEMAP.toString(),gameMap);
        mav.setViewName(Views.CHESS_APPLICATION_PAGE.getPageName());
        return mav;
    }
    
    @RequestMapping(value="/createGame")
    public String createGame(Model model ,@ModelAttribute("PLAYER") Player player){
        ServerChessGame serverGame=new ServerChessGame(player);
        long uuid=UUID.randomUUID().getMostSignificantBits();
        gameMap.put(uuid, serverGame);
        model.addAttribute(ServerConstants.GAME_UUID.toString(), uuid);
        return "forward:/app/chessgame/chessapplication";
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
    
    @Resource(name="gameMap")
    public void setGameMap(Map<Long, ServerChessGame> gameMap){
        this.gameMap=gameMap;
    }
    
}
