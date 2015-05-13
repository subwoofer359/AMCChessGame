package org.amc.game.chessserver;

import org.amc.User;
import org.amc.dao.DAO;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import javax.annotation.Resource;

@Controller
public class UserNameAvailable {
    
    private static Logger logger = Logger.getLogger(UserNameAvailable.class);
    
    private static final Pattern pattern = Pattern.compile("[a-z]{1}[a-z0-9]{5,}");
    
    static final String USERNAME_MODEL_ATTR = "USERNAME_AVAILABLE";
    
    static final String USERNAME_JSP = "UserNameAvailable";
    
    private DAO<User> userDAO;
    
    @Async
    @RequestMapping(method = RequestMethod.POST, value = "/isUserNameAvailable")
    public Callable<String> isUserNameAvailable(final Model model, @RequestParam final String userName) throws Exception {
        logger.debug("Username Check in progress");
        return new Callable<String>() {
            public String call() throws Exception {
                if (pattern.matcher(userName).matches()) {
                    List<User> users = userDAO.findEntities("userName", userName);
                    model.asMap().put(USERNAME_MODEL_ATTR, users.isEmpty());
                } else {
                    model.asMap().put(USERNAME_MODEL_ATTR, false);
                    logger.debug("username:"+userName+" is not valid");
                }
                return USERNAME_JSP;
            }
        };
    }
    
    @Resource(name="myUserDAO")
    public void setUserDAO(DAO<User> userDAO){
        this.userDAO = userDAO;
    }

}
