package org.amc.game.chessserver.messaging.svg;

import org.amc.game.chess.Colour;
import org.amc.game.chess.Location;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SVGPawnPiece extends SVGChessPiece {

    public SVGPawnPiece(Document document, String svgNs) {
        super(document, svgNs);
    }

    public Element getChessPieceElement(String id, Location location, Colour pieceColour) {
        SVGColour svgColour = SVGColour.getColour(pieceColour);
        float coordX = X + (offsetXY * (location.getLetter().ordinal()));
        float coordY = Y - (offsetXY * (location.getNumber() - 1));

        Element g = document.createElementNS(svgNS, "g");
        g.setAttributeNS(null, "transform", "translate(" + coordX + "," + coordY + ")");
        g.setAttributeNS(null, "id", id);

        String pawnStyle = "fill:" + svgColour.fill + ";fill-opacity:1;stroke:" + svgColour.stroke
                        + ";stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;"
                        + "stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;"
                        + "stroke-dashoffset:50";

        appendNewPath(g,
                        "M 18.950592,3139.9627 C 27.356133,3140.0207 35.761678,3140.0781 44.167219,"
                                        + "3140.1358 40.865876,3132.8847 33.112108,3126.5232 34.263185,3118.3823 "
                                        + "32.093064,3118.3823 29.922942,3118.3823 27.752826,3118.3823 29.827721,3126.4655 "
                                        + "21.884668,3132.7692 18.950592,3139.9627 z", pawnStyle);

        appendNewPath(g,
                        "M 41.239663,3110.1612 C 41.239663,3114.9277 36.879064,3118.7916 31.499991,"
                                        + "3118.7916 26.120911,3118.7916 21.760312,3114.9277 21.760312,3110.1612 "
                                        + "21.760312,3105.3948 26.120911,3101.5309 31.499991,3101.5309 36.879064,"
                                        + "3101.5309 41.239663,3105.3948 41.239663,3110.1612 z",
                        pawnStyle);

        appendNewPath(g, "M 12.11958,3152.6434 C 23.04719,3152.6434 39.914086,3153.0002 50.841696,"
                        + "3153.0002 50.841696,3148.7306 51.813575,3143.4796 47.710072,3140.1019 "
                        + "36.782462,3140.1019 26.070827,3140.1019 15.143217,3140.1019 11.363671,"
                        + "3142.4091 12.11958,3148.3737 12.11958,3152.6434 z", pawnStyle);
        return g;
    }

}
