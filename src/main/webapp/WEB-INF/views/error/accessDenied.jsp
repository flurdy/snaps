<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<style>

</style>

<div class="centreCargo">


    <h3>access has been denied!</h3>

    <p>	you probably should not have tried to do that. </p>

    <p>	please say sorry?</p>

    <hr width="70%"/>

    <c:if test="${not empty exception}">

        <p>
            <b>${exception.accessError}</b> 
        </p>

    <hr width="70%"/>

    </c:if>

    <p>
        <a href="${pageContext.request.contextPath}/">return to the front page</a>.
    </p>

    <p>
        if you are sure you should have access to this page,<br/>
        then <a href="http://flurdy.com/contact/index.html">contact	us</a>.
    </p>



</div>






