package com.flurdy.grid.snaps.integration;

import com.flurdy.grid.snaps.dao.IHolidayGroupRepository;
import com.flurdy.grid.snaps.dao.IPhotoAlbumRepository;
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

public class PhotoAlbumServiceIntegrationTest extends AbstractServiceIntegrationTest {

	@Mock
	private IEmailService emailService;

	@InjectMocks
	@Autowired
	private ISecurityService securityService;

	@InjectMocks
	@Autowired
	private IPhotoAlbumRepository photoAlbumRepository;

	@InjectMocks
	@Autowired
	private IHolidayGroupRepository holidayGroupRepository;

	@Mock
	private ITravellerService travellerService;

	@InjectMocks
	@Autowired
	private IHolidayGroupService holidayGroupService;

	@InjectMocks
	@Autowired
	private ITravellerService realTravellerService;

	@InjectMocks
	@Autowired
	private IPhotoAlbumService photoAlbumService;

	private static final PhotoSharingProvider DEFAULT_PROVIDER = PhotoSharingProvider.flickr;
	private static final String DEFAULT_PHOTOALBUM_URL = "http://www.flickr.com/photos/flurdy/sets/72157624009834665/";


	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
	}

	private Long registerDefaultTraveller(){
		Traveller traveller = generateDefaultTraveller();
		securityService.registerTraveller(traveller);
		long defaultTravellerId = traveller.getTravellerId();
		Assert.assertTrue( defaultTravellerId > 0 );
		Mockito.verify(emailService).notifyNewRegistration(Mockito.<Traveller>anyObject());
		Mockito.verifyNoMoreInteractions(emailService);
		return defaultTravellerId;
	}

	private Traveller findDefaultTraveller(long defaultTravellerId){
		return realTravellerService.findTraveller(defaultTravellerId);
	}

	private Traveller registerAndFindDefaultTraveller(){
		return realTravellerService.findTraveller(registerDefaultTraveller());
	}

	private HolidayGroup generateDefaultHoliday(Traveller defaultTraveller){
		final HolidayGroup defaultHoliday = new HolidayGroup.Builder()
					.groupName(DEFAULT_HOLIDAY_NAME)
					.members(new HashSet<HolidayMember>())
					.build();
		defaultHoliday.addMember(defaultTraveller);
		return defaultHoliday;
	}

	private PhotoAlbum generateDefaultPhotoAlbum(Traveller defaultTraveller) {
		return new PhotoAlbum.Builder()
				.holidayGroup(generateDefaultHoliday(defaultTraveller))
				.owner(generateDefaultTraveller())
				.sharingProvider(PhotoSharingProvider.flickr)
				.url(DEFAULT_PHOTOALBUM_URL)
				.build();
	}

	private Long addDefaultHoliday(long defaultTravellerId){
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(findDefaultTraveller(defaultTravellerId));
		final HolidayGroup defaultHoliday = generateDefaultHoliday( findDefaultTraveller(defaultTravellerId));
		holidayGroupService.addHolidayGroup(defaultHoliday);
		Long holidayId = defaultHoliday.getGroupId();
		Assert.assertTrue( holidayId > 0 );
		Mockito.verify(travellerService).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verifyZeroInteractions(emailService);
		return holidayId;
	}

	private HolidayGroup findDefaultHoliday(long defaultHolidayId){
		return holidayGroupRepository.findHolidayGroup(defaultHolidayId);
	}


	private HolidayGroup addAndFindDefaultHoliday(long defaultTravellerId){
		Long holidayId =  addDefaultHoliday(defaultTravellerId);
		return holidayGroupRepository.findHolidayGroup(holidayId);
	}




	@Test
	public void testAddPhotoAlbum(){

		long defaultTravellerId = registerDefaultTraveller();
		assert defaultTravellerId > 0;
		final HolidayGroup holidayGroup = addAndFindDefaultHoliday(defaultTravellerId);

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(realTravellerService.findTraveller(defaultTravellerId));

		final PhotoAlbum photoAlbum = photoAlbumService.addAlbum(holidayGroup, DEFAULT_PROVIDER, DEFAULT_PHOTOALBUM_URL);

		Assert.assertNotNull(photoAlbum);
		Assert.assertEquals(photoAlbum.getHolidayGroup().getGroupName(),DEFAULT_HOLIDAY_NAME);
		Assert.assertEquals(photoAlbum.getUrl(),DEFAULT_PHOTOALBUM_URL);
		Assert.assertEquals(photoAlbum.getOwner().getSecurityDetail().getUsername(), DEFAULT_USERNAME);
		Assert.assertEquals(photoAlbum.getSharingProvider(), DEFAULT_PROVIDER);

		Mockito.verify(travellerService, Mockito.times(2)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verifyZeroInteractions(emailService);
	}


	@Test
	public void testFindPhotoAlbum(){

		long defaultTravellerId = registerDefaultTraveller();

		long defaultHolidayId = addDefaultHoliday(defaultTravellerId);

		HolidayGroup holidayGroup = findDefaultHoliday(defaultHolidayId);

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(realTravellerService.findTraveller(defaultTravellerId))
				.thenReturn(realTravellerService.findTraveller(defaultTravellerId));

		 PhotoAlbum photoAlbum = photoAlbumService.addAlbum(holidayGroup, DEFAULT_PROVIDER, DEFAULT_PHOTOALBUM_URL);


		photoAlbum = photoAlbumService.findPhotoAlbum( defaultHolidayId, photoAlbum.getAlbumId() );

		Assert.assertNotNull(photoAlbum);
		Assert.assertEquals(photoAlbum.getHolidayGroup().getGroupName(),DEFAULT_HOLIDAY_NAME);
		Assert.assertEquals(photoAlbum.getUrl(),DEFAULT_PHOTOALBUM_URL);
		Assert.assertEquals(photoAlbum.getOwner().getSecurityDetail().getUsername(), DEFAULT_USERNAME);
		Assert.assertEquals(photoAlbum.getSharingProvider(), DEFAULT_PROVIDER);

		Mockito.verify(travellerService, Mockito.times(3)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verifyZeroInteractions(emailService);
	}



}
