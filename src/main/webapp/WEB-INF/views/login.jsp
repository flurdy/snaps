<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="centreCargo">
	
	
	<form action="j_spring_security_check" method="post">
		<table>
			<tr>
				<th>username</th>
				<td><input type="text" name="j_username"/></td>
			</tr>
			<tr>
				<th>password</th>
				<td><input type="password" name="j_password"/></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="login"/></td>
			</tr>
		</table>
	</form>

    <c:if test="${!empty param.login_error}">
    <div>
         <h4>login failed</h4>
        <p>please try again?</p>

        <a href="${pageContext.request.contextPath}/password/reset">
            reset password
         </a>

    </div>
    </c:if>

</div>






