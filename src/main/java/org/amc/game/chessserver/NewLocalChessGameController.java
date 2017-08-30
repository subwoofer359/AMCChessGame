package org.amc.game.chessserver;
import org.amc.dao.SCGDAOInterface;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.amc.game.chessserver.spring.FullNameValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.HashMap;

import javax.annotation.Resource;

@Controller
@SessionAttributes({ "PLAYER", "GAME_UUID" })
public class NewLocalChessGameController {
	static final String PLAYERS_NAME_FIELD = "playersName";
	
	private static final Logger logger = Logger.getLogger(NewLocalChessGameController.class);
	
	private GameControllerHelper helper = new GameControllerHelper(logger);
	
	@RequestMapping(value = "/createGame", params="gameType=LOCAL_GAME")
    public String createGame(Model model, @ModelAttribute("PLAYER") Player player,
                    @RequestParam GameType gameType, @RequestParam String playersName) {
		return createGameLocal(model, player, playersName);
    }
	
    public String createGameLocal(Model model, Player player, String playersName) {
        Validator validator = new FullNameValidator(PLAYERS_NAME_FIELD);
        BindingResult errors = new  MapBindingResult(new HashMap<String, Object>(), PLAYERS_NAME_FIELD);
        
        validator.validate(playersName, errors);
        if(errors.hasErrors()) {
            model.addAttribute(ServerConstants.ERRORS_MODEL_ATTR, errors);
            model.addAttribute("playersName", playersName);
            return GameControllerHelper.TWOVIEW_FORWARD_PAGE;
        } else {
            AbstractServerChessGame serverGame = createLocalGame(player, playersName);
            helper.setUpModel(model, serverGame.getUid(), serverGame, player);
            return GameControllerHelper.CHESSGAME_PORTAL;
        }
    }
    
    private AbstractServerChessGame createLocalGame(Player player, String playersName) {
        long uuid = helper.getNewGameUID();
        AbstractServerChessGame serverGame = helper.getServerChessGame(GameType.LOCAL_GAME, uuid, player);
        Player opponent = new HumanPlayer(playersName);
        opponent.setUserName(generateUserName(playersName));
        serverGame.addOpponent(opponent); 
        helper.saveToDatabase(serverGame);
        return serverGame;
    }
       
    private String generateUserName(String fullName) {
        return fullName.toLowerCase().split(" ")[0];
    }
	
    @Resource(name = "myServerChessGameDAO")
    public void setServerChessGameDAO(SCGDAOInterface serverChessGameDAO) {
        helper.setDAO(serverChessGameDAO);
    }
    
    @Autowired
    public void setServerChessGameFactory(ServerChessGameFactory scgFactory) {
        helper.setServerChessGameFactory(scgFactory);
    }
}
