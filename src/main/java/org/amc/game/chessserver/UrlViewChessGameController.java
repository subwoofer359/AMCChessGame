package org.amc.game.chessserver;

import org.amc.DAOException;
import org.amc.dao.ServerChessGameDAO;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame.ServerGameStatus;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
@SessionAttributes({ "GAME_UUID", "PLAYER" })
public class UrlViewChessGameController {
    
    private static final Logger logger = Logger.getLogger(UrlViewChessGameController.class);
    
    private ServerChessGameDAO serverchessGameDAO;
    
    static final String CANT_VIEW_CHESSGAME = "Can't not view the ChessGame:(%d)";

    @RequestMapping(method=RequestMethod.GET, path = "/urlview/{gameUid}")
    public ModelAndView viewChessGame(@ModelAttribute("PLAYER") Player player, @PathVariable long gameUid) {
        ModelAndView mav = new ModelAndView();
        try {
            ServerChessGame scgGame = serverchessGameDAO.getServerChessGame(gameUid);
            if(scgGame == null) {
                mav.setViewName(ServerJoinChessGameController.ERROR_FORWARD_PAGE);
                mav.getModel().put(ServerConstants.ERRORS, String.format(CANT_VIEW_CHESSGAME, gameUid));
            } else
            if(ServerGameStatus.IN_PROGRESS.equals(scgGame.getCurrentStatus())) {
                mav.getModel().put(ServerConstants.GAME_UUID, gameUid);
                mav.getModel().put(ServerConstants.GAME, scgGame);
                mav.getModel().put(ServerConstants.CHESSPLAYER, scgGame.getPlayer(player));
                mav.setViewName(ServerJoinChessGameController.TWO_VIEW_CHESS_PAGE);
            } else if(ServerGameStatus.AWAITING_PLAYER.equals(scgGame.getCurrentStatus())) {
                mav.getModel().put(ServerConstants.ERRORS, ServerJoinChessGameController.ERROR_GAME_HAS_NO_OPPONENT);
                mav.setViewName(ServerJoinChessGameController.ERROR_FORWARD_PAGE);
            } else if(ServerGameStatus.FINISHED.equals(scgGame.getCurrentStatus())) {
                mav.getModel().put(ServerConstants.ERRORS, ServerJoinChessGameController.ERROR_GAMEOVER);
                mav.setViewName(ServerJoinChessGameController.ERROR_FORWARD_PAGE);
            }
            else {
                mav.getModel().put(ServerConstants.ERRORS, String.format(CANT_VIEW_CHESSGAME, gameUid));
                mav.setViewName(ServerJoinChessGameController.ERROR_FORWARD_PAGE);
            }
        } catch (DAOException de) {
            logger.error(de);
        }
        return mav;
        
    }
    
    @Resource(name = "myServerChessGameDAO")
    public void setServerChessGameDAO(ServerChessGameDAO serverChessGameDAO) {
        this.serverchessGameDAO = serverChessGameDAO;
    }
}
