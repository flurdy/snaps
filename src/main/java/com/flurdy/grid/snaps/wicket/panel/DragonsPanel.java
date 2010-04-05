package com.flurdy.grid.fotogator.wicket.panel;
import com.flurdy.grid.nautical.NauticalGrid;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

public final class DragonsPanel extends Panel {

	static final String id = NauticalGrid.DRAGONS.getWicketId();
	static final String ANALYTICS_ID = "analytics";
	static final String STOPIE_ID = "stopie";

    public DragonsPanel( boolean isFrontPage, boolean isTestSite ) {
        super (id);

		if( !isTestSite ) {
			Panel analyticsPanel = new AnalyticsPanel(ANALYTICS_ID);
			analyticsPanel.setRenderBodyOnly(true);
			add( analyticsPanel );
		} else {
			WebMarkupContainer analyticsPanel = new WebMarkupContainer(ANALYTICS_ID);
			add(analyticsPanel);
			analyticsPanel.setVisible(false);
		}

		if( isFrontPage ){
			Panel stopiePanel = new StopIEPanel(STOPIE_ID);
			stopiePanel.setRenderBodyOnly(true);
			add(stopiePanel);
		} else {
			WebMarkupContainer stopiePanel = new WebMarkupContainer(STOPIE_ID);
			add(stopiePanel);
			stopiePanel.setVisible(false);
		}
    }


}
