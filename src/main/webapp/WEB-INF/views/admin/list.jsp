<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<div class="centreCargo">

	<ul>
		<c:forEach var="traveller" items="${travellers}">
		<li><a href="${pageContext.request.contextPath}/admin/traveller/${traveller.travellerId}/edit">(${traveller.securityDetail.username}) ${traveller.fullname}</a></li>
		</c:forEach>
	</ul>
</div>

