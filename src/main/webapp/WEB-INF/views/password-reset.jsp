<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="centreCargo">
	
	
	<form action="${pageContext.request.contextPath}/password/reset" method="post">
		<table>
			<tr>
				<th>username or email address</th>
			</tr>
			<tr>
				<td><input type="text" name="usernameOrEmail"/></td>
			</tr>
			<tr>
				<td colspan="1"><input type="submit" value="reset"/></td>
			</tr>
		</table>
	</form>


</div>






