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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@ContextConfiguration(locations = {"classpath:test-database.xml", "classpath:test-domain.xml", "classpath:test-email.xml"})
public class HolidayGroupServiceTest  extends AbstractServiceTest {


	protected final Logger log = LoggerFactory.getLogger(this.getClass());


	private Long defaultHolidayGroupId = null;
	private Long defaultHoliday2GroupId = null;

	private static final String DEFAULT_HOLIDAY3_NAME = "Christmas in the Alps";

	@Autowired
	@Mock
	private IEmailService emailService;

	@Autowired
	@Mock
	private IHolidayGroupRepository holidayGroupRepository;

	@Mock
	private ITravellerService travellerService;

	@InjectMocks
	private IHolidayGroupService holidayGroupService;

	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);



		
		super.setUp();
		long user2Id = addDefaultUser2();
		assert user2Id > 0;
		addNonMemberHoliday();
		long user1Id = addDefaultUser();
		assert user1Id > 0;
		addDefaultHoliday();
	}

	private void addDefaultHoliday(){
		final HolidayGroup holidayGroup = new HolidayGroup.Builder()
					.groupName(DEFAULT_HOLIDAY_NAME)
					.build();
//		Mockito.reset(securityService);
		Mockito.when(securityService.findLoggedInUsername()).thenReturn(DEFAULT_USERNAME);
		holidayGroupService.addHolidayGroup(holidayGroup);
		defaultHolidayGroupId = holidayGroup.getGroupId();
	}

	private void addNonMemberHoliday(){
		HolidayGroup holidayGroup = new HolidayGroup.Builder().groupName(DEFAULT_HOLIDAY2_NAME).build();
//		Mockito.reset(securityService);
		Mockito.when(securityService.findLoggedInUsername())
				.thenReturn(DEFAULT_USERNAME2).thenReturn(DEFAULT_USERNAME2).thenReturn(DEFAULT_USERNAME);
		holidayGroupService.addHolidayGroup(holidayGroup);
		Assert.assertTrue(holidayGroup.getGroupId()>0);

		defaultHoliday2GroupId = holidayGroup.getGroupId();
		holidayGroup = holidayGroupService.findHolidayGroup(defaultHoliday2GroupId);

		Assert.assertTrue(holidayGroup.getMembers().size()==1);
		HolidayMember holidayMember = holidayGroup.getMembers().iterator().next();
		Assert.assertNotNull(holidayMember.getTraveller());
		Assert.assertEquals(holidayMember.getTraveller().getSecurityDetail().getUsername(),DEFAULT_USERNAME2);
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
		HolidayMember holidayMember = holidayGroup.getMembers().iterator().next();
		Assert.assertNotNull(holidayMember.getTraveller());
		Assert.assertEquals( holidayMember.getTraveller().getSecurityDetail().getUsername() , DEFAULT_USERNAME );

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