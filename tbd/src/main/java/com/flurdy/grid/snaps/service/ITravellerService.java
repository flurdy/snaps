/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.Traveller;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

public interface ITravellerService {

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER')")
	public List<Traveller> findTravellers();

	@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_SUPER')")
	public Traveller findTraveller(long travellerId);

	@PreAuthorize("isAuthenticated()")
	public Traveller findCurrentTraveller();
}
