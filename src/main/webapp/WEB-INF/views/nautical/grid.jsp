<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<title><tiles:getAsString name="pageTitle"/> <tiles:getAsString name="headerTitle"/></title>
		<meta name="keywords" content="snaps,photo,photography,holiday,travel,events,friends,social,group,flurdy" />
		<meta name="description" content="holiday photo snaps aggregator" />
		<link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico"/>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/grid.css"/>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/site.css"/>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style/cargo.css"/>
		<tiles:insertAttribute name="debug"/>
        <style>
			/* firefox css bug hack */
			#ocean #ship #stern { display: none; }
			#ocean #ship #aft { display: none; }
		</style>
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.0/jquery-ui.min.js"></script>
		<!-- <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.custom.min.js"></script> -->
		<script type="text/javascript">
			<tiles:insertAttribute name="cargoJQuery"/>
				function loader(){
					document.getElementById("stern").style.display="block";
					document.getElementById("aft").style.display="block";
					// window.setTimeout("window.location.reload()", 3000);
				}
		</script>
	</head>
	<body onload="loader()">
		<div id="ocean" class="structure">
			<div id="lighthouse" class="structure">
				<div class="compartment"><!--  --></div>
			</div>
			<div id="fleet" class="structure">
				<div id="regatta" class="structure">
					<div id="tug" class="structure">
						<div class="compartment ad">
							<!-- <img src="${pageContext.request.contextPath}/images/leaderboard.gif" alt=""/> -->
						</div>
					</div>
					<div id="ship" class="structure">
						<div id="figurehead" class="compartment"><!--  --></div>
						<div id="hull" class="structure">
							<div id="vaka" class="structure">
								<div id="prow" class="compartment">
									<sec:authorize ifNotGranted="ROLE_USER">
										<a href="${pageContext.request.contextPath}/login.html">login</a>
									</sec:authorize>
									<sec:authorize ifAllGranted="ROLE_USER">
										<sec:authentication property="principal.username" />
										| <a href="${pageContext.request.contextPath}/j_spring_security_logout">logout</a>
									</sec:authorize>
								</div>
								<div id="bow" class="compartment">
									<a href="${pageContext.request.contextPath}/"><h1><tiles:getAsString name="bowTitle"/></h1></a>
									<!-- <div id="mission">your holiday photos</div> -->
								</div>
								<div id="lookout" class="compartment">
									<ul class="horizontal">
										<li><a href="${pageContext.request.contextPath}/">home</a></li>
										<sec:authorize ifNotGranted="ROLE_USER">
											<li><a href="${pageContext.request.contextPath}/join.html">join</a></li>
										</sec:authorize>
										<li><a href="${pageContext.request.contextPath}/about.html">about</a></li>
									</ul>
								</div>
								<div id="keel" class="structure">
									<div id="port" class="compartment"><tiles:insertAttribute name="port" /></div>
									<div id="innerhull" class="structure">
										<div id="jib" class="compartment"><h2><tiles:getAsString name="pageTitle"/></h2></div>
										<div id="foremast" class="compartment">
											<!-- <img src="${pageContext.request.contextPath}/images/leaderboard.gif" alt=""/> -->
										</div>
										<div id="bulkhead" class="structure">
											<div id="hatch" class="compartment"></div>
											<div id="ballast" class="structure compartment">
												<div id="cargo" class="compartment">
													<tiles:insertAttribute name="cargo" />
												</div>
											</div>
											<div id="deck" class="compartment"><tiles:insertAttribute name="deck" /></div>
										</div>
										<div id="bridge" class="compartment"><!--  --></div>
									</div>
									<div id="starboard" class="compartment"><tiles:insertAttribute name="starboard" /></div>
								</div>
								<div id="aft" class="compartment">
									<ul class="horizontal">
										<li><a href="http://flurdy.com/contact/">contact</a></li>
										<li><a href="http://code.flurdy.com/grid/snaps">project</a></li>
										<li><a href="http://code.flurdy.com/grid/snaps">help</a></li>
									</ul>
								</div>
								<div id="stern" class="compartment">
									<p title="<tiles:getAsString name="version"/>"><tiles:getAsString name="releaseStage"/></p>
									<a href="http://flurdy.com"><img alt="flurdy" src="${pageContext.request.contextPath}/images/flurdy-small-crop.png"  /></a>
								</div>
							</div>
						</div>
						<div id="anchor" class="compartment"><!--  --></div>
					</div>
					<div id="ama" class="structure">
						<div class="compartment ad">
						<!--	<img src="${pageContext.request.contextPath}/images/wideskyscraper.gif" alt=""/> -->
						</div>
					</div>
					<div id="net" class="structure">
						<div class="compartment ad">
							<!-- <img src="${pageContext.request.contextPath}/images/leaderboard_img.jpg" alt=""/> -->
						</div>
					</div>
				</div>
			</div>
			<div id="buoy" class="structure"><div class="compartment"><!--  --></div></div>
		</div>
		<div id="dragons" class="structure"><tiles:insertAttribute name="dragons"/></div>
	</body>
	<!-- Copyright Ivar Abrahamsen 2010. Copyleft: AGPL & CC (by sa) -->
</html>