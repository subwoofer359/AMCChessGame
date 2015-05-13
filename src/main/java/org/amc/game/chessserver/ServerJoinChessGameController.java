package org.amc.game.chessserver;

import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.ComparePlayers;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame.ServerGameStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

import java.util.Map;

import javax.annotation.Resource;

@Controller
@SessionAttributes({ "GAME_UUID", "PLAYER" })
@RequestMapping("/joinGame")
public class ServerJoinChessGameController {

    private static final Logger logger = Logger.getLogger(ServerJoinChessGameController.class);

    private Map<Long, ServerChessGame> gameMap;

    @Autowired
    private SimpMessagingTemplate template;

    private GameFinishListener gameFinishListener;

    static final String ERROR_GAME_HAS_NO_OPPONENT = "Game has no opponent assigned";
    static final String ERROR_PLAYER_NOT_OPPONENT = "Player is not playing this chess game";
    static final String ERROR_GAMEOVER = "Chess game is over";
    static final String ERROR_FORWARD_PAGE = "forward:/app/chessgame/chessapplication";
    static final String ERROR_REDIRECT_PAGE = "redirect:/app/chessgame/chessapplication";
    static final String CHESS_PAGE = "chessGamePortal";

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView joinGame(@ModelAttribute("PLAYER") Player player,
                    @RequestParam long gameUUID) {
        ServerChessGame chessGame = gameMap.get(gameUUID);
        ModelAndView mav = new ModelAndView();
        if (isPlayerJoiningOwnGame(chessGame, player)) {
            enterChessGame(mav, chessGame, player, gameUUID);
        } else {
            joinChessGame(mav, chessGame, player, gameUUID);
        }
        return mav;
    }

    private boolean isPlayerJoiningOwnGame(ServerChessGame chessGame, Player player) {
        return ComparePlayers.comparePlayers(player, chessGame.getPlayer());
    }

    private void enterChessGame(ModelAndView mav, ServerChessGame chessGame, Player player,
                    long gameUUID) {
        if (canPlayerEnterGame(chessGame, player)) {
            setupModelForChessGameScreen(mav, chessGame.getPlayer(player), gameUUID);
        } else {
            setModelErrorMessage(chessGame, player, mav);
        }
    }

    private boolean canPlayerEnterGame(ServerChessGame chessGame, Player player) {
        return inProgressState(chessGame);
    }

    public boolean inProgressState(ServerChessGame chessGame) {
        return chessGame.getCurrentStatus().equals(ServerGameStatus.IN_PROGRESS);
    }

    private void joinChessGame(ModelAndView mav, ServerChessGame chessGame, Player player,
                    long gameUUID) {
        if (canPlayerJoinGame(chessGame, player)) {
            if (inAwaitingPlayerState(chessGame)) {
                addPlayerToGame(chessGame, player);
                addView(chessGame);
                addGameListener(chessGame);
            }
            setupModelForChessGameScreen(mav, chessGame.getPlayer(player), gameUUID);
        } else {
            setModelErrorMessage(chessGame, player, mav);
        }
    }

    private boolean canPlayerJoinGame(ServerChessGame chessGame, Player player) {
        return inAwaitingPlayerState(chessGame) || joiningCurrentGame(chessGame, player);
    }

    private void addPlayerToGame(ServerChessGame chessGame, Player player) {
        chessGame.addOpponent(player);
    }

    private void setupModelForChessGameScreen(ModelAndView mav, ChessGamePlayer player,
                    long gameUUID) {
        mav.getModel().put(ServerConstants.GAME_UUID, gameUUID);
        ServerChessGame serverGame = gameMap.get(gameUUID);
        mav.getModel().put(ServerConstants.GAME, serverGame);
        mav.getModel().put(ServerConstants.CHESSPLAYER, player);
        logger.info(String.format("Chess Game(%d): has been started", gameUUID));
        mav.setViewName(CHESS_PAGE);
    }

    private void setModelErrorMessage(ServerChessGame chessGame, Player player, ModelAndView mav) {
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

    private boolean isGameInFinishedState(ServerChessGame chessGame) {
        return ServerChessGame.ServerGameStatus.FINISHED.equals(chessGame.getCurrentStatus());
    }

    private void addView(ServerChessGame chessGame) {
        new JsonChessGameView(chessGame, template);
    }

    private void addGameListener(ServerChessGame chessGame) {
        new GameStateListener(chessGame, template);
        gameFinishListener.addServerChessGame(chessGame);
    }

    private boolean inAwaitingPlayerState(ServerChessGame chessGame) {
        return chessGame.getCurrentStatus()
                        .equals(ServerChessGame.ServerGameStatus.AWAITING_PLAYER);
    }

    private boolean joiningCurrentGame(ServerChessGame chessGame, Player player) {
        if (chessGame.getCurrentStatus().equals(ServerChessGame.ServerGameStatus.IN_PROGRESS)) {
            return ComparePlayers.comparePlayers(player, chessGame.getOpponent());
        } else {
            return false;
        }
    }

    /**
     * Dependency Injection of Map containing current ChessGames
     * 
     * @param gameMap
     *            Map<Log,ServerChessGame>
     */
    @Resource(name = "gameMap")
    public void setGameMap(Map<Long, ServerChessGame> gameMap) {
        this.gameMap = gameMap;
    }

    @Autowired
    public void setGameFinishListener(GameFinishListener gameFinishListener) {
        this.gameFinishListener = gameFinishListener;
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
