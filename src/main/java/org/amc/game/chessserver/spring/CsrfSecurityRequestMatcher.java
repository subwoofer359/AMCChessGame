package org.amc.game.chessserver.spring;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class CsrfSecurityRequestMatcher implements RequestMatcher {

    private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
    private AntPathRequestMatcher webSocketPathRequestMatcher = new AntPathRequestMatcher("/app/chessgame/chessgame/**");
    private AntPathRequestMatcher topAntPathRequestMatcher = new AntPathRequestMatcher("/**");
    
    @Override
    public boolean matches(HttpServletRequest request) {
        if(allowedMethods.matcher(request.getMethod()).matches()){
            return false;
        } else if(webSocketPathRequestMatcher.matches(request)) {
            return false;
        } else {
            return topAntPathRequestMatcher.matches(request);
        }
    }

}
