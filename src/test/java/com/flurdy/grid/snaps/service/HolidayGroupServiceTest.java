package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.SnapNotFoundException;
import com.flurdy.grid.snaps.exception.SnapTechnicalException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
public class HolidayGroupServiceTest extends AbstractServiceTest {

	private Long defaultHolidayGroupId = null;
	private Long defaultHoliday2GroupId = null;

	private static final String DEFAULT_HOLIDAY3_NAME = "Christmas in the Alps";

	
	@Before
	public void setUp(){
		addDefaultUser2();
		Mockito.when(securityService.findLoggedInUsername()).thenReturn(DEFAULT_USERNAME2);
		addNonMemberHoliday();
		Mockito.when(securityService.findLoggedInUsername()).thenReturn(DEFAULT_USERNAME);
		addDefaultUser();
		addDefaultHoliday();
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



	@Test
	public void testFindNonExistentHoliday(){

		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(999);

		Assert.assertNull( holidayGroup );
	}



	@Test(expected = AssertionError.class)
	public void testFindInvalidHoliday(){
		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(-1);
	}
	

	@Test
	public void testFindHoliday(){

		Assert.assertNotNull( defaultHolidayGroupId );
		Assert.assertTrue( defaultHolidayGroupId > 0 );

		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(defaultHolidayGroupId);

		Assert.assertNotNull( holidayGroup );
		Assert.assertEquals( holidayGroup.getGroupId(), defaultHolidayGroupId );

	}



	/*
	 * Todo: extend later?
	 */
	@Test
	public void testFindNonMemberHoliday(){


		Assert.assertNotNull( defaultHoliday2GroupId );
		Assert.assertTrue( defaultHoliday2GroupId > 0 );

		HolidayGroup holidayGroup = holidayGroupService.findHolidayGroup(defaultHoliday2GroupId);

		Assert.assertNotNull(holidayGroup);

	}

	@Test
	public void testAddHoliday(){

		HolidayGroup holidayGroup = new HolidayGroup.Builder()
					.groupName(DEFAULT_HOLIDAY3_NAME)
					.build();
		holidayGroupService.addHolidayGroup(holidayGroup);

		Assert.assertNotNull(holidayGroup.getGroupId());

		holidayGroup = holidayGroupService.findHolidayGroup(holidayGroup.getGroupId());

		Assert.assertNotNull(holidayGroup);

		Assert.assertEquals( holidayGroup.getGroupName(), DEFAULT_HOLIDAY3_NAME);

		Assert.assertTrue( holidayGroup.getMembers().size() == 1 );
		Assert.assertEquals( holidayGroup.getMembers().iterator().next().getTraveller().getSecurityDetail().getUsername() , DEFAULT_USERNAME );

	}


	@Test(expected = SnapTechnicalException.class)
	public void testAddNullHoliday(){
		holidayGroupService.addHolidayGroup(null);
	}



	@Test(expected = SnapTechnicalException.class)
	public void testAddInvalidHoliday(){

		HolidayGroup holidayGroup = new HolidayGroup.Builder()
					.groupName(null)
					.build();
		holidayGroupService.addHolidayGroup(holidayGroup);
	}






	@Test
	public void testAddDuplicateHoliday(){

		HolidayGroup holidayGroup = new HolidayGroup.Builder()
					.groupName(DEFAULT_HOLIDAY_NAME)
					.build();
		holidayGroupService.addHolidayGroup(holidayGroup);


		HolidayGroup holidayGroup2 = new HolidayGroup.Builder()
					.groupName(DEFAULT_HOLIDAY_NAME)
					.build();
		holidayGroupService.addHolidayGroup(holidayGroup2);

	}

}