package org.amc.game.chessserver;

import static org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;

import org.amc.DAOException;
import org.amc.dao.SCGDAOInterface;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ComparePlayers;
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
    private SCGDAOInterface serverChessGameDAO;
    
    static final String ERROR_GAME_HAS_NO_OPPONENT = "Game has no opponent assigned";
    static final String ERROR_PLAYER_NOT_OPPONENT = "Player is not playing this chess game";
    static final String ERROR_GAMEOVER = "Chess game is over";
    static final String ERROR_FORWARD_PAGE = "forward:/app/chessgame/chessapplication";
    static final String ERROR_REDIRECT_PAGE = "redirect:/app/chessgame/chessapplication";
    static final String TWO_VIEW_CHESS_PAGE = "chessGamePortal";
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView joinGame(@ModelAttribute("PLAYER") Player player,
                    @RequestParam long gameUUID) {
        AbstractServerChessGame chessGame = getServerChessGame(gameUUID);
        ModelAndView mav = new ModelAndView();
        if (isPlayerJoiningOwnGame(chessGame, player)) {
            enterChessGame(mav, chessGame, player, gameUUID);
        } else {
            joinChessGame(mav, chessGame, player, gameUUID);
        }
        return mav;
    }

    private AbstractServerChessGame getServerChessGame(long gameUid) {
        try {
            return this.serverChessGameDAO.getServerChessGame(gameUid);
        } catch(DAOException de) {
            logger.error(de);
            return null;
        }
        
    }
    
    private boolean isPlayerJoiningOwnGame(AbstractServerChessGame chessGame, Player player) {
        return ComparePlayers.isSamePlayer(player, chessGame.getPlayer());
    }

    private void enterChessGame(ModelAndView mav, AbstractServerChessGame chessGame, Player player,
                    long gameUUID) {
        if (canPlayerEnterGame(chessGame)) {
            setupModelForChessGameScreen(mav, chessGame.getPlayer(player), gameUUID);
        } else {
            setModelErrorMessage(chessGame, player, mav);
        }
    }

    private boolean canPlayerEnterGame(AbstractServerChessGame chessGame) {
        return inProgressState(chessGame);
    }

    public boolean inProgressState(AbstractServerChessGame chessGame) {
        return ServerGameStatus.IN_PROGRESS.equals(chessGame.getCurrentStatus());
    }

    private void joinChessGame(ModelAndView mav, AbstractServerChessGame chessGame, Player player,
                    long gameUUID) {
        if (canPlayerJoinGame(chessGame, player)) {
            if (inAwaitingPlayerState(chessGame)) {
                addPlayerToGame(chessGame, player);
                saveGameToDatabase(chessGame);
            }
            setupModelForChessGameScreen(mav, chessGame.getPlayer(player), gameUUID);
        } else {
            setModelErrorMessage(chessGame, player, mav);
        }
    }

    private boolean canPlayerJoinGame(AbstractServerChessGame chessGame, Player player) {
        return inAwaitingPlayerState(chessGame) || joiningCurrentGame(chessGame, player);
    }

    private void addPlayerToGame(AbstractServerChessGame chessGame, Player player) {
        chessGame.addOpponent(player);
    }

    private void saveGameToDatabase(AbstractServerChessGame chessGame) {
        try {
            serverChessGameDAO.saveServerChessGame(chessGame);
        } catch (DAOException de) {
            logger.error(de);
        }
    }
    
    private void setupModelForChessGameScreen(ModelAndView mav, ChessGamePlayer player,
                    long gameUUID) {
        mav.getModel().put(ServerConstants.GAME_UUID, gameUUID);
        AbstractServerChessGame serverGame = getServerChessGame(gameUUID);
        mav.getModel().put(ServerConstants.GAME, serverGame);
        mav.getModel().put(ServerConstants.CHESSPLAYER, player);
        mav.setViewName(TWO_VIEW_CHESS_PAGE);
        logger.info(String.format("Chess Game(%d): has been started", gameUUID));
        if (serverGame instanceof TwoViewServerChessGame) {
            mav.getModel().put(ServerConstants.GAME_TYPE, ServerConstants.TWO_VIEW);
        } else {
        	mav.getModel().put(ServerConstants.GAME_TYPE, ServerConstants.ONE_VIEW);
        }
    }

    private void setModelErrorMessage(AbstractServerChessGame chessGame, Player player, ModelAndView mav) {
        if (isPlayerJoiningOwnGame(chessGame, player)) {
            setErrorPageAndMessage(mav, ERROR_GAME_HAS_NO_OPPONENT);
        } else {
            if (isGameInFinishedState(chessGame)) {
                setErrorPageAndMessage(mav, ERROR_GAMEOVER);
            } else {
                setErrorPageAndMessage(mav, ERROR_PLAYER_NOT_OPPONENT);
            }
        }
    }

    private void setErrorPageAndMessage(ModelAndView mav, String errorMessage) {
        mav.setViewName(ERROR_FORWARD_PAGE);
        mav.getModel().put(ServerConstants.ERRORS, errorMessage);
        logger.error(errorMessage);
    }

    private boolean isGameInFinishedState(AbstractServerChessGame chessGame) {
        return ServerGameStatus.FINISHED.equals(chessGame.getCurrentStatus());
    }

    private boolean inAwaitingPlayerState(AbstractServerChessGame chessGame) {
        return ServerGameStatus.AWAITING_PLAYER.equals(chessGame.getCurrentStatus());
    }

    private boolean joiningCurrentGame(AbstractServerChessGame chessGame, Player player) {
        return ServerGameStatus.IN_PROGRESS.equals(chessGame.getCurrentStatus()) &&
            ComparePlayers.isSamePlayer(player, chessGame.getOpponent());
    }

    @Resource(name = "myServerChessGameDAO")
    public void setServerChessGameDAO(SCGDAOInterface serverChessGameDAO) {
        this.serverChessGameDAO = serverChessGameDAO;
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
