package org.amc.game.chessserver;

import org.apache.log4j.Logger;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes({ "PLAYER", "GAME_UUID" })
public class StartPageController {

    @Autowired
    WebApplicationContext springContext;
    
    private Map<Long, ServerChessGame> gameMap;
    private static final Logger logger = Logger.getLogger(StartPageController.class);
    
    static final String CHESS_APPLICATION_PAGE = "ChessApplicationPage";
    static final String TWOVIEW_FORWARD_PAGE = "forward:/app/chessgame/chessapplication";
    static final String TWOVIEW_REDIRECT_PAGE = "redirect:/app/chessgame/chessapplication";;
    
    private ServerChessGameFactory scgFactory;

    @RequestMapping("/chessapplication")
    public ModelAndView chessGameApplication(HttpSession session) {
        ModelAndView mav = new ModelAndView();
        mav.getModel().put(ServerConstants.GAMEMAP, gameMap);
        mav.setViewName(CHESS_APPLICATION_PAGE);
        return mav;
    }

    @RequestMapping(value = "/createGame/{gameType}")
    public String createGame(Model model, @ModelAttribute("PLAYER") Player player, 
                    @PathVariable ServerChessGameFactory.GameType gameType) {
        long uuid = UUID.randomUUID().getMostSignificantBits();
        ServerChessGame serverGame = scgFactory.getServerChessGame(gameType, uuid, player);
        gameMap.put(uuid, serverGame);
        model.addAttribute(ServerConstants.GAME_UUID, uuid);
        
        String view = null;
        if(GameType.LOCAL_GAME.equals(gameType)) {
            view = ServerJoinChessGameController.CHESS_PAGE;
        } else {
            view = TWOVIEW_FORWARD_PAGE;
        }
        
        return view;
        
    }
    
    /**
     * Intercept HttpSessionRequiredExceptions and cause a redirect to
     * chessGameApplication handler
     * 
     * @param hsre
     *            HttpSessionRequiredException
     * @return String redirect url
     */
    @ExceptionHandler(HttpSessionRequiredException.class)
    public String handleMissingSessionAttributes(HttpSessionRequiredException hsre) {
        logger.error("HttpSessionRequiredException:" + hsre.getMessage());
        return TWOVIEW_REDIRECT_PAGE;
    }

    @Resource(name = "gameMap")
    public void setGameMap(Map<Long, ServerChessGame> gameMap) {
        this.gameMap = gameMap;
    }
    
    @Autowired
    public void setServerChessGameFactory(ServerChessGameFactory scgFactory) {
        this.scgFactory = scgFactory;
    }
}
