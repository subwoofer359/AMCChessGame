package org.amc.game.chessserver.messaging.svg;

import org.amc.game.chess.Colour;
import org.amc.game.chess.Location;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class SVGChessPiece {

    protected Document document;
    protected String svgNS;
    
    protected enum SVGColour {
        WHITE("#ffd5d5","#000000"),
        BLACK("#191406","#ffffff");
        
        String stroke;
        String fill;
        
        private SVGColour(String fill, String stroke) {
            this.stroke = stroke;
            this.fill = fill;
        }
        
        public static SVGColour getColour(org.amc.game.chess.Colour colour) {
            if(colour.equals(org.amc.game.chess.Colour.BLACK)) {
                return BLACK;
            } else {
                return WHITE;
            }
        }
    }
    /**
     * x-axis origin in the SVG chessboard
     * @member
     */
    static final float X = 0f;
    /**
     * y-axis origin in the SVG chessboard
     * @member
     */
    static final float Y = -2655f;
    /**
     * The size of the SVG squares
     * @member
     */
    static final float offsetXY = 62.5f;
    
    public SVGChessPiece(Document document, String svgNS) {
        this.document = document;
        this.svgNS = svgNS;
    }
    
    public abstract Element getChessPieceElement(String id, Location location, Colour pieceColour);
    
    void setStyle(Element element, String style) {
        element.setAttributeNS(null, "style", style);
    }
    
    void setD(Element element, String dValue) {
        element.setAttributeNS(null, "d", dValue);
    }

    Element createElement(String type) {
        return document.createElementNS(svgNS, type);
    }
    
    Element createPath(String dValue, String style) {
        Element element = createElement("path");
        setD(element, dValue);
        setStyle(element, style);
        return element;
    }
    
    void appendNewPath(Element root, String dValue, String style) {
        root.appendChild(createPath(dValue, style));  
    }
}
