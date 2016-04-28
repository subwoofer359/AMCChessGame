var promotion = (function() {
    var messageRegex = /PAWN_PROMOTION\s\(([A-Ha-h]),([1-8])\)/;
    
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
    
    function parsePromotionMessage(message) {
        if(messageRegex.test(message)) {
            var find = messageRegex.exec(message);
            var letter = find[1].toLowerCase();
            var number = find[2];
            return letter + number;
        }
        throw "Message can't be parsed";
    }
    
    return {
        findPawnForPromotion : findPawnForPromotion,
        parsePromotionMessage : parsePromotionMessage
    };
})();