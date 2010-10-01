<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
            <style>
<c:if test="${pageContext.request.serverName == 'localhost'
    || fn:startsWith(pageContext.request.serverName,'192.168')
    || fn:startsWith(pageContext.request.serverName,'djalma') }">
                /* incognito/paranoia.. */
				/* body { background-color: #ffffff; } */
				/* #bow { display: none; }
				/* #jib, #cargo h2  { display: none; } */
				/* #bow, #bow a, #jib, #cargo h2  { color: #e0e0e0; }  */
</c:if>
            </style>