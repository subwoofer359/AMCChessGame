package org.amc.game.chessserver;

import org.amc.game.chess.ObservableChessGame;
import org.amc.game.chess.Player;
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

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView joinGame(@ModelAttribute("PLAYER") Player player,
                    @RequestParam long gameUUID) {
        ServerChessGame chessGame = gameMap.get(gameUUID);
        ModelAndView mav = new ModelAndView();
        if (canPlayerJoinGame(chessGame, player)) {
            addPlayerToGame(chessGame, player);
            setupModelForChessGameScreen(mav, gameUUID);
            addView(chessGame);
        } else {
            setModelErrorMessage(chessGame, player, mav);
        }
        return mav;
    }

    private boolean canPlayerJoinGame(ServerChessGame chessGame, Player player) {
        return inAwaitingPlayerState(chessGame) && !isPlayerJoiningOwnGame(chessGame, player);
    }

    private void addPlayerToGame(ServerChessGame chessGame, Player player) {
        chessGame.addOpponent(player);
    }

    private void setupModelForChessGameScreen(ModelAndView mav, long gameUUID) {
        mav.getModel().put("GAME_UUID", gameUUID);
        logger.info(String.format("Chess Game(%d): has been started", gameUUID));
        mav.setViewName("chessGamePortal");
    }

    private void setModelErrorMessage(ServerChessGame chessGame, Player player, ModelAndView mav) {
        if (!inAwaitingPlayerState(chessGame)) {
            setErrorPageAndMessage(mav, "Can't join chess game");
        } else if (isPlayerJoiningOwnGame(chessGame, player)) {
            setErrorPageAndMessage(mav, "Player can't join a game they started");
        }
    }

    private void addView(ServerChessGame chessGame) {
        new JsonChessBoardView(
                        (ObservableChessGame) chessGame.getChessGame(), template);
    }

    private boolean inAwaitingPlayerState(ServerChessGame chessGame) {
        return chessGame.getCurrentStatus().equals(ServerChessGame.status.AWAITING_PLAYER);
    }

    private boolean isPlayerJoiningOwnGame(ServerChessGame chessGame, Player player) {
        return player.equals(chessGame.getPlayer());
    }

    private void setErrorPageAndMessage(ModelAndView mav, String errorMessage) {
        mav.setViewName("forward:/app/chessgame/chessapplication");
        mav.getModel().put("ERRORS", errorMessage);
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
        mav.getModel().put("ERRORS", "Missing GAME Id");
        mav.setViewName("redirect:/app/chessgame/chessapplication");
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
        return "redirect:/app/chessgame/chessapplication";
    }

}
