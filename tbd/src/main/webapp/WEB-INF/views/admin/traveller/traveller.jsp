<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="centreCargo">
	<form action="${pageContext.request.contextPath}/admin/traveller/security/${securityDetail.username}/password" method="post">
		<input type="hidden" name="_method" value="PUT"/>
		<table>
			<tr>
				<th>username</th>
				<td><input type="text" name="username" disabled="disabled" value="${securityDetail.username}"/></td>
			</tr>
			<tr>
				<th>password</th>
				<td><input type="password" name="password" value=""/></td>
			</tr>
			<tr>
				<th>confirm password</th>
				<td><input type="password" name="confirmPassword" value=""/></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="change password"/></td>
			</tr>
		</table>
	</form>

	<br/>

	<form action="${pageContext.request.contextPath}/admin/traveller/${traveller.travellerId}" method="post">
		<input type="hidden" name="_method" value="PUT"/>
		<input type="hidden" name="travellerId" value="${traveller.travellerId}"/>
		<table>
			<tr>
				<th>full name</th>
				<td><input type="text" name="fullname" value="${traveller.fullname}"/></td>
			</tr>
			<tr>
				<th>email address</th>
				<td><input type="text" name="email" value="${traveller.email}"/></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="update traveller details"/></td>
			</tr>
		</table>
	</form>

	<br/>

	<table>
		<tr>
			<th>enabled</th>
			<td>
				<spring:bind path="securityDetail.enabled">
					<input type="hidden" name="_${status.expression}">
					<input type="checkbox" disabled="disabled" name="${status.expression}" value="true"
						   <c:if test="${status.value}">checked="checked"</c:if>/>
				</spring:bind>
			</td>
		<form action="${pageContext.request.contextPath}/admin/traveller/security/${securityDetail.username}/enable" method="post">
			<input type="hidden" name="_method" value="PUT"/>
			<c:choose>
				<c:when test="${securityDetail.enabled}">
					<td colspan="2"><input type="submit" value="disable user"/></td>
					</c:when>
					<c:otherwise>
					<td colspan="2"><input type="submit" value="enable user"/></td>
					</c:otherwise>
				</c:choose>
		</form>
		</tr>
	</table>

	<br/>
	<br/>

	<table>
		<c:forEach var="authority" items="${securityDetail.authorities}">
			<form action="${pageContext.request.contextPath}/admin/traveller/security/${securityDetail.username}/authority/${authority}" method="post">
				<input type="hidden" name="_method" value="DELETE"/>
				<tr>
					<td>${authority}</td>
					<td><button type="submit">remove</button></td>
				</tr>
			</form>
		</c:forEach>
	</table>

	<br/>
	<br/>

	<table>
		<c:set var="hasAuthority" scope="request"  value="0" />
		<c:forEach var="authority" items="${securityDetail.authorities}">
			<c:if test="${authority == 'ROLE_USER'}">
				<c:set var="hasAuthority" scope="request"  value="1" />
			</c:if>
		</c:forEach>
		<c:if test="${hasAuthority == 0}">
			<form action="${pageContext.request.contextPath}/admin/traveller/security/${securityDetail.username}/authority" method="post">
				<input type="hidden" name="authorityRole" value="ROLE_USER"/>
				<tr>
					<td>ROLE_USER</td>
					<td><button type="submit">add</button></td>
				</tr>
			</form>
		</c:if>
		<c:set var="hasAuthority" scope="request"  value="0" />
		<c:forEach var="authority" items="${securityDetail.authorities}">
			<c:if test="${authority == 'ROLE_ADMIN'}">
				<c:set var="hasAuthority" scope="request"  value="1" />
			</c:if>
		</c:forEach>
		<c:if test="${hasAuthority == 0}">
			<form action="${pageContext.request.contextPath}/admin/traveller/security/${securityDetail.username}/authority" method="post">
				<input type="hidden" name="authorityRole" value="ROLE_ADMIN"/>
				<tr>
					<td>ROLE_ADMIN</td>
					<td><button type="submit">add</button></td>
				</tr>
			</form>
		</c:if>
		<c:set var="hasAuthority" scope="request"  value="0" />
		<c:forEach var="authority" items="${securityDetail.authorities}">
			<c:if test="${authority == 'ROLE_SUPER'}">
				<c:set var="hasAuthority" scope="request"  value="1" />
			</c:if>
		</c:forEach>
		<c:if test="${hasAuthority == 0}">
			<form action="${pageContext.request.contextPath}/admin/traveller/security/${securityDetail.username}/authority" method="post">
				<input type="hidden" name="authorityRole" value="ROLE_SUPER"/>
				<tr>
					<td>ROLE_SUPER</td>
					<td><button type="submit">add</button></td>
				</tr>
			</form>
		</c:if>
		<c:set var="hasAuthority" scope="request"  value="0" />
		<c:forEach var="authority" items="${securityDetail.authorities}">
			<c:if test="${authority == 'ROLE_MONITOR'}">
				<c:set var="hasAuthority" scope="request"  value="1" />
			</c:if>
		</c:forEach>
		<c:if test="${hasAuthority == 0}">
			<form action="${pageContext.request.contextPath}/admin/traveller/security/${securityDetail.username}/authority" method="post">
				<input type="hidden" name="authorityRole" value="ROLE_MONITOR"/>
				<tr>
					<td>ROLE_MONITOR</td>
					<td><button type="submit">add</button></td>
				</tr>
			</form>
		</c:if>
	</table>

	<br/>
	<br/>

	<form action="${pageContext.request.contextPath}/admin/traveller/${traveller.travellerId}" method="post" onsubmit="return confirm('Sure you want to delete this traveller')"">
		<input type="hidden" name="_method" value="DELETE"/>
		<button method="submit">delete traveller</button>
	</form>

    <br/>
    <br/>

    <a href="${pageContext.request.contextPath}/admin/traveller">return to list</a>

</div>