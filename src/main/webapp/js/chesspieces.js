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

var chesspieces = {
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
            coordY = this.y - (this.offsetXY * (location.rank - 1));
        return '<g ' +
                'transform="translate(' + coordX + ',' + coordY + ')" ' +
                'id="' + id + '" ' +
                'class="chesspiece draggable" ' +
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
            coordY = 0 - this.offsetXY * (location.rank - 1);
        return '<g ' +  'id="' + id + '" class="chesspiece draggable" transform="translate(' + coordX + ',' + coordY + ')"><path style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" d="m 19.164173,457.97417 24.363471,-0.18711 3.207655,24.82314 -30.137248,-0.0935 z" id="path5379" /><path style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.4675346;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dashoffset:50" d="m 13.083803,446.84895 36.538917,0 -1.283062,11.11695 -33.652029,-0.0935 z" id="rect5449" /><rect style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" id="rect5454" width="7.2172222" height="4.3972631" x="13.069632" y="442.34985" /><rect style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" id="rect5454-1" width="7.2172222" height="4.3972631" x="23.253931" y="442.30307" /><rect style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" id="rect5454-7" width="7.2172222" height="4.3972631" x="42.018715" y="442.30307" /><rect style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" id="rect5454-17" width="7.2172222" height="4.3972631" x="33.197662" y="442.30307" /><path style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.3504566;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" d="m 9.5236162,495.76948 c 12.5961078,0 32.0383488,0.37522 44.6344558,0.37522 0,-4.4898 1.120276,-10.01147 -3.609778,-13.56319 -12.596108,0 -24.943265,0 -37.539373,0 -4.3566298,2.42606 -3.4853048,8.69817 -3.4853048,13.18797 z" id="rect3002"/></g>';
    },
    bishop : function (id, squareCoordinates, pieceColour) {
        var location = parseSquareCoordinates(squareCoordinates),
            coordX = 0 + (this.offsetXY * (coordinates[location.file] - 1)),
            coordY = -554 - this.offsetXY * (location.rank - 1);
        return '<g transform="translate(' + coordX + ',' + coordY + ')"' +  'id="' + id + '" class="chesspiece draggable"><rect width="35.355339" height="4.0406103" x="14.520943" y="55.176395" transform="translate(0,989.86218)" id="rect3789" style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:'  + pieceColour.stroke + ';stroke-width:0.1;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" /><rect width="30.430845" height="4.2931485" x="16.793787" y="50.630707" transform="translate(0,989.86218)" id="rect3791" style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:'  + pieceColour.stroke + ';stroke-width:0.1;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" /><rect width="17.578154" height="2.9223585" ry="1.4611793" x="23.220133" y="1015.23" id="rect3793" style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:'  + pieceColour.stroke + ';stroke-width:0.08167382;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" /><path d="M 39.342805,6.9601953 A 6.4397225,9.975256 0 1 1 38.535974,5.600699 l -4.317056,7.401826 z" transform="translate(-1.894036,992.13502)" id="path3795" style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:'  + pieceColour.stroke + ';stroke-width:0.1;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" /><path d="m 34.084445,2.8670609 a 1.8940361,1.5152289 0 1 1 -0.0018,-0.00187" transform="translate(0,989.86218)" id="path3797" style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:'  + pieceColour.stroke + ';stroke-width:0.1;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" /><path d="m 28.663078,1018.5221 6.565991,0 1.495423,12.3247 8.281137,9.8247 -25.792606,0 7.760741,-9.8247 z" id="rect3799" style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:'  + pieceColour.stroke + ';stroke-width:0.1;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dashoffset:50" /></g>';
    },
    knight : function (id, squareCoordinates, pieceColour) {
        var location = parseSquareCoordinates(squareCoordinates),
            coordX = 0 + (this.offsetXY * (coordinates[location.file] - 1)),
            coordY = -554 - this.offsetXY * (location.rank - 1);
        return '<g transform="translate(' + coordX + ',' + coordY + ')" id="' + id + '"  class="chesspiece draggable" style="fill:' + pieceColour.fill + '"><g transform="matrix(0.47208604,0,0,0.5491603,-79.202298,-10.14942)" id="g49858"><path d="m 231.714,1837.494 c 37.531,4.161 54.642,26.149 53.007,94.791 h -75.885 c -0.894,-36.918 30.719,-21.425 24.182,-68.82" id="path48825" style="fill:' + pieceColour.fill + '" /><path d="m 241.853,1864.379 c 1.37,9.711 -16.586,24.578 -25.312,30.02 -10.696,6.671 -10.052,14.486 -17.828,13.343 -4.118,-5.333 0.435,-5.904 -3.565,-3.336 -3.565,0 -14.273,3.336 -14.263,-13.342 0,-6.672 18.895,-40.026 18.895,-40.026 0,0 5.295,-6.345 5.702,-11.675 -2.589,-3.316 -1.068,-6.671 -1.068,-10.007 3.565,-3.335 12.661,8.339 12.661,8.339 h 4.274 c 0,0 3.681,-6.644 9.807,-10.006 3.565,0 3.565,10.006 3.565,10.006" id="path48827" style="fill:' + pieceColour.fill + '" /><path d="m 190.861,1888.158 c 0,0.902 -0.732,1.635 -1.635,1.635 -0.903,0 -1.634,-0.732 -1.634,-1.635 0,-0.903 0.731,-1.634 1.634,-1.634 0.903,0 1.635,0.732 1.635,1.634 z" id="path48829" style="fill:' + pieceColour.fill + '" /><path d="m 208.618,1856.289 c -1.354,2.345 -3.085,3.88 -3.867,3.429 -0.781,-0.451 -0.317,-2.718 1.036,-5.063 1.354,-2.345 3.085,-3.88 3.866,-3.429 0.782,0.451 0.318,2.717 -1.035,5.063 z" id="path48831" style="fill:' + pieceColour.fill + '" /><path d="m 240.048,1838.802 -0.98,3.596 1.798,0.326 c 10.137,1.56 20.667,7.303 28.025,21.227 7.358,13.923 9.551,35.834 7.926,68.335 l -0.163,0.027 h 5.72 v -0.027 c 1.643,-32.872 -2.865,-55.088 -10.622,-69.766 -7.758,-14.678 -18.921,-21.683 -30.028,-23.392 l -1.676,-0.326 z" id="path48833" style="fill:' + pieceColour.fill + '" /><path d="m 232.819,1840.39 c 32.206,3.067 50.609,24.541 49.076,88.96 h -70.547 c 0,-27.608 30.673,-19.939 24.538,-64.419"  id="path48837" style="stroke:' + pieceColour.stroke + ';stroke-linecap:round" /><path d="m 238.954,1864.931 c 1.18,8.931 -17.032,22.604 -24.538,27.607 -9.201,6.136 -8.646,13.322 -15.336,12.271 -3.195,-2.896 4.335,-9.318 0,-9.203 -3.067,0 0.575,3.779 -3.067,6.136 -3.067,0 -12.278,3.067 -12.269,-12.271 0,-6.135 18.403,-36.811 18.403,-36.811 0,0 5.784,-5.835 6.135,-10.736 -2.228,-3.051 -1.534,-6.135 -1.534,-9.203 3.067,-3.067 9.202,7.669 9.202,7.669 h 6.134 c 0,0 2.398,-6.11 7.669,-9.202 3.066,0 3.066,9.202 3.066,9.202" id="path48839" style="stroke:' + pieceColour.stroke + ';stroke-linecap:round;stroke-linejoin:round" /><path d="m 194.479,1887.938 c 0,0.847 -0.687,1.533 -1.534,1.533 -0.847,0 -1.533,-0.687 -1.533,-1.533 0,-0.848 0.687,-1.534 1.533,-1.534 0.847,0 1.534,0.687 1.534,1.534 z" id="path48841" style="fill:' + pieceColour.stroke + ';stroke:' + pieceColour.stroke + ';stroke-width:0.32600001;stroke-linecap:round;stroke-linejoin:round" /><path d="m 211.143,1858.029 c -1.271,2.201 -2.895,3.642 -3.629,3.218 -0.733,-0.423 -0.298,-2.551 0.973,-4.751 1.271,-2.201 2.896,-3.642 3.629,-3.219 0.733,0.424 0.298,2.552 -0.973,4.752 z" id="path48843" style="fill:' + pieceColour.stroke + ';stroke:' + pieceColour.stroke + ';stroke-width:0.32600001;stroke-linecap:round;stroke-linejoin:round" /><path d="m 239.712,1841.618 -0.921,3.374 1.688,0.307 c 9.513,1.464 19.396,6.854 26.302,19.92 6.905,13.066 9.892,32.666 8.366,63.167 l -0.153,1.534 h 5.367 v -1.534 c 1.543,-30.85 -3.616,-50.734 -10.896,-64.509 -7.28,-13.775 -17.756,-20.349 -28.181,-21.952 l -1.572,-0.307 z" id="path48845" style="fill:' + pieceColour.stroke + '" /></g></g>';
    }
};