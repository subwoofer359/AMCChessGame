package org.amc.game.chessserver.messaging;

import java.io.IOException;

import org.amc.game.chess.AbstractChessGame;
import org.amc.game.chess.Colour;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.AbstractServerChessGame;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class MoveUpdateEmail extends EmailTemplate {

	private static final Logger logger = Logger.getLogger(MoveUpdateEmail.class);
    
    private ChessBoardSVGFactory chessBoardSVGFactory;

    private static final String EMAIL_TEMPLATE = "gameStatus.html";
    
    private static final String DEFAULT_EMAIL_SUBJECT = "Move update from AMCChessGame";
    
    static final String TEMPLATE_CHESSBOARD_TAG = "svg";
    
    static final String CHESSBOARD_IMAGE_RESOURCE = "svg";
        
    public MoveUpdateEmail() {
        setEmailSubject(DEFAULT_EMAIL_SUBJECT);
        setEmailTemplateName(EMAIL_TEMPLATE);
    }
    
    public MoveUpdateEmail(Player player, AbstractServerChessGame serverChessGame) {
        this();
    	setPlayer(player);
    	setServerChessGame(serverChessGame);
    }

    @Override
    public void addImages() {
        try {
        	logger.info("adding chessboard picture");
            addEmbeddedImage(getMailImageFactory().getTempRootPathImage(CHESSBOARD_IMAGE_RESOURCE,
            		createChessBoardImage(), EmailTemplate.IMAGE_TYPE));
            logger.info("adding background picture");
            addEmbeddedImage(getMailImageFactory().getServletPathImage(BACKGROUND_IMAGE_RESOURCE,
            		backgroundImagePath, EmailTemplate.IMAGE_TYPE));
            
        } catch (IOException e) {
            logger.error("Error Adding Images to Move Email:" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void addContextVariables() {
        AbstractChessGame chessGame = getServerChessGame().getChessGame();
        Player opponent = chessGame
                        .getOpposingPlayer(getServerChessGame().getPlayer(getPlayer()));
        
        addContextVariable("player", getPlayer());
        addContextVariable("status", getServerChessGame().getCurrentStatus().toString());
        addContextVariable("gameState", chessGame.getGameState().toString());
        addContextVariable("opponent", opponent);
        addContextVariable("colourBlack", Colour.BLACK);
        addContextVariable("gameUrl", getUrlRoot() + getServerChessGame().getUid());
        addContextVariable(TEMPLATE_CHESSBOARD_TAG, CHESSBOARD_IMAGE_RESOURCE);
        addContextVariable(TEMPLATE_BACKGROUND_TAG, BACKGROUND_IMAGE_RESOURCE);
    }
    
    private String createChessBoardImage() throws IOException {
        chessBoardSVGFactory.setServerChessGame(getServerChessGame());
    	return chessBoardSVGFactory.getChessBoardImage().getAbsolutePath();
    }
    
    @Autowired
    public void setChessBoardSVGFactory(ChessBoardSVGFactory chessBoardSVGFactory) {
    	this.chessBoardSVGFactory = chessBoardSVGFactory;
    }
    
    ChessBoardSVGFactory getChessBoardSVGFactory() {
        return this.chessBoardSVGFactory;
    }
}
