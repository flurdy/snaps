/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.fotogator.wicket.panel;

import com.flurdy.grid.fotogator.wicket.html.MenuLink;
import com.flurdy.grid.nautical.NauticalGrid;
import java.util.List;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;

public class MenuPanel extends Panel {

	private static final String ROW_ID = "menuRow";

	public MenuPanel(NauticalGrid grid) {
        super(grid.getWicketId());
		setVisible(false);
    }
	
    public MenuPanel(NauticalGrid grid, List<MenuLink> menuLinks) {
        super(grid.getWicketId());
		RepeatingView menuRows = new RepeatingView(ROW_ID);
		add(menuRows);
		for(MenuLink menuLink : menuLinks ){
			menuRows.add( new MenuRow(menuRows.newChildId(),menuLink) );
		}
    }
	private class MenuRow extends WebMarkupContainer {
		public MenuRow(String rowId,MenuLink menuLink){
			super( rowId );
			add( menuLink );
		}		
	}

}
