<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<style>

</style>

<div class="centreCargo">


    <h3>you were not allowed to do that!</h3>

    <p>
        it may be something we did,<br/>but mostly likely something you did by accident....
    </p>

    <hr width="70%"/>

    <c:if test="${not empty exception}">

        <h4>logical error</h4>

        <p>
            <b>${exception.errorCode.description}</b>
        </p>

    <hr width="70%"/>

    </c:if>

    <p>
        does that indicate something you did?
        if so, you can go back and correct it,<br/> or <a href="index.html">return to the front page</a>?
    </p>

    <hr width="70%" />

    <p>
        if not, and if it occurs again, please <a href="http://flurdy.com/contact/">let us know</a>?
    </p>


</div>






