package com.flurdy.grid.fotogator.wicket.panel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class JibPanel extends Panel {

	public static final String ID = "jib";
	private static final String PAGETITLE_ID = "pageTitle";

	public JibPanel() {
		super(ID);
		add( new Label(PAGETITLE_ID,""));
		setVisible(false);
		
	}
	public JibPanel(String pageTitel) {
		super(ID);
		add( new Label(PAGETITLE_ID,pageTitel));
	}

}
