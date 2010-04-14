package com.flurdy.grid.snaps.dao;

import com.flurdy.grid.snaps.domain.SecurityAuthority;
import com.flurdy.grid.snaps.domain.SecurityAuthority.AuthorityRole;
import com.flurdy.grid.snaps.domain.SecurityDetail;

public interface ISecurityRepository {

	public SecurityDetail findSecurityDetail(String username);

	public SecurityAuthority findAuthority(AuthorityRole authorityRole);

	public void addAuthority(SecurityAuthority authority);

	public void addSecurityDetail(SecurityDetail securityDetail);

	public void updateSecurityDetail(SecurityDetail securityDetail);

}
