package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.IHolidayGroupRepository;
import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.HolidayMember;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.SnapNotFoundException;
import com.flurdy.grid.snaps.exception.SnapTechnicalException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@ContextConfiguration(locations = {"classpath:test-database.xml", "classpath:test-domain.xml"})
public class HolidayGroupServiceTest  extends AbstractServiceTest {


//
//	private Long defaultHolidayGroupId = null;
//	private Long defaultHoliday2GroupId = null;

	private static final String DEFAULT_HOLIDAY3_NAME = "Christmas in the Alps";

//	@Autowired
//	@Mock
//	private IEmailService emailService;

//	@Autowired
	@Mock
	private IHolidayGroupRepository holidayGroupRepository;

//	@Autowired
//	@Mock
//	private ISecurityService securityService;

	@Mock
	private ITravellerService travellerService;

	@InjectMocks
	private IHolidayGroupService holidayGroupService = new HolidayGroupService();

	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
	}

	private HolidayGroup findDefaultHoliday(){
		final HolidayGroup defaultHoliday = new HolidayGroup.Builder()
					.groupName(DEFAULT_HOLIDAY_NAME)
					.members(new HashSet<HolidayMember>())
					.build();
		defaultHoliday.addMember(CURRENT_TRAVELLER);
		return defaultHoliday;
	}

	private HolidayGroup findOtherHoliday(){
		final HolidayGroup holiday = new HolidayGroup.Builder()
					.groupName(DEFAULT_HOLIDAY2_NAME)
					.members(new HashSet<HolidayMember>())
					.build();
		return holiday;				
	}

//
//
//		super.setUp();
//		long user2Id = addDefaultUser2();
//		assert user2Id > 0;
//		addNonMemberHoliday();
//		long user1Id = addDefaultUser();
//		assert user1Id > 0;
//		addDefaultHoliday();


//	private void addDefaultHoliday(){
//		final HolidayGroup holidayGroup = new HolidayGroup.Builder()
//					.groupName(DEFAULT_HOLIDAY_NAME)
//					.build();
////		Mockito.reset(securityService);
//		Mockito.when(securityService.findLoggedInUsername()).thenReturn(DEFAULT_USERNAME);
//		holidayGroupService.addHolidayGroup(holidayGroup);
//		defaultHolidayGroupId = holidayGroup.getGroupId();
//	}

//	private void addNonMemberHoliday(){
//		HolidayGroup holidayGroup = new HolidayGroup.Builder().groupName(DEFAULT_HOLIDAY2_NAME).build();
////		Mockito.reset(securityService);
//		Mockito.when(securityService.findLoggedInUsername())
//				.thenReturn(DEFAULT_USERNAME2).thenReturn(DEFAULT_USERNAME2).thenReturn(DEFAULT_USERNAME);
//		holidayGroupService.addHolidayGroup(holidayGroup);
//		Assert.assertTrue(holidayGroup.getGroupId()>0);
//
//		defaultHoliday2GroupId = holidayGroup.getGroupId();
//		holidayGroup = holidayGroupService.findHolidayGroup(defaultHoliday2GroupId);
//
//		Assert.assertTrue(holidayGroup.getMembers().size()==1);
//		HolidayMember holidayMember = holidayGroup.getMembers().iterator().next();
//		Assert.assertNotNull(holidayMember.getTraveller());
//		Assert.assertEquals(holidayMember.getTraveller().getSecurityDetail().getUsername(),DEFAULT_USERNAME2);
//	}



	@Test
	public void testFindNonExistentHoliday(){
		
		Mockito.when(holidayGroupRepository.findHolidayGroup(new Long(999))).thenReturn(null);

		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(999);
		Assert.assertNull( holidayGroup );

		Mockito.verify(holidayGroupRepository).findHolidayGroup(new Long(999));
		Mockito.verifyNoMoreInteractions(holidayGroupRepository);
        Mockito.verifyZeroInteractions(travellerService);
	}

	@Test(expected = AssertionError.class)
	public void testFindInvalidHoliday(){
		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(-1);
        Mockito.verifyZeroInteractions(holidayGroupRepository,travellerService);
	}


	@Test
	public void testFindHoliday(){

		Mockito.when(holidayGroupRepository.findHolidayGroup(Mockito.anyLong())).thenReturn(findDefaultHoliday());
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(CURRENT_TRAVELLER);

		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(1);

		Assert.assertNotNull( holidayGroup );

		Mockito.verify(holidayGroupRepository).findHolidayGroup(new Long(1));
		Mockito.verifyNoMoreInteractions(holidayGroupRepository);
		Mockito.verify(travellerService).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
	}



	@Test
	public void testFindNonMemberHoliday(){

		Mockito.when(holidayGroupRepository.findHolidayGroup(Mockito.anyLong())).thenReturn(findOtherHoliday());
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(CURRENT_TRAVELLER);

		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(2);

		Assert.assertNotNull(holidayGroup);
		Assert.assertNull(holidayGroup.getPhotoAlbums());
		Mockito.verify(holidayGroupRepository).findHolidayGroup(new Long(2));
		Mockito.verifyNoMoreInteractions(holidayGroupRepository);
		Mockito.verify(travellerService).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);

	}


	@Test
	public void testAddHoliday(){


		HolidayGroup holidayGroupWithMember = new HolidayGroup.Builder()
					.groupName(DEFAULT_HOLIDAY3_NAME)
					.groupId(new Long(3))
					.members(new HashSet<HolidayMember>())
					.build();
		holidayGroupWithMember.addMember(CURRENT_TRAVELLER);

		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(CURRENT_TRAVELLER);
		Mockito.when(holidayGroupRepository.addHolidayGroup(Mockito.<HolidayGroup>anyObject())).thenReturn(new Long(3));
		Mockito.when(holidayGroupRepository.findHolidayGroup(new Long(3))).thenReturn(holidayGroupWithMember);
		Mockito.doNothing().when(holidayGroupRepository).updateHolidayGroup(Mockito.<HolidayGroup>anyObject());

		HolidayGroup holidayGroup = new HolidayGroup.Builder()
					.groupName(DEFAULT_HOLIDAY3_NAME)
//					.groupId(new Long(3))
					.build();

		holidayGroupService.addHolidayGroup(holidayGroup);

//		Assert.assertNotNull(holidayGroup.getGroupId());
		Mockito.verify(travellerService).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verify(holidayGroupRepository).addHolidayGroup(Mockito.<HolidayGroup>anyObject());
		Mockito.verify(holidayGroupRepository).findHolidayGroup(new Long(3));
		Mockito.verify(holidayGroupRepository).updateHolidayGroup(Mockito.<HolidayGroup>anyObject());
		Mockito.verifyNoMoreInteractions(holidayGroupRepository);

		Mockito.when(holidayGroupRepository.findHolidayGroup(Mockito.anyLong())).thenReturn(holidayGroupWithMember);
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(CURRENT_TRAVELLER);

		holidayGroup = holidayGroupService.findHolidayGroup(new Long(3));

		Assert.assertNotNull(holidayGroup);

		Assert.assertEquals( holidayGroup.getGroupName(), DEFAULT_HOLIDAY3_NAME);

		Assert.assertTrue( holidayGroup.getMembers().size() == 1 );
		HolidayMember holidayMember = holidayGroup.getMembers().iterator().next();
		Assert.assertNotNull(holidayMember.getTraveller());
		Assert.assertEquals( holidayMember.getTraveller().getSecurityDetail().getUsername() , DEFAULT_USERNAME );
				
		Mockito.verify(travellerService , Mockito.times(2)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verify(holidayGroupRepository, Mockito.times(2)).findHolidayGroup(new Long(3));
		Mockito.verifyNoMoreInteractions(holidayGroupRepository);

	}


	@Test(expected = SnapTechnicalException.class)
	public void testAddNullHoliday(){
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(CURRENT_TRAVELLER);
		holidayGroupService.addHolidayGroup(null);
		Mockito.verify(travellerService).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
        Mockito.verifyZeroInteractions( holidayGroupRepository);
	}



	@Test(expected = SnapTechnicalException.class)
	public void testAddInvalidHoliday(){
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(CURRENT_TRAVELLER);
		HolidayGroup holidayGroup = new HolidayGroup.Builder()
					.groupName(null)
					.build();
		holidayGroupService.addHolidayGroup(holidayGroup);
		Mockito.verify(travellerService).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
        Mockito.verifyZeroInteractions( holidayGroupRepository);
	}




	@Test
	public void testAddDuplicateHoliday(){

		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(CURRENT_TRAVELLER);
		Mockito.when(holidayGroupRepository.addHolidayGroup(Mockito.<HolidayGroup>anyObject()))
				.thenReturn(new Long(1)).thenReturn(new Long(2));
		Mockito.when(holidayGroupRepository.findHolidayGroup(new Long(1))).thenReturn(findDefaultHoliday());
		Mockito.when(holidayGroupRepository.findHolidayGroup(new Long(2))).thenReturn(findOtherHoliday());
		Mockito.doNothing().when(holidayGroupRepository).updateHolidayGroup(Mockito.<HolidayGroup>anyObject());

		final HolidayGroup defaultHoliday = new HolidayGroup.Builder()
					.groupName(DEFAULT_HOLIDAY_NAME)
					.members(new HashSet<HolidayMember>())
					.build();
		holidayGroupService.addHolidayGroup(findDefaultHoliday());


		final HolidayGroup otherHoliday = new HolidayGroup.Builder()
					.groupName(DEFAULT_HOLIDAY_NAME)
					.members(new HashSet<HolidayMember>())
					.build();
		holidayGroupService.addHolidayGroup(findOtherHoliday());

		Mockito.verify(travellerService, Mockito.times(2)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verify(holidayGroupRepository, Mockito.times(1)).findHolidayGroup(new Long(1));
		Mockito.verify(holidayGroupRepository, Mockito.times(1)).findHolidayGroup(new Long(2));
		Mockito.verify(holidayGroupRepository, Mockito.times(2)).addHolidayGroup(Mockito.<HolidayGroup>anyObject());
		Mockito.verify(holidayGroupRepository, Mockito.times(2)).updateHolidayGroup(Mockito.<HolidayGroup>anyObject());
		Mockito.verifyNoMoreInteractions(holidayGroupRepository);

	}


}