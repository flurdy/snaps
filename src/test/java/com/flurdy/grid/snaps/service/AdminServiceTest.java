package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.ITravellerRepository;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.SnapLogicalException;
import com.flurdy.grid.snaps.exception.SnapNotFoundException;
import com.flurdy.grid.snaps.exception.SnapTechnicalException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AdminServiceTest extends AbstractServiceTest {


	@Mock
	private ITravellerRepository travellerRepository;

	@Mock
	private ITravellerService travellerService;

	@InjectMocks
	private IAdminService adminService = new AdminService();

	private static final String TRAVELLER_FULLNAME = "New testuser";
	private static final String TRAVELLER_EMAIL = "new.testuser@example.com";

	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);	
	}


	@Test
	public void testUpdateTraveller(){

		Mockito.when(travellerRepository.findTraveller(new Long(1))).thenReturn(generateDefaultRegisterdTraveller());

		Traveller traveller = new Traveller.Builder()
				.fullname(TRAVELLER_FULLNAME)
				.email(TRAVELLER_EMAIL)
				.username(DEFAULT_USERNAME)
				.password(DEFAULT_PASSWORD)
				.travellerId(new Long(1))
				.build();

		adminService.updateTraveller(traveller);

//		traveller = travellerService.findTraveller(new Long(1));
//
//		Assert.assertNotNull( traveller );
//		Assert.assertEquals( traveller.getFullname(), TRAVELLER_FULLNAME );
//		Assert.assertEquals( traveller.getEmail(), TRAVELLER_EMAIL );

		Mockito.verify(travellerRepository).findTraveller(new Long(1));
		Mockito.verify(travellerRepository).updateTraveller(Mockito.<Traveller>anyObject());
		Mockito.verifyNoMoreInteractions(travellerRepository);
		Mockito.verifyZeroInteractions(travellerService);
	}





	@Test(expected = SnapLogicalException.class)
	public void testUpdateTravellerInvalidFullname(){

		Traveller traveller = new Traveller.Builder()
				.fullname("")
				.email(TRAVELLER_EMAIL)
				.username(DEFAULT_USERNAME)
				.password(DEFAULT_PASSWORD)
				.travellerId(new Long(1))
				.build();

		adminService.updateTraveller(traveller);

	}

	@Test(expected = SnapLogicalException.class)
	public void testUpdateTravellerNullFullname(){
		Traveller traveller = new Traveller.Builder()
				.fullname(null)
				.email(TRAVELLER_EMAIL)
				.username(DEFAULT_USERNAME)
				.password(DEFAULT_PASSWORD)
				.travellerId(new Long(1))
				.build();

		adminService.updateTraveller(traveller);

	}


	@Test(expected = SnapNotFoundException.class)
	public void testUpdateNonExistentTraveller(){

		Mockito.when(travellerRepository.findTraveller(new Long(999))).thenReturn(null);


		Traveller traveller = new Traveller.Builder()
				.fullname(TRAVELLER_FULLNAME)
				.email(TRAVELLER_EMAIL)
				.username(DEFAULT_USERNAME)
				.password(DEFAULT_PASSWORD)
				.travellerId(new Long(999))
				.build();

		adminService.updateTraveller(traveller);

	}


	@Test(expected = SnapTechnicalException.class)
	public void testUpdateNullTraveller(){

		adminService.updateTraveller(null);

	}



	@Test
	public void testDeleteTraveller(){

		Mockito.when(travellerRepository.findTraveller(new Long(1))).thenReturn(generateDefaultRegisterdTraveller());

		adminService.deleteTraveller(new Long(1));

		Mockito.verify(travellerRepository).findTraveller(new Long(1));
		Mockito.verify(travellerRepository).deleteTraveller(Mockito.<Traveller>anyObject());
		Mockito.verifyNoMoreInteractions(travellerRepository);
		Mockito.verifyZeroInteractions(travellerService);
	}


	@Test(expected = SnapNotFoundException.class)
	public void testDeleteTravellerTwice(){

		Mockito.when(travellerRepository.findTraveller(new Long(1)))
				.thenReturn(generateDefaultRegisterdTraveller())
				.thenReturn(null);


		adminService.deleteTraveller(new Long(1));
		adminService.deleteTraveller(new Long(1));

	}


	@Test(expected = SnapNotFoundException.class)
	public void testDeleteNonExistentTraveller(){


		Mockito.when(travellerRepository.findTraveller(new Long(1)))
				.thenReturn(null);

		adminService.deleteTraveller(999);

	}


	@Test(expected = AssertionError.class)
	public void testDeleteInvalidTraveller(){

		adminService.deleteTraveller(-1);

	}


}
