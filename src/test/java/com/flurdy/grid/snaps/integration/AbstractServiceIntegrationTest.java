package com.flurdy.grid.snaps.integration;

import com.flurdy.grid.snaps.domain.HolidayGroup;
import com.flurdy.grid.snaps.domain.HolidayMember;
import com.flurdy.grid.snaps.domain.Traveller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Transactional
@ContextConfiguration(locations = {"classpath:test-database.xml",
		"classpath:test-domain.xml", "classpath:test-repositories.xml",
		"classpath:test-services.xml", "classpath:test-email.xml"})
public abstract class AbstractServiceIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

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
	protected static final String ADMIN_USERNAME = "admin testuser";
	protected static final String ADMIN_PASSWORD = "admin testuser";
	protected static final String ADMIN_FULLNAME = "admin testuser";
	protected static final String ADMIN_EMAIL = "admin.testuser@example.com";
	protected static final String ENCODED_PASSWORD = "encoded password";

	protected Traveller generateDefaultTraveller(){
		return new Traveller.Builder()
					.username(DEFAULT_USERNAME)
					.fullname(DEFAULT_FULLNAME)
					.password(DEFAULT_PASSWORD)
					.email(DEFAULT_EMAIL).build();
	}

	protected Traveller generateAdminTraveller(){
		return new Traveller.Builder()
					.username(ADMIN_USERNAME)
					.fullname(ADMIN_FULLNAME)
					.password(ADMIN_PASSWORD)
					.email(ADMIN_EMAIL).build();
	}
	
}
