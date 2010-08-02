<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="centreCargo">

	<form action="${pageContext.request.contextPath}/admin/holiday/${holidayGroup.groupId}" method="post">
		<input type="hidden" name="_method" value="PUT"/>
		<input type="hidden" name="groupId" value="${holidayGroup.groupId}"/>
		<table>
			<tr>
				<th>group name</th>
				<td><input type="text" name="groupName" value="${holidayGroup.groupName}"/></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="update holiday details"/></td>
			</tr>
		</table>
	</form>

    <br/>
    <hr/>
    <br/>

	<form action="${pageContext.request.contextPath}/admin/holiday/${holidayGroup.groupId}" method="post">
		<input type="hidden" name="_method" value="DELETE"/>
		<input type="hidden" name="groupId" value="${holidayGroup.groupId}"/>
		<table>
			<tr>
				<td colspan="2"><input type="submit" value="delete holiday"/></td>
			</tr>
		</table>
	</form>

    <br/>
    <hr/>
    <br/>

    <a href="${pageContext.request.contextPath}/admin/holiday">return to list</a>

</div>