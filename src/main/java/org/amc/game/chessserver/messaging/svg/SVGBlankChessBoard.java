package org.amc.game.chessserver.messaging.svg;

import org.amc.game.chess.ChessBoard;
import org.amc.game.chess.ChessBoard.Coordinate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SVGBlankChessBoard {

    private static final String WHITESQUARE_COLOUR = "#e6e6e6";

    private static final String BLACKSQUARE_COLOUR = "#000000";
    
    private static final float SQUARE_SIZE = 62.5f; 
    
    private Document document;
    private String svgNS;
    
    public SVGBlankChessBoard(Document document, String svgNS) {
        this.document = document;
        this.svgNS = svgNS;
    }

    public void createBlankChessBoard(Element layer) {
        float x = 0f, y = 0f;
        boolean whiteToggle = true;

        Element basicRectangle = document.createElementNS(svgNS, "rect");
        basicRectangle.setAttributeNS(null, "width", String.valueOf(SQUARE_SIZE));
        basicRectangle.setAttributeNS(null, "height", String.valueOf(SQUARE_SIZE));
        basicRectangle.setAttributeNS(null, "stroke", "none");
        basicRectangle.setAttributeNS(null, "fill-opacity", "1");
        basicRectangle.setAttributeNS(null, "class", "dropzone");
        
        for (int i = 0; i < ChessBoard.BOARD_WIDTH; i++) {
            x = 0;
            for (Coordinate letter : ChessBoard.Coordinate.values()) {
                Element rectangle = (Element)basicRectangle.cloneNode(true);
                rectangle.setAttributeNS(null, "id", letter.toString() + i);
                rectangle.setAttributeNS(null, "x", String.valueOf(x));
                rectangle.setAttributeNS(null, "y", String.valueOf(y));
                if (whiteToggle) {
                    rectangle.setAttributeNS(null, "fill", WHITESQUARE_COLOUR);
                } else {
                    rectangle.setAttributeNS(null, "fill", BLACKSQUARE_COLOUR);
                }
                whiteToggle = !whiteToggle;
                
                layer.appendChild(rectangle);
                x = x + 62.5f;
            }
            y = y + 62.5f;
            whiteToggle = !whiteToggle; // alternate black white pattern
        }

    }
}
