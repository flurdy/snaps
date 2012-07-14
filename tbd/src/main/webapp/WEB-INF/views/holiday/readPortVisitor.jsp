<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:if test="${not isPendingTraveller}">
    <style> #port { display: table-cell; } </style>

    <ul class="vertical">

        <!--
        <li><button>edit</button></li>
        -->


        <form action="${holidayGroup.groupId}/member/" method="post">
            <li><button type="submit">I was on this holiday!</button></li>
        </form>



    </ul>
</c:if>