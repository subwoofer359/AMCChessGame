package org.amc.game.chessserver;

import org.apache.log4j.Logger;
import org.amc.DAOException;
import org.amc.dao.SCGDAOInterface;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes({ "PLAYER", "GAME_UUID" })
public class NewNetworkChessGameController {

    @Autowired
    WebApplicationContext springContext;
 
    private static final Logger logger = Logger.getLogger(NewNetworkChessGameController.class);
    
    private GameControllerHelper helper = new GameControllerHelper(logger);

    @RequestMapping("/chessapplication")
    public ModelAndView chessGameApplication(HttpSession session, @ModelAttribute("PLAYER") Player player) {
        ModelAndView mav = new ModelAndView();
        try {
            mav.getModel().put(ServerConstants.GAMEMAP, helper.getGamesForPlayer(player));
        } catch(DAOException de) {
            logger.error(de);
        }
        mav.setViewName(GameControllerHelper.CHESS_APPLICATION_PAGE);
        return mav;
    }
    
    @RequestMapping(value = "/createGame", params="gameType=NETWORK_GAME")
    public String createGame(Model model, @ModelAttribute("PLAYER") Player player,
                    @RequestParam GameType gameType) {
            return createGameNetwork(model, player);
    }

    public String createGameNetwork(Model model, Player player) {
        long uuid = helper.getNewGameUID();
        AbstractServerChessGame serverGame = helper.getServerChessGame(GameType.NETWORK_GAME, uuid, player);
        model.addAttribute(ServerConstants.GAME_UUID, uuid);
        helper.saveToDatabase(serverGame);
        
        return GameControllerHelper.CHESS_APPLICATION_PAGE;
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
        return GameControllerHelper.TWOVIEW_REDIRECT_PAGE;
    }

    @Resource(name = "myServerChessGameDAO")
    public void setServerChessGameDAO(SCGDAOInterface serverChessGameDAO) {
        this.helper.setDAO(serverChessGameDAO);
    }
    
    @Autowired
    public void setServerChessGameFactory(ServerChessGameFactory scgFactory) {
        this.helper.setServerChessGameFactory(scgFactory);
    }
}
