package org.amc.game.chessserver;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ObservableChessGame;
import org.amc.game.chess.view.ChessGameTextView;
import org.amc.util.Observer;
import org.amc.util.Subject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

public class WebTextChessView implements Observer {

    private SimpMessagingTemplate template;
    
    private ChessGameTextView textView;
    
    private static final Logger logger = Logger.getLogger(WebTextChessView.class);
    
    public WebTextChessView(ObservableChessGame chessGame,SimpMessagingTemplate template) {
        textView=new ChessGameTextView(chessGame);
        this.template=template;
        chessGame.attachObserver(this);
    }

    @Override
    public void update(Subject subject, Object message) {
        if(message instanceof ChessBoard){
            String textBoard=textView.displayTheBoard((ChessBoard)message);
            this.template.convertAndSend("/topic/updates",textBoard);
            logger.debug("Message sent to /topic/updates");
        }else
        {
            //Ignore update notification
        }
    }

}