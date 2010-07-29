package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.PhotoAlbum;
import com.flurdy.grid.snaps.domain.PhotoSharingProvider;
import com.flurdy.grid.snaps.exception.SnapAccessDeniedException;
import com.flurdy.grid.snaps.exception.SnapLogicalException;
import com.flurdy.grid.snaps.exception.SnapNotFoundException;
import com.flurdy.grid.snaps.exception.SnapTechnicalException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PhotoAlbumServiceTest extends AbstractServiceTest {

	private static final PhotoSharingProvider DEFAULT_PROVIDER = PhotoSharingProvider.flickr;
	private static final String DEFAULT_PHOTOALBUM_URL = "http://www.flickr.com/photos/flurdy/sets/72157624009834665/";
	private static final String DEFAULT_PHOTOALBUM2_URL = "http://www.flickr.com/photos/flurdy/sets/72157624009834665/";

	private Long defaultHolidayGroupId = null;
	private Long defaultHoliday2GroupId = null;
	private Long defaultPhotoAlbumId = null;

	@Before
	public void SetUp(){
		super.setUp();
		addDefaultUser2();
		Mockito.when(securityService.findLoggedInUsername()).thenReturn(DEFAULT_USERNAME2);
		addNonMemberHoliday();
		Mockito.when(securityService.findLoggedInUsername()).thenReturn(DEFAULT_USERNAME);
		addDefaultUser();
		addDefaultHoliday();
		addDefaultAlbum();
	}

	private void addDefaultHoliday(){
		final HolidayGroup holidayGroup = new HolidayGroup.Builder()
					.groupName(DEFAULT_HOLIDAY_NAME)
					.build();
		holidayGroupService.addHolidayGroup(holidayGroup);
		defaultHolidayGroupId = holidayGroup.getGroupId();
	}

	private void addNonMemberHoliday(){
		final HolidayGroup holidayGroup = new HolidayGroup.Builder()
					.groupName(DEFAULT_HOLIDAY2_NAME)
					.build();
		holidayGroupService.addHolidayGroup(holidayGroup);
		defaultHoliday2GroupId = holidayGroup.getGroupId();
	}

	private void addDefaultAlbum(){
		final HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(defaultHolidayGroupId);
		final PhotoAlbum photoAlbum = photoAlbumService.addAlbum(holidayGroup, DEFAULT_PROVIDER, DEFAULT_PHOTOALBUM_URL);
		Assert.assertNotNull(photoAlbum);
		defaultPhotoAlbumId = photoAlbum.getAlbumId();
	}



	@Test
	public void testAddPhotoAlbum(){
		final HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(defaultHolidayGroupId);
		final PhotoAlbum photoAlbum = photoAlbumService.addAlbum(holidayGroup, DEFAULT_PROVIDER, DEFAULT_PHOTOALBUM2_URL);
		Assert.assertNotNull(photoAlbum);
		Assert.assertEquals(photoAlbum.getHolidayGroup().getGroupName(),DEFAULT_HOLIDAY_NAME);
		Assert.assertEquals(photoAlbum.getUrl(),DEFAULT_PHOTOALBUM2_URL);
		Assert.assertEquals(photoAlbum.getOwner().getSecurityDetail().getUsername(), DEFAULT_USERNAME);
		Assert.assertEquals(photoAlbum.getSharingProvider(), DEFAULT_PROVIDER);
	}


	@Test(expected = SnapLogicalException.class)
	public void testAddPhotoAlbumWithInvalidURL(){
		final HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(defaultHolidayGroupId);
		final PhotoAlbum photoAlbum = photoAlbumService.addAlbum(holidayGroup, DEFAULT_PROVIDER, "asdfasdfasdfasd");
	}


	@Test(expected = AssertionError.class)
	public void testAddPhotoAlbumWithNullURL(){
		final HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(defaultHolidayGroupId);
		final PhotoAlbum photoAlbum = photoAlbumService.addAlbum(holidayGroup, DEFAULT_PROVIDER, null);
	}


	@Test(expected = AssertionError.class)
	public void testAddPhotoAlbumWithNullProvider(){
		final HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(defaultHolidayGroupId);
		final PhotoAlbum photoAlbum = photoAlbumService.addAlbum(holidayGroup, null, DEFAULT_PHOTOALBUM2_URL);
	}


	@Test(expected = SnapAccessDeniedException.class)
	public void testAddPhotoAlbumToAnotherHoliday(){
		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(defaultHoliday2GroupId);
		final PhotoAlbum photoAlbum = photoAlbumService.addAlbum(holidayGroup, DEFAULT_PROVIDER, DEFAULT_PHOTOALBUM2_URL);
	}


	@Test
	public void testFindPhotoAlbum(){
		final PhotoAlbum photoAlbum = photoAlbumService.findPhotoAlbum(defaultHolidayGroupId, defaultPhotoAlbumId );
		Assert.assertNotNull(photoAlbum);
		Assert.assertEquals(photoAlbum.getHolidayGroup().getGroupName(),DEFAULT_HOLIDAY_NAME);
		Assert.assertEquals(photoAlbum.getUrl(),DEFAULT_PHOTOALBUM_URL);
		Assert.assertEquals(photoAlbum.getOwner().getSecurityDetail().getUsername(), DEFAULT_USERNAME);
		Assert.assertEquals(photoAlbum.getSharingProvider(), DEFAULT_PROVIDER);
	}


	@Test
	public void testFindNonExistentPhotoAlbum(){
		final PhotoAlbum photoAlbum = photoAlbumService.findPhotoAlbum(defaultHolidayGroupId, 999 );
		Assert.assertNull(photoAlbum);
	}


	@Test(expected = AssertionError.class)
	public void testFindPhotoAlbumByInvalidAlbumId(){
		final PhotoAlbum photoAlbum = photoAlbumService.findPhotoAlbum(defaultHolidayGroupId, -1 );
	}


	@Test(expected = AssertionError.class)
	public void testFindPhotoAlbumByInvalidHolidayId(){
		final PhotoAlbum photoAlbum = photoAlbumService.findPhotoAlbum(-1, defaultPhotoAlbumId );
	}


	@Test(expected = SnapNotFoundException.class)
	public void testFindPhotoAlbumNonExistentHoliday(){
		final PhotoAlbum photoAlbum = photoAlbumService.findPhotoAlbum(99999, defaultPhotoAlbumId );
	}


	@Test(expected = SnapAccessDeniedException.class)
	public void testFindPhotoAlbumNonMember(){
		final PhotoAlbum photoAlbum = photoAlbumService.findPhotoAlbum(defaultHoliday2GroupId, defaultPhotoAlbumId );
	}









}
