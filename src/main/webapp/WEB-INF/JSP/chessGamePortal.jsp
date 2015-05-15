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
<script src="../../js/chessGamePortal.js"></script>
<script src="../../js/chessGameInteract.js"></script>

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
    var stompClient = setupStompConnection("ws://${pageContext.request.localAddr}:${pageContext.request.localPort}" +
                        "${pageContext.request.contextPath}" +
                        "/app/chessgame/chessgame", 
                        "${GAME_UUID}",
                        "${GAME.player.name}", 
                        "${GAME.opponent.name}", 
                        '<c:out value="${CHESSPLAYER.colour}"/>'
                       );
    chessGameInteract(stompClient, "${GAME_UUID}");
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
                <li><a href="./chessapplication"><button id="home-btn">Home</button></a></li>
                <li><button id="quit-btn">Quit Game</a></li>
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