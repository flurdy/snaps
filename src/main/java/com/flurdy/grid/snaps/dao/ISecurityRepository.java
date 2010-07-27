package com.flurdy.grid.snaps.dao;

import com.flurdy.grid.snaps.domain.SecurityDetail;

public interface ISecurityRepository {

	public SecurityDetail findSecurityDetail(String username);

//	public AuthorityRole findAuthority(AuthorityRole authorityRole);

//	public void addAuthority(SecurityAuthority authority);

	public void addSecurityDetail(SecurityDetail securityDetail);

	public void updateSecurityDetail(SecurityDetail securityDetail);

	public SecurityDetail findSecurityDetailByEmail(String usernameOrEmail);
}
