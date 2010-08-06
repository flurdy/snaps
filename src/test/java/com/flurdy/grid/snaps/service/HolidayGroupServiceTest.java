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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
//@ContextConfiguration(locations = {"classpath:test-domain.xml"})
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




	@Test
	public void testFindHolidaysByNameWithNumber(){
		Mockito.when(holidayGroupRepository.findHolidayGroups(Mockito.anyString())).thenReturn(
						new ArrayList<HolidayGroup>(){{
//							add( findDefaultHoliday() );
//							add( findOtherHoliday() );
						}});
//		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(CURRENT_TRAVELLER);

		List<HolidayGroup> holidayGroups = holidayGroupService.searchForHolidayGroups("Ibiza 2010");

		Assert.assertNotNull( holidayGroups == null || holidayGroups.isEmpty() );

		Mockito.verify(holidayGroupRepository).findHolidayGroups("Ibiza 2010");
		Mockito.verifyNoMoreInteractions(holidayGroupRepository);
//		Mockito.verify(travellerService).findCurrentTraveller();
		Mockito.verifyZeroInteractions(travellerService);
	}



	@Test
	public void testFindHolidaysByName(){

		Mockito.when(holidayGroupRepository.findHolidayGroups(Mockito.anyString())).thenReturn(
				new ArrayList<HolidayGroup>(){{
					add( findDefaultHoliday() );
//							add( findOtherHoliday() );
				}});
//		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(CURRENT_TRAVELLER);

		List<HolidayGroup>  holidayGroups = holidayGroupService.searchForHolidayGroups("Test Holiday");

		Assert.assertNotNull( holidayGroups );
		Assert.assertTrue( holidayGroups.size() == 1 );

		Mockito.verify(holidayGroupRepository).findHolidayGroups("Test Holiday");
		Mockito.verifyNoMoreInteractions(holidayGroupRepository);
	Mockito.verifyZeroInteractions(travellerService);
	}



	@Test
	public void testFindHolidaysByPartialName(){

		Mockito.when(holidayGroupRepository.findHolidayGroups(Mockito.anyString())).thenReturn(
						new ArrayList<HolidayGroup>(){{
							add( findDefaultHoliday() );
							add( findOtherHoliday() );
						}});
//		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(CURRENT_TRAVELLER);

		List<HolidayGroup>  holidayGroups = holidayGroupService.searchForHolidayGroups("Test");

		Assert.assertNotNull( holidayGroups );
		Assert.assertTrue( holidayGroups.size() == 2 );

		Mockito.verify(holidayGroupRepository).findHolidayGroups("Test");
		Mockito.verifyNoMoreInteractions(holidayGroupRepository);
		Mockito.verifyZeroInteractions(travellerService);
	}


	@Test
	public void testFindHolidaysById(){

		Mockito.when(holidayGroupRepository.findHolidayGroup(Mockito.anyLong())).thenReturn(findDefaultHoliday());
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(CURRENT_TRAVELLER);

		List<HolidayGroup>  holidayGroups = holidayGroupService.searchForHolidayGroups("1");

		Assert.assertNotNull( holidayGroups );

		Mockito.verify(holidayGroupRepository).findHolidayGroup(new Long(1));
		Mockito.verifyNoMoreInteractions(holidayGroupRepository);
		Mockito.verify(travellerService).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
	}



	@Test
	public void testFindEmptyHolidays(){

		Mockito.when(holidayGroupRepository.findAllHolidayGroups())
				.thenReturn(
						new ArrayList<HolidayGroup>(){{
							add( findDefaultHoliday() );
							add( findOtherHoliday() );
						}});
//		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(CURRENT_TRAVELLER);

		List<HolidayGroup>  holidayGroups = holidayGroupService.searchForHolidayGroups("");

		Assert.assertNotNull( holidayGroups );
		Assert.assertTrue( holidayGroups.size() == 2 );

		Mockito.verify(holidayGroupRepository).findAllHolidayGroups();
		Mockito.verifyNoMoreInteractions(holidayGroupRepository);
		Mockito.verifyZeroInteractions(travellerService);
	}






	@Test
	public void testFindNoMatchingHolidays(){
		Mockito.when(holidayGroupRepository.findHolidayGroups(Mockito.anyString())).thenReturn(
						new ArrayList<HolidayGroup>(){{
//							add( findDefaultHoliday() );
//							add( findOtherHoliday() );
						}});
//		Mockito.when(holidayGroupRepository.findHolidayGroup(Mockito.anyLong())).thenReturn(findDefaultHoliday());
//		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(CURRENT_TRAVELLER);

		List<HolidayGroup>  holidayGroups = holidayGroupService.searchForHolidayGroups("F123123123123");

		Assert.assertTrue( holidayGroups == null || holidayGroups.isEmpty() );

		Mockito.verify(holidayGroupRepository).findHolidayGroups("F123123123123");
		Mockito.verifyNoMoreInteractions(holidayGroupRepository);
		Mockito.verifyZeroInteractions(travellerService);
	}





	@Test(expected = AssertionError.class)
	public void testFindHolidaysInvalidInput(){

		Mockito.when(holidayGroupRepository.findHolidayGroup(Mockito.anyLong())).thenReturn(findDefaultHoliday());
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(CURRENT_TRAVELLER);

		List<HolidayGroup>  holidayGroups = holidayGroupService.searchForHolidayGroups(null);

	}






	@Test(expected = AssertionError.class)
	public void testFindInvalidHolidays(){

		Mockito.when(holidayGroupRepository.findHolidayGroup(Mockito.anyLong())).thenReturn(findDefaultHoliday());
//		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(CURRENT_TRAVELLER);

		List<HolidayGroup>  holidayGroups = holidayGroupService.searchForHolidayGroups("-1");
	}




}