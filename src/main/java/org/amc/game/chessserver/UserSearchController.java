package org.amc.game.chessserver;

import com.google.gson.Gson;


import org.amc.dao.UserSearchDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.Callable;

import javax.annotation.Resource;

@Controller
public class UserSearchController {
    
    private UserSearchDAO userDAO;
    
    @RequestMapping(method=RequestMethod.GET, value="/userSearchPage")
    public String getUserSearchPage() {
        return "UserSearchPage";
    }
    
    @RequestMapping(method=RequestMethod.POST, value="/searchForUsers")
    @ResponseBody
    public Callable<String> searchForUsers(@RequestParam final String searchTerm) {
        return new Callable<String>() {
          @Override
          public String call() throws Exception {
              final Gson gson = new Gson();      
              return gson.toJson(userDAO.findUsers(searchTerm.replaceAll("\\*", "%")));
          }
        };
    }

    @Resource(name="userSearchDAO")
    public void setUserDAO(UserSearchDAO userDAO) {
        this.userDAO = userDAO;
    }
}
