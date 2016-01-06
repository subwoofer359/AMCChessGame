<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Chess Application Home Page</title>


<%@ include file="/WEB-INF/JSP/Csrf.jspf" %>
    
<%@ include file="/BootStrapHeader.jsp" %>

 <script src="//cdn.jsdelivr.net/sockjs/0.3.4/sockjs.min.js"></script>
 <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.js"></script>
 <script src="${pageContext.request.contextPath}/js/sidebar.js"></script>
 <script src="${pageContext.request.contextPath}/js/selectTableRow.js"></script>
 <script src="${pageContext.request.contextPath}/jsfull/ajaxCSRF.js"></script>
 <script src="${pageContext.request.contextPath}/jsfull/ChessApplicationPage.js"></script>
<style>
    @import url(${pageContext.request.contextPath}/css/General.css);
    @import url(${pageContext.request.contextPath}/css/selectTableRow.css);
    @import url(${pageContext.request.contextPath}/css/PlayerName.css);
    @import url(${pageContext.request.contextPath}/css/PlayerList.css);
    
    body {
        background-image: url(${pageContext.request.contextPath}/img/1700128.jpg);
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
    
    span[class$="glyphicon-time"] {
        color:#ffcc00;
        display: block;
        text-align: center;
    }
    
    span[class$="glyphicon-play-circle"] {
        color:green;
        display: block;
        text-align: center;
    }
    
    span[class$="glyphicon-remove"] {
        color:red;
        display: block;
        text-align: center;
    }
    
    
    #createNewChessGamePanel {
        display: none; 
    }
    
    #createNewChessGamePanel panel-body {
        padding-left: 30px;
    }
    
    @media(min-width:992px) {
        #createNewChessGamePanel {
            position: absolute;
            top: 220px;
            left: 154px;
            font-family: Raleway;
            font-size: 1.8em;
        }
    }
    
    @media(max-width:992px) {
        #createNewChessGamePanel {
            position: absolute;
            top: 0px;
            left: 0px;
            font-family: Raleway;
            font-size: 1.8em;
        }
        
        #createNewChessGamePanel .panel {
            height: 100vh;
        }
        
        
    }
</style>
<script>
    var playerName = '<c:out value="${PLAYER.name}"/>',
    	userName = '<c:out value="${PLAYER.userName}"/>';
    
    $(document).ready(function () {
        $(".create-game").click(function () {
            $("#createNewChessGamePanel").css("display","block");
        });
    });
</script>
</head>
<body>
<form id="side-menu-form" method="post">
    <input type="hidden" name="${_csrf.parameterName}"	value="${_csrf.token}"/>

    <tags:BottomMenu>
    	<tags:NavMenuButton id="userToggle"><span class="glyphicon glyphicon-user"></span> User List</tags:NavMenuButton>
        <tags:NavMenuButton aclass="create-game"><span class="glyphicon glyphicon-plus"></span> Create Game</tags:NavMenuButton>   
        <tags:FormMenuButton aclass="join-button"><span class="glyphicon glyphicon-play"></span> Join Game</tags:FormMenuButton>
        <tags:NavMenuButton url="./userSearchPage"><span class="glyphicon glyphicon-search"></span> User Search</tags:NavMenuButton>
       	<tags:NavMenuButton url="./logout"><span class="glyphicon glyphicon-log-out"></span> Log out</tags:NavMenuButton> 
    </tags:BottomMenu>

<div class="container-fluid full-height">
   
    <div class="row full-height">
        <tags:SideMenu>
            <li><button class="create-game" type="button">Create Game</button></li>
            <li><button class="join-button" type="submit">Join Game</button></li>
            <%-- <li><a class="description" href="#">Delete Game</a></li> --%>
            <li><a href="${pageContext.request.contextPath}/app/chessgame/userSearchPage">User Search</a></li>
            <li><a href="${pageContext.request.contextPath}/app/chessgame/logout">Log out</a></li>
        </tags:SideMenu>
        <div class="player-name col-sm-10 col-xs-12"><span class="title">Player:</span><span class="name"><c:out value="${PLAYER.name}"/></span></div>
        
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
<%@ include file="/BootStrapFooter.jsp" %>
<%@ include file="/WEB-INF/JSP/CreateNewChessGame.jspf" %>
</body>
</html>