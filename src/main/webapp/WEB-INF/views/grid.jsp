<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:wicket="http://wicket.apache.org">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<title><tiles:getAsString name="pageTitle"/></title>
		<meta name="keywords" content="snaps,photo,photography,holiday,travel,events,friends,social,group,flurdy" />
		<meta name="description" content="holiday photo snaps aggregator" />
		<link rel="shortcut icon" href="favicon.ico">
		<link rel="stylesheet" type="text/css" href="style/grid.css"/>
		<link rel="stylesheet" type="text/css" href="style/site.css"/>
		<link rel="stylesheet" type="text/css" href="style/cargo.css"/>
		<script type="text/javascript">
			function loader(){
					window.setTimeout("window.location.reload()", 3000);
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
						<div class="compartment">
							<img src="images/leaderboard.gif" alt=""/>
						</div>
					</div>
					<div id="ship" class="structure">
						<div id="figurehead" class="compartment"><!--  --></div>
						<div id="hull" class="structure">
							<div id="vaka" class="structure">
								<div id="prow" class="compartment"><!--  --></div>
								<div id="bow" class="compartment"><h1><tiles:getAsString name="bowTitle"/></h1></div>
								<div id="lookout" class="compartment" wicket:id="lookout"><!-- super menu --></div>
								<div id="keel" class="structure">
									<div id="port" class="compartment"><!-- site menu --></div>
									<div id="innerhull" class="structure">
										<div id="jib" class="compartment" wicket:id="jib"><!-- page title --></div>
										<div id="foremast" class="compartment"><!-- breadcrumb --></div>
										<div id="bulkhead" class="structure">
											<div id="hatch" class="compartment" wicket:id="hatch"><!-- item menu --></div>
											<div id="ballast" class="structure compartment">
												<div id="cargo" class="compartment">
													<tiles:insertAttribute name="cargo" />
												</div>
											</div>
											<div id="deck" class="compartment"><!-- item info --></div>
										</div>
										<div id="bridge" class="compartment" wicket:id="feedback"><!--  --></div>
									</div>
									<div id="starboard" class="compartment"><!-- site info --></div>
								</div>
								<div id="aft" class="compartment">
									<ul class="horizontal">
										<li><a href="http://flurdy.com/contact/">contact</a></li>
										<li><a wicket:id="aboutLink" href="#">about</a></li>
										<li><a wicket:id="helpLink" href="#">help</a></li>
									</ul>
								</div>
								<div id="stern" class="compartment">
									<p>v.0.2&alpha;</p>
									<a href="http://flurdy.com"><img alt="flurdy" src="images/flurdy-small-crop.png"  /></a>
								</div>
							</div>
						</div>
						<div id="anchor" class="compartment"><!--  --></div>
					</div>
					<div id="ama" class="structure">
						<div class="compartment">
							<img src="images/wideskyscraper.gif" alt=""/>
						</div>
					</div>
					<div id="net" class="structure">
						<div class="compartment">
							<img src="images/leaderboard_img.jpg" alt=""/>
						</div>
					</div>
				</div>
			</div>
			<div id="buoy" class="structure">
				<div class="compartment"><!--  --></div>
			</div>
		</div>
		<div id="dragons" class="structure"><!-- tiles:insertAttribute name="dragoons"  --></div>
    </body>
	<!-- copyright and copyleft flurdy.com -->
</html>