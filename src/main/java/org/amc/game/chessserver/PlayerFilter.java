package org.amc.game.chessserver;

import org.amc.DAOException;
import org.amc.dao.DAO;
import org.amc.dao.DAOInterface;
import org.amc.game.chess.Player;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class PlayerFilter Adds a Player object which
 * represents the currently logged in player to the Session If the Player is
 * deactivated, the player is automatically logged out
 * 
 * @author Adrian Mclaughlin
 * @version 1
 */
public class PlayerFilter implements Filter {
    private FilterConfig filterConfig;
    
    private static final Logger logger = Logger.getLogger(PlayerFilter.class);
    
    /**
     * Spring WebAPP Context
     */
    static final String SPRING_WEBAPPCONTEXT = "org.springframework.web.context.WebApplicationContext.ROOT";
    
    /**
     * Session variable to hold the Player Object
     */
    static final String SESSIONVAR_PLAYER = "PLAYER";

    // The Spring bean's name referring a PlayerDAO object
    static final String PLAYERDAO = "myPlayerDAO";

    // Field of the Player entity (@see org.amc.model.Player)
    static final String PLAYER_USERNAME = "userName";

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // Empty No Resource to clean up.
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
        logger.debug("Filter:PlayerFilter called");
        // Save Player
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        synchronized (httpRequest.getSession()) {
            // Get the playername from the Request object
            if (httpRequest.getUserPrincipal() != null
                            && httpRequest.getSession().getAttribute(SESSIONVAR_PLAYER) == null) {
                String playerName = httpRequest.getUserPrincipal().getName();
                // Get the User object from the database
                Player player = getPlayer(playerName);
                addLoggedPlayerToSession(httpRequest.getSession(), player);
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        this.filterConfig = fConfig;

    }

    /**
     * 
     * @param session
     * @param playerName
     * @return Player Object representing the current player
     * @throws ServletException
     *             if Player is not found in the database
     */

    @SuppressWarnings("unchecked")
    private final Player getPlayer(String playerName) throws ServletException {
        // Spring Context containing the PlayerDAO object
        ApplicationContext context2;

        synchronized (filterConfig.getServletContext()) {
            context2 = (ApplicationContext) filterConfig.getServletContext().getAttribute(
                            SPRING_WEBAPPCONTEXT);
        }

        DAOInterface<Player> playerDAO = context2.getBean(PLAYERDAO, DAO.class);

        try {
            List<Player> listOfPlayer = playerDAO.findEntities(PLAYER_USERNAME, playerName);
            if (listOfPlayer != null && listOfPlayer.size() == 1) {
                return listOfPlayer.get(0);
            } else {
                logger.debug("Error when adding Player " + playerName + " added to Session:");
                throw new ServletException(
                                "No Player has been found in the Player Database with that Player name");
            }
        } catch (DAOException de) {
            logger.error(de.getMessage());
            throw (ServletException) new ServletException(de);
        }

    }

    /**
     * Adds the Player Object to the HttpSession
     * 
     * @param session
     * @param player
     */
    private final void addLoggedPlayerToSession(final HttpSession session, Player player) {
        synchronized (session) {
            session.setAttribute(SESSIONVAR_PLAYER, player);
            logger.debug("Player added to Session:" + player);
        }
    }

}
