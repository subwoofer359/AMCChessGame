package org.amc.game.chessserver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/logout")
public class LogOutController {

    @RequestMapping(method = RequestMethod.GET)
    public String logOut(HttpServletRequest request, HttpSession session) throws ServletException {
        session.invalidate();
        request.logout();
        return "redirect:/";
    }
}
