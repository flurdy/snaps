package com.flurdy.grid.snaps.service;

import com.flurdy.grid.snaps.dao.ITravellerRepository;
import com.flurdy.grid.snaps.domain.SecurityDetail;
import com.flurdy.grid.snaps.domain.Traveller;
import com.flurdy.grid.snaps.exception.SnapLogicalException;
import com.flurdy.grid.snaps.exception.SnapTechnicalException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:test-database.xml", "classpath:test-application.xml"})
public class EmailServiceTest extends AbstractServiceTest {

	private final transient Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String ADMIN_USERNAME = "testuser";
	private static final String ADMIN_PASSWORD = "testuser";
	private static final String ADMIN_FULLNAME = "testuser";
	private static final String ADMIN_EMAIL = "ivar+admin.snaps@localhost.localdomain";
	private static final String TEST_USERNAME = "testuser";
	private static final String TEST_PASSWORD = "testuser";
	private static final String TEST_FULLNAME = "testuser";
	private static final String TEST_EMAIL = "ivar+snaps@localhost.localdomain";

	@Mock
	@Autowired
	private JavaMailSenderImpl mailSender;

	@Mock
	@Autowired
	private ITravellerRepository travellerRepository;

	@InjectMocks
	@Autowired
	private IEmailService emailService;

	private boolean sendEmail = false;

	private List<Traveller> travellers = new ArrayList<Traveller>();

	@Before
	public void setUp(){
//		Mockito.doCallRealMethod().when(emailService).setPasswordTextSubject(Mockito.anyString());
//		Mockito.doCallRealMethod().when(emailService).setRegistrationNotificationTextSubject(Mockito.anyString());
//		Mockito.doCallRealMethod().when(emailService).setFromAddress(Mockito.anyString());
//		Mockito.doCallRealMethod().when(emailService).setPasswordTextTemplate(Mockito.anyString());
//		Mockito.doCallRealMethod().when(emailService).setRegistrationNotificationTextTemplate(Mockito.anyString());
//		Mockito.doCallRealMethod().when(emailService).setSendEmails(Mockito.anyBoolean());
		MockitoAnnotations.initMocks(this);

//		super.setUp();

		Mockito.when(mailSender.createMimeMessage()).thenCallRealMethod();
		Mockito.doNothing().when(mailSender).send(Mockito.<MimeMessage>anyObject());

		Traveller admin = new Traveller.Builder()
					.username(ADMIN_USERNAME)
					.fullname(ADMIN_FULLNAME)
					.password(ADMIN_PASSWORD)
					.email(ADMIN_EMAIL).build();
		admin.getSecurityDetail().addAuthority(SecurityDetail.AuthorityRole.ROLE_USER);
		admin.getSecurityDetail().addAuthority(SecurityDetail.AuthorityRole.ROLE_ADMIN);
		travellers.add(admin);
		Mockito.when(travellerRepository.findAllTravellers()).thenReturn(travellers);
//		Mockito.doCallRealMethod().when(emailService).sendPassword(Mockito.<Traveller>anyObject(),Mockito.anyString());
//		Mockito.doCallRealMethod().when(emailService).notifyNewRegistration(Mockito.<Traveller>anyObject());

	}

	@Test
	public void testEmailPassword(){
		Traveller traveller = new Traveller.Builder()
					.username(TEST_USERNAME)
					.fullname(TEST_FULLNAME)
					.password(TEST_PASSWORD)
					.email(TEST_EMAIL).build();
		traveller.getSecurityDetail().addAuthority(SecurityDetail.AuthorityRole.ROLE_USER);
		traveller.getSecurityDetail().addAuthority(SecurityDetail.AuthorityRole.ROLE_ADMIN);

		if( sendEmail ){
			Mockito.doCallRealMethod().doNothing().when(mailSender).send(Mockito.<MimeMessage>anyObject());
		}

		emailService.sendPassword(traveller,"testEmailPassword");

	}


	@Test(expected = AssertionError.class)
	public void testEmailPasswordNullTraveller(){
		emailService.sendPassword(null,"testEmailPasswordNullTraveller");
	}


	@Test(expected = SnapTechnicalException.class)
	public void testEmailPasswordMissingEmail(){
		Traveller traveller = new Traveller.Builder()
					.username(TEST_USERNAME)
					.fullname(TEST_FULLNAME)
					.password(TEST_PASSWORD)
					.build();
		emailService.sendPassword(traveller,"testEmailPasswordInvalidTraveller");
	}


	@Test(expected = SnapTechnicalException.class)
	public void testEmailPasswordInvalidTraveller2(){
		Traveller traveller = new Traveller.Builder()
					.fullname(TEST_FULLNAME)
					.password(TEST_PASSWORD)
					.email(TEST_EMAIL).build();
		emailService.sendPassword(traveller,"testEmailPasswordInvalidTraveller");
	}


	@Test(expected = SnapTechnicalException.class)
	public void testEmailPasswordInvalidPassword(){
		Traveller traveller = new Traveller.Builder()
					.username(TEST_USERNAME)
					.fullname(TEST_FULLNAME)
					.password(TEST_PASSWORD)
					.email(TEST_EMAIL).build();
		emailService.sendPassword(traveller,"");
	}


	@Test(expected = AssertionError.class)
	public void testEmailPasswordNullPassword(){
		Traveller traveller = new Traveller.Builder()
					.username(TEST_USERNAME)
					.fullname(TEST_FULLNAME)
					.password(TEST_PASSWORD)
					.email(TEST_EMAIL).build();
		emailService.sendPassword(traveller,null);
	}


	@Test
	public void testNotifyNewRegistration(){
		Traveller traveller = new Traveller.Builder()
					.username(TEST_USERNAME)
					.fullname(TEST_FULLNAME)
					.password(TEST_PASSWORD)
					.email(TEST_EMAIL).build();
		traveller.getSecurityDetail().addAuthority(SecurityDetail.AuthorityRole.ROLE_USER);
//		traveller.getSecurityDetail().addAuthority(SecurityDetail.AuthorityRole.ROLE_ADMIN);
//		Assert.assertNotNull(traveller.getSecurityDetail().getAuthorities().contains(SecurityDetail.AuthorityRole.ROLE_USER));
//		Assert.assertNotNull(traveller.getSecurityDetail().getAuthorities().contains(SecurityDetail.AuthorityRole.ROLE_ADMIN));
//		Assert.assertTrue(traveller.getSecurityDetail().hasAuthority(SecurityDetail.AuthorityRole.ROLE_USER));
//		Assert.assertTrue(traveller.getSecurityDetail().hasAuthority(SecurityDetail.AuthorityRole.ROLE_ADMIN));
		if( sendEmail ){
			Mockito.doNothing().when(mailSender).send(Mockito.<MimeMessage>anyObject());
			Mockito.doCallRealMethod().doNothing().when(mailSender).send(Mockito.<MimeMessage>anyObject());
		}
		emailService.notifyNewRegistration(traveller);
	}


	@Test(expected = SnapLogicalException.class)
	public void testNotifyNewRegistrationNoAdmins(){
		Traveller traveller = new Traveller.Builder()
					.username(TEST_USERNAME)
					.fullname(TEST_FULLNAME)
					.password(TEST_PASSWORD)
					.email(TEST_EMAIL).build();		
		traveller.getSecurityDetail().addAuthority(SecurityDetail.AuthorityRole.ROLE_USER);
//		Assert.assertNotNull(traveller.getSecurityDetail().getAuthorities().contains(SecurityDetail.AuthorityRole.ROLE_USER));
//		Assert.assertNotNull(traveller.getSecurityDetail().getAuthorities().contains(SecurityDetail.AuthorityRole.ROLE_ADMIN));
//		Assert.assertTrue(traveller.getSecurityDetail().hasAuthority(SecurityDetail.AuthorityRole.ROLE_USER));
//		Assert.assertTrue(traveller.getSecurityDetail().hasAuthority(SecurityDetail.AuthorityRole.ROLE_ADMIN));
//		realSecurityService.removeAuthority(TEST_USERNAME, SecurityDetail.AuthorityRole.ROLE_ADMIN );
//		traveller = travellerService.findTraveller(defaultTravellerId);
//		Assert.assertNotNull(traveller.getSecurityDetail().getAuthorities().contains(SecurityDetail.AuthorityRole.ROLE_USER));
//		Assert.assertFalse(traveller.getSecurityDetail().getAuthorities().contains(SecurityDetail.AuthorityRole.ROLE_ADMIN));
//		Assert.assertTrue(traveller.getSecurityDetail().hasAuthority(SecurityDetail.AuthorityRole.ROLE_USER));
//		Assert.assertFalse(traveller.getSecurityDetail().hasAuthority(SecurityDetail.AuthorityRole.ROLE_ADMIN));

		Traveller admin = new Traveller.Builder()
					.username(ADMIN_USERNAME)
					.fullname(ADMIN_FULLNAME)
					.password(ADMIN_PASSWORD)
					.email(ADMIN_EMAIL).build();
		admin.getSecurityDetail().addAuthority(SecurityDetail.AuthorityRole.ROLE_USER);
		List<Traveller> noAdmins = new ArrayList<Traveller>();
		travellers.add(admin);
		Mockito.when(travellerRepository.findAllTravellers()).thenReturn(noAdmins).thenReturn(travellers);
		emailService.notifyNewRegistration(traveller);
	}


	@Test(expected = AssertionError.class)
	public void testNotifyNewRegistrationNullTraveller(){
		emailService.notifyNewRegistration(null);
	}


	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}
}
