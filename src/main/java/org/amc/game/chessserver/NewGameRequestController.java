package org.amc.game.chessserver;

import org.amc.User;
import org.amc.dao.DAO;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGameFactory.GameType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.Resource;
@SessionAttributes({ "PLAYER" })
@Controller
public class NewGameRequestController {
    private static Logger logger = Logger.getLogger(NewGameRequestController.class);
    
    @Autowired
    private WebApplicationContext context;

    private DAO<User> userDAO;
    
    @RequestMapping(method=RequestMethod.POST, value="/requestGame")
    @ResponseBody
    public Callable<Boolean> requestNewChessGame(final Model model, @ModelAttribute("PLAYER") final Player player, @RequestParam final String userToPlay) {
        return new Callable<Boolean> () {
            @Override
            public Boolean call() throws Exception {
                List<User> userList = userDAO.findEntities("userName", userToPlay);
                if(userList.size() == 1) {
                    User user = userList.get(0);
                    StartPageController controller = context.getBean(StartPageController.class);
                    controller.createGame(model, user.getPlayer(), GameType.NETWORK_GAME, null);
                                
                    ServerJoinChessGameController sjcgc = context.getBean(ServerJoinChessGameController.class);
                
                    long gameUUID = (Long)model.asMap().get(ServerConstants.GAME_UUID);
                
                    sjcgc.joinGame(player, gameUUID);
                    logger.info("requestNewChessGame:Game Created");
                    
                    return true;
                } else {
                    logger.error("One User should have been retrieve but hasn't");
                    return false;
                }
            }
        };
    }

    @Resource(name="myUserDAO")
    public void setUserDAO(DAO<User> userDAO) {
        this.userDAO = userDAO;
    }
    
}
