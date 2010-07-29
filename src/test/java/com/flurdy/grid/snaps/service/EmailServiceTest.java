package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.SecurityDetail;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.SnapLogicalException;
import com.flurdy.grid.snaps.exception.SnapTechnicalException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.annotation.Resource;


public class EmailServiceTest extends AbstractServiceTest {

	private static final String TEST_EMAIL = "ivar+snaps@localhost.localdomain";
	private long defaultTravellerId;

	@Resource
	private IEmailService realEmailService;

	@Before
	public void setUp(){
		super.setUp();
		Traveller traveller = new Traveller.Builder()
					.username(DEFAULT_USERNAME)
					.fullname(DEFAULT_FULLNAME)
					.password(DEFAULT_PASSWORD)
					.email(TEST_EMAIL).build();

		realSecurityService.registerTraveller(traveller);
		defaultTravellerId = traveller.getTravellerId();

		traveller = travellerService.findTraveller(defaultTravellerId);
		Assert.assertNotNull(traveller);
		Assert.assertNotNull(traveller.getSecurityDetail().getAuthorities().contains(SecurityDetail.AuthorityRole.ROLE_USER));
		Assert.assertTrue(traveller.getSecurityDetail().hasAuthority(SecurityDetail.AuthorityRole.ROLE_USER));

		realSecurityService.addAuthority(DEFAULT_USERNAME, SecurityDetail.AuthorityRole.ROLE_ADMIN );
		traveller = travellerService.findTraveller(defaultTravellerId);
		Assert.assertNotNull(traveller);
		Assert.assertNotNull(traveller.getSecurityDetail().getAuthorities().contains(SecurityDetail.AuthorityRole.ROLE_USER));
		Assert.assertNotNull(traveller.getSecurityDetail().getAuthorities().contains(SecurityDetail.AuthorityRole.ROLE_ADMIN));
		Assert.assertTrue(traveller.getSecurityDetail().hasAuthority(SecurityDetail.AuthorityRole.ROLE_USER));
		Assert.assertTrue(traveller.getSecurityDetail().hasAuthority(SecurityDetail.AuthorityRole.ROLE_ADMIN));
	}


	@Test
	public void testEmailPassword(){
		Traveller traveller = travellerService.findTraveller(defaultTravellerId);
		Assert.assertNotNull(traveller);
		Assert.assertNotNull(traveller.getSecurityDetail().getAuthorities().contains(SecurityDetail.AuthorityRole.ROLE_USER));
		Assert.assertNotNull(traveller.getSecurityDetail().getAuthorities().contains(SecurityDetail.AuthorityRole.ROLE_ADMIN));
		Assert.assertTrue(traveller.getSecurityDetail().hasAuthority(SecurityDetail.AuthorityRole.ROLE_USER));
		Assert.assertTrue(traveller.getSecurityDetail().hasAuthority(SecurityDetail.AuthorityRole.ROLE_ADMIN));
		realEmailService.sendPassword(traveller,"testEmailPassword");
	}


	@Test(expected = AssertionError.class)
	public void testEmailPasswordNullTraveller(){
		Traveller traveller = travellerService.findTraveller(defaultTravellerId);
		realEmailService.sendPassword(null,"testEmailPasswordNullTraveller");
	}


	@Test(expected = SnapTechnicalException.class)
	public void testEmailPasswordMissingEmail(){
		Traveller traveller = new Traveller.Builder()
					.username(DEFAULT_USERNAME)
					.fullname(DEFAULT_FULLNAME)
					.password(DEFAULT_PASSWORD)
					.build();
		realEmailService.sendPassword(traveller,"testEmailPasswordInvalidTraveller");
	}


	@Test(expected = SnapTechnicalException.class)
	public void testEmailPasswordInvalidTraveller2(){
		Traveller traveller = new Traveller.Builder()
					.fullname(DEFAULT_FULLNAME)
					.password(DEFAULT_PASSWORD)
					.email(TEST_EMAIL).build();
		realEmailService.sendPassword(traveller,"testEmailPasswordInvalidTraveller");
	}


	@Test(expected = SnapTechnicalException.class)
	public void testEmailPasswordInvalidPassword(){
		Traveller traveller = travellerService.findTraveller(defaultTravellerId);
		realEmailService.sendPassword(traveller,"");
	}


	@Test(expected = AssertionError.class)
	public void testEmailPasswordNullPassword(){
		Traveller traveller = travellerService.findTraveller(defaultTravellerId);
		realEmailService.sendPassword(traveller,null);
	}


	@Test
	public void testNotifyNewRegistration(){
		Traveller traveller = travellerService.findTraveller(defaultTravellerId);
		Assert.assertNotNull(traveller.getSecurityDetail().getAuthorities().contains(SecurityDetail.AuthorityRole.ROLE_USER));
		Assert.assertNotNull(traveller.getSecurityDetail().getAuthorities().contains(SecurityDetail.AuthorityRole.ROLE_ADMIN));
		Assert.assertTrue(traveller.getSecurityDetail().hasAuthority(SecurityDetail.AuthorityRole.ROLE_USER));
		Assert.assertTrue(traveller.getSecurityDetail().hasAuthority(SecurityDetail.AuthorityRole.ROLE_ADMIN));
		realEmailService.notifyNewRegistration(traveller);
	}


	@Test(expected = SnapLogicalException.class)
	public void testNotifyNewRegistrationNoAdmins(){
		Traveller traveller = travellerService.findTraveller(defaultTravellerId);
		Assert.assertNotNull(traveller.getSecurityDetail().getAuthorities().contains(SecurityDetail.AuthorityRole.ROLE_USER));
		Assert.assertNotNull(traveller.getSecurityDetail().getAuthorities().contains(SecurityDetail.AuthorityRole.ROLE_ADMIN));
		Assert.assertTrue(traveller.getSecurityDetail().hasAuthority(SecurityDetail.AuthorityRole.ROLE_USER));
		Assert.assertTrue(traveller.getSecurityDetail().hasAuthority(SecurityDetail.AuthorityRole.ROLE_ADMIN));
		realSecurityService.removeAuthority(DEFAULT_USERNAME, SecurityDetail.AuthorityRole.ROLE_ADMIN );
		traveller = travellerService.findTraveller(defaultTravellerId);
		Assert.assertNotNull(traveller.getSecurityDetail().getAuthorities().contains(SecurityDetail.AuthorityRole.ROLE_USER));
		Assert.assertFalse(traveller.getSecurityDetail().getAuthorities().contains(SecurityDetail.AuthorityRole.ROLE_ADMIN));
		Assert.assertTrue(traveller.getSecurityDetail().hasAuthority(SecurityDetail.AuthorityRole.ROLE_USER));
		Assert.assertFalse(traveller.getSecurityDetail().hasAuthority(SecurityDetail.AuthorityRole.ROLE_ADMIN));
		realEmailService.notifyNewRegistration(traveller);
	}


	@Test(expected = AssertionError.class)
	public void testNotifyNewRegistrationNullTraveller(){
		realEmailService.notifyNewRegistration(null);
	}


}
