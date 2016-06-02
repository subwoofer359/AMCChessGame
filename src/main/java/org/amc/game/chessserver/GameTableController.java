package org.amc.game.chessserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.amc.dao.SCGameDAO;
import org.amc.game.chess.Player;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.lang.reflect.Type;
import java.util.concurrent.Callable;

import javax.annotation.Resource;

@Controller
@SessionAttributes("PLAYER")
public class GameTableController {

    private SCGameDAO dao;
    
    private static final Gson GSON;
    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeHierarchyAdapter(AbstractServerChessGame.class, 
                        new GameTableController.ServerChessGameSerialiser());
                builder.serializeNulls();
        GSON = builder.create();
    }
    
    private static final Logger logger = Logger.getLogger(GameTableController.class);
    
    @Async
    @ResponseBody
    @RequestMapping(value = "/getGameMap", method = RequestMethod.POST)
    public Callable<String> getGames(@ModelAttribute("PLAYER") final Player player){
        logger.debug("async getGameMap received");
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
            	return GSON.toJson(dao.getGamesForPlayer(player));
            }
            
        };
        
    }
    
    @Resource(name = "myServerChessGameDAO")
    public void setServerChessGameDAO(SCGameDAO scgDAO) {
        this.dao = scgDAO;
    }
    
    static class ServerChessGameSerialiser implements JsonSerializer<AbstractServerChessGame> {

		@Override
		public JsonElement serialize(AbstractServerChessGame serverChessGame, Type arg1, JsonSerializationContext arg2) {
			JsonObject jsObj = new JsonObject();
			
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
