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
        console.log("colour="+pieceColour);
        var location = parseSquareCoordinates(squareCoordinates),
            coordX = this.x + (this.offsetXY * (coordinates[location.file] - 1)),
            coordY = this.y - (this.offsetXY * (location.rank - 1));
        return '<g ' +
                'transform="translate(' + coordX + ',' + coordY + ')" ' +
                'id="' + id + '" ' +
                'class="chesspiece draggable" ' +
                'inkscape:label="Layer 1"> ' +
                '<path ' +
                'sodipodi:nodetypes="ccccc" ' +
                'inkscape:connector-curvature="0" ' +
                'id="path5379-3-38" ' +
                'd="M 18.950592,3139.9627 C 27.356133,3140.0207 35.761678,3140.0781 44.167219,3140.1358 40.865876,3132.8847 33.112108,3126.5232 34.263185,3118.3823 32.093064,3118.3823 29.922942,3118.3823 27.752826,3118.3823 29.827721,3126.4655 21.884668,3132.7692 18.950592,3139.9627 z" ' +
                'style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" /> ' +
                    '<path ' +
                    'inkscape:connector-curvature="0" ' +
                    'id="rect2994-4-57" ' +
                    'd="M 41.239663,3110.1612 C 41.239663,3114.9277 36.879064,3118.7916 31.499991,3118.7916 26.120911,3118.7916 21.760312,3114.9277 21.760312,3110.1612 21.760312,3105.3948 26.120911,3101.5309 31.499991,3101.5309 36.879064,3101.5309 41.239663,3105.3948 41.239663,3110.1612 z" ' +
                    'style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" /> ' +
                    '<path ' +
                    'sodipodi:nodetypes="ccccc" ' +
                    'inkscape:connector-curvature="0" ' +
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
        return '<g inkscape:label="Layer 1" inkscape:groupmode="layer"' +  'id="' + id + '" class="chesspiece draggable" transform="translate(' + coordX + ',' + coordY + ')"><path style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" d="m 19.164173,457.97417 24.363471,-0.18711 3.207655,24.82314 -30.137248,-0.0935 z" id="path5379" inkscape:connector-curvature="0" sodipodi:nodetypes="ccccc" /><path style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.4675346;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dashoffset:50" d="m 13.083803,446.84895 36.538917,0 -1.283062,11.11695 -33.652029,-0.0935 z" id="rect5449" inkscape:connector-curvature="0" sodipodi:nodetypes="ccccc" /><rect style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" id="rect5454" width="7.2172222" height="4.3972631" x="13.069632" y="442.34985" /><rect style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" id="rect5454-1" width="7.2172222" height="4.3972631" x="23.253931" y="442.30307" /><rect style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" id="rect5454-7" width="7.2172222" height="4.3972631" x="42.018715" y="442.30307" /><rect style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" id="rect5454-17" width="7.2172222" height="4.3972631" x="33.197662" y="442.30307" /><path style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.3504566;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50" d="m 9.5236162,495.76948 c 12.5961078,0 32.0383488,0.37522 44.6344558,0.37522 0,-4.4898 1.120276,-10.01147 -3.609778,-13.56319 -12.596108,0 -24.943265,0 -37.539373,0 -4.3566298,2.42606 -3.4853048,8.69817 -3.4853048,13.18797 z" id="rect3002" inkscape:connector-curvature="0" sodipodi:nodetypes="ccccc" /></g>';
    }
};