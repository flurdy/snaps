package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.SnapLogicalException;
import com.flurdy.grid.snaps.exception.SnapTechnicalException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class EmailServiceTest extends AbstractServiceTest {

	private static final String TEST_EMAIL = "ivar+snaps@localhost.localdomain";
	private long defaultTravellerId;

	@Before
	public void setUp(){
		Mockito.when(securityService.findLoggedInUsername()).thenReturn(DEFAULT_USERNAME);

		Traveller traveller = new Traveller.Builder()
					.username(DEFAULT_USERNAME)
					.fullname(DEFAULT_FULLNAME)
					.password(DEFAULT_PASSWORD)
					.email(TEST_EMAIL).build();

		realSecurityService.registerTraveller(traveller);
		defaultTravellerId = traveller.getTravellerId();
	}


	@Test
	public void testEmailPassword(){
		Traveller traveller = travellerService.findTraveller(defaultTravellerId);
		emailService.sendPassword(traveller,"testEmailPassword");
	}


	@Test(expected = AssertionError.class)
	public void testEmailPasswordNullTraveller(){
		Traveller traveller = travellerService.findTraveller(defaultTravellerId);
		emailService.sendPassword(null,"testEmailPasswordNullTraveller");
	}


	@Test(expected = SnapTechnicalException.class)
	public void testEmailPasswordMissingEmail(){
		Traveller traveller = new Traveller.Builder()
					.username(DEFAULT_USERNAME)
					.fullname(DEFAULT_FULLNAME)
					.password(DEFAULT_PASSWORD)
					.build();
		emailService.sendPassword(traveller,"testEmailPasswordInvalidTraveller");
	}


	@Test(expected = SnapTechnicalException.class)
	public void testEmailPasswordInvalidTraveller2(){
		Traveller traveller = new Traveller.Builder()
					.fullname(DEFAULT_FULLNAME)
					.password(DEFAULT_PASSWORD)
					.email(TEST_EMAIL).build();
		emailService.sendPassword(traveller,"testEmailPasswordInvalidTraveller");
	}


	@Test(expected = SnapTechnicalException.class)
	public void testEmailPasswordInvalidPassword(){
		Traveller traveller = travellerService.findTraveller(defaultTravellerId);
		emailService.sendPassword(traveller,"");
	}


	@Test(expected = AssertionError.class)
	public void testEmailPasswordNullPassword(){
		Traveller traveller = travellerService.findTraveller(defaultTravellerId);
		emailService.sendPassword(traveller,null);
	}






}
