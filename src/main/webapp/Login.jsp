<%@page contentType="text/html; charset=ISO-8859-1" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>

<head>
<title>AMCChessGame login</title>
<%@ include file="/WEB-INF/JSP/Csrf.jspf" %>
    
<%@ include file="/BootStrapHeader.jsp" %>
<script src="${pageContext.request.contextPath}/js/ajaxCSRF.js"></script>
<script src="${pageContext.request.contextPath}/js/User.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/Login.css">
<script>
$(document).ready(function () {
    
    $("#signup-msg").click(function () {
        $("#login-box").css("display", "none");
        $("#signup-box").css("display", "block");
    });
    
    <%-- 
        Message box will fade out when touched
    --%>
    $(".messageLabel").click(function() {
        $(".messageLabel").fadeOut(3000);
    });
    
    if(${not empty errors and errors.hasErrors()}) {
        $("#signup-msg").click();
    }
<c:set var="inputFields" value="${fn:split('fullName,userName,password,emailAddress', ',')}"/>
    
<c:forEach var="inputField" items="${inputFields}">
<c:if test='${not empty errors and not empty errors.getFieldError(inputField)}'>
    var $field = $("#${inputField}");
    $field.css("background-color", "#dc4040");
    $field.after('<span class="signup-error">${errors.getFieldError(inputField).code}</span>');
</c:if>
</c:forEach>
});
</script>
</head>
<body>
<div id="main" class="container-fluid">
<!-- Top row -->
    <div class="row">
        <form method="post" action="./login">
        <div id="login-box" class="col-sm-offset-3 col-sm-6 col-md-offset-4 col-md-4 box">
            <img id="login-icon" alt="knight"  src="./img/Knight.svg"/>
            <p id="login-title">Adrian McLaughlin's <strong>Chess Game</strong></p>
            <input class="inputtext form-control" type="text" name="username" required="required" placeholder="Username"/>
            <input class="inputtext form-control" type="password" name="password" required="required" placeholder="Password"/>
            <input class="btn btn-lg btn-block btn-primary submit-btn" type="submit" value="Play Game"/>
            <a id="signup-msg">Not a Member? Sign up now <span class="glyphicon glyphicon-chevron-right"></span></a>
        </div>
            <input type="hidden" name="${_csrf.parameterName}"	value="${_csrf.token}"/>
        </form>
        <!-- Sign up box -->
        <form id="userForm" method="post" action="signup">
        <div id="signup-box" class="col-sm-offset-3 col-sm-6 col-md-offset-4 col-md-4 box">
            <div id="title">
                <div class="col-xs-3"><img id="signup-icon" alt="knight" src="./img/Knight.svg"/></div>
                <p class="col-xs-9" id="signup-title">Adrian McLaughlin's <br><strong>Chess Game</strong></p>
            </div>
            <input class="inputtext signup-inputtext form-control" type="text" name="fullName" id="fullName" required="required" placeholder="Name" pattern="\b[a-zA-Z .']{5,50}\b" title="Letters and spaces only, Name length greater than 5 and less than 50" <c:if test="${not empty userForm}">value="<c:out value="${userForm.fullName}"/>"</c:if>/>
            
            <input class="inputtext signup-inputtext form-control" type="text" name="userName" id="userName" required="required" pattern="[a-z][a-z0-9_]{4,50}" title="Start with a letter. No spaces or special characters. Length greater than 4 and less 50" placeholder="Username" <c:if test="${not empty userForm}">value="<c:out value="${userForm.userName}"/>"</c:if>/>       
            
        <input class="inputtext signup-inputtext form-control" type="password" name="password" id="password" required="required" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}" title="Must contain numbers, lowercase and uppercase letters and be greater than 6 and less then 20." placeholder="Password"/>
        
            <input class="inputtext signup-inputtext form-control" type="password" required="required" id="passwordTwo" placeholder="Confirm password"/>
        
        <input class="inputtext signup-inputtext form-control" type="email" name="emailAddress" id="emailAddress" required="required" placeholder="Email Address" pattern="\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,10}\b" title="format: name@company.domain" <c:if test="${not empty userForm}">value="<c:out value="${userForm.emailAddress}"/>"</c:if>/>
    
            <input class="btn btn-lg btn-block btn-primary submit-btn" type="submit" value="Create Player"/>
            <div class="filler"></div>
        </div>
            <input type="hidden" name="${_csrf.parameterName}"	value="${_csrf.token}"/>
        </form>
        <%-- Display message --%>
        <c:if test="${not empty MESSAGE}">
        	   <span id="messageBox" class="messageLabel label label-success"><c:out value="${MESSAGE}"></c:out></span>
        </c:if>
        <%-- Display Login error --%>
        <c:if test="${not empty param.MESSAGE}">
        	   <span id="loginFailed" class="messageLabel label label-danger"><c:out value="${param.MESSAGE}"></c:out></span>
        </c:if>
    </div>
   
</div> <!-- Container -->
    
<%@ include file="/BootStrapFooter.jsp" %>
</body>
</html>