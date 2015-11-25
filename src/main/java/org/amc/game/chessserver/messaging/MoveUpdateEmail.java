package org.amc.game.chessserver.messaging;

import java.io.File;
import java.io.IOException;

import org.amc.game.chess.ChessGame;
import org.amc.game.chess.ChessGamePlayer;
import org.amc.game.chess.Colour;
import org.amc.game.chess.Player;
import org.amc.game.chessserver.ServerChessGame;
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
        ChessGame chessGame = getServerChessGame().getChessGame();
        Player opponent = chessGame
                        .getOpposingPlayer(getServerChessGame().getPlayer(getPlayer()));
        
        addContextVariable("player", (ChessGamePlayer)getPlayer());
        addContextVariable("status", getServerChessGame().getCurrentStatus().toString());
        addContextVariable("gameState", chessGame.getGameState().toString());
        addContextVariable("opponent", opponent);
        addContextVariable("colourBlack", Colour.BLACK);
        addContextVariable("gameUrl", URL_ROOT + getServerChessGame().getUid());
        addContextVariable(TEMPLATE_CHESSBOARD_TAG, CHESSBOARD_IMAGE_RESOURCE);
        addContextVariable(TEMPLATE_BACKGROUND_TAG, BACKGROUND_IMAGE_RESOURCE);
    }
    
    private File createChessBoardImage() throws IOException {
        chessBoardSVGFactory.setServerChessGame(getServerChessGame());
    	return chessBoardSVGFactory.getChessBoardImage();
    }
    
    @Autowired
    public void setChessBoardSVGFactory(ChessBoardSVGFactory chessBoardSVGFactory) {
    	this.chessBoardSVGFactory = chessBoardSVGFactory;
    }
    
    ChessBoardSVGFactory getChessBoardSVGFactory() {
        return this.chessBoardSVGFactory;
    }
}
