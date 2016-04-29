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
    
    function stompConnection(stompObject) {
    	var stompClient,
        	socket;
    	socket = new SockJS(stompObject.URL);
	    stompClient = Stomp.over(socket);
	    setupStompConnection(stompClient, stompObject);
    }
    
    function setUpStompConnection(stompClient, stompObject) {
	    var	gameUUID = stompObject.gameUUID,
	        USER_UPDATES = "/user/queue/updates",
	        TOPIC_UPDATES = "/topic/updates/",
	        APP_GET = "/app/get/",
	        PRIORITY = {priority : 9},
	        APP_PROMOTE = "/app/promote/",
	        PROMOTE = "promote ",
	        chessboard,
	        squareOfPawn;
	        
	    
	    stompClient.connect(stompObject.headers, function() {
	        stompClient.subscribe(USER_UPDATES, function(message){
	            if(message.headers.TYPE === "STATUS") {
	                squareOfPawn = parsePromotionMessage(message.body);
	                return squareOfPawn;
	            } 
	        });
	        
			stompClient.subscribe(TOPIC_UPDATES + gameUUID, function(message){
			    if(message.headers.TYPE === "UPDATE") {
	                var board = $.parseJSON(message.body);
	                playerColour = board.currentPlayer.colour;
	            } else if(message.headers.TYPE === "STATUS") {
	                squareOfPawn = parsePromotionMessage(message.body);
	            }
	        });
	        
	        stompClient.send(APP_GET + gameUUID, PRIORITY, "Get ChessBoard");
	        
	        $("#queenBtn").click(function() {
	            var piece = playerColour === "WHITE" ? "q" : "Q";
	            stompClient.send(APP_PROMOTE + gameUUID, PRIORITY, PROMOTE + piece + squareOfPawn);
	        });
	        $("#rookBtn").click(function() {
	            var piece = playerColour === "WHITE" ? "r" : "R";
	            stompClient.send(APP_PROMOTE + gameUUID, PRIORITY, PROMOTE + piece + squareOfPawn);
	        });
	        $("#knightBtn").click(function() {
	            var piece = playerColour === "WHITE" ? "n" : "N";
	            stompClient.send(APP_PROMOTE + gameUUID, PRIORITY, PROMOTE + piece + squareOfPawn);
	        });
	        $("#bishopBtn").click(function() {
	            var piece = playerColour === "WHITE" ? "b" : "B";
	            stompClient.send(APP_PROMOTE + gameUUID, PRIORITY, PROMOTE + piece + squareOfPawn);
	        });
	    });
	    
    }
    
    return {
    	parsePromotionMessage : parsePromotionMessage,
    	findPawnForPromotion : findPawnForPromotion,
    	setUpStompConnection : setUpStompConnection,
        stompConnection : stompConnection,
    };
})();