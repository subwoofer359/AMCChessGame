package org.amc.game.chessserver;

import org.amc.dao.SCGDAOInterface;
import org.amc.game.chess.Player;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
@SessionAttributes({ "GAME_UUID", "PLAYER" })
@RequestMapping("/joinGame")
public class ServerJoinChessGameController {

    private static final Logger logger = Logger.getLogger(ServerJoinChessGameController.class);
    private JoinGameHelper helper;
    private JoinOwnGameHelper ownGameHelper;
    private JoinOthersGameHelper othersGameHelper; 
    
    static final String ERROR_GAME_HAS_NO_OPPONENT = "Game has no opponent assigned";
    static final String ERROR_PLAYER_NOT_OPPONENT = "Player is not playing this chess game";
    static final String ERROR_GAMEOVER = "Chess game is over";
    static final String ERROR_FORWARD_PAGE = "forward:/app/chessgame/chessapplication";
    static final String ERROR_REDIRECT_PAGE = "redirect:/app/chessgame/chessapplication";
    static final String ERROR_NO_RESOURCES = "The Controller's resources haven't be initialised";
    static final String TWO_VIEW_CHESS_PAGE = "chessGamePortal";
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView joinGame(@ModelAttribute("PLAYER") Player player,
                    @RequestParam long gameUUID) {
    	
    	checkResourcesNotNull();
    	
        AbstractServerChessGame chessGame = helper.getServerChessGame(gameUUID);
        ModelAndView mav = new ModelAndView();
        if (helper.isPlayerJoiningOwnGame(chessGame, player)) {
        	ownGameHelper.enterChessGame(mav, chessGame, player, gameUUID);
        } else {
        	othersGameHelper.joinChessGame(mav, chessGame, player, gameUUID);
        }
        return mav;
    }
    
    private void checkResourcesNotNull() {
    	if(helper == null || ownGameHelper == null || othersGameHelper == null) {
    		throw new NullPointerException(ERROR_NO_RESOURCES);
    	}
    }

    @Resource(name = "myServerChessGameDAO")
    public void setServerChessGameDAO(SCGDAOInterface serverChessGameDAO) {
        this.helper = new JoinGameHelper(serverChessGameDAO);
        this.ownGameHelper = new JoinOwnGameHelper(helper);
        this.othersGameHelper = new JoinOthersGameHelper(helper);
    }

    /**
     * Handles Exception raised when the required parameters are missing form
     * the html response
     * 
     * @param be
     *            MissingServletRequestParameterException
     * @return ModelAndView redirect main page
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingRequestParameter(MissingServletRequestParameterException be) {
        ModelAndView mav = new ModelAndView();
        logger.error("MissingServletRequestParameterException:" + be.getMessage());
        mav.getModel().put(ServerConstants.ERRORS, "Missing GAME Id");
        mav.setViewName(ERROR_REDIRECT_PAGE);
        return mav;
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
        return ERROR_REDIRECT_PAGE;
    }
}
