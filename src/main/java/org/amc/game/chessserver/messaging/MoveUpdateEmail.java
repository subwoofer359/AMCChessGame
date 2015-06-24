package org.amc.game.chessserver.messaging;

import java.io.File;
import java.io.IOException;

import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class MoveUpdateEmail extends EmailTemplate {

	private static final Logger logger = Logger.getLogger(MoveUpdateEmail.class);
    
    private ChessBoardSVGFactory chessBoardImage;

    private static final String EMAIL_TEMPLATE = "gameStatus.html";
    
    private static final String DEFAULT_EMAIL_SUBJECT = "Move update from AMCChessGame";
    
    static final String TEMPLATE_CHESSBOARD_TAG = "svg";
    
    static final String CHESSBOARD_IMAGE_RESOURCE = "svg";
        
    public MoveUpdateEmail() {
        setEmailSubject(DEFAULT_EMAIL_SUBJECT);
        setEmailTemplateName(EMAIL_TEMPLATE);
    }
    
    public MoveUpdateEmail(Player player, ServerChessGame serverChessGame) {
        this();
    	setPlayer(player);
    	setServerChessGame(serverChessGame);
    }

    @Override
    public void addImages() {
        try {
            addTempEmbeddedImage(CHESSBOARD_IMAGE_RESOURCE, createChessBoardImage());
            addEmbeddedImage(BACKGROUND_IMAGE_RESOURCE, new File(backgroundImagePath));
            
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void addContextVariables() {
        addContextVariable("name", getPlayer().getName());
        addContextVariable("status", getServerChessGame().getCurrentStatus().toString());
        addContextVariable("opponent", getServerChessGame().getChessGame()
                        .getOpposingPlayer(getServerChessGame().getPlayer(getPlayer())));
        addContextVariable(TEMPLATE_CHESSBOARD_TAG, CHESSBOARD_IMAGE_RESOURCE);
        addContextVariable(TEMPLATE_BACKGROUND_TAG, BACKGROUND_IMAGE_RESOURCE);
    }
    
    private File createChessBoardImage() throws IOException {
    	chessBoardImage.setServerChessGame(getServerChessGame());
    	return chessBoardImage.getChessBoardImage();
    }
    
    @Autowired
    public void setChessBoardSVGImage(ChessBoardSVGFactory chessboardImage) {
    	this.chessBoardImage = chessboardImage;
    }
    
    ChessBoardSVGFactory getChessBoardSVGFactory() {
        return this.chessBoardImage;
    }
}
