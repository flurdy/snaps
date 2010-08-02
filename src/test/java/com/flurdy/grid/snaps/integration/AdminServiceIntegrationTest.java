package com.flurdy.grid.snaps.integration;

import com.flurdy.grid.snaps.dao.IHolidayGroupRepository;
import com.flurdy.grid.snaps.dao.ISecurityRepository;
import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.HolidayMember;
import com.flurdy.grid.snaps.domain.SecurityDetail;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.service.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import java.util.HashSet;


public class AdminServiceIntegrationTest  extends AbstractServiceIntegrationTest {

	@Mock
	private IEmailService emailService;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	@Autowired
	private ISecurityRepository securityRepository;

	@InjectMocks
	@Autowired
	private IAdminService adminService;

	@InjectMocks
	@Autowired
	private IHolidayGroupService holidayGroupService;

	@InjectMocks
	@Autowired
	private IHolidayGroupRepository holidayGroupRepository;

	@InjectMocks
	@Autowired
	private ISecurityService securityService;

	@Mock
	private ITravellerService travellerService;

	@InjectMocks
	@Autowired
	private ITravellerService realTravellerService;


	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
	}

	private static final String TRAVELLER_FULLNAME = "New testuser";
	private static final String TRAVELLER_EMAIL = "new.testuser@example.com";


	private Long registerDefaultTraveller(){
		Mockito.when(passwordEncoder.encodePassword(Mockito.anyString(),Mockito.<Object>anyObject())).thenReturn(ENCODED_PASSWORD);
		Traveller traveller = generateDefaultTraveller();
		securityService.registerTraveller(traveller);
		long defaultTravellerId = traveller.getTravellerId();
		Assert.assertTrue( defaultTravellerId > 0 );
		Mockito.verify(emailService).notifyNewRegistration(Mockito.<Traveller>anyObject());
		Mockito.verify(passwordEncoder).encodePassword(Mockito.anyString(),Mockito.<Object>anyObject());
		Mockito.verifyNoMoreInteractions(emailService,passwordEncoder);
		return defaultTravellerId;
	}

	private Long registerAdminTraveller(){
		Mockito.when(passwordEncoder.encodePassword(Mockito.anyString(),Mockito.<Object>anyObject())).thenReturn(ENCODED_PASSWORD);
		Traveller traveller = generateAdminTraveller();
		securityService.registerTraveller(traveller);
		long travellerId = traveller.getTravellerId();
		Assert.assertTrue( travellerId > 0 );
		Mockito.verify(emailService).notifyNewRegistration(Mockito.<Traveller>anyObject());
		Mockito.verify(passwordEncoder).encodePassword(Mockito.anyString(),Mockito.<Object>anyObject());
		Mockito.verifyNoMoreInteractions(emailService,passwordEncoder);
		return travellerId;
	}

	private void registerAdminTravellerAndEscalateToAdmin(){

		registerAdminTraveller();
		securityService.addAuthority(ADMIN_USERNAME, SecurityDetail.AuthorityRole.ROLE_ADMIN );

		Mockito.verifyNoMoreInteractions(emailService,passwordEncoder);
	}


	private HolidayGroup generateDefaultHoliday(){
		final HolidayGroup defaultHoliday = new HolidayGroup.Builder()
					.groupName(DEFAULT_HOLIDAY_NAME)
					.members(new HashSet<HolidayMember>())
					.build();
		defaultHoliday.addMember(generateDefaultTraveller());
		return defaultHoliday;
	}


	private Long addDefaultHoliday(){
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(generateDefaultTraveller());
		final HolidayGroup defaultHoliday = generateDefaultHoliday();
		holidayGroupService.addHolidayGroup(defaultHoliday);
		long defaultHolidayId = defaultHoliday.getGroupId();
		Assert.assertTrue( defaultHolidayId > 0 );
		Mockito.verify(travellerService).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verifyZeroInteractions(emailService);
		return defaultHolidayId;
	}


//	private Traveller findDefaultTraveller(long defaultTravellerId){
//		return travellerService.findTraveller(defaultTravellerId);
//	}

	@Test
	public void testUpdateTraveller(){

		long defaultTravellerId = registerDefaultTraveller();

		assert defaultTravellerId > 0;

		Assert.assertNotNull(  securityService.findLogin(DEFAULT_USERNAME) );

		Traveller traveller = realTravellerService.findTraveller(defaultTravellerId);

		Assert.assertNotNull( 	traveller );

		traveller = new Traveller.Builder()
				.fullname(TRAVELLER_FULLNAME)
				.email(TRAVELLER_EMAIL)
				.username(DEFAULT_USERNAME)
				.password(ENCODED_PASSWORD)
				.travellerId(defaultTravellerId)
				.build();


		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(realTravellerService.findTraveller(defaultTravellerId))
				.thenReturn(realTravellerService.findTraveller(defaultTravellerId));

		adminService.updateTraveller(traveller);

		traveller = realTravellerService.findTraveller(defaultTravellerId);

		Assert.assertNotNull( traveller );
		Assert.assertEquals( traveller.getFullname(), TRAVELLER_FULLNAME );
		Assert.assertEquals( traveller.getEmail(), TRAVELLER_EMAIL );

		Mockito.verify(travellerService, Mockito.times(1)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verifyZeroInteractions(emailService,passwordEncoder);
	}



	@Test
	public void testDeleteTraveller(){

		long defaultTravellerId = registerDefaultTraveller();

		Traveller traveller = realTravellerService.findTraveller(defaultTravellerId);

		Assert.assertNotNull( 	traveller );

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(realTravellerService.findTraveller(defaultTravellerId))
				.thenReturn(realTravellerService.findTraveller(defaultTravellerId));

		adminService.deleteTraveller(defaultTravellerId);

		 traveller = realTravellerService.findTraveller(defaultTravellerId);

		Assert.assertNull( traveller );

		Mockito.verify(travellerService, Mockito.times(1)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verifyZeroInteractions(emailService,passwordEncoder);
	}


	@Test
	public void testUpdateHoliday(){
		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(generateDefaultTraveller())
				.thenReturn(generateDefaultTraveller())
				.thenReturn(generateDefaultTraveller());

		long holidayId = addDefaultHoliday();
		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(holidayId);
		Assert.assertNotNull(holidayGroup);
		Assert.assertTrue(!holidayGroup.getGroupName().equals("asasasasas"));

		holidayGroup.setGroupName("asasasasas");

		adminService.updateHolidayGroup(holidayGroup);

		holidayGroup = holidayGroupService.findHolidayGroup(holidayId);

		Assert.assertNotNull(holidayGroup);
		Assert.assertTrue(holidayGroup.getGroupName().equals("asasasasas"));
		Mockito.verify(travellerService, Mockito.times(4)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verifyZeroInteractions(emailService,passwordEncoder);
	}

	@Test
	public void testDeleteHoliday(){

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(generateDefaultTraveller())
				.thenReturn(generateDefaultTraveller())
				.thenReturn(generateDefaultTraveller());

		long holidayId = addDefaultHoliday();
		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(holidayId);
		Assert.assertNotNull(holidayGroup);


		adminService.deleteHolidayGroup(holidayId);

		holidayGroup = holidayGroupService.findHolidayGroup(holidayId);

		Assert.assertNull(holidayGroup);
		Mockito.verify(travellerService, Mockito.times(3)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verifyZeroInteractions(emailService,passwordEncoder);
	}

	
}
