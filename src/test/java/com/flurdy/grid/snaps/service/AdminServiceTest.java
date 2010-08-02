package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.IHolidayGroupRepository;
import com.flurdy.grid.snaps.dao.ITravellerRepository;
import com.flurdy.grid.snaps.domain.HolidayGroup;
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

	@Mock
	private IHolidayGroupRepository holidayGroupRepository;

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

		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(generateDefaultTraveller());
		Mockito.when(travellerRepository.findTraveller(new Long(1))).thenReturn(generateDefaultRegisteredTraveller());

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

		Mockito.verify(travellerService).findCurrentTraveller();
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
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(generateDefaultTraveller());
		Mockito.when(travellerRepository.findTraveller(new Long(1))).thenReturn(generateDefaultRegisteredTraveller());

		adminService.deleteTraveller(new Long(1));

		Mockito.verify(travellerService).findCurrentTraveller();
		Mockito.verify(travellerRepository).findTraveller(new Long(1));
		Mockito.verify(travellerRepository).deleteTraveller(Mockito.<Traveller>anyObject());
		Mockito.verifyNoMoreInteractions(travellerRepository);
		Mockito.verifyZeroInteractions(travellerService);
	}


	@Test(expected = SnapNotFoundException.class)
	public void testDeleteTravellerTwice(){

		Mockito.when(travellerRepository.findTraveller(new Long(1)))
				.thenReturn(generateDefaultRegisteredTraveller())
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


	@Test
	public void testUpdateHoliday(){

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(generateDefaultTraveller());

		Mockito.when(holidayGroupRepository.findHolidayGroup(new Long(1)))
				.thenReturn(generateDefaultHoliday());

		HolidayGroup holidayGroup = generateDefaultHoliday();
		holidayGroup.setGroupName("asasasasas");
		holidayGroup.setGroupId(new Long(1));

		adminService.updateHolidayGroup(holidayGroup);

		Mockito.verify(travellerService, Mockito.times(1)).findCurrentTraveller();
		Mockito.verify(holidayGroupRepository, Mockito.times(1)).findHolidayGroup(Mockito.anyLong());
		Mockito.verify(holidayGroupRepository, Mockito.times(1)).updateHolidayGroup(Mockito.<HolidayGroup>anyObject());
		Mockito.verifyNoMoreInteractions(travellerService,holidayGroupRepository);
	}


	@Test(expected = SnapLogicalException.class)
	public void testUpdateHolidayWithNullName(){

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(generateDefaultTraveller());

		Mockito.when(holidayGroupRepository.findHolidayGroup(new Long(1)))
				.thenReturn(generateDefaultHoliday());

		HolidayGroup holidayGroup = generateDefaultHoliday();
		holidayGroup.setGroupName(null);

		adminService.updateHolidayGroup(holidayGroup);
	}


	@Test(expected = SnapLogicalException.class)
	public void testUpdateHolidayWithInvalidName(){

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(generateDefaultTraveller());

		Mockito.when(holidayGroupRepository.findHolidayGroup(new Long(1)))
				.thenReturn(generateDefaultHoliday());

		HolidayGroup holidayGroup = generateDefaultHoliday();
		holidayGroup.setGroupName("");

		adminService.updateHolidayGroup(holidayGroup);
	}



	@Test(expected = SnapLogicalException.class)
	public void testUpdateNonExistentHoliday(){

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(generateDefaultTraveller());

		Mockito.when(holidayGroupRepository.findHolidayGroup(new Long(1)))
				.thenReturn(null);

		HolidayGroup holidayGroup = generateDefaultHoliday();
		holidayGroup.setGroupName("asasassa");

		adminService.updateHolidayGroup(holidayGroup);
	}


	@Test
	public void testDeleteHoliday(){

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(generateDefaultTraveller());

		Mockito.when(holidayGroupRepository.findHolidayGroup(new Long(1)))
				.thenReturn(generateDefaultHoliday());


		adminService.deleteHolidayGroup(1);


		Mockito.verify(travellerService, Mockito.times(1)).findCurrentTraveller();
		Mockito.verify(holidayGroupRepository, Mockito.times(1)).findHolidayGroup(Mockito.anyLong());
		Mockito.verify(holidayGroupRepository, Mockito.times(1)).deleteHolidayGroup(Mockito.<HolidayGroup>anyObject());
		Mockito.verifyNoMoreInteractions(travellerService,holidayGroupRepository);
	}


	@Test // idempotent
	public void testDeleteNonExistentHoliday(){

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(generateDefaultTraveller());

		Mockito.when(holidayGroupRepository.findHolidayGroup(new Long(1)))
				.thenReturn(null);


		adminService.deleteHolidayGroup(1);
	}


	@Test(expected = SnapTechnicalException.class)
	public void testDeleteInvalidHoliday(){

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(generateDefaultTraveller());

		adminService.deleteHolidayGroup(-1);		
	}



}
