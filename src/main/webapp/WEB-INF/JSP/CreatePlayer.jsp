<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/BootStrapHeader.jsp"%>
<title>Chess Application Page:Create Player</title>
<style>
.title {
	border-style: solid;
	margin: 5px 5px 60px 5px;
	text-align: center;
	border-radius: 33px;
	width: 450px;
	background-color: #428bca;
	color: white;
}
</style>
</head>
<body>
	<div class="title">
		<h1>Chess Application Page</h1>
		<h2>Create Player</h2>
	</div>

	<div class="container-fluid">
		<div class="row">
			<form class="form-horizontal" method="post" action="playerCreate">
				<div class="form-group">
					<label for="name" class="control-label col-xs-2">Player's
						Name</label>
					<div class="col-xs-4">
						<input id="name" name="name" class="form-control" type="text">
					</div>

					<div class="col-xs-2">
						<INPUT class="btn btn-primary " type="submit"
							value="create Player" />
					</div>
				</div>
			</form>
		</div>
	</div>
</body>
<%@ include file="/BootStrapFooter.jsp"%>
</html>