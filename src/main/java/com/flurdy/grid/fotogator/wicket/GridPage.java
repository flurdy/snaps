/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flurdy.grid.fotogator.wicket;

import com.flurdy.grid.fotogator.wicket.html.MenuLink;
import com.flurdy.grid.fotogator.wicket.panel.LookoutPanel;
import com.flurdy.grid.fotogator.wicket.panel.DragonsPanel;
import com.flurdy.grid.fotogator.wicket.panel.HatchPanel;
import com.flurdy.grid.fotogator.wicket.panel.JibPanel;
import com.flurdy.grid.nautical.NauticalGrid;
import java.util.List;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.protocol.http.WebRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ivar
 */
public class GridPage extends WebPage {

	protected transient Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String FEEDBACK_ID ="feedback";

	public GridPage() {
		super();
		addLayout();
	}

	public GridPage(PageParameters params) {
		super(params);
		addLayout();
	}

	private void addLayout() {
		Label titleLabel = new Label("title.head", new ResourceModel("title.head"));
		titleLabel.setRenderBodyOnly(true);
		add(titleLabel);

		addEmptyLookout();
		addEmptyJib();
//		addEmptyPort();
		addEmptyHatch();

		add( new BookmarkablePageLink<Void>("aboutLink", AboutPage.class ));

		add( new BookmarkablePageLink<Void>("helpLink", HelpPage.class ));

		add( new FeedbackPanel(FEEDBACK_ID));


		Panel dragonsPanel = new DragonsPanel(isFrontPage(), isTestSite());
		add(dragonsPanel);
//		dragonsPanel.setOutputMarkupPlaceholderTag(false);
		dragonsPanel.setRenderBodyOnly(true);
	}

	private boolean isFrontPage() {
		return (getPage() instanceof HomePage);
	}

	private boolean isTestSite() {
		String serverName = ((WebRequest) getRequest()).getHttpServletRequest().getServerName();
		if (serverName.equals("localhost")) {
			return true;
		} else {
			return false;
		}
	}

	protected void addEmptyJib(){
		addOrReplace( new JibPanel() );
	}
	protected void addEmptyLookout(){
		addOrReplace(new LookoutPanel());
	}
//	protected void addEmptyPort(){
//
//	}
	protected void addEmptyHatch(){
		addOrReplace(new HatchPanel());
	}


	protected void addJibTitle(String jibTitle){
//		remove( JibPanel.ID);
		replace( new JibPanel(jibTitle));
	}

	protected void addHatchMenu(List<MenuLink> menuLinks){
//		remove( NauticalGrid.HATCH.getWicketId() );
		replace( new HatchPanel( menuLinks ) );
	}
}

