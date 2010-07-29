package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.Traveller;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@ContextConfiguration(locations = {"classpath:test-database.xml", "classpath:test-application.xml"})
public abstract class AbstractServiceTest  extends AbstractTransactionalJUnit4SpringContextTests {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
    protected IHolidayGroupService holidayGroupService;

	@Resource
    protected IPhotoAlbumService photoAlbumService;

	@Resource
    protected ISecurityService securityService;

	@Resource
    protected ISecurityService realSecurityService;

	@Resource
    protected ITravellerService travellerService;

	@Resource
    protected IAdminService adminService;

	@Resource
    protected IEmailService emailService;


//	protected Long defaultTravellerId = null;



	protected static final String DEFAULT_USERNAME = "testuser";
	protected static final String DEFAULT_PASSWORD = "testuser";
	protected static final String DEFAULT_FULLNAME = "testuser";
	protected static final String DEFAULT_EMAIL = "testuser@example.com";
	protected static final String DEFAULT_USERNAME2 = "testuser2";
	protected static final String DEFAULT_PASSWORD2 = "testuser2";
	protected static final String DEFAULT_FULLNAME2 = "testuser2";
	protected static final String DEFAULT_EMAIL2 = "testuser2@example.com";

	protected static final String DEFAULT_HOLIDAY_NAME = "Test Holiday";
	protected static final String DEFAULT_HOLIDAY2_NAME = "Test 2nd Holiday";

	protected void setUp(){		
		ReflectionTestUtils.setField(realSecurityService, "emailService", emailService);
		Mockito.when(securityService.findLoggedInUsername()).thenReturn(DEFAULT_USERNAME);
	}

	protected Long addDefaultUser(){
		Traveller traveller = new Traveller.Builder()
					.username(DEFAULT_USERNAME)
					.fullname(DEFAULT_FULLNAME)
					.password(DEFAULT_PASSWORD)
					.email(DEFAULT_EMAIL).build();

		realSecurityService.registerTraveller(traveller);
		return traveller.getTravellerId();
	}

	protected void addDefaultUser2(){
		Traveller traveller = new Traveller.Builder()
					.username(DEFAULT_USERNAME2)
					.fullname(DEFAULT_FULLNAME2)
					.password(DEFAULT_PASSWORD2)
					.email(DEFAULT_EMAIL2).build();

		realSecurityService.registerTraveller(traveller);
	}

}
