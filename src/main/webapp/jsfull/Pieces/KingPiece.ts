/*global ChessPieceModule */

import { ChessPiece } from "./ChessPiece";

export class KingPiece extends ChessPiece {
    constructor(pieceColour) {
        super(pieceColour);
    }

    public toString(id: string, squareCoordinates: string, playerColour: {fill, stroke}): string {
        const location = ChessPiece.parseSquareCoordinates(squareCoordinates);
        const clazz = this.getClasses(playerColour);
        const xCoord = this.getCoordX(location);
        const yCoord = this.getCoordY(location);

        return `<g transform="translate(${xCoord},${yCoord})" id="${id}" class="${clazz}"
            style="fill:${this.pieceColour.fill}">
            <path d="M 31.498132,15.653006 V 5.6201861" style="fill:${this.pieceColour.fill};
                stroke:${this.pieceColour.fill};stroke-width:1.63150001;stroke-linecap:round" />
            <path d="m 31.498132,36.232326 c 0,0 7.86904,-13.50016 5.39709,-18.851 0,0 -2.10116,-4.33596 -5.39709,
                -4.33596 -3.29593,0 -5.39709,4.5821 -5.39709,4.5821 -2.47195,5.34905 5.39709,18.60486 5.39709,18.60486"
                style="fill:${this.pieceColour.fill};stroke:${this.pieceColour.fill};stroke-width:0.40790001" />'
            <path d="m 13.935751,53.877606 c 9.063821,6.24264 24.978231,6.11957 34.042051,-0.12307 l -0.11371,
                -11.25995 c 0,0 12.115845,-6.43527 7.171945,-17.13516 -6.591865,
                -11.59348 -22.247545,-6.24264 -26.367465,7.13445 l 2.82956,3.42632 v -6.24265 c -5.99366,
                -10.92819 -17.689271,-15.05368 -23.7653314,-5.05297 -4.9439,10.70168 5.9771804,17.83612 5.9771804,
                17.83612 l 0.22577,11.41691 z" style="fill:${this.pieceColour.fill};stroke:${this.pieceColour.fill};
                stroke-width:0.40790001;stroke-linecap:round;stroke-linejoin:round" />
            <path d="m 27.378212,9.1874161 h 8.23984" style="fill:${this.pieceColour.fill};
                stroke:${this.pieceColour.fill};stroke-width:1.63150001;stroke-linecap:round" />
            <path d="M 31.361762,14.560576 V 5.9727061" style="fill:none;stroke:${this.pieceColour.stroke};
                stroke-width:0.48899999;stroke-linecap:round"/>
            <path d="m 31.361762,34.980636 c 0,0 6.10833,-11.4505 4.07222,-16.0307 0,0 -1.3574,-3.81683 -4.07222,
                -3.81683 -2.71481,0 -4.07221,3.81683 -4.07221,3.81683 -2.03611,4.5802 4.07221,16.0307 4.07221,16.0307"
                style="stroke:${this.pieceColour.stroke};stroke-width:0.48899999" />
            <path d="m 16.430302,53.301436 c 7.46573,5.34357 21.03979,5.34357 28.50553,0 v -10.68713 c 0,0 12.216645,
                -6.8703 8.144425,-16.0307 -5.429615,-9.92377 -18.324975,-5.34357 -21.718495,
                6.10693 v 5.34357 -5.34357 c -4.75092,-11.4505 -17.646281,-16.0307 -21.7184914,-6.10693 -4.07222,
                9.1604 6.7870314,15.26733 6.7870314,15.26733 v 11.4505 z" style="stroke:${this.pieceColour.stroke};
                stroke-width:0.48899999;stroke-linecap:round;stroke-linejoin:round" />
            <path d="m 27.968252,9.0261661 h 6.78703" style="fill:none;stroke:${this.pieceColour.stroke};
                stroke-width:0.48899999;stroke-linecap:round" />
            <path d="m 16.430302,41.850936 c 7.46573,-3.81683 21.03979,-3.81683 28.50553,0.76337" style="fill:none;
                stroke:${this.pieceColour.stroke};stroke-width:0.48899999;stroke-linecap:round" />
            <path d="m 16.430302,53.301436 c 7.46573,-3.81683 21.03979,-3.81683 28.50553,0" style="fill:none;
                stroke:${this.pieceColour.stroke};stroke-width:0.48899999;stroke-linecap:round;stroke-linejoin:round" />
            <path d="m 16.430302,47.957866 c 7.46573,-3.05346 21.03979,-3.05346 28.50553,0" style="fill:none;
                stroke:${this.pieceColour.stroke};stroke-width:0.48899999;stroke-linecap:round;stroke-linejoin:round" />
            <path d="m 44.257122,41.850936 c 0,0 11.537955,-6.10693 8.179725,-14.73756 -5.265375,-8.93139 -17.681565,
                -2.81987 -21.075085,7.10389 l 0.0163,3.20156 -0.0163,-3.20156 c -3.39351,-9.92376 -17.093811,
                -16.03528 -21.043861,-7.10389 -3.3894504,8.63063 6.580701,13.7406 6.580701,13.7406" style="fill:none;
                stroke:${this.pieceColour.fill};stroke-width:0.48899999;stroke-linecap:round;stroke-linejoin:round"/>
                </g>`;
    }
}
