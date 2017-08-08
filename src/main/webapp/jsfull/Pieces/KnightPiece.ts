/*global ChessPieceModule */
import { ChessPiece } from "./ChessPiece";

export class KnightPiece extends ChessPiece {
            constructor(pieceColour) {
                super(pieceColour);
            }

    public toString(id, squareCoordinates, playerColour): string {
        const location = ChessPiece.parseSquareCoordinates(squareCoordinates);
        const clazz = this.getClasses(playerColour);
        const xCoord = this.getCoordX(location);
        const yCoord = this.getCoordY(location);

        return `<g transform="translate(${xCoord},${yCoord})" id="${id}"  class="${clazz}" style="fill:${this.pieceColour.fill}"><g transform="matrix(0.47208604,0,0,0.5491603,-78.452298,-1000.8994)" >
            <path d="m 231.714,1837.494 c 37.531,4.161 54.642,26.149 53.007,94.791 h -75.885 c -0.894,-36.918 30.719,-21.425 24.182,-68.82"  style="fill:${this.pieceColour.fill}" />
            <path d="m 241.853,1864.379 c 1.37,9.711 -16.586,24.578 -25.312,30.02 -10.696,6.671 -10.052,14.486 -17.828,13.343 -4.118,-5.333 0.435,-5.904 -3.565,
                -3.336 -3.565,0 -14.273,3.336 -14.263,-13.342 0,-6.672 18.895,-40.026 18.895,-40.026 0,0 5.295,-6.345 5.702,-11.675 -2.589,-3.316 -1.068,-6.671 -1.068,
                -10.007 3.565,-3.335 12.661,8.339 12.661,8.339 h 4.274 c 0,0 3.681,-6.644 9.807,-10.006 3.565,0 3.565,10.006 3.565,10.006"  style="fill:${this.pieceColour.fill}" />
            <path d="m 190.861,1888.158 c 0,0.902 -0.732,1.635 -1.635,1.635 -0.903,0 -1.634,-0.732 -1.634,-1.635 0,-0.903 0.731,-1.634 1.634,-1.634 0.903,0 1.635,0.732 1.635,1.634 z"
                style="fill:${this.pieceColour.fill}" />
            <path d="m 208.618,1856.289 c -1.354,2.345 -3.085,3.88 -3.867,3.429 -0.781,-0.451 -0.317,-2.718 1.036,-5.063 1.354,-2.345 3.085,-3.88 3.866,-3.429 0.782,0.451 0.318,
                2.717 -1.035,5.063 z"  style="fill:${this.pieceColour.fill}" />
            <path d="m 240.048,1838.802 -0.98,3.596 1.798,0.326 c 10.137,1.56 20.667,7.303 28.025,21.227 7.358,13.923 9.551,35.834 7.926,68.335 l -0.163,0.027 h 5.72 v -0.027 c 1.643
                ,-32.872 -2.865,-55.088 -10.622,-69.766 -7.758,-14.678 -18.921,-21.683 -30.028,-23.392 l -1.676,-0.326 z"  style="fill:${this.pieceColour.fill}" />
            <path d="m 232.819,1840.39 c 32.206,3.067 50.609,24.541 49.076,88.96 h -70.547 c 0,-27.608 30.673,-19.939 24.538,-64.419"   style="stroke:${this.pieceColour.stroke}
            ;stroke-linecap:round" />
            <path d="m 238.954,1864.931 c 1.18,8.931 -17.032,22.604 -24.538,27.607 -9.201,6.136 -8.646,13.322 -15.336,12.271 -3.195,-2.896 4.335,-9.318 0,
                -9.203 -3.067,0 0.575,3.779 -3.067,6.136 -3.067,0 -12.278,3.067 -12.269,-12.271 0,-6.135 18.403,-36.811 18.403,-36.811 0,0 5.784,-5.835 6.135,
                -10.736 -2.228,-3.051 -1.534,-6.135 -1.534,-9.203 3.067,-3.067 9.202,7.669 9.202,7.669 h 6.134 c 0,0 2.398,-6.11 7.669,-9.202 3.066,0 3.066,
                9.202 3.066,9.202"  style="stroke:${this.pieceColour.stroke};stroke-linecap:round;stroke-linejoin:round" />
            <path d="m 194.479,1887.938 c 0,0.847 -0.687,1.533 -1.534,1.533 -0.847,0 -1.533,-0.687 -1.533,-1.533 0,-0.848 0.687,-1.534 1.533,-1.534 0.847,
                0 1.534,0.687 1.534,1.534 z"  style="fill:${this.pieceColour.stroke};stroke:${this.pieceColour.stroke};stroke-width:0.72600001;stroke-linecap:round;stroke-linejoin:round" />
            <path d="m 211.143,1858.029 c -1.271,2.201 -2.895,3.642 -3.629,3.218 -0.733,-0.423 -0.298,-2.551 0.973,-4.751 1.271,-2.201 2.896,-3.642 3.629,
                -3.219 0.733,0.424 0.298,2.552 -0.973,4.752 z"  style="fill:${this.pieceColour.stroke};stroke:${this.pieceColour.stroke}
            ;stroke-width:0.72600001;stroke-linecap:round;stroke-linejoin:round" />
            <path d="m 239.712,1841.618 -0.921,3.374 1.688,0.307 c 9.513,1.464 19.396,6.854 26.302,19.92 6.905,13.066 9.892,32.666 8.366,63.167 l -0.153,1.534 h 5.367 v -1.534 c 1.543,-30.85 -3.616,
                -50.734 -10.896,-64.509 -7.28,-13.775 -17.756,-20.349 -28.181,-21.952 l -1.572,-0.307 z"  style="fill:${this.pieceColour.stroke}" /></g></g>`;
    }
}
