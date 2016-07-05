package org.amc.game.chessserver.messaging.svg;

import org.amc.game.chess.Colour;
import org.amc.game.chess.Location;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class SVGChessPiece {

    protected Document document;

    protected String svgNS;

    protected enum SVGColour {
        WHITE("#ffd5d5", "#000000"),
        BLACK("#191406", "#ffffff");

        String stroke;
        String fill;

        SVGColour(String fill, String stroke) {
            this.stroke = stroke;
            this.fill = fill;
        }

        public static SVGColour getColour(Colour colour) {
            if (org.amc.game.chess.Colour.BLACK.equals(colour)) {
                return BLACK;
            } else {
                return WHITE;
            }
        }
    }

    /**
     * x-axis origin in the SVG chessboard
     */
    static final float X = 0f;
    /**
     * y-axis origin in the SVG chessboard
     */
    static final float Y = -2655f;
    /**
     * The size of the SVG squares
     */
    static final float offsetXY = 62.5f;

    static final String linecap = "stroke-linecap:round;";
    static final String linejoin = "stroke-linejoin:round;";
    static final String strokeWidth = "stroke-width:%s;";
    static final String stroke = "stroke:%s;";
    static final String fillNone = "fill:none;";
    static final String fill = "fill:%s;";

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

    Element appendNewPath(Element root, String dValue, String style) {
        Element element = createPath(dValue, style);
        root.appendChild(element);
        return element;
    }

    Element createRectangle(String x, String y, String width, String height, String style) {
        Element element = createElement("rect");
        element.setAttributeNS(null, "x", x);
        element.setAttributeNS(null, "y", y);
        element.setAttributeNS(null, "width", width);
        element.setAttributeNS(null, "height", height);
        setStyle(element, style);
        return element;

    }

    Element appendNewRectangle(Element root, String x, String y, String width, String height,
                    String style) {
        Element element = createRectangle(x, y, width, height, style);
        root.appendChild(element);
        return element;
    }

    Element createG(String transform) {
        Element element = createElement("g");
        element.setAttributeNS(null, "transform", transform);
        return element;
    }

    Element createG(String id, String transform) {
        Element element = createG(transform);
        element.setAttributeNS(null, "id", id);
        return element;
    }

    Element createG(String id, String transform, String style) {
        Element element = createG(id, transform);
        setStyle(element, style);
        return element;
    }

    Element setTransform(Element element, String transform) {
        element.setAttributeNS(null, "transform", transform);
        return element;
    }
}
