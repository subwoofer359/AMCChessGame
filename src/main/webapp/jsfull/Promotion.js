var promotion = (function() {
    function findPawnForPromotion(colour, chessBoardObj) {
        var whiteRank = 8,
            blackRank = 1,
            rank = colour === "WHITE" ? whiteRank : blackRank;
            pawn = colour === "WHITE" ? 'p' : 'P';
       for(var s in chessBoardObj.squares) {
           if(chessBoardObj.squares[s] === pawn) {
               var rankExp = new RegExp(rank + "$");
               if(rankExp.test(s)) {
                   return s.toLowerCase();
               }
           }
       }
    }
    
    return {
        findPawnForPromotion : findPawnForPromotion
    };
})();