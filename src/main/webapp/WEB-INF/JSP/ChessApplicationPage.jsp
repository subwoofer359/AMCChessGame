<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/BootStrapHeader.jsp" %>
<style>
.title{
    border-style: solid;
    margin: 5px 5px 60px 5px;
    text-align: center;
    border-radius: 33px;
    width: 450px;
    background-color: #428bca;
    color: white;
}
</style>
<title>Chess Application Page</title>
</head>
<body>
	<div class="title">
		<h1>Chess Application Page</h1>
	</div>

	<div class="container-fluid">
		<div class="row">
			<div class="col-xs-2">
				<button type="button" class="btn btn-primary">Create Game</button>
			</div>
			<div class="col-xs-offset-6 col-xs-4">
				<div class="panel panel-primary">
					<div class="panel-heading">Available Chess Games</div>
					<div class="panel-body">
						<table class="table table-bordered">
							<thead>
								<tr>
									<th>Game</th>
									<th>Player</th>
									<th>Status</th>
								</tr>
							</thead>
							<tbody>
							<c:forEach var="game" items='${GAMEMAP}'>
								<tr><td>${game.key}</td><td>${game.value.currentPlayer.name}</td><td></td></tr>
							</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<%@ include file="/BootStrapFooter.jsp" %>
</html>