package org.amc.game.chessserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.amc.dao.ChessGameInfo;
import org.amc.dao.SCGDAOInterface;
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

    private SCGDAOInterface dao;
    
    private static final Gson GSON;
    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeHierarchyAdapter(ChessGameInfo.class,
                        new ChessGameInfoSerialiser());
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
            	return GSON.toJson(dao.getGameInfoForPlayer(player));
            }
            
        };
        
    }
    
    @Resource(name = "myServerChessGameDAO")
    public void setServerChessGameDAO(SCGDAOInterface scgDAO) {
        this.dao = scgDAO;
    }
    
    static class ChessGameInfoSerialiser implements JsonSerializer<ChessGameInfo> {
        final String UID = "uid";
        final String CURRENT_STATUS = "currentStatus";
        final String PLAYER = "player";
        final String OPPONENT = "opponent";

		@Override
		public JsonElement serialize(ChessGameInfo chessGameInfo, Type arg1, JsonSerializationContext arg2) {
			JsonObject jsObj = new JsonObject();
			
			jsObj.addProperty(UID, chessGameInfo.getUid());
			jsObj.addProperty(CURRENT_STATUS, chessGameInfo.getCurrentStatus().toString());
			
			if(chessGameInfo.getPlayer() == null) {
				jsObj.add(PLAYER, new JsonNull());
			} else {
				jsObj.addProperty(PLAYER, chessGameInfo.getPlayer());
			}
			
			if(chessGameInfo.getOpponent() == null) {
				jsObj.add(OPPONENT, new JsonNull());
			} else {
				jsObj.addProperty(OPPONENT, chessGameInfo.getOpponent());
			}
			return jsObj;
		}
    	
    }
}
