<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%@ include file="/BootStrapHeader.jsp" %>    

<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<script src="//cdn.jsdelivr.net/sockjs/0.3.4/sockjs.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/interact.js/1.2.9/interact.min.js"></script>
<script src="${pageContext.request.contextPath}/js/require.js"></script>

<title>Chess Game</title>
<style>
    @import url(${pageContext.request.contextPath}/css/General.css);
    @import url(${pageContext.request.contextPath}/css/PromotionDialog.css);
    @import url(${pageContext.request.contextPath}/css/GamePortal.css);
</style>
<script>
    require(["../../js/app.js"], function () {
        require(["snapsvg",
                 "../../js/chessGamePortal",
                 "../../js/StompObject"
                ], function (Snap, chessGamePortal, StompObj) {                        
                    var s = new StompObj.StompObject(
                        "http://" + location.hostname + ":" + location.port +
                            "${pageContext.request.contextPath}" +
                            "/app/chessgame/chessgame",
                        "${GAME_UUID}",
                        '<c:out value="${GAME.player.name}"/>',
                        '<c:out value="${GAME.opponent.name}"/>',
                        '<c:out value="${CHESSPLAYER.colour}"/>');

                    s.headers["${_csrf.headerName}"] = "${_csrf.token}";
                    
                    // todo: A Hack to be fixed
                    chessGamePortal.setStompjs(Stomp);
            <c:choose>
                <c:when test="${gameType eq 'TWOVIEW'}">
                    chessGamePortal.setupStompConnection(s);
                </c:when>
                <c:when test="${gameType eq 'ONEVIEW'}">
                    chessGamePortal.setupOneViewStompConnection(s);
                </c:when>
            </c:choose>
        });
    });
</script>
</head>
<body>
    <tags:BottomMenu>
    	<tags:NavMenuButton id="home-btn" url="${pageContext.request.contextPath}/app/chessgame/chessapplication"><span class="glyphicon glyphicon-home"></span> Home</tags:NavMenuButton>
        <tags:NavMenuButton id="save-btn" aclass="save-btn"><span class="glyphicon glyphicon-floppy-disk"></span> Save Game</tags:NavMenuButton>
        <tags:NavMenuButton id="quit-btn" aclass="quit-btn"><span class="glyphicon glyphicon-remove"></span> End Game</tags:NavMenuButton>
    </tags:BottomMenu>
    
    <div id="boardContainer" class="container-fluid full-height">
    <div class="row full-height">
        <tags:SideMenu>
                <li><a href="${pageContext.request.contextPath}/app/chessgame/chessapplication"><button id="home-btn">Home</button></a></li>
                <li><button class="save-btn">Save Game</button></li>
                <li><button class="quit-btn">End Game</button></li>
        </tags:SideMenu>

        <div class="player-name col-sm-offset-2 col-sm-8 col-md-offset-0 col-md-10 col-xs-12">
            <div id="white-player" class="player-name-holder col-sm-5"><span class="title title-player">Player:</span><span class="name"><c:out value="${GAME.player.name}"/></span></div>
            <div id="black-player" class="player-name-holder col-sm-5"><span class="title title-opponent">Opponent:</span><span class="name"><c:out value="${GAME.opponent.name}"/></span></div>
        </div>
        <div id="my-alert" class="col-xs-12 col-sm-offset-2 col-sm-5 alert-hidden">
            <div class="alert alert-warning" role="alert"></div>
        </div>
        <div id="chessboard-surround" class="col-sm-6 col-sm-offset-2 col-xs-12" ontouchmove="return false;">
            <div class="inner col-xs-offset-1 col-xs-11">
            </div><!-- col-xs-11 -->
        </div>
    </div>
    <div class="row bottom-spacer">
    </div>
  </div>
<%@ include file="/WEB-INF/JSP/Promotion.jspf" %>
<script>
    $(document).ready(function(){
    <c:choose>
        <c:when test="${gameType eq 'TWOVIEW'}">
        connection = new promotionModule.TwoViewStompConnection();
        </c:when>
        <c:when test="${gameType eq 'ONEVIEW'}">
        connection = new promotionModule.OneViewStompConnection();
        </c:when>
    </c:choose>
        connection.connect();
    });
</script>
<%@ include file="/BootStrapFooter.jsp" %>
</body>
</html>