<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="centreCargo">

	<form action="${pageContext.request.contextPath}/holiday" method="post">

		<h4>
			what do you want the holiday group to be called?
			
		</h4>

		<input type="text" name="groupName" />

		<br/>
			
		<input type="submit" value="create holiday group"/>
			
	</form>

</div>




