<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<div class="centreCargo">

	<ul>
		<c:forEach var="holidayGroup" items="${holidayGroups}">
		<li><a href="${pageContext.request.contextPath}/holiday/${holidayGroup.groupId}">${holidayGroup.groupName}</a></li>
		</c:forEach>
	</ul>
</div>

