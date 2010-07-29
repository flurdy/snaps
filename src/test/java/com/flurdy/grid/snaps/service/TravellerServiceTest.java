package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.Traveller;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class TravellerServiceTest extends AbstractServiceTest {

	private Long defaultTravellerId = null;

	@Before
	public void setUp(){
		super.setUp();
		Mockito.when(securityService.findLoggedInUsername()).thenReturn(DEFAULT_USERNAME);
		defaultTravellerId = addDefaultUser();
	}
	

	@Test
	public void testFindTraveller(){

		assert defaultTravellerId > 0;

		Traveller traveller = travellerService.findTraveller(defaultTravellerId);

		Assert.assertNotNull( traveller );
		Assert.assertEquals( traveller.getFullname(), DEFAULT_FULLNAME );
	}



	@Test(expected = AssertionError.class)
	public void testFindInvalidTraveller(){

		Traveller traveller = travellerService.findTraveller(-1);

	}



	@Test
	public void testFindNonExistentTraveller(){

		Traveller traveller = travellerService.findTraveller(999);

		Assert.assertNull( traveller );

	}
	

	@Test
	public void testFindAllTravellers(){
		List<Traveller> travellers = travellerService.findTravellers();
		Assert.assertTrue( travellers.size() == 1 );
	}

}
