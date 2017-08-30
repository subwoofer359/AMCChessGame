package org.amc.game.chessserver;

import org.amc.dao.SCGDAOInterface;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.annotation.Resource;

@Controller
@SessionAttributes({ "PLAYER", "GAME_UUID" })
public class NewComputerGameController {
	
	private static final Logger logger = Logger.getLogger(NewComputerGameController.class);

	private GameControllerHelper helper = new GameControllerHelper(logger);
	
	@RequestMapping(value = "/createGame", params="gameType=COMPUTER_WHITE_GAME")
    public String createComputerWhiteGame(Model model, @ModelAttribute("PLAYER") Player player,
                    @RequestParam GameType gameType) {
		return createGameComputer(model, player, gameType);
    }
	
	@RequestMapping(value = "/createGame", params="gameType=COMPUTER_BLACK_GAME")
    public String createComputerBlackGame(Model model, @ModelAttribute("PLAYER") Player player,
                    @RequestParam GameType gameType) {
		return createGameComputer(model, player, gameType);
    }
	
    private String createGameComputer(Model model, Player player, GameType gameType) {
    	final long uid = helper.getNewGameUID();
    	AbstractServerChessGame serverGame = helper.getServerChessGame(gameType, uid, player);
    	if(GameType.COMPUTER_BLACK_GAME == gameType) {
    		((ComputerServerChessGame)serverGame).addOpponent();
    	} else {
    		serverGame.addOpponent(player);
    	}
    	helper.setUpModel(model, serverGame.getUid(), serverGame, player);
    	helper.saveToDatabase(serverGame);
    	return GameControllerHelper.CHESSGAME_PORTAL;
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
