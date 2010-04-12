/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.SecurityDetail;
import com.flurdy.grid.snaps.domain.Traveller;

public interface ISecurityService {

	public SecurityDetail findLogin(String username);

	public void registerTraveller(Traveller traveller);

	public void enforceAristocracy();

}
