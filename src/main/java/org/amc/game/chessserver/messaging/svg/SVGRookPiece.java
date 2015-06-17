package org.amc.game.chessserver.messaging.svg;

import org.amc.game.chess.Colour;
import org.amc.game.chess.Location;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SVGRookPiece extends SVGChessPiece {

    public SVGRookPiece(Document document, String svgNS) {
        super(document, svgNS);
    }

    @Override
    public Element getChessPieceElement(String id, Location location, Colour pieceColour) {
        SVGColour svgColour = SVGColour.getColour(pieceColour);
        float coordX = X + (offsetXY * (location.getLetter().ordinal()));
        float coordY = -offsetXY * (location.getNumber() - 1);

        Element g = document.createElementNS(svgNS, "g");
        g.setAttributeNS(null, "transform", "translate(" + coordX + "," + coordY + ")");
        g.setAttributeNS(null, "id", id);

        
        String styleString_1 = "fill:"
                        + svgColour.fill
                        + ";fill-opacity:1;stroke:"
                        + svgColour.stroke
                        + ";stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50";
 
        String rectWidth = "7.2172222";
        String rectHeight = "4.3972631";

        appendNewPath(g, "m 19.164173,457.97417 24.363471,-0.18711 3.207655,24.82314 -30.137248,-0.0935 z", styleString_1);
        appendNewPath(g, "m 13.083803,446.84895 36.538917,0 -1.283062,11.11695 -33.652029,-0.0935 z", styleString_1);
                
        appendNewRectangle(g, "13.069632", "442.34985", rectWidth, rectHeight, styleString_1);
       
        appendNewRectangle(g, "23.253931", "442.30307", rectWidth, rectHeight, styleString_1);

        appendNewRectangle(g, "42.018715", "442.30307", rectWidth, rectHeight, styleString_1);

        appendNewRectangle(g, "33.197662", "442.30307", rectWidth, rectHeight, styleString_1);

        appendNewPath(g, "m 9.5236162,495.76948 c 12.5961078,0 32.0383488,0.37522 44.6344558,0.37522 0,-4.4898 1.120276,-10.01147 -3.609778,-13.56319 -12.596108,0 -24.943265,0 -37.539373,0 -4.3566298,2.42606 -3.4853048,8.69817 -3.4853048,13.18797 z", styleString_1);
        
        return g;
    }

}
