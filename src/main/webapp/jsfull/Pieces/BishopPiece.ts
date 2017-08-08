/*global ChessPieceModule */

import { ChessPiece } from "./ChessPiece";

export class BishopPiece extends ChessPiece {
    constructor(pieceColour) {
        super(pieceColour);
    }

    public toString(id: string, squareCoordinates: string, playerColour: {fill, stroke}): string {
        const location = ChessPiece.parseSquareCoordinates(squareCoordinates);
        const clazz = this.getClasses(playerColour);
        const xCoord = this.getCoordX(location);
        const yCoord = this.getCoordY(location);

        return `<g id="${id}" class="${clazz}" transform="translate(${xCoord},${yCoord})">
                <rect width="35.355339" height="4.0406103" x="12.648308" y="55.91304"  style="fill:${this.pieceColour.fill};fill-opacity:1;stroke:${this.pieceColour.stroke};stroke-width:0.7;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" />
                <rect width="30.430845" height="4.2931485" x="14.921153" y="51.367386" style="fill:${this.pieceColour.fill};fill-opacity:1;stroke:${this.pieceColour.stroke};stroke-width:0.7;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" />
                <rect width="17.578154" height="2.9223585" ry="1.4611793" x="21.347498" y="26.104445"  style="fill:${this.pieceColour.fill};fill-opacity:1;stroke:${this.pieceColour.stroke};stroke-width:0.7;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" />
                <path d="m 35.576134,9.9696847 a 6.4397225,9.975256 0 1 1 -0.806831,-1.3595 l -4.317056,7.4017803 z" style="fill:${this.pieceColour.fill};fill-opacity:1;stroke:${this.pieceColour.stroke};stroke-width:0.7;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" />
                <path d="m 32.21181,3.6037047 a 1.8940361,1.5152289 0 1 1 -0.0018,-0.002" style="fill:${this.pieceColour.fill};fill-opacity:1;stroke:${this.pieceColour.stroke};stroke-width:0.7;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" />
                <path d="m 26.790443,29.396565 6.565991,0 1.495423,12.3247 8.281137,9.8247 -25.792605,0 7.76074,-9.8247 z"  style="fill:${this.pieceColour.fill};fill-opacity:1;stroke:${this.pieceColour.stroke};stroke-width:0.7;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dashoffset:50" /></g>`;
    }
}
