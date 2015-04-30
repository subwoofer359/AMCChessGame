package org.amc.game.chessserver;

import org.amc.DAOException;
import org.amc.dao.DAO;
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
 * represents the currently logged in user to the Session If the Player is
 * deactivated, the user is automatically logged out
 * 
 * @author Adrian Mclaughlin
 * @version 1
 */
public class PlayerFilter implements Filter {
    private FilterConfig filterConfig;
    
    /**
     * Spring WebAPP Context
     */

    public final static String SPRING_WEBAPPCONTEXT = "org.springframework.web.context.WebApplicationContext.ROOT";
    
    /**
     * Session variable to hold the Player Object
     */
    
    public static final String SESSIONVAR_PLAYER = "PLAYER";

    private final static Logger logger = Logger.getLogger(PlayerFilter.class);

    // The Spring bean's name referring a PlayerDAO object
    private final static String USERDAO = "myPlayerDAO";

    // Field of the Player entity (@see org.amc.model.Player)
    private final static String PLAYER_NAME = "name";

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
            // Get the username from the Request object
            if (httpRequest.getUserPrincipal() != null
                            && httpRequest.getSession().getAttribute(SESSIONVAR_PLAYER) == null) {
                String userName = httpRequest.getUserPrincipal().getName();
                // Get the User object from the database
                Player user = getPlayer(httpRequest.getSession(), userName);
                addLoggedPlayerToSession(httpRequest.getSession(), user);
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
     * @param userName
     * @return Player Object representing the current user
     * @throws ServletException
     *             if Player is not found in the database
     */

    @SuppressWarnings("unchecked")
    private final Player getPlayer(HttpSession session, String userName) throws ServletException {
        // Spring Context containing the PlayerDAO object
        ApplicationContext context2;

        synchronized (filterConfig.getServletContext()) {
            context2 = (ApplicationContext) filterConfig.getServletContext().getAttribute(
                            SPRING_WEBAPPCONTEXT);
        }

        DAO<Player> userDAO = context2.getBean(USERDAO, DAO.class);

        try {
            List<Player> listOfPlayer = userDAO.findEntities(PLAYER_NAME, userName);
            if (listOfPlayer != null && listOfPlayer.size() == 1) {
                return listOfPlayer.get(0);
            } else {
                logger.debug("Error when adding Player " + userName + " added to Session:");
                throw new ServletException(
                                "No Player has been found in the Player Database with that Player name");
            }
        } catch (DAOException de) {
            logger.error(de.getMessage());
            throw (ServletException) new ServletException().initCause(de);
        }

    }

    /**
     * Adds the Player Object to the HttpSession
     * 
     * @param session
     * @param user
     */
    private final void addLoggedPlayerToSession(HttpSession session, Player user) {
        synchronized (session) {
            session.setAttribute(SESSIONVAR_PLAYER, user);
            logger.debug("Player added to Session:" + user);
        }
    }

}
