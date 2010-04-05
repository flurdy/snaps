package com.flurdy.grid.fotogator.wicket;

import com.flurdy.grid.fotogator.wicket.holiday.HolidayGroupPage;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

public class WicketApplication extends WebApplication {

	public WicketApplication() {
		super();
	}
	
	@Override
	public void init(){
		super.init();
		mountBookmarkablePage("index.html", HomePage.class);
		mountBookmarkablePage("about.html", AboutPage.class);
		mountBookmarkablePage("help.html", HelpPage.class);
//			mount("/group", PackageName.forClass(HolidayGroupPage.class));
		mountBookmarkablePage("/group/holiday.html", HolidayGroupPage.class);
        addComponentInstantiationListener(new SpringComponentInjector(this));
	}

	/**
	 * @see wicket.Application#getHomePage()
	 */
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}
}
