var promotion = (function() {
    function findPawnForPromotion(colour, chessBoardObj) {
        var whiteRank = 8,
            blackRank = 1,
            pawn = colour === "WHITE" ? 'p' : 'P';
       for(var s in chessBoardObj.squares) {
           if(chessBoardObj.squares[s] === pawn) {
               return s;
           }
       }
    }
    
    return {
        findPawnForPromotion : findPawnForPromotion
    };
})();