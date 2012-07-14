<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<div class="centreCargo">

	<ul>
		<c:forEach var="holiday" items="${holidayGroups}">
		<li><a href="${pageContext.request.contextPath}/admin/holiday/${holiday.groupId}/edit">(${holiday.groupId}) ${holiday.groupName}</a></li>
		</c:forEach>
	</ul>
</div>

