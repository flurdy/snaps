package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.domain.SecurityDetail;
import com.flurdy.grid.snaps.domain.Traveller;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.HashSet;


@ContextConfiguration(locations = {"classpath:test-database.xml" } )//, "classpath:test-application.xml"})
public abstract class AbstractServiceTest  extends AbstractTransactionalJUnit4SpringContextTests {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

//	@Resource
//	@InjectMocks
//    protected IHolidayGroupService holidayGroupService;
//
//	@Resource
//	@InjectMocks
//    protected IPhotoAlbumService photoAlbumService;
//
//	@Mock
//    protected ISecurityService securityService;
//
//	@Resource
//	@InjectMocks
//    protected ISecurityService realSecurityService;
//
//	@Resource
//	@InjectMocks
//    protected ITravellerService travellerService;
//
//	@Resource
//	@InjectMocks
//    protected IAdminService adminService;
//
//	@Mock
//	@Resource
//    protected IEmailService emailService;
//
//	@Mock
//	protected JavaMailSenderImpl mailSender;

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

	protected final Traveller CURRENT_TRAVELLER = new Traveller.Builder()
					.username(DEFAULT_USERNAME)
					.fullname(DEFAULT_FULLNAME)
					.password(DEFAULT_PASSWORD)
					.email(DEFAULT_EMAIL).build();

	protected void setUp(){
//		MockitoAnnotations.initMocks(this);
//		Mockito.doNothing().when(emailService).sendPassword(Mockito.<Traveller>anyObject(),Mockito.anyString());
//		Mockito.doNothing().when(emailService).notifyNewRegistration(Mockito.<Traveller>anyObject());
//		Mockito.when(securityService.findLoggedInUsername()).thenReturn(DEFAULT_USERNAME);
	}

	protected Traveller generateDefaultTraveller(){
		return new Traveller.Builder()
					.username(DEFAULT_USERNAME)
					.fullname(DEFAULT_FULLNAME)
					.password(DEFAULT_PASSWORD)
					.email(DEFAULT_EMAIL).build();
	}

	protected Traveller generateDefaultRegisterdTraveller(){
		return new Traveller.Builder()
			.username(DEFAULT_USERNAME)
			.fullname(DEFAULT_FULLNAME)
			.password(DEFAULT_PASSWORD)
			.authorities(new HashSet<String>(){{ add("ROLE_USER"); }})
			.email(DEFAULT_EMAIL).build();
	}

	protected Long addDefaultUser(SecurityService securityService){
		Traveller traveller = new Traveller.Builder()
					.username(DEFAULT_USERNAME)
					.fullname(DEFAULT_FULLNAME)
					.password(DEFAULT_PASSWORD)
					.email(DEFAULT_EMAIL).build();

		securityService.registerTraveller(traveller);
//		realSecurityService.addAuthority(DEFAULT_USERNAME,SecurityDetail.AuthorityRole.ROLE_ADMIN);
		return traveller.getTravellerId();
	}

	protected Long add2ndDefaultUser(SecurityService securityService){
		Traveller traveller = new Traveller.Builder()
					.username(DEFAULT_USERNAME2)
					.fullname(DEFAULT_FULLNAME2)
					.password(DEFAULT_PASSWORD2)
					.email(DEFAULT_EMAIL2).build();

		securityService.registerTraveller(traveller);
		return traveller.getTravellerId();
	}

}
