package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.IHolidayGroupRepository;
import com.flurdy.grid.snaps.dao.IPhotoAlbumRepository;
import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.PhotoAlbum;
import com.flurdy.grid.snaps.domain.PhotoSharingProvider;
import com.flurdy.grid.snaps.exception.SnapAccessDeniedException;
import com.flurdy.grid.snaps.exception.SnapLogicalException;
import com.flurdy.grid.snaps.exception.SnapNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PhotoAlbumServiceTest extends AbstractServiceTest {

	private static final PhotoSharingProvider DEFAULT_PROVIDER = PhotoSharingProvider.FLICKR;
	private static final String DEFAULT_PHOTOALBUM_URL = "http://www.flickr.com/photos/flurdy/sets/72157624009834665/";
	private static final String DEFAULT_PHOTOALBUM2_URL = "http://www.flickr.com/photos/flurdy/sets/72157624009834665/";

//	private Long defaultHolidayGroupId = null;
//	private Long defaultHoliday2GroupId = null;
//	private Long defaultPhotoAlbumId = null;

	@Mock
	private IHolidayGroupRepository holidayGroupRepository;

	@Mock
	private ITravellerService travellerService;

	@Mock
	private IPhotoAlbumRepository photoAlbumRepository;

	@InjectMocks
	private IPhotoAlbumService photoAlbumService = new PhotoAlbumService();

	@Before
	public void SetUp(){
		MockitoAnnotations.initMocks(this);
	}


	private PhotoAlbum generateDefaultPhotoAlbum() {
		return new PhotoAlbum.Builder()
				.holidayGroup(generateDefaultHoliday())
				.owner(generateDefaultTraveller())
				.sharingProvider(PhotoSharingProvider.FLICKR)
				.url(DEFAULT_PHOTOALBUM_URL)
				.build();
	}



	@Test
	public void testAddPhotoAlbum(){

		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(generateDefaultRegisteredTraveller());

		final HolidayGroup holidayGroup = generateDefaultHoliday();

		final PhotoAlbum photoAlbum = photoAlbumService.addAlbum(holidayGroup, DEFAULT_PROVIDER, DEFAULT_PHOTOALBUM2_URL);

//		Assert.assertNotNull(photoAlbum);
//		Assert.assertEquals(photoAlbum.getHolidayGroup().getGroupName(),DEFAULT_HOLIDAY_NAME);
//		Assert.assertEquals(photoAlbum.getUrl(),DEFAULT_PHOTOALBUM2_URL);
//		Assert.assertEquals(photoAlbum.getOwner().getSecurityDetail().getUsername(), DEFAULT_USERNAME);
//		Assert.assertEquals(photoAlbum.getSharingProvider(), DEFAULT_PROVIDER);

		Mockito.verify(travellerService, Mockito.times(1)).findCurrentTraveller();
		Mockito.verify(photoAlbumRepository, Mockito.times(1)).addAlbum(Mockito.<PhotoAlbum>anyObject());
		Mockito.verifyNoMoreInteractions(travellerService,photoAlbumRepository);
		Mockito.verifyZeroInteractions(holidayGroupRepository);
	}


	@Test(expected = SnapLogicalException.class)
	public void testAddPhotoAlbumWithInvalidURL(){
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(generateDefaultRegisteredTraveller());
		final HolidayGroup holidayGroup = generateDefaultHoliday();
		final PhotoAlbum photoAlbum = photoAlbumService.addAlbum(holidayGroup, DEFAULT_PROVIDER, "asdfasdfasdfasd");
	}


	@Test(expected = AssertionError.class)
	public void testAddPhotoAlbumWithNullURL(){
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(generateDefaultRegisteredTraveller());
		final HolidayGroup holidayGroup = generateDefaultHoliday();
		final PhotoAlbum photoAlbum = photoAlbumService.addAlbum(holidayGroup, DEFAULT_PROVIDER, null);
	}



	@Test(expected = AssertionError.class)
	public void testAddPhotoAlbumWithNullProvider(){
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(generateDefaultRegisteredTraveller());
		final HolidayGroup holidayGroup = generateDefaultHoliday();
		final PhotoAlbum photoAlbum = photoAlbumService.addAlbum(holidayGroup, null, DEFAULT_PHOTOALBUM2_URL);
	}


	@Test(expected = SnapAccessDeniedException.class)
	public void testAddPhotoAlbumToAnotherHoliday(){
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(generateDefaultRegisteredTraveller());
		final HolidayGroup holidayGroup = generateNonMemberHoliday();

		final PhotoAlbum photoAlbum = photoAlbumService.addAlbum(holidayGroup, DEFAULT_PROVIDER, DEFAULT_PHOTOALBUM2_URL);
	}


	@Test
	public void testFindPhotoAlbum(){
		Mockito.when(holidayGroupRepository.findHolidayGroup(new Long(1))).thenReturn(generateDefaultHoliday());
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(generateDefaultRegisteredTraveller());
		Mockito.when(photoAlbumRepository.findAlbum(new Long(1))).thenReturn(generateDefaultPhotoAlbum());


		final PhotoAlbum photoAlbum = photoAlbumService.findPhotoAlbum( 1, 1 );

		Assert.assertNotNull(photoAlbum);
		Assert.assertEquals(photoAlbum.getHolidayGroup().getGroupName(),DEFAULT_HOLIDAY_NAME);
		Assert.assertEquals(photoAlbum.getUrl(),DEFAULT_PHOTOALBUM_URL);
		Assert.assertEquals(photoAlbum.getOwner().getSecurityDetail().getUsername(), DEFAULT_USERNAME);
		Assert.assertEquals(photoAlbum.getSharingProvider(), DEFAULT_PROVIDER);

		Mockito.verify(holidayGroupRepository, Mockito.times(1)).findHolidayGroup(new Long(1));
		Mockito.verify(travellerService, Mockito.times(1)).findCurrentTraveller();
		Mockito.verify(photoAlbumRepository, Mockito.times(1)).findAlbum(Mockito.anyLong());
		Mockito.verifyNoMoreInteractions(holidayGroupRepository,travellerService,photoAlbumRepository);
	}


	@Test
	public void testFindNonExistentPhotoAlbum(){
		Mockito.when(holidayGroupRepository.findHolidayGroup(new Long(1))).thenReturn(generateDefaultHoliday());
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(generateDefaultRegisteredTraveller());
		Mockito.when(photoAlbumRepository.findAlbum(new Long(999))).thenReturn(null);

		final PhotoAlbum photoAlbum = photoAlbumService.findPhotoAlbum(1, 999 );
		Assert.assertNull(photoAlbum);

		Mockito.verify(travellerService, Mockito.times(1)).findCurrentTraveller();
		Mockito.verify(holidayGroupRepository, Mockito.times(1)).findHolidayGroup(new Long(1));
		Mockito.verify(photoAlbumRepository, Mockito.times(1)).findAlbum(Mockito.anyLong());
		Mockito.verifyNoMoreInteractions(holidayGroupRepository,travellerService,photoAlbumRepository);
	}




	@Test(expected = AssertionError.class)
	public void testFindPhotoAlbumByInvalidAlbumId(){
		final PhotoAlbum photoAlbum = photoAlbumService.findPhotoAlbum(1, -1 );
	}


	@Test(expected = AssertionError.class)
	public void testFindPhotoAlbumByInvalidHolidayId(){
		final PhotoAlbum photoAlbum = photoAlbumService.findPhotoAlbum(-1, 1 );
	}


	@Test(expected = SnapNotFoundException.class)
	public void testFindPhotoAlbumNonExistentHoliday(){
		Mockito.when(holidayGroupRepository.findHolidayGroup(new Long(9999))).thenReturn(null);

		final PhotoAlbum photoAlbum = photoAlbumService.findPhotoAlbum(99999, 1 );
	}


	@Test(expected = SnapAccessDeniedException.class)
	public void testFindPhotoAlbumNonMember(){
		Mockito.when(holidayGroupRepository.findHolidayGroup(new Long(2))).thenReturn(generateNonMemberHoliday());
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(generateDefaultRegisteredTraveller());
//		Mockito.when(photoAlbumRepository.findAlbum(new Long(1))).thenReturn(null);

		final PhotoAlbum photoAlbum = photoAlbumService.findPhotoAlbum( 2, 1 );
	}



}
