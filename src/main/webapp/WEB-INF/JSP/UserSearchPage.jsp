<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Chess Application Home Page</title>


<%@ include file="/WEB-INF/JSP/Csrf.jspf" %>
    
<%@ include file="/BootStrapHeader.jsp" %>

<script src="${pageContext.request.contextPath}/js/sidebar.js"></script>
<script src="${pageContext.request.contextPath}/jsfull/ajaxCSRF.js"></script>
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

    #search-box th { 
        font-size: 1.5em;
        font-weight: bold;
        font-family: "Orbitron";
    }
    
    #search-box tbody td {
        font-family: "Raleway";
    }
    
    .search-bar {
        font-size: 2em;
        margin-left: 0;
    }
    
    .search-bar input{
        width:100%;
        padding-left: 0;
    }
    
    #search-bar{
        padding-left: 0;
    }
    
    
    
    .search-bar button {
       /* padding:5px 19px 8px 5px;*/
    }
    
    .block {
        display:block;
        margin-bottom: 5px;
    }
    /* 
     * side menu 
     * to be removed
     */
    .side-menu{
        font-size:1.8em;
    }
    
    .side-menu button{
        padding: 0;
        background-color: #2886d5;
        border: none;
    }
</style>
<script>
$(document).ready(function () {
    
    function requestGame(userName) {
        "use strict";
        var formData = "userToPlay=" + userName;
        console.log("request game with " + userName);
        $.post('./requestGame', formData, function (data) {
            var $button = $(".play-button");
            if(data === true) {
                $button.removeClass("btn-primary");
                $button.addClass("btn-success");
                $button.html("request sent");
            } else {
                $button = $(".play-button");
                $button.removeClass("btn-primary");
                $button.addClass("btn-danger");
                $button.html("request failed");
            }
            $button.attr("disabled", true);
            $button.parents("tr").unbind("click");
        });
    }
    
    function addTableRowListener() {
        "use strict";
        $("#search-results tbody tr").click(function () {
            $(".play-button").remove();
            var td = $(this).find(".buttonHolder"),
                userName = $(this).find(".userName").text();
            td.append('<button class="play-button btn btn-primary">Request Game</button>');
            
            $(".play-button").click(function () {
                event.preventDefault();
                requestGame(userName);
            });
        });
    }
    
    function searchForUserName() {
        "use strict";
        var inputUserName = $('#userName'),
            userName = inputUserName.val(),
            formData = "searchTerm=" + userName;
        
        console.log("formData=" + formData);

        $.post('./searchForUsers', formData, function (data) {
           
            var users = $.parseJSON(data),
                i,
                userLength,
                $tbody = $("#search-box tbody"),
                tableResult = "";
            if (Array.isArray(users)) {
                if(users.length === 0) {
              		$tbody.html("<tr><td><h2>Search returned no results</h2></td></tr>");
                } else {
            		for (i = 0, userLength = users.length; i < userLength; i += 1) {
                    	tableResult += "<tr><td class='userName'>" + users[i].userName + "</td><td class=\"buttonHolder\"><span class=\"block\">" + users[i].fullName + "</span></td></tr>";
            		}
            		$tbody.html(tableResult);
                    addTableRowListener();
                }
            }
        });
    }
    
    $("#search-btn").click(function(event){
        searchForUserName();
    });
    
    $("#userName").keypress( function( event ) {
        if ( event.which == 13 ) {
            event.preventDefault();
            searchForUserName();
        }
    });
    
});
</script>
</head>
<body>
    <tags:BottomMenu>
        <li>
        	<tags:NavMenuButton id="home-btn" url="./chessapplication"><span class="glyphicon glyphicon-home"></span> Home</tags:NavMenuButton>
        </li>
    </tags:BottomMenu>
    
<div id="box" class="container-fluid ">
    <div class="row ">
        <tags:SideMenu>
            <li><a href="./chessapplication"><button id="home-btn">Home</button></a></li>
        </tags:SideMenu>
            
    <div id="search-box" class="panel panel-default col-md-10 col-xs-12">
        <div id="search-bar" class="search-bar panel-heading container-fluid">
            <div class="col-xs-8 col-md-10 ">
                <input id="userName" type="text" placeholder="Search for Users"/>
            </div>
            <div class="col-xs-2">
            <button id="search-btn" type="submit" class="btn btn-primary"><span class="glyphicon glyphicon-search"> Search</span></button>
            </div>
        </div>
        <div id="search-results" class="panel-body">
        <table class="table table-striped">
        <thead>
            <tr><th>Username</th><th>FullName</th></tr>
        </thead>
        <tbody>
        </tbody>
        </table>
        </div>
    </div>
    </div>
</div>
<%@ include file="/BootStrapFooter.jsp" %>
</body>
</html>
    