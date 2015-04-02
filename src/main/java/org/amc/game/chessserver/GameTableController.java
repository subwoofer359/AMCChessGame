package org.amc.game.chessserver;

import com.google.gson.Gson;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import java.util.concurrent.Callable;

import javax.annotation.Resource;

@Controller
public class GameTableController {

    private Map<Long, ServerChessGame> gameMap;
    
    private static final Logger logger = Logger.getLogger(GameTableController.class);
    
    @Async
    @RequestMapping(value = "/getGameMap", method = RequestMethod.POST)
    public Callable<String> getGames(final Model model){
        logger.debug("async getGameMap received");
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                Gson gson = new Gson();
                model.asMap().put("gameTable",gson.toJson(gameMap));
                return "GameTable";
            }
            
        };
        
    }
    
    @Resource(name = "gameMap")
    public void setGameMap(Map<Long, ServerChessGame> gameMap) {
        this.gameMap = gameMap;
    }

}
