package org.amc.game.chessserver;

import org.amc.game.chess.Player;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletContext;

@Controller
@SessionAttributes({ "GAME_UUID", "PLAYER" })
@RequestMapping("/joinGame")
public class ServerJoinChessGameController {
    private static final Logger logger = Logger.getLogger(ServerJoinChessGameController.class);
    private ServletContext context;

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView joinGame(
                    @ModelAttribute("GAMEMAP") ConcurrentMap<Long, ServerChessGame> gameMap,
                    @ModelAttribute("PLAYER") Player player, @RequestParam long gameUUID) {
        ServerChessGame chessGame = gameMap.get(gameUUID);
        ModelAndView mav = new ModelAndView();
        if (canPlayerJoinGame(chessGame, player)) {
            addPlayerToGame(chessGame, player);
            setupModelForChessGameScreen(mav, gameUUID);
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

    @Autowired
    void setServletContext(ServletContext context) {
        this.context = context;
    }

    @ModelAttribute("GAMEMAP")
    @SuppressWarnings(value = "unchecked")
    private ConcurrentMap<Long, ServerChessGame> getGameMap() {
        synchronized (context) {
            return (ConcurrentMap<Long, ServerChessGame>) context
                            .getAttribute(ServerConstants.GAMEMAP.toString());
        }
    }
}
