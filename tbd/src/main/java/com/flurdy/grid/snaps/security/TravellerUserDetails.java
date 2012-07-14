package com.flurdy.grid.snaps.security;


import com.flurdy.grid.snaps.domain.SecurityDetail;
import com.flurdy.grid.snaps.domain.SecurityDetail.AuthorityRole;
import com.flurdy.grid.snaps.service.ISecurityService;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class TravellerUserDetails implements UserDetailsService {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected ISecurityService securityService;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {

//		log.debug("Loading user: "+ username);

		SecurityDetail securityDetail = null;

		try {
			securityDetail = securityService.findLogin(username);
		} catch (Exception ex) {
			log.error("username error",ex);
			throw new DataAccessResourceFailureException("Security loading problem.",ex);
		}

		if( securityDetail == null ){
				log.info("User not found: " + username);
				throw new UsernameNotFoundException("User is not on this system.");
		}

//		log.debug("Found security detail: " +securityDetail);
		
		Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
		if( securityDetail.getAuthorities() != null ) {
			for(AuthorityRole authority : securityDetail.getAuthorities() ){
//				log.debug("Authority: " + authority);
				grantedAuthorities.add( new GrantedAuthorityImpl( authority.toString() ));
			}
		}


		return new User( username, securityDetail.getPassword(),
				true,							// account enabled
				true,							// Account not expired
				true,							// credentials not expired
				true,							// account not locked
				grantedAuthorities );
	}
	



}
