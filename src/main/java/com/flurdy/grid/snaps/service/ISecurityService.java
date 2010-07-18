/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.SecurityAuthority;
import com.flurdy.grid.snaps.domain.SecurityAuthority.AuthorityRole;
import com.flurdy.grid.snaps.domain.SecurityDetail;
import com.flurdy.grid.snaps.domain.Traveller;
import org.springframework.security.access.prepost.PreAuthorize;

public interface ISecurityService {

//	@PreAuthorize("isAnonymous()")
	public SecurityDetail findLogin(String username);

	@PreAuthorize("isAnonymous()")
	public void registerTraveller(Traveller traveller);

	@PreAuthorize("isAuthenticated()")
	public void enforceAristocracy();

//	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER')")
//	public void updateSecurityDetail(String username, SecurityDetail securityDetail);
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER')")
	public void removeAuthority(String username, SecurityAuthority authority);

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER')")
	public void addAuthority(String username, SecurityAuthority authority);

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER')")
	public void changeSecurityDetailPassword(String username, String password);

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER')")
	public void enableSecurityDetail(String username);

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_SUPER')")
	public void disableSecurityDetail(String username);

	@PreAuthorize("isAuthenticated()")
	public String findLoggedInUsername();


}
