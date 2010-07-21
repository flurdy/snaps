<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:if test="${not empty pendingTravellers}">
<style>
    #starboard { display: table-cell; min-width: 9em; }
    #starboard h4 { padding: 0.2em 1em;  }
    #starboard p { padding: 0.2em 1em;  }
    #starboard ul {   }
    #starboard ul li { border: 2px inset silver; margin: 0.4em;  }
    #starboard ul li .joinRequest { width: 10em; overflow: hidden; padding: 0.1em 0.3em 0.1em 0.3em;  }
    #starboard ul li .joinRequest .fullname {  padding: 0 }
    #starboard ul li .joinRequest .approveButton { text-align: right; clear: both; }
</style>

<h4>pending travellers</h4>

<p>where these on your holiday?</p>

<ul class="vertical">
    <c:forEach items="${pendingTravellers}" var="pendingTraveller">
        <form action="${pageContext.request.contextPath}/holiday/${holidayGroup.groupId}/member/${pendingTraveller.travellerId}/approve" method="post">
            <li>
                <div class="joinRequest">
                    <span title="${pendingTraveller.fullname} (${pendingTraveller.securityDetail.username} | ${pendingTraveller.email})"
                          class="fullname">
                        ${pendingTraveller.fullname}
                   </span>
                    <div class="approveButton"><button type="submit">approve</button></div>
                </div>
            </li>
        </form>
    </c:forEach>
</ul>
</c:if>
