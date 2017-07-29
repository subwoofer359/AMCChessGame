requirejs.config({
    "baseUrl": "",
    "paths": {
        "app": "../app",
        "jquery": "http://code.jquery.com/jquery-2.2.4.min"
    }
});
/*
requirejs(["./js/chessboard.js"], function (myApp) {
    var jsonC = '{"squares":{"C8":"B","A2":"p","A1":"r","C7":"P","A8":"R","A7":"P","E7":"P","E8":"K","G7":"P","G8":"N","E2":"p","E1":"k","G2":"p","G1":"n","C1":"b","C2":"p","D8":"Q","B2":"p","D7":"P","B8":"N","B7":"P","F8":"B","F7":"P","H8":"R","F1":"b","H7":"P","H2":"p","H1":"r","F2":"p","D2":"p","D1":"q","B1":"n"}}';
    board = myApp.chessboardModule.createChessBoard("WHITE", jsonC);
    $('#chessboard-surround').html(board);
});
*/