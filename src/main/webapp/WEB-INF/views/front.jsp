<%@page contentType="text/html" pageEncoding="UTF-8"%>

<style>
	#jib { display: none; }
</style>

<div class="centreCargo">

    <h2>holidays</h2>

    <form action="holiday/" method="get">
            <button type="submit">list all holidays</button>
    </form>

	<br/>

	<hr width="80%"/>

    <br/>

	<form action="holiday/" method="get">
			<input type="text" name="groupName"/>
			<button type="submit">find holiday</button>
	</form>

	<br/>

	<hr width="80%"/>

	<br/>

	<form action="holiday/new.html">
		<button type="submit">create new holiday</button>
	</form>




</div>






