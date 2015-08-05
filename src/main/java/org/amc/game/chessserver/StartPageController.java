package org.amc.game.chessserver;

import org.apache.log4j.Logger;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
      
    static final String CHESS_APPLICATION_PAGE = "ChessApplicationPage";
    static final String TWOVIEW_FORWARD_PAGE = "forward:/app/chessgame/chessapplication";
    static final String TWOVIEW_REDIRECT_PAGE = "redirect:/app/chessgame/chessapplication";
    static final String ONE_VIEW_CHESS_PAGE = "OneViewChessGamePortal";
    
    private Map<Long, ServerChessGame> gameMap;
    private static final Logger logger = Logger.getLogger(StartPageController.class);
    private SCGInitialiser initialiser;
    private ServerChessGameFactory scgFactory;
    

    @RequestMapping("/chessapplication")
    public ModelAndView chessGameApplication(HttpSession session) {
        ModelAndView mav = new ModelAndView();
        mav.getModel().put(ServerConstants.GAMEMAP, gameMap);
        mav.setViewName(CHESS_APPLICATION_PAGE);
        return mav;
    }

    @RequestMapping(value = "/createGame/NETWORK_GAME")
    public String createGame(Model model, @ModelAttribute("PLAYER") Player player) {
        long uuid = UUID.randomUUID().getMostSignificantBits();
        ServerChessGame serverGame = scgFactory.getServerChessGame(GameType.NETWORK_GAME, uuid, player);
        gameMap.put(uuid, serverGame);
        model.addAttribute(ServerConstants.GAME_UUID, uuid);
        
        return TWOVIEW_REDIRECT_PAGE;
    }
    
    @RequestMapping(value = "/createGame/LOCAL_GAME")
    public String createLocalGame(Model model, @ModelAttribute("PLAYER") Player player, 
                    @RequestParam String playersName) {
        long uuid = UUID.randomUUID().getMostSignificantBits();
        ServerChessGame serverGame = scgFactory.getServerChessGame(GameType.LOCAL_GAME, uuid, player);
        gameMap.put(uuid, serverGame);
        model.addAttribute(ServerConstants.GAME_UUID, uuid);
        model.addAttribute(ServerConstants.GAME, serverGame);
        model.addAttribute(ServerConstants.CHESSPLAYER, serverGame.getPlayer(player));
        
        Player opponent = new HumanPlayer(playersName);
        opponent.setUserName(playersName.toLowerCase().split(" ")[0]);
        serverGame.addOpponent(opponent);
        initialiser.init(serverGame);
        
        return ONE_VIEW_CHESS_PAGE;
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
    
    @Resource(name = "sCGInitialiser")
    public void setSCGInitialiser(SCGInitialiser initialiser) {
        this.initialiser = initialiser;
    }
}
