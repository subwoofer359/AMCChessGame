/*global ChessPieceModule */
var RookPieceModule = (function () {
    'use strict';
    function RookPiece(pieceColour) {
        ChessPieceModule.ChessPiece.call(this, pieceColour);
    }

    RookPiece.prototype = Object.create(ChessPieceModule.ChessPiece.prototype);

    RookPiece.prototype.toString = function (id, squareCoordinates, pieceColour) {
        var location = this.parseSquareCoordinates(squareCoordinates),
            style = 'style="fill:' + pieceColour.fill +
                ';fill-opacity:1;stroke:' + pieceColour.stroke +
                ';stroke-width:0.44588459;stroke-linecap:butt;stroke-linejoin:round;stroke-miterlimit:3.5;' +
                'stroke-opacity:0.98453603;stroke-dasharray:none;stroke-dashoffset:50"';
        return '<g ' +  'id="' + id + '" class="' + this.getClasses(pieceColour) + '" transform="translate(' + this.getCoordX(location) + ',' + this.getCoordY(location) + ')">' +
                '<path ' + style + ' d="M 18.664173,19.97417 43.027644,19.78706 46.235299,44.6102 16.098051,44.5167 z" />' +
                '<path ' + style + ' d="m 12.583803,8.84895 36.538917,0 -1.283062,11.11695 -33.652029,-0.0935 z" />' +
                '<rect ' + style + ' width="7.2172222" height="4.3972631" x="12.569632" y="4.3498535" />' +
                '<rect ' + style + ' width="7.2172222" height="4.3972631" x="22.753931" y="4.3030701" />' +
                '<rect ' + style + ' width="7.2172222" height="4.3972631" x="41.518715" y="4.3030701" />' +
                '<rect ' + style + ' width="7.2172222" height="4.3972631" x="32.697662" y="4.3030701" />' +
                '<path ' + style + ' d="m 9.0236162,57.76948 c 12.5961078,0 32.0383488,0.37522 44.6344558,' +
                '0.37522 0,-4.4898 1.120276,-10.01147 -3.609778,-13.56319 -12.596108,0 -24.943265,0 -37.539373, ' +
                '0 -4.3566298,2.42606 -3.4853048,8.69817 -3.4853048,13.18797 z"/></g>';
    };

    return {
        RookPiece : RookPiece
    };
}());