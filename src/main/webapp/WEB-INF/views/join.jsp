<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="centreCargo">

	<form action="${pageContext.request.contextPath}/registration" method="post">
		<table>
			<tr>
				<th>username</th>
				<td><input type="text" name="username"/></td>
			</tr>
			<tr>
				<th>full name</th>
				<td><input type="text" name="fullname"/></td>
			</tr>
			<tr>
				<th>email address</th>
				<td><input type="text" name="email"/></td>
			</tr>
			<tr>
				<th>password</th>
				<td><input type="text" name="password"/></td>
			</tr>
			<tr>
				<th>confirm</th>
				<td><input type="text" name="confirmPassword" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="join"/></td>
			</tr>
		</table>
	</form>


	<br/>
	<br/>
	<br/>

	<p>
		Want to join the <a href="http://code.flurdy.grid/snaps">actual project</a>?
	</p>
</div>