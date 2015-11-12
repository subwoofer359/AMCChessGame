package org.amc.game.chessserver;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.annotation.Resource;

@Controller
public class GameTableController {

    private Map<Long, ServerChessGame> gameMap;
    
    private static final Logger logger = Logger.getLogger(GameTableController.class);
    
    @Async
    @ResponseBody
    @RequestMapping(value = "/getGameMap", method = RequestMethod.POST)
    public Callable<String> getGames(){
        logger.debug("async getGameMap received");
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                Gson gson = new Gson();
                return gson.toJson(gameMap);
            }
            
        };
        
    }
    
    @Resource(name = "gameMap")
    public void setGameMap(Map<Long, ServerChessGame> gameMap) {
        this.gameMap = gameMap;
    }
    
    static class ServerChessGameSerialiser implements JsonSerializer<ServerChessGame> {

		@Override
		public JsonElement serialize(ServerChessGame serverChessGame, Type arg1, JsonSerializationContext arg2) {
			JsonObject jsObj = new JsonObject();
			if(serverChessGame == null ) {
				return jsObj;
			}
			
			jsObj.addProperty("uid", serverChessGame.getUid());
			jsObj.addProperty("currentStatus", serverChessGame.getCurrentStatus().toString());
			
			if(serverChessGame.getPlayer() == null) {
				jsObj.add("player", new JsonNull());
			} else {
				jsObj.addProperty("player", serverChessGame.getPlayer().getUserName());
			}
			
			if(serverChessGame.getOpponent() == null) {
				jsObj.add("opponent", new JsonNull());
			} else {
				jsObj.addProperty("opponent", serverChessGame.getOpponent().getUserName());
			}
			return jsObj;
		}
    	
    }
}
