package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.SecurityDetail;
import com.flurdy.grid.snaps.domain.SecurityDetail.AuthorityRole;
import com.flurdy.grid.snaps.domain.Traveller;

import com.flurdy.grid.snaps.exception.SnapInvalidClientInputException;
import com.flurdy.grid.snaps.exception.SnapTechnicalException;
import com.flurdy.grid.snaps.exception.SnapTechnicalException.SnapTechnicalError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class SecurityService extends AbstractService implements ISecurityService {

	private static final String DEFAULT_SUPER_USERNAME = "super";
	private static final String DEFAULT_SUPER_FULLNAME = "Super User";
	private static final String DEFAULT_SUPER_PASSWORD = "superpassword"; // TODO create a reset pw email instead
	private static final String DEFAULT_SUPER_EMAIL = "invalid@example.com";
		
	private static final AuthorityRole[] defaultAuthorityRoles = {AuthorityRole.ROLE_USER};

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
				throw new SnapInvalidClientInputException( SnapInvalidClientInputException.InputError.USERNAME_TAKEN );
			}
		} else {
			throw new SnapTechnicalException( SnapTechnicalError.INVALID_INPUT, new NullPointerException() );
		}
	}

	private synchronized void applyDefaultAuthorities(SecurityDetail securityDetail) {
		for(int i=0;i<defaultAuthorityRoles.length;i++){
			securityDetail.addAuthority(defaultAuthorityRoles[i]);
		}
	}


	private synchronized void applyEncryptedPassword(SecurityDetail securityDetail) {
		securityDetail.setPassword( passwordEncoder.encodePassword(
				securityDetail.getPassword(), securityDetail.getUsername() ) );
	}


	@Override
	public void enforceAristocracy() {
		log.warn("Enforcing aristocracy check!");
		if(!doesSuperUserExist()){
			log.warn("Aristocracy will be enforced! As no super user exist!");
			// TODO: reset pw by email instead
			if( findLogin(DEFAULT_SUPER_USERNAME) != null  ){
				log.info("Updating old super user as it exist.");
				updateDefaultSuperUser();
			} else {
				createDefaultSuperUser();
			}
		} else
			log.info("Aristocracy preserved. Super user was found.");
	}



	private boolean doesSuperUserExist() {
		SecurityDetail securityDetail = securityRepository.findSecurityDetail(DEFAULT_SUPER_USERNAME);
		// TODO loop all users
		if( securityDetail != null ){
			for (AuthorityRole authority : securityDetail.getAuthorities()) {
				if( authority.equals(AuthorityRole.ROLE_SUPER) ){
					return true;
				}
			}
		}
		return false;
	}


	private void updateDefaultSuperUser() {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	
//	private SecurityAuthority createAndAddAuthority(AuthorityRole role){
//		SecurityAuthority authority = securityRepository.findAuthority(role);
//		if( authority == null ){
//			securityRepository.addAuthority(new SecurityAuthority( role ));
//			authority = securityRepository.findAuthority( role );
//		}
//		return authority;
//	}

	
	private void createDefaultSuperUser() {

		log.info("Creating default super user");

		Traveller traveller = new Traveller.Builder()
					.username(DEFAULT_SUPER_USERNAME)
					.fullname(DEFAULT_SUPER_FULLNAME)
					.password(DEFAULT_SUPER_PASSWORD)
					.email(DEFAULT_SUPER_EMAIL)
					.build();

		traveller.getSecurityDetail().addAuthority(AuthorityRole.ROLE_SUPER);
		traveller.getSecurityDetail().addAuthority(AuthorityRole.ROLE_ADMIN);
		traveller.getSecurityDetail().addAuthority(AuthorityRole.ROLE_USER);

		applyEncryptedPassword(traveller.getSecurityDetail());

		securityRepository.addSecurityDetail(traveller.getSecurityDetail());
		travellerRepository.addTraveller(traveller);

	}



	@Override
	public void changeSecurityDetailPassword(String username, String password) {

		// TODO validate
		
		log.info("Changing password for: " + username);

		SecurityDetail securityDetail = securityRepository.findSecurityDetail(username);

		assert securityDetail != null;
		if( password !=null && password.trim().length()>0)
			securityDetail.setPassword(password);

		applyEncryptedPassword(securityDetail);

		securityRepository.updateSecurityDetail(securityDetail);
	}



	@Override
	public void disableSecurityDetail(String username) {

		log.info("Disabling user: " + username);

		SecurityDetail securityDetail = securityRepository.findSecurityDetail(username);
		assert securityDetail != null;

		securityDetail.setEnabled(false);

		securityRepository.updateSecurityDetail(securityDetail);
	}

	@Override
	public String findLoggedInUsername() {

		assert (SecurityContextHolder.getContext()!=null &&
					SecurityContextHolder.getContext().getAuthentication() != null &&
					SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null &&
					SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User );
		
		return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
	}


	@Override
	public void enableSecurityDetail(String username) {

		log.info("Enabling user: " + username);

		SecurityDetail securityDetail = securityRepository.findSecurityDetail(username);
		assert securityDetail != null;

		securityDetail.setEnabled(true);

		securityRepository.updateSecurityDetail(securityDetail);
	}


	@Override
	public void removeAuthority(String username, AuthorityRole authority) {

		// TODO validate

		log.info("Removing authority ["+authority+"] from: " + username);

		SecurityDetail securityDetail = securityRepository.findSecurityDetail(username);
//		SecurityAuthority realAuthority = securityRepository.findAuthority(authority.getAuthorityRole());

		log.debug("Auths:"+securityDetail.getAuthorities().size());
		
		securityDetail.removeAuthority(authority);

		log.debug("Auths:"+securityDetail.getAuthorities().size());

		securityRepository.updateSecurityDetail(securityDetail);

	}

	

	@Override
	public void addAuthority(String username, AuthorityRole authority) {

		// TODO validate

		assert username != null && username.length()>0;
		
		log.info("Adding authority ["+authority+"] from: " + username);
		

		SecurityDetail securityDetail = securityRepository.findSecurityDetail(username);
//		SecurityAuthority realAuthority = securityRepository.findAuthority(authority.getAuthorityRole());

		log.debug("Auths:"+securityDetail.getAuthorities().size());

		securityDetail.addAuthority(authority);

		log.debug("Auths:"+securityDetail.getAuthorities().size());

		securityRepository.updateSecurityDetail(securityDetail);
	}


}
