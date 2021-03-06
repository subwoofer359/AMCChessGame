package org.amc.game.chessserver.messaging.svg;

import org.amc.game.chess.Colour;
import org.amc.game.chess.Location;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SVGQueenPiece extends SVGChessPiece {

    private Element g;

    public SVGQueenPiece(Document document, String svgNs) {
        super(document, svgNs);
    }

    private void setTopNodeAttribute(String id, Location location, SVGColour svgColour) {
        float coordX = offsetXY * (location.getLetter().ordinal());
        float coordY = -554 - offsetXY * (location.getNumber() - 1);
        this.g = createG(id, "translate(" + coordX + "," + coordY + ")", "fill:" + svgColour.fill);
    }

    @Override
    public Element getChessPieceElement(String id, Location location, Colour pieceColour) {
        SVGColour svgColour = SVGColour.getColour(pieceColour);

        setTopNodeAttribute(id, location, svgColour);

        Element g2 = createG("matrix(0.37182432,0,0,0.48727711,-545.05741,836.59105)");

        String styleString_1 = "fill:" + svgColour.fill + ";stroke:" + svgColour.fill
                        + ";stroke-width:0.2277;stroke-linecap:round;stroke-linejoin:round";
        String styleString_2 = "fill:" + svgColour.fill + ";stroke:" + svgColour.fill
                        + ";stroke-width:1;stroke-linejoin:round";
        String styleString_4 = "stroke:" + svgColour.stroke
                        + ";stroke-width:0.72600001;stroke-linejoin:round";
        String styleString_3 = styleString_4 + ";stroke-linecap:round";

        appendNewPath(g2,
                        "m 1510.813,344.529 c 0,4.852 -3.934,8.784 -8.783,8.784 -4.852,0 -8.783,-3.933 -8.783,-8.784 0,-4.851 3.932,-8.783 8.783,-8.783 4.849,-10e-4 8.783,3.932 8.783,8.783 z",
                        styleString_1);

        appendNewPath(g2,
                        "m 1535.635,333.514 c 0,4.852 -3.932,8.784 -8.781,8.784 -4.852,0 -8.783,-3.933 -8.783,-8.784 0,-4.851 3.932,-8.783 8.783,-8.783 4.849,0 8.781,3.932 8.781,8.783 z",
                        styleString_1);

        appendNewPath(g2,
                        "m 1561.647,330.737 c 0,4.851 -3.932,8.783 -8.781,8.783 -4.852,0 -8.783,-3.933 -8.783,-8.783 0,-4.852 3.932,-8.784 8.783,-8.784 4.849,-10e-4 8.781,3.932 8.781,8.784 z",
                        styleString_1);

        appendNewPath(g2,
                        "m 1587.911,335.787 c 0,4.851 -3.932,8.783 -8.781,8.783 -4.852,0 -8.783,-3.933 -8.783,-8.783 0,-4.852 3.932,-8.784 8.783,-8.784 4.849,0 8.781,3.933 8.781,8.784 z",
                        styleString_1);

        appendNewPath(g2,
                        "m 1507.186,384.802 c 29.277,-5.167 70.566,-5.167 91.234,0 l 5.625,-44.874 -22.09,32.843 -1.012,-42.42 -18.186,43.476 -10.082,-49.4 -9.828,48.643 -18.188,-43.683 -0.252,46.163 -22.092,-33.601 4.871,42.853 z",
                        styleString_1);

        appendNewPath(g2,
                        "m 1507.186,384.802 c 0,6.89 4.156,7.647 7.602,14.537 3.443,5.167 4.455,2.688 2.732,11.3 -5.168,3.444 -5.168,8.611 -5.168,8.611 -5.166,5.168 1.723,10.612 1.723,10.612 22.389,3.445 54.814,3.445 77.203,0 0,0 5.166,-5.444 0,-10.612 0,0 1.723,-5.167 -3.443,-8.611 -1.723,-8.612 0.297,-6.891 3.742,-12.058 3.443,-6.89 6.59,-6.89 6.59,-13.779 -29.278,-5.167 -61.704,-5.167 -90.981,0 z",
                        styleString_2);

        appendNewPath(g2,
                        "m 1612.407,344.373 c 0,4.852 -3.932,8.784 -8.781,8.784 -4.852,0 -8.783,-3.933 -8.783,-8.784 0,-4.851 3.932,-8.783 8.783,-8.783 4.849,0 8.781,3.932 8.781,8.783 z",
                        styleString_1);

        Element g3 = createG("matrix(3.067229,0,0,3.067558,-1332.787,-2735.3764)");

        appendNewPath(g3,
                        "m 926.332,1004.07 c 0,1.105 -0.895,2 -2,2 -1.105,0 -2,-0.895 -2,-2 0,-1.105 0.895,-2 2,-2 1.105,0 2,0.895 2,2 z",
                        styleString_3);
        appendNewPath(g3,
                        "m 942.832,999.57 c 0,1.104 -0.895,2 -2,2 -1.105,0 -2,-0.896 -2,-2 0,-1.105 0.895,-2 2,-2 1.105,0 2,0.895 2,2 z",
                        styleString_3);

        appendNewPath(g3,
                        "m 959.332,1004.07 c 0,1.105 -0.895,2 -2,2 -1.105,0 -2,-0.895 -2,-2 0,-1.105 0.895,-2 2,-2 1.105,0 2,0.895 2,2 z",
                        styleString_3);

        appendNewPath(g3,
                        "m 934.332,1000.57 c 0,1.105 -0.895,2 -2,2 -1.104,0 -2,-0.895 -2,-2 0,-1.105 0.896,-2 2,-2 1.104,0 2,0.895 2,2 z",
                        styleString_3);

        appendNewPath(g3,
                        "m 951.331,1001.07 c 0,1.105 -0.895,2 -1.999,2 -1.105,0 -2,-0.896 -2,-2 0,-1.104 0.895,-2 2,-2 1.104,0 1.999,0.895 1.999,2 z",
                        styleString_3);

        appendNewPath(g3,
                        "m 927.331,1018.07 c 8.5,-1.5 21,-1.5 27,0 l 1.999,-12 -6.999,11 v -14 l -5.5,13.5 -3,-15 -3,15 -5.5,-14 v 14.5 l -7,-11 2,12 z",
                        styleString_4);

        appendNewPath(g3,
                        "m 927.331,1018.07 c 0,2 1.5,2 2.5,4 1,1.5 1,1 0.5,3.5 -1.5,1 -1.5,2.5 -1.5,2.5 -1.5,1.5 0.5,2.5 0.5,2.5 6.5,1 16.5,1 23,0 0,0 1.5,-1 0,-2.5 0,0 0.5,-1.5 -1,-2.5 -0.5,-2.5 -0.5,-2 0.5,-3.5 1,-2 2.5,-2 2.5,-4 -8.499,-1.5 -18.499,-1.5 -27,0 z",
                        styleString_4);

        appendNewPath(g3, "m 929.831,1022.07 c 3.5,-1 18.5,-1 22,0", styleString_3);

        appendNewPath(g3, "m 930.331,1025.57 c 6,-1 15,-1 21,0", styleString_3);

        appendNewPath(g3, "m 928.832,1028.07 c 5,-1 18.5,-1 23.5,0", styleString_3);

        g.appendChild(g2);
        g2.appendChild(g3);

        return g;
    }

}
