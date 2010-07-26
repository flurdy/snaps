package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.Traveller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@ContextConfiguration(locations = {"classpath:test-database.xml", "classpath:test-application.xml"})
public abstract class AbstractServiceTest  extends AbstractTransactionalJUnit4SpringContextTests {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Resource
    protected IHolidayGroupService holidayGroupService;

	@Resource
    protected ISecurityService securityService;

	@Resource
    protected ISecurityService realSecurityService;

	@Resource
    protected ITravellerService travellerService;

//	protected Long defaultTravellerId = null;



	protected static final String DEFAULT_USERNAME = "testuser";
	protected static final String DEFAULT_PASSWORD = "testuser";
	protected static final String DEFAULT_FULLNAME = "testuser";
	protected static final String DEFAULT_EMAIL = "testuser@example.com";
	
	protected void addDefaultUser(){
		Traveller traveller = new Traveller.Builder()
					.username(DEFAULT_USERNAME)
					.fullname(DEFAULT_FULLNAME)
					.password(DEFAULT_PASSWORD)
					.email(DEFAULT_EMAIL).build();

		realSecurityService.registerTraveller(traveller);

//		defaultTravellerId = traveller.getTravellerId();
	}

}
