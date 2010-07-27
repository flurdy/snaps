package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.SecurityDetail;
import com.flurdy.grid.snaps.domain.SecurityDetail.AuthorityRole;
import com.flurdy.grid.snaps.domain.Traveller;

import com.flurdy.grid.snaps.exception.SnapInvalidClientInputException;
import com.flurdy.grid.snaps.exception.SnapLogicalException;
import com.flurdy.grid.snaps.exception.SnapNotFoundException;
import com.flurdy.grid.snaps.exception.SnapTechnicalException;
import com.flurdy.grid.snaps.exception.SnapTechnicalException.SnapTechnicalError;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
public class SecurityService extends AbstractService implements ISecurityService {

	private static final String DEFAULT_SUPER_USERNAME = "super";
	private static final String DEFAULT_SUPER_FULLNAME = "Super User";
	private static final String DEFAULT_SUPER_PASSWORD = "superpassword"; // TODO create a reset pw email instead
	private static final String DEFAULT_SUPER_EMAIL = "invalid@example.com";
		
	private static final AuthorityRole[] defaultAuthorityRoles = {AuthorityRole.ROLE_USER};

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private IEmailService emailService;

	
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

			if(traveller.isValid()){

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
				throw new SnapTechnicalException( SnapTechnicalError.INVALID_INPUT, "Traveller invalid" );
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

	
	
	private void createDefaultSuperUser() {

		log.warn("Creating default super user");

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

		assert username != null;
		assert password != null;
		
		log.info("Changing password for: " + username);

		SecurityDetail securityDetail = securityRepository.findSecurityDetail(username);

		if (securityDetail != null ){


			if( password.trim().length() > 3){

				securityDetail.setPassword(password);

				applyEncryptedPassword(securityDetail);

				securityRepository.updateSecurityDetail(securityDetail);
			} else {
				throw new SnapInvalidClientInputException(SnapInvalidClientInputException.InputError.PASSWORD_LENGTH);
			}
		} else {
			throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.SECURITY_DETAILS);
		}
	}



	@Override
	public void disableSecurityDetail(String username) {

		log.info("Disabling user: " + username);

		SecurityDetail securityDetail = securityRepository.findSecurityDetail(username);
		if (securityDetail != null ){

			securityDetail.setEnabled(false);

			securityRepository.updateSecurityDetail(securityDetail);
			
		} else {
			throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.SECURITY_DETAILS);
		}
	}

	@Override
	public String findLoggedInUsername() {

		assert SecurityContextHolder.getContext()!=null;

		if( SecurityContextHolder.getContext().getAuthentication() != null ){

			assert SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null;
			assert SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User;

			return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

		} else {
			throw new SnapLogicalException(SnapLogicalException.SnapLogicalError.INVALID_STATE,"No one is logged in");
		}
	}

	private void resetPassword(final Traveller traveller){
		log.info("Resetting password for: " + traveller);

		final String randomPassword = RandomStringUtils.randomAlphanumeric(8);
		traveller.getSecurityDetail().setPassword(randomPassword);

		applyEncryptedPassword(traveller.getSecurityDetail());

		securityRepository.updateSecurityDetail(traveller.getSecurityDetail());

		emailService.sendPassword(traveller,randomPassword);

	}


	@Override
	public void resetPassword(final String usernameOrEmail) {

		assert usernameOrEmail != null && usernameOrEmail.trim().length()>3;

		log.info("Trying to reset password for: " + usernameOrEmail);

		Set<SecurityDetail> possibleCandidates = new HashSet<SecurityDetail>();

		if( usernameOrEmail.contains("@") ){
			SecurityDetail securityDetail = securityRepository.findSecurityDetailByEmail(usernameOrEmail);
			if (securityDetail != null ){
				possibleCandidates.add(securityDetail);
			}
		}

		SecurityDetail securityDetail = securityRepository.findSecurityDetail(usernameOrEmail);
		if (securityDetail != null ){
			possibleCandidates.add(securityDetail);
		}

		if (possibleCandidates.size() == 1){
			resetPassword( travellerRepository.findTraveller(possibleCandidates.iterator().next().getUsername()) );
		} else if( possibleCandidates.isEmpty()){
			throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.SECURITY_DETAILS);
		} else {			
			throw new SnapLogicalException(SnapLogicalException.SnapLogicalError.INVALID_STATE,"Too many candidates matched");
		}

	}

	@Override
	public void enableSecurityDetail(String username) {

		log.info("Enabling user: " + username);

		SecurityDetail securityDetail = securityRepository.findSecurityDetail(username);
		if (securityDetail != null ){

			securityDetail.setEnabled(true);

			securityRepository.updateSecurityDetail(securityDetail);
			
		} else {
			throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.SECURITY_DETAILS);
		}

	}


	@Override
	public void removeAuthority(String username, AuthorityRole authority) {

		assert username != null;
		assert authority != null;

		log.info("Removing authority ["+authority+"] from: " + username);

		SecurityDetail securityDetail = securityRepository.findSecurityDetail(username);

		if (securityDetail != null ){
//			log.debug("Auths:"+securityDetail.getAuthorities().size());

			securityDetail.removeAuthority(authority);

//			log.debug("Auths:"+securityDetail.getAuthorities().size());

			securityRepository.updateSecurityDetail(securityDetail);
		} else {
			throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.SECURITY_DETAILS);
		}
	}

	

	@Override
	public void addAuthority(String username, AuthorityRole authority) {

		assert username != null && username.length()>0;
		assert authority != null;
		
		log.info("Adding authority ["+authority+"] from: " + username);
		

		SecurityDetail securityDetail = securityRepository.findSecurityDetail(username);

		if (securityDetail != null ){

//			log.debug("Auths:"+securityDetail.getAuthorities().size());

			securityDetail.addAuthority(authority);

//			log.debug("Auths:"+securityDetail.getAuthorities().size());

			securityRepository.updateSecurityDetail(securityDetail);
		} else {
			throw new SnapNotFoundException(SnapNotFoundException.SnapResourceNotFound.SECURITY_DETAILS);
		}
	}


}
