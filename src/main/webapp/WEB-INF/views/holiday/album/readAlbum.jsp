<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<div class="centreCargo">

	<c:choose>
		<c:when test="${photoAlbum.sharingProvider eq 'PicasaWeb'}">

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
		<c:when test="${photoAlbum.sharingProvider eq 'flickr'}">

			<p>
				<a href="${pageContext.request.contextPath}//holiday/${holidayGroup.groupId}">${holidayGroup.groupName}</a>

				<a href="http://flickr.com">provider: flickr</a>

				<a href="${photoAlbum.url}">photo album</a>
			</p>

			<!-- Start of Flickr Badge -->
<style type="text/css">
.zg_div {margin:0px 5px 5px 0px; width:117px;}
.zg_div_inner {border: solid 1px #000000; background-color:#ffffff;  color:#666666; text-align:center; font-family:arial, helvetica; font-size:11px;}
.zg_div a, .zg_div a:hover, .zg_div a:visited {color:#3993ff; background:inherit !important; text-decoration:none !important;}
</style>
<script type="text/javascript">
zg_insert_badge = function() {
var zg_bg_color = 'ffffff';
var zgi_url = 'http://www.flickr.com/apps/badge/badge_iframe.gne?zg_bg_color='+zg_bg_color+'&zg_person_id=31241890%40N00&zg_set_id=72157622527536656&zg_context=in%2Fset-72157622527536656%2F';
document.write('<iframe style="background-color:#'+zg_bg_color+'; border-color:#'+zg_bg_color+'; border:none;" width="113" height="151" frameborder="0" scrolling="no" src="'+zgi_url+'" title="Flickr Badge"><\/iframe>');
if (document.getElementById) document.write('<div id="zg_whatlink"><a href="http://www.flickr.com/badge.gne"	style="color:#3993ff;" onclick="zg_toggleWhat(); return false;">What is this?<\/a><\/div>');
}
zg_toggleWhat = function() {
document.getElementById('zg_whatdiv').style.display = (document.getElementById('zg_whatdiv').style.display != 'none') ? 'none' : 'block';
document.getElementById('zg_whatlink').style.display = (document.getElementById('zg_whatdiv').style.display != 'none') ? 'none' : 'block';
return false;
}
</script>
<div class="zg_div"><div class="zg_div_inner"><a href="http://www.flickr.com">www.<strong style="color:#3993ff">flick<span style="color:#ff1c92">r</span></strong>.com</a><br>
<script type="text/javascript">zg_insert_badge();</script>
<div id="zg_whatdiv">This is a Flickr badge showing items in a set called <a href="http://www.flickr.com/photos/31241890@N00/sets/72157622527536656">Galapagos 2009</a>. Make your own badge <a href="http://www.flickr.com/badge.gne">here</a>.</div>
<script type="text/javascript">if (document.getElementById) document.getElementById('zg_whatdiv').style.display = 'none';</script>
</div>
</div>
<!-- End of Flickr Badge -->



		</c:when>
		<c:otherwise>
			Sharing provider ${photoAlbum.sharingProvider} not recognised.
		</c:otherwise>
	</c:choose>




</div>




