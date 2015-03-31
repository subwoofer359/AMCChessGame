/*
 * file:chessPieces.js
 * Contains svg elements for chesspieces
 * @author Adrian McLaughlin
 */

/*
 * Chess Letter coordinates
 */
"use strict";

var coordinates = {
    A : 1,
    B : 2,
    C : 3,
    D : 4,
    E : 5,
    F : 6,
    G : 7,
    H : 8
};

/**
 * Parses a Square coordinate in the form <letter><number>
 * into an object with properties rank and file
 *
 * @access public
 * @param {string} squareCoordinates
 * @return {object} containing rank and file coordinates
 * @throws {string} if coordinate can't be parsed
 */
function parseSquareCoordinates(squareCoordinates) {
    var coordinateRegex = /(\w)(\d)/,
        coordinate;
    if (coordinateRegex.test(squareCoordinates)) {
        coordinate = coordinateRegex.exec(squareCoordinates);
        return {file : coordinate[1],
                rank : coordinate[2]
            };
    }
    throw "Not valid ChessBoard coordinate";
}

function ChessPieces(playerColour) {
    this.playerColour = playerColour;
}

ChessPieces.prototype = {
    colour : {
        white : {
            fill : '#ffd5d5',
            stroke : '#000000'
        },
        black : {
            fill : '#191406',
            stroke : '#ffffff'
        }
    },
    /**
     * x-axis origin in the SVG chessboard
     * @member
     */
    x : 0,
    /**
     * y-axis origin in the SVG chessboard
     * @member
     */
    y : -2655,
    /**
     * The size of the SVG squares
     * @member
     */
    offsetXY : 62.5,

    getClasses : function (pieceColour) {
        if ((this.playerColour === "WHITE" && pieceColour === this.colour.white) ||
                (this.playerColour === "BLACK" && pieceColour === this.colour.black)) {
            return "chesspiece draggable";
        } else {
            return "chesspiece";
        }
    },
    /**
     * SVG pawn
     * @public
     * @member
     * @param {string} id ID attribute of the pawn
     * @param {string} chessboard coordinates in the form <letter><number>
     * @param {string} colour
     * @return {string} svg element
     */
    pawn : function (id, squareCoordinates, pieceColour) {
        console.log("colour=" + pieceColour);
        var location = parseSquareCoordinates(squareCoordinates),
            coordX = this.x + (this.offsetXY * (coordinates[location.file] - 1)),
            coordY = this.y - (this.offsetXY * (location.rank - 1)),
            classes = this.getClasses(pieceColour);

        return '<g ' +
                'transform="translate(' + coordX + ',' + coordY + ')" ' +
                'id="' + id + '" ' +
                'class="' + classes + '" ' +
                '> ' +
                '<path ' +
                'id="path5379-3-38" ' +
                'd="M 18.950592,3139.9627 C 27.356133,3140.0207 35.761678,3140.0781 44.167219,3140.1358 40.865876,3132.8847 33.112108,3126.5232 34.263185,3118.3823 32.093064,3118.3823 29.922942,3118.3823 27.752826,3118.3823 29.827721,3126.4655 21.884668,3132.7692 18.950592,3139.9627 z" ' +
                'style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" /> ' +
                    '<path ' +
                    'id="rect2994-4-57" ' +
                    'd="M 41.239663,3110.1612 C 41.239663,3114.9277 36.879064,3118.7916 31.499991,3118.7916 26.120911,3118.7916 21.760312,3114.9277 21.760312,3110.1612 21.760312,3105.3948 26.120911,3101.5309 31.499991,3101.5309 36.879064,3101.5309 41.239663,3105.3948 41.239663,3110.1612 z" ' +
                    'style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" /> ' +
                    '<path ' +
                    'id="rect3002-1-6" ' +
                    'd="M 12.11958,3152.6434 C 23.04719,3152.6434 39.914086,3153.0002 50.841696,3153.0002 50.841696,3148.7306 51.813575,3143.4796 47.710072,3140.1019 36.782462,3140.1019 26.070827,3140.1019 15.143217,3140.1019 11.363671,3142.4091 12.11958,3148.3737 12.11958,3152.6434 z" ' +
                    'style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.31832072;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" /> ' +
            '</g> ';
    },
    /**
     * SVG rook
     * @public
     * @member
     * @param {string} id ID attribute of the pawn
     * @param {string} chessboard coordinates in the form <letter><number>
     * @param {string} pieceColour
     * @return {string} svg element
     */
    rook : function (id, squareCoordinates, pieceColour) {
        var location = parseSquareCoordinates(squareCoordinates),
            coordX = this.x + (this.offsetXY * (coordinates[location.file] - 1)),
            coordY = 0 - this.offsetXY * (location.rank - 1),
            classes = this.getClasses(pieceColour);
        return '<g ' +  'id="' + id + '" class="' + classes + '" transform="translate(' + coordX + ',' + coordY + ')"><path style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" d="m 19.164173,457.97417 24.363471,-0.18711 3.207655,24.82314 -30.137248,-0.0935 z" id="path5379" /><path style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.4675346;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dashoffset:50" d="m 13.083803,446.84895 36.538917,0 -1.283062,11.11695 -33.652029,-0.0935 z" id="rect5449" /><rect style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" id="rect5454" width="7.2172222" height="4.3972631" x="13.069632" y="442.34985" /><rect style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" id="rect5454-1" width="7.2172222" height="4.3972631" x="23.253931" y="442.30307" /><rect style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" id="rect5454-7" width="7.2172222" height="4.3972631" x="42.018715" y="442.30307" /><rect style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" id="rect5454-17" width="7.2172222" height="4.3972631" x="33.197662" y="442.30307" /><path style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.3504566;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" d="m 9.5236162,495.76948 c 12.5961078,0 32.0383488,0.37522 44.6344558,0.37522 0,-4.4898 1.120276,-10.01147 -3.609778,-13.56319 -12.596108,0 -24.943265,0 -37.539373,0 -4.3566298,2.42606 -3.4853048,8.69817 -3.4853048,13.18797 z" id="rect3002"/></g>';
    },
    bishop : function (id, squareCoordinates, pieceColour) {
        var location = parseSquareCoordinates(squareCoordinates),
            coordX = 0 + (this.offsetXY * (coordinates[location.file] - 1)),
            coordY = -554 - this.offsetXY * (location.rank - 1),
            classes = this.getClasses(pieceColour);
        return '<g transform="translate(' + coordX + ',' + coordY + ')"' +  'id="' + id + '" class="' + classes + '"><rect width="35.355339" height="4.0406103" x="14.520943" y="55.176395" transform="translate(0,989.86218)" id="rect3789" style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:'  + pieceColour.stroke + ';stroke-width:0.1;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" /><rect width="30.430845" height="4.2931485" x="16.793787" y="50.630707" transform="translate(0,989.86218)" id="rect3791" style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:'  + pieceColour.stroke + ';stroke-width:0.1;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" /><rect width="17.578154" height="2.9223585" ry="1.4611793" x="23.220133" y="1015.23" id="rect3793" style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:'  + pieceColour.stroke + ';stroke-width:0.08167382;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" /><path d="M 39.342805,6.9601953 A 6.4397225,9.975256 0 1 1 38.535974,5.600699 l -4.317056,7.401826 z" transform="translate(-1.894036,992.13502)" id="path3795" style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:'  + pieceColour.stroke + ';stroke-width:0.1;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" /><path d="m 34.084445,2.8670609 a 1.8940361,1.5152289 0 1 1 -0.0018,-0.00187" transform="translate(0,989.86218)" id="path3797" style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:'  + pieceColour.stroke + ';stroke-width:0.1;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" /><path d="m 28.663078,1018.5221 6.565991,0 1.495423,12.3247 8.281137,9.8247 -25.792606,0 7.760741,-9.8247 z" id="rect3799" style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:'  + pieceColour.stroke + ';stroke-width:0.1;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dashoffset:50" /></g>';
    },
    knight : function (id, squareCoordinates, pieceColour) {
        var location = parseSquareCoordinates(squareCoordinates),
            coordX = 0 + (this.offsetXY * (coordinates[location.file] - 1)),
            coordY = -554 - this.offsetXY * (location.rank - 1),
            classes = this.getClasses(pieceColour);
        return '<g transform="translate(' + coordX + ',' + coordY + ')" id="' + id + '"  class="' + classes + '" style="fill:' + pieceColour.fill + '"><g transform="matrix(0.47208604,0,0,0.5491603,-79.202298,-10.14942)" id="g49858"><path d="m 231.714,1837.494 c 37.531,4.161 54.642,26.149 53.007,94.791 h -75.885 c -0.894,-36.918 30.719,-21.425 24.182,-68.82" id="path48825" style="fill:' + pieceColour.fill + '" /><path d="m 241.853,1864.379 c 1.37,9.711 -16.586,24.578 -25.312,30.02 -10.696,6.671 -10.052,14.486 -17.828,13.343 -4.118,-5.333 0.435,-5.904 -3.565,-3.336 -3.565,0 -14.273,3.336 -14.263,-13.342 0,-6.672 18.895,-40.026 18.895,-40.026 0,0 5.295,-6.345 5.702,-11.675 -2.589,-3.316 -1.068,-6.671 -1.068,-10.007 3.565,-3.335 12.661,8.339 12.661,8.339 h 4.274 c 0,0 3.681,-6.644 9.807,-10.006 3.565,0 3.565,10.006 3.565,10.006" id="path48827" style="fill:' + pieceColour.fill + '" /><path d="m 190.861,1888.158 c 0,0.902 -0.732,1.635 -1.635,1.635 -0.903,0 -1.634,-0.732 -1.634,-1.635 0,-0.903 0.731,-1.634 1.634,-1.634 0.903,0 1.635,0.732 1.635,1.634 z" id="path48829" style="fill:' + pieceColour.fill + '" /><path d="m 208.618,1856.289 c -1.354,2.345 -3.085,3.88 -3.867,3.429 -0.781,-0.451 -0.317,-2.718 1.036,-5.063 1.354,-2.345 3.085,-3.88 3.866,-3.429 0.782,0.451 0.318,2.717 -1.035,5.063 z" id="path48831" style="fill:' + pieceColour.fill + '" /><path d="m 240.048,1838.802 -0.98,3.596 1.798,0.326 c 10.137,1.56 20.667,7.303 28.025,21.227 7.358,13.923 9.551,35.834 7.926,68.335 l -0.163,0.027 h 5.72 v -0.027 c 1.643,-32.872 -2.865,-55.088 -10.622,-69.766 -7.758,-14.678 -18.921,-21.683 -30.028,-23.392 l -1.676,-0.326 z" id="path48833" style="fill:' + pieceColour.fill + '" /><path d="m 232.819,1840.39 c 32.206,3.067 50.609,24.541 49.076,88.96 h -70.547 c 0,-27.608 30.673,-19.939 24.538,-64.419"  id="path48837" style="stroke:' + pieceColour.stroke + ';stroke-linecap:round" /><path d="m 238.954,1864.931 c 1.18,8.931 -17.032,22.604 -24.538,27.607 -9.201,6.136 -8.646,13.322 -15.336,12.271 -3.195,-2.896 4.335,-9.318 0,-9.203 -3.067,0 0.575,3.779 -3.067,6.136 -3.067,0 -12.278,3.067 -12.269,-12.271 0,-6.135 18.403,-36.811 18.403,-36.811 0,0 5.784,-5.835 6.135,-10.736 -2.228,-3.051 -1.534,-6.135 -1.534,-9.203 3.067,-3.067 9.202,7.669 9.202,7.669 h 6.134 c 0,0 2.398,-6.11 7.669,-9.202 3.066,0 3.066,9.202 3.066,9.202" id="path48839" style="stroke:' + pieceColour.stroke + ';stroke-linecap:round;stroke-linejoin:round" /><path d="m 194.479,1887.938 c 0,0.847 -0.687,1.533 -1.534,1.533 -0.847,0 -1.533,-0.687 -1.533,-1.533 0,-0.848 0.687,-1.534 1.533,-1.534 0.847,0 1.534,0.687 1.534,1.534 z" id="path48841" style="fill:' + pieceColour.stroke + ';stroke:' + pieceColour.stroke + ';stroke-width:0.32600001;stroke-linecap:round;stroke-linejoin:round" /><path d="m 211.143,1858.029 c -1.271,2.201 -2.895,3.642 -3.629,3.218 -0.733,-0.423 -0.298,-2.551 0.973,-4.751 1.271,-2.201 2.896,-3.642 3.629,-3.219 0.733,0.424 0.298,2.552 -0.973,4.752 z" id="path48843" style="fill:' + pieceColour.stroke + ';stroke:' + pieceColour.stroke + ';stroke-width:0.32600001;stroke-linecap:round;stroke-linejoin:round" /><path d="m 239.712,1841.618 -0.921,3.374 1.688,0.307 c 9.513,1.464 19.396,6.854 26.302,19.92 6.905,13.066 9.892,32.666 8.366,63.167 l -0.153,1.534 h 5.367 v -1.534 c 1.543,-30.85 -3.616,-50.734 -10.896,-64.509 -7.28,-13.775 -17.756,-20.349 -28.181,-21.952 l -1.572,-0.307 z" id="path48845" style="fill:' + pieceColour.stroke + '" /></g></g>';
    },
    queen : function (id, squareCoordinates, pieceColour) {
        var location = parseSquareCoordinates(squareCoordinates),
            coordX = 0 + (this.offsetXY * (coordinates[location.file] - 1)),
            coordY = -554 - this.offsetXY * (location.rank - 1),
            classes = this.getClasses(pieceColour);
        return '<g transform="translate(' + coordX + ',' + coordY + ')" id="' + id + '" class="' + classes + '" style="fill:' + pieceColour.fill + '"><g transform="matrix(0.37182432,0,0,0.48727711,-545.05741,836.59105)" id="g67023"><path d="m 1510.813,344.529 c 0,4.852 -3.934,8.784 -8.783,8.784 -4.852,0 -8.783,-3.933 -8.783,-8.784 0,-4.851 3.932,-8.783 8.783,-8.783 4.849,-10e-4 8.783,3.932 8.783,8.783 z" id="path67025" style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:0.2277;stroke-linecap:round;stroke-linejoin:round" /><path d="m 1535.635,333.514 c 0,4.852 -3.932,8.784 -8.781,8.784 -4.852,0 -8.783,-3.933 -8.783,-8.784 0,-4.851 3.932,-8.783 8.783,-8.783 4.849,0 8.781,3.932 8.781,8.783 z" id="path67027" style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:0.2277;stroke-linecap:round;stroke-linejoin:round" /><path d="m 1561.647,330.737 c 0,4.851 -3.932,8.783 -8.781,8.783 -4.852,0 -8.783,-3.933 -8.783,-8.783 0,-4.852 3.932,-8.784 8.783,-8.784 4.849,-10e-4 8.781,3.932 8.781,8.784 z" id="path67029" style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:0.2277;stroke-linecap:round;stroke-linejoin:round" /><path d="m 1587.911,335.787 c 0,4.851 -3.932,8.783 -8.781,8.783 -4.852,0 -8.783,-3.933 -8.783,-8.783 0,-4.852 3.932,-8.784 8.783,-8.784 4.849,0 8.781,3.933 8.781,8.784 z" id="path67031" style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:0.2277;stroke-linecap:round;stroke-linejoin:round" /><path d="m 1507.186,384.802 c 29.277,-5.167 70.566,-5.167 91.234,0 l 5.625,-44.874 -22.09,32.843 -1.012,-42.42 -18.186,43.476 -10.082,-49.4 -9.828,48.643 -18.188,-43.683 -0.252,46.163 -22.092,-33.601 4.871,42.853 z" id="path67033" style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:1;stroke-linejoin:round" /><path d="m 1507.186,384.802 c 0,6.89 4.156,7.647 7.602,14.537 3.443,5.167 4.455,2.688 2.732,11.3 -5.168,3.444 -5.168,8.611 -5.168,8.611 -5.166,5.168 1.723,10.612 1.723,10.612 22.389,3.445 54.814,3.445 77.203,0 0,0 5.166,-5.444 0,-10.612 0,0 1.723,-5.167 -3.443,-8.611 -1.723,-8.612 0.297,-6.891 3.742,-12.058 3.443,-6.89 6.59,-6.89 6.59,-13.779 -29.278,-5.167 -61.704,-5.167 -90.981,0 z" id="path67035" style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:1;stroke-linejoin:round" /><path d="m 1612.407,344.373 c 0,4.852 -3.932,8.784 -8.781,8.784 -4.852,0 -8.783,-3.933 -8.783,-8.784 0,-4.851 3.932,-8.783 8.783,-8.783 4.849,0 8.781,3.932 8.781,8.783 z" id="path67037" style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:0.2277;stroke-linecap:round;stroke-linejoin:round" /><g transform="matrix(3.067229,0,0,3.067558,-1332.787,-2735.3764)" id="g67039"><path d="m 926.332,1004.07 c 0,1.105 -0.895,2 -2,2 -1.105,0 -2,-0.895 -2,-2 0,-1.105 0.895,-2 2,-2 1.105,0 2,0.895 2,2 z" id="path67041" style="stroke:' + pieceColour.stroke + ';stroke-width:0.32600001;stroke-linecap:round;stroke-linejoin:round" /><path d="m 942.832,999.57 c 0,1.104 -0.895,2 -2,2 -1.105,0 -2,-0.896 -2,-2 0,-1.105 0.895,-2 2,-2 1.105,0 2,0.895 2,2 z" id="path67043" style="stroke:' + pieceColour.stroke + ';stroke-width:0.32600001;stroke-linecap:round;stroke-linejoin:round" /><path d="m 959.332,1004.07 c 0,1.105 -0.895,2 -2,2 -1.105,0 -2,-0.895 -2,-2 0,-1.105 0.895,-2 2,-2 1.105,0 2,0.895 2,2 z" id="path67045" style="stroke:' + pieceColour.stroke + ';stroke-width:0.32600001;stroke-linecap:round;stroke-linejoin:round" /><path d="m 934.332,1000.57 c 0,1.105 -0.895,2 -2,2 -1.104,0 -2,-0.895 -2,-2 0,-1.105 0.896,-2 2,-2 1.104,0 2,0.895 2,2 z" id="path67047" style="stroke:' + pieceColour.stroke + ';stroke-width:0.32600001;stroke-linecap:round;stroke-linejoin:round" /><path d="m 951.331,1001.07 c 0,1.105 -0.895,2 -1.999,2 -1.105,0 -2,-0.896 -2,-2 0,-1.104 0.895,-2 2,-2 1.104,0 1.999,0.895 1.999,2 z" id="path67049" style="stroke:' + pieceColour.stroke + ';stroke-width:0.32600001;stroke-linecap:round;stroke-linejoin:round" /><path d="m 927.331,1018.07 c 8.5,-1.5 21,-1.5 27,0 l 1.999,-12 -6.999,11 v -14 l -5.5,13.5 -3,-15 -3,15 -5.5,-14 v 14.5 l -7,-11 2,12 z" id="path67051" style="stroke:' + pieceColour.stroke + ';stroke-width:0.32600001;stroke-linejoin:round" /><path d="m 927.331,1018.07 c 0,2 1.5,2 2.5,4 1,1.5 1,1 0.5,3.5 -1.5,1 -1.5,2.5 -1.5,2.5 -1.5,1.5 0.5,2.5 0.5,2.5 6.5,1 16.5,1 23,0 0,0 1.5,-1 0,-2.5 0,0 0.5,-1.5 -1,-2.5 -0.5,-2.5 -0.5,-2 0.5,-3.5 1,-2 2.5,-2 2.5,-4 -8.499,-1.5 -18.499,-1.5 -27,0 z" id="path67053" style="stroke:' + pieceColour.stroke + ';stroke-width:0.32600001;stroke-linejoin:round" /><path d="m 929.831,1022.07 c 3.5,-1 18.5,-1 22,0" id="path67055" style="fill:none;stroke:' + pieceColour.stroke + ';stroke-width:0.32600001;stroke-linecap:round;stroke-linejoin:round" /><path d="m 930.331,1025.57 c 6,-1 15,-1 21,0" id="path67057" style="fill:none;stroke:' + pieceColour.stroke + ';stroke-width:0.32600001;stroke-linecap:round;stroke-linejoin:round" /><path d="m 928.832,1028.07 c 5,-1 18.5,-1 23.5,0" id="path67059" style="fill:none;stroke:' + pieceColour.stroke + ';stroke-width:0.32600001;stroke-linecap:round;stroke-linejoin:round" /></g></g></g>';
    },
    king : function (id, squareCoordinates, pieceColour) {
        var location = parseSquareCoordinates(squareCoordinates),
            coordX = 0 + (this.offsetXY * (coordinates[location.file] - 1)),
            coordY = -554 - this.offsetXY * (location.rank - 1),
            classes = this.getClasses(pieceColour);
        return '<g transform="translate(' + coordX + ',' + coordY + ')" id="' + id + '" class="' + classes + '" style="fill:' + pieceColour.fill + '"><g transform="matrix(0.44812684,0,0,0.48498523,-553.32634,787.27784)" id="g70004"><g transform="matrix(3.677456,0,0,3.6776632,-684.87287,-2670.1206)" id="g67079"><path d="m 541.391,848.41 v -5.625" id="path67081" style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:1.63150001;stroke-linecap:round" /><path d="m 541.391,859.948 c 0,0 4.775,-7.569 3.275,-10.569 0,0 -1.275,-2.431 -3.275,-2.431 -2,0 -3.275,2.569 -3.275,2.569 -1.5,2.999 3.275,10.431 3.275,10.431" id="path67083" style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:0.40790001" /><path d="m 530.734,869.841 c 5.5,3.5 15.157,3.431 20.657,-0.069 l -0.069,-6.313 c 0,0 7.352,-3.608 4.352,-9.607 -4,-6.5 -13.5,-3.5 -16,4 l 1.717,1.921 v -3.5 c -3.637,-6.127 -10.734,-8.44 -14.421,-2.833 -3,6 3.627,10 3.627,10 l 0.137,6.401 z" id="path67085" style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:0.40790001;stroke-linecap:round;stroke-linejoin:round" /><path d="m 538.891,844.785 h 5" id="path67087" style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:1.63150001;stroke-linecap:round" /></g><g transform="matrix(3.067229,0,0,3.067558,-329.09607,-2142.8556)" id="g67061"><path d="m 533.135,844.656 v -5.625" id="path67063" style="fill:none;stroke:' + pieceColour.stroke + ';stroke-width:0.48899999;stroke-linecap:round" /><path d="m 533.135,858.031 c 0,0 4.5,-7.5 3,-10.5 0,0 -1,-2.5 -3,-2.5 -2,0 -3,2.5 -3,2.5 -1.5,3 3,10.5 3,10.5" id="path67065" style="stroke:' + pieceColour.stroke + ';stroke-width:0.48899999" /><path d="m 522.135,870.031 c 5.5,3.5 15.5,3.5 21,0 v -7 c 0,0 9,-4.5 6,-10.5 -4,-6.5 -13.5,-3.5 -16,4 v 3.5 -3.5 c -3.5,-7.5 -13,-10.5 -16,-4 -3,6 5,10 5,10 v 7.5 z" id="path67067" style="stroke:' + pieceColour.stroke + ';stroke-width:0.48899999;stroke-linecap:round;stroke-linejoin:round" /><path d="m 530.635,841.031 h 5" id="path67069" style="fill:none;stroke:' + pieceColour.stroke + ';stroke-width:0.48899999;stroke-linecap:round" /><path d="m 522.135,862.531 c 5.5,-2.5 15.5,-2.5 21,0.5" id="path67071" style="fill:none;stroke:' + pieceColour.stroke + ';stroke-width:0.48899999;stroke-linecap:round" /><path d="m 522.135,870.031 c 5.5,-2.5 15.5,-2.5 21,0" id="path67073" style="fill:none;stroke:' + pieceColour.stroke + ';stroke-width:0.48899999;stroke-linecap:round;stroke-linejoin:round" /><path d="m 522.135,866.531 c 5.5,-2 15.5,-2 21,0" id="path67075" style="fill:none;stroke:' + pieceColour.stroke + ';stroke-width:0.48899999;stroke-linecap:round;stroke-linejoin:round" /><path d="m 542.635,862.531 c 0,0 8.5,-4 6.026,-9.653 -3.879,-5.85 -13.026,-1.847 -15.526,4.653 l 0.012,2.097 -0.012,-2.097 c -2.5,-6.5 -12.593,-10.503 -15.503,-4.653 -2.497,5.653 4.848,9 4.848,9" id="path67077" style="fill:none;stroke:' + pieceColour.fill + ';stroke-width:0.48899999;stroke-linecap:round;stroke-linejoin:round" /></g></g></g>';

    }
};