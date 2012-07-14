<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!--[if IE 6]>
	<script type="text/javascript">
		var __noconflict = true;
		var IE6UPDATE_OPTIONS = {
			icons_path: "http://static.ie6update.com/hosted/ie6update/images/",
			url: "http://www.mozilla.com/en-US/firefox/ie.html?from=sfx&amp;uid=49307&amp;t=450"
		}
	</script>
	<script type="text/javascript" src="http://static.ie6update.com/hosted/ie6update/ie6update.js"></script>
<![endif]-->
<c:choose>
<c:when test="${pageContext.request.serverName != 'localhost'
        && ! fn:startsWith(pageContext.request.serverName,'192.168')
        && ! fn:startsWith(pageContext.request.serverName,'djalma.flurdy.net')
        && ! empty analyticsId }">
 <script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
try {
var pageTracker = _gat._getTracker('${analyticsId}');
pageTracker._trackPageview();
} catch(err) {}</script>
</c:when>
 <c:otherwise><!-- Analytics disabled. Id: ${analyticsId} --></c:otherwise>
</c:choose>