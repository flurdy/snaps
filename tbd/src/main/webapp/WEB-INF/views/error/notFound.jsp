<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<style>

</style>

<div class="centreCargo">

    <h3>what you are looking for is no longer here</h3>

    <p>it has wondered on to better pastures...</p>

    <hr width="70%"/>

    <c:if test="${not empty exception}">

        <p>
            <b>${exception.resourceNotFound.description}</b>
        </p>

        <hr width="70%"/>

    </c:if>


    <p><a href="${pageContext.request.contextPath}/">move along</a> now, there is nothing of interest here...</p>

    <!--
    <p>
        if you believe the page should be here, <br/>
        then <a href="http://flurdy.com/contact/index.html">contact us</a>.
    </p>
    -->
    

</div>






