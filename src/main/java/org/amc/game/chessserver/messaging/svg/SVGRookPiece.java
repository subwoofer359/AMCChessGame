package org.amc.game.chessserver.messaging.svg;

import org.amc.game.chess.Colour;
import org.amc.game.chess.Location;
import org.w3c.dom.Attr;
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

        Attr style = document.createAttributeNS(null, "style");
        style.setValue("fill:"
                        + svgColour.fill
                        + ";fill-opacity:1;stroke:"
                        + svgColour.stroke
                        + ";stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50");

        Element path1 = document.createElementNS(svgNS, "path");
        Element rectangle1 = document.createElementNS(svgNS, "rect");

        path1.setAttributeNodeNS(style);
        path1.setAttributeNS(null, "d",
                        "m 19.164173,457.97417 24.363471,-0.18711 3.207655,24.82314 -30.137248,-0.0935 z");

        Element path2 = (Element) path1.cloneNode(true);
        Element path3 = (Element) path1.cloneNode(true);

        path2.setAttributeNS(null, "d",
                        "m 13.083803,446.84895 36.538917,0 -1.283062,11.11695 -33.652029,-0.0935 z");

        rectangle1.setAttributeNodeNS(style);
        rectangle1.setAttributeNS(null, "width", "7.2172222");
        rectangle1.setAttributeNS(null, "height", "4.3972631");
        rectangle1.setAttributeNS(null, "x", "13.069632");
        rectangle1.setAttributeNS(null, "y", "442.34985");

        Element rectangle2 = (Element) rectangle1.cloneNode(true);
        rectangle2.setAttributeNS(null, "x", "23.253931");
        rectangle2.setAttributeNS(null, "y", "442.30307");

        Element rectangle3 = (Element) rectangle1.cloneNode(true);
        rectangle3.setAttributeNS(null, "x", "42.018715");
        rectangle3.setAttributeNS(null, "y", "442.30307");

        Element rectangle4 = (Element) rectangle1.cloneNode(true);
        rectangle4.setAttributeNS(null, "x", "33.197662");
        rectangle4.setAttributeNS(null, "y", "442.30307");

        path3.setAttributeNS(
                        null,
                        "d",
                        "m 9.5236162,495.76948 c 12.5961078,0 32.0383488,0.37522 44.6344558,0.37522 0,-4.4898 1.120276,-10.01147 -3.609778,-13.56319 -12.596108,0 -24.943265,0 -37.539373,0 -4.3566298,2.42606 -3.4853048,8.69817 -3.4853048,13.18797 z");

        g.appendChild(path1);
        g.appendChild(path2);
        g.appendChild(rectangle1);
        g.appendChild(rectangle2);
        g.appendChild(rectangle3);
        g.appendChild(rectangle4);
        g.appendChild(path3);

        return g;
    }

}
