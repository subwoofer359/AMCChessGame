<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
            $button.attr("disabled", "disabled");
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
    
    $("#search-btn").click(function(){
        searchForUserName();
    });
    
});
</script>
</head>
<body>
<div id="box" class="container-fluid ">
    <div class="row ">
    <div id="search-box" class="panel panel-default">
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
</body>
</html>
    