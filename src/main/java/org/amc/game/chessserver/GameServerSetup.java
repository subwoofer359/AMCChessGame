package org.amc.game.chessserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class GameServerSetup  implements ServletContextListener {
    /**
     * Creates a Map and stores it in the Servlet Context
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ConcurrentHashMap<Long,ServerChessGame> gameMap=new ConcurrentHashMap<>();
        saveHashMap(gameMap, sce.getServletContext());
    }
    
    private void saveHashMap(Map<Long, ServerChessGame> map,ServletContext context){
        synchronized (context) {
            context.setAttribute(ServerConstants.GAMEMAP.toString(), map);
        }
    }

    /**
     * Clears the Map and removes it from the Servlet Context
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
       ConcurrentHashMap<?, ?> gameMap=(ConcurrentHashMap<?,?>)sce.getServletContext().
                       getAttribute(ServerConstants.GAMEMAP.toString());
       gameMap.clear();
       removeHashMap(sce.getServletContext(), ServerConstants.GAMEMAP.toString());
    }
    
    private void removeHashMap(ServletContext context,String attributeName){
        synchronized (context) {
            context.removeAttribute(attributeName);
        }
    }
    
}
