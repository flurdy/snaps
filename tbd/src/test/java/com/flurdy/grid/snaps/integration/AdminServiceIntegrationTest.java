package com.flurdy.grid.snaps.integration;

import com.flurdy.grid.snaps.dao.IHolidayGroupRepository;
import com.flurdy.grid.snaps.dao.ISecurityRepository;
import com.flurdy.grid.snaps.domain.*;
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
	private IPhotoAlbumService photoAlbumService;

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


	private static final PhotoSharingProvider DEFAULT_PROVIDER = PhotoSharingProvider.FLICKR;
	private static final String DEFAULT_PHOTOALBUM_URL = "http://www.flickr.com/photos/flurdy/sets/72157624009834665/";


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


	private HolidayGroup generateDefaultUnregisteredHoliday(){
		final HolidayGroup defaultHoliday = new HolidayGroup.Builder()
					.groupName(DEFAULT_HOLIDAY_NAME)
					.members(new HashSet<HolidayMember>())
					.build();
		return defaultHoliday;
	}


	private Long addDefaultHoliday(long travellerId){
		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(realTravellerService.findTraveller(travellerId))
				.thenReturn(realTravellerService.findTraveller(travellerId));
		final HolidayGroup defaultHoliday = generateDefaultUnregisteredHoliday();

		holidayGroupService.addHolidayGroup(defaultHoliday);

		long holidayId = defaultHoliday.getGroupId();
		Assert.assertTrue( holidayId > 0 );
		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(holidayId);
		Assert.assertNotNull(holidayGroup);
		Assert.assertTrue(holidayGroup.isMember(realTravellerService.findTraveller(travellerId)));
		Mockito.verify(travellerService, Mockito.times(2)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verifyZeroInteractions(emailService);
		return holidayId;
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
		long defaultTravellerId = registerDefaultTraveller();

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(realTravellerService.findTraveller(defaultTravellerId))
				.thenReturn(realTravellerService.findTraveller(defaultTravellerId))
				.thenReturn(realTravellerService.findTraveller(defaultTravellerId));

		long holidayId = addDefaultHoliday(defaultTravellerId);
		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(holidayId);
		Assert.assertNotNull(holidayGroup);
		Assert.assertTrue(!holidayGroup.getGroupName().equals("asasasasas"));

		holidayGroup.setGroupName("asasasasas");

		adminService.updateHolidayGroup(holidayGroup);

		holidayGroup = holidayGroupService.findHolidayGroup(holidayId);

		Assert.assertNotNull(holidayGroup);
		Assert.assertTrue(holidayGroup.getGroupName().equals("asasasasas"));
		Mockito.verify(travellerService, Mockito.times(5)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verifyZeroInteractions(emailService,passwordEncoder);
	}

	@Test
	public void testDeleteHoliday(){

		long defaultTravellerId = registerDefaultTraveller();

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(realTravellerService.findTraveller(defaultTravellerId))
				.thenReturn(realTravellerService.findTraveller(defaultTravellerId))
				.thenReturn(realTravellerService.findTraveller(defaultTravellerId));

		long holidayId = addDefaultHoliday(defaultTravellerId);
		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(holidayId);
		Assert.assertNotNull(holidayGroup);


		adminService.deleteHolidayGroup(holidayId);

		holidayGroup = holidayGroupService.findHolidayGroup(holidayId);

		Assert.assertNull(holidayGroup);
		Mockito.verify(travellerService, Mockito.times(4)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verifyZeroInteractions(emailService,passwordEncoder);
	}




	@Test
	public void testDeletePhotoAlbum(){

		long travellerId = registerDefaultTraveller();
		assert travellerId > 0;
		Mockito.verifyZeroInteractions(travellerService,emailService,passwordEncoder);

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(realTravellerService.findTraveller(travellerId));

		long holidayId = addDefaultHoliday(travellerId);
		assert holidayId > 0;
		Mockito.verifyNoMoreInteractions(travellerService);

		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(holidayId);
		Assert.assertNotNull(holidayGroup);
		Assert.assertTrue(holidayGroup.isMember(realTravellerService.findTraveller(travellerId)));
		Mockito.verify(travellerService, Mockito.times(3)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);


		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(realTravellerService.findTraveller(travellerId));

		PhotoAlbum photoAlbum = photoAlbumService.addAlbum(holidayGroup, DEFAULT_PROVIDER, DEFAULT_PHOTOALBUM_URL);
		Assert.assertNotNull(photoAlbum);
		Assert.assertTrue(photoAlbum.getAlbumId()>0);
		Mockito.verify(travellerService, Mockito.times(4)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);

		long albumId = photoAlbum.getAlbumId();

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(realTravellerService.findTraveller(travellerId))
				.thenReturn(realTravellerService.findTraveller(travellerId));

		adminService.deletePhotoAlbum(holidayId,albumId);

		Mockito.verify(travellerService, Mockito.times(5)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(realTravellerService.findTraveller(travellerId))
				.thenReturn(realTravellerService.findTraveller(travellerId));

		photoAlbum = photoAlbumService.findPhotoAlbum(holidayId,albumId);
		Assert.assertNull(photoAlbum);

		holidayGroup = holidayGroupService.findHolidayGroup(holidayId);
		Assert.assertNotNull(holidayGroup);

		Mockito.verify(travellerService, Mockito.times(7)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verifyZeroInteractions(emailService,passwordEncoder);
	}



	
}
