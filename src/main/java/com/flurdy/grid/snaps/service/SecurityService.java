package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.ISecurityRepository;
import com.flurdy.grid.snaps.dao.ITravellerRepository;
import com.flurdy.grid.snaps.domain.SecurityAuthority;
import com.flurdy.grid.snaps.domain.SecurityAuthority.AuthorityRole;
import com.flurdy.grid.snaps.domain.SecurityDetail;
import com.flurdy.grid.snaps.domain.Traveller;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService implements ISecurityService {

	private static final String DEFAULT_SUPER_USERNAME = "super";
	private static final String DEFAULT_SUPER_FULLNAME = "Super User";
	private static final String DEFAULT_SUPER_PASSWORD = "superpassword"; // TODO create a reset pw email instead
	private static final String DEFAULT_SUPER_EMAIL = "info@code.flurdy.com";

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final AuthorityRole[] defaultAuthorityRoles = {AuthorityRole.ROLE_USER};

	@Autowired
	private ISecurityRepository securityRepository;

	@Autowired
	private ITravellerRepository travellerRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	
	
	@Override
	public SecurityDetail findLogin(String username) {
		if (username != null && username.length() > 0) {
			return securityRepository.findSecurityDetail(username);
		} else {
			return null;
		}
	}

	@Override
	public void registerTraveller(Traveller traveller) {
		if (traveller != null) {
			log.debug("Registering traveller: "+traveller);

			// TODO validate input

			if( securityRepository.findSecurityDetail(
						traveller.getSecurityDetail().getUsername()) == null ){

				applyDefaultAuthorities(traveller.getSecurityDetail());
				applyEncryptedPassword(traveller.getSecurityDetail());
				securityRepository.addSecurityDetail(traveller.getSecurityDetail());
				travellerRepository.addTraveller(traveller);
			} else {
				throw new IllegalArgumentException("Username already taken");
			}
		} else
			throw new NullPointerException();

	}

	private synchronized void applyDefaultAuthorities(SecurityDetail securityDetail) {
		securityDetail.setAuthorities(null);
		securityDetail.setAuthorities(new HashSet<SecurityAuthority>());

		for(int i=0;i<defaultAuthorityRoles.length;i++){
			SecurityAuthority authority = securityRepository.findAuthority(defaultAuthorityRoles[i]);
			if( authority == null ){
				securityRepository.addAuthority(new SecurityAuthority(defaultAuthorityRoles[i]));
				authority = securityRepository.findAuthority(defaultAuthorityRoles[i]);
			}
			securityDetail.getAuthorities().add(authority);
		}
	}


	private synchronized void applyEncryptedPassword(SecurityDetail securityDetail) {
		securityDetail.setPassword( passwordEncoder.encodePassword(
				securityDetail.getPassword(), securityDetail.getUsername() ) );
	}

	@Override
	public void enforceAristocracy() {
		if(!doesSuperUserExist()){
			if( findLogin(DEFAULT_SUPER_USERNAME) != null  ){
				updateDefaultSuperUser();
			} else
				createDefaultSuperUser();
		}
	}

	private boolean doesSuperUserExist() {
		SecurityDetail securityDetail = securityRepository.findSecurityDetail(DEFAULT_SUPER_USERNAME);
		// TODO loop all users
		if( securityDetail != null ){
			for (SecurityAuthority authority : securityDetail.getAuthorities()) {
				if( authority.getAuthorityRole().equals(AuthorityRole.ROLE_SUPER) ){
					return true;
				}
			}
		}
		return false;
	}

	private void updateDefaultSuperUser() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	private void createDefaultSuperUser() {
		Traveller traveller = new Traveller.Builder()
					.username(DEFAULT_SUPER_USERNAME)
					.fullname(DEFAULT_SUPER_FULLNAME)
					.password(DEFAULT_SUPER_PASSWORD)
					.email(DEFAULT_SUPER_EMAIL)
					.authorities(new HashSet<SecurityAuthority>(){{
						add(new SecurityAuthority(AuthorityRole.ROLE_SUPER));
						add(new SecurityAuthority(AuthorityRole.ROLE_ADMIN));
						add(new SecurityAuthority(AuthorityRole.ROLE_USER));
					}})
					.build();
		registerTraveller(traveller);
	}

}
