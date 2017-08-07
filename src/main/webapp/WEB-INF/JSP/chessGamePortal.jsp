<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%@ include file="/BootStrapHeader.jsp" %>    

<meta name="viewport" content="width=device-width, initial-scale=0.8, maximum-scale=1.0, user-scalable=no">

<script src="//cdn.jsdelivr.net/sockjs/0.3.4/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/interact.js/1.2.9/interact.min.js"></script>
<script src="${pageContext.request.contextPath}/js/require.js"></script>

<title>Chess Game</title>
<style>
    @import url(${pageContext.request.contextPath}/css/General.css);
    @import url(${pageContext.request.contextPath}/css/PromotionDialog.css);
    body{
          background-image: url(${pageContext.request.contextPath}/img/1700128.jpg);
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
        /*padding: 15px;*/
        font-size: 2em;
        text-align: center;
    }
    .alert {
        margin-bottom: 5px;
        min-height: 2.6em;
    }
    
    .bottom-spacer {
         height: 220px;
    }
    
    @media (max-width:600px) {
        body {
            overflow-x: auto;
            background-repeat: repeat-x;
        }
    }
    
    @media (max-height:650px) {
        body {
            overflow-y: auto;
            background-repeat: repeat-y;
        }
    }
    
    #promotionDialog {
        width:50%;
    }
    
    .hidePromotionDialog {
        display: none;
    }
    .displayPromotionDialog {
        position: absolute;
        display: block;
        top: 20%;
        left: 25%;
        right: 25%;
    }
    
    #promotionDialog .panel-body {
        border-style: dashed;
        border-width: 1px;
        border-color: black;
        background-color: white;
    }
    #promotionDialog button {
        display: block;
        margin-left: auto;
        margin-right: auto;
        width:120px;
        height: 120px;
        padding: 0;
        border-radius: 122px 122px;
        
    }
    
    @media(max-width:992px) {
        #promotionDialog button {
            margin-bottom: 20px;
            width: 100%;
            border-radius: 12px 12px;
        }   
    }
    
</style>
<script>
    require(['../../js/app.js'], function () {
        require(["snapsvg",
                 "../../js/chessGamePortal.js",
                "../../js/InteractActions.js",
                "../../js/chessGameInteract.js",
            ], function (Snap, chessGamePortal, interact) {
                        var headerName = "${_csrf.headerName}",
                            token = "${_csrf.token}",
                            stompObject = {};
                        
                        stompObject.headers = {};
                        stompObject.headers[headerName] = token;
                        stompObject.URL = "http://" + location.hostname + ":" + location.port +
                                            "${pageContext.request.contextPath}" +
                                            "/app/chessgame/chessgame";
                        stompObject.gameUUID = "${GAME_UUID}";
                        stompObject.playerName = '<c:out value="${GAME.player.name}"/>';
                        stompObject.opponentName = '<c:out value="${GAME.opponent.name}"/>';
                        stompObject.playerColour = '<c:out value="${CHESSPLAYER.colour}"/>';
                        
                        chessGamePortal.setStompjs(Stomp);

                        var stompClient = chessGamePortal.setupStompConnection(stompObject);
                        chessGameInteract(new interact.InteractActions (stompClient, "${GAME_UUID}"));
                        
                        chessGamePortal.addMessageDialogListener();
        });
    });

</script>
</head>
<body>
    <tags:BottomMenu>
    	<tags:NavMenuButton id="home-btn" url="${pageContext.request.contextPath}/app/chessgame/chessapplication"><span class="glyphicon glyphicon-home"></span> Home</tags:NavMenuButton>
        <tags:NavMenuButton id="save-btn" aclass="save-btn"><span class="glyphicon glyphicon-floppy-disk"></span> Save Game</tags:NavMenuButton>
        <tags:NavMenuButton id="quit-btn" aclass="quit-btn"><span class="glyphicon glyphicon-remove"></span> Quit Game</tags:NavMenuButton>
    </tags:BottomMenu>
    
    <div class="container-fluid full-height">
    <div class="row full-height">
        <tags:SideMenu>
                <li><a href="${pageContext.request.contextPath}/app/chessgame/chessapplication"><button id="home-btn">Home</button></a></li>
                <li><button class="save-btn">Save Game</button></li>
                <li><button class="quit-btn">Quit Game</button></li>
        </tags:SideMenu>

        <div class="player-name col-sm-offset-2 col-sm-8 col-md-offset-0 col-md-10 col-xs-12 hidden-xs">
            <div id="white-player" class="player-name-holder col-sm-5"><span class="title title-player">Player:</span><span class="name"><c:out value="${GAME.player.name}"/></span></div>
            <div id="black-player" class="player-name-holder col-sm-5"><span class="title title-opponent">Opponent:</span><span class="name"><c:out value="${GAME.opponent.name}"/></span></div>
        </div>
        <div id="my-alert" class="col-xs-10 col-sm-offset-2 col-sm-5 alert-hidden">
            <div class="alert alert-warning" role="alert"></div>
        </div>
        <div id="chessboard-surround" class="col-sm-6 col-sm-offset-2 col-xs-12">
            <div class="inner col-xs-offset-1 col-xs-11">
            </div><!-- col-xs-11 -->
        </div>
    </div>
    <div class="row bottom-spacer">
    </div>
  </div>
<%@ include file="/WEB-INF/JSP/Promotion2.jspf" %>
<%@ include file="/BootStrapFooter.jsp" %>
</body>
</html>