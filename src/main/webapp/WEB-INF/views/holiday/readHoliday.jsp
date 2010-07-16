<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="readHoliday" class="centreCargo">
	
	<h3>${holidayGroup.groupName}</h3>


    <div id="photoAlbums">
        <div>
            <ul>
                <c:forEach items="${holidayGroup.photoAlbums}" var="photoAlbum">
                <li class="listItem">
                    ${photoAlbum.owner.fullname}'s
                    <a href="${photoAlbum.url}">photo album</a>
                    at ${photoAlbum.sharingProvider}
                </li>
                </c:forEach>
            </ul>
        </div>
    </div>

</div>

