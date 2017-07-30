<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="csrfName" required="false" rtexprvalue="true" type="java.lang.String" %>
<%@ attribute name="csrfToken" required="false" rtexprvalue="true" type="java.lang.String" %>
<script>
$(document).ready(function(){
	var headerName = "${csrfName}",
	token = "${csrfToken}",
	stompObject = {};

	stompObject.headers = {};
	stompObject.headers[headerName] = token;
	stompObject.URL = "http://" + location.hostname + ":" + location.port +
                "${pageContext.request.contextPath}" +
                "/app/chessgame/chessgame";
	var stompClient,
		socket;
	socket = new SockJS(stompObject.URL);
	stompClient = Stomp.over(socket);

	stompClient.connect(stompObject.headers, function () {
    
    	function onlinePlayerList(message) {
        	var $playerList = $(".player-list"),
    		users = $.parseJSON(message.body),
    		html = "<ul>",
    		i,
    		len;
    		for(i = 0, len = users.length; i< len; i+=1) {
	        	html += "<li>"+users[i].username+"</li>";
    		}
    		html += "</ul>";
    		$playerList.html(html);
		}
    	stompClient.subscribe("/topic/updates/onlineplayerlist", onlinePlayerList);
    	stompClient.subscribe("/user/queue/updates/onlineplayerlist", onlinePlayerList);
    
	    stompClient.send("/app/get/onlinePlayerList", {priority : 9}, "get onlinePlayerList");
	});
});
</script>
<div id="player-list" class="player-list"></div>