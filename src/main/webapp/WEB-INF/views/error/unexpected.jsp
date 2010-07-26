<%@page contentType="text/html" pageEncoding="UTF-8"%>

<style>

</style>

<div class="centreCargo">


    <h4>the server encountered an error!</h4>

    <p>that is not good at all...</p>

    <p>
        it most likely is something odd you did,<br/> we did or that bloke over there did.
    </p>

    <br />
    <hr width="70%" />
    <br />

    <h3>technical exception</h3>

    <p>
        this error is quite severe, however is often temporary.<br />
        try again in a little while?<br />
    </p>

    <p>
        however this was an error not expected by the application,<br />
        and should be <a href="http://flurdy.com/contact/index.html">reported</a> to the developers,<br/>
        especially if it reoccurs?
    </p>

    <hr width="70%" />


    <c:if test="${not empty exception}">

        <!--
        
        <p>
            <b>${exception}</b>
        </p>

        -->

    </c:if>


    <p><a href="index.html">return to the front page</a>.</p>

    

</div>






