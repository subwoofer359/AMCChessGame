package org.amc.game.chessserver.messaging;

import java.io.IOException;
import java.util.Locale;

import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

public class EmailTemplate {

    private static final Logger logger = Logger.getLogger(EmailTemplate.class);

    private SpringTemplateEngine templateEngine;

    private ServerChessGame serverChessGame;

    private Player otherPlayer;
    
    private ChessBoardSVGImage chessBoardImage;

    private static final String EMAIL_TEMPLATE = "gameStatus.html";
    
    private String imageURL = "";
    
    public EmailTemplate(Player player, ServerChessGame serverChessGame){
        this.serverChessGame = serverChessGame;
        this.otherPlayer = player;
    }

    
    
    public String getEmailHtml() {
        final Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("name", otherPlayer.getName());
        ctx.setVariable("status", serverChessGame.getCurrentStatus().toString());
        try {
        	this.imageURL = createChessBoardImage();
            ctx.setVariable("svg", this.imageURL);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return this.templateEngine.process(EMAIL_TEMPLATE, ctx);
    }

    private String createChessBoardImage() throws IOException {
    	chessBoardImage.setServerChessGame(this.serverChessGame);
    	return chessBoardImage.getChessBoardImage();
    }
    
    @Autowired
    public void setTemplateEngine(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
    
    ServerChessGame getServerChessGame() {
        return this.serverChessGame;
    }
    
    Player getPlayer() {
        return otherPlayer;
    }

    public void setServerChessGame(ServerChessGame serverChessGame) {
        this.serverChessGame = serverChessGame;
    }

    public void setPlayer(Player player) {
        this.otherPlayer = player;
    }
    
    public String getImageFileName() {
    	return this.imageURL;
    }

    @Autowired
    public void setChessBoardSVGImage(ChessBoardSVGImage chessboardImage) {
    	this.chessBoardImage = chessboardImage;
    }
    
    
}
