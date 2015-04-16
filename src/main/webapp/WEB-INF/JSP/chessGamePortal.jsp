<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- delete later -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no">
<!-- Bootstrap -->
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<!-- delete later{end} -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/interact.js/1.2.3/interact.min.js"></script>
<script src="../../js/chesspieces.js"></script>
<script src="../../js/chessboard.js"></script>
<script src="../../js/player.js"></script>
<title>Chess Game</title>
<style>
    @import url(../../css/General.css);
    body{
          background-image: url(../../img/1700128.jpg);
          background-size: cover;
          background-repeat: no-repeat;
    }
    #header{
        width:100%;
        padding-bottom: 70px;
    }
    
    #title{
        border-style: solid;
        text-align: center;
        border-radius: 33px;
        background-color: #428bca;
        color: white;
    }
    
    .container{
        width: 100%;
    }
    
    #playerBlock legend{
        background-color: #428bca;
        border-radius: 10px 10px;
        padding-left: 20px;
        color: white;
    }
    
    #playerBlock span{
        padding-left: 20px;
    }
    
    .side-menu{
        font-size:1.8em;
    }
    
    .side-menu button{
        padding: 0;
        background-color: #2886d5;
        border: none;
    }
      
    #chessboard .inner {
        margin-top: 10%;
    }
/** 
  *   Player's Name boxes 
  *   Duplicate in ChessAplicationPage.html
  *   Todo remove
  */    
     .player-name {
        font-family: "Raleway";
        font-size: 2em;
        margin-top: 30px;
        overflow: hidden;
        height: 50px;
        padding-bottom: 120px;
        min-width: 470px;
    }
    
    .player-name .title {
        display: inline-block;
        font-family: "Orbitron";
        border-style: double;
        border-width: 1px;
        background-color: antiquewhite;
        border-radius: 15px 0px 0px 15px;
        background-color: #2886d5;    
        box-shadow: 5px 5px 8px #000000;
    }
    
    .player-name .title-player {
        padding:10px 34px 12px 15px;
    }
    
    .player-name .title-opponent {
        padding:10px 1px 12px 15px;
    }
    
    .player-name .name {
        display: inline-block;
        border-style: double;
        border-left-style: none;
        border-width: 1px;
        padding:8px 30px 14px 15px;
        background-color: #ffffff;
        box-shadow: 5px 5px 8px #000000;
        min-width: 298px;
    }
    
    .player-name-holder {
        min-width: 488px;
        margin-bottom: 5px;
    }
    
    .player-name-highlight{
        background-color: green!important;
    }
    
    .drop-target{
        fill:red!important;
    }
    
    .alert-hidden {
        opacity: 0;
    }
    
    #my-alert {
        padding: 15px;
        font-size: 2em;
        text-align: center;
    }
    
</style>
<script>
$(document).ready(function(){
var sourceId = "",
    destId = "",
    startOfMove = true,
    playerColour = '<c:out value="${CHESSPLAYER.colour}"/>'
    alertBox = $("#my-alert"),
    alertBoxText = $("#my-alert .alert");
    GAME_STATUS = {
            WHITE_CHECKMATE : "WHITE_CHECKMATE",
            BLACK_CHECKMATE : "BLACK_CHECKMATE",
            STALEMATE : "STALEMATE",
            WHITE_IN_CHECK : "WHITE_IN_CHECK",
            BLACK_IN_CHECK : "BLACK_IN_CHECK"
    };
    
    $("#quit-btn").click(function () {
        stompClient.send("/app/quit/${GAME_UUID}", {priority: 9},"quit");
    });
    
interact('.draggable')
  .draggable({
    // enable inertial throwing
    inertia: true,
    // keep the element within the area of it's parent
    restrict: {
      restriction: '#layer1',
      endOnly: true,
      elementRect: { top: 0, left: 0, bottom: 1, right: 1 }
    },

    // call this function on every dragmove event
    onmove: function (event) {
      var target = event.target,
          // keep the dragged position in the data-x/data-y attributes
          x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx,
          y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;

      var correctY=target.transform.baseVal[0].matrix.f;
      var correctX=target.transform.baseVal[0].matrix.e;
      // translate the element
      target.style.webkitTransform =
      target.style.transform =
        'translate(' + (x+correctX) + 'px, ' + (y+correctY) + 'px)';

      // update the posiion attributes
      target.setAttribute('data-x', x);
      target.setAttribute('data-y', y);
    },
    // call this function on every dragend event
  });


interact('.dropzone').dropzone({
    accept: '.chesspiece',
    overlap: 0.1,
    ondropactivate: function (event) {
    // add active dropzone feedback
    event.target.classList.add('drop-active');
  },
  ondragenter: function (event) {
    var draggableElement = event.relatedTarget,
        dropzoneElement = event.target;

    // feedback the possibility of a drop
    dropzoneElement.classList.add('drop-target');
    draggableElement.classList.add('can-drop');

  },
  ondragleave: function (event) {
    event.target.classList.remove('drop-target');
    event.relatedTarget.classList.remove('can-drop');
    if(startOfMove){
	   console.log("Source:"+event.target.id);
       sourceId=event.target.id;
	   startOfMove=false;
    }
  },
  ondrop: function (event) {
	destId=event.target.id;
	console.log("destination:"+destId);
	startOfMove=true;
    var move=sourceId+"-"+destId;
    stompClient.send("/app/move/${GAME_UUID}",{priority: 9},move);
  },
  ondropdeactivate: function (event) {
    // remove active dropzone feedback
    event.target.classList.remove('drop-active');
    event.target.classList.remove('drop-target');
  }
});
    
    var socket=new WebSocket("ws://${pageContext.request.localAddr}:${pageContext.request.localPort}${pageContext.request.contextPath}/app/chessgame/chessgame");
	var stompClient=Stomp.over(socket);
	var subid='234';
	stompClient.connect({},
	        function(frame){
            var oldChessBoard = {};
        
            function updateChessBoard(playerColour, chessBoardJson) {
                createChessBoard(playerColour, chessBoardJson);
                oldChessBoard = chessBoardJson;
                updatePlayer(chessBoardJson);
            }
        
        function showFadingAlertMessage(message) {
            alertBoxText.html(message);
            alertBox.css("display","block");
            alertBox.css("opacity","1");
            alertBox.fadeOut(5000);
        }
        
        function showAlertMessage(message) {
            alertBoxText.html(message);
            alertBox.css("display","block");
            alertBox.css("opacity","1");
        }
        
	    	stompClient.subscribe("/user/queue/updates",
	    	        function(message){
                            if(message.headers.TYPE === "ERROR") {
                                showFadingAlertMessage(message.body);
                                if(oldChessBoard !== undefined || oldChessBoard !== {}) {
                                    createChessBoard(playerColour, oldChessBoard);
                                }
                            } else if(message.headers.TYPE === "UPDATE") {
                                updateChessBoard(playerColour, message.body);
                            } else if(message.headers.TYPE === "INFO") {
                                console.log(message.body);
                            }
            });
	    	
            stompClient.subscribe("/topic/updates/${GAME_UUID}",
    	        function(message){
                        console.log("message:" + message.body);
                        if(message.headers.TYPE === "ERROR") {
                            updateChessBoard(message.body);    
                        } else if(message.headers.TYPE === "STATUS") {
                            switch(message.body) {
                            case GAME_STATUS.WHITE_CHECKMATE:
                                    showAlertMessage("${GAME.opponent.name} has won the game");
                                    break;
                            case GAME_STATUS.BLACK_CHECKMATE:
                                    showAlertMessage("${GAME.player.name} has won the game");
                                    break;
                            case GAME_STATUS.STALEMATE:
                                    showAlertMessage("Game has ended in a draw");
                                    break;
                            case GAME_STATUS.WHITE_IN_CHECK:
                                    showFadingAlertMessage("${GAME.player.name}'s king is in check");
                                    break;
                            case GAME_STATUS.BLACK_IN_CHECK:
                                    showFadingAlertMessage("${GAME.opponent.name}'s king is in check");
                                    break;
                            default:
                                    break;
                            }
                        } else if(message.headers.TYPE === "INFO"){
                            if (/has quit the game$/.test(message.body)) {
                                showAlertMessage(message.body);
                            } else {
                                console.log("INFO:" + message.body);
                            }
                        } else if(message.headers.TYPE === "UPDATE") {
                            updateChessBoard(playerColour, message.body);
                        }
                });
        
            stompClient.send("/app/get/${GAME_UUID}", {priority: 9},"Get ChessBoard");
	});	    	
});
</script>
</head>
<body>
    <div class="container-fluid full-height">
    <div class="row full-height">
        <div class="sidebar-left col-sm-2 hidden-sm hidden-xs">
            <a id="chess-icon" href="https://openclipart.org/detail/18661/-by--18661">
                <img alt="Queen" src="https://openclipart.org/download/18661/portablejim-Chess-tile-Queen-3.svg" />
            </a>    
            <div class="side-menu">
            <ul>
                <li><button id="save-btn">Save</button></li>
                <li><button id="quit-btn">Quit</a></li>
            </ul>
            </div>
        </div><!-- sidebar-left -->
        <div class="player-name col-sm-offset-2 col-sm-8 col-md-offset-0 col-md-10 col-xs-12">
            <div id="white-player" class="player-name-holder col-sm-5"><span class="title title-player">Player:</span><span class="name"><c:out value="${GAME.player.name}"/></span></div>
            <div id="black-player" class="player-name-holder col-sm-5"><span class="title title-opponent">Opponent:</span><span class="name"><c:out value="${GAME.opponent.name}"/></span></div>
        </div>
        <div id="chessboard-surround" class="col-sm-6 col-sm-offset-2 col-xs-8">
            <div class="inner col-xs-offset-1 col-xs-11">
            </div><!-- col-xs-11 -->
        </div>
        <div id="my-alert" class="col-xs-10 col-sm-offset-2 col-sm-5 alert-hidden">
            <div class="alert alert-warning" role="alert"></div>
        </div>
    </div>
  </div>
</body>
</html>