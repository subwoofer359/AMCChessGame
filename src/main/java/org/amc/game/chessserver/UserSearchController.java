package org.amc.game.chessserver;

import org.amc.User;
import org.amc.dao.DAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.Resource;

@Controller
public class UserSearchController {
    
    private DAO<User> userDAO;
    
    @RequestMapping(method=RequestMethod.POST, value="/searchForUsers")
    @ResponseBody
    public Callable<List<User>> searchForUsers(@RequestParam String searchTerm) {
        return new Callable<List<User>>() {
          @Override
          public List<User> call() throws Exception {
              return userDAO.findEntities("userName", searchTerm);
          }
        };
    }

    @Resource(name="myUserDAO")
    public void setUserDAO(DAO<User> userDAO) {
        this.userDAO = userDAO;
    }
}
