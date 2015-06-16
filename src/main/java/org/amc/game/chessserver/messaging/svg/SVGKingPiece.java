package org.amc.game.chessserver.messaging.svg;

import org.amc.game.chess.Colour;
import org.amc.game.chess.Location;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SVGKingPiece extends SVGChessPiece {

    public SVGKingPiece(Document document, String svgNS) {
        super(document, svgNS);
    }

    @Override
    public Element getChessPieceElement(String id, Location location, Colour pieceColour) {
        SVGColour svgColour = SVGColour.getColour(pieceColour);
        float coordX = offsetXY * location.getLetter().ordinal();
        float coordY = -554 - offsetXY * (location.getNumber()-1);
        
        Element[] g = new Element[4];
        for(int i = 0 ; i < g.length; i++) {
            g[i] = document.createElementNS(svgNS, "g");
        }
        
        g[0].setAttributeNS(null, "transform", "translate(" + coordX + "," + coordY + ")");
        g[0].setAttributeNS(null, "id", id);
        setStyle(g[0], "fill:" + svgColour.fill);
        
        g[1].setAttributeNS(null, "transform","matrix(0.44812684,0,0,0.48498523,-553.32634,787.27784)");
        g[2].setAttributeNS(null, "transform","matrix(3.677456,0,0,3.6776632,-684.87287,-2670.1206)");
        
        appendNewPath(g[2], "m 541.391,848.41 v -5.625", 
                        "fill:" + svgColour.fill + ";stroke:" + svgColour.fill + 
                        ";stroke-width:1.63150001;stroke-linecap:round");
        
        appendNewPath(g[2], "m 541.391,859.948 c 0,0 4.775,-7.569 3.275,-10.569 0,0 -1.275,-2.431 "
                        + "-3.275,-2.431 -2,0 -3.275,2.569 -3.275,2.569 -1.5,2.999 3.275,10.431 3.275,"
                        + "10.431", 
                        "fill:" + svgColour.fill + ";stroke:" + svgColour.fill + ";stroke-width:0.40790001");
        appendNewPath(g[2], "m 530.734,869.841 c 5.5,3.5 15.157,3.431 20.657,-0.069 l -0.069,-6.313 c 0,0 "
                        + "7.352,-3.608 4.352,-9.607 -4,-6.5 -13.5,-3.5 -16,4 l 1.717,1.921 v -3.5 c "
                        + "-3.637,-6.127 -10.734,-8.44 -14.421,-2.833 -3,6 3.627,10 3.627,10 l 0.137,"
                        + "6.401 z", 
                        "fill:" + svgColour.fill + ";stroke:" + svgColour.fill + 
                        ";stroke-width:0.40790001;stroke-linecap:round;stroke-linejoin:round");
        appendNewPath(g[2], "m 538.891,844.785 h 5",
                        "fill:" + svgColour.fill + ";stroke:" + svgColour.fill + ";stroke-width:1.63150001;"
                                        + "stroke-linecap:round");
        
        
        g[3].setAttributeNS(null, "transform", "matrix(3.067229,0,0,3.067558,-329.09607,-2142.8556)");
        
        appendNewPath(g[3], "m 533.135,844.656 v -5.625", 
                   "fill:none;stroke:" + svgColour.stroke + ";stroke-width:0.48899999;stroke-linecap:round");
        appendNewPath(g[3], "m 533.135,858.031 c 0,0 4.5,-7.5 3,-10.5 0,0 -1,-2.5 -3,-2.5 -2,0 -3,"
                        + "2.5 -3,2.5 -1.5,3 3,10.5 3,10.5", 
                        "stroke:" + svgColour.stroke + ";stroke-width:0.48899999");
        appendNewPath(g[3], "m 522.135,870.031 c 5.5,3.5 15.5,3.5 21,0 v -7 c 0,0 9,-4.5 6,-10.5 -4,"
                        + "-6.5 -13.5,-3.5 -16,4 v 3.5 -3.5 c -3.5,-7.5 -13,-10.5 -16,-4 -3,6 5,10 5,"
                        + "10 v 7.5 z", 
                        "stroke:" + svgColour.stroke + ";stroke-width:0.48899999;stroke-linecap:round;"
                                        + "stroke-linejoin:round");
        appendNewPath(g[3], "m 530.635,841.031 h 5", 
                        "fill:none;stroke:" + svgColour.stroke + ";stroke-width:0.48899999;stroke-linecap:round");
        appendNewPath(g[3], "m 522.135,862.531 c 5.5,-2.5 15.5,-2.5 21,0.5", 
                        "fill:none;stroke:" + svgColour.stroke + ";stroke-width:0.48899999;stroke-linecap:round");
        appendNewPath(g[3], "m 522.135,870.031 c 5.5,-2.5 15.5,-2.5 21,0", 
                        "fill:none;stroke:" + svgColour.stroke + ";stroke-width:0.48899999;stroke-linecap:round;stroke-linejoin:round");
        appendNewPath(g[3], "m 522.135,866.531 c 5.5,-2 15.5,-2 21,0", 
                        "fill:none;stroke:" + svgColour.stroke + ";stroke-width:0.48899999;stroke-linecap:round;stroke-linejoin:round");
        appendNewPath(g[3], "m 542.635,862.531 c 0,0 8.5,-4 6.026,-9.653 -3.879,-5.85 -13.026,-1.847 -15.526,4.653 l 0.012,2.097 -0.012,"
                        + "-2.097 c -2.5,-6.5 -12.593,-10.503 -15.503,-4.653 -2.497,5.653 4.848,9 4.848,9", "fill:none;stroke:" + svgColour.fill + ";stroke-width:0.48899999;stroke-linecap:round;stroke-linejoin:round");
        
        g[0].appendChild(g[1]);
        g[1].appendChild(g[2]);
        g[1].appendChild(g[3]);
        return g[0];
    }
}
