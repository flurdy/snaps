<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<div class="centreCargo">

	<c:choose>
		<c:when test="${photoAlbum.sharingProvider=='PicasaWeb'}">

			<table style="width:194px;">
				<tr>
					<td align="center" style="height:194px;background:url(http://picasaweb.google.com/s/c/transparent_album_background.gif) no-repeat left">
						<a href="${photoAlbum.url}&feat=embedwebsite"><img src="http://lh3.ggpht.com/_mbFRkAi58dE/Svc9zgimIgE/AAAAAAAAdbU/yqtozvehKfU/s160-c/GalapagosUnderwater.jpg"
										width="160" height="160" style="margin:1px 0 0 4px;"></a>
					</td>
				</tr>
				<tr>
					<td style="text-align:center;font-family:arial,sans-serif;font-size:11px">
						<a href="photoAlbum.url&feat=embedwebsite" style="color:#4D4D4D;font-weight:bold;text-decoration:none;">Photo Album</a>
					</td>
				</tr>
			</table>
			


		</c:when>
		<c:when test="photoAlbum.sharingProvider == 'flickr'"></c:when>
		<c:otherwise>
			Sharing provider ${photoAlbum.sharingProvider} not recognised.
		</c:otherwise>
	</c:choose>




</div>




