<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="centreCargo">

	<form action="${pageContext.request.contextPath}/admin/holiday/${holidayGroup.groupId}" method="post">
		<input type="hidden" name="_method" value="PUT"/>
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
		<table>
			<tr>
				<td colspan="2"><input type="submit" value="delete holiday"/></td>
			</tr>
		</table>
	</form>

    <br/>
    <hr/>
    <br/>

    <ul>
        <c:forEach items="${holidayGroup.photoAlbums}" var="photoAlbum">
        <li>
            <form action="${pageContext.request.contextPath}/admin/holiday/${holidayGroup.groupId}/album/${photoAlbum.albumId}" method="post">
		        <input type="hidden" name="_method" value="DELETE"/>
                ${photoAlbum.owner.fullname}'s
                <a href="${photoAlbum.url}">photo album</a>
                at ${photoAlbum.sharingProvider}
		        <input type="submit" value="remove photo album"/>
	        </form>
        </li>
        </c:forEach>
    </ul>

    <br/>
    <hr/>
    <br/>



    <a href="${pageContext.request.contextPath}/admin/holiday">return to list</a>

</div>