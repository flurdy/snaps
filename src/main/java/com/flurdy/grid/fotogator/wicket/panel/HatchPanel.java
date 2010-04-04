/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.fotogator.wicket.panel;
import com.flurdy.grid.fotogator.wicket.html.MenuLink;
import com.flurdy.grid.nautical.NauticalGrid;
import java.util.List;

/**
 *
 * @author ivar
 */
public final class HatchPanel extends MenuPanel {

    public HatchPanel() {
        super(NauticalGrid.HATCH);
    }

    public HatchPanel(List<MenuLink> menuLinks) {
        super(NauticalGrid.HATCH,menuLinks);
    }

}
