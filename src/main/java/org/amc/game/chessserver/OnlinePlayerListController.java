package org.amc.game.chessserver;

import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.Callable;

import javax.annotation.Resource;

@Controller
public class OnlinePlayerListController {
   
    private SessionRegistry registry;
    
    @Autowired
    OnlinePlayerListMessager messager;
    
    /**
     * ASynchronous call for the list of Online Users.
     * @return String representation of a JSON
     */
    @RequestMapping(method= RequestMethod.GET, value="/onlinePlayerList",produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Callable<String> getOnlinePlayerList() {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                Gson gson = new Gson();
                return gson.toJson(registry.getAllPrincipals());
            }
            
        };
    }

    /**
     * STOMP message handle to return a list of Online Users
     * to only the requester.
     */
    @MessageMapping("/get/onlinePlayerList")
    @SendToUser(value = "/queue/updates/onlineplayerlist", broadcast = false)
    public void getOnlinePlayerListViaSTOMP() {
        messager.sendPlayerList();
    }
    
    @Resource(name="sessionRegistry")
    public void setSessionRegistry(SessionRegistry registry) {
        this.registry = registry;
    }
}
