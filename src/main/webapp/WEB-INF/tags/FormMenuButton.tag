<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ attribute name="id" type="java.lang.String" rtexprvalue="true" required="false" %>
<%@ attribute name="aclass" type="java.lang.String" rtexprvalue="true" required="false" %>
<%@ attribute name="formaction" type="java.lang.String" rtexprvalue="true" required="false" %>

<c:set var="classes" value="btn btn-default btn-block btn-lg"/>
<c:if test="${not empty aclass}">
	<c:set var="classes" value='${classes} ${aclass}'/>
</c:if>

 <li>
 	<button type="submit" <c:if test="${not empty id}">id=${id}</c:if>class="${classes}" <c:if test="${not empty formaction}">formaction="${formaction}"</c:if> required="true">
 		<jsp:doBody/>
 	</button>
 </li>
