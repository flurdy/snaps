<%@page contentType="text/html" pageEncoding="UTF-8"%>

	<link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
<!--
	<link href="${contextPage.request.contextPath}/style/jquery-ui.custom.css" rel="stylesheet" type="text/css"/>
-->
<style>
	.ui-widget-header {
		background: none;
	}
	.ui-widget-content {
		border: 0;
	}
	.ui-widget-header {
		border: 0;
		border-bottom: 1px solid black;
	}
}

</style>

	<div id="tabs">
		<ul>
			<li><a href="#tabs-1"><span>Picasa</span></a></li>
			<li><a href="#tabs-2"><span>Flickr</span></a></li>
			<!--
			<li><a href="#tabs-3"><span>SnapFish</span></a></li>
			<li><a href="#tabs-4"><span>Fotobucket</span></a></li>
			<li><a href="#tabs-5"><span>FotoKnudsen</span></a></li>
			-->
		</ul>
				
		<div id="tabs-1">
			<form action="${pageContext.request.contextPath}/holiday/${holidayGroup.groupId}/album" method="post">
				<input type="hidden" name="providerName" value="PicasaWeb"/>

				URL: <input type="text" name="url" value=""/>

				<button>add album</button>

			</form>
		</div>
		<div id="tabs-2">

			<form action="${pageContext.request.contextPath}/holiday/${holidayGroup.groupId}/album" method="post">
				<input type="hidden" name="providerName" value="flickr"/>
				URL: <input type="text" name="url" value=""/>

				<button>add set</button>

			</form>
		</div>
	</div>

