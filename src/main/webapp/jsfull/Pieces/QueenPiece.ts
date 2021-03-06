/*global ChessPieceModule */
import { ChessPiece } from "./ChessPiece";

export class QueenPiece extends ChessPiece {
    constructor(pieceColour) {
        super(pieceColour);
    }

    public toString(id, squareCoordinates, playerColour): string {
        const location = ChessPiece.parseSquareCoordinates(squareCoordinates);
        const clazz = this.getClasses(playerColour);
        const xCoord = this.getCoordX(location);
        const yCoord = this.getCoordY(location);

        return `<g transform="translate(${xCoord},${yCoord})" id="${id}" class="${clazz}"
            style="fill:${this.pieceColour.fill}">
            <path d="m 16.91246,16.87538 c 0,2.13562 -1.35857,3.8663 -3.03312,3.8663 -1.67558,0 -3.03311,
                -1.73112 -3.03311,-3.8663 0,-2.13518 1.35787,-3.86585 3.03311,-3.86585 1.67455,-4.4e-4 3.03312,
                1.73067 3.03312,3.86585 z" style="fill:${this.pieceColour.fill};stroke:${this.pieceColour.fill};
                stroke-width:0.2277;stroke-linecap:round;stroke-linejoin:round"/>
            <path d="m 25.48446,12.02711 c 0,2.13561 -1.35787,3.8663 -3.03242,3.8663 -1.67558,0 -3.03311,
                -1.73113 -3.03311,-3.8663 0,-2.13518 1.35787,-3.86585 3.03311,-3.86585 1.67455,
                0 3.03242,1.73067 3.03242,3.86585 " style="fill:${this.pieceColour.fill};
                stroke:${this.pieceColour.fill};stroke-width:0.2277;stroke-linecap:round;stroke-linejoin:round"/>
            <path d="m 34.46742,10.80481 c 0,2.13518 -1.35787,3.86585 -3.03242,3.86585 -1.67558,
                0 -3.03311,-1.73111 -3.03311,-3.86585 0,-2.13562 1.35787,-3.86629 3.03311,-3.86629 1.67455,
                -4.4e-4 3.03242,1.73067 3.03242,3.86629 z" style="fill:${this.pieceColour.fill};
                stroke:${this.pieceColour.fill};stroke-width:0.2277;stroke-linecap:round;stroke-linejoin:round"/>
            <path d="m 43.53741,13.02758 c 0,2.13517 -1.35787,3.86585 -3.03242,3.86585 -1.67559,0 -3.03312,
                -1.73112 -3.03312,-3.86585 0,-2.13562 1.35788,-3.8663 3.03312,-3.8663 1.67455,0 3.03242,1.73112 3.03242
                ,3.8663 z" style="fill:${this.pieceColour.fill};stroke:${this.pieceColour.fill};stroke-width:0.2277;
                stroke-linecap:round;stroke-linejoin:round"/>
            <path d="m 15.65991,34.6016 c 10.11049,-2.27426 24.3692,-2.27426 31.50667,0 L 49.10911,14.85024 41.48057,
                29.30614 41.13109,10.63491 34.85075,29.77095 31.36904,8.02746 27.97505,29.43775 21.69402,
                10.2106 21.60702,30.52932 13.97779,15.73978 15.65994,34.6016 z"  style="fill:${this.pieceColour.fill};
                stroke:${this.pieceColour.fill};stroke-width:1;stroke-linejoin:round"/>
            <path d="m 15.65991,34.6016 c 0,3.03265 1.43523,3.36584 2.62527,6.39849 1.189,2.27427 1.53848,1.18312
                0.94346,4.97372 -1.78471,1.51588 -1.78471,3.79014 -1.78471,3.79014 -1.78402,2.2747 0.59502,
                4.67088 0.59502,4.67088 7.7318,1.51633 18.92942,1.51633 26.66122,0 0,0 1.78402,-2.39618 0,
                -4.67088 0,0 0.59502,-2.27426 -1.18901,-3.79014 -0.59501,-3.7906 0.10257,-3.03309 1.29226,
                -5.30736 1.18901,-3.03264 2.27579,-3.03264 2.27579,-6.06485 -10.11084,-2.27426 -21.30881,-2.27426
                -31.4193,0 z"  style="fill:${this.pieceColour.fill};stroke:${this.pieceColour.fill};stroke-width:1;
                stroke-linejoin:round"/>
            <path d="m 51.99684,16.80671 c 0,2.13562 -1.35788,3.8663 -3.03243,3.8663 -1.67558,0 -3.03311,
                -1.73112 -3.03311,-3.8663 0,-2.13517 1.35787,-3.86584 3.03311,-3.86584 1.67455,0 3.03243,
                1.73067 3.03243,3.86584 z" style="fill:${this.pieceColour.fill};stroke:${this.pieceColour.fill};
                stroke-width:0.2277;stroke-linecap:round;stroke-linejoin:round"/>
            <path d="m 16.33402,17.22895 c 0,1.49396 -0.94291,2.70401 -2.10707,2.70401 -1.16415,0 -2.10708,
                -1.21005 -2.10708,-2.70401 0,-1.49398 0.94293,-2.70402 2.10708,-2.70402 1.16416,
                0 2.10707,1.21004 2.10707,2.70402 z" style="stroke:${this.pieceColour.stroke};
                stroke-width:0.72600001;stroke-linecap:round;stroke-linejoin:round"/>
            <path d="m 33.71737,11.14489 c 0,1.49261 -0.9429,2.70403 -2.10706,2.70403 -1.16417,0 -2.10708,
                -1.21142 -2.10708,-2.70403 0,-1.49397 0.94291,-2.70402 2.10708,-2.70402 1.16416,0 2.10706,
                1.21005 2.10706,2.70402 z" style="stroke:${this.pieceColour.stroke};stroke-width:0.72600001;
                stroke-linecap:round;stroke-linejoin:round"/>
            <path d="m 51.10073,17.22895 c 0,1.49396 -0.94292,2.70401 -2.10708,2.70401 -1.16415,0 -2.10707,
                -1.21005 -2.10707,-2.70401 0,-1.49398 0.94292,-2.70402 2.10707,-2.70402 1.16416,
                0 2.10708,1.21004 2.10708,2.70402 z" style="stroke:${this.pieceColour.stroke};
                stroke-width:0.72600001;stroke-linecap:round;stroke-linejoin:round"/>
            <path d="m 24.76231,12.4969 c 0,1.49398 -0.94291,2.70403 -2.10707,2.70403 -1.16311,0 -2.10708,
                -1.21005 -2.10708,-2.70403 0,-1.49398 0.94397,-2.70401 2.10708,-2.70401 1.16309,0 2.10707,
                1.21003 2.10707,2.70401 z"  style="stroke:${this.pieceColour.stroke};stroke-width:0.72600001;
                stroke-linecap:round;stroke-linejoin:round"/>
            <path d="m 42.67138,13.17291 c 0,1.49396 -0.94291,2.70402 -2.10601,2.70402 -1.16416,0 -2.10707,
                -1.2114 -2.10707,-2.70402 0,-1.49263 0.94291,-2.70402 2.10707,-2.70402 1.1631,0 2.10601,
                1.21004 2.10601,2.70402 z"  style="stroke:${this.pieceColour.stroke};stroke-width:0.72600001;
                stroke-linecap:round;stroke-linejoin:round"/>
            <path d="m 17.38651,36.15709 c 8.95506,-2.02801 22.12426,-2.02801 28.44549,0 L 47.93801,
                19.93296 40.56432,34.80508 V 15.87693 L 34.76986,34.12908 31.60924,13.84892 28.44864,
                34.12908 22.65418,15.20093 v 19.60415 l -7.37475,-14.87212 2.10708,16.22413"
                style="stroke:${this.pieceColour.stroke};stroke-width:0.72600001;stroke-linejoin:round"/>
            <path d="m 17.38651,36.15709 c 0,2.70403 1.5803,2.70403 2.63384,5.40804 1.05354,2.02801 1.05354,
                1.352 0.52677,4.73202 -1.58031,1.35202 -1.58031,3.38003 -1.58031,3.38003 -1.5803,2.02802 0.52677,
                3.38003 0.52677,3.38003 6.84799,1.35201 17.38336,1.35201 24.23134,0 0,0 1.5803,-1.35201 0,
                -3.38003 0,0 0.52677,-2.02801 -1.05354,-3.38003 -0.52675,
                -3.38002 -0.52675,-2.70401 0.52677,-4.73202 1.05354,-2.70401 2.63385,-2.70401 2.63385,
                -5.40804 -8.95401,-2.02801 -19.48939,-2.02801 -28.44549,0 z"
                style="stroke:${this.pieceColour.stroke};stroke-width:0.72600001;stroke-linejoin:round"/>
            <path d="m 20.02035,41.56513 c 3.68737,-1.352 19.49042,-1.352 23.1778,0"  style="fill:none;
                stroke:${this.pieceColour.stroke};stroke-width:0.72600001;stroke-linecap:round;stroke-linejoin:round"/>
            <path d="m 20.54712,46.29715 c 6.32123,-1.35201 15.80305,-1.35201 22.12426,0"  style="fill:none;
                stroke:${this.pieceColour.stroke};stroke-width:0.72600001;stroke-linecap:round;stroke-linejoin:round"/>
            <path d="m 18.96786,49.67718 c 5.26768,-1.35201 19.49044,-1.35201 24.7581,0"  style="fill:none;
                stroke:${this.pieceColour.stroke};stroke-width:0.72600001;stroke-linecap:round;stroke-linejoin:round"/>
                </g>`;
    }
}
