package org.amc.game.chessserver;

import org.amc.game.chess.HumanPlayer;
import org.amc.game.chess.Player;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring Controller who responsibility is with creating a new Player
 * The created Player is stored in the user's web session
 * 
 * @author Adrian Mclaughlin
 *
 */
@Controller
@SessionAttributes("PLAYER")
public class PlayerController {

    /**
     * Processes the create Player request from the user.
     * 
     * @param name name of Player
     * @return ModelAndView containing Player object and view to the main page
     */
    @RequestMapping(method = RequestMethod.POST, value = "/playerCreate")
    public ModelAndView createPlayer(@RequestParam("name") String name) {
        ModelAndView mav = new ModelAndView();
        if (name.length() < 5) {
            mav.setViewName(StartPageController.Views.CREATE_PLAYER_PAGE.getPageName());
            mav.getModel().put("ERRORS", "Player's name to short");
        } else {
            Player player = new HumanPlayer(name);
            mav.getModel().put(ServerConstants.PLAYER.toString(), player);
            mav.setViewName("forward:/app/chessgame/chessapplication");
        }
        return mav;
    }
}
