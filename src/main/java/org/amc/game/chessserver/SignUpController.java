package org.amc.game.chessserver;

import org.amc.DAOException;
import org.amc.User;
import org.amc.dao.DAO;
import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;


@Controller
@RequestMapping("/signup")
public class SignUpController {

    private static final Logger logger = Logger.getLogger(SignUpController.class);
    
    
    static final String MESSAGE_MODEL_ATTR = "MESSAGE";
    static final String ERRORS_MODEL_ATTR = "ERRORS";
    static final String SUCCESS_MSG = "account created";
    static final String USERTAKEN_MSG = "Username is already taken";
    static final String ERROR_MSG = "Trouble creating account";
    
    private DAO<User> userDAO;
    private DAO<Player> playerDAO;
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView signUp(String name, String userName, String password) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/Login");
        try {
            if (isUserNameFree(userName)) {
                Player player = new HumanPlayer(name);
                player.setUserName(userName);
                createEntryInUserTable(player, password);
                createEntryInPlayerTable(player);
                mav.getModel().put(MESSAGE_MODEL_ATTR, SUCCESS_MSG);
            } else {
                mav.getModel().put(ERRORS_MODEL_ATTR, USERTAKEN_MSG);
            }
        } catch(DAOException dao){
            mav.getModel().put(ERRORS_MODEL_ATTR, ERROR_MSG);
            logger.error("Error on accessing database:" + dao.getMessage());
            dao.printStackTrace();
        }
        return mav;
    }

    boolean isUserNameFree(String userName) throws DAOException {
        return userDAO.findEntities("userName", userName).isEmpty();
    }

    void createEntryInUserTable(Player player, String password) throws DAOException {
        User user = new User();
        user.setName(player.getName());
        user.setUserName(player.getUserName());
        user.setPassword(password.toCharArray());
        user.setPlayer(player);
        userDAO.addEntity(user);
    }
    
    void createEntryInPlayerTable(Player player) throws DAOException {
        playerDAO.addEntity(player);
    }
    
    @Resource(name="myUserDAO")
    public void setUserDAO(DAO<User> userDAO){
        this.userDAO = userDAO;
    }
    
    @Resource(name="myPlayerDAO")
    public void setPlayerDAO(DAO<Player> playerDAO){
        this.playerDAO = playerDAO;
    }
}
