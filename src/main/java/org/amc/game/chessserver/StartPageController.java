package org.amc.game.chessserver;

import org.apache.log4j.Logger;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.messaging.OfflineChessGameMessager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
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

    enum Views {
        CHESS_APPLICATION_PAGE("ChessApplicationPage"), CREATE_PLAYER_PAGE("CreatePlayer");
        private String pageName;

        private Views(String pageName) {
            this.pageName = pageName;
        }

        public String getPageName() {
            return this.pageName;
        }
    };

    @Autowired
    private WebApplicationContext springContext;
    
    private Map<Long, ServerChessGame> gameMap;
    private static final Logger logger = Logger.getLogger(StartPageController.class);
    static final String FORWARD_PAGE = "forward:/app/chessgame/chessapplication";
    static final String REDIRECT_PAGE = "redirect:/app/chessgame/chessapplication";

    @RequestMapping("/chessapplication")
    public ModelAndView chessGameApplication(HttpSession session) {
        ModelAndView mav = new ModelAndView();
        Player player = (Player) session.getAttribute(ServerConstants.PLAYER);
        if (player == null) {
            mav.setViewName(Views.CREATE_PLAYER_PAGE.getPageName());
            return mav;
        }
        mav.getModel().put(ServerConstants.GAMEMAP, gameMap);
        mav.setViewName(Views.CHESS_APPLICATION_PAGE.getPageName());
        return mav;
    }

    @RequestMapping(value = "/createGame")
    public String createGame(Model model, @ModelAttribute("PLAYER") Player player) {
        long uuid = UUID.randomUUID().getMostSignificantBits();
        ServerChessGame serverGame = new ServerChessGame(uuid, player);
        serverGame.attachObserver(getOfflineChessGameMessager());
        gameMap.put(uuid, serverGame);
        model.addAttribute(ServerConstants.GAME_UUID, uuid);
        return FORWARD_PAGE;
    }
    
    private OfflineChessGameMessager getOfflineChessGameMessager() {
        return (OfflineChessGameMessager)springContext.getBean("offlineChessGameMessager");
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
        return REDIRECT_PAGE;
    }

    @Resource(name = "gameMap")
    public void setGameMap(Map<Long, ServerChessGame> gameMap) {
        this.gameMap = gameMap;
    }
}
