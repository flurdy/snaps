<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--
<style> #starboard { display: table-cell; } </style>
<style>
    #starboard ul {   }
    #starboard ul li { border: 2px inset silver; margin: 0.4em;  }
</style>

<ul class="vertical">

        <c:forEach var="photoAlbum" items="${holidayGroup.photoAlbums}">
            <c:choose>
                <c:when test="${photoAlbum.sharingProvider eq 'flickr' }">
                <li>
                    <div>
                             ${photoAlbum.url}
                    </div>
                </li>
                </c:when>
                <c:otherwise>
                <li>
                    <div>
                             ${photoAlbum.url}
                    </div>
                </li>
                </c:otherwise>
            </c:choose>
        </c:forEach>

<c:forEach begin="1" end="5">
    <li>
        <div>
              ibiza holiday
        </div>
        <div>
              by john
        </div>
        <div>
              <img src="${pageContext.request.contextPath}/blank.gif" alt="" width="100" height="50"/>
              <img src="${pageContext.request.contextPath}/blank.gif" alt="" width="100" height="50"/>
              <img src="${pageContext.request.contextPath}/blank.gif" alt="" width="100" height="50"/>
        </div>
    </li>
</c:forEach>


</ul>

--%>
