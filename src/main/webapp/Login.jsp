<!DOCTYPE html>
<html>

<head>
<title>AMCChessGame login</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no">

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
<style>
@import url(css/General.css);  

body {
    background-image: url(img/1700128.jpg);
    background-size: cover;
    background-repeat: no-repeat;
    
}

#main{
    height:100%;
}
    
 
.padding {
    height: 20px;
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

</style>
<script>
$(document).ready(function () {
    $("#signup-msg").click(function () {
        $("#login-box").css("display", "none");
        $("#signup-box").css("display", "block");
    });
});
</script>
</head>
<body>
<div id="main" class="container-fluid">
><!-- Top row -->
    <div class="row">
        <form method="post" action="./login">
        <div id="login-box" class="col-sm-offset-4 col-sm-5 col-md-offset-4 col-md-4">
            <img id="login-icon" alt="knight"  src="./img/Knight.svg"/>
            <p id="login-title">Adrian McLaughlin's <strong>Chess Game</strong></p>
            <span id="login-fail" class="label label-danger">Login failed</span>
            <input class="inputtext form-control" type="text" name="username" required="required" placeholder="Username"/>
            <input class="inputtext form-control" type="password" name="password" required="required" placeholder="Password"/>
            <input class="btn btn-lg btn-block btn-primary submit-btn" type="submit" value="Play Game"/>
            <a id="signup-msg">Not a Member? Sign up now</a>
        </div>
        </form>
        <div id="signup-box" class="col-sm-offset-4 col-sm-5 col-md-offset-4 col-md-4">
            <div id="title">
                <div class="col-xs-2"><img id="signup-icon" alt="knight" src="./img/Knight.svg"/></div>
                <p class="col-xs-10" id="signup-title">Adrian McLaughlin's <br><strong>Chess Game</strong></p>
            </div>
            <input class="inputtext signup-inputtext form-control" type="text" name="name" required="required" placeholder="Name"/>
            <input class="inputtext signup-inputtext form-control" type="text" name="userName" required="required" placeholder="Username"/>
            <input class="inputtext signup-inputtext form-control" type="password" name="password" required="required" placeholder="Password"/>
            <input class="inputtext signup-inputtext form-control" type="password" required="required" placeholder="Confirm password"/>
            <input class="btn btn-lg btn-block btn-primary submit-btn" type="submit" value="create Player"/>
            <div class="filler"></div>
        </div>
        <form>
        
        </form>
    </div>
</div> <!-- Container -->
    
<!-- Include all compiled plugins (below), or include individual files as needed -->
 <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
</body>
</html>