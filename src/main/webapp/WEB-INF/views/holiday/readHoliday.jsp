<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
    #cargo #readHoliday #photoAlbums .thumbnail {
        border: 2px inset silver;
        padding: 3px;
        vertical-align: middle;
        width: 40px;
        height: 40px;
        margin: 1px 3px;
    }
    #cargo #readHoliday #photoAlbums li.listItem {
     /*   border: 2px outset silver; */
    }
</style>

<div id="readHoliday" class="centreCargo">
	
	<h3>${holidayGroup.groupName}</h3>


    <div id="photoAlbums">
        <div>
            <ul>
                <c:forEach items="${holidayGroup.photoAlbums}" var="photoAlbum">
                <li class="listItem">
                    <div class="">
                        ${photoAlbum.owner.fullname}'s
                        <a href="${photoAlbum.url}">photo album</a>
                        at ${photoAlbum.sharingProvider}
                    </div>
                    <div>
                        <c:forEach items="${photoAlbum.thumbnails}" var="thumbnail">
                           <a href="${photoAlbum.url}"><img class="thumbnail" src="${thumbnail}" alt=""/></a>
                        </c:forEach>
                    </div>
                </li>
                </c:forEach>
            </ul>
        </div>
    </div>

</div>

