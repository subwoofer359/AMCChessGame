<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Chess Application Home Page</title>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no">
<meta name="_csrf" content="${_csrf.token}"/>
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}"/>
    
<!-- Bootstrap -->
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" rel="stylesheet">
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
<script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->

 <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
 <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
 <script src="//cdn.jsdelivr.net/sockjs/0.3.4/sockjs.min.js"></script>
 <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.js"></script>
 <script src="${pageContext.request.contextPath}/js/sidebar.js"></script>
 <script src="${pageContext.request.contextPath}/js/selectTableRow.js"></script>
 <script src="${pageContext.request.contextPath}/jsfull/ajaxCSRF.js"></script>
 <script src="${pageContext.request.contextPath}/jsfull/ChessApplicationPage.js"></script>
<style>
    @import url(../../css/General.css);
    @import url(../../css/selectTableRow.css);
    @import url(../../css/PlayerName.css);
    @import url(../../css/PlayerList.css);
    
    body {
        background-image: url(../../img/1700128.jpg);
        background-size: cover;
        background-repeat: repeat-y;
        overflow: auto;
    }
    
    
    
    .side-menu{
        font-size:1.8em;
    }
    
    .side-menu button{
        padding: 0;
        background-color: #2886d5;
        border: none;
    }
    
    .games-table {
        font-size: 1.5em;
        color: black;
        margin-top:100px;
        height: 70vh;
        overflow: auto;
        box-shadow: 10px 10px 8px #000000;
        background-color: white;
        opacity: 0.8;
    }
    
    .games-table th{
        font-family: "Orbitron";
        background-color: #2886d5;
    }
    
    .games-table tbody tr {
        background-color: antiquewhite;
    }
    
    /* Hide checkboxes in the tables */
    .games-table tr td:last-of-type,tr th:last-of-type {
        display: none;
    }
        
    #other-games-table {
    	margin-bottom:70px;
    }
</style>
<script>
    var playerName = '<c:out value="${PLAYER.name}"/>',
    	userName = '<c:out value="${PLAYER.userName}"/>';
</script>
</head>
<body>
<form id="side-menu-form" method="post">
    <input type="hidden" name="${_csrf.parameterName}"	value="${_csrf.token}"/>
<nav role="navigation" class="navbar navbar-default navbar-fixed-bottom visible-xs visible-sm">
 <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" data-target="#navbarCollapse" data-toggle="collapse" class="navbar-toggle">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            
            
            <a href="#" class="navbar-brand"></a>
        </div>
        <!-- Collection of nav links and other content for toggling -->
        <div id="navbarCollapse" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li><button id="userToggle" type="button" class="btn btn-default btn-block btn-lg">
                    User List <span class="glyphicon glyphicon-user"></span></button>
                </li>
                <li><button formaction="createGame" type="submit" class="btn btn-default btn-block btn-lg">Create Game</button></li>
                <li><button type="submit" class="join-button btn btn-default btn-block btn-lg ">Join Game</button></li>
                <li><button type="submit" class="btn btn-default btn-block btn-lg">Delete Game</button></li>
                <li><a href="${pageContext.request.contextPath}/app/chessgame/logout" role="button" class="btn btn-default btn-block btn-lg">Log out</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="container-fluid full-height">
   
    <div class="row full-height">
        <div class="sidebar-left col-sm-2 hidden-sm hidden-xs">
            <a id="chess-icon" href="https://openclipart.org/detail/18661/-by--18661">
                <img alt="Queen" src="https://openclipart.org/download/18661/portablejim-Chess-tile-Queen-3.svg" />
            </a>    
            <div class="side-menu">
            <ul>
                <li><button formaction="createGame" type="submit">Create Game</button></li>
                <li><button class="join-button" type="submit">Join Game</button></li>
                <li><a class="description" href="#">Delete Game</a></li>
                <li><a href="${pageContext.request.contextPath}/app/chessgame/logout">Log out</a></li>
            </ul>
            </div>
        </div><!-- sidebar-left -->
        <div class="player-name col-sm-10 col-xs-12"><span class="title">Player:</span><span class="name">${PLAYER.name}</span></div>
        
        <div class="col-xs-12 col-md-5">
            <div id="your-games-table" class="games-table">
            <table class="table table-bordered table-striped">
                <thead>
				<tr>
					<th>Game</th>
					<th>Player</th>
                    <th>Opponent</th>
					<th>Status</th>
					<th></th>
				</tr>
				</thead>
                <tbody>
                    <c:forEach var="game" items='${GAMEMAP}'>
                        <c:if test="${PLAYER.userName eq game.value.player.userName}">
                        <c:set var="uid">${game.key}</c:set>
                        <c:set var="index" value="${fn:length(uid)}"/>
				            <tr><td>${fn:substring(uid, index-5, index)}</td><td>${game.value.player.userName}</td><td>${game.value.opponent.userName}</td><td>${game.value.currentStatus}</td>
						          <td><input type="checkbox" name="gameUUID" value="${game.key}"></td>
						    </tr>		
                        </c:if>
				    </c:forEach>
                </tbody>
            </table>
            </div>
        </div>
        <div class="col-xs-12 col-md-5">
            <div id="other-games-table" class="games-table">
            <table class="table table-bordered table-striped">
                <thead>
				<tr>
					<th>Game</th>
					<th>Player</th>
                    <th>Opponent</th>
					<th>Status</th>
					<th></th>
				</tr>
				</thead>
                <tbody>
                    <c:forEach var="game" items='${GAMEMAP}'>
                        <c:if test="${PLAYER.userName ne game.value.player.userName}">
                         	<c:set var="uid">${game.key}</c:set>
                        	<c:set var="index" value="${fn:length(uid)}"/>
				            <tr><td>${fn:substring(uid, index-5, index)}</td><td>${game.value.player.userName}</td><td>${game.value.opponent.userName}</td><td>${game.value.currentStatus}</td>
						          <td><input type="checkbox" name="gameUUID" value="${game.key}"></td>
						    </tr>		
                        </c:if>
				    </c:forEach>
                </tbody>
            </table>
            </div>
        </div>
    </div><!-- row#1 -->
    
        </div> <!-- container -->
</form>
<footer>
<tags:PlayerList csrfName="${_csrf.headerName}" csrfToken="${_csrf.token}"/>
</footer>
<script>
    $(document).ready(function(){
        var open = false;
        
        $("#userToggle").click(function () {
            if(open) {
                $("#sidebar-user").css("right","-150px");        
            } else {
                $("#sidebar-user").css("right","0");
                $('#navbarCollapse').removeClass('in');
            }
            open = !open;
        });
        
        $("#sidebar-user").click(function(){
            $("#sidebar-user").css("right","-150px");
            open = false;
        });
    });
</script>
<div id="sidebar-user" class="player-list">
    
</div>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
</body>
</html>