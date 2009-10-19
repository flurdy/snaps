/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.flurdy.grid.fotogator.wicket.html;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

public abstract class MenuLink extends Link<String> {

	private static final String LINK_ID = "menuLink";
	private static final String LABEL_ID = "menuLabel";

	public MenuLink(String labelText) {
		super(LINK_ID);
		add(new Label(LABEL_ID, labelText));
	}
	
}
