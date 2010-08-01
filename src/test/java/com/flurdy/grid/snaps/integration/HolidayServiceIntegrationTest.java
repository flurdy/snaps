package com.flurdy.grid.snaps.integration;

import com.flurdy.grid.snaps.dao.ITravellerRepository;
import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.HolidayMember;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Transactional
public class HolidayServiceIntegrationTest extends AbstractServiceIntegrationTest {


	@Mock
	private IEmailService emailService;
//
//	@InjectMocks
//	@Autowired
//	private ITravellerRepository travellerRepository;

	@InjectMocks
	@Autowired
	private ISecurityService securityService;

	@Mock
	private ITravellerService travellerService;

	@Autowired
	private ITravellerService realTravellerService;

	@InjectMocks
	@Autowired
	private IHolidayGroupService holidayGroupService;


	private static final String DEFAULT_HOLIDAY3_NAME = "Christmas in the Alps";


	private long defaultTravellerId;
	private long defaultHolidayId;

	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		registerDefaultTraveller();
//		addDefaultHoliday();
	}

	private void registerDefaultTraveller(){
		Traveller traveller = generateDefaultTraveller();
		securityService.registerTraveller(traveller);
		defaultTravellerId = traveller.getTravellerId();
		Assert.assertTrue( defaultTravellerId > 0 );
		Mockito.verify(emailService).notifyNewRegistration(Mockito.<Traveller>anyObject());
		Mockito.verifyNoMoreInteractions(emailService);
	}

	private Traveller findDefaultTraveller(){
//		Mockito.when(travellerService.findTraveller(defaultTravellerId))
//				.thenReturn(realTravellerService.findTraveller(defaultTravellerId));
		Traveller traveller = realTravellerService.findTraveller(defaultTravellerId);
		return traveller;
	}

	private HolidayGroup generateDefaultHoliday(){
		final HolidayGroup defaultHoliday = new HolidayGroup.Builder()
					.groupName(DEFAULT_HOLIDAY_NAME)
					.members(new HashSet<HolidayMember>())
					.build();
		defaultHoliday.addMember(findDefaultTraveller());
		return defaultHoliday;
	}

	private void addDefaultHoliday(){
		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(findDefaultTraveller());
		final HolidayGroup defaultHoliday = generateDefaultHoliday();
		holidayGroupService.addHolidayGroup(defaultHoliday);
		defaultHolidayId = defaultHoliday.getGroupId();
		Assert.assertTrue( defaultHolidayId > 0 );
		Mockito.verify(travellerService).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verifyZeroInteractions(emailService);
	}

	@Test
	public void testFindHoliday(){
		addDefaultHoliday();

		Mockito.when(travellerService.findCurrentTraveller()).thenReturn(findDefaultTraveller());

		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(defaultHolidayId);

		Assert.assertNotNull( holidayGroup );

		Mockito.verify(travellerService, Mockito.times(2)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verifyZeroInteractions(emailService);
	}

	@Test
	public void testAddHoliday(){


		HolidayGroup holidayGroupWithMember = new HolidayGroup.Builder()
					.groupName(DEFAULT_HOLIDAY3_NAME)
					.groupId(new Long(3))
					.members(new HashSet<HolidayMember>())
					.build();
		holidayGroupWithMember.addMember(findDefaultTraveller());

		Mockito.when(travellerService.findCurrentTraveller())
				.thenReturn(findDefaultTraveller())
				.thenReturn(findDefaultTraveller());;

		HolidayGroup holidayGroup = new HolidayGroup.Builder()
					.groupName(DEFAULT_HOLIDAY3_NAME)
					.build();

		holidayGroupService.addHolidayGroup(holidayGroup);

		long holidayGroupId = holidayGroup.getGroupId();

		holidayGroup = holidayGroupService.findHolidayGroup(holidayGroupId);

		Assert.assertNotNull(holidayGroup);
		Assert.assertEquals( holidayGroup.getGroupName(), DEFAULT_HOLIDAY3_NAME);

		Assert.assertTrue( holidayGroup.getMembers().size() == 1 );
		HolidayMember holidayMember = holidayGroup.getMembers().iterator().next();
		Assert.assertNotNull(holidayMember.getTraveller());
		Assert.assertEquals( holidayMember.getTraveller().getSecurityDetail().getUsername() , DEFAULT_USERNAME );

		Mockito.verify(travellerService , Mockito.times(2)).findCurrentTraveller();
		Mockito.verifyNoMoreInteractions(travellerService);
		Mockito.verifyZeroInteractions(emailService);
	}


}
