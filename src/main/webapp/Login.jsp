<%@page contentType="text/html; charset=ISO-8859-1" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
<title>AMCChessGame login</title>
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
 <script src="js/User.js"></script>
<style>
@import url(css/General.css);  

body {
    background-image: url(img/1700128.jpg);
    background-size: cover;
    background-repeat: repeat-y;
    overflow:auto;
}

#main{
    height:100%;
}
    
 
.padding {
    height: 20px;
}
    
.box {
    background: -webkit-linear-gradient(left, rgb(125, 165, 203), rgb(13, 116, 212), rgb(125, 165, 203));
    background: -moz-linear-gradient(left, rgb(125, 165, 203), rgb(13, 116, 212), rgb(125, 165, 203));
    box-shadow: black 5px 5px 10px;
}
    
#login-box {
    background-color: #428aca;
    border-radius: 22px;
    margin-top: 100px;
    padding-left:50px;
    padding-right:50px;
    //display: none;
}

#signup-box {
    background-color: #428aca;
    border-radius: 22px;
    margin-top: 100px;
    padding-left:50px;
    padding-right:50px;
    display: none;
}    

#login-icon {
    margin-top: 35px;
    margin-bottom: 10px;
    display: block;
    width: 150px;
    height: 150px;
    margin-left: auto;
    margin-right: auto;
}

#login-title {
    text-align: center;
    font-family: Raleway;
    font-size: x-large;
    margin-bottom: 30px;
}
#login-title strong {
    line-height: 1.3em;
    font-family:"Orbitron";
    font-size: 2em;
}

#login-fail {
    display: block;
    font-size: 2em;
    margin-top: -38px;
    margin-bottom: 8px
}
    
#signup-icon {
    margin-top: 35px;
    margin-bottom: 20px;
    margin-left: -50px;
    width: 120px;
    height: 120px;
}
    
#signup-title {
    text-align: center;
    font-family: Raleway;
    font-size: x-large;
    margin-bottom: 10px;
    margin-top: 70px;
 
}
#signup-title strong {
    line-height: 1.3em;
    font-family:"Orbitron";
    font-size: 1.0em;
}
    
.inputtext {
    height: 40px;
    margin-bottom: 20px;
    background-color: #285478;
    color: white;
    border-style: none;
    border-radius: 20px;
    box-shadow: 0px 0px 2px 2px #407199 inset;
    text-indent: 30px;
}

.signup-inputtext {
    margin-bottom: 30px;
}

.submit-btn {
    margin-bottom: 20px;
    background-color: #0b5494;
}

.filler {
    height: 20px;
}
    
#signup-msg {
    font-size: 1.3em;    
    display: block;
    color: white;
    text-align: center;
    padding-bottom: 20px;
    text-decoration: none;
}

@media (max-height:650px) and (max-width:450px) {
	#login-box {
		margin-top: -45px;
	}
	
	#signup-box {
		margin-top: 0px;
	}
    
}

@media (max-width:450px) {
	#login-box {
		margin-top: 0px;
	}
	
	#signup-box {
		margin-top: 0px;
	}
    
}

@media (max-height:650px) {
	#login-box {
		margin-top: -0px;
	}
	
	#signup-box {
		margin-top: 0px;
	}
    
}

</style>
<script>
$(document).ready(function () {
    /*
     * add CSRF token to AJAX requests
     */
    $(function () {
        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function(e, xhr, options) {
	       xhr.setRequestHeader(header, token);
        });
    });
    
    $("#signup-msg").click(function () {
        $("#login-box").css("display", "none");
        $("#signup-box").css("display", "block");
    });
    
    if("${param.ERRORS}") {
        $("#signup-msg").click();
    }
});
</script>
</head>
<body>
<div id="main" class="container-fluid">
><!-- Top row -->
    <div class="row">
        <form method="post" action="./login">
        <div id="login-box" class="col-sm-offset-3 col-sm-6 col-md-offset-4 col-md-4 box">
            <img id="login-icon" alt="knight"  src="./img/Knight.svg"/>
            <p id="login-title">Adrian McLaughlin's <strong>Chess Game</strong></p>
            <c:if test="${param.error != null}">
            	<span id="login-fail" class="label label-danger">Login failed</span>
            </c:if>
            <input class="inputtext form-control" type="text" name="username" required="required" placeholder="Username"/>
            <input class="inputtext form-control" type="password" name="password" required="required" placeholder="Password"/>
            <input class="btn btn-lg btn-block btn-primary submit-btn" type="submit" value="Play Game"/>
            <a id="signup-msg">Not a Member? Sign up now <span class="glyphicon glyphicon-chevron-right"></span></a>
        </div>
            <input type="hidden" name="${_csrf.parameterName}"	value="${_csrf.token}"/>
        </form>
        <form id="userForm" method="post" action="signup">
        <div id="signup-box" class="col-sm-offset-3 col-sm-6 col-md-offset-4 col-md-4 box">
             <c:if test="${param.ERRORS != null}">
            	   <span id="login-fail" class="label label-danger"><c:out value="${param.ERRORS}"></c:out></span>
            </c:if>
            <div id="title">
                <div class="col-xs-2"><img id="signup-icon" alt="knight" src="./img/Knight.svg"/></div>
                <p class="col-xs-10" id="signup-title">Adrian McLaughlin's <br><strong>Chess Game</strong></p>
            </div>
            <input class="inputtext signup-inputtext form-control" type="text" name="name" required="required" placeholder="Name"/>
            <input class="inputtext signup-inputtext form-control" type="text" name="userName" id="userName" required="required" placeholder="Username"/>
            <input class="inputtext signup-inputtext form-control" type="password" name="password" id="passwordOne" required="required" placeholder="Password"/>
            <input class="inputtext signup-inputtext form-control" type="password" required="required" id="passwordTwo" placeholder="Confirm password"/>
            <input class="btn btn-lg btn-block btn-primary submit-btn" type="submit" value="Create Player"/>
            <div class="filler"></div>
        </div>
            <input type="hidden" name="${_csrf.parameterName}"	value="${_csrf.token}"/>
        </form>
    </div>
</div> <!-- Container -->
    
<!-- Include all compiled plugins (below), or include individual files as needed -->
 <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
</body>
</html>