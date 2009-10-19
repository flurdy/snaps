package com.flurdy.grid.fotogator.wicket;

import com.flurdy.grid.fotogator.wicket.holiday.HolidayGroupPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.lang.PackageName;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see wicket.myproject.Start#main(String[])
 */
public class WicketApplication extends WebApplication {

	/**
	 * Constructor
	 */
	public WicketApplication() {
			mountBookmarkablePage("index.html", HomePage.class);
			mountBookmarkablePage("about.html", AboutPage.class);
			mountBookmarkablePage("help.html", HelpPage.class);
//			mount("/group", PackageName.forClass(HolidayGroupPage.class));
			mountBookmarkablePage("/group/holiday.html", HolidayGroupPage.class);
	}

	/**
	 * @see wicket.Application#getHomePage()
	 */
	public Class getHomePage() {
		return HomePage.class;
	}
}
