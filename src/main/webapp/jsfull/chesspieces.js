/*
 * file:chessPieces.js
 * Contains svg elements for chesspieces
 * @author Adrian McLaughlin
 */

var chesspiecesModule = (function () {
    'use strict';
    /*
    * Chess Letter coordinates
    */
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
        var coordinateRegex = /([A-H])([1-8])/,
            coordinate;
        if (coordinateRegex.test(squareCoordinates)) {
            coordinate = coordinateRegex.exec(squareCoordinates);
            return {file : coordinate[1],
                    rank : coordinate[2]
                };
        }
        throw 'Not valid ChessBoard coordinate';
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
        y : 0,
        /**
        * The size of the SVG squares
        * @member
        */
        offsetXY : 62.5,

        getClasses : function (pieceColour) {
            if ((this.playerColour === "WHITE" && pieceColour === this.colour.white) ||
                    (this.playerColour === "BLACK" && pieceColour === this.colour.black)) {
                return 'chesspiece draggable';
            }
            return 'chesspiece';
        },
        getCoordX : function (location) {
            return this.x + (this.offsetXY * (coordinates[location.file] - 1));
        },
        
        getCoordY : function (location) {
            return this.y + (this.offsetXY * (8 - location.rank));
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
            var location = parseSquareCoordinates(squareCoordinates),
                coordX = this.getCoordX(location),
                coordY = this.getCoordY(location),
                classes = this.getClasses(pieceColour),
                style = 'style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + pieceColour.stroke + ';stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50;"';

            return '<g transform="translate(' + coordX + ',' + coordY + ')" id="' + id + '" class="' + classes + '"> ' +
                    '<path d="m 19.201062,45.600762 c 8.40554,0.058 16.81108,0.1154 25.21663,0.1731 -3.30135,-7.251102 -11.05511,-13.612602 -9.90404,-21.753502 -2.17012,0 -4.34024,0 -6.51036,0 2.0749,8.0832 -5.86815,14.3869 -8.80223,21.580402 z" ' + style + '/> ' +
                        '<path d="m 41.045412,15.486384 c 0,4.7665 -4.3606,8.6304 -9.73967,8.6304 -5.37908,0 -9.73968,-3.8639 -9.73968,-8.6304 0,-4.7664 4.3606,-8.6303005 9.73968,-8.6303005 5.37907,0 9.73967,3.8639005 9.73967,8.6303005 z" ' + style + '/>' +
                        '<path d="m 12.181722,58.139172 c 10.92761,0 27.7945,0.35681 38.72211,0.35681 0,-4.2696 0.97188,-9.5206 -3.13162,-12.8983 -10.92761,0 -21.63925,0 -32.56686,0 -3.77954,2.3072 -3.02363,8.27179 -3.02363,12.54149 z" ' + style + '/></g>';
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
                coordX = this.getCoordX(location),
                coordY = this.getCoordY(location),
                classes = this.getClasses(pieceColour),
                style = 'style="fill:' + pieceColour.fill + 
                    ';fill-opacity:1;stroke:' + pieceColour.stroke + 
                    ';stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;' +
                    'stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50"';
            return '<g ' +  'id="' + id + '" class="' + classes + '" transform="translate(' + coordX + ',' + coordY + ')">' +
                '<path ' + style + ' d="M 18.664173,19.97417 43.027644,19.78706 46.235299,44.6102 16.098051,44.5167 z" />' +
                '<path ' + style + ' d="m 12.583803,8.84895 36.538917,0 -1.283062,11.11695 -33.652029,-0.0935 z" />' +
                '<rect ' + style + ' width="7.2172222" height="4.3972631" x="12.569632" y="4.3498535" />' +
                '<rect ' + style + ' width="7.2172222" height="4.3972631" x="22.753931" y="4.3030701" />' +
                '<rect ' + style + ' width="7.2172222" height="4.3972631" x="41.518715" y="4.3030701" />' +
                '<rect ' + style + ' width="7.2172222" height="4.3972631" x="32.697662" y="4.3030701" />' +
                '<path ' + style + ' d="m 9.0236162,57.76948 c 12.5961078,0 32.0383488,0.37522 44.6344558,0.37522 0,-4.4898 1.120276,-10.01147 -3.609778,-13.56319 -12.596108,0 -24.943265,0 -37.539373,0 -4.3566298,2.42606 -3.4853048,8.69817 -3.4853048,13.18797 z"/></g>';
        },
        bishop : function (id, squareCoordinates, pieceColour) {
            var location = parseSquareCoordinates(squareCoordinates),
                coordX = this.getCoordX(location),
                coordY = this.getCoordY(location),
                classes = this.getClasses(pieceColour);
            return '<g ' +  'id="' + id + '" class="' + classes + '" transform="translate(' + coordX + ',' + coordY + ')"><rect width="35.355339" height="4.0406103" x="12.648308" y="55.91304"  style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:'  + pieceColour.stroke + ';stroke-width:0.7;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" />' + 
                        '<rect width="30.430845" height="4.2931485" x="14.921153" y="51.367386" style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:'  + pieceColour.stroke + ';stroke-width:0.7;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" />' +
                        '<rect width="17.578154" height="2.9223585" ry="1.4611793" x="21.347498" y="26.104445"  style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:'  + pieceColour.stroke + ';stroke-width:0.7;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" />' +
                        '<path d="m 35.576134,9.9696847 a 6.4397225,9.975256 0 1 1 -0.806831,-1.3595 l -4.317056,7.4017803 z" style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:'  + pieceColour.stroke + ';stroke-width:0.7;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" />' +
                        '<path d="m 32.21181,3.6037047 a 1.8940361,1.5152289 0 1 1 -0.0018,-0.002" style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:'  + pieceColour.stroke + ';stroke-width:0.7;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dasharray:none;stroke-dashoffset:50" />' +
                        '<path d="m 26.790443,29.396565 6.565991,0 1.495423,12.3247 8.281137,9.8247 -25.792605,0 7.76074,-9.8247 z"  style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:'  + pieceColour.stroke + ';stroke-width:0.7;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:4;stroke-opacity:1;stroke-dashoffset:50" /></g>';
        },
        knight : function (id, squareCoordinates, pieceColour) {
            var location = parseSquareCoordinates(squareCoordinates),
                coordX = this.getCoordX(location),
                coordY = this.getCoordY(location),
                classes = this.getClasses(pieceColour);
            return '<g transform="translate(' + coordX + ',' + coordY + ')" id="' + id + '"  class="' + classes + '" style="fill:' + pieceColour.fill + '"><g transform="matrix(0.47208604,0,0,0.5491603,-78.452298,-1000.8994)" >' +
            '<path d="m 231.714,1837.494 c 37.531,4.161 54.642,26.149 53.007,94.791 h -75.885 c -0.894,-36.918 30.719,-21.425 24.182,-68.82"  style="fill:' + pieceColour.fill + '" />' +
            '<path d="m 241.853,1864.379 c 1.37,9.711 -16.586,24.578 -25.312,30.02 -10.696,6.671 -10.052,14.486 -17.828,13.343 -4.118,-5.333 0.435,-5.904 -3.565,-3.336 -3.565,0 -14.273,3.336 -14.263,-13.342 0,-6.672 18.895,-40.026 18.895,-40.026 0,0 5.295,-6.345 5.702,-11.675 -2.589,-3.316 -1.068,-6.671 -1.068,-10.007 3.565,-3.335 12.661,8.339 12.661,8.339 h 4.274 c 0,0 3.681,-6.644 9.807,-10.006 3.565,0 3.565,10.006 3.565,10.006"  style="fill:' + pieceColour.fill + '" />' +
            '<path d="m 190.861,1888.158 c 0,0.902 -0.732,1.635 -1.635,1.635 -0.903,0 -1.634,-0.732 -1.634,-1.635 0,-0.903 0.731,-1.634 1.634,-1.634 0.903,0 1.635,0.732 1.635,1.634 z"  style="fill:' + pieceColour.fill + '" />' +
            '<path d="m 208.618,1856.289 c -1.354,2.345 -3.085,3.88 -3.867,3.429 -0.781,-0.451 -0.317,-2.718 1.036,-5.063 1.354,-2.345 3.085,-3.88 3.866,-3.429 0.782,0.451 0.318,2.717 -1.035,5.063 z"  style="fill:' + pieceColour.fill + '" />' +
            '<path d="m 240.048,1838.802 -0.98,3.596 1.798,0.326 c 10.137,1.56 20.667,7.303 28.025,21.227 7.358,13.923 9.551,35.834 7.926,68.335 l -0.163,0.027 h 5.72 v -0.027 c 1.643,-32.872 -2.865,-55.088 -10.622,-69.766 -7.758,-14.678 -18.921,-21.683 -30.028,-23.392 l -1.676,-0.326 z"  style="fill:' + pieceColour.fill + '" />' +
            '<path d="m 232.819,1840.39 c 32.206,3.067 50.609,24.541 49.076,88.96 h -70.547 c 0,-27.608 30.673,-19.939 24.538,-64.419"   style="stroke:' + pieceColour.stroke + ';stroke-linecap:round" />' +
            '<path d="m 238.954,1864.931 c 1.18,8.931 -17.032,22.604 -24.538,27.607 -9.201,6.136 -8.646,13.322 -15.336,12.271 -3.195,-2.896 4.335,-9.318 0,-9.203 -3.067,0 0.575,3.779 -3.067,6.136 -3.067,0 -12.278,3.067 -12.269,-12.271 0,-6.135 18.403,-36.811 18.403,-36.811 0,0 5.784,-5.835 6.135,-10.736 -2.228,-3.051 -1.534,-6.135 -1.534,-9.203 3.067,-3.067 9.202,7.669 9.202,7.669 h 6.134 c 0,0 2.398,-6.11 7.669,-9.202 3.066,0 3.066,9.202 3.066,9.202"  style="stroke:' + pieceColour.stroke + ';stroke-linecap:round;stroke-linejoin:round" />' +
            '<path d="m 194.479,1887.938 c 0,0.847 -0.687,1.533 -1.534,1.533 -0.847,0 -1.533,-0.687 -1.533,-1.533 0,-0.848 0.687,-1.534 1.533,-1.534 0.847,0 1.534,0.687 1.534,1.534 z"  style="fill:' + pieceColour.stroke + ';stroke:' + pieceColour.stroke + ';stroke-width:0.72600001;stroke-linecap:round;stroke-linejoin:round" />' +
            '<path d="m 211.143,1858.029 c -1.271,2.201 -2.895,3.642 -3.629,3.218 -0.733,-0.423 -0.298,-2.551 0.973,-4.751 1.271,-2.201 2.896,-3.642 3.629,-3.219 0.733,0.424 0.298,2.552 -0.973,4.752 z"  style="fill:' + pieceColour.stroke + ';stroke:' + pieceColour.stroke + ';stroke-width:0.72600001;stroke-linecap:round;stroke-linejoin:round" />' +
            '<path d="m 239.712,1841.618 -0.921,3.374 1.688,0.307 c 9.513,1.464 19.396,6.854 26.302,19.92 6.905,13.066 9.892,32.666 8.366,63.167 l -0.153,1.534 h 5.367 v -1.534 c 1.543,-30.85 -3.616,-50.734 -10.896,-64.509 -7.28,-13.775 -17.756,-20.349 -28.181,-21.952 l -1.572,-0.307 z"  style="fill:' + pieceColour.stroke + '" /></g></g>';
        },
        queen : function (id, squareCoordinates, pieceColour) {
            var location = parseSquareCoordinates(squareCoordinates),
                coordX = this.getCoordX(location),
                coordY = this.getCoordY(location),
                classes = this.getClasses(pieceColour);
            return '<g transform="translate(' + coordX + ',' + coordY + ')" id="' + id + '" class="' + classes + '" style="fill:' + pieceColour.fill + '"> ' +
            '<path d="m 16.91246,16.87538 c 0,2.13562 -1.35857,3.8663 -3.03312,3.8663 -1.67558,0 -3.03311,-1.73112 -3.03311,-3.8663 0,-2.13518 1.35787,-3.86585 3.03311,-3.86585 1.67455,-4.4e-4 3.03312,1.73067 3.03312,3.86585 z" style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:0.2277;stroke-linecap:round;stroke-linejoin:round" />' +
            '<path d="m 25.48446,12.02711 c 0,2.13561 -1.35787,3.8663 -3.03242,3.8663 -1.67558,0 -3.03311,-1.73113 -3.03311,-3.8663 0,-2.13518 1.35787,-3.86585 3.03311,-3.86585 1.67455,0 3.03242,1.73067 3.03242,3.86585 " style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:0.2277;stroke-linecap:round;stroke-linejoin:round" />' +
            '<path d="m 34.46742,10.80481 c 0,2.13518 -1.35787,3.86585 -3.03242,3.86585 -1.67558,0 -3.03311,-1.73111 -3.03311,-3.86585 0,-2.13562 1.35787,-3.86629 3.03311,-3.86629 1.67455,-4.4e-4 3.03242,1.73067 3.03242,3.86629 z" style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:0.2277;stroke-linecap:round;stroke-linejoin:round" />' +
            '<path d="m 43.53741,13.02758 c 0,2.13517 -1.35787,3.86585 -3.03242,3.86585 -1.67559,0 -3.03312,-1.73112 -3.03312,-3.86585 0,-2.13562 1.35788,-3.8663 3.03312,-3.8663 1.67455,0 3.03242,1.73112 3.03242,3.8663 z" style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:0.2277;stroke-linecap:round;stroke-linejoin:round" />' + 
            '<path d="m 15.65991,34.6016 c 10.11049,-2.27426 24.3692,-2.27426 31.50667,0 L 49.10911,14.85024 41.48057,29.30614 41.13109,10.63491 34.85075,29.77095 31.36904,8.02746 27.97505,29.43775 21.69402,10.2106 21.60702,30.52932 13.97779,15.73978 15.65994,34.6016 z"  style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:1;stroke-linejoin:round" />' +
            '<path d="m 15.65991,34.6016 c 0,3.03265 1.43523,3.36584 2.62527,6.39849 1.189,2.27427 1.53848,1.18312 0.94346,4.97372 -1.78471,1.51588 -1.78471,3.79014 -1.78471,3.79014 -1.78402,2.2747 0.59502,4.67088 0.59502,4.67088 7.7318,1.51633 18.92942,1.51633 26.66122,0 0,0 1.78402,-2.39618 0,-4.67088 0,0 0.59502,-2.27426 -1.18901,-3.79014 -0.59501,-3.7906 0.10257,-3.03309 1.29226,-5.30736 1.18901,-3.03264 2.27579,-3.03264 2.27579,-6.06485 -10.11084,-2.27426 -21.30881,-2.27426 -31.4193,0 z"  style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:1;stroke-linejoin:round" />' +
            '<path d="m 51.99684,16.80671 c 0,2.13562 -1.35788,3.8663 -3.03243,3.8663 -1.67558,0 -3.03311,-1.73112 -3.03311,-3.8663 0,-2.13517 1.35787,-3.86584 3.03311,-3.86584 1.67455,0 3.03243,1.73067 3.03243,3.86584 z" style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:0.2277;stroke-linecap:round;stroke-linejoin:round" />' +
            '<path d="m 16.33402,17.22895 c 0,1.49396 -0.94291,2.70401 -2.10707,2.70401 -1.16415,0 -2.10708,-1.21005 -2.10708,-2.70401 0,-1.49398 0.94293,-2.70402 2.10708,-2.70402 1.16416,0 2.10707,1.21004 2.10707,2.70402 z" style="stroke:' + pieceColour.stroke + ';stroke-width:0.72600001;stroke-linecap:round;stroke-linejoin:round" />' +
            '<path d="m 33.71737,11.14489 c 0,1.49261 -0.9429,2.70403 -2.10706,2.70403 -1.16417,0 -2.10708,-1.21142 -2.10708,-2.70403 0,-1.49397 0.94291,-2.70402 2.10708,-2.70402 1.16416,0 2.10706,1.21005 2.10706,2.70402 z" style="stroke:' + pieceColour.stroke + ';stroke-width:0.72600001;stroke-linecap:round;stroke-linejoin:round" />' + 
            '<path d="m 51.10073,17.22895 c 0,1.49396 -0.94292,2.70401 -2.10708,2.70401 -1.16415,0 -2.10707,-1.21005 -2.10707,-2.70401 0,-1.49398 0.94292,-2.70402 2.10707,-2.70402 1.16416,0 2.10708,1.21004 2.10708,2.70402 z" style="stroke:' + pieceColour.stroke + ';stroke-width:0.72600001;stroke-linecap:round;stroke-linejoin:round" />' + 
            '<path d="m 24.76231,12.4969 c 0,1.49398 -0.94291,2.70403 -2.10707,2.70403 -1.16311,0 -2.10708,-1.21005 -2.10708,-2.70403 0,-1.49398 0.94397,-2.70401 2.10708,-2.70401 1.16309,0 2.10707,1.21003 2.10707,2.70401 z"  style="stroke:' + pieceColour.stroke + ';stroke-width:0.72600001;stroke-linecap:round;stroke-linejoin:round" />' + 
            '<path d="m 42.67138,13.17291 c 0,1.49396 -0.94291,2.70402 -2.10601,2.70402 -1.16416,0 -2.10707,-1.2114 -2.10707,-2.70402 0,-1.49263 0.94291,-2.70402 2.10707,-2.70402 1.1631,0 2.10601,1.21004 2.10601,2.70402 z"  style="stroke:' + pieceColour.stroke + ';stroke-width:0.72600001;stroke-linecap:round;stroke-linejoin:round" />' + 
            '<path d="m 17.38651,36.15709 c 8.95506,-2.02801 22.12426,-2.02801 28.44549,0 L 47.93801,19.93296 40.56432,34.80508 V 15.87693 L 34.76986,34.12908 31.60924,13.84892 28.44864,34.12908 22.65418,15.20093 v 19.60415 l -7.37475,-14.87212 2.10708,16.22413"  style="stroke:' + pieceColour.stroke + ';stroke-width:0.72600001;stroke-linejoin:round" />' + 
            '<path d="m 17.38651,36.15709 c 0,2.70403 1.5803,2.70403 2.63384,5.40804 1.05354,2.02801 1.05354,1.352 0.52677,4.73202 -1.58031,1.35202 -1.58031,3.38003 -1.58031,3.38003 -1.5803,2.02802 0.52677,3.38003 0.52677,3.38003 6.84799,1.35201 17.38336,1.35201 24.23134,0 0,0 1.5803,-1.35201 0,-3.38003 0,0 0.52677,-2.02801 -1.05354,-3.38003 -0.52675,-3.38002 -0.52675,-2.70401 0.52677,-4.73202 1.05354,-2.70401 2.63385,-2.70401 2.63385,-5.40804 -8.95401,-2.02801 -19.48939,-2.02801 -28.44549,0 z"  style="stroke:' + pieceColour.stroke + ';stroke-width:0.72600001;stroke-linejoin:round" />' +
            '<path d="m 20.02035,41.56513 c 3.68737,-1.352 19.49042,-1.352 23.1778,0"  style="fill:none;stroke:' + pieceColour.stroke + ';stroke-width:0.72600001;stroke-linecap:round;stroke-linejoin:round" />' +
            '<path d="m 20.54712,46.29715 c 6.32123,-1.35201 15.80305,-1.35201 22.12426,0"  style="fill:none;stroke:' + pieceColour.stroke + ';stroke-width:0.72600001;stroke-linecap:round;stroke-linejoin:round" />' +
            '<path d="m 18.96786,49.67718 c 5.26768,-1.35201 19.49044,-1.35201 24.7581,0"  style="fill:none;stroke:' + pieceColour.stroke + ';stroke-width:0.72600001;stroke-linecap:round;stroke-linejoin:round" /></g>';
        },
        king : function (id, squareCoordinates, pieceColour) {
            var location = parseSquareCoordinates(squareCoordinates),
                coordX = this.getCoordX(location),
                coordY = this.getCoordY(location),
                classes = this.getClasses(pieceColour);
            return '<g transform="translate(' + coordX + ',' + coordY + ')" id="' + id + '" class="' + classes + '" style="fill:' + pieceColour.fill + '">' +
                '<path d="M 31.498132,15.653006 V 5.6201861" style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:1.63150001;stroke-linecap:round" />' +
                '<path d="m 31.498132,36.232326 c 0,0 7.86904,-13.50016 5.39709,-18.851 0,0 -2.10116,-4.33596 -5.39709,-4.33596 -3.29593,0 -5.39709,4.5821 -5.39709,4.5821 -2.47195,5.34905 5.39709,18.60486 5.39709,18.60486" style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:0.40790001" />' +
                '<path d="m 13.935751,53.877606 c 9.063821,6.24264 24.978231,6.11957 34.042051,-0.12307 l -0.11371,-11.25995 c 0,0 12.115845,-6.43527 7.171945,-17.13516 -6.591865,-11.59348 -22.247545,-6.24264 -26.367465,7.13445 l 2.82956,3.42632 v -6.24265 c -5.99366,-10.92819 -17.689271,-15.05368 -23.7653314,-5.05297 -4.9439,10.70168 5.9771804,17.83612 5.9771804,17.83612 l 0.22577,11.41691 z" style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:0.40790001;stroke-linecap:round;stroke-linejoin:round" />' + 
                '<path d="m 27.378212,9.1874161 h 8.23984" style="fill:' + pieceColour.fill + ';stroke:' + pieceColour.fill + ';stroke-width:1.63150001;stroke-linecap:round" />' +
                '<path d="M 31.361762,14.560576 V 5.9727061" style="fill:none;stroke:' + pieceColour.stroke + ';stroke-width:0.48899999;stroke-linecap:round" />' +
                '<path d="m 31.361762,34.980636 c 0,0 6.10833,-11.4505 4.07222,-16.0307 0,0 -1.3574,-3.81683 -4.07222,-3.81683 -2.71481,0 -4.07221,3.81683 -4.07221,3.81683 -2.03611,4.5802 4.07221,16.0307 4.07221,16.0307" style="stroke:' + pieceColour.stroke + ';stroke-width:0.48899999" />' +
                '<path d="m 16.430302,53.301436 c 7.46573,5.34357 21.03979,5.34357 28.50553,0 v -10.68713 c 0,0 12.216645,-6.8703 8.144425,-16.0307 -5.429615,-9.92377 -18.324975,-5.34357 -21.718495,6.10693 v 5.34357 -5.34357 c -4.75092,-11.4505 -17.646281,-16.0307 -21.7184914,-6.10693 -4.07222,9.1604 6.7870314,15.26733 6.7870314,15.26733 v 11.4505 z" style="stroke:' + pieceColour.stroke + ';stroke-width:0.48899999;stroke-linecap:round;stroke-linejoin:round" />' +
                '<path d="m 27.968252,9.0261661 h 6.78703" style="fill:none;stroke:' + pieceColour.stroke + ';stroke-width:0.48899999;stroke-linecap:round" />' +
                '<path d="m 16.430302,41.850936 c 7.46573,-3.81683 21.03979,-3.81683 28.50553,0.76337" style="fill:none;stroke:' + pieceColour.stroke + ';stroke-width:0.48899999;stroke-linecap:round" />' +
                '<path d="m 16.430302,53.301436 c 7.46573,-3.81683 21.03979,-3.81683 28.50553,0" style="fill:none;stroke:' + pieceColour.stroke + ';stroke-width:0.48899999;stroke-linecap:round;stroke-linejoin:round" />' +
                '<path d="m 16.430302,47.957866 c 7.46573,-3.05346 21.03979,-3.05346 28.50553,0" style="fill:none;stroke:' + pieceColour.stroke + ';stroke-width:0.48899999;stroke-linecap:round;stroke-linejoin:round" />' +
                '<path d="m 44.257122,41.850936 c 0,0 11.537955,-6.10693 8.179725,-14.73756 -5.265375,-8.93139 -17.681565,-2.81987 -21.075085,7.10389 l 0.0163,3.20156 -0.0163,-3.20156 c -3.39351,-9.92376 -17.093811,-16.03528 -21.043861,-7.10389 -3.3894504,8.63063 6.580701,13.7406 6.580701,13.7406" style="fill:none;stroke:' + pieceColour.fill + ';stroke-width:0.48899999;stroke-linecap:round;stroke-linejoin:round" /></g>';

        }
    };
    return {
        parseSquareCoordinates : parseSquareCoordinates,
        coordinates : coordinates,
        ChessPieces : ChessPieces
    };
}());
