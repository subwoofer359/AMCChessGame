var PawnPieceModule = (function(){

    function PawnPiece(pieceColour) {
        "use strict";
        ChessPieceModule.ChessPiece.call(this, pieceColour);
    }

    PawnPiece.prototype = Object.create(ChessPieceModule.ChessPiece.prototype);

    PawnPiece.prototype.toString = function (id, squareCoordinates, pieceColour) {
        var location = this.parseSquareCoordinates(squareCoordinates),
            style = 'style="fill:' + pieceColour.fill + ';fill-opacity:1;stroke:' + 
            pieceColour.stroke + ';stroke-width:0.40499824;stroke-linecap:butt;stroke-linejoin:round;' +
            'stroke-miterlimit:3.5;stroke-opacity:0.98453603;stroke-dasharray:none;' +
            'stroke-dashoffset:50;"';
        return '<g transform="translate(' + this.getCoordX(location) + ',' + this.getCoordY(location) + ')" id="' + id + '" class="' + this.getClasses(pieceColour) + '"> ' +
                    '<path d="m 19.201062,45.600762 c 8.40554,0.058 16.81108,0.1154 25.21663,0.1731 -3.30135,-7.251102 -11.05511,-13.612602 -9.90404,-21.753502 -2.17012,0 -4.34024,0 -6.51036,0 2.0749,8.0832 -5.86815,14.3869 -8.80223,21.580402 z" ' + style + '/> ' +
                    '<path d="m 41.045412,15.486384 c 0,4.7665 -4.3606,8.6304 -9.73967,8.6304 -5.37908,0 -9.73968,-3.8639 -9.73968,-8.6304 0,-4.7664 4.3606,-8.6303005 9.73968,-8.6303005 5.37907,0 9.73967,3.8639005 9.73967,8.6303005 z" ' + style + '/>' +
                    '<path d="m 12.181722,58.139172 c 10.92761,0 27.7945,0.35681 38.72211,0.35681 0,-4.2696 0.97188,-9.5206 -3.13162,-12.8983 -10.92761,0 -21.63925,0 -32.56686,0 -3.77954,2.3072 -3.02363,8.27179 -3.02363,12.54149 z" ' + style + '/></g>';
    };

    return {
        PawnPiece : PawnPiece
    };
}());