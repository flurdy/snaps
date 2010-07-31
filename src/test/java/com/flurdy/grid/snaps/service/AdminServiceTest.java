package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.SnapLogicalException;
import com.flurdy.grid.snaps.exception.SnapNotFoundException;
import com.flurdy.grid.snaps.exception.SnapTechnicalException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AdminServiceTest extends AbstractServiceTest {
/*
	private static final String TRAVELLER_FULLNAME = "New testuser";
	private static final String TRAVELLER_EMAIL = "new.testuser@example.com";

	private Long defaultTravellerId = null;

	@Before
	public void setUp(){
		super.setUp();
		Mockito.reset(securityService);
		Mockito.when(securityService.findLoggedInUsername()).thenReturn(DEFAULT_USERNAME2).thenReturn(DEFAULT_USERNAME);
		addDefaultUser2();
		defaultTravellerId = addDefaultUser();
		assert defaultTravellerId > 0;
	}

	@Test
	public void testUpdateTraveller(){
		Traveller traveller = travellerService.findTraveller(defaultTravellerId);
		Assert.assertNotNull( traveller );
		Assert.assertEquals( traveller.getFullname(), DEFAULT_FULLNAME );
		Assert.assertEquals( traveller.getEmail(), DEFAULT_EMAIL );

		Traveller updateTraveller = traveller.clone();
		updateTraveller.setFullname(TRAVELLER_FULLNAME);
		updateTraveller.setEmail(TRAVELLER_EMAIL);

		adminService.updateTraveller(updateTraveller);

		traveller = travellerService.findTraveller(defaultTravellerId);

		Assert.assertNotNull( traveller );
		Assert.assertEquals( traveller.getFullname(), TRAVELLER_FULLNAME );
		Assert.assertEquals( traveller.getEmail(), TRAVELLER_EMAIL );
	}


	@Test(expected = SnapLogicalException.class)
	public void testUpdateTravellerInvalidFullname(){
		Traveller traveller = travellerService.findTraveller(defaultTravellerId);
		Assert.assertNotNull( traveller );
		Assert.assertEquals( traveller.getFullname(), DEFAULT_FULLNAME );
		Assert.assertEquals( traveller.getEmail(), DEFAULT_EMAIL );

		Traveller updateTraveller = traveller.clone();
		updateTraveller.setFullname("");
		updateTraveller.setEmail(TRAVELLER_EMAIL);

		adminService.updateTraveller(updateTraveller);

		traveller = travellerService.findTraveller(defaultTravellerId);

		Assert.assertNotNull( traveller );
		Assert.assertEquals( traveller.getFullname(), TRAVELLER_FULLNAME );
		Assert.assertEquals( traveller.getEmail(), TRAVELLER_EMAIL );
	}

	@Test(expected = SnapLogicalException.class)
	public void testUpdateTravellerNullFullname(){
		Traveller traveller = travellerService.findTraveller(defaultTravellerId);
		Assert.assertNotNull( traveller );
		Assert.assertEquals( traveller.getFullname(), DEFAULT_FULLNAME );
		Assert.assertEquals( traveller.getEmail(), DEFAULT_EMAIL );

		Traveller updateTraveller = traveller.clone();
		updateTraveller.setFullname(null);
		updateTraveller.setEmail(TRAVELLER_EMAIL);

		adminService.updateTraveller(updateTraveller);

		traveller = travellerService.findTraveller(defaultTravellerId);

		Assert.assertNotNull( traveller );
		Assert.assertEquals( traveller.getFullname(), TRAVELLER_FULLNAME );
		Assert.assertEquals( traveller.getEmail(), TRAVELLER_EMAIL );
	}


	@Test(expected = SnapNotFoundException.class)
	public void testUpdateNonExistentTraveller(){
		Traveller traveller = travellerService.findTraveller(defaultTravellerId);
		Assert.assertNotNull( traveller );
		Assert.assertEquals( traveller.getFullname(), DEFAULT_FULLNAME );
		Assert.assertEquals( traveller.getEmail(), DEFAULT_EMAIL );

		Traveller updateTraveller = traveller.clone();
		updateTraveller.setFullname(TRAVELLER_FULLNAME);
		updateTraveller.setEmail(TRAVELLER_EMAIL);
		updateTraveller.setTravellerId(new Long(999));

		adminService.updateTraveller(updateTraveller);

	}


	@Test(expected = SnapTechnicalException.class)
	public void testUpdateNullTraveller(){

		adminService.updateTraveller(null);

	}



	@Test
	public void testDeleteTraveller(){

		Traveller traveller = travellerService.findTraveller(defaultTravellerId);
		Assert.assertNotNull( traveller );

		adminService.deleteTraveller(defaultTravellerId);

		traveller = travellerService.findTraveller(defaultTravellerId);
		Assert.assertNull( "Traveller not deleted! " +traveller, traveller );
	}

	@Test(expected = SnapNotFoundException.class)
	public void testDeleteTravellerTwice(){

		Traveller traveller = travellerService.findTraveller(defaultTravellerId);
		Assert.assertNotNull( traveller );

		adminService.deleteTraveller(defaultTravellerId);

		traveller = travellerService.findTraveller(defaultTravellerId);
		Assert.assertNull( "Traveller not deleted! " +traveller, traveller );

		adminService.deleteTraveller(defaultTravellerId);
	}


	@Test(expected = SnapNotFoundException.class)
	public void testDeleteNonExistentTraveller(){

		adminService.deleteTraveller(999);

	}


	@Test(expected = AssertionError.class)
	public void testDeleteInvalidTraveller(){

		adminService.deleteTraveller(-1);

	}


*/
}
