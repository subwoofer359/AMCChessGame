package org.amc.game.chessserver;

import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame.ServerGameStatus;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/enterGame")
public class EnterChessGameController {

    private static final Logger logger = Logger.getLogger(EnterChessGameController.class);

    private Map<Long, ServerChessGame> gameMap;

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView joinGame(@ModelAttribute("PLAYER") Player player,
                    @RequestParam long gameUUID) {
        ModelAndView mav = new ModelAndView();
        ServerChessGame chessGame = gameMap.get(gameUUID);

        if (canPlayerEnterGame(chessGame, player)) {
            setupModelForChessGameScreen(mav, gameUUID);
        } else {
            setModelErrorMessage(chessGame, player, mav);
        }
        return mav;
    }

    private boolean canPlayerEnterGame(ServerChessGame chessGame, Player player) {
        return inProgressState(chessGame) && isPlayerJoiningOwnGame(chessGame, player);
    }

    public boolean inProgressState(ServerChessGame chessGame) {
        return chessGame.getCurrentStatus().equals(ServerGameStatus.IN_PROGRESS);
    }

    private boolean isPlayerJoiningOwnGame(ServerChessGame chessGame, Player player) {
        logger.info(player + " equals " + chessGame.getPlayer());
        return player.equals(chessGame.getPlayer());
    }

    private void setupModelForChessGameScreen(ModelAndView mav, long gameUUID) {
        mav.getModel().put("GAME_UUID", gameUUID);
        ServerChessGame serverGame = gameMap.get(gameUUID); 
        mav.getModel().put("GAME", serverGame);
        mav.getModel().put("CHESSPLAYER",serverGame.getPlayer());
        
        logger.info(String.format("Chess Game(%d): has been Entered", gameUUID));
        mav.setViewName("chessGamePortal");
    }

    private void setModelErrorMessage(ServerChessGame chessGame, Player player, ModelAndView mav) {
        if (!inProgressState(chessGame)) {
            setErrorPageAndMessage(mav, "Can't enter chess game");
        } else if (!isPlayerJoiningOwnGame(chessGame, player)) {
            setErrorPageAndMessage(mav, "Player can't enter a game they didn't started");
        }
    }

    private void setErrorPageAndMessage(ModelAndView mav, String errorMessage) {
        mav.setViewName("forward:/app/chessgame/chessapplication");
        mav.getModel().put("ERRORS", errorMessage);
        logger.error(errorMessage);
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
}
