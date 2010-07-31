package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.ISecurityRepository;
import com.flurdy.grid.snaps.domain.SecurityDetail;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.SnapInvalidClientInputException;
import com.flurdy.grid.snaps.exception.SnapLogicalException;
import com.flurdy.grid.snaps.exception.SnapNotFoundException;
import org.junit.Assert;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;

@Transactional
public class SecurityServiceTest extends AbstractServiceTest {
/*
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Resource
	private PasswordEncoder mockPasswordEncoder;

	@Autowired
	private ISecurityRepository securityRepository;

	private static final String TRAVELLER_USERNAME = "travelling testuser";
	private static final String TRAVELLER_PASSWORD = "travelling testuser";
	private static final String TRAVELLER_FULLNAME = "travelling testuser";
	private static final String TRAVELLER_EMAIL = "travelling.testuser@example.com";

	private Long defaultTravellerId = null;

	@Before
	public void setUp(){
		super.setUp();
		ReflectionTestUtils.setField(realSecurityService, "passwordEncoder", passwordEncoder );
		Mockito.when(mockPasswordEncoder.encodePassword(Mockito.anyString(), Mockito.eq(DEFAULT_USERNAME))).thenReturn("RANDOM");
		Mockito.doNothing().when(emailService).sendPassword(Mockito.<Traveller>anyObject(),Mockito.anyString());
		Mockito.doNothing().when(emailService).notifyNewRegistration(Mockito.<Traveller>anyObject());
		addDefaultUser2();
		defaultTravellerId = addDefaultUser();
		assert defaultTravellerId > 0;
	}


	@Test
	public void testFindLogin(){
		SecurityDetail securityDetail = realSecurityService.findLogin(DEFAULT_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertEquals( securityDetail.getUsername(), DEFAULT_USERNAME );
	}

	@Test
	public void testFindNonExistentLogin(){
		SecurityDetail securityDetail = realSecurityService.findLogin("asdasdas");
		Assert.assertNull( securityDetail );
	}

	@Test
	public void testFindNullLogin(){
		SecurityDetail securityDetail = realSecurityService.findLogin(null);
		Assert.assertNull( securityDetail );
	}

	@Test
	public void testRegisterTraveller(){
		Traveller traveller = new Traveller.Builder()
					.username(TRAVELLER_USERNAME)
					.fullname(TRAVELLER_FULLNAME)
					.password(TRAVELLER_PASSWORD)
					.email(TRAVELLER_EMAIL).build();

		realSecurityService.registerTraveller(traveller);

		SecurityDetail securityDetail = realSecurityService.findLogin(TRAVELLER_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertEquals( securityDetail.getUsername(), TRAVELLER_FULLNAME );
	}

	@Test
	public void testRegisterTravellerRoleUse(){
		Traveller traveller = new Traveller.Builder()
					.username(TRAVELLER_USERNAME)
					.fullname(TRAVELLER_FULLNAME)
					.password(TRAVELLER_PASSWORD)
					.email(TRAVELLER_EMAIL).build();

		realSecurityService.registerTraveller(traveller);

		SecurityDetail securityDetail = realSecurityService.findLogin(TRAVELLER_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertNotNull( securityDetail.getAuthorities() );
		Assert.assertTrue( securityDetail.getAuthorities().size() == 1 );
		Assert.assertEquals( securityDetail.getAuthorities().iterator().next() , SecurityDetail.AuthorityRole.ROLE_USER );
	}

	@Test(expected = SnapLogicalException.class)
	public void testRegisterTravellerTwice(){

		Traveller traveller = new Traveller.Builder()
					.username(TRAVELLER_USERNAME)
					.fullname(TRAVELLER_FULLNAME)
					.password(TRAVELLER_PASSWORD)
					.email(TRAVELLER_EMAIL).build();

		realSecurityService.registerTraveller(traveller);

		traveller = new Traveller.Builder()
					.username(TRAVELLER_USERNAME)
					.fullname(TRAVELLER_FULLNAME)
					.password(TRAVELLER_PASSWORD)
					.email(TRAVELLER_EMAIL).build();

		realSecurityService.registerTraveller(traveller);
	}


	@Test(expected = SnapLogicalException.class)
	public void testRegisterExistingTravellerUsername(){

		Traveller traveller = new Traveller.Builder()
					.username(DEFAULT_USERNAME)
					.fullname(TRAVELLER_FULLNAME)
					.password(TRAVELLER_PASSWORD)
					.email(TRAVELLER_EMAIL).build();

		realSecurityService.registerTraveller(traveller);

	}



	@Test
	public void testAddAuthority(){

		SecurityDetail securityDetail = realSecurityService.findLogin(DEFAULT_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertNotNull( securityDetail.getAuthorities() );
		Assert.assertTrue( securityDetail.getAuthorities().size() == 1 );
		Assert.assertEquals( securityDetail.getAuthorities().iterator().next() , SecurityDetail.AuthorityRole.ROLE_USER );

		realSecurityService.addAuthority(DEFAULT_USERNAME, SecurityDetail.AuthorityRole.ROLE_ADMIN );

		securityDetail = realSecurityService.findLogin(DEFAULT_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertNotNull( securityDetail.getAuthorities() );
		Assert.assertTrue( securityDetail.getAuthorities().size() == 2 );

		Iterator<SecurityDetail.AuthorityRole> iterator = securityDetail.getAuthorities().iterator();
		SecurityDetail.AuthorityRole role1 = iterator.next();
		SecurityDetail.AuthorityRole role2 = iterator.next();
		Assert.assertTrue( ( role1 == SecurityDetail.AuthorityRole.ROLE_USER && role2 == SecurityDetail.AuthorityRole.ROLE_ADMIN )
			|| ( role2 == SecurityDetail.AuthorityRole.ROLE_USER && role1 == SecurityDetail.AuthorityRole.ROLE_ADMIN ) );
	}

	@Test // (expected = SnapLogicalException.class) // TODO:
	public void testAddAuthorityTwice(){
		realSecurityService.addAuthority(DEFAULT_USERNAME, SecurityDetail.AuthorityRole.ROLE_ADMIN );
		realSecurityService.addAuthority(DEFAULT_USERNAME, SecurityDetail.AuthorityRole.ROLE_ADMIN );
	}

	@Test(expected = AssertionError.class)
	public void testAddNullAuthority(){
		realSecurityService.addAuthority(DEFAULT_USERNAME, null );
	}
	@Test // (expected = SnapLogicalException.class) // TODO:
	public void testAddExistingAuthority(){
		realSecurityService.addAuthority(DEFAULT_USERNAME, SecurityDetail.AuthorityRole.ROLE_USER );
	}

	@Test(expected = AssertionError.class)
	public void testAddAuthorityToNullTraveller(){
		realSecurityService.addAuthority(null, SecurityDetail.AuthorityRole.ROLE_ADMIN );
	}


	@Test(expected = SnapNotFoundException.class)
	public void testAddAuthorityToNonExistantTraveller(){
		realSecurityService.addAuthority("abrah", SecurityDetail.AuthorityRole.ROLE_ADMIN );
	}


	@Test
	public void testRemoveAuthority(){

		SecurityDetail securityDetail = realSecurityService.findLogin(DEFAULT_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertNotNull( securityDetail.getAuthorities() );
		Assert.assertTrue( securityDetail.getAuthorities().size() == 1 );
		Assert.assertEquals( securityDetail.getAuthorities().iterator().next() , SecurityDetail.AuthorityRole.ROLE_USER );

		realSecurityService.removeAuthority(DEFAULT_USERNAME, SecurityDetail.AuthorityRole.ROLE_USER );

		securityDetail = realSecurityService.findLogin(DEFAULT_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( securityDetail.getAuthorities() == null || securityDetail.getAuthorities().isEmpty() );
	}

	@Test // (expected = SnapLogicalException.class) // TODO:
	public void testRemoveAuthorityTwice(){
		realSecurityService.removeAuthority(DEFAULT_USERNAME, SecurityDetail.AuthorityRole.ROLE_USER );
		realSecurityService.removeAuthority(DEFAULT_USERNAME, SecurityDetail.AuthorityRole.ROLE_USER );
	}

	@Test(expected = AssertionError.class)
	public void testRemoveNullAuthority(){
		realSecurityService.removeAuthority(DEFAULT_USERNAME, null );
	}

	@Test // (expected = SnapLogicalException.class) // TODO:
	public void testRemoveNonExistentAuthority(){
		realSecurityService.removeAuthority(DEFAULT_USERNAME, SecurityDetail.AuthorityRole.ROLE_ADMIN );
	}


	@Test(expected = AssertionError.class)
	public void testRemoveAuthorityFromNullTraveller(){
		realSecurityService.removeAuthority(null, SecurityDetail.AuthorityRole.ROLE_USER );
	}


	@Test(expected = SnapLogicalException.class)
	public void testRemoveAuthorityFromNonExistentTraveller(){
		realSecurityService.removeAuthority("abrah", SecurityDetail.AuthorityRole.ROLE_USER );
	}


	@Test
	public void testEnableSecurityDetail(){

		realSecurityService.disableSecurityDetail(DEFAULT_USERNAME);

		SecurityDetail securityDetail = realSecurityService.findLogin(DEFAULT_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( !securityDetail.isEnabled() );

		realSecurityService.enableSecurityDetail(DEFAULT_USERNAME);

		securityDetail = realSecurityService.findLogin(DEFAULT_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( securityDetail.isEnabled() );
	}


	@Test
	public void testEnablingAlreadyEnabledSecurityDetail(){

		SecurityDetail securityDetail = realSecurityService.findLogin(DEFAULT_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( securityDetail.isEnabled() );

		realSecurityService.enableSecurityDetail(DEFAULT_USERNAME);

		securityDetail = realSecurityService.findLogin(DEFAULT_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( securityDetail.isEnabled() );
	}


	@Test(expected = SnapLogicalException.class)
	public void testEnableNonExistentSecurityDetail(){

		SecurityDetail securityDetail = realSecurityService.findLogin("asdasdsa");
		Assert.assertNull( securityDetail );

		realSecurityService.enableSecurityDetail("asdasdsa");
	}


	@Test(expected = AssertionError.class)
	public void testEnableNullSecurityDetail(){

		realSecurityService.enableSecurityDetail(null);

	}


	@Test
	public void testDisableSecurityDetail(){

		SecurityDetail securityDetail = realSecurityService.findLogin(DEFAULT_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( securityDetail.isEnabled() );

		realSecurityService.disableSecurityDetail(DEFAULT_USERNAME);

		securityDetail = realSecurityService.findLogin(DEFAULT_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( !securityDetail.isEnabled() );
	}


	@Test
	public void testDisablingAlreadyDisabledSecurityDetail(){

		realSecurityService.disableSecurityDetail(DEFAULT_USERNAME);
		
		SecurityDetail securityDetail = realSecurityService.findLogin(DEFAULT_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( !securityDetail.isEnabled() );

		realSecurityService.disableSecurityDetail(DEFAULT_USERNAME);

		securityDetail = realSecurityService.findLogin(DEFAULT_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertTrue( !securityDetail.isEnabled() );
	}


	@Test(expected = SnapLogicalException.class)
	public void testDisableNonExistentSecurityDetail(){


		SecurityDetail securityDetail = realSecurityService.findLogin("asdasdsa");
		Assert.assertNull( securityDetail );

		realSecurityService.disableSecurityDetail("asdasdsa");

	}


	@Test(expected = AssertionError.class)
	public void testDisableNullSecurityDetail(){

		realSecurityService.disableSecurityDetail(null);

	}


	@Test
	public void  testChangePassword(){

		SecurityDetail securityDetail = realSecurityService.findLogin(DEFAULT_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertEquals(securityDetail.getPassword(),
				passwordEncoder.encodePassword( DEFAULT_PASSWORD, DEFAULT_USERNAME ) );

		realSecurityService.changeSecurityDetailPassword(DEFAULT_USERNAME, "new password");

		securityDetail = realSecurityService.findLogin(DEFAULT_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertEquals(securityDetail.getPassword(),
				passwordEncoder.encodePassword( "new password", DEFAULT_USERNAME ) );

	}

	@Test(expected = AssertionError.class)
	public void  testChangeNullPassword(){

		realSecurityService.changeSecurityDetailPassword(DEFAULT_USERNAME, null);

	}


	@Test(expected = SnapInvalidClientInputException.class)
	public void  testChangeInvalidPassword(){

		realSecurityService.changeSecurityDetailPassword(DEFAULT_USERNAME, "");				

	}



	@Test
	public void testResetPasswordByEmail(){

		final String originalPassword = passwordEncoder.encodePassword(DEFAULT_PASSWORD,DEFAULT_USERNAME);
		SecurityDetail securityDetail = securityRepository.findSecurityDetailByEmail(DEFAULT_EMAIL);
		Assert.assertNotNull( securityDetail ); 
		Assert.assertEquals( securityDetail.getPassword(), originalPassword );

		ReflectionTestUtils.setField(realSecurityService, "passwordEncoder", mockPasswordEncoder );



		realSecurityService.resetPassword(DEFAULT_EMAIL);
		securityDetail = securityRepository.findSecurityDetailByEmail(DEFAULT_EMAIL);
		Assert.assertEquals( securityDetail.getPassword(), "RANDOM" );

		ReflectionTestUtils.setField(realSecurityService, "passwordEncoder", passwordEncoder );
	}


	@Test
	public void testResetPasswordByUsername(){

		final String originalPassword = passwordEncoder.encodePassword(DEFAULT_PASSWORD,DEFAULT_USERNAME);
		SecurityDetail securityDetail = securityRepository.findSecurityDetail(DEFAULT_USERNAME);
		Assert.assertNotNull( securityDetail );
		Assert.assertEquals( securityDetail.getPassword(), originalPassword );

		ReflectionTestUtils.setField(realSecurityService, "passwordEncoder", mockPasswordEncoder );

//		Mockito.when(mockPasswordEncoder.encodePassword(Mockito.anyString(),DEFAULT_USERNAME)).thenReturn("RANDOM");

		realSecurityService.resetPassword(DEFAULT_USERNAME);

		 securityDetail = securityRepository.findSecurityDetail(DEFAULT_USERNAME);
		Assert.assertEquals( securityDetail.getPassword(), "RANDOM" );

		ReflectionTestUtils.setField(realSecurityService, "passwordEncoder", passwordEncoder );
	}


	@Test(expected = SnapNotFoundException.class)
	public void testResetPasswordByNonExistentEmail(){
		realSecurityService.resetPassword("avava@asass.com");
	}


	@Test(expected = SnapNotFoundException.class)
	public void testResetPasswordByNonexistentUsername(){
		realSecurityService.resetPassword("avava");
	}


	@Test(expected = SnapLogicalException.class)
	public void testResetPasswordWhereUsernameAndEmailMatch(){
		
		Traveller traveller = new Traveller.Builder()
					.username("testusermatch")
					.fullname(DEFAULT_FULLNAME)
					.password(DEFAULT_PASSWORD)
					.email("testusermatch@example.org").build();

		realSecurityService.registerTraveller(traveller);

		traveller = new Traveller.Builder()
					.username("testusermatch@example.org")
					.fullname(DEFAULT_FULLNAME)
					.password(DEFAULT_PASSWORD)
					.email("anothertestusermatch@example.org").build();

		realSecurityService.registerTraveller(traveller);

		realSecurityService.resetPassword("testusermatch@example.org");
	}


//	public void enforceAristocracy();
	
*/
}
