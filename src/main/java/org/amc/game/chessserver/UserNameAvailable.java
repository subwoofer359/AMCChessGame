package org.amc.game.chessserver;

import org.amc.User;
import org.amc.dao.DAOInterface;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

import javax.annotation.Resource;

@Controller
public class UserNameAvailable {
    
    private static Logger logger = Logger.getLogger(UserNameAvailable.class);
    
    private static final Pattern pattern = Pattern.compile("[a-z]{1}[a-z0-9]{4,}");
    
    private DAOInterface<User> userDAO;
    
    @Async
    @RequestMapping(method = RequestMethod.POST, value = "/isUserNameAvailable")
    @ResponseBody
    public Callable<Boolean> isUserNameAvailable(@RequestParam final String userName) throws Exception {
        logger.debug("Username Check in progress");
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                if (pattern.matcher(userName).matches()) {
                    List<User> users = userDAO.findEntities("userName", userName);
                    return users.isEmpty();
                } else {
                    logger.debug("username:"+userName+" is not valid");
                    return false;
                }
            }
        };
    }
    
    @Resource(name="myUserDAO")
    public void setUserDAO(DAOInterface<User> userDAO){
        this.userDAO = userDAO;
    }

}
