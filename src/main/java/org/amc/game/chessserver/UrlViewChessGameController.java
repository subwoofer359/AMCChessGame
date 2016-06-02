package org.amc.game.chessserver;

import org.amc.DAOException;
import org.amc.dao.SCGameDAO;
import org.amc.game.chess.ComparePlayers;
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
    
    private SCGameDAO serverChessGameDAO;
    
    static final String CANT_VIEW_CHESSGAME = "Can't not view the ChessGame:(%d)";

    @RequestMapping(method=RequestMethod.GET, path = "/urlview/{gameUid}")
    public ModelAndView viewChessGame(@ModelAttribute("PLAYER") Player player, @PathVariable long gameUid) {
        
        ModelAndView mav = new ModelAndView();
        
        AbstractServerChessGame scgGame = null;
        try {
            scgGame = serverChessGameDAO.getServerChessGame(gameUid);
        } catch(DAOException de) {
            logger.error(de);
        }
        
        if(canServerChessGameBeViewed(player, scgGame)) {
            mav.getModel().put(ServerConstants.GAME_UUID, gameUid);
            mav.getModel().put(ServerConstants.GAME, scgGame);
            mav.getModel().put(ServerConstants.CHESSPLAYER, scgGame.getPlayer(player));
            mav.setViewName(ServerJoinChessGameController.TWO_VIEW_CHESS_PAGE);
        } else {
            mav.setViewName(ServerJoinChessGameController.ERROR_REDIRECT_PAGE);
            mav.getModel().put(ServerConstants.ERRORS, String.format(CANT_VIEW_CHESSGAME, gameUid));
        } 
        return mav;
        
    }
    
    private boolean canServerChessGameBeViewed(Player player, AbstractServerChessGame scgGame) {
        return scgGame !=null && instanceOfTwoServerChessGame(scgGame)
                        && isInProgressState(scgGame)
                        && isPlayerPartOfThisGame(player, scgGame);
    }
    
    private boolean instanceOfTwoServerChessGame(AbstractServerChessGame scgGame) {
        return scgGame instanceof TwoViewServerChessGame;
    }
    
    private boolean isPlayerPartOfThisGame(Player player, AbstractServerChessGame scgGame) {
        return ComparePlayers.comparePlayers(player, scgGame.getPlayer()) ||
                        ComparePlayers.comparePlayers(player, scgGame.getOpponent());
    }
    
    private boolean isInProgressState(AbstractServerChessGame scgGame) {
        return ServerGameStatus.IN_PROGRESS.equals(scgGame.getCurrentStatus());
    }
     
    @Resource(name = "myServerChessGameDAO")
    public void setServerChessGameDAO(SCGameDAO serverChessGameDAO) {
        this.serverChessGameDAO = serverChessGameDAO;
    }
}
